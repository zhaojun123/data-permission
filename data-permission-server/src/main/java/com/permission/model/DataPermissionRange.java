package com.permission.model;
/**
 * @author 赵钧
 * @ClassName DataPermissionRange
 * @Description 权限范围
 * @create 2022/2/9 下午2:52
 * @since v1.0.0
 */
public class DataPermissionRange {

    private String id;

    private String name;

    //权限类型 DEPT/AREA/PERSON 等
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
