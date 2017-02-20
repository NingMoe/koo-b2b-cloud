//创建公共DOM节点
var _create = {
	_auto : function() {
		_create._opa();
		_create._html('alert', '警告', '警告内容');
		_create._html('confirm', '温馨提示', '提示内容');
	},
	_html : function(type, title, msg) {
		var type = type || 'alert', title = title || '温馨提示', msg = msg || '请输入提示内容', _html = '';
		if (type == 'alert') {
			_html += '<div id="pop_alert" class="pop_box pop_win">';
		} else if (type == 'confirm') {
			_html += '<div id="pop_confirm" class="pop_box pop_win">';
		}
		_html += '<div class="pop_top bg"><h2 class="pop_tit">' + title + '</h2><a class="pop_close" href="javascript:;" title="关闭">关闭</a></div>';
		_html += '<div class="pop_main"><p class="pop_txt">' + msg + '</p></div>';
		_html += '<div class="pop_btns"><a class="pop_subm" href="javascript:;">确 定</a>';
		if (type == 'confirm') {
			_html += '<a class="pop_cancel" href="javascript:;">取 消</a>';
		}
		_html += '</div>';
		_html += '</div>';
		document.write(_html);
	},
	_opa : function() {
		var _html = '<div id="pop_opa" class="pop_opa"></div>';
		document.write(_html);
	}
};
_create._auto();

//弹层-警示框
window.pop_alert = function(title, txt,timer,callback_ok, callback_no) {
	var title = title || '温馨提示', txt = txt || '提示内容';
	var obj = $('#pop_alert'), opa = $('#pop_opa');
	var time = 450;
	var timer = timer||0;
	
	obj.find('.pop_tit').html(title);
	obj.find('.pop_txt').html(txt);
	var a = {
		auto : function() {
			var _this = this;
			var pop_close = obj.find('.pop_close');
			var pop_subm = obj.find('.pop_subm');
			_this.opa_show();
			
			pop_close.on('click', function() {
				pop_close.off('click');
				if ( typeof callback_no == 'function') {
					callback_no();
				}
				_this.obj_hide();
			});
			pop_subm.on('click', function() {
				pop_close.off('click');
				if ( typeof callback_ok == 'function') {
					callback_ok();
				}
				_this.obj_hide();
			});
		},
		opa_show : function() {
			var _this = this;
			_this.obj_show();
		},
		opa_hide : function() {
		},
		obj_show : function() {
			if(timer){
				obj.find('.pop_btns').hide();
			}else{
				obj.find('.pop_btns').show();
			}
			obj.css({
				'display' : 'block',
				'top':'50%',
				'margin':'-125px 0 0 -176px'
			});
			if(timer){
				setTimeout(function(){
					a.obj_hide();
					if(callback_ok){
						callback_ok();
					}
				},timer);
			}
		},
		obj_hide : function() {
			obj.css({
				'display' : 'none'
				});
			a.opa_hide();
		}
	};
	if (obj.length > 0 && opa.length > 0) {
		a.auto();
	}
};
//弹窗-确认框
window.pop_confirm = function(id, title, txt, callback_ok, callback_no) {
	var id = id, title = title || '温馨提示', txt = txt || '提示内容';
	var obj = $('#pop_confirm'), opa = $('#pop_opa');
	var time = 450;
	obj.find('.pop_tit').html(title);
	obj.find('.pop_txt').html(txt);
	var a = {
		auto : function() {
			var _this = this;
			var pop_close = obj.find('.pop_close,.pop_cancel');
			var pop_ok = obj.find('.pop_subm');
			_this.opa_show();
			//确定
			pop_ok.on('click', function() {
				pop_ok.off('click');
				pop_close.off('click');
				_this.obj_hide();
				if ( typeof callback_ok == 'function') {
					callback_ok(id);
				}
			});
			//取消
			pop_close.on('click', function() {
				pop_ok.off('click');
				pop_close.off('click');
				_this.obj_hide();
				if ( typeof callback_no == 'function') {
					callback_no();
				}
			});
		},
		opa_show : function() {
			var _this = this;
			 _this.obj_show();
		},
		opa_hide : function() {
		},
		obj_show : function() {
			obj.css({
				'display' : 'block',
				'top':'50%',
				'margin':'-125px 0 0 -176px'
			})
		},
		obj_hide : function() {
			obj.css({
				'display' : 'none'
				});
			a.opa_hide();
		}
	};
	if (obj.length > 0 && opa.length > 0) {
		a.auto();
	}
};

