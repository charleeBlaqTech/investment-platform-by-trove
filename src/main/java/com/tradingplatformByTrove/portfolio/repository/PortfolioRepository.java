package com.tradingplatformByTrove.portfolio.repository;

import com.tradingplatformByTrove.portfolio.model.Portfolio;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Portfolio storage repository.
 */
@Repository
public class PortfolioRepository {
    private final Map<UUID, Portfolio> storage = new ConcurrentHashMap<>();

    public Portfolio save(Portfolio portfolio) {
        if (portfolio.getPortfolioId() == null) {
            portfolio.setPortfolioId(UUID.randomUUID());
        }
        storage.put(portfolio.getPortfolioId(), portfolio);
        return portfolio;
    }

    public Optional<Portfolio> findByUserId(UUID userId) {
        return storage.values().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst();
    }
}