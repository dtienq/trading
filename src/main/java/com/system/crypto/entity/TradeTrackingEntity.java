package com.system.crypto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@Entity(name = "TRADE_TRACKING")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeTrackingEntity {
    @Id
    @UuidGenerator
    String id;
    @Column
    String symbolTrade;
    @Column
    String symbolTarget;
    @Column
    Double qtyTrade;
    @Column
    Double qtyTarget;
    @Column
    Double amountTrade;
    @Column
    Double amountTarget;
    @Column
    Date createdDate;
    @Column
    Date updatedDate;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;
}
