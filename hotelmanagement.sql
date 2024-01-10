/*
 Navicat Premium Data Transfer

 Source Server         : lzw
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : hotelmanagement

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 10/01/2024 19:00:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for reservation
-- ----------------------------
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation`  (
  `reservationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '住宿id',
  `roomId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号id',
  `checkInDate` datetime NOT NULL COMMENT '入住日期',
  `checkOutDate` datetime NOT NULL COMMENT '退房日期',
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `totalCost` decimal(10, 2) NOT NULL COMMENT '预定费用',
  PRIMARY KEY (`reservationId`) USING BTREE,
  INDEX `RI`(`roomId` ASC) USING BTREE,
  INDEX `UI`(`userId` ASC) USING BTREE,
  CONSTRAINT `RI` FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `UI` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation
-- ----------------------------
INSERT INTO `reservation` VALUES ('1704615945902a2b1d303', '1704530334740c14da991', '2024-01-12 16:11:26', '2024-01-13 16:11:29', '1', 1000.00);
INSERT INTO `reservation` VALUES ('170461651127753e48cc3', '101', '2024-01-30 16:31:15', '2024-01-31 16:31:25', '1', 86.00);
INSERT INTO `reservation` VALUES ('17047058032171ea087e3', '101', '2024-01-10 17:23:02', '2024-01-11 17:23:10', '1', 86.00);
INSERT INTO `reservation` VALUES ('170470584494331c57d0a', '101', '2024-01-12 17:23:50', '2024-01-14 17:23:53', '1704344424558fb9c27d9', 172.00);
INSERT INTO `reservation` VALUES ('17047058750950731d18a', '102', '2024-01-17 17:23:50', '2024-01-19 17:23:53', '1704453235260972f9625', 280.00);
INSERT INTO `reservation` VALUES ('1704705943642e7f6d1d1', '1704530334740c14da991', '2024-01-11 17:23:50', '2024-01-12 17:23:53', '2', 1000.00);
INSERT INTO `reservation` VALUES ('17047060819427649eb58', '1704530681439fdfbb98b', '2024-01-11 17:27:35', '2024-01-12 17:27:41', '2', 140.00);
INSERT INTO `reservation` VALUES ('17047062568935a01baaa', '170470615783349a33ee0', '2024-01-18 17:30:40', '2024-01-20 17:30:43', '1704344424558fb9c27d9', 376.00);
INSERT INTO `reservation` VALUES ('17048822288951b1ff1e6', '102', '2024-01-13 18:23:05', '2024-01-14 18:23:08', '1704453235260972f9625', 140.00);

-- ----------------------------
-- Table structure for revenue
-- ----------------------------
DROP TABLE IF EXISTS `revenue`;
CREATE TABLE `revenue`  (
  `revenueId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收益情况id',
  `roomNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `reservationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '住宿情况id',
  `amount` decimal(10, 2) NOT NULL COMMENT '这一天这个房间产生的收益',
  `date` datetime NOT NULL COMMENT '日期',
  PRIMARY KEY (`revenueId`) USING BTREE,
  INDEX `RID`(`reservationId` ASC) USING BTREE,
  INDEX `RoD`(`roomNum` ASC) USING BTREE,
  CONSTRAINT `RID` FOREIGN KEY (`reservationId`) REFERENCES `reservation` (`reservationId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `RoD` FOREIGN KEY (`roomNum`) REFERENCES `room` (`roomNumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of revenue
-- ----------------------------
INSERT INTO `revenue` VALUES ('1704615945926f50fc46c', '203B', '1704615945902a2b1d303', 1000.00, '2024-01-01 16:25:46');
INSERT INTO `revenue` VALUES ('17046165112927592a21f', '101A', '170461651127753e48cc3', 86.00, '2024-01-02 16:35:11');
INSERT INTO `revenue` VALUES ('170470580323678a443c8', '101A', '17047058032171ea087e3', 86.00, '2024-01-03 17:23:23');
INSERT INTO `revenue` VALUES ('170470584495183aae689', '101A', '170470584494331c57d0a', 172.00, '2024-01-04 17:24:05');
INSERT INTO `revenue` VALUES ('17047058751043d6438ca', '102A', '17047058750950731d18a', 280.00, '2024-01-05 17:24:35');
INSERT INTO `revenue` VALUES ('1704705943650f7a29509', '203B', '1704705943642e7f6d1d1', 1000.00, '2024-01-08 17:25:44');
INSERT INTO `revenue` VALUES ('1704706081949461a7b1e', '103A', '17047060819427649eb58', 140.00, '2024-01-08 17:28:02');
INSERT INTO `revenue` VALUES ('17047062569022289556d', '304A', '17047062568935a01baaa', 376.00, '2024-01-08 17:30:57');
INSERT INTO `revenue` VALUES ('170488222891994cd3b3c', '102A', '17048822288951b1ff1e6', 140.00, '2024-01-10 18:23:49');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `roomId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间id',
  `roomNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `status` int NULL DEFAULT NULL COMMENT '1表示已预定，0表示未预定，-1表示维护中',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房间描述',
  `roomTypeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间类型号',
  PRIMARY KEY (`roomId`) USING BTREE,
  INDEX `RoomTypeId`(`roomTypeId` ASC) USING BTREE,
  INDEX `roomId`(`roomId` ASC) USING BTREE,
  INDEX `roomNumber`(`roomNumber` ASC) USING BTREE,
  CONSTRAINT `RoomTypeId` FOREIGN KEY (`roomTypeId`) REFERENCES `roomtype` (`roomTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('101', '101A', 1, '朝阳,光线好', '1704448068413d7d7cf8d');
INSERT INTO `room` VALUES ('102', '102A', 1, '适合情侣', '2');
INSERT INTO `room` VALUES ('1704529963516c5c28589', '203A', 0, '房间远离道路', '1704265179038d8eda74b');
INSERT INTO `room` VALUES ('1704530334740c14da991', '203B', 1, '适合聚会', '1704348897741ee263ca7');
INSERT INTO `room` VALUES ('1704530681439fdfbb98b', '103A', 1, '安静', '2');
INSERT INTO `room` VALUES ('170470615783349a33ee0', '304A', 1, '网速快', '1704609341827701a4857');
INSERT INTO `room` VALUES ('202', '202B', 0, '有舞台', '1704281084838484316a9');

-- ----------------------------
-- Table structure for roomprice
-- ----------------------------
DROP TABLE IF EXISTS `roomprice`;
CREATE TABLE `roomprice`  (
  `priceId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '价格id',
  `roomTypeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房间类型id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '房间价格',
  PRIMARY KEY (`priceId`) USING BTREE,
  INDEX `RTI`(`roomTypeId` ASC) USING BTREE,
  CONSTRAINT `RTI` FOREIGN KEY (`roomTypeId`) REFERENCES `roomtype` (`roomTypeId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roomprice
-- ----------------------------
INSERT INTO `roomprice` VALUES ('170428108507000167487', '1704281084838484316a9', 1280.00);
INSERT INTO `roomprice` VALUES ('17043488977513a240635', '1704348897741ee263ca7', 800.00);
INSERT INTO `roomprice` VALUES ('17044480684140a9e391b', '1704448068413d7d7cf8d', 86.00);
INSERT INTO `roomprice` VALUES ('170460934183296f1f243', '1704609341827701a4857', 188.00);
INSERT INTO `roomprice` VALUES ('2', '2', 140.00);
INSERT INTO `roomprice` VALUES ('3', '1704265179038d8eda74b', 290.00);
INSERT INTO `roomprice` VALUES ('4', '3', 750.00);
INSERT INTO `roomprice` VALUES ('5', '4', 90.00);
INSERT INTO `roomprice` VALUES ('6', '5', 100.00);
INSERT INTO `roomprice` VALUES ('7', '6', 188.00);
INSERT INTO `roomprice` VALUES ('8', '7', 468.00);
INSERT INTO `roomprice` VALUES ('9', '8', 388.00);

-- ----------------------------
-- Table structure for roomtype
-- ----------------------------
DROP TABLE IF EXISTS `roomtype`;
CREATE TABLE `roomtype`  (
  `roomTypeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间类型id',
  `roomTypeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间类型名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `basePrice` decimal(10, 0) NOT NULL COMMENT '基础价格',
  PRIMARY KEY (`roomTypeId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roomtype
-- ----------------------------
INSERT INTO `roomtype` VALUES ('1704265179038d8eda74b', '豪华套房', '豪华套房', 300);
INSERT INTO `roomtype` VALUES ('1704281084838484316a9', '大型会议厅', '大型会议厅', 2000);
INSERT INTO `roomtype` VALUES ('1704348897741ee263ca7', '小型会议厅', '小型会议厅', 1000);
INSERT INTO `roomtype` VALUES ('1704448068413d7d7cf8d', '单人间', '单人间', 100);
INSERT INTO `roomtype` VALUES ('1704609341827701a4857', '电竞4人间', '千兆网络', 200);
INSERT INTO `roomtype` VALUES ('2', '双人间', '豪华双人间', 150);
INSERT INTO `roomtype` VALUES ('3', '海景房', '面朝大海', 800);
INSERT INTO `roomtype` VALUES ('4', '单人间2', '普通单人间', 90);
INSERT INTO `roomtype` VALUES ('5', '双人间2', '标准双人间', 120);
INSERT INTO `roomtype` VALUES ('6', '3人家庭间', '适合带小孩', 200);
INSERT INTO `roomtype` VALUES ('7', '豪华3人家庭间', '有客厅', 500);
INSERT INTO `roomtype` VALUES ('8', '民宿风格', '有厨房', 400);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户id',
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名',
  `idCardNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户身份证号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户备注',
  `phoneNum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户电话号码',
  `flag` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户删除标记，真删为1，假删为0',
  `balance` decimal(10, 2) NOT NULL COMMENT '用户余额',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '荔枝味', '123456789012', '常住用户', '15779554232', 0, 9828.00);
INSERT INTO `user` VALUES ('1704344424558fb9c27d9', '李四', '23837676131', NULL, '15779554232', 0, 2452.00);
INSERT INTO `user` VALUES ('1704453235260972f9625', '王五', '2090037702', '喜欢安静', '15779554232', 0, 3080.00);
INSERT INTO `user` VALUES ('2', '张三', '098765432121', '临时用户', '13027000692', 0, 1360.00);

SET FOREIGN_KEY_CHECKS = 1;
