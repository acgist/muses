CREATE DATABASE `muses` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `t_gateway`;
CREATE TABLE `t_gateway` (
  `id` bigint(20) NOT NULL COMMENT '网关ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `modify_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `query_id` bigint(20) DEFAULT NULL COMMENT '网关标识',
  `request` varchar(1024) NOT NULL COMMENT '请求数据',
  `response` varchar(1024) DEFAULT NULL COMMENT '响应数据',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_gateway_query_id` (`query_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='网关记录';

DROP TABLE IF EXISTS `t_gateway_0`;
CREATE TABLE `t_gateway_0` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `modify_date` datetime(6) DEFAULT NULL,
  `query_id` bigint(20) DEFAULT NULL,
  `request` varchar(1024) NOT NULL,
  `response` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_gateway_query_id` (`query_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `t_gateway_1`;
CREATE TABLE `t_gateway_1` (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `modify_date` datetime(6) DEFAULT NULL,
  `query_id` bigint(20) DEFAULT NULL,
  `request` varchar(1024) NOT NULL,
  `response` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_gateway_query_id` (`query_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `t_path`;
CREATE TABLE `t_path` (
  `id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `modify_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `memo` varchar(128) DEFAULT NULL COMMENT '描述',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否可用',
  `sorted` int(11) DEFAULT '0' COMMENT '排序',
  `path` varchar(128) NOT NULL COMMENT '规则匹配',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级ID',
  `parent_id_path` varchar(256) DEFAULT NULL COMMENT '上级ID路径',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `key_path_parent_id` (`parent_id`) USING BTREE,
  CONSTRAINT `key_path_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `t_path` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权限';

INSERT INTO `t_path` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '查询用户备注', '查询用户备注', b'1', 0, 'GET:/user/memo', NULL, NULL);
INSERT INTO `t_path` VALUES (2, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '修改用户备注', '修改用户备注', b'1', 0, 'POST:/user/memo', NULL, NULL);
INSERT INTO `t_path` VALUES (3, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '查询用户信息', '查询用户信息', b'1', 0, 'POST:/user', NULL, NULL);

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `modify_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `memo` varchar(128) DEFAULT NULL COMMENT '描述',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否可用',
  `sorted` int(11) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色';

INSERT INTO `t_role` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '超级用户', '超级用户', b'1', 0);

DROP TABLE IF EXISTS `t_role_path`;
CREATE TABLE `t_role_path` (
  `role_id` bigint(20) NOT NULL,
  `path_id` bigint(20) NOT NULL,
  KEY `key_role_path_path_id` (`path_id`) USING BTREE,
  KEY `key_role_path_role_id` (`role_id`) USING BTREE,
  CONSTRAINT `key_role_path_path_id` FOREIGN KEY (`path_id`) REFERENCES `t_path` (`id`),
  CONSTRAINT `key_role_path_role_id` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

INSERT INTO `t_role_path` VALUES (1, 1);
INSERT INTO `t_role_path` VALUES (1, 2);
INSERT INTO `t_role_path` VALUES (1, 3);

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `modify_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `memo` varchar(128) DEFAULT NULL COMMENT '描述',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `enabled` bit(1) DEFAULT b'1' COMMENT '是否可用',
  `sorted` int(11) DEFAULT '0' COMMENT '排序',
  `password` varchar(128) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_user_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户';

INSERT INTO `t_user` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '超级用户', 'root', b'1', 0, '$2a$10$lRFeC7uj.rCiI7p9YLWMAeOqdx.1ZV6iRJzilNs6WagRa0S63wcou');
INSERT INTO `t_user` VALUES (2, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '碧螺萧萧', 'acgist', b'1', 0, '$2a$10$lRFeC7uj.rCiI7p9YLWMAeOqdx.1ZV6iRJzilNs6WagRa0S63wcou');

DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `key_user_role_role_id` (`role_id`) USING BTREE,
  KEY `key_user_role_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `key_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `key_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

INSERT INTO `t_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
