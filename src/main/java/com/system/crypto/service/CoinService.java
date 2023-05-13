package com.system.crypto.service;

import com.system.crypto.dto.*;

import java.util.List;

public interface CoinService {
    CoinResponseDTO getBestPrices();

    TradeResponseDTO tradeCoin(TradeRequestDTO requestDTO);

    List<CryptoWalletDTO> getCryptoWallet(String userId);

    List<TradeTrackingDTO> getHistory(String userId);
}
