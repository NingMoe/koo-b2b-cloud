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
			console.log(o.index);
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

//DOM结构加载完成
$(function() {

});
