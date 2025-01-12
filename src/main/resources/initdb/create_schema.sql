CREATE DATABASE IF NOT EXISTS `stark_industry`;
USE stark_industry;
CREATE USER IF NOT EXISTS `tony`@`localhost` IDENTIFIED BY 'edth';
CREATE USER `tony`@`%` IDENTIFIED BY 'edth';
GRANT all privileges ON `stark_industry`.* TO `tony`@`localhost`;
GRANT all privileges ON `stark_industry`.* TO `tony`@`%`;

CREATE TABLE `USER`
(
    `user_id`    VARCHAR(255)           NOT NULL COMMENT '사용자 ID',
    `password`   VARCHAR(255)           NOT NULL COMMENT '사용자 Password',
    `role`       VARCHAR(255)           NOT NULL COMMENT '권한; 등급; ADMIN, USER',
    `nick_name`  VARCHAR(255)           NOT NULL COMMENT '별명',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (user_id)
);

CREATE TABLE `PRODUCT`
(
    `product_id`   VARCHAR(255)           NOT NULL COMMENT '상품 ID',
    `product_name` VARCHAR(255)           NOT NULL COMMENT '상품명',
    `created_at`   DATETIME DEFAULT NOW() NOT NULL,
    `updated_at`   DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (product_id)

);

CREATE TABLE `CART`
(
    `cart_id`        VARCHAR(255)           NOT NULL COMMENT '장바구니 ID(PK)',
    `user_id`        VARCHAR(255)           NOT NULL COMMENT '사용자(FK)',
    `cart_detail_id` VARCHAR(255)           NOT NULL COMMENT '장바구니 아이템(FK)',
    `created_at`     DATETIME DEFAULT NOW() NOT NULL,
    `updated_at`     DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (cart_id),
    UNIQUE KEY (cart_id, user_id, cart_detail_id)
);

CREATE TABLE `CART_DETAIL`
(
    `id`         VARCHAR(255)           NOT NULL COMMENT '장바구니 ID(PK)',
    `cart_id`    VARCHAR(255)           NOT NULL COMMENT '사용자(FK)',
    `product_id` VARCHAR(255)           NOT NULL COMMENT '장바구니 아이템(FK)',
    `size`       VARCHAR(255)           NOT NULL COMMENT '장바구니 아이템(FK)',
    `quantity`   VARCHAR(255)           NOT NULL COMMENT '장바구니 아이템(FK)',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (id),
    UNIQUE KEY (cart_id, product_id, size)
)
