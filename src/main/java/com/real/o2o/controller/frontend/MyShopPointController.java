package com.real.o2o.controller.frontend;

import com.real.o2o.dto.UserShopMapExecution;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.UserShopMap;
import com.real.o2o.enums.UserShopMapStateEnum;
import com.real.o2o.service.UserShopMapService;
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
 * @create: 2019/5/8 21:21
 */
@Controller
@RequestMapping("/frontend")
public class MyShopPointController {

    @Autowired
    private UserShopMapService userShopMapService;

    /**
     * 列出用户的积分情况
     * @param request
     * @return
     */
    @RequestMapping(value = "/listusershopmapsbycustomer",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listUserShopMapsByCustomer(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        PersonInfo customer = (PersonInfo) request.getSession().getAttribute("user");
        if (pageIndex>-1 && pageSize>-1 && customer!=null && customer.getUserId()!=null){
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setUser(customer);
            //如果传入的shopName不为空，则按shopName模糊查询
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            if (shopName!=null){
                Shop shop = new Shop();
                shop.setShopName(shopName);
                userShopMapCondition.setShop(shop);
            }
            //根据查询条件列出查询结果
            UserShopMapExecution userShopMapExecution = userShopMapService.listUserShopMapList(userShopMapCondition,pageIndex,pageSize);
            if (userShopMapExecution.getState() == UserShopMapStateEnum.SUCCESS.getState()){
                modelMap.put("userShopMapList",userShopMapExecution.getUserShopMapList());
                modelMap.put("count",userShopMapExecution.getCount());
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",userShopMapExecution.getStateInfo());
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or userId");
        }
        return modelMap;
    }
}
