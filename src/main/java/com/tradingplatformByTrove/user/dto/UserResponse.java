package com.tradingplatformByTrove.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID userId;
    private String username;
    private Integer gemCount;
    private Integer totalTradesExecuted;
    private Integer rank;
}