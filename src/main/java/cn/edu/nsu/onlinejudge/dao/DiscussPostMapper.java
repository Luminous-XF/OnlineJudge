package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

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


    DiscussPost selectDiscussPostById(int postId);
}
