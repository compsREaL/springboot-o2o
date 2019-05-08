package com.real.o2o.dao;


import com.real.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/5 18:30
 */
public interface UserAwardMapDao {

    /**
     * 根据传入的查询条件分页返回用户兑换奖品记录的列表信息
     * @param userAwardMap
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardMapCondition") UserAwardMap userAwardMap, @Param("rowIndex") int rowIndex,
                                             @Param("pageSize") int pageSize);

    /**
     * 返回相同查询条件下记录的总数
     * @param userAwardMap
     * @return
     */
    int queryUserAwardMapCount(@Param("userAwardMapCondition") UserAwardMap userAwardMap);

    /**
     * 根据userAwardId返回m某条奖品兑换信息
     * @param userAwardId
     * @return
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * 插入一条奖品兑换信息
     * @param userAwardMap
     * @return
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 更新奖品兑换信息，主要用于更新奖品领取时状态
     * @param userAwardMap
     * @return
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);
}
