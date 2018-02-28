package com.yc.hulahoop.pojo;

public class UserStrategy {
    private Integer id;

    private Integer strategyId;

    private Integer cityId;

    private Integer userId;

    public UserStrategy(Integer id, Integer strategyId, Integer cityId, Integer userId) {
        this.id = id;
        this.strategyId = strategyId;
        this.cityId = cityId;
        this.userId = userId;
    }

    public UserStrategy() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}