<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>信息提示页面</title>
  </head>
  <body>
    <form id="early" action="<%=basePath%>${target}"></form>
  </body>
  <script type="text/javascript">
  	alert("${msg}");
  	document.forms[0].submit();
  </script>
</html>
