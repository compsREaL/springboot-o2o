package com.real.o2o.controller.frontend;

import com.real.o2o.dto.UserProductMapExecution;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Product;
import com.real.o2o.entity.Shop;
import com.real.o2o.entity.UserProductMap;
import com.real.o2o.service.UserProductMapService;
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
 * @create: 2019/5/8 20:42
 */
@Controller
@RequestMapping("/frontend")
public class MyProductController {

    @Autowired
    private UserProductMapService userProductMapService;

    /**
     * 列出某个顾客的商品消费信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/listuserproductmapsbycustomer",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listUserProductMapsByCustomer(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session获取顾客信息
        PersonInfo customer = (PersonInfo) request.getSession().getAttribute("user");
        if (pageIndex>-1 && pageSize>-1 && customer!=null && customer.getUserId()!=null){
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setUser(customer);
            long shopId = HttpServletRequestUtil.getLong(request,"shopId");
            if (shopId>-1){
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userProductMapCondition.setShop(shop);
            }
            String productName = HttpServletRequestUtil.getString(request,"productName");
            if (productName!=null){
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            //根据查询条件分页返回查询结果
            UserProductMapExecution userProductMapExecution = userProductMapService.listUserProductMap(userProductMapCondition,pageIndex,pageSize);
            modelMap.put("count",userProductMapExecution.getCount());
            modelMap.put("userProductMapList",userProductMapExecution.getUserProductMapList());
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or userId");
        }
        return modelMap;
    }
}
