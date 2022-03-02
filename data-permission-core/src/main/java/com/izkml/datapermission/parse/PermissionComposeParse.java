package com.izkml.datapermission.parse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.izkml.datapermission.PermissionCustom;
import com.izkml.datapermission.model.Permission;
import com.izkml.datapermission.model.PermissionCompose;

import java.util.*;

/**
 * @author 赵钧
 * @Description
 * @ClassName PermissionComposeParse
 * @create 2022/2/21 上午12:03
 * @since v1.0.0
 */
public class PermissionComposeParse {

    private PermissionCompose permissionCompose;
    private SQLSelectQueryBlock rootQuery;
    private PermissionCustom permissionCustom;
    private Map<SQLSelectQueryBlock, Map<Integer, List<SQLExprWrapper>>> conditionQueryMap = new HashMap<>();

    public PermissionComposeParse(SQLSelectQueryBlock rootQuery, PermissionCompose permissionCompose, PermissionCustom permissionCustom) {
        this.permissionCompose = permissionCompose;
        this.rootQuery = rootQuery;
        this.permissionCustom = permissionCustom;
    }

    public String parse() {
        List<Permission> permissionList = permissionCompose.getPermissions();
        for(int i = 0; i < permissionList.size(); i ++) {
            Permission permission = permissionList.get(i);
            new PermissionParse(permissionCompose.getSqlId()
                    , i
                    , permission
                    , rootQuery
                    , permissionCustom
                    , conditionQueryMap).parse();
        }
        filter(conditionQueryMap);
        conditionQueryMap.forEach((query, sqlMap) ->{
            SQLExpr or = null;
            for(List<SQLExprWrapper> list : sqlMap.values()) {
                if(list.isEmpty()) {
                    continue;
                }
                SQLExpr and = andSQLExpr(list);
                if(or == null) {
                    or = and;
                }else {
                    or = SQLBinaryOpExpr.or(or, and);
                }
            }
            query.addCondition(SQLUtils.toSQLExpr("("+or.toString()+")", rootQuery.getDbType()));
        });
        return rootQuery.toString();
    }

    private void filter(Map<SQLSelectQueryBlock, Map<Integer, List<SQLExprWrapper>>> conditionQueryMap) {
        conditionQueryMap.forEach((query, sqlMap) ->{
            Object[] keys = sqlMap.keySet().toArray();
            for(int i = 0; i < keys.length; i ++) {
                List<SQLExprWrapper> sqlExprWrapperList = sqlMap.get(i);
                for(SQLExprWrapper current : sqlExprWrapperList) {
                    //说明该SQLExpr有改动，将原SQLExpr复制给其他权限
                    if(!current.isAdd()) {
                        for(int j = 0; j < keys.length; j ++) {
                            if(j == i) {
                                continue;
                            }
                            List<SQLExprWrapper> other = sqlMap.get(j);
                            if(!containsSQLExpr(current, other)) {
                                other.add(new SQLExprWrapper(current.getSqlExpr(), true));
                            }
                        }
                    }
                }
            }
            //最后过滤掉要删除的SQLExpr
            for(List<SQLExprWrapper> deleteList : sqlMap.values()) {
                ListIterator<SQLExprWrapper> listIterator = deleteList.listIterator();
                while(listIterator.hasNext()) {
                    SQLExprWrapper sqlExprWrapper = listIterator.next();
                    if(!sqlExprWrapper.isAdd()) {
                        listIterator.remove();
                        query.removeCondition(sqlExprWrapper.getSqlExpr());
                    }
                }
            }
        });
    }

    private boolean containsSQLExpr(SQLExprWrapper sqlExprWrapper, List<SQLExprWrapper> list) {
        for(SQLExprWrapper other : list) {
            if(sqlExprWrapper.getSqlExpr().equals(other.getSqlExpr())) {
                return true;
            }
        }
        return  false;
    }

    private SQLExpr andSQLExpr(List<SQLExprWrapper> list) {
        SQLExpr sqlExpr = list.get(0).getSqlExpr();
        for(int i = 1; i < list.size(); i ++) {
            sqlExpr = SQLBinaryOpExpr.and(sqlExpr, list.get(i).getSqlExpr());
        }
        return SQLUtils.toSQLExpr("("+sqlExpr.toString()+")", rootQuery.getDbType());
    }
}