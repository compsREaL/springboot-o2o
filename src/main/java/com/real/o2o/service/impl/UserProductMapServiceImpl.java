package com.real.o2o.service.impl;

import com.real.o2o.dao.UserProductMapDao;
import com.real.o2o.dao.UserShopMapDao;
import com.real.o2o.dto.UserProductMapExecution;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.UserProductMap;
import com.real.o2o.entity.UserShopMap;
import com.real.o2o.enums.UserProductMapStateEnum;
import com.real.o2o.exception.UserProductMapOperationException;
import com.real.o2o.service.UserProductMapService;
import com.real.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 14:47
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex, Integer pageSize) {
        if (userProductCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            //依据查询条件列出列表
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition, beginIndex, pageSize);
            int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
            UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
            userProductMapExecution.setCount(count);
            userProductMapExecution.setUserProductMapList(userProductMapList);
            return userProductMapExecution;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException {
        if(userProductMap!=null && userProductMap.getUser()!=null && userProductMap.getUser().getUserId()!=null
                && userProductMap.getShop()!=null && userProductMap.getShop().getShopId()!=null){
            userProductMap.setCreateTime(new Date());
            try {
                //添加消费记录
                int effectRows = userProductMapDao.insertUserProductMap(userProductMap);
                if (effectRows<=0){
                    throw new UserProductMapOperationException("添加消费记录失败");
                }
                //若本次消费能够积分
                if (userProductMap.getPoint()!=null && userProductMap.getPoint()>0){
                    //查询该顾客是否在该店铺进行过消费
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),userProductMap.getShop().getShopId());
                    if (userShopMap!=null && userShopMap.getUserShopId()!=null){
                        //若之前进行过消费，即存在积分记录，进行总积分的更新
                        userShopMap.setPoint(userShopMap.getPoint()+userProductMap.getPoint());
                        effectRows = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (effectRows<=0){
                            throw new UserProductMapOperationException("更新积分信息失败");
                        }
                    }else {
                        //该顾客未曾在该店铺有消费记录，添加一条店铺积分信息
                        userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),userProductMap.getShop().getShopId(),userProductMap.getPoint());
                        effectRows = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectRows<=0){
                            throw new UserProductMapOperationException("积分信息创建失败");
                        }
                    }
                }
                return  new UserProductMapExecution(UserProductMapStateEnum.SUCCESS,userProductMap);
            } catch (Exception e){
                throw new UserProductMapOperationException("添加授权失败"+e.getMessage());
            }
        }else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    /**
     * 封装顾客积分信息
     * @param userId
     * @param shopId
     * @param point
     * @return
     */
    private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
        if (userId!=null && shopId!=null){
            UserShopMap userShopMap = new UserShopMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(userId);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setUser(customer);
            userShopMap.setShop(shop);
            userShopMap.setPoint(point);
            userShopMap.setCreateTime(new Date());
            return userShopMap;
        }else {
            return null;
        }
    }
}
