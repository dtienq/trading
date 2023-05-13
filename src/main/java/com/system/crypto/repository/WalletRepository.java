package com.system.crypto.repository;

import com.system.crypto.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletRepository extends JpaRepository<WalletEntity, String> {
    @Query("from WALLET a where a.user.id = :userId")
    WalletEntity findByUserId(@Param("userId") String userId);
}
