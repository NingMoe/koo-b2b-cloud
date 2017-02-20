//如果当前页面不在iframe中，则强制放入iframe中。

if(window==top){
	//window.location.href = "/pages/main.jsp?rwin="+window.location.href.replace(/.*?\/([^\/.]*?)\.jsp*/ig,"$1");
}

$(function(){
//	$(".menu li").hover(function(){
//		$("dl",this).addClass("hv");
//	},function(){
//		$("dl",this).removeClass("hv");
//	}).each(function(i){
//		$(this).css({
//			"left":$(this).position().left+"px",
//			"z-index":2+i
//		});
//	}).addClass("posa");
//	$(".ontab").ontab(".ontabfn",".ontabtxt","selected","click");
	$(".fck").each(function(i){
		i = i+1+100;
		addFck(this,i);
		$(this).closest(".fmt").attr("n",i);
	});
});
String.prototype.len=function(){return this.replace(/[^\x00-\xff]/g,"aa").length;};
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
$(".fmbx1 .add").live("click",function(){
	var li = $(this).closest("li"), newli = li.clone();
	newli.find(".ipt").val("");
	newli.find(".xh").html("");
	li.after(newli);
	rsetfm(li.closest(".fmbx1"));
	return false;
});
$(".fmbx1 .remove").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	$("li",fmbx1).size()>2 && fmbx1.attr("rmv",$(".xh",li).html()) && li.remove();
	rsetfm(fmbx1);
	return false;
});
$(".fmbx1 .up").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	$("li:not(.hdtit)",fmbx1).index(li) && li.after(li.prev());
	rsetfm(fmbx1);
	return false;
});
$(".fmbx1 .down").live("click",function(){
	var li = $(this).closest("li"),fmbx1 = li.closest(".fmbx1");
	($("li:not(.hdtit)",fmbx1).index(li)<($("li:not(.hdtit)",fmbx1).size()-1)) && li.before(li.next());
	rsetfm(fmbx1);
	return false;
});
$(".tab4 .edit").live("click",function(){
	var tr = $(this).closest("tr");
	if(typeof(findEditReadUrl)=='function'){
		addtr(tr,true,findEditReadUrl(tr));
	}else{
		addtr(tr,true);
	} 
	return false;
});
$(".tab4 .add").live("click",function(){
	var tr = $(this).closest("tr");
	if(typeof(addReadSubItem)=='function'){
		addReadSubItem(tr);
	}else{
	addtr(tr);
	}
	return false;
});
$(".tab4 .remove").live("click",function(){
	var tr = $(this).closest("tr"), tab = tr.closest("table");
	confirmMsg('确定删除?',function(){
		if(typeof(removeReadSubItem)=='function'){
				removeReadSubItem(tr);
		}
		tr.remove();
		$("tr",tab).size()==1 && tab.addClass("hide2");
	});
	return false;
});
$(".tab4 .up").live("click",function(){
	var tr = $(this).closest("tr");
	tr.index()>1 && tr.after(tr.prev());
	return false;
});
$(".tab4 .down").live("click",function(){
	var tr = $(this).closest("tr"), tab = tr.closest("table");
	(tr.index()<($("tr",tab).size()-1)) && tr.before(tr.next());
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
$(".ipt[reg]").live("focus",function(e,s){
	s || $(this).parent().find(".error1").hide();
	return !s;
});
$(".gjc .ipt").live("blur keypress",function(event){
	var code = event.keyCode || event.which;
	if($(this).val()==$(this).attr("df") || (code && code!=13))return;
	var s = $.trim($(this).val()).split(/[;；]+/ig), hd = $(this).next("input"), list = $(this).closest(".gjc").find(".gjclist");
	$.each(s,function(i,n){
		n = $.trim(n);
		n && n.len()<30 && (!new RegExp("(^|;)"+n+";").test(hd.val())) && list.append('<li>'+n+'<span class="close3" title="删除"></span></li>');
		updtgjc(list,hd);
	});
	$(this).val("");
});

//更新关键词
function updtgjc(ul,hd){
	var s = "";
	$("li",ul).each(function(){
		s+=$(this).text()+";";
	});
	hd.val($.trim(s));
}
//重置选择项的每一行
function rsetfm(fm,num,px){
	num = num || fm.attr("ic")=="num";
	px = px || fm.attr("px")!="no";
	var index = $(".fmbx1").index(fm)+1, lis = $("li:not(.hdtit)",fm), rmv = fm.attr("rmv");
	lis.each(function(i){
		var tpx = px,j = i;
		if($(".xh",this).html()=="" && !tpx){
			tpx = true;
			j = lis.filter(function(){
				return !!$(".xh",this).html();
			}).size();
		}
		tpx && $(".xh",this).html(num?(j+1):String.fromCharCode(65+j));
		$(".ipt",this).attr("name","fmbx"+index+"_"+(num?(i+1):String.fromCharCode(97+i)));
	});
	if(!px && rmv){
		lis.each(function(){
			var a = $(".xh", this).html(), b = rmv;
			if (num) {
				a = Number(a);
				b = Number(b);
			}else{
				a = a.charCodeAt(0);
				b = b.charCodeAt(0);
			}
			if(a>b){
				$(".xh",this).html(num?(a-1):String.fromCharCode(a-1));
				$(".ipt",this).attr("name","fmbx"+index+"_"+(num?(a-1):String.fromCharCode(a-1+32)));
			}
		});
		fm.attr("rmv","");
	}
	rsetfm.callback && rsetfm.callback(fm);
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
function addfm2(fmbx2){
	fmbx2 = fmbx2 || $(".fmbx2:last");
	var fmt = fmbx2.closest(".fmt"), n = fmt.attr("n") || 101;
	var nfm = $(".fmbx2:eq(0)").removeClass("fmone").clone();
	fmbx2.after(nfm);
	var ipta = nfm.find(".ipta").val("");
	rsetfms(2);
	addFck(ipta, ++n);
	fmt.attr("n",n);
}
//插入一个位置
function addwz(ipta,fm,name){
//ipta.val(ipta.val()+'<span class="tb1"></span>');
ordwz(ipta,fm,name);
}
//重新给位置排序
function ordwz(ipta,fm,name){
	var html = $("<div />"), tb1, ul = fm.find("ul"), li;
	html.html(ipta.html());
	tb1 = html.find(".tb1");
	ul.addClass("hide2").children("li:gt(0)").remove();
	tb1.each(function(i){
		$(this).html(i+1);
		li = ul.children("li:last");
		if (i) {
			li.find(".add").click();
			li = ul.children("li:last");
		}
		li.find(".tb1").html(i+1);
		ul.removeClass("hide2");
	});
	name.innerHTML=html.html();
	//ipta.val(html.html());
	rsetfms(1);
}
//插入一个位置
function addwz2(ipta,fm,name){
//ipta.val(ipta.val()+'<span class="tb1"></span>');
ordwz2(ipta,fm,name);
}
//重新给位置排序
function ordwz2(ipta,fm,name){
	var html = $("<div />"), tb1, ul = fm.find("ul"), li;
	html.html(ipta.html());
	tb1 = html.find(".tb4");
	ul.addClass("hide2").children("li:gt(0)").remove();
	tb1.each(function(i){
		var v=$(this).text().replace('[','').replace(']','');
		$(this).val(v);
		li = ul.children("li:last");
		if (i) {
			li.find(".add").click();
			li = ul.children("li:last");
		}
		li.find("input").val(v);
		ul.removeClass("hide2");
	});
	//name.innerHTML=html.html();
	//ipta.val(html.html());
	rsetfms(1);
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
	rc_pop.setContent("alertCon", "<textarea class='ipta2' id='area"+n+"'></textarea><div class='ta_c'><a href='javascript:;' class='btn1'>完成</a></div>");
	rc_pop.setContent("title", "编辑文本");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$("#area"+n).data("ipt",ipt).val(ipt.val()).next().find(".btn1").click(function(){
		var area = $(this).closest("div").prev();
		area.data("ipt").val(area.val());
		rc_pop.close();
	});
	return rc_pop;
}
//弹出的选择学校
function selxx(obj) {
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 660,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1),
	sf = [
		["北京",1],["上海",2],["黑龙江",3],["吉林",4],["天津",5],["安徽",6],["江苏",7],
		["北京",1],["上海",2],["黑龙江",3],["吉林",4],["天津",5],["安徽",6],["江苏",7],
		["北京",1],["上海",2],["黑龙江",3],["吉林",4],["天津",5],["安徽",6],["江苏",7],
		["北京",1],["上海",2],["黑龙江",3],["吉林",4],["天津",5],["安徽",6],["江苏",7],
		["北京",1],["上海",2],["黑龙江",3],["吉林",4],["天津",5],["安徽",6]
	],
	tabs = sf.join("-").replace(/([^-]*?),([^-]*)[-]?/g,"<a href='#link$2'>$1</a>");
	rc_pop.setContent("alertCon", "<div class='winbx1'><div class='tabs mb10'>"+tabs+"</div><ul class='list1 fc mb10'></ul><div class='ta_r'><input type='text' df='输入学校名称' value='输入学校名称' class='ipt'/><a href='javascript:;' class='btn1 ml1'>搜索</a></div></div>");
	rc_pop.setContent("title", "<span class='ft1'>选择学校</span>");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$(".winbx1 .tabs a").click(function(){
		$(this).addClass("sd").siblings(".sd").removeClass("sd");
		var list = $(this).closest(".winbx1").find(".list1"), liststr = "", ajaxdata, lia;
		
		//下面是填充的临时数据，正式环境中请用ajax读取正确数据。
		ajaxdata = [
			["#大学",1],["#航空航天大学",2],["清华大学",3],["中国人民大学",4]
		];
		//复制临时数据，正式环境请删除
		for(var i=0;i<$(this).text().charCodeAt(0)%10;i++){
			ajaxdata= ajaxdata.concat(ajaxdata.slice(0));
		}
		liststr = ajaxdata.join("-").replace(/\#/g,$(this).text()).replace(/([^-]*?),([^-]*)[-]?/g,"<li><a href='#xx$2'>$1</a></li>");
		list.html(liststr);
		lia = list.find("a").click(function(){
			$(obj).html($(this).html()).attr({"tab":$(this).attr("tab"),"li":$(this).closest("li").index()}).next("input:hidden").val($(this).attr("href").replace("#xx",""));
			rc_pop.close();
		}).attr({"tab":$(this).index()});
		if($(obj).attr("tab")==$(this).index() && $(obj).attr("li")){
			lia.eq($(obj).attr("li")).addClass("sd")
		}
	}).eq($(obj).attr("tab") || 0).click();
	return rc_pop;
}
//弹出的选择试题来源
function selst(obj) {
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 660,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1),tabs = "";
	for(var i=65;i<91;i++){
		tabs+="<a href='#link_"+String.fromCharCode(i+32)+"' "+(i%3==2?"class='ml1'":"")+">"+String.fromCharCode(i)+"</a>";
	}
	rc_pop.setContent("alertCon", "<div class='winbx1'><div class='tabs tabs1 mb10'>"+tabs+"</div><ul class='list1 fc mb10'></ul><div class='ta_r'><input type='text' df='输入来源名称' value='输入来源名称' class='ipt'/><a href='javascript:;' class='btn1 ml1'>搜索</a></div></div>");
	rc_pop.setContent("title", "<span class='ft1'>选择试题来源</span>");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$(".winbx1 .tabs a").click(function(){
		$(this).addClass("sd").siblings(".sd").removeClass("sd");
		var list = $(this).closest(".winbx1").find(".list1"), liststr = "", ajaxdata, lia;
		
		//下面是填充的临时数据，正式环境中请用ajax读取正确数据。
		ajaxdata = [
			["#三年模拟一年高考",1],["#公务员考试题",2],["#计算机考级考试三级网络",3],["#国家会计师专业资格考试",4]
		];
		//复制临时数据，正式环境请删除
		for(var i=0;i<$(this).text().charCodeAt(0)%10;i++){
			ajaxdata= ajaxdata.concat(ajaxdata.slice(0));
		}
		liststr = ajaxdata.join("-").replace(/\#/g,$(this).text()).replace(/([^-]*?),([^-]*)[-]?/g,"<li><a href='#xx$2'>$1</a></li>");
		list.html(liststr);
		var lia = list.find("a").click(function(){
			$(obj).html($(this).html()).attr({"tab":$(this).attr("tab"),"li":$(this).closest("li").index()}).next("input:hidden").val($(this).attr("href").replace("#xx",""));
			rc_pop.close();
		}).attr({"tab":$(this).index()});
		if($(obj).attr("tab")==$(this).index() && $(obj).attr("li")){
			lia.eq($(obj).attr("li")).addClass("sd")
		}
	}).eq($(obj).attr("tab") || 0).click();
	return rc_pop;
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
	rc_pop.setContent("title", "<span class='ft1'>批量添加选项（请每行输入一个选项，多组选项以空行分割）</span>");
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
		rsetfms(1);
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
//重新给填空项排序
function ordtk(ipta,ul,bhxx){
	var html = $("<div />"),htmls = {}, newindex = 0, eq, li, lis, mli, tkbox, fmt = ipta.closest(".fmt"), iptas = $(".ipta",fmt), fmbx1 = $(".fmbx1");
	iptas.each(function(i){
		htmls["ipta"+i] = $("<div />").html($(this).val());
		html.append(htmls["ipta"+i]);
	});
	tkbox = html.find(".tkbox");
	tkbox.each(function(){
		if (bhxx) {
			fmbx1.filter("*[index='" + $(this).attr("index") + "']").size() == 0 && !$(this).hasClass("newtkbox") && $(this).remove();
		}
		else {
			ul.find("li[index='" + $(this).attr("index") + "']").size() == 0 && !$(this).hasClass("newtkbox") && $(this).remove();
		}
		tkbox.filter("*[index='"+$(this).attr("index")+"']:gt(0)").remove();
	});
	tkbox = html.find(".tkbox").each(function(i){
		lis = ul.find("li");
		fmbx1 = $(".fmbx1");
		newindex = $(this).attr("index");
		$(this).html("("+(i+1)+")");
		if($(this).hasClass("newtkbox")){
			eq = i;
			if (bhxx) {
				var nfmbx1;
				if(fmbx1.is(".hide2")){
					nfmbx1 = fmbx1.removeClass("hide2");
				}else{
					nfmbx1 = addfm(fmbx1.eq(i-1));
				}
				nfmbx1.attr("index",newindex);
			}
			else {
				li = $('<li index="' + newindex + '"><span class="tktit">(' + (eq + 1) + ')</span><input type="text" name="answer" class="ipt2" /></li>');
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
				if (i) {
					fmbx1.eq(i - 1).after(mli);
				}else{
					fmbx1.parent().prepend(mli);
				}
			}
			else {
				if (i) {
					lis.eq(i - 1).after(mli);
				}else{
					ul.prepend(mli);
				}
				mli.find(".tktit").text('(' + (i+1) + ')');
			}
		}
	});
	iptas.each(function(i){
		$(this).val(htmls["ipta"+i].html());
	});
	if (bhxx) {
		$(".fmbx1").slice(tkbox.size()).remove();
		rsetfms(1);
	}
	else {
		ul.find("li").slice(tkbox.size()).remove();
	}
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



//弹出的编辑评语框
function commentWin(link,tit) {
	tit = tit || "考试";
	var sumScore = $("#sumScore").val();
	if(!sumScore || parseInt(sumScore) == 0){
		popAlert(tit+"总分为0，不能添加评语");
		return false;
	}
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 652,
		isAutoSize : true
	}), tab = $("#commentTab"), trs = $("tr",tab), len = trs.length, v0=v1=v2="0", v3="评语名称",v2=v4 ="";
	var n = parseInt(Math.random()*10000+1),html = '<div class="pyTxt"><div class="fmt">';
	for (var i = 0; i < len || i == 0; i++) {
		if(len){
			v0 = trs.eq(i).find("td:eq(0)").text();
			v1 = trs.eq(i).find("td:eq(1)").text().replace(/^([.0-9]*)\D*[.0-9]*$/ig,"$1");
			v2 = trs.eq(i).find("td:eq(1)").text().replace(/^[.0-9]*\D*([.0-9]*)$/ig,"$1");
			v3 = trs.eq(i).find("td:eq(2)").text();
			v4 = trs.eq(i).find("td:eq(3)").text();
		}
		html += '<div class="fmbx fmbx2 unfmone"><form class="commentForm"><div class="fc">\
				<div class="fl fmfn">\
					<div class="num">'+(i+1)+'</div>\
					<a href="javascript:;" class="fmclose"></a>\
				</div>\
				<div class="fl fc">\
				<div class="fml">分数段：<input type="hidden" name="id" value="'+v0+'" class="ipt"/><input type="text" name="beginScore" readonly="readonly" value="'+v1+'" class="ipt ipt3"/><span> <= x <span class="endfh"><</span></span> <input type="text" name="endScore" value="'+v2+'" class="ipt ipt3"/><input type="text" name="name" df="评语名称" value="'+v3+'" class="ipt ml2"/></div>\
				<textarea name="content" class="ipta">'+v4+'</textarea></div></div><span class="error1"></span>\
				<div class="ta_r">\
					<a href="javascript:;" class="btn1 addpy">增加评语</a>\
				</div></form>\
			</div>';
	}
	html += '</div></div>';
	rc_pop.setContent("alertCon", html+"<div class='ta_c'><a href='javascript:;' id='btn"+n+"' class='btn1'>保存</a><a href='javascript:;' class='btn2 ml1'>取消</a></div>");
	rc_pop.setContent("title", "设置"+tit+"评语");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	var rset = function(){
		$(".pyTxt .endfh").text("<").eq(-1).text("<=");
	},fmclose = function(){
		var size = $(".pyTxt .fmclose").size();
		if(size == 1){
			rc_pop.close();
		}
		setTimeout(rset,100);
	};
	rset();
	$(".pyTxt .ipt3").die().live("change",function(){
		if($(this).attr("readonly"))return;
		var ipt3 = $(this).closest(".pyTxt").find(".ipt3"),
		min = parseInt(ipt3.eq(ipt3.index(this)-1).val());
		if($(this).val()<=min)$(this).val(min+1);
		ipt3.eq(ipt3.index(this)+1).val($(this).val());
	});
	$(".pyTxt .addpy").die().live("click",function(){
		var e1 = $(this).closest(".fmbx2");
		var nfm = $(".fmbx2:eq(0)").removeClass("fmone").clone();
		ipts = nfm.find(".ipt");
		ipts.val("").blur();
		ipts.eq(1).val(e1.find(".ipt:eq(2)").val());
		e1.next().find(".error1").empty().hide();
		e1.next().find(".ipta").show();
		$(".fmclose",nfm).click(fmclose);
		e1.after(nfm);
		nfm.find(".ipta").val("");
		rset();
		rsetfms(2);
		return nfm;
	});
	//$(".pyTxt .fmclose").click(fmclose);
	$(".pyTxt .fmbx2 .fmclose").click(function(){
		var index = $(".fmclose").index(this);
		var p = $(this).parent().parent();
		var f = p.find("input[name='id']");
		var id = $(this).parent().parent().find("input[name='id']").val();
		if(!id || id == 0){
			fmclose();
		}else{
			if(confirm("该评语将被删除且无法恢复，确认继续吗？")){
				$.ajax({
					url: "/maintain/comment/ajaxDelete/"+id,
					type: "post",
					cache: false,
					data: id,
					success: function(data){
						var d =eval("("+data+")");//转换为json对象 
						if(d && d.result == "true"){
							$("#commentTab tr").eq(index).remove();
							var size = $("#commentTab tr").size();
							if(size == 0){
								rc_pop.close();
							}
						}else{
							alert(d.errorMessage);
						}
						
					},
					error: function(data){
						alert("请求失败，请重试！");
					}
				});
			}else{
				return false;
			}
		}
	});	
	$("input[name='endScore']").blur(function(){
		$(this).closest(".fmbx2").next().find("input[name='beginScore']").val(this.value);
	});
	$("#btn"+n).click(function(){
		//验证
		var validate = true;
		var checkScore = document.getElementById("sumScore") ;
		var sScore = -1;
		$(".pyTxt .fmbx2").each(function(i){
			var endScore = $("input[name='endScore']",this);
			var startScore = $("input[name='beginScore']",this);
			var name = $("input[name='name']",this);
			var content = $("textarea",this);
			var error = $(".error1",this);
			error.empty().hide();
			var reg = /^\d+\.?\d?$/; 
			if(!reg.test(endScore.val())){
				error.text("分数必须为正数");
				error.show();
				validate = false;
				return true;
			}
			if(/^\d+\.?\d*$/.test(endScore.val()) && !reg.test(endScore.val())){
				error.text("请保留一位小数");
				error.show();
				validate = false;
				return true;
			}if(checkScore && parseFloat(endScore.val()) > parseFloat(sumScore)){
				error.text("分数不能大于"+sumScore+"分");
				error.show();
				validate = false;
				return true;
			}if(!name.val() || $.trim(name.val()) =="评语名称"){
				error.text("请填写评语名称");
				error.show();
				validate = false;
				return true;
			}if(!content.val() || $.trim(content.val()) ==""){
				error.text("请填写评语内容");
				error.show();
				validate = false;
				return true;
			}
			if(parseFloat(endScore.val()) < parseFloat(startScore.val())){
				error.text("结束分数段小于起始分数段");
				error.show();
				validate = false;
				return true;
			}
			if(sScore == startScore.val()){
				error.text("存在相同的起始分数段");
				error.show();
				validate = false;
				return true;
			}else{
				sScore = startScore.val();
			}
		});
		if(!validate){
			return false;
		}
		var params = "[";
		$(".commentForm").each(function(i,form){
			params = params + $.toJSON($(form).serializeObject())+",";
		});
		params = params + "]";
		$.ajax({
			url: "/maintain/comment/ajaxInsert",
			type: "post",
			cache: false,
			datatype: "application/json",
			//data: params,
			data: {
				0:params
				},
			success: function(data){
				var d =eval("("+data+")");//转换为json对象 
				if(d && d.result == "true"){
					$("#commentIds").val(d.commentIds);
					tab.empty();
					var edit = false;
					var ids = d.commentIds.split(",");
					$(".pyTxt .fmbx2").each(function(i){
						var ipt = $(".ipt,.ipta",this), v0 = ids[i], v1 = ipt.eq(1).val(),v2 = ipt.eq(2).val(),v3 = ipt.eq(3).val(),v4 = ipt.eq(4).val(), s = ipt.eq(1).next().text();
						v1&&v2&& tab.append('<tr><td style="display:none">'+v0+'</td><td>'+v1+s+v2+'</td><td>'+v3+'</td><td>'+v4+'</td></tr>') && (edit = true);
					});
					$(link).text((edit?"修改":"添加")+tit+"评语");
					rc_pop
					.close();
				}else{
					alert(d.errorMessage);
				}
				
			},
			error: function(data){
				alert("请求失败，请重试！");
			}
		});
	}).next().click(function(){
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
/**
 * 
 * @param {Object} ele 当前对象
 * @param {Object} edit  是否为编辑
 */
function addtr(ele,edit,page){
	var page=page||"page14_a.jsp";
	var arg='dialogTop:0;dialogWidth:'+(window.screen.availWidth)+'px;DialogHeight='+(window.screen.availHeight)+'px;help=0;center=1;status:yes;scroll=1';
	if(typeof(showModalDialog)=='function'){
		var html = showModalDialog(page,window,arg);
		if(typeof(readReload)=='function'){
			readReload();
		}
	}else{
		arg='height='+(window.screen.availHeight)+'px,width='+(window.screen.availWidth)+'px,top=0,left=0,toolbar=no,menubar=no,dependent=yes,scrollbars=yes, resizable=yes,location=no, status=no';
		var html=window.open(page,'_blank',arg);
		
	}
	return;
	//window.open(page,"_t");
	if(html&&html.split("##").length==4){
		html=html.split("##");
		if(ele.is("table")){
			ele.removeClass("hide2");
			ele = ele.find("tr:last");
		}
		var ntr = $('<tr>\
				<td class="td41"><a href="javascript:;" class="a1" questionId="'+html[0]+'">'+html[0]+'</a></td>\
				<td class="td44">'+html[1]+'</td>\
				<td class="td42">'+html[2]+'</td>\
				<td class="td45"><span class="fn"><a href="javascript:;" class="edit"></a><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span></td>\
			</tr>');
		ele.after(ntr);
		if(edit){
			ele.remove();
		}
		return ntr;
	}
}