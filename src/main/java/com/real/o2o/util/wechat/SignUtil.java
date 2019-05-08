package com.real.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信请求校验工具类
 * @author: mabin
 * @create: 2019/5/1 17:13
 */
public class SignUtil {

    //与接口配置信息中的Token一致
    private static String token = "o2o";

    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String[] arr = new String[]{token,timestamp,nonce};
        //将token,timestamp,nonce进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tempStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            //将三个参数字符串拼接成一个字符串并进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tempStr = byteToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        //将sha1加密后的字符换与signature进行比较，标识该请求来源于微信
        return tempStr != null ? tempStr.equals(signature.toUpperCase()):false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param digest
     * @return
     */
    private static String byteToString(byte[] digest) {
        String digestStr = "";
        for (byte b:digest){
            digestStr += byToHexStr(b);
        }
        return digestStr;
    }

    /**
     * 将字节转换为十六进制字符串
     * @param b
     * @return
     */
    private static String byToHexStr(byte b) {
        char[] Digit = {'0','1','2','3','4','5','6','7','8','0','A','B','C','D','E','F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(b>>>4) & 0X0F];
        tempArr[1] = Digit[b & 0X0F];

        String s = new String(tempArr);
        return s;
    }

}
