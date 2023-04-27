package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.common.Enum.CommentTypeEnum;
import cn.edu.nsu.onlinejudge.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentByEntity(CommentTypeEnum entityType, int entityId, int offset, int limit);

    int selectCountByEntity(CommentTypeEnum entityType, int entityId);

    int insertComment(Comment comment);
}
