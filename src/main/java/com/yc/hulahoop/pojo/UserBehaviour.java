package com.yc.hulahoop.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class UserBehaviour implements Comparable<UserBehaviour>{
    private Integer id;

    private Integer userId;

    private Integer strategyId;

    private BigDecimal preference;

    private Date createTime;

    private Date updateTime;

    public UserBehaviour(Integer id, Integer userId, Integer strategyId, BigDecimal preference, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.strategyId = strategyId;
        this.preference = preference;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserBehaviour() {
        super();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(UserBehaviour o) {
        return o.preference.compareTo(this.getPreference());
    }

    @Override
    public String toString() {
        return "UserBehaviour{" +
                "id=" + id +
                ", userId=" + userId +
                ", strategyId=" + strategyId +
                ", preference=" + preference +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}