package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum PostStatusEnum {

    COMMON(0, "普通博文"),

    GOOD(1, "优质博文"),

    TOP(2, "置顶博文"),

    BANNED(1, "已封禁"),

    DELETE(2, "已删除");


    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    PostStatusEnum (int key, String value) {
        this.key = key;
        this.value = value;
    }

    static {
        map = new HashMap<>();
        for (GenderEnum type : GenderEnum.values()) {
            map.put(type.key, type);
        }
    }

    public static GenderEnum fromKey(int key) {
        return map.get(key);
    }
}
