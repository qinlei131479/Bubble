import { defineStore } from 'pinia';

/** 验证码类型枚举 */
export enum CaptchaType {
	ClickWord = 'clickWord',
	BlockPuzzle = 'blockPuzzle',
	Math = 'math',
	None = 'none',
}

/** 密码规则预设 */
export enum PasswordRule {
	LetterNumber = 'letterNumber',
	LetterNumberSymbol = 'letterNumberSymbol',
	UpperLowerNumberSymbol = 'upperLowerNumberSymbol',
}

/**
 * 网站配置类型定义
 */
export interface SiteConfig {
	clarityId: string;
	captchaType: CaptchaType | string;
	passwordRule: PasswordRule | string;
	forceResetPwd: boolean;
	forceLogout: boolean;
	smsLoginEnable: boolean;
	socialLoginEnable: boolean;
	registerEnable: boolean;
	resetPassword: boolean;
	i18nEnable: boolean;
	darkModeEnable: boolean;
	antiDebugEnable: boolean;
	antiDebugKey: string;
	title: string;
	footer: string;
	privacyTip: string;
	logo: string;
}

/** 默认配置（兜底值） */
const DEFAULT_SITE_CONFIG: SiteConfig = {
	clarityId: '',
	captchaType: CaptchaType.ClickWord,
	passwordRule: PasswordRule.LetterNumber,
	forceResetPwd: false,
	forceLogout: false,
	smsLoginEnable: true,
	socialLoginEnable: true,
	registerEnable: true,
	resetPassword: true,
	i18nEnable: true,
	darkModeEnable: true,
	antiDebugEnable: false,
	antiDebugKey: 'bubble',
	title: 'Bubble-UI',
	footer: '©2025 BUBBLE CLOUD',
	privacyTip: '',
	logo: '',
};

export const useSiteConfig = defineStore('siteConfig', {
	state: (): { siteConfig: SiteConfig } => ({
		siteConfig: { ...DEFAULT_SITE_CONFIG },
	}),
	actions: {
		setSiteConfig(config?: Partial<SiteConfig> | null) {
			const next: SiteConfig = { ...DEFAULT_SITE_CONFIG };
			if (config) {
				(Object.keys(next) as (keyof SiteConfig)[]).forEach((key) => {
					const value = config[key];
					if (value !== null && value !== undefined) {
						next[key] = value as SiteConfig[typeof key];
					}
				});
			}
			if (!next.passwordRule) {
				next.passwordRule = DEFAULT_SITE_CONFIG.passwordRule;
			}
			if (!next.captchaType) {
				next.captchaType = DEFAULT_SITE_CONFIG.captchaType;
			}
			this.siteConfig = next;
		},
	},
});
