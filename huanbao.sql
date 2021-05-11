/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.3.66_3306
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 192.168.3.66:3306
 Source Schema         : huanbao

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 12/05/2021 00:21:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for device_noise
-- ----------------------------
DROP TABLE IF EXISTS `device_noise`;
CREATE TABLE `device_noise`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `device_flag` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备标识',
  `device_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `flag_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识名称(为空时用device_name的值自增1)',
  `model` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
  `company_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家名称',
  `linkman` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家联系电话',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '监测设备' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_noise
-- ----------------------------
INSERT INTO `device_noise` VALUES ('0000000044ddbf71ffffffffdfbfd3f5', 'lhgc102458', '兰花广场噪音检测设备', '兰花广场南角C', 'zy1024V202012', '深圳富泰科技', NULL, NULL, NULL, '2021-05-10 18:46:27');

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监测点id',
  `site_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '位置地点id(site.kid)',
  `device_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id(device_noise.kid|device_sound.kid)',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '检测点名称',
  `longs` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '经度',
  `lat` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '纬度',
  `province_id` bigint(0) NOT NULL COMMENT '省级id',
  `province_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '省级名称',
  `city_id` bigint(0) NOT NULL COMMENT '市级id',
  `city_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '市级名称',
  `county_id` bigint(0) NOT NULL COMMENT '区县id',
  `county_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区县名称',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '面积（亩）',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '检测点信息(一对多关系)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES ('ffffffffdde7743e00000000681eda6e', 'ffffffffc92808440000000035a6e613', '0000000044ddbf71ffffffffdfbfd3f5', '兰花广场设备', '106.689434', '26.567337', 52, '贵州省', 520100000000, '贵阳市', 520102000000, '南明区', '贵阳市南明区花果园兰花广场', '102.12', '兰花广场监测噪音', '2021-05-11 23:29:22');

