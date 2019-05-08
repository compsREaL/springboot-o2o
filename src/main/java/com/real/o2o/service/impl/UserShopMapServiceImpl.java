package com.real.o2o.service.impl;

import com.real.o2o.dao.UserShopMapDao;
import com.real.o2o.dto.UserShopMapExecution;
import com.real.o2o.entity.UserShopMap;
import com.real.o2o.service.UserShopMapService;
import com.real.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/7 18:58
 */
@Service
public class UserShopMapServiceImpl implements UserShopMapService {

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserShopMapExecution listUserShopMapList(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
        if (userShopMapCondition!=null && pageIndex>-1 && pageSize>-1){
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition,beginIndex,pageSize);
            int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
            UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
            userShopMapExecution.setCount(count);
            userShopMapExecution.setUserShopMapList(userShopMapList);
            return userShopMapExecution;
        }else {
            return null;
        }
    }

    @Override
    public UserShopMap getUserShopMap(Long userId, Long shopId) {
        return userShopMapDao.queryUserShopMap(userId,shopId);
    }
}
