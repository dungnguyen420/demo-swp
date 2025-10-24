package com.example.swp.Service.impl;

import com.example.swp.DTO.CartDTO;
import com.example.swp.DTO.CartItemDTO;
import com.example.swp.DTO.OrderDTO;
import com.example.swp.DTO.OrderItemDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.PaymentData;
import vn.payos.type.ItemData;
import vn.payos.type.CheckoutResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private PayOS payOSClient; // Tiêm Bean từ AppConfig

    @Value("${app.base-url}")
    private String baseUrl;

    public CheckoutResponseData createPaymentLink(OrderDTO order) throws Exception {

        final long orderIdForPayOS = order.getId();
        final int amount = order.getTotalPrice().intValue();
        final String description = "Thanh toan " + order.getOrderCode();

        final String returnUrl = baseUrl + "/orders/payment/return";
        final String cancelUrl = baseUrl + "/cart"; // Quay về giỏ hàng nếu hủy

        // Sửa lỗi: Lấy item thật từ order
        List<ItemData> items = new ArrayList<>();
        for (OrderItemDTO orderItem : order.getItems()) {
            ItemData itemData = ItemData.builder()
                    .name(orderItem.getProductName())
                    .quantity(orderItem.getQuantity())
                    // Sửa lỗi (image_cfd43f.png): Ép kiểu double sang int
                    .price((int) orderItem.getPrice())
                    .build();
            items.add(itemData);
        }

        // Sửa lỗi (image_2213c0.png): Dùng cú pháp builder()
        PaymentData data = PaymentData.builder()
                .orderCode(orderIdForPayOS)
                .amount(amount)
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .items(items)
                .build();

        // Gọi API thật của PayOS
        return payOSClient.createPaymentLink(data);
    }

    // (Bạn có thể thêm hàm verifyWebhook ở đây)
}
