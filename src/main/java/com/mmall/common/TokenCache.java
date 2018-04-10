package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : Administrator
 * @create 2018-04-10 21:52
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    /*本地缓存最大 10000 最多小时是12 小时*/
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        // 默认的数据加载实现,当调用get取值的时候，如果key没有对应的值，就调用这个方法加载
        @Override
        public String load(String s) throws Exception {
            return null;
        }
    });

    public static void setKey(String key, String value) {
        loadingCache.put(key,key);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = loadingCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
        } catch (ExecutionException e) {
            logger.error("localCatches get errors", e);
        }
        return null;
    }
}
