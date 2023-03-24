package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum PostTypeEnum {

    COMMON(0, "普通博客"),

    TUTORIAL(1, "题解");


    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    PostTypeEnum (int key, String value) {
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
