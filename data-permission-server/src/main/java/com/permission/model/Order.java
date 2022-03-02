package com.permission.model;

import java.math.BigDecimal;

/**
 * @author 赵钧
 * @Description
 * @ClassName Order
 * @create 2022/2/14 上午1:34
 * @since v1.0.0
 */
public class Order {

    private String id;

    private String userId;

    private BigDecimal amount;

    private String areaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
