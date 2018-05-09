package com.mmall.common;

import com.mmall.util.Propertiesutil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author : Administrator
 * @create 2018-05-08 16:23
 */
public class RedisPool {
    // jedis 连接池
    private static JedisPool pool;
    // 最大连接数
    private static Integer maxTotal = Integer.valueOf(Propertiesutil.getProperty("redis.max.total", "20"));
    // //在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(Propertiesutil.getProperty("redis.max.idle", "20"));
    // 在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(Propertiesutil.getProperty("redis.min.idle", "20"));
    // 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnBorrow = Boolean.parseBoolean(Propertiesutil.getProperty("redis.test.borrow", "true"));
    // 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(Propertiesutil.getProperty("redis.test.return", "false"));
    // redis 服务器IP
    private static String redisIp = Propertiesutil.getProperty("redis.ip");
    // redis 端口号
    private static int redisPort = Integer.parseInt(Propertiesutil.getProperty("redis.port"));
    // redis 的密码
    private static String redisPass = String.valueOf(Propertiesutil.getProperty("redis.password"));

    static{
        initPool();
    }

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2, redisPass);
    }

    /**
     *  jedis 开放
     * @return Jedis 连结的时间
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }

    /**
     * 坏连接--放回Jedis之中
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    /**
     * 返回jedis 连接池
     * @param jedis
     */
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("xcallenTest","Xcallenvalue");
        returnResource(jedis);
        //临时调用，销毁连接池中的所有连接
        pool.destroy();
        System.out.println("program is end");
    }
}
