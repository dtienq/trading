package com.system.crypto.controller;

import com.system.crypto.dto.*;
import com.system.crypto.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class TradingController {
    @Autowired
    private CoinService coinService;

    @GetMapping("/coins/get-best-price")
    public ResponseEntity<CoinResponseDTO> GetBestAggregatedPrice() {
        CoinResponseDTO responseDTO = null;

        try {
            responseDTO = coinService.getBestPrices();
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/coins/trade")
    public ResponseEntity<TradeResponseDTO> tradeCoin(
            @RequestBody TradeRequestDTO requestDTO
    ) {
        TradeResponseDTO responseDTO = null;

        try {
            responseDTO = coinService.tradeCoin(requestDTO);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/coins/crypto-wallet/query")
    public ResponseEntity<List<CryptoWalletDTO>> getCurrentCrypto(
            @RequestBody CryptoQueryRequestDTO requestDTO
    ) {
        List<CryptoWalletDTO> responseDTO = null;

        try {
            responseDTO = coinService.getCryptoWallet(requestDTO.getUserId());
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/coins/trade/history")
    public ResponseEntity<List<TradeTrackingDTO>> getTradeHistory(
            @RequestBody CryptoQueryRequestDTO requestDTO
    ) {
        List<TradeTrackingDTO> responseDTO = null;

        try {
            responseDTO = coinService.getHistory(requestDTO.getUserId());
        } catch(Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
