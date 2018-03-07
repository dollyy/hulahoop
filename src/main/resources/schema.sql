-- ------------------------------------
-- Database `hulahoop`
-- ------------------------------------
CREATE DATABASE IF NOT EXISTS `hulahoop` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS collections;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS help_info;
DROP TABLE IF EXISTS resource_info;
DROP TABLE IF EXISTS feedback_info;

-- ------------------------------------
-- Table structure for `users`
-- ------------------------------------
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL COMMENT '密码加密',
  `avatar` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `phone` varchar(20) NOT NULL,
  `role` int(1) NOT NULL COMMENT '角色,0:用户,1:管理员',
  `bio` varchar(50) DEFAULT NULL COMMENT '个人简介',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

TRUNCATE TABLE users;

-- ------------------------------------
-- Table structure for `strategies`
-- ------------------------------------
CREATE TABLE `strategies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `name` varchar(20) NOT NULL,
  `city_id` int(11) NOT NULL COMMENT '省份',
  `duration` varchar(10) NOT NULL COMMENT '时长',
  `content` text NOT NULL COMMENT '攻略内容',
  `main_img` varchar(100)  COMMENT '攻略主图在服务器上的路径',
  `for_num` int(4) DEFAULT '0' COMMENT '攻略的支持数',
  `collect_num` int(4) DEFAULT '0' COMMENT '攻略的收藏数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

TRUNCATE TABLE strategies;

INSERT INTO strategies(user_id, name, city_id, duration, content, main_img, for_num, collect_num, create_time, update_time)
VALUES
  (4,'1日游',1,'3天','第一天@#11111ftp://images/a0.jpg#-第二天@#22222#-第三天@#33333ftp://images/a1.jpg#-第四天@#44444','ftp://images/a0.jpg',5,7,sysdate(),sysdate()),
  (2,'2日游',4,'5天','百度@#baidu#-准备@#zhunbeiftp://images/a1.jpg#-开始@#kaishi#-途中@#tuzhong#-回家@#huijia','ftp://images/a1.jpg',8,5,sysdate(),sysdate()),
  (2,'3日游',1,'7天','1天@#11111#-2天@#22222#-3天@#33333','ftp://images/default.jpg',15,1,sysdate(),sysdate()),
  (3,'4五日游',2,'15天','2百度@#2baidu#-2准备@#2zhunbeiftp://images/a2.jpg#-2开始@#2kaishi#-2途中@#2tuzhong#-2回家@#2huijia','ftp://images/a2.jpg',3,15,sysdate(),sysdate()),
  (2,'5日游',1,'3天','3百度@#3baidu#-3准备@#3zhunbei#-3途中@#3tuzhong#-3回家@#3huijia','ftp://images/default.jpg',5,7,sysdate(),sysdate());

INSERT INTO strategies(user_id, name, city_id, duration, content, main_img, for_num, collect_num, create_time, update_time)
VALUES
  (2,'6日游',4,'5天','5百度@#5baidu#-5准备@#5zhunbeiftp://images/a3.jpg#-5开始@#5kaishi#-5途中@#5tuzhong#-5回家@#5huijia','ftp://images/a3.jpg',8,5,sysdate(),sysdate()),
  (3,'7日游',5,'7天','6百度@#6baidu#-6准备@#6zhunbei#-6开始@#6kaishi','ftp://images/default.jpg',15,1,sysdate(),sysdate()),
  (3,'8日游',4,'15天','7百度@#7baidu#-7准备@#7zhunbeiftp://images/a4.jpg#-7开始@#7kaishiftp://images/a5.jpg#-7途中@#7tuzhong#-7回家@#7huijia','ftp://images/a4.jpg',3,15,sysdate(),sysdate()),
  (4,'9日游',7,'3天','8百度@#8baidu#-8准备@#8zhunbei','ftp://images/default.jpg',5,7,sysdate(),sysdate()),
  (2,'10日游',4,'5天','9百度@#9baidu#-9准备@#9zhunbeiftp://images/a6.jpg#-9开始@#9kaishi#-9途中@#9tuzhong#-9回家@#2huijia','ftp://images/a6.jpg',8,5,sysdate(),sysdate());

SELECT * FROM strategies;

SELECT * FROM strategies WHERE user_id=(SELECT users.id FROM users WHERE username LIKE '%哈');

SELECT * FROM strategies WHERE user_id=2;

SELECT s.id, s.name, city_id, c.name FROM strategies s JOIN cities c ON s.city_id=c.id
WHERE user_id = 2;

SELECT count(*) FROM strategies WHERE user_id=2 AND city_id=1;

SELECT s.id, s.content, c.name, s.duration, s.name, s.for_num, s.collect_num, u.username FROM strategies s JOIN cities c ON s.city_id=c.id JOIN users u ON
s.user_id=u.id WHERE s.id=2;

-- ------------------------------------
-- Table structure for `collections`
-- ------------------------------------
CREATE TABLE `collections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

TRUNCATE TABLE collections;

INSERT INTO collections(strategy_id, user_id)
VALUES(1,2),(2,3),(3,4),(4,2),(5,3),(6,2),(7,2),(8,4),(9,2),(10,4),(15,3);

SELECT s.id, u.username, s.name, ci.name as cityName, duration, for_num, collect_num, s.create_time
FROM strategies s JOIN collections co ON s.id=co.strategy_id JOIN users u ON s.user_id=u.id
JOIN cities ci ON s.city_id=ci.id WHERE co.user_id=2;

