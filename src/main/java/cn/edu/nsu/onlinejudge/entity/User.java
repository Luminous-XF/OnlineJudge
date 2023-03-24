package cn.edu.nsu.onlinejudge.entity;

import cn.edu.nsu.onlinejudge.common.Enum.GenderEnum;
import cn.edu.nsu.onlinejudge.common.Enum.UserActivationStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.UserDefunctStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.UserTypeEnum;

import java.util.Date;


public class User {

    private int userId;
    private String username;
    private String nickname;
    private String password;
    private String salt;
    private String email;
    private UserTypeEnum type;
    private UserActivationStatusEnum status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
    private int submit;
    private int solved;
    private UserDefunctStatusEnum defunct;
    private int rating;
    private int ranks;
    private String school;
    private GenderEnum gender;
    private String brief;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }

    public UserActivationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserActivationStatusEnum status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public UserDefunctStatusEnum getDefunct() {
        return defunct;
    }

    public void setDefunct(UserDefunctStatusEnum defunct) {
        this.defunct = defunct;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRanks() {
        return ranks;
    }

    public void setRanks(int ranks) {
        this.ranks = ranks;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", activationCode='" + activationCode + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                ", createTime=" + createTime +
                ", submit=" + submit +
                ", solved=" + solved +
                ", defunct=" + defunct +
                ", rating=" + rating +
                ", ranks=" + ranks +
                ", school='" + school + '\'' +
                ", gender=" + gender +
                ", brief='" + brief + '\'' +
                '}';
    }
}
