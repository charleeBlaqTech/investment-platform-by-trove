package com.tradingplatformByTrove.portfolio.service;

import com.tradingplatformByTrove.common.exception.ResourceNotFoundException;
import com.tradingplatformByTrove.portfolio.dto.AddAssetRequest;
import com.tradingplatformByTrove.portfolio.dto.PortfolioResponse;
import com.tradingplatformByTrove.portfolio.mapper.PortfolioMapper;
import com.tradingplatformByTrove.portfolio.model.Portfolio;
import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import com.tradingplatformByTrove.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    public Portfolio createPortfolioForUser(UUID userId) {
        Portfolio portfolio = Portfolio.builder()
                .userId(userId)
                .build();
        return portfolioRepository.save(portfolio);
    }

    public PortfolioResponse getPortfolioByUserId(UUID userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user: " + userId));

        BigDecimal totalValuation = calculateTotalValue(portfolio);
        return portfolioMapper.toResponse(portfolio, totalValuation);
    }

    public PortfolioResponse addAssetToPortfolio(UUID userId, AddAssetRequest request) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user: " + userId));

        PortfolioAsset asset = portfolio.getHoldings().get(request.getAssetSymbol());
        if (asset != null) {
            asset.setQuantity(asset.getQuantity().add(request.getQuantity()));
        } else {
            portfolio.getHoldings().put(request.getAssetSymbol(), PortfolioAsset.builder()
                    .assetSymbol(request.getAssetSymbol())
                    .assetName(request.getAssetName())
                    .quantity(request.getQuantity())
                    .averageBuyPrice(request.getPrice())
                    .build());
        }

        Portfolio updated = portfolioRepository.save(portfolio);
        return portfolioMapper.toResponse(updated, calculateTotalValue(updated));
    }

    public PortfolioResponse removeAssetFromPortfolio(UUID userId, String assetSymbol) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user: " + userId));

        if (!portfolio.getHoldings().containsKey(assetSymbol)) {
            throw new ResourceNotFoundException("Asset not found in portfolio: " + assetSymbol);
        }

        portfolio.getHoldings().remove(assetSymbol);
        Portfolio updated = portfolioRepository.save(portfolio);
        return portfolioMapper.toResponse(updated, calculateTotalValue(updated));
    }

    private BigDecimal calculateTotalValue(Portfolio portfolio) {
        BigDecimal assetsSum = portfolio.getHoldings().values().stream()
                .map(holding -> holding.getQuantity().multiply(holding.getAverageBuyPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return portfolio.getCashBalance().add(assetsSum);
    }
}