function popUrl(title_,url_){
	var rc_pop = new Popup({
		contentType : 1,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 780,
		height : 260,
		title:title_,
		confirmBtnName:"确 定",
		scrollType:'yes',
		isAutoSize : true
	});
	rc_pop.setContent('contentUrl',url_);
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
}

function popAlert(message,callFun){
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: false,
		isShowShadow: true,
		width : 220,
		height : 120,
		title:"提示",
		confirmBtnName:"确 定",
		isAutoSize : true
	});
	rc_pop.setContent("alertCon",message +"<div class='ta_c'><a href='javascript:;' id='alertBtn' class='btn1'>完成</a></div>");
	rc_pop.setContent("confirmBtnName","确 定" );
	if(typeof(callFun)=='function'){
		rc_pop.setContent("callBack",callFun );
	}
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$('#alertBtn').click(function(){
		rc_pop.close();
		if(typeof(callFun)=='function'){
			callFun();
		}
	});
}
function initRadioEvent(){
	$("input[name='isright']").each(function(){
		$(this).click(function(){
		
		var a=$(this).parent("li").find("span").first().get(0).innerHTML;
			$(this).val(a);
		});
	});
}
function setRadioValue(){
	$("input[name='isright']").each(function(){
		if($(this).is(':checked')){
			var a=$(this).parent("li").find("span").first().get(0).innerHTML;
			$(this).val(a);
		}else{
			//$(this).val(0);
		}
	});
}
/**
  *更新试题状态
  */
function updateStatus(ids,status){
	var dat="[{ids:'"+ids+"',status:"+status+"}]";
	var context_path=TOTAL_CONTEXT_PATH||"";
	var url_=context_path+"/maintain/question/updateStatus";
		$.ajax({  
              type : "post",  
              url : url_,
              cache: false,  
              async : false,
              data:dat,  
              success : function(data){
              	var d =eval("("+data+")");//转换为json对象
					if(d && d.result == "true"){
						alert("更新成功!");
						window.location.reload();
	              	}else{
	              		popAlert(d.message);
	              	}
	              }
            }); 
}
function auditStatus(ids,status){
	var dat="[{ids:'"+ids+"',status:"+status+"}]";
	var context_path=TOTAL_CONTEXT_PATH||"";
	var url_=context_path+"/maintain/question/auditStatus";
		$.ajax({  
              type : "post",  
              url : url_,
              cache: false,  
              async : false,
              data:dat,  
              success : function(data){
              	var d =eval("("+data+")");//转换为json对象
					if(d && d.result == "true"){
						alert("更新成功!");
						window.location.reload();
	              	}else{
	              		popAlert(d.message);
	              	}
	              }
            }); 
}
function tagOperate(ids,tagIds,fun){
	if(!ids){
		alert("请选择试题");
		return;
	}
	//如果是批量操作，至少选择两道试题
	var isBatch = $("#is_batch").val();
	var idArr = ids.split(",");
	if(isBatch && idArr.length == 1){
		alert("请至少选择两道试题");
		return;
	}
	var dat="[{ids:'"+ids+"',tagIds:'"+tagIds+"'}]";
	var context_path=TOTAL_CONTEXT_PATH||"";
	var url_=context_path+"/maintain/question/tagOperate";
		$.ajax({  
              type : "post",  
              url : url_,
              cache: false,  
              async : false,
              data:dat,  
              success : function(data){
              	var d =eval("("+data+")");//转换为json对象
					if(d && d.result == "true"){
						  popAlert("添加成功!",function(){
						  	if(typeof(fun)=='function'){
								fun(ids);						  	
						  	}
						  	//window.location.reload();
						  });
	              	}else{
	              		popAlert(d.message);
	              	}
	              }
            }); 
}
function findLabelTags4TagOperate(id){
	var context_path=TOTAL_CONTEXT_PATH||"";
	var rs='';
	var url_=context_path+"/maintain/klb/selectedTag?appId=2&knowledgeObjectId=2&knowledgeId="+id;
		$.ajax({  
              type : "post",  
              url : url_,
              cache: false,  
              async : false,
              success : function(data){
              		rs=data;
	              }
            }); 
       return rs;
}
function findLabelTags4TagOperateSub(id){
	var context_path=TOTAL_CONTEXT_PATH||"";
	var rs='';
	var url_=context_path+"/maintain/klb/selectedTag?appId=2&knowledgeObjectId=7&knowledgeId="+id;
		$.ajax({  
              type : "post",  
              url : url_,
              cache: false,  
              async : false,
              success : function(data){
              		rs=data;
	              }
            }); 
       return rs;
}
function checkall(obj){
	var b = $(obj).attr("checked");
	$("input:checkbox[class='listcheck']").attr("checked",b);
	//同步更改batchTag元素的aid属性
	var ids = "";
	$(".listcheck").filter(":checked").each(function(i,ele){
		ids = ids+$(ele).val()+",";
	});
	if(ids.length>0){
		ids = ids.substring(0,ids.length-1);
	}
	var isBatch = $("#is_batch").val();
	if(isBatch && $("#bq_attr2").is(":visible")){
		$("#batchTag").attr("aid",ids);
		$("#bq_attr2 .save_btn").attr('aid',ids);
		$("#save_kn").attr("aid",ids);
	}
//	$(obj).parent().parent().parent().parent().find("input[name='questionId']").each(function(){
//		$(this).attr('checked','true');
//	});
}
function doBuckUpdateStatus(){
	var ids='';
	$("input[name='questionId']:checked").each(function(){
		ids=ids+','+$(this).val();
	});
	ids=ids.substring(1);
	updateStatus(ids,1);
}
function deleteQuestion(id){
var context_path=TOTAL_CONTEXT_PATH||"";
	var url_=context_path+"/maintain/question/deleteQuestion?id="+id;
		$.ajax({  
			  type : "get",  
              url : url_,
              cache: false,  
              async : false,
              success : function(data){
              	var d =eval("("+data+")");//转换为json对象
					if(d && d.result == "true"){
						alert("删除成功!");
						window.location.reload();
	              	}else{
	              		popAlert(d.message);
	              	}
	              }
            }); 
}
function initSelectAndCheckedValue(){
	var t=$('select[name="choiceQuestion.composeType"]');
	var v=t.attr('v');
	if(v!=''){
		t.val(v);
	}
	$('input[name="isright"]').each(function(i){
		v=$(this).val();
		if(v&&v!=''&&v!=0){
			$(this).attr("checked",'true');
		}
	});
}
function autoShowAnswer(){
	var b=false;
	$('input[name="isright"]').each(function(i){
		var v=$(this).val();
		if(v&&v.length>0){
			b=true;
			return ;
		}
	});
	if(b){
		$('#zstfm ul').removeClass('hide2');
	}
}
/**
 * 验证试题编码
 * @param idInput 试题ID的input id 默认为"qid"
 * @param codeInput 试题编码的 input id 默认为"code"
 * @return true：验证通过，false：验证不通过
 */
