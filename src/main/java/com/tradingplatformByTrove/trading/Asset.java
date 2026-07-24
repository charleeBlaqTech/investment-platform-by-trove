package com.tradingplatformByTrove.trading;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Tradable asset domain model.
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