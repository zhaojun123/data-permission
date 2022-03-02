package com.permission.model;

/**
 * @author 赵钧
 * @ClassName DataPermission
 * @Description 初始化权限表
 * @create 2022/2/9 下午2:49
 * @since v1.0.0
 */
public class DataPermissionInit {

    private String id;

    private String name;

    private String comment;

    //指定菜单范围，多个菜单用,隔开
    private String menuIds;

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
