package com.real.o2o.cache;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: mabin
 * @create: 2019/5/3 2:37
 */
public class JedisPoolWriper {

    //redis连接池对象
    private JedisPool jedisPool;

    @Value("${redis.database}")
    private int database;

    private String redisAuth = "123456";

    public JedisPoolWriper(final JedisPoolConfig poolConfig,final String hostname,final int port){
        try {
            jedisPool = new JedisPool(poolConfig,hostname,port,3000,redisAuth,database);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取redis连接池对象
     * @return
     */
    public JedisPool getJedisPool(){
        return jedisPool;
    }

    /**
     * 注入redis连接池对象
     * @param jedisPool
     */
    public void setJedisPool(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }
}
