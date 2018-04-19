package com.yc.hulahoop.vo;

import java.math.BigDecimal;

public class UserBehaviourVo {
    private Integer order;

    private Integer id;

    private Integer userId;

    private Integer strategyId;

    private BigDecimal preference;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public BigDecimal getPreference() {
        return preference;
    }

    public void setPreference(BigDecimal preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "UserBehaviourVo{" +
                "order=" + order +
                ", id=" + id +
                ", userId=" + userId +
                ", strategyId=" + strategyId +
                ", preference=" + preference +
                '}';
    }
}
