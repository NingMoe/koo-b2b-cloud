//如果当前页面不在iframe中，则强制放入iframe中。
if(window==top){
	//window.location.href = "main.jsp?rwin="+window.location.href.replace(/.*?\/([^\/.]*?)\.jsp*/ig,"$1");
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
	$(".ontab").ontab(".ontabfn",".ontabtxt","selected","click");
	$(".fck").each(function(i){
		addFck(this,i+100);
	});
	
	
	
	$(".hor").each(function(i){
		$(".xh",this).html(String.fromCharCode(65+i));
	});
	$(".ver").each(function(i){
		$(".xh",this).html(1+i);
	});
	$(".dx_td1").each(function(i){
		$(this).html(String.fromCharCode(65+i));
	});
	
	var code = $("#code").val();
	if(isBlank(code)){
		$("#code").val($("#code").attr("df"));
	}
	var cid = $("#cid").val();
	if(cid && cid !="0"){
		$("#code").attr("readonly",true);
	}
	$("#preview").click(function(){
		var showForm = $("#showForm").val();
		if(showForm==2){
			fillTextarea_param();
			if(validate() && checkBox()){
				var preview = 'preview';
				window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
				$("#matrixForm").attr("target",preview);
				$("#matrixForm").attr("action","/question/base/matrix/preview/1?1="+Math.random());
				$("#matrixForm").submit();
			}
		}else if(showForm==1){
			fillTextarea_param();
			if(validate() && checkBox()){
				var preview = 'preview';
				window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
				$("#matrixForm").attr("target",preview);
				$("#matrixForm").attr("action","/question/base/matrix/preview2/1?1="+Math.random());
				$("#matrixForm").submit();
			}
		}else if(showForm==3){
			fillTextarea_param();
			if(validate() && checkBox()){
				var preview = 'preview';
				window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=780,height=600');
				$("#matrixForm").attr("target",preview);
				$("#matrixForm").attr("action","/question/base/matrix/preview3/1?1="+Math.random());
				$("#matrixForm").submit();
			}
		}
	});
});
$(".addtg").live("click",function(){
	addfm2($(this).closest(".fmbx2"));
	//addfm();
});
$(".numSel").live("change",function(){
	if($("option:selected",this).hasClass("other")){
		$(this).next(".hide").removeClass("hide");
	}else{
		$(this).next("input").addClass("hide");
	}
});

/**************************************************/
//横向选项操作
$("#fmt1 .add").live("click",function(){
	var index = $("#fmt1 .add").index($(this)[0])+1;
	var li = $(this).closest("li"), newli = li.clone(true);
	newli.find(".ipt").val("");
	li.after(newli);
	$("#ckda tr").each(function(){
		var td = $("td",$(this)).eq(index),newtd = td.clone();
		newtd.find("input[type='checkbox']").attr("checked",false);
		td.after(newtd);
	});
	rsetfm(li.closest(".fmbx1"));
	return false;
});
$("#fmt1 .remove").live("click",function(){
	var index = $("#fmt1 .remove").index($(this)[0])+1;
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	var rm = $("li",fmbx1).size()>2 && li.remove();
	if(rm){
		$("#ckda tr").each(function(){
			$("td",$(this)).eq(index).remove();
		});
	}
	
	rsetfm(fmbx1);
	return false;
});
$("#fmt1 .up").live("click",function(){
	var index = $("#fmt1 .up").index($(this)[0])+1;
	if(index == 1){
		return false;
	}
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	li.index() && li.after(li.prev());
	$("#ckda tr").each(function(){
		var td = $("td",$(this)).eq(index);
		td.after(td.prev());
	});
	rsetfm(fmbx1);
	return false;
});
$("#fmt1 .down").live("click",function(){
	var index = $("#fmt1 .down").index($(this)[0])+1;
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	(li.index()<($("li",fmbx1).size()-1)) && li.before(li.next());
	$("#ckda tr").each(function(){
		var td = $("td",$(this)).eq(index);
		td.before(td.next());
	});
	rsetfm(fmbx1);
	return false;
});

