package com.example.swp.Service.impl;

import com.example.swp.DTO.*;

import com.example.swp.Entity.*;
import com.example.swp.Enums.OrderStatus;
import com.example.swp.Repository.*;
import com.example.swp.Service.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class OrderService implements IOrderService {
    @Autowired
    private PayOS payos;

    private final CartRepository cartRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    public OrderService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    private Long getCurrentUserId() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new Exception("User not authenticated");
        }

        String username = authentication.getName(); // lấy username đang login
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new Exception("User not found for username: " + username));

        return user.getId();
    }

    @Override
    public CheckoutResponseData createOrder(OrderDTO dto) throws Exception {

        Long userId = dto.getUserId() != null ? dto.getUserId() : getCurrentUserId();


        CartEntity existingCart = cartRepository.findByUserId(userId);
        if (existingCart == null) {
            throw new Exception("Cart not found for user id: " + userId);
        }
        List<CartItemEntity> cartItems = cartItemRepository.findByCart_UserId(userId);
        if (cartItems.isEmpty()) {
            throw new Exception("Cart is empty for user id: " + userId);
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

       int amount = (int) totalPrice;

        String returnUrl = "http://localhost:8080/orders/payment-success";
        String cancelUrl = "http://localhost:8080/cart/view";
        Long orderCode = System.currentTimeMillis() / 1000;

        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toán giỏ hàng " + existingCart.getId())
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();

        CheckoutResponseData result = payos.createPaymentLink(paymentData);


        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(result.getAmount());
        payment.setDescription(result.getDescription());
        payment.setOrderCode(String.valueOf(result.getOrderCode()));
        payment.setStatus(result.getStatus());
        payment.setCheckoutUrl(result.getCheckoutUrl());
        payment.setQrCode(result.getQrCode());
        payment.setUser(existingCart.getUser());

        paymentRepository.save(payment);


        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTitle("Đơn hàng cho giỏ hàng " + existingCart.getId());
        orderEntity.setTotalPrice(existingCart.getTotalPrice());
        orderEntity.setPaymentEntity(payment);
        orderEntity.setUserEntity(existingCart.getUser());
        orderEntity.setOrderCode(String.valueOf(result.getOrderCode()));
        orderEntity.setStatus(OrderStatus.PENDING);
        orderRepository.save(orderEntity);


        List<CartItemEntity> cartsItems = cartItemRepository.findByCart_UserId(userId);


        for (CartItemEntity item : cartsItems) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductId(item.getProduct().getId());
            orderItemEntity.setProductName(item.getProduct().getName());
            orderItemEntity.setProductPrice(item.getProduct().getPrice());
            orderItemEntity.setOrderEntity(orderEntity);
            orderItemRepository.save(orderItemEntity);
        }

        return result;
    }
    @Override
    public void processSuccessfulPayment(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với mã: " + orderCode));


        if (order.getStatus() == OrderStatus.PENDING) {
            //  Cập nhật trạng thái Đơn hàng
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        }
            //  Cập nhật trạng thái Payment
            PaymentEntity payment = order.getPaymentEntity();
            if (payment != null) {
                payment.setStatus("PAID");
                paymentRepository.save(payment);
            }

            UserEntity user = order.getUserEntity();
            if (user == null) {
                throw new Exception("Đơn hàng không liên kết với người dùng nào.");
            }

            //  XÓA giỏ hàng của User này
            CartEntity cart = cartRepository.findByUserId(user.getId());
            if (cart != null) {
                List<CartItemEntity> cartItems = cartItemRepository.findByCart_UserId(user.getId());
                cartItemRepository.deleteAll(cartItems);
                cart.setTotalPrice(0);
                cartRepository.save(cart);
            }
        }



    @Override
    public void processFailedPayment(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với mã: " + orderCode));

        // Chỉ xử lý nếu đơn hàng đang chờ
        if (order.getStatus() == OrderStatus.PENDING) {
            // 1. Cập nhật trạng thái Đơn hàng
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            // 2. Cập nhật trạng thái Payment
            PaymentEntity payment = order.getPaymentEntity();
            if (payment != null) {
                payment.setStatus("CANCELLED");
                paymentRepository.save(payment);
            }
        }
    }


}