function checkCode(idInput,codeInput){
	idInput = idInput || "qid";
	codeInput = codeInput || "code";
	var id = $("#"+idInput).val() || "";
	var code = $("#"+codeInput).val();
	var defalutValue = $("#"+codeInput).attr("df");
	var err = $("#"+codeInput).siblings(".error1");
	if(isBlank(code) || code == defalutValue){
		$("#"+codeInput).focus();
		err.html("请填写试题编码").show();
		return false;
	}else if(!isCodePattern(code)){
		$("#"+codeInput).focus();
		err.html("编码只能由数字,字母,下划线和斜线('/')组成!").show();
		return false; 
	}else{
		var isReturn = true;
		err.hide();
		var param="["+$.toJSON($("#"+idInput).serializeObject())+","+$.toJSON($("#"+codeInput).serializeObject())+"]";
		$.ajax({
			url: "/maintain/complex/uniqueCode",
			type: "POST",
			cache: false,
			async:false,
			data: param,
			success: function(data){
				var d =eval("("+data+")");//转换为json对象 
				if(d && d.result == "true"){
				}else{
					err.html(d.message).show();
					isReturn=false;
				}
			},
			error: function(data){
				popAlert("请求失败，请重试！");
				isReturn=false;
			}
		});
		if(!isReturn){
			$("#"+codeInput).focus();
		}
		return isReturn;
	}
}

/**
 * 验证所有class包含required的元素值是否为空
 * @return
 */
function checkRequireds(){
	var result = true;
	$(".required").each(function(i,el){
		result = checkRequired(el) && result;
	})
	return result;
}

