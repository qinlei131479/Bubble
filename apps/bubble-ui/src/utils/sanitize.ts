import DOMPurify from 'dompurify';

/**
	 * 清洗通过 v-html 渲染的富文本。
 */
export const sanitizeHtml = (html: string | null | undefined): string => {
	return DOMPurify.sanitize(html ?? '', {
		USE_PROFILES: { html: true },
		FORBID_TAGS: ['form', 'iframe', 'object', 'embed', 'style'],
		FORBID_ATTR: ['style'],
	});
};
