package com.example.swp.Service.impl;

import com.example.swp.DTO.ProductDTO;
import com.example.swp.Entity.ProductEntity;
import com.example.swp.Repository.IProductRepository;
import com.example.swp.Service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            throw new IllegalArgumentException("Tên sản phẩm '" + dto.getName() + "' đã tồn tại!");
        }

        if (dto.getPrice() < 0){
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }

        if(dto.getQuantity() < 0){
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
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
            if(dto.getPrice() < 0){
                throw new RuntimeException("Giá phải lớn hơn 0");
            }

            if(dto.getQuantity() < 0){
                throw new RuntimeException("Số lượng phải lớn hơn 0");
            }

            Optional<ProductEntity> duplicate = productRepository.findByName(dto.getName());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(dto.getId())) {
                throw new RuntimeException("Tên sản phẩm '" + dto.getName() + "' đã tồn tại!");
            }

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
        ProductEntity existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        if (existingProduct.getQuantity() > 0){
            throw new RuntimeException("Không thể xóa sản phẩm khi số lượng còn:" + existingProduct.getQuantity());
        }
            productRepository.delete(existingProduct);

        return "Xóa sản phẩm thành công!";
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

    public List<ProductEntity> searchAdvanced(String keyword,
                                              Double minPrice,
                                              Double maxPrice,
                                              LocalDateTime fromDate,
                                              LocalDateTime toDate) {
        return productRepository.searchAdvanced(keyword, minPrice, maxPrice, fromDate, toDate);
    }

    @Override
    public Page<ProductEntity> findAllPaged(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) return productRepository.findAll(pageable);
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    @Override
    @Transactional(rollbackFor = Exception.class) // Quan trọng: Nếu lỗi, rollback lại
    public void decreaseStock(Long productId, int quantityToDecrease) throws Exception {

        // 1. Kiểm tra số lượng hợp lệ
        if (quantityToDecrease <= 0) {
            throw new Exception("Số lượng cần giảm phải lớn hơn 0");
        }

        // 2. Lấy sản phẩm từ CSDL
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm với ID: " + productId));

        // 3. Lấy số lượng tồn kho hiện tại
        int currentStock = product.getQuantity();

        // 4. KIỂM TRA TỒN KHO
        // Đây là bước kiểm tra an toàn cuối cùng
        if (currentStock < quantityToDecrease) {
            throw new Exception("Không đủ hàng tồn kho cho sản phẩm: '" + product.getName()
                    + "'. Chỉ còn " + currentStock + " sản phẩm.");
        }


        int newStock = currentStock - quantityToDecrease;
        product.setQuantity(newStock);


        productRepository.save(product);
    }

}
