package com.real.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.o2o.cache.JedisUtil;
import com.real.o2o.dao.HeadLineDao;
import com.real.o2o.entity.HeadLine;
import com.real.o2o.exception.HeadLineOperationException;
import com.real.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mabin
 * @create: 2019/4/29 15:50
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    @Transactional
    public List<HeadLine> headLineList(HeadLine headLineCondition) {
        //定义redis的key前缀
        String key = HEADLINELISTKEY;
        //定义接收对象
        List<HeadLine> headLineList = null;
        //定义jackson数据转换操作类
        ObjectMapper objectMapper = new ObjectMapper();

        if (headLineCondition!=null && headLineCondition.getEnableStatus()!=null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }

        if (!jedisKeys.exists(key)){
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(headLineList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = objectMapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }
}
