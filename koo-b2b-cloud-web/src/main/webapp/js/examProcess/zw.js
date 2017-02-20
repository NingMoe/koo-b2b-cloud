window.onerror=function(){   
    arglen=arguments.length;   
    var errorMsg="参数个数："+arglen+"个";   
    for(var i=0;i<arglen;i++){   
    errorMsg+="\n参数"+(i+1)+"："+arguments[i]; 
    }
    //alert(errorMsg); 
    return false; 
}; 
$(function(){
	$(".ZWBZ").each(setBZ);//初始化所以标注
	//可编辑状态
	$(".ZwEdit").bind("mouseup",function(e){
		if($(e.target).closest(".bztit").length)return;
		$(".bztit2 .btn1_b").click();
		if ((rangy.getSelection().getRangeAt(0)+"")!="" && surroundRange()) {
			var setBZ = $("#setBZ").show(),xy = mousePosition(e);
			var wp = setBZ.closest(".wp").width();
			var bzleft = xy.x-setBZ.width()/2;
			/*setBZ.css({"left":bzleft+120,"top":xy.y});*/
			setBZ.css({"left":bzleft-90,"top":xy.y-260});
			
			EmptySelection();
		}
	}).bind("mousedown",function(e){
		if($(e.target).closest(".bztit").length)return;
		$(".bztit2 .btn1_b").click();
	});
	//.bztit所有浮动框
	$(".bztit").click(stopev).mousedown(function(){
		$(this).hide();
	});
	$(".bztitbx").mousedown(stopev);
	$("#setBZ").find(".btn1_b").click(function(){//添加修改标注中的取消链接
		$(this).closest(".bztit").hide();
		var bz = $("#ZWBZ_hover");
		if (bz.attr("edit")){
			bz.removeAttr("edit").removeAttr("id");
		}else {
			bz.upwrapInner();
		}
	}).end().find(".btn1_l").click(function(){//添加修改标注中的确定链接
		if (addBZ()) {
			$(this).closest(".bztit").hide();
		}else{
			alert("请选择错误类型！");
		}
	});
	$(".area3").autoTextarea({"maxHeight":100}).focus(function(ev,pfis){
		if (!pfis) {
			var setPF = $("#setPF").show(), _this = $(this), xy = _this.offset(), data = eval(_this.next(".dn").html()), htmls = "";
			if(setPF.data("area")==this)return;
			$.each(data,function(i,n){
				htmls+='<tr><td class="tw8"><input type="radio" '+(_this.val()==n[0]?'checked="checked"':'')+' name="radioPF" id="radio2_'+i+'"><label for="radio2_'+i+'">\
	                    '+n[0]+'</label></td><td class="ta_r">'+n[1]+'分</td></tr>';
			})
			setPF.find(".tab4").empty().append(htmls);
			setPF.data({
				"area": this,
				"_focus": true,
				"focus": false
			});
			setPF.css({
				"left": xy.left - setPF.width() / 2 + 24,
				"top": xy.top + $(this).height()
			});
		}
		EmptySelection();
	}).blur(function(ev){
		$("#setPF").data("_focus",false);
		setTimeout(function(){
			var setPF = $("#setPF");
			setPF.data("focus") || setPF.data("_focus") || setPF.hide().removeData("area");
			EmptySelection();
		},50);
	});
	$("#setPF .bztitbx").mousedown(function(){
		var setPF = $(this).closest("#setPF");
		setPF.data("focus",true);
	}).click(function(){
		var setPF = $(this).closest("#setPF");
		setTimeout(function(){$(setPF.data("area")).trigger("focus",[true])},30);
	}).find("tr").hover(function(){
		$(this).css("background-color","#eee");
	},function(){
		$(this).css("background-color","");
	}).click(function(){
		$(this).find("input:radio").attr("checked",true);
	}).end().find(".btn1_b").click(function(){//评分描述中的取消链接
		$(this).closest("#setPF").hide().find("input:checked").removeAttr("checked");
		return false;
	}).end().find(".btn1_l").click(function(){//评分描述中的确定链接
		var setPF = $(this).closest("#setPF"),radio = setPF.find("input:checked").removeAttr("checked");
		if(radio.size()==0)return alert("请选择评分描述");
		$(setPF.data("area")).val($.trim(radio.next("label").text())).triggerHandler("blur");
		$(setPF.data("area")).closest("td").next().find("input:text").val($.trim(radio.closest("td").next().text()).replace("分",""));
		setPF.hide();
		return false;
	});
});
$.fn.extend({
	upwrapInner:function(){ //使元素内的内容脱离当前元素
		return this.each(function() {
			if($(this).children().unwrap().length==0){
				$(this).replaceWith($(this).html());
			}
		});
	}
});
(function($){
$.fn.autoTextarea = function(options) {
var defaults={
maxHeight:null,//文本框是否自动撑高，默认：null，不自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
minHeight:$(this).height() //默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
};
var opts = $.extend({},defaults,options);
return $(this).each(function() {
$(this).bind("paste cut keydown keyup focus blur",function(){
var height,style=this.style;
this.style.height =  opts.minHeight + 'px';
if (this.scrollHeight > opts.minHeight) {
if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
height = opts.maxHeight;
style.overflowY = 'scroll';
} else {
height = this.scrollHeight;
style.overflowY = 'hidden';
}
style.height = height  + 'px';
}
});
});
};
})(jQuery);
function stopev(event){ //阻止事件冒泡
	event.stopPropagation();
}
function delBZ(o){ //点击删除链接，删除相应标注
	var bzi = $(o).closest(".bzi"),ZWBZ=bzi.prev(".ZWBZ"),bzs = ZWBZ.find(".ZWBZ");
	bzi.remove();
	var answer = ZWBZ.closest("dt").find(".sc_ans");
	var input = ZWBZ.closest("dt").find(".sc_ans_html");
	var div = ZWBZ.closest("div");
	ZWBZ.children(".fred").remove().end().upwrapInner();
	var divClone = div.clone();
	$(".bzi",divClone).remove();
	input.val(encodeURI(divClone.html()));
	divClone.find(".ZWBZ").each(function(){
		var editipt = $(this).attr("editipt");
		if(!editipt){
			editipt = "";
		}
		$(this).replaceWith($(this).attr("editipt"));
	});
	answer.val(divClone.text());
	resetBZ(bzs);
}
function editBZ(o){//点击修改链接，修改相应标注
	var bzi = $(o).closest(".bzi"),ZWBZ=bzi.prev(".ZWBZ"),setBZ = $("#setBZ").show(),xy = bzi.offset();
	xy.left+=bzi.width()/2;
	setBZ.css({"left":xy.left-setBZ.width()/2+120,"top":xy.top});
	clearBZform();
	setBZ.find("label:contains('"+ZWBZ.attr("tit")+"')").prev("input:radio").attr("checked",true);
	$(".editipt",setBZ).val(ZWBZ.attr("editipt"));
	ZWBZ.attr({"edit":true,"id":"ZWBZ_hover"});
}
function restoreBZ(bz){//还原一个标注，使其处于未解析状态
	bz.next(".bzi").remove();
	bz.attr("title",bz.attr("tit")).removeAttr("tit").removeAttr("style").children(".fred").remove();
}
function resetBZ(dm){//校验并重置指定范围内的标注
	dm = $(dm);
	if(dm.is(".ZWBZ")){
		dm.each(function(){
			restoreBZ($(this));
			setBZ.apply(this);
		})
	}else{
		dm.find(".ZWBZ").each(function(){
			resetBZ(this);
		})
	}
}
function clearBZform(){//清空添加标注的表单数据
	$("#setBZ input:radio").removeAttr("checked");
	$("#setBZ input:text").val("");
}
function setBZ(){//解析当前标注元素,this需要为.ZWBZ
	var edit=$(this).closest(".ZW").is(".ZwEdit"), editstr = "";
	if(edit){
		editstr = "<span class='fr pl30 a3 ft8'><a href='javascript:;' onclick='editBZ(this)'>编辑</a><span class='fg'>|</span><a href='javascript:;' onclick='delBZ(this)'>删除</a></span>";
	}
	$(this).prepend("<span class='fred'>[</span>").append("<span class='fred'>]</span>").after($('<span class="bzi"><div class="bztit">\
				<span class="bzticon"></span>\
				<span class="bztitbx">\
					<b class="bztlt"></b><b class="bztrt"></b><b class="bztlb"></b><b class="bztrb"></b>\
					<strong class="lh30">'+editstr+$(this).attr("title")+'</strong>\
					<span class="lh18 db">'+$(this).attr("content")+'</span>\
				</span>\
			</div></span>').hover(function(){
					$(".bzi").css("z-index","12");
					$(this).css("z-index","13"); 
					$(".bztit",this).show();
				},function(){
					$(".bztit",this).hide();
					$(this).css("z-index","12");
				}).find(".bztitbx").hover(null,function(){
					$(this).closest(".bztit").hide();
				}).end().css("z-index","12"));
	var f = $(this).parent(".ZWBZ"), c = [255,230,230];
	if(f.size()){
		c = f.data("bgc").join(",").split(",");
		c[1]=c[2]=Math.max(c[1]-40,0);
	}
	$(this).css("background-color","rgb("+c.join(",")+")").data("bgc",c).attr("tit",$(this).attr("title")).removeAttr("title").removeAttr("edit");
}
function addBZ(){//添加修改一个标注
	var ZWBZ = $("#ZWBZ_hover"), radio = $("#setBZ input[name='radioBZ']"),title,id,editipt = $("#setBZ .editipt").focus().val(),content="";
	var input = ZWBZ.closest("dt").find(".sc_ans_html");
	var answer = ZWBZ.closest("dt").find(".sc_ans");
	radio.each(function(){
		if($(this).attr("checked")){
			title = $.trim($(this).next("label").attr("data-tit"));
			id = $(this).attr("id").replace("radio1_","");
			$(this).removeAttr("checked");
		}
	});
	if(!title)return false;
	var bz = $("#ZWBZ_hover");
	if (bz.attr("edit")){
		bz.next(".bzi").remove();
		bz.children(".fred").remove();
	}
	$("#setBZ .editipt").val("");
	content = ZWBZ.text();
	if(title=="修改"){
		content = '<span class="fred1">[</span>' + content +'<span class="fred1">]</span>' + " 修改为 " + '<span class="fred1">[</span>' + editipt + '<span class="fred1">]</span>';
	}else{
		editipt = "";
		content = "删除 " + '<span class="fred1">[</span>' + content +'<span class="fred1">]</span>';
	}
	ZWBZ.attr({
		"title":title,
		"content":content,
		"editipt":editipt,
		"id":null,
		"sid":id
	});
	setBZ.apply(ZWBZ[0]);
	resetBZ(ZWBZ.find(".ZWBZ"));
	var divClone = ZWBZ.closest("div").clone();
	divClone.find(".bzi").remove();
	input.val(encodeURI(divClone.html()));
	divClone.find(".ZWBZ").each(function(){
		$(this).replaceWith($(this).attr("editipt"));
	});
	answer.val(divClone.text());
	return true;
}
function getZWContent(){
	var ZW = $(".ZW").clone(),zwcontent = $("#ZW_Content");
	$(".ZWBZ",ZW).each(function(){
		restoreBZ($(this));
	});
	zwcontent.val(ZW.html());
	//alert(zwcontent.val());
	return zwcontent.val();
}
function getZWJson(){
	var ZW = $(".ZW").clone(),json = "[";
	$(".ZWBZ",ZW).each(function(){
		restoreBZ($(this));
	}).each(function(){
		var ob = "{";
		ob += '"id":"'+$(this).attr("sid")+'"';
		ob += ',"title":"'+$(this).attr("title")+'"';
		ob += ',"content":"'+$(this).attr("content").replace(/"/g,'\\"')+'"';
		ob += ',"text":"'+$(this).text().replace(/"/g,'\\"')+'"';
		ob+="},";
		json+=ob;
	});
	json = json.slice(0,-1);
	json += "]";
	return json;
}
function RemoveSelection(){//删除选中的文字
    if (window.getSelection) {
        var selection = window.getSelection();
        selection.deleteFromDocument();
        if (!selection.isCollapsed) {
            var selRange = selection.getRangeAt(0);
            selRange.deleteContents();
        }
        if (selection.anchorNode) {
            selection.collapse(selection.anchorNode, selection.anchorOffset);
        }
    }
    else {
        if (document.selection) {
            document.selection.clear();
        }
    }
}
function EmptySelection(){//取消文字选中状态
   if(document.selection){
       document.selection.empty();
   }else{
     var selection = document.getSelection();
     selection.removeAllRanges();
  }
}
function mousePosition(ev){//获取鼠标位置
    if (ev.pageX || ev.pageY) {
        return {
            x: ev.pageX,
            y: ev.pageY
        };
    }
    return {
        x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
        y: ev.clientY + document.body.scrollTop - document.body.clientTop
    };
}
//rangy
function getFirstRange() {
    var sel = rangy.getSelection();
    return sel.rangeCount ? sel.getRangeAt(0) : null;
}
function surroundRange() {
    var range = getFirstRange();
    if (range) {
        var el = $("<span class='ZWBZ' id='ZWBZ_hover'></span>")[0];
        try {
            range.surroundContents(el);
        } catch(ex) {
            if ((ex instanceof rangy.RangeException || Object.prototype.toString.call(ex) == "[object RangeException]") && ex.code == 1) {
				alert("不支持内容交叉或跨段落选择，请选择正确的内容。");
			}else {
				alert("程序执行错误或浏览器版本太低，请刷新页面重试，或者使用其它浏览器。");
			}
			EmptySelection();
			return false;
        }
		return true;
    }else{
		return false;
	}
}  
