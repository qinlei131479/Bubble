-- Bubble Cloud upgrade: system menus, i18n, and administrative areas.
-- Run against the Bubble business database after bubble.sql or upgrade-4.1.sql.
-- Idempotent: existing rows are kept, missing rows are inserted.

SET NAMES utf8mb4;

-- Align menu routes with the current Vue pages.
UPDATE sys_menu
SET path = '/admin/system/user/index'
WHERE menu_id = 1100
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/admin/system/user/index');

UPDATE sys_menu
SET path = '/admin/system/menu/index'
WHERE menu_id = 1200
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/admin/system/menu/index');

UPDATE sys_menu
SET path = '/admin/system/role/index'
WHERE menu_id = 1300
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/admin/system/role/index');

UPDATE sys_menu
SET path = '/admin/system/dept/index'
WHERE menu_id = 1400
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/admin/system/dept/index');

UPDATE sys_menu
SET path = '/admin/system/post/index'
WHERE menu_id = 1600
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/admin/system/post/index');

UPDATE sys_menu
SET path = '/tools/job-manage/index'
WHERE menu_id = 2800
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/tools/job-manage/index');

UPDATE sys_menu
SET path = '/tools/data/cache'
WHERE menu_id = 4002
  AND del_flag = '0'
  AND (path IS NULL OR path <> '/tools/data/cache');

