import {defineConfig} from 'vitepress'

export default defineConfig({
    title: 'Bubble',
    description: '智能体原生平台知识库',
    lang: 'zh-CN',
    lastUpdated: true,
    rewrites: {
        'wiki/index.md': 'index.md',
    },

    themeConfig: {
        logo: '/images/logo.svg',
        siteTitle: 'Bubble Docs',

        nav: [
            {text: '指南', link: '/wiki/guide/#stack'},
            {text: '架构', link: '/wiki/architecture/#overview'},
            {text: '开发', link: '/wiki/development/backend'},
            {text: '运维', link: '/wiki/ops/#docker'},
            {
                text: '版本',
                items: [
                    {text: '变更日志', link: '/wiki/changelog/index'},
                    {text: '上游同步', link: '/wiki/architecture/#upstream'},
                    {text: '参考手册', link: '/wiki/reference/#env'},
                ],
            },
        ],

        sidebar: {
            '/wiki/guide/': [
                {
                    text: '入门',
                    items: [
                        {text: '项目介绍', link: '/wiki/guide/#stack'},
                        {text: '快速开始', link: '/wiki/guide/#quick-start'},
                        {text: '目录结构', link: '/wiki/guide/#layout'},
                        {text: '账号与客户端', link: '/wiki/guide/#accounts'},
                        {text: '钉钉与企业微信绑定', link: '/wiki/guide/social-bind'},
                    ],
                },
            ],

            '/wiki/architecture/': [
                {
                    text: '架构',
                    items: [
                        {text: '架构总览', link: '/wiki/architecture/#overview'},
                        {text: '端口与服务', link: '/wiki/architecture/#ports'},
                        {text: '模块职责', link: '/wiki/architecture/#modules'},
                        {text: '数据库', link: '/wiki/architecture/#database'},
                        {text: '注册与配置', link: '/wiki/architecture/#nacos'},
                        {text: '网关', link: '/wiki/architecture/#gateway'},
                        {text: '认证与鉴权', link: '/wiki/architecture/#auth'},
                    ],
                },
                {
                    text: '演进',
                    items: [
                        {text: '上游同步基线', link: '/wiki/architecture/#upstream'},
                        {text: '测试策略', link: '/wiki/architecture/#testing'},
                    ],
                },
            ],

            '/wiki/development/': [
                {
                    text: '开发',
                    items: [
                        {text: '后端开发', link: '/wiki/development/backend'},
                        {text: '前端开发', link: '/wiki/development/frontend'},
                        {text: '平台能力', link: '/wiki/development/platform'},
                        {text: '智能体', link: '/wiki/development/agentic'},
                    ],
                },
            ],

            '/wiki/ops/': [
                {
                    text: '运维',
                    items: [
                        {text: 'Docker 部署', link: '/wiki/ops/#docker'},
                        {text: '脚本部署', link: '/wiki/ops/#script'},
                        {text: '监控与日志', link: '/wiki/ops/#monitoring'},
                        {text: '常见问题', link: '/wiki/ops/#troubleshooting'},
                    ],
                },
            ],

            '/wiki/changelog/': [
                {
                    text: '版本',
                    items: [
                        {text: '变更日志', link: '/wiki/changelog/index'},
                    ],
                },
            ],

            '/wiki/reference/': [
                {
                    text: '参考',
                    items: [
                        {text: '环境变量', link: '/wiki/reference/#env'},
                        {text: '错误码', link: '/wiki/reference/#errors'},
                        {text: 'API 约定', link: '/wiki/reference/#api'},
                    ],
                },
            ],
        },

        outlineTitle: '大纲',

        footer: {
            message: 'Bubble - 智能体原生平台',
            copyright: 'Copyright © 2025-present BubbleCloud',
        },

        lastUpdated: {
            text: '最后更新时间',
            formatOptions: {
                dateStyle: 'long',
                timeStyle: 'medium'
            }
        },

        search: {
            provider: 'local'
        },

        docFooter: {
            prev: '上一页',
            next: '下一页'
        }
    },
})
