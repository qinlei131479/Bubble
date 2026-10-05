package com.bubblecloud.common.data.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 读取缓存时兼容历史 Java 序列化数据。
 * <p>
 * 缓存值已改为 JSON。Redis 中仍残留 JDK 序列化条目时，反序列化会失败并中断登录。
 * 遇到这种条目直接删除，让调用方按未命中重新加载。
 */
@Slf4j
class LegacyCompatibleRedisCache extends RedisCache {

	LegacyCompatibleRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
		super(name, cacheWriter, cacheConfig);
	}

	@Override
	protected Object lookup(Object key) {
		try {
			return super.lookup(key);
		}
		catch (SerializationException ex) {
			log.warn("丢弃无法按 JSON 读取的缓存 {}:{}", getName(), key);
			evict(key);
			return null;
		}
	}

}