//纵向选项操作
$("#fmt2 .add").live("click",function(){
	var index = $("#fmt2 .add").index($(this)[0])+1;
	var li = $(this).closest("li"), newli = li.clone(true);
	newli.find(".hid").val(0);
	newli.find(".hidid").val(0);
	newli.find(".ipt").val("");
	li.after(newli);
	var tr = $("#ckda tr").eq(index),newtr = tr.clone(true);
	newtr.find("input[type='checkbox']").attr("checked",false).val(li.find("input[type='checkbox']").val());
	tr.after(newtr);
	rsetfm(li.closest(".fmbx1"));
	return false;
});
$("#fmt2 .remove").live("click",function(){
	var index = $("#fmt2 .remove").index($(this)[0])+1;
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	var rm = $("li",fmbx1).size()>2 && li.remove();
	if(rm){
		$("#ckda tr").eq(index).remove();
		var delqid = $("#delqid").val();
		var delsid = $("#delsid").val();
		var sid = $(".hid",li).val();
		var qid = $(".hidid",li).val();
		if(sid && (sid != "0")){
			$("#delsid").val(sid+","+delsid);
		}
		if(qid && (qid != "0")){
			$("#delqid").val(qid+","+delqid);
		}
	}
	rsetfm(fmbx1);
	return false;
});
$("#fmt2 .up").live("click",function(){
	var index = $("#fmt2 .up").index($(this)[0])+1;
	if(index == 1){
		return false;
	}
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	li.index() && li.after(li.prev());
	var tr = $("#ckda tr").eq(index);
	tr.find(":checkbox").attr("name","1");
	tr.prev().find(":checkbox").attr("name","2");
	tr.after(tr.prev());
	rsetfm(fmbx1);
	return false;
});
$("#fmt2 .down").live("click",function(){
	var index = $("#fmt2 .down").index($(this)[0])+1;
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	(li.index()<($("li",fmbx1).size()-1)) && li.before(li.next());
	var tr = $("#ckda tr").eq(index);
	tr.find(":checkbox").attr("name","1");
	tr.next().find(":checkbox").attr("name","2");
	tr.before(tr.next());
	rsetfm(fmbx1);
	return false;
});

