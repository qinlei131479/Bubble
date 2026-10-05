<template>
	<div class="flex min-h-full flex-col bg-slate-100 p-6 dark:bg-slate-900">
		<!-- 骨架屏 -->
		<div v-if="pageLoading" class="flex min-h-0 flex-1 flex-col animate-pulse">
			<div class="mb-5 grid shrink-0 grid-cols-2 gap-3.5 md:grid-cols-3 xl:grid-cols-6">
				<div v-for="i in 6" :key="i" class="rounded-2xl bg-white p-[18px] dark:bg-slate-800">
					<div class="h-3 mb-2 rounded w-14 bg-slate-200 dark:bg-slate-700" />
					<div class="w-20 mb-2 rounded h-7 bg-slate-200 dark:bg-slate-700" />
					<div class="h-3 rounded w-28 bg-slate-200 dark:bg-slate-700" />
				</div>
			</div>
			<div class="grid min-h-[520px] flex-1 grid-cols-1 gap-4 xl:grid-cols-[minmax(0,3fr)_minmax(360px,2fr)]">
				<div class="h-full rounded-2xl bg-white dark:bg-slate-800" />
				<div class="h-full rounded-2xl bg-white dark:bg-slate-800" />
			</div>
		</div>

		<!-- 主内容 -->
		<template v-else>
			<!-- 统计卡片 -->
			<div class="mb-5 grid shrink-0 grid-cols-2 gap-3.5 md:grid-cols-3 xl:grid-cols-6">
				<div
					v-for="(card, i) in statisticsCards"
					:key="i"
					class="flex flex-col rounded-2xl border border-slate-100 bg-white p-[18px] shadow-sm transition-all hover:-translate-y-0.5 hover:shadow-md dark:border-slate-700 dark:bg-slate-800"
				>
					<div class="flex items-center gap-2 mb-2">
						<div class="flex items-center justify-center rounded-lg h-7 w-7 shrink-0 bg-indigo-50 dark:bg-indigo-950">
							<svg class="h-3.5 w-3.5 text-indigo-600 dark:text-indigo-400" viewBox="0 0 24 24" fill="currentColor">
								<path :d="card.iconPath" />
							</svg>
						</div>
						<span class="text-[10px] font-semibold uppercase tracking-widest text-slate-400 dark:text-slate-500">
							{{ card.label }}
						</span>
					</div>
					<div class="mb-1 text-2xl font-bold tracking-tight truncate text-slate-900 dark:text-slate-100">
						{{ card.value }}
					</div>
					<div class="mb-2 truncate text-[11px] text-slate-400 dark:text-slate-500">{{ card.sub }}</div>
					<span
						class="mt-auto inline-flex w-fit items-center rounded-full px-1.5 py-0.5 text-[10px] font-medium"
						:class="
							card.ok
								? 'bg-emerald-50 text-emerald-600 dark:bg-emerald-950 dark:text-emerald-400'
								: 'bg-rose-50 text-rose-500 dark:bg-rose-950 dark:text-rose-400'
						"
					>
						{{ card.badge }}
					</span>
				</div>
			</div>

			<!-- 图表双栏 -->
			<div class="grid min-h-[520px] flex-1 grid-cols-1 items-stretch gap-4 xl:grid-cols-[minmax(0,3fr)_minmax(360px,2fr)]">
				<!-- 命令统计 -->
				<div
					class="flex min-h-0 flex-col overflow-hidden rounded-2xl border border-slate-100 bg-white shadow-sm dark:border-slate-700 dark:bg-slate-800"
				>
					<div class="flex items-center justify-between border-b border-slate-100 px-5 py-[18px] dark:border-slate-700">
						<span class="border-l-[3px] border-indigo-700 pl-2.5 text-base font-semibold text-slate-900 dark:text-slate-100">
							{{ t('cache.commandStats') }}
						</span>
						<span class="text-[11px] text-slate-400 dark:text-slate-500">
							{{ t('cache.realtimeOps', { ops: baseInfo.instantaneous_ops_per_sec || 0 }) }}
						</span>
					</div>
					<div class="grid min-h-0 flex-1 grid-cols-[minmax(250px,1.05fr)_minmax(260px,0.95fr)] items-center max-[720px]:grid-cols-1">
						<div class="flex items-center justify-center px-5 py-6">
							<v-chart class="h-[clamp(280px,36vh,360px)] w-full" :option="commandChartOption" autoresize />
						</div>
						<!-- 图例：仅展示 Top 8 -->
						<div class="flex flex-col justify-center gap-3 px-5 py-6">
							<div v-for="(item, i) in topCommands" :key="i" class="flex items-center gap-2.5 text-[13px]">
								<span class="h-2 w-2 shrink-0 rounded-full" :style="{ background: commandColors[i % commandColors.length] }" />
								<span class="min-w-0 flex-1 truncate text-slate-500 dark:text-slate-400" :title="item.name">{{ item.name }}</span>
								<span class="min-w-16 text-right text-xs font-semibold tabular-nums text-slate-900 dark:text-slate-100">
									{{ item.value.toLocaleString() }}
								</span>
								<span class="w-10 text-right text-xs tabular-nums text-slate-400 dark:text-slate-500"> {{ getCommandPct(item.value) }}% </span>
							</div>
						</div>
					</div>
					<div class="shrink-0 border-t border-slate-100 px-5 py-2.5 dark:border-slate-700">
						<span class="text-[11px] text-slate-400 dark:text-slate-500">
							{{
								t('cache.commandSummary', {
									count: commandList.length,
									total: Number(baseInfo.total_commands_processed || 0).toLocaleString(),
								})
							}}
						</span>
					</div>
				</div>

				<!-- 内存详情 -->
				<div
					class="flex min-h-0 flex-col overflow-hidden rounded-2xl border border-slate-100 bg-white shadow-sm dark:border-slate-700 dark:bg-slate-800"
				>
					<div class="flex items-center border-b border-slate-100 px-5 py-[18px] dark:border-slate-700">
						<span class="border-l-[3px] border-indigo-700 pl-2.5 text-base font-semibold text-slate-900 dark:text-slate-100">
							{{ t('cache.memoryDetail') }}
						</span>
					</div>
					<div class="flex min-h-0 flex-1 flex-col">
						<!-- 半圆仪表盘 -->
						<div class="flex flex-1 items-center px-5 pt-2">
							<v-chart class="h-full max-h-[250px] min-h-[190px] w-full" :option="memoryChartOption" autoresize />
						</div>
						<!-- 峰值 & Lua -->
						<div class="grid shrink-0 grid-cols-2 gap-3 px-5 pb-3">
							<div class="p-3 rounded-xl bg-slate-50 dark:bg-slate-700/50">
								<div class="mb-0.5 text-[10px] font-semibold uppercase tracking-wider text-slate-400 dark:text-slate-500">
									{{ t('cache.peakHistory') }}
								</div>
								<div class="text-sm font-bold text-slate-900 dark:text-slate-100">{{ baseInfo.used_memory_peak_human || '-' }}</div>
								<div class="mt-0.5 text-[10px] text-slate-400">{{ t('cache.peakShare', { perc: baseInfo.used_memory_peak_perc || '-' }) }}</div>
							</div>
							<div class="p-3 rounded-xl bg-slate-50 dark:bg-slate-700/50">
								<div class="mb-0.5 text-[10px] font-semibold uppercase tracking-wider text-slate-400 dark:text-slate-500">
									{{ t('cache.luaMemory') }}
								</div>
								<div class="text-sm font-bold text-slate-900 dark:text-slate-100">{{ baseInfo.used_memory_lua_human || '-' }}</div>
								<div class="mt-0.5 text-[10px] text-slate-400">
									{{ t('cache.cachedScripts', { count: baseInfo.number_of_cached_scripts || '0' }) }}
								</div>
							</div>
						</div>
						<!-- 进度条：当前 vs 峰值 -->
						<div class="shrink-0 px-5 pb-3">
							<div class="mb-1 flex items-center justify-between text-[11px] text-slate-400 dark:text-slate-500">
								<span>{{ t('cache.currentVsPeak') }}</span>
								<span class="tabular-nums">{{ usedMemoryMB }} MB / {{ peakMemoryMB }} MB</span>
							</div>
							<div class="h-1.5 overflow-hidden rounded-full bg-slate-100 dark:bg-slate-700">
								<div class="progress-fill" :style="{ width: memoryPct + '%' }" />
							</div>
						</div>
						<!-- 命中率 & 驱逐 -->
						<div class="grid shrink-0 grid-cols-2 gap-3 px-5 pb-3">
							<div class="p-3 rounded-xl bg-slate-50 dark:bg-slate-700/50">
								<div class="mb-0.5 text-[10px] font-semibold uppercase tracking-wider text-slate-400 dark:text-slate-500">
									{{ t('cache.hitRate') }}
								</div>
								<div class="text-sm font-bold text-slate-900 dark:text-slate-100">{{ cacheHitRate }}%</div>
								<div class="mt-0.5 text-[10px] text-slate-400">
									{{
										t('cache.hitMiss', {
											hits: Number(baseInfo.keyspace_hits || 0).toLocaleString(),
											misses: Number(baseInfo.keyspace_misses || 0).toLocaleString(),
										})
									}}
								</div>
							</div>
							<div class="p-3 rounded-xl bg-slate-50 dark:bg-slate-700/50">
								<div class="mb-0.5 text-[10px] font-semibold uppercase tracking-wider text-slate-400 dark:text-slate-500">
									{{ t('cache.evictedKeys') }}
								</div>
								<div class="text-sm font-bold text-slate-900 dark:text-slate-100">{{ Number(baseInfo.evicted_keys || 0).toLocaleString() }}</div>
								<div class="mt-0.5 text-[10px] text-slate-400">
									{{ t('cache.policy', { policy: baseInfo.maxmemory_policy || 'noeviction' }) }}
								</div>
							</div>
						</div>
					</div>
					<!-- 碎片率 & 订阅 -->
					<div class="flex shrink-0 items-center justify-between border-t border-slate-100 px-5 py-3 dark:border-slate-700">
						<div class="text-[11px] text-slate-500 dark:text-slate-400">
							<span class="font-medium text-slate-700 dark:text-slate-300">{{ t('cache.fragRatio') }}</span>
							<span class="ml-1.5">{{ baseInfo.mem_fragmentation_ratio || '-' }}</span>
							<span
								v-if="fragRatio >= 2"
								class="ml-1.5 rounded-full bg-rose-50 px-1.5 py-0.5 text-[10px] font-medium text-rose-500 dark:bg-rose-950 dark:text-rose-400"
								>{{ t('cache.fragHigh') }}</span
							>
						</div>
						<div class="text-[11px] text-slate-500 dark:text-slate-400">
							<span class="font-medium text-slate-700 dark:text-slate-300">{{ t('cache.pubsub') }}</span>
							<span class="ml-1.5">{{ t('cache.pubsubCount', { count: baseInfo.pubsub_channels || '0' }) }}</span>
						</div>
					</div>
				</div>
			</div>
		</template>
	</div>
