package com.real.o2o.dao;

import com.real.o2o.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/6 15:50
 */
public interface ShopAuthMapDao {

    /**
     * 感觉查询条件分页列出店铺下面的授权信息
     * @param shopId
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<ShopAuthMap> queryShopAuthMapList(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 获取店铺授权总数
     * @param shopId
     * @return
     */
    int queryShopAuthMapCount(long shopId);

    /**
     * 新增一条店铺与店员的授权信息
     * @param shopAuthMap
     * @return
     */
    int insertShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 更新授权信息
     * @param shopAuthMap
     * @return
     */
    int updateShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 对某员工除权
     * @param shopAuthId
     * @return
     */
    int deleteShopAuthMap(long shopAuthId);

    /**
     * 查询授权信息
     * @param shopAuthId
     * @return
     */
    ShopAuthMap queryShopAuthMapById(Long shopAuthId);



}
