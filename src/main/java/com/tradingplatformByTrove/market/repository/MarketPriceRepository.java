package com.tradingplatformByTrove.market.repository;

import com.tradingplatformByTrove.market.model.MarketPrice;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MarketPriceRepository {

    private final Map<String, MarketPrice> storage = new ConcurrentHashMap<>();

    public MarketPriceRepository() {
        // Default seed data for in-memory execution / testing
        seed("AAPL", "Apple Inc.", new BigDecimal("185.50"));
        seed("TSLA", "Tesla Inc.", new BigDecimal("240.20"));
        seed("NVDA", "NVIDIA Corporation", new BigDecimal("125.75"));
        seed("BTC",  "Bitcoin", new BigDecimal("65000.00"));
        seed("ETH",  "Ethereum", new BigDecimal("3400.00"));
    }

    private void seed(String symbol, String name, BigDecimal price) {
        storage.put(symbol, MarketPrice.builder()
                .symbol(symbol)
                .assetName(name)
                .currentPrice(price)
                .currency("USD")
                .updatedAt(Instant.now())
                .build());
    }

    public Optional<MarketPrice> findBySymbol(String symbol) {
        return Optional.ofNullable(storage.get(symbol.toUpperCase()));
    }

    public List<MarketPrice> findAll() {
        return new ArrayList<>(storage.values());
    }

    public MarketPrice save(MarketPrice marketPrice) {
        marketPrice.setSymbol(marketPrice.getSymbol().toUpperCase());
        marketPrice.setUpdatedAt(Instant.now());
        storage.put(marketPrice.getSymbol(), marketPrice);
        return marketPrice;
    }
}
