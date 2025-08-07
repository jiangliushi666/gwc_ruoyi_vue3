package com.ruoyi.shop.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Cart {
    private String tableId;
    private ConcurrentHashMap<String, CartItem> items = new ConcurrentHashMap<>();
    private BigDecimal total = BigDecimal.ZERO;

    @Data
    public static class CartItem {
        private String itemId;
        private String itemName;
        private BigDecimal price;
        private int quantity;
    }
}
