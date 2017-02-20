<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<fe:html title="上传资源" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk-upload/page.css,
         /project/b-ms-cloud/1.x/common/webuploader/css/webuploader.css">
    <script type="text/javascript" src="/js/upload/webuploader-0.1.5/webuploader.js"></script>

    <title>上传资源</title>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zyk-upload/page': 'project/b-ms-cloud/1.x/js/t-zyk-upload/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-zyk-upload/page',function(init){
            init({
                treeUrl:'/teacher/resource/getTree',//树json
                treeId:"#treeDemo",//树Id
                getreeId:"treeDemo",//树Id
                sureUrl:'/teacher/resource/uploadFile',
                //联级
                selProductLine: '/teacher/resource/getSubject',
                selExamSeasonId: '/teacher/resource/getRange',
                selSubjectId: '/teacher/resource/getBookVersion?klbType=0',
                selLastId: '/teacher/resource/getStr',
                obligatoryId: '/teacher/resource/getObligatory',
                <%--swfUrl:'<%= PropertiesConfigUtils.getProperty("domains.ui")%>/project/b-ms-cloud/1.x/common/webuploader/js/Uploader.swf',//ie8/9兼容 flash地址 -- 不要修改--%>
                swfUrl: '/picture/upload/Uploader.swf',
                fileUrlDo:'/teacher/resource/uploadFile',//上传地址
                formDataObj:'${uuid}'
            });
        });
    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="zyk"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-upload">
        <form id="jp-form-chos" action="/teacher/resource/saveResource" method="post" name="resourceForm" enctype="multipart/form-data">
            <div class="p-upl-chos fc">
                <label>学科</label>
                <select id="sel-show" class="i-sel4" name="subjectId">
                    <!-- <option>语文</option> -->
                </select>
                <label>学段</label>
                <select id="sel-show1" class="i-sel4" name="rangeId">
                    <!-- <option>小学</option> -->
                </select>
                <label>版本</label>
                <select id="sel-show2" class="i-sel4" name="bookVersion">
                    <!-- <option>人教版</option> -->
                </select>
                <label>教材</label>
                <select id="sel-show3" class="i-sel4" name="obligatoryId">
                    <!-- <option>必修一</option> -->
                </select>
            </div>
            <input id="jp-lxid" type="hidden" name="type" value="1">
            <input id="jp-scid" type="hidden" name="shareType" value="2">
            <input id="jp-tagId" type="hidden" name="bookVersionIds" value="">

            <!-- 上传文件数据 -->
            <input type="hidden" name="filePath"    value="" id="filePath"/>
            <input type="hidden" name="format"      value="" id="format"/>
            <input type="hidden" name="storageSize" value="" id="storageSize"/>
            <input type="hidden" name="pageSize"    value="" id="pageSize"/>
            <input type="hidden" name="fileOldName" value="" id="fileOldName"/>
            <input type="hidden" name="fileNewName" value="" id="fileNewName"/>
            <input type="hidden" name="extendName"  value="" id="extendName"/>
            <input type="hidden" name="frontcoverUrl"  value="" id="frontcoverUrl"/>
            </form>
        <div class="fc">
            <div class="p-upl-side fl">
                <!-- 树 -->
                <ul id="treeDemo" class="ztree p-ztree"></ul>
            </div>
            <div class="p-upl-cont fr">
                <dl class="p-type">
                    <dt>资源类型</dt>
                    <dd class="radio_area jp-radio1">
                        <c:forEach items="${typeList}" var="t" varStatus="i">
                            <span><input type="radio" id="a${i.index}" class="for-radio" data-lx="${t.value}" <c:if test="${i.index == 0}">checked="checked"</c:if> ><b>${t.name}</b></span>
                        </c:forEach>
                    </dd>
                </dl>
                <dl class="p-type nobd">
                        <dt>选择文件</dt>
                        <dd>
                            <!-- 上传文件 -->
                            <div id="uploader" class="wu-example">
                                <!--用来存放文件信息-->
                                <div class="btns fc">
                                    <div id="picker" class="p-buts">选择文件</div>
                                    <%--<button id="ctlBtn" class="p-buts btn btn-default">开始上传</button>--%>
                                    <span class="jp-hint" style="color:#ff5151;"></span>
                                </div>
                                <div id="thelist" class="uploader-list">
                                    <div class="progress progress-striped active">
                                    <div class="progress-bar" role="progressbar" style="width: 0%">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="radio_area p-limit jp-radio2">
                                <span><input type="radio" id="aa" class="for-radio" data-sc="3"><b>设为私密</b></span>
                                <span><input type="radio" id="bb" class="for-radio" data-sc="2" checked="checked"><b>同时上传至校本资源库</b></span>
                                <span><input type="radio" id="cc" class="for-radio" data-sc="4"><b>同时上传至公共资源库</b></span>
                            </div>
                            <p class="p-format">
                                上传须知：<br />
                                上传文档 WORD/PPT不能超过10M,MP3/MP4/PDF/GIF/JPEG/JPG/PNG文件不能超过100M<br />
                                支持的文档格式如下：<br />
                                WORD/PPT/PDF/MP3/MPG4/GIF/JPEG/JPG/PNG
                            </p>
                        </dd>
                </dl>
            </div>
        </div>
        <p class="p-save">
            <a id="jp-saveresou" class="orange-btn" href="javascript:;" onclick="save()">保存资源</a>
        </p>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
