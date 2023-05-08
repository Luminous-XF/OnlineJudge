package cn.edu.nsu.onlinejudge;


import cn.edu.nsu.onlinejudge.dao.ProblemMapper;
import cn.edu.nsu.onlinejudge.entity.Problem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = OnlineJudgeApplication.class)
public class ProblemTest {

    @Autowired
    private ProblemMapper problemMapper;

    @Test
    public void testProblemList() {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        List<Problem> list = problemMapper.selectProblems(0, 10, statusList);
//        for (Problem problem : list) {
//            System.out.println(problem);
//        }

    }
}
