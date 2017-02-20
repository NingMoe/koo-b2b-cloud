<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%
    QuestionBarHtml questionBarHtml= (QuestionBarHtml) request.getAttribute("questionBarHtml");

%>
<ul class="fc p-list" id="jp-list">
    <li class="s">试题</li>
    <li>知识点</li>
    <li>分数</li>
</ul>
<div class="p-list-info" id="jp-list-box">
    <!--试题tab-->
        <%out.println(questionBarHtml.getHtmlQuestionNum());%>
    <!--知识点-->
    <div class="p-spot jp-list-info">
        <ul>
            <li class="fc">
                <span class="fl p-spot-name">绝对值</span>
                <span class="p-spot-bl fl"><em style="width: 100%"></em></span>
                <span class="fr">10</span>
            </li>
            <li class="fc">
                <span class="fl p-spot-name">绝对值</span>
                <span class="p-spot-bl fl"><em style="width: 30%"></em></span>
                <span class="fr">10</span>
            </li>
            <li class="fc">
                <span class="fl p-spot-name">绝对值</span>
                <span class="p-spot-bl fl"><em style="width: 50%"></em></span>
                <span class="fr">10</span>
            </li>
            <li class="fc">
                <span class="fl p-spot-name">绝对值</span>
                <span class="p-spot-bl fl"><em style="width: 20%"></em></span>
                <span class="fr">10</span>
            </li>
            <li class="fc">
                <span class="fl p-spot-name">绝对值</span>
                <span class="p-spot-bl fl"><em style="width: 15%"></em></span>
                <span class="fr">10</span>
            </li>
        </ul>
    </div>
    <!--分数-->
    <%out.println(questionBarHtml.getHtmlQuestionScore());%>
</div>