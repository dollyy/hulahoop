package com.yc.hulahoop.pojo;

import java.util.Date;

public class FeedbackInfo {
    private Integer id;

    private Integer userId;

    private String content;

    private String level;

    private Integer parent;

    private Integer sequence;

    private Date createTime;

    public FeedbackInfo(Integer id, Integer userId, String content, String level, Integer parent, Integer sequence, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.level = level;
        this.parent = parent;
        this.sequence = sequence;
        this.createTime = createTime;
    }

    public FeedbackInfo() {
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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
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
}