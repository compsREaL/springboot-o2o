package com.real.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.AwardExecution;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.entity.*;
import com.real.o2o.enums.AwardStateEnum;
import com.real.o2o.service.AwardService;
import com.real.o2o.service.UserShopMapService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/7 22:45
 */
@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {

    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 根据店铺id获取该店铺下的所有奖品列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listawardsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listAwardsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值校验
        if (pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
            //判断查询条件是否传入奖品名
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            //拼接查询条件
            Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(),awardName);
            AwardExecution awardExecution = awardService.getAwardList(awardCondition,pageIndex,pageSize);
            modelMap.put("awardList",awardExecution.getAwardList());
            modelMap.put("count",awardExecution.getCount());
            modelMap.put("success",true);
            //从session中获取用户信息，主要为了显示用户在该店铺的积分
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user!=null && user.getUserId()!=null){
                //获取用户在本店铺的积分
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(),currentShop.getShopId());
                if (userShopMap!=null){
                    modelMap.put("totalPoint",userShopMap.getPoint());
                } else {
                    modelMap.put("totalPoint",0);
                }
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    /**
     * 通过奖品id获取奖品信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getawardbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAwardById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long awardId = HttpServletRequestUtil.getLong(request,"awardId");
        if (awardId>-1){
            //根据传入的id返回奖品信息
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award",award);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty awardId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/addaward",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addAward(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        //接受前端参数变量的初始化，包括奖品信息，缩略图
        Award award = null;
        ObjectMapper objectMapper = new ObjectMapper();
        String awardStr = HttpServletRequestUtil.getString(request,"awardStr");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (resolver.isMultipart(request)){
                thumbnail = handleImage(request,thumbnail);
            }
        } catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        try {
            //将前端传入的awardStr转换为对应的实例
            award = objectMapper.readValue(awardStr,Award.class);
        } catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        if (award != null && thumbnail != null){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                //添加award
                AwardExecution awardExecution = awardService.addAward(award,thumbnail);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",awardExecution.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入奖品信息");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyaward",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyAward(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }

        //接受前端传入的参数，包括商品，缩略图
        ObjectMapper objectMapper = new ObjectMapper();
        Award award = null;
        ImageHolder thumbnail = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (resolver.isMultipart(request)){
                thumbnail = handleImage(request,thumbnail);
            }
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        try {
            String awardStr = HttpServletRequestUtil.getString(request,"awardStr");
            award = objectMapper.readValue(awardStr,Award.class);
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        if (award!=null){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution awardExecution = awardService.modifyAward(award,thumbnail);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",awardExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","奖品信息不正确");
        }
        return modelMap;
    }

    private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
        if (thumbnailFile!=null){
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
        }
        return thumbnail;
    }


    private Award compactAwardCondition4Search(Long shopId, String awardName) {
        Award award = new Award();
        award.setShopId(shopId);
        if ( awardName!=null){
            award.setAwardName(awardName);
        }
        return award;
    }
}
