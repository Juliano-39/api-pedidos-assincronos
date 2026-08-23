-- V1__baseline.sql
-- Fotografia do schema no momento em que o controle de versão via Flyway
-- foi introduzido. Reflete o estado gerado anteriormente pelo Hibernate
-- (ddl-auto=update). Não é executado no banco existente (baseline),
-- mas serve como fonte de verdade para recriação do zero.

CREATE TABLE `users`
(
    `id`       bigint       NOT NULL AUTO_INCREMENT,
    `password` varchar(255) NOT NULL,
    `role`     varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `category`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `name`        varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `products`
(
    `id`          bigint         NOT NULL AUTO_INCREMENT,
    `created_at`  datetime(6) NOT NULL,
    `description` varchar(255) DEFAULT NULL,
    `name`        varchar(255)   NOT NULL,
    `price`       decimal(10, 2) NOT NULL,
    `quantity`    int            NOT NULL,
    `status`      bit(1)         NOT NULL,
    `updated_at`  datetime(6) NOT NULL,
    `category_id` bigint         NOT NULL,
    PRIMARY KEY (`id`),
    KEY           `FK1cf90etcu98x1e6n9aks3tel3` (`category_id`),
    CONSTRAINT `FK1cf90etcu98x1e6n9aks3tel3` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `orders`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) NOT NULL,
    `status`     enum('CANCELLED','PAID','PENDING') NOT NULL,
    `total`      decimal(38, 2) DEFAULT NULL,
    `updated_at` datetime(6) NOT NULL,
    `user_id`    bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY          `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
    CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `order_item`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `product_name` varchar(255)   DEFAULT NULL,
    `quantity`     int    NOT NULL,
    `unit_price`   decimal(38, 2) DEFAULT NULL,
    `order_id`     bigint NOT NULL,
    `product_id`   bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY            `FKt4dc2r9nbvbujrljv3e23iibt` (`order_id`),
    KEY            `FKc5uhmwioq5kscilyuchp4w49o` (`product_id`),
    CONSTRAINT `FKc5uhmwioq5kscilyuchp4w49o` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
    CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

