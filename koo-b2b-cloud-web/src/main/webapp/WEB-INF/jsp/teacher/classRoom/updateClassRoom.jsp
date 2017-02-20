<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<!DOCTYPE html>
<fe:html title="编辑课堂" initSeajs="true" ie="true"
    assets="/project/b-ms-cloud/1.x/common/webuploader/css/webuploader.css,
            /project/b-ms-cloud/1.x/css/t-fz-createclass/page.css">

    <script type="text/javascript" src="/js/upload/webuploader-0.1.5/webuploader.js"></script>
<fe:seaConfig>
    alias: {
    'project/b-ms-cloud/1.x/js/t-fz-createclass/page': 'project/b-ms-cloud/1.x/js/t-fz-createclass/page.js',
    'project/b-ms-cloud/1.x/js/t-common-allevel/page': 'project/b-ms-cloud/1.x/js/t-common-allevel/page.js'
    }
</fe:seaConfig>
<script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/t-common-allevel/page', function (init) {
        init({
            treeUrl: '/teacher/resource/getTree',//树json
            treeId: "#treeDemo",//树Id
            getreeId: "treeDemo",//树IdtpExam
            sureUrl: '/teacher/resource/search',
            //联级
            selProductLine: '/teacher/resource/getSubject',
            selExamSeasonId: '/teacher/resource/getRange',
            selSubjectId: '/teacher/resource/getBookVersion',
            selLastId: '/teacher/resource/getStr',
            obligatoryId: '/teacher/resource/getObligatory',
            subjectId:'${tpExam.subjectId}',
            rangeId:'${tpExam.rangeId}',
            bookVersion:'${tpExam.bookVersion}',
            obligatory:'${tpExam.obligatoryId}',
            selectId:'${tpExam.tagId}'
        });
    });
    seajs.use('project/b-ms-cloud/1.x/js/t-fz-createclass/page', function (init) {
        init({
            chooseClass: '/teacher/classRoom/getClasses'//右侧班级加载地址
            // fileUrlDo: '/teacher/resource/classRoom/uploadFile',
            // formDataObj: '${uuid}'
        });
    });
