package cn.edu.nsu.onlinejudge.entity;

import cn.edu.nsu.onlinejudge.common.Enum.LoginTicketStatusEnum;

import java.util.Date;

public class LoginTicket {
    // get & set & toString
    private int ticketId;
    private int userId;
    private String ticket;
    private LoginTicketStatusEnum status;
    private Date expired;

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public LoginTicketStatusEnum getStatus() {
        return status;
    }

    public void setStatus(LoginTicketStatusEnum status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "ticketId=" + ticketId +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
