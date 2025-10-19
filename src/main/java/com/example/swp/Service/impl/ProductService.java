package com.example.swp.Service.impl;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;
import com.example.swp.Repository.IProductRepository;
import com.example.swp.Service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public ProductEntity createProduct(ProductDTO dto) {
        Optional<ProductEntity> existingProduct = productRepository.findByName(dto.getName());
        if (existingProduct.isPresent()) {
            throw new RuntimeException("Tên sản phẩm '" + dto.getName() + "' đã tồn tại!");
        }

        ProductEntity newProduct = new ProductEntity();

        newProduct.setName(dto.getName());
        newProduct.setDescription(dto.getDescription());
        newProduct.setPrice(dto.getPrice());
        newProduct.setQuantity(dto.getQuantity());
        newProduct.setImage(dto.getImage());

        return productRepository.save(newProduct);
    }

    @Override
    public ProductEntity updateProduct(ProductDTO dto) {
        ProductEntity existingProduct = productRepository.findById(dto.getId()).get();
        if(existingProduct != null){
            existingProduct.setName(dto.getName());
            existingProduct.setDescription(dto.getDescription());
            existingProduct.setPrice(dto.getPrice());
            existingProduct.setQuantity(dto.getQuantity());
            existingProduct.setImage(dto.getImage());

            productRepository.save(existingProduct);
            return existingProduct;
        }
        return null;
    }

    @Override
    public List<ProductEntity> getListProduct() {
        return productRepository.findAll();
    }

    @Override
    public String deleteProduct(Long id) {
        ProductEntity existingProduct = productRepository.findById(id).get();

        if (existingProduct != null){
            productRepository.deleteById(id);
        }
        return "Delete product successfully";
    }

    @Override
    public ProductEntity findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<ProductEntity> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<ProductEntity> searchProduct(String keyword) {
        if (keyword == null || keyword.isEmpty()){
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }


    @Override
    public Page<ProductEntity> findAllPaged(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) return productRepository.findAll(pageable);
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

}
