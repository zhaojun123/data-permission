package com.izkml.datapermission;

import com.izkml.datapermission.model.PermissionCompose;
import com.izkml.datapermission.parse.SqlParse;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author 赵钧
 * @ClassName PermissionInterceptor
 * @Description TODO
 * @create 2022/2/9 下午4:40
 * @since v1.0.0
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PermissionInterceptor implements Interceptor {

    private volatile static PermissionCustom permissionCustom = new PermissionCustom.DefaultPermissionCustom();

    public PermissionInterceptor(PermissionCustom permissionCustom) {
        if(permissionCustom != null) {
            PermissionInterceptor.permissionCustom = permissionCustom;
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject
                .forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                        new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String sqlId = mappedStatement.getId();
        //当前用户的权限
        PermissionCompose permissionCompose = PermissionCache.currentSqlPermission(sqlId);
        if(permissionCompose != null) {
            new SqlParse(permissionCompose, statementHandler, permissionCustom).parse();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