-- Pages and button permissions required by the current admin APIs.
INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  1105, '用户查看', NULL, 'sys_user_view', NULL, 1100, NULL, '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1105 OR (permission = 'sys_user_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  1306, '角色查看', NULL, 'sys_role_view', NULL, 1300, NULL, '1',
  4, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1306 OR (permission = 'sys_role_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  1700, '网站配置', NULL, NULL, '/admin/siteconfig/index', 1000, 'iconfont icon-anquanjiance', '1',
  9, '0', '0', '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1700);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  1701, '网站配置查看', NULL, 'sys_site_config_view', NULL, 1700, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1701 OR (permission = 'sys_site_config_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  1702, '网站配置编辑', NULL, 'sys_site_config_edit', NULL, 1700, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 1702 OR (permission = 'sys_site_config_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2107, '日志查看', NULL, 'sys_log_view', NULL, 2001, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2107 OR (permission = 'sys_log_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2204, '字典查看', NULL, 'sys_dict_view', NULL, 2200, NULL, '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2204 OR (permission = 'sys_dict_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2214, '参数查看', NULL, 'sys_syspublicparam_view', NULL, 2210, NULL, '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2214 OR (permission = 'sys_syspublicparam_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2404, '客户端查看', NULL, 'sys_client_view', NULL, 2400, NULL, '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2404 OR (permission = 'sys_client_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2500, '密钥管理', NULL, NULL, '/admin/social/index', 2000, 'iconfont icon-miyueguanli', '1',
  10, '0', NULL, '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2500);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2501, '密钥新增', NULL, 'sys_social_details_add', NULL, 2500, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2501 OR (permission = 'sys_social_details_add' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2502, '密钥修改', NULL, 'sys_social_details_edit', NULL, 2500, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2502 OR (permission = 'sys_social_details_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2503, '密钥删除', NULL, 'sys_social_details_del', NULL, 2500, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2503 OR (permission = 'sys_social_details_del' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2504, '密钥查看', NULL, 'sys_social_details_view', NULL, 2500, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2504 OR (permission = 'sys_social_details_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2900, '国际化管理', NULL, NULL, '/admin/i18n/index', 2000, 'iconfont icon-zhongyingwenqiehuan', '1',
  8, '0', NULL, '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2900);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2901, '系统表-国际化查看', NULL, 'sys_i18n_view', NULL, 2900, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2901 OR (permission = 'sys_i18n_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2902, '系统表-国际化新增', NULL, 'sys_i18n_add', NULL, 2900, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2902 OR (permission = 'sys_i18n_add' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2903, '系统表-国际化修改', NULL, 'sys_i18n_edit', NULL, 2900, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2903 OR (permission = 'sys_i18n_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2904, '系统表-国际化删除', NULL, 'sys_i18n_del', NULL, 2900, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2904 OR (permission = 'sys_i18n_del' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2905, '导入导出', NULL, 'sys_i18n_export', NULL, 2900, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2905 OR (permission = 'sys_i18n_export' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2908, '查看文件', NULL, 'sys_file_view', NULL, 2906, NULL, '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2908 OR (permission = 'sys_file_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2910, '行政区划', NULL, NULL, '/admin/sysArea/index', 2000, 'iconfont icon-hangzhengquhuaguanli', '1',
  99, '0', NULL, '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2910);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2911, '行政区划表查看', NULL, 'sys_sysArea_view', NULL, 2910, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2911 OR (permission = 'sys_sysArea_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2912, '行政区划表新增', NULL, 'sys_sysArea_add', NULL, 2910, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2912 OR (permission = 'sys_sysArea_add' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2913, '行政区划表删除', NULL, 'sys_sysArea_del', NULL, 2910, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2913 OR (permission = 'sys_sysArea_del' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2914, '导入导出', NULL, 'sys_sysArea_export', NULL, 2910, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2914 OR (permission = 'sys_sysArea_export' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2915, '行政区划表修改', NULL, 'sys_sysArea_edit', NULL, 2910, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2915 OR (permission = 'sys_sysArea_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2920, '敏感词管理', NULL, NULL, '/admin/sensitive/index', 2000, 'iconfont icon-sensitiveword', '1',
  12, '0', NULL, '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2920);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2921, '敏感词查看', NULL, 'admin_sysSensitiveWord_view', NULL, 2920, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2921 OR (permission = 'admin_sysSensitiveWord_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2922, '敏感词新增', NULL, 'admin_sysSensitiveWord_add', NULL, 2920, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2922 OR (permission = 'admin_sysSensitiveWord_add' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2923, '敏感词修改', NULL, 'admin_sysSensitiveWord_edit', NULL, 2920, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2923 OR (permission = 'admin_sysSensitiveWord_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2924, '敏感词删除', NULL, 'admin_sysSensitiveWord_del', NULL, 2920, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2924 OR (permission = 'admin_sysSensitiveWord_del' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2925, '导入导出', NULL, 'admin_sysSensitiveWord_export', NULL, 2920, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2925 OR (permission = 'admin_sysSensitiveWord_export' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4003, '缓存监控查看', NULL, 'sys_cache_view', NULL, 4002, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4003 OR (permission = 'sys_cache_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4004, '站点统计', NULL, NULL, '/tools/data/clarity', 4000, 'iconfont icon-shuju', '1',
  2, '0', '0', '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4004);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4005, '站点统计查看', NULL, 'sys_clarity_view', NULL, 4004, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4005 OR (permission = 'sys_clarity_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4010, '信息推送', NULL, NULL, '/tools/message/index', 4000, 'iconfont icon-xinxituisong', '1',
  7, '0', NULL, '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4010);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4011, '信息推送查看', NULL, 'sys_message_view', NULL, 4010, '1', '1',
  0, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4011 OR (permission = 'sys_message_view' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4012, '信息推送新增', NULL, 'sys_message_add', NULL, 4010, '1', '1',
  1, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4012 OR (permission = 'sys_message_add' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4013, '信息推送修改', NULL, 'sys_message_edit', NULL, 4010, '1', '1',
  2, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4013 OR (permission = 'sys_message_edit' AND del_flag = '0'));

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  4014, '信息推送删除', NULL, 'sys_message_del', NULL, 4010, '1', '1',
  3, '0', NULL, '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 4014 OR (permission = 'sys_message_del' AND del_flag = '0'));

-- Keep API key buttons under Key Management. upgrade-4.1.sql inserts them at the root.
UPDATE sys_menu
SET parent_id = 2500, name = 'API密钥查看'
WHERE permission = 'admin_apikey_view' AND del_flag = '0';

UPDATE sys_menu
SET parent_id = 2500, name = 'API密钥删除'
WHERE permission = 'admin_apikey_del' AND del_flag = '0';

-- Grant the new menus to the administrator role only.
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
WHERE m.menu_id IN (1105, 1306, 1700, 1701, 1702, 2107, 2204, 2214, 2404, 2500, 2501, 2502, 2503, 2504, 2900, 2901, 2902, 2903, 2904, 2905, 2908, 2910, 2911, 2912, 2913, 2914, 2915, 2920, 2921, 2922, 2923, 2924, 2925, 4003, 4004, 4005, 4010, 4011, 4012, 4013, 4014)
  AND m.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm
    WHERE rm.role_id = 1 AND rm.menu_id = m.menu_id
  );

-- Menu titles used by the language switch.
INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1, 'router.permissionManagement', '权限管理', 'Permission Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1 OR name = 'router.permissionManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  2, 'router.userManagement', '用户管理', 'User Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 2 OR name = 'router.userManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  3, 'router.menuManagement', '菜单管理', 'Menu Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 3 OR name = 'router.menuManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  4, 'router.roleManagement', '角色管理', 'Role Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 4 OR name = 'router.roleManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  5, 'router.departmentManagement', '部门管理', 'Department Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 5 OR name = 'router.departmentManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  7, 'router.postManagement', '岗位管理', 'Post Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 7 OR name = 'router.postManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  8, 'router.systemManagement', '系统管理', 'System Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 8 OR name = 'router.systemManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  9, 'router.operationLog', '操作日志', 'Operation Log', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 9 OR name = 'router.operationLog');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  10, 'router.dictManagement', '字典管理', 'Dictionary Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 10 OR name = 'router.dictManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  11, 'router.parameterManagement', '参数管理', 'Parameter Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 11 OR name = 'router.parameterManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  12, 'router.codeGeneration', '代码生成', 'Code Generation', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 12 OR name = 'router.codeGeneration');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  13, 'router.terminalManagement', '终端管理', 'Terminal Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 13 OR name = 'router.terminalManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  14, 'router.keyManagement', '密钥管理', 'Key Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 14 OR name = 'router.keyManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  15, 'router.tokenManagement', '令牌管理', 'Token Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 15 OR name = 'router.tokenManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  16, 'router.quartzManagement', 'Quartz管理', 'Quartz Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 16 OR name = 'router.quartzManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  17, 'router.metadataManagement', '元数据管理', 'Metadata Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 17 OR name = 'router.metadataManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  18, 'router.documentExtension', '文档扩展', 'Document Extension', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 18 OR name = 'router.documentExtension');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  19, 'router.fileManagement', '文件管理', 'File Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 19 OR name = 'router.fileManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  20, 'router.platformDevelopment', '开发平台', 'Platform Development', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 20 OR name = 'router.platformDevelopment');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  21, 'router.dataSourceManagement', '数据源管理', 'Data Source Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 21 OR name = 'router.dataSourceManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  23, 'router.appManagement', 'APP管理', 'App Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 23 OR name = 'router.appManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  24, 'router.customerManagement', '客户管理', 'Customer Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 24 OR name = 'router.customerManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  25, 'router.appRole', 'APP角色', 'App Role', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 25 OR name = 'router.appRole');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  26, 'router.appPermission', 'APP权限', 'App Permission', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 26 OR name = 'router.appPermission');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  27, 'router.appKey', 'APP秘钥', 'App Key', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 27 OR name = 'router.appKey');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  28, 'router.internationalizationManagement', '国际化管理', 'Internationalization Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 28 OR name = 'router.internationalizationManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  30, 'router.systemMonitoring', '系统监控', 'System Monitoring', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 30 OR name = 'router.systemMonitoring');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  31, 'router.generatePages', '生成页面', 'Generate Pages', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 31 OR name = 'router.generatePages');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  32, 'router.templateManagement', '模板管理', 'Template Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 32 OR name = 'router.templateManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  33, 'router.templateGroup', '模板分组', 'Template Group', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 33 OR name = 'router.templateGroup');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  34, 'router.fieldManagement', '字段管理', 'Field Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 34 OR name = 'router.fieldManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  35, 'router.wechatPlatform', '公众号平台', 'WeChat Platform', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 35 OR name = 'router.wechatPlatform');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  36, 'router.accountManagement', '账号管理', 'Account Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 36 OR name = 'router.accountManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  37, 'router.menuSettings', '菜单设置', 'Menu Settings', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 37 OR name = 'router.menuSettings');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  38, 'router.fanManagement', '粉丝管理', 'Fan Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 38 OR name = 'router.fanManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  39, 'router.messageManagement', '消息管理', 'Message Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 39 OR name = 'router.messageManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  40, 'router.paymentSystem', '支付系统', 'Payment System', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 40 OR name = 'router.paymentSystem');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  41, 'router.checkoutCounter', '收银台', 'Checkout Counter', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 41 OR name = 'router.checkoutCounter');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  42, 'router.mediaManagement', '素材管理', 'Media Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 42 OR name = 'router.mediaManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  43, 'router.paymentChannel', '支付渠道', 'Payment Channel', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 43 OR name = 'router.paymentChannel');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  44, 'router.productOrder', '商品订单', 'Product Order', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 44 OR name = 'router.productOrder');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  45, 'router.notificationRecord', '通知记录', 'Notification Record', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 45 OR name = 'router.notificationRecord');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  46, 'router.refundOrder', '退款订单', 'Refund Order', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 46 OR name = 'router.refundOrder');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  47, 'router.paymentOrder', '支付订单', 'Payment Order', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 47 OR name = 'router.paymentOrder');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  48, 'router.autoReply', '自动回复', 'Auto Reply', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 48 OR name = 'router.autoReply');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  49, 'router.operationalData', '运营数据', 'Operational Data', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 49 OR name = 'router.operationalData');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  50, 'router.logManagement', '日志管理', 'Log Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 50 OR name = 'router.logManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  52, 'router.modelManagement', '模型管理', 'Model Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 52 OR name = 'router.modelManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  53, 'router.modelDiagramView', '模型图查看', 'Model Diagram View', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 53 OR name = 'router.modelDiagramView');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  55, 'router.leaveWorkOrder', '请假工单', 'Leave Work Order', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 55 OR name = 'router.leaveWorkOrder');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  56, 'router.todoTask', '我的待办', 'Todo Task', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 56 OR name = 'router.todoTask');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  57, 'router.tagManagement', '标签管理', 'Tag Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 57 OR name = 'router.tagManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  58, 'router.articleInformation', '文章资讯', 'Article Information', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 58 OR name = 'router.articleInformation');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  59, 'router.articleCategory', '文章分类', 'Article Category', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 59 OR name = 'router.articleCategory');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  60, 'router.interfaceSettings', '界面设置', 'Interface Settings', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 60 OR name = 'router.interfaceSettings');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  62, 'router.cacheMonitoring', '缓存监控', 'Cache Monitoring', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 62 OR name = 'router.cacheMonitoring');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  63, 'rotuer. initiateProcess', '发起流程', 'Initiate Process', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 63 OR name = 'rotuer. initiateProcess');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  64, 'router.taskManagement', '任务管理', 'Task Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 64 OR name = 'router.taskManagement');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  65, 'router.myInitiations', '我的发起', 'My Initiations', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 65 OR name = 'router.myInitiations');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  66, 'router.copiedtoMe', '抄送给我', 'Copied to Me', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 66 OR name = 'router.copiedtoMe');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  67, 'router.completedTasks', '我的已办', 'Completed Tasks', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 67 OR name = 'router.completedTasks');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  69, 'router.baseTools', '基础工具', 'Base Tools', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 69 OR name = 'router.baseTools');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  70, 'router.route', '路由管理', 'Route Management', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 70 OR name = 'router.route');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  71, 'router.datav', '大屏看板', 'Data Visual', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 71 OR name = 'router.datav');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  72, 'router.bi', '数据报表', 'Bi Report', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 72 OR name = 'router.bi');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  73, 'router.message', '信息推送', 'Message Push', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 73 OR name = 'router.message');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  74, 'router.sensitiveWords', '敏感词管理', 'Sensitive words', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 74 OR name = 'router.sensitiveWords');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  75, 'router.clarityMonitoring', '站点统计', 'Site Analytics', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 75 OR name = 'router.clarityMonitoring');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1000, 'router.local.1000', '网站配置', 'Site Settings', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1000 OR name = 'router.local.1000');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1001, 'router.local.1001', '行政区划', 'Area', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1001 OR name = 'router.local.1001');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1002, 'router.local.1002', '表单设计', 'Form Design', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1002 OR name = 'router.local.1002');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1003, 'router.local.1003', 'AI大模型', 'AI Models', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1003 OR name = 'router.local.1003');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1004, 'router.local.1004', '模型供应商', 'Model Providers', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1004 OR name = 'router.local.1004');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1005, 'router.local.1005', 'MCP服务管理', 'MCP Servers', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1005 OR name = 'router.local.1005');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1006, 'router.local.1006', '自然语言转SQL', 'Text to SQL', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1006 OR name = 'router.local.1006');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1007, 'router.local.1007', '知识库', 'Knowledge', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1007 OR name = 'router.local.1007');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1008, 'router.local.1008', '智能体', 'Agents', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1008 OR name = 'router.local.1008');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1009, 'router.local.1009', '对话管理', 'Conversations', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1009 OR name = 'router.local.1009');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1010, 'router.local.1010', '对话统计', 'Conversation Stats', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1010 OR name = 'router.local.1010');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1011, 'router.local.1011', '数据源配置', 'Datasource Config', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1011 OR name = 'router.local.1011');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1012, 'router.local.1012', 'SQL示例管理', 'SQL Examples', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1012 OR name = 'router.local.1012');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1013, 'router.local.1013', '术语管理', 'Terminology', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1013 OR name = 'router.local.1013');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1014, 'router.local.1014', '知识库配置', 'Knowledge Bases', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1014 OR name = 'router.local.1014');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1015, 'router.local.1015', '知识库文件', 'Knowledge Files', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1015 OR name = 'router.local.1015');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1016, 'router.local.1016', '评估基准', 'Evaluation Benchmarks', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1016 OR name = 'router.local.1016');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1017, 'router.local.1017', '评估结果', 'Evaluation Results', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1017 OR name = 'router.local.1017');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1018, 'router.local.1018', '评估结果明细', 'Evaluation Details', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1018 OR name = 'router.local.1018');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1019, 'router.local.1019', 'OA', 'OA', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1019 OR name = 'router.local.1019');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1101, 'router.apiKeyView', 'API密钥查看', 'API Key View', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1101 OR name = 'router.apiKeyView');

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1102, 'router.apiKeyDelete', 'API密钥删除', 'Delete API Key', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1102 OR name = 'router.apiKeyDelete');

