package com.real.o2o.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author: mabin
 * @create: 2019/5/3 1:54
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static Logger logger = LoggerFactory.getLogger(EncryptPropertyPlaceholderConfigurer.class);

    //需要加密的字段数组
    private String[] encryptPropertyNames = {"jdbc.username","jdbc.password"};

    /**
     * 对关键属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    protected String convertProperty(String propertyName,String propertyValue){
        if (isEncryptProp(propertyName)){
            //对已加密的字段进行解密工作
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            logger.debug("加密的属性：{}，解密前的结果：{},解密后的结果：{}",propertyName,propertyValue,decryptValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    /**
     * 判断该属性是否已加密
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName) {
        //若等于需要加密的field，则需要加密
        for (String str : encryptPropertyNames){
            if (str.equals(propertyName)){
                return true;
            }
        }
        return false;
    }
}
