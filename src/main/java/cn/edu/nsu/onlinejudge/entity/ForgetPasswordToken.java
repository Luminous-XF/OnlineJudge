package cn.edu.nsu.onlinejudge.entity;

import java.util.Date;

public class ForgetPasswordToken {

    private int userId;

    private String token;

    private Date expired;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "ForgetPassword{" +
                "userId=" + userId +
                ", code=" + token +
                ", expired=" + expired +
                '}';
    }
}
