package com.yc.hulahoop.vo;

public class DwrRecordVo {

    private int dwrId;
    private int commentSequence;
    private String responseName;
    private String content;
    private String createTime;
    private int status;

    public int getDwrId() {
        return dwrId;
    }

    public void setDwrId(int dwrId) {
        this.dwrId = dwrId;
    }

    public int getCommentSequence() {
        return commentSequence;
    }

    public void setCommentSequence(int commentSequence) {
        this.commentSequence = commentSequence;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
