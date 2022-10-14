-- apply changes
create table bank_accounts (
                               id                            bigint auto_increment not null,
                               balance                       double not null,
                               created_at                    datetime not null,
                               updated_at                    datetime not null,
                               constraint pk_bank_accounts primary key (id)
);

create table bank_account_transactions (
                                           bank_account_id               bigint not null,
                                           transaction_id                bigint not null,
                                           constraint uq_bank_accounts_transaction_id unique (transaction_id),
                                           constraint pk_bank_account_transactions primary key (bank_account_id,transaction_id)
);

create table discord_accounts (
                                  id                            bigint auto_increment not null,
                                  discord_id                    bigint not null,
                                  username                      varchar(255) not null,
                                  constraint uq_discord_accounts_discord_id unique (discord_id),
                                  constraint uq_discord_accounts_username unique (username),
                                  constraint pk_discord_accounts primary key (id)
);

create table transactions (
                              id                            bigint auto_increment not null,
                              description                   varchar(255),
                              amount                        double not null,
                              type                          integer,
                              action                        integer,
                              created_at                    datetime not null,
                              updated_at                    datetime not null,
                              constraint pk_transactions primary key (id)
);

create table friendship_requests (
                                     id                            bigint auto_increment not null,
                                     issuer_id                     bigint not null,
                                     target_id                     bigint not null,
                                     expires_at                    datetime,
                                     status                        integer,
                                     created_at                    datetime not null,
                                     updated_at                    datetime not null,
                                     constraint uq_friendship_requests_issuer_id unique (issuer_id),
                                     constraint uq_friendship_requests_target_id unique (target_id),
                                     constraint pk_friendship_requests primary key (id)
);

create table sessions (
                          id                            bigint auto_increment not null,
                          ip                            varchar(255),
                          locale                        varchar(20),
                          launcher                      tinyint(1) default 0 not null,
                          version                       varchar(255),
                          launcher_version              varchar(255),
                          ended_at                      datetime,
                          created_at                    datetime not null,
                          updated_at                    datetime not null,
                          constraint pk_sessions primary key (id)
);

create table players (
                         id                            bigint auto_increment not null,
                         uuid                          varchar(40) not null,
                         name                          varchar(255) not null,
                         locale                        varchar(20),
                         play_time                     bigint not null,
                         last_login                    datetime,
                         preferences_id                bigint,
                         bank_account_id               bigint,
                         discord_account_id            bigint,
                         account_id                    bigint,
                         rank_id                       bigint,
                         created_at                    datetime not null,
                         updated_at                    datetime not null,
                         constraint uq_players_uuid unique (uuid),
                         constraint uq_players_name unique (name),
                         constraint uq_players_preferences_id unique (preferences_id),
                         constraint uq_players_bank_account_id unique (bank_account_id),
                         constraint uq_players_discord_account_id unique (discord_account_id),
                         constraint uq_players_account_id unique (account_id),
                         constraint uq_players_rank_id unique (rank_id),
                         constraint pk_players primary key (id)
);

create table players_sessions (
                                  player_id                     bigint not null,
                                  session_id                    bigint not null,
                                  constraint uq_players_session_id unique (session_id),
                                  constraint pk_players_sessions primary key (player_id,session_id)
);

create table players_friendship_requests (
                                             players_id                    bigint not null,
                                             friendship_requests_id        bigint not null,
                                             constraint pk_players_friendship_requests primary key (players_id,friendship_requests_id)
);

create table friends (
                         friend_id                     bigint not null,
                         id                            bigint not null,
                         constraint pk_friends primary key (friend_id,id)
);

create table player_accounts (
                                 id                            bigint auto_increment not null,
                                 balance                       double not null,
                                 created_at                    datetime not null,
                                 updated_at                    datetime not null,
                                 constraint pk_player_accounts primary key (id)
);

create table player_account_transactions (
                                             player_account_id             bigint not null,
                                             transaction_id                bigint not null,
                                             constraint uq_player_accounts_transaction_id unique (transaction_id),
                                             constraint pk_player_account_transactions primary key (player_account_id,transaction_id)
);

create table preferences (
                             id                            bigint auto_increment not null,
                             chat                          tinyint(1) default 0 not null,
                             private_messages              tinyint(1) default 0 not null,
                             friend_requests               tinyint(1) default 0 not null,
                             constraint pk_preferences primary key (id)
);

create table products (
                          id                            bigint auto_increment not null,
                          name                          varchar(255),
                          payload                       varchar(255),
                          type                          integer,
                          description                   varchar(255),
                          price                         double not null,
                          stock                         integer not null,
                          created_at                    datetime not null,
                          updated_at                    datetime not null,
                          constraint uq_products_name unique (name),
                          constraint pk_products primary key (id)
);

