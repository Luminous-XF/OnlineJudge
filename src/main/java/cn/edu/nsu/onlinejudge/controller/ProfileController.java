package cn.edu.nsu.onlinejudge.controller;


import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.entity.Message;
import cn.edu.nsu.onlinejudge.entity.Page;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.DiscussPostService;
import cn.edu.nsu.onlinejudge.service.MessageService;
import cn.edu.nsu.onlinejudge.service.ProfileService;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import cn.edu.nsu.onlinejudge.util.TextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 个人信息页相关入口
 */
@Controller
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Value("${onlinejudge.path.uploadHeader}")
    private String uploadPathHeader;

    @Value("${onlinejudge.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    /**
     * 个其他用户人信息页-个人信息总览页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/preview", method = RequestMethod.GET)
    public String profilePreview(Model model) {
        User user = hostHolder.getUser();

        int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnReadCount", letterUnReadCount);
        model.addAttribute("owner", user);

        return "/site/profile";
    }

    /**
     * 个其他用户人信息页-个人信息总览页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/preview/{ownerId}", method = RequestMethod.GET)
    public String profilePreview(Model model, @PathVariable String ownerId) {
        User user = hostHolder.getUser();

        if (user.getUserId() == Integer.parseInt(ownerId)) {
            // 查询未读消息数量
            int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
            model.addAttribute("letterUnReadCount", letterUnReadCount);
            model.addAttribute("owner", user);
        } else {
            user = userService.findUserByUserId(Integer.parseInt(ownerId));
            int letterUnReadCount = 0;
            model.addAttribute("letterUnReadCount", letterUnReadCount);
            model.addAttribute("owner", user);
        }

        return "/site/profile";
    }


    /**
     * 个人信息页-设置页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/setting", method = RequestMethod.GET)
    public String profileSetting(Model model) {
        User user = hostHolder.getUser();
        // 查询未读消息数量
        int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnReadCount", letterUnReadCount);
        model.addAttribute("owner", user);


        return "/site/profile-setting";
    }


    /**
     * 个人信息页-个人提交页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/submissions", method = RequestMethod.GET)
    public String profileSubmissions(Model model) {
        User user = hostHolder.getUser();
        // 查询未读消息数量
        int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnReadCount", letterUnReadCount);
        model.addAttribute("owner", user);

        return "/site/profile-my-submissions";
    }


    /**
     * 个人信息页-个人提交页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/submissions/{ownerId}", method = RequestMethod.GET)
    public String profileSubmissions(Model model, @PathVariable String ownerId) {
        User user = hostHolder.getUser();
        // 查询未读消息数量
        int letterUnReadCount = 0;
        model.addAttribute("letterUnReadCount", letterUnReadCount);

        User owner = userService.findUserByUserId(Integer.parseInt(ownerId));
        model.addAttribute("owner", owner);


        return "/site/profile-my-submissions";
    }


    /**
     * 个人信息页-他人博客页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/blogs", method = RequestMethod.GET)
    public String profileBlogs(Model model, Page page) {
        User user = hostHolder.getUser();
        // 查询未读消息数量
        int letterUnReadCount = 0;
        model.addAttribute("letterUnReadCount", letterUnReadCount);

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/forum");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();

        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();

                User author = userService.findUserByUserId(post.getUserId());

                post.setContent(TextUtil.markDownToText(post.getContent()));
                map.put("post", post);
                map.put("user", author);

                discussPosts.add(map);
            }
        }

        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("owner", user);


        return "/site/profile-my-blogs";
    }


    /**
     * 个人信息页-个人博客页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/blogs/{ownerId}", method = RequestMethod.GET)
    public String profileBlogs(Model model, Page page, @PathVariable String ownerId) {
        User user = hostHolder.getUser();
        // 查询未读消息数量
        int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnReadCount", letterUnReadCount);

        page.setRows(discussPostService.findDiscussPostRows(Integer.parseInt(ownerId)));
        page.setPath("/forum");

        List<DiscussPost> list = discussPostService.findDiscussPosts(Integer.parseInt(ownerId), page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();

        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();

                User author = userService.findUserByUserId(post.getUserId());

                post.setContent(TextUtil.markDownToText(post.getContent()));
                map.put("post", post);
                map.put("user", author);

                discussPosts.add(map);
            }
        }

        model.addAttribute("discussPosts", discussPosts);
        User owner = userService.findUserByUserId(Integer.parseInt(ownerId));
        model.addAttribute("owner", owner);

        return "/site/profile-my-blogs";
    }

    @LoginRequired
    @RequestMapping(path = "/profile/message", method = RequestMethod.GET)
    public String profileMessage(Model model, Page page) {
        User user = hostHolder.getUser();

        // 分页信息
        page.setLimit(10);
        page.setPath("letter/list");
        page.setRows(messageService.findConversationCount(user.getUserId()));
        // 会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getUserId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unReadCount", messageService.findLetterUnreadCount(user.getUserId(), message.getConversationId()));
                int targetId = user.getUserId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserByUserId(targetId));
                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
        int letterUnReadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnReadCount", letterUnReadCount);
        model.addAttribute("owner", user);


        return "/site/profile-message";
    }


    /**
     * 上传头像
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload/header", method = RequestMethod.POST)
    public String updateHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "You have not selected a picture!");
            return "/site/profile-setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "The image format is incorrect!");
            return "/site/profile-setting";
        }

        // 生产随机文件名
        fileName = OnlineJudgeUtil.generateUUID() + suffix;
        // 确定文件存放位置
        File dest = new File(uploadPathHeader + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传头像失败: " + e.getMessage());
            throw new RuntimeException("上传头像失败,服务器发生异常!", e);
        }


        User user = hostHolder.getUser();

        // 删除老的头像文件
        String oldHeaderUrl = user.getHeaderUrl();
        String oldFileName = uploadPathHeader + "/" + oldHeaderUrl.substring(oldHeaderUrl.lastIndexOf("/") + 1);
        File file = new File(oldFileName);
        if (file.exists()) {
            if (file.delete()) {
                logger.info("原头像文件删除成功!");
            } else {
                logger.info("原头像文件删除失败,原图像文件可能不存在!");
            }
        }

        // 更新当前用户的头像的路径(Web访问路径)
        // http://localhost:8081/community/user/header/xxx.png
        String headerUrl = domain + contextPath + "/header/" + fileName;
        int res = profileService.updateHeader(user.getUserId(), headerUrl);
        if (res == 1) {
            logger.info("头像更新成功!");
        } else {
            logger.info("头像更新异常!");
        }

        return "redirect:/profile/setting";
    }


    /**
     * 访问头像资源
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPathHeader + "/" + fileName;
        // 解析文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fileInputStream = new FileInputStream(fileName);
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int index = 0;
            while ((index = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, index);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @LoginRequired
    @RequestMapping(path = "/change-profile", method = RequestMethod.POST)
    public String changeProfile(String nickname,
                                @RequestParam(value = "gender", defaultValue = "0") int gender, String brief) {

        System.out.println(nickname + " " + gender + " " + brief);
        profileService.changeProfile(nickname, gender, brief);

        return "redirect:/profile/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/change-password", method = RequestMethod.POST)
    public String changePassword(Model model, String oldPassword, String newPassword) {
        Map<String, Object> map = profileService.changePassword(oldPassword, newPassword);

        model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
        model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
        model.addAttribute("success", map.get("success"));

        return "/site/profile-setting";
    }
}
