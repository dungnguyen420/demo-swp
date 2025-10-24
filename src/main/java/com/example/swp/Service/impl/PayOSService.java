package com.example.swp.service;

import com.example.swp.DTO.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayOSService {

    private final PayOS payOS; // SDK đã được cấu hình trong PayOSConfig

    public CheckoutResponseData createPaymentLink(CartDTO cart) throws Exception {
        // Tạo mã đơn hàng
        long orderCode = System.currentTimeMillis(); // PayOS yêu cầu long

        // Chuyển items sang ItemData
        List<ItemData> items = cart.getItems().stream()
                .map(item -> ItemData.builder()
                        .name(item.getProductName())
                        .quantity(item.getQuantity())
                        .price((int) item.getUnitPrice())
                        .build())
                .collect(Collectors.toList());

        // Tổng tiền
        int amount = (int) Math.round(cart.getTotalPrice());

        // Tạo dữ liệu thanh toán
        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("Thanh toán đơn hàng #" + orderCode)
                .returnUrl("http://localhost:8080/payment/success")
                .cancelUrl("http://localhost:8080/payment/cancel")
                .items(items)
                .build();

        // Gọi API thật của PayOS
        CheckoutResponseData response = payOS.createPaymentLink(paymentData);

        return response;
    }
}
