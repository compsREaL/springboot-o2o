package com.real.o2o.util;

import com.google.code.kaptcha.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码验证工具类
 * @author: mabin
 * @create: 2019/4/15 15:42
 */
public class CodeUtil {

    public static boolean checkVerifyCode(HttpServletRequest request){

        String verifyCodeExpect = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if (verifyCodeActual != null && verifyCodeActual.equals(verifyCodeExpect)){
            return true;
        }
        return false;
    }

    /**
     * 生成二维码图片流
     * @param content
     * @param response
     * @return
     */
    public static BitMatrix generateQRCodeStream(String content, HttpServletResponse response){
        response.setHeader("Cache-Control","no-store");
        response.setHeader("Pragma","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/png");
        //设置图片的文字编码以及内边框距
        Map<EncodeHintType,Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        hints.put(EncodeHintType.MARGIN,0);
        BitMatrix bitMatrix;
        try {
            //参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,300,300,hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }
}
