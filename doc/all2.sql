# ebook
use wikidev;
drop table if exists `ebook`;
create table `ebook` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment '名称',
  `category1_id` bigint comment '分类1',
  `category2_id` bigint comment '分类2',
  `description` varchar(200) comment '描述',
  `cover` varchar(200) comment '封面',
  `doc_count` int not null default 0 comment '文档数',
  `view_count` int not null default 0 comment '阅读数',
  `vote_count` int not null default 0 comment '点赞数likes count',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='电子书';

insert into `ebook` (id, name, description) values (1, 'Spring Boot introductory tutorial', 'Zero-based entry into Java development, the best framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (2, 'Vue introductory tutorial', 'Zero-based entry to Vue development, the best framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (3, 'Python introductory tutorial', 'Zero-based entry into Python development, the best framework for enterprise application development');
insert into `ebook` (id, name, description) values (4, 'Mysql introductory tutorial', 'Zero-based entry into Mysql development, the best preferred framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (5, 'Oracle Getting Started Tutorial', 'Zero Basics for Oracle Development, the Best Preferred Framework for Enterprise Application Development');

drop table if exists `test`;
create table `test` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment '名称',
  `password` varchar(50) comment '密码',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `test` (id, name, password) values (1, '测试', 'password');

# 分类
use wikidev;
drop table if exists `category`;
create table `category` (
  `id` bigint not null comment 'id',
  `parent` bigint not null default 0 comment '父id',
  `name` varchar(50) not null comment '名称',
  `sort` int comment '顺序',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='分类';

insert into `category` (id, parent, name, sort) values (100, 000, 'front-end development', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, 'Basic application', 201);
insert into `category` (id, parent, name, sort) values (202, 200, 'Framework application', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, 'Basic Application', 301);
insert into `category` (id, parent, name, sort) values (302, 300, 'Advanced application', 302);
insert into `category` (id, parent, name, sort) values (400, 000, 'database', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, 'other', 500);
insert into `category` (id, parent, name, sort) values (501, 500, 'server', 501);
insert into `category` (id, parent, name, sort) values (502, 500, 'Development Tools', 502);
insert into `category` (id, parent, name, sort) values (503, 500, 'Popular server-side languages', 503);

-- 文档表
drop table if exists `doc`;
create table `doc` (
  `id` bigint not null comment 'id',
  `ebook_id` bigint not null default 0 comment '电子书id',
  `parent` bigint not null default 0 comment '父id',
  `name` varchar(50) not null comment '名称',
  `sort` int comment '顺序',
  `view_count` int default 0 comment '阅读数',
  `vote_count` int default 0 comment '点赞数',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档';

insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, 'file1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, 'file1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, 'file2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, 'file2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, 'file2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, 'file2.2.1', 1, 0, 0);

-- file content
drop table if exists `content`;
create table `content` (
  `id` bigint not null comment '文档id',
  `content` mediumtext not null comment '内容',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档内容';

-- 用户表
drop table if exists `user`;
create table `user` (
  `id` bigint not null comment 'ID',
  `login_name` varchar(50) not null comment '登陆名',
  `name` varchar(50) comment '昵称',
  `password` char(32) not null comment '密码',
  primary key (`id`),
  unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='用户';

insert into `user` (id, `login_name`, `name`, `password`) values (1, 'test', '测试', 'e70e2222a9d67c4f2eae107533359aa4');

-- 电子书快照表
drop table if exists `ebook_snapshot`;
create table `ebook_snapshot` (
  `id` bigint auto_increment not null comment 'id',
  `ebook_id` bigint not null default 0 comment '电子书id',
  `date` date not null comment '快照日期',
  `view_count` int not null default 0 comment '阅读数',
  `vote_count` int not null default 0 comment '点赞数',
  `view_increase` int not null default 0 comment '阅读增长',
  `vote_increase` int not null default 0 comment '点赞增长',
  primary key (`id`),
  unique key `ebook_id_date_unique` (`ebook_id`, `date`)
) engine=innodb default charset=utf8mb4 comment='电子书快照表';


drop table if exists `demo`;
create table `demo` (
  `id` bigint not null comment 'id',
  `name` varchar(50) comment '名称',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='测试';

insert into `demo` (id, name) values (1, '测试');
