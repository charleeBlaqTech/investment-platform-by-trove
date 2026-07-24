package com.tradingplatformByTrove.trading.mapper;

import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import com.tradingplatformByTrove.trading.dto.TradeRequest;
import org.springframework.stereotype.Component;

/**
 * Mapping utilities for trade conversions.
 */
@Component
public class TradingMapper {

    public PortfolioAsset toPortfolioAsset(TradeRequest request) {
        return PortfolioAsset.builder()
                .assetSymbol(request.getAssetSymbol().toUpperCase())
                .assetName(request.getAssetSymbol().toUpperCase() + " Asset")
                .quantity(request.getQuantity())
                .averageBuyPrice(request.getPrice())
                .build();
    }
}