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
    private PayOS payOSClient;

    @Value("${app.base-url}")
    private String baseUrl;

    public CheckoutResponseData createPaymentLink(OrderDTO order) throws Exception {

        final long orderIdForPayOS = order.getId();
        final int amount = order.getTotalPrice().intValue();
        final String description = "Thanh toan " + order.getOrderCode();

        final String returnUrl = baseUrl + "/orders/payment/return";
        final String cancelUrl = baseUrl + "/cart";


        List<ItemData> items = new ArrayList<>();
        for (OrderItemDTO orderItem : order.getItems()) {
            ItemData itemData = ItemData.builder()
                    .name(orderItem.getProductName())
                    .quantity(orderItem.getQuantity())

                    .price((int) orderItem.getPrice())
                    .build();
            items.add(itemData);
        }


        PaymentData data = PaymentData.builder()
                .orderCode(orderIdForPayOS)
                .amount(amount)
                .description(description)
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl)
                .items(items)
                .build();


        return payOSClient.createPaymentLink(data);
    }


}
