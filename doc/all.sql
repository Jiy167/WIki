drop table if exists `test`;
create table test
(
    id int not null comment 'id',
    name varchar(50) null comment 'name',
    password varchar(50) comment 'password',
    primary key (id)
)engine=innodb default charset=utf8mb4 comment='test';

drop table if exists `demo`;
create table demo
(
    id int not null comment 'id',
    name varchar(50) null comment 'name',
    primary key (id)
)engine=innodb default charset=utf8mb4 comment='demo';

show variables like "%time_zone%";