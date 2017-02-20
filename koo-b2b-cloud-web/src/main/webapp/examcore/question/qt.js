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
		var wst = $(window).scrollTop()>=67, hbx = $(".hbx1:eq(0)"), hbx2 = hbx.nextAll(".hbx2,.wp"), rnav = $(".rnav"), fixed = hbx.hasClass("fixed"), niceback = window.niceback || function(){};
		if (wst) {
			if(!fixed){
				hbx.addClass("fixed");
				hbx.after("<div class='hbx1 _hbx1_'/>");
				hbx2.addClass("hbx2_f");
				rnav.size() && rnav.addClass("rnavf") && $(window).resize(), niceback(true);
			}
		}else{
			fixed && (hbx.removeClass("fixed") && $("._hbx1_").remove() && hbx2.removeClass("hbx2_f") && rnav.size() && rnav.removeClass("rnavf") && $(window).resize(), niceback(false));
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
	$(".ztTime").click(function(){
		var zt = !!$("#theTime").data("zt");
		$(this).text(zt?"暂停答题":"恢复答题");
		$("#theTime").data("zt",!zt);
		zt || pauseTest();
	});
	var startTime=$("#startTime").val();
	//var mDate=new Date();
	//var vMs=Date.parse(new Date());
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
		elapsedTime=parseInt((pageTime-reStartTime)/1000+parseInt(userdTime));
	}
	var first=false;
	var testCountdown=setInterval(function(){
		var theTime = $("#theTime"), time = theTime.data("time"), zt = theTime.data("zt"), s1 = (theTime.text() || "0:0:0").split(":"), s2, h,m,s;
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
		$("#u_Time").val(time);
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
			if(exType=="1"){
				window.clearInterval(testCountdown);
			}
			limitTimeSubmit(exType);
		}
	},1000);
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
    	//console.log("1");
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
$(".tit3 .close").on("click",function(){
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
	}else if(f&&f.lastIndexOf('.WMA')==(f.length-4)){
		f=f.replace(/\.WMA/,".mp3");
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
	var pq = function(){if(typeof(parent.quizsubmit) == "function"){rc_pop.setContent("alertCon", "<div class='ft8 mb10'>"+title+"</div><div class='ta_c mb20 ft4'><div class='mb20'>答题时间："+time1+"分钟<span class='ml1'>已用时间："+time2+"分钟</span></div><div><a href='javascript:;' id='btn"+n+"' class='btnq1'>继续考试</a></div></div>");}}
	pq();
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
	setPosi("#dialogBox");
	return rc_pop;
}
function limitTimeSubmit(exType){
	var rc_pop = new Popup({
		contentType : 4,
		isReloadOnClose : false,
		width : 420,
		isAutoSize : true
	});
	if(exType=="2"){
	rc_pop.setContent("alertCon", "<div class='ft8 ta_c mb10'>时间用尽</div><div class='mb20 bx1 bx1_p1 ft4'>\
		你的时间已经用尽，你可以点击 <b>确定交卷</b> 完成本次测试。也可以点击  <b>继续答题</b> 回到答题页面，但是这样可能无法测试你的真实水平！\
	</div><div class='ta_c ft4 mb20'><a href='#' class='btnq1' onClick='submit()'>确定交卷</a><a href='#' class='a1 ml2' onClick='changeLimit();return false;' >继续答题</a></div>");
	}else{
		rc_pop.setContent("alertCon", "<div class='ft8 ta_c mb10'>时间用尽</div><div class='mb20 bx1 bx1_p1 ft4'>\
					你的时间已经用尽，你可以点击 <b>确定交卷</b> 完成本次测试。<span id='subTime' class='ft2'>30</span>秒钟后，系统将自动进行本次提交。\
				</div><div class='ta_c ft4 mb20'><a href='#' class='btnq1' onClick='submit()'>确定交卷</a></div>");
		//10秒钟后提交
		var time=30;
		var subCountdown = setInterval(function(){
			time=time-1;
			$("#subTime").text(time);
			if(time==0 || time<0){
				window.clearInterval(subCountdown);
				submit();
			}
		},1000);
		//setTimeout(function(){
		//	submit();
		//},10000) 
	}
	rc_pop.setContent("title", "Koolearn测试练习系统");
	rc_pop.build();
	rc_pop.show();
	$("#dialogYES").parent().remove();
	$("#dialogClose").remove();
	setPosi("#dialogBox");
	return rc_pop;
}
function changeLimit(){
	$("#unLimit").val(1);
	 window.parent.document.body.removeChild(window.parent.document.getElementById("dialogCase"));
}
$(".dragfrom li:not(.bl)").on("mouseover",function(){
	$(this).addClass("ul1_hv");
}).on("mouseout",function(){
	$(this).removeClass("ul1_hv");
});
$(".dragto .ct").on("mouseover",function(){
	$(this).closest("li").addClass("hv");
}).on("mouseout",function(){
	$(this).closest("li").removeClass("hv");
});
$(".dragsort li").on("mouseover",function(){
	$(this).addClass("hv");
}).on("mouseout",function(){
	$(this).removeClass("hv");
});
$(".dragto .remove").on("click",function(){
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
$(".st .tb1").on("mouseover",function(){
	$(this).addClass("tb1_hv");
}).on("mouseout",function(){
	$(this).removeClass("tb1_hv");
}).on("click",function(){
	var st = $(this).closest(".st"), txt = st.find(".ft8").text();
	$(".tb1:hidden",st).show().next("strong").remove();
	$(this).hide().after("<strong>"+txt+"</strong>");
	$(this).closest("div .mb10").children("input:hidden").val($(this).text()-1);
});

//单选点句   点句多选
$(".st .tb4").on("mouseover",function(){
	$(this).addClass("tb4_hv");
}).on("mouseout",function(){
	$(this).removeClass("tb4_hv");
}).on("click",function(){
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
		eval("data="+data);
		var md5=$.md5(tb4.text());
		$.each(data,function(i,n){
			if(n[0]==md5){
				ipt.val(n[1]);
			}
		});
	}
	tb4.addClass("tb4_cl");
	return false;
});


$(".bx3 dd").on("click",function(){
	$(this).addClass("sd").siblings().removeClass("sd");
	$(this).parent().children("dt").children("input:hidden").val($(this).attr("value"));
});
$(".toggle").on("click",function(){
	var ele = $(this).closest("div").prev();
	if($(this).hasClass("toggleOpen")){
		$(this).removeClass("toggleOpen").text("展开");
		ele.addClass("hd");
	}else{
		$(this).addClass("toggleOpen").text("收起");
		ele.removeClass("hd");
	}
});
$(".dAn label").on("hover",function(event){
	if (event.type =='mouseover'){
		$(this).addClass("hv");
    }else {
		$(this).removeClass("hv");
    }
}).on("click",function(){
	var ipt=$(this).parent().find("label input");
	ipt.parent().removeClass("sd");
	ipt.filter(":checked").parent().addClass("sd");
	rsetTs();
});
$(".zhk").on("click",function(){
	var area = $(this).closest("div").prev("textarea").scrollTop(1000);
	if($(this).hasClass("zhk2")){
		$(this).removeClass("zhk2").text("展开");
		area.css("height","");
	}else{
		$(this).addClass("zhk2").text("收起");
		area.scrollTop() && area.css("height",area.scrollTop()+area.outerHeight()+"px");
	}
});
$(".jdtzhk").on("click",function(){
	var textarea= $(this).parent().parent().find("textarea");
	var div= $(this).parent().parent().find("div.jdtdiv");
	if($(this).hasClass("zhk2")){
		$(this).removeClass("zhk2").text("编辑");
		div.html(textarea.val());
		textarea.hide();
		div.find("img").bind("load", changePH);
		div.show();
	}else{
		$(this).addClass("zhk2").text("完成");
		textarea.show();
		div.hide();
		window.changePH && changePH();
	}
});
$(".zhktab").on("click",function(){
	var tab = $(this).closest("div").prev("table"), max = tab.attr("maxtr");
	if($(this).hasClass("zhk2")){
		tab.find("tr:hidden").show();
	}else{
		tab.find("tr:gt("+max+")").hide();
	}
});
$(".yyjx").on("click",function(event){
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
$(".spjx").on("click",function(event){
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
$(".bx5+.btnq1").on("click",function(){
	var bx5 = $(this).prev();
	var subid=bx5.attr("id");
	var index=bx5.attr("name");
	if($(".ipt",bx5).val()==null || $(".ipt",bx5).val()=="") return false;
	var score=$(".ipt",bx5).val();
	if(!/^[0-9]+(.[0-9]{1})?$/.test(score)){
		alert('只能输入包含一位小数的正数分数！');
		return false;
	}
	selfRatedSave(index,subid,score);
	bx5.text($(".ipt",bx5).val()+"分").removeClass("dis_ib");
	$(this).hide();
});
$(".bx5:not(.dis_ib)").on("click",function(){
	var btnq1 = $(this).next(".btnq1").show();
	if(btnq1.size()==0){
		btnq1 = $('<a href="javascript:;" class="btnq1">确认</a>');
		$(this).after(btnq1);
	}
	$(this).html('<input type="text" class="ipt" value="'+$(this).text().replace(/[^.0-9]/g,"")+'" /><span class="bx5a">分</span>').addClass("dis_ib");
});
$(".bx5 .ipt").on("keypress",function(event){
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
$(".tkt").on("keyup change",tktfn).on("blur",function(){
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
$(".jpy1")/*.die()*/.on("click",function(){
	var src = $(this).attr("src");
	if((src.indexOf(".mp3") == -1)&&(src.indexOf(".MP3") == -1)){
		$(this).next("span").text("该音频无法播放");
		return;
	}else{
		$(this).next("span").text("");
	}
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
}).on("mouseover",function(){
	clearTimeout($(this).data("hidefn"));
	if ($(this).hasClass("jpy1_2")) {
		$(".jpyfn",this).animate({
		   width: 126
		 }, 400);
	}
	else {
		$(this).addClass("jpy1_1");
	}
}).on("mouseout",function(){
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
	html += '<span class="s0 s0a s0t">'+mdata[0]+'分</span><span class="s0 s0a s0b">第'+ele.attr("rank")+'名</span>';
	html += '</div></div></div>';
	ele.html(html);
}

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