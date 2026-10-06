<template>
	<div
		class="inline-block relative text-center align-bottom select-none overflow-hidden rounded-full"
		:style="{ width: `${size}px`, height: `${size}px`, fontSize: `${fontSize}px` }"
	>
		<img v-if="faceUrl" :src="faceUrl" class="w-full h-full" />
		<div v-else class="w-full h-full flex items-center justify-center text-white bg-[#3696f2]">{{ text }}</div>
	</div>
</template>

<script setup lang="ts" name="nameAvatar">
const props = defineProps<{
	name?: string;
	faceUrl?: string;
	size?: number;
}>();

const CJK_REGEX = /[\u4e00-\u9fff\u3400-\u4dbf]/;

const size = computed(() => props.size || 40);

const text = computed(() => {
	const name = props.name?.trim();
	if (!name) return '';
	if (CJK_REGEX.test(name)) return name.slice(0, 1);
	const parts = name.split(/\s+/).filter(Boolean);
	return parts
		.map((p) => p[0].toUpperCase())
		.slice(0, 2)
		.join('');
});

const fontSize = computed(() => Math.max(12, Math.round(size.value * (text.value.length > 1 ? 0.32 : 0.42))));
</script>
