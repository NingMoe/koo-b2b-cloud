<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<div class="p-class-paly fc">

    <div class="p-side fl">
        <div class="p-play">
            <div id="video" style="width:640px;margin:auto;">
            </div>
        </div>
        <p class="p-ass">
            <jsp:include page="page.jsp"/>
        </p>
    </div>
    <div class="p-cont fr">
        <jsp:include page="learning.jsp"/>
    </div>
</div>
<jsp:include page="comment.jsp"/>
<script type="text/javascript">
    var layId = 123;
    //        var videoUrl = 'http://vedio.koolearn.com/upload/vedio/material/dashijiangtang/dream/dream4.mp4';
    var videoUrl = '<%=PropertiesConfigUtils.getProperty("domain")%>/data${resource.filePath}';
    $(document).ready(function () {
        koo.player.create('video', layId, koo.player.lastest.swfPath, {
            layId: "123",//flash调用js时用到的唯一id
            //视频配置
            videoType: "http",//视频类型rtmp 还是http
            volume: "0.8",//设置默认音量
            url: videoUrl,//视频播放地址
            autoPlay: "true",   //视频自动播放
            isEncrypt: "false",//url地址是否需要解密(如果是加密的话，必须为true)
            playfromTime: "0", //设置播放时间
            muted: "false",//静音控制
            //控制条和视频区域参数
            controlBarAutoHide: "true",//控制条是否自动隐藏
            controlBarAutoHideTime: "4",//控制条自动隐藏的时间
            controlBarType: "0",//控制条类型
            controlBarHeight: "35",//控制条高度
            videoWidth: "640",//视频区域宽度
            videoHeight: "360",//视频区域高度
            width: 640,
            height: 395,
            //广告配置
//            plugin:"true",
//            pluginTime:"30",   //片头默认时间（如果是视频则取视频的时间，如果是图片取这个时间）
//            pluginContentUrl:"http://static.koolearn.com/assets/mp4_PT.mp4", //片头地址（视频或是图片）
//            pluginAutoPlay:"true",//广告是否自动播放
//            pluginBackImage:"http://static.koolearn.com/assets/back.png",//片头备用图片
            //播放器跳转地址配置
//                errorBlankUrl:"http://www.koolearn.com",  //播放器报错后用户的留言地址
//                hostTestBlankUrl:"http://www.koolearn.com",  //自动切换站点后视频仍然播放不流畅，用户的留言地址
//                markImageUrl:"http://static.koolearn.com/assets/a.jpg",//用户唯一标识水印图片
//                plugin:"true",//是否加载片头
//                pluginWidth:"640",//片头的宽度
//                pluginHeight:"360",//片头的高度
        });
    });
</script>
