package cn.edu.nsu.onlinejudge.util;


import cn.edu.nsu.onlinejudge.controller.ProfileController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Value("${onlinejudge.path.uploadFirstImage}")
    private String uploadPath;

    @Value("${onlinejudge.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Map<String, Object> uploadImage(MultipartFile Image) {
        Map<String, Object> res = new HashMap<>();

        String fileName = Image.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            res.put("error", "The first-image format is incorrect!");
            return res;
        }

        // 生产随机文件名
        fileName = OnlineJudgeUtil.generateUUID() + suffix;
        // 确定文件存放位置
        File dest = new File(uploadPath + "/" + fileName);
        try {
            Image.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传博客首图失败: " + e.getMessage());
            throw new RuntimeException("上传博客首图失败,服务器发生异常!", e);
        }

        String firstImageUrl = domain + contextPath + "/first-image/" + fileName;
        res.put("firstImageUrl", firstImageUrl);

        return res;
    }

    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
