package com.tradingplatformByTrove.portfolio.mapper;

import com.tradingplatformByTrove.portfolio.dto.PortfolioResponse;
import com.tradingplatformByTrove.portfolio.model.Portfolio;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Converts Portfolio Entities to client DTO responses.
 */
@Component
public class PortfolioMapper {

    public PortfolioResponse toResponse(Portfolio portfolio, BigDecimal calculatedTotalValue) {
        return PortfolioResponse.builder()
                .portfolioId(portfolio.getPortfolioId())
                .userId(portfolio.getUserId())
                .cashBalance(portfolio.getCashBalance())
                .holdings(portfolio.getHoldings())
                .totalPortfolioValue(calculatedTotalValue)
                .build();
    }
}
