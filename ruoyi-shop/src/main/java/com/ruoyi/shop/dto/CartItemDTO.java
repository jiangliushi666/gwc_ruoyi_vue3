package com.ruoyi.shop.dto;

import java.math.BigDecimal;

public record CartItemDTO(String itemId, String itemName, BigDecimal price, int quantity) {
}
