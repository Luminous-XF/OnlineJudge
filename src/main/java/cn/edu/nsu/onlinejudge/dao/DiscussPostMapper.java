package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 查询帖子
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);


    /**
     * @Param用于给参数取别名
     * 如果只有一个参数,并且在<if>里使用,则必须取别名
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);


    /**
     * 插入一个帖子
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);


    /**
     * 通过 postId 查找帖子
     * @param postId
     * @return
     */
    DiscussPost selectDiscussPostById(int postId);

    /**
     * 更新帖子评论数
     * @param postId
     * @param commentCount
     * @return
     */
    int updateCommentCount(int postId, int commentCount);

    int updateDiscussPost(DiscussPost discussPost);
}
