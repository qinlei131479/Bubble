-- Bubble Cloud 4.1 schema migration for MySQL 8.
-- Run this file against the already-selected Bubble database.
-- It is intentionally idempotent and does not overwrite business data.

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `sys_api_key` (
  `id`  bigint NOT NULL COMMENT '主键',
  `user_id`  bigint NOT NULL COMMENT '用户编号',
  `username`  varchar(64) NOT NULL COMMENT '所属用户',
  `name`  varchar(64) NOT NULL COMMENT '密钥名称',
  `api_key_hash`  varchar(64) NOT NULL COMMENT '密钥摘要',
  `allowed_ips`  varchar(512) DEFAULT NULL COMMENT '地址白名单',
  `expires_at`  datetime DEFAULT NULL COMMENT '过期时间',
  `status`   char(1) NOT NULL DEFAULT '0' COMMENT '状态，0正常，1禁用',
  `last_used_at`  datetime DEFAULT NULL COMMENT '最近使用',
  `del_flag`   char(1) NOT NULL DEFAULT '0' COMMENT '删除标记，1已删除，0正常',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_key_hash` (`api_key_hash`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='接口密钥';

CREATE TABLE IF NOT EXISTS `sys_area` (
  `id`  bigint unsigned NOT NULL COMMENT '主键',
  `pid`  bigint unsigned NOT NULL DEFAULT '0' COMMENT '上级区划',
  `name`  varchar(255) NOT NULL DEFAULT '' COMMENT '区划名称',
  `short_name` varchar(32) NOT NULL DEFAULT '' COMMENT '简称',
  `deep` tinyint DEFAULT NULL COMMENT '层级深度，0省，1市，2区，3镇',
  `letter`  varchar(255) DEFAULT '' COMMENT '首字母',
  `pinyin` varchar(64) NOT NULL DEFAULT '' COMMENT '完整拼音',
  `adcode`  bigint NOT NULL COMMENT '区划编码',
  `location`  varchar(255) DEFAULT '' COMMENT '经纬度',
  `area_sort`  bigint DEFAULT NULL COMMENT '排序值',
  `area_status`   char(1) NOT NULL DEFAULT '1' COMMENT '区划状态，0未生效，1生效',
  `area_type`   char(1) NOT NULL DEFAULT '0' COMMENT '区划类型，0国家，1省，2城市，3区县，4街道',
  `hot`   char(1) NOT NULL DEFAULT '0' COMMENT '是否热门，0否，1是',
  `city_code`  varchar(30) DEFAULT '' COMMENT '城市编码',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag`  char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='行政区划';

CREATE TABLE IF NOT EXISTS `sys_clarity_data` (
  `id`  bigint NOT NULL COMMENT '主键',
  `data_date`  date NOT NULL COMMENT '统计日期',
  `total_sessions`  int DEFAULT NULL COMMENT '会话总数',
  `distinct_users`  int DEFAULT NULL COMMENT '独立用户',
  `pages_per_session`  decimal(10,2) DEFAULT NULL COMMENT '人均页数',
  `scroll_depth`  decimal(10,2) DEFAULT NULL COMMENT '平均滚动',
  `dead_click_rate`  decimal(10,2) DEFAULT NULL COMMENT '无效点击率',
  `rage_click_rate`  decimal(10,2) DEFAULT NULL COMMENT '愤怒点击率',
  `device_data`  text COMMENT '设备分布',
  `top_urls`  text COMMENT '热门地址',
  `num_of_days`  tinyint NOT NULL DEFAULT 1 COMMENT '统计天数',
  `fetch_status`   varchar(10) NOT NULL DEFAULT 'pending' COMMENT '拉取状态，待处理、成功、失败',
  `referrer_url_data`  text COMMENT '来源分布',
  `page_title_data`  text COMMENT '页面标题',
  `browser_data`  text COMMENT '浏览器分布',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `del_flag`   char(1) DEFAULT '0' COMMENT '删除标记，1已删除，0正常',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='访问统计';

CREATE TABLE IF NOT EXISTS `sys_file_group` (
  `id`  bigint unsigned NOT NULL COMMENT '主键',
  `type`   tinyint unsigned DEFAULT 10 COMMENT '分组类型，10图片，20视频',
  `name`  varchar(32) DEFAULT '' COMMENT '分组名称',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag`   char(1) DEFAULT '0' COMMENT '删除标记，1已删除，0正常',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `pid`  bigint DEFAULT NULL COMMENT '上级分组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件分组';

CREATE TABLE IF NOT EXISTS `sys_i18n` (
  `id`  bigint NOT NULL COMMENT '主键',
  `name`  varchar(255) NOT NULL COMMENT '语言标识',
  `zh_cn`  varchar(255) NOT NULL COMMENT '中文内容',
  `en`  varchar(255) NOT NULL COMMENT '英文内容',
  `create_by`  varchar(64) DEFAULT ' ' COMMENT '创建人',
  `create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`  varchar(64) DEFAULT ' ' COMMENT '修改人',
  `update_time`  datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag`  char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='国际化';

CREATE TABLE IF NOT EXISTS `sys_message` (
  `id`  bigint NOT NULL COMMENT '主键',
  `category`   varchar(255) DEFAULT NULL COMMENT '分类，0公告，1站内信',
  `title`  varchar(255) DEFAULT NULL COMMENT '标题',
  `content`  text COMMENT '内容',
  `send_flag`  char(1) DEFAULT '0' COMMENT '推送标记',
  `all_flag`  char(1) DEFAULT '0' COMMENT '全员发送',
  `sort`  int unsigned NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `create_by`  varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag`  char(1) NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='站内消息';

CREATE TABLE IF NOT EXISTS `sys_message_relation` (
  `id`  bigint NOT NULL COMMENT '主键',
  `msg_id`  bigint DEFAULT NULL COMMENT '消息编号',
  `user_id`  bigint DEFAULT NULL COMMENT '用户编号',
  `content`  text COMMENT '内容',
  `read_flag`   char(1) DEFAULT '0' COMMENT '已读标记，0否，1是',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `create_by`  varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(32) DEFAULT NULL COMMENT '修改人',
  `del_flag`  char(1) NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息收件';

CREATE TABLE IF NOT EXISTS `sys_role_widget` (
  `id`  bigint NOT NULL COMMENT '主键',
  `role_id`  bigint NOT NULL COMMENT '角色编号',
  `widget_keys`  varchar(2000) NOT NULL COMMENT '组件标识',
  `layout_config`  text COMMENT '布局配置',
  `del_flag`   char(1) DEFAULT '0' COMMENT '删除标记，1已删除，0正常',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色组件';

CREATE TABLE IF NOT EXISTS `sys_schedule` (
  `id`  bigint NOT NULL COMMENT '主键',
  `title`  varchar(255) DEFAULT NULL COMMENT '标题',
  `schedule_type`  varchar(255) DEFAULT NULL COMMENT '日程类型',
  `schedule_state`  varchar(255) DEFAULT NULL COMMENT '日程状态',
  `content`  text COMMENT '内容',
  `schedule_time`  time DEFAULT NULL COMMENT '日程时间',
  `schedule_date`  date DEFAULT NULL COMMENT '日程日期',
  `create_by`  varchar(64) DEFAULT ' ' COMMENT '创建人',
  `create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`  varchar(64) DEFAULT ' ' COMMENT '修改人',
  `update_time`  datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag`  char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程安排';

CREATE TABLE IF NOT EXISTS `sys_sensitive_word` (
  `sensitive_id`  bigint NOT NULL COMMENT '主键',
  `sensitive_word`  varchar(255) DEFAULT NULL COMMENT '敏感词',
  `sensitive_type`  char(1) DEFAULT NULL COMMENT '敏感词类型',
  `remark`  varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag`  char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`sensitive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='敏感词';

CREATE TABLE IF NOT EXISTS `sys_social_details` (
  `id`  bigint NOT NULL COMMENT '主键',
  `type`  varchar(16) DEFAULT NULL COMMENT '登录类型',
  `remark`  varchar(64) DEFAULT NULL COMMENT '备注',
  `app_id`  varchar(64) DEFAULT NULL COMMENT '应用编号',
  `app_secret`  varchar(1024) DEFAULT NULL COMMENT '应用密钥',
  `redirect_url`  varchar(128) DEFAULT NULL COMMENT '回调地址',
  `ext`  varchar(255) DEFAULT NULL COMMENT '扩展信息',
  `create_by`  varchar(64) DEFAULT ' ' COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT ' ' COMMENT '修改人',
  `create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag`   char(1) DEFAULT '0' COMMENT '删除标记，1已删除，0正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='社交账号';

CREATE TABLE IF NOT EXISTS `sys_system_config` (
  `id`  bigint NOT NULL COMMENT '主键',
  `config_type`  varchar(64) DEFAULT NULL COMMENT '配置类型',
  `config_name`  varchar(255) DEFAULT NULL COMMENT '配置名称',
  `config_key`  varchar(255) DEFAULT NULL COMMENT '配置键名',
  `config_value`  longtext COMMENT '配置内容',
  `config_status`  char(1) DEFAULT NULL COMMENT '配置状态',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time`  datetime DEFAULT NULL COMMENT '创建时间',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag`  char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置';

CREATE TABLE IF NOT EXISTS `sys_user_dept` (
  `user_id`  bigint NOT NULL COMMENT '用户编号',
  `dept_id`  bigint NOT NULL COMMENT '部门编号',
  PRIMARY KEY (`user_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户部门';

CREATE TABLE IF NOT EXISTS `gen_create_table` (
  `id`  bigint NOT NULL COMMENT '主键',
  `table_name`  varchar(32) NOT NULL COMMENT '表名',
  `ds_name`  varchar(32) DEFAULT NULL COMMENT '数据源名',
  `comments`  varchar(512) DEFAULT NULL COMMENT '表注释',
  `create_by`  varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by`  varchar(64) DEFAULT NULL COMMENT '修改人',
  `create_time`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  datetime DEFAULT NULL COMMENT '修改时间',
  `column_info`  text NOT NULL COMMENT '字段信息',
  `del_flag`   char(1) DEFAULT NULL COMMENT '删除标记，1已删除，0正常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='建表记录';

DELIMITER $$

DROP PROCEDURE IF EXISTS `bubble_add_4_1_column` $$
CREATE PROCEDURE `bubble_add_4_1_column`(
  IN table_name_arg VARCHAR(64),
  IN column_name_arg VARCHAR(64),
  IN column_definition_arg TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = table_name_arg
      AND COLUMN_NAME = column_name_arg
  ) THEN
    SET @bubble_column_ddl = CONCAT(
      'ALTER TABLE `', table_name_arg,
      '` ADD COLUMN `', column_name_arg, '` ', column_definition_arg
    );
    PREPARE bubble_column_stmt FROM @bubble_column_ddl;
    EXECUTE bubble_column_stmt;
    DEALLOCATE PREPARE bubble_column_stmt;
  END IF;
END $$

DELIMITER ;

CALL bubble_add_4_1_column('sys_menu', 'component', 'varchar(255) DEFAULT NULL COMMENT ''组件路径''');
CALL bubble_add_4_1_column('sys_user', 'password_expire_flag', 'char(1) DEFAULT ''0'' COMMENT ''密码过期''');
CALL bubble_add_4_1_column('sys_user', 'password_modify_time', 'datetime DEFAULT NULL COMMENT ''改密时间''');
CALL bubble_add_4_1_column('sys_user', 'wx_cp_userid', 'varchar(100) DEFAULT NULL COMMENT ''企微账号''');
CALL bubble_add_4_1_column('sys_user', 'wx_ding_userid', 'varchar(100) DEFAULT NULL COMMENT ''钉钉账号''');
CALL bubble_add_4_1_column('sys_file', 'group_id', 'bigint DEFAULT NULL COMMENT ''分组编号''');
CALL bubble_add_4_1_column('sys_file', 'dir', 'varchar(200) DEFAULT NULL COMMENT ''文件目录''');
CALL bubble_add_4_1_column('sys_file', 'hash', 'varchar(50) DEFAULT NULL COMMENT ''文件摘要''');
CALL bubble_add_4_1_column('sys_dict_item', 'list_class', 'varchar(50) DEFAULT NULL COMMENT ''标签样式''');
CALL bubble_add_4_1_column('sys_job_log', 'scheduled_fire_time', 'datetime DEFAULT NULL COMMENT ''计划触发时间''');
CALL bubble_add_4_1_column('sys_job_log', 'fire_instance_id', 'varchar(128) DEFAULT NULL COMMENT ''触发实例号''');
CALL bubble_add_4_1_column('sys_job_log', 'dedup_status', 'char(1) DEFAULT ''0'' COMMENT ''去重状态，0正常执行，1重复触发，2运行中跳过''');
CALL bubble_add_4_1_column('gen_field_type', 'default_form_type', 'varchar(64) DEFAULT NULL COMMENT ''默认表单''');
CALL bubble_add_4_1_column('gen_field_type', 'default_query_form_type', 'varchar(64) DEFAULT NULL COMMENT ''默认查询项''');
CALL bubble_add_4_1_column('gen_table', 'sync_menu_id', 'bigint DEFAULT NULL COMMENT ''同步菜单''');
CALL bubble_add_4_1_column('gen_table', 'sync_route', 'char(1) DEFAULT ''0'' COMMENT ''同步路由''');
CALL bubble_add_4_1_column('gen_table', 'parent_field', 'varchar(200) DEFAULT NULL COMMENT ''父级字段''');
CALL bubble_add_4_1_column('gen_table', 'name_field', 'varchar(200) DEFAULT NULL COMMENT ''名称字段''');
CALL bubble_add_4_1_column('gen_table', 'package_common_name', 'varchar(200) DEFAULT NULL COMMENT ''项目公共模块包名''');
CALL bubble_add_4_1_column('gen_table', 'package_entity_name', 'varchar(200) DEFAULT NULL COMMENT ''项目实体模块包名''');

DROP PROCEDURE IF EXISTS `bubble_add_4_1_column`;

INSERT INTO sys_user_dept (user_id, dept_id)
SELECT u.user_id, u.dept_id
FROM sys_user u
WHERE u.dept_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM sys_user_dept ud
    WHERE ud.user_id = u.user_id
      AND ud.dept_id = u.dept_id
  );

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT 99000001, 'API Key Viewing', NULL, 'admin_apikey_view', NULL, 0, NULL, '1',
       99, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP, NULL, NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE permission = 'admin_apikey_view' AND del_flag = '0');

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT 99000002, 'API Key Deletion', NULL, 'admin_apikey_del', NULL, 0, NULL, '1',
       99, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP, NULL, NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE permission = 'admin_apikey_del' AND del_flag = '0');

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
WHERE m.permission IN ('admin_apikey_view', 'admin_apikey_del')
  AND m.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1
    FROM sys_role_menu rm
    WHERE rm.role_id = 1
      AND rm.menu_id = m.menu_id
  );
