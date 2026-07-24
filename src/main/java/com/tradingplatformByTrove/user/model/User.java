package com.tradingplatformByTrove.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * User Domain Entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID userId;
    private String username;

    @Builder.Default
    private Integer gemCount = 0;

    @Builder.Default
    private Integer totalTradesExecuted = 0;

    private Integer rank;
}