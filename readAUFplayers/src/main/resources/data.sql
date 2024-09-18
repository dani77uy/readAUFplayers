-- TABLE CREATION
create table if not exists season
(
    id              tinyint primary key,
    year_of_season  smallint not null unique,
    tournament_name char(40) not null
);

create table if not exists team
(
    id                 smallint,
    name               varchar_ignorecase not null unique,
    url                varchar_ignorecase not null unique,
    season_that_belong tinyint,
    constraint pk_team primary key (id, season_that_belong),
    constraint fk_tea_sea foreign key (season_that_belong) references season (id)
);

create table if not exists player
(
    id        integer,
    name      varchar_ignorecase not null,
    birthdate date               null,
    position  char(10)           null,
    team_id   smallint,
    season_id tinyint,
    constraint pk_player primary key (id, team_id, season_id),
    constraint fk_pla_tea foreign key (team_id, season_id) references team (id, season_that_belong)
);

create table if not exists current_team_rooster
(
    player_id integer  not null,
    team_id   smallint not null,
    season_id tinyint  not null,
    since_date     date     null,
    to_date        date     null,
    constraint pk_cur_tea_roo primary key (player_id, team_id, season_id),
    constraint fk_cur_tea_roo_pla foreign key (player_id) references player(id),
    constraint fk_cur_tea_roo_tea foreign key (team_id,season_id) references team(id,season_that_belong)
);

insert into current_team_rooster(player_id, team_id, season_id) select p.id, p.team_id, p.season_id from player p;

alter table player drop constraint fk_pla_tea;
alter table player drop constraint pk_player;
alter table player drop column season_id;
alter table player drop column team_id;
alter table player add constraint pk_player primary key (id);

create table if not exists match
(
    id              bigint primary key ,
    home_team       smallint not null ,
    away_team       smallint not null ,
    home_goals      tinyint   not null default 0,
    away_goals      tinyint   not null default 0,
    season_id       tinyint   not null,
    match_day       tinyint   not null,
    match_date_time timestamp not null
);

alter table match add constraint fk_mat_sea foreign key (season_id) references season (id);

create table if not exists match_players
(
    match_id        bigint  not null,
    player_id       integer not null,
    minutes_played  tinyint not null default 0,
    goals_converted tinyint not null default 0,
    was_starting    boolean not null default false,
    was_substitute  boolean not null default false,
    was_not_present boolean not null default true,
    constraint pk_mat_pla primary key (match_id, player_id)
);

create table if not exists execution
(
    id                    bigint auto_increment primary key,
    datetime_of_execution timestamp not null,
    team_id               integer,
    team_id_last_opponent integer
);

alter table execution add season_id tinyint not null default 1;
alter table execution alter column team_id smallint;
alter table execution alter column team_id_last_opponent smallint;

create table if not exists player_change_after_execution
(
    player_id                       integer  not null,
    execution_id                    bigint   not null,
    new_minutes_value               smallint not null,
    new_matches_participation_value tinyint  not null,
    new_goals_value                 tinyint  not null,
    constraint pk_pla_cha_aft_exe primary key (player_id, execution_id),
    constraint fk_pla_cha_aft_exe_exe foreign key (execution_id) references execution (id)
);

create table if not exists team_last_opponent
(
  current_team smallint primary key,
  opponent_team smallint not null ,
  match_id bigint not null
);

alter table team_last_opponent add column season_id tinyint not null default 1;
alter table team_last_opponent add constraint fk_tea_las_opp_ct foreign key (current_team,season_id) references team(id,season_that_belong);
alter table team_last_opponent add constraint fk_tea_las_opp_ot foreign key (opponent_team,season_id) references team(id,season_that_belong);
alter table team_last_opponent add constraint fk_tea_las_opp_mat foreign key (match_id) references match(id);

-- SEQUENCES
create sequence if not exists seq_players start with 1 increment by 1;
create sequence if not exists seq_matches start with 1 increment by 1;

-- TRIGGERS

CREATE TRIGGER add_last_opponent
    AFTER INSERT
    ON match
    FOR EACH ROW
CALL "com.tipsuy.auf.service.trigger.AddLastOpponent";

CREATE TRIGGER add_match_players
    AFTER INSERT
    ON player_change_after_execution
    FOR EACH ROW
CALL "com.tipsuy.auf.service.trigger.AddMatchPlayers";

--- INSERT DATA
insert into season
values (1, 2024, 'Divisional D');

insert into team values (1, 'Academia FC', 'https://auf.org.uy/academia-fc/', 1);
insert into team values (2, 'Deportivo CEM', 'https://auf.org.uy/deportivo-cem/', 1);
insert into team values (3, 'Deutscher FK', 'https://auf.org.uy/deutscher-fk/', 1);
insert into team values (4, 'CA Dilio Sport', 'https://auf.org.uy/club-atletico-dilio-sport/', 1);
insert into team values (5, 'UD Estudiantes del Plata', 'https://auf.org.uy/estudiantes-del-plata/', 1);
insert into team values (6, 'CSyD Keguay Toledo', 'https://auf.org.uy/csd-keguay/', 1);
insert into team values (7, 'CD Melilla SAD', 'https://auf.org.uy/deportivo-melilla-sad/', 1);
insert into team values (8, 'C Mvd Boca Juniors', 'https://auf.org.uy/club-montevideo-boca-juniors/', 1);
insert into team values (9, 'CA Nuevo Casabó', 'https://auf.org.uy/club-atletico-nuevo-casabo/', 1);
insert into team values (10, 'CSyD Paso de la Arena', 'https://auf.org.uy/csyd-paso-de-la-arena/', 1);
insert into team values (11, 'C Real Montevideo', 'https://auf.org.uy/club-real-montevideo/', 1);
insert into team values (12, 'Rincón FC', 'https://auf.org.uy/rincon-de-carrasco/', 1);

insert into match values ( next value for seq_matches, 6, 5, 1,0,1,1, {ts '2024-09-05 20:30:00'});
insert into match values ( next value for seq_matches, 10, 7, 2,1,1,1, {ts '2024-09-05 21:20:00'});
insert into match values ( next value for seq_matches, 12, 8, 4,0,1,1, {ts '2024-09-08 20:08:00'});
insert into match values ( next value for seq_matches, 4, 2, 0,2,1,1, {ts '2024-09-10 20:30:00'});
insert into match values ( next value for seq_matches, 1, 11, 0,2,1,1, {ts '2024-09-11 20:30:00'});
insert into match values ( next value for seq_matches, 3, 9, 7,0,1,1, {ts '2024-09-11 21:10:00'});

