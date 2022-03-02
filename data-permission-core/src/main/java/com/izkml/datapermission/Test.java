package com.izkml.datapermission;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;

import java.util.List;

/**
 * @author 赵钧
 * @ClassName Test
 * @Description TODO
 * @create 2022/2/9 下午11:54
 * @since v1.0.0
 */
public class Test {

    //select * from table1 a  left join table2 b on a.a = b.b  where a.c1 = 1
    //select * from (select * from f)
    public static void main(String[] args) {
        String sql = "select * from table1 a  left join table2 b on a.a = b.b  where a.c1 = 1";
        String dbType = "mysql";
        System.out.println("原始SQL 为 ： "+sql);
        List<SQLStatement> list = SQLUtils.parseStatements(sql, dbType);
        for(SQLStatement sqlStatement : list) {
            SQLSelectStatement statement = (SQLSelectStatement) sqlStatement;
            SQLSelect select = statement.getSelect();
            SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
            SQLJoinTableSource tableSource = (SQLJoinTableSource)query.getFrom();
            SQLExprTableSource sqlExprTableSource = (SQLExprTableSource)tableSource.getLeft();
            System.err.println(sqlExprTableSource.getTableName());
            System.err.println(sqlExprTableSource.getAlias());
        }

    }

}
