<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<HTML>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'zhUpdate' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="/js/zTree_v3/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="/js/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
	<script type="text/javascript">
	var tempNodeName = "";
			function zTreeOnRename(event, treeId, treeNode, isCancel){
				var path = "";
				if(treeNode.type=="version"){
					path = treeNode.getParentNode().name;
					path = path+"#A#"+treeNode.name;
					path = path+"#A#"+tempNodeName;
				}else if(treeNode.type=="materials"){
					path = treeNode.getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().name;
					path = path+"#A#"+treeNode.name;
					path = path+"#A#"+tempNodeName;
				}else if(treeNode.type=="term"){
					path = treeNode.getParentNode().getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().name;
					path = path+"#A#"+treeNode.name;
					path = path+"#A#"+tempNodeName;
				}else if(treeNode.type=="unit"){
					path = treeNode.getParentNode().getParentNode().getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().getParentNode().name;
					path = path+"#A#"+treeNode.getParentNode().name;
					path = path+"#A#"+treeNode.name;
					path = path+"#A#"+tempNodeName;
				}
				var pm = {
					path : path,
					subject:"${param.subject}",
					type:treeNode.type
				};
				$.ajax({
					url:"/updateTree",
					data:$.param(pm),
					type:'post',
					dataType:'json',
					success:function(r){
						if(!r){
							alert("更新失败!");
						}
					}
				});
			}
			function beforeEditName(treeId, treeNode){
				tempNodeName = treeNode.name;
				if(!treeNode.id){
					alert("当前节点不允许修改！");
					return false;
				}
			}
			var setting = {
				edit: {
					enable: true,
					renameTitle: "修改",
					showRenameBtn:true,
					showRemoveBtn:false
				},
				data: {
						simpleData: {
							enable: true
						}
				},
				async: {
						enable: true,
						url:"/findTree",
						otherParam:{"subject":"${param.subject}"}
				},
				callback : {
					beforeEditName: beforeEditName,
					onRename: zTreeOnRename
				}
			};
		$(function(){
			$.fn.zTree.init($("#treeDemo"), setting);
		});
		
	</script>
  </head>
  <body>
  <ul id="treeDemo" class="ztree" style="width:260px; overflow:auto;"></ul>
  </body>
</html>
