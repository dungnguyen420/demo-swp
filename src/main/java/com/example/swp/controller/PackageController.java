package com.example.swp.Controller;

import com.example.swp.Entity.PackageEntity;
import com.example.swp.Repository.IPackageRepository;
import com.example.swp.Service.ICartService;
import com.example.swp.Service.impl.CartService;
import com.example.swp.Service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/package")
public class PackageController {

    private final IPackageRepository packageRepository;
    private final ICartService cartService;

    @GetMapping
    public String showPackageShop(
            Model model,
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "dir", defaultValue = "desc") String dir,
            @RequestParam(name = "keyword", required = false) String keyword) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(dir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);


            Specification<PackageEntity> spec = (root, query, criteriaBuilder) -> {
                if (keyword == null || keyword.trim().isEmpty()) {
                    return criteriaBuilder.conjunction();
                }

                String searchKeyword = "%" + keyword.toLowerCase().trim() + "%";


                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchKeyword),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchKeyword)
                );
            };


            Page<PackageEntity> packagePage = packageRepository.findAll(spec, pageable);

            model.addAttribute("packages", packagePage.getContent());
            model.addAttribute("totalPages", packagePage.getTotalPages());
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("dir", dir);
            model.addAttribute("keyword", keyword);

            if (principal != null) {
                model.addAttribute("cartCount", cartService.getCartSummary(principal.getUser().getId()).getTotalItems());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi tải trang: " + e.getMessage());
        }

        return "package/package";
    }
}



