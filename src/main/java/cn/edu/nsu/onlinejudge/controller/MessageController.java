package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.common.Enum.MessageStatusEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.common.vo.LettersVO;
import cn.edu.nsu.onlinejudge.entity.Message;
import cn.edu.nsu.onlinejudge.entity.Page;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.MessageService;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    // 私信列表
    @LoginRequired
    @RequestMapping(path = "/message/letter/list", method = RequestMethod.GET)
    public String getLettereList(Model model, Page page) {
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

        return "/site/profile-message";
    }


    @RequestMapping(path = "/message/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Model model) {
        // 分页信息
        Page page = new Page();
        page.setLimit(100);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserByUserId(message.getFromId()));
                letters.add(map);
            }
        }

        model.addAttribute("letters", letters);
        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));

        // 将消息状态更改为已读
        List<Integer> messageIdList = getMessageIdList(letterList);
        if (!messageIdList.isEmpty()) {
            messageService.readMessage(messageIdList);
        }

        return "/site/message-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] id = conversationId.split("_");

        int id1 = Integer.valueOf(id[0]);
        int id2 = Integer.valueOf(id[1]);

        if (hostHolder.getUser().getUserId() == id1) {
            return userService.findUserByUserId(id2);
        } else {
            return userService.findUserByUserId(id1);
        }
    }


    @RequestMapping(path = "/message/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(int targetUserId, String content) {
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getUserId());
        message.setToId(targetUserId);
        message.setConversationId(getConversationId(message));
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setStatus(MessageStatusEnum.UNREAD);
        messageService.addMessage(message);

        return OnlineJudgeUtil.getJSONString(0, "Successfully!");
    }

    private String getConversationId(Message message) {
        int from = message.getFromId();
        int to = message.getToId();

        return Math.min(from, to) + "_" + Math.max(from, to);
    }

    private List<Integer> getMessageIdList(List<Message> letterList) {
        List<Integer> list = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (message.getStatus() == MessageStatusEnum.UNREAD &&
                        hostHolder.getUser().getUserId() == message.getToId()) {
                    list.add(message.getMessageId());
                }
            }
        }

        return list;
    }
}
