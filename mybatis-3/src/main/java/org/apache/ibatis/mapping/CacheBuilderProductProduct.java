package org.apache.ibatis.mapping;


import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.CacheException;

public class CacheBuilderProductProduct {
	private Integer size;
	private Long clearInterval;
	private boolean readWrite;
	private boolean blocking;

	public CacheBuilder size(Integer size, CacheBuilder cacheBuilder) {
		this.size = size;
		return cacheBuilder;
	}

	public Cache setStandardDecorators(Cache cache) {
		try {
			MetaObject metaCache = SystemMetaObject.forObject(cache);
			if (size != null && metaCache.hasSetter("size")) {
				metaCache.setValue("size", size);
			}
			if (clearInterval != null) {
				cache = new ScheduledCache(cache);
				((ScheduledCache) cache).setClearInterval(clearInterval);
			}
			if (readWrite) {
				cache = new SerializedCache(cache);
			}
			cache = new LoggingCache(cache);
			cache = new SynchronizedCache(cache);
			if (blocking) {
				cache = new BlockingCache(cache);
			}
			return cache;
		} catch (Exception e) {
			throw new CacheException("Error building standard cache decorators.  Cause: " + e, e);
		}
	}

	public CacheBuilder clearInterval(Long clearInterval, CacheBuilder cacheBuilder) {
		this.clearInterval = clearInterval;
		return cacheBuilder;
	}

	public CacheBuilder readWrite(boolean readWrite, CacheBuilder cacheBuilder) {
		this.readWrite = readWrite;
		return cacheBuilder;
	}

	public CacheBuilder blocking(boolean blocking, CacheBuilder cacheBuilder) {
		this.blocking = blocking;
		return cacheBuilder;
	}
}