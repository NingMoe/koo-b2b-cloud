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

$(function(){
	$(".menu li").hover(function(){
		$("dl",this).addClass("hv");
	},function(){
		$("dl",this).removeClass("hv");
	}).each(function(i){
		$(this).css({
			"left":$(this).position().left+"px",
			"z-index":2+i
		});
	}).addClass("posa");
//	$(".ontab").ontab(".ontabfn",".ontabtxt","selected","click");
	$(".fck").each(function(i){
		addFck(this,i+1);
	});
	var code = $("#code").val();
	if(isBlank(code)){
		$("#code").val($("#code").attr("df"));
	}
	var cid = $("#cid").val();
	if(cid && cid !="0"){
		$("#code").attr("readonly",true);
	}
	$(".cbx1").click(function(){
		if(this.checked){
			this.value = 1;
			$(this).parent("li").siblings().find(":radio").attr("checked",false);
		}else{
			this.value = 0;
		}
	});
	$("#preview").click(function(){
		$(".error1").empty().hide();
		$("#cberror").empty().hide();
		if(validate()&&checkChoiceTopic() && checkChoice() && checkCB()){
			window.open('','preview','toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
			$("#complexForm").attr("target","preview");
			$("#complexForm").attr("action","/maintain/complex/preview/1");
			$("#complexForm").submit();
		}
	});
	$("#previewTk").click(function(){
		if(validate() &&checkEssayTopic() && checkEssay()){
			window.open('','preview','toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
			$("#complexForm").attr("target","preview");
			$("#complexForm").attr("action","/maintain/complex/preview/1");
			$("#complexForm").submit();
		}
	});
	$("#submit").click(function(){
		if(validate() &&checkChoiceTopic() && checkChoice() && checkCB()){
			$("#complexForm").attr("action","/maintain/complex/insert");
			$("#complexForm").attr("target","_self");
			$("#complexForm").submit();
		}
		return false;
	});
	$("#submitTk").click(function(){
		if(validate()&&checkEssayTopic() && checkEssay()){
			$("#complexForm").attr("action","/maintain/complex/insert");
			$("#complexForm").attr("target","_self");
			$("#complexForm").submit();
		}
		return false;
	});
	_fill_blank_answer_ext();
});
function _append_ext(){
	var txt='';
	if($('input[name="questionDto.question.questionTypeId"]').val()==2){

	 txt='<span class="fn"><a href="javascript:;" class="add"></a></span>';
	}
	
	return txt;
}
function _fill_blank_answer_ext(){
	
	$('.tklist li').each(function(index, el) {
			var th=$(this);
			 
		if(th.find('.fn .add').size()==0){
			 
			var ls=th.children().eq(-2);
			var txt='<span class="fn"><a href="javascript:;" class="edit"></a><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a></span>';
			txt=_append_ext();
			

			ls.before(txt);
		}
		if($('.tks',th).size()>1){
			$('.tks',th).each(function(index) {
				$(this).children().last().after('<span class="fn"><a href="javascript:;" class="remove"></a></span>');
			});
		}
	});
	$('.tklist .fn .add').live('click',function(){
		var li=$(this).closest("li"), tks = $(".tks:last", li);
		var tmp=$(".tks", li);
		if(tmp.size()==1&&tmp.first().find('.remove').size()==0){
			tks.append('<span class="fn"><a href="javascript:;" class="remove"></a></span>');
		}
		var t_clone=tks.clone(true);
		
		t_clone.find('input').val('');
		tks.after(t_clone);
		_reset_answer_ext_order($(".tks", li));
	});
	$('.tklist .fn .remove').live('click',function(){
		var li=$(this).closest("li");
		$(this).closest(".tks").remove();
		var tks=li.find('.tks')
		
		if(tks.size()==1){
		
			tks.find('.fn').remove();
		}
		_reset_answer_ext_order($(".tks", li));
	});
	$('input[name="essayQuestion_sensing_box"]').bind('change',function(){
		var t=$('input[name="essayQuestion.sensing"]');
		if($(this).attr("checked")){
			t.val(1);
		}else{
			t.val(0);
		}
	});

	//自动隐藏 essayQuestion.one2one 
	_autoHiddenOne2One();
}
function _autoHiddenOne2One(){
	var t=$('.tklist').find('li');
	if(t.size()<2){
		$('#autohidespan').hide();
	}else{
		$('#autohidespan').show();
	}
}
function _reset_answer_ext_order(tks){
	
	tks.each(function(index) {
		
			var name=$(this).find('input').attr('name');
			name=name.replace(/.exts\[\d+\]/,'.exts['+index+']');
			
			$(this).find('input').attr('name',name);
	});
}
$(".addtg").live("click",function(){
	addfm2($(this).closest(".fmbx2"));
	
	$(".fmbx2").each(function(i){
		var ipta = $(".ipta",$(this));
		ipta.attr("name","content1"+i);
	});
	$(".fmbx2:last").find(".ipta").attr("name","content1");
	//addfm();
});
$(".numSel").live("change",function(){
	if($("option:selected",this).hasClass("other")){
		$(this).next(".hide").removeClass("hide");
	}else{
		$(this).next("input").addClass("hide");
	}
});
$(".fmbx1 .add").live("click",function(){
	var li = $(this).closest("li"), newli = li.clone();
	newli.find(".ipt").val("");
	newli.find(".hid").val(0);
	newli.find(".cbx1").removeAttr("checked");
	li.after(newli);
	rsetfm(li.closest(".fmbx1"));
	return false;
});
$(".fmbx1 .remove").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	if($("li",fmbx1).size()>2){
		var delaid = $("#delaid").val();
		var aid = $(".hid",li).val();
		
		if(aid != "0"){
			$("#delaid").val($(".hid",li).val()+","+delaid);
		}
	}
	$("li",fmbx1).size()>2 && li.remove();
	rsetfm(fmbx1);
	return false;
});
$(".fmbx1 .up").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	li.index() && li.after(li.prev());
	rsetfm(fmbx1);
	return false;
});
$(".fmbx1 .down").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	(li.index()<($("li",fmbx1).size()-1)) && li.before(li.next());
	rsetfm(fmbx1);
	return false;
});
$(".bjk").live("click",function(){
	// editMsg($(this).prevAll(".ipt,.ipt2"));
	editMsg($(this).prevAll(".ipt2_ext"));
	return false;
});
$(".fmbx1 .fmclose").live("click",function(){
	var oEditor = FCKeditorAPI.GetInstance($(".fmbx2 .ipta:last").attr("id")); 
	var bd = oEditor.EditorDocument.body;
	var index = $(".fmclose").index($(this))-1;
	 $(bd).find(".tkbox").eq(index).remove();
	 removeChoice(this,bd);
});
$(".fmbx2 .fmclose").live("click",function(){
	
	var BD_tkt = $(this).closest(".fmt").is(".BD_tkt"),fmt = $(this).closest(".fmt");
	if ($(".fmbx2").size() == 1) {
		$(this).closest(".fmbx2").find("input,textarea,select").val("");
	}else{
		$(this).closest(".fmbx2").remove();
	}
	rsetfms(2);
	if(BD_tkt){
		fmt.find(".ipta:eq(0)").blur();
	}
	return false;
});
//完形填空有选择项的选择项删除
$(".fortkt .fmclose").live("click",function(){
	var ipta = $(".BD_tkt .iptatk:eq(0)"), ul = $(ipta.closest(".fmt").attr("ul"));
	setTimeout(function(){
		ordtk(ipta,ul,true);
	},30);
});
$(".iptatk").live("blur",function(){
	var ul = $($(this).closest(".fmt").attr("ul"));
	ordtk($(this),ul,$('#bhxx1').attr('checked'));
});
$(".ipt[reg]").live("focus",function(){
	$(this).parent().find(".error1").hide();
});

