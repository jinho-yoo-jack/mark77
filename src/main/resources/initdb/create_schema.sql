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
    `user_id`    VARCHAR(255)           NOT NULL COMMENT '사용자(FK)',
    `created_at` DATETIME DEFAULT NOW() NOT NULL,
    `updated_at` DATETIME DEFAULT NOW() NOT NUll,
    PRIMARY KEY (id),
    UNIQUE KEY (id, user_id)
);

CREATE TABLE `CART_DETAIL`
(
    `id`         BIGINT                        NOT NULL COMMENT '장바구니 ID(PK)',
    `cart_id`    VARCHAR(255)                  NOT NULL COMMENT '사용자(FK)',
    `product_id` VARCHAR(255)                  NOT NULL COMMENT '장바구니 아이템(FK)',
    `size`       VARCHAR(255)                  NOT NULL COMMENT '장바구니 아이템(FK)',
    `quantity`   VARCHAR(255)                  NOT NULL COMMENT '장바구니 아이템(FK)',
    `status`     VARCHAR(255) DEFAULT 'wanted' NULL COMMENT 'wanted / ordered',
    `created_at` DATETIME     DEFAULT NOW()    NOT NULL,
    `updated_at` DATETIME     DEFAULT NOW()    NOT NUll,
    PRIMARY KEY (id)
)