-- Provincial administrative areas. City and county rows are maintained in the admin page.
INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  1, 0, '全国', '', 100000, '', NULL, '1', '0',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 1 OR adcode = 100000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  2, 100000, '北京市', '', 110000, '', NULL, '1', '1',
  '1', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 2 OR adcode = 110000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  3, 100000, '天津市', '', 120000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 3 OR adcode = 120000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  4, 100000, '河北省', '', 130000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 4 OR adcode = 130000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  5, 100000, '山西省', '', 140000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 5 OR adcode = 140000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  6, 100000, '内蒙古自治区', '', 150000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 6 OR adcode = 150000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  7, 100000, '辽宁省', '', 210000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 7 OR adcode = 210000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  8, 100000, '吉林省', '', 220000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 8 OR adcode = 220000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  9, 100000, '黑龙江省', '', 230000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 9 OR adcode = 230000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  10, 100000, '上海市', '', 310000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 10 OR adcode = 310000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  11, 100000, '江苏省', '', 320000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 11 OR adcode = 320000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  12, 100000, '浙江省', '', 330000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 12 OR adcode = 330000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  13, 100000, '安徽省', '', 340000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 13 OR adcode = 340000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  14, 100000, '福建省', '', 350000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 14 OR adcode = 350000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  15, 100000, '江西省', '', 360000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 15 OR adcode = 360000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  16, 100000, '山东省', '', 370000, '', 100, '1', '1',
  '1', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 16 OR adcode = 370000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  17, 100000, '河南省', '', 410000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 17 OR adcode = 410000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  18, 100000, '湖北省', '', 420000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 18 OR adcode = 420000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  19, 100000, '湖南省', '', 430000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 19 OR adcode = 430000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  20, 100000, '广东省', '', 440000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 20 OR adcode = 440000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  21, 100000, '广西壮族自治区', '', 450000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 21 OR adcode = 450000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  22, 100000, '海南省', '', 460000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 22 OR adcode = 460000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  23, 100000, '重庆市', '', 500000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 23 OR adcode = 500000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  24, 100000, '四川省', '', 510000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 24 OR adcode = 510000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  25, 100000, '贵州省', '', 520000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 25 OR adcode = 520000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  26, 100000, '云南省', '', 530000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 26 OR adcode = 530000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  27, 100000, '西藏自治区', '', 540000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 27 OR adcode = 540000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  28, 100000, '陕西省', '', 610000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 28 OR adcode = 610000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  29, 100000, '甘肃省', '', 620000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 29 OR adcode = 620000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  30, 100000, '青海省', '', 630000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 30 OR adcode = 630000);

