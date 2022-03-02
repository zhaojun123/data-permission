package com.permission;

import com.izkml.datapermission.PermissionCache;
import com.izkml.datapermission.PermissionCustom;
import com.izkml.datapermission.model.Operate;
import com.izkml.datapermission.model.Rule;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author 赵钧
 * @Description
 * @ClassName MyPermissionCustom
 * @create 2022/2/14 上午9:50
 * @since v1.0.0
 */
@Component
public class MyPermissionCustom implements PermissionCustom {

    @Override
    public Rule customize(String sqlId, String businessKey, Rule rule) {
        //获取当前登陆人
        String userId = PermissionCache.getCurrentLoginId();
        //如果是地区查找本地区以及下属地区
        if("areaMyselfAndLower".equals(rule.getLabel())) {
            rule.setOperate(Operate.LIKE);
            rule.setValue(Arrays.asList("0100%"));
        //如果是部门查找本部门和下属部门
        }else if("deptMyselfAndLower".equals(rule.getLabel())) {
            rule.setOperate(Operate.IN);
            rule.setValue(Arrays.asList("1","2","3"));
        }
        return rule;
    }

    @Override
    public String customizeSql(String sqlId, String businessKey, Rule rule) {
        if("areaMyselfAndLower".equals(rule.getLabel())) {
            return "o.area_id like '0100%'";
            //如果是部门查找本部门和下属部门
        }else if("deptMyselfAndLower".equals(rule.getLabel())) {
            return "dept.id in ('1','2','3')";
        }
        return null;
    }
}
