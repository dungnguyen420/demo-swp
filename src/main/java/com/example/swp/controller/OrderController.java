package com.example.swp.Controller;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.Service.IOrderService;
import com.example.swp.base.BaseAPIController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;

import java.io.IOException;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseAPIController {

    @Autowired
    private IOrderService orderService;


    @GetMapping("/create")
    public String showCreateOrderPage(Model model) {
        model.addAttribute("order", new OrderDTO());
        return "orders/create";
    }


    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") OrderDTO dto,
                              Model model) throws Exception {
        Long userId = getCurrentUserId();
        dto.setUserId(userId);

        CheckoutResponseData result = orderService.createOrder(dto);

        model.addAttribute("checkoutUrl", result.getCheckoutUrl());
        model.addAttribute("orderCode", result.getOrderCode());

        return "orders/success";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
}
