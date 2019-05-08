package com.real.o2o.controller.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.real.o2o.entity.PersonInfo;
import com.real.o2o.entity.Product;
import com.real.o2o.service.ProductService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import com.real.o2o.util.ShortNetAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 列出商品的详细信息
 *
 * @author: mabin
 * @create: 2019/5/4 18:41
 */
@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listproductdetailpageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listProductDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request,"productId");
        Product product = null;
        if (productId!=-1L){
            product = productService.getProductById(productId);

            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user==null){
                modelMap.put("needQRCode",false);
            }else {
                modelMap.put("needQRCode",true);
            }
            modelMap.put("product",product);
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品信息错误");
        }
        return modelMap;
    }

    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加顾客商品映射信息的url
    private static String productMapUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }
    @Value("${wechat.productmap.url}")
    public void setProductMapUrl(String productMapUrl) {
        ProductDetailController.productMapUrl = productMapUrl;
    }

    @RequestMapping(value = "/generateqrcode4product",method = RequestMethod.GET)
    @ResponseBody
    public void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response){
        //获取前端传递过来的商品ID
        long productId = HttpServletRequestUtil.getLong(request,"productId");
        //从session中获取当前顾客信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if (productId>-1 && user!=null && user.getUserId()!=null){
            //获取当前事件戳，以保证二维码的时效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            //将商品id，顾客id和timeStamp传入content，赋值到state中，这样微信获取到这些信息后会回传到用户商品映射信息的添加方法中
            String content = "{aaaproductIdaaa:" + productId +",aaauserIddaaa:" + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                //将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
                String longUrl = urlPrefix+productMapUrl+urlMiddle+ URLEncoder.encode(content,"UTF-8")+urlSuffix;
                //将目标URL转换为短的URL
                String shortURL = ShortNetAddressUtil.getShortURL(longUrl);
                //调用二维码生成的工具类方法，传入短的URL，生成二维码
                BitMatrix qRCodeImg = CodeUtil.generateQRCodeStream(shortURL,response);
                //将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRCodeImg,"png",response.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
