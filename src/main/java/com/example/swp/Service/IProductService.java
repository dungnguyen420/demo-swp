package com.example.swp.Service;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductEntity createProduct(ProductDTO dto);
    ProductEntity updateProduct(ProductDTO dto);
    List<ProductEntity> getListProduct();
    String deleteProduct(Long id);
    ProductEntity findById(Long id);
    List<ProductEntity> searchProduct(String keyword);
    Page<ProductEntity> findAllPaged(String keyword, Pageable pageable);
    Optional<ProductEntity> findByName(String name);

    List<ProductEntity> searchAdvanced(String keyword, Double minPrice, Double maxPrice, LocalDateTime fromDate, LocalDateTime toDate);
}
