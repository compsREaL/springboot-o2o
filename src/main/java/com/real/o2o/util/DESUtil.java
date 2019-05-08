package com.real.o2o.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * DES是一种对称加密算法
 * @author: mabin
 * @create: 2019/5/3 1:26
 */
public class DESUtil {

    private static Key key;

    private static final String KEY_STR = "myKey";
    private static final String CHARSETNAME="UTF-8";
    private static final String ALGORITHM = "DES";

    static {
        try {
            //生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //设置上密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成密钥对象
            key = generator.generateKey();
            generator = null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密后的信息
     * @param str
     * @return
     */
    public static String getEncryptString(String str){
        //基于BASE64编码，接受byte[]并转换为String
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            //按UTF-8编码
            byte[] bytes = str.getBytes(CHARSETNAME);
            //获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE,key);
            //加密
            byte[] doFinal = cipher.doFinal(bytes);
            //获取加密后的string
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取解密之后的信息
     * @param str
     * @return
     */
    public static String getDecryptString(String str){
        //基于BASE64编码，接受byte[]并转换为String
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            //将字符串转换为byte[]
            byte[] bytes = base64Decoder.decodeBuffer(str);
            //获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE,key);
            //解密
            byte[] doFinal = cipher.doFinal(bytes);
            //返回解密之后的信息
            return new String(doFinal,CHARSETNAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getEncryptString("root"));
        System.out.println(getEncryptString("123456"));
    }
}
