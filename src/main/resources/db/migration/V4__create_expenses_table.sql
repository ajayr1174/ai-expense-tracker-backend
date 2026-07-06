CREATE TABLE expenses
(
    id             UUID                        NOT NULL,
    user_id        UUID                        NOT NULL,
    category_id    UUID                        NOT NULL,
    amount         DECIMAL(19, 2)              NOT NULL,
    currency       VARCHAR(3)                  NOT NULL,
    description    VARCHAR(255)                NOT NULL,
    expense_date   date                        NOT NULL,
    payment_method VARCHAR(30)                 NOT NULL,
    source         VARCHAR(30)                 NOT NULL,
    merchant_name  VARCHAR(150),
    notes          VARCHAR(1000),
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_expenses PRIMARY KEY (id)
);

CREATE INDEX idx_expenses_expense_date ON expenses (expense_date);

CREATE INDEX idx_expenses_user_date ON expenses (user_id, expense_date);

ALTER TABLE expenses
    ADD CONSTRAINT FK_EXPENSES_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

CREATE INDEX idx_expenses_category_id ON expenses (category_id);

ALTER TABLE expenses
    ADD CONSTRAINT FK_EXPENSES_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_expenses_user_id ON expenses (user_id);