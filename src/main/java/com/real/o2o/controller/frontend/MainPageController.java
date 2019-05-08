package com.real.o2o.controller.frontend;

import com.real.o2o.entity.HeadLine;
import com.real.o2o.entity.ShopCategory;
import com.real.o2o.service.HeadLineService;
import com.real.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/4/29 15:53
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private HeadLineService headLineService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 初始化前端展示系统的主页信息，包括获取一级类别店铺类别列表以及头条列表
     * @return
     */
    @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listMainPageInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            //获取一级店铺类别列表
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList",shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        List<HeadLine> headLineList = new ArrayList<>();
        try {
            //获取enableStatus状态为1的头条列表
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineService.headLineList(headLineCondition);
            modelMap.put("headLineList",headLineList);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        modelMap.put("success",true);
        return modelMap;
    }
}
