<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="题库-试卷组题" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-paper/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
 <body>
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="tk"/>
    </jsp:include>
    <div class="i-main fc">
        <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
            <jsp:param name="nav" value="<%=GlobalConstant.PAPER_CREATE_TYPE_SJZT%>"/>
            <jsp:param name="createFrom" value="${questionFilter.createFrom}"/>
        </jsp:include>
        <div class="screen">
            <dl class="jp-tab screen_dl">
                <dt>地区</dt>
                <dd  class="p-downdd jp-downdd">
                    <span class="jp-downbtn"></span>
                    <a href="#" class="jp-tab-link white-btn-tab on">全部</a>
                    <c:forEach items="${area}" var="tag"  varStatus="status">
                        <a href="javascript:;" id="${tag.id}" class="jp-tab-link white-btn-tab">${tag.name}</a>
                    </c:forEach>
                </dd>
                <dt>年份</dt>
                <dd class="p-downdd jp-downdd">
                    <span class="jp-downbtn"></span>
                    <a href="#" class="jp-tab-link white-btn-tab on">全部</a>
                    <c:forEach items="${year}" var="tag"  varStatus="status">
                        <a href="javascript:;" id="${tag.id}" class="jp-tab-link white-btn-tab">${tag.name}</a>
                    </c:forEach>
                </dd>
                <dt>试卷类型</dt>
                <dd>
                    <a href="#" class="jp-tab-link white-btn-tab on">全部</a>
                    <c:forEach items="${paperType}" var="tag"  varStatus="status">
                        <a href="javascript:;" id="${tag.id}" class="jp-tab-link white-btn-tab">${tag.name}</a>
                    </c:forEach>
                </dd>
                <dt>适用年级</dt>
                <dd>
                    <a href="javascript:;"  id="<%=GlobalConstant.PAPER_SEARCH_TYPE_XDF_Grade_all%>" class="jp-tab-link white-btn-tab on">全部</a>
                    <c:forEach items="${grade}" var="tag"  varStatus="status">
                        <a href="javascript:;" id="${tag.id}" class="jp-tab-link white-btn-tab">${tag.name}</a>
                    </c:forEach>
                </dd>
                <dt>适用学科</dt>
                <dd>
                    <a href="javascript:;" id="<%=GlobalConstant.PAPER_SEARCH_TYPE_XDF_Subject_all%>" class="jp-tab-link white-btn-tab on">全部</a>
                    <c:forEach items="${subject}" var="tag"  varStatus="status">
                        <a href="javascript:;" id="${tag.id}" class="jp-tab-link white-btn-tab">${tag.name}</a>
                    </c:forEach>
                </dd>
            </dl>
        </div>
        <div id="paperList" >
        </div>
        <form id="paperListForm" enctype="multipart/form-data">
            <fieldset id="formId"></fieldset>
            <input type="hidden" name="searchFrom" id="searchFrom" value="<%=GlobalConstant.PAPER_SEARCH_TYPE_XDF%>"/>
            <input type="hidden" name="pageNo" id="pageNo" value="0"/>
            <input type="hidden" name="navigation"   value="<%=GlobalConstant.PAPER_CREATE_TYPE_SJZT%>"/>
            <input type="hidden" name="createFrom"   value="${questionFilter.createFrom}"/>
        </form>
    </div>
    <!-- 试题栏-->
    <%--<jsp:include page="/WEB-INF/jsp/examcore/questionlib/questionBar.jsp"/>--%>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-tiku-paper/page': 'project/b-ms-cloud/1.x/js/t-tiku-paper/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-tiku-paper/page'],function(app){
            app.init({
                api:{
                }
            });
        });
        searchFenye  =function(pageNo){
            if(!pageNo){
                pageNo=0;
            }
            $("#pageNo").val(pageNo)
            $('#paperListForm').submit();
        }
        $('#paperListForm').submit(function(){
            var options = {
                url : '/teacher/exam/core/findPaperList' ,
                contentType : "multipart/form-data; charset=utf-8",
                type : 'POST',
                success : function(data) {
                    $("#paperList").html(data);
                    $('body,html').animate({ scrollTop: 0 }, 200);
                } ,
                error:function(data){ }

            };
            // 提交表单
            $(this).ajaxSubmit(options);
            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
            return false;
        });
        $("#paperList").on("click",".jion-button",function(){
            var _this=$(this);
            var paperId=$(this).attr("id");
            var yc='移出我的试卷库';
            var jr='加入我的试卷库';
            var text=$(this).text();
            if(text==yc){
                text=jr;
            }else if(text==jr){
                text=yc;
            }
            $.ajax({
                type: "POST",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                url:  '${ctx }/teacher/exam/core/paper/jionMyself?paperId='+paperId,
                success: function(data) {
                    _this.text(text);
                }
            });
        });
    </script>
</fe:html>