</template>

<script setup lang="ts" name="cache">
import { useI18n } from 'vue-i18n';
import { systemCache } from '/@/api/admin/system';
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { PieChart, GaugeChart } from 'echarts/charts';
import { TooltipComponent, GraphicComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';

use([PieChart, GaugeChart, TooltipComponent, GraphicComponent, CanvasRenderer]);

const { t } = useI18n();
const pageLoading = ref(true);
const baseInfo = ref<any>({});
const commandList = ref<{ name: string; value: number }[]>([]);

const commandColors = [
	'#312e81',
	'#4338ca',
	'#6366f1',
	'#818cf8',
	'#a5b4fc',
	'#c7d2fe',
	'#0284c7',
	'#0ea5e9',
	'#38bdf8',
	'#7dd3fc',
	'#065f46',
	'#059669',
	'#34d399',
	'#6ee7b7',
];

const getCacheMonitorData = async () => {
	pageLoading.value = true;
	try {
		const { data } = await systemCache();
		baseInfo.value = data.info ?? {};
		baseInfo.value.dbSize = data.dbSize;
		// value 从接口返回为字符串，统一转 number 并按调用量降序排列
		commandList.value = (data.commandStats ?? [])
			.map((c: any) => ({ name: c.name, value: Number(c.value) }))
			.sort((a: any, b: any) => b.value - a.value);
	} catch {
		// 静默处理
	} finally {
		pageLoading.value = false;
	}
};

// 图例只展示 Top 8，图表展示全部
const topCommands = computed(() => commandList.value.slice(0, 8));

// ── 内存换算 ──
const usedMemoryMB = computed(() => (baseInfo.value.used_memory ? (baseInfo.value.used_memory / 1024 / 1024).toFixed(2) : '0'));
const peakMemoryMB = computed(() =>
	baseInfo.value.used_memory_peak ? (baseInfo.value.used_memory_peak / 1024 / 1024).toFixed(2) : usedMemoryMB.value
);
const memoryPct = computed(() => {
	const used = parseFloat(usedMemoryMB.value);
	const peak = parseFloat(peakMemoryMB.value);
	return peak ? Math.min(100, (used / peak) * 100).toFixed(1) : '0';
});
const fragRatio = computed(() => parseFloat(baseInfo.value.mem_fragmentation_ratio || '0'));
const memoryHealthy = computed(() => fragRatio.value < 2);

// 缓存命中率
const cacheHitRate = computed(() => {
	const hits = Number(baseInfo.value.keyspace_hits || 0);
	const misses = Number(baseInfo.value.keyspace_misses || 0);
	const total = hits + misses;
	return total ? ((hits / total) * 100).toFixed(1) : '0.0';
});

// ── 统计卡片 ──
const statisticsCards = computed(() => {
	const info = baseInfo.value;
	return [
		{
			iconPath: 'M9.4 16.6L4.8 12l4.6-4.6L8 6l-6 6 6 6 1.4-1.4zm5.2 0l4.6-4.6-4.6-4.6L16 6l6 6-6 6-1.4-1.4z',
			label: t('cache.version'),
			value: info.redis_version || '-',
			sub: t('cache.modeBits', {
				mode: info.redis_mode === 'cluster' ? t('cache.modeCluster') : t('cache.modeStandalone'),
				bits: info.arch_bits || 64,
			}),
			badge: t('cache.stable'),
			ok: true,
		},
		{
			iconPath:
				'M12 3C7.58 3 4 4.79 4 7s3.58 4 8 4 8-1.79 8-4-3.58-4-8-4zM4 9v3c0 2.21 3.58 4 8 4s8-1.79 8-4V9c0 2.21-3.58 4-8 4s-8-1.79-8-4zm0 5v3c0 2.21 3.58 4 8 4s8-1.79 8-4v-3c0 2.21-3.58 4-8 4s-8-1.79-8-4z',
			label: t('cache.keyspace'),
			value: info.dbSize || '0',
			sub: t('cache.expiredKeys', { count: info.expired_keys || 0 }),
			badge: t('cache.normal'),
			ok: true,
		},
		{
			iconPath:
				'M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z',
			label: t('cache.clients'),
			value: info.connected_clients || '0',
			sub: t('cache.maxClients', { count: Number(info.maxclients || 0).toLocaleString() }),
			badge: t('cache.connected'),
			ok: true,
		},
		{
			iconPath:
				'M15 9H9v6h6V9zm-2 4h-2v-2h2v2zm8-2V9h-2V7c0-1.1-.9-2-2-2h-2V3h-2v2h-2V3H9v2H7c-1.1 0-2 .9-2 2v2H3v2h2v2H3v2h2v2c0 1.1.9 2 2 2h2v2h2v-2h2v2h2v-2h2c1.1 0 2-.9 2-2v-2h2v-2h-2v-2h2zm-4 6H7V7h10v10z',
			label: t('cache.memoryUsage'),
			value: info.used_memory_human || '-',
			sub: t('cache.fragSub', {
				ratio: info.mem_fragmentation_ratio || '-',
				suffix: fragRatio.value >= 2 ? t('cache.fragHighParen') : '',
			}),
			badge: memoryHealthy.value ? t('cache.healthy') : t('cache.warning'),
			ok: memoryHealthy.value,
		},
		{
			iconPath: 'M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 1.99 2H18c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z',
			label: t('cache.aof'),
			value: info.aof_enabled == 1 ? t('cache.aofOn') : t('cache.aofOff'),
			sub: t('cache.lastWrite', { status: info.aof_last_write_status || '-' }),
			badge: info.aof_enabled == 1 ? t('cache.enable') : t('cache.closed'),
			ok: info.aof_enabled == 1,
		},
		{
			iconPath:
				'M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 3c1.93 0 3.5 1.57 3.5 3.5S13.93 13 12 13s-3.5-1.57-3.5-3.5S10.07 6 12 6zm7 13H5v-.23c0-.62.28-1.2.76-1.58C7.47 15.82 9.64 15 12 15s4.53.82 6.24 2.19c.48.38.76.97.76 1.58V19z',
			label: t('cache.rdb'),
			value: info.rdb_last_bgsave_status === 'ok' ? t('cache.rdbOk') : t('cache.rdbFail'),
			sub: t('cache.pendingChanges', { count: info.rdb_changes_since_last_save || 0 }),
			badge: info.rdb_last_bgsave_status === 'ok' ? t('cache.success') : t('cache.failed'),
			ok: info.rdb_last_bgsave_status === 'ok',
		},
	];
});

// ── 命令统计 Donut 图 ──
const totalOps = computed(() => commandList.value.reduce((s, i) => s + i.value, 0));
const fmtOps = (n: number) => (n >= 1000 ? (n / 1000).toFixed(1) + 'k' : String(n));
const getCommandPct = (val: number) => {
	return totalOps.value ? ((val / totalOps.value) * 100).toFixed(1) : '0';
};

const commandChartOption = computed(() => ({
	tooltip: {
		trigger: 'item',
		formatter: (params: { name: string; value: number; percent: number }) =>
			t('cache.tooltipCalls', { name: params.name, value: params.value, percent: params.percent }),
	},
	color: commandColors,
	graphic: [
		{
			type: 'group',
			left: 'center',
			top: 'center',
			children: [
				{
					type: 'text',
					z: 100,
					left: 'center',
					top: '-14px',
					style: { text: fmtOps(totalOps.value), font: 'bold 26px system-ui, sans-serif', fill: '#0f172a', textAlign: 'center' },
				},
				{
					type: 'text',
					z: 100,
					left: 'center',
					top: '16px',
					style: { text: t('cache.totalCalls'), font: '11px system-ui, sans-serif', fill: '#94a3b8', textAlign: 'center' },
				},
			],
		},
	],
	series: [
		{
			type: 'pie',
			radius: ['48%', '72%'],
			center: ['50%', '50%'],
			data: commandList.value,
			label: { show: false },
			emphasis: { scale: false, itemStyle: { opacity: 0.85 } },
			itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
		},
	],
}));

// ── 内存 Gauge 半圆 ──
const memoryChartOption = computed(() => ({
	series: [
		{
			type: 'gauge',
			startAngle: 200,
			endAngle: -20,
			radius: '90%',
			min: 0,
			max: parseFloat(peakMemoryMB.value) || 1,
			progress: {
				show: true,
				width: 18,
				itemStyle: { color: memoryHealthy.value ? '#4338ca' : '#f43f5e' },
			},
			axisLine: { lineStyle: { width: 18, color: [[1, '#e2e8f0']] } },
			pointer: { show: false },
			axisTick: { show: false },
			splitLine: { show: false },
			axisLabel: { show: false },
			title: {
				offsetCenter: [0, '30%'],
				fontSize: 11,
				color: '#94a3b8',
				fontFamily: 'system-ui, sans-serif',
			},
			detail: {
				offsetCenter: [0, '5%'],
				fontSize: 28,
				fontWeight: 'bold',
				color: '#0f172a',
				formatter: '{value} MB',
				fontFamily: 'system-ui, sans-serif',
			},
			data: [
				{
					value: parseFloat(usedMemoryMB.value),
					name: memoryHealthy.value ? t('cache.statusHealthy') : t('cache.fragHighStatus'),
				},
			],
		},
	],
}));

onMounted(() => {
	getCacheMonitorData();
});
</script>

<style scoped>
.progress-fill {
	height: 100%;
	border-radius: 99px;
	background: linear-gradient(90deg, #4338ca, #818cf8);
	width: 0;
	transition: width 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}
</style>
