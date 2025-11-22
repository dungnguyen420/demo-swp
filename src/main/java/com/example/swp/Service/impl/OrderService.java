package com.example.swp.Service.impl;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.DTO.OrderItemDTO;
import com.example.swp.Entity.CartEntity;
import com.example.swp.Entity.CartItemEntity;
import com.example.swp.Entity.OrderEntity;
import com.example.swp.Entity.OrderItemEntity;
import com.example.swp.Entity.PaymentEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.OrderStatus;
import com.example.swp.Repository.CartItemRepository;
import com.example.swp.Repository.CartRepository;
import com.example.swp.Repository.OrderItemRepository;
import com.example.swp.Repository.OrderRepository;
import com.example.swp.Repository.PaymentRepository;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IOrderService;
import com.example.swp.Service.IProductService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final PayOS payos;
    private final CartRepository cartRepository;
    private final IUserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final IProductService productService;

    private Long getCurrentUserId() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new Exception("User not authenticated");
        }
        String username = authentication.getName();
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new Exception("User not found for username: " + username));
        return user.getId();
    }

    @Override
    @Transactional
    public CreatePaymentLinkResponse createOrder(OrderDTO dto) throws Exception {
        Long userId = dto.getUserId() != null ? dto.getUserId() : getCurrentUserId();

        List<CartItemEntity> cartItems = cartItemRepository.findByCart_UserId(userId);
        if (cartItems.isEmpty()) {
            throw new Exception("Giỏ hàng trống!");
        }

        long totalPrice = Math.round(
                cartItems.stream()
                        .mapToDouble(item -> {
                            Double unitPrice = item.getUnitPrice() != null
                                    ? item.getUnitPrice()
                                    : item.getDisplayPrice();
                            return unitPrice * item.getQuantity();
                        })
                        .sum()
        );

        long amount = totalPrice;
        String returnUrl = "http://localhost:8080/orders/payment-success";
        String cancelUrl = "http://localhost:8080/cart/view";
        Long orderCode = System.currentTimeMillis() / 1000;

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toán DH" + orderCode)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .build();

        CreatePaymentLinkResponse response = payos.paymentRequests().create(paymentData);

        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(response.getAmount());
        payment.setDescription(response.getDescription());
        payment.setOrderCode(String.valueOf(response.getOrderCode()));
        payment.setStatus(OrderStatus.PENDING);
        payment.setCheckoutUrl(response.getCheckoutUrl());
        payment.setQrCode(response.getQrCode());
        payment.setUser(cartItems.get(0).getCart().getUser());
        paymentRepository.save(payment);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setPaymentEntity(payment);
        orderEntity.setUserEntity(cartItems.get(0).getCart().getUser());
        orderEntity.setOrderCode(String.valueOf(response.getOrderCode()));
        orderEntity.setStatus(OrderStatus.PENDING);

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        for (CartItemEntity item : cartItems) {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setOrderEntity(savedOrder);
            orderItemEntity.setQuantity(item.getQuantity());

            Double unitPrice = item.getUnitPrice() != null
                    ? item.getUnitPrice()
                    : item.getDisplayPrice();

            orderItemEntity.setUnitPrice(unitPrice);
            orderItemEntity.setProductPrice(unitPrice);

            if (item.getProduct() != null) {
                orderItemEntity.setProductId(item.getProduct().getId());
                orderItemEntity.setPackageId(null);
                orderItemEntity.setProductName(item.getProduct().getName());
            } else if (item.getPackageEntity() != null) {
                orderItemEntity.setPackageId(item.getPackageEntity().getId());
                orderItemEntity.setProductId(null);
                orderItemEntity.setProductName(item.getPackageEntity().getName());
            }

            orderItemRepository.save(orderItemEntity);
        }

        return response;
    }

    @Override
    @Transactional
    public void processSuccessfulPayment(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với mã: " + orderCode));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        List<OrderItemEntity> orderItems = order.getOrderItems();
        for (OrderItemEntity item : orderItems) {
            if (item.getProductId() != null) {
                productService.decreaseStock(item.getProductId(), item.getQuantity());
            }
        }

        orderRepository.save(order);
        PaymentEntity payment = order.getPaymentEntity();
        if (payment != null) {
            payment.setStatus(OrderStatus.PAID);
            paymentRepository.save(payment);
        }

        UserEntity user = order.getUserEntity();
        if (user == null) {
            throw new Exception("Đơn hàng không liên kết với người dùng nào.");
        }

        List<CartItemEntity> cartItems = cartItemRepository.findByCart_UserId(user.getId());
        if (cartItems != null && !cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            CartEntity cart = cartItems.get(0).getCart();
            if (cart != null) {
                cart.setTotalPrice(0.0);
                cartRepository.save(cart);
            }
        }
    }

    @Override
    @Transactional
    public void processFailedPayment(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với mã: " + orderCode));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            PaymentEntity payment = order.getPaymentEntity();
            if (payment != null) {
                payment.setStatus(OrderStatus.CANCELLED);
                paymentRepository.save(payment);
            }
        }
    }

    @Override
    @Transactional(Transactional.TxType.NEVER)
    public Page<OrderDTO> findOrdersByUserId(Long userId, String keyword, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<OrderEntity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("userEntity").get("id"), userId));

            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("orderCode"), "%" + keyword.trim() + "%"));
            }

            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("createdAt"), startOfDay, endOfDay));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<OrderEntity> orderPageEntity = orderRepository.findAll(spec, pageable);

        return orderPageEntity.map(this::convertToDto);
    }

    @Transactional(Transactional.TxType.NEVER)
    @Override
    public OrderDTO findByOrderCode(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Không tìm thấy đơn hàng với mã: " + orderCode));
        return convertToDto(order);
    }

    @Override
    public Page<OrderEntity> findMyOrders(UserEntity user, String orderCode, LocalDate searchDate, String status, Pageable pageable) {
        Specification<OrderEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("userEntity"), user));

            if (orderCode != null && !orderCode.trim().isEmpty()) {
                predicates.add(cb.like(root.get("orderCode"), "%" + orderCode.trim() + "%"));
            }

            if (searchDate != null) {
                LocalDateTime startOfDay = searchDate.atStartOfDay();
                LocalDateTime endOfDay = searchDate.atTime(LocalTime.MAX);
                predicates.add(cb.between(root.get("createdAt"), startOfDay, endOfDay));
            }

            if (status != null && !status.trim().isBlank()) {
                try {
                    OrderStatus statusEnum = OrderStatus.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException e) {
                    System.err.println("Trạng thái tìm kiếm không hợp lệ: " + status);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepository.findAll(spec, pageable);
    }

    private OrderDTO convertToDto(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        OrderDTO dto = new OrderDTO();

        dto.setId(orderEntity.getId());

        if (orderEntity.getUserEntity() != null) {
            dto.setUserId(orderEntity.getUserEntity().getId());
        }

        dto.setOrderCode(orderEntity.getOrderCode());
        dto.setStatus(orderEntity.getStatus());

        if (orderEntity.getTotalPrice() != null) {
            dto.setTotalPrice(orderEntity.getTotalPrice().doubleValue());
        }

        dto.setCreatedAt(orderEntity.getCreatedAt());

        if (orderEntity.getOrderItems() != null) {
            List<OrderItemDTO> itemDTOs = orderEntity.getOrderItems().stream()
                    .map(this::convertItemToDto)
                    .collect(java.util.stream.Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    private OrderItemDTO convertItemToDto(OrderItemEntity itemEntity) {
        if (itemEntity == null) return null;
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setPackageId(itemEntity.getProductId());
        itemDTO.setPackageId(itemEntity.getPackageId());
        itemDTO.setProductName(itemEntity.getProductName());
        itemDTO.setQuantity(itemEntity.getQuantity());
        Double price = itemEntity.getUnitPrice() != null
                ? itemEntity.getUnitPrice()
                : itemEntity.getProductPrice();
        itemDTO.setPrice(price);
        return itemDTO;
    }
}
