package com.real.o2o.util;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 将HttpServletRequest请求中的参数转化为相应的数据类型
 * @author: mabin
 * @create: 2019/4/15 15:46
 */
public class HttpServletRequestUtil {

    public static int getInt(HttpServletRequest request,String key){
        try {
            return Integer.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    public static long getLong(HttpServletRequest request,String key){
        try {
            return Long.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1L;
        }
    }

    public static double getDouble(HttpServletRequest request,String key){
        try {
            return Double.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1d;
        }
    }

    public static boolean getBoolean(HttpServletRequest request,String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        }catch (Exception e){
            return false;
        }
    }

    public static String getString(HttpServletRequest request,String key){
        try {
            String str = request.getParameter(key);
            if (str!=null && !"".equals(str.trim())){
                return str.trim();
            }else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    public static String decodeString(HttpServletRequest request,String key){
        try {
            String str = new String(request.getParameter(key).getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            if (str!=null && "".equals(str.trim())){
                return str.trim();
            }else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }
}
