package cn.edu.nsu.onlinejudge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 首页入口
 */
@Controller
public class HomeController {


    /**
     * 首页入口
     * @param model
     * @return
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model) {

        return "/index";
    }
}
