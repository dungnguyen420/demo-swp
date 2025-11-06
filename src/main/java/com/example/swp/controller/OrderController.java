package com.example.swp.Controller;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.Service.IOrderService;
import com.example.swp.Service.impl.CustomUserDetails;
import com.example.swp.base.BaseAPIController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.payos.type.CheckoutResponseData;

import java.io.IOException;
import java.time.LocalDate;


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

                return "redirect:/shop";

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
    @GetMapping("/history")
    public String viewOrderHistory(
            @AuthenticationPrincipal CustomUserDetails principal,
            Model model,
            RedirectAttributes redirectAttributes,

            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập.");
            return "redirect:/auth/login";
        }

        try {
            Long userId = principal.getUser().getId();
            int pageSize = 10;
            int pageIndex = page - 1;

            Page<OrderDTO> orderPage = orderService.findOrdersByUserId(
                    userId, keyword, date, pageIndex, pageSize
            );

            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", orderPage.getTotalElements());


            model.addAttribute("keyword", keyword);
            model.addAttribute("date", (date != null) ? date.toString() : "");

            return "orders/order-history";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tải lịch sử: " + e.getMessage());
            return "redirect:/home";
        }
    }
    @GetMapping("/detail/{orderCode}")
    public String viewOrderDetail(@PathVariable String orderCode, Model model,
                                  @AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) return "redirect:/auth/login";

        try {
            OrderDTO order = orderService.findByOrderCode(orderCode);


            if (!order.getUserId().equals(principal.getUser().getId())) {
                model.addAttribute("errorMessage", "Bạn không có quyền xem đơn hàng này.");
                return "orders/order-history";
            }

            model.addAttribute("order", order);
            return "orders/order-detail";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không tìm thấy đơn hàng: " + e.getMessage());
            return "orders/order-history";
        }
    }
}
