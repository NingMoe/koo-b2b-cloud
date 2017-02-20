package com.koolearn.cloud.teacher.listenCourse.player;

import java.io.Serializable;

/**
 *
 * {"code":200,"data":
 * {"aid":0,
     * "key":"556d9ec5f6e6ddc3d2e4af3aec5bdd6d",
     * "rtmpUrl":"M0qTMD1VkZQ5lveXmwtUlsAWkwEYkdJVJDhVLHAanzbWmC8XlsQULHAa",
     * "sid":"719C30FAA2F7C86CDFF3DA39CD4EB9A3",
     * "time":1452502913458},
 * "message":"success"}
 * Created by Administrator on 2016/1/11.
 */
public class VideoPlayModel implements Serializable {
    private String aid;
    private String key;
    private String rtmpUrl;
    private String swfUrl;
    private String sid;
    private String time;
    private String message;
    private String code;

    public String getSwfUrl() {
        return swfUrl;
    }

    public void setSwfUrl(String swfUrl) {
        this.swfUrl = swfUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
