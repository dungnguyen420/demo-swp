package com.example.swp.Service;

import com.example.swp.DTO.CartDTO;
import com.example.swp.DTO.CartItemDTO;
import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.DTO.OrderDTO;
import com.example.swp.Enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import vn.payos.type.CheckoutResponseData;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService {
    CheckoutResponseData createOrder(OrderDTO dto) throws Exception;
    void processSuccessfulPayment(String orderCode) throws Exception;
    void processFailedPayment(String orderCode) throws Exception;


    Page<OrderDTO> findOrdersByUserId(Long userId, String keyword, LocalDate date, int page, int size);
    OrderDTO findByOrderCode(String orderCode) throws Exception;

}
