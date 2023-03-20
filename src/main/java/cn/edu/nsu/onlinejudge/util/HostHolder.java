package cn.edu.nsu.onlinejudge.util;


import cn.edu.nsu.onlinejudge.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息, 用于代替 session 对象
 */
@Component
public class HostHolder {

    private final ThreadLocal<User> users = new ThreadLocal<>();


    public void setUser(User user) {
        users.set(user);
    }


    public User getUser() {
        return users.get();
    }


    public void clear() {
        users.remove();
    }
}
