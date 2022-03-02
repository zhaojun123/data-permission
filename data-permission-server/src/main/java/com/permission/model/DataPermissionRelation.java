package com.permission.model;

/**
 * @author 赵钧
 * @ClassName DataPermissionRelation
 * @Description 权限关联表
 * @create 2022/2/9 下午4:15
 * @since v1.0.0
 */
public class DataPermissionRelation {

    private String id;

    private String dataPermissionId;

    private String relationId;

    //关联类型 ROLE/PERSON 等
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataPermissionId() {
        return dataPermissionId;
    }

    public void setDataPermissionId(String dataPermissionId) {
        this.dataPermissionId = dataPermissionId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
