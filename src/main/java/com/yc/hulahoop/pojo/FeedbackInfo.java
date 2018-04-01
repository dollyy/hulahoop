package com.yc.hulahoop.pojo;

import java.util.Date;

public class FeedbackInfo {
    private Integer id;

    private Integer sendId;

    private Integer receiveId;

    private String content;

    private String level;

    private Integer parent;

    private Integer sequence;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    public FeedbackInfo(Integer id, Integer sendId, Integer receiveId, String content, String level, Integer parent, Integer sequence, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.level = level;
        this.parent = parent;
        this.sequence = sequence;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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