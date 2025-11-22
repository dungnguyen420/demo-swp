package com.example.swp.Controller;

import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Entity.CartEntity;
import com.example.swp.Entity.CartItemEntity;
import com.example.swp.Service.ICartService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

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
            redirectAttributes.addFlashAttribute("msg", "Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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
                return "redirect:/index";
            }
            return "redirect:/";
        }
    }
    @PostMapping("/update-item")
    public String updateCartItemQuantity(@RequestParam("itemId") Long itemId,
                                         @RequestParam("quantity") Integer quantity,
                                         @AuthenticationPrincipal CustomUserDetails principal,
                                         RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/index";
        }

        Long userId = principal.getUser().getId();

        try {
            cartService.updateCartItemQuantity(userId, itemId, quantity);
            redirectAttributes.addFlashAttribute("msg", "Cập nhật số lượng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart/view";
    }
    @PostMapping("/update-item-ajax")
    @ResponseBody
    public ResponseEntity<?> updateCartItemAjax(@RequestParam("itemId") Long itemId,
                                                @RequestParam("quantity") int quantity,
                                                @AuthenticationPrincipal CustomUserDetails principal) {
        try {
            Long userId = principal.getUser().getId();


            CartItemEntity updatedItem = cartService.updateCartItemQuantity(userId, itemId, quantity);


            CartEntity cart = updatedItem.getCart();

            double lineTotal = updatedItem.getDisplayPrice() * updatedItem.getQuantity();
            double cartTotal = cart.getTotalPrice();

            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            body.put("itemId", updatedItem.getId());
            body.put("quantity", updatedItem.getQuantity());
            body.put("lineTotal", lineTotal);
            body.put("cartTotal", cartTotal);

            return ResponseEntity.ok(body);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}