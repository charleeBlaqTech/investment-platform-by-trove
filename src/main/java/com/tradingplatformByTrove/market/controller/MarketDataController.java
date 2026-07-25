package com.tradingplatformByTrove.market.controller;

import com.tradingplatformByTrove.market.dto.AssetPriceResponse;
import com.tradingplatformByTrove.market.dto.TradeQuoteResponse;
import com.tradingplatformByTrove.market.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/market")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping("/prices")
    public ResponseEntity<List<AssetPriceResponse>> getAllPrices() {
        return ResponseEntity.ok(marketDataService.getAllMarketPrices());
    }

    @GetMapping("/prices/{symbol}")
    public ResponseEntity<AssetPriceResponse> getPriceBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(marketDataService.getPriceBySymbol(symbol));
    }

    @GetMapping("/quote")
    public ResponseEntity<TradeQuoteResponse> getTradeQuote(
            @RequestParam UUID userId,
            @RequestParam String symbol,
            @RequestParam String tradeType,
            @RequestParam int quantity) {
        return ResponseEntity.ok(marketDataService.getTradeQuote(userId, symbol, tradeType, quantity));
    }
}