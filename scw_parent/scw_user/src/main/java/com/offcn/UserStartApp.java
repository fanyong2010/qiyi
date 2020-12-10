package com.offcn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
// 去哪个包下扫描mybatis的接口文件
@MapperScan("com.offcn.user.mapper")
public class UserStartApp {

    public static void main(String[] args) {
        SpringApplication.run(UserStartApp.class);
    }

}
