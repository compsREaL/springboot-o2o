package com.real.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ShopExecution;
import com.real.o2o.entity.Area;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopCategory;
import com.real.o2o.enums.ShopStateEnum;
import com.real.o2o.exception.ShopOperationException;
import com.real.o2o.service.AreaService;
import com.real.o2o.service.ShopCategoryService;
import com.real.o2o.service.ShopService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/4/15 15:24
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition,0,100);
            request.getSession().setAttribute("shopList",shopExecution.getShopList());
            modelMap.put("shopList",shopExecution.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        } catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String ,Object> modifyShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg = null;
        //获取文件上传内容
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getServletContext());
        //判断request中是否有上传的文件流
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution shopExecution = null;
            try {
                if (shopImg==null){
                    shopExecution = shopService.modifyShop(shop,null);
                }else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                    shopExecution = shopService.modifyShop(shop, imageHolder);
                }
                if (shopExecution.getStatus() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStatusInfo());
                }
            } catch (ShopOperationException | IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("succes", false);
            modelMap.put("errMsg", "请输入店铺id");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Long id = HttpServletRequestUtil.getLong(request,"shopId");
        if (id>-1) {
            try {
                Shop shop = shopService.getByShopId(id);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("success",true);
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
            } catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = null;
        List<Area> areaList = null;
        try{
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request) {
        //接受并转化相应的参数
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg = null;
        //从servletContext中获取文件上传的内容
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getServletContext());
        //判断request中是否有上传的文件流
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        //注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(personInfo);
            ShopExecution shopExecution = null;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                shopExecution = shopService.addShop(shop, imageHolder);
                if (shopExecution.getStatus() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStatusInfo());
                }
            } catch (ShopOperationException | IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("succes", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

}
