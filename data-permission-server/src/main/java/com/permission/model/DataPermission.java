package com.permission.model;

/**
 * @author 赵钧
 * @ClassName DataPermissionCustom
 * @Description 客户具体定义的权限
 * @create 2022/2/9 下午4:01
 * @since v1.0.0
 */
public class DataPermission {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
