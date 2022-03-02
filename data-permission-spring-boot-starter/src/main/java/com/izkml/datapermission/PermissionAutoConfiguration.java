package com.izkml.datapermission;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 赵钧
 * @Description
 * @ClassName PermissionAutoConfiguration
 * @create 2022/2/14 上午1:07
 * @since v1.0.0
 */
@Configuration
@ConditionalOnClass(MybatisAutoConfiguration.class)
public class PermissionAutoConfiguration {

    @Autowired(required = false)
    PermissionCustom permissionCustom;

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor(permissionCustom);
    }

}
