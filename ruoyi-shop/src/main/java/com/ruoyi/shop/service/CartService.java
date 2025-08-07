package com.ruoyi.shop.service;

import com.ruoyi.shop.domain.Cart;
import com.ruoyi.shop.dto.AddItemRequest;
import com.ruoyi.shop.repository.InMemoryCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor // 给final自动加上autowired
public class CartService {

    private final InMemoryCartRepository cartRepository;

    public Cart addItem(String tableId, AddItemRequest request) {
        Cart cart = cartRepository.getOrCreateCart(tableId);
        Cart.CartItem item = cart.getItems().compute(request.itemId(), (k, v) -> { // 根据指定的键和函数 对键的值进行修改
            if (v == null) {
                v = new Cart.CartItem();
                v.setItemId(request.itemId());
                v.setItemName(request.itemName());
                v.setPrice(request.price());
                v.setQuantity(1);
            } else {
                v.setQuantity(v.getQuantity() + 1);
            }
            return v;
        });
        recalculateTotal(cart);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeItem(String tableId, String itemId) {
        Cart cart = cartRepository.getOrCreateCart(tableId);
        cart.getItems().remove(itemId);
        recalculateTotal(cart);
        cartRepository.save(cart);
        return cart;
    }

    public Cart updateQuantity(String tableId, String itemId, int quantity) {
        Cart cart = cartRepository.getOrCreateCart(tableId);
        if (quantity <= 0) {
            cart.getItems().remove(itemId);
        } else {
            Cart.CartItem item = cart.getItems().get(itemId);
            if (item != null) {
                item.setQuantity(quantity);
            }
        }
        recalculateTotal(cart);
        cartRepository.save(cart);
        return cart;
    }

    public Cart getCart(String tableId) {
        return cartRepository.getOrCreateCart(tableId);
    }

    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().values().stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
    }
}
