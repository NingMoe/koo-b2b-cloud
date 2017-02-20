// JavaScript Document
$(function(){
	var num_attr = ["A","B","C","D","E","F","G","H","I","J","k","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","[","]","{","}","<",">","(",")","@","#","$","%","^","&","*","~","!"],info_index = null;
	//点击增加题干，出现一个新的题干，若只有一个题干的时候，该题干的数字标号隐藏，每添加一个题干的时候，所有的题干的序号要重新排一次。
	$(".casual_work div input").live("click",function(){
		  var otigan = $("<div></div>");
		  otigan.attr("class","casual_work").html('<ul class="num ml"><li class="num2">'+($(this).parents("casual_work").index()+3)+'</li><li class="gbi"></li></ul><textarea class="ml sru_box2"></textarea><div class="btn_box ml"><input type="button" class="button" value="增加题干" onfocus="this.blur()"/></div>');
		  $(this).parents(".casual_work").after(otigan);
		  var aPar_box = $(".casual_work");
		  aPar_box.each(function(){
			   $(this).find(".num2").html($(this).index()+1);
		  });
		  $(this).parents(".casual_work").find("ul").show();
	});
	//鼠标滑入滑出题干的关闭按钮的效果，鼠标点击题干的关闭按钮的时候关闭该题干,当剩下一个题干的时候，关闭按钮隐藏。每次关闭一个题干的时候，所有的题干的序号要重新排一次。
	$(".casual_work ul li.gbi").live("click mouseover mouseout",function(event){
		if(event.type=="click"){
			 $(this).parents(".casual_work").remove();
			 var aPar_box = $(".casual_work");
			 if(aPar_box.length == 1){
				  $(aPar_box[0]).find("ul").hide();	 
			 }
		    aPar_box.each(function(){
		       $(this).find(".num2").html($(this).index()+1);  
		    });	
		}else if(event.type=="mouseover"){
			 $(this).addClass("gbi_h");
		}else{
			$(this).removeClass("gbi_h");	
		}	
	});
	//点击“编辑框”，出现弹框，并将该条信息上的表单的值获取到，放入到弹框的表单中。
	$(".data_list .bianji").live("click",function(){
		editMsg($(this).prevAll(".text"));
		return false;
//		$(".tk_box p:first").find("span").text("编辑文本");
//		$(".tk_box").show();
//		var oInput = $(this).parents("li").find("input"),otk_box = $(".tk_box .tk_wb");
//		otk_box.val(oInput.val());
//		info_index = $(this).parents("li").index();
//		var otk_box = $(".tk_box textarea");
//		otk_box.get(0).btn = false;
	});
	//鼠标滑入滑出关闭按钮有效果，点击关闭按钮，关闭弹框。
	$(".tk_box p i").hover(function(){
		$(this).addClass("hover");
	},function(){
		$(this).removeClass();
	}).click(function(){
		$(".tk_box").hide();	
	});
	//点击弹框的提交按钮，关闭弹框,同时将弹框的表单的值获取到，放入到相应的信息的表单里去。
	$(".tk_box a:first").click(function(){
		$(".tk_box .button2").show();
		$(".tk_box .ts").show();
		$(this).parent().find(".upload").hide();
		$(".tk_box").hide();
		var otk_box = $(this).parents(".tk_box").find("textarea");
		if(otk_box.get(0).btn){ //当是通过点击"批量添加备选单词"弹出来的弹框的时候。
			var astr = otk_box.get(0).value.split("\n"),dt_li = $(".data_list li");
			if(astr.length<=dt_li.length){  //若弹框里的textarea里的值小于或者等于
				//对于表单里的值重新分配（用弹框里的值。）
				for(var i=0;i<astr.length;i++){
					$(dt_li[i]).find("input").val(astr[i]);	
				}
				//多余的表单项和表格项删除
				var n = astr.length;
				if(n < 2){
					n=2;
				}
				for(var i=n;i<dt_li.length;i++){  
					dt_li.eq(i).remove();
					$(".dx_dt tr").each(function(){
						$(this).find("td").eq(i).remove();
					});
				}
				//重新排编号
				$(".dx_dt tr").eq(0).find("td").each(function(){
					$(this).html(num_attr[$(this).index()-1]);
				});
			}else{
				for(var i=0;i<dt_li.length;i++){
					$(dt_li[i]).find("input").val(astr[i]);	
				}
				//批量添加列表条数和表格数列选择。
				for(var i=dt_li.length;i<astr.length;i++){
					//添加横列选择
					var oLi = $("<li></li>");
					//oLi.html('<span class="ml font fl">A</span><input type="text" class="text ml fl" /><span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span><span class="bianji ml fl">编辑框</span><span class="error1"></span>');
					oLi.html('<span class="ml font fl">A</span><input type="text" class="text ml fl" /><span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span><span class="error1"></span>');
					oLi.find("input").val(astr[i]);
					$(".data_list").append(oLi);
					//添加表格数列选择,同时排编号。
					var atr = $(".dx_dt tr");
					atr.each(function(){
						if($(this).index()==0){
							var otd = $("<td></td>");
							otd.attr("class","back").html(num_attr[$(this).find("td").length-1]);
							$(this).append(otd); 	
						}else{
							var otd = $("<td></td>");
							otd.html('<input type="radio" />');
							$(this).append(otd);
						}
					});
				}
				//重置信息列表序号。
				var aLi = $(".data_list li");
				aLi.each(function(){
					$(this).find("span").eq(0).html(num_attr[$(this).index()]);   
				});
			}
		}else{   //当是通过点击"编辑框"弹出来的弹框的时候。
			var oInput = $(".data_list").find("li").eq(info_index).find("input");
			oInput.val(otk_box.val());
		}
		sort_nameAndRadio();
	});
	//点击添加图片的按钮或者添加音频的按钮，上传表单显示。同时签名的标注变成相应的文字。
	$(".tk_box .button2").click(function(){
		var oupload = $(this).parent().find(".upload");
		oupload.show();
		oupload.find("span").text($(this).text()+"：");
	});
	//滑入滑出表单的提交按钮的效果
	$(".button").hover(function(){
		$(this).addClass("button_h");	
	},function(){
		$(this).removeClass("button_h");
	});
	//点击"批量添加备选单词",查询“批量添加备选单词”下所有的表单的值，同时将表单里所有的值用换行隔开，放入弹出来的弹框的Textarea里。
	$("#piliang").click(function(){
		$(".tk_box").show();
		$(".tk_box .button2").hide();
		$(".tk_box .upload").hide();
		$(".tk_box .ts").hide();
		var sval = "", idx = 0;
		$(".data_list li input").each(function(){
			idx++;
			var value = $(this).val();
			if(idx==$(".data_list li input").length){
				sval+=value;
			}else if(idx == 1){
				if($.trim(value)){
					sval+=value+"\n";
				}else{
					sval+=value;
				}
			}else{
				sval+=value+"\n";
			}
		});
		$(".tk_box .tk_wb").val(sval);
		$(".tk_box p:first").find("span").text("批量添加选项(请每行输入一个选项，多组选项以空行分割)");
		var otk_box = $(".tk_box textarea");
		otk_box.get(0).btn = true;
	});
	//备选单词
    //值上移,当该条值的信息为空的时候，不能移动
	$(".data_list .dt_up").live("click",function(){
		  var oPar =  $(this).parents("li"),
		      oLi_prev = oPar.prev(),
			  oThis = oPar.find("input"),
			  oPev = oLi_prev.find("input");
		  if(oLi_prev && oThis.val()!=""){
				var sval_this = oThis.val();
				oThis.val(oPev.val());
				oPev.val(sval_this);
		  }	
	});
	//值下移，当该条值的信息为空的时候，不能移动。
	$(".data_list .dt_down").live("click",function(){
		 var oPar = $(this).parents("li"),
		 	 oLi_next = oPar.next(),
			 oThis = oPar.find("input"),
			 oNext = oLi_next.find("input");
		 if(oLi_next && oThis.val()!=""){
			 var sval_this = oThis.val();
			 	oThis.val(oNext.val()); 
				oNext.val(sval_this);
		 }
	});
	//点击删除，删除该条信息，表单的单选也删除了一数列选择。
	$(".data_list .dt_remove").live("click",function(){
		   var aLi = $(".data_list li");
		   if(aLi.length>2){
			   var Index = $(this).parents("li").index();
				$(this).parents("li").remove(); 
				//删除表单的一数列选择。
			   var atr = $(".dx_dt tr");
			   atr.each(function(){
					$(this).find("td").eq(Index+1).remove();   
			   });  
		   }
		   aLi = $(".data_list li");
		   aLi.each(function(){
				$(this).find("span").eq(0).html(num_attr[$(this).index()]);   
		   });
		   var atd = $(".dx_dt tr").eq(0).find("td");
		   atd.each(function(){
			  if($(this).index()!=0){
					$(this).html(num_attr[$(this).index()-1])  
			  }   
		   });
		   sort_nameAndRadio();
	});
	//点击添加，添加一条信息,表单的单选也添加了一数列选择。
	$(".data_list .dt_add").live("click",function(){
			var oLi = $("<li></li>");
			//oLi.html('<span class="ml font fl">A</span><input type="text" class="text ml fl" /><span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span><span class="bianji ml fl">编辑框</span><span class="error1"></span>');
			oLi.html('<span class="ml font fl">A</span><input type="text" class="text ml fl" /><span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span><span class="error1"></span>');
			$(this).parents(".data_list").append(oLi);
			var aLi = $(".data_list li");
			aLi.each(function(){
				$(this).find("span").eq(0).html(num_attr[$(this).index()]);   
			});
			//添加表单的一数列单选选择。
			var atr = $(".dx_dt tr");
			atr.each(function(){
				if($(this).index()==0){
					var otd = $("<td></td>");
					otd.attr("class","back").html(num_attr[$(this).find("td").length-1]);
					$(this).append(otd); 	
				}else{
					var otd = $("<td></td>");
					otd.html('<input type="radio" />');
					$(this).append(otd);
				}
			});
			sort_nameAndRadio();
	});
	//参考答案的单选
	$(".dx_dt input").live("click",function(){
		//横着单选。
		var aInput_x = $(this).parents("tr").find("input");
		for(var i=0;i<aInput_x.length;i++){
			aInput_x[i].checked  = false;	
		}
		//竖着也是单选。
		var td_index = $(this).parent().index(),aInput_y = $(this).parents("tr").siblings().find("input");
		aInput_y.each(function(){
			if($(this).parent().index()==td_index){
				$(this)[0].checked = false;
			}	
		});
		//当前点击的选中。
		$(this)[0].checked = true;
	});
	//点击增加一选择按钮，增加一横列选择。
	var i_add = 0;
	/*$("#add_seclect").click(function(){
		i_add++;
		var svar = "<span class='fill_in'>("+i_add+")</span>";
		$(".casual_work").eq(0).find("textarea").get(0).value+=svar;
		var otr = $("<tr></tr>"),atr = $(".dx_dt tr"),atd = atr.eq(0).find("td"),sele = null;
		for(var i=0;i<atd.length;i++){
			if(i==0){
			   	sele+=	'<td>'+atr.length+'</td>';
			}else{
				sele+= '<td><input type="radio" /></td>';
			}
		}
		otr.html(sele);
		$(".dx_dt").append(otr);
	});*/
//	function add_tr(){
//		i_add++;
//		var svar = "<span class='fill_in'>("+i_add+")</span>";
//		$(".casual_work").eq(0).find("textarea").get(0).value+=svar;
//		var otr = $("<tr></tr>"),atr = $(".dx_dt tr"),atd = atr.eq(0).find("td"),sele = null;
//		for(var i=0;i<atd.length;i++){
//			if(i==0){
//			   	sele+='<td>'+atr.length+'</td>';
//			}else{
//				sele+='<td><input type="radio" /></td>';
//			}
//		}
//		otr.html(sele);
//		$(".dx_dt").append(otr);
//	}
	//点击“删除一选择”按钮，删除一横列选择。
	/*$("#remove_seclect").click(function(){
		var otr = $(".dx_dt tr").eq(3);
		otr.remove();	
	});*/
//	function remove_tr(num){
//		$(".dx_dt tr").eq(num).remove();
//	}
	
});
function checkChoiceTopic(){
	var name = $(".fmbx2").find(".ipta").last().attr("id");
	if(checkFck(name)){
		var oEditor=FCKeditorAPI.GetInstance(name);
		var content=oEditor.GetXHTML(true);
		var index = $(content).find(".tkbox").size();
		if(!index || index <= 0){
			alertMsg("请在题干处添加填空");
			return false;
		}
		else if(index<2){
			alertMsg("题干内至少需要添加两处填空");
			return false;
		}
		else{
			return true;
		}
	}
}
function checkChoice(){
	var result = true;
	var len = 900;
	$(".data_list input").each(function(){
		if(isBlank(this.value)){
			$(this).siblings(".error1").html("答案内容不能为空").show();
			result=false;
			return result;
		}
		var le = lengthLE(this.value,len);
		if(le){
			$(this).siblings(".error1").hide();
		}else{
			$(this).siblings(".error1").html("答案长度不能大于"+len+"个字符").show();
		}
		result = le && result;
	});
	return result;
}

