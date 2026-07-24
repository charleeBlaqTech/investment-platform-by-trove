package com.tradingplatformByTrove.trading.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Financial Instrument asset model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    private String symbol;
    private String name;
    private BigDecimal price;
}