SELECT s.id, u.username, s.name, ci.name as cityName, duration, for_num, collect_num, s.create_time
FROM strategies s JOIN collections co ON s.id=co.strategy_id JOIN users u ON s.user_id=u.id
  JOIN cities ci ON s.city_id=ci.id WHERE co.user_id=2;


-- ------------------------------------
-- Table structure for `cities`
-- ------------------------------------
CREATE TABLE `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cities(name)
VALUES ('安徽'),('澳门'),('北京'),('重庆'),('福建'),('吉林'),('江苏'),('江西'),('海南'),('河北'),('河南'),('黑龙江'),
  ('湖北'),('湖南'),('甘肃'),('广东'),('广西'),('贵州'),('辽宁'),('南海诸岛'),('内蒙古'),('宁夏'),('青海'),('山东'),
  ('山西'),('陕西'),('上海'),('四川'),('台湾'),('天津'),('西藏'),('香港'),('新疆'),('云南'),('浙江');


-- ------------------------------------
-- Table structure for `comments`
-- ------------------------------------
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `content` varchar(200) NOT NULL COMMENT '评论内容',
  `level` varchar(50) NOT NULL COMMENT '标识唯一评论,值为parent.sequence',
  `parent` varchar(50) NOT NULL COMMENT '这条评论是哪条评论的回复,值为父评论的level',
  `sequence` int(11) NOT NULL COMMENT '同等级评论中的顺序',
  `create_time` datetime NOT NULL COMMENT '评论提交的时间',
  `for_num` int(4) DEFAULT '0' COMMENT '评论的支持数',
  `against_num` int(4) DEFAULT '0' COMMENT '评论的反对数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO comments(user_id, content, level, parent, sequence, create_time)
VALUES
  (1, '今天是几号呀', '1', '0', 1, '2017-12-15 11:11:11'),
  (3, '16号', '2.2', '2', 2, '2017-12-15 11:11:13'),
  (3, '嘿嘿', '2.1.1', '2.1', 1, '2017-12-15 11:11:13'),
  (3, '哈哈', '1.1.1', '1.1', 1, '2017-12-15 11:11:13'),
  (1, '造了', '2.1.2', '2.1', 2, '2017-12-15 11:11:12'),
  (1, '好嘞', '1.1.2', '1.1', 2, '2017-12-15 11:11:12'),
  (3, '12.15', '1.2', '1', 2, '2017-12-15 11:11:13'),
  (1, '明天是几号啊', '2', '0', 2, '2017-12-15 11:11:11'),
  (2, '12.16', '2.1', '2', 1, '2017-12-15 11:11:12'),
  (4, '不造啊', '1.3', '1', 3, '2017-12-15 11:11:14'),
  (2, '15号', '1.1', '1', 1, '2017-12-15 11:11:12'),
  (4, 'emmmm', '2.3', '2', 3, '2017-12-15 11:11:14'),
  (5, 'hhhh不告诉你', '1.4', '1', 4, '2017-12-15 14:50:02');

SELECT count(*) FROM comments WHERE level LIKE '_';

SELECT count(*) FROM comments WHERE level LIKE '1.1._';

SELECT c.id,c.level,c.parent,u.avatar,u.id,u.username,c.content,c.create_time,c.for_num,c.against_num
FROM comments c JOIN users u ON c.user_id=u.id ORDER BY level;

SELECT u.id,u.username FROM comments c JOIN users u ON c.user_id=u.id WHERE level='1.1';

-- ------------------------------------
-- Table structure for `help_info`
-- ------------------------------------
CREATE TABLE `help_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(10) NOT NULL COMMENT '帮助信息的标题',
  `content` text NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ------------------------------------
-- Table structure for `resource_info`
-- ------------------------------------
CREATE TABLE `resource_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL COMMENT '资源的名称',
  `path` varchar(50) NOT NULL COMMENT '资源在服务器上的路径',
  `status` int(1) DEFAULT '1' COMMENT '资源的状态,0:弃用,1:使用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ------------------------------------
-- Table structure for `feedback_info`
-- ------------------------------------
CREATE TABLE `feedback_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `title` varchar(20) NOT NULL COMMENT '反馈信息的题目',
  `content` varchar(500) NOT NULL COMMENT '反馈信息的内容',
  `level` varchar(50) NOT NULL COMMENT '标识唯一反馈信息,值为parent.sequence',
  `parent` int(11) NOT NULL COMMENT '这条反馈信息是哪条反馈信息的回复,值为第一条反馈信息的level',
  `sequence` int(11) NOT NULL COMMENT '同等级反馈信息中的顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

INSERT INTO feedback_info(user_id, title, content, level, parent, sequence, create_time)
VALUES (2,'你好','你好呀',1,0,1,'2018-02-27 12:23:45'),(3,'登录','首次登录',2,0,1,'2018-02-27 12:23:46'),
  (1,'Re: 你好','hi!',1.2,1,2,'2018-02-27 12:23:47'),(1,'Re: 登录','欢迎你',2.2,2,2,'2018-02-27 12:23:48'),
  (2,'哈哈','o(∩∩)o...哈哈',1.3,1,3,'2018-02-27 12:23:49');

SELECT * FROM feedback_info;

-- 列出用户的反馈(只显示反馈信息中的第一条)
SELECT count(*) FROM feedback_info WHERE parent=0;

-- 查看某条反馈的全部信息
SELECT * FROM feedback_info WHERE level LIKE '1%';