package com.wes.goddard.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * all server responds will be encapsulated to this class
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //1: success, 0: other

    private String msg; //error message

    private T data; //data

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
