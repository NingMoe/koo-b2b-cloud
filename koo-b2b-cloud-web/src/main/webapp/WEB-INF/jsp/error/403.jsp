<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<fe:html title="403" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-kt-list/page.css">
    <body>
    <div class="i-errorbox">
        <div class="i-error-img"></div>
        <div class="i-error-txt">
            <p class="i-txt1">403</p>
            <p class="i-txt2">拒绝访问!</p>
        </div>
    </div>
    </body>
</fe:html>