/**
 * 非空项验证
 * @param el
 * @return
 */
function checkRequired(el){
	if(!$(el).hasClass("required") || $(el).is(":hidden")){
		return true;
	}
	var isblank = isBlank(el.value);
	if(isblank){
		$(el).siblings(".error1").html("不能为空").show();
		$(el).focus();
	}else{
		$(el).siblings(".error1").hide();
	}
	return !isblank;
}

function maxLength(el){
	var len = $(el).attr("ml") || "0";
	len = parseInt(len) || 0;
	if(len == 0){
		return true;
	}
	var error1 = $(el).siblings(".error1");
	var max = checkRequired(el) && lengthLE(el.value,len);
	if(max){
		error1.hide();
	}else{
		if(error1.html().trim() == ""){
			error1.html("请输入长度不大于"+len+"的字符串");
		}
		error1.show();
		$(el).focus();
	}
	return max;
}
function checkML(){
	var result = true;
	$(".maxlength").each(function(i,el){
		result = maxLength(el) && result;
	})
	return result;
}
function checkFck(name){
	var result = true;
	$("textarea[id='"+name+"']").each(function(){
		var oEditor=FCKeditorAPI.GetInstance(name);
		var content=oEditor.GetXHTML(true);
		result = isBlank(content) && result;
		 if(result){
			$(this).siblings(".error1").html("题干不能为空").show(); 
			 alertMsg("题干不能为空，若无需输入题干，请输入两个及以上空格即可");
		 }else{
			 $(this).siblings(".error1").empty().hide();
		 }
	});
	return !result;
}
function validate(){
	return checkRequireds() && checkML();
}
//$(function(){
//	//试题编码验证
//	$("#code").blur(function(){
//		checkCode();
//	})
//	//非空项验证
//	$(".required").blur(function(){
//		checkRequired(this);
//	})
//	$(".maxlength").blur(function(){
//		maxLength(this);
//	});
//	
//});
/*
*预览单选
*/
function previewPop(){
var context_path=TOTAL_CONTEXT_PATH||"";
var url_=context_path+'/question/base/choice/preview/0?'+"1="+Math.random();
	//popUrl('预览',url_);
	windowOpen(url_,780,400);
}
function windowOpen(url_,width1,height1){
	var iTop = (window.screen.availHeight-30-height1)/2; //获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth-10-width1)/2; //获得窗口的水平位置;
	var s=',width='+width1+',height='+height1+',top='+iTop+',left='+iLeft;
	window.open(url_,'_blank','toolbar=no,location=no,scrollbars=yes,status=yes,resizable=yes,menubar=no'+s);
}
function findPreViewData(){
	$("textarea[name^='content']").each(function(){
			var id=$(this).attr('id');
			var oEditor=FCKeditorAPI.GetInstance(id);
			var content=oEditor.GetXHTML(true);
			 //$(this).attr('value',content);
			content=replaceBlank(content);
			  $(this).html(content);
		});
	var j=$('textarea[name="choiceQuestion.feature"]');
	if(j.size()!=0){
		var oEditor=FCKeditorAPI.GetInstance('choiceQuestion.feature');
		var content=oEditor.GetXHTML(true);
		content=replaceBlank(content);
		j.attr('value',content);
		 j.html(content);
	}
	j=$('input[name="questionDto.question.questionTip"]');
	j.attr('value',j.val());
	//j[0].setAttribute('value',j.val());
	
	setRadioValue();
	fillTextarea_param();
	fecthTopicExt();
	return $("form").html();
}

