package cn.edu.nsu.onlinejudge.service;

import cn.edu.nsu.onlinejudge.dao.DiscussPostMapper;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
//import cn.edu.nsu.onlinejudge.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

//    @Autowired
//    private SensitiveFilter sensitiveFilter;


    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 查询该用户发帖数量
     * @param userId
     * @return
     */
    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }


    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义 HTML 标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

//        // 过滤敏感词
//        post.setTitle(sensitiveFilter.filter(post.getTitle()));
//        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }
}
