// JavaScript Document
//获取个域名路径
//var aUrlPath = "http://"+window.location.host;
var aUrlPath = "";//"http://"+window.location.host;

//产生多个重复的字符,n为产生的个数
String.prototype.copyN=function(n){
	var s="";
	n=n || 1;
	for(var i=0;i<n;i++){
		s+=this;
	}
	return s;
};
//格式化数字,n为最小长度，s为前导填充字符
Number.prototype.formatLen=function(n,s){
	s=s || "0",n=n || 0;
	var l=String(this).length;
	if(n==0 || l>=n){
		return this+"";	
	}else{
		return s.copyN(n-l)+this;
	}
};
$(function(){
	$(window).scroll(function(){
		var wst = $(window).scrollTop()>=67, hbx = $(".hbx1"), hbx2 = hbx.next(".hbx2,.wp"), rnav = $(".rnav"), fixed = hbx.hasClass("fixed"), niceback = window.niceback || function(){};
		if (wst) {
			if(!fixed){
				hbx.addClass("fixed");
				hbx2.addClass("hbx2_f");
				rnav.size() && rnav.addClass("rnavf") && $(window).resize(), niceback(true);
			}
		}else{
			fixed && (hbx.removeClass("fixed") && hbx2.removeClass("hbx2_f") && rnav.size() && rnav.removeClass("rnavf") && $(window).resize(), niceback(false));
		}
	});
	$(".rnav").size() && $(window).resize(function(){
		var rnav = $(".rnav"), fixed = rnav.hasClass("rnavf");
		if (fixed) {
			rnav.css("right",($("body").width()-960)/2+"px");
		}else{
			rnav.css("right","");
		}
	});
	rsetTs();

	$(".jpy2").each(function(){
		var self = this;
		if(!$(this).is("div")){
			self = $('<div class="jpy2" src="'+$(this).attr("src")+'"></div>');
			$(this).replaceWith(self);
		}
		jpy2.call(self);
	});
	$(".jpy3").each(function(){
		var self = this;
		if(!$(this).is("div")){
			self = $('<div class="jpy3" src="'+$(this).attr("src")+'"></div>');
			$(this).replaceWith(self);
		}
		jpy3.call(self);
	});
	
    $( ".dragfrom li:not(.bl)" ).draggable({
        appendTo: "body",
		addClasses: false,
        helper: "clone"
    });
    $( ".dragto li" ).droppable({
		addClasses: false,
        drop: dropFn
        ,over: function( event, ui ) {
			$(this).addClass("dropover");
        }
        ,out: function( event, ui ) {
			$(this).removeClass("dropover");
        }
		/**end*/
    });
    function dropFn( event, ui ) {
    	//console.log(ui.draggable);
		$(this).data("form") && $(this).find(".remove").click();
		var bx2 = $(".ft9",this).size();
        $(this).data("form",ui.draggable);
		if(bx2){
			$(".ft9",this).hide();
			$(this).append( '<span class="ct">'+ui.draggable.text()+'<span class="remove"></span></span>' );
		}else{
			$(this).html( '<span class="ct">'+ui.draggable.text()+'<span class="remove"></span></span>' );
		}
		ui.draggable.addClass("bl").draggable({ disabled: true });
		/**add 2013-01-13*/
		$(this).removeClass("dropover");
		$(this).append("<input type='hidden' name='"+(ui.draggable.parent().attr("name") || $(this).parent().attr("name"))+"' value='"+ui.draggable.attr("val")+"' />");
    };
    $( ".dragfrom li[toIndex!='']" ).each(function(e){
    	var toIndex = $(this).attr("toIndex"), toIndex1=false, toIndex2 = 0;
    	if(typeof(toIndex)=='undefined'){
    		return ;
    	}
    	if(/^\d+$/g.test(toIndex)){
    		toIndex2 = toIndex;
    	}else{
    		toIndex = toIndex.split("-");
    		toIndex1 = toIndex[0];
    		toIndex2 = toIndex[1];
    	}
    	var fs = (toIndex1!==false?"[name="+toIndex1+"]":""), dragtoli  = $(this).closest(".drag").find(".dragto"+fs).children().eq(toIndex2);
    	dropFn.call(dragtoli,e,{draggable:$(this)});
    });
	$(".dragsort").sortable();
	$(".bc").each(function(){
		initBc($(this));
		$(".bch",this).click(function(){
			showtit3($("#tit3_1"),$(".bch2h",this));
		}).click();
	});
	$(".zhktab").click();	
	$(".st span").each(function(i){
		if($(this).hasClass('tb4')){
			$(this).find('span').each(function(j){
				if($(this).hasClass('hot')){
					$(this).remove();
				}
			});
		}
	});
	
});
function showtit3(tit,ele){
	tit.css({top:0,left:0}).show();
	tit.css({
		top:ele.offset().top-tit.offset().top-tit.height()+1+"px",
		left:ele.offset().left-tit.offset().left-tit.width()/2+ele.width()/2+"px"
	});
	return tit;
}
$(".tit3 .close").live("click",function(){
	$(this).closest(".tit3").hide();
});
function jpy2(){
	var file = $(this).attr("src");
	if(!file)return;
	var i = jpy2.i || 0;
	jpy2.i = ++i;
	$(this).attr("index",i);
	var jpdom = $(this).next(".jp-jplayer");
	if(jpdom.size()==0){
		jpdom = $('<div class="jp-jplayer"></div>');
		$(this).addClass("jp-audio").html('<div class="jp-type-single">\
			<div class="jp-gui jp-interface">\
				<ul class="jp-controls">\
					<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>\
					<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>\
					<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>\
					<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>\
					<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>\
					<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>\
				</ul>\
				<div class="jp-progress">\
					<div class="jp-seek-bar">\
						<div class="jp-play-bar"></div>\
					</div>\
				</div>\
				<div class="jp-volume-bar">\
					<div class="jp-volume-bar-value"></div>\
				</div>\
				<div class="jp-time-holder">\
					<div class="jp-current-time"></div>\
					<div class="jp-duration"></div>\
					<ul class="jp-toggles">\
						<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>\
						<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>\
					</ul>\
				</div>\
			</div>\
		</div>');
		$(this).after(jpdom);
	}
		jpdom.jPlayer({
			ready: function () {
				$(this).jPlayer("setMedia", {
					mp3:findRightFile(file)
				});
			},
                        solution: "flash, html",
                        swfPath: "/jPlayer/js",
			cssSelectorAncestor: ".jpy2[index="+i+"]",
			wmode: "window"
		});
}
/**wav.wma 2 mp3 wp -add sine 20130318*/
function findRightFile(f){
	if(f&&f.lastIndexOf('.wav')==(f.length-4)){
		f=f.replace(/\.wav/,".mp3");
	}else if(f&&f.lastIndexOf('.wma')==(f.length-4)){
		f=f.replace(/\.wma/,".mp3");
	}
	return f;
}
function jpy3(){
	var file = $(this).attr("src");
	if(!file)return;
	var i = jpy3.i || 0;
	jpy3.i = ++i;
	jpy3['width'] = jpy3['width'] || 640;
	jpy3['height'] = jpy3['height'] || 360;
	$(this).attr("index",i);
	var jpdom = $(this).find(".jp-jplayer");
	if(jpdom.size()==0){
		$(this).addClass("jp-video").html('<div class="jp-type-single">\
			<div class="jp-jplayer"></div>\
			<div class="jp-gui">\
				<div class="jp-video-play">\
					<a href="javascript:;" class="jp-video-play-icon" tabindex="1">play</a>\
				</div>\
				<div class="jp-interface">\
					<div class="jp-progress">\
						<div class="jp-seek-bar">\
							<div class="jp-play-bar"></div>\
						</div>\
					</div>\
					<div class="jp-current-time"></div>\
					<div class="jp-duration"></div>\
					<div class="jp-controls-holder">\
						<ul class="jp-controls">\
							<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>\
							<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>\
							<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>\
							<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>\
							<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>\
							<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>\
						</ul>\
						<div class="jp-volume-bar">\
							<div class="jp-volume-bar-value"></div>\
						</div>\
						<ul class="jp-toggles">\
							<li><a href="javascript:;" class="jp-full-screen" tabindex="1" title="full screen">full screen</a></li>\
							<li><a href="javascript:;" class="jp-restore-screen" tabindex="1" title="restore screen">restore screen</a></li>\
							<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>\
							<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>\
						</ul>\
					</div>\
				</div>\
			</div>\
			<div class="jp-no-solution">\
				<span>Update Required</span>\
				To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.\
			</div>\
		</div>');
		jpdom = $(this).find(".jp-jplayer");
	}
	jpdom.jPlayer({
		ready: function () {
			$(this).jPlayer("setMedia", {
				m4v: file,
				poster: "/css/img/sp.jpg"
			});
		},
		swfPath: aUrlPath+"/jPlayer/js",
		supplied: "m4v",
		cssSelectorAncestor: ".jpy3[index="+i+"]",
		size: {
			width: jpy3['width']+"px",
			height: jpy3['height']+"px",
			cssClass: "jp-video-"+(jpy3['height']>=360?360:270)+"p"
		}
	});
}
function save(){
	var ele = dialog1("正在保存答案…","save");
	setTimeout(function(){
		dialog1(false,"save");//此处用延时模拟了保存成功后关闭提示，正式流程中，请将这一行写到保存成功后的回调函数中。
	},3000);
}
function dialog1(s,name){
	var ele = dialog1[name];
	if(!s){
		ele && ele.dialog("destroy") && ele.remove();
		dialog1[name] = null;
		return;
	}
	if (!ele) {
		ele = $("<div class='dialog1'>" + s + "</div>");
		$("body").append(ele);
		dialog1[name] = ele;
	}
	var st = $(window).scrollTop();
	ele.dialog({ draggable: false,closeText: "", disabled: true, title: false,minHeight: 20,minWidth: 100,position: ["center","center"] });
	$(window).scrollTop(st);
	return ele;
}
//暂停考试弹框
function Pause(title,time1,time2,closeurl) {
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		width : 380,
		isAutoSize : true
	});
	var n = parseInt(Math.random()*10000+1);
	rc_pop.setContent("alertCon", "<div class='ft8 mb10'>"+title+"</div><div class='ta_c mb20 ft4'><div class='mb20'>答题时间："+time1+"分钟<span class='ml1'>已用时间："+time2+"分钟</span></div><div><a href='javascript:;' id='btn"+n+"' class='btnq1'>开始考试</a><a href='#' class='a1 ml2' onClick='closeWindow()'>关闭考试</a></div></div>");
	rc_pop.setContent("title", "Koolearn测试练习系统");
	rc_pop.build();
	rc_pop.show();
	$("#btn"+n+",#dialogBoxClose").click(function(){
		var ztTime = $(".ztTime");
		ztTime.click();
		ztTime.after(ztTime.clone(true)).remove();
		rc_pop.close();
	});
	$("#dialogYES").parent().remove();
	
	
	var o = $("#dialogBox"), w = $(window), wtop, cleft, ctop;
	if(window!=top){
		wtop = $(parent);
		cleft = (w.width()-o.width())/2;
		ctop = Math.max((wtop.height()-($(parent.document.getElementById('frameContent')).offset().top - wtop.scrollTop())-o.height())/2,0);
	}else{
		cleft = (w.width()-o.width())/2+w.scrollLeft();
		ctop = (w.height()-o.height())/2+w.scrollTop();
	}
	o.show().css({
		left: cleft,
		top: ctop
	});
	return rc_pop;
}
function limitTimeSubmit(exType){
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		width : 420,
		isAutoSize : true
	});
	rc_pop.setContent("alertCon", "<div class='ft8 ta_c mb10'>"+$("#testName").val()+"</div><div >\
		你的时间已用完，点击提交练习查看解答，或者重新开始本练习。\
	</div><div class='ta_c ft4 mb20'><a href='#' class='btnq1' onClick='submit()'>提交练习</a><a href='#' class='a1 ml2' onClick='gotoNew()' >重新开始</a></div>");
	rc_pop.setContent("title", "Koolearn测试练习系统");
	rc_pop.build();
	rc_pop.show();
	$("#dialogYES").parent().remove();
	$("#dialogClose").remove();
	return rc_pop;
}
function changeLimit(){
	$("#unLimit").val(1);
	 window.parent.document.body.removeChild(window.parent.document.getElementById("dialogCase"));
}
$(".dragfrom li:not(.bl)").live("mouseover",function(){
	$(this).addClass("ul1_hv");
}).live("mouseout",function(){
	$(this).removeClass("ul1_hv");
});
$(".dragto .ct").live("mouseover",function(){
	$(this).closest("li").addClass("hv");
}).live("mouseout",function(){
	$(this).closest("li").removeClass("hv");
});
$(".dragsort li").live("mouseover",function(){
	$(this).addClass("hv");
}).live("mouseout",function(){
	$(this).removeClass("hv");
});
$(".dragto .remove").live("click",function(){
	var li = $(this).closest("li"), bx2 = $(".ft9",li).size();
	li.data("form").removeClass("bl").draggable({ disabled: false });
	if(bx2){
		$(".ft9",li).show();
		$(".ct",li).remove();
	}else{
		li.html("&nbsp;");
	}
	li.find("input:hidden").remove();
	li.data("form",false);
});
$(".st .tb1").live("mouseover",function(){
	$(this).addClass("tb1_hv");
}).live("mouseout",function(){
	$(this).removeClass("tb1_hv");
}).live("click",function(){
	var st = $(this).closest(".st"), txt = st.find(".ft8").text();
	$(".tb1:hidden",st).show().next("strong").remove();
	$(this).hide().after("<strong>"+txt+"</strong>");
	$(this).closest("div .mb10").children("input:hidden").val($(this).text()-1);
});
//单选点句   点句多选
$(".st .tb4").live("mouseover",function(){
	$(this).addClass("tb4_hv");
}).live("mouseout",function(){
	$(this).removeClass("tb4_hv");
}).live("click",function(){
	var st = $(this).closest(".st"), checkbox = (st.attr("checkbox")=='true');
	var p = $(this).closest("div"), data = p.attr("data"), ipt = p.children("input:hidden"),tb4 = $(this);
	if(checkbox){
		if(tb4.hasClass("tb4_cl")){
			tb4.removeClass("tb4_cl");
			return false;
		}
	}
	else{
		$(".tb4_cl",st).removeClass("tb4_cl");
	}
	tb4.addClass("tb4_cl");
	eval("data="+data);
	var md5=$.md5(tb4.text());
	$.each(data,function(i,n){
		if(n[0]==md5){
			ipt.val(n[1]);
		}
	});
	return false;
});

