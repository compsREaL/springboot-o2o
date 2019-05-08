package com.real.o2o.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: mabin
 * @create: 2019/5/6 20:57
 */
public class ShortNetAddressUtil {

    private static Logger logger = LoggerFactory.getLogger(ShortNetAddressUtil.class);

    public static int TIMEOUT = 30*1000;
    public static String ENCODING = "UTF-8";
    private final static String TOKEN = "8930b8561a9155ce7ab21d5ee325600e";

    /**
     * 将长的URL转换为短URL以保证二维码正常生产
     * @param originURL
     * @return
     */
    public static String getShortURL(String originURL){
        String shortUrl = null;
        String params = "{\"url\":\""+originURL+"\"}";
        try {
            //指定百度短视频的接口
            URL url = new URL("https://dwz.cn/admin/v2/create");
            //建立连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接的参数
            //使用连接进行输出
            connection.setDoOutput(true);
            //使用连接进行输入
            connection.setDoInput(true);
            //不使用缓存
            connection.setUseCaches(false);
            //设置连接超时时间为30s
            connection.setConnectTimeout(TIMEOUT);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("content-type","application/json");
            connection.setRequestProperty("Token", TOKEN);
            //设置请求模式为POST
            connection.setRequestMethod("POST");
            connection.connect();
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),ENCODING);
            writer.append(params).flush();
            writer.close();
            //获取返回的字符串
            String responseStr = getResponseStr(connection);
            logger.info("response string:"+responseStr);
            //在字符串里获取tinyurl，即短链接
            shortUrl = getValueByKey(responseStr,"ShortUrl");
            logger.info("shortUrl:"+shortUrl);
            connection.disconnect();
        } catch (IOException e) {
            logger.error("getShortUrl error:{}",e.getMessage());
        }
        return shortUrl;
    }

    /**
     * JSON依据传入的key获取value
     * @param responseStr
     * @param key
     * @return
     */
    private static String getValueByKey(String responseStr, String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        //定义json结点
        JsonNode node;
        String targetValue=null;
        try {
            //把调用返回的消息串转换成json对象
            node = objectMapper.readTree(responseStr);
            //根据key从json对象中获取对应的值
            targetValue = node.get(key).asText();
        } catch (IOException e) {
            logger.error("getValueByKey error:{}",e.getMessage());
            e.printStackTrace();
        }
        return targetValue;
    }

    /**
     * 通过HttpConnection获取返回的字符串
     * @param connection
     * @return
     */
    private static String getResponseStr(HttpURLConnection connection) throws IOException {
        StringBuffer result = new StringBuffer();
        //从连接中获取http状态码
        int responseCode = connection.getResponseCode();

        if (responseCode==HttpURLConnection.HTTP_OK){
            //如果返回的状态码是OK，则取出连接的输入流
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,ENCODING));
            String inputLine = "";
            while ((inputLine = reader.readLine())!=null){
                result.append(inputLine);
            }
        }
        return String.valueOf(result);
    }

    public static void main(String[] args) {
        getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");
    }
}
