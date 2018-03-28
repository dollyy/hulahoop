package com.yc.hulahoop.vo;

import java.util.Date;

public class CommentVo {
    private int id;
    private int StrategyId;
    private String level;
    private String avatar;
    private String parent;
    private int responseId;
    private String responseName;
    private int requestId;
    private String requestName;
    private String content;
    private String createTime;
    private int forNum;
    private int AgainstNum;
    private int commentCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrategyId() {
        return StrategyId;
    }

    public void setStrategyId(int strategyId) {
        StrategyId = strategyId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
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

    public int getForNum() {
        return forNum;
    }

    public void setForNum(int forNum) {
        this.forNum = forNum;
    }

    public int getAgainstNum() {
        return AgainstNum;
    }

    public void setAgainstNum(int againstNum) {
        AgainstNum = againstNum;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
