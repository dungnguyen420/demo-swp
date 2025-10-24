package com.example.swp.DTO;

import com.example.swp.Enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private String orderCode;
    private OrderStatus status;
    private Double totalPrice;
    private List<OrderItemDTO> items;
    private String cancelUrl;
    private String returnUrl;
    private String paymentUrl;
    private String description;

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
