<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--底部按钮-->
<div class="i-foot">
    <div class="i-box fc">
        <form id="jp-qusetion-form">
            <input id="jp-questinpt" type="hidden" value="">
        </form>
        <div class="p-addexam-numb fl">
            试题篮：<em id="jp-tnum">0</em>
            <ul class="p-addexam-box" id="jp-addexam-box">
            </ul>
            <i class="p-addicon"></i>
        </div>
        <div class="fr">
            <a href="javascript:;" class="green-btn orange-btn green-btn-style" id="jp-conserver-btn">生成试题</a>
            <a href="javascript:;" class="green-btn green-btn-style" id="jp-download-btn">预览试题</a>
            <%if(GlobalConstant.PAPER_CREATE_TYPE_SJZT.equals(request.getParameter("createType"))){%>
                <a href="JavaScript:;" class="green-btn green-btn-style" id="jp-addAll" data-type="1">全部加入试题篮</a>
            <%}%>
        </div>
    </div>
</div>