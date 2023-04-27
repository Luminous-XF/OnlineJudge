package cn.edu.nsu.onlinejudge.entity;

import cn.edu.nsu.onlinejudge.common.Enum.CommentStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.CommentTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

    private int commentId;

    private int userId;

    private CommentTypeEnum entityType;

    private int entityId;

    private int targetId;

    private String content;

    private CommentStatusEnum status;

    private Date createTime;


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CommentTypeEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(CommentTypeEnum entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CommentStatusEnum status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
