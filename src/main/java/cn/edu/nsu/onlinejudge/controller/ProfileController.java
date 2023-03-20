package cn.edu.nsu.onlinejudge.controller;


import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.ProfileService;
import cn.edu.nsu.onlinejudge.util.HostHolder;
import cn.edu.nsu.onlinejudge.util.OnlineJudgeUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * 个人信息页相关入口
 */
@Controller
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Value("${onlinejudge.path.upload}")
    private String uploadPath;

    @Value("${onlinejudge.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private HostHolder hostHolder;


    /**
     * 个人信息页-个人信息总览页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/preview", method = RequestMethod.GET)
    public String profilePreview() {

        return "/site/profile";
    }


    /**
     * 个人信息页-设置页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/setting", method = RequestMethod.GET)
    public String profileSetting(Model model) {

        return "/site/profile-setting";
    }


    /**
     * 个人信息页-个人提交页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/submissions", method = RequestMethod.GET)
    public String profileSubmissions() {

        return "/site/profile-my-submissions";
    }


    /**
     * 个人信息页-个人博客页面入口
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/profile/blogs", method = RequestMethod.GET)
    public String profileBlogs() {

        return "/site/profile-my-blogs";
    }

    @LoginRequired
    @RequestMapping(path = "/profile/message", method = RequestMethod.GET)
    public String profileMessage() {

        return "/site/profile-message";
    }


    /**
     * 上传头像
     *
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload/header", method = RequestMethod.POST)
    public String updateHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "You have not selected a picture!");
            return "/site/profile-setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "The image format is incorrect!");
            return "/site/profile-setting";
        }

        // 生产随机文件名
        fileName = OnlineJudgeUtil.generateUUID() + suffix;
        // 确定文件存放位置
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }


        User user = hostHolder.getUser();

        // 删除老的头像文件
        String oldHeaderUrl = user.getHeaderUrl();
        String oldFileName = uploadPath + "/" + oldHeaderUrl.substring(oldHeaderUrl.lastIndexOf("/") + 1);
        File file = new File(oldFileName);
        if (file.exists()) {
            if (file.delete()) {
                logger.info("原头像文件删除成功!");
            } else {
                logger.info("原头像文件删除失败,原图像文件可能不存在!");
            }
        }

        // 更新当前用户的头像的路径(Web访问路径)
        // http://localhost:8081/community/user/header/xxx.png
        String headerUrl = domain + contextPath + "/header/" + fileName;
        int res = profileService.updateHeader(user.getUserId(), headerUrl);
        if (res == 1) {
            logger.info("头像更新成功!");
        } else {
            logger.info("头像更新异常!");
        }

        return "redirect:/profile/setting";
    }


    /**
     * 访问头像资源
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 解析文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fileInputStream = new FileInputStream(fileName);
                OutputStream outputStream = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int index = 0;
            while ((index = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, index);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @LoginRequired
    @RequestMapping(path = "/change-profile", method = RequestMethod.POST)
    public String changeProfile(String nickname, @RequestParam(value = "gender", defaultValue = "0") int gender, String brief) {

        System.out.println(nickname + " " + gender + " " + brief);
        profileService.changeProfile(nickname, gender, brief);

        return "redirect:/profile/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/change-password", method = RequestMethod.POST)
    public String changePassword(Model model, String oldPassword, String newPassword) {
        Map<String, Object> map = profileService.changePassword(oldPassword, newPassword);

        model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
        model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
        model.addAttribute("success", map.get("success"));

        return "/site/profile-setting";
    }
}
