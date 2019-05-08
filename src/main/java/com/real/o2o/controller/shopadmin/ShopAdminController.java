package com.real.o2o.controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 解析路由并转发至相应的html
 * @author: mabin
 * @create: 2019/4/13 16:20
 */
@Controller
@RequestMapping(value = "/shopadmin",method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        //转发至店铺编辑页面
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        //转发至店铺列表页面
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        //转发至店铺管理页面
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryManagement(){
        //转发至商品类别管理页面
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productoperation")
    public String product(){
        //转发至商品添加编辑页面
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productmanagement")
    public String productManagement(){
        //转发至商品添加编辑页面
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/operationfail")
    public String operationFail(){
        return "shop/operationfail";
    }

    @RequestMapping(value = "operationsuccess")
    public String operationSuccess(){
        return "shop/operationsuccess";
    }

    @RequestMapping(value = "/productbuycheck")
    public String productBuyCheck(){
        return "shop/productbuycheck";
    }

    @RequestMapping(value = "/usershopcheck")
    public String userShopCheck(){
        return "shop/usershopcheck";
    }

    @RequestMapping(value = "/awarddelivercheck")
    public String awardDeliverCheck(){
        return "shop/awarddelivercheck";
    }

    @RequestMapping(value = "/awardmanagement")
    public String awardManagement(){
        return "shop/awardmanagement";
    }

    @RequestMapping(value = "/awardoperation")
    public String awardOperation(){
        return "shop/awardoperation";
    }


}
