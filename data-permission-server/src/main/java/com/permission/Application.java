package com.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 赵钧
 * @Description
 * @ClassName Application
 * @create 2022/2/14 上午1:47
 * @since v1.0.0
 */
@SpringBootApplication
@MapperScan(basePackages = "com.permission.dao")
public class Application {

}