</script>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp">
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="fzkt"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main p-creaclass">
    <div class="p-creatop" id="jp-creatop">
        <h2><b title="${tpExam.examName}">${tpExam.examName}</b><em id="jp-amend-title">修改</em>
            <a class="p-as" href="/teacher/classRoom/index"></a>
        </h2>
        <input id="jp-titleval" class="p-amend" type="text" value="${tpExam.examName}"
               placeholder="${tpExam.examName}">
    </div>
    <div class="p-creacont fc">
        <div class="p-side fl">
            <div class="p-creatime p-gress">
                <label>选择进度点：</label>
            </div>
            <div class="p-overfi" id="jp-overfi">
                <dl class="p-chos">
                    <dt class="p-chos-dt jp-chos">
                        <span>高中物理、新课标新课标新课标</span>
                    </dt>
                    <dd class="p-chos-dd" id="jp-temp">
                        <div class="p-pd12">
                            <form id="jp-form-chos" action="/teacher/classRoom/update" method="post">
                                <div class="p-psnew">
                                    <p>学科</p>

                                    <p>学段</p>

                                    <p>教材版本</p>
                                </div>
                                <div class="p-selnew">
                                    <select class="i-sel1 sel1" id="sel-show" name="subjectId">
                                        <!-- <option value="1">语文</option> -->
                                    </select>
                                    <select class="i-sel1 sel2" id="sel-show1" name="rangeId">
                                        <option value="1">小学</option>
                                    </select>
                                    <select class="i-sel1 sel3" id="sel-show2" name="bookVersion">
                                        <option value="1">人教版</option>
                                    </select>
                                </div>
                                <input name="id" type="hidden" value="${tpExam.id}"/>
                                <input id="jp-oblig" type="hidden" value="${tpExam.obligatoryId}" name="obligatoryId"/><!-- 必修id -->
                                <input id="jp-examName" type="hidden" value="${tpExam.examName}" name="examName"/><!-- 考试题目 -->
                                <input id="jp-tagId" type="hidden" value="${tpExam.tagId}" name="tagId"/><!-- 树选中节点id -->
                                <input id="jp-startime" type="hidden" value="<fmt:formatDate value='${tpExam.startTime}' pattern='yyyy-MM-dd HH:mm' />" name="startTimeStr"/><!-- 开放时间 -->
                                <input id="jp-endtime" type="hidden" value="<fmt:formatDate value='${tpExam.endTime}' pattern='yyyy-MM-dd HH:mm' />" name="endTimeStr"/><!-- 截止时间 -->
                                <input id="jp-classesIds" type="hidden" value="${tpExam.classesIds}" name="classesIds"/><!-- 选择班级id -->
                                <input id="jp-submitType" type="hidden" value="0" name="submitType"/>
                                <input id="jp-section" type="hidden" value="0" name="klbType">

                                <!-- 上传文件 -->
                                <div id="fileId">
                                    <c:forEach items="${tpExam.attachments}" var="att" varStatus="i">
                                        <div id="div_${i.index}">
                                            <input type="hidden" value="${att.attachmentId}"
                                                   name="attachments[${i.index}].attachmentId">
                                            <input type="hidden" value="${att.attachmentType}"
                                                   name="attachments[${i.index}].attachmentType">
                                            <input id="attachmentName_${i.index}" type="hidden"
                                                   value="${att.attachmentName}"
                                                   name="attachments[${i.index}].attachmentName">
                                            <input id="attachmentOrder_${i.index}" type="hidden" value="${att.sort}"
                                                   name="attachments[${i.index}].sort">
                                        </div>
                                    </c:forEach>
                                </div>
                            </form>
                        </div>
                        <a class="p-sure jp-sure" href="javascript:;">确定</a>
                    </dd>
                </dl>
                <div class="p-chos p-compu">
                    <select id="obligatoryId" class="obligatoryId i-sel4" name="obligatory">
                        <option>必修一</option>
                        <option>必修二</option>
                        <option>必修三</option>
                    </select>
                </div>
                <!-- 章节 -->
                <div class="p-chapter">
                    <!-- 树 -->
                    <ul id="treeDemo" class="ztree p-pd15"></ul>
                </div>
            </div>
            <!-- 选择班级 -->
            <div class="p-choseclass" id="jp-choseclass">
                <input id="jp-classarr" type="hidden" value="" name="classArr">
                <dl class="p-chose-class">
                    <dt class="p-cc-dt">选择班级：</dt>
                    <dd class="p-cc-dd checkbox_area" id="jp-grade">

                    </dd>
                </dl>
            </div>
            <!-- 设置课堂时间 -->
            <div class="p-creatime" id="jp-creatime">
                <label>设置课堂时间：</label>

                <p class="p-tims">
                    <input type="text" value="<fmt:formatDate value="${tpExam.startTime}" pattern="yyyy-MM-dd HH:mm"/>" class="p-set-tim-input jp-set-startime" readonly placeholder="开放时间"
                           name="startTime" >
                    <span><i></i>课堂开放后，学生才可以看到课堂内容。</span>
                </p>
                <p class="p-tims">
                    <input type="text" value="<fmt:formatDate value="${tpExam.endTime}" pattern="yyyy-MM-dd HH:mm"/>"  class="p-set-tim-input jp-set-endtime" readonly placeholder="截止时间"
                           name="endTime">
                    <span><i></i>课堂截止后，学生只能浏览课件，不能提交作业。</span>
                </p>

            </div>
        </div>
        <div class="p-cont fr" id="jp-cont-inpt">
            <ul id="jp-uploadul">
                <c:forEach items="${tpExam.attachments}" var="att" varStatus="i">
                    <li class="aaa li_${i.index}" name="li_${i.index}">
                        <h4>
                            <c:if test="${att.attachmentType == 2}">
                                <span>作业：</span>
                            </c:if>
                            <c:if test="${att.attachmentType == 0}">
                                <span>附件：</span>
                            </c:if>
                            <c:if test="${att.attachmentType == 1}">
                                <span>${att.typeStr}：</span>
                            </c:if>
                            <b style="display: inline;">${att.attachmentName}</b>
                            <input id="input_${i.index}" class="p-amend jp-inpts" type="text" value="${att.attachmentName}" placeholder="${att.attachmentName}" name="attachmentName" style="display: none;">
                            <em onclick="setName(${i.index})">修改</em>
                        </h4>
                        <div class="p-as">
                            <a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(${i.index})"></a>
                            <a class="p-down bbb" style="display: none" href="javascript:;" onclick="down(${i.index})"></a>
                            <a class="p-del" href="javascript:;" onclick="del(${i.index})"></a>
                        </div>
                        <c:if test="${att.attachmentType == 2}">
                            <p class="p-txt">${att.classesFullNames}</p>
                        </c:if>
                    </li>
                </c:forEach>
                    <%-- <li class="aaa li_1">
                         <h4>
                             <b>课件：第一课 </b>
                             <input id="" class="p-amend jp-inpts" type="text" value="" placeholder="课件：第一课" name="">
                             <em>确定</em>
                         </h4>
                         <div class="p-as">
                             <a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(1)"></a>
                             <a class="p-down bbb"  style="display: none" href="javascript:;" onclick="down(1)"></a>
                             <a class="p-del" href="javascript:;"></a>
                         </div>
                     </li>--%>
            </ul>
            <ul>

                <li class="bg">
                    <a class="p-add" href="javascript:;">新增条目:</a>

                    <div class="p-as">
                        <a id="jp-local-upload" class="p-upload" href="javascript:;"><i></i>本地</a>
                        <a id="zyk-upload" class="p-upload" href="javascript:;"><i></i>资源库</a>
                        <a id="tiku-upload" class="p-upload" href="javascript:;"><i></i>题库</a>
                    </div>
                    <!-- 上传后的状态 -->
                    <div id="thelist" class="uploader-list">
                        <div class="progress progress-striped active">
                            <div class="progress-bar" role="progressbar" style="width: 0%"></div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="p-release">
        <a id="jp-release-class" class="orange-btn mgr20" href="javascript:;">发布课堂</a>
        <a id="js-savebtn" class="green-btn green-btn-style" href="javascript:;">保存</a>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</fe:html>
