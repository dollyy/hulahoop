package com.yc.hulahoop.pojo;

import java.util.Date;

public class Strategy {
    private Integer id;

    private Integer userId;

    private String name;

    private Integer cityId;

    private String duration;

    private String content;

    private String mainImg;

    private Integer forNum;

    private Integer collectNum;

    private Date createTime;

    private Date updateTime;

    public Strategy(Integer id, Integer userId, String name, Integer cityId, String duration, String content, String mainImg, Integer forNum, Integer collectNum, Date createTime, Date updateTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.cityId = cityId;
        this.duration = duration;
        this.content = content;
        this.mainImg = mainImg;
        this.forNum = forNum;
        this.collectNum = collectNum;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Strategy() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg == null ? null : mainImg.trim();
    }

    public Integer getForNum() {
        return forNum;
    }

    public void setForNum(Integer forNum) {
        this.forNum = forNum;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
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
}