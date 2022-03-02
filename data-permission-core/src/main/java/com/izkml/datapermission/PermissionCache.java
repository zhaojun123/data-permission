package com.izkml.datapermission;

import com.izkml.datapermission.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 赵钧
 * @ClassName PermissionCache
 * @Description 权限缓存 存储权限数据和用户的对应关系 和 当前登陆用户
 * @create 2022/2/9 下午7:27
 * @since v1.0.0
 */
public class PermissionCache {

    private final static String DEFAULT_BUSINESS_KEY = "DEFAULT_BUSINESS_KEY";

    private static volatile Map<String, Map<String,PermissionCompose>> cache = new HashMap<>();

    private static ThreadLocal<String> currentLogin = new ThreadLocal<>();

    private static ThreadLocal<String> currentBusinessKey = new ThreadLocal<>();

    public static String getCurrentLoginId() {
        return currentLogin.get();
    }

    public static void setCurrentLoginId(String loginId) {
        currentLogin.set(loginId);
    }

    public static void cleanCurrentLogin() {
        currentLogin.set(null);
    }

    public static String getCurrentBusinessKey() {
        return currentBusinessKey.get();
    }

    public static void setCurrentBusinessKey(String businessKey) {
        currentBusinessKey.set(businessKey);
    }

    public static void cleanCurrentBusinessKey() {
        currentBusinessKey.set(null);
    }

    public synchronized static void cleanPermission() {
        cache.clear();
    }

    public synchronized static void cleanPermission(String userId) {
        cache.put(userId, null);
    }

    private static String createKey(String sqlId, String businessKey) {
        if(businessKey == null) {
            businessKey = DEFAULT_BUSINESS_KEY;
        }
        return new StringBuilder(sqlId).append("|").append(businessKey).toString();
    }

    private static <T, R> Map<String, List<PermissionParams>> listToMap(List params, Function<T, R> function) {
        Map<String, List<PermissionParams>> map = (Map<String, List<PermissionParams>>) params.stream()
                .collect(Collectors.toMap(function, p-> {
                        List permissionList = new ArrayList<>();
                            permissionList.add(p);
                            return permissionList;
                        },
                        (List v1, List v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }
        ));
        return map;
    }

    public synchronized static void setPermission(List<PermissionParams> params) {
        if(params == null) {
            cleanPermission();
        }
        ConcurrentHashMap<String, Map<String,PermissionCompose>> newCache = new ConcurrentHashMap<>();
        Map<String, List<PermissionParams>> userMap = listToMap(params,PermissionParams::getLoginId);
        userMap.forEach((userId,userV)->{
            Map<String, PermissionCompose> permissionComposeMap = new HashMap<>();
            Map<String, List<PermissionParams>> sqlMap = listToMap(userV, PermissionParams::getSqlId);
            sqlMap.forEach((sqlId,sqlV)->{
                PermissionCompose permissionCompose = new PermissionCompose();
                List<Permission> permissionList = new ArrayList<>();
                permissionCompose.setPermissions(permissionList);
                permissionCompose.setSqlId(sqlId);
                Map<String,List<PermissionParams>> permissionMap = listToMap(sqlV,PermissionParams::getPermissionId);
                permissionMap.forEach((permissionId, permissionV)->{
                    Permission permission = new Permission();
                    List<Rule> ruleList = new ArrayList<>();
                    permission.setRules(ruleList);
                    for(PermissionParams permissionParams : permissionV) {
                        ruleList.add(createRule(permissionParams));
                    }
                    permissionList.add(permission);
                });
                String menuIds = sqlV.get(0).getMenuIds();
                if(menuIds == null) {
                    menuIds = DEFAULT_BUSINESS_KEY;
                }
                for(String menuId : menuIds.split(",")) {
                    String key = createKey(sqlId,menuId);
                    permissionComposeMap.put(key, permissionCompose);
                }
            });
            newCache.put(userId, permissionComposeMap);
        });
        cache = newCache;
    }

    private static PermissionCompose createPermissionCompose(PermissionParams params) {
        PermissionCompose permissionCompose = new PermissionCompose();
        permissionCompose.setSqlId(params.getSqlId());
        List<Permission> permissionList = new ArrayList<>();
        Permission permission = new Permission();
        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(createRule(params));
        permission.setRules(ruleList);
        permissionCompose.setPermissions(permissionList);
        return permissionCompose;
    }

    private static Rule createRule(PermissionParams params) {
        Rule rule = new Rule();
        Operate operate = Operate.valueOf(params.getOperate());
        rule.setOperate(operate);
        rule.setType(params.getType());
        rule.setLabel(params.getLabel());
        rule.setColumnName(params.getColumnName());
        rule.setTableName(params.getTableName());
        if(params.getValue() != null) {
            rule.setValue(Arrays.asList(params.getValue().split(",")));
        }
        return rule;
    }

    public static PermissionCompose getPermissionOrDefault(String loginId, String sqlId, String businessKey) {
        if(loginId == null) {
            return null;
        }
        PermissionCompose permissionCompose = getPermission(loginId, sqlId, businessKey);
        if(permissionCompose == null && businessKey != null) {
            return getPermission(loginId, sqlId, null);
        }
        return permissionCompose;
    }

    private static PermissionCompose getPermission(String loginId, String sqlId, String businessKey) {
        if(loginId == null) {
            return null;
        }
        Map<String, PermissionCompose> map = cache.get(loginId);
        if(map == null) {
            return null;
        }
        String key = createKey(sqlId, businessKey);
        return map.get(key);
    }

    public static PermissionCompose currentSqlPermission(String sqlId) {
        String loginId = getCurrentLoginId();
        String businessKey = getCurrentBusinessKey();
        return getPermissionOrDefault(loginId, sqlId, businessKey);
    }
}
