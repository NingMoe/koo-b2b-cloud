<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!--引入koolearn播放器类库--->
<script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/koo.player.js"></script>
<!-- 引入最新的配置文件，注意后面要加? -->
<script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/lastest.player.config.js?v=1"></script>

<!DOCTYPE html>
<fe:html title="翻转课堂-播放课堂" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-fz-courseware/page.css,
         /project/b-ms-cloud/1.x/css/t-fz-watchClass/page.css">

<fe:seaConfig>
    alias: {
    'project/b-ms-cloud/1.x/js/t-fz-courseware/page': 'project/b-ms-cloud/1.x/js/t-fz-courseware/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-fz-courseware/page', function (init) {
            init();
        });

    </script>

    <style>
        .p-back-home{
            position: absolute;
            right: 40px;
            top: 20px;
            width: 72px;
            height: 28px;
            line-height: 28px;
            text-align: center;
            color: #fff;
            background: #546681;
            border-radius: 15px;
        }


    </style>

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
            <p>开始时间：<fmt:formatDate value='${tpExam.startTime}' pattern='yyyy-MM-dd HH:mm'/>
                ／ 截止时间：<fmt:formatDate value='${tpExam.endTime}' pattern='yyyy-MM-dd HH:mm'/>
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
            <a  style="float: right; padding-right: 100Px;"><h2>${classesName}</h2></a>
            <a class="p-back-home" href="/teacher/classRoom/previewDetail?classRoomId=${classRoomId}&classesId=${classesId}">返回课堂</a>
        </div>
        <div class="p-contvare fc">
            <!-- 左侧 -->
            <div class="p-side fl">
                <ul class="i-sidevare">
                    <c:forEach items="${taList}" var="ta" varStatus="i">
                        <li class="li1">
                            <a href="javascript:;" name="${ta.id}" class="jp-file">${ta.typeStr}</a>
                            <em></em>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!-- 右侧 -->
            <div id="jp-quest" class="p-cont p-pvm fr">

            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
    <!-- 弹层 讨论-->
    <div class="p-pop-exam" id="jp-pop-exam" style="display: none;">
        <div class="p-popadd-page">
            <div class="p-addpop-box">
                <button href="javascript:;" class="p-close" id="jp-close">×</button>
            </div>
            <ul class="p-issue-cont">

            </ul>
        </div>
    </div>

</fe:html>
<script type="text/javascript">

    function findComment(id){
        $.ajax({
            url: "/teacher/classRoom/findComment",
            type: "post",
            data: {'tpExamAttachmentId': id},
            success: function (data) {
                $(".p-issue-cont").html("");
                $(".p-issue-cont").html(data);
                $("#jp-pop-exam").css('display','block');
            }
        });
    }

    $("#jp-close").on("click",function(){
        $("#jp-pop-exam").css('display','none');
        $(".p-issue-cont").html("");
    })


    $(".jp-file").on("click", function () {
        $("#jp-close").click();
        $('.li1').removeClass("cur");
        $(this).parent().addClass("cur");
        $.ajax({
            url: "/teacher/classRoom/reader",
            type: "post",
            data: {'tpExamAttachmentId': this.name, 'sType': 'player',classesId:${classesId},classRoomId:${classRoomId}},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
            }
        });
    })

    $(document).ready(function () {
        $(".li1").children().first().click();
    });

</script>
