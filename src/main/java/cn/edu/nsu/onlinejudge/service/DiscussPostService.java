package cn.edu.nsu.onlinejudge.service;

import cn.edu.nsu.onlinejudge.dao.DiscussPostMapper;
import cn.edu.nsu.onlinejudge.entity.DiscussPost;
import cn.edu.nsu.onlinejudge.util.FileUtil;
import cn.edu.nsu.onlinejudge.common.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscussPostService {

    @Value("${onlinejudge.path.uploadFirstImage}")
    private String uploadPathFirstImage;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private FileUtil fileUtil;

    /**
     * 查询所有发帖列表
     * @param userId 0, 没有个这个用户ID所以查询不会被过滤
     * @param offset
     * @param limit
     * @return
     */
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


    public Map<String, Object> addDiscussPost(DiscussPost post, MultipartFile firstImage) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 首图上传失败
        Map<String, Object> res;
        if (firstImage != null) {
            res = fileUtil.uploadImage(uploadPathFirstImage, firstImage);
            if (res.containsKey("error")) {
                return res;
            }

            // 设置首图
            post.setFirstImageUrl((String) res.get("firstImageUrl"));
        } else {
            res = new HashMap<>();
        }


        // 转义 HTML 标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        int row = discussPostMapper.insertDiscussPost(post);
        if (row == 0) {
            res.put("error", "Blog upload failed, please try again later!");
        }

        return res;
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Map<String, Object> editDiscussPost(DiscussPost oldPost, DiscussPost post, MultipartFile firstImage, boolean firstImageChangeFlag) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 首图上传失败
        Map<String, Object> res;
        if (firstImageChangeFlag && firstImage != null) {
            res = fileUtil.uploadImage(uploadPathFirstImage, firstImage);
            if (res.containsKey("error")) {
                return res;
            }

            // 设置首图
            post.setFirstImageUrl((String) res.get("firstImageUrl"));
            // 删除原图
            fileUtil.deleteOldFirstImage(uploadPathFirstImage, oldPost.getFirstImageUrl());
        } else {
            res = new HashMap<>();
            post.setFirstImageUrl(oldPost.getFirstImageUrl());
        }

        // 转义 HTML 标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        int row = discussPostMapper.updateDiscussPost(post);
        if (row == 0) {
            res.put("error", "Blog update failed, please try again later!");
        }

        return res;
    }

    public DiscussPost findDiscussPostById(int postId) {
        return discussPostMapper.selectDiscussPostById(postId);
    }


    public int updateCommentCount(int postId, int commentCount) {
        return discussPostMapper.updateCommentCount(postId, commentCount);
    }
}
