<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.login.entity.UserEntity" %>
<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ page import="com.koolearn.sso.util.CookieUtil" %>

<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
<link href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<%--<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
--%><link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/zw.css" />
<%--<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/cal.css" />--%>
<%--<script type="text/javascript" src="${examctx}/examcore/js-exam/zw-q.js"></script>--%>
<%--<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>--%>
<%--<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>--%>
<%--<script type="text/javascript" src="${examctx}/examcore/js-exam/rangy-core.js"></script>--%>
<%--<script type="text/javascript" src="${examctx}/examcore/errornote.js"></script>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String staticDomain= PropertiesConfigUtils.getProperty(GlobalConstant.EXAM_STATIC_PAGECSS);
    staticDomain= StringUtils.isBlank(staticDomain)?"":staticDomain;
    request.setAttribute("examctx",staticDomain+request.getContextPath());
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
    function soundUploadCallback(id,val){ // 口语题回调函数
        // 回调
        var valArr = val.split(",");
        if(valArr[0] == "0"){
            // 提交失败
            //pop_alert("温馨提示", "录音提交失败，请重新提交！");
            alert("录音提交失败，请重新提交！");
        }else{
            // 提交成功
            //pop_alert("温馨提示", "录音提交成功！");
            alert("录音提交成功！");
        }
    };
    function showMessage(message){
        alert(message);
    }
</script>
<%
    Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    String authId = authCookie.getValue();
    UserEntity ue = CacheTools.getCache(authId, UserEntity.class);
//	UserEntity ue = (UserEntity)request.getSession().getAttribute("loginUser");;
%>
<input type="hidden" id="userType" value="<%=ue.getType()%>"/>
<script type="text/javascript">
        function initStyle(){
            if($('.jp-kldge')){
                $('.p-kldge').css({
                    'background':'#f5f5f5',
                    'border-bottom': '4px solid #e9e9e9',
                    'padding':'10px 30px'
                });
                $('.st').css({
                    'border-bottom':0
                });
                $('.jp-answer').css({
                    'padding':'10px 30px 0 30px'
                });
            }
            parseShowInStudent();
        }
    function parseShowInStudent(){
        //控制模版内只在学生端显示的模块隐藏，默认显示
        var teacherType=$("#userType").val();
        if('1'==teacherType){
          $(".show_in_stu").hide();//如果是老师则隐藏
          $("textarea.dhl_xz").attr("disabled","disabled");
          $(".dhl_fill").attr("disabled","disabled");
          $(".dhl_dx").attr("disabled","disabled");
          $(".dhl_ddx").attr("disabled","disabled");
        }
    }
$(function(){
    //题目解析展示隐藏
    $("body").on("click",".jp-parsi",function(){
        var showObj = $(this).closest("[questiontypesx]").find(".oppClass");
        if(showObj.is(":hidden")){
            showObj.css("display","block");
        }else{
            showObj.hide();
        }
    });
    // 含有小题的解析
    $("body").on("click",".jp-parsi-min",function(){
        var showObj = $(this).parents(".jp-little-quest").prev(".jp-answer-min");
        if(showObj.is(":hidden")){
            showObj.css("display","block");
        }else{
            showObj.hide();
        }
    });
});

</script>