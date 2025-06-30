package com.tshirtshop.backend.service;


import com.tshirtshop.backend.model.Stock;
import com.tshirtshop.backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock getStockByProduitId(Long produitId) {
        return stockRepository.findByProduitId(produitId);
    }

    public void diminuerStock(Long produitId, int quantite) {
        Stock stock = stockRepository.findByProduitId(produitId);
        if (stock != null && stock.getQuantiteDisponible() >= quantite) {
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() - quantite);
            stockRepository.save(stock);
        } else {
            throw new RuntimeException("Stock insuffisant");
        }
    }

    public void ajouterStock(Long produitId, int quantite) {
        Stock stock = stockRepository.findByProduitId(produitId);
        if (stock != null) {
            stock.setQuantiteDisponible(stock.getQuantiteDisponible() + quantite);
            stockRepository.save(stock);
        }
    }
}
/**StockService contient la logique métier du stock :

 Chercher un stock par ID produit.

 Diminuer la quantité (avec vérification et persistance).

 Augmenter la quantité.

 Tout passe par StockRepository, injecté automatiquement par Spring grâce à @Autowired.
 */