$(".bx3 dd").live("click",function(){
	$(this).addClass("sd").siblings().removeClass("sd");
	$(this).parent().children("dt").children("input:hidden").val($(this).attr("value"));
});
$(".toggle").live("click",function(){
	var ele = $(this).closest("div").prev();
	if($(this).hasClass("toggleOpen")){
		$(this).removeClass("toggleOpen").text("展开");
		ele.addClass("hd");
	}else{
		$(this).addClass("toggleOpen").text("收起");
		ele.removeClass("hd");
	}
});
$(".dAn label").live("hover",function(event){
	if (event.type =='mouseover'){
		$(this).addClass("hv");
    }else {
		$(this).removeClass("hv");
    }
}).live("click",function(){
	var ipt=$(this).parent().find("label input");
	ipt.parent().removeClass("sd");
	ipt.filter(":checked").parent().addClass("sd");
	rsetTs();
});
$(".zhk").live("click",function(){
	var area = $(this).closest("div").prev("textarea").scrollTop(1000);
	if($(this).hasClass("zhk2")){
		$(this).removeClass("zhk2").text("展开");
		area.css("height","");
	}else{
		$(this).addClass("zhk2").text("收起");
		area.scrollTop() && area.css("height",area.scrollTop()+area.outerHeight()+"px");
	}
});
$(".zhktab").live("click",function(){
	var tab = $(this).closest("div").prev("table"), max = tab.attr("maxtr");
	if($(this).hasClass("zhk2")){
		tab.find("tr:hidden").show();
	}else{
		tab.find("tr:gt("+max+")").hide();
	}
});
$(".yyjx").live("click",function(event){
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		width : 480,
		height : 180,
		isAutoSize : true
	});
	rc_pop.setContent("alertCon", '<div class="jpy2" src="'+$(this).attr("src")+'"></div>');
	rc_pop.setContent("title", "语音解析");
	rc_pop.build();
	rc_pop.show();
	$("#dialogYES").parent().remove();
	jpy2.call($("#confirmConTxt .jpy2"));
	event.preventDefault();
});
$(".spjx").live("click",function(event){
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		width : 700,
		height : 510,
		isAutoSize : true
	});
	rc_pop.setContent("alertCon", '<div class="jpy3" src="'+$(this).attr("src")+'"></div>');
	rc_pop.setContent("title", "视频解析");
	rc_pop.build();
	rc_pop.show();
	$("#dialogYES").parent().remove();
	var confirmConTxt = $("#confirmConTxt").css("display","block");
	confirmConTxt.parent().css("padding-top","0");
	jpy3.call($(".jpy3",confirmConTxt));
	event.preventDefault();
});
$(".bx5+.btnq1").live("click",function(){
	var bx5 = $(this).prev();
	var subid=bx5.attr("id");
	var index=bx5.attr("name");
	if($(".ipt",bx5).val()==null || $(".ipt",bx5).val()=="") return false;
	var score=$(".ipt",bx5).val();
	selfRatedSave(index,subid,score);
	bx5.text($(".ipt",bx5).val()+"分").removeClass("dis_ib");
	$(this).hide();
});
$(".bx5:not(.dis_ib)").live("click",function(){
	var btnq1 = $(this).next(".btnq1").show();
	if(btnq1.size()==0){
		btnq1 = $('<a href="javascript:;" class="btnq1">确认</a>');
		$(this).after(btnq1);
	}
	$(this).html('<input type="text" class="ipt" value="'+$(this).text().replace(/[^.0-9]/g,"")+'" /><span class="bx5a">分</span>').addClass("dis_ib");
});
$(".bx5 .ipt").live("keypress",function(event){
	var code = event.keyCode || event.which;
	if(code && code==13){
		$(this).closest(".bx5").next(".btnq1").click();
	}else{
		var ipt = $(this);
		setTimeout(function(){
			ipt.val(ipt.val().replace(/[^.0-9]/g,""));
		},10);
	}
});
var tktfn = function(){
	var sleft = $(this).scrollLeft(1000).scrollLeft(), n = +($(this).attr("n") || "4"), wid;
	if($.browser.mozilla){
		wid = inputMM.text($(this).val()).width();
		sleft = Math.max(wid-$(this).width(),0);
	}
	if(sleft && n>1){
		$(this).removeClass("tkt"+n).addClass("tkt"+(n/=2));
		$(this).attr("n",n);
		if(n==1){
			$(this).parent().addClass("tktb1");
		}
		return true;
	}
};
$(function(){
	window.inputMM = $('<span id="inputMM" style="position:absolute;left:-10000px;"></span>');
	$("body").append(inputMM);
});
$(".tkt").live("keyup change",tktfn).live("blur",function(){
	var n = +($(this).attr("n") || "4"), b = true;
	while(n<4 && b){
		if(n==1){
			$(this).parent().removeClass("tktb1");
		}
		$(this).removeClass("tkt"+n).addClass("tkt"+(n*=2)).attr("n",n);
		b = !tktfn.call(this);
		n = +($(this).attr("n") || "4");
	}
});
$(".jpy1").live("click",function(){
	var jpyfn = $(".jpyfn",this);
	if($(this).hasClass("jpy1_2")){
		playMp3($(this),true);
		$(this).addClass("jpy1_1").removeClass("jpy1_2");
		jpyfn.animate({
		   width: 0
		 }, 400);
	}else{
		if (!jpyfn.size()) {
			jpyfn = $("<div class='jpyfn'>\
		<span class='jia'></span><span class='jian'></span><span class='jpylt'></span><span class='jpyct' style='width:67px;'></span><span class='jpyrtbx'><span class='jpyrt' style='left:61px;'></span></span>\
		</div>").click(function(event){
				event.stopPropagation();
			});
			$(".jia",jpyfn).click(function(){
				var jplayer = $("#jplayer");
				var yl = jplayer.jPlayer("option","volume");
				setjpy1volume($(this).closest(".jpyfn"),yl+0.1);
			});
			$(".jian",jpyfn).click(function(){
				var jplayer = $("#jplayer");
				var yl = jplayer.jPlayer("option","volume");
				setjpy1volume($(this).closest(".jpyfn"),yl-0.1);
			});
			$(this).append(jpyfn);
			var xy1 = jpyfn.offset(),xy2 = $(".jpyrt",jpyfn).offset();
			$(".jpyrt",jpyfn).draggable({
				axis: "x",
				containment: "parent",
				scroll: false,
		        drag: function( event, ui) {
					setjpy1volume($(this).closest(".jpyfn"),ui.position.left/84);
		        }
		    });
			$(".jpyrtbx",jpyfn).click(function(event){
				setjpy1volume($(this).closest(".jpyfn"),(event.offsetX-6)/84);
			});
		}
		jpyfn.animate({
		   width: 126
		 }, 400);
		playMp3($(this));
	}
}).live("mouseover",function(){
	clearTimeout($(this).data("hidefn"));
	if ($(this).hasClass("jpy1_2")) {
		$(".jpyfn",this).animate({
		   width: 126
		 }, 400);
	}
	else {
		$(this).addClass("jpy1_1");
	}
}).live("mouseout",function(){
	var jpyfn = $(".jpyfn",this);
	$(this).removeClass("jpy1_1").data("hidefn",setTimeout(function(){
		jpyfn.animate({
		   width: 0
		 }, 400);
	},3000));
});
//调整音量大小
function setjpy1volume(jpyfn,volume){
	var jplayer = $("#jplayer");
	volume = volume || jplayer.jPlayer("option","volume");
	volume = Math.min(Math.max(volume,0),1);
	jplayer.jPlayer("option","volume",volume);
	$(".jpyct",jpyfn).width(84*volume);
	$(".jpyrt",jpyfn).css("left",84*volume+"px");
}
/**
 * 播放一个音频
 * @param {Object} file	mp3文件的路径
 * @param {Object} stop	结束播放
 */
