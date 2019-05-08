package com.real.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.ImageHolder;
import com.real.o2o.dto.ProductExecution;
import com.real.o2o.entity.Product;
import com.real.o2o.entity.ProductCategory;
import com.real.o2o.entity.Shop;
import com.real.o2o.enums.ProductStateEnum;
import com.real.o2o.exception.ProductOperationException;
import com.real.o2o.service.ProductCategoryService;
import com.real.o2o.service.ProductService;
import com.real.o2o.util.CodeUtil;
import com.real.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mabin
 * @create: 2019/4/19 13:49
 */
@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传的商品详情图的最大数量
    private static final int IMAGEMAXCOUNT = 6;

    @RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductListByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取前台传来的每页要求返回的商品总数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if (pageIndex>-1 && pageSize>-1 && currentShop!=null && currentShop.getShopId()!=null){
            //根据传入的条件进行组合
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            //进行查询
            ProductExecution productExecution = productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("success",true);
            modelMap.put("count",productExecution.getCount());
            modelMap.put("productList",productExecution.getProductList());
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName!=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码判断
        if ( !statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }

        //接受前端参数的变量的初始化，包括商品，缩略图，详情图等
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        ImageHolder imageHolder = null;
        List<ImageHolder> imageHolderList = new ArrayList<>();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //若请求中存在文件流，则取出相关文件
        try {
            if (resolver.isMultipart(request)){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建ImageHolder对象
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                if (thumbnailFile!=null){
                    imageHolder = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
                }
                //取出详情图片并构建List<ImageHolder>对象
                for (int i=0;i<IMAGEMAXCOUNT;i++){
                    CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg"+i);
                    //若取出的第i个详情图片文件流不为空，则将其加入到详情图列表
                    if (productImgFile!=null){
                        imageHolderList.add(new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream()));
                    }else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request,"productStr");
            product = objectMapper.readValue(productStr, Product.class);
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        if (product!=null){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                ProductExecution productExecution = productService.modifyProduct(product,imageHolder,imageHolderList);
                if (productExecution.getState()== ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","商品信息错误");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductById(@RequestParam("productId") Long productId) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productId >= 0) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
                modelMap.put("success",true);
                modelMap.put("productCategoryList",productCategoryList);
                modelMap.put("product",product);
            } else {
                modelMap.put("success",false);
                modelMap.put("errMsg","商品信息错误");
            }
        } else {
            modelMap.put("success",false);
            modelMap.put("errMsg","Empty productId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        //处理文件流
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        //处理缩略图
        ImageHolder imageHolder = null;
        List<ImageHolder> imageHolderList = new ArrayList<>();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if (resolver.isMultipart(request)) {
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //取出缩略图并构建imageHolder对象
                CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                imageHolder = new ImageHolder(commonsMultipartFile.getOriginalFilename(), commonsMultipartFile.getInputStream());
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productImageFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productImageFile != null) {
                        ImageHolder productImg = new ImageHolder(productImageFile.getOriginalFilename(), productImageFile.getInputStream());
                        imageHolderList.add(productImg);
                    } else {
                        break;
                    }
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        try {
            //尝试从获取前端传送过来的表单string流并将其转化为Product实体类对象
            product = objectMapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        //若product，缩略图以及详情图列表非空，则开始进行商品添加操作
        if (product != null && imageHolder != null && imageHolderList != null) {
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行添加操作
                ProductExecution productExecution = productService.addProduction(product, imageHolder, imageHolderList);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }
}
