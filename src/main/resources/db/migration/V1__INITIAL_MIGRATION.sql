CREATE TABLE user_preferences
(
    id                 UUID         NOT NULL,
    user_id            UUID         NOT NULL,
    currency           VARCHAR(255) NOT NULL,
    language           VARCHAR(255) NOT NULL,
    timezone           VARCHAR(255) NOT NULL,
    theme              VARCHAR(255) NOT NULL,
    email_notification BOOLEAN      NOT NULL,
    push_notification  BOOLEAN      NOT NULL,
    CONSTRAINT pk_user_preferences PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                      UUID         NOT NULL,
    email                   VARCHAR(100) NOT NULL,
    first_name              VARCHAR(50)  NOT NULL,
    last_name               VARCHAR(50)  NOT NULL,
    password                VARCHAR(255) NOT NULL,
    profile_picture         VARCHAR(500),
    email_verified          BOOLEAN      NOT NULL,
    enabled                 BOOLEAN      NOT NULL,
    account_non_locked      BOOLEAN      NOT NULL,
    account_non_expired     BOOLEAN      NOT NULL,
    credentials_non_expired BOOLEAN      NOT NULL,
    last_login_at           TIMESTAMP WITHOUT TIME ZONE,
    password_changed_at     TIMESTAMP WITHOUT TIME ZONE,
    role                    VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE user_preferences
    ADD CONSTRAINT uc_user_preferences_user UNIQUE (user_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE user_preferences
    ADD CONSTRAINT FK_USER_PREFERENCES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);