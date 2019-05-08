package com.real.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.ShopAuthMapExecution;
import com.real.o2o.dto.UserAccessToken;
import com.real.o2o.dto.UserAwardMapExecution;
import com.real.o2o.dto.WechatInfo;
import com.real.o2o.entity.*;
import com.real.o2o.enums.UserAwardMapStateEnum;
import com.real.o2o.exception.UserAwardMapOperationException;
import com.real.o2o.service.PersonInfoService;
import com.real.o2o.service.ShopAuthMapService;
import com.real.o2o.service.UserAwardMapService;
import com.real.o2o.service.WechatAuthService;
import com.real.o2o.util.HttpServletRequestUtil;
import com.real.o2o.util.wechat.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/7 20:32
 */
@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {

    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(currentShop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.getUserAwardMapList(userAwardMap, pageIndex, pageSize);
            modelMap.put("count", userAwardMapExecution.getCount());
            modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapList());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    /**
     * 操作员扫顾客的奖品二维码并派发奖品，证明顾客已领取过
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/exchangeaward", method = RequestMethod.GET)
    public String exchangeaward(HttpServletRequest request) {
        //获取负责扫描二维码的店员信息
        WechatAuth wechatAuth = getOperatorInfo(request);
        if (wechatAuth != null) {
            //通过userId获取店员信息
            PersonInfo operator = personInfoService.getPersonInfoById(wechatAuth.getPersonInfo().getUserId());
            request.getSession().setAttribute("user", operator);
            String qRCodeInfo;
            try {
                //解析微信回传过来的自定义参数state
                qRCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return "shop/operationfail";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                wechatInfo = objectMapper.readValue(qRCodeInfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                return "shop/operationfail";
            }
            //检验二维码是否过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }
            //获取用户映射主键
            Long userAwardId = wechatInfo.getUserAwardId();
            //获取顾客id
            Long customerId = wechatInfo.getCustomerId();
            //将顾客信息，操作员信息以及奖品信息封装成userAwardMap
            UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
            if (userAwardMap != null) {
                try {
                    //检验员工是否具有扫码权限
                    if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
                        return "shop/operationfail";
                    }
                    //修改奖品的领取状态
                    UserAwardMapExecution userAwardMapExecution = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (userAwardMapExecution.getState()== UserAwardMapStateEnum.SUCCESS.getState()){
                        return "shop/operationsuccess";
                    }
                } catch (UserAwardMapOperationException e) {
                    return "shop/operationfail";
                }
            }
        }
        return "shop/operationfail";
    }

    /**
     * 检查员工权限
     * @param userId
     * @param userAwardMap
     * @return
     */
    private boolean checkShopAuth(Long userId, UserAwardMap userAwardMap) {
        //取出所有授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 9999);
        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId && shopAuthMap.getEnableStatus() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 封装用户奖品映射实体类，以供扫码使用，主要将其领取状态变为已领取
     *
     * @param customerId
     * @param userAwardId
     * @param operator
     * @return
     */
    private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = null;
        if (customerId != null && userAwardId != null && operator != null) {
            //获取原有的userAwardMap信息
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUsedStatus(1);
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
        }
        return userAwardMap;
    }


    /**
     * 根据code获取UserAccessToken，进而通过token中的openID获取微信用户信息
     *
     * @param request
     * @return
     */
    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth wechatAuth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wechatAuth;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过十分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - wechatInfo.getCreateTime() <= 600000) {
                return true;
            }
        }
        return false;
    }
}
