package com.permission.dao;

import com.izkml.datapermission.model.PermissionParams;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 赵钧
 * @Description
 * @ClassName DataPermissionDao
 * @create 2022/3/1 上午2:24
 * @since v1.0.0
 */
public interface DataPermissionDao {

    @Select("select relation.relation_id login_id,relation.data_permission_id permission_id,_sql.sql_id,rule.name,rule.operate,init_rule.value,init_sql.menu_ids,_sql.table_name,_sql.column_name,rule.label,rule.type from data_permission_relation relation \n" +
            "\tleft join data_permission_init_rule init_rule on init_rule.data_permission_id = relation.data_permission_id \n" +
            "\tleft join data_permission_init_sql init_sql on init_sql.data_permission_init_id = init_rule.data_permission_init_id\n" +
            "\tleft join data_permission_rule rule on rule.id = init_rule.data_permission_rule_id \n" +
            "\tleft join data_permission_sql _sql on _sql.id = init_sql.data_permission_sql_id\n" +
            "\twhere _sql.type = rule.type")
    public List<PermissionParams> list();

}
