package com.bubblecloud.common.core.constant;

/**
 * 缓存 key 常量。统一使用 Bubble 前缀，避免和上游默认 key 混用。
 *
 * @author lengleng
 * @date 2019-04-28
 */
public interface CacheConstants {

	/**
	 * 每个项目定义一个顶级目录。新代码里的 GLOBALLY 与它是同一个前缀。
	 */
	String TOP = "bubble-cloud::";

	/**
	 * 全局缓存前缀。
	 */
	String GLOBALLY = TOP;

	/**
	 * oauth 缓存前缀
	 */
	String PROJECT_OAUTH_ACCESS = TOP + "token::access_token";

	/**
	 * 验证码前缀
	 */
	String DEFAULT_CODE_KEY = TOP + "DEFAULT_CODE_KEY:";

	/**
	 * 菜单信息缓存
	 */
	String MENU_DETAILS = TOP + "menu_details";

	/**
	 * 用户信息缓存
	 */
	String USER_DETAILS = TOP + "user_details";

	/**
	 * 移动端用户信息缓存
	 */
	String USER_DETAILS_MINI = TOP + "user_details_mini";

	/**
	 * 角色信息缓存
	 */
	String ROLE_DETAILS = TOP + "role_details";

	/**
	 * 字典信息缓存
	 */
	String DICT_DETAILS = TOP + "dict_details";

	/**
	 * oauth 客户端信息
	 */
	String CLIENT_DETAILS_KEY = TOP + "client:details";

	/**
	 * 参数缓存
	 */
	String PARAMS_DETAILS = TOP + "params_details";

	/**
	 * 登录错误次数
	 */
	String LOGIN_ERROR_TIMES = TOP + "login_error_times";

	/**
	 * API Key 缓存（hash -> SysApiKey）
	 */
	String API_KEY_DETAILS = TOP + "api_key_details";

	/**
	 * 网站配置聚合缓存（i18n + site config）
	 */
	String SITE_CONFIG_DETAILS = TOP + "site_config_details";

	/**
	 * 敏感词重新加载
	 */
	String SENSITIVE_REDIS_RELOAD_TOPIC = TOP + "sensitive_client_reload_topic";

	/**
	 * 公众号 reload
	 */
	String MP_REDIS_RELOAD_TOPIC = TOP + "mp_redis_reload_topic";

}
