package com.tshirtshop.backend.service;

import com.tshirtshop.backend.dto.CreateOrderRequest;
import com.tshirtshop.backend.model.*;
import com.tshirtshop.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * Création d’une commande (utilisé par le POST /orders)
     */
    @Transactional
    public Order createOrder(CreateOrderRequest req) {

        // 🔍 Récupérer l’utilisateur
        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> lignes = new ArrayList<>();
        double total = 0;

        for (CreateOrderRequest.Item it : req.getItems()) {
            Product p = productRepo.findById(it.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit ID " + it.getProductId() + " introuvable."));

            // ➖ Décrémenter le stock
            Stock stock = stockRepo.findByProduitId(p.getId());
            if (stock == null || stock.getQuantiteDisponible() < it.getQuantity()) {
                throw new RuntimeException("Stock insuffisant pour " + p.getName());
            }
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() - it.getQuantity());

            // 🧾 Créer une ligne de commande
            OrderItem oi = new OrderItem(
                    null,
                    p,
                    order,
                    it.getQuantity(),
                    p.getPrice() // snapshot du prix au moment de la commande
            );
            lignes.add(oi);

            total += p.getPrice() * it.getQuantity();
        }

        order.setItems(lignes);
        order.setTotal(total);

        return orderRepo.save(order); // ⤵️ grâce au cascade, les OrderItem sont enregistrés aussi
    }

    /**
     * Récupère les commandes passées par un utilisateur donné.
     */
    public List<Order> findByUser(User user) {
        return orderRepo.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Cherche un utilisateur par son e‑mail (récupéré depuis le token JWT)
     */
    public User findUserByEmail(String email) {
        Optional<User> optional = userRepo.findByEmail(email);
        return optional.orElse(null);
    }
}
