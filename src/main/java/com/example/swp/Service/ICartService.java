package com.example.swp.Service;

import com.example.swp.DTO.CartRequestDTO;
import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Entity.CartEntity;
import com.example.swp.Entity.CartItemEntity;

import java.util.List;


public interface ICartService {


    void addProductToCart(Long userId, Long productId, int quantity) throws Exception;

    void addPackageToCart(Long userId, Long packageId, int quantity) throws Exception;

    void removeItemFromCart(Long userId, Long cartItemId) throws Exception;

    CartSummaryDTO getCartSummary(Long userId) throws Exception;

}