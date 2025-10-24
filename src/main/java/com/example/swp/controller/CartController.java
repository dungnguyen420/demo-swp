package com.example.swp.controller;

import com.example.swp.DTO.CartRequestDTO;
import com.example.swp.Service.ICartService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    @PostMapping("/add")
    public String add(@ModelAttribute CartRequestDTO req,
                      @RequestParam(required=false) String keyword,
                      @RequestParam(defaultValue="0") int page,
                      @RequestParam(defaultValue="12") int size,
                      @RequestParam(defaultValue="name") String sortBy,
                      @RequestParam(defaultValue="asc") String dir,
                      @AuthenticationPrincipal CustomUserDetails principal,
                      RedirectAttributes ra) {
        if (principal == null) return "redirect:/auth/login";
        try {
            cartService.addItem(principal.getUser().getId(), req);
            ra.addFlashAttribute("msg", "Đã thêm vào giỏ");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        ra.addAttribute("keyword", keyword);
        ra.addAttribute("page", page);
        ra.addAttribute("size", size);
        ra.addAttribute("sortBy", sortBy);
        ra.addAttribute("dir", dir);
        return "redirect:/shop";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute CartRequestDTO req,
                         @AuthenticationPrincipal CustomUserDetails principal,
                         RedirectAttributes ra) {
        if (principal == null) return "redirect:/login";
        try {
            cartService.updateItem(principal.getUser().getId(), req);
            ra.addFlashAttribute("msg", "Đã cập nhật số lượng");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart/view";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId,
                         @AuthenticationPrincipal CustomUserDetails principal,
                         RedirectAttributes ra) {
        if (principal == null) return "redirect:/login";
        cartService.removeItem(principal.getUser().getId(), productId);
        ra.addFlashAttribute("msg", "Đã xóa khỏi giỏ");
        return "redirect:/cart/view";
    }

    @PostMapping("/clear")
    public String clear(@AuthenticationPrincipal CustomUserDetails principal,
                        RedirectAttributes ra) {
        if (principal == null) return "redirect:/login";
        cartService.clear(principal.getUser().getId());
        ra.addFlashAttribute("msg", "Đã xóa toàn bộ giỏ");
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String view(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        if (principal == null) return "redirect:/login";
        var summary = cartService.getCart(principal.getUser().getId());
        model.addAttribute("cart", summary);
        return "cart/view";
    }
}