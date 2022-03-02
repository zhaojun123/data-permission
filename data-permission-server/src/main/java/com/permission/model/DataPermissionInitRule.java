package com.permission.model;

/**
 * @author 赵钧
 * @Description
 * @ClassName DataPermissionInitRange
 * @create 2022/2/23 上午10:50
 * @since v1.0.0
 */
public class DataPermissionInitRule {

    private String dataPermissionInitId;

    private String dataPermissionRangeId;

    private String dataPermissionRuleId;

    private String dataPermissionId;

    //传入sql的值，多个值用英文逗号隔开
    private String value;

    public String getDataPermissionRuleId() {
        return dataPermissionRuleId;
    }

    public void setDataPermissionRuleId(String dataPermissionRuleId) {
        this.dataPermissionRuleId = dataPermissionRuleId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDataPermissionInitId() {
        return dataPermissionInitId;
    }

    public void setDataPermissionInitId(String dataPermissionInitId) {
        this.dataPermissionInitId = dataPermissionInitId;
    }

    public String getDataPermissionRangeId() {
        return dataPermissionRangeId;
    }

    public void setDataPermissionRangeId(String dataPermissionRangeId) {
        this.dataPermissionRangeId = dataPermissionRangeId;
    }

    public String getDataPermissionId() {
        return dataPermissionId;
    }

    public void setDataPermissionId(String dataPermissionId) {
        this.dataPermissionId = dataPermissionId;
    }
}
