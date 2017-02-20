<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<input type="hidden" id="_attachmentName" value="${tpExamAttachment.attachmentName}">
<%--<span class="p-btns">
   <a class="white-btn-gry" id="jp-up" href="javascript:;">上一节</a>
   <a class="white-btn-gry bg" id="jp-down" href="javascript:;">下一节</a>
</span>
<script>
    $("#jp-up").on("click",function(){
        upBtnClick();
        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId":${tpExamAttachmentId},"tpExamId":${tpExamId},"submitType":"pre"},
            success: function (data) {
                console.log(data);
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());

            }
        });
    })

    $("#jp-down").on("click",function(){
        downBtnClick();
        $.ajax({
            url: "/student/classRoom/reader",
            type: "post",
            data: {"tpExamAttachmentId":${tpExamAttachmentId},"tpExamId":${tpExamId},"submitType":"next"},
            success: function (data) {
                $("#jp-quest").html("");
                $("#jp-quest").html(data);
                $("#_name").html($("#_attachmentName").val());

            }
        });
    })

    /*上一节*/
    var upBtnClick = function(){
        var $dt = $('#jp-select dt'),
            $dd = $('#jp-select dd'),
            curA = $dd.find('.cur'),
            curAId = curA.attr('data-id'),
            curAprev = curA.prev(),
            curAprevId = curAprev.attr('data-id'),
            $hideInput = $('#jp-cours');

        if(curAprev.length != 0){
            curA.removeClass('cur');
            curAprev.addClass('cur');
            $dt.attr('data-id',curAprevId);
            $dt.text(curAprev.text());
            $hideInput.val(curAprevId);
        }else{
            $('#jp-up').hide();

        }
    };
    /*下一节*/
    var downBtnClick = function(){
        var $dt = $('#jp-select dt'),
            $dd = $('#jp-select dd'),
            curA = $dd.find('.cur'),
            curAId = curA.attr('data-id'),
            curAnext = curA.next(),
            curAnextId = curAnext.attr('data-id'),
            $hideInput = $('#jp-cours');

        if(curAnext.length != 0){
            curA.removeClass('cur');
            curAnext.addClass('cur');
            $dt.attr('data-id',curAnextId);
            $dt.text(curAnext.text());
            $hideInput.val(curAnextId);
        }else{
            $('#jp-down').hide();
        }
    }

    $(document).ready(function (){
        var $dd = $('#jp-select dd'),
                curA = $dd.find('.cur'),
                curAprev = curA.prev();

        if(curAprev.length == 0){
            $('#jp-up').hide();
        }

        var $dd = $('#jp-select dd'),
                curA = $dd.find('.cur'),
                curAnext = curA.next();

        if(curAnext.length == 0){
            $('#jp-down').hide();
        }
    })--%>
<script>
    $("#_tpExamAttachmentId").val(${tpExamAttachmentId});
</script>