<script type="text/javascript">

var count = ${fn:length(tpExam.attachments)};

$("#jp-uploadul").on("mouseover", ".aaa", function () {
    $(this).children().children(".bbb").css('display', 'inline-block');
});

$("#jp-uploadul").on("mouseout", ".aaa", function () {
    $(this).children().children(".bbb").css('display', 'none');
});

//上移
function up(num) {
    var obj = $(".li_" + num);
    var upObj = obj.prev();
    if (upObj.attr("name") != null) {
        var upId = upObj.attr("name").substring(3);
        var objVal = $("#attachmentOrder_" + num).val();
        var upobjVal = $("#attachmentOrder_" + upId).val();
        $("#attachmentOrder_" + num).val(upobjVal);
        $("#attachmentOrder_" + upId).val(objVal);
        obj.insertBefore(upObj);
    }
}

//下移
function down(num) {
    var obj = $(".li_" + num);
    var downObj = obj.next();
    if (downObj.attr("name") != null) {
        var downId = downObj.attr("name").substring(3);
        var objVal = $("#attachmentOrder_" + num).val();
        var downObjVal = $("#attachmentOrder_" + downId).val();
        $("#attachmentOrder_" + num).val(downObjVal);
        $("#attachmentOrder_" + downId).val(objVal);
        obj.insertAfter(downObj);
    }
}


//资源库
$("#zyk-upload").on("click", function () {
    window.open('/teacher/resource/index?urlType=1', '选择资源', 'height=800, width=1300, top=0, left=0, toolbar=no, menubar=yes, scrollbars=yes,resizable=yes,location=no, status=no');
})

