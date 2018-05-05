-- ------------------------------------
-- Database `hulahoop`
-- ------------------------------------
CREATE DATABASE IF NOT EXISTS `hulahoop` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


DROP TABLE IF EXISTS users;
-- ------------------------------------
-- ------------------------------------
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL COMMENT '密码加密',
  `avatar` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `email` varchar(100) NOT NULL,
  `role` int(1) NOT NULL COMMENT '角色,0:用户,1:管理员',
  `bio` varchar(50) DEFAULT NULL COMMENT '个人简介',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `city` varchar(10) DEFAULT NULL COMMENT '城市',
  `recommend` varchar(10) DEFAULT NULL COMMENT '推荐列表',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS strategies;
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

DROP TABLE IF EXISTS collections;
-- ------------------------------------
-- Table structure for `collections`
-- ------------------------------------
CREATE TABLE `collections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS user_behaviours;
-- ------------------------------------
-- Table structure for `user_behaviours`
-- ------------------------------------
CREATE TABLE `user_behaviours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  `preference` decimal(5,1) NOT NULL COMMENT '用户对资源的操作记录评分',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS strategy_for;
-- ------------------------------------
-- Table structure for `strategy_for`
-- ------------------------------------
CREATE TABLE `strategy_for` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS cities;
-- ------------------------------------
-- Table structure for `cities`
-- ------------------------------------
CREATE TABLE `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `area_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cities(id, name, area_id)
VALUES (1,'安徽',1),(2,'澳门',8),(3,'北京',4),(4,'重庆',6),(5,'福建',1),(6,'吉林',7),(7,'江苏',1),(8,'江西',3),(9,'海南',2),(10,'河北',4),
  (11,'河南',3),(12,'黑龙江',7),(13,'湖北',3),(14,'湖南',3),(15,'甘肃',5),(16,'广东',2),(17,'广西',2),(18,'贵州',6),(19,'辽宁',7),
  (20,'南海诸岛',8),(21,'内蒙古',4),(22,'宁夏',5),(23,'青海',5),(24,'山东',1),(25,'山西',4),(26,'陕西',5),(27,'上海',1),(28,'四川',6),
  (29,'台湾',8),(30,'天津',4),(31,'西藏',6),(32,'香港',8),(33,'新疆',5),(34,'云南',6),(35,'浙江',1);


DROP TABLE IF EXISTS comments;
-- ------------------------------------
-- Table structure for `comments`
-- ------------------------------------
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
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


DROP TABLE IF EXISTS help_info;
-- ------------------------------------
-- Table structure for `help_info`
-- ------------------------------------
CREATE TABLE `help_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '帮助信息的标题',
  `content` text NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


TRUNCATE TABLE help_info;

DROP TABLE IF EXISTS feedback_info;
-- ------------------------------------
-- Table structure for `feedback_info`
-- ------------------------------------
CREATE TABLE `feedback_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `send_id` int(11) NOT NULL COMMENT '发送信息的user的users.id',
  `receive_id` int(11) NOT NULL COMMENT '接收信息的user的users.id',
  `content` varchar(500) NOT NULL COMMENT '反馈信息的内容',
  `level` varchar(50) NOT NULL COMMENT '标识唯一反馈信息,值为parent.sequence',
  `parent` int(11) NOT NULL COMMENT '这条反馈信息是哪条反馈信息的回复,值为第一条反馈信息的level',
  `sequence` int(11) NOT NULL COMMENT '同等级反馈信息中的顺序',
  `status` int(11) NOT NULL COMMENT '0:已查看,1:未查看',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS dwr_record;
-- ------------------------------------
-- Table structure for `dwr_record`
-- ------------------------------------
CREATE TABLE dwr_record(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `response_id` int(11) NOT NULL COMMENT '评论人的user_id',
  `request_id` int(11) NOT NULL COMMENT '回复人的user_id',
  `comment_sequence` int(11) NOT NULL COMMENT '祖父评论的sequence',
  `content` varchar(200) NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL COMMENT '评论提交的时间',
  `status` int(11) NOT NULL COMMENT '0:已查看,1:未查看',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

TRUNCATE TABLE dwr_record;

SELECT c.id, c.strategy_id AS strategyId, c.level, c.parent, u.avatar, u.id AS responseId, u.username AS responseName, c.content, DATE_FORMAT(c.create_time, '%Y-%m-%d %T') AS createTime, c.for_num AS forNum, c.against_num AS againstNum FROM comments c JOIN users u ON c.user_id = u.id WHERE c.level LIKE '2.%' OR level = '2' ORDER BY level;


DROP TABLE IF EXISTS user_behaviours;
-- ------------------------------------
-- Table structure for `user_behaviours`
-- ------------------------------------
CREATE TABLE `user_behaviours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT 'users.id',
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  `preference` decimal(5,1) NOT NULL COMMENT '用户对资源的操作记录评分',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS strategy_item;
-- ------------------------------------
-- Table structure for `strategy_item`
-- ------------------------------------
CREATE TABLE `strategy_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) NOT NULL COMMENT 'strategies.id',
  `area_HD` int(11) DEFAULT 0 COMMENT '华东(山东24、江苏7-2、安徽1-3、浙江35-8、福建5-1、上海27-2)   16',
  `area_HN` int(11) DEFAULT 0 COMMENT '华南(广东16-2、广西17-2、海南9-2)                            6',
  `area_HZ` int(11) DEFAULT 0 COMMENT '华中(湖北13-1、湖南14-6、河南11-1、江西8)                     8',
  `area_HB` int(11) DEFAULT 0 COMMENT '华北(北京3-1、天津30、河北10-1、山西25-2、内蒙古21-4)         8',
  `area_XB` int(11) DEFAULT 0 COMMENT '西北(宁夏22-2、新疆33-1、青海23、陕西26-1、甘肃15-2)          6',
  `area_XN` int(11) DEFAULT 0 COMMENT '西南(四川28-5、云南34-1、贵州18-1、西藏31-1、重庆4-2)         10',
  `area_DB` int(11) DEFAULT 0 COMMENT '东北(辽宁19-1、吉林6、黑龙江12)                               1',
  `area_other` int(11) DEFAULT 0 COMMENT '港澳台南海诸岛(台湾29-1、香港32-1、澳门2-3、南海诸岛20)     5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

select * from strategies where city_id=19;

SELECT city_id, count(city_id) FROM strategies GROUP BY city_id;

