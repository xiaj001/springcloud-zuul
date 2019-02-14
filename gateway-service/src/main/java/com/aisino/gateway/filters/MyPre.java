package com.aisino.gateway.filters;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * @author: xiajun003
 * @Date: 2019/1/7 21:58
 * @Description:
 */
@Component
public class MyPre implements Predicate {
    @Override
    public boolean test(Object object) {
        System.err.println(object);
        return true;
    }
}
