<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="p-browse">

    <textarea class="p-discuss j-value jp-textarea">输入评论内容</textarea>
    <p>
        <a class="green-btn p-issue-btn j-submit" href="javascript:;">发布</a>
    </p>
    <ul class="p-issue-cont j-ul">
        <c:forEach items="${cList}" var="c">
            <li class="j-li">
                <dl class="p-dlone">
                    <dt class="fc">
                        <span class="fl"><i></i>${c.userName}</span>
                        <a class="fr reply" href="javascript:;" name="${c.userName}" userId="${c.userId}"
                           intId="${c.id}" parentId="${c.id}"><i></i>回复</a>
                    </dt>
                    <dd>
                        <p class="p-txt">
                                ${c.comment}
                        </p>
                        <p class="p-time"><fmt:formatDate value="${c.createTime}"
                                                          pattern="yyyy.MM.dd HH:mm:ss"></fmt:formatDate></p>
                    </dd>
                    <div class="co_${c.id} j-hf">

                    </div>
                </dl>
                <div class="p-divs coo_${c.id}">
                    <c:forEach items="${c.lists}" var="cc">
                        <dl>
                            <dt class="fc">
                                <span class="fl"><b><i></i>${cc.userName}</b> 回复 ${cc.replyUserName}</span>
                                <%--<a class="fr reply" href="javascript:;" name="${cc.userName}" userId="${cc.userId}"--%>
                                   <%--intId="${cc.id}" parentId="${cc.parentId}"><i></i>回复</a>--%>
                            </dt>
                            <dd>
                                <p class="p-txt">
                                        ${cc.comment}
                                </p>
                                <p class="p-time"><fmt:formatDate value="${cc.createTime}"
                                                                  pattern="yyyy.MM.dd HH:mm:ss"></fmt:formatDate></p>
                            </dd>
                            <div class="co_${cc.id} j-hf">

                            </div>
                        </dl>
                    </c:forEach>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<form id="j-from" name="j-from" action="/student/classRoom/saveComment" method="post">
    <input type="hidden" name="examAttachmentId" id="examAttachmentId" value="${tpExamAttachmentId}">
    <input type="hidden" name="comment" id="comment">
    <input type="hidden" name="replyUserId" id="replyUserId">
    <input type="hidden" name="replyUserName" id="replyUserName">
    <input type="hidden" name="parentId" id="parentId">
</form>
<script>

    function getStrLength(str) {
        var cArr = str.match(/[^\x00-\xff]/ig);
        return str.length + (cArr == null ? 0 : cArr.length);
    }

    //发表评论
    textareaFun = function(obj,txt){

        var $obj = $(obj);
        $obj.on('focus',function(){
            var self = $(this),
                    oldTxt = $.trim(self.val());
            if(oldTxt == txt){
                self.val('');
            }
        }).on('blur',function(){
            var self = $(this),
                    oldTxt = $.trim(self.val());
            if(oldTxt == ''){
                self.val(txt);
            }
        });
    }
    $(document).ready(function (){
        textareaFun('.jp-textarea','输入评论内容');
        textareaFun('.jp-textarea-notes','请输入笔记');
    })

    //回复
    $('.j-ul').on('focus','.jp-textarea',function(){
        var self = $(this),
                oldTxt = $.trim(self.val());
        if(oldTxt == "输入回复内容"){
            self.val('');
        }
    }).on('blur','.jp-textarea',function(){
        var self = $(this),
                oldTxt = $.trim(self.val());
        if(oldTxt == ''){
            self.val("输入回复内容");
        }
    });


    //发布
    $(".j-submit").on("click", function () {
        var inputVal = $.trim($(".j-value").val()),
                numb = getStrLength(inputVal);

        if((inputVal)==""){
            window._hintPop.hintFun("评论内容不能为空");
            return false;
        }
        if((inputVal)=="输入评论内容"){
            return false;
        }
        if(2000 < numb || numb < 30){
            window._hintPop.hintFun("评论内容最多1000字，最少15字");
            return false;
        }

        $("#comment").val($.trim($(".j-value").val()));
        $.ajax({
            url: "/student/classRoom/saveComment",
            type: "post",
            dataType: 'json',
            data: $("#j-from").serialize(),
            success: function (data) {
                $(".j-ul").prepend('<li class="j-li"><dl class="p-dlone"><dt class="fc">' +
                        '<span class="fl"><i></i>' + data.userName + '</span><a class="fr reply" href="javascript:;" name="' + data.userName + '" userId="' + data.userId + '" intId="' + data.id + '" parentId="' + data.id + '"><i></i>回复</a>' +
                        '</dt><dd><p class="p-txt">' + data.comment + '</p>' +
                        '<p class="p-time">' + data.createTimeStr + '</p></dd>' +
                        '<div class="co_' + data.id + ' j-hf"></div></dl>' +
                        '<div class="p-divs coo_' + data.id + '"></div></li>');
                $(".j-value").val("输入评论内容");
            }, error: function () {
                 window._hintPop.hintFun("发布失败请稍后再试!")
            }
        });

    })

    //回复
    $(".j-ul").on("click", ".reply", function () {
        $(".j-hf").children().remove();
        var name = this.name;
        var parentId = $(this).attr("parentId");
        var intId = $(this).attr("intId");
        var userId = $(this).attr("userId");
        $("#replyUserId").val(userId);
        $("#replyUserName").val(name);
        $("#parentId").val(parentId);
        $('.co_' + intId).append('<p>回复: ' + name + '</p>'
                + '<textarea class="p-discuss jp-textarea" id="comment_' + intId + '">输入回复内容</textarea><p>'
                + '<a class="green-btn p-issue-btn j-close" href="javascript:;">取消</a> '
                + '<a class="green-btn p-issue-btn j-commit" name="' + intId + '" href="javascript:;">发布回复</a></p>');
    });


    //取消
    $(".j-ul").on("click", ".j-close", function () {
        $(".j-hf").children().remove();
    });


    //发布回复
    $(".j-ul").on("click", ".j-commit", function () {

        var inputVal = $.trim($("#comment_" + this.name).val()),
                numb = getStrLength(inputVal);

        if((inputVal)==""){
            window._hintPop.hintFun("回复内容不能为空");
            return false;
        }
        if((inputVal)=="输入回复内容"){
            return false;
        }
        if(2000 < numb || numb < 30){
            window._hintPop.hintFun("评论内容最多1000字，最少15字");
            return false;
        }

        $("#comment").val($.trim($("#comment_" + this.name).val()));
        $(".j-hf").children().remove();
        var parentId = $("#parentId").val();
        $.ajax({
            url: "/student/classRoom/saveComment",
            type: "post",
            dataType: 'json',
            data: $("#j-from").serialize(),
            success: function (data) {
                $('.coo_' + parentId).prepend('<dl><dt class="fc"><span class="fl"><b><i></i>' +
                        data.userName + '</b> 回复 ' + data.replyUserName + '</span><a class="fr reply" href="javascript:;" name="' + data.userName + '" userId="' + data.userId + '" intId="' + data.id + '" parentId="' + data.parentId + '"></a>' +
                        '</dt><dd><p class="p-txt">' + data.comment + '</p>' +
                        '<p class="p-time">' + data.createTimeStr + '</p></dd>' +
                        '<div class="co_' + data.id + ' j-hf"></div></dl>');
            }, error: function () {
                 window._hintPop.hintFun("发布回复失败请稍后再试!")
            }
        });

    });


</script>
