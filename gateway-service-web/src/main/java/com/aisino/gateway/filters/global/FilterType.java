package com.aisino.gateway.filters.global;

/**
 * @author 程天亮
 * @Created
 */
public enum FilterType {
    Pre("pre"), Post("post"), Routing("routing"), Error("error");
    private String type;

    FilterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
