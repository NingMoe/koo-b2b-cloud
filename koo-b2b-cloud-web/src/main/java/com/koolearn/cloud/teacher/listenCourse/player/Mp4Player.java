package com.koolearn.cloud.teacher.listenCourse.player;

import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.library.maintain.course.entity.CourseUnit;
import com.koolearn.library.maintain.knowledge.entity.Resource;
import com.koolearn.library.maintain.util.HttpRequestUtil;
import com.koolearn.library.maintain.util.PlayerMD5;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/5.
 */
public class Mp4Player extends Player {

    private final static Logger logger = Logger.getLogger(Mp4Player.class);

    private static final int CONSUMER_TYPES = 1003003;
    private static final String PC_KEY = "AB22CFF2-E895-4A31-9EE2-CD576EA652A1";
    private static final String MOVE_KEY = "9659972E-C956-4AE3-AE6E-E47F9690AAE1";

    public Mp4Player(CourseUnit courseUnit, ModelMap map) {
        this.courseUnit = courseUnit;
        this.map = map;
    }

    /**
     * Id=459
     * consumerType=1003003
     * playerNumber=0001
     * PC端秘钥=AB22CFF2-E895-4A31-9EE2-CD576EA652A1
     * 移动端秘钥=9659972E-C956-4AE3-AE6E-E47F9690AAE1
     * 公共信息秘钥=2D3ABEB3-10E9-4730-90C0-B7A751025A85
     * <p/>
     * code
     * 成功：200
     * 失败：500系统异常
     * 50101请求参数不完整或有误
     * 50102 业务方验证失败
     * 50103 业务验证DTO为空
     * 50104 请求已超时
     * 50105 请求不合法
     *
     * @return
     */
    public String getUrl() {
        try {
            VideoPlayModel vp = new VideoPlayModel();
            StringBuilder parameter = new StringBuilder();
            //视频接口获取数据地址
            String plUrl = PropertiesConfigUtils.getProperty("mp4.pc.url");
            if (logger.isInfoEnabled()) {
                logger.info("Mp4Player------getplUrl=" + plUrl);
            }
            long timesTamp = new Date().getTime();
            String skey = new PlayerMD5().getMD5ofStr(String.valueOf(CONSUMER_TYPES) + String.valueOf(timesTamp) + PC_KEY);
            skey = skey.substring(0, 9) + skey.substring(skey.length() - 10, skey.length());
            String mp4Url = listenService.getResourceUrl(courseUnit.getKnowledgeitemId(), Resource.MP4_TYPP);
            if (logger.isInfoEnabled()) {
                logger.info("Mp4Player------mp4Url=" + mp4Url);
            }
            if (mp4Url != null && !"".equals(mp4Url)) {
                parameter.append(plUrl);
                parameter.append("?url=" + URLEncoder.encode(mp4Url, "utf-8"));
                //parameter.append("?url=/mp4/111.mp4");
                parameter.append("&timesTamp=" + timesTamp);
                parameter.append("&consumerTypes=" + CONSUMER_TYPES);
                parameter.append("&aid=0");
                parameter.append("&sid=719C30FAA2F7C86CDFF3DA39CD4EB9A3");
                parameter.append("&vodType=2001");
                parameter.append("&sKey=" + skey);
                if (logger.isInfoEnabled()) {
                    logger.info("Mp4Player------videoUrl=" + parameter.toString());
                }
                //请求视频地址
                String result = HttpRequestUtil.doGet(parameter.toString());
                if (logger.isInfoEnabled()) {
                    logger.info("Mp4Player------returnVideoUrl=" + result);
                }
                JSONObject jsonObject = JSONObject.fromObject(result);
                vp.setCode(jsonObject.getString("code"));
                vp.setMessage(jsonObject.getString("message"));
                if ("200".equals(jsonObject.getString("code"))) {
                    jsonObject = JSONObject.fromObject(jsonObject.getString("data"));
                    vp.setAid(jsonObject.getString("aid"));
                    vp.setSid(jsonObject.getString("sid"));
                    vp.setKey(jsonObject.getString("key"));
                    vp.setRtmpUrl(jsonObject.getString("rtmpUrl"));
                    vp.setTime(jsonObject.getString("time"));
                }
                map.addAttribute("vp", vp);
            } else {
                errorMessage = "视频资源不存在!";
                success = false;
            }
            if (success) {
                url = "/teacher/listenCourse/listenCoursePlayer";
            }
            map.addAttribute("errorMessage", errorMessage + "   code:" + vp.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            map.addAttribute("errorMessage", errorMessage);
        }
        return url;
    }
}
