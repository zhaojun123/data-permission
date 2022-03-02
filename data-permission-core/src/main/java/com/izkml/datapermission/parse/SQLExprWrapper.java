package com.izkml.datapermission.parse;

import com.alibaba.druid.sql.ast.SQLExpr;

/**
 * @author 赵钧
 * @Description
 * @ClassName SQLExprWrapper
 * @create 2022/2/22 上午12:32
 * @since v1.0.0
 */
public class SQLExprWrapper {

    private SQLExpr sqlExpr;

    //true为添加 false为删除
    private boolean add;

    public SQLExprWrapper(SQLExpr sqlExpr, boolean add) {
        this.sqlExpr = sqlExpr;
        this.add = add;
    }

    public SQLExpr getSqlExpr() {
        return sqlExpr;
    }

    public boolean isAdd() {
        return add;
    }
}