function playMp3(ele,stop){
	var jplayer = $("#jplayer"), mfn = function(){
		return $(this).jPlayer("setMedia", {
			mp3:findRightFile(file)
		}).jPlayer("play").data("ele",ele);
	};
	if(stop){
		jplayer.jPlayer("stop");
		return jplayer;
	}
	var file = ele.attr("src");
	$(".jpy1_2").removeClass("jpy1_2");
	ele.addClass("jpy1_2").removeClass("jpy1_1");
	if(jplayer.size()==0){
		jplayer = $("<div id='jplayer'></div>");
		$("body:last").append(jplayer);
	}
	if(jplayer.attr("load")=="true"){
		mfn.call(jplayer);
	}else{
		jplayer.jPlayer({
			ready: function () {
				mfn.call(this).attr("load","true");
			},
			ended: function () {
				$(this).data("ele").removeClass("jpy1_1 jpy1_2");
				$(".jpyfn",$(this).data("ele")).animate({
					   width: 0
					 }, 400);
			},
			volume: 0.8,
			customCssIds: true,
			//swfPath: aUrlPath+"/jPlayer/js",
			solution: "flash, html",
			swfPath: "/jPlayer/js",
			supplied: "mp3,wav,wma,avi",
			preload: "none",
			wmode: "window"
		 });
	}
	setjpy1volume($(".jpyfn",ele));
	return jplayer;
}
//jPlayer.event.ended
//设置状态栏的数字
function rsetTs(){
	var write = $(".write"), max = +write.attr("max"), min = +write.attr("min"), n=0, s1 = $(".ft1:eq(0)",write), s2 = $(".ft1:eq(1)",write), s3 = $(".ft2:eq(0)",write);
	$(".st").each(function(){
		switch($(this).attr("ty")){
			case "0"://选择题
			if ($(this).find(".dAn :checked").size()) {
				n++;
			}
			break;
		}
	});
	s1.text(min+n);
	s2.text(max);
	s3.text(max-min-n);
}
function initBc(ele){
	eval('var data = '+ele.attr("data")+',mdata = '+ele.attr("mdata"));
	var len = data.length , tot = data[len-1][0], sta = data[0][0];
	var html = '<span class="s1 s1t">分数</span><span class="s1 s1b">人数</span><div class="bck0">';
	$.each(data,function(i,n){
		if(i<len-1 && i>0){
			html += '<div class="bck1" style="width:'+((data[i][0]-sta)/(tot-sta)*100)+'%">';
		}
		if(data[i].length>1){
			var lcla = (i==len-1?" s0a":"");
			if(i == 0 || i ==10){
				html += '<span class="s0'+lcla+' s0t">'+data[i][0]+'分</span><span class="s0'+lcla+' s0b">'+data[i][1]+'人</span>';
			}
			else{
				html += '<span class="s0'+lcla+' s0t">'+data[i][0]+'分</span>';
			}
		}
		if(i<len-1 && i>0){
			html += '</div>';
		}
		
	});
	html += '</div><div class="bch"><div class="bch1"><div class="bch1a"></div><div class="bch1b"></div></div>';
	html += '<div class="bch2" style="width:'+((mdata[0]-sta)/(tot-sta)*100)+'%">';
	html += '<div class="bch2a"></div><div class="bch2b"></div><div class="bch2h">';
	html += '<span class="s0 s0a s0t">'+mdata[0]+'分</span><span class="s0 s0a s0b">'+mdata[1]+'人</span>';
	html += '</div></div></div>';
	ele.html(html);
}
//---------------------------------------------------------
//                    跨平台部分开始
//---------------------------------------------------------

