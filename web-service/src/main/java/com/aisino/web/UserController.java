package com.aisino.web;

import com.aisino.web.exception.BizException;
import com.lianjia.matrix.dto.user.BrandGetRepDTO;
import com.lianjia.matrix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Autowired
    //@Lazy
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


    @GetMapping("/user")
    public List<BrandGetRepDTO> user(){
        logger.info("user home");
        return userService.getBrandList();
    }
}