INSERT INTO sys_area (
  id, pid, name, letter, adcode, location, area_sort, area_status, area_type,
  hot, city_code, create_by, create_time, update_by, update_time, del_flag
)
SELECT
  31, 100000, '宁夏回族自治区', '', 640000, '', NULL, '1', '1',
  '0', '', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_area WHERE id = 31 OR adcode = 640000);

-- Upstream menu omitted from the first pass: create-table under the dev platform.
INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  9070, '数据表管理', NULL, NULL, '/gen/create-table/index', 9000, 'iconfont icon-shujubiaoguanli1', '1',
  1, '0', '0', '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 9070);

INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  9071, '新增', NULL, 'codegen_table_add', NULL, 9070, NULL, '0',
  0, '0', '0', '1', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_menu WHERE menu_id = 9071 OR (permission = 'codegen_table_add' AND del_flag = '0')
);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
WHERE m.menu_id IN (9070, 9071)
  AND m.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm
    WHERE rm.role_id = 1 AND rm.menu_id = m.menu_id
  );

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1103, 'router.createTable', '数据表管理', 'Create Table', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1103 OR zh_cn = '数据表管理' OR name = 'router.createTable');

-- Audit log page under log management. Reuses sys_log (operation records).
INSERT INTO sys_menu (
  menu_id, name, en_name, permission, path, parent_id, icon, visible,
  sort_order, keep_alive, embedded, menu_type, create_by, create_time,
  update_by, update_time, del_flag
)
SELECT
  2110, '审计日志', NULL, NULL, '/admin/audit/index', 2001, 'ele-Document', '1',
  3, '0', '0', '0', 'admin', CURRENT_TIMESTAMP,
  'admin', NULL, '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 2110 OR (path = '/admin/audit/index' AND del_flag = '0'));

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, m.menu_id
FROM sys_menu m
WHERE m.menu_id = 2110
  AND m.del_flag = '0'
  AND NOT EXISTS (
    SELECT 1 FROM sys_role_menu rm
    WHERE rm.role_id = 1 AND rm.menu_id = m.menu_id
  );

