package com.system.crypto.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity(name = "WALLET")
public class WalletEntity {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column
    private Double amount;
}
