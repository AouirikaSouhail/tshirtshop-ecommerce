package com.tshirtshop.backend.service;

import com.tshirtshop.backend.model.CartItem;
import com.tshirtshop.backend.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository repo;

    public CartService(CartItemRepository repo) {
        this.repo = repo;
    }

    public List<CartItem> getCartForUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public CartItem add(CartItem item) {
        return repo.save(item);
    }

    public CartItem update(CartItem item) {
        return repo.save(item);
    }

    public void remove(Long id) {
        repo.deleteById(id);
    }

    public void clear(Long userId) {
        repo.deleteByUserId(userId);
    }
}