$(function(){
	//当鼠标滑入滑出有效果。
	//给每个按钮添加开关。
	$(".up_xz p span[class='btn_link']").each(function(){
		this.btn = true;
	});
	//当开关是开着的时候，鼠标滑入滑出执行效果。
	$(".up_xz p span[class='btn_link']").hover(function(){
			if(this.btn==true){
				$(this).removeClass().addClass("btn_h");
				$(this).find("img").eq(0).attr("src","images/l1_bg.jpg");
				$(this).find("img").eq(1).attr("src","images/r1_bg.jpg");		
			}
	},function(){
		    if(this.btn==true){
				$(this).removeClass().addClass("btn_link");
				$(this).find("img").eq(0).attr("src","images/l0_bg.jpg");
				$(this).find("img").eq(1).attr("src","images/r0_bg.jpg");	
			}
	});
	//每两个单词只能单选。当前点击的那个开关是关闭状态。
	$(".up_xz p span[class='btn_link']").click(function(){
		var aspan = $(this).parent().find("span");
		aspan.each(function(){
			this.btn = true;
			$(this).removeClass().addClass("btn_link");
			var aImg = $(this).find("img");
			aImg.eq(0).attr("src","images/l0_bg.jpg");
			aImg.eq(1).attr("src","images/r0_bg.jpg");	
		});
		$(this).removeClass().addClass("btn_act");
		var oImg = $(this).find("img");
		oImg.eq(0).attr("src","images/l2_bg.jpg");
		oImg.eq(1).attr("src","images/r2_bg.jpg");	
		this.btn = false;
	});
	//点击暂停，弹框显示。
	$(".zanting").click(function(){
		var theTime = $("#time_show"),s1 = (theTime.text() || "0:0:0").split(":");
		$("#usedM").text("已用时间："+(parseInt(s1[0]*60)+parseInt(s1[1]))+"分钟");
		var zt = !!$("#time_show").data("zt");
		$("#time_show").data("zt",!zt);
		var iWidth = document.documentElement.clientWidth || document.body.clientWidth,this_width = $("#zt_ts").width();
		$("#zt_ts").css("left",parseInt((iWidth-this_width)/2)+"px").show();
	});
	//点击弹框的关闭按钮，关闭弹框。
	$(".tk_ts .ts_box dt img").click(function(){
		var zt = !!$("#time_show").data("zt");
		$("#time_show").data("zt",!zt);
		$(this).parents(".tk_ts").hide();
	});
	//点击弹框的继续按钮，关闭弹框。
	$(".tk_ts .ts_box .btn_box .conti_btn").click(function(){
		var zt = !!$("#time_show").data("zt");
		$("#time_show").data("zt",!zt);
		$(this).parents(".tk_ts").hide();	
	});
	//点击弹框的提交按钮，关闭弹框。
	$(".tk_ts .ts_box .btn_box .sub_btn").click(function(){
		submit();
		$(this).parents(".tk_ts").hide();	
	});
	var startTime=$("#startTime").val();
	var pageTime=$("#pageTime").val();
	var elapsedTime=parseInt((pageTime-startTime)/1000);
	var autoTime=$("#autoTime").val();
	var autoType=$("#autoType").val();
	var userdTime=$("#userdTime").val();
	var reStartTime=$("#reStartTime").val();
	var limitTime=parseInt($("#limitTime").val()*60);
	var exType=$("#exType").val();
	var flag=true;
	if(userdTime!=0){
		elapsedTime=parseInt(userdTime);
	}
	var first=false;
	setInterval(function(){
		var theTime = $("#time_show"), time = theTime.data("time"), zt = theTime.data("zt"), s1 = (theTime.text() || "0:0:0").split(":"), s2, h,m,s;
		if(zt)return;
		if(first)
			time = (time || parseInt(s1[0])*3600+parseInt(s1[1])*60+parseInt(s1[2]))+1;
		else
			{
			 time=elapsedTime+1;
			 first=true;
			}
		h = parseInt(time/3600);
		m = parseInt((time-h*3600)/60);
		s = parseInt((time-h*3600-m*60)%60);
		s2 = h.formatLen(2)+":"+m.formatLen(2)+":"+s.formatLen(2);
		theTime.text(s2);
		theTime.data("time",time);
		if(autoType==1 && (time%autoTime==0))
		{
		 var options = { 
	             url:'extemporeSave', //提交给哪个执行
	             type:'POST', 
	             success: function(){ } //显示操作提示
	             }; 
	             $('#testProcessForm').ajaxSubmit(options); 
	             return false; //为了不刷新页面,返回false，反正都已经在后台执行完了，没事！
		}
		if(limitTime!=0 && time>=limitTime && flag){
			flag=false;
			limitTimeSubmit(exType);
		}
	},1000);
	//当输入的文字超过20个字的时候，输入框自动变长。
	var aInput = $(".tf_main .pt_box ul li input"),aInput_small = $(".tf_main .pt_box dl dd input");
	aInput.keyup(function(){
		change_width(this,20,300);
	});
	aInput_small.keyup(function(){
		change_width(this,10,150);	
	});
	function change_width(obj,n,w){
		var sval = $(obj).val();
		if(confine(sval)>n){
			$(obj).css("width",w*2+"px");
		}else{
			$(obj).css("width",w+"px");
		}
	}
	var aSpan_place = $(".xc_box .up .tkbox"),sName = $(".xc_box .up").attr("name");strAnswer=$(".xc_box .up").attr("attr");
	//替换span里的内容。
	aSpan_place.each(function(index){
		var sAnswer=null;
		if(strAnswer!=null && strAnswer!="null" && strAnswer!="")
			sAnswer=strAnswer.split("&");
		if(sAnswer!=null && sAnswer.length>0 && sAnswer[index]!=null && sAnswer[index]!="")
			{
				$(this).html("<input type=hidden name='"+sName+"' value='"+sAnswer[index]+"'/><i>"+$(this).text()+"</i><em>"+$("span[atrr="+sAnswer[index]+"]").text()+"</em>")
				   .removeClass().addClass("bold");
				$("span[atrr="+sAnswer[index]+"]").parent(".btn").removeClass().addClass("grey_btn");
			}else{
				$(this).html("<input type=hidden name='"+sName+"'/><i>"+$(this).text()+"</i><em></em>")
				.removeClass();
			}
	});
});
//测字符串的长度的封装函数。（汉字占1个字节，其他类型字符占半个字节）
function confine(str){  
	 var iLen = 0;   
	 for (i = 0; i < str.length; i++) 
	 iLen += /[^\x00-\xff]/g.test(str.charAt(i)) ? 1 : 0.5;
	 return iLen;
};
//拖拽滚动条，内容随之滚动的封装函数。
//第一个参数表示滚动条的块。第二个参数表示内容的dom元素。第三个参数表示内容的dom元素的父级元素。
function scroll_drag(obj,ele,opar){
	//滚动条的高度比例应当由内容的实际高度的比例来算。
	var perce = ele.height()/opar.height();
	if(perce<1 || perce==1){
		obj.hide();
	}else{
		var iH = parseInt((opar.height()/ele.height())*obj.parent().height());
		obj.css("height",iH+"px").show();
	}
	//拖拽滚动条，内容随之滚动。（若查询到该元素，那么执行函数，若查询不到该元素，不执行函数。）
	obj.size() && (obj.get(0).onmousedown = function(ev){
		var ev = ev || event,idisy = ev.clientY - $(this).position().top,iHeight = $(this).parent().height()-$(this).height();
		if(this.setCapture){
			this.setCapture();
		}
		$(this).mousemove(function(ev){
			var iT = ev.clientY - idisy;
			//若内容的高度小于或者等于它的父级的高度时候，那么滚动条是不应该滚动的。
			if(ele.height()<opar.height() || ele.height()==opar.height()){
				$(this).css("top","0px");
				return false;
			}else{
				$(this).css("top",iT+"px");
			}
			//滚动条的上下拖拽范围。
			if(parseInt($(this).css("top"))<0 || parseInt($(this).css("top"))==0){
				$(this).css("top","0px");	
			}else if(parseInt(obj.css("top"))==iHeight || parseInt(obj.css("top"))>iHeight){
				$(this).css("top",iHeight+"px");			
			}
			//滚动条一侧的内容相应的滚动。
			var per = parseInt(obj.css("top"))/iHeight, itop = (opar.height()-ele.height())*per;
			ele.css("top",itop+"px");
		});
		$(this).mouseup(function(){
			$(this).unbind("mousemove mouseup");
			if(this.releaseCapture){
				this.releaseCapture();	
			}
		});
		return false;
	});
}

