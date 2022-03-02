package com.permission.model;

/**
 * @author 赵钧
 * @ClassName dataPermissionSql
 * @Description 权限关联的sql
 * @create 2022/2/9 下午2:41
 * @since v1.0.0
 */
public class DataPermissionSql {

    private String id;

    private String sqlId;

    private String tableName;

    private String columnName;

    //权限类型 DEPT/AREA/PERSON 等
    private String type;

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
