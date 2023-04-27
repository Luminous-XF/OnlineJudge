package cn.edu.nsu.onlinejudge.common.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.omg.CORBA.UNKNOWN;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public enum CommentTypeEnum {

    POST(0, "博客的评论"),

    COMMENT(1, "评论回复");


    public final int key;

    public final String value;

    private static Map<Integer, GenderEnum> map;

    CommentTypeEnum (int key, String value) {
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
