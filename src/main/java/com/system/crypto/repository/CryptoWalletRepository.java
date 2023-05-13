package com.system.crypto.repository;

import com.system.crypto.entity.CryptoWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CryptoWalletRepository extends JpaRepository<CryptoWalletEntity, String> {
    @Query("from CRYPTO_WALLET a where a.user.id = :userId")
    List<CryptoWalletEntity> findByUserId(@Param("userId") String userId);

    @Query("from CRYPTO_WALLET a where a.user.id = :userId and coin.symbol = :symbol")
    CryptoWalletEntity findByUserIdAndSymbol(@Param("userId") String userId, @Param("symbol") String symbol);
}
