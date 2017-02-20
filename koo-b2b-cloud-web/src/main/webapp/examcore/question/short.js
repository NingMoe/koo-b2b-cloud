

function findIdCode(){
	var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
	str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
	return str;
}

/*简答题表单提交校验.type=0为普通的添加简答题。type=1为普通的修改简答题。type=2为新增版本*/
function saveShort(type){
	//document.shortForm.target="_self";
	var answerreference = ckAnswerreference();
	var markType = ckMarkType();
	//var code = ckCode();
	var topic = ckTopic();
	var answer=ckAnswer();
	//if(code && markType  && answerreference && topic && answer){		
	if(markType  && answerreference && topic && answer){		
		if(type == 0 || type == 2){
			document.shortForm.action = '/question/base/shortQuestion/insert?saveType='+type;
		}else if(type == 1){
			document.shortForm.action = '/question/base/shortQuestion/modify';
		}
		popAlert("保存成功！",saveShortSubmit);		
		return true;	
	}else{
		return false;
	}	
}
function saveShortSubmit(){

	//标签赋值
	fillHeadValue();
	
	document.shortForm.submit();
}
/*写作题表单提交校验.type=0为普通的添加简答题。type=1为普通的修改简答题。type=2为新增版本*/
function saveWhrite(type){
	//document.whriteForm.target="_self";
	//var code = ckCode();
	var answer=ckAnswer();
	var topic = ckTopic();
	if(topic && answer){		
		if(type == 0 || type == 2){
			document.whriteForm.action = '/question/base/whriteQuestion/insert?saveType='+type;
		}else if(type == 1){
			document.whriteForm.action = '/question/base/whriteQuestion/modify';
		}
		popAlert("保存成功！",saveWhriteSubmit);		
		
		return true;	
	}else{
		return false;
	}	
}
function saveGuideWhrite(type){
	var code = ckCode();
	var answer=ckAnswer();
	var topic = ckTopic();
	var paragraph = ckParagraph();
	if(code && topic && answer && paragraph){		
		if(type == 0 || type == 2){
			document.whriteForm.action = '/question/base/guideWhriteQuestion/insert?saveType='+type;
		}else if(type == 1){
			document.whriteForm.action = '/question/base/guideWhriteQuestion/modify';
		}
		popAlert("保存成功！",saveWhriteSubmit);		
		
		return true;	
	}else{
		return false;
	}	
}
function saveWhriteSubmit(){
	$("#whriteForm").attr("target","");
	
	//标签赋值
	fillHeadValue();
	
	document.whriteForm.submit();
}
/*口语题表单提交校验.type=0为普通的添加简答题。type=1为普通的修改简答题。type=2为新增版本*/
function saveSpoken(type){
	//document.spokenForm.target="_self";
	var recordtime = ckRecordtime();
	var code = ckCode();
	var topic = ckTopic();
	var answer=ckAnswer();
	fillHeadValue();
	if(code  && recordtime && topic && answer){		
		if(type == 0 || type == 2){
			document.spokenForm.action = '/question/base/spokenQuestion/insert?saveType='+type;
		}else if(type == 1){
			document.spokenForm.action = '/question/base/spokenQuestion/modify';
		}
		popAlert("保存成功！",saveSpokenSubmit);	
		return true;	
	}else{
		return false;
	}	
}
function saveSpokenSubmit(){
	document.spokenForm.submit();
}
/*预览简答题*/
function previewShort(){
	//document.shortForm.target="_blank";
	var answerreference = ckAnswerreference();
	var markType = ckMarkType();
	//var code = ckCode();
	var topic = ckTopic();
	if(markType  && answerreference && topic){	
		//document.shortForm.action = '/question/base/shortQuestion/preview';
		//document.shortForm.submit();
		var preview = 'preview';
		window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
		$("#shortForm").attr("target",preview);
		$("#shortForm").attr("action","/question/base/shortQuestion/preview?random="+Math.random());
		$("#shortForm").submit();
		
		$("#shortForm").attr("target","_self");
	}else{
		return false;
	}	
}
/*预览口语题*/
function previewSpoken(){
	//document.spokenForm.target="_blank";
	var code = ckCode();
	var recordtime = ckRecordtime();
	var topic = ckTopic();
	if(code  && recordtime && topic){	
		//document.spokenForm.action = '/question/base/spokenQuestion/preview';
		//document.spokenForm.submit();
		var preview = 'preview';
		window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
		$("#spokenForm").attr("target",preview);
		$("#spokenForm").attr("action","/question/base/spokenQuestion/preview?random="+Math.random());
		$("#spokenForm").submit();
		
		$("#spokenForm").attr("target","");
	}else{
		return false;
	}	
}
/*预览写作题*/
function previewWhrite(){
	//document.whriteForm.target="_blank";
	var code = ckCode();	
	var topic = ckTopic();
	if(code && topic){		
		//document.whriteForm.action = '/question/base/whriteQuestion/preview';
		//document.whriteForm.submit();
		var preview = 'preview';
		window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
		$("#whriteForm").attr("target",preview);
		$("#whriteForm").attr("action","/question/base/whriteQuestion/preview?random="+Math.random());
		$("#whriteForm").submit();
		
		//$("#whriteForm").attr("target","_self");
	}else{
		return false;
	}	
}
/*预览引导作文题*/
function previewGuideWhrite(){
	//document.whriteForm.target="_blank";
	var code = ckCode();	
	var topic = ckTopic();
	var paragraph = ckParagraph();
	if(code && topic && paragraph){		
		//document.whriteForm.action = '/question/base/whriteQuestion/preview';
		//document.whriteForm.submit();
		var preview = 'preview';
		window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
		$("#whriteForm").attr("target",preview);
		$("#whriteForm").attr("action","/question/base/guideWhriteQuestion/preview?random="+Math.random());
		$("#whriteForm").submit();
		
		//$("#whriteForm").attr("target","_self");
	}else{
		return false;
	}	
}
/*校验code*/
function ckCode(){
	/*var isReturn=false;
	var code = document.getElementById("question.code").value;	
	var err=$("input[name='question.code']").parent().find(".error1:eq(0)");
	var err1=$("input[name='question.code']").parent().find(".error1:eq(1)");
	var err2=$("input[name='question.code']").parent().find(".error1:eq(2)");
	var ipt = $("input[name='question.code']");
	if( isBlank(code) || (code == "产品简写+日期+自定义编码")){
		err.show();
		isReturn=true;	
		document.getElementById("question.code").focus();
		$(window).scrollTop(ipt.offset().top);
	}
	else{	
		if(!isCodePattern(code)){
			err2.show();
			isReturn=true;	
			document.getElementById("question.code").focus();
			$(window).scrollTop(ipt.offset().top);
		}else{			
			err.hide();
			err2.hide();
			document.getElementById("questionDto.question.code").value = code;
			$.ajax({
				url: "/question/base/choice/uniqueCode",
				type: "POST",
				cache: false,
				async:false,
				data: findIdCode(),
				success: function(data){
					var d =eval("("+data+")");//转换为json对象 
					if(d && d.result == "true"){
						err1.hide();
						isReturn=false;					
					}else{					
						err1.show();					
						isReturn=true;				
						document.getElementById("question.code").focus();
						$(window).scrollTop(ipt.offset().top);
					}
				},
				error: function(data){
					popAlert("请求失败，请重试！");
					isReturn=true;
				}
			});
		}
	}
	if(isReturn){
		return false;
	}else{
		return true;
	}*/
	return true;
}
function validRadio(){
	var turns = document.getElementsByName("shortQuestion.marktype");
	var trunValue = 0;
	for(var i=0;i<turns.length;i++){
		if   (turns[i].checked) 
		{ 
	 		trunValue = turns[i].value;  
		}   
	}
	return trunValue;
}
/*校验批改方式*/
function ckMarkType(){
	var oEditor = FCKeditorAPI.GetInstance('shortQuestion.scorestandad') ;
	var scorestandad = oEditor.GetXHTML();

	var keyWords = document.getElementById("keyWords");
	var radioValue = validRadio();
	if(radioValue == 1){
		var ipt = $("#scorestandadId");
		if (isBlank(scorestandad))
		{			
			error1(ipt,1);
			oEditor.Focus();
			$(window).scrollTop(ipt.offset().top);
			return false;
		}else{
			hideerror(ipt,1);
			return true;
		}
	}else{
		var ipt = $("#keyWord");
		if (isBlank(keyWords.value) )
		{			
			error1(ipt,1);
			document.getElementById("keyWord").focus();
			$(window).scrollTop(ipt.offset().top);
			return false;
		}else{
			hideerror(ipt,1);
			return true;
		}
	}
}
/*检验前台答题框高度*/
function ckBoxheight(){
	var selectValue = document.getElementById("shortQuestion.boxheight").value;
	if(selectValue == 0){
		var reg =  "^[0-9]*[1-9][0-9]*$";
		var otherValue = document.getElementById("otherHeight").value;
		if(isBlank(otherValue) || !(new RegExp(reg)).test(otherValue)){			
			var ipt = $("#shortQuestion.boxheight");
			error1(ipt,1);
			document.getElementById("otherHeight").focus();
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}
/*检验参考答案*/
function ckAnswerreference(){
	var oEditor = FCKeditorAPI.GetInstance('shortQuestion.answerreference') ;
	var answerreference = oEditor.GetXHTML();
	var ipt = $("#answerreferenceId");
	if (isBlank(answerreference)){		
		error1(ipt,1);
		oEditor.Focus();
		$(window).scrollTop(ipt.offset().top);
		return false;
	}else{
		hideerror(ipt,1);
		return true;
	}
}
/*检验录音时间*/
function ckRecordtime(){
	var recordtime = document.getElementById("spokenQuestion.recordtime").value;
	var err=$("input[name='spokenQuestion.recordtime']").parent().find(".error1:eq(0)");	
	if(!isBlank(recordtime) && (recordtime != "大于0的正数")){
		var reg = "^(([0-9]+[\.]?[0-9]+)|[0-9])$";
		if(!(new RegExp(reg)).test(recordtime)){			
			err.show();
			document.getElementById("spokenQuestion.recordtime").focus();
			var ipt = $("#spokenQuestion.recordtime");
			$(window).scrollTop(ipt.offset().top);
			return false;
		}else{
			err.hide();
			return true;
		}
	}else{
		document.getElementById("spokenQuestion.recordtime").value=0;
		err.hide();
		return true;
	}
}
/*对子试题的编码进行赋值*/
function fectchParentCode1(){
	if(parent.window.dialogArguments){
		var v=parent.window.dialogArguments.document.getElementById("questionDto.question.code").value+"_1";
		document.getElementById("questionDto.question.code").value=v;		
	}	
}
function ckAnswer(){
	var isReturn = true;
	var answer=$('input[name="answer"]');
	var v=answer.val();
	 var err =answer.parent().children(".error1:eq(0)");
	err.hide();
	if(isBlank(v)){
		return isReturn;
	}
	if(lengthLarger(v, 255)){
		 err.show();
		 isReturn = false;
//		 $(window).scrollTop(answer.offset().top);
		 answer.focus();  
	}
	 return isReturn;
}
function ckTopic(){
	var isReturn = false;
	$("textarea[name^='content']").each(function(){
		var id=$(this).attr('id');
		var oEditor=FCKeditorAPI.GetInstance(id);
		var content=oEditor.GetXHTML(true);
		var err = $(this).parent().children(".error1:eq(0)");
		var err1 = $(this).parent().children(".error1:eq(1)");
		if(isBlank(content)){
			err.show();
			isReturn = true;
			alertMsg("题干不能为空!若无需输入题干，请输入两个及以上空格即可");	
			return;
		}
		if(lengthLarger(content, 65535)){
			err1.show();
			isReturn = true;
			alertMsg("题干不能超过65535字符!");	
			return;
		}		
	});
	if(isReturn){
		return false;
	}else{
		fillTextarea_param1();		
		return true;
	}

}
function ckParagraph(){
	var isReturn = false;
	$("input[name='wordnumMin']").each(function(){
		var err = $(this).parent().children(".error1:eq(0)");
		var num = trim($(this).val());
		if(num == ''){
			err.show();
			isReturn = true;
			return;
		}
		if(!isInt(num)){
			err.show();
			isReturn = true;
			return;
		}
	});
	$("input[name='wordnumMax']").each(function(){
		var err = $(this).parent().children(".error1:eq(0)");
		var num = trim($(this).val());
		if(num == ''){
			err.show();
			isReturn = true;
			return;
		}
		if(!isInt(num)){
			err.show();
			isReturn = true;
			return;
		}
	});
	$("input[name='linenum']").each(function(){
		var err = $(this).parent().children(".error1:eq(0)");
		var num = trim($(this).val());
		if(num == ''){
			err.show();
			isReturn = true;
			return;
		}
		if(!isInt(num)){
			err.show();
			isReturn = true;
			return;
		}
	});
	$("input[name='questionTip']").each(function(){
		var err = $(this).parent().children(".error1:eq(0)");
		var num = trim($(this).val());
		if(num == ''){
			err.show();
			isReturn = true;
			return;
		}
	});
	for(var i=0; i<$("input[name='wordnumMin']").length; i++){
		var min = parseInt(trim($("input[name='wordnumMin']:eq("+i+")").val()));
		var max = parseInt(trim($("input[name='wordnumMax']:eq("+i+")").val()));
		var err = $("input[name='wordnumMin']:eq("+i+")").parent().children(".error1:eq(1)");
		if(min >= max){
			err.show();
			isReturn = true;
			return;
		}
	}
	if(isReturn){
		return false;
	}else{
		fillTextarea_param1();		
		return true;
	}
}
function fillTextarea_param1(){
	var j=$('input[name="questionDto.textarea_param"]');
		if(j.size()>0){
			var aa=[];
			$("textarea[name^='content']").each(function(i){
				aa[aa.length]=$(this).attr('name');
			});
			j.val(aa.join());
		}
}

/*隐藏错误*/
function hideerror(ipt,n) {
	if(!ipt)return;
	n = n || 1;
	var error = $(ipt).parent().find(".error1:eq("+(n-1)+")");
	error.hide();
};