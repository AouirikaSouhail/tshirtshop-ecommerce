package com.tshirtshop.backend.repository;

import com.tshirtshop.backend.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProduitId(Long produitId);
}

