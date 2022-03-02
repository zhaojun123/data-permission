package com.izkml.datapermission.model;

import java.util.List;

/**
 * @author 赵钧
 * @ClassName Rule
 * @Description TODO
 * @create 2022/2/10 下午2:27
 * @since v1.0.0
 */
public class Rule implements Cloneable{
    //权限类型
    private String type;

    //用户自定义标签
    private String label;

    private String tableName;

    private String columnName;

    //传入的参数值
    private List<String> value;

    //sql运算符
    private Operate operate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

    @Override
    public Rule clone(){
        try {
            return (Rule) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
