package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.common.Constant.EventTopicConstant;
import cn.edu.nsu.onlinejudge.common.Enum.CommentStatusEnum;
import cn.edu.nsu.onlinejudge.common.Enum.EntityTypeEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.entity.Comment;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.entity.Event;
import cn.edu.nsu.onlinejudge.event.EventProducer;
import cn.edu.nsu.onlinejudge.service.CommentService;
import cn.edu.nsu.onlinejudge.service.DiscussPostService;
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
public class CommentController implements EventTopicConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;


    @LoginRequired
    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(@PathVariable int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getUserId());
        comment.setStatus(CommentStatusEnum.COMMON);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getUserId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);

        if (comment.getEntityType() == EntityTypeEnum.POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == EntityTypeEnum.COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

//        eventProducer.fireEvent(event);

        return OnlineJudgeUtil.getJSONString(0, "Comment published successfully!");
    }
}
