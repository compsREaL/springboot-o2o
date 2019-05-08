package com.real.o2o.dao;

import com.real.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/5 18:29
 */
public interface UserShopMapDao {

    /**
     * 根据查询条件分页返回查询结果
     * @param userShopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 返回相同查询条件下返回的总数
     * @param userShopCondition
     * @return
     */
    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

    /**
     * 根据传入的用户id和shopId查询该用户在某个店铺的积分信息
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);

    /**
     * 添加一条用户店铺的积分记录
     * @param userShopMap
     * @return
     */
    int insertUserShopMap(UserShopMap userShopMap);

    /**
     * 更新用户在某商铺的积分
     * @param userShopMap
     * @return
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);
}
