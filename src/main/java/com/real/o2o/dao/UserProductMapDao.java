package com.real.o2o.dao;

import com.real.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/5 20:20
 */
public interface UserProductMapDao {

    /**
     * 根据条件分页返回用户购买商品的记录列表
     * @param userProductCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 返回相同查询条件下记录的总数
     * @param userProductCondition
     * @return
     */
    int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

    /**
     * 添加一条用户购买商品的记录
     * @param userProductMap
     * @return
     */
    int insertUserProductMap(UserProductMap userProductMap);
}
