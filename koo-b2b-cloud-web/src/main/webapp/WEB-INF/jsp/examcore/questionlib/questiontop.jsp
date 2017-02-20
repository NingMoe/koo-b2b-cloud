<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="p-navig i-box">
    <div class="fl">
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_JDD.equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/exam/core/question/jdd?createFrom=<%=request.getParameter("createFrom")%>">教学进度点组题</a>
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_ZSD.equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/exam/core/question/zsd?createFrom=<%=request.getParameter("createFrom")%>">知识点组题</a>
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_SJZT.equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/exam/core/question/sjzt?createFrom=<%=request.getParameter("createFrom")%>">试卷组题</a>
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_ZNZT.equals(request.getParameter("nav"))){%>class="cur p-nobdr"<%}%>   href="/teacher/exam/core/question/znzt?createFrom=<%=request.getParameter("createFrom")%>">智能组题</a>
    </div>
    <div class="fr">
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_bxsj.equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/exam/core/question/bxsj?createFrom=<%=request.getParameter("createFrom")%>">本校试卷</a>
        <a <%if(GlobalConstant.PAPER_CREATE_TYPE_wdsj.equals(request.getParameter("nav"))){%>class="cur p-nobdr"<%}%> href="/teacher/exam/core/question/wdsj?createFrom=<%=request.getParameter("createFrom")%>">我的试卷</a>
    </div>
</div>