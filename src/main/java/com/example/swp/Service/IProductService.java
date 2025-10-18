package com.example.swp.Service;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductEntity createProduct(ProductDTO dto);
    ProductEntity updateProduct(ProductDTO dto);
    List<ProductEntity> getListProduct();
    String deleteProduct(Long id);
    ProductEntity findById(Long id);
    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> searchProduct(String keyword);
}
