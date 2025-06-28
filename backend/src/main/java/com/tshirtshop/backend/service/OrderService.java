package com.tshirtshop.backend.service;

import com.tshirtshop.backend.dto.CreateOrderRequest;
import com.tshirtshop.backend.model.*;
import com.tshirtshop.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final StockRepository stockRepo;

    public OrderService(OrderRepository orderRepo,
                        UserRepository userRepo,
                        ProductRepository productRepo,
                        StockRepository stockRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.stockRepo = stockRepo;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest req) {

        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> lignes = new ArrayList<>();
        double total = 0;

        for (CreateOrderRequest.Item it : req.getItems()) {

            Product p = productRepo.findById(it.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product id " + it.getProductId() + " absent"));

            // ➖ décrémenter le stock
            Stock stock = stockRepo.findByProduitId(p.getId());
            if (stock == null || stock.getQuantiteDisponible() < it.getQuantity()) {
                throw new RuntimeException("Stock insuffisant pour " + p.getName());
            }
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() - it.getQuantity());

            OrderItem oi = new OrderItem(
                    null,
                    p,
                    order,
                    it.getQuantity(),
                    p.getPrice()
            );
            lignes.add(oi);

            total += p.getPrice() * it.getQuantity();
        }

        order.setItems(lignes);
        order.setTotal(total);

        return orderRepo.save(order);          // grâce au cascade, OrderItem est enregistré aussi
    }
}
