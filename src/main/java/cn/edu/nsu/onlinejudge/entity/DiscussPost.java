package cn.edu.nsu.onlinejudge.entity;

import cn.edu.nsu.onlinejudge.common.Enum.PostStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.PostTypeEnum;

import java.util.Date;

public class DiscussPost {
    private int postId;
    private int userId;
    private String title;
    private String content;
    private PostTypeEnum type;
    private Date createTime;
    private int commentCount;
    private double score;
    private PostStatusEnum status;
    private int likes;
    private String firstImageUrl;

    private int topWeight;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostTypeEnum getType() {
        return type;
    }

    public void setType(PostTypeEnum type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public PostStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PostStatusEnum status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public int getTopWeight() {
        return topWeight;
    }

    public void setTopWeight(int topWeight) {
        this.topWeight = topWeight;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                ", status=" + status +
                ", likes=" + likes +
                ", firstImageUrl='" + firstImageUrl + '\'' +
                ", topWeight=" + topWeight +
                '}';
    }
}
