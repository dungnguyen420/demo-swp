package com.example.swp.DTO;


import lombok.Data;

@Data
public class CreatePaymentResult {
    private String checkoutUrl;
    private String qrCode;
    private String orderCode;
    private long amount;
    private String status;
}
