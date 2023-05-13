package com.system.crypto.repository;

import com.system.crypto.entity.TradeTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TradeTrackingRepository extends JpaRepository<TradeTrackingEntity, String> {
    @Query("from TRADE_TRACKING a where a.user.id = :userId")
    List<TradeTrackingEntity> findByUserId(@Param("userId") String userId);
}
