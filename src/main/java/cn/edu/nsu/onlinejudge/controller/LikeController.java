package cn.edu.nsu.onlinejudge.controller;


import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.common.Constant.EventTopicConstant;
import cn.edu.nsu.onlinejudge.common.Enum.EntityTypeEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.entity.Event;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.event.EventProducer;
import cn.edu.nsu.onlinejudge.service.LikeService;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements EventTopicConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;


    @LoginRequired
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId) {
        User user = hostHolder.getUser();

        // 点赞
        likeService.like(user.getUserId(), EntityTypeEnum.fromKey(entityType), entityId);

        // 数量
        long likeCount = likeService.findEntityLikeCount(EntityTypeEnum.fromKey(entityType), entityId);

        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getUserId(), EntityTypeEnum.fromKey(entityType), entityId);

        // 返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getUserId())
                    .setEntityType(EntityTypeEnum.fromKey(entityType))
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", entityId);

//            eventProducer.fireEvent(event);
        }

        return OnlineJudgeUtil.getJSONString(0, "Successfully!", map);
    }
}
