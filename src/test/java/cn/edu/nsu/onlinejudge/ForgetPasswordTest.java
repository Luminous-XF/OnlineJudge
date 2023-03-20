package cn.edu.nsu.onlinejudge;


import cn.edu.nsu.onlinejudge.dao.ForgetPasswordTokenMapper;
import cn.edu.nsu.onlinejudge.entity.ForgetPasswordToken;
import cn.edu.nsu.onlinejudge.service.ForgetPasswordService;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = OnlineJudgeApplication.class)
public class ForgetPasswordTest {

    @Autowired
    private ForgetPasswordTokenMapper forgetPasswordTokenMapper;

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @Test
    public void testForgetPasswordDao() {
        ForgetPasswordToken forgetPassword = forgetPasswordTokenMapper.selectForgetPasswordToken(1);

        System.out.println(forgetPassword);

        forgetPasswordTokenMapper.insertForgetPasswordToken(1, OnlineJudgeUtil.generateUUID(), new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        forgetPassword = forgetPasswordTokenMapper.selectForgetPasswordToken(1);
        System.out.println(forgetPassword);

        forgetPasswordTokenMapper.updateForgetPasswordToken(1, OnlineJudgeUtil.generateUUID(), new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        forgetPassword = forgetPasswordTokenMapper.selectForgetPasswordToken(1);
        System.out.println(forgetPassword);
    }

    @Test
    public void testSendCode() {
        forgetPasswordService.forgetPassword("1094038421@qq.com");
    }
}
