package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum UserDefunctStatusEnum {

    NORMAL(0, "正常"),

    BANNED(1, "已封禁");



    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    UserDefunctStatusEnum (int key, String value) {
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
