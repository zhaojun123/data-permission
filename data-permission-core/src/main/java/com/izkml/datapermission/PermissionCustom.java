package com.izkml.datapermission;

import com.izkml.datapermission.model.Rule;

/**
 * @author 赵钧
 * @ClassName PermissionRuleCustom
 * @Description 自定义拦截
 * @create 2022/2/9 下午5:09
 * @since v1.0.0
 */
public interface PermissionCustom {

    /**
     * 自定义rule，修改相应的Operate 和 value即可
     *
     * @Author 赵钧
     * @Date 2022/2/10 下午2:59
     * @param rule
     * @return com.izkml.datapermission.model.Rule
    **/
    Rule customize(String sqlId, String businessKey, Rule rule);

    /**
     * 也可以自己拼接sql 只需要拼接条件语句即可 例如 dept.id = 2 or dept.id > 4
     * 如果该方法返回不为null 则忽略{@link PermissionCustom#customize(String, String, Rule)}
     *
     * @Author 赵钧
     * @Date 2022/2/10 下午2:59
     * @param rule
     * @return java.lang.String
    **/
    String customizeSql(String sqlId, String businessKey, Rule rule);


    class DefaultPermissionCustom implements PermissionCustom {

        @Override
        public Rule customize(String sqlId, String businessKey, Rule rule) {
            return null;
        }

        @Override
        public String customizeSql(String sqlId, String businessKey, Rule rule) {
            return null;
        }
    }

}
