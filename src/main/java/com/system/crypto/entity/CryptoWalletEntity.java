package com.system.crypto.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity(name = "CRYPTO_WALLET")
public class CryptoWalletEntity {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "COIN_ID")
    private CoinEntity coin;

    @Column
    private Double quantity;
}
