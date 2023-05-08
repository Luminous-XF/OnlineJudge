package cn.edu.nsu.onlinejudge.dao;


import cn.edu.nsu.onlinejudge.entity.Problem;
import cn.edu.nsu.onlinejudge.entity.SampleData;
import cn.edu.nsu.onlinejudge.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper {

    List<Problem> selectProblems(int offset, int limit, List<Integer> statusList);

    int selectProblemRows(List<Integer> statusList);

    List<Problem> selectProblemsByTitleOrNumber(int offset, int limit, String param, List<Integer> statusList);

    int selectProblemsByTitleOrNumberRows(String param, List<Integer> statusList);

    Problem selectProblemById(int problemId, List<Integer> statusList);
}
