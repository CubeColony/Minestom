CREATE TABLE IF NOT EXISTS `account` (
    `id`       BIGINT      NOT NULL, -- id can be deconstructed into a timestamp, Instant.ofEpochMilli(id >> 22).
    `username` VARCHAR(16) NOT NULL, -- players username, updated everytime a player joins.
    `uuid`     BINARY(16)  NOT NULL, -- players stripped uuid, can be constructed with UUIDUtil.build(HEX(uuid)).
    PRIMARY KEY (`id`, `uuid`),
    UNIQUE KEY `uuid` (`uuid`)
);

CREATE TABLE IF NOT EXISTS `account_discord` (
    `account_id` BIGINT      NOT NULL,
    `snowflake`   BIGINT      NOT NULL,
    `username`   VARCHAR(32) NOT NULL,
    `nickname`   VARCHAR(32) NULL,
    PRIMARY KEY (`account_id`, `snowflake`),
    UNIQUE KEY `snowflake` (`snowflake`),
    FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `rank` (
    `id`     BIGINT       NOT NULL, -- id can be deconstructed into a timestamp, Instant.ofEpochMilli(id >> 22).
    `name`   VARCHAR(16)  NOT NULL, -- human-readable rank name.
    `weight` TINYINT      NOT NULL, -- rank weight, determines importance.
    `prefix`  VARCHAR(255) NOT NULL, -- rank prefix, can contain colors, symbols, bitmap characters.
    `font`   VARCHAR(32)  NOT NULL, -- rank font, this is for when bitmap fonts are needed for the prefix.
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `weight` (`weight`)
);

CREATE TABLE IF NOT EXISTS `account_rank` (
    `account_id`  BIGINT    NOT NULL, -- the id reference to the player.
    `rank_id`     BIGINT    NOT NULL, -- the id reference to the rank.
    `issued_by`   VARCHAR   NOT NULL, -- the issuer, ei. console, store, event, player_name.
    `acquired_at` TIMESTAMP NOT NULL, -- the time the rank was acquired by the player.
    `expires_at`  TIMESTAMP NOT NULL, -- the time the rank expires, defaults to 2099/1/1.
    PRIMARY KEY (`account_id`, `rank_id`),
    FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`rank_id`) REFERENCES `rank` (`id`) ON DELETE CASCADE
);