<script type="text/javascript">

    function save(){
        if($("#jp-tagId").val() == ""){
             window._hintPop.hintFun("请选择章节内容!");
            return;
        }
        if($("#filePath").val() == ""){
             window._hintPop.hintFun("请上传文件!");
            return;
        }
        $("#jp-form-chos").submit();
        $("#jp-saveresou").removeAttr("onclick");
    }

 /*   $(function(){
        var uploader = WebUploader.create({
            // swf文件路径
            swf: '/js/upload/webuploader-0.1.5/Uploader.swf',
            // 文件接收服务端。
            server: '/teacher/resource/uploadFile',///upload/do
            formData:{uuid:'${uuid}'},
            chunked:true,
            fileNumLimit: 1,
            fileSingleSizeLimit:100*1024*1024,
            threads:3,
            auto:true,
            chunkSize:11*1024*1024,
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#picker',
            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false,
            accept: {
                title: 'file',
                extensions: 'jpg,png,mp3,mp4,pdf,ppt,gif,jpeg,doc,docx,pptx',
                mimeTypes: 'file*//*'
            }
        });
        var $list = $("#thelist");
        //选择文件，等待上传
        uploader.on( 'fileQueued', function( file ) {
            $('#thelist').empty();
            $list.append( '<div id="' + file.id + '" class="item fc">' +
                    '<h4 class="info fl">' + file.name + '</h4>' +
                    '<p class="state fl">等待上传...</p>' +
                    '</div>' );
            $('#thelist').show();
        });
        //上传过程中触发，携带上传进度
        uploader.on('uploadProgress', function( file, percentage ) {
            var $li = $( '#'+file.id ),
                    $percent = $li.find('.progress .progress-bar');
            $li.find('p.state').text('上传中');
            if ( !$percent.length ) {
                $percent = $('<div class="progress">' +
                        '<div class="progress-bar" role="progressbar" style="width: 0%;background:#4fd07f;max-height: 20px">' +
                        '&nbsp;</div>' +
                        '</div>').appendTo( $li).find('.progress-bar');
            }
            $percent.css( 'width', percentage * 100 + '%' );
            // 避免重复创建
            $('#filePath').val("");
        });
        //上传成功
        uploader.on('uploadSuccess', function(file,data) {
            if(data.error == true){
                $( '#'+file.id ).find('p.state').text('上传失败，WORD/PPT不能超过10M');
            }else{
                $( '#'+file.id ).find('p.state').text('已上传');
                $('#filePath').val(data.filePath);
                $('#format').val(data.format);
                $('#storageSize').val(data.storageSize);
                $('#pageSize').val(data.pageSize);
                $('#fileOldName').val(data.fileOldName);
                $('#fileNewName').val(data.fileNewName);
                $('#extendName').val(data.extendName);
                $('#frontcoverUrl').val(data.frontcoverUrl);
            }
            uploader.removeFile(file);

        });
        //上传格式错误
        uploader.on('error', function(message){
            if("F_EXCEED_SIZE" == message){
                 window._hintPop.hintFun("文件大小不能超过100M");
            }else if("Q_TYPE_DENIED" == message){
                 window._hintPop.hintFun("上传格式错误");
            }else{
                 window._hintPop.hintFun("上传文件错误，请尝试刷新页面重新上传");
            }
        });
        //上传错误
        uploader.on( 'uploadError', function( file ) {
            $( '#'+file.id ).find('p.state').text('上传出错');
        });
        //不管成功或者失败，文件上传完成时触发
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').fadeOut();
        });


    })*/


</script>
