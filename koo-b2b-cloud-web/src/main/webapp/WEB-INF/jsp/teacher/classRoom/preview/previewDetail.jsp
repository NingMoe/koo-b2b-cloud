<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--引入koolearn播放器类库--->
<script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/koo.player.js"></script>
<!-- 引入最新的配置文件，注意后面要加? -->
<script type="text/javascript"
        src="http://static.koolearn.com/kooplayer_new/js/lastest.player.config.js?v=1"></script>

<!DOCTYPE html>
<fe:html title="翻转课堂-预览课堂" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-fz-courseware/page.css,
         /project/b-ms-cloud/1.x/css/t-fz-watchClass/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-fz-courseware/page': 'project/b-ms-cloud/1.x/js/t-fz-courseware/page.js'
        }
    </fe:seaConfig>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/_fz-top/page': 'project/b-ms-cloud/1.x/js/_fz-top/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-fz-courseware/page',function(init){
            init();
        });
        seajs.use('project/b-ms-cloud/1.x/js/_fz-top/page',function(init){
            init();
        });
    </script>


    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="fzkt"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-courvare">
        <!-- 模板框架 B-->
        <div class="p-fz-title">
            <h2>${tpExam.examName}</h2>
            <p>开始时间：<fmt:formatDate value='${tpExam.startTime}' pattern='yyyy-MM-dd HH:mm' />
                ／ 截止时间：<fmt:formatDate value='${tpExam.endTime}' pattern='yyyy-MM-dd HH:mm' />
                ／ 状态：
                <c:if test="${tpExam.finishStatus == 1}">
                    <c:if test="${tpExam.status == 1}">
                        未发布
                    </c:if>
                    <c:if test="${tpExam.status == 2}">
                        已撤回
                    </c:if>
                    <c:if test="${tpExam.status == 4}">
                        进行中
                    </c:if>
                </c:if>
                <c:if test="${tpExam.finishStatus == 0}">
                    已结束
                </c:if>
            </p>
            <a class="green-btn green-btn-style" href="/teacher/classRoom/previewPlayer?classRoomId=${classRoomId}&classesId=${classesId}">播放课堂</a>
        </div>
        <div class="p-navig">
            <div id="_jp-chose">
                <dl class="p-select" id="jp-select">
                    <input id="jp-cours" type="hidden" value="" name="courseware">
                    <dt id="_dt" data-id=""></dt>
                    <dd id="jp-chose1">
                        <c:forEach items="${cList}" var="c">
                            <a href="/teacher/classRoom/previewDetail?classRoomId=${classRoomId}&classesId=${c.id}" data-id="${c.id}"
                               <c:if test="${classesId == c.id }">class="cur"</c:if> id="full_${c.id}" >${c.fullName}</a>
                        </c:forEach>
                    </dd>
                </dl>
            </div>
            <a class="p-homeback" title="返回" href="/teacher/classRoom/index"></a>
        </div>
        <div class="p-contvare fc">
            <!-- 左侧 -->
            <div class="p-side fl">
                <ul class="i-sidevare">
                    <li class="cur li1">
                        <a href="javascript:;" class="jp-file-all ">总览</a><em></em>
                    </li>
                    <c:forEach items="${taList}" var="ta" varStatus="i">
                        <li class="li1">
                            <a href="javascript:;"  name="${ta.id}" class="jp-file">${ta.typeStr}</a>
                            <em></em>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!-- 右侧 -->
            <div id="jp-quest" class="p-cont fr">

            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
<script type="text/javascript">

    $(".jp-file-all").on("click",function(){
        $('.li1').removeClass("cur");
        $(this).parent().addClass("cur");
        $.ajax({
            url: "/teacher/classRoom/reader",
            type: "post",
            data: {classesId:${classesId},'classRoomId':${classRoomId}},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
            }
        });
    })

    $(".jp-file").on("click",function(){
        $('.li1').removeClass("cur");
        $(this).parent().addClass("cur");
        $.ajax({
            url: "/teacher/classRoom/reader",
            type: "post",
            data: {'tpExamAttachmentId':this.name,'sType':'detail',classesId:${classesId},'classRoomId':${classRoomId}},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
            }
        });
    })

    $(document).ready(function (){
        $(".jp-file-all").click();
        _firstChoseFun();
    });
    /*初始事件*/
    var _firstChoseFun  = function(){
        var $obj = $('#jp-select'),
                $dt = $obj.find('dt');
        $dt.text($("#full_${classesId}").text());
    }

    $("#jp-chose1").on("mouseleave",function(){
        $(this).removeClass();
        $("#_dt").removeClass();
    })

</script>
