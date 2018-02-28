-- ------------------------------------
-- Database `hulahoop`
-- ------------------------------------
CREATE DATABASE IF NOT EXISTS `hulahoop` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS user_strategy;
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
  `role` int(1) DEFAULT '0' COMMENT '角色,0:用户,1:管理员',
  `bio` varchar(50) DEFAULT NULL COMMENT '个人简介',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------
-- Table structure for `strategies`
-- ------------------------------------
CREATE TABLE `strategies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `name` varchar(20) NOT NULL,
  `city_id` int(11) NOT NULL COMMENT '省份',
  `duration` varchar(10) NOT NULL COMMENT '时长',
  `content` text NOT NULL,
  `main_img` varchar(500) DEFAULT NULL COMMENT '攻略主图在服务器上的路径',
  `sub_img` text COMMENT '子图片在服务器上的路径,json',
  `for_num` int(4) DEFAULT '0' COMMENT '攻略的支持数',
  `collect_num` int(4) DEFAULT '0' COMMENT '攻略的收藏数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------
-- Table structure for `user_strategy`
-- ------------------------------------
CREATE TABLE `user_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  `city_id` int(11) NOT NULL COMMENT 'cities.id',
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------
-- Table structure for `cities`
-- ------------------------------------
CREATE TABLE `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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



-- ------------------------------------
-- Records of `cities`
-- ------------------------------------
INSERT INTO cities(name)
VALUES ('安徽'),('澳门'),('北京'),('重庆'),('福建'),('吉林'),('江苏'),('江西'),('海南'),('河北'),('河南'),('黑龙江'),
  ('湖北'),('湖南'),('甘肃'),('广东'),('广西'),('贵州'),('辽宁'),('南海诸岛'),('内蒙古'),('宁夏'),('青海'),('山东'),
  ('山西'),('陕西'),('上海'),('四川'),('台湾'),('天津'),('西藏'),('香港'),('新疆'),('云南'),('浙江');

-- ------------------------------------
-- Records of `comments`
-- ------------------------------------
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

-- ------------------------------------
-- Records of `user_strategy`
-- ------------------------------------
INSERT INTO user_strategy(strategy_id, city_id, user_id)
VALUES(1,1,1),(2,23,1),(3,1,1),(4,1,1),(5,18,1),(6,2,1),(7,1,2),(8,8,1),(9,20,1),(10,35,1),(11,12,1),(12,35,1),
  (13,35,1),(14,1,2),(15,34,2),(16,27,1),(17,23,1),(18,8,1),(19,7,2),(20,8,1),(21,1,1),(22,5,1),(23,1,1),(24,1,1),
  (25,1,2),(26,15,1),(27,27,1),(28,1,1),(29,27,1),(30,35,1),(31,9,2),(32,23,1),(33,1,1),(34,6,1),(35,23,1),(36,4,1),
  (37,1,1),(38,11,2),(39,23,1),(40,23,1),(41,15,1),(42,27,1),(43,1,1),(44,23,1),(45,12,1),(46,8,1),(47,3,1),(48,1,1),
  (49,2,1),(50,18,1),(51,23,1),(52,1,1),(53,8,1),(54,1,1),(55,4,1),(56,8,1),(57,8,1),(58,1,1),(59,8,1),(60,1,1);

-- ------------------------------------
-- Records of `feedback_info`
-- ------------------------------------
INSERT INTO feedback_info(user_id, title, content, level, parent, sequence, create_time)
VALUES (2,'你好','你好呀',1,0,1,'2018-02-27 12:23:45'),(3,'登录','首次登录',2,0,1,'2018-02-27 12:23:46'),
  (1,'Re: 你好','hi!',1.2,1,2,'2018-02-27 12:23:47'),(1,'Re: 登录','欢迎你',2.2,2,2,'2018-02-27 12:23:48'),
  (2,'哈哈','o(∩∩)o...哈哈',1.3,1,3,'2018-02-27 12:23:49');

SELECT * FROM feedback_info;

-- 列出用户的反馈(只显示反馈信息中的第一条)
SELECT count(*) FROM feedback_info WHERE parent=0;

-- 查看某条反馈的全部信息
SELECT * FROM feedback_info WHERE level LIKE '1%';