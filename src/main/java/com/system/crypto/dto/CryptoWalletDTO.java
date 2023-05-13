package com.system.crypto.dto;

import lombok.Data;

@Data
public class CryptoWalletDTO {
    private CoinDTO coin;
    private Double quantity;
}
