package com.ruoyi.shop.dto;

import java.math.BigDecimal;

public record AddItemRequest(String itemId, String itemName, BigDecimal price) {
}
