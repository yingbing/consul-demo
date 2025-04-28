package com.ice.client;

import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.ice.client")  // 扫描实体类
@EnableIgniteRepositories("com.ice.client.repository") // 启用JPA仓库
@SpringBootApplication
public class IgniteClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(IgniteClientApplication.class, args);
    }
}