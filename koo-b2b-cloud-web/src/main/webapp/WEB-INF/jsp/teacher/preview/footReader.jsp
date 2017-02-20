<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--//下载暂时屏蔽--%>
<%--<a id="jp-downl" class="p-downl" href="/teacher/resource/preview/download/${resource.id}" ><i class="ico"></i>下载</a>--%>
<a id="jp-colle" class="p-colle <c:if test="${resource.collection == true}">cur</c:if> " href="javascript:;" onclick="collection(${resource.id})"><i class="ico"></i>收藏</a>

<script>
    function collection(sourceId) {
        setTimeout(function(){
            $.ajax({
                url: "/teacher/resource/collection/"+sourceId,    //请求的url地址
                dataType: "json",   //返回格式为json
                async: true,//请求是否异步，默认为异步，这也是ajax重要特性
                //  data: {"id": sourceId},    //参数值
                type: "post",   //请求方式
                success: function (data) {

                },
                error: function () {
                    window._hintPop.hintFun("操作失败!")
                }
            });
        }, 1000);

    }

</script>