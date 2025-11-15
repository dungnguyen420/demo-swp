package com.example.swp.Service.impl;

import com.example.swp.DTO.CartItemDTO;
import com.example.swp.DTO.CartRequestDTO;
import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Entity.*;
import com.example.swp.Repository.*;
import com.example.swp.Service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {


    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepository;
    private final IProductRepository productRepo;
    private final IPackageRepository packageRepo;
    private final IUserRepository userRepo;


    @Override
    @Transactional
    public void addProductToCart(Long userId, Long productId, int quantity) throws Exception {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");

        CartEntity cart = findOrCreateCart(userId);
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new Exception("Sản phẩm không tồn tại"));

        int availableStock = product.getQuantity();


        Optional<CartItemEntity> existingItemOpt = cartItemRepository.findByCart_UserIdAndProduct_Id(userId, productId);

        CartItemEntity itemToSave;
        int newTotalQuantityInCart;

        if (existingItemOpt.isPresent()) {

            itemToSave = existingItemOpt.get();

            newTotalQuantityInCart = itemToSave.getQuantity() + quantity;

        } else {

            itemToSave = new CartItemEntity();
            itemToSave.setCart(cart);
            itemToSave.setProduct(product);
            itemToSave.setPackageEntity(null);
            itemToSave.setUnitPrice(product.getPrice());
            newTotalQuantityInCart = quantity;
        }

        if (newTotalQuantityInCart > availableStock) {
            throw new Exception("Số lượng yêu cầu (" + newTotalQuantityInCart
                    + ") vượt quá số lượng tồn kho hiện có (" + availableStock + ")");
        }


        itemToSave.setQuantity(newTotalQuantityInCart);
        cartItemRepository.save(itemToSave);

        updateCartTotalPrice(cart);
    }


    @Override
    @Transactional
    public void addPackageToCart(Long userId, Long packageId, int quantity) throws Exception {
        if (quantity <= 0) throw new Exception("Số lượng phải lớn hơn 0");

        CartEntity cart = findOrCreateCart(userId);
        PackageEntity pkg = packageRepo.findById(packageId)
                .orElseThrow(() -> new Exception("Gói tập không tồn tại với ID: " + packageId));

        CartItemEntity item = cartItemRepository.findByCart_UserIdAndPackageEntity_Id(userId, packageId)
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    return existingItem;
                })
                .orElseGet(() -> {
                    CartItemEntity newItem = new CartItemEntity();
                    newItem.setCart(cart);
                    newItem.setPackageEntity(pkg);
                    newItem.setProduct(null);
                    newItem.setQuantity(quantity);
                    newItem.setUnitPrice(pkg.getPrice());
                    return newItem;
                });

        cartItemRepository.save(item);
        updateCartTotalPrice(cart);
    }


    @Override
    @Transactional
    public void removeItemFromCart(Long userId, Long cartItemId) throws Exception {
        CartItemEntity itemToRemove = cartItemRepository.findByIdAndCart_UserId(cartItemId, userId)
                .orElseThrow(() -> new Exception("Mục không có trong giỏ hàng"));

        CartEntity cart = itemToRemove.getCart();
        cartItemRepository.delete(itemToRemove);
        updateCartTotalPrice(cart);
    }


    @Override
    public CartSummaryDTO getCartSummary(Long userId) throws Exception {
        CartEntity cart = findOrCreateCart(userId);
        List<CartItemEntity> items = cartItemRepository.findByCart_UserId(userId);

        CartSummaryDTO summaryDTO = new CartSummaryDTO();
        summaryDTO.setCartId(cart.getId());
        summaryDTO.setUserId(userId);

        List<CartItemDTO> itemDTOs = items.stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setCartItemId(item.getId());
            dto.setQuantity(item.getQuantity());

            dto.setUnitPrice(item.getDisplayPrice());
            dto.setItemName(item.getDisplayName());
            dto.setLineTotal(item.getDisplayPrice() * item.getQuantity());

            if (item.getProduct() != null) {
                dto.setProductId(item.getProduct().getId());
                dto.setImage(item.getProduct().getImage());
                dto.setEntityType("PRODUCT");

            } else if (item.getPackageEntity() != null) {
                dto.setPackageId(item.getPackageEntity().getId());
                dto.setEntityType("PACKAGE");
            }
            return dto;
        }).collect(Collectors.toList());

        summaryDTO.setItems(itemDTOs);
        summaryDTO.setTotalItems(itemDTOs.stream().mapToInt(CartItemDTO::getQuantity).sum());
        summaryDTO.setTotalAmount(cart.getTotalPrice());

        return summaryDTO;
    }


    private CartEntity findOrCreateCart(Long userId) throws Exception {
        CartEntity cart = cartRepo.findByUser_Id(userId).orElse(null);
        if (cart == null) {
            UserEntity user = userRepo.findById(userId)
                    .orElseThrow(() -> new Exception("User not found"));
            cart = new CartEntity();
            cart.setUser(user);
            return cartRepo.save(cart);
        }
        return cart;
    }

    private void updateCartTotalPrice(CartEntity cart) {
        List<CartItemEntity> items = cartItemRepository.findByCart_UserId(cart.getUser().getId());

        double total = items.stream()
                .mapToDouble(item -> item.getDisplayPrice() * item.getQuantity())
                .sum();

        cart.setTotalPrice(total);
        cartRepo.save(cart);
    }
}