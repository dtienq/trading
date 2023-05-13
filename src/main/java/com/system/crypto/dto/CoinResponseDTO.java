package com.system.crypto.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoinResponseDTO {
    private List<CoinDTO> coins;
}
