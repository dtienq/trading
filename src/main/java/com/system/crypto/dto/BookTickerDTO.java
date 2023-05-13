package com.system.crypto.dto;

import lombok.Data;

@Data
public class BookTickerDTO {
    private String id;
    private String symbol;
    private String bidPrice;
    private String bidQty;
    private String askPrice;
    private String askQty;
}
