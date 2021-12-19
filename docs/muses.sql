/*
 Navicat Premium Data Transfer

 Source Server         : localhost-MySQL
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : muses

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 19/12/2021 14:44:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_gateway
-- ----------------------------
DROP TABLE IF EXISTS `t_gateway`;
CREATE TABLE `t_gateway`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `query_id` bigint(20) NULL DEFAULT NULL,
  `request` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `response` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_gateway_query_id`(`query_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_gateway
-- ----------------------------

-- ----------------------------
-- Table structure for t_gateway_1
-- ----------------------------
DROP TABLE IF EXISTS `t_gateway_1`;
CREATE TABLE `t_gateway_1`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `query_id` bigint(20) NULL DEFAULT NULL,
  `request` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `response` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_gateway_query_id`(`query_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_gateway_1
-- ----------------------------

-- ----------------------------
-- Table structure for t_gateway_2
-- ----------------------------
DROP TABLE IF EXISTS `t_gateway_2`;
CREATE TABLE `t_gateway_2`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `query_id` bigint(20) NULL DEFAULT NULL,
  `request` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `response` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_gateway_query_id`(`query_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_gateway_2
-- ----------------------------

-- ----------------------------
-- Table structure for t_path
-- ----------------------------
DROP TABLE IF EXISTS `t_path`;
CREATE TABLE `t_path`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `memo` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort` smallint(6) NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_path_parent_id`(`parent_id`) USING BTREE,
  CONSTRAINT `key_path_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `t_path` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_path
-- ----------------------------
INSERT INTO `t_path` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '查询用户备注', '查询用户备注', 'GET:/user/memo', 1, NULL);
INSERT INTO `t_path` VALUES (2, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '修改用户备注', '修改用户备注', 'POST:/user/memo', 1, NULL);
INSERT INTO `t_path` VALUES (3, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '查询用户信息', '查询用户信息', 'POST:/user', 1, NULL);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `memo` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', '超级用户', '超级用户');

-- ----------------------------
-- Table structure for t_role_path
-- ----------------------------
DROP TABLE IF EXISTS `t_role_path`;
CREATE TABLE `t_role_path`  (
  `role_id` bigint(20) NOT NULL,
  `path_id` bigint(20) NOT NULL,
  INDEX `key_role_path_path_id`(`path_id`) USING BTREE,
  INDEX `key_role_path_role_id`(`role_id`) USING BTREE,
  CONSTRAINT `key_role_path_path_id` FOREIGN KEY (`path_id`) REFERENCES `t_path` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `key_role_path_role_id` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_path
-- ----------------------------
INSERT INTO `t_role_path` VALUES (1, 1);
INSERT INTO `t_role_path` VALUES (1, 2);
INSERT INTO `t_role_path` VALUES (1, 3);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `modify_date` datetime(6) NULL DEFAULT NULL,
  `enable` bit(1) NULL DEFAULT NULL,
  `memo` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', b'1', '超级用户', 'root', '$2a$10$lRFeC7uj.rCiI7p9YLWMAeOqdx.1ZV6iRJzilNs6WagRa0S63wcou');
INSERT INTO `t_user` VALUES (2, '2020-01-01 00:00:00.000000', '2020-01-01 00:00:00.000000', b'1', '碧螺萧萧', 'acgist', '$2a$10$lRFeC7uj.rCiI7p9YLWMAeOqdx.1ZV6iRJzilNs6WagRa0S63wcou');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  INDEX `key_user_role_role_id`(`role_id`) USING BTREE,
  INDEX `key_user_role_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `key_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `key_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
