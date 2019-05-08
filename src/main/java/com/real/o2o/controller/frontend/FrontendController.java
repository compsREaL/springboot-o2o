package com.real.o2o.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: mabin
 * @create: 2019/4/29 17:11
 */
@Controller
@RequestMapping(value = "/frontend",method = RequestMethod.GET)
public class FrontendController {

    @RequestMapping("/index")
    public String index(){
        return "frontend/index";
    }

    /**
     * 商品列表页路由
     * @return
     */
    @RequestMapping("/shoplist")
    public String shopList(){
        return "frontend/shoplist";
    }

    /**
     * 店铺详情页
     * @return
     */
    @RequestMapping("/shopdetail")
    public String shopDetail(){
        return "frontend/shopdetail";
    }

    @RequestMapping("/productdetail")
    public String productDetail(){
        return "frontend/productdetail";
    }

    @RequestMapping("/awarddetail")
    public String awardDetail(){
        return "frontend/awarddetail";
    }

    @RequestMapping("/awardlist")
    public String awardList(){
        return "frontend/awardlist";
    }

    @RequestMapping("/pointrecord")
    public String pointRecord(){
        return "frontend/pointrecord";
    }

    @RequestMapping("/myawarddetail")
    public String myAwardDetail(){
        return "frontend/myawarddetail";
    }

    @RequestMapping("/myrecord")
    public String myRecord(){
        return "frontend/myrecord";
    }

    @RequestMapping("/mypoint")
    public String myPoint(){
        return "frontend/mypoint";
    }
}
