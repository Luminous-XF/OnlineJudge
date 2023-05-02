package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.common.Enum.MessageStatusEnum;
import cn.edu.nsu.onlinejudge.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 查询会话列表,针对每个会话只返回一条最新的消息
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的消息列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的消息数量
    int selectLetterCount(String conversationId);

    // 查询未读消息的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息状态
    int updateMessageStatus(List<Integer> messageIdList, MessageStatusEnum status);
}
