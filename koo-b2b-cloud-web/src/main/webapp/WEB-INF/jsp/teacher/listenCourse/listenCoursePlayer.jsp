<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fe:html title="小学说课-播放页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-saidclass-play/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/s-saidclass-play/page': 'project/b-ms-cloud/1.x/js/s-saidclass-play/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/s-saidclass-play/page',function(init){
            init({
                treeUrl:'/teacher/listenCourse/tree/${productId}',//右侧数据
                treeId:'#treeDemo',//树id
                selectId:'${courseUnit.id}',
                productId:'/teacher/listenCourse/player/${productId}/',
                oneCategoryId:'/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}'
            });
        });
    </script>
    <!--引入koolearn播放器类库--->
    <script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/koo.player.js"></script>
    <!-- 引入最新的配置文件，注意后面要加? -->
    <script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/lastest.player.config.js?v=1"></script>

    <script type="text/javascript">

        //flash调用js时用到的唯一id
        var layId = '123';
        //视频地址
        var videoUrl = 'http://vedio.koolearn.com/upload/vedio/material/dashijiangtang/dream/dream4.mp4';
        //页面中要将video组件放入的容器id
        var videoDiv = 'video';
        $(document).ready(function() {
            //创建播放器
            koo.player.create(videoDiv,layId,koo.player.lastest.swfPath,{
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
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="xxsk"/>
    </jsp:include>

    <!-- 公共的头部 E-->
    <div class="i-main p-saidclass-play">
        <p class="p-title">
            <span>${threeName}</span>
            <b title="${courseUnit.name}">${courseUnit.name}</b>
            <a href="/teacher/listenCourse/detail/${productId}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}" title="返回个人中心"></a>
        </p>
        <div class="s-said-cont fc">
            <div class="p-side fl">
                <div class="p-play">
                    <div class="p-play" id="video">
                    </div>
                </div>
                <p class="p-txt">讲师：${teachers} ／ 课时：${amout} ／ 学过：${percent}<c:if test="${percent == null}">0%</c:if> </p>
            </div>
            <div class="p-cont fr" id="jqcorbox">
                <ul id="treeDemo" class="ztree ztreePlay"></ul>
            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
