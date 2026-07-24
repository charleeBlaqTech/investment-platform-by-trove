package com.tradingplatformByTrove.common.exception;

/**
 * Exception thrown when user lacks cash balance or asset quantities during execution.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
