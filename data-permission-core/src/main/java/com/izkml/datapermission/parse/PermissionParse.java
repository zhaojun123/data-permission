package com.izkml.datapermission.parse;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.izkml.datapermission.PermissionCache;
import com.izkml.datapermission.PermissionCustom;
import com.izkml.datapermission.model.Operate;
import com.izkml.datapermission.model.Permission;
import com.izkml.datapermission.model.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 赵钧
 * @Description
 * @ClassName PermissionParse
 * @create 2022/2/21 上午12:03
 * @since v1.0.0
 */
public class PermissionParse {

    private String sqlId;
    private Permission permission;
    private SQLSelectQueryBlock rootQuery;
    private PermissionCustom permissionCustom;
    private int index = 0;
    private DbType dbType;
    private Map<SQLSelectQueryBlock, Map<Integer, List<SQLExprWrapper>>> conditionQueryMap;

    public PermissionParse(String sqlId, int index, Permission permission, SQLSelectQueryBlock rootQuery
            , PermissionCustom permissionCustom
            ,Map<SQLSelectQueryBlock, Map<Integer, List<SQLExprWrapper>>> conditionQueryMap ){
        this.sqlId = sqlId;
        this.permission = permission;
        this.rootQuery = rootQuery;
        this.permissionCustom = permissionCustom;
        this.index = index;
        this.conditionQueryMap = conditionQueryMap;
        this.dbType = rootQuery.getDbType();
    }

    public void parse() {
        new TableSourceParse().parse(rootQuery, null);
    }

    private class TableSourceParse {

        //是否需要添加条件语句
        private boolean addCondition = false;

        //是否是copy的query
        private boolean copy = false;

        private void parse(Object source,SQLSelectQueryBlock conditionQuery) {
            if(source == null) {
                return;
            }
            if(source instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock root = rootQuery;
                SQLSelectQueryBlock query = (SQLSelectQueryBlock)source;
                rootQuery = query;
                parse(query.getFrom(), conditionQuery);
                parseWhere(rootQuery, conditionQuery);
                rootQuery = root;
            }
            if(source instanceof SQLExprTableSource) {
                parseExprTable((SQLExprTableSource)source, conditionQuery);
            }else if(source instanceof SQLJoinTableSource) {
                parseJoinTable((SQLJoinTableSource)source, conditionQuery);
            }else if(source instanceof SQLUnionQueryTableSource) {
                parseUnionTable((SQLUnionQueryTableSource)source, conditionQuery);
            }else if(source instanceof SQLSubqueryTableSource) {
                parseSubQueryTable((SQLSubqueryTableSource)source, conditionQuery);
            }

        }

        /**
         * 分析每一层query相对应的where条件 找出子查询
         * @Author 赵钧
         * @Date 2022/2/13 下午8:12
         * @param query
         * @return void
         **/
        private void parseWhere(SQLSelectQueryBlock query, SQLSelectQueryBlock conditionQuery) {
            if(conditionQuery == null) {
                conditionQuery = query;
            }
            SQLExpr sqlExpr = query.getWhere();
            if(sqlExpr == null) {
                return;
            }
            parseWhere(sqlExpr, conditionQuery);
        }

        private void parseWhere(SQLExpr sqlExpr, SQLSelectQueryBlock conditionQuery) {
            if(sqlExpr instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr)sqlExpr;
                parseWhere(sqlBinaryOpExpr.getLeft(), conditionQuery);
                parseWhere(sqlBinaryOpExpr.getRight(), conditionQuery);
            }
            if(sqlExpr instanceof SQLInSubQueryExpr) {
                TableSourceParse where = new TableSourceParse();
                if(!copy) {
                    SQLExpr copySQLExpr = sqlExpr.clone();
                    SQLSelectQuery whereQuery = ((SQLInSubQueryExpr)copySQLExpr).getSubQuery().getQuery();
                    where.parse(whereQuery, conditionQuery);
                    where.copy = true;
                    if(where.addCondition) {
                        addCondition(new SQLExprWrapper(sqlExpr,false), conditionQuery);
                        addCondition(new SQLExprWrapper(copySQLExpr,true), conditionQuery);
                    }
                }else {
                    SQLSelectQuery whereQuery = ((SQLInSubQueryExpr)sqlExpr).getSubQuery().getQuery();
                    where.parse(whereQuery, conditionQuery);
                    this.addCondition = where.addCondition;
                }
            }
        }

        /**
         * 解析union sql
         * @Author 赵钧
         * @Date 2022/2/21 上午11:22
         * @param source
         * @param conditionQuery
         * @return void
        **/
        private void parseUnionTable(SQLUnionQueryTableSource source,SQLSelectQueryBlock conditionQuery) {
            SQLUnionQuery query = source.getUnion();
            parse(query.getLeft(),conditionQuery);
            parse(query.getRight(),conditionQuery);
        }

