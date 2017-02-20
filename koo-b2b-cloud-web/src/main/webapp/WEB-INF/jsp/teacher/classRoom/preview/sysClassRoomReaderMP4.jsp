<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 右侧 -->
<div class="p-cont fr">
    <div class="p-browse">
        <h2>
            ${ta.attachmentName}
                <c:if test="${sType != 'detail'}">
                    <span style="float: right;  cursor: pointer" onclick="findComment(${ta.id})">查看讨论</span>
                </c:if>
        </h2>
        <div class="p-pvm">
            <!-- 预览 B-->
            <div id="video" style="width:640px;margin:auto;">
            </div>
            <!-- 预览 E-->
        </div>
        <c:if test="${sType == 'detail'}">
            <jsp:include page="comment.jsp"/>
        </c:if>
    </div>
</div>

<script type="text/javascript">
    var layId = 123;
    //        var videoUrl = 'http://vedio.koolearn.com/upload/vedio/material/dashijiangtang/dream/dream4.mp4';
    $(document).ready(function () {
        koo.player.create('video', layId, koo.player.lastest.swfPath, {
            aid: "${vp.aid}",
            //用户唯一标识
            sid:"${vp.sid}",
            //当前时间戳
            time: "${vp.time}",
            //密钥
            key:"${vp.key}",
            isEncrypt : "true",//url地址是否需要解密(如果是加密的话，必须为true)
            //flash调用js时用到的唯一id
            layId:layId,
            //视频配置
            //视频类型rtmp 还是http
            videoType:'rtmp',
            //默认音量
            volume:"0.8",
            //视频播放地址
            url:"${vp.rtmpUrl}",
            //url:videoUrl,
            //url:videoUrl,
            //视频是否自动播放
            autoPlay : "true",

            //控制条和视频区域参数
            controlBarAutoHide: "true",//控制条是否自动隐藏
            controlBarAutoHideTime:"4",//控制条自动隐藏的时间
            controlBarType: "0",//控制条类型
            controlBarHeight:"35",//控制条高度
            //视频区域宽高
            videoWidth:"640",
            videoHeight:"360",
            //播放器在页面中的宽高
            width: 640,
            height: 395,

            //广告配置
            //是否加载片头（一定要设置true）
            plugin:"true",
            pluginTime:"30",   //片头默认时间（如果是视频则取视频的时间，如果是图片取这个时间）
            pluginContentUrl:"http://static.koolearn.com/assets/mp4_PT.mp4", //片头地址（视频或是图片）
            pluginAutoPlay:"true",//广告是否自动播放
            pluginBackImage:"http://static.koolearn.com/assets/back.png",//片头备用图片
            //播放器高级配置
            localData:"true"//是否保存用户上一次的播放时间(只有rtmp视频时应用)
        });
    });
</script>
