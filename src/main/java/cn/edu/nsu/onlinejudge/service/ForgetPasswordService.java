package cn.edu.nsu.onlinejudge.service;


import cn.edu.nsu.onlinejudge.dao.ForgetPasswordTokenMapper;
import cn.edu.nsu.onlinejudge.dao.UserMapper;
import cn.edu.nsu.onlinejudge.entity.ForgetPasswordToken;
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

@Service
public class ForgetPasswordService {

    @Autowired
    private ForgetPasswordTokenMapper forgetPasswordTokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${onlinejudge.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;



    public Map<String, Object> resetPassword(String username, String newPassword, String token) {
        Map<String, Object> map = new HashMap<>();

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            System.out.println(user);
            System.out.println(username);
            System.out.println(userMapper.selectByUsername(username));
        }
        ForgetPasswordToken forgetPasswordToken = forgetPasswordTokenMapper.selectForgetPasswordToken(user.getUserId());

        if (forgetPasswordToken == null || !token.equals(forgetPasswordToken.getToken())) {
            map.put("tokenMsg", "Invalid token, if you want to reset the password, please click the link below to get " +
                    "the token!");
            return map;
        }

        if (new Date().after(forgetPasswordToken.getExpired())) {
            map.put("tokenMsg", "Your token has expired, if you want to continue to reset your password, please click " +
                    "the link below to get a new token!");
            return map;
        }

        newPassword = OnlineJudgeUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(user.getUserId(), newPassword);
        forgetPasswordTokenMapper.updateForgetPasswordToken(user.getUserId(), forgetPasswordToken.getToken(), new Date(System.currentTimeMillis() - 1000L * 60 * 30));

        return map;
    }


    /**
     * 向用户邮箱发送验证码
     * @return
     */
    public Map<String, Object> forgetPassword(String email) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "E-mail cannot be empty!");
            return map;
        }

        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "Unable to find matching email address!");
            return map;
        }

        // token
        String token = OnlineJudgeUtil.generateUUID();
        Date expired = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        ForgetPasswordToken forgetPassword = forgetPasswordTokenMapper.selectForgetPasswordToken(user.getUserId());
        if (forgetPassword == null) {
            forgetPasswordTokenMapper.insertForgetPasswordToken(user.getUserId(), token, expired);
        } else {
            forgetPasswordTokenMapper.updateForgetPasswordToken(user.getUserId(), token, expired);
        }

        // 发送验证码邮件
        Context context = new Context();
        context.setVariable("nickname", user.getNickname());
        context.setVariable("token", token);
        //localhost:8080/reset-password/101/token
        String url = domain + contextPath + "/reset-password/" + user.getUsername() + "/" + token;
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/forget-password", context);
        mailClient.sendMail(user.getEmail(), "Forget Password", content);

        return map;
    }
}
