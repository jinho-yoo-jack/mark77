CREATE DATABASE IF NOT EXISTS `stark_industry`;
USE stark_industry;
CREATE USER IF NOT EXISTS `tony`@`localhost` IDENTIFIED BY 'edth';
CREATE USER `tony`@`%` IDENTIFIED BY 'edth';
GRANT all privileges ON `stark_industry`.* TO `tony`@`localhost`;
GRANT all privileges ON `stark_industry`.* TO `tony`@`%`;

CREATE TABLE `MEMBER`
(
    `user_id`    VARCHAR(255)           NOT NULL COMMENT '사용자 ID',
    `password`   VARCHAR(255)           NOT NULL COMMENT '사용자 Password',
    `role`       BIGINT                 NOT NULL COMMENT '권한; 등급; ADMIN(99), USER(1)',
    `nick_name`  VARCHAR(255)           NOT NULL COMMENT '별명',
    `cart_id`    BIGINT                 NULL COMMENT '장바구니 ID',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (user_id)
);

CREATE TABLE `PRODUCT`
(
    `id`         BIGINT AUTO_INCREMENT  NOT NULL COMMENT '상품 ID',
    `name`       VARCHAR(255)           NOT NULL COMMENT '상품명',
    `price`      VARCHAR(255)           NOT NULL COMMENT '가격',
    `size`       VARCHAR(255)           NOT NULL COMMENT '사이즈',
    `color`      VARCHAR(255)           NOT NULL COMMENT '색상',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (id)

);

CREATE TABLE `CART`
(
    `id`         BIGINT AUTO_INCREMENT  NOT NULL COMMENT '장바구니 ID(PK)',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (id)
);

CREATE TABLE `CART_DETAIL`
(
    `id`         BIGINT AUTO_INCREMENT         NOT NULL COMMENT '장바구니 상세 ID(PK)',
    `cart_id`    BIGINT                        NOT NULL COMMENT '장바구니 ID(FK)',
    `product_id` VARCHAR(255)                  NOT NULL COMMENT '장바구니 아이템',
    `size`       VARCHAR(255)                  NOT NULL COMMENT '장바구니 아이템',
    `quantity`   BIGINT                        NOT NULL COMMENT '장바구니 아이템',
    `status`     VARCHAR(255) DEFAULT 'wanted' NULL COMMENT 'wanted / ordered',
    `created_at` DATETIME     DEFAULT NOW()    NOT NULL,
    `updated_at` DATETIME     DEFAULT NOW()    NOT NUll,
    PRIMARY KEY (id),
    UNIQUE KEY (id, cart_id, product_id, size) -- 카트에 동일한 사이즈의 상품을 2개 이상 담을 수 없다.
);

INSERT INTO PRODUCT
VALUES (DEFAULT, '구름_신상_강의', 100000, 'free', 'free', DEFAULT, DEFAULT);
INSERT INTO PRODUCT
VALUES (DEFAULT, '구름_신상_아이템', '100000', 'free', 'free', DEFAULT, DEFAULT);


CREATE TABLE `SETTLEMENT`
(
    `id`         BIGINT AUTO_INCREMENT  NOT NULL COMMENT '정산 ID',
    `name`       VARCHAR(255)           NOT NULL COMMENT '정산명',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    `deleted_at` DATETIME DEFAULT NULL  NUll,
    PRIMARY KEY (id)
);

CREATE TABLE `EXPENSE`
(
    `id`            BIGINT AUTO_INCREMENT NOT NULL COMMENT '지출 ID',
    `user_id`       VARCHAR(255)          NOT NULL COMMENT '지출한 사람',
    `expense_name`  VARCHAR(255)          NOT NULL COMMENT '지출 항목',
    `amount`        DECIMAL(10, 2)        NOT NULL COMMENT '지출 금액',
    `expensed_at`    DATE                  NOT NULL COMMENT '지출 날짜',
    `settlement_id` BIGINT                NOT NULL COMMENT '정산 ID',
    PRIMARY KEY (id),
    UNIQUE KEY (settlement_id, user_id, expense_name)
);

CREATE TABLE `SETTLEMENT_RESULT`
(
    `id`                 BIGINT AUTO_INCREMENT NOT NULL COMMENT '정산 결과 ID',
    `sender_user_no`     VARCHAR(255)          NOT NULL COMMENT '보내는 사람의 ID',
    `sender_user_name`   VARCHAR(255)          NOT NULL COMMENT '보내는 사람의 이름',
    `send_amount`        DECIMAL(10, 2)        NOT NULL COMMENT '보내는 금액',
    `receiver_user_no`   VARCHAR(255)          NOT NULL COMMENT '받는 사람의 ID',
    `receiver_user_name` VARCHAR(255)          NOT NULL COMMENT '받는 사람의 이름',
    `created_at`         DATETIME              NOT NULL COMMENT '정산 결과 날짜',
    `settlement_id`      BIGINT                NOT NULL COMMENT '정산 ID',
    PRIMARY KEY (id)
);

CREATE TABLE `SETTLEMENT_USERS`
(
    `id`            BIGINT AUTO_INCREMENT  NOT NULL COMMENT '참여 ID',
    `user_id`       VARCHAR(255)           NOT NULL COMMENT '정산에 참여한 USER ID',
    `settlement_id` BIGINT                 NOT NULL COMMENT '참여한 정산 ID',
    `created_at`    DATETIME DEFAULT NOW() NOT NULL COMMENT '참여한 날짜',
    PRIMARY KEY (id),
    UNIQUE KEY (settlement_id, user_id)
);


