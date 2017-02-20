package com.koolearn.cloud.common.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.koolearn.framework.common.exception.SerializationException;
import com.koolearn.framework.redis.client.RedisSerializer;

/**
 * @description:redis数据序列化
 * @author yangzhenye
 * @date 2014-7-25
 */
public class K12JavaRedisSerializer implements RedisSerializer<Object> {

    /**
     * 序列化
     */
    public byte[] serialize(Object t) throws SerializationException {
        ByteArrayOutputStream bis = null;
        ObjectOutputStream os = null;
        byte[] byteArray = (byte[]) null;
        try {
            bis = new ByteArrayOutputStream(1024);
            os = new ObjectOutputStream(bis);
            os.writeObject(t);
            byteArray = bis.toByteArray();
        }
        catch (Exception e) {
            throw new SerializationException("序列化失败", e);
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                }
                catch (Exception e1) {
                    throw new SerializationException("", e1);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                }
                catch (Exception e2) {
                    throw new SerializationException("", e2);
                }
            }
        }
        return byteArray;
    }
    /**
     * 反序列化
     */
    public Object deserialize(byte[] bytes) throws SerializationException {
        ByteArrayInputStream bos = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            if (bytes == null)
                return null;
            bos = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bos);
            obj = ois.readObject();
        }
        catch (Exception e) {
            throw new SerializationException("反序列化失败", e);
        }
        finally {
            if (ois != null) {
                try {
                    ois.close();
                }
                catch (Exception e1) {
                    throw new SerializationException("", e1);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                }
                catch (Exception e2) {
                    throw new SerializationException("", e2);
                }
            }
        }
        return obj == null ? null : (Object)obj;
    }
}