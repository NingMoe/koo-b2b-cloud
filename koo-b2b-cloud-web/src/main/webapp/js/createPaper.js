var g_paper=globalPaper;
/**获取本地域     */
function getLS() {
	return window.localStorage;
}
/**获取本地域中值     */
function LSGET(key) {
	var _result = getLS().getItem(key);
	if (undefined == _result) {
		_result = null;
	}
	return _result;
}
/**设置本地域中值     */
function LSSET(key, value) {
	getLS().setItem(key, value);
}
/**删除本地域中值     */
function LSREMOVE(key) {
	getLS().removeItem(key);
}
/**清除本地域中值     */
function LSCLS() {
	getLS().clear();
}
/**将JSON转成String   */
function tostr(value) {
	return JSON.stringify(value);
}
/**将String转成JSON     */
function tojson(value) {
	return JSON.parse(value);
}

/** 保存编辑过来的分数 */
function takeEditScore(){
	try {
		var scoreJson = tojson(LSGET("editScore"));
		if(null == scoreJson){
			scoreJson = {};
		}
		jQuery("[score!=0]").each(
				function(index, ele) {
					if (undefined == jQuery(ele).attr("questionid")
							|| undefined == jQuery(ele).attr("score")) {
					} else {
						scoreJson[jQuery(ele).attr("questionid")] = jQuery(ele).attr("score");
					}
				});
		LSSET("editScore", tostr(scoreJson));
	} catch (e) {
		// TODO: handle exception
	}
}
/** 把编辑时的分数放回页面元素上 */
function putEditScore(){
	try {
		var scoreJson = tojson(LSGET("editScore"));
		if (null != scoreJson) {
			for ( var key in scoreJson) {
				jQuery("[questionid=" + key + "]").attr("score", scoreJson[key]);
			}
		}
	} catch (e) {
		// TODO: handle exception
	}
}
/** 为0分的样式标红 */
function showRed(){
	jQuery(".qscore").each(function(sindex,sele){
		 jQuery(sele).css("color","#000000").css("font-weight","normal");
		if("(0)分" == jQuery(sele).text()){
			 jQuery(sele).css("color","red").css("font-weight","bold");
		}
	});
}



