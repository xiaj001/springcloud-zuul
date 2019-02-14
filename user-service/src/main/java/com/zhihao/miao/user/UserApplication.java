package com.zhihao.miao.user;

import com.lianjia.matrix.spring.boot.EnableLianJiaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


//@EnableDiscoveryClient
@SpringBootApplication
@EnableLianJiaService
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }

    @Bean
    //@LoadBalanced
    public RestTemplate template(){
        return new RestTemplate();
    }
}