//更新关键词
function updtgjc(ul,hd){
	var s = "";
	$("li",ul).each(function(){
		s+=$(this).text()+" ";
	});
	hd.val($.trim(s));
}
//重置选择项的每一行
//function rsetfm(fm){
//	var index = $(".fmbx1").index(fm)+1;
//	$("li",fm).each(function(i){
//		$(".xh",this).html(String.fromCharCode(65+i));
////		$(".ipt",this).attr("name","fmbx"+index+"_"+String.fromCharCode(97+i));
//		$(".ipt",this).attr("name",$(".fmbx1 li").eq(0).find(".ipt").attr("name"));
//	});
//}
//重置选择项的每一行
function rsetfm(fm){
	var index = $(".fmbx1").index(fm);
	$(".hidcid",fm).attr("name","choiceQuestionDtos["+index+"].choiceQuestion.id");
	$(".hidqid",fm).attr("name","choiceQuestionDtos["+index+"].choiceQuestion.questionId");
	$("li",fm).each(function(i){
		$(".xh",this).html(String.fromCharCode(65+i));
		$(".ipt",this).attr("name","choiceQuestionDtos["+index+"].choiceAnswers["+i+"].description");
		$(".cbx1",this).attr("name","choiceQuestionDtos["+index+"].choiceAnswers["+i+"].isright");
		$(".hid",this).attr("name","choiceQuestionDtos["+index+"].choiceAnswers["+i+"].id");
		$(".ipt",this).blur(function(){
			checkRequired(this);
		});
		$(".cbx1",this).click(function(){
			if(this.checked){
				this.value = 1;
				$(this).parent("li").siblings().find(":radio").attr("checked",false);
			}else{
				this.value = 0;
			}
		});
	});
}
//添加一个选择项组
function addfm(fmbx1,index){
	fmbx1 = fmbx1 || $(".fmbx1:last");
	var nfm = $(".fmbx1:eq(0)").removeClass("fmone").clone(true);
	$(".hidcid",nfm).val(0);
	$(".hidqid",nfm).val(0);
	$(".hid",nfm).val(0);
	$(".cbx1",nfm).attr("checked",false);
	$(".cbx1",nfm).click(function(){
		if(this.checked){
			this.value = 1;
		}else{
			this.value = 0;
		}
	});
	if(index == -1){
		$(".fmbx1:eq(0)").before(nfm);
		
	}else{
		fmbx1.after(nfm);
	}
	
	clearfm(nfm);
	return nfm;
}
//重置所有选择项组或者题干组
function rsetfms(n){
	n = n || 1;
	var fmbx = $(".fmbx"+n).each(function(i){
		$(".num",this).html(i+1);
		$(".hidcid",this).attr("name","choiceQuestionDtos["+i+"].choiceQuestion.id");
		$(".hidqid",this).attr("name","choiceQuestionDtos["+i+"].choiceQuestion.questionId");
		$("li",this).each(function(j,li){
			$(".xh",li).html(String.fromCharCode(65+j));
			$(".ipt",li).attr("name","choiceQuestionDtos["+i+"].choiceAnswers["+j+"].description");
			$(".cbx1",li).attr("name","choiceQuestionDtos["+i+"].choiceAnswers["+j+"].isright");
			$(".hid",li).attr("name","choiceQuestionDtos["+i+"].choiceAnswers["+j+"].id");
		});
		
	});
	if(fmbx.size()==1 && !fmbx.hasClass("unfmone")){
		fmbx.addClass("fmone");
	}
}
//初始化一个选择项组
function clearfm(fm){
	return fm.each(function(){
		$("li:gt(1)",$(this)).remove();
		$(".ipt",$(this)).val("");
		$(".num",$(this)).html($(".fmbx1").index($(this))+1);
		rsetfm($(this));
	});
}
//删除一个选择项组-最少留一个
function removefm(fmbx1){
	fmbx1 = fmbx1 || $(".fmbx1:last");
	if(fmbx1.size()>1){
		fmbx1.each(function(){
			removefm($(this));
		});
		return fmbx1;
	}
	var len = fmbx1.parent().children(".fmbx1").size();
	if(len<=1){
		clearfm(fmbx1).addClass("hide2");
	}else{
		fmbx1.remove();
	}
	return fmbx1;
}

