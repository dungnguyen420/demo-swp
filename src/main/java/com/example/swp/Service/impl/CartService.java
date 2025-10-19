package com.example.swp.Service.impl;

import com.example.swp.DTO.CartItemDTO;
import com.example.swp.DTO.CartRequestDTO;
import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Entity.CartEntity;
import com.example.swp.Entity.CartItemEntity;
import com.example.swp.Entity.ProductEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Repository.CartItemRepository;
import com.example.swp.Repository.CartRepository;
import com.example.swp.Repository.IProductRepository;
import com.example.swp.Service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final IProductRepository productRepo;

    private CartEntity ensureCart(Long userId) {
        return cartRepo.findByUser_Id(userId).orElseGet(() -> {
            UserEntity u = new UserEntity();
            u.setId(userId);
            return cartRepo.save(new CartEntity(u));
        });
    }

    private CartSummaryDTO toSummary(Long userId) {
        List<CartItemEntity> items = itemRepo.findByCart_UserId(userId);
        List<CartItemDTO> list = new ArrayList<>();
        int totalItems = 0;
        double totalAmount = 0.0;

        for (CartItemEntity it : items) {
            double line = it.getProduct().getPrice() * it.getQuantity();
            totalItems += it.getQuantity();
            totalAmount += line;

            CartItemDTO d = new CartItemDTO();
            d.setProductId(it.getProduct().getId());
            d.setProductName(it.getProduct().getName());
            d.setUnitPrice(it.getProduct().getPrice());
            d.setQuantity(it.getQuantity());
            d.setLineTotal(line);
            d.setImage(it.getProduct().getImage());
            list.add(d);
        }

        CartSummaryDTO s = new CartSummaryDTO();
        s.setItems(list);
        s.setTotalItems(totalItems);
        s.setTotalAmount(totalAmount);
        return s;
    }

    @Override
    @Transactional
    public CartSummaryDTO addItem(Long userId, CartRequestDTO req) {
        if (req.getProductId() == null) throw new RuntimeException("Thiếu productId");

        int qty = (req.getQuantity() == null || req.getQuantity() <= 0) ? 1 : req.getQuantity();

        ProductEntity p = productRepo.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        CartEntity cart = ensureCart(userId);

        CartItemEntity item = itemRepo.findByCart_UserIdAndProduct_Id(userId, req.getProductId())
                .orElseGet(() -> {
                    CartItemEntity ci = new CartItemEntity();
                    ci.setCart(cart);
                    ci.setProduct(p);
                    ci.setQuantity(0);
                    return ci;
                });

        int newQty = item.getQuantity() + qty;
        if (newQty > p.getQuantity()) throw new RuntimeException("Vượt quá tồn kho");

        item.setQuantity(newQty);
        itemRepo.save(item);

        return toSummary(userId);
    }

    @Override
    @Transactional
    public CartSummaryDTO updateItem(Long userId, CartRequestDTO req) {
        if (req.getProductId() == null) throw new RuntimeException("Thiếu productId");
        int qty = (req.getQuantity() == null) ? 1 : req.getQuantity();

        CartItemEntity item = itemRepo.findByCart_UserIdAndProduct_Id(userId, req.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ"));

        if (qty <= 0) {
            itemRepo.delete(item);
            return toSummary(userId);
        }

        if (qty > item.getProduct().getQuantity()) throw new RuntimeException("Vượt quá tồn kho");

        item.setQuantity(qty);
        itemRepo.save(item);

        return toSummary(userId);
    }

    @Override
    @Transactional
    public CartSummaryDTO removeItem(Long userId, Long productId) {
        itemRepo.findByCart_UserIdAndProduct_Id(userId, productId)
                .ifPresent(itemRepo::delete);
        return toSummary(userId);
    }

    @Override
    @Transactional
    public void clear(Long userId) {
        List<CartItemEntity> items = itemRepo.findByCart_UserId(userId);
        itemRepo.deleteAll(items);
    }

    @Override
    @Transactional
    public CartSummaryDTO getCart(Long userId) {
        // Không tạo cart mới nếu chưa có; chỉ trả về rỗng
        return toSummary(userId);
    }
}
