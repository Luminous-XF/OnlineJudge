package cn.edu.nsu.onlinejudge.common.Enum;

import java.util.HashMap;
import java.util.Map;


public enum EntityTypeEnum {

    POST(0, "博客的评论"),

    COMMENT(1, "评论回复");


    public final int key;

    public final String value;

    private static Map<Integer, EntityTypeEnum> map;

    EntityTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    static {
        map = new HashMap<>();
        for (EntityTypeEnum type : EntityTypeEnum.values()) {
            map.put(type.key, type);
        }
    }

    public static EntityTypeEnum fromKey(int key) {
        return map.get(key);
    }
}
