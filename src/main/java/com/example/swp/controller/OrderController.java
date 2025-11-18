package com.example.swp.Controller;

import com.example.swp.DTO.OrderDTO;
import com.example.swp.Entity.OrderEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.OrderStatus;
import com.example.swp.Service.IOrderService;
import com.example.swp.Service.impl.CustomUserDetails;
import com.example.swp.Service.impl.UserService;
import com.example.swp.base.BaseAPIController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import org.springframework.data.domain.Pageable;


import java.util.Collections;
import java.io.IOException;
import java.time.LocalDate;


@Controller
@RequestMapping("/orders")
public class OrderController extends BaseAPIController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private UserService userService;

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


            CreatePaymentLinkResponse paymentInfo = orderService.createOrder(orderRequest);


            return "redirect:" + paymentInfo.getCheckoutUrl();


        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo đơn hàng: " + e.getMessage());

            return "redirect:/cart/view";
        }
    }
//    @PostMapping("/webhook-payos")
//    public String handleWebhook(@RequestBody String rawJson){
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
//            Webhook webhook = objectMapper.readValue(rawJson, Webhook.class);
//            WebhookData data = payOS.verifyPaymentWebhookData(webhook);
//            WebhookData data = webhook.getData();
//            return success(data.getCode());
//        }
//        catch (Exception e){
//            return badRequest(e.getMessage());
//        }
//    }
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
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xử lý thanh toán: " + e.getMessage());
            return "redirect:/cart/view";
        }
    }
    @GetMapping("/history")
    public String showHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(name = "code", required = false) String orderCode,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "1") int pageNo) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").descending());

        try {
            UserEntity currentUser = userService.findByUserNameOptional(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<OrderEntity> ordersPage = orderService.findMyOrders(
                    currentUser, orderCode, searchDate, status, pageable);

            model.addAttribute("ordersPage", ordersPage);
            model.addAttribute("orders", ordersPage.getContent());

            model.addAttribute("totalPages", ordersPage.getTotalPages());

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể tải lịch sử đơn hàng: " + e.getMessage());

            model.addAttribute("ordersPage", Page.empty(pageable));
            model.addAttribute("orders", Collections.emptyList());

            model.addAttribute("totalPages", 0);

        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("orderCode", orderCode);
        model.addAttribute("searchDate", searchDate);
        model.addAttribute("status", status);

        return "orders/order-history";
    }
    @GetMapping("/detail/{orderCode}")
    public String viewOrderDetail(@PathVariable String orderCode,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails principal,
                                  RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        try {
            OrderDTO order = orderService.findByOrderCode(orderCode);

            if (!order.getUserId().equals(principal.getUser().getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xem đơn hàng này.");
                return "redirect:/orders/history";
            }

            model.addAttribute("order", order);
            return "orders/order-detail";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng: " + e.getMessage());
            return "redirect:/orders/history";
        }
    }

}
