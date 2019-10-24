SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` char(36) NOT NULL COMMENT '主键id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `name` varchar(255) NULL COMMENT '真实姓名',
  `phone` varchar(20) NULL COMMENT '手机号码',
  `enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否启用 1：已启用 0：禁止',
  `create_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1：已删除 0：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('20190801144916022418df456eb455466381', 'admin', '123456', '超级管理员', '', 1, '2019-08-01 00:00:00', '2019-08-01 00:00:00', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` char(36) NOT NULL COMMENT '主键id',
  `name` varchar(255) NULL DEFAULT NULL COMMENT '角色名',
  `code` varchar(255) NULL DEFAULT NULL COMMENT '角色编码',
  `create_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('201908011449160202894810bc9454487eba', '超级管理员', 'admin', '2019-08-01 00:00:00', '2019-08-01 00:00:00');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` char(36) NOT NULL COMMENT '用户id',
  `role_id` char(36) NOT NULL COMMENT '角色id',
  `create_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('20190801144916022418df456eb455466381', '201908011449160202894810bc9454487eba', '2019-08-01 00:00:00');

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
  `id` char(36) NOT NULL COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `title` varchar(255) DEFAULT NULL COMMENT '模块标题',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '模块编码',
  `parent_id` char(36) DEFAULT NULL COMMENT '模块的父模块id',
  `uri` varchar(255) DEFAULT NULL COMMENT '模块连接地址',
  `icon` varchar(255) DEFAULT NULL COMMENT '模块图标',
  `sort` int(11) DEFAULT '110' COMMENT '模块的排序',
  `is_menu` tinyint(1) NOT NULL DEFAULT '0' COMMENT '模块是否是菜单,0否 1是',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_sys_module_code` (`code`) USING BTREE,
  KEY `index_sys_module_parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('20190801183042011110caosxsdk94jrtjko', '系统设置', '系统设置', '00000000', NULL, '/sys', 'icon-test0', 1, 1, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('20190801183042027370c00056b41d4009af', '用户管理', '用户管理', '00010000', '20190801183042011110caosxsdk94jrtjko', '/sys/user', 'icon-test1', 1, 1, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420591f6dcffb555744da1b3', '新增', '新增', '00010001', '20190801183042027370c00056b41d4009af', 'sys:user:create', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420602b84dacfe617444a183', '修改', '修改', '00010002', '20190801183042027370c00056b41d4009af', 'sys:user:update', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('20190801183042061268d8e134f8d343dc95', '删除', '删除', '00010003', '20190801183042027370c00056b41d4009af', 'sys:user:delete', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420623b36722c73551425591', '详情', '详情', '00010004', '20190801183042027370c00056b41d4009af', 'sys:user:detail', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304206345c8f491664c5426798', '列表', '列表', '00010005', '20190801183042027370c00056b41d4009af', 'sys:user:list', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304206448c9317164f7646e183', '重置密码', '重置密码', '00010006', '20190801183042027370c00056b41d4009af', 'sys:user:resetPassword', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('20190801183042065552b4e0e6376c4320ac', '角色管理', '角色管理', '00020000', '20190801183042011110caosxsdk94jrtjko', '/sys/role', 'icon-test2', 2, 1, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304206666af834fb721d48ca82', '新增', '新增', '00020001', '20190801183042065552b4e0e6376c4320ac', 'sys:role:create', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420676112c5789b3fe4bc482', '修改', '修改', '00020002', '20190801183042065552b4e0e6376c4320ac', 'sys:role:update', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304206879e17bddadc7b41be8b', '删除', '删除', '00020003', '20190801183042065552b4e0e6376c4320ac', 'sys:role:delete', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420697cd8040da3f5c4baeaf', '详情', '详情', '00020004', '20190801183042065552b4e0e6376c4320ac', 'sys:role:detail', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304207131dd99a8b96244fa0a9', '列表', '列表', '00020005', '20190801183042065552b4e0e6376c4320ac', 'sys:role:list', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420724fe5d0afcc5e34e34a3', '模块管理', '模块管理', '00030000', '20190801183042011110caosxsdk94jrtjko', '/sys/module', 'icon-test3', 3, 1, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('2019080118304207346d69f60d3e174a6387', '新增', '新增', '00030001', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:create', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420745f82ab86078054231bb', '修改', '修改', '00030002', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:update', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('20190801183042075500c8b12dd03b4a199d', '删除', '删除', '00030003', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:delete', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420766c98b0effcd434f1487', '详情', '详情', '00030004', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:detail', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420777775f9fd65c96470792', '列表', '列表', '00030005', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:list', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');
INSERT INTO `sys_module` VALUES ('201908011830420787c4697552949c48dfad', '树', '树', '00030006', '201908011830420724fe5d0afcc5e34e34a3', 'sys:module:tree', NULL, 110, 0, '2019-08-01 00:00:00', '2019-08-01 00:00:00');

-- ----------------------------
-- Table structure for sys_role_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_module`;
CREATE TABLE `sys_role_module` (
  `role_id` char(36) NOT NULL COMMENT '角色id',
  `module_id` char(36) NOT NULL COMMENT '模块id',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`,`module_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色模块关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_module
-- ----------------------------
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '20190801183042011110caosxsdk94jrtjko', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '20190801183042027370c00056b41d4009af', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420591f6dcffb555744da1b3', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420602b84dacfe617444a183', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '20190801183042061268d8e134f8d343dc95', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420623b36722c73551425591', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304206345c8f491664c5426798', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304206448c9317164f7646e183', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '20190801183042065552b4e0e6376c4320ac', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304206666af834fb721d48ca82', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420676112c5789b3fe4bc482', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304206879e17bddadc7b41be8b', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420697cd8040da3f5c4baeaf', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304207131dd99a8b96244fa0a9', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420724fe5d0afcc5e34e34a3', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '2019080118304207346d69f60d3e174a6387', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420745f82ab86078054231bb', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '20190801183042075500c8b12dd03b4a199d', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420766c98b0effcd434f1487', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420777775f9fd65c96470792', '2019-08-01 00:00:00');
INSERT INTO `sys_role_module` VALUES ('201908011449160202894810bc9454487eba', '201908011830420787c4697552949c48dfad', '2019-08-01 00:00:00');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` char(36) NOT NULL COMMENT '主键id',
  `operation_event` varchar(255) NULL DEFAULT NULL COMMENT '操作事件',
  `operation_type` varchar(255) NULL DEFAULT NULL COMMENT '操作类型',
  `success` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否成功 1：成功 0：失败',
  `operation_user_id` char(36) NOT NULL COMMENT '操作者id',
  `operation_username` varchar(255) NOT NULL COMMENT '操作者用户名',
  `remote_addr` varchar(255) NULL DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) NULL DEFAULT NULL COMMENT '代理',
  `request_uri` varchar(4000) NULL DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) NULL DEFAULT NULL COMMENT '请求类型',
  `params` text NULL DEFAULT NULL COMMENT '请求参数',
  `exception` text NULL DEFAULT NULL COMMENT '异常信息',
  `create_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY `index_sys_log_id` (`id`) COMMENT '主键id' USING BTREE,
  KEY `index_sys_log_operation_event` (`operation_event`) COMMENT '操作事件' USING BTREE,
  KEY `index_sys_log_operation_type` (`operation_type`) COMMENT '操作类型' USING BTREE,
  KEY `index_sys_log_operation_user_id` (`operation_user_id`) COMMENT '操作者id' USING BTREE,
  KEY `index_sys_log_operation_username` (`operation_username`) COMMENT '操作者用户名' USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;