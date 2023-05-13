package com.system.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponseDTO {
    String symbolTrade;
    String symbolTarget;
    Double amountTrade;
    Double amountTarget;
    Double qtyTrade;
    Double qtyTarget;
}