//随意拖拽和运动。
$(function(){
	//定位每个标签的位置。
	/*
	var aButton = $(".tf_main .xc_box .down li"),aposi = [];
	aButton.each(function(){
		aposi.push({left:$(this).position().left,top:$(this).position().top});	
	});
	aButton.each(function(){
		$(this).css({
			"position":"absolute",
			"left":aposi[$(this).index()].left+"px",
			"top":aposi[$(this).index()].top+"px"
		});
	});*/
	var aButton2 = $(".tf_main .xc_box .down li");
	//每个单词都可以随意拖拽。
	aButton2.each(function(){
		drag(this);	
	});
	//给每个填空都设置开关。
	/*
	 var aTkong = $(".xc_box .up span");
	aTkong.each(function(){
		if($(this).attr("class")=="bold"){
			this.btn = false;
		}else{
			this.btn = true;
		}
	});
	 */
	var obj_index = null;
	//点击当前的填空，使其开关关闭，同时让相应的单词按钮恢复为点击状态。
	/*
	 aTkong.click(function(){
		if(this.btn==false){
			this.btn = true;
			$(this).find("em").text("");
			$(this).removeClass();
			aButton.eq($(this).find("input").val()).removeClass().addClass("btn");
			$(this).find("input").val("");
			//aButton.eq($(this).get(0).index).removeClass().addClass("btn");		
		}
	});
	 */
	//多个按钮可随意拖拽的封装函数,拖拽的当前物体处于最上面，同时复制一个，拖拽移动的为复制的这个，同时之前的变灰。
	function drag(obj){
		//定位每个标签的位置。
		var aButton = $(obj).parent().find("li"),aposi = [];
		aButton.each(function(){
			aposi.push({left:$(this).position().left,top:$(this).position().top});	
		});
		aButton.each(function(){
			$(this).css({
				"position":"absolute",
				"left":aposi[$(this).index()].left+"px",
				"top":aposi[$(this).index()].top+"px"
			});
		});
		//给每个填空都设置开关。
		var aTkong = $(obj).parents(".xc_box").find("span");
		aTkong.each(function(){
			if($(this).attr("class")=="bold"){
				this.btn = false;
			}else{
				this.btn = true;
			}
		});
		//点击当前的填空，使其开关关闭，同时让相应的单词按钮恢复为点击状态。
		aTkong.click(function(){
			if(this.btn==false){
				this.btn = true;
				$(this).find("em").text("");
				$(this).removeClass();
				//aButton.eq($(this).find("input").val()).removeClass().addClass("btn");
				$(this).find("input").val("");
				//alert($(this).get(0).index);
				aButton.eq($(this).get(0).index).removeClass().addClass("btn");		
			}
		});
		obj.onmousedown=function(ev){
			//若当前的按钮为灰色，为不可点击的类型，那么不再进行拖拽，不再往下执行。
			if(this.className=="grey_btn"){
				return false;	
			}
			//记录当前鼠标按下的按钮的索引值
			obj_index = $(this).index();
			//让当前的处于最顶层。
			$(this).css("z-Index",3);
			$(this).siblings().each(function(){
				$(this).css("z-Index",2);
			});
			var ev = ev || event,This_obj=this,
				disX = ev.clientX - $(this).position().left,
				disY = ev.clientY - $(this).position().top,
				oThis = $(this).clone(false);   //false为取消复制的事件。
			//复制当前鼠标按下的这个。原来的按钮变灰，当前复制的按钮变绿，为可拖拽型。
			$(this).parent().append(oThis);
			if(oThis[0].setCapture){
				oThis[0].setCapture();	
			}
			$(this).removeClass().addClass("grey_btn");
			oThis.addClass("gren_btn");
			oThis.mousemove(function(ev){
				var iX = ev.clientX - disX ,iY = ev.clientY - disY;
				$(this).css({
					"left":iX+"px",
					"top":iY+"px"	
				});
				//在拖拽的过程中，若碰到其中的一个填空，那么该填空将会添加样式；同时其他填空若有内容，那么这个填空也保留其加粗样式。
				if(latest($(this),aTkong)!=null){
					aTkong.each(function(){
						if(this.btn==true){
							$(this).removeClass();	
						}
					});
					aTkong.eq(latest($(this),aTkong)).addClass("bold");
				}else{   //若撞不到，那么已有内容的填空还是保留加粗样式，没有内容的就不添加样式。
					aTkong.each(function(){
						if(this.btn==true){
							$(this).removeClass();	
						}	
					});
				}
			});
			oThis.mouseup(function(){
				//取消鼠标移动和鼠标抬起事件。取消层。
				oThis.unbind("mousemove mouseup");
				if(oThis[0].releaseCapture){
					oThis[0].releaseCapture();	
				}
				//若碰到其中的一个填空，让该拖拽的按钮运动到当前填空的位置，同时将该按钮的值赋给该填空。
				if(latest($(this),aTkong)!=null){
					$(this).stop().animate({
						"top": aTkong.eq(latest($(this),aTkong)).position().top-10+"px",
						"left": aTkong.eq(latest($(this),aTkong)).position().left+25+"px"
					},function(){  //若运动达到目的地后，自行删除，同时将该复制的按钮的值放进填空里。同时原来的按钮变成灰色，不可点击的类型。
						var stk_val = $(this).find("span").text();						
						if(aTkong.eq(latest($(this),aTkong)).find("em").html()!=""){
							aButton.eq(aTkong.eq(latest($(this),aTkong)).get(0).index).removeClass().addClass("btn");
						}
						aTkong.removeClass();
						//alert(latest($(this),aTkong));
						aTkong.eq(latest($(this),aTkong)).find("em").html(stk_val);
						aTkong.eq(latest($(this),aTkong)).get(0).btn=false;  //同时填空已经有内容的填空的开关关闭。
						aTkong.eq(latest($(this),aTkong)).get(0).index = obj_index;  //同时把当前不可点击的原按钮的索引值赋给该填空的属性。
						//将该按钮的属性值赋给填空的属性。
						var str_this = $(this).find("span").attr("atrr");
						aTkong.eq(latest($(this),aTkong)).find("input").val(str_this);//.attr("value",str_this);
						$(this).remove();
					});
				}else{  //若碰不到其中的一个填空，那么该复制的按钮自行删除；同时原来的按钮变成可点击的。
					$(this).remove();
					$(This_obj).removeClass().addClass("btn");
				}
			});
			return false;
		};
	}
	//判断是否碰撞
	function pengzhuang(obj,obj2){
		var ic_posi = obj.position(),
			ic_l = ic_posi.left,
			ic_r = ic_posi.left+obj.width(), 
			ic_t = ic_posi.top,
			ic_b = ic_posi.top-obj.height(),
			
			tk_posi = obj2.position(),
			tk_l = tk_posi.left, 
			tk_r = tk_posi.left+obj2.width(),
			tk_t = tk_posi.top, 
			tk_b = tk_posi.top - obj2.height();
		if(ic_r<tk_l || ic_l>tk_r || ic_t<tk_b || ic_b>tk_t){  //碰不到
			return false;
		}else{  //碰到
			return true;
		}
	}
	//若碰撞，返回碰撞最近距离的那个。
	function latest(obj,aEle){
		var i_posi = obj.position(),iL = i_posi.left+obj.width()/2,iT = i_posi.top-obj.height()/2,x=-1,iLine = 9999999999999;
		for(var i=0;i<aEle.length;i++){
			var this_posi = $(aEle[i]).position(),this_l = this_posi.left+$(aEle[i]).width()/2,this_t = this_posi.top-$(aEle[i]).height()/2;	
			var iX = Math.pow((this_l-iL),2),iY = Math.pow((this_t-iT),2),iJuli = Math.sqrt(iX+iY);
			if(pengzhuang(obj,$(aEle[i]))){  //若碰到的话，返回最近的那个。
				if(iJuli<iLine){
					iLine = iJuli;
					x = i;
				}	
			}	
		}
		if(x==-1){   //没有碰到
			return null;	
		}else{   //碰到
			return x;	
		}
	}
});
$(function(){
	//为段落的每句话设置开关。
	$(".dx_dluo span").each(function(){
		this.btn = true;	
	});
	//鼠标滑入滑出每句话的效果。
	$(".dx_dluo span").mouseover(function(){
		$(this).parents(".dx_dluo").find("span").removeClass("gren_h");
		$(this).addClass("gren_h");	
	});
	//鼠标点击多选。
	$(".dx_dluo span").click(function(){
		if(this.btn==true){
			$(this).addClass("gren_act");
			this.btn = false;	
		}else{
			$(this).removeClass();
			this.btn = true;	
		}	
	});
});
//点击隐藏按钮，时间的切换；同时按钮的值也跟着切换。
$(function(){
	var aSpan = $(".time span");
	aSpan.eq(1).click(function(){
		var oText = $(this).find("i");
		if(oText.text()=="HIDE TIME"){
			aSpan.eq(0).stop().animate({width:"0px"},function(){
				$(this).css("border-right","none");
				aSpan.eq(0).hide();
			});
			oText.text("SHOW");
		}else{
			aSpan.eq(0).css("border-right","#c1c1c1 solid 1px");
			aSpan.eq(0).show();
			aSpan.eq(0).stop().animate({width:"80px"});
			oText.text("HIDE TIME");	
		}	
	})
	.hover(function(){
		$(this).addClass("time_ts_h");
	},function(){
		$(this).removeClass("time_ts_h");
	});
});
//点击引导作文的关闭按钮，关闭提示。
$(function(){
	$(".yind_ts img").click(function(){
		$(this).parents(".yind_ts").hide();	
	});
//	$(".sentence textarea").blur(function(){
//		if(confine($(this).val())<50){
//			var oDiv = $(this).parents(".sentence").find(".yind_ts");
//				oDiv.show();
//				oDiv.find("span").html("亲，你写的太少嘞，老师会给低分哦。。。");
//		}
//	}).keyup(function(){
//		var oDiv = $(this).parents(".sentence").find(".yind_ts");
//		if(confine($(this).val())>180){
//			oDiv.show();
//			oDiv.find("span").html("亲，写的太多了，没地方了");
//			return false;
//		}else{
//			oDiv.hide();	
//		}
//	});
});
function isAnswered(){return true;}

