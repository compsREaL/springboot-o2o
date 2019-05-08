package com.real.o2o.service.impl;

import com.real.o2o.dao.UserAwardMapDao;
import com.real.o2o.dao.UserShopMapDao;
import com.real.o2o.dto.UserAwardMapExecution;
import com.real.o2o.entity.UserAwardMap;
import com.real.o2o.entity.UserShopMap;
import com.real.o2o.enums.UserAwardMapStateEnum;
import com.real.o2o.exception.UserAwardMapOperationException;
import com.real.o2o.service.UserAwardMapService;
import com.real.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 20:26
 */
@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {

    @Autowired
    private UserAwardMapDao userAwardMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserAwardMapExecution getUserAwardMapList(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize) {

        if (userAwardMapCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMapCondition, beginIndex, pageSize);
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);
            UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
            userAwardMapExecution.setCount(count);
            userAwardMapExecution.setUserAwardMapList(userAwardMapList);
            return userAwardMapExecution;
        } else {
            return null;
        }
    }

    @Override
    public UserAwardMap getUserAwardMapById(Long userAwardId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
                && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effectRows = 0;
                //若该奖品需要积分，则将tb_user_shop_map对用用户的积分扣除
                if (userAwardMap.getPoint()!=null && userAwardMap.getPoint()>0){
                    //根据用户id和店铺id获取该用户在该店铺的积分
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),userAwardMap.getShop().getShopId());
                    //判断该用户在该店铺是否有积分
                    if (userAwardMap!=null){
                        //若有积分，则保证积分大于本次兑换所需积分
                        if(userShopMap.getPoint()>=userAwardMap.getPoint()){
                            //积分抵扣
                            userShopMap.setPoint(userShopMap.getPoint()-userAwardMap.getPoint());
                            //更新积分信息
                            effectRows = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (effectRows<=0){
                                throw new UserAwardMapOperationException("更新积分信息失败");
                            }
                        } else {
                            throw new UserAwardMapOperationException("积分不足无法领取");
                        }
                    }else {
                        //在店铺没有积分，则抛出异常
                        throw new UserAwardMapOperationException("在该店铺没有积分，无法兑换奖品");
                    }
                }
                effectRows = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effectRows<=0){
                    throw new UserAwardMapOperationException("领取奖品失败");
                }
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMap);
            } catch (Exception e){
                throw new UserAwardMapOperationException("领取奖品失败："+e.getMessage());
            }
        }else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
        }
    }

    @Override
    @Transactional
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        if (userAwardMap ==null || userAwardMap.getUserAwardId()==null || userAwardMap.getUsedStatus()==null){
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
        }else {
            try {
                //更新可用状态
                int effectRows = userAwardMapDao.updateUserAwardMap(userAwardMap);
                if (effectRows<=0){
                    return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
                }else {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMap);
                }
            } catch (Exception e){
                throw new UserAwardMapOperationException("modifyUserAwardMap error:"+e.getMessage());
            }
        }
    }
}
