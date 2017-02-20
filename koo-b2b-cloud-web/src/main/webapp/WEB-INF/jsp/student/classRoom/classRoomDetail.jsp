<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fe:html title="课堂" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-classrm-play/page.css">
    <!--引入koolearn播放器类库--->
    <script type="text/javascript" src="http://static.koolearn.com/kooplayer_new/js/koo.player.js"></script>
    <!-- 引入最新的配置文件，注意后面要加? -->
    <script type="text/javascript"
            src="http://static.koolearn.com/kooplayer_new/js/lastest.player.config.js?v=1"></script>
<fe:seaConfig>
    alias: {
    'project/b-ms-cloud/1.x/js/s-classrm-play/page': 'project/b-ms-cloud/1.x/js/s-classrm-play/page.js'
    }
</fe:seaConfig>
    <fe:seaConfig>
        alias: {
        'project/b-ms-cloud/1.x/js/t-kt-ktzy/page': 'project/b-ms-cloud/1.x/js/t-kt-ktzy/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/s-classrm-play/page', function (init) {
            init();
        });

        seajs.use('project/b-ms-cloud/1.x/js/t-kt-ktzy/page', function (exports) {
            exports.init({
                sendTextUrl: './../json/t-hw-workreview/sendText.php'
            });
        });
    </script>

    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="fzkt"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-classrm">
        <h2 class="p-title">
                <%-- <select id="" name="" onchange="_select(value)">
                     <c:forEach items="${tpExamAttachmentList}" var="t">
                         <option value="${t.id}">${t.typeStr}</option>
                 </select>--%>
            <c:if test="${!empty tpExamAttachmentList}">
                <dl class="p-select" id="jp-select">
                    <input id="jp-cours" type="hidden" value="" name="courseware">
                    <dt id="_dt" data-id=""></dt>
                    <dd id="jp-chose">
                        <c:forEach items="${tpExamAttachmentList}" var="t" begin="0">
                            <a href="javascript:;" onclick="_select(${t.id})" data-id="${t.id}">${t.typeStr}</a>
                        </c:forEach>
                    </dd>
                </dl>
                &nbsp;&nbsp;

                <a id="_name">${tpExamAttachment.attachmentName}</a>
            <span class="p-btns">
               <a class="white-btn-gry bg" id="jp-up" href="javascript:;">上一节</a>
               <a class="white-btn-gry bg" id="jp-down" href="javascript:;">下一节</a>
            </span>
            </c:if>
            <a href="/student/classRoom/index?subjectId=${subjectId}&pageNo=${pageNo}&searchValue=${searchValue}&endTimeStr=${endTimeStr}"
               title="返回" class="p-homeback"></a>

        </h2>
        <!-- 内容 -->
        <div class="jp-file" id="jp-quest" style="overflow: hidden">
            <c:if test="${empty tpExamAttachmentList}">
                <div class="i-no-result">课堂没有内容，请与任课老师联系</div>
            </c:if>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
    <input type="hidden" name="_tpExamAttachmentId" id="_tpExamAttachmentId" value="${tpExamAttachmentId}">
</fe:html>

<script>


    $(document).ready(function () {
        //初始判断是否显示上一节、下一节
        var $obj = $('#jp-chose').find('a.cur'),
                $prev = $obj.prev(),
                $next = $obj.next(),
                $upBtn = $('#jp-up'),
                $downBtn = $('#jp-down');

        if ($('#jp-chose').find('a').length == 1) {
            $upBtn.hide();
            $downBtn.hide();
        } else {
            if ($prev.length == 0) {
                $upBtn.hide();
                $downBtn.show();
            } else if ($next.length == 0) {
                $upBtn.show();
                $downBtn.hide();
            } else {
                $upBtn.show();
                $downBtn.show();
            }
        }


        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId":${tpExamAttachment.id}, "tpExamId":${tpExamId}},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());
            }
        });
    });

    function _select(id) {
        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId": id, "tpExamId":${tpExamId}},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());

            }
        });
    }

    $("#jp-chose").on("mouseleave", function () {
        $(this).removeClass();
        $("#_dt").removeClass();
    })

    $("#jp-up").on("click", function () {
        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId": $("#_tpExamAttachmentId").val(), "tpExamId":${tpExamId}, "submitType": "pre"},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());

            }
        });
    })

    $("#jp-down").on("click", function () {
        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId": $("#_tpExamAttachmentId").val(), "tpExamId":${tpExamId}, "submitType": "next"},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());
            }
        });
    })

    /*上一节*/
    var upBtnClick = function () {
        var $dt = $('#jp-select dt'),
                $dd = $('#jp-select dd'),
                curA = $dd.find('.cur'),
                curAId = curA.attr('data-id'),
                curAprev = curA.prev(),
                curAprevId = curAprev.attr('data-id'),
                $hideInput = $('#jp-cours');

        if (curAprev.length != 0) {
            curA.removeClass('cur');
            curAprev.addClass('cur');
            $dt.attr('data-id', curAprevId);
            $dt.text(curAprev.text());
            $hideInput.val(curAprevId);
        } else {
            $('#jp-up').hide();
            $('#jp-down').show();
        }
    };
    /*下一节*/
    var downBtnClick = function () {
        var $dt = $('#jp-select dt'),
                $dd = $('#jp-select dd'),
                curA = $dd.find('.cur'),
                curAId = curA.attr('data-id'),
                curAnext = curA.next(),
                curAnextId = curAnext.attr('data-id'),
                $hideInput = $('#jp-cours');

        if (curAnext.length != 0) {
            curAnext.addClass('cur');
            curA.removeClass('cur');
            $dt.attr('data-id', curAnextId);
            $dt.text(curAnext.text());
            $hideInput.val(curAnextId);
        } else {
            $('#jp-up').show();
            $('#jp-down').hide();
        }
    }


</script>