function zyCommit(sourceId, sourceName,sourceType) {
    var $ul = $("#jp-uploadul");
    $ul.append('<li class="aaa li_' + count + '" name="li_' + count + '"><h4><span>'+sourceType+'：</span><b>' + sourceName + '</b>' +
            '<input id="input_' + count + '" class="p-amend jp-inpts" type="text" value="' + sourceName + '" placeholder="" name="">' +
            '<em onclick="setName(' + count + ')">确定</em></h4><div class="p-as">' +
            '<a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(' + count + ')"></a>' +
            '<a class="p-down bbb" style="display: none" href="javascript:;" onclick="down(' + count + ')"></a>' +
            '<a class="p-del" href="javascript:;" onclick="del(' + count + ')"></a></div></li>');
    $("#fileId").append(
                    '<div id="div_' + count + '">' +
                    '<input type="hidden" value="' + sourceId + '" name="attachments[' + count + '].attachmentId"/>' +
                    '<input type="hidden" value="1" name="attachments[' + count + '].attachmentType"/>' +
                    '<input id="attachmentName_' + count + '" type="hidden" value="' + sourceName + '" name="attachments[' + count + '].attachmentName"/>' +
                    '<input id="attachmentOrder_' + count + '" type="hidden" value="' + count + '" name="attachments[' + count + '].sort"  />' +
                    '</div>'
    );
    count++;
}

//题库
$("#tiku-upload").on("click", function () {
    gradeNodes();
    window.open('/teacher/task/classRoom/assign?subjectId='+$("#sel-show").val()+'&classesIds=' + $("#jp-classesIds").val(), '选择题库', 'height=800, width=1300, top=0, left=0, toolbar=no, menubar=yes, scrollbars=yes,resizable=yes,location=no, status=no');
})

function tikuCommit(examId, examName, classesName) {
    var $ul = $("#jp-uploadul");
    $ul.append('<li class="aaa li_' + count + '" name="li_' + count + '"><h4><span>作业：</span><b>' + examName + '</b>' +
            '<input id="input_' + count + '" class="p-amend jp-inpts" type="text" value="' + examName + '" placeholder="" name="">' +
            '<em onclick="setName(' + count + ')">确定</em></h4><div class="p-as">' +
            '<a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(' + count + ')"></a>' +
            '<a class="p-down bbb" style="display: none" href="javascript:;" onclick="down(' + count + ')"></a>' +
            '<a class="p-del" href="javascript:;" onclick="del(' + count + ')"></a></div><p class="p-txt">' + classesName + '</p></li>');
    $("#fileId").append(
                    '<div id="div_' + count + '">' +
                    '<input type="hidden" value="' + examId + '" name="attachments[' + count + '].attachmentId"/>' +
                    '<input type="hidden" value="2" name="attachments[' + count + '].attachmentType"/>' +
                    '<input id="attachmentName_' + count + '" type="hidden" value="' + examName + '" name="attachments[' + count + '].attachmentName"/>' +
                    '<input id="attachmentOrder_' + count + '" type="hidden" value="' + count + '" name="attachments[' + count + '].sort"/>' +
                    '</div>'
    );
    count++;
}


//删除
function del(num) {
    $(".li_" + num).remove();
    $("#div_" + num).remove();
}
//设置名称
function setName(num) {
    $("#attachmentName_" + num).val($("#input_" + num).val());
}

var uploader = WebUploader.create({
    // swf文件路径
    <%--swf: '<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/common/webuploader/js/Uploader.swf',--%>
    swf: '/picture/upload/Uploader.swf',
    // 文件接收服务端。
    server: "/teacher/resource/classRoom/uploadFile",
    formData: {uuid: '${uuid}'},
    chunked: false,
    fileNumLimit: 1,
    fileSingleSizeLimit: 100 * 1024 * 1024,
    threads: 1,
    auto: true,
    chunkSize: 11 * 1024 * 1024,
    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#jp-local-upload',
    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false,
    accept: {
        title: 'file',
        extensions: 'jpg,png,mp3,mp4,pdf,ppt,gif,jpeg,doc,docx,pptx',
        mimeTypes: 'file/*'
    }
});
var $list = $("#thelist"),
        $ul = $("#jp-uploadul");

uploader.on('uploadBeforeSend', function (object, data, headers) {
    data.subjectId = $("#sel-show").val();
    data.rangeId = $("#sel-show1").val();
    data.bookVersionIds = $("#jp-tagId").val();
});

uploader.on('beforeFileQueued', function (file) {
    if ($("#jp-tagId").val() == "") {
         window._hintPop.hintFun("请选择进度点");
        return false;
    }
    //校验
    var extension = file.ext,//上传文件扩展名
            fileSize = file.size;//上传文件大小
    if(extension == 'ppt' || extension == 'doc' || extension == 'docx') {
        if (fileSize > 1024 * 1024 * 10) {
            $list.hide();
            window._hintPop.hintFun("文件大小超出限制，最大10m");
            return false;
        }
    }
})

