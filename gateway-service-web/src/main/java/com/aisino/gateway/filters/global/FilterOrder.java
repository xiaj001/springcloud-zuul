package com.aisino.gateway.filters.global;

/**
 * @author: xiajun003
 * @Date: 2019/1/7 15:40
 * @Description:
 */
public enum  FilterOrder {

    /**
     * order值越小，preFilter 越先执行，postFilter 越后执行
     */
    AccessFilterCoder(100),
    SignFilterOrder(200),
    RateLimitFilterOrder(300);
    private int order;
    public int getOrder(){
        return this.order;
    }
    FilterOrder(int order){
        this.order = order;
    }
}
