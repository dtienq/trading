package com.system.crypto.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.crypto.dto.*;
import com.system.crypto.entity.*;
import com.system.crypto.repository.*;
import com.system.crypto.service.CoinService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {
    private final CoinRepository coinRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final TradeTrackingRepository tradeTrackingRepository;

    private final ObjectMapper objectMapper;

    @Override
    public CoinResponseDTO getBestPrices() {
        CoinResponseDTO result = new CoinResponseDTO();
        List<CoinEntity> coinEntities = coinRepository.findAll();

        result.setCoins(objectMapper.convertValue(coinEntities, new TypeReference<List<CoinDTO>>() {
        }));

        return result;
    }

    @Override
    @Transactional
    public TradeResponseDTO tradeCoin(TradeRequestDTO requestDTO) {
        Double qtyTarget = null;
        // trade usd to coin
        if(requestDTO.getSymbolTrade().equals("USDT")) {
            qtyTarget = this.usd2Coin(requestDTO.getAmountTrade(), requestDTO.getSymbolTarget());
            this.subtractMoney(requestDTO.getUserId(), requestDTO.getAmountTrade());
            this.addCoin(requestDTO.getUserId(), requestDTO.getSymbolTarget(), qtyTarget);
        } else {
            Double usdTrade = this.coin2USD(requestDTO.getSymbolTrade(), requestDTO.getQtyTrade());
            qtyTarget = this.usd2Coin(usdTrade, requestDTO.getSymbolTarget());
            this.subtractCoin(requestDTO.getUserId(), requestDTO.getSymbolTrade(), requestDTO.getQtyTrade());
            this.addCoin(requestDTO.getUserId(), requestDTO.getSymbolTarget(), qtyTarget);
        }

        UserEntity user = new UserEntity();
        user.setId(requestDTO.getUserId());
        this.trackingTrade(TradeTrackingEntity.builder()
                .symbolTrade(requestDTO.getSymbolTrade())
                .symbolTarget(requestDTO.getSymbolTarget())
                .qtyTrade(requestDTO.getQtyTrade())
                .qtyTarget(qtyTarget)
                .amountTrade(requestDTO.getAmountTrade())
                .createdDate(new Date())
                .updatedDate(new Date())
                .user(user)
                .build());

        return TradeResponseDTO.builder()
                .symbolTrade(requestDTO.getSymbolTrade())
                .symbolTarget(requestDTO.getSymbolTarget())
                .amountTrade(requestDTO.getAmountTrade())
                .amountTarget(null)
                .qtyTrade(requestDTO.getQtyTrade())
                .qtyTarget(qtyTarget)
                .build();
    }

    private void trackingTrade(TradeTrackingEntity entity) {
        tradeTrackingRepository.save(entity);
    }

    private void subtractCoin(String userId, String symbol, Double quantity) {
        CryptoWalletEntity entity = cryptoWalletRepository.findByUserIdAndSymbol(userId, symbol);

        if(Objects.isNull(entity)) {
            CoinEntity coin = coinRepository.findBySymbol(symbol);
            UserEntity user = userRepository.findById(userId).get();
            entity = new CryptoWalletEntity();
            entity.setCoin(coin);
            entity.setUser(user);
            entity.setQuantity(quantity);
        } else {
            quantity = entity.getQuantity() - quantity;

            if(quantity < 0) {
                throw new RuntimeException("User do not have enough coin to trade!");
            }

            entity.setQuantity(quantity);
        }

        cryptoWalletRepository.save(entity);
    }

    private void addCoin(String userId, String symbol, Double quantity) {
        CryptoWalletEntity entity = cryptoWalletRepository.findByUserIdAndSymbol(userId, symbol);

        if(Objects.isNull(entity)) {
            CoinEntity coin = coinRepository.findBySymbol(symbol);
            UserEntity user = userRepository.findById(userId).get();
            entity = new CryptoWalletEntity();
            entity.setCoin(coin);
            entity.setUser(user);
            entity.setQuantity(quantity);
        } else {
            quantity = entity.getQuantity() + quantity;
            entity.setQuantity(quantity);
        }

        cryptoWalletRepository.save(entity);
    }

    private void subtractMoney(String userId, Double amountTrade) {
        WalletEntity walletEntity = walletRepository.findByUserId(userId);

        if(Objects.isNull(walletEntity)) {
            throw new RuntimeException("User do not have wallet");
        }

        Double amount = walletEntity.getAmount() - amountTrade;

        if(amount < 0) {
            throw new RuntimeException("Not enough money");
        }

        walletEntity.setAmount(amount);
        walletRepository.save(walletEntity);
    }

    @Override
    public List<CryptoWalletDTO> getCryptoWallet(String userId) {
        List<CryptoWalletEntity> entities = cryptoWalletRepository.findByUserId(userId);

        return objectMapper.convertValue(entities, new TypeReference<List<CryptoWalletDTO>>() {
        });
    }

    @Override
    public List<TradeTrackingDTO> getHistory(String userId) {
        List<TradeTrackingEntity> entities = tradeTrackingRepository.findByUserId(userId);

        return objectMapper.convertValue(entities, new TypeReference<List<TradeTrackingDTO>>() {
        });
    }

    private Double coin2USD(String symbolTrade, Double qtyTrade) {
        CoinEntity coinEntity = coinRepository.findBySymbol(symbolTrade);

        if(Objects.nonNull(coinEntity)) {
            return qtyTrade * (coinEntity.getBid() / coinEntity.getBidSize());
        }

        return null;
    }

    // return quantity of coins
    private Double usd2Coin(Double amountTrade, String symbolTarget) {
        CoinEntity coinEntity = coinRepository.findBySymbol(symbolTarget);

        if (Objects.nonNull(coinEntity)) {
            return (amountTrade * coinEntity.getAskSize()) / coinEntity.getAsk();
        }

        return null;
    }
}
