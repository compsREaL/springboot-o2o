package com.real.o2o.controller.shopadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.real.o2o.dto.ShopAuthMapExecution;
import com.real.o2o.dto.UserAccessToken;
import com.real.o2o.dto.WechatInfo;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.ShopAuthMap;
import com.real.o2o.entity.WechatAuth;
import com.real.o2o.enums.ShopAuthMapStateEnum;
import com.real.o2o.service.PersonInfoService;
import com.real.o2o.service.ShopAuthMapService;
import com.real.o2o.service.WechatAuthService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import com.real.o2o.util.ShortNetAddressUtil;
import com.real.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/6 17:47
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {

    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    private static Logger logger = LoggerFactory.getLogger(ShopAuthManagementController.class);


    @RequestMapping(value = "/listshopauthmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShopAuthMapsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //读出分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if (pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
            //分页获取该店铺下的授权信息列表
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(),pageIndex,pageSize);
            modelMap.put("shopAuthMapList",shopAuthMapExecution.getShopAuthMapList());
            modelMap.put("count",shopAuthMapExecution.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopauthmapbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopAuthMapById(@RequestParam Long shopAuthId){
        Map<String,Object> modelMap = new HashMap<>();
        if (shopAuthId!=null && shopAuthId>-1){
            //根据前台传入的shopAuthId查找对应的授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("shopAuthMap",shopAuthMap);
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty shopAuthId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshopauthmap",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyShopAuthMap(String shopAuthMapStr,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //是授权编辑时候调用还是删除/恢复授权操作时调用
        //若为前者，则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码校验
        if (!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            //传入的json字符串转换为对应的ShopAuthMap实例
            shopAuthMap = objectMapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            logger.error("modifyShopAuthMap error:{}",e.getMessage());
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }

        if (shopAuthMap != null && shopAuthMap.getShopAuthId()!=null){
            try {
                //店家本身不支持修改
                if(!checkPermission(shopAuthMap.getShopAuthId())){
                    modelMap.put("success",false);
                    modelMap.put("errMsg","无权对店家权限进行操作");
                    return modelMap;
                }

                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (shopAuthMapExecution.getState()== ShopAuthMapStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",shopAuthMapExecution.getStateInfo());
                }
            } catch (Exception e){
                logger.error("modifyShopAuthMap error:{}",e.getMessage());
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入要修改的授权信息");
        }
        return modelMap;
    }

    private boolean checkPermission(Long shopAuthId) {
        ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (shopAuthMap.getTitleFlag() == 0){
            //若是店家自身，则无法进行权限修改
            return false;
        }else {
            return true;
        }
    }

    //微信获取用户信息的api前缀
    private static String urlPrefix;
    //微信获取用户信息的api中间部分
    private static String urlMiddle;
    //微信获取用户信息的api后缀
    private static String urlSuffix;
    //微信回传给的响应添加授权信息的url
    private static String authUrl;
    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }
    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    /**
     * 生成带有url的二维码，微信扫一扫就能链接到对应的URL中
     * @param request
     * @param response
     */
    @RequestMapping(value = "generateqrcode4shopauth",method = RequestMethod.GET)
    @ResponseBody
    public void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response){
        //从session中获取当前店铺shop的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop!=null && currentShop.getShopId()!=null){
            //获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            //将店铺di和timestamp传入content，赋值到state中，这样微信获取到这些信息后就会回传到授权信息的添加方法
            //加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaashopIdaaa:"+ currentShop.getShopId() + ",aaacreateTimeaaa:"+ timeStamp+"}";
            try {
                //将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
                String longUrl = urlPrefix+authUrl+urlMiddle+ URLEncoder.encode(content,"UTF-8")+urlSuffix;
                //将目标URL转换成短的URL
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                //调用二维码生成的工具类方法，传入短的URL，生成二维码
                BitMatrix qRCodeImg = CodeUtil.generateQRCodeStream(shortUrl,response);
                //将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRCodeImg,"png",response.getOutputStream());
            } catch (IOException e) {
                logger.error("generateQRCode4ShopAuth error:{}",e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/addshopauthmap",method = RequestMethod.GET)
    public String addShopAuthMap(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //从request里面获取微信用户的信息
        WechatAuth auth = getEmpolyInfo(request);
        if(auth!=null){
            //根据userId获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将用户信息添加到user中
            request.getSession().setAttribute("user",user);
            //解析微信回传过来的自定义参数state，由于之前进行了编码，此处需要解码
            String qRCodeInfo = new String(
                    URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码后的内容用"/"替换之前生成二维码时添加的aaa前缀，转换成WechatInfo实例
                wechatInfo = objectMapper.readValue(qRCodeInfo.replace("aaa","\""),WechatInfo.class);
            } catch (Exception e){
                logger.error("addShopAuthMap error:{}",e.getMessage());
                return "shop/operationfail";
            }
            //检验二维码是否已过期
            if (!checkQRCodeInfo(wechatInfo)){
                return "shop/operationfail";
            }
            //去重校验
            //获取该店铺下的授权信息
            ShopAuthMapExecution authMapExecution = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(),1,999);
            List<ShopAuthMap> shopAuthMapList = authMapExecution.getShopAuthMapList();
            for (ShopAuthMap shopAuthMap:shopAuthMapList){
                if (shopAuthMap.getEmployee().getUserId()==user.getUserId()){
                    return "shop/operationfail";
                }
            }
            try {
                //根据获取的内容添加微信授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (shopAuthMapExecution.getState()==ShopAuthMapStateEnum.SUCCESS.getState()){
                    return "shop/operationsuccess";
                }else {
                    return "shop/operationfail";
                }
            } catch (RuntimeException e){
                logger.error("addShopAuthMap error:{}",e.getMessage());
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过十分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo!=null && wechatInfo.getShopId()!=null && wechatInfo.getCreateTime()!=null){
            long nowTime = System.currentTimeMillis();
            if ((nowTime-wechatInfo.getCreateTime())<=600000){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 根据微信回传的code获取用户信息
     * @param request
     * @return
     */
    private WechatAuth getEmpolyInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth wechatAuth = null;
        if (null!=code){
            UserAccessToken token;
            try {
                token = WechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId",openId);
                wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return wechatAuth;
    }
}
