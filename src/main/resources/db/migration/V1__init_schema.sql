-- Enable UUID extension for auto-generating UUID primary keys
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--------------------------------------------------------------------------------
-- 1. USERS TABLE
--------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username              VARCHAR(20) NOT NULL UNIQUE,
    gem_count             INT NOT NULL DEFAULT 0,
    total_trades_executed INT NOT NULL DEFAULT 0,
    rank                  INT
);

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

--------------------------------------------------------------------------------
-- 2. ASSETS TABLE
--------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS assets (
    symbol                VARCHAR(20) PRIMARY KEY,
    name                  VARCHAR(255) NOT NULL,
    price                 NUMERIC(19, 4) NOT NULL DEFAULT 0.0000
);

--------------------------------------------------------------------------------
-- 3. PORTFOLIOS TABLE
--------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS portfolios (
    portfolio_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id               UUID NOT NULL UNIQUE,
    cash_balance          NUMERIC(19, 4) NOT NULL DEFAULT 10000.0000,
    CONSTRAINT fk_portfolios_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_portfolios_user_id ON portfolios(user_id);

--------------------------------------------------------------------------------
-- 4. PORTFOLIO HOLDINGS (PortfolioAsset)
--------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS portfolio_assets (
    id                    BIGSERIAL PRIMARY KEY,
    portfolio_id          UUID NOT NULL,
    asset_symbol          VARCHAR(20) NOT NULL,
    asset_name            VARCHAR(255) NOT NULL,
    quantity              NUMERIC(19, 8) NOT NULL DEFAULT 0,
    average_buy_price     NUMERIC(19, 4) NOT NULL DEFAULT 0.0000,
    CONSTRAINT fk_portfolio_assets_portfolio FOREIGN KEY (portfolio_id)
        REFERENCES portfolios(portfolio_id) ON DELETE CASCADE,
    CONSTRAINT fk_portfolio_assets_asset FOREIGN KEY (asset_symbol)
        REFERENCES assets(symbol) ON DELETE RESTRICT,
    CONSTRAINT uq_portfolio_asset UNIQUE (portfolio_id, asset_symbol)
);

CREATE INDEX IF NOT EXISTS idx_portfolio_assets_portfolio_id ON portfolio_assets(portfolio_id);

--------------------------------------------------------------------------------
-- 5. STREAK TRACKER TABLE
--------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS streak_trackers (
    user_id               UUID PRIMARY KEY,
    current_streak        INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_streak_trackers_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);