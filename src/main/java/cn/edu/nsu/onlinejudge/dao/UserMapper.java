package cn.edu.nsu.onlinejudge.dao;

import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.common.Enum.GenderEnum;
import cn.edu.nsu.onlinejudge.common.Enum.UserActivationStatusEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectByUserId(int userId);

    User selectByUsername(String username);

    User selectByEmail(String email);

    List<User> selectUserBySolvedRange(int offset, int limit);

    int findUserBySolvedRangeRows();

    int insertUser(User user);

    int updateNickname(int userId, String nickname);

    int updateType(int userId, int type);

    int updateSubmit(int userId, int submit);

    int updateSolved(int userId, int solved);

    int updateDefunct(int userId, int defunct);

    int updateRating(int userId, int rating);

    int updateRanks(int userId, int ranks);

    int updateSchool(int userId, String school);

    int updateStatus(int userId, UserActivationStatusEnum status);

    int updateHeaderUrl(int userId, String headerUrl);

    int updatePassword(int userId, String password);

    int updateGender(int userId, GenderEnum gender);

    int updateBrief(int userId, String brief);
}
