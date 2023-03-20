package cn.edu.nsu.onlinejudge.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 排行榜相关入口
 */
@Controller
public class RatingController {


    /**
     * 排行榜页面入口
     * @return
     */
    @RequestMapping(path = "/rating", method = RequestMethod.GET)
    public String rating() {

        return "/site/rating";
    }

}
