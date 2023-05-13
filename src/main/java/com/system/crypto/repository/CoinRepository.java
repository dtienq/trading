package com.system.crypto.repository;

import com.system.crypto.entity.CoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<CoinEntity, String> {
    CoinEntity findBySymbol(String symbol);
}
