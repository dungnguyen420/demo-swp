package com.example.swp.Service;

import com.example.swp.DTO.PaymentResponseDTO;

public interface IPaymentService {
    PaymentResponseDTO processPayment(String orderCode, String method);
}