INSERT INTO sys_i18n (id, name, zh_cn, en, create_by, create_time, update_by, del_flag)
SELECT
  1104, 'router.auditLog', '审计日志', 'Audit Log', 'admin', CURRENT_TIMESTAMP, 'admin', '0'
WHERE NOT EXISTS (SELECT 1 FROM sys_i18n WHERE id = 1104 OR name = 'router.auditLog');

-- Site config values (footer HTML, privacy tip, logo path) exceed varchar(128).
ALTER TABLE `sys_public_param`
  MODIFY COLUMN `public_value` varchar(2000) DEFAULT NULL COMMENT '值';

INSERT INTO sys_public_param (
  public_id, public_name, public_key, public_value, status, public_type, system_flag, del_flag, create_by, create_time
)
SELECT 30, '密码过期天数', 'PASSWORD_EXPIRE_DAYS', '90', '0', '0', '1', '0', 'admin', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_public_param WHERE public_key = 'PASSWORD_EXPIRE_DAYS' AND del_flag = '0');

INSERT INTO sys_public_param (
  public_id, public_name, public_key, public_value, status, public_type, system_flag, del_flag, create_by, create_time
)
SELECT 31, '登录失败锁定次数', 'LOGIN_ERROR_TIMES', '5', '0', '0', '1', '0', 'admin', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_public_param WHERE public_key = 'LOGIN_ERROR_TIMES' AND del_flag = '0');

