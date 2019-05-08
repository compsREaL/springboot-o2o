package com.real.o2o.controller.wechat;

import com.real.o2o.dto.UserAccessToken;
import com.real.o2o.dto.WechatAuthExexution;
import com.real.o2o.dto.WechatUser;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.WechatAuth;
import com.real.o2o.enums.WechatAuthStateEnum;
import com.real.o2o.service.PersonInfoService;
import com.real.o2o.service.WechatAuthService;
import com.real.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取关注公众号之后的微信用户信息接口，如果在微信浏览器中访问https://open.weixin.qq.com/connect/oauth2/authorize,
 * 则会获得code，之后通过code获取access_token，进而获取到用户信息
 *
 * @author: mabin
 * @create: 2019/5/1 17:50
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

    private static Logger logger = LoggerFactory.getLogger(WechatController.class);

    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";

    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck",method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, HttpServletResponse response){
        logger.debug("weixin login get....");
        //获取微信公众号传输过来的code，通过code获取access_token，进而获取用户信息
        String code = request.getParameter("code");
        //这个state可以用来传自定义的信息，方便程序调用，也可以不用
        String roleType = request.getParameter("state");
        logger.debug("wechat login code:"+code);
        WechatUser user = null;
        String openId = null;
        WechatAuth wechatAuth = null;
        if (null != code){
            try {
                UserAccessToken token;
                token = WechatUtil.getUserAccessToken(code);
                logger.debug("wechat login token:"+token.toString());
                //通过token获取accessToken
                String accessToken = token.getAccessToken();
                //通过accessToken获取openId
                openId = token.getOpenId();
                //通过accessToken和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken,openId);
                logger.debug("wechat login user",user.toString());
                request.getSession().setAttribute("openId",user.getOpenId());
                wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (Exception e) {
                logger.error("error in getUserAccessToken or getUserInfo or findByOpenId"+e.getMessage());
                e.printStackTrace();
            }
        }

        if (wechatAuth == null){
            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            wechatAuth = new WechatAuth();
            wechatAuth.setOpenId(openId);
            if (FRONTEND.equals(roleType)){
                personInfo.setUserType(1);
            }else {
                personInfo.setUserType(2);
            }
            wechatAuth.setPersonInfo(personInfo);
            WechatAuthExexution wechatAuthExexution = wechatAuthService.register(wechatAuth);
            if (wechatAuthExexution.getState()!= WechatAuthStateEnum.SUCCESS.getState()){
                return null;
            } else {
                personInfo = personInfoService.getPersonInfoById(wechatAuth.getPersonInfo().getUserId());
                request.getSession().setAttribute("user",personInfo);
            }
        }

        //若用户点击的是前端系统展示页，则进入前端展示系统
        if (FRONTEND.equals(roleType)){
            return "frontend/index";
        }else {
            return "shopadmin/shoplist";
        }
    }
}