$(function(){
	//滚动条的拖拽，同时内容页跟着相应的滚动。
	//点击关闭按钮，关闭弹框。
	$(".pt_title img").click(function(){
		$(this).parents(".pt_tk").hide();	
	});
	var okuai2 = $(".dx_scroll p"),ele2 = $(".dx_nr .dx_dluo"),opar2 = $(".dx_dt");
	//读写训练---点句多选
	scroll_drag(okuai2,ele2,opar2);
});

$(function(){
	//点击hint按钮，提示出现，约5秒钟消失。
	$(".tf_head ul li.button .btn_3").click(function(){
		var tips=$(".pt_box .tip");
		if(tips.length>0){
			$(".pt_box .tip").fadeIn("slow");
			setTimeout(function(){
				$(".pt_box .tip").fadeOut("slow");
			},5000);
		}else{
			alert("暂无提示!");
		}
	});	
	//点击script按钮，出现弹框。
	$(".tf_head ul li.button .btn_0").click(function(){
		 if(isAnswered()){
			  var okuai0 = $(".scroll_biao p"),ele0 = $(".scro_main .scrol_dt"),opar0 = $(".scro_main");
			  $(".pt_tk").show();
			  scroll_drag(okuai0,ele0,opar0);
		 }else{
			 alert("请先答题!");
		 }
	});
	//点击go按钮，ok按钮变成白色。
	$(".button .btn_2").click(function(){
		 $(this).parent().find(".btn_6").removeClass().addClass("btn_1");	
	});
});

