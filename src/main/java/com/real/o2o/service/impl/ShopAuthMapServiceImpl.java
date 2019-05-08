package com.real.o2o.service.impl;

import com.real.o2o.dao.ShopAuthMapDao;
import com.real.o2o.dto.ShopAuthMapExecution;
import com.real.o2o.entity.ShopAuthMap;
import com.real.o2o.enums.ShopAuthMapStateEnum;
import com.real.o2o.exception.ShopAuthMapOperationException;
import com.real.o2o.service.ShopAuthMapService;
import com.real.o2o.util.PageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/5/6 17:08
 */
@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    private static Logger logger = LoggerFactory.getLogger(ShopAuthMapServiceImpl.class);

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
        if (shopId != null && pageIndex != null && pageSize != null) {
            //将传入的页数转换为行数
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
            //查询并返回该店铺的授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapList(shopId,beginIndex,pageSize);
            int count = shopAuthMapDao.queryShopAuthMapCount(shopId);
            ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();
            shopAuthMapExecution.setShopAuthMapList(shopAuthMapList);
            shopAuthMapExecution.setCount(count);
            return shopAuthMapExecution;
        } else {
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //空值判断，主要对店铺id和员工id进行校验
        if (shopAuthMap!=null && shopAuthMap.getShop()!=null && shopAuthMap.getShop().getShopId()!=null
                && shopAuthMap.getEmployee()!=null && shopAuthMap.getEmployee().getUserId()!=null){
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setTitleFlag(0);

            try {
                //添加授权
                int effectRows = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectRows<=0){
                    throw new ShopAuthMapOperationException("添加授权失败");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
            } catch (Exception e){
                logger.error("添加授权失败"+e.getMessage());
                throw new ShopAuthMapOperationException("添加授权失败"+e.getMessage());
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
        }
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //空值判断，主要对授权id进行校验
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null){
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
        } else {
            try {
                int effectRows = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectRows<=0){
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
                }else {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,shopAuthMap);
                }
            } catch (Exception e){
                logger.error("modifyShopAuthMapperError:{}",e.getMessage());
                throw new ShopAuthMapOperationException("modifyShopAuthMapperError:"+e.getMessage());
            }
        }
    }
}
