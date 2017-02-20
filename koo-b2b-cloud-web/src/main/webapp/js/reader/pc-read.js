/* @grunt-copy */
/* @grunt-raw */
//author:yuanshuang

//creatTime:20140102

//name:移动阅读端页面优化

$(function(){
	
pcreadPath = baseDomain + pcreadPath;

/*if($.browser.msie && jQuery.browser.version<8){
	(window.alertMsg || alert)("您的浏览版本过低，请使用谷歌、火狐、Safari、IE8/9/10/11+");
}else{*/
//正文开始

var mobile_Optimization = mobile_Optimization || {};

mobile_Optimization.common = mobile_Optimization.common || {};

mobile_Optimization.common.init = function(){
	var This = this, setInt = null;
	$(function(){
		$(".subarrow").hover(function(){
			$(this).removeClass("subarrowbg1").addClass("subarrowbg2");
		},function(){
			$(this).addClass("subarrowbg1").removeClass("subarrowbg2");
		})
		
	})
	mobile_Optimization.common.load = function(){
		This.timeout();
	};
	setInt = setInterval(function(){
		if($("#documentViewer .flexpaper_page").size()>0){
			clearInterval(setInt);
			mobile_Optimization.common.isload = true;
			mobile_Optimization.common.load && mobile_Optimization.common.load();
			$('.pagebtnx').click(function(){
				//绑定 单页、双页 、多页 点击事件
				var self = this;
				setTimeout(function(){
					//绑定滚屏事件
					var view = $("#pagesContainer_documentViewer");//文档显示区域
					view.scroll(function(o){
						pageNum(0);
					});
					if($('.pagebtnx').index(self)===2){
						//如果点击的是 多页按钮
						view.click(function(){
							//则，点击文档显示区域时，点击一下 单页按钮
							$('.pagebtnx:eq(0)').click();
						});
					}
				},1000);
			});
			if(!mobile_Optimization.common.fullscreen()){
				//如果浏览器不支持全屏，则去掉全屏按钮
				$(".screenbtn1").remove();
			}
		};
	},100);
};

mobile_Optimization.common.isload = false;

mobile_Optimization.common.load = null;

mobile_Optimization.common.fullScreenbx = function(){
	jQuery("#documentViewer").showFullScreen();
	$(".screenbtn1").css("background-position","-253px 0");
};

mobile_Optimization.common.fullscreen = function(){
	return document.fullscreenEnabled || document.webkitFullscreenEnabled || document.mozFullScreenEnabled || document.msFullscreenEnabled || false;
};

mobile_Optimization.common.exitFullscreen = function(){
	if(document.exitFullscreen){
        document.exitFullscreen();
    }else if(document.webkitCancelFullScreen){
        document.webkitCancelFullScreen();
    }else if(document.mozCancelFullScreen){
        document.mozCancelFullScreen();
    }
};

mobile_Optimization.common._fullscreenchange=function(fullscreen){
	setTimeout(function(){
		if (!fullscreen) {
			$(".flexpaper_viewer_container").css("height", parseInt($(".flexpaper_viewer_container").css("height")) + parseInt($(".flexpaper_bottom").height() - 9));
			$(".screenbtn1").css("background-position","");
		}else{
			$(".screenbtn1").css("background-position","-253px 0");
		}
		$(".screenbtn1").data("two", fullscreen);
	},0);
};

mobile_Optimization.common.timeout = function(){
	
		var _this = this;
		pageNum(0);
		$("#pagesContainer_documentViewer").scroll(function(o){
			pageNum(0);
		});
		
		$(".screenbtn1").click(function(){
			if($(this).data("two")){
				_this.exitFullscreen();
			}else{
				jQuery("#documentViewer").showFullScreen();
			}
		});
		
		$(document).keypress(function(e){
			var code = e.keyCode ? e.keyCode : e.which;
			if(code == 27 || code == 96){
				$(".screenbtn1").css("background-position","-253px -38px");
			};
		});
		
		document.addEventListener("fullscreenchange", function () {  
		      _this._fullscreenchange(document.fullscreen);
		}, false); 
		document.addEventListener("webkitfullscreenchange", function () {  
		      _this._fullscreenchange(document.webkitIsFullScreen);
		}, false); 
		document.addEventListener("mozfullscreenchange", function () {  
		      _this._fullscreenchange(document.mozFullScreen);
		}, false); 
		document.addEventListener("MSfullscreenchange", function () {  
		      _this._fullscreenchange(document.msFullScreen);
		}, false); 
		
		$("#modal-print,#printFrame_documentViewer").remove();
		$("#pagesContainer_documentViewer").width($("#documentViewer").width());
		$("#pagesContainer_documentViewer").height($("#documentViewer").height());
		$(".flexpaper_viewer_container").append($(".flexpaper_bottom"));
		$(".flexpaper_viewer_container").append($(".subarrow"));
		$(".flexpaper_viewer_container").height(parseInt($(".flexpaper_viewer_container").height())+parseInt($(".flexpaper_bottom").height()+2));
		$("#pagenumx").html($(".flexpaper_lblTotalPages").html());
		$(".suballnum").html($(".flexpaper_lblTotalPages").html());
		$(".flexpaper_bottom").show();
		$(".subarrow").show();
		
		
		$(".pagebtn3").click(function(){
			$("#pcPageNumber").unbind("keyup").keyup(function(e){
				var code = e.keyCode ? e.keyCode : e.which;
				if(isNaN(this.value)) return;
				if(code == 13)
					$(".pagebtn1").trigger("click");
			});
		});
		$(".pagebtn2").click(function(){
			$("#pcPageNumber").unbind("keyup").keyup(function(e){
				var code = e.keyCode ? e.keyCode : e.which,res = 1;
				if(code == 13){
					var value = this.value.split("-");
					if(isNaN(value[0]) || value[1] && isNaN(value[1])) return;
					if(value[1]-value[0] == 1){
						if(value[0]%2 == 0) res = (value[0]-1)+"-"+value[0];
						else res = this.value;
					}else{
						res = value[0]%2 == 0 ? (value[0]-1)+"-"+value[0] : value[0]+"-"+(value[0]-0+1);
					}
					$(".subnum").html(res);
					$("#PageNumberx").val(res);
				}
			});
			
			setTimeout(function(){
				var paper = $FlexPaper("documentViewer"),pages = paper.getTotalPages(),curpage = paper.getCurrPage(),page = 1;
				if(pages > 1) page = "1-2";
				$(".subnum").html(page);
				$("#PageNumberx").val(page);
			},1000);
		});
		$(".pagebtn1").click(function(){
			$("#pcPageNumber").unbind("keyup").keyup(function(e){
				var code = e.keyCode ? e.keyCode : e.which;
				if(isNaN(this.value)) return;
				if(code == 13){
					$(".subnum").html(this.value);
					$("#PageNumberx").val(this.value);
				}
			});
		});
		
		$(".pdfarrowl,.pdfarrowr,.subarrowt,.subarrowb").click(function(){
			setTimeout(function(){
				var val = $("#pcPageNumber").val();
				$(".subnum").html(val);
				$("#PageNumberx").val(val);
			},600)
		});
};
/** 屏蔽搜索时 出现复制 **/
mobile_Optimization.common.loadbefore = function(){
	ZeroClipboard.setMoviePath(pcreadPath+"js/ZeroClipboard.swf");
	clip=new ZeroClipboard.Client();
	clip.setHandCursor(true);
	/**屏蔽搜索时 出现复制
	clip.addEventListener('mouseOver', function (client) {
		clip.setText( $('#selector').val() );
	});
	clip.addEventListener('complete', function (client, text) {
		var copy_menu = $("#copy_menu");
		copy_menu.addClass("copy_menu_ok");//复制成功
		setTimeout(function(){
			copy_menu.fadeOut();
		},1500);
	});**/
};
/**屏蔽搜索时 出现复制
mobile_Optimization.common.copy = function(doc){
	doc.bind("mouseup", function(e){
		var e_Target = $(e.target);
		if(e_Target.is(".copy_menu,embed,object")){
			if(e_Target.hasClass("copy_menu") && !e_Target.hasClass("copy_menu_ok")){
				alert("复制失败！");
			}
		}else{
			var font = $(".flexpaper_selected_default:last"), copy;
			$(".copy_menu").remove();
			if(font.size()===1){
				copy = '<span id="copy_menu" class="copy_menu" title="复制" style="top:'+(font.position().top+font.height())+'px;left:'+(font.position().left+font.width()/2)+'px"></span>'; 
				//copy=''; //屏蔽搜索时 出现复制
				font.after(copy);
				clip.destroy();
				clip.glue("copy_menu",font.closest(".flexpaper_page")[0]);
			}
		}
	});
};
**/

//初始化。
mobile_Optimization.common.init();

//正文结束

/*}*/

(function(){
	var startDocument = "Paper";
	
			jQuery.get(htmlTemplate,
				function(toolbarData) {
					var viewerConfig = $.extend({
							//参数
							IMGFiles: "/project/b-ms-cloud/1.x/js/common/reader/45/index_{page}.png", 
							JSONFile: "/project/b-ms-cloud/1.x/js/common/reader/45/index.js",
							PDFFile: "/project/b-ms-cloud/1.x/js/common/reader/45/index.pdf",
							
							key:'@b186e9fe9ce4cabfda3$b3047f8e707bff557a8',
							Scale : 1,
							ZoomTransition : 'easeOut',
							ZoomTime : 0.5,
							ZoomInterval : 0.1,
							FitPageOnLoad: false,
							FitWidthOnLoad: false,
							FullScreenAsMaxWindow: true,
							ProgressiveLoading: true,
							MinZoomSize : 0.2,
							MaxZoomSize : 5,
							SearchMatchAll : false,
							Toolbar : toolbarData,
							InitViewMode : 'Portrait',
							RenderingOrder : 'html,flash',
							StartAtPage : '',
							ViewModeToolsVisible : true,
							ZoomToolsVisible : true,
							NavToolsVisible : true,
							CursorToolsVisible : true,
							SearchToolsVisible : true,
							WMode : 'window',
//							localeDirectory:'/project/b-ms-cloud/1.x/js/common/reader/locale/',
							localeChain: 'en_US'
						}, pageViewerConfig, {
							localeDirectory: pcreadPath + 'locale/'
						});
					mobile_Optimization.common.loadbefore();
					$('#documentViewer').FlexPaperViewer(
						{config: viewerConfig}
					);
					//mobile_Optimization.common.copy($('#documentViewer'));
			});
})();

});

