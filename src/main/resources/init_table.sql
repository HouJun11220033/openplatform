create table if not exists users (id int not null primary key auto_increment,username varchar(100),password varchar(100),status int);

drop table if exists city;
drop table if exists hotel;

create table city (id int primary key auto_increment, name varchar(100), state varchar(100), country varchar(100));

DROP TABLE IF EXISTS `appl_user`;
CREATE TABLE `appl_user` (
  `userId` varchar(35) NOT NULL COMMENT '用户ID',
  `userPhone` varchar(11) NOT NULL COMMENT '用户手机号',
  `userEmail` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `password` varchar(16) DEFAULT NULL COMMENT '用户密码',
  `accreditFlag` varchar(1) DEFAULT '0' COMMENT '用户是否授权',
  `registTime` varchar(16) DEFAULT NULL COMMENT '注册时间',
  `updateTime` varchar(16) DEFAULT NULL COMMENT '修改时间'
)  COMMENT='用户表';