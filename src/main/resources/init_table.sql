create table if not exists users (id int not null primary key auto_increment,username varchar(100),password varchar(100),status int);

drop table if exists city;
drop table if exists hotel;

create table city (id int primary key auto_increment, name varchar(100), state varchar(100), country varchar(100));

