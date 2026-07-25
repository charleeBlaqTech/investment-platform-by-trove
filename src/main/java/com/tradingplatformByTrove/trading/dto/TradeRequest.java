package com.tradingplatformByTrove.trading.dto;

import com.tradingplatformByTrove.common.model.TradeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TradeRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Asset Symbol is required")
    private String assetSymbol;

    @NotNull(message = "Trade type (BUY/SELL) is required")
    private TradeType tradeType;

    @NotNull(message = "Quantity must be specified")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

//    @NotNull(message = "Execution price is required")
//    @Positive(message = "Execution price must be positive")
//    private BigDecimal price;
}
