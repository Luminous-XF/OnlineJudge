package cn.edu.nsu.onlinejudge;

import cn.edu.nsu.onlinejudge.dao.DiscussPostMapper;
import cn.edu.nsu.onlinejudge.dao.LoginTicketMapper;
import cn.edu.nsu.onlinejudge.dao.MessageMapper;
import cn.edu.nsu.onlinejudge.dao.UserMapper;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.entity.LoginTicket;
import cn.edu.nsu.onlinejudge.entity.Message;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.common.Enum.GenderEnum;
import cn.edu.nsu.onlinejudge.common.Enum.LoginTicketStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.UserActivationStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = OnlineJudgeApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectByUserId(1);
        System.out.println(user);

        user = userMapper.selectByUsername("19380120319");
        System.out.println(user);

        user = userMapper.selectByEmail("1094038421@qq.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        
        user.setUsername("19380120317");
        user.setNickname("何浪");
        user.setPassword("abc123");
        user.setSalt("abc");
        user.setEmail("123213@qq.com");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);

        System.out.println(rows);
        System.out.println(user);
        System.out.println(user.getUserId());
    }

    @Test
    public void testUpdate() {
        int rows = userMapper.updateNickname(3, "helang");
        System.out.println(rows);

        rows = userMapper.updateType(3, 1);
        System.out.println(rows);

        rows = userMapper.updateSubmit(3, 111);
        System.out.println(rows);

        rows = userMapper.updateSolved(3, 11);
        System.out.println(rows);

        rows = userMapper.updateDefunct(3, 1);
        System.out.println(rows);

        rows = userMapper.updateRating(3, 33);
        System.out.println(rows);

        rows = userMapper.updateRanks(3, 44);
        System.out.println(rows);

        rows = userMapper.updateSchool(3, "CNU");
        System.out.println(rows);

        rows = userMapper.updateStatus(3, UserActivationStatusEnum.FAIL);
        System.out.println(rows);

        rows = userMapper.updateHeaderUrl(3, "aa/aa/aa");
        System.out.println(rows);

        rows = userMapper.updatePassword(3, "aaaa");
        System.out.println(rows);

        rows = userMapper.updateGender(3, GenderEnum.FEMALE);
        System.out.println(rows);

        rows = userMapper.updateBrief(3, "Why sleep long, when forever rest after death！");
        System.out.println(rows);
    }


    @Test
    public void testSelectDiscussPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);

        for (DiscussPost discussPost : list) {
            System.out.println(discussPost);
        }

        int rows = discussPostMapper.selectDiscussPostRows(1);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();

        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(LoginTicketStatusEnum.FAIL);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000  * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");

        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", LoginTicketStatusEnum.FAIL);

        loginTicket = loginTicketMapper.selectByTicket("abc");

        System.out.println(loginTicket);
    }

    @Test
    public void testMessageMapper() {
        List<Message> list = messageMapper.selectConversations(1, 0, 10);

        for (Message message : list) {
            System.out.println(message);
        }

//        int count = messageMapper.selectConversationCount(1);
//        System.out.println(count);
//
//        list = messageMapper.selectLetters("1_2", 0, 10);
//        for (Message message : list) {
//            System.out.println(message);
//        }
//
//        count = messageMapper.selectLetterCount("1_2");
//        System.out.println(count);
//
//        count = messageMapper.selectLetterUnreadCount(2, null);
//        System.out.println(count);
    }
}
