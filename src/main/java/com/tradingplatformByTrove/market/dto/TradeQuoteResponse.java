package com.tradingplatformByTrove.market.dto;

import java.math.BigDecimal;

public record TradeQuoteResponse(
        String symbol,
        String tradeType,
        int quantity,
        BigDecimal pricePerUnit,
        BigDecimal totalEstimatedAmount,
        BigDecimal currentUserCashBalance,
        boolean canAffordTrade
) {}