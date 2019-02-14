package com.aisino.web;

import com.lianjia.matrix.spring.boot.EnableLianJiaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: xiajun003
 * @Date: 2019/1/26 09:18
 * @Description:
 */
@SpringBootApplication
@EnableLianJiaService
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class,args);
    }
}
