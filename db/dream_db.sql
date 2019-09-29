/*
SQLyog v10.2 
MySQL - 5.7.27 : Database - dream_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dream_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `dream_db`;

/*Table structure for table `comment` */

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `con_id` bigint(20) DEFAULT NULL,
  `com_id` bigint(20) DEFAULT NULL,
  `by_id` bigint(20) DEFAULT NULL,
  `com_content` text,
  `comm_time` datetime DEFAULT NULL,
  `children` varchar(255) DEFAULT NULL,
  `upvote` int(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `comment_index_key` (`id`),
  KEY `FK_Reference_7` (`com_id`),
  KEY `FK_Reference_8` (`con_id`),
  KEY `FK_Reference_9` (`by_id`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`com_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`con_id`) REFERENCES `user_content` (`id`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`by_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `login_log` */

CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_log_index_key` (`id`),
  KEY `FK_Reference_12` (`u_id`),
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `open_user` */

CREATE TABLE `open_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) NOT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `open_type` varchar(255) DEFAULT NULL,
  `expired_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_uid` (`id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `resource` */

CREATE TABLE `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `enabled` varchar(1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_index_key` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `role` */

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  `role_value` varchar(255) DEFAULT NULL,
  `role_matcher` varchar(255) DEFAULT NULL,
  `enabled` varchar(1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_index_key` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `role_resource` */

CREATE TABLE `role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `r_id` bigint(20) DEFAULT NULL,
  `res_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_resource_index_key` (`id`),
  KEY `FK_Reference_1` (`r_id`),
  KEY `FK_Reference_2` (`res_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`r_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`res_id`) REFERENCES `resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `role_user` */

CREATE TABLE `role_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) DEFAULT NULL,
  `r_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_role_index_key` (`id`),
  KEY `FK_Reference_3` (`r_id`),
  KEY `FK_Reference_4` (`u_id`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`r_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `upvote` */

CREATE TABLE `upvote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `upvote` varchar(1) DEFAULT NULL,
  `downvote` varchar(1) DEFAULT NULL,
  `upvote_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `upvote_index_key` (`id`),
  KEY `FK_Reference_10` (`u_id`),
  KEY `FK_Reference_11` (`content_id`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`content_id`) REFERENCES `user_content` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `state` varchar(1) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `enable` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_index_key` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `user_content` */

CREATE TABLE `user_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `content` text,
  `personal` varchar(1) DEFAULT NULL,
  `rpt_time` datetime DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `upvote` int(100) DEFAULT NULL,
  `downvote` int(100) DEFAULT NULL,
  `comment_num` int(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_content_index_key` (`id`),
  KEY `FK_Reference_6` (`u_id`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `user_info` */

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `u_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` varchar(1) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `hobby` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_info_index_key` (`id`),
  KEY `FK_Reference_5` (`u_id`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`u_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*!50106 set global event_scheduler = 1*/;

/* Event structure for event `e_delete_upvote` */

DELIMITER $$

/*!50106 CREATE DEFINER=`root`@`localhost` EVENT `e_delete_upvote` ON SCHEDULE EVERY 1 DAY STARTS '2019-07-26 00:00:00' ON COMPLETION PRESERVE ENABLE DO TRUNCATE TABLE dream_db.upvote */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
