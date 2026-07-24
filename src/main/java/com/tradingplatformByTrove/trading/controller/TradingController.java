package com.tradingplatformByTrove.trading.controller;

import com.tradingplatformByTrove.common.dto.ApiResponse;
import com.tradingplatformByTrove.trading.dto.TradeRequest;
import com.tradingplatformByTrove.trading.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trading")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/trade")
    public ResponseEntity<ApiResponse<String>> executeTrade(@Valid @RequestBody TradeRequest request) {
        tradingService.executeTrade(request);
        return ResponseEntity.ok(ApiResponse.success("Trade executed successfully and gems calculated", "OK"));
    }
}