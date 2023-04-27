package cn.edu.nsu.onlinejudge.service;

import cn.edu.nsu.onlinejudge.common.Enum.*;
import cn.edu.nsu.onlinejudge.dao.LoginTicketMapper;
import cn.edu.nsu.onlinejudge.dao.UserMapper;
import cn.edu.nsu.onlinejudge.entity.LoginTicket;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.common.MailClient;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${onlinejudge.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserByUserId(int userId) {
        return userMapper.selectByUserId(userId);
    }


    public User findUserByEmail(String email) { return userMapper.selectByEmail(email); }


    public User findUserByUsername(String username) {return userMapper.selectByUsername(username); }


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 对空值进行处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "Username cannot be empty!");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "Password cannot be empty!");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "Email cannot be empty!");
            return map;
        }


        // 验证账号是否已经被注册
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "This username already exists!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "This email address has been registered!");
            return map;
        }


        // 注册用户
        user.setSalt(OnlineJudgeUtil.generateUUID().substring(0, 6));
        user.setPassword(OnlineJudgeUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(UserTypeEnum.COMMON);
        user.setStatus(UserActivationStatusEnum.FAIL);
        user.setActivationCode(OnlineJudgeUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        user.setGender(GenderEnum.UNCERTAIN);

        // 将用户信息加入数据库
        userMapper. insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());

        // 激活链接
        // http://localhost:8080/onlinejudge/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getUserId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "Account Activation", content);

        return map;
    }

    /**
     * 激活账号
     *
     * @param userId
     * @param code
     * @return
     */
    public UserActivationResultEnum activation(int userId, String code) {
        User user = userMapper.selectByUserId(userId);

        // 校验账号激活状态
        if (user.getStatus() == UserActivationStatusEnum.SUCCESS) {
            return UserActivationResultEnum.REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, UserActivationStatusEnum.SUCCESS);
            return UserActivationResultEnum.SUCCESS;
        } else {
            return UserActivationResultEnum.FAIL;
        }
    }


    /**
     * 用户登录
     *
     * @param username       用户名
     * @param password       密码
     * @param expiredSeconds 登录凭证时长
     * @return
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "Username can not be empty!");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "Password can not be empty!");
            return map;
        }


        // 验证账号是否注册
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            map.put("usernameMsg", "This username does not exist!");
            return map;
        }

        // 验证账号激活状态
        if (user.getStatus() == UserActivationStatusEnum.FAIL) {
            map.put("usernameMsg", "This username does not activation!");
            return map;
        }

        // 验证密码(比较加密后的密码)
        password = OnlineJudgeUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "Wrong password!");
            return map;
        }

        // 生产登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getUserId());
        loginTicket.setTicket(OnlineJudgeUtil.generateUUID());
        loginTicket.setStatus(LoginTicketStatusEnum.SUCCESS);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    /**
     * 退出操作
     * 将登录凭证状态改为失效
     * @param ticket
     */
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, LoginTicketStatusEnum.FAIL);
    }


    /**
     * 查询登录凭证
     * @param ticket
     * @return 返回登录凭证实体
     */
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
}
