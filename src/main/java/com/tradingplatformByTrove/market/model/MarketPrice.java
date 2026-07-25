package com.tradingplatformByTrove.market.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "market_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketPrice {

    @Id
    @Column(name = "symbol", length = 20)
    private String symbol;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "current_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal currentPrice;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
