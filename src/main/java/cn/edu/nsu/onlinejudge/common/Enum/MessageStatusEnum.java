package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;

public enum MessageStatusEnum {

    UNREAD(0, "未读"),

    HAVEREAD(1, "已读"),

    MESSAGE(2, "消息"),

    DELETE(3, "删除");



    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    MessageStatusEnum (int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
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
