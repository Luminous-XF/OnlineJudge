package cn.edu.nsu.onlinejudge.util;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class OnlineJudgeUtil {

    /**
     * 生成随机字符串
     * @return 返回一个只有数字以及字母的随机字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 对字符串key进行MD5加密
     * @param key
     * @return 返回key进行MD5加密后的结果,如果key为空则返会null
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);

        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }

        return json.toJSONString();
    }


    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }
}
