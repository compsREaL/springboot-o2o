package com.real.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.real.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.real.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * EnableWebMvc注解的作用相当于<mvc:annotation-driven />
 *
 * @author: mabin
 * @create: 2019/5/5 12:27
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer, ApplicationContextAware {

    //Spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources");
//        String os = System.getProperty("os.name");
//        if (os.toLowerCase().startsWith("win")){
//            registry.addResourceHandler("/upload/**").addResourceLocations("C:/project")
//        }
        registry.addResourceHandler("/upload/**").addResourceLocations("file:C:/project/image/upload/");
    }

    /**
     * 定义默认的请求处理器
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }

    /**
     * 创建ViewResolver
     * @return
     */
    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置spring容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 文件上传解析器
     * @return
     */
    @Bean
    public CommonsMultipartResolver createCommonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        //设置上传文件大小，最大为20M
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.textproducer.char.string}")
    private String cString;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;
    @Value("${kaptcha.noise.color}")
    private String nColor;
    @Value("${kaptcha.textproducer.char.length}")
    private String clength;
    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servletRegistrationBean.addInitParameter("kaptcha.border",border);//无边框
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.color",fcolor);//字体颜色
        servletRegistrationBean.addInitParameter("kaptcha.img.width",width);//图片宽度
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.char.string",cString);//使用哪些字符生成验证码
        servletRegistrationBean.addInitParameter("kaptcha.img.height",height);//图片高度
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.size",fsize);//字体大小
        servletRegistrationBean.addInitParameter("kaptcha.noise.color",nColor);//干扰线颜色
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.char.length",clength);//验证码字符个数
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.names",fnames);//字体
        return servletRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        String interceptPath = "/shopadmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截路径
        loginIR.addPathPatterns(interceptPath);
        loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
        //继续注册拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截路径
        permissionIR.addPathPatterns(interceptPath);
        //配置不拦截路径
        //shop list page
        permissionIR.excludePathPatterns("/shopadmin/shoplist");
        permissionIR.excludePathPatterns("/shopadmin/getshoplist");
        //shop register page
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionIR.excludePathPatterns("/shopadmin/registershop");
        permissionIR.excludePathPatterns("/shopadmin/shopoperation");
        //shop management page
        permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
        permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
        permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
    }


}