//标签切换
$.fn.tabslider = function(opts) {
	var elm = $(this);
	var defaults = {
		index : 0,
		clas : 'cur',
		tag : elm.find('.tab_tag > a'),
		con : elm.find('.tab_con > .con'),
		ev : 'click'
	};
	var o = $.extend(defaults, opts || {});
	o.tag.each(function() {
		if ($(this).hasClass('cur')) {
			o.index = $(this).index();
		}
	});
	switch(o.ev) {
		case "click":
			o.tag.on('click', function() {
				var index = $(this).index();
				currFn(index);
			})
			break;
		case "hover":
			o.tag.hover(function() {
				var index = $(this).index();
				currFn(index);
			});
			break;
		default:
			alert(o.ev + '事件暂时不支持！');
			break;
	};
	//显示当前所引致标签以及内容
	function currFn(index) {
		o.tag.removeClass(o.clas);
		o.tag.eq(index).addClass(o.clas);
		o.con.hide();
		o.con.eq(index).show();
	}

	currFn(o.index);
};
/*处理input属性placehoder在ie10以下的兼容性*/
function placeholder() {
	var input = document.createElement('input');
	return 'placeholder' in input;
}
  if( !('placeholder' in document.createElement('input')) ){  
    $('input[placeholder],textarea[placeholder]').each(function(){
      var that = $(this),    
      text= that.attr('placeholder');    
      if(that.val()==""){    
        that.val(text).addClass('placeholder');
        $('input.placeholder').css('color','#c8c8c8');
      }
      that.focus(function(){    
        if(that.val()==text){    
          that.val("").removeClass('placeholder');
          that.val("").css('color','#333');
        } 
      })    
      .blur(function(){    
        if(that.val()==""){    
          that.val(text).addClass('placeholder');
          $('input.placeholder').css('color','#c8c8c8');
        }    
      })    
      .closest('form').submit(function(){    
        if(that.val() == text){    
          that.val('');    
        }    
      });
      /*运营后台端   解决密码输入框在ie10以下浏览器是点的问题*/
      $("#inp_psw,#password").each(function(){
      	   var that2=$("#inp_psw,#password");
      		if(that2.val() == ''){    
		          that2.css('background','url(/platform/i/c/password.png) no-repeat 44px center');   
		       }else{
		       	that2.css('background','');
		       }
			that2.attr('placeholder','');
			var pwdField = $(this);
			pwdField.focus(function(){
				pwdField.css('background','');
			})
			pwdField.blur(function () {
				if (pwdField.val() == '') {
					pwdField.css('background','url(/platform/i/c/password.png) no-repeat 44px center');
				}else{
					pwdField.css('background','');
				}
			});
			pwdField.submit(function(){
				pwdField.css('background','');
			})
			pwdField.attr('autocomplete','off');
		})
		$('#userName').change(function(){
			$("#inp_psw,#password").css('background','');
		});
    });    
  }   

/*--------头部随宽度自适应版心变化*/
$(function(){
	var widths = $('.hInner .logo').width() + $('.hInner .logo_txt').width()+ 45 + $('.hInner .nav').width() + 35 + $('.hInner .h_user').width();
	if(widths<1200){
		$('.hInner').css('width','1200px');
	}else if(widths<1300){
		$('.hInner').css('width','1300px');
	}else if(widths<1400){
		$('.hInner').css('width','1400px');
	}else{
		$('.hInner').css('width','1500px');
	}
})

$(function(){
	var closeStatus = true;
	$.ajax({
		url:"/getStatus",
		async:false,
		cache:false,
		dataType:'json',
		success:function(data){
			closeStatus = data;
		}
	});
	var mozilla =/firefox/.test(navigator.userAgent.toLowerCase())
        ||/kooclient/.test(navigator.userAgent.toLowerCase()) ;
	if(!mozilla&&!closeStatus){
		$('.ji-hint').show();
	}
	$(".hintbtn").click(function(){
		$.ajax({
			url:"/close",
			async:false,
			cache:false,
			dataType:'json',
			success:function(){
			}
		});
		$('.ji-hint').hide();
	});
});

//DOM结构加载完成
$(function() {

});