function removeChoice(fmclose,bd){
	$(".fmbx1").size()>1 && $(fmclose).closest(".fmbx1").remove();
	resetFcktk(bd);
	rsetfms();
	var qid = $(fmclose).siblings(".hidqid").val();
	var sid = $(fmclose).siblings(".hidcid").val();
	var delqid = $("#delqid").val();
	var delsid = $("#delsid").val();
	if(qid && qid != "0"){
		$("#delqid").val(qid+","+delqid);
		$("#delsid").val(sid+","+delsid);
	}
}
//添加一个题干
function addfm2(fmbx2){
	fmbx2 = fmbx2 || $(".fmbx2:last");
	var nfm = $(".fmbx2:eq(0)").removeClass("fmone").clone();
	fmbx2.after(nfm);
	nfm.find(".ipta").val("");
	rsetfms(2);
	var ipta = nfm.find(".ipta").val("");
	addFck(ipta, parseInt(fmbx2.find(".num").text())+100);
	return nfm;
}
function savest() {
	var isok = true;
	$(".ipt[reg]").each(function(){
		var reg = $(this).attr("reg"), val = $.trim($(this).val()), error = $(this).parent().find(".error1:eq(0)");
		if(reg && !(new RegExp(reg)).test(val)){
			isok = false;
			error.show();
		}
	});
	isok && alertMsg("保存成功！");
};
function error1(ipt,n) {
	if(!ipt)return;
	n = n || 1;
	var error = $(ipt).parent().find(".error1:eq("+(n-1)+")");
	error.show();
};
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
	
	if(ipt.parent().find(".bjk").hasClass("pttk_1")){
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
	}else{
		rc_pop.setContent("alertCon", "<textarea class='ipta2' id='area"+n+"'></textarea><div class='ta_c'><a href='javascript:;' class='btn1'>完成</a></div>");
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

function uploadCallBack(url,partPath,num){
	if(partPath=="image"){
		$("#area"+num).val($("#area"+num).val()+"<img src='"+url+"'/>");
	}else if(partPath=="video"){
		$("#area"+num).val($("#area"+num).val()+"<span class='jpy1' src='"+url+"'></span>");
	}
	$('#upfile_form1').hide();	
	$('#upfile_form2').hide();	
}

//批量添加描述
function pladdfm(max,min) {
	min = Number(min || 0);
	max = Number(String(max) || 50);
	min = Math.min(min,max);
	max = Math.max(min,max);
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 660,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1);
	rc_pop.setContent("alertCon", "<textarea class='ipta2'></textarea><div class='ta_c'><a href='javascript:;' id='btn"+n+"' class='btn1'>完成</a></div>");
	rc_pop.setContent("title", "<span class='ft1'>批量添加选项（请每行输入一个选项）</span>");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	var area = $("#btn"+n).closest("div").prev(),areav = "";
	$(".fmbx1").each(function(){
		if(areav)areav+="\n";
		$(".ipt",this).each(function(){
			if($(this).val()){
				areav+=$(this).val()+"\n";
			}
		});
	});
	area.val(areav);
	$("#btn"+n).click(function(){
		var area = $(this).closest("div").prev(), ar1, st = true, fmbx1, index = 0, index2 = 0, fmi, ipt, len;
		ar1 = area.val().split("\n");
		$(".fmbx1:gt("+(max-1)+")").remove();
		clearfm($(".fmbx1"));
		rsetfms(1);
		for(var i=0;i<ar1.length;i++){
			fmbx1 = $(".fmbx1");
			if(max==index)break;
			if(ar1[i]){
				if(fmbx1.size()>index){
					fmi = fmbx1.eq(index);
				} else{
					fmi = addfm();
				}
				ipt = fmi.find(".ipt:eq("+(index2++)+")");
				if(ipt.size()==0){
					fmi.find(".add:last").click();
					ipt = fmi.find(".ipt:last");
				}
				ipt.val(ar1[i]);
				st = false;
			}else{
				if(!st){
					st = true;
					index++;
					index2 = 0;
				}
			}
		}
		for(var i=0;i<min-$(".fmbx1").size();i++){
			addfm();
		}
		rc_pop.close();
	});
	return rc_pop;
}

