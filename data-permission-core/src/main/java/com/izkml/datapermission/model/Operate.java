package com.izkml.datapermission.model;

/**
 * @author 赵钧
 * @ClassName PermissionRule
 * @Description sql操作
 * @create 2022/2/9 下午4:56
 * @since v1.0.0
 */
public enum Operate {

    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    EQ("="),
    NE("!="),
    IN("in"),
    LIKE("like"),
    BETWEEN("between"),
    //自定义
    CUSTOM("custom");

    private String text;

    Operate(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
