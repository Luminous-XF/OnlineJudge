package cn.edu.nsu.onlinejudge;

import cn.edu.nsu.onlinejudge.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

@SpringBootTest
@ContextConfiguration(classes = OnlineJudgeApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;


    @Test
    public void testTextMail() {
        mailClient.sendMail("1094038421@qq.com", "Test", "Hello World!");
    }


    @Test
    public void textHtmlMail() {
        Context context = new Context();
        context.setVariable("kaptcha", "345245");

        String content =  templateEngine.process("demo/view", context);
        System.out.println(content);

        mailClient.sendMail("1094038421@qq.com", "Test HTML", content);
    }
}
