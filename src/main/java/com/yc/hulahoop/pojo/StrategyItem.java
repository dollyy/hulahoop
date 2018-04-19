package com.yc.hulahoop.pojo;

public class StrategyItem {
    private Integer id;

    private Integer strategyId;

    private Integer areaHd;

    private Integer areaHn;

    private Integer areaHz;

    private Integer areaHb;

    private Integer areaXb;

    private Integer areaXn;

    private Integer areaDb;

    private Integer areaOther;

    public StrategyItem(Integer id, Integer strategyId, Integer areaHd, Integer areaHn, Integer areaHz, Integer areaHb, Integer areaXb, Integer areaXn, Integer areaDb, Integer areaOther) {
        this.id = id;
        this.strategyId = strategyId;
        this.areaHd = areaHd;
        this.areaHn = areaHn;
        this.areaHz = areaHz;
        this.areaHb = areaHb;
        this.areaXb = areaXb;
        this.areaXn = areaXn;
        this.areaDb = areaDb;
        this.areaOther = areaOther;
    }

    public StrategyItem() {
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

    public Integer getAreaHd() {
        return areaHd;
    }

    public void setAreaHd(Integer areaHd) {
        this.areaHd = areaHd;
    }

    public Integer getAreaHn() {
        return areaHn;
    }

    public void setAreaHn(Integer areaHn) {
        this.areaHn = areaHn;
    }

    public Integer getAreaHz() {
        return areaHz;
    }

    public void setAreaHz(Integer areaHz) {
        this.areaHz = areaHz;
    }

    public Integer getAreaHb() {
        return areaHb;
    }

    public void setAreaHb(Integer areaHb) {
        this.areaHb = areaHb;
    }

    public Integer getAreaXb() {
        return areaXb;
    }

    public void setAreaXb(Integer areaXb) {
        this.areaXb = areaXb;
    }

    public Integer getAreaXn() {
        return areaXn;
    }

    public void setAreaXn(Integer areaXn) {
        this.areaXn = areaXn;
    }

    public Integer getAreaDb() {
        return areaDb;
    }

    public void setAreaDb(Integer areaDb) {
        this.areaDb = areaDb;
    }

    public Integer getAreaOther() {
        return areaOther;
    }

    public void setAreaOther(Integer areaOther) {
        this.areaOther = areaOther;
    }
}