window.jQuery = window.jQuery || window.top.jQuery;
$ = jQuery;
(function($){
$.fn.extend({
ontab: function(s1,s2,cls,sj) {
    return this.each(function() {
    	var msj = $(this).attr("sj") || sj || "mouseover";
		$(this).attr("sj",msj);
		var sd = $(this).find(s1).filter("."+cls),sdn = 0;
		if(sd.length>0)sdn = $(this).find(s1).index(sd);
		$(this).find(s1).bind(msj,{c:cls,s:s2,s1:s1,f:this},function(e){
			var _this = this, id = $(e.data.f).attr("id"), eq = $(e.data.f).find(e.data.s1).index(_this);
			$(_this).siblings().removeClass(e.data.c);
			$(_this).addClass(e.data.c);
			$(e.data.f).find(e.data.s).hide().eq(eq).show();
			$(e.data.f).find("h2 .t").attr("href", $(_this).find("a").attr("href"));
		}).eq(sdn).trigger(msj);
	});
  }
}); 
})(jQuery);
$(".ipt").live("focus",function(){
	$(this).addClass("iptover");
	var df = $(this).attr("df");
	if(df){
		if($(this).val()==df){
			$(this).val("");
		}
	}
}).live("blur",function(){
	$(this).removeClass("iptover");
	var df = $(this).attr("df");
	if(df){
		if($(this).val()==""){
			$(this).val(df);
		}
	}
});
//提示信息
function alertMsg(msg,ty) {
	var ty = ty || "ok", cls;//消息的类型
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : 350,
		isAutoSize : true
	});
	if(ty=="ok")cls = "rightTxt";
	if(ty=='error') cls="errorTxt";
	if(ty=="Warning")cls = "WarningTxt";
	rc_pop.setContent("alertCon", "<span class='"+cls+"'>"+msg+"</span>");
	rc_pop.setContent("title", "提示信息");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$("#confirmConTxt").css({
		"display":"inline-block"
	});
	setTimeout(function(){
		rc_pop.close();
	},2000);
	return rc_pop;
}
//确认信息
function confirmMsg(msg,fn,other) {
	other = other || {};
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		isBackgroundCanClick: true,
		isShowShadow: false,
		width : other.width || 350,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1);
	rc_pop.setContent("alertCon", "<span class='confirmTxt'>"+msg+"</span><div class='popupFn'><a href='javascript:;' id='btn"+n+"' class='btn1'>确定</a><a href='javascript:;' class='btn2 ml1'>取消</a></div>");
	rc_pop.setContent("title", other.title || "操作提示");
	rc_pop.build();
	rc_pop.show();
	rsPopup(rc_pop);
	$("#btn"+n).click(function(){
		fn();
		rc_pop.close();
	}).next().click(function(){
		rc_pop.close();
	});
	$("#confirmConTxt").css({
		"display":"inline-block"
	});
	return rc_pop;
}
//初始化弹出框，设置样式
function rsPopup(p) {
	$("#dialogBoxClose").attr({
		"src":"http://images.koolearn.com/www/subject/2010_0723/blank.gif"
	}).addClass("dialogBoxClose").hover(function(){
		$(this).toggleClass("dialogBoxClose2");
	});
	$("#dialogBoxTitle").css({
		"font-size":"12px",
		"font-weight":"normal"
	});
	$("#dialogBoxTitle,#dialogClose").css({
		"border-bottom":"none"
	}).closest("table").css({
		"background":"rgb(193,234,245)"
	});
	$("#dialogBox").css({
		"border":"rgb(135,135,135) solid 1px"
	});
	$("#confirmConTxt").css({
		"display":"block",
		"margin":"0 auto"
	});
	$("#dialogYES").closest("div").empty().css({
		"margin":"0",
		"height":"10px"
	});
}
//全选复选框
function checkall(all,check){
	var b = $(all).attr("checked");
	$("input:checkbox[name='"+check+"']").attr("checked",b);
}
function closeWindow()
{
window.opener=null;
window.open('', '_self', ''); 
window.close(); 
}

