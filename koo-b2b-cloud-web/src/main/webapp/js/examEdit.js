var tempErr='<i style="font-size:18px;margin:2px 0 0 6px;float:left;">*</i><b style="font-weight:normal;float:left;">模版数量不符：</b>';
//试卷设置
function paperSettingFn() {
	var elm = $('#paper_setting');
	var up = elm.find('.ico_up'), down = elm.find('.ico_down');
	var plus = elm.find('.ico_plus'), minus = elm.find('.ico_minus');
	var s = elm.find('li').size();
	var t = 450;
	var index = 0;
	var _expNum = /^[0-9]*$/;
	var ieltsId=$("#ieltsId").val();//配置文件获取雅思标签id
	var paperType=$("#paperType").val();
	var ieltsFalg=false;//判断是雅思组卷
	 if(ieltsId && paperType){
		 ieltsFalg=paperType == ieltsId;
	 }
	var go = {
		auto : function() {
			var _this = this;
			_this.onFn();
			var _val = 0,
			maxCount = function(obj){
				//判断是否大于最大值
				var liObj = obj.closest('li'),
				curV = parseInt(obj.val());
				maxNum = parseInt(liObj.find('div.inpA').data('max')),
				inpB = liObj.find('input.inpB'),
				minNum = parseInt(obj.data('min')) || 0,
				inpBCount = 0;
				//数据库题数
				var numdb = obj.attr('data-numdb');
				//模版二级标签题目数量（雅思不能修改这个数量）
				var tempNum=parseInt(obj.parent('.inp_num') .siblings('.inpA').attr('data-tempnum'));
				if(numdb>=0){
					//只有智能组卷需要判断题目数量
					if(curV-numdb>0){
						obj.val(numdb);
						pop_alert("温馨提示：","当前题库试题数量："+numdb,3000);
						return false;
					}
				}
				if(maxNum){
					inpB.each(function(){
						inpBCount += parseInt($(this).val());
					});
					if(inpBCount > maxNum){
						pop_alert('温馨提示', '试题总数数已超过最大值：'+maxNum,3000);
						obj.val(maxNum+curV-inpBCount); 
						return false;
					}
				}
//				if(ieltsFalg){//雅思注释1
//					//雅思二级题型模版数量不能修改
//					var minQuestionNum=0;
//					inpB.each(function(){
//						minQuestionNum += parseInt($(this).val());
//					});
//					if(minQuestionNum!=tempNum){
//							//obj.val(tempNum+curV-minQuestionNum); 
//						 // 提示雅思试题不满足于 模版数据
//						$(obj.parent('.inp_num') .siblings('.inpA').children('span')).html(tempErr+tempNum).show() ;
//					}else{
//						$(obj.parent('.inp_num') .siblings('.inpA').children('span')).hide() ;
//					}
//					
//			    }
				//判断是否小于最小值
				if(curV < minNum){
					pop_alert('温馨提示', '不能小于已选择的试题数量！',3000);
					obj.val(minNum);
					return false;
				}
				
				return true;
			};
			elm.find('.inp_num input.inp_txt').blur(function(){	
				var _val = $(this).val();
				if(!Number(_val)){
					$(this).val(0);
				}
				
				maxCount($(this));//判断是否大于最大值
			});
			plus.click(function() {
				//改变输入框的值
				var inp_txt = $(this).siblings('input.inp_txt');
				var _val = inp_txt.val();
				inp_txt.val((_val - 0 + 1));
				maxCount(inp_txt);//判断是否大于最大值
			});
			minus.click(function() {
				var inp_txt = $(this).siblings('input.inp_txt');
				var _val = inp_txt.val();
				var min = inp_txt.attr('data-min');
				 inp_txt.val((_val - 0 - 1) <= min ? min : (_val - 0 - 1));
				 maxCount(inp_txt);//ieltsFalg 复用雅思逻辑
				 if((_val - 0 - 1) < min){
					 pop_alert("温馨提示：","不能小于已选择的试题数量！");
				 }
			});
		},
		onFn : function() {
			var _this = this;
			up.on('click', function() {
				index = $(this).parent('li').index();
				_this.offFn();
				_this.upFn();
			});
			down.on('click', function() {
				index = $(this).parent('li').index();
				_this.offFn();
				_this.downFn();
			});
		},
		offFn : function() {
			var _this = this;
			up.off('click');
			down.off('click');
			setTimeout(function() {
				_this.onFn();
			}, t);
		},
		upFn : function() {
			if (index <= 0) {
				return false;
			}
			elm.find('li').eq(index).stop(true).animate({
				'opacity' : '0'
			}, t, function() {
				var $this = $(this);
				if (index <= 1) {
					$this.find('.ico_up').addClass('ico_up_no');
					elm.find('li').eq(index - 1).find('.ico_up').removeClass('ico_up_no');
				}
				if (index >= (s - 1)) {
					$this.find('.ico_down').removeClass('ico_down_no');
					elm.find('li').eq(index - 1).find('.ico_down').addClass('ico_down_no');
				}
				elm.find('li').eq(index - 1).removeClass('ico_up_no').before($this);
				$this.css('opacity', '1');
			});
		},
		downFn : function() {
			if (index >= (s - 1)) {
				return false;
			}
			elm.find('li').eq(index).stop(true).animate({
				'opacity' : '0'
			}, t, function() {
				var $this = $(this);
				if (index == 0) {
					$this.find('.ico_up').removeClass('ico_up_no');
					elm.find('li').eq(index + 1).find('.ico_up').addClass('ico_up_no');
				}
				if (index >= (s - 2)) {
					$this.find('.ico_down').addClass('ico_down_no');
					elm.find('li').eq(index + 1).find('.ico_down').removeClass('ico_down_no');
				}
				elm.find('li').eq(index + 1).after($this);
				$this.css('opacity', '1');
				
				//修改数据
			});
		}
	};
	go.auto();
}

