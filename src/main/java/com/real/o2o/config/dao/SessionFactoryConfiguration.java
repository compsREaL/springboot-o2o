package com.real.o2o.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;


/**
 * @author: mabin
 * @create: 2019/5/5 10:31
 */
@Configuration
public class SessionFactoryConfiguration {

    @Autowired
    private DataSource dataSource;

    //mybatis-config.xml文件路径
    private static String mybatisConfigFile;
    //mapper文件路径
    private static String mapperPath;
    //实体类所在包
    @Value("${type_alias_package}")
    private String typeAliasPackage;

    @Value("${mybatis_config_file}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }

    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SessionFactoryConfiguration.mapperPath = mapperPath;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean creataSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        //添加mapper扫描路径
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
        return sqlSessionFactoryBean;
    }
}
