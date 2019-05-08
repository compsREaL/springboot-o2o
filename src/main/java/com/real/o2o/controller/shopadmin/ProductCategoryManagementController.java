package com.real.o2o.controller.shopadmin;

import com.real.o2o.dto.ProductCategoryExecution;
import com.real.o2o.dto.Result;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.entity.Shop;
import com.real.o2o.enums.ProductCategoryStateEnum;
import com.real.o2o.exception.ProductCategoryOperationException;
import com.real.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/4/18 10:00
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if (productCategoryId!=null && productCategoryId>0){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                ProductCategoryExecution productCategoryExecution = productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
                if (productCategoryExecution.getState()== ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productCategoryExecution.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个商品类别");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> productCategoryList = null;
        if (currentShop!=null && currentShop.getShopId()>0) {
            productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,productCategoryList);
        }else {
            ProductCategoryStateEnum state = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,state.getStateInfo(),state.getState());
        }
    }

    @RequestMapping(value = "/addproductcategory",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        if (productCategoryList!=null && productCategoryList.size()>0){
            for (ProductCategory productCategory:productCategoryList){
                productCategory.setShopId(currentShop.getShopId());
                productCategory.setCreateTime(new Date());
            }
            try {
                ProductCategoryExecution productCategoryExecution = productCategoryService.batchAddProductCategory(productCategoryList);
                if (productCategoryExecution.getState()== ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productCategoryExecution.getStateInfo());
                }
            } catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入至少一个商品类别");
        }
        return modelMap;
    }
}
