$(function(){
//	var code = $("#code").val();
//	if(isBlank(code)){
//		$("#code").val($("#code").attr("df"));
//	}
//	var cid = $("#cid").val();
//	if(cid && cid !="0"){
//		$("#code").attr("readonly",true);
//	}
});

function checkTopic(){
	var name = $(".fmbx2").find(".ipta").last().attr("id");
	return checkFck(name);
}
function checkFenju(){
	var result = true;
	var ul = $("#zstfm").find("ul");
	if(ul.hasClass("hide2")){
		result = false;
		ul.next("span").show();
	}else{
		ul.next("span").hide();
		$(".fenju_a").each(function(){
			var value = $(this).val();
			var blank = isBlank(value);
			if(blank){
				$(this).parent("div").next("span").show();
				$(this).focus();
			}else{
				$(this).parent("div").next("span").hide();
			}
			result = !blank && result;
		});
	}
	return result;
}
function savest(){
	if(checkRequireds() && checkML() && checkTopic() && checkFenju()){
		$("#saveForm").attr("action","/question/base/correctionQuestion/save");
		$("#saveForm").attr("target","_self");
		
     	fillHeadValue();
		
		$("#saveForm").submit();
	}
}

function preview(){
	if(validate() && checkTopic() && checkFenju()){
		var preview = 'preview';
		window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
		$("#saveForm").attr("target",preview);
		$("#saveForm").attr("action","/question/base/correctionQuestion/preview/1");
		$("#saveForm").submit();
	}
	
}

//插入一个位置
function addfenju(ipta,fm,name){
ordfenju(ipta,fm,name);
}
//重新给位置排序
function ordfenju(ipta,fm,name){
	var html = $("<div />"), tb1, ul = fm.find("ul"), li;
	html.html(ipta.html());
	tb1 = html.find(".tb4");
	ul.addClass("hide2").children("li:gt(0)").remove();
	tb1.each(function(i){
		var v=$(this).text().replace('[','').replace(']','');
		if(v=="")return;
		$(this).val(v);
		li = ul.children("li:last");
		if (i) {
			li.find(".add").click();
			li = ul.children("li:last");
		}
		li.find(".fjct").html(v).next(".fenju").val(v);
		ul.removeClass("hide2");
	});
	rsetfms(1);
//	resetFcktk(fm);
	ul.children("li").each(function(i){
		$(".num", this).html(i+1);
		$(".fenju",this).attr("name","correctionQuestionDtos["+i+"].correctionQuestion.clause");
		$(".fenju_id",this).attr("name","correctionQuestionDtos["+i+"].questionDto.question.id");
		$(".fenju_a",this).attr("name","correctionQuestionDtos["+i+"].correctionQuestion.clauseAnswer");
		$(".fenju_t_id",this).attr("name","correctionQuestionDtos["+i+"].questionDto.question.questionTypeId");
	});
}

//重置fck编辑器中的填空项
function resetFcktk(fm){
	$(fm).find(".tb4").each(function(i){
		$(this).attr("index",i);
	});
}