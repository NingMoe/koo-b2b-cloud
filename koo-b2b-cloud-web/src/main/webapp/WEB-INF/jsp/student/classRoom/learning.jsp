<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<div class="p-tabs fc" id="jp-spans1">
    <span class="second-item-title fl item-selected">当前学习人员</span>
    <span class="first-item-title fl ">笔记</span>
</div>
<div id="jp-divs">
    <ul class="p-public p-person show">
        <c:forEach items="${userList}" var="user">
            <li>
                <c:if test="${empty user.imageUrl}"><img src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/i/u-def.png"></c:if>
                <c:if test="${not empty user.imageUrl}"><img src="${user.imageUrl}"></c:if>
                <span title="${user.name}">${user.name}</span>
            </li>
        </c:forEach>
    </ul>
    <div class="p-public p-notes">
        <textarea id="note" class="jp-textarea-notes">请输入笔记</textarea>
        <a href="javascript:;" id="saveNote"><i>+</i>输入笔记</a>
        <ul id="_ul" class="p-notes-ul">
            <c:forEach items="${noteList}" var="note">
                <li>
                    <p class="p-txt">${note.comment}</p>
                    <p class="p-time"><fmt:formatDate value="${note.createTime}"
                                       pattern="yyyy.MM.dd HH:mm:ss"></fmt:formatDate></p>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<script>

    window.setInterval(showUser, 60000);
    function showUser() {
        $.ajax({
            url: "/student/classRoom/findLearning",
            type: "post",
            dataType: "json",
            data: {"tpExamAttachmentId":${tpExamAttachment.id}},
            success: function (data) {
                $(".p-person").html("");
                $.each(data, function (i, n) {
                    var imageUrl = n.imageUrl;
                    if (imageUrl == null) {
                        imageUrl = "";
                    }
                    $(".p-person").append(' <li><img src="' + imageUrl + '"><span title="' + n.name + '">' + n.name + '</span></li>');
                })
            }
        });
    }

    $('#jp-spans1').on("click", 'span', function () {
        var self = $(this);
        self.addClass('item-selected').siblings().removeClass('item-selected');
        $('#jp-divs .p-public').eq(self.index()).addClass('show').siblings().removeClass('show');
    })

    //保存笔记
    $("#saveNote").on("click", function () {
        if ($("#note").val() == "") {
            return false;
        }
        if ($("#note").val() == "请输入笔记") {
            return false;
        }
        if ($("#note").val().length >1000) {
             window._hintPop.hintFun("笔记最多1000字");
            return false;
        }
        $.ajax({
            url: "/student/classRoom/saveNote",
            type: "post",
            dataType: "json",
            data: {"tpExamAttachmentId":${tpExamAttachment.id}, "note": $("#note").val()},
            success: function (data) {
                $("#_ul").prepend(' <li><p class="p-txt">' + data.comment + '</p><p class="p-time">' + data.createTimeStr + '</p></li>')
                $("#note").val("请输入笔记");
            }
        });
    })


</script>