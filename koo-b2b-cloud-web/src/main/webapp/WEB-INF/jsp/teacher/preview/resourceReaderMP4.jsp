<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<!DOCTYPE html>
<fe:html title="资源库" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk-detail/page.css">

    <script type="text/javascript">
        seajs.config({
            alias: {
                'project/b-ms-cloud/1.x/js/t-zyk-detail/page': 'project/b-ms-cloud/1.x/js/t-zyk-detail/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/t-zyk-detail/page', function (init) {
            init();
        });
    </script>
    <!--引入koolearn播放器类库--->
    <script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/koo.player.js"></script>
    <!-- 引入最新的配置文件，注意后面要加? -->
    <script type="text/javascript"
            src="http://static.koolearn.com/kooplayer_new/js/lastest.player.config.js?v=1"></script>
    <!---构造播放器--->
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="zyk"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-detail">
        <div class="p-dtitle">
            <jsp:include page="/WEB-INF/jsp/teacher/preview/headReader.jsp"/>
        </div>
        <div id="video" style="width:640px;margin:auto;">
        </div>
        <div class="p-opera">
            <jsp:include page="/WEB-INF/jsp/teacher/preview/footReader.jsp"/>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
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
                height: 395
                //广告配置
//                plugin:"true",
//                pluginTime:"30",   //片头默认时间（如果是视频则取视频的时间，如果是图片取这个时间）
//                pluginContentUrl:"http://static.koolearn.com/assets/mp4_PT.mp4", //片头地址（视频或是图片）
//                pluginAutoPlay:"true",//广告是否自动播放
//                pluginBackImage:"http://static.koolearn.com/assets/back.png",//片头备用图片
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
</fe:html>