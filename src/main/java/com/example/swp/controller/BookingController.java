package com.example.swp.controller;

import com.example.swp.DTO.BookingRequestDTO;
import com.example.swp.DTO.SlotRequest;
import com.example.swp.Entity.TrainerSlotPriceEntity; // <- THÊM MỚI
import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IScheduleService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal; // <- THÊM MỚI
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // <- THÊM MỚI
import java.util.stream.Collectors; // <- THÊM MỚI

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final IScheduleService scheduleService;
    private final IUserRepository userRepository;
    private static final BigDecimal PRIVATE_SESSION_PRICE = new BigDecimal("250000");

    /**
     * HIỂN THỊ TRANG ĐẶT LỊCH (CẬP NHẬT)
     */
    @GetMapping("/create")
    public String showBookingForm(@RequestParam("trainerId") Long trainerId, Model model) {

        UserEntity trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Trainer"));

        // Tạo DTO mới và khởi tạo 1 slot rỗng (giống hệt createClass)
        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setSlots(new ArrayList<>(List.of(new SlotRequest())));

        model.addAttribute("trainer", trainer);
        model.addAttribute("bookingDTO", dto); // <- Đổi DTO
        model.addAttribute("slotNumbers", List.of(1, 2, 3, 4, 5, 6));

        return "booking/create";
    }

    /**
     * XỬ LÝ VIỆC ĐẶT LỊCH (CẬP NHẬT)
     */
    @PostMapping("/create")
    public String processBooking(@RequestParam("trainerId") Long trainerId,
                                 @ModelAttribute("bookingDTO") BookingRequestDTO bookingDTO, // <- Đổi DTO
                                 @AuthenticationPrincipal CustomUserDetails principal,
                                 RedirectAttributes ra) {

        if (principal == null) {
            // ... (xử lý chưa đăng nhập)
        }

        Long memberId = principal.getUser().getId();

        try {
            // Gọi service mới (số nhiều)
            scheduleService.bookPrivateSessions(trainerId, memberId, bookingDTO.getSlots());

            ra.addFlashAttribute("message", "Đặt lịch thành công " + bookingDTO.getSlots().size() + " buổi tập!");
            return "redirect:/trainers/detail/" + trainerId;
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", "Đặt lịch thất bại: " + ex.getMessage());
            return "redirect:/booking/create?trainerId=" + trainerId;
        }
    }
}