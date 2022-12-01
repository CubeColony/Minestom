CREATE TABLE IF NOT EXISTS `account` (
    `id`       BIGINT      NOT NULL,
    `username` VARCHAR(16) NOT NULL,
    `uuid`     BINARY(16)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uuid` (`uuid`)
);

-- CREATE TABLE IF NOT EXISTS `account_discord` (
--     `id`        BIGINT      NOT NULL AUTO_INCREMENT,
--     `snowflake`  BIGINT      NOT NULL UNIQUE,
--     `username`  VARCHAR(32) NOT NULL,
--     `nickname`  VARCHAR(32) NULL,
--     PRIMARY KEY (`id`, `snowflake`)
-- );
--
-- CREATE TABLE IF NOT EXISTS `account_website` (
--     `id`       BIGINT       NOT NULL AUTO_INCREMENT,
--     `email`    VARCHAR(255) NOT NULL UNIQUE,
--     `username` VARCHAR(32)  NOT NULL,
--     PRIMARY KEY (`id`, `email`)
-- );
--
-- CREATE TABLE IF NOT EXISTS `account_bridge` (
--     `id`         INT(11) NOT NULL AUTO_INCREMENT,
--     `account_id` INT(11) NULL UNIQUE,
--     `discord_id` INT(11) NULL UNIQUE,
--     `website_id` INT(11) NULL UNIQUE,
--     PRIMARY KEY (`id`),
--     FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
--     FOREIGN KEY (`discord_id`) REFERENCES `account_discord` (`id`) ON DELETE CASCADE,
--     FOREIGN KEY (`website_id`) REFERENCES `account_website` (`id`) ON DELETE CASCADE
-- );