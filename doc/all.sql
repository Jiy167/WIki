drop table if exists `test`;
create table test
(
    id int not null comment 'id',
    name varchar(50) null comment 'name',
    password varchar(50) comment 'password',
    primary key (id)
)engine=innodb default charset=utf8mb4 comment='test';