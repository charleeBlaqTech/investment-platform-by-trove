package com.tradingplatformByTrove.trading.service;

import com.tradingplatformByTrove.common.exception.InsufficientFundsException;
import com.tradingplatformByTrove.common.exception.ResourceNotFoundException;
import com.tradingplatformByTrove.common.model.TradeType;
import com.tradingplatformByTrove.gamification.service.GamificationService;
import com.tradingplatformByTrove.market.repository.MarketPriceRepository;
import com.tradingplatformByTrove.portfolio.model.Portfolio;
import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import com.tradingplatformByTrove.portfolio.repository.PortfolioRepository;
import com.tradingplatformByTrove.trading.dto.TradeRequest;
import com.tradingplatformByTrove.trading.mapper.TradingMapper;
import com.tradingplatformByTrove.user.model.User;
import com.tradingplatformByTrove.user.repository.UserRepository;
import com.tradingplatformByTrove.market.model.MarketPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final GamificationService gamificationService;
    private final TradingMapper tradingMapper;
    private final MarketPriceRepository marketPriceRepository;

    public synchronized void executeTrade(TradeRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Portfolio portfolio = portfolioRepository
                .findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Portfolio not found"));

        MarketPrice marketPrice = marketPriceRepository
                .findBySymbol(request.getAssetSymbol().toUpperCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Asset not found"));

        BigDecimal currentPrice =
                marketPrice.getCurrentPrice();

        BigDecimal totalAmount =
                currentPrice.multiply(request.getQuantity());

        if (request.getTradeType() == TradeType.BUY) {

            handleBuyOrder(
                    portfolio,
                    request,
                    currentPrice,
                    totalAmount);

        } else {

            handleSellOrder(
                    portfolio,
                    request,
                    totalAmount);
        }

        portfolioRepository.save(portfolio);

        gamificationService.processTradeGamification(user);
    }

    private void handleBuyOrder(
            Portfolio portfolio,
            TradeRequest request,
            BigDecimal currentPrice,
            BigDecimal totalCost) {

        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient cash balance.");
        }

        portfolio.setCashBalance(
                portfolio.getCashBalance().subtract(totalCost));

        String symbol = request.getAssetSymbol().toUpperCase();

        PortfolioAsset asset = portfolio.getHoldings().get(symbol);

        if (asset == null) {

            asset = PortfolioAsset.builder()
                    .assetSymbol(symbol)
                    .assetName(symbol)
                    .quantity(request.getQuantity())
                    .averageBuyPrice(currentPrice)
                    .build();

            portfolio.getHoldings().put(symbol, asset);

        } else {

            BigDecimal totalShares =
                    asset.getQuantity().add(request.getQuantity());

            BigDecimal totalInvestment =
                    asset.getAverageBuyPrice()
                            .multiply(asset.getQuantity())
                            .add(currentPrice.multiply(request.getQuantity()));

            asset.setAverageBuyPrice(
                    totalInvestment.divide(
                            totalShares,
                            4,
                            RoundingMode.HALF_UP));

            asset.setQuantity(totalShares);
        }
    }
    private void handleSellOrder(Portfolio portfolio, TradeRequest request, BigDecimal totalRevenue) {
        String symbol = request.getAssetSymbol().toUpperCase();
        PortfolioAsset asset = portfolio.getHoldings().get(symbol);

        if (asset == null || asset.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new InsufficientFundsException("Insufficient asset quantity available to execute SELL order.");
        }

        portfolio.setCashBalance(portfolio.getCashBalance().add(totalRevenue));

        BigDecimal remainingQuantity = asset.getQuantity().subtract(request.getQuantity());
        if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
            portfolio.getHoldings().remove(symbol);
        } else {
            asset.setQuantity(remainingQuantity);
        }
    }
}