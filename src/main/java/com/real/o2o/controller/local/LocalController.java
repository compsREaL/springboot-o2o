package com.real.o2o.controller.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: mabin
 * @create: 2019/5/4 12:14
 */
@Controller
@RequestMapping(value = "/local",method = RequestMethod.GET)
public class LocalController {


    /**
     * 账号绑定页面
     * @return
     */
    @RequestMapping("/accountbind")
    public String accountBind(){
        return "local/accountbind";
    }

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "local/login";
    }

    /**
     * 修改密码界面
     * @return
     */
    @RequestMapping("changepwd")
    public String changePassword(){
        return "local/changepwd";
    }
}