/**
 * 添加一个填空项
 * @param bhxx 是否包含选择项
 * @param bd fckeditor中的内容
 */
function addtk3(bhxx,bd){
	//bhxx：是否包含选项
	var ipta = $(".fmbx2").find(".ipta").last(); 
	var ul = $("#tklist1"),
	index = (parseInt(ul.attr("index")) || 0)+1;
	ipta.val($(bd).html().replace('newtkbox"','newtkbox" index="'+index+'"'));
	ul.attr("index",index);
	ordtk(ipta,ul,bhxx);
//	$(bd).html(ipta.val());
	resetFcktk(bd);
	if(!bhxx){
		resettk();
	}
}
function setDeleteId(el){
	var qid = $(el).find(".hidqid").val();
	var sid = $(el).find(".hidcid").val();
	var aid = $(el).find(".hid").val();
	var delqid = $("#delqid").val();
	var delsid = $("#delsid").val();
	var delaid = $("#delaid").val();
	if(qid && qid != "0"){
		$("#delqid").val(qid+","+delqid);
		$("#delsid").val(sid+","+delsid);
		$("#delaid").val(aid+","+delaid);
	}	
}
/**
 * 删除一个填空项
 * @param a
 * @param bhxx
 * @param bd
 * @param delIndex
 * @return
 */
