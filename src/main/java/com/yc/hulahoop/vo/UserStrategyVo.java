package com.yc.hulahoop.vo;

import java.util.List;

public class UserStrategyVo {

    private int cityId;
    private int count;
    private String cityName;
    private List<StrategyVo> strategyVoList;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCount() {
        return count;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StrategyVo> getStrategyVoList() {
        return strategyVoList;
    }

    public void setStrategyVoList(List<StrategyVo> strategyVoList) {
        this.strategyVoList = strategyVoList;
    }
}
