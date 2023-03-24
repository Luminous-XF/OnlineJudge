package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum UserActivationResultEnum {

    FAIL(0, "激活失败"),

    SUCCESS(1, "激活成功"),

    REPEAT(2, "重复激活");


    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    UserActivationResultEnum (int key, String value) {
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
