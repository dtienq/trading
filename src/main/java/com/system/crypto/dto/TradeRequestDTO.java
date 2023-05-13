package com.system.crypto.dto;

import lombok.Data;

@Data
public class TradeRequestDTO {
    Double amountTrade;
    String userId;
    String symbolTrade;
    String symbolTarget;
    Double qtyTrade;
}
