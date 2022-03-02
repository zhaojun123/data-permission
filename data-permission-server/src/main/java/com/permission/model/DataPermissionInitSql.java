package com.permission.model;

/**
 * @author 赵钧
 * @Description
 * @ClassName DataPermissionInitSql
 * @create 2022/2/28 下午2:18
 * @since v1.0.0
 */
public class DataPermissionInitSql {

    private String id;

    private String dataPermissionInitId;

    private String dataPermissionSqlId;

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

    public String getDataPermissionInitId() {
        return dataPermissionInitId;
    }

    public void setDataPermissionInitId(String dataPermissionInitId) {
        this.dataPermissionInitId = dataPermissionInitId;
    }

    public String getDataPermissionSqlId() {
        return dataPermissionSqlId;
    }

    public void setDataPermissionSqlId(String dataPermissionSqlId) {
        this.dataPermissionSqlId = dataPermissionSqlId;
    }
}
