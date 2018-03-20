package com.yc.hulahoop.pojo;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userId;

    private Integer strategyId;

    private String content;

    private String level;

    private String parent;

    private Integer sequence;

    private Date createTime;

    private Integer forNum;

    private Integer againstNum;

    public Comment(Integer id, Integer userId, Integer strategyId, String content, String level, String parent, Integer sequence, Date createTime, Integer forNum, Integer againstNum) {
        this.id = id;
        this.userId = userId;
        this.strategyId = strategyId;
        this.content = content;
        this.level = level;
        this.parent = parent;
        this.sequence = sequence;
        this.createTime = createTime;
        this.forNum = forNum;
        this.againstNum = againstNum;
    }

    public Comment() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent == null ? null : parent.trim();
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getForNum() {
        return forNum;
    }

    public void setForNum(Integer forNum) {
        this.forNum = forNum;
    }

    public Integer getAgainstNum() {
        return againstNum;
    }

    public void setAgainstNum(Integer againstNum) {
        this.againstNum = againstNum;
    }
}