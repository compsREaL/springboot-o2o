package com.real.o2o.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import java.util.Set;

/**
 * @author: mabin
 * @create: 2019/5/3 2:38
 */
public class JedisUtil {

    //redis连接池对象
    private JedisPool jedisPool;

    //操作key的方法
    public Keys KEYS;

    //对存储结构为String类型的操作
    public Strings STRINGS;

    /**
     * 获取redis连接池
     *
     * @return
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 设置redis连接池
     *
     * @param jedisPoolWriper
     */
    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    /**
     * 从jedis连接池中获取jedis对象
     *
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public class Keys {
        /**
         * 清空所有
         *
         * @return
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String state = jedis.flushAll();
            jedis.close();
            return state;
        }

        /**
         * 删除keys对应的记录
         *
         * @param keys
         * @return
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         *判断key是否存在
         *
         * @param key
         * @return
         */
        public boolean exists(String key){
            Jedis jedis = getJedis();
            boolean exist = jedis.exists(key);
            jedis.close();
            return exist;
        }

        /**
         * 查找所有匹配给定模式的键
         *
         * @param pattern
         * @return
         */
        public Set<String> keys(String pattern){
            Jedis jedis = getJedis();
            Set<String> stringSet = jedis.keys(pattern);
            jedis.close();
            return stringSet;
        }
    }

    public class Strings{

        /**
         * 根据key获取记录
         *
         * @param key
         * @return
         */
        public String get(String key){
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 条件记录，如果记录已经存在，将覆盖原有value
         *
         * @param key
         * @param value
         * @return 状态码
         */
        public  String set(String key,String value){
            return set(SafeEncoder.encode(key),SafeEncoder.encode(value));
        }

        public String set(byte[] key,byte[] value){
            Jedis jedis = getJedis();
            String state = jedis.set(key,value);
            jedis.close();
            return state;
        }
    }
}
