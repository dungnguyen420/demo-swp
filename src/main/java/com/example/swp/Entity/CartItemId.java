package com.example.swp.Entity;

import java.io.Serializable;
import java.util.Objects;

public class CartItemId implements Serializable {
    private Long cart;
    private Long product;

    public CartItemId() {}

    public CartItemId(Long cart, Long product) {
        this.cart = cart;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItemId)) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(cart, that.cart) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, product);
    }
}