create table punishments (
                             id                            bigint auto_increment not null,
                             player_id                     bigint,
                             issuer                        varchar(255),
                             type                          integer,
                             reason                        varchar(255),
                             expires_at                    datetime,
                             permanent                     tinyint(1) default 0 not null,
                             revoked                       tinyint(1) default 0 not null,
                             revoker                       varchar(255),
                             revocation_reason             varchar(255),
                             created_at                    datetime not null,
                             updated_at                    datetime not null,
                             constraint uq_punishments_player_id unique (player_id),
                             constraint pk_punishments primary key (id)
);

create table purchases (
                           id                            bigint auto_increment not null,
                           offline_player_id             bigint not null,
                           product_id                    bigint,
                           amount                        integer not null,
                           price                         double not null,
                           created_at                    datetime not null,
                           updated_at                    datetime not null,
                           constraint uq_purchases_product_id unique (product_id),
                           constraint pk_purchases primary key (id)
);

create table ranks (
                       id                            bigint auto_increment not null,
                       name                          varchar(255) not null,
                       prefix                        varchar(255),
                       child_id                      bigint,
                       discord_id                    bigint not null,
                       created_at                    datetime not null,
                       updated_at                    datetime not null,
                       constraint uq_ranks_name unique (name),
                       constraint uq_ranks_prefix unique (prefix),
                       constraint uq_ranks_child_id unique (child_id),
                       constraint uq_ranks_name_prefix unique (name,prefix),
                       constraint pk_ranks primary key (id)
);

create table ranks_permissions (
                                   ranks_id                      bigint not null,
                                   value                         varchar(255) not null
);

-- foreign keys and indices
alter table bank_account_transactions add constraint fk_bank_account_transactions_bank_accounts foreign key (bank_account_id) references bank_accounts (id) on delete restrict on update restrict;

alter table bank_account_transactions add constraint fk_bank_account_transactions_transactions foreign key (transaction_id) references transactions (id) on delete restrict on update restrict;

alter table friendship_requests add constraint fk_friendship_requests_issuer_id foreign key (issuer_id) references players (id) on delete restrict on update restrict;

alter table friendship_requests add constraint fk_friendship_requests_target_id foreign key (target_id) references players (id) on delete restrict on update restrict;

alter table players add constraint fk_players_preferences_id foreign key (preferences_id) references preferences (id) on delete restrict on update restrict;

alter table players add constraint fk_players_bank_account_id foreign key (bank_account_id) references bank_accounts (id) on delete restrict on update restrict;

alter table players add constraint fk_players_discord_account_id foreign key (discord_account_id) references discord_accounts (id) on delete restrict on update restrict;

alter table players add constraint fk_players_account_id foreign key (account_id) references player_accounts (id) on delete restrict on update restrict;

alter table players add constraint fk_players_rank_id foreign key (rank_id) references ranks (id) on delete restrict on update restrict;

alter table players_sessions add constraint fk_players_sessions_players foreign key (player_id) references players (id) on delete restrict on update restrict;

alter table players_sessions add constraint fk_players_sessions_sessions foreign key (session_id) references sessions (id) on delete restrict on update restrict;

create index ix_players_friendship_requests_players on players_friendship_requests (players_id);
alter table players_friendship_requests add constraint fk_players_friendship_requests_players foreign key (players_id) references players (id) on delete restrict on update restrict;

create index ix_players_friendship_requests_friendship_requests on players_friendship_requests (friendship_requests_id);
alter table players_friendship_requests add constraint fk_players_friendship_requests_friendship_requests foreign key (friendship_requests_id) references friendship_requests (id) on delete restrict on update restrict;

create index ix_friends_players_1 on friends (friend_id);
alter table friends add constraint fk_friends_players_1 foreign key (friend_id) references players (id) on delete restrict on update restrict;

create index ix_friends_players_2 on friends (id);
alter table friends add constraint fk_friends_players_2 foreign key (id) references players (id) on delete restrict on update restrict;

alter table player_account_transactions add constraint fk_player_account_transactions_player_accounts foreign key (player_account_id) references player_accounts (id) on delete restrict on update restrict;

alter table player_account_transactions add constraint fk_player_account_transactions_transactions foreign key (transaction_id) references transactions (id) on delete restrict on update restrict;

alter table punishments add constraint fk_punishments_player_id foreign key (player_id) references players (id) on delete restrict on update restrict;

create index ix_purchases_offline_player_id on purchases (offline_player_id);
alter table purchases add constraint fk_purchases_offline_player_id foreign key (offline_player_id) references players (id) on delete restrict on update restrict;

alter table purchases add constraint fk_purchases_product_id foreign key (product_id) references products (id) on delete restrict on update restrict;

alter table ranks add constraint fk_ranks_child_id foreign key (child_id) references ranks (id) on delete restrict on update restrict;

create index ix_ranks_permissions_ranks_id on ranks_permissions (ranks_id);
alter table ranks_permissions add constraint fk_ranks_permissions_ranks_id foreign key (ranks_id) references ranks (id) on delete restrict on update restrict;