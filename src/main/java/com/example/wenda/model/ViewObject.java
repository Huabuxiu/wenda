package com.example.wenda.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-25 16:55
 **/
public class ViewObject {
    private Map<String ,Object> objs = new HashMap<>();
    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
