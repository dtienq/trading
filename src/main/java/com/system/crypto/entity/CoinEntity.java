package com.system.crypto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity(name = "COIN")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(unique = true)
    private String symbol;
    @Column
    private Double open;
    @Column
    private Double high;
    @Column
    private Double low;
    @Column
    private Double close;
    @Column
    private Double amount;
    @Column
    private Double vol;
    @Column
    private Double count;
    @Column
    private Double bid;
    @Column
    private Double bidSize;
    @Column
    private Double ask;
    @Column
    private Double askSize;
}
