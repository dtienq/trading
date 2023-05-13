package com.system.crypto.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.crypto.dto.BaseResponseDTO;
import com.system.crypto.dto.BookTickerDTO;
import com.system.crypto.dto.CoinDTO;
import com.system.crypto.entity.CoinEntity;
import com.system.crypto.repository.CoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataSyncScheduler {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CoinRepository coinRepository;

    @Scheduled(fixedDelay = 10000)
    public void execute() {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        log.info("Sync data from server started!");

        try {
            this.syncBookTickers();
            this.syncTickers();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Sync data from servers encounter error {}", ex.getMessage());
        } finally {
            MDC.clear();
        }

        log.info("Sync data from server completed!");
    }

    private void syncBookTickers() {
        ResponseEntity<List<BookTickerDTO>> response = restTemplate.exchange(
                "https://api.binance.com/api/v3/ticker/bookTicker",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookTickerDTO>>() {
                });

        List<CoinDTO> coinDTOList = response.getBody()
                .stream()
                .map(bookTickerDTO -> CoinDTO.builder()
                        .symbol(bookTickerDTO.getSymbol().toUpperCase())
                        .bid(Double.parseDouble(bookTickerDTO.getBidPrice()))
                        .bidSize(Double.parseDouble(bookTickerDTO.getBidQty()))
                        .ask(Double.parseDouble(bookTickerDTO.getAskPrice()))
                        .askSize(Double.parseDouble(bookTickerDTO.getAskQty()))
                        .build())
                .collect(Collectors.toList());

        Long totalBookTickers = coinRepository.count();

        if (totalBookTickers.equals(0L)) {
            List<CoinEntity> entities = objectMapper.convertValue(
                    coinDTOList,
                    new TypeReference<List<CoinEntity>>() {
                    }
            );
            coinRepository.saveAll(entities);
        } else {
            for (CoinDTO coinDTO : coinDTOList) {
                // only check bidPrice because there are same distance between bid and ask price among symbols
                CoinEntity coinEntity = coinRepository.findBySymbol(coinDTO.getSymbol());
                Double bidPrice = coinDTO.getBid();

                if (Objects.nonNull(coinEntity)) {
                    if (coinEntity.getBid().compareTo(bidPrice) < 0) {
                        coinDTO.setId(coinEntity.getId());
                        coinEntity = objectMapper.convertValue(coinDTO, CoinEntity.class);
                        coinRepository.save(coinEntity);
                    }
                } else {
                    coinEntity = objectMapper.convertValue(coinDTO, new TypeReference<CoinEntity>() {
                    });
                    coinRepository.save(coinEntity);
                }
            }
        }
    }

    private void syncTickers() {
        ResponseEntity<BaseResponseDTO<List<CoinDTO>>> responseEntity = restTemplate.exchange(
                "https://api.huobi.pro/market/tickers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponseDTO<List<CoinDTO>>>() {
                });
        BaseResponseDTO<List<CoinDTO>> response = responseEntity.getBody();

        if (!response.getStatus().equalsIgnoreCase("ok")) {
            log.error("Call API get tickers return status not ok!");
            return;
        }

        Long totalCoins = coinRepository.count();
        List<CoinDTO> data = response.getData();
        data.forEach(x -> {
            x.setSymbol(x.getSymbol().toUpperCase());
        });

        if (totalCoins.equals(0L)) {
            List<CoinEntity> entities = objectMapper.convertValue(
                    response.getData(),
                    new TypeReference<List<CoinEntity>>() {
                    }
            );
            coinRepository.saveAll(entities);
        } else {
            for (CoinDTO coinDTO : response.getData()) {
                // only check bidPrice because there are same distance between bid and ask price among symbols
                CoinEntity coinEntity = coinRepository.findBySymbol(coinDTO.getSymbol());
                Double bid = coinDTO.getBid();

                if (Objects.nonNull(coinEntity)) {
                    if (coinEntity.getBid().compareTo(bid) < 0) {
                        coinDTO.setId(coinEntity.getId());
                        coinEntity = objectMapper.convertValue(coinDTO, new TypeReference<CoinEntity>() {
                        });
                        coinRepository.save(coinEntity);
                    }
                } else {
                    coinEntity = objectMapper.convertValue(coinDTO, new TypeReference<CoinEntity>() {
                    });
                    coinRepository.save(coinEntity);
                }
            }
        }
    }
}
