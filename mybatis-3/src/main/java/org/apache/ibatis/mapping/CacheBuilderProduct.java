package org.apache.ibatis.mapping;


import java.util.Properties;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.cache.decorators.ScheduledCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.CacheException;
import java.util.Map;
import java.util.Map;
import org.apache.ibatis.builder.InitializingObject;

public class CacheBuilderProduct {
	private CacheBuilderProductProduct cacheBuilderProductProduct = new CacheBuilderProductProduct();
	private Properties properties;
	public CacheBuilder size(Integer size, CacheBuilder cacheBuilder) {
		return cacheBuilderProductProduct.size(size, cacheBuilder);
	}

	public Cache setStandardDecorators(Cache cache) {
		return cacheBuilderProductProduct.setStandardDecorators(cache);
	}

	public CacheBuilder clearInterval(Long clearInterval, CacheBuilder cacheBuilder) {
		return cacheBuilderProductProduct.clearInterval(clearInterval, cacheBuilder);
	}

	public CacheBuilder readWrite(boolean readWrite, CacheBuilder cacheBuilder) {
		return cacheBuilderProductProduct.readWrite(readWrite, cacheBuilder);
	}

	public CacheBuilder blocking(boolean blocking, CacheBuilder cacheBuilder) {
		return cacheBuilderProductProduct.blocking(blocking, cacheBuilder);
	}

	public CacheBuilder properties(Properties properties, CacheBuilder cacheBuilder) {
		this.properties = properties;
		return cacheBuilder;
	}

	public void setCacheProperties(Cache cache) {
		if (properties != null) {
			MetaObject metaCache = SystemMetaObject.forObject(cache);
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				String name = (String) entry.getKey();
				String value = (String) entry.getValue();
				if (metaCache.hasSetter(name)) {
					Class<?> type = metaCache.getSetterType(name);
					if (String.class == type) {
						metaCache.setValue(name, value);
					} else if (int.class == type || Integer.class == type) {
						metaCache.setValue(name, Integer.valueOf(value));
					} else if (long.class == type || Long.class == type) {
						metaCache.setValue(name, Long.valueOf(value));
					} else if (short.class == type || Short.class == type) {
						metaCache.setValue(name, Short.valueOf(value));
					} else if (byte.class == type || Byte.class == type) {
						metaCache.setValue(name, Byte.valueOf(value));
					} else if (float.class == type || Float.class == type) {
						metaCache.setValue(name, Float.valueOf(value));
					} else if (boolean.class == type || Boolean.class == type) {
						metaCache.setValue(name, Boolean.valueOf(value));
					} else if (double.class == type || Double.class == type) {
						metaCache.setValue(name, Double.valueOf(value));
					} else {
						throw new CacheException("Unsupported property type for cache: '" + name + "' of type " + type);
					}
				}
			}
		}
		if (InitializingObject.class.isAssignableFrom(cache.getClass())) {
			try {
				((InitializingObject) cache).initialize();
			} catch (Exception e) {
				throw new CacheException("Failed cache initialization for '" + cache.getId() + "' on '"
						+ cache.getClass().getName() + "'", e);
			}
		}
	}
}