$(".bjk").live("click",function(){
	editMsg($(this).prevAll(".ipt"));
	return false;
});
$(".fmbx1 .fmclose").live("click",function(){
	$(".fmbx1").size()>1 && $(this).closest(".fmbx1").remove();
	rsetfms();
	return false;
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
$(".iptatk").live("blur",function(){
	var ul = $($(this).closest(".fmt").attr("ul"));
	ordtk($(this),ul,$('#bhxx1').attr('checked'));
});
$(".gjc .close3").live("click",function(){
	var list = $(this).closest(".gjclist"), gjc = $(this).closest(".gjc"), hd = $(".ipt",gjc).next("input");
	$(this).closest("li").remove();
	updtgjc(list,hd);
});
$(".ipt[reg]").live("focus",function(){
	$(this).parent().find(".error1").hide();
});
$(".gjc .ipt").live("blur keypress",function(event){
	if($(this).val()==$(this).attr("df") || (event.keyCode && event.keyCode!=13))return;
	var s = $.trim($(this).val()).split(/ +/ig), hd = $(this).next("input"), list = $(this).closest(".gjc").find(".gjclist");
	$.each(s,function(i,n){
		n && list.append('<li>'+n+'<span class="close3" title="删除"></span></li>');
	});
	$(this).val("");
	updtgjc(list,hd);
});

//更新关键词
function updtgjc(ul,hd){
	//console.log(ul,hd);
	var s = "";
	$("li",ul).each(function(){
		s+=$(this).text()+" ";
	});
	hd.val($.trim(s));
}
//重置选择项的每一行
function rsetfm(fm){
	var index = $(".fmbx1").index(fm);
	var hsize = $(".hor").size();
	var vsize = $(".ver").size();
	$("li",fm).each(function(i){
		if($(this).hasClass("hor")){
			$(".xh",this).html(String.fromCharCode(65+i));
			$(".ipt",this).attr("name","choiceAnswers["+i+"].description");
		}else{
			$(".xh",this).html(1+i);
			$(".ipt",this).attr("name","choiceQuestionDtos["+i+"].choiceQuestion.topic");
			$(".hidid",this).attr("name","choiceQuestionDtos["+i+"].questionDto.question.id");
			$(".hid",this).attr("name","choiceQuestionDtos["+i+"].choiceQuestion.id");
		}
	});
	$("#ckda .order").each(function(i){
		$(this).html(1+i);
	});
	$("#ckda .dx_td1").each(function(i){
		$(this).html(String.fromCharCode(65+i));
	});
	$("#ckda tr").each(function(i){
		if(i==0){
			return true;
		}
		if(i>vsize){
			$(this).remove();
		}
		$("input[type='checkbox']",$(this)).each(function(j,radio){
			$(radio).attr("name","choiceQuestionDtos["+(i-1)+"].choiceQuestion.answer").val(j+1);
			if(j>=hsize){
				$("#ckda tr").eq(0).find("td").eq(j+1).remove();
				$(radio).parent().remove();
			}
		});
	});
}
//重置所有选择项组或者题干组
function rsetfms(n){
	n = n || 1;
	var fmbx = $(".fmbx"+n).each(function(i){
		$(".num",this).html(i+1);
	});
	if(fmbx.size()==1 && !fmbx.hasClass("unfmone")){
		fmbx.addClass("fmone");
	}
}
//初始化一个选择项组
function clearfm(fm){
	$("li:gt(1)",fm).remove();
	$(".ipt",fm).val("");
	$(".num",fm).html($(".fmbx1").index(fm)+1);
	rsetfm(fm);
}
//添加一个选择项组
function addfm(fmbx1){
	fmbx1 = fmbx1 || $(".fmbx1:last");
	var nfm = $(".fmbx1:eq(0)").removeClass("fmone").clone();
	fmbx1.after(nfm);
	clearfm(nfm);
	return nfm;
}
//添加一个题干
var tgnum = 1;
function addfm2(fmbx2){
	fmbx2 = fmbx2 || $(".fmbx2:last");
	var nfm = $(".fmbx2:eq(0)").removeClass("fmone").clone();
	fmbx2.after(nfm);
	var ipta = nfm.find(".ipta").val("");
	rsetfms(2);
	tgnum = tgnum + 1;
	addFck(ipta, tgnum+100);
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
	if(ipt.nextAll(".fn").size()==0){
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
function uploadCallBack(url,partPath,num){
	if(partPath=="image"){
		$("#area"+num).val($("#area"+num).val()+"<img src='"+url+"'/>");
	}else if(partPath=="video"){
		$("#area"+num).val($("#area"+num).val()+"<span class='jpy1' src='"+url+"'></span>");
	}
}
//批量添加描述
function pladdfm(max,min,body) {
	min = Number(min || 0);
	max = Number(String(max) || 50);
	min = Math.min(min,max);
	max = Math.max(min,max);
	body = body || document;
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
	$(".fmbx1",body).each(function(){
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
		$(".fmbx1:gt("+(max-1)+")",body).remove();
		clearfm($(".fmbx1",body));
//		rsetfms(1);
		for(var i=0;i<ar1.length;i++){
			fmbx1 = $(".fmbx1",body);
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
		for(var i=0;i<min-$(".fmbx1",body).size();i++){
			addfm();
		}
		rc_pop.close();
	});
	return rc_pop;
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
		var context_path=getTotalContextPath();
         oFCKeditor.BasePath = context_path+"/js/fckeditor/";
         var tbSet=getTbSet();
         oFCKeditor.ToolbarSet = tbSet;
         oFCKeditor.Width="100%";
         oFCKeditor.Height="217";
         oFCKeditor.Create(next);
    $("input[name='"+name+"']",next).remove();
}
function getTotalContextPath(){
	if(typeof(TOTAL_CONTEXT_PATH)=='undefined'){
			return "";
		}else{
			return TOTAL_CONTEXT_PATH;
		}
}
function getTbSet(){
	if(typeof(FWB_TOOLBAR_NAME)=='undefined'){
		return "Basic";
	}else{
		return FWB_TOOLBAR_NAME;
	}
}

function checkChoice(){
	var result = true;
	var len = 900;
	$(".fmbx1").find("input[type='text']").each(function(){
		var le = lengthLE(this.value,len);
		if(le){
			$(this).siblings(".error1").hide();
		}else{
			$(this).focus().siblings(".error1").html("请输入长度不大于"+len+"的字符").show();
		}
		result = le && result;
	});
	return result;
}
function checkRadio(){
		var result = true;
		var trlen = $("#ckda tr").length-1;
		var ralen = $("#ckda tr").find("input[type='radio'][checked]").length;
		result = (ralen==trlen);
		if(result){
			$("#checkRadio").hide();
		}else{
			popAlert("请选择参考答案");
			$("#checkRadio").show();
		}
		return result;
	}
	
function checkBox(){
	var result = true;
	var i = 0;
	$("#ckda tr").each(function(){
		if(i==0){
			i = i+1;
			return ;
		}
		var len = $(this).find("input[type='checkbox']:checked").size();
		if(len<=0){
			result = false;
		}
	});
	if(result){
		$("#checkRadio").hide();
	}else{
		popAlert("请选择参考答案");
		$("#checkRadio").show();
	}
	return result;
	
}	
	