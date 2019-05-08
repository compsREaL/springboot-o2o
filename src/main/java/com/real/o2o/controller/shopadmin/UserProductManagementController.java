package com.real.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.*;
import com.real.o2o.entity.*;
import com.real.o2o.enums.UserProductMapStateEnum;
import com.real.o2o.service.*;
import com.real.o2o.util.HttpServletRequestUtil;
import com.real.o2o.util.wechat.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: mabin
 * @create: 2019/5/7 14:57
 */
@Controller
public class UserProductManagementController {

    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserproductmapsbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listUserProductMapsByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        if (pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
            //添加查询条件
            UserProductMap userProductCondition = new UserProductMap();
            userProductCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request,"productName");
            if (productName!=null){
                Product product = new Product();
                product.setProductName(productName);
                userProductCondition.setProduct(product);
            }
            //根据传入的查询条件获取该店铺的商品销售情况
            UserProductMapExecution userProductMapExecution = userProductMapService.listUserProductMap(userProductCondition,pageIndex,pageSize);
            modelMap.put("userProductMapList",userProductMapExecution.getUserProductMapList());
            modelMap.put("count",userProductMapExecution.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/listproductselldailyinfobyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listProductSellDailyInfoByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop!=null && currentShop.getShopId()!=null){
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            //获取昨天的日期
            calendar.add(Calendar.DATE,-1);
            Date endTime = calendar.getTime();
            //获取7天前的日期
            calendar.add(Calendar.DATE,-6);
            Date beginTime = calendar.getTime();
            //获取过去7天的销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition,beginTime,endTime);
            //指定日期格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表，保证商品名唯一性
            LinkedHashSet<String> legendData = new LinkedHashSet<>();
            //x轴数据
            LinkedHashSet<String> xData = new LinkedHashSet<>();
            //定义series
            List<EchartSeries> series = new ArrayList<>();
            //日销量列表
            List<Integer> totalList = new ArrayList<>();
            //当前商品名，默认为空
            String currentProductName="";
            for (int i=0;i<productSellDailyList.size();i++){
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                //自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(simpleDateFormat.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) && !currentProductName.isEmpty()){
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0,totalList.size()));
                    series.add(es);
                    //重置totalList
                    totalList = new ArrayList<>();
                    //变换下currentProductId为当前的productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    //如果还是当前的productId则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                //队列之末，需要将最后的一个商品销量信息也添加上
                if (i==productSellDailyList.size()-1){
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0,totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series",series);
            modelMap.put("legendData",legendData);
            //拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis",xAxis);
            modelMap.put("success",true);
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/adduserproductmap",method = RequestMethod.GET)
    public String addUserProductMap(HttpServletRequest request, HttpServletResponse response){
        //获取微信授权信息
        WechatAuth wechatAuth = getOperatorInfo(request);
        if (wechatAuth!=null){
            PersonInfo operator = wechatAuth.getPersonInfo();
            request.getSession().setAttribute("user",operator);
            String qRCodeInfo = null;
            try {
                //获取二维码state携带的content信息并解码
                qRCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return "shop/operationfail";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码后的内容中的aaa使用"/"代替
                wechatInfo = objectMapper.readValue(qRCodeInfo.replace("aaa","\""),WechatInfo.class);
            } catch (IOException e) {
                return "shop/operationfail";
            }
            //检验二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)){
                return "shop/operationfail";
            }
            //获取添加消费记录所需的参数并组成UserProductMap实例
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserProductMap userProductMap = compactUserProductMap4Add(customerId,productId,operator);
            //空值校验
            if (userProductMap!=null && customerId!=-1){
                try {
                    if(!checkShopAuth(operator.getUserId(),userProductMap)){
                        return "shop/operationfail";
                    }
                    //添加消费记录
                    UserProductMapExecution userProductMapExecution = userProductMapService.addUserProductMap(userProductMap);
                    if (userProductMapExecution.getState()== UserProductMapStateEnum.SUCCESS.getState()){
                        return "shop/operationsuccess";
                    }
                } catch (RuntimeException e){
                    return "shop/operationfail";
                }
            }
        }
        return "shop/operationfail";
    }

    /**
     * 检查扫码的人员是否有操作权限
     *
     * @param userId
     * @param userProductMap
     * @return
     */
    private boolean checkShopAuth(Long userId, UserProductMap userProductMap) {
        //获取店铺的所有授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
                .listShopAuthMapByShopId(userProductMap.getShop().getShopId(),1,9999);
        for(ShopAuthMap shopAuthMap:shopAuthMapExecution.getShopAuthMapList()){
            if (shopAuthMap.getEmployee().getUserId()==userId && shopAuthMap.getEnableStatus()==1){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据传入的customerId, productId以及操作员信息组建用户消费记录
     *
     * @param customerId
     * @param productId
     * @param operator
     * @return
     */
    private UserProductMap compactUserProductMap4Add(Long customerId, Long productId,PersonInfo operator) {
        UserProductMap userProductMap = null;
        if (customerId!=null && productId!=null){
            userProductMap = new UserProductMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            //主要为了获取商品积分
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShop(product.getShop());
            userProductMap.setPoint(product.getPoint());
            userProductMap.setUser(customer);
            userProductMap.setCreateTime(new Date());
            userProductMap.setOperator(operator);
        }
        return userProductMap;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过十分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo!=null){
            long nowTime = System.currentTimeMillis();
            if (nowTime-wechatInfo.getCreateTime()<=600000){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据code获取UserAccessToken，进而通过token中的openID获取微信用户信息
     * @param request
     * @return
     */
    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth wechatAuth = null;
        if (null!=code){
            UserAccessToken token;
            try {
                token = WechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId",openId);
                wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return wechatAuth;
    }
}
