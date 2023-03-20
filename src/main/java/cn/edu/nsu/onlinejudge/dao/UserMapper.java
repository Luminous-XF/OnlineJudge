package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectByUserId(int userId);

    User selectByUsername(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateNickname(int userId, String nickname);

    int updateType(int userId, int type);

    int updateSubmit(int userId, int submit);

    int updateSolved(int userId, int solved);

    int updateDefunct(int userId, int defunct);

    int updateRating(int userId, int rating);

    int updateRanks(int userId, int ranks);

    int updateSchool(int userId, String school);

    int updateStatus(int userId, int status);

    int updateHeaderUrl(int userId, String headerUrl);

    int updatePassword(int userId, String password);

    int updateGender(int userId, int gender);

    int updateBrief(int userId, String brief);
}
