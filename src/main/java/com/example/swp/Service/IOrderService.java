package com.example.swp.Service;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.Entity.OrderEntity;
import com.example.swp.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;


import java.time.LocalDate;


public interface IOrderService {
    CreatePaymentLinkResponse createOrder(OrderDTO dto) throws Exception;
    void processSuccessfulPayment(String orderCode) throws Exception;
    void processFailedPayment(String orderCode) throws Exception;


    Page<OrderDTO> findOrdersByUserId(Long userId, String keyword, LocalDate date, int page, int size);
    OrderDTO findByOrderCode(String orderCode) throws Exception;
    Page<OrderEntity> findMyOrders(
            UserEntity user,
            String orderCode,
            LocalDate searchDate,
            String status,
            Pageable pageable
    );
}
