CREATE TABLE budgets
(
    id              UUID                        NOT NULL,
    name            VARCHAR(100)                NOT NULL,
    amount          DECIMAL(19, 2)              NOT NULL,
    period_type     VARCHAR(20)                 NOT NULL,
    start_date      date                        NOT NULL,
    end_date        date                        NOT NULL,
    alert_threshold INTEGER                     NOT NULL,
    user_id         UUID                        NOT NULL,
    category_id     UUID,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_budgets PRIMARY KEY (id)
);

CREATE INDEX idx_budget_period ON budgets (start_date, end_date);

ALTER TABLE budgets
    ADD CONSTRAINT FK_BUDGETS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

CREATE INDEX idx_budget_category ON budgets (category_id);

ALTER TABLE budgets
    ADD CONSTRAINT FK_BUDGETS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_budget_user ON budgets (user_id);