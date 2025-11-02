package com.example.swp.Controller;

import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Service.ICartService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final ICartService cartService;

    private Long getUserId(CustomUserDetails principal) throws Exception {
        if (principal == null) {
            throw new Exception("Vui lòng đăng nhập.");
        }
        return principal.getUser().getId();
    }

    @PostMapping("/add/product")
    public String addProductToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
            @AuthenticationPrincipal CustomUserDetails principal,
            RedirectAttributes redirectAttributes) {

        try {
            Long userId = getUserId(principal);
            cartService.addProductToCart(userId, productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/shop";
    }

    @PostMapping("/add/package")
    public String addPackageToCart(
            @RequestParam("packageId") Long packageId,
            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
            @AuthenticationPrincipal CustomUserDetails principal,
            RedirectAttributes redirectAttributes) {

        try {
            Long userId = getUserId(principal);
            cartService.addPackageToCart(userId, packageId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm gói tập vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/package";
    }

    @PostMapping("/remove")
    public String removeItemFromCart(
            @RequestParam("cartItemId") Long cartItemId,
            @AuthenticationPrincipal CustomUserDetails principal,
            RedirectAttributes redirectAttributes) {

        try {
            Long userId = getUserId(principal);
            cartService.removeItemFromCart(userId, cartItemId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khỏi giỏ hàng.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/cart/view";
    }


    @GetMapping("/view")
    public String viewCart(@AuthenticationPrincipal CustomUserDetails principal, Model model, RedirectAttributes ra) {
        try {
            Long userId = getUserId(principal);

            CartSummaryDTO cartDTO = cartService.getCartSummary(userId);
            model.addAttribute("cart", cartDTO);
            return "cart/view";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());

            if (principal == null) {
                return "redirect:/auth/login";
            }
            return "redirect:/";
        }
    }
}