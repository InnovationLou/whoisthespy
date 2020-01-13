package com.lck.whoisthespy.util;

import java.util.HashMap;
import java.util.Map;

public class CacheModel {

    //声明一个map,用来作为缓存模型
    private static Map<String, Object> map = new HashMap<String, Object>();


    public synchronized static Object getValue(String key) {
        Object value = map.get(key);
        return value;
    }
    public synchronized static Boolean putObj(String key,Object object){
        if(map.containsKey(key)){
            return false;
        }
        map.put(key,object);
        return true;
    }
}
