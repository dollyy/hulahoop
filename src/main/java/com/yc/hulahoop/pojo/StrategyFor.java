package com.yc.hulahoop.pojo;

public class StrategyFor {
    private Integer id;

    private Integer userId;

    private Integer strategyId;

    private Integer status;

    public StrategyFor(Integer id, Integer userId, Integer strategyId, Integer status) {
        this.id = id;
        this.userId = userId;
        this.strategyId = strategyId;
        this.status = status;
    }

    public StrategyFor() {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}