package com.real.o2o.controller.frontend;

import com.real.o2o.dto.ProductExecution;
import com.real.o2o.entity.Product;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.entity.Shop;
import com.real.o2o.service.ProductCategoryService;
import com.real.o2o.service.ProductService;
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
 * @create: 2019/4/30 15:53
 */
@Controller
@RequestMapping("/frontend")
public class ShopDetailController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取店铺信息以及该店铺下面的商品类别列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取前台传来的shopId
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        List<ProductCategory> productCategoryList = null;
        Shop shop=null;
        if (shopId!=-1){
            shop = shopService.getByShopId(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    /**
     * 依据查询条件分页列出该店铺下的所有商品
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listProductsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (pageIndex>-1 && pageSize>-1 && shopId>-1){
            //尝试获取查询条件
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = compactProductCondition4Search(shopId,productCategoryId,productName);
            ProductExecution productExecution = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("count",productExecution.getCount());
            modelMap.put("productList",productExecution.getProductList());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId or pageIndex or pageSize");
        }
        return modelMap;
    }

    /**
     * 组合查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName!=null){
            productCondition.setProductName(productName);
        }
        //只允许选出状态为上架的商品
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
