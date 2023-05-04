package cn.edu.nsu.onlinejudge.event;


import cn.edu.nsu.onlinejudge.common.Constant.EventTopicConstant;
import cn.edu.nsu.onlinejudge.common.Constant.SystemUserConstant;
import cn.edu.nsu.onlinejudge.common.Enum.MessageStatusEnum;
import cn.edu.nsu.onlinejudge.entity.Event;
import cn.edu.nsu.onlinejudge.entity.Message;
import cn.edu.nsu.onlinejudge.service.MessageService;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements EventTopicConstant, SystemUserConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;


    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityId());
        message.setConversationId(event.getTopic());
        message.setStatus(MessageStatusEnum.MESSAGE);
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            content.putAll(event.getData());
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
