package cn.edu.nsu.onlinejudge.controller;


import cn.edu.nsu.onlinejudge.entity.Page;
import cn.edu.nsu.onlinejudge.entity.Problem;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.UserService;
import com.sun.mail.imap.protocol.MODSEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 排行榜相关入口
 */
@Controller
public class RatingController {

    @Autowired
    private UserService userService;


    /**
     * 排行榜页面入口
     * @return
     */
    @RequestMapping(path = "/rating", method = RequestMethod.GET)
    public String rating(Model model, Page page) {

        page.setLimit(20);
        page.setRows(userService.findUserBySolvedRangeRows());
        page.setPath("/problemset");

        List<User> userList = userService.findUserBySolvedRange(page.getOffset(), page.getLimit());
        model.addAttribute("userList", userList);


        return "/site/rating";
    }

}
