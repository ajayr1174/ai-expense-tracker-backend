CREATE TABLE categories
(
    id         UUID                        NOT NULL,
    name       VARCHAR(100)                NOT NULL,
    icon       VARCHAR(100),
    color      VARCHAR(20),
    type       VARCHAR(20)                 NOT NULL,
    user_id    UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE INDEX idx_categories_type ON categories (type);

ALTER TABLE categories
    ADD CONSTRAINT FK_CATEGORIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_categories_user_id ON categories (user_id);