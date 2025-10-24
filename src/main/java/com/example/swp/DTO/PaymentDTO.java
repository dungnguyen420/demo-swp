package com.example.swp.DTO;

import com.example.swp.Enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private String orderCode;
    private Long amount;
    private PaymentStatus status;
    private String transactionId;
    private String redirectUrl;
}
