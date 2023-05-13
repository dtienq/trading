package com.system.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinDTO {
    private String id;
    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double amount;
    private Double vol;
    private Double count;
    private Double bid;
    private Double bidSize;
    private Double ask;
    private Double askSize;
}
