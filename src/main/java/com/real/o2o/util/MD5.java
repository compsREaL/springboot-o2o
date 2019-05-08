package com.real.o2o.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author: mabin
 * @create: 2019/5/3 6:28
 */
public class MD5 {

    /**
     * 对传入的String进行MD5加密
     * @param s
     * @return
     */
    public static final String getMD5(String s) {
        //16进制数组
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            char[] str;
            byte[] tempStr = s.getBytes();
            //获取MD5加密对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            //传入需要加密的目标数组
            md.update(tempStr);
            //获取加密后的数组
            byte[] bytes = md.digest();
            int j = bytes.length;
            str = new char[2*j];
            int k = 0;
            //将数组做移位
            for (int i=0;i<j;i++){
                byte b = bytes[i];
                str[k++] = hexDigits[b>>>4 & 0Xf];
                str[k++] = hexDigits[b & 0Xf];
            }
            //转换成String并返回
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
    }
}
