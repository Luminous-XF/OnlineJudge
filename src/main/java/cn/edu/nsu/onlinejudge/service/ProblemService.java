package cn.edu.nsu.onlinejudge.service;


import cn.edu.nsu.onlinejudge.dao.ProblemMapper;
import cn.edu.nsu.onlinejudge.entity.Problem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {

    @Autowired
    private ProblemMapper problemMapper;


    public List<Problem> findProblems(int offset, int limit, List<Integer> statusList) {
        return problemMapper.selectProblems(offset, limit, statusList);
    }


    public int findProblemsRows(List<Integer> statusList) {
        return problemMapper.selectProblemRows(statusList);
    }


    public List<Problem> findProblemsByTitleOrNumber(int offset, int limit, String param, List<Integer> statusList) {
        return problemMapper.selectProblemsByTitleOrNumber(offset, limit, param, statusList);
    }

    public int findProblemsByTitleOrNumberRows(String param, List<Integer> statusList) {
        return problemMapper.selectProblemsByTitleOrNumberRows(param, statusList);
    }


    public Problem findProblemById(int problemId, List<Integer> statusList) {
        return problemMapper.selectProblemById(problemId, statusList);
    }
}
