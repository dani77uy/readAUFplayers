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

create sequence if not exists seq_players start with 1 increment by 1;
create sequence if not exists seq_matches start with 1 increment by 1;

--- INSERT DATA
insert into season
values (1, 2024, 'Divisional D');

insert into team
values (1, 'Academia FC', 'https://auf.org.uy/academia-fc/', 1);
insert into team
values (2, 'Deportivo CEM', 'https://auf.org.uy/deportivo-cem/', 1);
insert into team
values (3, 'Deutscher FK', 'https://auf.org.uy/deutscher-fk/', 1);
insert into team
values (4, 'CA Dilio Sport', 'https://auf.org.uy/club-atletico-dilio-sport/', 1);
insert into team
values (5, 'UD Estudiantes del Plata', 'https://auf.org.uy/estudiantes-del-plata/', 1);
insert into team
values (6, 'CSyD Keguay Toledo', 'https://auf.org.uy/csd-keguay/', 1);
insert into team
values (7, 'CD Melilla SAD', 'https://auf.org.uy/deportivo-melilla-sad/', 1);
insert into team
values (8, 'C Mvd Boca Juniors', 'https://auf.org.uy/club-montevideo-boca-juniors/', 1);
insert into team
values (9, 'CA Nuevo Casabó', 'https://auf.org.uy/club-atletico-nuevo-casabo/', 1);
insert into team
values (10, 'CSyD Paso de la Arena', 'https://auf.org.uy/csyd-paso-de-la-arena/', 1);
insert into team
values (11, 'C Real Montevideo', 'https://auf.org.uy/club-real-montevideo/', 1);
insert into team
values (12, 'Rincón FC', 'https://auf.org.uy/rincon-de-carrasco/', 1);