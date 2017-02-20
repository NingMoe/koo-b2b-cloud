<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<fe:html title="题库-智能组题" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-znzt/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
 <body>
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="tk"/>
    </jsp:include>
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
    <div class="i-main fc">
        <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
            <jsp:param name="nav" value="<%=GlobalConstant.PAPER_CREATE_TYPE_ZNZT%>"/>
            <jsp:param name="createFrom" value="${questionFilter.createFrom}"/>
        </jsp:include>
        <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
        <form id="formId" method='post' action="">
            <input type="hidden" name="navigation"   value="<%=GlobalConstant.PAPER_CREATE_TYPE_ZNZT%>"/>
            <input type="hidden" name="createFrom"   value="${questionFilter.createFrom}"/>
            <div class="jp-select-area select-area">
                <span class="p-sel-box">
                    <label>选择学科</label>
                    <select id="sel-show" class="jp-sel-show" name="subjectId">
                    </select>
                </span>

                <span class="p-sel-box">
                    <label>选择学段</label>
                    <select id="sel-show1" class="jp-sel-show" name="rangeId">
                    </select>
                </span>

                <span class="p-sel-box">
                    <label>选择版本</label>
                    <select id="sel-show2" class="jp-sel-show" name="bookVersion">
                    </select>
                </span>

                <span class="p-sel-box">
                    <label>选择难度</label>
                    <select id="sel-show3" name="questionHard">
                            <c:forEach items="${questionHard}" var="tag"  varStatus="status">
                                <option value="${tag.id}">${tag.name}</option>
                            </c:forEach>
                    </select>
                </span>
            </div>
            <div class="jp-lr-area lr-area">
                <div class="lft">
                    <ul class="jp-lft-tab lft-tab">
                    </ul>
                </div>
                <div class="jp-rgt rgt">
                    <em class="jp-h-em h-em"></em>
                    <div class="jp-checkbox-all checkbox-all"><input type="checkbox" class="jp-checkbox"> <span class="nam">全选</span></div>
                    <div class="jp-dl-box">
                    </div>

                    <div class="choose-over">
                        <em class="em-tit">已选进度点（共<span class="jp-choose-num">0</span>个）</em>
                        <div class="jp-lst-case lst-case"></div>
                    </div>
                </div>
            </div>
            <div class="jp-practice-num practice-num"><!--异步加载题型--></div>
            <input id="jp-question-count" type="hidden" name="questionCount" value="">
            <input id="jp-obligatory" type="hidden" name="obligatoryId">
        </form>

        <div class="btn-area">
            <a href="javascript:;" class="jp-form-submit orange-btn">生成试卷</a>
        </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <%--<span id="ajxSubmitsYz"></span>--%>
 </body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-tiku-znzt/page': 'project/b-ms-cloud/1.x/js/t-tiku-znzt/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-tiku-znzt/page'],function(app){
            app.init({
                api:{
                    subject:'/teacher/resource/getSubject',
                    ranges:'/teacher/resource/getRange',
                    bookVersion:'/teacher/resource/getBookVersion',
                    switchs:'/teacher/resource/getTree',
                    checkLst:'/teacher/resource/getTreeRefChild',
                    questionCount:'/teacher/exam/core/question/znztType',
                    submits:"/teacher/exam/core/question/toCreatePaper",
                    submitsYz:"/teacher/exam/core/question/znyzts"
                }
            });
        });
    </script>
</fe:html>