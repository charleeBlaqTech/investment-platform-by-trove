package com.tradingplatformByTrove.market.dto;

import java.math.BigDecimal;

public record AssetPriceResponse(
        String symbol,
        String assetName,
        BigDecimal currentPrice,
        String currency
) {}
