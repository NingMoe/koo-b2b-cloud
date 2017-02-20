<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		
		<title>标签与题目类型关系管理</title>
		
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
		<link rel="stylesheet" type="text/css" href="styles.css">
		-->
		
	</head>
	
	<body>
		<h1>说明</h1>
		<form action="<%=basePath%>mc/sd">
			<p>
				找到对应的标签==》点击【显示】==》点击【关联考试题型】==》选择题型==》点击【确认关联】
			</p>
		</form>
		<hr style="border: 2px solid red;"/>
		<table border="1px solid #000;">
			<thead>
				<tr>
					<th>标签</th>
					<th>题型</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${tags}</td>
					<td>${qts}</td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" id="basePath" name="basePath" value="<%=basePath%>" />
	</body>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examcore/TagsAndQtRela.js"></script>
</html>
