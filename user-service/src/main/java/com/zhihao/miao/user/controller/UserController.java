package com.zhihao.miao.user.controller;

import com.lianjia.matrix.dto.user.BrandGetRepDTO;
import com.lianjia.matrix.service.UserService;
import com.zhihao.miao.user.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;


    @Autowired@Lazy
    private UserService userService;


    @RequestMapping(value="/index",method = RequestMethod.GET)
    public String index(){
       logger.info("user index");
       return "user index";
    }

    @GetMapping("/home")
    public String home(){
        logger.info("user home");
        return "user home";
    }

    public String index2(){
        logger.info("user index2");
        throw new NullPointerException("空指针异常");
    }

    @GetMapping("/sidecar")
    public String sidecar(){
        String response = restTemplate.getForObject("http://zuul-sidecar/",String.class);
        return response;
    }


    @PostMapping("/post")
    public String post(){
        logger.info("user home");
        return "user home";
    }


    @PostMapping("/exception")
    public String exception(){
        logger.info("user home");
        throw new RuntimeException("test run time exception");
    }


    @PostMapping("/bizException")
    public String bizException(){
        logger.info("user home");
        throw new BizException("test run time bizException",600000);
    }


/*
    @GetMapping("/user")
    public List<BrandGetRepDTO> user(){
        logger.info("user home");
        return userService.getBrandList();
    }*/
}
