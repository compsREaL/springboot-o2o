package com.real.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 使用注解EnableTransactionManagement开启事务支持
 * 在service方法上只需添加注解Transactional即可
 *
 * @author: mabin
 * @create: 2019/5/5 10:53
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
