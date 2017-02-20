function queryFirstTag(tag1,tag2){
	var $tag1 = $("#"+tag1);
	var $tag2 = $("#"+tag2);
	$tag2.val('');
	queryTag('header_tag1','header_tag2');
	queryTag('header_tag2','header_tag3');
	//标签切换去除questionType历史
	$("#questionTypeId").val(0);
	questionTypeMode();
	
}
function queryTagTwo(tag2,tag3){
	queryTag(tag2,tag3);
	//标签切换去除questionType历史
	$("#questionTypeId").val(0);
	questionTypeMode();
	
}
function questionTypeMode(){
	var $questionTypeTags = $("#questionTypeTags");
	var t = $("#header_tag3").val();
	var questionTypeId = $("#questionTypeId").val();
	$.ajax({
		url : "/question/base/getQuestionTypes",
		type : 'post',
		data : {tagId: t ,questionTypeId:questionTypeId},
		async : false,
		cache : false,
		//dataType : 'json',
		success : function (data) {
			$questionTypeTags.empty().append(data);
			$("#questionTypeTags span.current").click();
		},
		error : function () {
			alert("查询失败");
		}
	});
	
}
function queryTag(tag1,tag2){
	var $tag1 = $("#"+tag1);
	var $tag2 = $("#"+tag2);
	var questionTypeId = $("#questionTypeId").val();
	$.ajax({
		url : "/question/base/questionTag",
		type : 'post',
		data : {tagId: $tag1.val() },
		async : false,
		cache : false,
		//dataType : 'json',
		success : function (data) {
			$tag2.empty().append(data);
		},
		error : function () {
			alert("查询失败");
		}
	});
}
function goNew(typeId){
	//fillShareStatus();
	$("#questionTypeTags span").removeClass("current");
	$("#t_"+typeId).addClass("current");
	$("#questionTypeId").val(typeId);
	var tag1 = $("#header_tag1").val();
	var tag2 = $("#header_tag2").val();
	var tag3 = $("#header_tag3").val();
	tag1==null?"":tag1;
	tag2==null?"":tag2;
	tag3==null?"":tag3;
	var shareStatus = $("#header_shareStatus").val();
	document.location.href="/question/base/toAddIndex?questionTypeId="+typeId+"&tag1="+tag1+"&tag2="+tag2+"&tag3="+tag3+"&shareStatus="+shareStatus;
}
///题目页头编辑-end