//$('.oppClass2').show();//设置显示答案,默认显示
$(document).ready(function(){
	$('.js_lastdl').addClass('hideDiv');//.hide();
	var _isShowPaper=isShowPaper();
	if(_isShowPaper){
		selectQuestionBar();
		initTree();
		initEvent();
		hidenTreeHeadFooter();
	if(previewExt()){
		paperQuestion4Tiku();
	}
		return ;
	}
	initFun();
	initTree();
	initEvent();
	
	jQuery("#operBtn").live("click",function(){
		showRed();
	});
});
function initTree(){
	//2.加载三级分类及加载试题
	   var jsonData =loadTemplateData();
	   // 2: 0:题型选题  1:生成试卷  2:试卷选题
	   var questionOrPaperTypeSelect=$("#questionOrPaperTypeSelect").val();
	   var templateType=99;
	   var url=g_paper['ctx']+'/teacher/testpaper/toTestPaperform?templateType='+g_paper['templateType']+'&paperType='+g_paper['paperType']+'&paperId='+g_paper['paperId'];
	   var typeB=1;
	   if(isShowPaper()){
	   	typeB=2;
	   	templateType=parseInt(g_paper['templateType']);
	   }
	$.exam(jsonData,templateType,typeB,url);
	if(g_paper['templateType']==2){
		$('#examWap .types_add').hide();
	}
}
function initEvent(){
	//编辑试卷名称
	$('.examp_title .ep_h2 span').on('click', function(event) {
		event.preventDefault();
		input=$('#popDiv input');
		input.val($('#paper_name').text());
		$('#popDiv,#bgDiv').show();
		$('#bgDiv').height($(document.body).height());
		$('#popDiv .form_txt').remove();
		$('#popDiv .a_1').on('click',function(event){
			event.preventDefault();
			var valid=$(this).parent().parent().find('.form_txt');
			if(valid.size()>0&&valid.text().length>0){
				return;
			}
			$('#paper_name').text(input.val());
			$('#popDiv').hide();
		});
		$('#close_grade').on('click',function(event){
			event.preventDefault();
			$('#popDiv').hide();
		});
	});
	//保存试卷
	$('#operBtn').unbind('click').click(function(event){
		event.preventDefault();
		toSavePaper();
	});
}
/*
	保存试卷结构到数据库,那个结构下面包含那些试题,试题分数是多少
*/
function toSavePaper(){
	if(!isCompleteFill()){
		pop_alert('温馨提示', '试卷试题不完整，请继续添加！');
		return;
	}
	var examIdArr = $.cookie('examIdArr');
	if(examIdArr&&examIdArr!='[]'){}else{
		pop_alert('温馨提示', '试卷试题为空，不能生成试卷！',3000);
		return;
	}
	if(validBeforeSave()){
		pop_alert('温馨提示', '有未设定分值试题,请设定后保存 !',3000);
		return;
	}
	var rs=[];//提交的数据
	$('*[questionId]').each(function(index, el) {
		var teId=$(this).attr('teId');
		var qid=$(this).attr('questionId');
		var sid=$(this).attr('structureId');
		var score=$(this).attr('score');
		obj=sid+'_'+teId+'_'+qid+"_";
		if(score){
			obj+=score;
		}
		rs[rs.length]=obj;
	});

	$('#rs').val(rs);
	$('form input[name="paperName"]').val($('#paper_name').text());
	$('#saveForm').submit();
}
/*
上下移动 和收藏删除
*/
function initFun_sub1(){
	s2='<a class="bgimg" href="javascript:;"><em class="cur2"></em></a>';
	s3='<a class="bgimg" href="javascript:;"><em class="cur"></em></a>';
	//单独题
	var s='<span class="ep_handle"> '+s2+s3+'<a class="bgimg colg" href="javascript:;">收藏</a> <a class="bdbg" href="javascript:;">设定分值</a> <a class="bgo" href="javascript:;">答案</a> <a class="bgg" href="javascript:;">删除</a> </span>';
	//大题
	var s_1='<span class="ep_handle"> '+s2+s3+'<a class="bgimg colg" href="javascript:;">收藏</a><a class="bgg" href="javascript:;">删除</a> </span>';
	//小题
	var s_2='<span class="ep_handle"> <a class="bdbg" href="javascript:;">设定分值</a> <a class="bgo" href="javascript:;">答案</a> </span>';
	$('.ep_details').each(function(index, el){
		var size=$(this).find('*[teid]').filter(function(index){return $(this).attr('teid')>0}).size();
		if(size>0){
			$(this).prepend(s_1);
			$(this).find('*[teid]').filter(function(index){return $(this).attr('teid')>0}).each(function(index, el) {
				$(this).prepend(s_2);
			});
		}else{
			$(this).prepend(s);
		}
	});
$('.ep_details').hover(function() {
	var th=$(this).children().first();
	if(th.hasClass('ep_handle')){
		th.find('.cur').parent().show();
		th.find('.cur2').parent().show();
		var ep=$(this).closest('.js-exam');
		if(ep.next().size()==0||ep.next().find('.topic_tit').size()>0){//下
			th.find('.cur').parent().hide();
		}
		ep=$(this).closest('.js-exam');
		if(ep.prev().size()>0&&ep.prev().find('.ep_details').size()==0){//上
			th.find('.cur2').parent().hide();
		}

		th.css({'position':'absolute','display':'block','top':0,'right':0});
	//	th.show();
	}
},function(){
	var th=$(this).children().first();//.find('span.ep_handle');
		if(th.hasClass('ep_handle')){
		th.hide();
	}
});
$('*[taga]').hover(function() {
	if(!$(this).children().first().hasClass('ep_handle')){
		return;
	}
	$(this).css('position','relative');
	var th=$(this).find('.ep_handle').first();

	th.find('.cur').parent().show();
	th.find('.cur2').parent().show();
	var ep=$(this).closest('.js-exam');
	if(ep.next().size()==0||ep.next().find('.topic_tit').size()>0){//下
		th.find('.cur').parent().hide();
	}
	ep=$(this).closest('.js-exam');
	if(ep.prev().size()>0&&ep.prev().find('.ep_details').size()==0){//上
		th.find('.cur2').parent().hide();
	}
	th.css({'position':'absolute','display':'block','top':0,'right':0});
	//th.show();
}, function() {
	var th=$(this).find('.ep_handle')
	th.hide();	
});

}
function initFun(){
	initFun_sub1();
	/*收藏导航条start*/
	// s2='<a class="bgimg" href="javascript:;"><em class="cur2"></em></a>';
	// s3='<a class="bgimg" href="javascript:;"><em class="cur"></em></a>';
	// var s='<span class="ep_handle"> '+s2+s3+'<a class="bgimg colg" href="javascript:;">收藏</a> <a class="bdbg" href="javascript:;">设定分值</a> <a class="bgo" href="javascript:;">答案</a> <a class="bgg" href="javascript:;">删除</a> </span>';
	// $('dl dt').each(function(index, el){
	// 	$(this).append(s);
	// });
	// /*收藏导航条end*/
	// /*导航条show 处理隐藏上下的图标 start*/
	// //.nobold:hover span.ep_handle{display:block;}
	// $('.nobold').hover(function() {
	// 	var ss=$(this).find('span.ep_handle');
	// 	if(ss.size()>0){
	// 		ss.css("display","block");
	// 		ss.find('.cur').parent().show();
	// 		ss.find('.cur2').parent().show();
	// 		var ep=$(this).closest('.js-exam');
	// 		if(ep.next().size()==0||ep.next().find('.topic_tit').size()>0){//下
	// 			ss.find('.cur').parent().hide();
	// 		}
	// 		ep=$(this).closest('.js-exam');
	// 		//console.log(ep.parent()[0].outerHTML);
	// 		if(ep.prev().size()>0&&ep.prev().find('.ep_details').size()==0){//上
	// 			ss.find('.cur2').parent().hide();
	// 		}
	// 		ss.show();
	// 	}
	// }, function() {
	// 	var ss=$(this).find('span.ep_handle');
	// 	if(ss.size()>0){
	// 		ss.hide();
	// 	}
	// });
	/*导航条show 处理隐藏上下的图标 end*/
	/*收藏功能 start*/
	$('span.ep_handle a.colg').on('click',function(){
		// var qId=$(this).closest('*[questionId]').attr('questionId');
		// doCollection(qId);
		var obj=$(this).closest('.ep_details').children().find('*[questionId]');
		var qId=obj.attr('questionId');
		var code= $(this).closest('.ep_details').attr('sname');
		doCollection(qId,code);
	});
	/*收藏功能 end*/
	/*设定试题分数 start*/
	$('.ep_handle .bdbg').on('click',function(event){
		event.preventDefault();
		var th=$(this);
		var input=$('#popDiv2 input');
		var dl=$(this).closest('.ep_handle').next();//.closest('dl');
		if(dl.attr('score')){
		}else{
			dl=th.closest('.ep_handle').parent();
		}
		//console.log(dl.attr('score'));
		input.val(dl.attr('score'));
		$('#bgDiv2').height($(document.body).height());
		$('#popDiv2,#bgDiv2').show();
		$('#popDiv2 .form_txt').remove();
		$('#close_grade2').on('click',function(event){
			event.preventDefault();
			$('#popDiv2').hide();
		});
		$('#popDiv2 .a_1').die('click').live('click',function(event){
			event.preventDefault();
			var input2=$('#popDiv2 input');
			var dl=th.closest('.ep_handle').next();
			if(dl.attr('score')){
				// console.log(4444);
			}else{
				dl=th.closest('.ep_handle').parent();
			}
			var valid=$(this).parent().parent().find('.form_txt');
			if(valid.size()>0&&valid.text().length>0){
				return;
			}
			dl.attr('score',input2.val());
			//console.log(dl.attr('score'));
			$('#popDiv2').hide();
			dl.find('.qscore').text('('+input2.val()+')分');
			//$(this).parent().parent().find('.qscore').text('('+input2.val()+')分');
			calParentScore(th);
			showRed();
		});
	});
	/*设定试题分数 end*/
	/*显示答案 start*/
	$('.ep_handle .bgo').on('click',function(event){
		var div=$(this).parent().parent().find('.oppClass');
		div.children().not('.js_lastdl').not('.js_nohide').hide();
		// console.log(div.size())
		div.show();
		div.find('.js_lastdl');
		div.find('.js_lastdl').toggleClass('showdl').removeClass('hideDiv');

	});
	/*显示答案 end*/
	/*删除试题 start*/
	$('.ep_handle .bgg').each(function(index, el) {
		var dl=$(this).parent().next();//.closest('dl');
		$(this).addClass('examDel').attr('data-id',dl.attr('questionId')).attr('data-typea',dl.attr('taga')).attr('data-typeb',dl.attr('tagb'));
		//$(this).addClass('examDel').attr('data-id',dl.attr('questionId')).attr('data-typea',102517).attr('data-typeb',102527)
	});
	$('.ep_handle .bgg').on('click',function(event){
		var th=$(this).parent().next();//.closest('dl');
			th.remove();
	});
	/*删除试题 end*/
	/*移动题目start*/
	$('.ep_handle .cur').on('click',function(){//下移
		var obj=$(this).closest('.js-exam');
		obj.next().after(obj);
	});
	$('.ep_handle .cur2').on('click',function(){//上移
		var obj=$(this).closest('.js-exam');
		obj.prev().before(obj);
	});
	/*移动题目end*/
	/*增加题目分数显示*/
	addQuestionScoreFun();
}
/**
设置完小题,计算大题的分数
*/
function calParentScore(th){
	var first=null;
	var more=false;
	var total=0.0;
	th.closest('.js-exam').find('*[score][taga][tagb]').each(function(index, el) {
		if(index==0){
			first=$(this);
		}else{
			more=true;
			total+=parseFloat($(this).attr('score'));
		}

	});
	if(more){
		first.attr('score',total).find('.qscore:eq(0)').text('('+total.toFixed(2)+')分');
	}
}
function addQuestionScoreFun(){
	putEditScore();
	$('.js_score').each(function(index, el) {
		var score=$(this).closest('*[score]').attr('score');
		if(score==''){score=0;}
		var s='<span class="qscore" style="color:#C5BFBF;">('+score+')分</span>';
		$(this).append(s)
	});
	return;
	$('.nobold').each(function(index, el) {
		var score=$(this).closest('dl').attr('score');
		if(score==''){score=0;}
		var s='<span class="qscore" style="color:#C5BFBF;">('+score+')分</span>';
		$(this).append(s)
	});
}
function doCollection(id,questionCode){
	//console.log(id+'===='+questionCode)
	$.ajax({
			  url: g_paper['ctx']+'/collection/insert',
			  async:true,
			  data : {"questionId":id,"questionCode":questionCode},
			  success:function(data) {
				  if(data>0){
					  pop_alert('温馨提示', '收藏成功！',3000);
				  }else  if(data==0){
					  pop_alert('温馨提示', '试题已收藏！',3000);
				  }
				  
			  }
		});

}
//加载模版数据
function loadTemplateData(){
	var jsonData;
	examIdArr=$.cookie('examIdArr');
	 //console.log(examIdArr);
	 //console.log(typeof examIdArr);
	if(examIdArr&&examIdArr.indexOf('[')!=-1){
		//examIdArr=examIdArr.substr(examIdArr.indexOf('['),examIdArr.indexOf(']'));
		examIdArr=examIdArr.replace("\"",'').replace("\"",'').replace("[",'').replace("]",'');
	// console.log(examIdArr);
	}else{
		examIdArr="";
	}
    $.ajax({
				  url: g_paper['ctx']+'/paper/loadtemplateWithIds',
				  async:false,
				  type:'POST',
				  data : {"paperType":g_paper['paperType']
				  			,"ids":examIdArr
						},
				  dataType:'json',
				  success:function(data) {
					  jsonData=  data;
				  }
			});
   return jsonData;
}