function checkCB(){
	var result = true;
	var name = $(".fmbx2").find(".ipta").last().attr("id");
	var oEditor=FCKeditorAPI.GetInstance(name);
	var content=oEditor.GetXHTML(true);
	var len = $(".dx_dt input:checked").length;
	var cLen=$(content).find(".tkbox").size();
		if(len < cLen){
			result = false;
		}
	if(result){
		$("#cberror").hide();
	}else{
		$("#cberror").html("请选择参考答案").show();
		$("#cberror").focus();
		alertMsg("请选择参考答案");
	}
	
	return result;
}
//弹出的编辑框
function editMsg(ipt) {
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 660,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1);
	if(ipt.nextAll(".tbi").size()==0){
		rc_pop.setContent("alertCon", "<textarea class='ipta2' id='area"+n+"'></textarea><div class='ta_c'><a href='javascript:;' class='btn1'>完成</a></div>");
	}else{
		rc_pop.setContent("alertCon", "<textarea class='ipta2' id='area"+n+"'></textarea>\
		<div class='ta_c mb20'><a href='javascript:;' class='btn1'>完成</a>\
		<a href='javascript:;' class='btn2 ml1'>添加图片</a>\
		<a href='javascript:;' class='btn2 ml1'>添加音频</a></div>\
		<form id='upfile_form1' action='/maintain/upload2/optImg' method='POST' enctype='multipart/form-data' target='screct_frame' class='ta_c mb10' >添加图片：<input type='file' name='imagefile1' class='ml1' /><input type='hidden' name='randomNum' value='"+n+"'><a href='javascript:;' id='uploadImage' class='btn1 ml1'>上传</a></form>\
		<form id='upfile_form2' action='/maintain/upload2/optMp3' method='POST' enctype='multipart/form-data' target='screct_frame' class='ta_c mb10' >添加音频：<input type='file' name='mp3file2' class='ml1' /><input type='hidden' name='randomNum' value='"+n+"'><a href='javascript:;' id='uploadMp3' class='btn1 ml1'>上传</a></form>\
		<div class='mb10 bdb1'></div>\
		<iframe name='screct_frame' style='display:none;'></iframe>\
		<div class='ft2 mb10'><strong>帮助：</strong>如何修改图片的长和宽，在自动生成img标签中，添加<strong> width='200px;' hight='200px;' </strong>，注意之间需要空格。 </div>\
		");
	}
	rc_pop.setContent("title", "编辑文本");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$('#uploadImage').live("click",function(){
		if($('input[name="imagefile1"]',$('#upfile_form1')).val().length>0){
			$("#upfile_form1").submit().hide();                        
		}else{
			alert("请选择文件在提交!");
		}
	});
	$('#uploadMp3').live("click",function(){
		if($('input[name="mp3file2"]',$('#upfile_form2')).val().length>0){
			$("#upfile_form2").submit().hide();                        
		}else{
			alert("请选择文件在提交!");
		}
	});
	$("#area"+n).data("ipt",ipt).val(ipt.val()).next().find(".btn1").click(function(){
		var area = $(this).closest("div").prev();
		area.data("ipt").val(area.val());
		rc_pop.close();
	}).parent().parent().ontab(".btn2","form","sd","click").find("form").hide();
	return rc_pop;
}
//排布备选单词的表单的name值。
function sort_name(){
          var aInput = $(".data_list li input");
          aInput.each(function(){
                   $(this).attr("name","choiceAnswers["+$(this).parent().index()+"].description");
          });
}
//参考答案的单选按钮进行排序。
function sort_radio(){
          var a_radio = $(".dx_dt input");
          a_radio.each(function(){
                   var sval = ($(this).parents("tr").index()-1);
                   $(this).attr({"name":"choiceQuestionDtos["+sval+"].choiceQuestion.answer","value":$(this).parent().index()});         
          });     
}
function sort_nameAndRadio(){
	sort_name();
	sort_radio();
}