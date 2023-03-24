package cn.edu.nsu.onlinejudge.service;


import cn.edu.nsu.onlinejudge.dao.UserMapper;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.common.Enum.GenderEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 个人信息
 */
@Service
public class ProfileService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHolder hostHolder;


    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeaderUrl(userId, headerUrl);
    }



    public void changeProfile(String nickname, int gender, String brief) {
        User user = hostHolder.getUser();

        if (!user.getNickname().equals(nickname)) {
            userMapper.updateNickname(user.getUserId(), nickname);
        }

        if (user.getGender() != GenderEnum.fromKey(gender)) {
            userMapper.updateGender(user.getUserId(), GenderEnum.fromKey(gender));
        }


        if (user.getBrief() == null) {
            userMapper.updateBrief(user.getUserId(), brief);
        } else {
            if (!user.getBrief().equals(brief)) {
                userMapper.updateBrief(user.getUserId(), brief);
            }
        }
    }


    public Map<String, Object> changePassword(String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        User user = hostHolder.getUser();

        if (oldPassword == null || StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "Old password cannot be empty!");
            return map;
        }

        if (newPassword == null || StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "New password cannot be empty!");
            return map;
        }

        oldPassword = OnlineJudgeUtil.md5(oldPassword + user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            map.put("oldPasswordMsg", "Old Password Error!");

            return map;
        }

        userMapper.updatePassword(user.getUserId(), newPassword);
        newPassword = OnlineJudgeUtil.md5(newPassword + user.getSalt());

        map.put("success", "Password reset successful!");

        return map;
    }
}
