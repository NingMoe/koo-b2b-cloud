<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%><jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<base target="_self" />
<meta charset=utf-8 />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>改错题前台_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/cal.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/zw.css" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
<script type="text/javascript" src="${examctx}/examcore/js-exam/rangy-core.js"></script>
<script type="text/javascript" src="${examctx}/examcore/js-exam/zw-q.js"></script>
</head>
<body style="width:780px;margin:0 auto;">
    <%
    Integer index=(Integer)request.getAttribute("index");
    if(index==null){
        index=0;
    }
    IExamQuestionDto obj=(IExamQuestionDto)request.getAttribute("dto");
    QuestionViewDto questionViewDto = (QuestionViewDto)request.getAttribute("questionViewDto");
    questionViewDto = QuestionUtil.getSubQuestionViewDto(obj,questionViewDto);
    out.println(TemplateFtl.outHtml((IExamQuestionDto)obj, questionViewDto));	  
    %>
    <%-- 
<div class="wp4 wp4-1">
    <div class="tit1 pd1">
        <div class="ft5 mb20"><strong>${dto.questionDto.question.questionTip}</strong> <br />
    <div class="ZW" id="topic">${dto.complexQuestion.topic}</div>
        <input type="hidden" id="ZW_Content" title="保存修改后的批改内容" />
        <p class="ft3 ft4">请点击要修改的单词，修改或删除</p>
        </div>
    </div>
    <div class="mb20">
        <dl class="st">
        <c:forEach items="${dto.correctionQuestionDtos}" var="cq" varStatus="sta">
            <dt class="nobold"><span class="num">${sta.count}</span><div class="ZW ZwEdit">${cq.correctionQuestion.clause}</div></dt>
        </c:forEach>
        </dl>
    </div>
</div>
<!--添加修改标注-->
<div class="bztit bztit2" id="setBZ">
    <div class="bzticon">
    </div>
    <div class="bztitbx">
    <table class="tab3__zw mb10">
        <tr>
            <td>
                <input type="radio" name="radioBZ" id="radio1_1">
                <label for="radio1_1" data-tit="修改">
                        修改为
                </label>
                <input type="text" class="ipt editipt" style="width:200px" />
            </td>
        </tr>
        <tr>
            <td>
                <input type="radio" name="radioBZ" id="radio1_2">
                <label for="radio1_2" data-tit="删除">
                     删除
                </label>
            </td>
        </tr>
    </table>
        <div class="ta_r">
            <span class="btn1 btn1_l"><a href="javascript:void(0)">确 定</a></span>
            <span class="btn1 btn1_b"><a href="javascript:void(0)">取 消</a></span>
        </div>
    </div>
</div>
<!--end添加修改标注-->
    --%>

</body>
</html>