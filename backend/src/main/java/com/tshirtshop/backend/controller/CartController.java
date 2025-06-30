package com.tshirtshop.backend.controller;

import com.tshirtshop.backend.model.CartItem;
import com.tshirtshop.backend.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService service;

    public CartController(CartService service) { this.service = service; }

    /* -------- CRUD endpoints -------- */

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return service.getCartForUser(userId);
    }

    @PostMapping("/add")
    public CartItem add(@RequestBody CartItem item) {
        return service.add(item);
    }

    @PutMapping("/update")
    public CartItem update(@RequestBody CartItem item) {
        return service.update(item);
    }

    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }

    @DeleteMapping("/clear/{userId}")
    public void clear(@PathVariable Long userId) {
        service.clear(userId);
    }
}