$.extend({
	examEdit:function(jsonData,type){
		
		if(!jsonData){
			return;
		}
		
		var jsonHtml = '',classHtml='',
		data = jsonData,
		subType = type,
		paperSetting = $('#paper_setting');
		
		//循环数据
		for(var i=0, len = data.length; i<len; i++){
			
			if(i === 0){
				classHtml = '<a class="ico_set ico_up ico_up_no" href="javascript:;">上移</a>\
				<a class="ico_set ico_down" href="javascript:;">下移</a>';
			}else if(i === len-1){
				classHtml = '<a class="ico_set ico_up" href="javascript:;">上移</a>\
				<a class="ico_set ico_down ico_down_no" href="javascript:;">下移</a>';
			}else{
				classHtml = '<a class="ico_set ico_up" href="javascript:;">上移</a>\
				<a class="ico_set ico_down" href="javascript:;">下移</a>';
			}
			
			jsonHtml +='<li>'+classHtml;
			if(!data[i]['minCount']){
				jsonHtml +='<div class="inp_box inpA" data-tempNum="'+data[i]['questionCountStr']+'" data-index="'+i+'"><b style="float:left;">'+data[i]['name']+'</b><span style="display:none;color:#f00;font-size:12px;"></span></div>';
			}else{
				jsonHtml +='<div class="inp_box inpA" data-tempNum="'+data[i]['questionCountStr']+'" data-index="'+i+'"  data-min="'+data[i]['minCount']+'"  data-max="'+data[i]['maxCount']+'"><b style="float:left;">'+data[i]['name']+'</b><span style="display:none;color:#f00;font-size:12px;" ></span></div>';
			}
			
			//子元素
			var dChild = data[i]['children'];
			if(dChild.length){
				for(var j=0, jlen = dChild.length; j<jlen; j++ ){
					var numDB=dChild[j]['questionTypeNum'];
					var numDBStr=' ';
					if(numDB>=0){
						//智能组卷获取模版时，获取数据库题型题目数量
						numDBStr=' data-numDB="'+numDB+'" ';
					} 
					jsonHtml +='<div class="inp_num">\
					<label>'+dChild[j]['name']+'</label>\
					<a class="ico_set ico_minus" href="javascript:;" title="减">减</a>';
					var childNum=dChild[j]['questionCount'];
					if(childNum && isNaN(childNum) && childNum.indexOf('-')>0){
						//小题范围处理 取最大值
						childNum=parseInt(childNum.split('-')[1]);
					}
					jsonHtml +='<input class="inp_txt inpB" type="text" '+numDBStr+' value="'+childNum+'" data-index="'+j+'"  data-min="'+dChild[j]['joinedQuestionCount']+'"/>';
					
					jsonHtml +='<a class="ico_set ico_plus" href="javascript:;" title="加">加</a></div>';
				}
			}
			jsonHtml += '</li>';
		}
		
		//初始化弹层数据
		paperSetting.html(jsonHtml);

		//试卷设置操作
		paperSettingFn();
		
		//上移下移修改数据
		var changeArr = function(jsonData,i){
			var nextI = i + 1,
			temp = data[i];
			data[i] = data[nextI];
			data[nextI] = temp;
			return data;
		};
		
		$('#paper_setting').undelegate('click');//清楚上移下移已经加载的delegate事件
		
		$('#paper_setting').delegate('.ico_up','click',function(){
			var curLiobj = $(this).closest('li'),
			prevLiObj = curLiobj.prev('li');
			var curIndex = curLiobj.index(),
			upIndex = curIndex - 1;
			
			curLiobj.find('div.inpA').attr('data-index',upIndex);
			prevLiObj.find('div.inpA').attr('data-index',curIndex);
			
			data = changeArr(data,upIndex,$(this));
		});
		
		$('#paper_setting').delegate('.ico_down','click',function(){
			var curLiobj = $(this).closest('li'),
			nextLiObj = curLiobj.next('li');
			var curIndex = curLiobj.index(),
			nextIndex = curIndex + 1;
			
			curLiobj.find('div.inpA').attr('data-index',nextIndex);
			nextLiObj.find('div.inpA').attr('data-index',curIndex);
			
			data = changeArr(data,curIndex,$(this));
		});
		
		
		//保存修改的数据
		$('#paper_set_btn').unbind("click").click(function(){
			var ieltsId=$("#ieltsId").val();//配置文件获取雅思标签id
			var paperType=$("#paperType").val();
			var ieltsFalg=false;//判断是雅思组卷
			 if(ieltsId && paperType){
				 ieltsFalg=paperType == ieltsId;
			 }
//			if(ieltsFalg){//雅思注释2
//				 //雅思 要判断 修改的题数是否符合模版数据
//				if(!ieltsTempleteNum()){
//					//不能保存模版编辑
//					return false;
//				}
//			}
			paperSetting.find('li').each(function(){
				//inpA 二级题型元素   inpB： 三级
				var inpA = $(this).find('div.inpA'),
				inpB = $(this).find('input.inpB'),
				questionCount = 0;
				indexA = parseInt(inpA.data('index'));
				console.log('indexA:'+indexA);
				inpB.each(function(){
					var indexB = parseInt($(this).data('index')),
					v = parseInt($.trim($(this).val()));
					questionCount = questionCount + v;
					console.log('indexB:'+indexB);
					data[indexA]['children'][indexB]['questionCount'] = v;
				});
				data[indexA]['questionCount'] = questionCount;
			});
			 
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
						  success:function(ajaxdata) {
							//提交数据
							if(subType === 'pop'){
								$("#pop_paper_setting").hide();
								$("#pop_opa").hide();
								//window.location.reload()
								//更新页面模版
								  var jsonDataUP =loadTemplateData();
								   // 2: 0:题型选题  1:生成试卷  2:试卷选题
								   var questionOrPaperTypeSelect=$("#questionOrPaperTypeSelect").val();
								   var templateType=$("#templateType").val();
								$.exam(jsonDataUP,parseInt(templateType),parseInt(questionOrPaperTypeSelect));
							}else if(subType === 'page'){
								var paperType=$("#paperType").val();
								//提交数据（提交题目id，缓存获取模版数据并清除缓存和cookie数据）
								//templateType  0 固定模版  1随机模版  2智能组卷 
								location.href='/paper/autoCreate?paperType='+paperType+'&templateType=2';
							}
						  }
					});
			
		});
		
		
		//关闭弹层
		$("#poptopclose").unbind("click").click(function(){
			//关闭按钮
			$("#pop_opa").hide();
			$("#pop_paper_setting").hide();
		});
	}
});
//雅思模版编辑 要求二级题型数量同原始模版,否则不能保存模版编辑
function ieltsTempleteNum(){
	var tempList=$("#paper_setting li");
	for(var i=0, len = tempList.length; i<len; i++){
		var liObj=$(tempList[i]);
		var tag2Num=parseInt(liObj.children('.inpA').attr("data-tempnum") );
		 var tag3List=liObj.children('.inp_num').children('input');
		 var tag3Num=0;
		 for(var j=0, jlen = tag3List.length; j<jlen; j++){
			 tag3Num+=parseInt($(tag3List[j]).val());
		 }
		 if(tag2Num!=tag3Num){
			 //小题数量之和与模版原始数量不想等不能保存模版编辑
			 $(liObj.children('.inpA').children('span')).html(tempErr+tag2Num).show() ;
			 return false;
		 }else{
			 $(liObj.children('.inpA').children('span')).hide() ;
		 }
	}
	
	return true;
}