function deletetk(bhxx,bd,delIndex){
	$(".tkbox",$(bd)).eq(delIndex).remove();
	var ipta = $(".fmbx2").find(".ipta").last(); 
	var ul = $("#tklist1"),
	index = (parseInt(ul.attr("index")) || 1)-1;
	ul.attr("index",index);
	ipta.val($(bd).html());
	var li = $("#tklist1 li").eq(delIndex);
	var qid = li.find(".hidqid").val();
	var sid = li.find(".hidcid").val();
	var aid = li.find(".hid").val();
	var delqid = $("#delqid").val();
	var delsid = $("#delsid").val();
	var delaid = $("#delaid").val();
	if(qid && qid != "0"){
		$("#delqid").val(qid+","+delqid);
		$("#delsid").val(sid+","+delsid);
		$("#delaid").val(aid+","+delaid);
	}
	li.remove();
	ordtk(ipta,ul,bhxx);
//	$(bd).html(ipta.val());
	resetFcktk(bd);
	resettk();
}

function resettk(){
	$("#tklist1 li").each(function(i){
//		$(this).attr("index",i+1);
		$(this).find(".tktit").first().html("("+(i+1)+")");
		$(this).find(".ipt2").attr("name","essayQuestionDTOs["+i+"].fillblankAnswers[0].answer");
		$(this).find(".hidqid").attr("name","essayQuestionDTOs["+i+"].essayQuestion.questtionId");
		$(this).find(".hidcid").attr("name","essayQuestionDTOs["+i+"].essayQuestion.id");
		$(this).find(".hid").attr("name","essayQuestionDTOs["+i+"].fillblankAnswers[0].id");

		$(this).find(".ipt2_ext").each(function(index) {
			$(this).attr("name","essayQuestionDTOs["+i+"].fillblankAnswers[0].exts["+index+"]");
		});
		
	});
}
//重置fck编辑器中的填空项
function resetFcktk(bd){
	$(bd).find(".tkbox").each(function(i){
		$(this).html("("+(i+1)+")");
	});
}
//添加一个填空项
function addtk(a,bhxx){
//bhxx：是否包含选项
var ipta = $(a).closest(".fmbx").find(".ipta"), 
ul = $($(a).closest(".fmt").attr("ul")),
index = (parseInt(ul.attr("index")) || 0)+1;
ipta.val(ipta.val()+'<span class="tkbox newtkbox" index="'+index+'"></span>');
ul.attr("index",index);
ordtk(ipta,ul,bhxx);
}
/**
 * 重新给填空项排序
 * @param {Object} ipta  题干输入框
 * @param {Object} ul  填空列表
 * @param {Boolean} bhxx 是否是选择项
 * @param {Boolean} csh  是否是初始化
 */