-- ----------------------------
-- Table structure for monitor_value
-- ----------------------------
DROP TABLE IF EXISTS `monitor_value`;
CREATE TABLE `monitor_value`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `location_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监测点id(location.kid)',
  `visible` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '热力图是否显示,默认为1显示',
  `opacity` smallint(0) NOT NULL COMMENT '热力的透明度,1-100',
  `radius` int(0) NOT NULL COMMENT '势力图的每个点的半径大小',
  `temperature` decimal(10, 4) NULL DEFAULT NULL COMMENT '温度值',
  `humidity` decimal(10, 4) NULL DEFAULT NULL COMMENT '湿度值',
  `volume` smallint(0) UNSIGNED NULL DEFAULT NULL COMMENT '音量值',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '监测点数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '位置地点id',
  `pid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '父节点id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '位置地点名称',
  `sort` int(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '位置地点(区域)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of site
-- ----------------------------
INSERT INTO `site` VALUES ('0000000049752e4bffffffffd59c6bab', '0', '尚义小学', 2, '2021-05-10 14:49:59');
INSERT INTO `site` VALUES ('ffffffffc92808440000000035a6e613', '0', '兰花广场', 1, '2021-05-10 14:48:21');

-- ----------------------------
-- Table structure for site_user
-- ----------------------------
DROP TABLE IF EXISTS `site_user`;
CREATE TABLE `site_user`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `site_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '位置地点id(site.kid)',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_site_user`(`site_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '位置地点(区域)账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of site_user
-- ----------------------------
INSERT INTO `site_user` VALUES ('9c9b47e2b27311ebbb080242ac110002', '0000000049752e4bffffffffd59c6bab', '0000000007b9739a00000000365a7430');
INSERT INTO `site_user` VALUES ('3fd93385b17411eba42d0242ac110002', 'ffffffffc92808440000000035a6e613', '000000001d7593670000000079676f55');

-- ----------------------------
-- Table structure for sys_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area`  (
  `kid` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '区划代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `pid` bigint(0) UNSIGNED NOT NULL COMMENT '父级区划代码',
  `type` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城乡分类代码（100 -城镇，110 -城区，111 -主城区，112 --城乡结合区，120 -镇区，121 --镇中心区，122 -镇乡结合区，123 -特殊区域，200 -乡村，210 -乡中心区，220 -村庄）',
  `level` smallint(0) UNSIGNED NOT NULL COMMENT '级别1-5,省市县镇村',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 522732000001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '省市县区域' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_area
-- ----------------------------
INSERT INTO `sys_area` VALUES (52, '贵州省', 0, '', 1);
INSERT INTO `sys_area` VALUES (520100000000, '贵阳市', 52, '', 2);
INSERT INTO `sys_area` VALUES (520101000000, '市辖区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520102000000, '南明区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520103000000, '云岩区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520111000000, '花溪区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520112000000, '乌当区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520113000000, '白云区', 520100000000, '', 3);
INSERT INTO `sys_area` VALUES (520115000000, '观山湖区', 520100000000, '', 3);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `pid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '父节点id',
  `sort` int(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_user`;
CREATE TABLE `sys_dept_user`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `dept_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `KID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典主键',
  `NAME` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `DELETED` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除(0未删除1已删除)',
  `SORT` int(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  `PID` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '88888888888888888888888888888888' COMMENT '父级ID',
  `REMARKS` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`KID`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('000000003154d347000000000118619b', '已通过', 0, 3, '00000000382d48760000000079b1fbe5', '审核状态', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('0000000037d27354ffffffffc4ef7062', '未通过', 0, 2, '00000000382d48760000000079b1fbe5', '审核状态', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffffdf5ca826000000002855ee97', '未审核', 0, 1, '00000000382d48760000000079b1fbe5', '审核状态', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('00000000382d48760000000079b1fbe5', '审核状态', 0, 1, '88888888888888888888888888888888', '根节点', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffffc580f57b000000005d861734', '博士', 0, 9, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffff9e454217ffffffffb3d5e955', '硕士', 0, 8, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('00000000461deb28ffffffff87061372', '本科', 0, 7, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('00000000657d8fc30000000044020a60', '专科', 0, 6, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffffa20005cf0000000044d0c62f', '大专', 0, 5, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('0000000046e43ca9ffffffff8892d864', '中专', 0, 4, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffffabb9e458000000002264d37b', '高中', 0, 3, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('ffffffffee7f2588ffffffffa9e12b00', '初中', 0, 2, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('00000000090b40d7ffffffffb92e9caa', '小学', 0, 1, '0000000051f9d61affffffff8405fc42', '学历', '2021-03-30 10:53:51');
INSERT INTO `sys_dict` VALUES ('0000000051f9d61affffffff8405fc42', '学历', 0, 1, '88888888888888888888888888888888', '根节点', '2021-03-30 10:53:51');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `name` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单标识(权限标识)',
  `category` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '权限类型(1查询;2编辑;3删除;4添加)',
  `icon_style` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'menu-icon fa fa-caret-right' COMMENT '元素span后面的i元素样式',
  `pid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '88888888888888888888888888888888' COMMENT '父级id',
  `url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'javascript:;' COMMENT 'url路径',
  `subset` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否含子节点(1含子节点;0不含)',
  `type` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '菜单类型(1导航菜单;2普通按钮;3行内按钮)',
  `relation` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '88888888888888888888888888888888' COMMENT '菜单层次级别关系(限制最多就8级导航菜单)',
  `order_by` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_menu_permission`(`permission`) USING BTREE,
  UNIQUE INDEX `index_menu_url`(`url`) USING BTREE,
  UNIQUE INDEX `index_menu_relation`(`relation`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('000000000565166500000000620209a9', '删除(批量删除)', 'deviceNoise_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/delByKeys', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@000000000565166500000000620209a9', 6);
INSERT INTO `sys_menu` VALUES ('0000000006a9099300000000100d10cd', '获取数据列表(搜索)', 'menu_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@0000000006a9099300000000100d10cd', 1);
INSERT INTO `sys_menu` VALUES ('0000000009bc5e6dffffffffa6cc1b40', '编辑', 'location_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/edit', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@0000000009bc5e6dffffffffa6cc1b40', 3);
INSERT INTO `sys_menu` VALUES ('000000000ba93c43ffffffffe5c59508', '添加', 'menu_btn_add', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@000000000ba93c43ffffffffe5c59508', 2);
INSERT INTO `sys_menu` VALUES ('000000000c1ac66cffffffffb9dde56c', '获取单条数据', 'area_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/queryById', 0, 2, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@000000000c1ac66cffffffffb9dde56c', 4);
INSERT INTO `sys_menu` VALUES ('000000000ec31a77ffffffff8afa2e2c', '删除(批量删除)', 'area_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/delByKeys', 0, 2, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@000000000ec31a77ffffffff8afa2e2c', 6);
INSERT INTO `sys_menu` VALUES ('000000000ff54fcbffffffffbbe16acc', '添加', 'dictionary_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@000000000ff54fcbffffffffbbe16acc', 2);
INSERT INTO `sys_menu` VALUES ('000000001585cf24ffffffffe093ab94', '获取数据列表(搜索)', 'deviceNoise_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/listData', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@000000001585cf24ffffffffe093ab94', 1);
INSERT INTO `sys_menu` VALUES ('000000001dd3273c0000000029393519', '删除(批量删除)', 'site_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/delByKeys', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@000000001dd3273c0000000029393519', 6);
INSERT INTO `sys_menu` VALUES ('00000000208ad7d20000000054cbadea', '获取单条数据', 'location_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/queryById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@00000000208ad7d20000000054cbadea', 4);
INSERT INTO `sys_menu` VALUES ('000000002441c5fe000000005f6b6829', '获取单条数据', 'dictionary_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/queryById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@000000002441c5fe000000005f6b6829', 4);
INSERT INTO `sys_menu` VALUES ('000000002d8402d60000000072f7dea2', '获取数据列表(搜索)', 'user_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000002d8402d60000000072f7dea2', 1);
INSERT INTO `sys_menu` VALUES ('000000002e18cf48ffffffffab242e16', '角色菜单(保存)', 'role_row_saveRoleMenu', 4, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/saveRoleMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@000000002e18cf48ffffffffab242e16', 8);
INSERT INTO `sys_menu` VALUES ('0000000035fbca03000000003a33d6b7', '删除(批量删除)', 'user_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/delByKeys', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@0000000035fbca03000000003a33d6b7', 5);
INSERT INTO `sys_menu` VALUES ('000000003a56d4fb0000000037016444', '角色(保存)', 'user_btn_row_saveAllotRole', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/saveAllotRole', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000003a56d4fb0000000037016444', 9);
INSERT INTO `sys_menu` VALUES ('000000003cf86654000000006c39e14e', '编辑', 'site_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/edit', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@000000003cf86654000000006c39e14e', 3);
INSERT INTO `sys_menu` VALUES ('000000003d59e07400000000646ad837', '添加', 'area_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/add', 0, 2, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@000000003d59e07400000000646ad837', 2);
INSERT INTO `sys_menu` VALUES ('00000000412eae8efffffffff6b97de1', '删除(行内删除)', 'deviceNoise_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/delById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@00000000412eae8efffffffff6b97de1', 5);
INSERT INTO `sys_menu` VALUES ('00000000436e0e010000000069184e93', '删除(行内删除)', 'area_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/delById', 0, 3, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@00000000436e0e010000000069184e93', 5);
INSERT INTO `sys_menu` VALUES ('00000000438a8345ffffffffe75bf9f0', '环保业务', 'nav_huanbao', 1, 'menu-icon fa fa-globe', '88888888888888888888888888888888', 'nav_huanbao', 1, 1, '00000000438a8345ffffffffe75bf9f0', 3);
INSERT INTO `sys_menu` VALUES ('00000000545f18ddffffffff9482ed27', '删除(行内删除)', 'dictionary_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@00000000545f18ddffffffff9482ed27', 5);
INSERT INTO `sys_menu` VALUES ('0000000058e2ec83000000002f978e03', '编辑', 'menu_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@0000000058e2ec83000000002f978e03', 4);
INSERT INTO `sys_menu` VALUES ('000000005c41138100000000288a6b45', '删除(行内删除)', 'menu_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@000000005c41138100000000288a6b45', 3);
INSERT INTO `sys_menu` VALUES ('000000005cdfacaaffffffffbb1e36dc', '基础数据', 'nav_base_data', 1, 'menu-icon fa fa-database', '88888888888888888888888888888888', 'nav_base_data', 1, 1, '000000005cdfacaaffffffffbb1e36dc', 2);
INSERT INTO `sys_menu` VALUES ('000000005e02492a0000000033b9c62b', '删除(行内删除)', 'site_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/delById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@000000005e02492a0000000033b9c62b', 5);
INSERT INTO `sys_menu` VALUES ('00000000643b51a3000000004689a047', '添加', 'deviceNoise_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/add', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@00000000643b51a3000000004689a047', 2);
INSERT INTO `sys_menu` VALUES ('000000007330a2a9ffffffff98cebe66', '添加', 'user_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007330a2a9ffffffff98cebe66', 2);
INSERT INTO `sys_menu` VALUES ('0000000077224459ffffffffc9d752ca', '启用禁用', 'user_row_editEnabled', 2, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/editEnabled', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@0000000077224459ffffffffc9d752ca', 10);
INSERT INTO `sys_menu` VALUES ('000000007937b8de0000000034ef3b70', '清空菜单', 'role_row_delEmptyMenu', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delEmptyMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@000000007937b8de0000000034ef3b70', 6);
INSERT INTO `sys_menu` VALUES ('000000007a3bebc1ffffffffc0d19222', '角色(查询)', 'user_btn_row_getAllotRole', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getAllotRole', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007a3bebc1ffffffffc0d19222', 8);
INSERT INTO `sys_menu` VALUES ('000000007aaad4a40000000016f6244f', '添加', 'location_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/add', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@000000007aaad4a40000000016f6244f', 2);
INSERT INTO `sys_menu` VALUES ('000000007d24ec6fffffffffe5ac787c', '获取单条数据', 'site_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/queryById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@000000007d24ec6fffffffffe5ac787c', 4);
INSERT INTO `sys_menu` VALUES ('000000007ea75783000000004607fdec', '编辑', 'user_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@000000007ea75783000000004607fdec', 3);
INSERT INTO `sys_menu` VALUES ('111fffffbd911aa0ffffffffd5637fff', '角色权限', 'page_sys_role', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_role.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff', 3);
INSERT INTO `sys_menu` VALUES ('ffffffff81c3d84800000000742c484e', '地点位置', 'page_site', 1, 'menu-icon fa fa-caret-right', '00000000438a8345ffffffffe75bf9f0', 'page_site.html', 0, 1, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e', 2);
INSERT INTO `sys_menu` VALUES ('ffffffff8407cc4dffffffffdbdad884', '获取数据列表(搜索)', 'site_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/listData', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@ffffffff8407cc4dffffffffdbdad884', 1);
INSERT INTO `sys_menu` VALUES ('ffffffff84949f41ffffffffcc0503ed', '获取区域数据构建树形菜单', 'location_btn_getAreaTree', 1, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/getAreaTree', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffff84949f41ffffffffcc0503ed', 8);
INSERT INTO `sys_menu` VALUES ('ffffffff84e5da13fffffffface485ec', '删除(批量删除)', 'dictionary_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/delByKeys', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@ffffffff84e5da13fffffffface485ec', 6);
INSERT INTO `sys_menu` VALUES ('ffffffff87212d7d000000005fbd6e98', '添加', 'role_btn_add', 4, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/add', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff87212d7d000000005fbd6e98', 2);
INSERT INTO `sys_menu` VALUES ('ffffffff8b559df0ffffffff834bba04', '系统设置', 'page_sys_setting', 1, 'menu-icon fa fa-cog', '88888888888888888888888888888888', 'sys_setting', 1, 1, 'ffffffff8b559df0ffffffff834bba04', 1);
INSERT INTO `sys_menu` VALUES ('ffffffff8c468a55ffffffff8f59d635', '用户账号', 'page_sys_user', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_user.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635', 2);
INSERT INTO `sys_menu` VALUES ('ffffffff8e6151ff0000000015f6e145', '删除(行内删除)', 'role_row_delById', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff8e6151ff0000000015f6e145', 4);
INSERT INTO `sys_menu` VALUES ('ffffffff93ca53bcffffffffc7ca6854', '获取设备标识名称', 'location_btn_getDeviceList', 1, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/getDeviceList', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffff93ca53bcffffffffc7ca6854', 9);
INSERT INTO `sys_menu` VALUES ('ffffffff93f9086effffffffb4a239cd', '删除(批量删除)', 'role_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/delByKeys', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffff93f9086effffffffb4a239cd', 5);
INSERT INTO `sys_menu` VALUES ('ffffffff983d99e6ffffffff9a4c304c', '编辑', 'dictionary_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@ffffffff983d99e6ffffffff9a4c304c', 3);
INSERT INTO `sys_menu` VALUES ('ffffffff9db05c1000000000002e5179', '私有菜单(查询)', 'user_row_getOwnMenu', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getOwnMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffff9db05c1000000000002e5179', 6);
INSERT INTO `sys_menu` VALUES ('ffffffff9dcbdcebffffffff99573f80', '私有菜单(保存)', 'user_row_saveOwnMenu', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/saveOwnMenu', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffff9dcbdcebffffffff99573f80', 7);
INSERT INTO `sys_menu` VALUES ('ffffffffb02530130000000015e4ac7b', '删除(批量删除)', 'location_btn_delByKeys', 3, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/delByKeys', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffffb02530130000000015e4ac7b', 6);
INSERT INTO `sys_menu` VALUES ('ffffffffb71281a2ffffffffd931eb95', '删除(行内删除)', 'user_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/delById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffb71281a2ffffffffd931eb95', 4);
INSERT INTO `sys_menu` VALUES ('ffffffffbca39996ffffffffcd270f17', '监测设备', 'page_device_noise', 1, 'menu-icon fa fa-caret-right', '00000000438a8345ffffffffe75bf9f0', 'deviceNoise.html', 0, 1, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17', 2);
INSERT INTO `sys_menu` VALUES ('ffffffffc0336ff70000000041d6b048', '查询位置地点名称', 'location_btn_getListSite', 1, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/getSiteList', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffffc0336ff70000000041d6b048', 7);
INSERT INTO `sys_menu` VALUES ('ffffffffc43097b8000000003a72e62f', '权限菜单', 'user_row_getMenuData', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getMenuData', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffc43097b8000000003a72e62f', 11);
INSERT INTO `sys_menu` VALUES ('ffffffffc9421e24ffffffffe4170035', '获取数据列表(搜索)', 'dictionary_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffdc83a84dffffffffe53ca925', 'dictionary/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffdc83a84dffffffffe53ca925@ffffffffc9421e24ffffffffe4170035', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffc9b40d9c0000000067265dc3', '删除(行内删除)', 'location_row_delById', 3, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/delById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffffc9b40d9c0000000067265dc3', 5);
INSERT INTO `sys_menu` VALUES ('ffffffffcc4ea12c00000000032c1a2d', '获取数据列表(搜索)', 'area_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/listData', 0, 2, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@ffffffffcc4ea12c00000000032c1a2d', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffd07fcead000000006e6b1c2b', '添加', 'site_btn_add', 4, 'menu-icon fa fa-caret-right', 'ffffffff81c3d84800000000742c484e', 'site/add', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffff81c3d84800000000742c484e@ffffffffd07fcead000000006e6b1c2b', 2);
INSERT INTO `sys_menu` VALUES ('ffffffffd9a2e70e0000000052db7ed0', '区域地区', 'page_sys_area', 1, 'menu-icon fa fa-caret-right', '000000005cdfacaaffffffffbb1e36dc', 'sys_area.html', 0, 1, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0', 2);
INSERT INTO `sys_menu` VALUES ('ffffffffdae3cd4affffffff9894b623', '角色菜单(查询)', 'role_row_getRoleMenu', 1, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/getRoleMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffffdae3cd4affffffff9894b623', 7);
INSERT INTO `sys_menu` VALUES ('ffffffffdc83a84dffffffffe53ca925', '数据字典', 'page:sys_dict', 1, 'menu-icon fa fa-caret-right', '000000005cdfacaaffffffffbb1e36dc', 'sys_dict.html', 0, 1, '000000005cdfacaaffffffffbb1e36dc@ffffffffdc83a84dffffffffe53ca925', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffddf9f51ffffffffff6157ca3', '根据id获取详情', 'menu_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/queryById', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@ffffffffddf9f51ffffffffff6157ca3', 6);
INSERT INTO `sys_menu` VALUES ('ffffffffde74f975ffffffffd8a45e27', '编辑', 'area_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffd9a2e70e0000000052db7ed0', 'area/edit', 0, 3, '000000005cdfacaaffffffffbb1e36dc@ffffffffd9a2e70e0000000052db7ed0@ffffffffde74f975ffffffffd8a45e27', 3);
INSERT INTO `sys_menu` VALUES ('ffffffffdfaf333c000000001760f4a5', '编辑', 'role_row_edit', 2, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/edit', 0, 3, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@ffffffffdfaf333c000000001760f4a5', 3);
INSERT INTO `sys_menu` VALUES ('ffffffffe066d879000000002bb034af', '获取数据列表(搜索)', 'location_btn_listData', 1, 'menu-icon fa fa-caret-right', 'ffffffffea08dbadffffffffd38460b5', 'location/listData', 0, 2, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5@ffffffffe066d879000000002bb034af', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffe56e8ef0ffffffff912af74a', '菜单管理', 'page_sys_menu', 1, 'menu-icon fa fa-caret-right', 'ffffffff8b559df0ffffffff834bba04', 'sys_menu.html', 0, 1, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffe9667397ffffffff89a7556f', '获取单条数据', 'deviceNoise_row_queryById', 1, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/queryById', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@ffffffffe9667397ffffffff89a7556f', 4);
INSERT INTO `sys_menu` VALUES ('ffffffffea08dbadffffffffd38460b5', '设备监测位置', 'page_location', 1, 'menu-icon fa fa-caret-right', '00000000438a8345ffffffffe75bf9f0', 'location.html', 0, 1, '00000000438a8345ffffffffe75bf9f0@ffffffffea08dbadffffffffd38460b5', 3);
INSERT INTO `sys_menu` VALUES ('ffffffffef4aaccd000000000bb43432', '获取位置地点名称', 'user_row_getListSite', 1, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/getListSite', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffef4aaccd000000000bb43432', 12);
INSERT INTO `sys_menu` VALUES ('fffffffff051bf04ffffffffa2ae3c8e', '获取树形菜单', 'menu_btn_queryTreeMenu', 1, 'menu-icon fa fa-caret-right', 'ffffffffe56e8ef0ffffffff912af74a', 'menu/queryTreeMenu', 0, 2, 'ffffffff8b559df0ffffffff834bba04@ffffffffe56e8ef0ffffffff912af74a@fffffffff051bf04ffffffffa2ae3c8e', 5);
INSERT INTO `sys_menu` VALUES ('fffffffff794cb2b0000000056983a09', '编辑', 'deviceNoise_row_edit', 2, 'menu-icon fa fa-caret-right', 'ffffffffbca39996ffffffffcd270f17', 'deviceNoise/edit', 0, 3, '00000000438a8345ffffffffe75bf9f0@ffffffffbca39996ffffffffcd270f17@fffffffff794cb2b0000000056983a09', 3);
INSERT INTO `sys_menu` VALUES ('fffffffffaad7649ffffffffe7c3d6a6', '获取数据列表(搜索)', 'role_btn_listData', 1, 'menu-icon fa fa-caret-right', '111fffffbd911aa0ffffffffd5637fff', 'role/listData', 0, 2, 'ffffffff8b559df0ffffffff834bba04@111fffffbd911aa0ffffffffd5637fff@fffffffffaad7649ffffffffe7c3d6a6', 1);
INSERT INTO `sys_menu` VALUES ('ffffffffff8446b400000000171caa51', '账号绑定到位置地点', 'user_row_bindSite', 4, 'menu-icon fa fa-caret-right', 'ffffffff8c468a55ffffffff8f59d635', 'user/addBindSite', 0, 3, 'ffffffff8b559df0ffffffff834bba04@ffffffff8c468a55ffffffff8f59d635@ffffffffff8446b400000000171caa51', 13);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_flag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_role_name`(`role_name`) USING BTREE,
  UNIQUE INDEX `index_role_flag`(`role_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('000000003035dacf000000000c087217', '游客', 'ROLE_GUEST');
INSERT INTO `sys_role` VALUES ('0000000034775bc40000000006089871', '环保厅', 'ROLE_HBT');
INSERT INTO `sys_role` VALUES ('000000003c05b1fb00000000153a847a', '监测点用户', 'ROLE_SITE_USER');
INSERT INTO `sys_role` VALUES ('ffffffffa04b32a6ffffffffba40a72e', '系统管理员', 'ROLE_ADMINISTRATORS');
INSERT INTO `sys_role` VALUES ('ffffffffa966a308ffffffff9a5e4c52', '网管', 'ROLE_SUPER');
INSERT INTO `sys_role` VALUES ('ffffffffdc2ebe6a000000006b149e18', '操作员', 'ROLE_OPERATOR');
INSERT INTO `sys_role` VALUES ('ffffffffde90da3fffffffffdef7150f', '管理员', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `role_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色的id(sys_role.kid)',
  `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单的id(sys_menu.kid)',
  PRIMARY KEY (`kid`) USING BTREE,
  INDEX `index_role_menu`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('ffffffffa52d5a8effffffffd6c871f7', '0000000034775bc40000000006089871', '000000000565166500000000620209a9');
INSERT INTO `sys_role_menu` VALUES ('000000000a689aa80000000062f49e2a', '0000000034775bc40000000006089871', '0000000009bc5e6dffffffffa6cc1b40');
INSERT INTO `sys_role_menu` VALUES ('0000000019f935f7ffffffffa7a34f67', '0000000034775bc40000000006089871', '000000000c1ac66cffffffffb9dde56c');
INSERT INTO `sys_role_menu` VALUES ('0000000070f9301fffffffff8a5f1682', '0000000034775bc40000000006089871', '000000000ec31a77ffffffff8afa2e2c');
INSERT INTO `sys_role_menu` VALUES ('ffffffffab5c8e0bffffffffeee0bcff', '0000000034775bc40000000006089871', '000000000ff54fcbffffffffbbe16acc');
INSERT INTO `sys_role_menu` VALUES ('ffffffffe6e7412dffffffffd5a07e36', '0000000034775bc40000000006089871', '000000001585cf24ffffffffe093ab94');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa30298c5ffffffff862054fc', '0000000034775bc40000000006089871', '000000001dd3273c0000000029393519');
INSERT INTO `sys_role_menu` VALUES ('000000000fb8a8eb0000000064636094', '0000000034775bc40000000006089871', '00000000208ad7d20000000054cbadea');
INSERT INTO `sys_role_menu` VALUES ('ffffffffe77d7f7e000000003a00ab3e', '0000000034775bc40000000006089871', '000000002441c5fe000000005f6b6829');
INSERT INTO `sys_role_menu` VALUES ('ffffffff92d204fcffffffffbcb45efa', '0000000034775bc40000000006089871', '000000002d8402d60000000072f7dea2');
INSERT INTO `sys_role_menu` VALUES ('0000000053043239ffffffffda0ccf82', '0000000034775bc40000000006089871', '0000000035fbca03000000003a33d6b7');
INSERT INTO `sys_role_menu` VALUES ('ffffffff883ea3f0000000005fb6dff5', '0000000034775bc40000000006089871', '000000003a56d4fb0000000037016444');
INSERT INTO `sys_role_menu` VALUES ('000000005b717db6ffffffffb225402c', '0000000034775bc40000000006089871', '000000003cf86654000000006c39e14e');
INSERT INTO `sys_role_menu` VALUES ('ffffffffe00df948000000004acc2f4d', '0000000034775bc40000000006089871', '000000003d59e07400000000646ad837');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc037740effffffff9a6c18f9', '0000000034775bc40000000006089871', '00000000412eae8efffffffff6b97de1');
INSERT INTO `sys_role_menu` VALUES ('ffffffffccf386270000000060229031', '0000000034775bc40000000006089871', '00000000436e0e010000000069184e93');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa84343a4000000003d72ffcc', '0000000034775bc40000000006089871', '00000000438a8345ffffffffe75bf9f0');
INSERT INTO `sys_role_menu` VALUES ('0000000077693cbf000000004019be3e', '0000000034775bc40000000006089871', '00000000545f18ddffffffff9482ed27');
INSERT INTO `sys_role_menu` VALUES ('000000007388e6fe000000004e0ba9ee', '0000000034775bc40000000006089871', '000000005cdfacaaffffffffbb1e36dc');
INSERT INTO `sys_role_menu` VALUES ('000000002bf7cbb4000000002e956e44', '0000000034775bc40000000006089871', '000000005e02492a0000000033b9c62b');
INSERT INTO `sys_role_menu` VALUES ('fffffffffcb8a026fffffffffaf2bd76', '0000000034775bc40000000006089871', '00000000643b51a3000000004689a047');
INSERT INTO `sys_role_menu` VALUES ('ffffffffdec3d906ffffffffe2be547c', '0000000034775bc40000000006089871', '000000007330a2a9ffffffff98cebe66');
INSERT INTO `sys_role_menu` VALUES ('00000000784ca284ffffffffcec3b325', '0000000034775bc40000000006089871', '0000000077224459ffffffffc9d752ca');
INSERT INTO `sys_role_menu` VALUES ('ffffffffed442ad9000000006079ac54', '0000000034775bc40000000006089871', '000000007a3bebc1ffffffffc0d19222');
INSERT INTO `sys_role_menu` VALUES ('ffffffffba38f5070000000073304f19', '0000000034775bc40000000006089871', '000000007aaad4a40000000016f6244f');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc6e26594ffffffffb416ebfb', '0000000034775bc40000000006089871', '000000007d24ec6fffffffffe5ac787c');
INSERT INTO `sys_role_menu` VALUES ('0000000053ab8ca2000000007bc05660', '0000000034775bc40000000006089871', '000000007ea75783000000004607fdec');
INSERT INTO `sys_role_menu` VALUES ('ffffffffb4dd7193ffffffffac4e22ea', '0000000034775bc40000000006089871', 'ffffffff81c3d84800000000742c484e');
INSERT INTO `sys_role_menu` VALUES ('ffffffffec0e3d95000000001fd5ffa2', '0000000034775bc40000000006089871', 'ffffffff8407cc4dffffffffdbdad884');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc172f788ffffffff9549526b', '0000000034775bc40000000006089871', 'ffffffff84949f41ffffffffcc0503ed');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd12c6f5affffffffeea7ffec', '0000000034775bc40000000006089871', 'ffffffff84e5da13fffffffface485ec');
INSERT INTO `sys_role_menu` VALUES ('ffffffff89176361fffffffffc17de0f', '0000000034775bc40000000006089871', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_role_menu` VALUES ('fffffffff268becd00000000264552af', '0000000034775bc40000000006089871', 'ffffffff8c468a55ffffffff8f59d635');
INSERT INTO `sys_role_menu` VALUES ('0000000072870c5d0000000078d1599f', '0000000034775bc40000000006089871', 'ffffffff93ca53bcffffffffc7ca6854');
INSERT INTO `sys_role_menu` VALUES ('000000001fd92487000000000dee576f', '0000000034775bc40000000006089871', 'ffffffff983d99e6ffffffff9a4c304c');
INSERT INTO `sys_role_menu` VALUES ('0000000051b1ccb8ffffffffb9134b7c', '0000000034775bc40000000006089871', 'ffffffff9db05c1000000000002e5179');
INSERT INTO `sys_role_menu` VALUES ('000000001baab28affffffffc867bc67', '0000000034775bc40000000006089871', 'ffffffff9dcbdcebffffffff99573f80');
INSERT INTO `sys_role_menu` VALUES ('0000000079e44467ffffffffece0f080', '0000000034775bc40000000006089871', 'ffffffffb02530130000000015e4ac7b');
INSERT INTO `sys_role_menu` VALUES ('000000002d35cce7ffffffffd69e33fb', '0000000034775bc40000000006089871', 'ffffffffb71281a2ffffffffd931eb95');
INSERT INTO `sys_role_menu` VALUES ('00000000171762f00000000055fcc5b8', '0000000034775bc40000000006089871', 'ffffffffbca39996ffffffffcd270f17');
INSERT INTO `sys_role_menu` VALUES ('0000000030cd3300ffffffffefd25277', '0000000034775bc40000000006089871', 'ffffffffc0336ff70000000041d6b048');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd1cd937fffffffffe3230302', '0000000034775bc40000000006089871', 'ffffffffc43097b8000000003a72e62f');
INSERT INTO `sys_role_menu` VALUES ('00000000242727ddffffffffe122d6e0', '0000000034775bc40000000006089871', 'ffffffffc9421e24ffffffffe4170035');
INSERT INTO `sys_role_menu` VALUES ('000000005782470effffffffac49c2fa', '0000000034775bc40000000006089871', 'ffffffffc9b40d9c0000000067265dc3');
INSERT INTO `sys_role_menu` VALUES ('ffffffffba841d46ffffffff85c7fda4', '0000000034775bc40000000006089871', 'ffffffffcc4ea12c00000000032c1a2d');
INSERT INTO `sys_role_menu` VALUES ('0000000072a0bd73ffffffffd546fbf3', '0000000034775bc40000000006089871', 'ffffffffd07fcead000000006e6b1c2b');
INSERT INTO `sys_role_menu` VALUES ('000000001ff2a085ffffffffd967b5d8', '0000000034775bc40000000006089871', 'ffffffffd9a2e70e0000000052db7ed0');
INSERT INTO `sys_role_menu` VALUES ('00000000639bcf4bffffffffbb504f54', '0000000034775bc40000000006089871', 'ffffffffdc83a84dffffffffe53ca925');
INSERT INTO `sys_role_menu` VALUES ('00000000558bd9f40000000072a78746', '0000000034775bc40000000006089871', 'ffffffffde74f975ffffffffd8a45e27');
INSERT INTO `sys_role_menu` VALUES ('ffffffff81395f0bffffffff95bc2e25', '0000000034775bc40000000006089871', 'ffffffffe066d879000000002bb034af');
INSERT INTO `sys_role_menu` VALUES ('fffffffffdeda1e4000000007ef7aa62', '0000000034775bc40000000006089871', 'ffffffffe9667397ffffffff89a7556f');
INSERT INTO `sys_role_menu` VALUES ('000000003aa7ee71ffffffffa56e61d6', '0000000034775bc40000000006089871', 'ffffffffea08dbadffffffffd38460b5');
INSERT INTO `sys_role_menu` VALUES ('ffffffff896bccf9ffffffffa6333256', '0000000034775bc40000000006089871', 'ffffffffef4aaccd000000000bb43432');
INSERT INTO `sys_role_menu` VALUES ('0000000060cbd29bffffffffce0dec37', '0000000034775bc40000000006089871', 'fffffffff794cb2b0000000056983a09');
INSERT INTO `sys_role_menu` VALUES ('fffffffffb5bfbcd000000001d6a1bfe', '0000000034775bc40000000006089871', 'ffffffffff8446b400000000171caa51');
INSERT INTO `sys_role_menu` VALUES ('00000000568cd830ffffffff81c4da46', '000000003c05b1fb00000000153a847a', '000000000565166500000000620209a9');
INSERT INTO `sys_role_menu` VALUES ('ffffffff8263bb8dffffffffb3bed603', '000000003c05b1fb00000000153a847a', '0000000009bc5e6dffffffffa6cc1b40');
INSERT INTO `sys_role_menu` VALUES ('000000002657f22c00000000313d1d92', '000000003c05b1fb00000000153a847a', '000000001585cf24ffffffffe093ab94');
INSERT INTO `sys_role_menu` VALUES ('000000006fa0eecaffffffffd58f6263', '000000003c05b1fb00000000153a847a', '00000000208ad7d20000000054cbadea');
INSERT INTO `sys_role_menu` VALUES ('ffffffffcfb69f40ffffffff92010614', '000000003c05b1fb00000000153a847a', '00000000412eae8efffffffff6b97de1');
INSERT INTO `sys_role_menu` VALUES ('ffffffffdf69c3fbffffffffcfb19cfc', '000000003c05b1fb00000000153a847a', '00000000438a8345ffffffffe75bf9f0');
INSERT INTO `sys_role_menu` VALUES ('0000000062ac3eb600000000739771d4', '000000003c05b1fb00000000153a847a', '00000000643b51a3000000004689a047');
INSERT INTO `sys_role_menu` VALUES ('000000001ef0f754ffffffff842abf1d', '000000003c05b1fb00000000153a847a', '000000007aaad4a40000000016f6244f');
INSERT INTO `sys_role_menu` VALUES ('00000000100b8e8dffffffff8298e3eb', '000000003c05b1fb00000000153a847a', 'ffffffff84949f41ffffffffcc0503ed');
INSERT INTO `sys_role_menu` VALUES ('ffffffffef6911ecffffffffcefce1ec', '000000003c05b1fb00000000153a847a', 'ffffffff93ca53bcffffffffc7ca6854');
INSERT INTO `sys_role_menu` VALUES ('0000000066276f83000000001c38872a', '000000003c05b1fb00000000153a847a', 'ffffffffb02530130000000015e4ac7b');
INSERT INTO `sys_role_menu` VALUES ('fffffffffd73aa31000000003a627ebd', '000000003c05b1fb00000000153a847a', 'ffffffffbca39996ffffffffcd270f17');
INSERT INTO `sys_role_menu` VALUES ('000000000e327d44000000001ebc0463', '000000003c05b1fb00000000153a847a', 'ffffffffc0336ff70000000041d6b048');
INSERT INTO `sys_role_menu` VALUES ('0000000027c419c90000000040e091ad', '000000003c05b1fb00000000153a847a', 'ffffffffc9b40d9c0000000067265dc3');
INSERT INTO `sys_role_menu` VALUES ('ffffffffb3ecce66000000001739fa64', '000000003c05b1fb00000000153a847a', 'ffffffffe066d879000000002bb034af');
INSERT INTO `sys_role_menu` VALUES ('ffffffff920bd501000000001f37257f', '000000003c05b1fb00000000153a847a', 'ffffffffe9667397ffffffff89a7556f');
INSERT INTO `sys_role_menu` VALUES ('000000006fc0bbbeffffffffea12b9b4', '000000003c05b1fb00000000153a847a', 'ffffffffea08dbadffffffffd38460b5');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa30ba5ed00000000328c4c5c', '000000003c05b1fb00000000153a847a', 'fffffffff794cb2b0000000056983a09');
INSERT INTO `sys_role_menu` VALUES ('0000000072a56473ffffffffca21b1e2', 'ffffffffa966a308ffffffff9a5e4c52', '000000000c1ac66cffffffffb9dde56c');
INSERT INTO `sys_role_menu` VALUES ('0000000051f31868000000007fafe7bf', 'ffffffffa966a308ffffffff9a5e4c52', '000000000ec31a77ffffffff8afa2e2c');
INSERT INTO `sys_role_menu` VALUES ('fffffffff490a9ce000000004efb83c8', 'ffffffffa966a308ffffffff9a5e4c52', '000000000ff54fcbffffffffbbe16acc');
INSERT INTO `sys_role_menu` VALUES ('000000000db641e8ffffffffb3e79799', 'ffffffffa966a308ffffffff9a5e4c52', '000000001dd3273c0000000029393519');
INSERT INTO `sys_role_menu` VALUES ('000000001f8d604cffffffffb4c3e0fb', 'ffffffffa966a308ffffffff9a5e4c52', '000000002441c5fe000000005f6b6829');
INSERT INTO `sys_role_menu` VALUES ('fffffffff717250a000000003b0101c0', 'ffffffffa966a308ffffffff9a5e4c52', '000000002d8402d60000000072f7dea2');
INSERT INTO `sys_role_menu` VALUES ('0000000042708bae000000006a1d3f96', 'ffffffffa966a308ffffffff9a5e4c52', '000000002e18cf48ffffffffab242e16');
INSERT INTO `sys_role_menu` VALUES ('ffffffffacc720b4ffffffffae8d9396', 'ffffffffa966a308ffffffff9a5e4c52', '000000003a56d4fb0000000037016444');
INSERT INTO `sys_role_menu` VALUES ('fffffffff15b5b9900000000281d9281', 'ffffffffa966a308ffffffff9a5e4c52', '000000003cf86654000000006c39e14e');
INSERT INTO `sys_role_menu` VALUES ('ffffffffae2e1741ffffffff90eed5d8', 'ffffffffa966a308ffffffff9a5e4c52', '000000003d59e07400000000646ad837');
INSERT INTO `sys_role_menu` VALUES ('00000000374c62e1000000005c2ac1e5', 'ffffffffa966a308ffffffff9a5e4c52', '00000000436e0e010000000069184e93');
INSERT INTO `sys_role_menu` VALUES ('0000000032623c74000000000ee78900', 'ffffffffa966a308ffffffff9a5e4c52', '00000000438a8345ffffffffe75bf9f0');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa8688ac20000000066074011', 'ffffffffa966a308ffffffff9a5e4c52', '00000000545f18ddffffffff9482ed27');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa8dcbc6b000000007f65c499', 'ffffffffa966a308ffffffff9a5e4c52', '000000005cdfacaaffffffffbb1e36dc');
INSERT INTO `sys_role_menu` VALUES ('ffffffffe063e81fffffffff9ef8bbb6', 'ffffffffa966a308ffffffff9a5e4c52', '000000005e02492a0000000033b9c62b');
INSERT INTO `sys_role_menu` VALUES ('fffffffff9da8721000000004df1a1f7', 'ffffffffa966a308ffffffff9a5e4c52', '000000007330a2a9ffffffff98cebe66');
INSERT INTO `sys_role_menu` VALUES ('ffffffffbf2f117f000000002a85defd', 'ffffffffa966a308ffffffff9a5e4c52', '0000000077224459ffffffffc9d752ca');
INSERT INTO `sys_role_menu` VALUES ('0000000024c8f62cffffffffb088c5f5', 'ffffffffa966a308ffffffff9a5e4c52', '000000007937b8de0000000034ef3b70');
INSERT INTO `sys_role_menu` VALUES ('fffffffff5cadb15ffffffffc04bec31', 'ffffffffa966a308ffffffff9a5e4c52', '000000007a3bebc1ffffffffc0d19222');
INSERT INTO `sys_role_menu` VALUES ('000000006a6cc71c000000007bdfd2aa', 'ffffffffa966a308ffffffff9a5e4c52', '000000007d24ec6fffffffffe5ac787c');
INSERT INTO `sys_role_menu` VALUES ('000000005901ee7d000000003cf45e65', 'ffffffffa966a308ffffffff9a5e4c52', '000000007ea75783000000004607fdec');
INSERT INTO `sys_role_menu` VALUES ('000000007553b0b6ffffffffb840142b', 'ffffffffa966a308ffffffff9a5e4c52', '111fffffbd911aa0ffffffffd5637fff');
INSERT INTO `sys_role_menu` VALUES ('ffffffff913de7660000000051827000', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff81c3d84800000000742c484e');
INSERT INTO `sys_role_menu` VALUES ('000000007791e3500000000075a08b7d', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff8407cc4dffffffffdbdad884');
INSERT INTO `sys_role_menu` VALUES ('000000001f41f623ffffffffcbcbaa0e', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff84e5da13fffffffface485ec');
INSERT INTO `sys_role_menu` VALUES ('000000002d7c0d8dffffffffd3d00dde', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff87212d7d000000005fbd6e98');
INSERT INTO `sys_role_menu` VALUES ('000000003ca2083100000000776df44a', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_role_menu` VALUES ('00000000119f1646ffffffffd7d69c16', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff8c468a55ffffffff8f59d635');
INSERT INTO `sys_role_menu` VALUES ('0000000021499e8100000000503737d7', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff8e6151ff0000000015f6e145');
INSERT INTO `sys_role_menu` VALUES ('0000000010d97f8fffffffffbadd9827', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff983d99e6ffffffff9a4c304c');
INSERT INTO `sys_role_menu` VALUES ('ffffffff828ce644000000005eb0ffb3', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff9db05c1000000000002e5179');
INSERT INTO `sys_role_menu` VALUES ('ffffffff92475b0fffffffffd9a4ab78', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffff9dcbdcebffffffff99573f80');
INSERT INTO `sys_role_menu` VALUES ('0000000024c0ebcd000000000d96acff', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffb71281a2ffffffffd931eb95');
INSERT INTO `sys_role_menu` VALUES ('000000004fd3dca600000000701ad8d9', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffc43097b8000000003a72e62f');
INSERT INTO `sys_role_menu` VALUES ('000000000c94135b000000007c8e68de', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffc9421e24ffffffffe4170035');
INSERT INTO `sys_role_menu` VALUES ('000000002102392affffffffb16bfb06', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffcc4ea12c00000000032c1a2d');
INSERT INTO `sys_role_menu` VALUES ('ffffffffeec6f99fffffffffba26f2ea', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffd07fcead000000006e6b1c2b');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa48baeef0000000004bf72af', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffd9a2e70e0000000052db7ed0');
INSERT INTO `sys_role_menu` VALUES ('0000000006a97230000000004e591cdd', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffdae3cd4affffffff9894b623');
INSERT INTO `sys_role_menu` VALUES ('0000000035f8a1e300000000449bf873', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffdc83a84dffffffffe53ca925');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa5957646ffffffff86aead9b', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffde74f975ffffffffd8a45e27');
INSERT INTO `sys_role_menu` VALUES ('0000000068a774c0000000000d337127', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffdfaf333c000000001760f4a5');
INSERT INTO `sys_role_menu` VALUES ('0000000035062d1affffffffa138f147', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffef4aaccd000000000bb43432');
INSERT INTO `sys_role_menu` VALUES ('ffffffffabd89496ffffffff88fd81ea', 'ffffffffa966a308ffffffff9a5e4c52', 'fffffffffaad7649ffffffffe7c3d6a6');
INSERT INTO `sys_role_menu` VALUES ('000000000c5c64e40000000028b8c040', 'ffffffffa966a308ffffffff9a5e4c52', 'ffffffffff8446b400000000171caa51');
INSERT INTO `sys_role_menu` VALUES ('00000000011c8485ffffffffb710a352', 'ffffffffde90da3fffffffffdef7150f', '000000000565166500000000620209a9');
INSERT INTO `sys_role_menu` VALUES ('000000002efc2947000000000fd8a8e0', 'ffffffffde90da3fffffffffdef7150f', '0000000006a9099300000000100d10cd');
INSERT INTO `sys_role_menu` VALUES ('000000005b5c3e35ffffffffeb6e3d82', 'ffffffffde90da3fffffffffdef7150f', '0000000009bc5e6dffffffffa6cc1b40');
INSERT INTO `sys_role_menu` VALUES ('ffffffffd7f00a5200000000004cf00b', 'ffffffffde90da3fffffffffdef7150f', '000000000ba93c43ffffffffe5c59508');
INSERT INTO `sys_role_menu` VALUES ('000000003474d3cc00000000407bff1e', 'ffffffffde90da3fffffffffdef7150f', '000000000c1ac66cffffffffb9dde56c');
INSERT INTO `sys_role_menu` VALUES ('0000000047fc7a37ffffffff818ddb06', 'ffffffffde90da3fffffffffdef7150f', '000000000ec31a77ffffffff8afa2e2c');
INSERT INTO `sys_role_menu` VALUES ('000000006017fc93ffffffffe1d51cbf', 'ffffffffde90da3fffffffffdef7150f', '000000000ff54fcbffffffffbbe16acc');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc9ea40650000000015bc019e', 'ffffffffde90da3fffffffffdef7150f', '000000001585cf24ffffffffe093ab94');
INSERT INTO `sys_role_menu` VALUES ('000000005e71e1d7ffffffffe978548d', 'ffffffffde90da3fffffffffdef7150f', '000000001dd3273c0000000029393519');
INSERT INTO `sys_role_menu` VALUES ('00000000284ac90b00000000593a0602', 'ffffffffde90da3fffffffffdef7150f', '00000000208ad7d20000000054cbadea');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc59a8684000000001a43d729', 'ffffffffde90da3fffffffffdef7150f', '000000002441c5fe000000005f6b6829');
INSERT INTO `sys_role_menu` VALUES ('ffffffff92ae3237ffffffffdfcba227', 'ffffffffde90da3fffffffffdef7150f', '000000002d8402d60000000072f7dea2');
INSERT INTO `sys_role_menu` VALUES ('000000006b48caa20000000035767848', 'ffffffffde90da3fffffffffdef7150f', '000000002e18cf48ffffffffab242e16');
INSERT INTO `sys_role_menu` VALUES ('0000000050030867ffffffffa2be9e20', 'ffffffffde90da3fffffffffdef7150f', '0000000035fbca03000000003a33d6b7');
INSERT INTO `sys_role_menu` VALUES ('fffffffff8af53daffffffffe51cf8e4', 'ffffffffde90da3fffffffffdef7150f', '000000003a56d4fb0000000037016444');
INSERT INTO `sys_role_menu` VALUES ('0000000002af30710000000046a87197', 'ffffffffde90da3fffffffffdef7150f', '000000003cf86654000000006c39e14e');
INSERT INTO `sys_role_menu` VALUES ('ffffffff8e5cd6c300000000439ebe01', 'ffffffffde90da3fffffffffdef7150f', '000000003d59e07400000000646ad837');
INSERT INTO `sys_role_menu` VALUES ('ffffffff8dc68be40000000079a4f2db', 'ffffffffde90da3fffffffffdef7150f', '00000000412eae8efffffffff6b97de1');
INSERT INTO `sys_role_menu` VALUES ('000000005e7200460000000030084c25', 'ffffffffde90da3fffffffffdef7150f', '00000000436e0e010000000069184e93');
INSERT INTO `sys_role_menu` VALUES ('000000002140a0db0000000018ba3086', 'ffffffffde90da3fffffffffdef7150f', '00000000438a8345ffffffffe75bf9f0');
INSERT INTO `sys_role_menu` VALUES ('000000002e15a11000000000004a10d5', 'ffffffffde90da3fffffffffdef7150f', '00000000545f18ddffffffff9482ed27');
INSERT INTO `sys_role_menu` VALUES ('ffffffff820e4ffc000000000113746d', 'ffffffffde90da3fffffffffdef7150f', '0000000058e2ec83000000002f978e03');
INSERT INTO `sys_role_menu` VALUES ('ffffffffdf624ba50000000025f26fe3', 'ffffffffde90da3fffffffffdef7150f', '000000005c41138100000000288a6b45');
INSERT INTO `sys_role_menu` VALUES ('fffffffffb13916b000000006c779ebc', 'ffffffffde90da3fffffffffdef7150f', '000000005cdfacaaffffffffbb1e36dc');
INSERT INTO `sys_role_menu` VALUES ('000000002b01f031ffffffffb6a69e4e', 'ffffffffde90da3fffffffffdef7150f', '000000005e02492a0000000033b9c62b');
INSERT INTO `sys_role_menu` VALUES ('000000004fb8ade50000000023fec20e', 'ffffffffde90da3fffffffffdef7150f', '00000000643b51a3000000004689a047');
INSERT INTO `sys_role_menu` VALUES ('00000000188df731ffffffffa3f190b7', 'ffffffffde90da3fffffffffdef7150f', '000000007330a2a9ffffffff98cebe66');
INSERT INTO `sys_role_menu` VALUES ('00000000312e73d700000000547c2b73', 'ffffffffde90da3fffffffffdef7150f', '0000000077224459ffffffffc9d752ca');
INSERT INTO `sys_role_menu` VALUES ('ffffffff9305fdaa0000000061c3c41e', 'ffffffffde90da3fffffffffdef7150f', '000000007937b8de0000000034ef3b70');
INSERT INTO `sys_role_menu` VALUES ('000000005145a4d5ffffffffd2ae5396', 'ffffffffde90da3fffffffffdef7150f', '000000007a3bebc1ffffffffc0d19222');
INSERT INTO `sys_role_menu` VALUES ('0000000070d07629000000004d880e13', 'ffffffffde90da3fffffffffdef7150f', '000000007aaad4a40000000016f6244f');
INSERT INTO `sys_role_menu` VALUES ('ffffffff89d1739200000000577a3f80', 'ffffffffde90da3fffffffffdef7150f', '000000007d24ec6fffffffffe5ac787c');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa9824e9affffffff9a94f9d9', 'ffffffffde90da3fffffffffdef7150f', '000000007ea75783000000004607fdec');
INSERT INTO `sys_role_menu` VALUES ('0000000050b9ccfafffffffff2e2b7d7', 'ffffffffde90da3fffffffffdef7150f', '111fffffbd911aa0ffffffffd5637fff');
INSERT INTO `sys_role_menu` VALUES ('ffffffffee695942ffffffffc8d7db3e', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff81c3d84800000000742c484e');
INSERT INTO `sys_role_menu` VALUES ('fffffffff965b0affffffffff0eb8f7d', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8407cc4dffffffffdbdad884');
INSERT INTO `sys_role_menu` VALUES ('ffffffff937813a6000000005415df67', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff84949f41ffffffffcc0503ed');
INSERT INTO `sys_role_menu` VALUES ('000000006bf584e4ffffffff9d11859b', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff84e5da13fffffffface485ec');
INSERT INTO `sys_role_menu` VALUES ('00000000058b7475fffffffff5b094c0', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff87212d7d000000005fbd6e98');
INSERT INTO `sys_role_menu` VALUES ('0000000057c713b4000000004692eac4', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8b559df0ffffffff834bba04');
INSERT INTO `sys_role_menu` VALUES ('0000000049b06f53ffffffffaf82063d', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8c468a55ffffffff8f59d635');
INSERT INTO `sys_role_menu` VALUES ('0000000060c5d061ffffffffacf1fb77', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff8e6151ff0000000015f6e145');
INSERT INTO `sys_role_menu` VALUES ('0000000066af587bffffffffe551edf7', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff93ca53bcffffffffc7ca6854');
INSERT INTO `sys_role_menu` VALUES ('ffffffffe59be2ab000000003104ed2e', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff93f9086effffffffb4a239cd');
INSERT INTO `sys_role_menu` VALUES ('ffffffffb3dd9711ffffffffe0b9d046', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff983d99e6ffffffff9a4c304c');
INSERT INTO `sys_role_menu` VALUES ('0000000034a8bfcc000000005e18d81d', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff9db05c1000000000002e5179');
INSERT INTO `sys_role_menu` VALUES ('ffffffffa0d31f98fffffffff0cce354', 'ffffffffde90da3fffffffffdef7150f', 'ffffffff9dcbdcebffffffff99573f80');
INSERT INTO `sys_role_menu` VALUES ('000000003ec5ec72ffffffffc553b43b', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffb02530130000000015e4ac7b');
INSERT INTO `sys_role_menu` VALUES ('000000000161e52dffffffff8908458a', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffb71281a2ffffffffd931eb95');
INSERT INTO `sys_role_menu` VALUES ('000000001889cd4dfffffffff47d51d1', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffbca39996ffffffffcd270f17');
INSERT INTO `sys_role_menu` VALUES ('000000001d486dc8000000007422205e', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffc0336ff70000000041d6b048');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc6c8d582ffffffffa38c9f4c', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffc43097b8000000003a72e62f');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc30fb4ccffffffff8690c7e7', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffc9421e24ffffffffe4170035');
INSERT INTO `sys_role_menu` VALUES ('ffffffffb34ca744ffffffffae848e09', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffc9b40d9c0000000067265dc3');
INSERT INTO `sys_role_menu` VALUES ('000000004b3c7ecbffffffffbdbf6f86', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffcc4ea12c00000000032c1a2d');
INSERT INTO `sys_role_menu` VALUES ('0000000069784e930000000048fcba98', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffd07fcead000000006e6b1c2b');
INSERT INTO `sys_role_menu` VALUES ('ffffffffea1dda910000000034101d06', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffd9a2e70e0000000052db7ed0');
INSERT INTO `sys_role_menu` VALUES ('0000000035cca83fffffffff9d155b5e', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffdae3cd4affffffff9894b623');
INSERT INTO `sys_role_menu` VALUES ('000000000bc0a69a0000000047ac0f44', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffdc83a84dffffffffe53ca925');
INSERT INTO `sys_role_menu` VALUES ('ffffffff9b72306bffffffffe16b7714', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffddf9f51ffffffffff6157ca3');
INSERT INTO `sys_role_menu` VALUES ('ffffffffc3b1df7e000000003db13a96', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffde74f975ffffffffd8a45e27');
INSERT INTO `sys_role_menu` VALUES ('000000006b1bd1baffffffffb40bd587', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffdfaf333c000000001760f4a5');
INSERT INTO `sys_role_menu` VALUES ('000000003db3bde7ffffffff8d850ebc', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffe066d879000000002bb034af');
INSERT INTO `sys_role_menu` VALUES ('ffffffffeea0ec73ffffffffc0617e10', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffe56e8ef0ffffffff912af74a');
INSERT INTO `sys_role_menu` VALUES ('ffffffffcac2c61ffffffffffc70dc69', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffe9667397ffffffff89a7556f');
INSERT INTO `sys_role_menu` VALUES ('0000000047fae7d3ffffffff935547b8', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffea08dbadffffffffd38460b5');
INSERT INTO `sys_role_menu` VALUES ('000000002788f09f0000000060cbb60c', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffef4aaccd000000000bb43432');
INSERT INTO `sys_role_menu` VALUES ('ffffffffed693a1cffffffffd68ccada', 'ffffffffde90da3fffffffffdef7150f', 'fffffffff051bf04ffffffffa2ae3c8e');
INSERT INTO `sys_role_menu` VALUES ('ffffffff94510cfdffffffffda0ebaed', 'ffffffffde90da3fffffffffdef7150f', 'fffffffff794cb2b0000000056983a09');
INSERT INTO `sys_role_menu` VALUES ('fffffffffc7b0b76ffffffffddb1cf62', 'ffffffffde90da3fffffffffdef7150f', 'fffffffffaad7649ffffffffe7c3d6a6');
INSERT INTO `sys_role_menu` VALUES ('00000000293df052ffffffff88ce8917', 'ffffffffde90da3fffffffffdef7150f', 'ffffffffff8446b400000000171caa51');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `type` smallint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '用户名|账号类型(1系统账号;2注册账号;)',
  `add_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `enabled` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否冻结（0正常1冻结）',
  `logintime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后登录时间',
  `times` int(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '已成功登录次数,默认为1，每次累加1',
  `error_count` smallint(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '登录错误次数',
  `error_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '记录登录错误的时刻',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账号用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('0000000007b9739a00000000365a7430', 'syxx', 1, '2021-05-12 00:11:49', 0, '2021-05-12 00:12:22', 2, 0, '2021-05-12 00:12:22');
INSERT INTO `sys_user` VALUES ('000000001d7593670000000079676f55', 'lhgc', 1, '2021-05-10 13:18:06', 0, '2021-05-11 18:14:25', 6, 0, '2021-05-11 18:14:25');
INSERT INTO `sys_user` VALUES ('000000003cdd6d1d000000007bcafc09', 'operator', 1, '2021-05-10 12:50:53', 0, '2021-05-10 12:50:53', 1, 0, '2021-05-10 12:50:53');
INSERT INTO `sys_user` VALUES ('00000000473e07bcffffffffc51c4fcf', 'hbt', 1, '2021-05-12 00:14:00', 0, '2021-05-12 00:16:28', 2, 0, '2021-05-12 00:16:28');
INSERT INTO `sys_user` VALUES ('ffffffffddf9f1ffffffffff88888888', 'admin', 1, '2020-04-15 22:39:30', 0, '2021-05-12 00:13:25', 224, 0, '2021-05-12 00:13:25');
INSERT INTO `sys_user` VALUES ('ffffffffde4c407b000000005d6ffe19', 'super', 1, '2020-10-28 09:04:10', 0, '2021-05-10 17:51:06', 7, 0, '2021-05-10 17:51:06');

-- ----------------------------
-- Table structure for sys_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_menu`;
CREATE TABLE `sys_user_menu`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户菜单(私有菜单)id',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id(sys_user.kid)',
  `menu_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单id(sys_menu.kid)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`kid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_password
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_password`;
CREATE TABLE `sys_user_password`  (
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'user主键(sys_user.kid)',
  `user_password` char(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号密码',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `index_user_name`(`user_password`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户密码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_password
-- ----------------------------
INSERT INTO `sys_user_password` VALUES ('ffffffffddf9f1ffffffffff88888888', '$2a$10$2swupMvymbSVqW1qHWaxCuI4rv/Er6EdSqlVTKqe.wTqgGUGhLaDm');
INSERT INTO `sys_user_password` VALUES ('000000003cdd6d1d000000007bcafc09', '$2a$10$BIdGngEo8ogRAaCnSn7qD.FXy3s3dveXEemMXS87ePwJ9ZURBPIfO');
INSERT INTO `sys_user_password` VALUES ('00000000473e07bcffffffffc51c4fcf', '$2a$10$dwKHCJRrBKTRtOx1DqbbZO/0hm9BMEhvW20n6iLh8.b14nR8g/ANa');
INSERT INTO `sys_user_password` VALUES ('ffffffffde4c407b000000005d6ffe19', '$2a$10$fwJo3d.nwiXM1hJluFjg2uEa7oCCO58Qwyo4LRaSfDfaFxar51BWC');
INSERT INTO `sys_user_password` VALUES ('0000000007b9739a00000000365a7430', '$2a$10$gUjRd0uOsu3LxS5dkElfae5zan8GkaTeD4vNp3ydVKfWoYpZHDO0W');
INSERT INTO `sys_user_password` VALUES ('000000001d7593670000000079676f55', '$2a$10$skaUmmvZnqAauZa26PUh8uiAKnkPvkLB1.KjuopIK09ZfujlbOGq2');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `kid` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名|账号的id(sys_user.kid)',
  `role_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色的id(sys_role.kid)',
  PRIMARY KEY (`kid`) USING BTREE,
  UNIQUE INDEX `index_user_role`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('a830d1cbb27311ebbb080242ac110002', '0000000007b9739a00000000365a7430', '000000003c05b1fb00000000153a847a');
INSERT INTO `sys_user_role` VALUES ('7d84c98fb17b11eba42d0242ac110002', '000000001d7593670000000079676f55', '000000003c05b1fb00000000153a847a');
INSERT INTO `sys_user_role` VALUES ('393ff7d6b27411ebbb080242ac110002', '00000000473e07bcffffffffc51c4fcf', '0000000034775bc40000000006089871');
INSERT INTO `sys_user_role` VALUES ('ffffffffa04b32a6ffffffffba40ffff', 'ffffffffddf9f1ffffffffff88888888', 'ffffffffa04b32a6ffffffffba40a72e');
INSERT INTO `sys_user_role` VALUES ('fffffffff051bf04ffffffffa2afffff', 'ffffffffddf9f1ffffffffff88888888', 'ffffffffde90da3fffffffffdef7150f');
INSERT INTO `sys_user_role` VALUES ('31b397edb17511eba42d0242ac110002', 'ffffffffde4c407b000000005d6ffe19', 'ffffffffa966a308ffffffff9a5e4c52');

-- ----------------------------
-- View structure for view_user_login
-- ----------------------------
DROP VIEW IF EXISTS `view_user_login`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_user_login` AS select `su`.`kid` AS `kid`,`su`.`user_name` AS `user_name`,`sup`.`user_password` AS `user_password`,`su`.`enabled` AS `enabled`,`su`.`error_count` AS `error_count`,`su`.`error_time` AS `error_time` from (`sys_user` `su` join `sys_user_password` `sup`) where (`su`.`kid` = `sup`.`user_id`);

SET FOREIGN_KEY_CHECKS = 1;
