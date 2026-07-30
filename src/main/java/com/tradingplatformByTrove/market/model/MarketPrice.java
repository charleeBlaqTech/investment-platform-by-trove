package com.tradingplatformByTrove.market.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketPrice {

    private String symbol;

    private String assetName;

    private BigDecimal currentPrice;

    private String currency;

    @Builder.Default
    private Instant updatedAt = Instant.now();
}