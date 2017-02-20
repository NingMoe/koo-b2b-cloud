<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/koo" prefix="koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<!-- 导出题目附件路径 -->
		<base href="<%=basePath%>">
		<title>导出题目附件路径管理</title>
		
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	</head>
	
	<body>
		<h1>说明</h1>
		<form action="">
			<p>
				导出题目附件路径
				<input type="button" value="导出附件地址" id="expFile" />
			</p>
			<table class="manage_tab" border="0" cellspacing="1" cellpadding="0">
					<tr>
						<th width="200"></th>
						<th>试卷</th>
					</tr>
					
					<tr>
						<td class="tab_tile">
							<span></span>
						</td>
						<td class="tab_choose">
						<c:forEach items="${paperList}" var="paper">
								<input type="checkbox" name="testpapers" value="${paper.paperId}"><span>${paper.paperCode}</span>
						</c:forEach>
						</td>
					</tr>
			</table>
		</form>
	</body>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript">
	$("#expFile").one("click",function(){
		subFor();
	})
	function subFor(){
		var num = 0;
		var pIds = "";
		$("input[name='testpapers']").each(function(){
			$("#cc").attr("disabled")
			if($(this).attr("checked")=='checked' && $(this).attr("disabled")!='disabled'){
				pIds += $(this).val()+",";
				num++ ;
			}
		});
		if(num<1){
			alert("请最少勾选一张试卷");
			$("#expFile").one("click",function(){
				subFor();
			})
			return ;
		}
		if(num>20){
			alert("一次导出试卷数量不能大于20，当前数量："+num+" ");
			$("#expFile").one("click",function(){
				subFor();
			})
			return ;
		}
		$.ajax({
			url:'/mc/exportAttachFile',
			type:'post',
			data:{paperIds:pIds},
			dataType:'json',
			async: false,
			success:function(data){
				if(data == '1'){
					$("input[name='testpapers']").each(function(){
						if(this.checked==true){
							$(this).attr("disabled","disabled");
						}
					});
					alert("导出完成");
				}else{
					alert("导出出错了");
				}
				$("#expFile").one("click",function(){
					subFor();
				})
			}
		});
		
	}
	</script>
</html>
