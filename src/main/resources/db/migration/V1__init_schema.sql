-- ============================================================================
-- V1__init_schema.sql
-- Initial schema for the Gamified Investment Trading Platform
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Enable PostgreSQL extension for UUID generation
-- ----------------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ============================================================================
-- USERS
-- ============================================================================
CREATE TABLE IF NOT EXISTS users
(
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    username VARCHAR(50) NOT NULL UNIQUE,

    gem_count INT NOT NULL DEFAULT 0,

    total_trades_executed INT NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_users_username
ON users(username);

-- ============================================================================
-- ASSETS
-- Static asset metadata
-- ============================================================================
CREATE TABLE IF NOT EXISTS assets
(
    symbol VARCHAR(20) PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    asset_type VARCHAR(30) NOT NULL
);

-- ============================================================================
-- PORTFOLIOS
-- Each user owns one portfolio
-- ============================================================================
CREATE TABLE IF NOT EXISTS portfolios
(
    portfolio_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL UNIQUE,

    cash_balance NUMERIC(19,4) NOT NULL DEFAULT 10000.0000,

    CONSTRAINT fk_portfolio_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_portfolios_user
ON portfolios(user_id);

-- ============================================================================
-- PORTFOLIO HOLDINGS
-- Represents assets owned inside a portfolio
-- ============================================================================
CREATE TABLE IF NOT EXISTS portfolio_assets
(
    id BIGSERIAL PRIMARY KEY,

    portfolio_id UUID NOT NULL,

    asset_symbol VARCHAR(20) NOT NULL,

    quantity NUMERIC(19,8) NOT NULL DEFAULT 0,

    average_buy_price NUMERIC(19,4) NOT NULL DEFAULT 0.0000,

    CONSTRAINT fk_portfolio_asset_portfolio
        FOREIGN KEY (portfolio_id)
        REFERENCES portfolios(portfolio_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_portfolio_asset_asset
        FOREIGN KEY (asset_symbol)
        REFERENCES assets(symbol)
        ON DELETE RESTRICT,

    CONSTRAINT uq_portfolio_asset
        UNIQUE(portfolio_id, asset_symbol)
);

CREATE INDEX IF NOT EXISTS idx_portfolio_assets_portfolio
ON portfolio_assets(portfolio_id);

-- ============================================================================
-- STREAK TRACKERS
-- Tracks consecutive trading streaks
-- ============================================================================
CREATE TABLE IF NOT EXISTS streak_trackers
(
    user_id UUID PRIMARY KEY,

    current_streak INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_streak_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- ============================================================================
-- MARKET PRICES
-- Stores current simulated market prices.
-- Price history can be introduced later.
-- ============================================================================
CREATE TABLE IF NOT EXISTS market_prices
(
    symbol VARCHAR(20) PRIMARY KEY,

    asset_name VARCHAR(255) NOT NULL,

    current_price NUMERIC(19,4) NOT NULL,

    currency VARCHAR(10) NOT NULL DEFAULT 'USD',

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- Seed Initial Assets
-- ============================================================================
INSERT INTO assets(symbol, name, asset_type)
VALUES
    ('AAPL', 'Apple Inc.', 'STOCK'),
    ('TSLA', 'Tesla Inc.', 'STOCK'),
    ('NVDA', 'NVIDIA Corporation', 'STOCK'),
    ('BTC', 'Bitcoin', 'CRYPTO'),
    ('ETH', 'Ethereum', 'CRYPTO')
ON CONFLICT(symbol) DO NOTHING;

-- ============================================================================
-- Seed Initial Market Prices
-- ============================================================================
INSERT INTO market_prices
(symbol, asset_name, current_price, currency)
VALUES
    ('AAPL', 'Apple Inc.', 185.5000, 'USD'),
    ('TSLA', 'Tesla Inc.', 240.2000, 'USD'),
    ('NVDA', 'NVIDIA Corporation', 125.7500, 'USD'),
    ('BTC', 'Bitcoin', 65000.0000, 'USD'),
    ('ETH', 'Ethereum', 3400.0000, 'USD')
ON CONFLICT(symbol) DO NOTHING;
    create table market_prices (
        current_price numeric(19,4) not null,
        updated_at timestamp(6) with time zone,
        currency varchar(10) not null,
        symbol varchar(20) not null,
        asset_name varchar(255) not null,
        primary key (symbol)
    );
