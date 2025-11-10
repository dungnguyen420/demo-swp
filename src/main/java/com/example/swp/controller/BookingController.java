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


    @GetMapping("/create")
    public String showBookingForm(@RequestParam("trainerId") Long trainerId, Model model) {

        UserEntity trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Trainer"));

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setSlots(new ArrayList<>(List.of(new SlotRequest())));

        model.addAttribute("trainer", trainer);
        model.addAttribute("bookingDTO", dto);
        model.addAttribute("slotNumbers", List.of(1, 2, 3, 4, 5, 6));

        return "booking/create";
    }


    @PostMapping("/create")
    public String processBooking(@RequestParam("trainerId") Long trainerId,
                                 @ModelAttribute("bookingDTO") BookingRequestDTO bookingDTO,
                                 @AuthenticationPrincipal CustomUserDetails principal,
                                 RedirectAttributes ra) {

        if (principal == null) {
        }
        Long memberId = principal.getUser().getId();
        try {
            scheduleService.bookPrivateSessions(trainerId, memberId, bookingDTO.getSlots());
            ra.addFlashAttribute("message", "Đặt lịch thành công " + bookingDTO.getSlots().size() + " buổi tập!");
            return "redirect:/trainers/detail/" + trainerId;
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", "Đặt lịch thất bại: " + ex.getMessage());
            return "redirect:/booking/create?trainerId=" + trainerId;
        }
    }
}