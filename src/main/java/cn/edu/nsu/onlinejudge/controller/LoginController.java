package cn.edu.nsu.onlinejudge.controller;

import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.ForgetPasswordService;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.common.Constant.ExpiredSecondsConstant;
import cn.edu.nsu.onlinejudge.common.Enum.UserActivationResultEnum;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 登录相关入口
 */
@Controller
public class LoginController implements ExpiredSecondsConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;



    /**
     * 注册页面入口
     * @return
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }


    /**
     * 注册信息提交请求入口
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);

        if (map == null || map.isEmpty()) {
            model.addAttribute("msg",
                    "The registration is successful, we have sent an activation email to your mailbox, please activate as soon as possible!");
            model.addAttribute("target", "/index");

            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));

            return "/site/register";
        }
    }


    /**
     * 登录页面入口
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "site/login";
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean remember,
                        Model model, HttpSession session, HttpServletResponse response) {
        // 检查验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "Wrong verification code!");
            return "/site/login";
        }

        // 检查账号、密码
        int expiredSeconds = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }


    /**
     * 退出登录
     * @param ticket
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);

        return "redirect:/login";
    }



    /**
     * 激活验证
     * @param model
     * @param userId
     * @param code
     * @return
     */
    // http://localhost:8080/onlinejudge/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code) {
        UserActivationResultEnum result = userService.activation(userId, code);

        switch (result) {
            case SUCCESS:
                model.addAttribute("msg",
                        "The activation is successful, and your account can be used normally!");
                model.addAttribute("target", "/login");;
                break;
            case REPEAT:
                model.addAttribute("msg",
                        "Invalid operation, the account has been successfully activated, please do not repeat the operation!");
                model.addAttribute("target", "/index");
                break;
            case FAIL:
                model.addAttribute("msg",
                        "Activation failed, the activation code you provided is incorrect!");
                model.addAttribute("target", "/index");
                break;
        }

        return "/site/operate-result";
    }


    /**
     * 获取忘记密码页面
     * @return
     */
    @RequestMapping(path = "/forget-password", method = RequestMethod.GET)
    public String forgetPasswordPage() {

        return "/site/forget-password";
    }


    /**
     * 忘记密码
     * 获取重置密码操作链接
     * @param model
     * @param email
     * @return
     */
    @RequestMapping(path = "/forget-password", method = RequestMethod.POST)
    public String forgetPassword(Model model, String email) {

        Map<String, Object> map = forgetPasswordService.forgetPassword(email);

        if (!map.isEmpty()) {
            model.addAttribute("msg", map.get("emailMsg"));
            return "/site/forget-password";
        }

        User user = userService.findUserByEmail(email);
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("msg", "You have successfully requested to reset your password, we " +
                "will send an email containing a link to reset your password to your registered account, please check and" +
                " complete the relevant operations as soon as possible!");
        model.addAttribute("target", "/index");

        return "site/operate-result";
    }


    /**
     * 重置密码页面
     * @param token
     * @param username
     * @param model
     * @return
     */
    @RequestMapping(path = "/reset-password/{username}/{token}", method = RequestMethod.GET)
    public String resetPasswordPage(@PathVariable String token,
                                @PathVariable String username,
                                Model model) {
        model.addAttribute("username", username);
        model.addAttribute("token", token);


        return "/site/reset-password";
    }


    /**
     * 重置密码操作-token
     * @param username
     * @param password
     * @param token
     * @return
     */
    @RequestMapping(path = "/reset-password-token", method = RequestMethod.POST)
    public String resetPassword(Model model, String username, String password, String token, RedirectAttributes attributes) {
        Map<String, Object> map = forgetPasswordService.resetPassword(username, password, token);

        User user = userService.findUserByUsername(username);
        if (!map.isEmpty()) {
            attributes.addFlashAttribute("tokenMsg", map.get("tokenMsg"));
            return "redirect:/reset-password/" + username + "/" + token;
        }


        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("msg", "You have successfully reset the password, please keep your password" +
                "  and keep in mind it!");
        model.addAttribute("target", "/login");

        return "/site/operate-result";
    }


    /**
     * 生成验证码
     * @param response
     * @param session
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {

        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }
}
