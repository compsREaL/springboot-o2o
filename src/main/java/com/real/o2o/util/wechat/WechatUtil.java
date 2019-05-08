package com.real.o2o.util.wechat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.dto.UserAccessToken;
import com.real.o2o.dto.WechatUser;
import com.real.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;

/**
 * 微信工具类
 *
 * @author: mabin
 * @create: 2019/5/1 19:17
 */
public class WechatUtil {

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取UserAccessToken实体类
     *
     * @param code
     * @return
     */
    public static UserAccessToken getUserAccessToken(String code) throws IOException {
        //测试号信息里的appId
        String appId = "";
        logger.debug("appId:" + appId);
        //测试号信息的appsecret
        String appSecret = "";
        logger.debug("app secret:" + appSecret);
        //根据传入的code，拼接出访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code
                + "&grant_type=authorization_code";
        //向相应的url发送请求获取token json字符串
        String tokenStr = httpsRequest(url, "GET", null);
        logger.debug("UserAccessToken:" + tokenStr);
        UserAccessToken userAccessToken = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //将tokenStr转换为对应类的对象
            userAccessToken = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (JsonParseException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        }
        if (userAccessToken == null) {
            logger.error("获取用户accessToken失败");
        }
        return userAccessToken;
    }


    private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            //创建SSLContext对象，并使用指定的信任管理器初始化
            TrustManager[] trustManagers = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, trustManagers, new SecureRandom());
            //从上述SSLContext对象中获取SSLSocketFactory对象
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);

            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            //设置请求方式
            httpsURLConnection.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpsURLConnection.connect();
            }

            //当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //将返回的输入流转换成字符串
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpsURLConnection.disconnect();
            logger.debug("https buffer:" + buffer.toString());
        } catch (ConnectException e) {
            logger.error("wechat server connection timed out.");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return buffer.toString();
    }

    public static WechatUser getUserInfo(String accessToken, String openId) {
        //根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        //访问该URL获取用户信息json字符串
        String userStr = httpsRequest(url, "GET", null);
        logger.debug("user info:" + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (JsonParseException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        }

        if (user == null){
            return null;
        }
        return user;
    }

    /**
     * 将WechatUser里的信息转换为PersonInfo的信息并返回PersonInfo对象
     * @param wechatUser
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser wechatUser){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(wechatUser.getNickName());
        personInfo.setGender(wechatUser.getSex()+"");
        personInfo.setProfileImg(wechatUser.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }
}
