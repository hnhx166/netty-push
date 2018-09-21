package com.vinux.push.cache;

public abstract class CacheService<K, V> {

	public abstract void put(final K key, V value);

	public abstract void put(final K key, V value, Long expireTime);

	public abstract V get(K key);

	public abstract void remove(K key);

}
