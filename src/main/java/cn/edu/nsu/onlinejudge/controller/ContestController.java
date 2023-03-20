package cn.edu.nsu.onlinejudge.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 比赛相关入口
 */
@Controller
public class ContestController {


    @RequestMapping(path = "/contests", method = RequestMethod.GET)
    public String contests() {


        return "/site/contests";
    }
}
