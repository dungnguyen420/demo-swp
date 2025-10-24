package com.example.swp.Entity;

import lombok.Data;
import vn.payos.type.ItemData;

import java.util.List;

@Data
public class PaymentData {
    private String orderCode;
    private long amount;
    private String description;
    private String returnUrl;
    private String cancelUrl;
    private List<ItemData> items;
}
