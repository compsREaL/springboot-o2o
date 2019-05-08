package com.real.o2o.config.redis;

import com.real.o2o.cache.JedisPoolWriper;
import com.real.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: mabin
 * @create: 2019/5/5 11:09
 */
@Configuration
public class RedisConfiguration {

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.pool.maxActive}")
    private int maxTotal;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.maxWaitMillis}")
    private long maxWaitMillis;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisPoolWriper jedisPoolWriper;

    @Autowired
    private JedisUtil jedisUtil;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //获取连接时检查其有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper(){
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig,hostname,port);
        return jedisPoolWriper;
    }

    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil(){
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /**
     * redis的key操作
     * @return
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys(){
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    /**
     * redis的String操作
     * @return
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings(){
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }
}
