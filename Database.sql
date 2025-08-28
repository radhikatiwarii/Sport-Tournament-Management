CREATE DATABASE sport_tournament;

USE sport_tournament;

CREATE TABLE users
(
user_id int auto_increment primary key ,
user_name varchar(50) not null,
email varchar(250) Unique ,
password varchar(250) not null,
phone_no VARCHAR(10) UNIQUE,
role enum('player','coach','organizer','admin') not null,
created_at timestamp Default current_timestamp
);


drop table users;

CREATE TABLE players
(
user_id int Not null,
Player_Id int auto_increment primary key ,
team_id int ,  
address varchar(100),
date_of_birth date not null,
specialization varchar(50) ,                                        -- kis game me acha h
player_status enum('active','injured','retired') default 'active',                 -- weather a player is active,injured or retired:
height decimal(5,2),
weight decimal(5,2),
match_played  int default 0,
goal_scored int default 0,
foreign key(team_id) references teams(team_id),
foreign key(user_id) references  users(user_id),
created_at timestamp Default current_timestamp
);
insert into players
(user_id,player_id,team_id,address,date_of_birth,specialization,player_status,height,weight,match_played,goal_scored)
values
(13,1,1,"kolari","2005-02-03","cricket",'active',138,56,5,2);

drop table players;

CREATE TABLE coaches (
    coach_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    fees DECIMAL(10, 2) not null default 0.00,
    specialization VARCHAR(100) default null ,
    description text null ,
    years_of_experience int default 0 ,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE organizers (
    organizer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_revenue DECIMAL(15, 2) DEFAULT 0.00, -- revenue ka mtlb paise kitne generate hue h 
    events_managed INT DEFAULT 0, -- ek organizer ne kitne event organize kiye
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


create table teams
(
team_id int auto_increment primary key,
team_name varchar(50) not null,
coach_id int,
tournament_id int ,
team_status enum('active','inactive') default 'active',
team_logo varchar(250),
total_championships int default 0,
FOREIGN KEY (coach_id) REFERENCES coaches(coach_id),
FOREIGN KEY (tournament_id) REFERENCES tournaments(tournament_id) on delete set null ,
created_at timestamp Default current_timestamp
);
UPDATE teams
SET team_logo = 'organizer/team1logo.png', -- Replace with the actual logo path or value
    total_championships = 3,     -- Replace with the new number of championships
    created_at = '2025-02-08 12:00:00' -- Replace with the new timestamp
WHERE team_id = 1;  -- Replace with the team ID you want to update
insert into teams
(team_id,team_name,coach_id,tournament_id,team_status,team_logo,total_championships)
values
(1,'unknown',1,1,'active',"kolari","2005-02-03","cricket",'active',138,56,5,2);


drop table teams;

create table tournaments 
(
tournament_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) unique,
    start_date DATE,
    end_date DATE,
    status enum("Open","Ongoing","Coming","Closed") default "Open",
    Max_allowed int,
    organizer_id INT,
    FOREIGN KEY (organizer_id) REFERENCES organizers(organizer_id)
);
ALTER TABLE tournaments 
ADD COLUMN registration_opening_date DATE, 
ADD COLUMN registration_closing_date DATE;

ALTER TABLE tournaments
ADD CONSTRAINT fk_organizer_id
FOREIGN KEY (organizer_id) REFERENCES organizers(organizer_id)
ON DELETE CASCADE
ON UPDATE CASCADE;

drop table tournaments;

CREATE TABLE matches (
    match_id INT AUTO_INCREMENT PRIMARY KEY,
    tournament_id INT,
    team1_id INT,
    team2_id INT,
    match_date DATE,
    result ENUM('Team 1', 'Team 2', 'Draw') NOT NULL,
    winner_team_id INT null,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(tournament_id),
    FOREIGN KEY (team1_id) REFERENCES teams(team_id),
    FOREIGN KEY (team2_id) REFERENCES teams(team_id),
    FOREIGN KEY (winner_team_id) REFERENCES teams(team_id)
);

CREATE TABLE performance (
    performance_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT,
    match_id INT,
    score INT,
    assists INT,
    fouls INT,
    FOREIGN KEY (player_id) REFERENCES players(player_id),
    FOREIGN KEY (match_id) REFERENCES matches(match_id)
);

CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    match_id INT,
    team_id INT,
    message TEXT not null,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (match_id) REFERENCES matches(match_id),
    FOREIGN KEY (team_id) REFERENCES teams(team_id),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


insert into tournaments
(tournament_id,name,start_date,end_date,status,Max_allowed,organizer_id)
values
(1,"Running","2025-02-01","2025-02-02","Coming",10,4),
(2,"handball","2025-02-05","2025-02-07","Coming",16,2),
(3,"kabaddi","2025-02-28","2025-02-03","Coming",16,1),
(4,"volleyball","2025-02-26","2025-02-28","Coming",15,3);