//选择文件，等待上传
uploader.on('fileQueued', function (file) {
    //上传文件
    $ul.append('<li id="' + file.id + '" class="aaa li_' + count + '" name="li_' + count + '"><h4><span>附件：</span><b>' + file.name + '</b>' +
            '<input id="input_' + count + '" class="p-amend jp-inpts" type="text" value="' + file.name + '" placeholder="" name="">' +
            '<em onclick="setName(' + count + ')">确定</em></h4><div class="p-as">' +
            '<a class="p-up bbb" style="display: none" href="javascript:;" onclick="up(' + count + ')"></a>' +
            '<a class="p-down bbb" style="display: none" href="javascript:;" onclick="down(' + count + ')"></a>' +
            '<a class="p-del" href="javascript:;" onclick="del(' + count + ')"></a></div></li>');
});
//上传过程中触发，携带上传进度
uploader.on('uploadProgress', function (file, percentage) {
    var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');
    if (!$percent.length) {
        $percent = $('<div class="progress">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%;background:#4fd07f;max-height: 20px">' +
                '&nbsp;</div>' +
                '</div>').appendTo($li).find('.progress-bar');
    }
    $percent.css('width', percentage * 100 + '%');
    // 避免重复创建
    $('#filePath').val("");
});
//上传成功
uploader.on('uploadSuccess', function (file, data) {
    if (data.error == true) {
        $(".li_" + count).remove();
    } else {
        $("#fileId").append(
                        '<div id="div_' + count + '">' +
                        '<input type="hidden" value="' + data.id + '" name="attachments[' + count + '].attachmentId"/>' +
                        '<input type="hidden" value="0" name="attachments[' + count + '].attachmentType"/>' +
                        '<input id="attachmentName_' + count + '" type="hidden" value="' + data.name + '" name="attachments[' + count + '].attachmentName"/>' +
                        '<input id="attachmentOrder_' + count + '" type="hidden" value="' + count + '" name="attachments[' + count + '].sort"/>' +
                        '</div>');
        count++;
    }
    uploader.removeFile(file);
});
//上传格式错误
uploader.on('error', function (message) {
    if ("F_EXCEED_SIZE" == message) {
         window._hintPop.hintFun("文件大小不能超过100M");
    } else if ("Q_TYPE_DENIED" == message) {
         window._hintPop.hintFun("上传格式错误");
    } else {
         window._hintPop.hintFun("上传文件错误，请尝试刷新页面重新上传");
    }
});
//上传错误
uploader.on('uploadError', function (file) {
    $('#' + file.id).find('p.state').text('上传出错');
});
//不管成功或者失败，文件上传完成时触发
uploader.on('uploadComplete', function (file) {
    $('#' + file.id).find('.progress').fadeOut();
});


gradeNodes = function () {
    var $grade = $('#jp-grade'),
            $dtLabel = $grade.find('dt').find('label'),
            dtId = '',
            ddId = '',
            mess = '';

    for (var i = 0; i <= $dtLabel.length; i++) {
        if ($($dtLabel[i]).hasClass('on')) {
            dtId += $($dtLabel[i]).prev().val() + ',';
            mess += $($dtLabel[i]).parent().find('b').text() + ',';
        } else {
            var $ddlab = $($dtLabel[i]).parent().next('dd').find('label');
            for (var j = 0; j <= $ddlab.length; j++) {
                if ($($ddlab[j]).hasClass('on')) {
                    ddId += $($ddlab[j]).prev().val() + ',';
                    mess += $($dtLabel[i]).parent().find('b').text() + $($ddlab[j]).prev().prev().text() + ',';
                }
            }
        }
    }
    //班级id隐藏域赋值
    var ids = dtId + ddId,
            classesIds = ids.replace(/,$/gi, '');
    $('#jp-classesIds').val(classesIds);
    //选择班级name名称
    var name = mess.replace(/,$/gi, '');
    $('#jp-classarr').val(name);
}


</script>
