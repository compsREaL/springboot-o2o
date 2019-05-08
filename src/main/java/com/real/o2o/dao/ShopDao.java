package com.real.o2o.dao;

import com.real.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/12 21:06
 */
public interface ShopDao {


    /**
     * 返回制定条件下返回的店铺的总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);
    /**
     * 分页查询店铺，可输入的信息有店铺名，店铺状态，店铺类别，区域id，owner
     * @param shopCondition
     * @param rowIndex 从第几行开始取数据
     * @param pageSize 返回多少行数据
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
    /**
     * 通过shopId查询店铺信息
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
