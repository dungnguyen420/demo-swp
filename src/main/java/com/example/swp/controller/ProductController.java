package com.example.swp.Controller;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;
import com.example.swp.Service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createProduct(@ModelAttribute("product") ProductDTO dto, Model model){
        try {
            ProductEntity product = productService.createProduct(dto);
            model.addAttribute("success", "Tạo sản phẩm thành công!");
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
    public String updateProduct(@ModelAttribute("product") ProductDTO dto, Model model){
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

    @GetMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model){
        String result = productService.deleteProduct(id);
        model.addAttribute("mess", result);
        return "redirect:/products/list";
    }


}
