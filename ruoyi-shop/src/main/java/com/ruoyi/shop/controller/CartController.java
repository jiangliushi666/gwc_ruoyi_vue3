package com.ruoyi.shop.controller;

import com.ruoyi.shop.domain.Cart;
import com.ruoyi.shop.dto.AddItemRequest;
import com.ruoyi.shop.dto.CartDTO;
import com.ruoyi.shop.dto.CartItemDTO;
import com.ruoyi.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @MessageMapping("/cart/{tableId}/addItem")
    @SendTo("/topic/cart/{tableId}")
    public CartDTO addItem(@DestinationVariable String tableId, AddItemRequest request) {
        Cart cart = cartService.addItem(tableId, request);
        return toDto(cart);
    }

    @MessageMapping("/cart/{tableId}/removeItem")
    @SendTo("/topic/cart/{tableId}")
    public CartDTO removeItem(@DestinationVariable String tableId, String itemId) {
        Cart cart = cartService.removeItem(tableId, itemId);
        return toDto(cart);
    }

    @MessageMapping("/cart/{tableId}/updateQuantity")
    @SendTo("/topic/cart/{tableId}")
    public CartDTO updateQuantity(@DestinationVariable String tableId, String itemId, int quantity) {
        Cart cart = cartService.updateQuantity(tableId, itemId, quantity);
        return toDto(cart);
    }

    @MessageMapping("/cart/{tableId}/get")
    @SendTo("/topic/cart/{tableId}")
    public CartDTO getCart(@DestinationVariable String tableId) {
        Cart cart = cartService.getCart(tableId);
        return toDto(cart);
    }

    private CartDTO toDto(Cart cart) {
        return new CartDTO(
                cart.getTableId(),
                cart.getItems().values().stream().map(this::toItemDto).collect(Collectors.toList()),
                cart.getTotal()
        );
    }

    private CartItemDTO toItemDto(Cart.CartItem item) {
        return new CartItemDTO(item.getItemId(), item.getItemName(), item.getPrice(), item.getQuantity());
    }
}
