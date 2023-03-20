package cn.edu.nsu.onlinejudge.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 题目相关入口
 */
@Controller
public class ProblemController {


    /**
     * 题目列表页面入口
     * @return
     */
    @RequestMapping(path = "/problemset", method = RequestMethod.GET)
    public String problems() {

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
}
