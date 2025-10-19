package com.example.swp.Service;

import com.example.swp.DTO.CartRequestDTO;
import com.example.swp.DTO.CartSummaryDTO;
import com.example.swp.Entity.CartEntity;
import com.example.swp.Entity.CartItemEntity;

import java.util.List;


    public interface ICartService {

        CartSummaryDTO getCart(Long userId);

        CartSummaryDTO addItem(Long userId, CartRequestDTO req);

        CartSummaryDTO updateItem(Long userId, CartRequestDTO req);

        CartSummaryDTO removeItem(Long userId, Long productId);

        void clear(Long userId);
    }

