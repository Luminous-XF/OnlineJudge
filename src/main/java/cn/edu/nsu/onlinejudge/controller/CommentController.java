package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.common.Enum.CommentStatusEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.entity.Comment;
import cn.edu.nsu.onlinejudge.service.CommentService;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;


    @LoginRequired
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(@PathVariable int discussPostId, Comment comment) {
        System.out.println(comment);
        comment.setUserId(hostHolder.getUser().getUserId());
        comment.setStatus(CommentStatusEnum.COMMON);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        return OnlineJudgeUtil.getJSONString(0, "Comment published successfully!");
    }
}
