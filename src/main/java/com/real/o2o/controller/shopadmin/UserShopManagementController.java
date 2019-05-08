package com.real.o2o.controller.shopadmin;

import com.real.o2o.dto.UserShopMapExecution;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.UserShopMap;
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
 * @create: 2019/5/7 19:04
 */
@Controller
@RequestMapping("/shopadmin")
public class UserShopManagementController {

    @Autowired
    private UserShopMapService userShopMapService;

    @RequestMapping(value = "listusershopmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listUserShopMapsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
            UserShopMap userShopMapCondition = new UserShopMap();
            userShopMapCondition.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request,"userName");
            //如果传入的顾客名字非空，则按顾客名字模糊查询
            if (userName!=null){
                PersonInfo customer = new PersonInfo();
                customer.setName(userName);
                userShopMapCondition.setUser(customer);
            }
            UserShopMapExecution userShopMapExecution = userShopMapService.listUserShopMapList(userShopMapCondition,pageIndex,pageSize);
            modelMap.put("count",userShopMapExecution.getCount());
            modelMap.put("userShopMapList",userShopMapExecution.getUserShopMapList());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }
}
