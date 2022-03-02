package com.izkml.datapermission.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵钧
 * @Description 同一个sqlId 和 businessKey的权限组合 Permission之间使用并集
 * @ClassName PermissionCompose
 * @create 2022/2/20 下午9:51
 * @since v1.0.0
 */
public class PermissionCompose implements Cloneable{

    private String sqlId;

    //多个permission之间使用并集
    private List<Permission> permissions;

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public PermissionCompose clone(){
        PermissionCompose permissionCompose = new PermissionCompose();
        permissionCompose.setSqlId(this.sqlId);
        List<Permission> list = new ArrayList<>();
        for(Permission permission : permissions) {
            list.add(permission.clone());
        }
        permissionCompose.setPermissions(list);
        return permissionCompose;
    }
}
