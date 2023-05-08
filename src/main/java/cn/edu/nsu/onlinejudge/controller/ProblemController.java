package cn.edu.nsu.onlinejudge.controller;


import cn.edu.nsu.onlinejudge.common.HostHolder;
import cn.edu.nsu.onlinejudge.dao.ProblemMapper;
import cn.edu.nsu.onlinejudge.dao.SampleDataMapper;
import cn.edu.nsu.onlinejudge.entity.*;
import cn.edu.nsu.onlinejudge.service.ProblemService;
import cn.edu.nsu.onlinejudge.service.SampleDataService;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 题目相关入口
 */
@Controller
public class ProblemController {


    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private SampleDataService sampleDataService;


    /**
     * 题目列表页面入口
     * @return
     */
    @RequestMapping(path = "/problemset", method = RequestMethod.GET)
    public String problems(Model model, Page page) {

        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        page.setRows(problemService.findProblemsRows(statusList));
        page.setPath("/problemset");

        List<Problem> problemList = problemService.findProblems(page.getOffset(), page.getLimit(), statusList);
        List<Map<String, Object>> problems = new ArrayList<>();

        if (problemList != null) {
            for (Problem problem : problemList) {
                Map<String, Object> map = new HashMap<>();

                map.put("problem", problem);

                problems.add(map);
            }
        }
        model.addAttribute("problems", problems);

        List<User> userList = userService.findUserBySolvedRange(0, 10);
        List<Map<String, Object>> users = new ArrayList<>();

        if (userList != null) {
            for (User user : userList) {
                Map<String, Object> map = new HashMap<>();

                map.put("user", user);

                users.add(map);
            }
        }
        model.addAttribute("users", users);

        return "/site/problemset";
    }


    /**
     * 题目列表页面入口-搜索
     * @return
     */
    @RequestMapping(path = "/problemset/search", method = RequestMethod.POST)
    public String problems(Model model, Page page, String param) {

        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        page.setRows(problemService.findProblemsByTitleOrNumberRows(param, statusList));
        page.setPath("/problemset");

        List<Problem> problemList = problemService.findProblemsByTitleOrNumber(page.getOffset(), page.getLimit(), param, statusList);
        List<Map<String, Object>> problems = new ArrayList<>();

        if (problemList != null) {
            for (Problem problem : problemList) {
                Map<String, Object> map = new HashMap<>();

                map.put("problem", problem);

                problems.add(map);
            }
        }
        model.addAttribute("problems", problems);

        List<User> userList = userService.findUserBySolvedRange(0, 10);
        List<Map<String, Object>> users = new ArrayList<>();

        if (userList != null) {
            for (User user : userList) {
                Map<String, Object> map = new HashMap<>();

                map.put("user", user);

                users.add(map);
            }
        }
        model.addAttribute("users", users);

        return "/site/problemset";
    }


    /**
     * 题目提交状态页面入口
     * @return
     */
    @RequestMapping(path = "/status", method = RequestMethod.GET)
    public String status() {

        return "/site/status";
    }


    /**
     * 题目详情页
     * @return
     */
    @RequestMapping(path = "/problem/{problemId}", method = RequestMethod.GET)
    public String problem(Model model, @PathVariable int problemId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        Problem problem = problemService.findProblemById(problemId, statusList);

        if (problem == null) {
            return "/error/404";
        }


        User author = userService.findUserByUserId(problem.getAuthor());
        List<SampleData> sampleDataList = sampleDataService.findSampleData(problemId);
        System.out.println(problemId);
        System.out.println(sampleDataList);
        model.addAttribute("problem", problem);
        model.addAttribute("author", author);
        model.addAttribute("sampleDataList", sampleDataList);

        return "/site/problem";
    }
}
