package com.real.o2o.interceptor.shopadmin;

import com.real.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登录验证拦截器
 *
 * @author: mabin
 * @create: 2019/5/4 14:01
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(ShopLoginInterceptor.class);

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
        //从session中获取用户信息
        Object userObj = request.getSession().getAttribute("user");
        if (userObj!=null){
            PersonInfo user = (PersonInfo) userObj;
            if (user!=null && user.getUserId()!=null && user.getUserId()>0 && user.getEnableStatus()==1){
                logger.debug("操作用户ID为：{}",user.getUserId());
                return true;
            }
        }
        //若不满足登录验证，则直接跳转到账号登录页面
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html>");
        printWriter.println("<script>");
        printWriter.println("window.open('" + request.getContextPath() +"/local/login?usertype=2','_self')");
        printWriter.println("</script>");
        printWriter.println("</html>");
        return false;
    }
}
