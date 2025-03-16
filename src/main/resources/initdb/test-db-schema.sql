CREATE TABLE MEMBER
(
    user_id    VARCHAR(255)                        NOT NULL,
    password   VARCHAR(255)                        NOT NULL,
    role       BIGINT                              NOT NULL,
    nick_name  VARCHAR(255)                        NOT NULL,
    cart_id    BIGINT                              NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE PRODUCT
(
    id         BIGINT AUTO_INCREMENT               NOT NULL,
    name       VARCHAR(255)                        NOT NULL,
    price      VARCHAR(255)                        NOT NULL,
    size       VARCHAR(255)                        NOT NULL,
    color      VARCHAR(255)                        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)

);

CREATE TABLE CART
(
    id         BIGINT AUTO_INCREMENT               NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE CART_DETAIL
(
    id         BIGINT AUTO_INCREMENT                  NOT NULL,
    cart_id    BIGINT                                 NOT NULL,
    product_id VARCHAR(255)                           NOT NULL,
    size       VARCHAR(255)                           NOT NULL,
    quantity   BIGINT                                 NOT NULL,
    status     VARCHAR(255) DEFAULT 'wanted'          NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (id, cart_id, product_id, size) -- 카트에 동일한 사이즈의 상품을 2개 이상 담을 수 없다.
);

CREATE TABLE SETTLEMENT
(
    id         BIGINT AUTO_INCREMENT               NOT NULL,
    name       VARCHAR(255)                        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP DEFAULT NULL              NUll,
    PRIMARY KEY (id)
);

CREATE TABLE EXPENSE
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    user_id       VARCHAR(255)          NOT NULL,
    expense_name  VARCHAR(255)          NOT NULL,
    amount        DECIMAL(10, 2)        NOT NULL,
    expensed_at   DATE                  NOT NULL,
    settlement_id BIGINT                NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (settlement_id, user_id, expense_name)
);

CREATE TABLE SETTLEMENT_RESULT
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    sender_user_no     VARCHAR(255)          NOT NULL,
    sender_user_name   VARCHAR(255)          NOT NULL,
    send_amount        DECIMAL(10, 2)        NOT NULL,
    receiver_user_no   VARCHAR(255)          NOT NULL,
    receiver_user_name VARCHAR(255)          NOT NULL,
    created_at         DATETIME              NOT NULL,
    settlement_id      BIGINT                NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE SETTLEMENT_USERS
(
    id            BIGINT AUTO_INCREMENT  NOT NULL,
    user_id       VARCHAR(255)           NOT NULL,
    settlement_id BIGINT                 NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (settlement_id, user_id)
);


