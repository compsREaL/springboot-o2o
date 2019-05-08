package com.real.o2o.service;

/**
 * @author: mabin
 * @create: 2019/5/3 4:50
 */
public interface CacheService {

    /**
     * 依据key前缀删除匹配该模式下的所有key-value
     * 如传入shopcategory，则以shopcategory开头的所有key-value都会被清空
     *
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
