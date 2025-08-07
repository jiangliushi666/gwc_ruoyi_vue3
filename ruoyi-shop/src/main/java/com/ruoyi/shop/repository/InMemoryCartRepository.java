package com.ruoyi.shop.repository;

import com.ruoyi.shop.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// 数据访问层
@Repository
public class InMemoryCartRepository {

    // 这个map存的是什么？用这种方式比原始代码有什么好处？
    private final ConcurrentHashMap<String, Cart> carts = new ConcurrentHashMap<>();

    public Cart getOrCreateCart(String tableId) {
        return carts.computeIfAbsent(tableId, id -> { // java8新增map接口方法，返回键对应的值，不存在则执行函数
            Cart cart = new Cart();
            cart.setTableId(id);
            return cart;
        });
    }

    public void save(Cart cart) {
        carts.put(cart.getTableId(), cart);
    }

    public Optional<Cart> findById(String tableId) {
        return Optional.ofNullable(carts.get(tableId));
    }
}
