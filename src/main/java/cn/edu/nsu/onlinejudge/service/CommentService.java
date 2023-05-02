package cn.edu.nsu.onlinejudge.service;

import cn.edu.nsu.onlinejudge.common.Enum.CommentTypeEnum;
import cn.edu.nsu.onlinejudge.common.SensitiveFilter;
import cn.edu.nsu.onlinejudge.dao.CommentMapper;
import cn.edu.nsu.onlinejudge.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(CommentTypeEnum entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }


    public int findCommentCountByEntity(CommentTypeEnum entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);


        // 更新评论数量
        if (comment.getEntityType() == CommentTypeEnum.POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            System.out.println(count);
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }
}
