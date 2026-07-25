package com.tradingplatformByTrove.market.service;

import com.tradingplatformByTrove.market.dto.AssetPriceResponse;
import com.tradingplatformByTrove.market.dto.TradeQuoteResponse;
import com.tradingplatformByTrove.market.model.MarketPrice;
import com.tradingplatformByTrove.market.repository.MarketPriceRepository;
import com.tradingplatformByTrove.portfolio.model.Portfolio;
import com.tradingplatformByTrove.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketPriceRepository marketPriceRepository;
    private final PortfolioRepository portfolioRepository;

    public List<AssetPriceResponse> getAllMarketPrices() {
        return marketPriceRepository.findAll().stream()
                .map(m -> new AssetPriceResponse(m.getSymbol(), m.getAssetName(), m.getCurrentPrice(), m.getCurrency()))
                .toList();
    }

    public AssetPriceResponse getPriceBySymbol(String symbol) {
        MarketPrice marketPrice = marketPriceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new IllegalArgumentException("Asset symbol not found: " + symbol));
        return new AssetPriceResponse(marketPrice.getSymbol(), marketPrice.getAssetName(), marketPrice.getCurrentPrice(), marketPrice.getCurrency());
    }

    public TradeQuoteResponse getTradeQuote(UUID userId, String symbol, String tradeType, int quantity) {
        MarketPrice priceObj = marketPriceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new IllegalArgumentException("Asset symbol not found: " + symbol));

        BigDecimal pricePerUnit = priceObj.getCurrentPrice();
        BigDecimal totalAmount = pricePerUnit.multiply(BigDecimal.valueOf(quantity));

        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found for user: " + userId));

        BigDecimal userCash = portfolio.getCashBalance();
        boolean canAfford = "BUY".equalsIgnoreCase(tradeType) ? userCash.compareTo(totalAmount) >= 0 : true;

        return new TradeQuoteResponse(
                symbol.toUpperCase(),
                tradeType.toUpperCase(),
                quantity,
                pricePerUnit,
                totalAmount,
                userCash,
                canAfford
        );
    }
}