function checkForm(){
	$(".error1").hide();
	var isReturn = false;
	
	$("textarea[name^='content']").each(function(){
		var id=$(this).attr('id');
		var oEditor=FCKeditorAPI.GetInstance(id);
		//var content=oEditor.EditorDocument.body.innerHTML;
		var content=oEditor.GetXHTML(true);
		var err = $(this).parent().children(".error1:eq(0)");
		var err1 = $(this).parent().children(".error1:eq(1)");
		if(isBlank(content)||content=='&nbsp;'){
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
		 //$(this).attr('value',content);
		content=replaceBlank(content);
		oEditor.EditorDocument.body.innerHTML=content;
		 $(this).html(content);
	});
	
	if(isReturn){
		return false;
	}else{
		fillTextarea_param();
		
		return true;
	}
}
function replaceBlank(content){
	return content.replace(/^<span\s+.*?>&nbsp;<\/span>/, "").replace(/^<p\s*>&nbsp;/, "<p>").replace(/^\s+/,"");
}
function fillTextarea_param(){
	var j=$('input[name="questionDto.textarea_param"]');
		if(j.size()>0){
			var aa=[];
			$("textarea[name^='content']").each(function(i){
				aa[aa.length]=$(this).attr('name');
			});
			j.val(aa.join());
		}
}
function fecthTopicExt(){
	var a=$('input[name="questionDto.question.teId"]');
	if(a.val()==0){
		return ;
	}
	var oEditor=FCKeditorAPI.GetInstance("questionDto.question.topicExt");
	var content=oEditor.GetXHTML(true);
	content=replaceBlank(content);
	oEditor.EditorDocument.body.innerHTML=content;
	$("textarea[name='questionDto.question.topicExt']").attr('value',content).html(content);
}
function fectchParentCode(){
	if($('#questionDto_question_id').val()==0){
		if(parent.window.dialogArguments){
			var v=parent.window.dialogArguments.document.getElementById("questionDto.question.code").value+"_1";
			document.getElementById("questionDto.question.code").value=v;
		}else{
			var v=parent.window.opener.document.getElementById("questionDto.question.code").value+"_1";
			document.getElementById("questionDto.question.code").value=v;
		}
	}
}

function setFckToTextArea(name){
	$("textarea[name='"+name+"']").each(function(){
		var oEditor=FCKeditorAPI.GetInstance(name);
		var content=oEditor.GetXHTML(true);
		content=replaceBlank(content);
		oEditor.EditorDocument.body.innerHTML=content;
		 $(this).attr('value',content).html(content);
	});
}
/*题目预览,从qt.js里面部分摘录,整个在单个试题时候会报错*/
function preview_qt(){
	$("dt .tb1").live("mouseover",function(){
		$(this).addClass("tb1_hv");
	}).live("mouseout",function(){
		$(this).removeClass("tb1_hv");
	}).live("click",function(){
		var st = $(this).closest(".st"), txt = st.find(".ft8").text();
		if($.browser.msie){
			$(".tb1",st).each(function(i){
				if($(this).css('display')=='none'){
					$(this).show().next("span").remove();
				}
			});
		}else{
			$(".tb1:hidden",st).show().next("span").remove();
		}
		$(this).hide().after("<span>"+txt+"</span>");
	});
	$(".st .tb4").live("mouseover",function(){
		$(this).addClass("tb4_hv");
	}).live("mouseout",function(){
		$(this).removeClass("tb4_hv");
	}).live("click",function(){
		var st = $(this).closest(".st");
		$(".tb4_cl",st).removeClass("tb4_cl");
		$(this).addClass("tb4_cl");
	});
	
	$(".st span").each(function(i){
		if($(this).hasClass('tb4')){
			$(this).find('span').each(function(j){
				if($(this).hasClass('hot')){
					$(this).remove();
				}
			});
		}
	});
	
}
function chkReadListenSubItem(){
	if($('#zsttab').hasClass('hide2')){
		return false;
	}else{
		return true;
	}
}
function chkIsright(){
	var b=false;
	$("input[name='isright']").each(function(){
		if($(this).is(':checked')){
			b=true;
		}
	});
	return b;
}

/**
 * 将一个表单的数据返回成JSON对象
 * added by tangshuren
 */
$.fn.serializeObject = function() {   
	var o = {};   
	var a = this.serializeArray();   
	$.each(a, function() {   
	  if (o[this.name]) {   
	    if (!o[this.name].push) {   
	      o[this.name] = [ o[this.name] ];   
	    }   
	 o[this.name].push(this.value || '');   
	} else {   
	 o[this.name] = this.value || '';   
	}   
	});   
	return o;   
}

//显示/隐藏详情
$(".xqing").live('click',function(){
	var b = $(this).attr('open') || '0', tr = $(this).closest('tr'), ntr = tr.next();
	var _lel = $(this).attr('lel');//标签级别  2==》大题 ， 7==》子题，  8==》选项
	var nntr;
	if(2 == _lel || 7 == _lel){
		nntr = ntr.next();
	}
	if(b==='1'){
		tr.removeClass('trbg1a');
		ntr.hide();
		if(2 == _lel || 7 == _lel){
			nntr.hide();
		}
		$(this).attr('open','0').text('展开详情');
	}else{
		tr.addClass('trbg1a');
		ntr.show();
		var id = $(this).attr("aid");
		var context_path=TOTAL_CONTEXT_PATH||"";
		var url_;
		if(2 == _lel || 7 == _lel){
			url_=context_path+"/maintain/question/questionTag?questionId="+id;
		}
		if (8 == _lel) {
			url = context_path;//预留代码块，用于获取展现选项标签信息的链接处理
		}
		$.ajax({  
            type : "get",  
            url : url_,
            cache: false,  
            async : false,
            success : function(data){
				if(data){
					var flag = data.indexOf("抱歉，出错了");
					if(flag < 0){
						var d =eval("("+data+")");//转换为json对象
		            	var html = '<td class="td415" colspan="6" style="border:rgb(204,204,204) solid 1px;"><table class="trbg3 w100"><tr>';
		            	flag = 0;
		            	$.each(d, function(i) {
		            		flag++;
		            		var arr = d[i].split(":");
		            		var spans = "";
		            		if(arr.length ==2){
		            			var value = arr[1].split(",");
		            			$.each(value,function(j){
		            				var v = value[j].split("#");
		            				spans = spans+"<span style='margin-left:5px' aid='"+v[0]+"'>"+v[1]+"</span>"
		            			});
		            		}
		            		html = html + "<td>"+arr[0]+spans+";</td>"
		            		if((i+1)%4==0){
		            			html = html +"</tr><tr>";
		            		}
		            	});
		            	html = html + "</tr></table></td>";
		            	if(flag == 0){
		            		html = "";
		            	}
		            	ntr.html(html);
					}else{
						console.log("抱歉，获取题目标签出错了");
						ntr.html("");
					}
				}
            	
	         }
          });
		if(2 == _lel){
			jQuery.ajax({
				type : "get",  
	            url : context_path+"/maintain/question/aqbt?questionId="+id,
	            cache: false,  
	            async : false,
	            success : function(data){
	            	if(data){
	            		var flag = data.indexOf("ERROR");
	            		if(flag < 0){
		            		var d =eval("("+data+")");//转换为json对象
		            		var _subq = '<td class="td415" style="border:3px solid red;" colspan="6"><table class="trbg3 w100"><tbody>';
		            		flag = 0;
		            		jQuery.each(d, function(i) {
		            			flag++;
		            			_subq = _subq + 
		            					'<tr class="trbg1">'+
		            				'<td class="td41" style="text-align:center;border:rgb(204,204,204) solid 1px;width:140px;" title="'+d[i][1]+'"><div style="position:relative;"><span style="position:absolute;top:-8px;left:-6px;background-color:#f00;width:50px;height:15px;text-align:center;line-height:15px;color:#fff;">子试题</span>'+d[i][1]+'</div></td>'+
		            				'<td class="td42" style="border:rgb(204,204,204) solid 1px;width:100px;">'+d[i][2]+'</td>'+
		            				'<td class="td43" colspan="3" style="text-align:right;border:rgb(204,204,204) solid 1px;">'+
		            				'<a href="javascript:;" class="ml1 a1 xqing" aid="'+d[i][0]+'" lel="7">展开详情</a>'+
		            				'<a href="javascript:;" class="ml1 a1 addbq" aid="'+d[i][0]+'" lel="7">编辑标签</a>'+
		            				'</td>'+
		            				'</tr>'+
		            				'<tr></tr>'+
		            				'<tr></tr>';
		            		});
		            		_subq = _subq + '</tbody></table></td>';
		            		if(flag == 0){
		            			_subq = "";
		            		}
		            		nntr.html(_subq);
	            		}else{
	            			console.log(data);
	            		}
	            		nntr.show();
	            	}
	            }
			});
		}
		$(this).attr('open','1').text('隐藏详情');
	}
})
$(".trbg3").find("span").live("mouseover",function(){
	var $this = $(this);
	var id = $(this).attr("aid");
	var context_path=TOTAL_CONTEXT_PATH||"";
	var url_=context_path+"/maintain/klb/parentTags?id="+id;
	$.ajax({  
        type : "get",  
        url : url_,
        cache: false,  
        async : false,
        success : function(data){
			if(data){
				$this.attr("title",data);
			}
         }
	})
});
(function(a){a.fn.mask=function(c,b){a(this).each(function(){if(b!==undefined&&b>0){var d=a(this);d.data("_mask_timeout",setTimeout(function(){a.maskElement(d,c)},b))}else{a.maskElement(a(this),c)}})};a.fn.unmask=function(){a(this).each(function(){a.unmaskElement(a(this))})};a.fn.isMasked=function(){return this.hasClass("masked")};a.maskElement=function(d,c){if(d.data("_mask_timeout")!==undefined){clearTimeout(d.data("_mask_timeout"));d.removeData("_mask_timeout")}if(d.isMasked()){a.unmaskElement(d)}if(d.css("position")=="static"){d.addClass("masked-relative")}d.addClass("masked");var e=a('<div class="loadmask"></div>');if(navigator.userAgent.toLowerCase().indexOf("msie")>-1){e.height(d.height()+parseInt(d.css("padding-top"))+parseInt(d.css("padding-bottom")));e.width(d.width()+parseInt(d.css("padding-left"))+parseInt(d.css("padding-right")))}if(navigator.userAgent.toLowerCase().indexOf("msie 6")>-1){d.find("select").addClass("masked-hidden")}d.append(e);if(c!==undefined){var b=a('<div class="loadmask-msg" style="display:none;"></div>');b.append("<div>"+c+"</div>");d.append(b);b.css("top",Math.round(d.height()/2-(b.height()-parseInt(b.css("padding-top"))-parseInt(b.css("padding-bottom")))/2)+"px");b.css("left",Math.round(d.width()/2-(b.width()-parseInt(b.css("padding-left"))-parseInt(b.css("padding-right")))/2)+"px");b.show()}};a.unmaskElement=function(b){if(b.data("_mask_timeout")!==undefined){clearTimeout(b.data("_mask_timeout"));b.removeData("_mask_timeout")}b.find(".loadmask-msg,.loadmask").remove();b.removeClass("masked");b.removeClass("masked-relative");b.find("select").removeClass("masked-hidden")}})(jQuery);
function stopDClick(){
		$('body').mask("Loading...");
		var intval=null;
		intval=setInterval(function(){
			if($('.error1:visible').size()>0){
				$('body').unmask();
				clearInterval(intval);
			}
		},100);
		setTimeout(function(){$('body').unmask();},1000);

}
/*
	处理多次保存数据的问题,用遮罩处理,使用所有题目的地方 wp
*/
$(function(){
	var t=null;
	$("head").find('link[href$="main.css"]').each(function(){
		var name=$(this).attr('href').replace('main.css','jquery.loadmask.css');
		t=$(this).clone().attr('href',name);
	});
	$("head").append(t);

	submitBtn=$('.ta_c').find('a').first();
	if(submitBtn.size()==0){return;}
	if(submitBtn[0].getAttribute('onclick')){
		var value=submitBtn[0].getAttribute('onclick');
		var len=value.indexOf(':');
		var return_false=value.indexOf('false');
		if(len!=-1){
			value='javascript:'+"stopDClick();"+value.substring(len+1)+"";
		}else{
			value="stopDClick();"+value+"";
		}
		if(return_false!=-1){value=value+'return false;';}

		submitBtn[0].setAttribute("onclick",value);
	}else{
		value='stopDClick();';
		submitBtn[0].setAttribute("onclick",value);
	}
});

///题目页头编辑-start
///题目页头编辑-start
function fillHeadValue(){
	fillShareStatus();
	$("input[name='questionDto.questionBankExt.tag1']").val($("#header_tag1").val());
	$("input[name='questionDto.questionBankExt.tag2']").val($("#header_tag2").val());
	$("input[name='questionDto.questionBankExt.tag3']").val($("#header_tag3").val());
	$("input[name='questionDto.questionBankExt.status']").val($("#header_shareStatus").val());
}
function fillShareStatus(){
	if($("i[name='shareStatus']").attr("class")=='cur2'){
		//2-未共享  3-已共享
		$("#header_shareStatus").val(3);
	}else{
		$("#header_shareStatus").val(2);
	}
}
///题目页头编辑-end