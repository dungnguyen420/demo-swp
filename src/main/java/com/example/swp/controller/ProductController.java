package com.example.swp.controller;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;
import com.example.swp.Service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/list")
    public String getListProduct(@RequestParam(value = "keyword", required = false, defaultValue = "")String keyword, Model model){
        List<ProductEntity> listProduct;

        if (keyword.isEmpty() ){
            listProduct = productService.getListProduct();
        }else {
            listProduct = productService.searchProduct(keyword);
        }
        model.addAttribute("listProduct", listProduct);
        model.addAttribute("keyword", keyword);
        return "products/product-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("product", new ProductDTO());
        return "products/product-create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid  @ModelAttribute("product") ProductDTO dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model){

        if (bindingResult.hasErrors()){
            return "products/product-create";
        }
        try {
            ProductEntity product = productService.createProduct(dto);
            redirectAttributes.addFlashAttribute("success", "Tạo sản phẩm thành công!");
            return "redirect:/products/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", dto);
            return "products/product-create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditFrom(@PathVariable("id") Long id, Model model){
        ProductEntity existingProduct = productService.findById(id);

        if(existingProduct == null){
            model.addAttribute("error", "Not find product");
            return "redirect:/products/list";
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(existingProduct.getId());
        dto.setName(existingProduct.getName());
        dto.setDescription(existingProduct.getDescription());
        dto.setPrice(existingProduct.getPrice());
        dto.setQuantity(existingProduct.getQuantity());
        dto.setImage(existingProduct.getImage());

        model.addAttribute("product", dto);
        return "products/product-edit";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDTO dto,
                                BindingResult bindingResult,
                                Model model){

        if (bindingResult.hasErrors()) {
            return "products/product-edit";
        }

        try {
            ProductEntity updated = productService.updateProduct(dto);
            model.addAttribute("success", "Cập nhật sản phẩm thành công!");
            return "redirect:/products/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", dto);
            return "products/product-edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            String result = productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("mess", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/list";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Model model) {

        List<ProductEntity> products = productService.searchAdvanced(keyword, minPrice, maxPrice, fromDate, toDate);
        model.addAttribute("listProduct", products);

        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "products/product-list";
    }

}
