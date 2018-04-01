package com.yc.hulahoop.pojo;

import java.util.Date;

public class DwrRecord {
    private Integer id;

    private Integer responseId;

    private Integer requestId;

    private Integer commentSequence;

    private String content;

    private Date createTime;

    private Integer status;

    public DwrRecord(Integer id, Integer responseId, Integer requestId, Integer commentSequence, String content, Date createTime, Integer status) {
        this.id = id;
        this.responseId = responseId;
        this.requestId = requestId;
        this.commentSequence = commentSequence;
        this.content = content;
        this.createTime = createTime;
        this.status = status;
    }

    public DwrRecord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResponseId() {
        return responseId;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getCommentSequence() {
        return commentSequence;
    }

    public void setCommentSequence(Integer commentSequence) {
        this.commentSequence = commentSequence;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}