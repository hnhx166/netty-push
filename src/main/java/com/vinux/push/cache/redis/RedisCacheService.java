package com.vinux.push.cache.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.vinux.push.cache.CacheService;

@Service
public class RedisCacheService<K, V> extends CacheService<String, V> {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public void put(final String key, V value) {
		try {
			redisTemplate.opsForValue().set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(String key, V value, Long expireTime) {
		try {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public V get(String key) {
		return (V)redisTemplate.opsForValue().get(key);
	}
	
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

	@Override
	public void remove(String key) {
		if(exists(key)) {
			redisTemplate.delete(key);
		}

	}

}
