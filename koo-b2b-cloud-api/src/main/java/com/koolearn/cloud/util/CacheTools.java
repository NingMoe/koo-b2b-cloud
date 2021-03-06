package com.koolearn.cloud.util;


import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.common.utils.spring.SpringContextUtils;
import com.koolearn.framework.redis.client.KooJedisClient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheTools {
	
    /**
     * 获取缓存
     *
     * @param key
     * @param
     * @return
     */
    public static <T> T getCacheNoTime(String key,Class<T> classes) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        T t = client.get(key, classes);
        return t;
    }
    
    /**
     * 获取缓存
     *
     * @param key
     * @param
     * @return
     */
    public static <T> T getCache(String key, Class<T> classes) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        T t = client.get(key, classes);
        if (t != null) {
            //重新更新为30分钟
            client.setex(key, 120 * 60, t);
        }
        return t;
    }

    /**
     * 获取缓存无更新活动状态
     *
     * @param key
     * @param classes
     * @return
     */
    public static <T> T getCacheForever(String key, Class<T> classes) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        return client.get(key, classes);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param ot
     */
    public static <T> void addCache(String key, T ot) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            //30分钟无操作失效
            client.setex(key, 120 * 60, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param time 秒钟
     * @param ot
     */
    public static <T> void addCache(String key, int time, T ot) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            //time秒钟无操作失效
            client.setex(key, time, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void addCacheForever(String key, T ot) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            client.set(key, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public static void delCache(String key) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            client.del(key);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void delMapCache(String mapKey , String fildkey ) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            client.hdel(mapKey , fildkey );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Long syncStutus(String key) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        return client.setnx(key, new Date().getTime() + "");
    }

    public static void deleteLearning(UserEntity user) {
        Integer _tpExamAttachmentId = CacheTools.getCache("user_" + user.getId(), Integer.class);
        if (_tpExamAttachmentId != null) {
            List<UserEntity> _userEntityList = CacheTools.getCache("learning_" + _tpExamAttachmentId, List.class);
            if (_userEntityList != null && _userEntityList.size() > 0) {
                for (int i = 0; i < _userEntityList.size(); i++) {
                    if (user.getId().equals(_userEntityList.get(i).getId())) {
                        _userEntityList.remove(i);
                        i--;
                    }
                }
            }
            CacheTools.delCache("user_" + user.getId());
        }
    }

    public static Set<String> getKeys(String key) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        return client.keys(key + "*");
    }




}
