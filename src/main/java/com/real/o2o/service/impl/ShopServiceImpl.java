package com.real.o2o.service.impl;

import com.real.o2o.dao.ShopAuthMapDao;
import com.real.o2o.dao.ShopDao;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ShopExecution;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopAuthMap;
import com.real.o2o.enums.ShopStateEnum;
import com.real.o2o.exception.ShopOperationException;
import com.real.o2o.service.ShopService;
import com.real.o2o.util.ImageUtil;
import com.real.o2o.util.PageCalculator;
import com.real.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/13 11:00
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    private static Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution shopExecution = new ShopExecution();
        if (shopList!=null){
            shopExecution.setShopList(shopList);
            shopExecution.setCount(count);
        } else {
            shopExecution.setStatus(ShopStateEnum.INNER_ERROR.getState());
        }
        return shopExecution;
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                //判断是否需要处理图片
                if (imageHolder.getInputStream() != null && imageHolder.getImgName() != null && !"".equals(imageHolder.getImgName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, imageHolder);
                }
                //更新店铺信息
                shop.setLastEditTime(new Date());
                int effectRows = shopDao.updateShop(shop);
                if (effectRows <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                logger.error("modifyshop error:{}",e.getMessage());
                throw new ShopOperationException("modifyshop error:"+e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
        //空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息
            int effectedRow = shopDao.insertShop(shop);
            if (effectedRow <= 0) {
                throw new ShopOperationException("店铺创建失败");
            } else {
                if (imageHolder.getInputStream() != null) {
                    //存储图片
                    try {
                        addShopImg(shop, imageHolder);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedRow = shopDao.updateShop(shop);
                    if (effectedRow <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }

                    //执行增加shopAuthMap操作
                    ShopAuthMap shopAuthMap = new ShopAuthMap();
                    shopAuthMap.setEmployee(shop.getOwner());
                    shopAuthMap.setShop(shop);
                    shopAuthMap.setTitle("店家");
                    shopAuthMap.setTitleFlag(0);
                    shopAuthMap.setEnableStatus(1);
                    shopAuthMap.setCreateTime(new Date());
                    shopAuthMap.setLastEditTime(new Date());
                    try{
                        effectedRow = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                        if (effectedRow<=0){
                            throw new ShopOperationException("授权创建失败");
                        }
                    } catch (Exception e){
                        logger.error("授权创建失败:{}",e.getMessage());
                        throw new ShopOperationException("授权创建失败:"+e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("addShop error:{}",e.getMessage());
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, ImageHolder imageHolder) {
        //获取图片目录图片的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(imageHolder, dest);
        shop.setShopImg(shopImgAddr);
    }
}
