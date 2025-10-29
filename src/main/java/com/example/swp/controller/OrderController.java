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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    @GetMapping("/payment-success")
    public String handlePaymentSuccess(
            @RequestParam("orderCode") String orderCode,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {

        try {
            // Kiểm tra status PayOS trả về (PAID, CANCELLED, ...)
            // Giả sử "PAID" là trạng thái thành công, bạn cần kiểm tra tài liệu PayOS
            if ("PAID".equalsIgnoreCase(status)) {

                // Gọi service để xử lý đơn hàng thành công và xóa giỏ hàng
                orderService.processSuccessfulPayment(orderCode);

                redirectAttributes.addFlashAttribute("successMessage", "Thanh toán đơn hàng thành công!");
                // Chuyển hướng đến trang lịch sử đơn hàng hoặc trang chủ
                return "redirect:/user/orders"; // (Hoặc "redirect:/")

            } else {
                // Nếu thanh toán bị hủy hoặc thất bại
                orderService.processFailedPayment(orderCode);
                redirectAttributes.addFlashAttribute("errorMessage", "Thanh toán thất bại hoặc đã bị hủy.");
                return "redirect:/cart/view"; // Quay lại giỏ hàng
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý thanh toán: " + e.getMessage());
            return "redirect:/cart/view";
        }
    }
}
