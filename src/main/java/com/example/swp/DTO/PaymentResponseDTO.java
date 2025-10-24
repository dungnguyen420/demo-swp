package com.example.swp.DTO;

import com.example.swp.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String orderCode;
    private double amount;
    private PaymentStatus status;
    private String message;
    private String transactionId;
}
