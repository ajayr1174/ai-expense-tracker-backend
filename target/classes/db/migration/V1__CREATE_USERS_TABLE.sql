CREATE TABLE users
(
    id       UUID NOT NULL,
    email    VARCHAR(255),
    name     VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);