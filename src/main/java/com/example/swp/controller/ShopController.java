package com.example.swp.Controller;

import com.example.swp.Entity.ProductEntity;
import com.example.swp.Repository.IProductRepository;
import com.example.swp.Service.ICartService;
import com.example.swp.Service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final IProductService productService;
    private final ICartService cartService;

    @GetMapping
    public String shop(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       @RequestParam(defaultValue = "name") String sortBy,
                       @RequestParam(defaultValue = "asc") String dir,
                       @org.springframework.security.core.annotation.AuthenticationPrincipal com.example.swp.Service.impl.CustomUserDetails principal,
                       Model model) {

        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(1, size), 100),
                "asc".equalsIgnoreCase(dir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<ProductEntity> pageData = productService.findAllPaged(keyword, pageable);

        model.addAttribute("products", pageData.getContent());
        model.addAttribute("pageData", pageData);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", pageData.getNumber());
        model.addAttribute("size", pageData.getSize());
        model.addAttribute("totalPages", pageData.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("dir", dir);

        if (principal != null) {
            var summary = cartService.getCart(principal.getUser().getId());
            model.addAttribute("cartCount", summary.getTotalItems());
        } else {
            model.addAttribute("cartCount", 0);
        }

        return "shop/list";
    }
}
