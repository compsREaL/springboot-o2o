package com.real.o2o.controller.frontend;

import com.real.o2o.dto.ShopExecution;
import com.real.o2o.entity.Area;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopCategory;
import com.real.o2o.service.AreaService;
import com.real.o2o.service.ShopCategoryService;
import com.real.o2o.service.ShopService;
import com.real.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/4/29 19:49
 */
@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;

    /**
     * 返回商品列表页中ShopCategory列表（二级或一级），一级区域信息列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId != -1){
            //如果parentId存在，则取出该一级列表下的二级列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        } else {
            //如果parentId不存在，则返回所有一级ShopCategory列表
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        //获取区域列表信息
        try {
            areaList = areaService.getAreaList();
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("areaList",areaList);
        modelMap.put("success",true);
        return modelMap;
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取每页显示的数据条数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //非空判断
        if (pageIndex > -1 && pageSize > -1){
            //试着获取一级类别的id
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            //试着获取二级类别的id
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            //试着获取区域id
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //试着获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            //获取组合之后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            //根据查询条件和分页信息获取店铺列表，并返回总数
            ShopExecution shopExecution = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("count",shopExecution.getCount());
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageIndex or pageSize");
        }
        return modelMap;
    }

    /**
     * 组合查询条件
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L){
            //查询某个一级列表下的所有二级shopCategory里面的店铺列表
            ShopCategory childShopCategory = new ShopCategory();
            ShopCategory parentShopCategory = new ShopCategory();
            parentShopCategory.setParent(parentShopCategory);
            childShopCategory.setParent(parentShopCategory);
            shopCondition.setShopCategory(childShopCategory);
        }
        if (shopCategoryId!=-1L){
            //查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId!=-1){
            //查询位于某个区域下ID下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName!=null){
            //查询名字里包含shopName的店铺列表
            shopCondition.setShopName(shopName);
        }
        //前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
