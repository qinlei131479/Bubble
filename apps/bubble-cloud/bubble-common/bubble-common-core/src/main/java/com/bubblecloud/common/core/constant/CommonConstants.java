package com.bubblecloud.common.core.constant;

/**
 * @author lengleng
 * @date 2017/10/29
 */
public interface CommonConstants {

	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * 删除
	 */
	String STATUS_DEL = "1";

	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单树根节点
	 */
	Long MENU_TREE_ROOT_ID = -1L;

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * 前端工程名
	 */
	String FRONT_END_PROJECT = "pig-ui";

	/**
	 * 移动端工程名
	 */
	String UNI_END_PROJECT = "pig-app";

	/**
	 * 后端工程名
	 */
	String BACK_END_PROJECT = "pig";

	/**
	 * 公共参数
	 */
	String PIG_PUBLIC_PARAM_KEY = "PIG_PUBLIC_PARAM_KEY";

	/**
	 * 成功标记
	 */
	Integer SUCCESS = 0;

	/**
	 * 失败标记
	 */
	Integer FAIL = 1;

	/**
	 * 默认存储bucket
	 */
	String BUCKET_NAME = "lengleng";

	/**
	 * 滑块验证码
	 */
	String IMAGE_CODE_BLOCK_PUZZLE = "blockPuzzle";

	/**
	 * 点击文字验证码类型
	 */
	String IMAGE_CODE_CLICK_WORD = "clickWord";

	/**
	 * 数学（算术）图形验证码类型
	 */
	String IMAGE_CODE_MATH = "math";

	/**
	 * math 验证码凭据分隔符，约定格式 {@code randomStr---code}
	 */
	String CAPTCHA_VERIFICATION_SEPARATOR = "---";

	/**
	 * 验证码开关
	 */
	String CAPTCHA_FLAG = "captcha_flag";

	/**
	 * 密码传输是否加密
	 */
	String ENC_FLAG = "enc_flag";

	/**
	 * 客户端允许同时在线数量
	 */
	String ONLINE_QUANTITY = "online_quantity";

	/**
	 * 请求开始时间
	 */
	String REQUEST_START_TIME = "REQUEST-START-TIME";

	/**
	 * 当前页
	 */
	String CURRENT = "current";

	/**
	 * size
	 */
	String SIZE = "size";

}
