package com.izkml.datapermission.parse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.izkml.datapermission.PermissionCustom;
import com.izkml.datapermission.model.Permission;
import com.izkml.datapermission.model.PermissionCompose;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 赵钧
 * @Description
 * @ClassName SqlParse
 * @create 2022/2/10 下午5:24
 * @since v1.0.0
 */
public class SqlParse {

    private StatementHandler statementHandler;
    private PermissionCustom permissionCustom;
    private PermissionCompose permissionCompose;
    private final static String dbType = "mysql";

    public SqlParse(PermissionCompose permissionCompose, StatementHandler statementHandler, PermissionCustom permissionCustom) {
        this.statementHandler = statementHandler;
        this.permissionCustom = permissionCustom;
        this.permissionCompose = permissionCompose;
    }

    public void parse() {
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        //目前只支持单条sql
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, dbType).get(0);
        SQLSelectStatement statement = (SQLSelectStatement) sqlStatement;
        SQLSelect select = statement.getSelect();
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        String newSql = new PermissionComposeParse(query, permissionCompose, permissionCustom).parse();
        Field field = null;
        try {
            field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