        /**
         * 解析 join sql
         * @Author 赵钧
         * @Date 2022/2/21 上午11:22
         * @param source
         * @param conditionQuery
         * @return void
        **/
        private void parseJoinTable(SQLJoinTableSource source,SQLSelectQueryBlock conditionQuery) {
            if(conditionQuery == null) {
                conditionQuery = rootQuery;
            }
            parse(source.getLeft(),conditionQuery);
            parse(source.getRight(),conditionQuery);
        }

        /**
         * 解析嵌套查询
         * @Author 赵钧
         * @Date 2022/2/13 下午10:17
         * @param source
         * @return void
         **/
        private void parseSubQueryTable(SQLSubqueryTableSource source,SQLSelectQueryBlock conditionQuery) {
            parse(source.getSelect().getQuery(), conditionQuery);
        }


        /**
         * 解析基本sql类型
         * @Author 赵钧
         * @Date 2022/2/21 上午9:17
         * @param source
         * @param conditionQuery 添加condition的query
         * @return boolean 是否需要添加 condition
         **/
        private void parseExprTable(SQLExprTableSource source,SQLSelectQueryBlock conditionQuery) {
            if(conditionQuery == null) {
                conditionQuery = rootQuery;
            }
            String tableName = source.getTableName();
            for(Rule rule : permission.getRules()) {
                //如果表名匹配上了则开始添加条件
                if(eqTableName(tableName, rule.getTableName())) {
                    SQLExpr condition = createCondition(source, rule);
                    if(condition != null) {
                        if(conditionQuery == rootQuery) {
                            addCondition(new SQLExprWrapper(condition,true), conditionQuery);
                        }else {
                            rootQuery.addCondition(condition);
                        }
                        addCondition = true;
                    }
                }
            }
        }

        private SQLExpr createCondition(SQLExprTableSource source, Rule rule) {
            if(rule == null || rule.getOperate() == null) {
                return null;
            }
            Operate operate = rule.getOperate();
            //调用自定义规则
            if(Operate.CUSTOM == operate) {
                String businessKey = PermissionCache.getCurrentBusinessKey();
                //自定义sql条件语句
                String sql = permissionCustom.customizeSql(sqlId, businessKey, rule);
                if(sql != null) {
                    return SQLUtils.toSQLExpr(sql, dbType);
                }
                //自定义规则
                Rule customRule = permissionCustom.customize(sqlId, businessKey, rule);
                if(customRule != null) {
                    rule = customRule;
                }
            }
            //解析规则 拼装条件语句
            switch (rule.getOperate()) {
                case GT:
                case GTE:
                case LT:
                case LTE:
                case EQ:
                case NE:
                case LIKE:
                    return singleValueOperate(source, rule);
                case IN:
                    return inOperate(source, rule);
                case BETWEEN:
                    return betweenOperate(source, rule);
                default:
                    return null;
            }
        }

        private SQLExpr singleValueOperate(SQLExprTableSource source, Rule rule) {
            StringBuilder sql = createColumnOperate(source, rule);
            sql.append(createStringValue(rule.getValue().get(0)));
            return SQLUtils.toSQLExpr(sql.toString(), dbType);
        }

        private SQLExpr inOperate(SQLExprTableSource source, Rule rule) {
            StringBuilder sql = createColumnOperate(source, rule);
            sql.append("(");
            for(String value : rule.getValue()) {
                sql.append(createStringValue(value));
            }
            sql.append(")");
            return SQLUtils.toSQLExpr(sql.toString(), dbType);
        }

        private SQLExpr betweenOperate(SQLExprTableSource source, Rule rule) {
            StringBuilder sql = createColumnOperate(source, rule);
            sql.append(rule.getValue().get(0))
                    .append(" ")
                    .append(rule.getValue().get(1));
            return SQLUtils.toSQLExpr(sql.toString(), dbType);
        }

        private StringBuilder createColumnOperate(SQLExprTableSource source,Rule rule) {
            String alias = source.getAlias();
            if(alias == null) {
                alias = source.getTableName();
            }
            return new StringBuilder(alias)
                    .append(".")
                    .append(rule.getColumnName())
                    .append(" ")
                    .append(rule.getOperate().getText())
                    .append(" ");
        }
        private StringBuilder createStringValue(String value) {
            return new StringBuilder().append("'").append(value).append("'");
        }

        private boolean eqTableName(String t1, String t2) {
            if(t1 == null || t2 == null) {
                return false;
            }
            return t1.replace("`","").equalsIgnoreCase(t2.replace("`",""));
        }

        private void addCondition(SQLExprWrapper condition, SQLSelectQueryBlock query) {
            Map<Integer, List<SQLExprWrapper>> map = conditionQueryMap.get(query);
            if(map == null) {
                map = new HashMap<>();
                conditionQueryMap.put(query, map);
            }
            List<SQLExprWrapper> list = map.get(index);
            if(list == null) {
                list = new ArrayList<>();
                map.put(index, list);
            }
            list.add(condition);
        }
    }


}
