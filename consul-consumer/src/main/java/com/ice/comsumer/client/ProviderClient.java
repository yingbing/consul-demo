package com.ice.comsumer.client;

import com.ice.comsumer.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider", configuration = FeignConfig.class)
//@FeignClient(name = "providerClient", url = "http://localhost:8081")
public interface ProviderClient {
    @GetMapping("/hello")  // 映射到 Provider 的接口路径
    String hello();
}