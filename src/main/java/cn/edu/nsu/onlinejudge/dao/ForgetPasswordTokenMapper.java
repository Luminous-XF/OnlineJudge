package cn.edu.nsu.onlinejudge.dao;


import cn.edu.nsu.onlinejudge.entity.ForgetPasswordToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface ForgetPasswordTokenMapper {

    ForgetPasswordToken selectForgetPasswordToken(int userId);

    void insertForgetPasswordToken(int userId, String token, Date expired);

    void updateForgetPasswordToken(int userId, String token, Date expired);
}
