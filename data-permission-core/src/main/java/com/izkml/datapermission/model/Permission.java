package com.izkml.datapermission.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵钧
 * @Description
 * @ClassName Permission
 * @create 2022/2/20 下午9:57
 * @since v1.0.0
 */
public class Permission implements Cloneable{

    //多个rule之间使用交集
    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public Permission clone(){
        Permission permission = new Permission();
        List<Rule> list = new ArrayList<>();
        for(Rule rule : rules) {
            list.add(rule.clone());
        }
        permission.setRules(list);
        return permission;
    }
}
