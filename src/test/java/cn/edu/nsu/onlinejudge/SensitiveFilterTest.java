package cn.edu.nsu.onlinejudge;


import cn.edu.nsu.onlinejudge.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = OnlineJudgeApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Test
    public void testSensitiveFilter() {
        String text = "你是个傻逼,我操你妈的!";

        text = sensitiveFilter.filter(text);

        System.out.println(text);
    }
}