function ordtk(ipta,ul,bhxx,csh){
	var html = $("<div />"),htmls = {}, newindex = 0, eq, li, lis, mli, tkbox, fmt = ipta.closest(".fmt"), iptas = $(".ipta",fmt), fmbx1 = $(".fmbx1");
	html.append(ipta.val());
//	iptas.each(function(i){
//		htmls["ipta"+i] = $("<div />").html($(this).val());
//		html.append(htmls["ipta"+i]);
//	});
	tkbox = html.find(".tkbox");
	if (csh) {
		tkbox.addClass("newtkbox");
		if(bhxx){
//			removefm($(".fmbx1"));
			ul.empty();
		}else{
			removefm($(".fmbx1"));
//			ul.empty();
		}
	}
	else {
		tkbox.each(function(i){
			if (bhxx) {
//				fmbx1.filter("*[index='" + $(this).attr("index") + "']").size() == 0 && !$(this).hasClass("newtkbox") && $(this).remove();
//				fmbx1.filter("*[index='" + (i+1) + "']").size() == 0 && !$(this).hasClass("newtkbox") && $(this).remove();
			}
			else {
//				ul.find("li[index='" + $(this).attr("index") + "']").size() == 0 && !$(this).hasClass("newtkbox") && $(this).remove();
//				tkbox.filter("*[index='" + $(this).attr("index") + "']:gt(0)").remove();
			}
//			tkbox.filter("*[index='" + $(this).attr("index") + "']:gt(0)").remove();
		});
	}
	tkbox = html.find(".tkbox").each(function(i){
		lis = ul.find("li");
		fmbx1 = $(".fmbx1");
		if (bhxx) {
			newindex = i+1;
		}else{
			newindex = $(this).attr("index");
		}
		
		$(this).html("("+(i+1)+")");
		if($(this).hasClass("newtkbox")){
			eq = i;
			if (bhxx) {
				var nfmbx1;
				if(fmbx1.is(".hide2")){
					nfmbx1 = fmbx1.removeClass("hide2");
				}else{
					nfmbx1 = addfm(fmbx1.eq(i-1),i-1);
					
				}
				nfmbx1.attr("index",newindex);
			}
			else {
				// li = $('<li index="' + newindex + '"><span class="tktit">(' + (eq + 1) + ')</span><input type="text" class="ipt2" name="essayQuestionDTOs['+eq+'].fillblankAnswers[0].answer"/><a href="javascript:;" class="a1 bjk pttk_1" >编辑框</a></li>');
				li = $('<li index="' + newindex + '"><span class="tktit">(' + (eq + 1) + ')</span><input type="hidden" class="ipt2" name="essayQuestionDTOs['+eq+'].fillblankAnswers[0].answer"/><span class="tks" ><input type="text" class="ipt2_ext" name="essayQuestionDTOs['+eq+'].fillblankAnswers[0].exts[0]"/><a href="javascript:;" class="a1 bjk pttk_1" >编辑框</a></span></li>');
				
				li.append(_append_ext());
				
				if (i) {
					lis.eq(i - 1).after(li);
				}
				else {
					ul.prepend(li);
				}
			}
			$(this).removeClass("newtkbox");
		}
		if (bhxx) {
			mli = fmbx1.filter(".fmbx1[index='" + newindex + "']");
		}
		else {
			mli = lis.filter("li[index='" + newindex + "']");
		}
		if(mli.index()!=i){
			if (bhxx) {
//				if (i) {
//					fmbx1.eq(i - 1).after(mli);
//				}else{
//					fmbx1.parent().prepend(mli);
//				}
			}
			else {
//				if (i) {
//					lis.eq(i - 1).after(mli);
//				}else{
//					ul.prepend(mli);
//				}
				mli.find(".tktit").text('(' + (i+1) + ')');
			}
		}
	});
	ipta.val(html.html());
//	iptas.each(function(i){
//		$(this).val(htmls["ipta"+i].html());
//	});
	if (bhxx) {
		removefm($(".fmbx1").slice(tkbox.size()));
		rsetfms(1);
	}
	else {
		ul.find("li").slice(tkbox.size()).remove();
	}
	_autoHiddenOne2One();
}


