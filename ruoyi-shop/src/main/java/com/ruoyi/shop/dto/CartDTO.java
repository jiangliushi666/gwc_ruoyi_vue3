package com.ruoyi.shop.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDTO(String tableId, List<CartItemDTO> items, BigDecimal total) {
}
