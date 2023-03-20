package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.entity.Page;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.DiscussPostService;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.HostHolder;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 博客论坛相关入口
 */
@Controller
public class ForumController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/forum", method = RequestMethod.GET)
    public String getForumPage(Model model, Page page) {
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/forum");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();

        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();

                User user = userService.findUserByUserId(post.getUserId());

                map.put("post", post);
                map.put("user", user);

                discussPosts.add(map);
            }
        }

        model.addAttribute("discussPosts", discussPosts);

        return "/site/forum";
    }


    @LoginRequired
    @RequestMapping(path = ("/create-post"), method = RequestMethod.GET)
    public String getInputDiscussPostPage() {

        return "/site/blog-input";
    }


    @LoginRequired
    @RequestMapping(path = "/add-post", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content, MultipartFile firstImage) {
        User user = hostHolder.getUser();
        if (user == null) {
            return OnlineJudgeUtil.getJSONString(403, "You are not logged in!");
        }

        System.out.println(firstImage);

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getUserId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());

        discussPostService.addDiscussPost(post);

        // 报错的情况将来统一处理
        return OnlineJudgeUtil.getJSONString(0, "Published successfully!");
    }
}
