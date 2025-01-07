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
