/**
手工组卷-题型选题
jsonData : 页面初始化的json对象
typeA：0:固定模板  1:随机模板
typeB: 0:题型选题  1:生成试卷  2:试卷选题
**/
$.extend({
	exam:function(jsonData,typeA,typeB,url){
		if(!jsonData){
			return;
		}
		var jsonHtml = '',
		examHtml = '',
		data = jsonData,
		typeAHtml='',
		typeBHtml='',typeBBtn,
		examCount = [],//该数组保存模版支持的题型和题型可加题目的总数
		typeA = typeA || 0,
		examWap = $('#examWap'),//模板的存放位置
		examList = $('#examList');//试题列表
		var addImg1="<img src='/platform/i/c/add_img1.png'/>再加点题";
		var addImg2="<img src='/platform/i/c/add_img2.png'/>再加点题";
		
		//循环数据
		for(var i=0, len = data.length; i<len; i++){
			if(data[i]['minCount']){
				jsonHtml +='<dl class="types_dl"><dt><h6><i>●</i><b title="'+data[i]['name']+'">'+data[i]['name']+'</b><em><span id="question_'+data[i]['questionType']+'"  data-index="'+i+'">'+data[i]['joinedQuestionCount']+'</span>/<span>'+data[i]['minCount']+'-'+data[i]['maxCount']+'</span></em></h6></dt>';
			} else {
				jsonHtml +='<dl class="types_dl"><dt><h6><i>●</i><b title="'+data[i]['name']+'">'+data[i]['name']+'</b><em><span id="question_'+data[i]['questionType']+'"  data-index="'+i+'">'+data[i]['joinedQuestionCount']+'</span>/<span>'+data[i]['questionCount']+'</span></em></h6></dt>';
			}
			//子元素
			var dChild = data[i]['children'];
			if(dChild.length){
				for(var j=0, jlen = dChild.length; j<jlen; j++ ){
					jsonHtml +='<dd class="fc" title="'+dChild[j]['name']+'"><span class="js_span">'+dChild[j]['name']+'</span><em class="joinedCountFill" id="question_'+data[i]['questionType']+'_'+dChild[j]['questionType']+'"   data-index="'+j+'">'+dChild[j]['joinedQuestionCount']+'</em><span> /</span> <i>'+dChild[j]['questionCount']+'</i></dd>';
					//记录各个类型题目的总数
					var index = data[i]['questionType'] + '_' + dChild[j]['questionType'];
					//console.log(index +'======'+dChild[j]['questionCount']);
					examCount[index] = dChild[j]['questionCount'];
				}
			}
			jsonHtml +='</dl>';
		}
		//编辑修该数据
		if(arguments.length === 1){
			$('#jsonWap').html(jsonHtml);
			return;
		}
		//随机模版
		if(typeA === 1){
			typeAHtml = '<a class="compile" href="javascript:;" onclick="openEdit();">编辑模版</a>';
		}
		if(typeB === 0 ||typeB === 2){
			typeBBtn = '<a class="types_a" href="javascript:;" id="operBtn">生成试卷</a>';
		}else if(typeB === 1){
			var temp;
			if(g_paper){
			  temp=g_paper['templateType'];
			}
			if(temp &&temp==1){
				//页面加载处理加题按钮
				//随机模版可用
				typeBHtml = '<a class="types_add" href="'+url+'">'+addImg2+'</a>';
			}else{
				//固定模版首次加载置灰
				typeBHtml = '<a class="types_add" >'+addImg1+'</a>';
			}
			typeBBtn = '<a class="types_a" href="javascript:;" id="operBtn">保存试卷</a>';
		} 
		examHtml = '<div class="quest_types" id="quest_types">\
			'+typeBHtml+'\
			<div class="types_wrap">\
				'+typeAHtml+'\
				<div id="jsonWap">'+jsonHtml+'</div>\
				'+typeBBtn+'\
			</div>\
			<a class="types_atop" href="#"></a>\
		</div>';
		
		//初始化弹层数据
		examWap.html(examHtml);
		if(!isCompleteFill()){
			//刷新页面判断,固定模版未加满题 加题按钮可用
			$(".types_add").attr('href',url);
			$(".types_add").html(addImg2);
		} 
		//加入/删除试题
		 //examList.undelegate('click').delegate('a.examAdd,a.examDel','click',function(){
		//examList.find('a.examAdd,a.examDel').on('click',function(){
		examList.find('a.examAdd,a.examDel').die('click').live('click',function(){
			var id = $(this).data('id'),
			typeA = $(this).data('typea'),
			typeB = $(this).data('typeb'),
			curQuestion = $('#question_'+typeA),
			curQuestionChild = $('#question_'+typeA+'_'+typeB),
			curQuestion_V = parseInt(curQuestion.html()),
			curQuestionChild_V = parseInt(curQuestionChild.html()),
			examIdArr = JSON.parse($.cookie('examIdArr')) || [];
			if(typeof(examIdArr)=='string'){
				examIdArr=eval(examIdArr);
			}
			var typeA_B=examCount[typeA+'_'+typeB];
			if(typeA_B && isNaN(typeA_B) && typeA_B.indexOf('-')>0){
				//小题范围处理 取最大值
				typeA_B=parseInt(typeA_B.split('-')[1]);
			}
			if(isNaN(typeA_B)){
				typeA_B=parseInt(typeA_B);
			}
			
			if(typeA_B ===0){
				pop_alert('温馨提示', '请设定模版题目数量！',3000);
				return ;
			}
			if(!typeA_B){
				pop_alert('温馨提示', '模版不支持该题型！',3000);
				return ;
			}
			var addflag=true;
			if($(this).hasClass('examAdd')){
				//判断添加的试题是否已存在
				if($.inArray(id,examIdArr) >= 0){
					pop_alert('温馨提示', '该题不能重复加入！',3000);
					return;
				}
				//判断添加的试题是否超出总个数
				if(curQuestionChild_V+1 > typeA_B){
					var templateType=$("#templateType").val();
					if(templateType ==='1') {
						//随机模版
						pop_alert('温馨提示', '请编辑模板试题的数量！',3000);
						return ;
					}
					pop_alert('温馨提示', '该题型下试题已全部添加！',3000);
					return;
				}
				curQuestion_V ++;
				curQuestionChild_V ++;
				//添加试题id
				examIdArr.push(id);
				//alert('试题添加成功');
				
			}else if($(this).hasClass('examDel')){
				curQuestion_V --;
				curQuestionChild_V --;
				//删除试题id
				if(typeof (examIdArr)=='string'){
					examIdArr=eval(examIdArr);
				}
				examIdArr.splice($.inArray(id,examIdArr),1);
				pop_alert('温馨提示','删除成功!',1000);
				addflag=false;
				//删除题目
				$(this).closest('div.js-exam').remove();
				$(".types_add").attr('href',url);
				$(".types_add").html(addImg2);
			}
			 
			
			//保存修改后试题的id
			examIdArr = JSON.stringify(examIdArr);
			$.cookie('examIdArr',examIdArr,{path:'/'});
			
			curQuestion.html(curQuestion_V);
			curQuestionChild.html(curQuestionChild_V);
			
			//保存模板json字符串
			var indexA = parseInt(curQuestion.data('index')),
			indexB = parseInt(curQuestionChild.data('index'));
			data[indexA]['joinedQuestionCount'] = curQuestion_V;
			data[indexA]['children'][indexB]['joinedQuestionCount'] = curQuestionChild_V;
			//实时缓存模版数据
			$.ajax({
						  url: '/teacher/testpaper/cacheTemplate',
						  type:'POST',
						  async:false,
						  data : {
							  "templateJson":JSON.stringify(data),
							  "delete":false      
						  },
						  dataType:'json',
						  success:function(data) {
							  if(addflag){
								  pop_alert('温馨提示', '加入成功！',1000);
							  }
						  }
					});
		});
		
		//生成和保存试卷
		$('#operBtn').click(function(){
			var paperType=$("#paperType").val();
			var templateType=$("#templateType").val();
			var paperId=$("#paperId").val();
			if(!isCompleteFill()){
				//error 系统异常，请稍后再试
				pop_alert('温馨提示', '试卷试题不完整，请继续添加！',3000);
				return;
			}
			//删除cookie和缓存
//			$.cookie('examIdArr',null,{path:'/'});
			var examIdArr = $.cookie('examIdArr');
			if(examIdArr&&examIdArr!='[]'){
				//提交数据（提交题目id，缓存获取模版数据并清除缓存和cookie数据）
				location.href='/paper/create?paperType='+paperType+'&templateType='+templateType+'&paperId='+paperId;
			}else{
				pop_alert('温馨提示', '试卷试题为空，不能生成试卷！',3000);
			}
		});
	}
});
function isCompleteFill(){
	var templateType=$("#templateType").val();
	var ieltsId=$("#ieltsId").val();
	var paperType=$("#paperType").val();
	var ieltsFalg=paperType == ieltsId;//雅思，也必须加满
	if( (templateType ==='1' ||templateType ==='2')){
		//!ieltsFalg &&  去掉雅思
		return true;//随机模版不受限制,智能组卷
	}
	var flag=true;
	var jsonWap=$("#jsonWap dl dt em");
	for(var i=0, len = jsonWap.length; i<len; i++){
		var em=$(jsonWap[i]).children('span') ;
		var currNum=parseInt($(em[0]).html());
		var rangNum=$(em[1]).html();
		var  allNum=parseInt($(em[1]).html());
		if(isNaN(rangNum) ){
			if(currNum<allNum){
				return false;
			}
		}else{
			if(currNum!=allNum){
				//固定模版或雅思 没有加满不能生成试卷
				return false;
			}
		}
	}
	return flag;
}


