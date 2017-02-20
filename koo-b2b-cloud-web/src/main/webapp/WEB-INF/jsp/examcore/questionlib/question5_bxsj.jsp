<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="题库-本校试卷库" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-xbsj/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
<body>
<form id="paperListForm" enctype="multipart/form-data">
    <input type="hidden" name="searchFrom" id="searchFrom" value="<%=GlobalConstant.PAPER_SEARCH_TYPE_BXSJ%>"/>
    <input type="hidden" name="pageNo" id="pageNo" value="0"/>
<!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="tk"/>
    </jsp:include>
    <div class="i-main fc">
        <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
        <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
            <jsp:param name="nav" value="<%=GlobalConstant.PAPER_CREATE_TYPE_bxsj%>"/>
        </jsp:include>
        <div class="p-filter">
            <%--<form id="formId">--%>
                <select id="sel-show" class="jp-sel-show" name="subjectId">
                </select>
                <select id="sel-show1" class="jp-sel-show" name="rangeId" >
                </select>
                <div class ="p-rgt">
                    <span class="">创建人: </span>
                    <input type="text" value="" name="userName" class="p-input-text" />
                    <span class="">关键词: </span>
                    <input type="text" value="" name="searchText" class="p-input-text" />
                    <a href="javascript:;" class="jp-submit green-btn">搜索</a>
                </div>
            <%--</form>--%>
        </div>
        <div id="paperList" ></div>
    </div>
</form>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-tiku-xbsj/page': 'project/b-ms-cloud/1.x/js/t-tiku-xbsj/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-tiku-xbsj/page'],function(app){
            app.init({
                api:{
                    subject:'/teacher/resource/getSubject',
                    linkage:'/teacher/resource/getRange',
                    downloadUrl:'/exam/core/downloadPaperPDF'
                }
            });
        });
        serachFenye=function(pageNo){
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
                    window._ifhtml && window._ifhtml.ifhtmlheight();
                    $('body,html').animate({ scrollTop: 0 }, 200);
                } ,
                error:function(data){ }

            };
            // 提交表单
            $(this).ajaxSubmit(options);
            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
            return false;
        });
//        $(document).ready(function(){
//            serachFenye(0);
//        });
    </script>
</fe:html>