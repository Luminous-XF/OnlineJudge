package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.common.Enum.EntityTypeEnum;
import cn.edu.nsu.onlinejudge.common.Enum.PostStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.PostTypeEnum;
import cn.edu.nsu.onlinejudge.entity.Comment;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.entity.Page;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.CommentService;
import cn.edu.nsu.onlinejudge.service.DiscussPostService;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.FileUtil;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import cn.edu.nsu.onlinejudge.util.TextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 博客论坛相关入口
 */
@Controller
public class ForumController {

    @Value("${onlinejudge.path.uploadFirstImage}")
    private String uploadPathFirstImage;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;


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

                post.setContent(TextUtil.markDownToText(post.getContent()));
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

        if (StringUtils.isBlank(title)) {
            return OnlineJudgeUtil.getJSONString(1, "Title can not be empty!");
        } else if (title.length() > 100) {
            return OnlineJudgeUtil.getJSONString(1, "Title character exceeds limit!");
        } else if (StringUtils.isBlank(content)) {
            return OnlineJudgeUtil.getJSONString(1, "Content can not be empty!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getUserId());
        post.setTitle(title);
        post.setType(PostTypeEnum.COMMON);
        post.setStatus(PostStatusEnum.COMMON);
        post.setContent(content);
        post.setCreateTime(new Date());

        Map<String, Object> res = discussPostService.addDiscussPost(post, firstImage);
        if (res.containsKey("error")) {
            return OnlineJudgeUtil.getJSONString(1, (String) res.get("error"));
        }

        // 报错的情况将来统一处理
        return OnlineJudgeUtil.getJSONString(0, "You can check out just published blogs in My Blogs!");
    }


    @LoginRequired
    @RequestMapping(path = "/edit-post/{postId}", method = RequestMethod.GET)
    public String editDiscussPost(@PathVariable int postId, Model model) {
        User user = hostHolder.getUser();
        if (user == null) {
            // TODO
            return "/error/403";
        }

        DiscussPost oldPost = discussPostService.findDiscussPostById(postId);
        if (oldPost == null) {
            // TODO
//            return OnlineJudgeUtil.getJSONString(404, "Blog does not exist!");
            return "/error/404";
        }

        model.addAttribute("post", oldPost);
        model.addAttribute("editFlag", true);
        return "/site/blog-input";
    }

    @LoginRequired
    @RequestMapping(path = "/edit-post/{postId}", method = RequestMethod.POST)
    @ResponseBody
    public String editDiscussPost(@PathVariable int postId, String title, String content, MultipartFile firstImage, boolean firstImageChangeFlag) {
        User user = hostHolder.getUser();
        if (user == null) {
            return OnlineJudgeUtil.getJSONString(403, "You are not logged in!");
        }

        DiscussPost oldPost = discussPostService.findDiscussPostById(postId);
        if (oldPost == null) {
            return OnlineJudgeUtil.getJSONString(404, "Blog does not exist!");
        }

        if (StringUtils.isBlank(title)) {
            return OnlineJudgeUtil.getJSONString(1, "Title can not be empty!");
        } else if (title.length() > 100) {
            return OnlineJudgeUtil.getJSONString(1, "Title character exceeds limit!");
        } else if (StringUtils.isBlank(content)) {
            return OnlineJudgeUtil.getJSONString(1, "Content can not be empty!");
        }

        DiscussPost post = new DiscussPost();
        post.setPostId(postId);
        post.setUserId(user.getUserId());
        post.setTitle(title);
        post.setContent(content);

        Map<String, Object> res = discussPostService.editDiscussPost(oldPost, post, firstImage, firstImageChangeFlag);
        if (res.containsKey("error")) {
            return OnlineJudgeUtil.getJSONString(1, (String) res.get("error"));
        }

        // 报错的情况将来统一处理
        return OnlineJudgeUtil.getJSONString(0, "You can check out just published blogs in My Blogs!");
    }


    @RequestMapping(path = "/blog/detail/{postId}", method = RequestMethod.GET)
    public String getDiscussPostDetail(@PathVariable int postId, Model model, Page page) {
        // 查询帖子
        DiscussPost blog = discussPostService.findDiscussPostById(postId);
        model.addAttribute("post", blog);

        // 查询作者
        User user = userService.findUserByUserId(blog.getUserId());
        model.addAttribute("author", user);

        // 评论分页信息
        page.setLimit(100);
        page.setPath("/blog/detail/" + postId);
        page.setRows(blog.getCommentCount());

        // 评论: 博客评论
        // 回复: 评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                EntityTypeEnum.POST, blog.getPostId(), page.getOffset(), page.getLimit());
        // 评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 评论作者
                commentVo.put("user", userService.findUserByUserId(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        EntityTypeEnum.COMMENT, comment.getCommentId(), 0, Integer.MAX_VALUE);
                // 回复VO列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserByUserId(reply.getUserId()));

                        User target = reply.getTargetId() == 0 ? null : userService.findUserByUserId(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // int replyCount = commentService.findCommentCountByEntity(CommentTypeEnum.COMMENT, comment.getCommentId());

                commentVo.put("replyCount", replyVoList.size());
                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);

        return "/site/blog";
    }


    @RequestMapping(path = "/first-image/{fileName}", method = RequestMethod.GET)
    public void getFirstImage(@PathVariable String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPathFirstImage + "/" + fileName;
        // 解析文件后缀
        String suffix = FileUtil.getFileSuffix(fileName);
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
}
