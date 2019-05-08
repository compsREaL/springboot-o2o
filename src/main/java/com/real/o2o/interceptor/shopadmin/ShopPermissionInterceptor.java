package com.real.o2o.interceptor.shopadmin;

import com.real.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 店家管理系统操作验证拦截器
 *
 * @author: mabin
 * @create: 2019/5/4 14:03
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在用户操作发生前，改成preHandle里的逻辑，进行拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        //从session中获取当前选择店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //从session中获取当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        //非空判断
        if (currentShop!=null && shopList!=null){
            for (Shop shop:shopList){
                if (shop.getShopId() == currentShop.getShopId()){
                    return true;
                }
            }
        }
        //若不满足拦截器的验证，则返回false，终止用户操作的执行
        return false;
    }
}
