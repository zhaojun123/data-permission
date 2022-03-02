package com.permission.dao;

import com.permission.model.Order;
import org.apache.ibatis.annotations.Select;

/**
 * @author 赵钧
 * @Description
 * @ClassName OrderDao
 * @create 2022/2/14 上午1:38
 * @since v1.0.0
 */
public interface OrderDao {

    @Select("SELECT * FROM `order` o LEFT JOIN `area` a ON o.area_id = a.id WHERE o.user_id IN (SELECT user_id FROM dept)")
    public Order select();
}