//-------------/paper/showPaper-------------------
function isShowPaper(){
	return window.location.href.indexOf('/paper/showPaper')>-1;
}
function hoverBar(){
	$('.ep_details').hover(function() {
		var th=$(this).children().first();
		$(this).css('position','relative');
		if(th.hasClass('ep_handle')){
			th.find('.cur').parent().show();
			th.find('.cur2').parent().show();
			var ep=$(this).closest('.js-exam');
			if(ep.next().size()==0||ep.next().find('.topic_tit').size()>0){//下
				th.find('.cur').parent().hide();
			}
			ep=$(this).closest('.js-exam');
			if(ep.prev().size()>0&&ep.prev().find('.ep_details').size()==0){//上
				th.find('.cur2').parent().hide();
			}

			th.css({'position':'absolute','display':'block','top':0,'right':0});
			//th.show();
		}
	},function(){
		var th=$(this).children().first();//.find('span.ep_handle');
			if(th.hasClass('ep_handle')){
			th.hide();
		}
	});
	$('*[taga]').hover(function() {
		if(!$(this).children().first().hasClass('ep_handle')){
			return;
		}
		$(this).css('position','relative');
		var th=$(this).find('.ep_handle').first();

		th.find('.cur').parent().show();
		th.find('.cur2').parent().show();
		var ep=$(this).closest('.js-exam');
		if(ep.next().size()==0||ep.next().find('.topic_tit').size()>0){//下
			th.find('.cur').parent().hide();
		}
		ep=$(this).closest('.js-exam');
		if(ep.prev().size()>0&&ep.prev().find('.ep_details').size()==0){//上
			th.find('.cur2').parent().hide();
		}

		th.css({'position':'absolute','display':'block','top':0,'right':0});
		//th.show();
	}, function() {
		var th=$(this).find('.ep_handle')
		th.hide();	
	});
}
function selectQuestionBar(){
	var s2='';
	var s3='';
	//单独题
	var s='<span class="ep_handle"> '+s2+s3+'<a class="bgg" href="javascript:;">加入试卷</a><a class="bgo" href="javascript:;">查看解析</a> <a class="bgimg colg" href="javascript:;">收藏</a> </span>';
	//大题
	var s_1='<span class="ep_handle"> '+s2+s3+'<a class="bgg" href="javascript:;">加入试卷</a><a class="bgimg colg" href="javascript:;">收藏</a> </span>';
	//小题
	var s_2='<span class="ep_handle"><a class="bgo" href="javascript:;">查看解析</a> </span>';
	$('.ep_details').each(function(index, el){
		$(this).prepend(s);
	});
	hoverBar();

	/*收藏功能 start*/
	$('span.ep_handle a.colg').on('click',function(){
		var obj=$(this).closest('.ep_details').children().find('*[questionId]');
		var qId=obj.attr('questionId');
		var code= $(this).closest('.ep_details').attr('sname');
		doCollection(qId,code);
	});
	/*收藏功能 end*/
	/*显示答案 start*/
	$('.ep_handle .bgo').on('click',function(event){
		var div=$(this).parent().parent().find('.oppClass');
		div.children().not('.js_lastdl').not('.js_nohide').hide();
		div.show();
		div.find('.js_lastdl');
		div.find('.js_lastdl').toggleClass('showdl').removeClass('hideDiv');;

	});
	/*显示答案 end*/
	/*增加题目分数显示*/
	addQuestionScoreFun();
	/*加入试卷 start*/
	$('span.ep_handle .bgg').each(function(){
		var dl=$(this).parent().next();
		if(dl.attr('questionId')){

		}else{
			dl=$(this).parent().parent();
		}
		$(this).addClass('examAdd').attr('data-id',dl.attr('questionId')).attr('data-typea',dl.attr('taga')).attr('data-typeb',dl.attr('tagb'));
	});
	/*加入试卷 end*/
	return;

	// $('dl dt').each(function(index, el){
	// 	$(this).append(s);
	// });
	$('.nobold').hover(function() {
		var ss=$(this).find('span.ep_handle');
		if(ss.size()>0){
			ss.css("display","block");
			ss.show();
		}
	}, function() {
		var ss=$(this).find('span.ep_handle');
		if(ss.size()>0){
			ss.hide();
		}
	});
	$('.nobold').find('span.ep_handle .bgg').each(function(){
		var dl=$(this).closest('.nobold').parent();
		$(this).addClass('examAdd').attr('data-id',dl.attr('questionId')).attr('data-typea',dl.attr('taga')).attr('data-typeb',dl.attr('tagb'));
	});


	$('.nobold').delegate('span.ep_handle .bgo','click',function(){
		var div=$(this).closest('.nobold').parent().find('.oppClass');
		div.children().not('.js_lastdl').not('.js_nohide').hide();
		div.show();
		div.find('.js_lastdl');
		div.find('.js_lastdl').toggleClass('showdl');
	});
	//收藏试题
	$('.nobold').delegate('span.ep_handle .bgimg','click',function(){
		// var dl=$(this).closest('.nobold').parent();
		// var qId=dl.attr('questionId');
		// doCollection(qId);

		var obj=$(this).closest('.ep_details').children().find('*[questionId]');
		var qId=obj.attr('questionId');
		var code= $(this).closest('.ep_details').attr('sname');
		doCollection(qId,code);
	});
}
function hidenTreeHeadFooter(){
	$('#examWap .types_add').hide();
	//$('#jsonWap').next().hide();

	$('#operBtn').unbind('click').click(function(event){
		event.preventDefault();
		var paperType=$("#paperType").val();
		var templateType=$("#templateType").val();
		var paperId=$("#paperId").val();
		if(!isCompleteFill()){
			// pop_alert('温馨提示', '题目未添加完毕，请添加完成后生成试卷！',function(){
			// 	window.location.href='/teacher/testpaper/toTestPaperformPaper?paperType=102569&templateType=1'
			// });
			pop_alert('温馨提示', '试卷试题不完整，请继续添加！',3000);
			return;
		}
		var examIdArr = $.cookie('examIdArr');
		if(examIdArr&&examIdArr!='[]'){
			location.href='/paper/create?paperType='+paperType+'&templateType='+templateType+'&paperId='+paperId;
		}else{
			pop_alert('温馨提示', '试卷试题为空，不能生成试卷！',3000);
			return;
		}
		//提交数据（提交题目id，缓存获取模版数据并清除缓存和cookie数据）
		//保存试卷内容
		//$('#saveForm').submit();
	});
}
//-------------autocreate------------------
function isAutoPaper(){
	return window.location.href.indexOf('/paper/autoCreate')>-1;
}
//---------------试题页面 试卷题目收藏------------
/*
题库操作选题
*/
function previewExt(){
	var h=window.location.href;
	var t=h.indexOf('/paper/showPaper/')>-1;
	if(t&&h.indexOf('?tikuxuanshoucang=question')!=-1){
		return true;
	}

	return false;
}
/**
题库隐藏数据
*/
function paperQuestion4Tiku(){
	$('.ep_handle .bgg').remove();
	$('#examWap').remove();
}
//----------分数校验----------
function validBeforeSave(){
	$('.js-exam .topic_tit').removeClass('topicWarning')
var b=false;
	var ieltsId=$("#ieltsId").val();
	var paperType=$("#paperType").val();
	var ieltsFalg=paperType == ieltsId;//雅思
	if(ieltsFalg){
		//return b;
	}
	$('.qscore').each(function(index, el) {
		var score=$(this).text();	
		score=score.substring((score.indexOf("(") + 1),score.indexOf(")"));
		if(parseFloat(score)==0){
			$(this).closest('.js-exam').prev().find('.topic_tit').addClass('topicWarning');
			b=true;
		}
	});
	return b;
}
