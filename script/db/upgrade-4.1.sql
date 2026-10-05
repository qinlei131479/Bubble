-- Bubble Cloud 4.1 schema migration for MySQL 8.
-- Run this file against the already-selected Bubble database.
-- It is intentionally idempotent and does not overwrite business data.

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `sys_api_key` (
  `id` bigint NOT NULL COMMENT 'API key id',
  `user_id` bigint NOT NULL COMMENT 'Owner user id',
  `username` varchar(64) NOT NULL COMMENT 'Owner username',
  `name` varchar(64) NOT NULL COMMENT 'Key name',
  `api_key_hash` varchar(64) NOT NULL COMMENT 'SHA-256 hash',
  `allowed_ips` varchar(512) DEFAULT NULL COMMENT 'Comma-separated IP whitelist',
  `expires_at` datetime DEFAULT NULL COMMENT 'Expiration time',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0 enabled, 1 disabled',
  `last_used_at` datetime DEFAULT NULL COMMENT 'Last used time',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT 'Logical delete flag',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_key_hash` (`api_key_hash`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='API key';

CREATE TABLE IF NOT EXISTS `sys_area` (
  `id` bigint unsigned NOT NULL COMMENT 'Area id',
  `pid` bigint unsigned NOT NULL DEFAULT '0' COMMENT 'Parent id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT 'Area name',
  `letter` varchar(255) DEFAULT '' COMMENT 'Area letter',
  `adcode` bigint NOT NULL COMMENT 'Amap area code',
  `location` varchar(255) DEFAULT '' COMMENT 'Longitude and latitude',
  `area_sort` bigint DEFAULT NULL COMMENT 'Sort value',
  `area_status` char(1) NOT NULL DEFAULT '1' COMMENT '0 disabled, 1 enabled',
  `area_type` char(1) NOT NULL DEFAULT '0' COMMENT '0 country, 1 province, 2 city, 3 district',
  `hot` char(1) NOT NULL DEFAULT '0' COMMENT '0 normal, 1 hot',
  `city_code` varchar(30) DEFAULT '' COMMENT 'City code',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Administrative area';

CREATE TABLE IF NOT EXISTS `sys_clarity_data` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `data_date` date NOT NULL COMMENT 'Data date',
  `total_sessions` int DEFAULT NULL COMMENT 'Total sessions',
  `distinct_users` int DEFAULT NULL COMMENT 'Distinct users',
  `pages_per_session` decimal(10,2) DEFAULT NULL COMMENT 'Pages per session',
  `scroll_depth` decimal(10,2) DEFAULT NULL COMMENT 'Average scroll depth',
  `dead_click_rate` decimal(10,2) DEFAULT NULL COMMENT 'Dead click rate',
  `rage_click_rate` decimal(10,2) DEFAULT NULL COMMENT 'Rage click rate',
  `device_data` text COMMENT 'Device distribution JSON',
  `top_urls` text COMMENT 'Top URL JSON',
  `num_of_days` tinyint NOT NULL DEFAULT 1 COMMENT 'Number of days',
  `fetch_status` varchar(10) NOT NULL DEFAULT 'pending' COMMENT 'pending, success, or failed',
  `referrer_url_data` text COMMENT 'Referrer URL JSON',
  `page_title_data` text COMMENT 'Page title JSON',
  `browser_data` text COMMENT 'Browser distribution JSON',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Clarity data cache';

CREATE TABLE IF NOT EXISTS `sys_file_group` (
  `id` bigint unsigned NOT NULL COMMENT 'Primary key',
  `type` tinyint unsigned DEFAULT 10 COMMENT '10 image, 20 video',
  `name` varchar(32) DEFAULT '' COMMENT 'Group name',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `pid` bigint DEFAULT NULL COMMENT 'Parent id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='File group';

CREATE TABLE IF NOT EXISTS `sys_i18n` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `name` varchar(255) NOT NULL COMMENT 'Identifier',
  `zh_cn` varchar(255) NOT NULL COMMENT 'Chinese text',
  `en` varchar(255) NOT NULL COMMENT 'English text',
  `create_by` varchar(64) DEFAULT ' ' COMMENT 'Creator',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `update_by` varchar(64) DEFAULT ' ' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Internationalization';

CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `category` varchar(255) DEFAULT NULL COMMENT 'Category',
  `title` varchar(255) DEFAULT NULL COMMENT 'Title',
  `content` text COMMENT 'Content',
  `send_flag` char(1) DEFAULT '0' COMMENT 'Push flag',
  `all_flag` char(1) DEFAULT '0' COMMENT 'Send-to-all flag',
  `sort` int unsigned NOT NULL DEFAULT 0 COMMENT 'Sort value',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `create_by` varchar(32) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(32) DEFAULT NULL COMMENT 'Updater',
  `del_flag` char(1) NOT NULL COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='In-app message';

CREATE TABLE IF NOT EXISTS `sys_message_relation` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `msg_id` bigint DEFAULT NULL COMMENT 'Message id',
  `user_id` bigint DEFAULT NULL COMMENT 'Receiver id',
  `content` text COMMENT 'Content',
  `read_flag` char(1) DEFAULT '0' COMMENT 'Read flag',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `create_by` varchar(32) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(32) DEFAULT NULL COMMENT 'Updater',
  `del_flag` char(1) NOT NULL COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='In-app message relation';

CREATE TABLE IF NOT EXISTS `sys_role_widget` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `role_id` bigint NOT NULL COMMENT 'Role id',
  `widget_keys` varchar(2000) NOT NULL COMMENT 'Allowed widget keys',
  `layout_config` text COMMENT 'Layout configuration JSON',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Role home widget';

CREATE TABLE IF NOT EXISTS `sys_schedule` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `title` varchar(255) DEFAULT NULL COMMENT 'Title',
  `schedule_type` varchar(255) DEFAULT NULL COMMENT 'Schedule type',
  `schedule_state` varchar(255) DEFAULT NULL COMMENT 'Schedule state',
  `content` text COMMENT 'Content',
  `schedule_time` time DEFAULT NULL COMMENT 'Schedule time',
  `schedule_date` date DEFAULT NULL COMMENT 'Schedule date',
  `create_by` varchar(64) DEFAULT ' ' COMMENT 'Creator',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `update_by` varchar(64) DEFAULT ' ' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Schedule';

CREATE TABLE IF NOT EXISTS `sys_sensitive_word` (
  `sensitive_id` bigint NOT NULL COMMENT 'Primary key',
  `sensitive_word` varchar(255) DEFAULT NULL COMMENT 'Sensitive word',
  `sensitive_type` char(1) DEFAULT NULL COMMENT 'Sensitive word type',
  `remark` varchar(255) DEFAULT NULL COMMENT 'Remark',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`sensitive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Sensitive word';

CREATE TABLE IF NOT EXISTS `sys_social_details` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `type` varchar(16) DEFAULT NULL COMMENT 'Social login type',
  `remark` varchar(64) DEFAULT NULL COMMENT 'Remark',
  `app_id` varchar(64) DEFAULT NULL COMMENT 'Application id',
  `app_secret` varchar(1024) DEFAULT NULL COMMENT 'Application secret',
  `redirect_url` varchar(128) DEFAULT NULL COMMENT 'Redirect URL',
  `ext` varchar(255) DEFAULT NULL COMMENT 'Extension',
  `create_by` varchar(64) DEFAULT ' ' COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT ' ' COMMENT 'Updater',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Social login account';

CREATE TABLE IF NOT EXISTS `sys_system_config` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `config_type` varchar(64) DEFAULT NULL COMMENT 'Config type',
  `config_name` varchar(255) DEFAULT NULL COMMENT 'Config name',
  `config_key` varchar(255) DEFAULT NULL COMMENT 'Config key',
  `config_value` longtext COMMENT 'Config value',
  `config_status` char(1) DEFAULT NULL COMMENT 'Config status',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Creation time',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='System config';

CREATE TABLE IF NOT EXISTS `sys_user_dept` (
  `user_id` bigint NOT NULL COMMENT 'User id',
  `dept_id` bigint NOT NULL COMMENT 'Department id',
  PRIMARY KEY (`user_id`, `dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='User department relation';

CREATE TABLE IF NOT EXISTS `gen_create_table` (
  `id` bigint NOT NULL COMMENT 'Primary key',
  `table_name` varchar(32) NOT NULL COMMENT 'Table name',
  `ds_name` varchar(32) DEFAULT NULL COMMENT 'Datasource name',
  `comments` varchar(512) DEFAULT NULL COMMENT 'Table comment',
  `create_by` varchar(64) DEFAULT NULL COMMENT 'Creator',
  `update_by` varchar(64) DEFAULT NULL COMMENT 'Updater',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `column_info` text NOT NULL COMMENT 'Column information JSON',
  `del_flag` char(1) DEFAULT NULL COMMENT 'Logical delete flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Codegen table creation';

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

CALL bubble_add_4_1_column('sys_menu', 'component', 'varchar(255) DEFAULT NULL COMMENT ''Component path''');
CALL bubble_add_4_1_column('sys_user', 'password_expire_flag', 'char(1) DEFAULT ''0'' COMMENT ''Password expire flag''');
CALL bubble_add_4_1_column('sys_user', 'password_modify_time', 'datetime DEFAULT NULL COMMENT ''Password modify time''');
CALL bubble_add_4_1_column('sys_user', 'wx_cp_userid', 'varchar(100) DEFAULT NULL COMMENT ''WeCom user id''');
CALL bubble_add_4_1_column('sys_user', 'wx_ding_userid', 'varchar(100) DEFAULT NULL COMMENT ''DingTalk user id''');
CALL bubble_add_4_1_column('sys_file', 'group_id', 'bigint DEFAULT NULL COMMENT ''File group id''');
CALL bubble_add_4_1_column('sys_file', 'dir', 'varchar(200) DEFAULT NULL COMMENT ''File directory''');
CALL bubble_add_4_1_column('sys_file', 'hash', 'varchar(50) DEFAULT NULL COMMENT ''File hash''');
CALL bubble_add_4_1_column('sys_dict_item', 'list_class', 'varchar(50) DEFAULT NULL COMMENT ''Tag type''');
CALL bubble_add_4_1_column('sys_job_log', 'scheduled_fire_time', 'datetime DEFAULT NULL COMMENT ''Scheduled fire time''');
CALL bubble_add_4_1_column('sys_job_log', 'fire_instance_id', 'varchar(128) DEFAULT NULL COMMENT ''Quartz fire instance id''');
CALL bubble_add_4_1_column('sys_job_log', 'dedup_status', 'char(1) DEFAULT ''0'' COMMENT ''Deduplication status''');
CALL bubble_add_4_1_column('gen_field_type', 'default_form_type', 'varchar(64) DEFAULT NULL COMMENT ''Default form type''');
CALL bubble_add_4_1_column('gen_field_type', 'default_query_form_type', 'varchar(64) DEFAULT NULL COMMENT ''Default query form type''');
CALL bubble_add_4_1_column('gen_table', 'sync_menu_id', 'bigint DEFAULT NULL COMMENT ''Synchronized menu id''');
CALL bubble_add_4_1_column('gen_table', 'sync_route', 'char(1) DEFAULT ''0'' COMMENT ''Synchronize route flag''');
CALL bubble_add_4_1_column('gen_table', 'parent_field', 'varchar(200) DEFAULT NULL COMMENT ''Parent field''');
CALL bubble_add_4_1_column('gen_table', 'name_field', 'varchar(200) DEFAULT NULL COMMENT ''Name field''');
CALL bubble_add_4_1_column('gen_table', 'package_common_name', 'varchar(200) DEFAULT NULL COMMENT ''Common package name''');
CALL bubble_add_4_1_column('gen_table', 'package_entity_name', 'varchar(200) DEFAULT NULL COMMENT ''Entity package name''');

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
