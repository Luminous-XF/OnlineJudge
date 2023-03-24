package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum GenderEnum {

    UNCERTAIN(0, "未知"),

    MALE(1, "男性"),

    FEMALE(2, "女性");



    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    GenderEnum (int key, String value) {
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
