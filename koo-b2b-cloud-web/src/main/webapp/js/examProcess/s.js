$(function() {
	//开始考试--我要交卷
//	$('#hand_exam').click(function() {
//		pop_confirm('温馨提示', '您还有20道题没答！确定交卷？', function() {
//			console.log('确定');
//		}, function() {
//			console.log('取消');
//		});
//	});
	//开始考试--隐藏、显示倒计时
	$('#hide_aclick').click(function(){
			var brots = $(this).next('#hide_span');
			if(brots.is(":hidden")){
				$(this).html('隐藏倒计时');
				brots.show();
			}else{
				$(this).html('显示倒计时');
				brots.hide();
			}
		})
	
	//随堂测评--TAB切换
	$('#tab_ceping').tabslider();
	//随堂测评--开始测评
	$('#hide_aclick2').click(function(){
			var brots = $(this).next('#hide_span2');
			if(brots.is(":hidden")){
				$(this).html('隐藏');
				brots.show();
			}else{
				$(this).html('显示');
				brots.hide();
			}
		})

	//我的收藏--解析
	$('#subj_wrap').find('.as_3').click(function() {
		var cur_box = $(this).parent('.titl').next('.anal_box');
		if (cur_box.is(':hidden')) {
			$('p.titl a.as_3').removeClass('curr');
			$('p.titl a.as_3').siblings('b').removeClass('curr2');
			$('div.anal_box').hide();
			$(this).addClass('curr');
			$(this).siblings('b').addClass('curr2');
			cur_box.show();
		} else {
			$(this).removeClass('curr');
			$(this).siblings('b').removeClass('curr2');
			cur_box.hide();
		}
	});
	$('#subj_wrap .js_hide').click(function() {
		$('p.titl a.as_3').removeClass('curr');
		$('p.titl a.as_3').siblings('b').removeClass('curr2');
		$(this).parents('.anal_box').hide();
	});

	//我的收藏--取消收藏
	$('#subj_wrap').find('.as_5').click(function() {
		pop_confirm('温馨提示', '确认删除该收藏？', function() {
			console.log('确定');
		}, function() {
			console.log('取消');
		});
	});
	//我的收藏--收藏成功
	$('#subj_wrap').find('.as_2').click(function() {
		pop_confirm('温馨提示', '收藏成功<br />请到我的收藏进行查看！', function() {
			
		})
	});

	//开始练习--选择试卷
	$('table.tab_w985 td span').click(function() {
		$('table.tab_w985 td span').attr('class', 'spans');
		$(this).addClass('current');
	})
	
	//我的考试--答案解析
	$('.card_as a').click(function() {
		$(this).addClass('cur').siblings().removeClass('cur');
	})
	
	//我的考试--综合对比分析
	$('p.comp_title span').click(function(){
		$(this).addClass('current').siblings().removeClass('current');
		$('div.comp_tab table').eq($(this).index()).addClass('current').siblings().removeClass('current');
	})
	//在线练习--选择框
	$('.wrong_txt a').click(function(){
		$(this).addClass('current').siblings().removeClass('current');
	})
	//在线练习--开始练习
	$('ul.b_pract1 li,ul.b_pract2 li').click(function(){
		$(this).addClass('current').siblings().removeClass('current');
	})
	
});
