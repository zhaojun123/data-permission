package com.permission.model;

/**
 * @author 赵钧
 * @Description
 * @ClassName DataPermissionRule
 * @create 2022/2/28 下午3:57
 * @since v1.0.0
 */
public class DataPermissionRule {

    private String id;

    private String name;

    private String dataPermissionRangeId;


    //自定义标签，用户自定义规则的时候需要
    private String label;

    //权限运算
    private String operate;

    //权限类型 DEPT/AREA/PERSON 等
    private String type;

    //是否需要输入value
    private Boolean hasValue;

    public Boolean getHasValue() {
        return hasValue;
    }

    public void setHasValue(Boolean hasValue) {
        this.hasValue = hasValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataPermissionRangeId() {
        return dataPermissionRangeId;
    }

    public void setDataPermissionRangeId(String dataPermissionRangeId) {
        this.dataPermissionRangeId = dataPermissionRangeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
