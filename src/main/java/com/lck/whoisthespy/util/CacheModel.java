package com.lck.whoisthespy.util;

import java.util.HashMap;
import java.util.Map;

public class CacheModel {

    //声明一个map,用来作为缓存模型
    private static Map<String, Object> map = new HashMap<String, Object>();

    public static Object getValue(String key) {
        Object value = map.get(key);
        if(value==null){
            synchronized (CacheModel.class){
                if(value==null){
                    value="abc";//这里是去数据库查询
                    map.put(key,value);//将数据放到缓存模型中
                }
            }
        }
        return value;
    }
}
