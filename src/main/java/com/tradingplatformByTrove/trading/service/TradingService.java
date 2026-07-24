package com.tradingplatformByTrove.trading.service;

import com.tradingplatformByTrove.common.exception.InsufficientFundsException;
import com.tradingplatformByTrove.common.exception.ResourceNotFoundException;
import com.tradingplatformByTrove.common.model.TradeType;
import com.tradingplatformByTrove.gamification.service.GamificationService;
import com.tradingplatformByTrove.portfolio.model.Portfolio;
import com.tradingplatformByTrove.portfolio.model.PortfolioAsset;
import com.tradingplatformByTrove.portfolio.repository.PortfolioRepository;
import com.tradingplatformByTrove.trading.dto.TradeRequest;
import com.tradingplatformByTrove.trading.mapper.TradingMapper;
import com.tradingplatformByTrove.user.model.User;
import com.tradingplatformByTrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final GamificationService gamificationService;
    private final TradingMapper tradingMapper;

    public synchronized void executeTrade(TradeRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Portfolio portfolio = portfolioRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user: " + user.getUserId()));

        BigDecimal totalCost = request.getPrice().multiply(request.getQuantity());

        if (request.getTradeType() == TradeType.BUY) {
            handleBuyOrder(portfolio, request, totalCost);
        } else if (request.getTradeType() == TradeType.SELL) {
            handleSellOrder(portfolio, request, totalCost);
        }

        portfolioRepository.save(portfolio);

        // Process gamification rewards
        gamificationService.processTradeGamification(user);
    }

    private void handleBuyOrder(Portfolio portfolio, TradeRequest request, BigDecimal totalCost) {
        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new InsufficientFundsException("Insufficient cash balance. Required: "
                    + totalCost + ", Available: " + portfolio.getCashBalance());
        }

        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost));

        String symbol = request.getAssetSymbol().toUpperCase();
        PortfolioAsset asset = portfolio.getHoldings().get(symbol);

        if (asset == null) {
            portfolio.getHoldings().put(symbol, tradingMapper.toPortfolioAsset(request));
        } else {
            asset.setQuantity(asset.getQuantity().add(request.getQuantity()));
            asset.setAverageBuyPrice(request.getPrice());
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