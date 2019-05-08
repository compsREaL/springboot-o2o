package com.real.o2o.controller.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.real.o2o.dto.UserAwardMapExecution;
import com.real.o2o.entity.Award;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.UserAwardMap;
import com.real.o2o.enums.UserAwardMapStateEnum;
import com.real.o2o.exception.UserAwardMapOperationException;
import com.real.o2o.service.AwardService;
import com.real.o2o.service.PersonInfoService;
import com.real.o2o.service.UserAwardMapService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import com.real.o2o.util.ShortNetAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/8 14:43
 */
@Controller
@RequestMapping("/frontend")
public class MyAwardController {

    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private PersonInfoService personInfoService;

    /**
     * 获取顾客的兑换列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listuserawardmapbycustomer",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断
        if (pageIndex>-1 && pageSize>-1 && user!=null && user.getUserId()!=null){
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request,"shopId");
            if (shopId>-1){
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userAwardMapCondition.setShop(shop);
            }
            String awardName = HttpServletRequestUtil.getString(request,"awardName");
            if (awardName != null){
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }
            //根据传入的信息进行查询
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.getUserAwardMapList(userAwardMapCondition,pageIndex,pageSize);
            modelMap.put("userAwardMapList",userAwardMapExecution.getUserAwardMapList());
            modelMap.put("count",userAwardMapExecution.getCount());
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or userId");
        }
        return modelMap;
    }

    /**
     * 根据顾客奖品映射id获取单条顾客奖品的映射信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getawardbyuserawardid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAwardById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取从前端传入的userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request,"userAwardId");
        if (userAwardId>-1){
            //根据id获得奖品映射信息，进而获取奖品id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            //将奖品信息和领取状态返回给前端
            modelMap.put("award",award);
            modelMap.put("usedStatus",userAwardMap.getUsedStatus());
            modelMap.put("userAwardMap",userAwardMap);
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty userAwardId");
        }
        return modelMap;
    }

    /**
     * 在线兑换奖品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/adduserawardmap",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addUserAwardMap(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //从前端请求中获取awardId
        Long awardId = HttpServletRequestUtil.getLong(request,"awardId");
        //封装成用户奖品映射对象
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user,awardId);
        if (userAwardMap!=null){
            try {
                //添加兑换信息
                UserAwardMapExecution userAwardMapExecution = userAwardMapService.addUserAwardMap(userAwardMap);
                if (userAwardMapExecution.getState()== UserAwardMapStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                } else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",userAwardMapExecution.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请选择领取的奖品 ");
        }
        return modelMap;
    }

    /**
     * 封装用户奖品映射实体类
     *
     * @param user
     * @param awardId
     * @return
     */
    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
        if (user!=null && user.getUserId()!=null && awardId>-1){
            UserAwardMap userAwardMap = new UserAwardMap();
            //根据用户id获取用户实体类对象
            PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
            //根据奖品id获取奖品对象
            Award award = awardService.getAwardById(awardId);
            userAwardMap.setUser(personInfo);
            userAwardMap.setAward(award);
            Shop shop = new Shop();
            shop.setShopId(award.getShopId());
            userAwardMap.setShop(shop);
            //设置积分
            userAwardMap.setPoint(award.getPoint());
            userAwardMap.setCreateTime(new Date());
            //设置兑换状态为已领取
            userAwardMap.setUsedStatus(1);
            return userAwardMap;
        }else {
            return null;
        }
    }

    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加用户奖品映射信息的url
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }
    @Value("${wechat.exchange.url}")
    public void setExchangeUrl(String exchangeUrl) {
        MyAwardController.exchangeUrl = exchangeUrl;
    }

    /**
     * 生成领取奖品的二维码，供操作员扫描
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4Award",method = RequestMethod.GET)
    @ResponseBody
    public void generateQRCode4Award(HttpServletRequest request, HttpServletResponse response){
        //获取前端传递过来的用户奖品映射id
        long userAwardId = HttpServletRequestUtil.getLong(request,"userAwardId");
        //根据userAwardId获取顾客奖品映射实体类对象
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        //从session中获取顾客信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (userAwardMap!=null && user!=null && user.getUserId()!=null && userAwardMap.getUser().getUserId()==user.getUserId()){
            //获取当前事件戳，保证二维码时效性
            long timeStamp = System.currentTimeMillis();
            String content = "{aaauserAwardIdaaa:"+ userAwardId + ",aaacustomerIdaaa:"+ user.getUserId() +",aaacreateTimeaaa:"+ timeStamp +"}";
            try{
                String longUrl = urlPrefix+exchangeUrl+urlMiddle+ URLEncoder.encode(content,"UTF-8")+urlSuffix;
                //将目标URL转换为短的URL
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                //生成二维码
                BitMatrix qRCodeInfo = CodeUtil.generateQRCodeStream(shortUrl,response);
                //输出到前端
                MatrixToImageWriter.writeToStream(qRCodeInfo,"png",response.getOutputStream());
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
