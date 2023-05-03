package cn.edu.nsu.onlinejudge.service;

import cn.edu.nsu.onlinejudge.common.Enum.EntityTypeEnum;
import cn.edu.nsu.onlinejudge.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;


    // 点赞
    public void like(int userId, EntityTypeEnum entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType.getKey(), entityId);
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    // 查询实体点赞数量
    public long findEntityLikeCount(EntityTypeEnum entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType.getKey(), entityId);

        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    // 查询用户对某实体是否点过赞
    public int findEntityLikeStatus(int userId, EntityTypeEnum entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType.getKey(), entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