/*cuidingfeng 3qi*/
var tgFixed_sd = null, iframeTop = 0, tgTop = 0;
if(window!=top){
	iframeTop = $("#"+window.name,top.document).offset().top;
}
$(function(){
	$('.tgFixed').hover(function(){
		if (!$(this).hasClass("tgFixed_sd")) {
			$(this).addClass("tgFixed_hv");
			if($(".tgFixed_open", this).size()==0){
				$(this).append($("<span class='tgFixed_open'>始终前置</span>").click(function(){
					var tg = $(this).closest(".tgFixed");
					$(".tgFixed_close").click();
					tgTop = tg.offset().top;
					tg.width(tg.width()).css("left", tg.position().left + "px").addClass("tgFixed_sd").find(".tgFixed_ct").height(Math.min(tg.height(), $(top).height()/2));
					tg.after("<div class='tgFixed_hei' style='height:"+tg.outerHeight()+"px'></div>");
					tgFixed_sd = tg;
					$(top).bind("scroll", topScroll).scroll();
					$(this).hide().next().show();
					ifie(7);
					window.changePH && changePH();
				}));
				$(this).append($("<span class='tgFixed_close'>取消前置</span>").click(function(){
					var tg = $(this).closest(".tgFixed");
					tgTop = 0;
					tg.css({"top": "", "left": ""}).removeClass("tgFixed_sd").find(".tgFixed_ct").css("height","");
					tg.next(".tgFixed_hei").remove();
					tgFixed_sd = null;
					$(top).unbind("scroll", topScroll);
					$(this).hide().prev().css("display", "");
					ifie(7);
					window.changePH && changePH();
				}));
			}
		}
	},function(){
		if (!$(this).hasClass("tgFixed_sd")) {
			$(this).removeClass("tgFixed_hv");
		}
	});
});

