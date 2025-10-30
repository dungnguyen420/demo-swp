package com.example.swp.Controller;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.Service.IOrderService;
import com.example.swp.Service.impl.CustomUserDetails;
import com.example.swp.base.BaseAPIController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.payos.type.CheckoutResponseData;

import java.io.IOException;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseAPIController {

    @Autowired
    private IOrderService orderService;


    @PostMapping("/create")
    public String createOrderAndRedirect(

            @AuthenticationPrincipal CustomUserDetails principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        try {

            OrderDTO orderRequest = new OrderDTO();
            orderRequest.setUserId(principal.getUser().getId());


            CheckoutResponseData paymentInfo = orderService.createOrder(orderRequest);


            return "redirect:" + paymentInfo.getCheckoutUrl();


        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo đơn hàng: " + e.getMessage());

            return "redirect:/cart/view";
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }
    @GetMapping("/payment-success")
    public String handlePaymentSuccess(
            @RequestParam("orderCode") String orderCode,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {

        try {

            if ("PAID".equalsIgnoreCase(status)) {

                orderService.processSuccessfulPayment(orderCode);

                redirectAttributes.addFlashAttribute("successMessage", "Thanh toán đơn hàng thành công!");

                return "redirect:/cart/view";

            } else {

                orderService.processFailedPayment(orderCode);
                redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại hoặc đã bị hủy.");
                return "redirect:/cart/view";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý thanh toán: " + e.getMessage());
            return "redirect:/cart/view";
        }
    }
}