SELECT duration, count(duration) FROM strategies GROUP BY duration;

SELECT user_id,count(user_id) FROM user_behaviours GROUP BY user_id;


insert into user_behaviours(user_id, strategy_id, preference, create_time, update_time) VALUES
  (1,30,2.5,now(),now()),(1,68,1.5,now(),now()),(1,26,3.0,now(),now()),(6,15,1.0,now(),now()),(6,59,1.0,now(),now()),(7,68,1.0,now(),now()),(7,13,5.0,now(),now()),(7,59,3.5,now(),now()),(7,83,1.0,now(),now()),(7,66,1.5,now(),now()),(2,72,1.0,now(),now()),(2,20,3.5,now(),now()),(2,91,2.0,now(),now()),(2,7,4.5,now(),now()),(2,50,4.0,now(),now()),(2,82,2.5,now(),now()),(2,44,2.5,now(),now()),(1,63,4.5,now(),now()),(1,95,2.5,now(),now()),(1,2,4.5,now(),now()),(1,37,0.5,now(),now()),(1,42,3.5,now(),now()),(1,77,1.5,now(),now()),(1,8,5.0,now(),now()),(1,51,2.0,now(),now()),(1,44,3.0,now(),now()),(1,21,1.0,now(),now()),(1,29,2.0,now(),now()),(1,53,4.5,now(),now()),(1,86,3.0,now(),now()),(1,27,3.5,now(),now()),(4,33,2.5,now(),now()),(4,48,0.5,now(),now()),(4,40,5.0,now(),now()),(4,58,4.0,now(),now()),(4,96,1.5,now(),now()),(4,43,5.0,now(),now()),(4,79,1.5,now(),now()),(1,96,0.5,now(),now()),(1,97,2.5,now(),now()),(1,73,2.5,now(),now()),(2,60,2.5,now(),now()),(2,68,5.0,now(),now()),(2,8,5.0,now(),now()),(6,74,2.5,now(),now()),(6,83,2.0,now(),now()),(6,69,1.0,now(),now()),(6,33,4.5,now(),now()),(6,98,3.5,now(),now()),(6,82,2.0,now(),now()),(6,42,1.0,now(),now()),(2,73,3.5,now(),now()),(2,14,2.5,now(),now()),(2,19,3.5,now(),now()),(2,69,4.0,now(),now()),(2,86,4.5,now(),now()),(2,70,5.0,now(),now()),(2,16,3.0,now(),now()),(2,81,2.0,now(),now()),(3,19,4.5,now(),now()),(3,9,4.5,now(),now()),(3,87,5.0,now(),now()),(3,29,1.0,now(),now()),(3,88,2.0,now(),now()),(3,73,2.5,now(),now()),(3,62,3.5,now(),now()),(3,80,2.0,now(),now()),(3,17,1.5,now(),now()),(3,54,4.5,now(),now()),(3,60,3.5,now(),now()),(3,46,4.0,now(),now()),(3,33,2.5,now(),now()),(3,91,3.0,now(),now()),(3,10,5.0,now(),now()),(3,20,3.0,now(),now()),(3,71,0.5,now(),now()),(3,22,0.5,now(),now()),(4,27,2.0,now(),now()),(4,9,1.5,now(),now()),(4,34,1.5,now(),now()),(2,66,4.5,now(),now()),(2,92,2.0,now(),now()),(2,89,3.0,now(),now()),(2,95,0.5,now(),now()),(2,77,0.5,now(),now()),(2,79,0.5,now(),now()),(3,63,0.5,now(),now()),(4,8,3.5,now(),now()),(4,82,5.0,now(),now()),(4,50,5.0,now(),now()),(4,99,4.5,now(),now()),(4,60,0.5,now(),now()),(4,52,0.5,now(),now()),(4,67,2.0,now(),now()),(4,77,1.0,now(),now()),(4,64,4.5,now(),now()),(5,60,3.5,now(),now()),(5,76,2.5,now(),now()),(5,72,3.0,now(),now()),(5,40,1.5,now(),now()),(5,95,4.0,now(),now()),(5,58,3.5,now(),now()),(5,83,3.5,now(),now()),(5,93,4.5,now(),now()),(5,97,5.0,now(),now()),(5,38,1.0,now(),now()),(5,98,3.0,now(),now()),(5,42,1.5,now(),now()),(6,60,4.0,now(),now()),(4,88,3.5,now(),now()),(4,97,1.5,now(),now()),(4,57,4.0,now(),now()),(4,56,3.0,now(),now()),(4,61,4.5,now(),now()),(4,39,2.0,now(),now()),(4,25,3.0,now(),now()),(6,89,1.0,now(),now()),(6,56,1.5,now(),now()),(6,68,5.0,now(),now()),(6,91,1.5,now(),now()),(6,5,0.5,now(),now()),(6,10,5.0,now(),now()),(6,20,0.5,now(),now()),(6,41,3.5,now(),now()),(6,85,1.0,now(),now()),(6,96,4.5,now(),now()),(6,13,5.0,now(),now()),(6,30,4.5,now(),now()),(6,35,4.0,now(),now()),(5,32,5.0,now(),now()),(5,84,2.5,now(),now()),(5,10,3.0,now(),now()),(5,46,3.5,now(),now()),(5,13,0.5,now(),now()),(5,100,1.5,now(),now()),(5,61,2.5,now(),now()),(7,99,5.0,now(),now()),(7,21,2.0,now(),now()),(7,100,2.5,now(),now()),(7,10,4.5,now(),now()),(7,1,2.5,now(),now()),(7,45,3.0,now(),now()),(7,18,2.5,now(),now()),(7,14,4.5,now(),now()),(7,54,4.0,now(),now()),(7,42,1.5,now(),now()),(7,16,1.5,now(),now()),(7,6,3.0,now(),now()),(7,20,1.0,now(),now()),(7,38,4.5,now(),now()),(6,55,4.5,now(),now()),(6,18,3.0,now(),now()),(6,94,3.0,now(),now()),(6,49,3.5,now(),now()),(6,47,5.0,now(),now()),(6,90,2.0,now(),now()),(6,17,5.0,now(),now()),(8,33,1.5,now(),now()),(8,93,1.5,now(),now()),(8,17,4.5,now(),now()),(8,81,5.0,now(),now()),(8,95,3.5,now(),now()),(8,23,0.5,now(),now()),(9,72,1.5,now(),now()),(7,25,3.5,now(),now()),(7,50,4.0,now(),now()),(7,26,0.5,now(),now()),(7,89,2.0,now(),now()),
  (8,78,1.0,now(),now()),(8,8,4.0,now(),now()),(8,74,4.5,now(),now()),(8,89,4.5,now(),now()),(8,18,4.0,now(),now()),(8,65,4.0,now(),now()),(8,49,4.5,now(),now()),(8,35,3.0,now(),now()),(8,97,2.5,now(),now()),(8,56,2.0,now(),now()),(8,87,1.0,now(),now()),(8,92,1.5,now(),now()),(8,85,4.5,now(),now()),(8,2,1.5,now(),now()),(8,38,3.5,now(),now()),(8,54,3.0,now(),now()),(8,64,4.0,now(),now()),(9,66,1.0,now(),now()),(9,44,2.0,now(),now()),(9,59,3.0,now(),now()),(9,16,2.5,now(),now()),(9,80,5.0,now(),now()),(9,12,4.5,now(),now()),(9,94,5.0,now(),now()),(9,91,0.5,now(),now()),(9,99,1.5,now(),now()),(9,33,5.0,now(),now()),(9,8,1.0,now(),now()),(9,40,4.5,now(),now()),(9,87,0.5,now(),now()),(9,54,1.0,now(),now()),(9,3,2.5,now(),now()),(9,38,1.5,now(),now()),(9,39,1.5,now(),now()),(9,10,1.5,now(),now()),(9,68,5.0,now(),now()),(9,32,5.0,now(),now()),(9,76,1.5,now(),now()),(10,99,3.0,now(),now()),(10,35,3.0,now(),now()),(10,29,3.0,now(),now()),(10,11,2.0,now(),now()),(10,37,5.0,now(),now()),(10,60,4.0,now(),now()),(10,71,4.5,now(),now()),(10,77,2.0,now(),now()),(10,50,0.5,now(),now()),(10,66,4.0,now(),now()),(10,7,0.5,now(),now()),(10,69,1.0,now(),now()),(10,74,5.0,now(),now()),(10,87,5.0,now(),now()),(10,43,2.0,now(),now()),(10,48,4.5,now(),now()),(10,8,1.5,now(),now()),(10,68,3.0,now(),now()),(10,32,1.5,now(),now()),(10,9,3.0,now(),now()),(10,15,3.5,now(),now()),(11,27,3.5,now(),now()),(11,86,2.0,now(),now()),(11,79,1.5,now(),now()),(11,18,2.0,now(),now()),(11,7,5.0,now(),now()),(11,56,2.5,now(),now()),(11,39,4.0,now(),now()),(11,99,3.0,now(),now()),(11,24,4.0,now(),now()),(11,70,1.5,now(),now()),(11,41,4.0,now(),now()),(11,65,1.0,now(),now()),(11,45,3.5,now(),now()),(11,32,1.5,now(),now()),(11,51,4.0,now(),now()),(11,64,4.0,now(),now()),(11,91,4.5,now(),now()),(11,77,4.5,now(),now()),(11,12,0.5,now(),now()),(12,7,1.0,now(),now()),(12,65,4.5,now(),now()),(12,9,0.5,now(),now()),(12,89,1.0,now(),now()),(12,98,1.0,now(),now()),(12,90,2.5,now(),now()),(12,80,5.0,now(),now()),(12,46,3.0,now(),now()),(12,16,0.5,now(),now()),(12,36,3.0,now(),now()),(12,52,3.5,now(),now()),(12,100,2.0,now(),now()),(12,14,3.0,now(),now()),(12,19,3.0,now(),now()),(12,32,2.5,now(),now()),(12,33,5.0,now(),now()),(12,27,1.0,now(),now()),(12,86,3.5,now(),now()),(12,34,1.5,now(),now()),(12,38,3.0,now(),now()),(12,64,4.5,now(),now()),(12,44,0.5,now(),now()),(12,93,4.5,now(),now()),(12,96,4.0,now(),now()),(12,83,4.0,now(),now()),(13,22,4.5,now(),now()),(13,28,4.0,now(),now()),(13,52,4.5,now(),now()),(13,17,2.0,now(),now()),(13,31,1.5,now(),now()),(13,80,4.5,now(),now()),(13,76,4.0,now(),now()),(13,69,2.0,now(),now()),(13,49,5.0,now(),now()),(13,58,0.5,now(),now()),(13,68,1.0,now(),now()),(13,92,1.5,now(),now()),(13,39,3.5,now(),now()),(13,48,4.0,now(),now()),(13,33,2.0,now(),now()),(13,3,3.5,now(),now()),(13,82,2.0,now(),now()),(13,26,2.0,now(),now()),(13,34,3.5,now(),now()),(13,1,3.0,now(),now()),(13,12,2.5,now(),now()),(13,93,0.5,now(),now()),(13,90,3.5,now(),now()),(13,61,1.0,now(),now()),(13,2,5.0,now(),now()),(13,41,4.0,now(),now()),
  (14,89,4.0,now(),now()),(14,41,1.5,now(),now()),(14,4,4.0,now(),now()),(14,29,3.0,now(),now()),(14,10,4.0,now(),now()),(14,99,2.5,now(),now()),(14,62,2.0,now(),now()),(14,96,4.5,now(),now()),(14,68,1.5,now(),now()),(14,53,4.5,now(),now()),(14,22,2.5,now(),now()),(14,63,0.5,now(),now()),(14,64,4.0,now(),now()),(14,36,5.0,now(),now()),(14,17,0.5,now(),now()),(14,26,4.5,now(),now()),(14,79,3.5,now(),now()),(14,20,0.5,now(),now()),(14,3,4.5,now(),now()),(14,12,4.5,now(),now()),(14,82,3.5,now(),now()),(14,90,1.5,now(),now()),(15,72,5.0,now(),now()),(15,85,4.5,now(),now()),(15,9,2.5,now(),now()),(15,46,1.5,now(),now()),(15,33,2.0,now(),now()),(15,47,1.5,now(),now()),(15,11,0.5,now(),now()),(15,93,1.5,now(),now()),(15,17,1.0,now(),now()),(15,25,2.5,now(),now()),(15,27,4.0,now(),now()),(15,20,3.0,now(),now()),(15,58,1.5,now(),now()),(15,60,1.0,now(),now()),(15,100,0.5,now(),now()),(15,39,2.0,now(),now()),(15,61,3.5,now(),now()),(15,50,1.5,now(),now()),(15,7,3.0,now(),now()),(15,38,3.0,now(),now()),(15,99,2.0,now(),now()),(15,21,2.0,now(),now()),(15,6,4.0,now(),now()),(15,41,0.5,now(),now()),(15,18,3.5,now(),now()),(16,43,5.0,now(),now()),(16,14,1.5,now(),now()),(16,27,3.0,now(),now()),(16,76,2.0,now(),now()),(16,25,4.0,now(),now()),(16,63,3.5,now(),now()),(16,56,2.5,now(),now()),(16,11,5.0,now(),now()),(16,8,4.0,now(),now()),(16,58,4.5,now(),now()),(16,60,1.0,now(),now()),(16,35,0.5,now(),now()),(16,46,3.0,now(),now()),(16,75,0.5,now(),now()),(16,44,2.5,now(),now()),(16,53,3.0,now(),now()),(16,3,1.5,now(),now()),(16,37,5.0,now(),now()),(16,92,3.0,now(),now()),(16,70,1.0,now(),now()),(16,31,0.5,now(),now()),(16,1,2.5,now(),now()),(16,74,2.5,now(),now()),(16,41,4.5,now(),now()),(16,86,4.0,now(),now()),(16,16,1.0,now(),now()),(16,19,2.0,now(),now()),(16,55,2.0,now(),now()),(16,40,5.0,now(),now()),(17,7,0.5,now(),now()),(17,30,3.0,now(),now()),(17,28,1.0,now(),now()),(17,44,0.5,now(),now()),(17,21,1.5,now(),now()),(17,41,5.0,now(),now()),(17,93,0.5,now(),now()),(17,35,1.5,now(),now()),(17,26,1.0,now(),now()),(17,59,4.0,now(),now()),(17,94,4.5,now(),now()),(17,67,4.0,now(),now()),(17,18,3.5,now(),now()),(17,85,4.5,now(),now()),(17,5,3.5,now(),now()),(17,97,0.5,now(),now()),(17,65,2.5,now(),now()),(17,17,3.5,now(),now()),(17,73,1.5,now(),now()),(17,56,5.0,now(),now()),(17,49,1.0,now(),now()),(17,63,2.5,now(),now()),(17,36,4.5,now(),now()),(17,6,3.5,now(),now()),(17,40,4.0,now(),now()),(17,68,3.0,now(),now()),(18,50,3.0,now(),now()),(18,68,4.0,now(),now()),(18,43,2.0,now(),now()),(18,69,2.0,now(),now()),(18,92,1.5,now(),now()),(18,19,2.0,now(),now()),(18,48,4.0,now(),now()),(18,40,5.0,now(),now()),(18,28,5.0,now(),now()),(18,74,2.5,now(),now()),(18,2,3.0,now(),now()),(18,76,3.0,now(),now()),(18,57,2.5,now(),now()),(18,94,3.5,now(),now()),(18,36,3.5,now(),now()),(18,85,2.5,now(),now()),(18,16,2.5,now(),now()),(18,55,2.0,now(),now()),(18,67,5.0,now(),now()),(18,23,1.0,now(),now()),(18,53,3.0,now(),now()),(18,3,4.0,now(),now()),(18,12,4.0,now(),now()),(18,96,1.5,now(),now()),(18,88,3.5,now(),now()),(18,46,4.0,now(),now()),(18,17,3.5,now(),now()),(18,61,2.5,now(),now()),(18,1,0.5,now(),now()),(18,26,1.5,now(),now()),(18,51,5.0,now(),now()),(18,8,3.0,now(),now()),(18,95,1.0,now(),now()),(18,9,4.0,now(),now()),(18,99,0.5,now(),now()),(9,84,0.5,now(),now()),(9,9,2.0,now(),now()),(9,45,1.0,now(),now()),(9,77,3.5,now(),now()),(10,54,2.5,now(),now()),(10,23,3.0,now(),now()),(10,76,4.0,now(),now()),(19,91,4.0,now(),now()),(19,40,5.0,now(),now()),(19,46,0.5,now(),now()),(19,50,3.5,now(),now()),(19,63,2.0,now(),now()),(19,35,4.5,now(),now()),(19,15,1.0,now(),now()),(19,29,1.0,now(),now()),(19,42,4.0,now(),now()),(19,24,4.5,now(),now()),(19,39,3.5,now(),now()),(19,73,2.0,now(),now()),(19,32,3.5,now(),now()),(19,20,5.0,now(),now()),(19,44,2.0,now(),now()),(19,78,2.5,now(),now()),(19,23,2.0,now(),now()),(19,2,5.0,now(),now()),(19,36,3.0,now(),now()),(19,21,5.0,now(),now()),(19,70,5.0,now(),now()),(19,37,1.0,now(),now()),(19,65,3.5,now(),now()),(19,88,4.0,now(),now()),(19,71,2.5,now(),now()),(20,31,3.5,now(),now()),(20,87,2.0,now(),now()),(20,18,5.0,now(),now()),(20,97,1.0,now(),now()),(20,8,3.0,now(),now()),(20,64,0.5,now(),now()),(20,47,3.5,now(),now()),(20,23,2.0,now(),now()),(20,3,4.5,now(),now()),(20,25,4.5,now(),now()),(20,50,3.5,now(),now()),(20,100,5.0,now(),now()),(20,24,3.0,now(),now()),(20,26,0.5,now(),now()),(20,38,2.0,now(),now()),(20,91,1.0,now(),now()),(20,28,2.5,now(),now()),(20,46,3.5,now(),now()),(20,77,4.0,now(),now()),(20,13,1.0,now(),now()),(20,9,3.0,now(),now()),(20,96,4.0,now(),now()),(20,43,3.0,now(),now()),(20,27,4.0,now(),now()),(20,65,4.0,now(),now()),(20,17,3.0,now(),now()),(20,15,5.0,now(),now()),(20,20,1.5,now(),now()),(20,39,4.0,now(),now()),(20,54,1.0,now(),now()),(20,57,5.0,now(),now()),(20,48,4.0,now(),now()),(20,37,2.0,now(),now()),(21,3,1.5,now(),now()),(21,47,2.0,now(),now()),(21,59,2.5,now(),now()),(21,55,3.0,now(),now()),(21,62,2.0,now(),now()),(21,13,0.5,now(),now()),(21,9,4.5,now(),now()),(21,76,4.0,now(),now()),(21,99,4.5,now(),now()),(21,98,1.0,now(),now()),(21,73,3.5,now(),now()),(21,22,3.0,now(),now()),(21,14,5.0,now(),now()),(21,19,2.0,now(),now()),(21,89,4.5,now(),now()),(21,41,0.5,now(),now()),(21,35,1.0,now(),now()),(21,25,5.0,now(),now()),(21,5,4.5,now(),now()),(21,42,0.5,now(),now()),(21,24,5.0,now(),now()),(22,54,1.5,now(),now()),(22,33,2.5,now(),now()),(22,39,1.0,now(),now()),(22,88,1.5,now(),now()),(22,59,2.5,now(),now()),(22,97,0.5,now(),now()),(22,18,5.0,now(),now()),(22,82,1.0,now(),now()),(22,93,0.5,now(),now()),(22,81,2.5,now(),now()),(22,26,1.5,now(),now()),(22,91,3.5,now(),now()),(22,13,5.0,now(),now()),(22,43,5.0,now(),now()),(22,36,2.5,now(),now()),(22,56,3.0,now(),now()),(22,35,1.5,now(),now()),(22,5,2.5,now(),now()),(22,7,5.0,now(),now()),(22,68,2.5,now(),now()),(22,89,3.0,now(),now()),(22,60,2.0,now(),now()),(22,70,2.0,now(),now()),(22,42,0.5,now(),now()),(23,39,3.0,now(),now()),(23,53,4.5,now(),now()),(23,79,5.0,now(),now()),(23,18,1.5,now(),now()),(23,88,1.0,now(),now()),(23,42,2.5,now(),now()),(23,61,3.5,now(),now()),(23,51,4.0,now(),now()),(23,9,1.5,now(),now()),(23,24,0.5,now(),now()),(23,74,2.5,now(),now()),(23,4,0.5,now(),now()),(23,40,0.5,now(),now()),(23,96,1.5,now(),now()),(23,67,3.5,now(),now()),(23,70,4.5,now(),now()),(23,10,5.0,now(),now()),(23,97,2.5,now(),now()),(23,73,1.0,now(),now()),(23,54,4.0,now(),now()),(23,87,2.5,now(),now()),(24,17,0.5,now(),now()),(24,57,4.5,now(),now()),(24,44,0.5,now(),now()),(24,49,0.5,now(),now()),(24,81,2.5,now(),now()),(24,38,1.0,now(),now()),(24,41,4.0,now(),now()),(24,54,2.5,now(),now()),(24,74,3.0,now(),now()),(24,100,3.0,now(),now()),(24,1,3.5,now(),now()),(24,82,1.5,now(),now()),(24,51,0.5,now(),now()),(24,76,5.0,now(),now()),(24,94,2.5,now(),now()),(24,58,0.5,now(),now()),(24,21,4.0,now(),now()),(24,26,2.5,now(),now()),(24,34,2.5,now(),now()),(25,29,3.5,now(),now()),(25,19,1.5,now(),now()),(25,35,1.0,now(),now()),(25,41,3.0,now(),now()),(25,68,2.0,now(),now()),(25,98,5.0,now(),now()),(25,91,4.5,now(),now()),(25,23,2.0,now(),now()),(25,48,0.5,now(),now()),(25,3,0.5,now(),now()),(25,9,5.0,now(),now()),(25,17,4.5,now(),now()),(25,93,4.0,now(),now()),(25,4,2.5,now(),now()),(25,55,4.0,now(),now()),(25,24,3.0,now(),now()),(25,32,4.0,now(),now()),(25,25,2.0,now(),now()),(25,100,3.5,now(),now()),(25,20,0.5,now(),now()),(25,14,2.5,now(),now()),(25,46,3.5,now(),now()),(25,18,2.0,now(),now()),(25,87,4.0,now(),now()),(26,3,4.0,now(),now()),(26,6,0.5,now(),now()),(26,5,1.0,now(),now()),(26,38,2.5,now(),now()),(26,69,1.5,now(),now()),(26,35,3.5,now(),now()),(26,76,4.0,now(),now()),(26,86,2.0,now(),now()),(26,39,5.0,now(),now()),(26,45,1.5,now(),now()),(26,27,1.0,now(),now()),(26,96,2.0,now(),now()),(26,67,5.0,now(),now()),(26,80,0.5,now(),now()),(26,73,2.0,now(),now()),(26,87,5.0,now(),now()),(26,18,0.5,now(),now()),(26,43,1.5,now(),now()),(26,92,2.5,now(),now()),(26,21,4.5,now(),now()),(26,26,0.5,now(),now()),(33,42,5.0,now(),now()),(33,16,1.0,now(),now()),(33,77,1.0,now(),now()),(33,100,2.0,now(),now()),(33,1,4.5,now(),now()),(33,69,3.0,now(),now()),(33,71,5.0,now(),now()),(26,99,3.0,now(),now()),(26,95,0.5,now(),now()),(26,16,3.5,now(),now()),(26,54,2.0,now(),now()),(26,53,3.5,now(),now()),(27,62,5.0,now(),now()),(27,14,2.0,now(),now()),(27,48,0.5,now(),now()),(27,100,1.0,now(),now()),(27,92,3.5,now(),now()),(27,12,2.0,now(),now()),(27,46,1.0,now(),now()),(27,73,4.0,now(),now()),(27,65,4.0,now(),now()),(27,99,2.5,now(),now()),(27,35,3.5,now(),now()),(27,15,5.0,now(),now()),(27,93,1.0,now(),now()),(28,31,1.5,now(),now()),(28,51,4.5,now(),now()),(28,68,0.5,now(),now()),(28,49,1.0,now(),now()),(28,8,2.0,now(),now()),(28,9,5.0,now(),now()),(28,54,3.5,now(),now()),(28,65,0.5,now(),now()),(28,95,3.0,now(),now()),(28,58,4.0,now(),now()),(28,59,4.5,now(),now()),(28,90,5.0,now(),now()),(28,86,2.0,now(),now()),(28,67,0.5,now(),now()),(28,70,5.0,now(),now()),(27,85,5.0,now(),now()),(27,39,2.0,now(),now()),(27,60,1.0,now(),now()),(27,20,0.5,now(),now()),(27,18,2.0,now(),now()),(27,87,1.5,now(),now()),(27,70,3.5,now(),now()),(28,73,0.5,now(),now()),(28,25,0.5,now(),now()),(28,22,5.0,now(),now()),(28,87,4.0,now(),now()),(28,48,2.0,now(),now()),(28,52,0.5,now(),now()),(29,4,5.0,now(),now()),(29,20,2.0,now(),now()),(29,81,5.0,now(),now()),(29,97,1.5,now(),now()),(29,22,0.5,now(),now()),(29,80,4.0,now(),now()),(29,19,1.0,now(),now()),(29,55,0.5,now(),now()),(29,39,2.5,now(),now()),(29,8,5.0,now(),now()),(29,82,3.0,now(),now()),(29,64,4.5,now(),now()),(29,30,5.0,now(),now()),(29,16,0.5,now(),now()),(29,67,2.5,now(),now()),(29,28,0.5,now(),now()),(29,70,2.5,now(),now()),(29,68,2.0,now(),now()),(29,35,1.0,now(),now()),(29,45,1.0,now(),now()),(30,80,1.5,now(),now()),(30,13,3.0,now(),now()),(30,74,2.5,now(),now()),(30,64,4.0,now(),now()),(30,3,0.5,now(),now()),(30,4,2.0,now(),now()),(30,19,3.5,now(),now()),(30,78,2.0,now(),now()),(30,21,4.0,now(),now()),(30,1,1.0,now(),now()),(30,6,4.5,now(),now()),(30,47,0.5,now(),now()),(30,54,3.0,now(),now()),(30,85,1.0,now(),now()),(30,96,1.0,now(),now()),(30,51,3.0,now(),now()),(30,23,5.0,now(),now()),(30,41,4.5,now(),now()),(30,94,1.5,now(),now()),(30,8,4.5,now(),now()),(30,81,3.0,now(),now()),(30,5,3.5,now(),now()),(30,18,4.5,now(),now()),(31,98,1.5,now(),now()),(31,11,1.0,now(),now()),(31,74,0.5,now(),now()),(31,76,4.5,now(),now()),(31,54,2.5,now(),now()),(31,68,2.5,now(),now()),(31,75,3.5,now(),now()),(31,86,0.5,now(),now()),(31,14,0.5,now(),now()),(31,8,1.5,now(),now()),(31,79,4.5,now(),now()),(31,72,1.0,now(),now()),(31,26,4.0,now(),now()),(31,59,3.5,now(),now()),(31,6,3.5,now(),now()),(31,89,3.5,now(),now()),(31,24,4.5,now(),now()),(31,87,0.5,now(),now()),(32,75,1.0,now(),now()),(32,18,5.0,now(),now()),(32,1,2.5,now(),now()),(32,55,4.0,now(),now()),(32,74,2.0,now(),now()),(32,87,3.5,now(),now()),(32,95,0.5,now(),now()),(31,100,5.0,now(),now()),(31,52,1.0,now(),now()),(31,49,5.0,now(),now()),(31,82,5.0,now(),now()),(31,85,2.0,now(),now()),(32,49,3.0,now(),now()),(32,90,4.0,now(),now()),(32,34,4.0,now(),now()),(32,58,0.5,now(),now()),(32,17,3.0,now(),now()),(32,14,2.5,now(),now()),(32,77,3.0,now(),now()),(32,76,4.5,now(),now()),(32,35,2.5,now(),now()),(32,52,1.0,now(),now()),(32,41,3.0,now(),now()),(32,12,1.0,now(),now()),(32,97,1.0,now(),now()),(32,11,2.5,now(),now()),(32,56,2.5,now(),now()),(33,26,4.0,now(),now()),(33,32,1.5,now(),now()),(33,80,2.5,now(),now()),(33,63,1.0,now(),now()),(33,11,1.0,now(),now()),(33,24,2.5,now(),now()),(33,62,2.0,now(),now()),(33,91,5.0,now(),now()),(33,48,1.5,now(),now()),(33,27,4.0,now(),now()),(33,37,1.5,now(),now()),(33,2,4.5,now(),now()),(33,19,0.5,now(),now()),(33,17,1.5,now(),now()),(33,25,4.5,now(),now()),(33,95,0.5,now(),now()),(33,83,3.0,now(),now()),(33,93,1.5,now(),now()),(33,99,3.0,now(),now()),(34,24,1.5,now(),now()),(34,10,4.5,now(),now()),(34,63,4.0,now(),now()),(34,64,1.5,now(),now()),(34,25,5.0,now(),now()),(34,59,3.5,now(),now()),(34,45,2.0,now(),now()),(34,22,4.5,now(),now()),(34,13,3.0,now(),now()),(34,92,3.5,now(),now()),(34,82,2.5,now(),now()),(34,81,2.5,now(),now()),(34,57,5.0,now(),now()),(34,65,1.0,now(),now()),(34,32,3.0,now(),now()),(34,58,2.0,now(),now()),(34,26,3.5,now(),now()),(34,34,1.5,now(),now()),(34,97,2.0,now(),now()),(34,50,3.5,now(),now()),(34,20,1.0,now(),now()),(34,1,1.5,now(),now()),(34,83,4.0,now(),now()),(34,16,1.0,now(),now()),
  (35,96,3.0,now(),now()),(35,75,1.0,now(),now()),(35,2,1.5,now(),now()),(35,55,1.0,now(),now()),(35,67,2.0,now(),now()),(35,19,5.0,now(),now()),(35,39,0.5,now(),now()),(35,27,4.5,now(),now()),(35,61,1.5,now(),now()),(35,79,1.5,now(),now()),(35,18,5.0,now(),now()),(35,7,1.0,now(),now()),(35,50,1.0,now(),now()),(35,70,2.0,now(),now()),(35,21,2.0,now(),now()),(35,12,4.5,now(),now()),(35,89,3.5,now(),now()),(35,94,0.5,now(),now()),(35,40,3.5,now(),now()),(35,4,3.5,now(),now()),(35,68,5.0,now(),now()),(36,100,2.5,now(),now()),(36,18,3.0,now(),now()),(36,64,2.0,now(),now()),(36,4,3.0,now(),now()),(36,26,4.0,now(),now()),(36,74,3.5,now(),now()),(36,85,3.0,now(),now()),(36,59,1.5,now(),now()),(36,50,0.5,now(),now()),(36,31,1.5,now(),now()),(36,63,3.5,now(),now()),(36,83,3.0,now(),now()),(36,86,2.5,now(),now()),(36,93,1.0,now(),now()),(36,27,3.0,now(),now()),(36,69,4.5,now(),now()),(36,19,5.0,now(),now()),(36,91,2.0,now(),now()),(36,36,1.5,now(),now()),(36,5,1.5,now(),now()),(37,98,2.0,now(),now()),(37,61,2.0,now(),now()),(37,79,1.5,now(),now()),(37,71,4.5,now(),now()),(37,23,0.5,now(),now()),(37,32,1.5,now(),now()),(37,6,5.0,now(),now()),(37,75,3.0,now(),now()),(37,65,2.0,now(),now()),(37,5,2.5,now(),now()),(37,4,3.0,now(),now()),(37,89,5.0,now(),now()),(37,47,4.5,now(),now()),(37,82,2.5,now(),now()),(37,8,4.5,now(),now()),(37,29,3.0,now(),now()),(37,48,3.5,now(),now()),(37,9,5.0,now(),now()),(37,62,3.0,now(),now()),(37,13,1.0,now(),now()),(37,74,1.0,now(),now()),(37,55,2.5,now(),now()),(37,95,1.0,now(),now()),(38,4,1.5,now(),now()),(38,96,0.5,now(),now()),(38,37,1.5,now(),now()),(38,5,2.0,now(),now()),(38,16,4.0,now(),now()),(38,49,4.5,now(),now()),(38,55,1.5,now(),now()),(38,18,2.5,now(),now()),(38,27,1.0,now(),now()),(38,59,1.0,now(),now()),(38,25,4.0,now(),now()),(38,6,4.5,now(),now()),(38,8,0.5,now(),now()),(38,42,3.5,now(),now()),(38,30,5.0,now(),now()),(38,15,5.0,now(),now()),(38,60,4.5,now(),now()),(38,45,1.0,now(),now()),(38,24,4.5,now(),now()),(38,68,3.0,now(),now()),(38,46,3.0,now(),now()),(38,81,4.0,now(),now()),(39,97,3.0,now(),now()),(39,47,2.0,now(),now()),(39,61,1.0,now(),now()),(39,88,4.0,now(),now()),(39,39,4.0,now(),now()),(42,100,2.0,now(),now()),(42,63,0.5,now(),now()),(42,45,1.5,now(),now()),(42,2,4.0,now(),now()),(42,71,3.5,now(),now()),(42,16,4.0,now(),now()),(42,25,0.5,now(),now()),(39,83,0.5,now(),now()),(39,3,0.5,now(),now()),(39,8,1.0,now(),now()),(39,9,1.5,now(),now()),(39,77,2.5,now(),now()),(39,31,2.0,now(),now()),(39,43,0.5,now(),now()),(39,19,2.0,now(),now()),(39,72,4.5,now(),now()),(39,53,2.5,now(),now()),(39,96,4.5,now(),now()),(39,57,0.5,now(),now()),(39,2,1.5,now(),now()),(39,65,0.5,now(),now()),(39,79,3.5,now(),now()),(39,6,1.5,now(),now()),(39,22,1.5,now(),now()),(39,84,0.5,now(),now()),(39,50,3.0,now(),now()),(40,90,1.0,now(),now()),(40,68,3.0,now(),now()),(40,75,2.5,now(),now()),(40,73,1.5,now(),now()),(40,5,1.5,now(),now()),(40,22,1.0,now(),now()),(40,40,4.0,now(),now()),(40,62,2.5,now(),now()),(40,96,2.5,now(),now()),(40,11,2.5,now(),now()),(40,36,1.5,now(),now()),(40,93,0.5,now(),now()),(40,98,1.0,now(),now()),(40,53,1.5,now(),now()),(40,77,2.0,now(),now()),(40,66,5.0,now(),now()),(40,58,3.0,now(),now()),(40,23,0.5,now(),now()),(40,81,0.5,now(),now()),(40,71,2.0,now(),now()),(40,17,1.0,now(),now()),(40,4,1.0,now(),now()),(40,48,0.5,now(),now()),(41,9,0.5,now(),now()),(41,72,1.0,now(),now()),(41,73,2.0,now(),now()),(41,36,3.5,now(),now()),(41,15,5.0,now(),now()),(41,23,2.5,now(),now()),(41,27,3.5,now(),now()),(41,91,4.0,now(),now()),(41,28,2.0,now(),now()),(41,67,5.0,now(),now()),(41,83,4.5,now(),now()),(41,1,5.0,now(),now()),(41,19,5.0,now(),now()),(41,84,4.0,now(),now()),(41,90,5.0,now(),now()),(41,70,4.5,now(),now()),(41,80,2.0,now(),now()),(41,89,0.5,now(),now()),(41,37,3.0,now(),now()),(41,43,4.0,now(),now()),(41,31,1.0,now(),now()),(41,17,3.5,now(),now()),(41,95,1.0,now(),now()),(41,87,5.0,now(),now()),(41,68,1.5,now(),now()),(41,10,2.0,now(),now()),(41,74,2.0,now(),now()),(41,35,2.0,now(),now()),(41,48,3.0,now(),now()),(41,21,0.5,now(),now()),(41,47,1.0,now(),now()),(41,100,1.0,now(),now()),(42,53,1.5,now(),now()),(42,94,1.0,now(),now()),(42,77,3.5,now(),now()),(42,85,2.5,now(),now()),(42,15,1.0,now(),now()),(42,59,3.0,now(),now()),(42,36,5.0,now(),now()),(42,12,5.0,now(),now()),(42,46,3.5,now(),now()),(42,98,1.5,now(),now()),(42,28,3.5,now(),now()),(42,62,3.5,now(),now()),(42,31,3.5,now(),now()),(42,6,4.5,now(),now()),(42,41,3.0,now(),now()),(43,27,5.0,now(),now()),(43,97,2.0,now(),now()),(43,67,3.0,now(),now()),(43,12,2.0,now(),now()),(43,53,0.5,now(),now()),(43,89,4.0,now(),now()),(43,34,0.5,now(),now()),(43,29,3.5,now(),now()),(43,24,2.0,now(),now()),(43,62,0.5,now(),now()),(43,88,2.5,now(),now()),(43,14,3.5,now(),now()),(43,69,2.0,now(),now()),(43,87,4.5,now(),now()),(43,4,3.0,now(),now()),(43,55,1.0,now(),now()),(43,45,4.5,now(),now()),(43,33,3.5,now(),now()),(43,93,5.0,now(),now()),(43,76,2.5,now(),now()),(43,10,3.0,now(),now()),(43,58,3.5,now(),now()),(44,43,0.5,now(),now()),(44,48,0.5,now(),now()),(44,38,4.0,now(),now()),(44,50,3.5,now(),now()),(44,14,4.0,now(),now()),(44,45,4.5,now(),now()),(44,33,1.5,now(),now()),(44,87,2.0,now(),now()),(44,100,1.5,now(),now()),(44,62,5.0,now(),now()),(44,97,3.5,now(),now()),(44,35,4.0,now(),now()),(44,9,4.0,now(),now()),(44,36,5.0,now(),now()),(44,30,2.0,now(),now()),(44,34,4.5,now(),now()),(44,85,1.0,now(),now()),(44,52,1.0,now(),now()),(44,92,4.0,now(),now()),(44,15,2.0,now(),now()),(45,85,3.5,now(),now()),(45,35,2.0,now(),now()),(45,59,1.0,now(),now()),(45,64,2.5,now(),now()),(45,34,3.0,now(),now()),(45,63,3.5,now(),now()),(45,18,2.5,now(),now()),(45,86,3.0,now(),now()),(45,14,3.5,now(),now()),(45,95,3.0,now(),now()),(45,87,1.5,now(),now()),(45,36,3.0,now(),now()),(45,46,5.0,now(),now()),(45,31,2.5,now(),now()),(45,97,4.5,now(),now()),(45,92,1.0,now(),now()),(45,20,0.5,now(),now()),(45,15,2.0,now(),now()),(45,9,0.5,now(),now()),(46,17,4.5,now(),now()),(46,35,2.0,now(),now()),(46,40,5.0,now(),now()),(46,13,3.0,now(),now()),(46,33,4.5,now(),now()),(46,47,2.5,now(),now()),(46,67,5.0,now(),now()),(46,74,5.0,now(),now()),(46,70,4.5,now(),now()),(46,19,3.5,now(),now()),(46,80,1.0,now(),now()),(46,73,3.0,now(),now()),(46,99,2.0,now(),now()),(46,6,2.5,now(),now()),(46,44,2.5,now(),now()),(46,60,1.0,now(),now()),(46,52,3.0,now(),now()),(46,28,2.0,now(),now()),(46,11,0.5,now(),now()),(46,4,1.0,now(),now()),(46,59,1.5,now(),now()),(46,38,4.0,now(),now()),(47,1,3.0,now(),now()),(47,42,4.0,now(),now()),(47,27,4.0,now(),now()),(47,87,1.5,now(),now()),(47,80,5.0,now(),now()),(47,85,4.5,now(),now()),(47,14,3.5,now(),now()),(47,53,3.5,now(),now()),(47,79,1.5,now(),now()),(47,9,3.0,now(),now()),(47,54,2.5,now(),now()),(47,75,4.0,now(),now()),(47,61,1.5,now(),now()),(47,48,3.5,now(),now()),(47,3,3.5,now(),now()),(47,41,3.0,now(),now()),(47,66,0.5,now(),now()),(47,88,2.0,now(),now()),(47,63,4.0,now(),now()),(47,16,5.0,now(),now()),(48,12,1.0,now(),now()),(48,74,5.0,now(),now()),(48,10,1.0,now(),now()),(48,15,1.0,now(),now()),(48,38,1.0,now(),now()),(48,68,0.5,now(),now()),(48,29,4.0,now(),now()),(48,61,2.5,now(),now()),(48,45,4.0,now(),now()),(48,89,2.5,now(),now()),(48,60,0.5,now(),now()),(48,48,4.5,now(),now()),(48,100,5.0,now(),now()),(48,18,5.0,now(),now()),(48,95,4.0,now(),now()),(48,88,1.5,now(),now()),(48,44,5.0,now(),now()),(48,77,1.5,now(),now()),(48,91,0.5,now(),now()),(48,16,0.5,now(),now()),(48,17,3.5,now(),now()),(48,99,3.0,now(),now()),(48,52,3.0,now(),now()),(48,83,2.0,now(),now()),(49,84,5.0,now(),now()),(49,91,0.5,now(),now()),(49,72,3.5,now(),now()),(49,67,2.0,now(),now()),(49,88,4.5,now(),now()),(49,31,5.0,now(),now()),(49,20,5.0,now(),now()),(49,14,2.5,now(),now()),(49,10,2.5,now(),now()),(49,37,0.5,now(),now()),(49,66,0.5,now(),now()),(49,27,4.5,now(),now()),(49,7,4.5,now(),now()),(49,43,3.5,now(),now()),(49,79,4.0,now(),now()),(49,22,1.0,now(),now()),(49,45,1.0,now(),now()),(49,49,1.5,now(),now()),(49,50,3.5,now(),now()),(49,3,0.5,now(),now()),(49,63,2.5,now(),now()),(49,5,1.5,now(),now()),(49,4,4.0,now(),now()),(50,85,2.0,now(),now()),(50,41,2.5,now(),now()),(50,71,1.0,now(),now()),(50,98,2.0,now(),now()),(50,67,3.5,now(),now()),(50,19,4.0,now(),now()),(50,83,5.0,now(),now()),(50,52,4.5,now(),now()),(50,75,5.0,now(),now()),(50,38,1.5,now(),now()),(50,13,2.5,now(),now()),(50,65,1.5,now(),now()),(50,1,3.5,now(),now()),(50,31,2.0,now(),now()),(50,36,2.5,now(),now()),(50,51,1.0,now(),now()),(50,60,3.0,now(),now()),(50,72,2.0,now(),now()),(50,26,5.0,now(),now()),(50,2,0.5,now(),now()),(50,97,5.0,now(),now()),(50,35,0.5,now(),now()),(50,12,1.5,now(),now()),(50,45,4.5,now(),now()),(50,99,1.0,now(),now()),(50,74,4.0,now(),now()),(50,20,4.5,now(),now()),(47,17,5.0,now(),now()),(47,32,3.5,now(),now()),(47,69,3.5,now(),now()),(47,18,5.0,now(),now()),(47,98,1.0,now(),now()),(47,60,1.0,now(),now()),(47,26,3.5,now(),now());

