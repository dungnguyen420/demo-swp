package com.example.swp.controller;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Service.IMyScheduleService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Thêm

import java.util.List;

@Controller
@RequestMapping("/my-schedule")
@RequiredArgsConstructor
public class MyScheduleController {

    private final IMyScheduleService myScheduleService;

    /** Trang cho Lịch PT (Cá nhân) - Dành cho MEMBER */
    @GetMapping("/personal")
    @PreAuthorize("hasAuthority('MEMBER')")
    public String showMyPersonalSchedule(@AuthenticationPrincipal CustomUserDetails principal, Model model,
                                         @RequestParam(required = false, defaultValue = "upcoming") String mode) {
        if (principal == null) return "redirect:/login";

        List<ScheduleEntity> schedules = myScheduleService.getPersonalSchedules(principal.getUser().getId(), mode);

        model.addAttribute("schedules", schedules);
        model.addAttribute("pageTitle", "Lịch PT của tôi");
        model.addAttribute("currentUser", principal.getUser());
        model.addAttribute("mode", mode);
        model.addAttribute("baseURL", "/my-schedule/personal"); // <-- SỬA LỖI
        return "schedule/my_schedule_list";
    }

    /** Trang cho Lịch Lớp học - Dành cho MEMBER */
    @GetMapping("/classes")
    @PreAuthorize("hasAuthority('MEMBER')")
    public String showMyClassSchedule(@AuthenticationPrincipal CustomUserDetails principal, Model model,
                                      @RequestParam(required = false, defaultValue = "upcoming") String mode) {
        if (principal == null) return "redirect:/login";

        List<ScheduleEntity> schedules = myScheduleService.getClassSchedules(principal.getUser().getId(), mode);

        model.addAttribute("schedules", schedules);
        model.addAttribute("pageTitle", "Lớp học của tôi");
        model.addAttribute("currentUser", principal.getUser());
        model.addAttribute("mode", mode);
        model.addAttribute("baseURL", "/my-schedule/classes"); // <-- SỬA LỖI
        return "schedule/my_schedule_list";
    }

    /** Trang cho Lịch Dạy - Dành cho TRAINER */
    @GetMapping("/teaching")
    @PreAuthorize("hasAuthority('TRAINER')")
    public String showMyTeachingSchedule(@AuthenticationPrincipal CustomUserDetails principal, Model model,
                                         @RequestParam(required = false, defaultValue = "upcoming") String mode) {
        if (principal == null) return "redirect:/login";

        List<ScheduleEntity> schedules = myScheduleService.getTeachingSchedules(principal.getUser().getId(), mode);

        model.addAttribute("schedules", schedules);
        model.addAttribute("pageTitle", "Lịch dạy của tôi");
        model.addAttribute("currentUser", principal.getUser());
        model.addAttribute("mode", mode);
        model.addAttribute("baseURL", "/my-schedule/teaching"); // <-- SỬA LỖI
        return "schedule/my_schedule_list";
    }
}