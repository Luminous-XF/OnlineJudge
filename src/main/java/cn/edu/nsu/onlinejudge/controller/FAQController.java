package cn.edu.nsu.onlinejudge.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 常见问题解答相关入口
 */
@Controller
public class FAQController {


    @RequestMapping(path = "/faq", method = RequestMethod.GET)
    public String faq() {

        return "/site/faq";
    }
}
