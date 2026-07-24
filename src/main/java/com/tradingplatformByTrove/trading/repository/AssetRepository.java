package com.tradingplatformByTrove.trading.repository;

import com.tradingplatformByTrove.trading.model.Asset;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Storage repository for market assets. Pre-seeded with sample market assets.
 */
@Repository
public class AssetRepository {
    private final Map<String, Asset> marketAssets = new ConcurrentHashMap<>();

    public AssetRepository() {
        // Seed market assets
        marketAssets.put("AAPL", Asset.builder().symbol("AAPL").name("Apple Inc.").price(new BigDecimal("180.00")).build());
        marketAssets.put("TSLA", Asset.builder().symbol("TSLA").name("Tesla Inc.").price(new BigDecimal("240.00")).build());
        marketAssets.put("GOOGL", Asset.builder().symbol("GOOGL").name("Alphabet Inc.").price(new BigDecimal("140.00")).build());
    }

    public Optional<Asset> findBySymbol(String symbol) {
        return Optional.ofNullable(marketAssets.get(symbol.toUpperCase()));
    }

    public Asset save(Asset asset) {
        marketAssets.put(asset.getSymbol().toUpperCase(), asset);
        return asset;
    }
}