//setPosi(".addimg_bx");
/*弹框定位的封装函数*/
function setPosi(obj){
	var iW = document.documentElement.clientWidth,
		iH = $(obj).height(),
		iT = 0,
		iTop = document.documentElement.scrollTop || document.body.scrollTop,
		iClient = document.documentElement.clientHeight;
	if(window!=top){
		iClient = $(window.top).height(),
		iTop = $(window.top).scrollTop(),
		iT = $("#"+window.name,window.top.document).offset().top;
	}
	$(obj).css({
		"top":(iClient-iH)/2+(iTop-iT)+"px",
		"left":(iW-$(obj).width())/2+"px"
	});
}

function topScroll(){
	tgFixed_sd.css("top", Math.max($(this).scrollTop() - iframeTop + (window.tgTop2 || 0), tgTop));
}
function ifie(ie, fn){
	if($.browser.msie && $.browser.version<=ie){
		fn && fn.call(this);
		myreflow();
	}
}
function myreflow(){
	if(myreflow.temp || !($.browser.msie && $.browser.version<8))return;
	myreflow.temp = setTimeout(function(){
		var mtop = $(window).scrollTop();
		var temp = $("<div style='height:10px'></div>");
		$("body").append(temp).hide().show();
		temp.remove();
		$(window).scrollTop(mtop);
		clearTimeout(myreflow.temp);
		myreflow.temp = null;
	},20);
}

$(function(){
	//为段落的每句话设置开关。
	$(".dx_dluo span").each(function(){
		this.btn = true;	
	});
	//鼠标点击多选。
	$(".dx_dluo span").click(function(){
		if(this.btn==true){
			this.btn = false;	
		}else{
			this.btn = true;	
		}	
	});
});