var fck_num=1;
function findFckNum(){
	var i=fck_num;
	fck_num=fck_num+1;
	return i;
}
function addFck(ele,n){
	if(!ele)return;
	n = n || 0;
	ele = $(ele);
	var next = ele.next(".tpbox");
	if(!next.size()){
		next = $("<div class='tpbox'/>");
		ele.after(next);
	}
	var name = 'content'+n;
	ele.hide().attr("name",name).attr("id",name);
	var oFCKeditor= new FCKeditor(name);
		var context_path=TOTAL_CONTEXT_PATH||"";
         oFCKeditor.BasePath = context_path+"/js/fckeditor/";
         oFCKeditor.ToolbarSet = "InsertBlank";//Basic
         oFCKeditor.Width="100%";
         oFCKeditor.Height="217";
         oFCKeditor.Create(next);
    $("input[name='"+name+"']",next).remove();
}

function checkEssay(){
	var result = true;
	var len = 255;
	$(".ontabtxt").find("input[type='text']").each(function(){
		var isblank = isBlank(this.value);
		if(isblank){
			$(".ontabtxt").find(".error1").html("请填写参考答案").show();
			$(this).focus();
		}else{
			$(".ontabtxt").find(".error1").hide();
		}
		result = !isblank && result;
		if(result){
			var le = lengthLE(this.value,len);
			if(le){
				$(".ontabtxt").find(".error1").hide();
				$(this).removeClass("texterror");
			}else{
				$(this).addClass("texterror");
				$(".ontabtxt").find(".error1").html("参考答案长度不能大于"+len+"个字符").show();
				$(this).focus();
			}
			result = le && result;
		}
		
	});
	return result;
}

function checkChoice(){
	var result = true;
	var len = 900;
	$(".fmbx1").find("input[type='text']").each(function(){
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
	$(".fmbx1").each(function(){
		var len = $(this).find("input[type='radio'][checked]").length;
		if(len == 0){
			result = false;
			return false;
		}
	});
	if(result){
		$("#cberror").hide();
	}else{
		$("#cberror").html("请选择参考答案").show();
		$("#cberror").focus();
		alertMsg("请选择参考答案");
	}
	return result;
}

function checkChoiceTopic(){
	var name = $(".fmbx2").find(".ipta").last().attr("id");
	if(checkFck(name)){
		var index = $(".fortkt .fmbx1").eq(0).attr("index");
		if(!index || index <= 0){
			$(".BD_tkt").find(".error1").html("请在题干处添加填空").show();
			alertMsg("请在题干处添加填空");
			return false;
		}else{
			return true;
		}
	}
}

function checkEssayTopic(){
	var name = $(".fmbx2").find(".ipta").last().attr("id");
	if(checkFck(name)){
		var index = $("#tklist1").eq(0).attr("index");
		if(!index || index <= 0){
			$(".BD_tkt").find(".error1").html("请在题干处添加填空").show();
			alertMsg("请在题干处添加填空");
			return false;
		}else{
			return true;
		}
	}
}