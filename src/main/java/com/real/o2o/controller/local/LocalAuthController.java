package com.real.o2o.controller.local;

import com.real.o2o.dto.LocalAuthExecution;
import com.real.o2o.entity.LocalAuth;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.enums.LocalAuthStateEnum;
import com.real.o2o.exception.LocalAuthOperationException;
import com.real.o2o.service.LocalAuthService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/5/3 7:25
 */
@Controller
@RequestMapping("/local")
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 将用户信息与平台账户进行绑定
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindlocalauth",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> bindLocalAuth(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码不正确");
            return modelMap;
        }

        //获取输入的账号
        String userName = HttpServletRequestUtil.getString(request,"userName");
        //获取输入的密码
        String password = HttpServletRequestUtil.getString(request,"password");
        //从session中获取当前用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断，要求账号，密码以及当前的用户session非空
        if (userName!=null && password!=null && user!=null){
            //创建LocalAuth对象并进行赋值
            LocalAuth localAuth = new LocalAuth();
            localAuth.setPersonInfo(user);
            localAuth.setUsername(userName);
            localAuth.setPassword(password);
            //绑定账号
            LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState()== LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            } else {
                modelMap.put("success",false);
                modelMap.put("errMsg",localAuthExecution.getStateInfo());
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码不能为空");
        }
        return modelMap;
    }

    /**
     * 修改密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/changelocalpwd",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeLocalPwd(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }

        String userName = HttpServletRequestUtil.getString(request,"userName");
        String password = HttpServletRequestUtil.getString(request,"password");
        String newPassword = HttpServletRequestUtil.getString(request,"newPassword");

        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //非空判断
        if (userName!=null && password!=null && newPassword!=null && user!=null && user.getUserId()!=null && !password.equals(newPassword)){
            try {
                //查看原先账号，看看与输入的账号是否一致，不一致则认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth==null || !localAuth.getUsername().equals(userName)){
                    modelMap.put("success",false);
                    modelMap.put("errMsg","输入的账号非本次登录账号");
                    return modelMap;
                }

                //修改用户的密码
                LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(user.getUserId(),userName,password,newPassword);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                } else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",localAuthExecution.getStateInfo());
                }
            } catch (LocalAuthOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }

        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入密码");
        }
        return modelMap;
    }

    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request,"needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }

        String userName = HttpServletRequestUtil.getString(request,"userName");
        String password = HttpServletRequestUtil.getString(request,"password");

        if (userName!=null && password!=null){
            //传入账号密码去获取平台账号信息
            LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName,password);
            if (localAuth != null){
                //登录成功
                modelMap.put("success",true);
                request.getSession().setAttribute("user",localAuth.getPersonInfo());
            } else {
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
            }
        }
        return modelMap;
    }


    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> loginOut(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //将用户session设置为空
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap;
    }
}
