/**
 * 考试过程的公共JS 
 */
// =========================================================================================================================
String.prototype.trim = function() {return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');}
// =========================================================================================================================
var wn = {"A":1,"B":2,"C":3,"D":4,"E":5,"F":6,"G":7,"H":8,"I":9,"J":10,"K":11,"L":12,"M":13,"N":14,"O":15,"P":16,"Q":17,"R":18,"S":19,"T":20,"U":21,"V":22,"W":23,"X":24,"Y":25,"Z":26};
var _mm, _ss, _tt, _timer, tmm, tss;
/**
 * 计时器输出
 */
function timerPrint(){
	var _sss = "";
	if (_ss < 10) {
		_sss = "0" + _ss;
	} else {
		_sss = _sss + _ss;
	}
	jQuery("#timer").text(_mm + " : " + _sss);
}
/**
 * 正计时器
 */
function up() {
	timerPrint();
	if (_ss == 59) {
		_mm = _mm + 1;
		_ss = 0;
	}
	if (_mm == tmm && _ss == tss) {
		clearInterval(_timer);
		jQuery("#timer").text(_mm + " : 00");
		submit();
		jQuery("#jsalert").show();
		return ; 
	}
	_ss = _ss + 1;
}
/**
 * 倒计时器
 */
function down(){
	timerPrint();
	if (_ss == 0) {
		_mm = _mm - 1;
		_ss = 59;
	}
	if (_mm < 0) {
		clearInterval(_timer);
		submit();
		jQuery("#jsalert").show();
		return ;
	}
	_ss = _ss - 1;
}
/**
 * 计时器
 * tt ==》 tatolTime 总时间
 * et ==》 examType  考试类型
 */
function timer(tt, et) {
	_tt = tt;
	if (1 == et) {
		_mm = _tt - 1;
		_ss = 59;
		_timer = setInterval("down()", 1000);
	} else if (2 == et) {
		_mm = 0;
		_ss = 1;
		_timer = setInterval("up()", 1000);
	} else if (3 == et) {
		_mm = 0;
		_ss = 1;
		_timer = setInterval("up()", 1000);
	} else {
		alert("恶意代码攻击！");
	}
}
/**
 * 计时器2
 * @param mm 拥有分钟时长
 * @param ss 拥有不足一分钟的秒时长
 * @param et 考试类型
 */
function timer2(mm, ss, et){
	tmm = mm;
	tss = ss;
	if (1 == et) {
		_mm = tmm;
		_ss = tss;
		_timer = setInterval("down()", 1000);
	} else if (2 == et) {
		_mm = 0;
		_ss = 1;
		_timer = setInterval("up()", 1000);
	} else if (3 == et) {
		_mm = 0;
		_ss = 1;
		_timer = setInterval("up()", 1000);
	} else {
		alert("恶意代码攻击！");
	}
}


//=========================================================================================================================

/*
 * ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓以下为基础操作↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
 */
//获取本地域
function getLS() {
	return window.localStorage;
}
//获取本地域中值
function LSGET(key) {
	var _result = getLS().getItem(key);
	if (undefined == _result) {
		_result = null;
	}
	return _result;
}
//设置本地域中值
function LSSET(key, value) {
	getLS().setItem(key, value);
}
//删除本地域中值
function LSREMOVE(key) {
	getLS().removeItem(key);
}
//清除本地域中值
function LSCLS() {
	getLS().clear();
}
//将JSON转成String
function tostr(value) {
	return JSON.stringify(value);
}
//将String转成JSON
function tojson(value) {
	return JSON.parse(value);
}
/*
 * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑以上为基础操作↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 */

/**
 * 将考试结构放到LocalStorage中
 */
function putStructureLS(key, ur){ // ur ==> User Result
	if(getLS()){
		if (!LSGET(key)) {
			LSSET(key, ur);
		}
	}else{
		alert("您的浏览器版本过低，部分内容不支持，请升级您的浏览器！");
	}
}

/**
 * 将考试结构从LocalStorage中取出来
 */
function takeStructureLS(key){
	return tojson(LSGET(key));
}
/**
 * 提交用户答案
 */
function submit () {
	var _bp = jQuery("#hidBasePath").val();
	var _rid = jQuery("#hidResultId").val();
	var _ur = jQuery("#hidUrKey").val();
	var _su = _bp + "student/pc/submit_2_" + _rid;
	if(navigator.userAgent.indexOf("MSIE")>0){
		_su = "/student/pc/submit_2_" + _rid;
	}
	var _da = {"urs" : tostr(takeStructureLS(_ur))};
	jQuery.post(_su, _da, function (result) {
		if (true == result["flag"]) {
			var _et = jQuery("#subjectId").val();
			var _target = _bp + "/student/pc/index?subjectId=" + _et;
			if(navigator.userAgent.indexOf("MSIE")>0){
				_target = "/student/pc/index?subjectId=" + _et;
			}
			LSREMOVE(_ur);
			unLock();
			refreshEnabled(true);
			backEnabled(true);
			forwardEnabled(true);
            var handExam=$("#jp-handexam").val();
            var urlType=$("#jp-urlType").val();
            var classRoomId=$("#jp-classRoomId").val();
            var resultId = jQuery("#hidResultId").val()
            if(urlType){
                //课堂作业
                window.location.href="/student/pc/reviewPage?resultId="+resultId+"&urlType=1&classRoomId="+classRoomId;
            }else{
                if('1'==handExam||'2'==handExam){
                    //组题自测，弹层显示自测结果
                    $.ajax({
                        url: '/student/test/findDetailByResult?resultId='+resultId,
                        type : 'POST',
                        async:false,
                        dataType:'JSON',
                        success:function(data) {
                            window._handexamVal(data.correctRate,data.correctNum,data.errorNum,'/student/pc/reviewPage?resultId='+resultId);
                        }
                    });

                }else{
                    window.open(_target, "_self");
                }
//            else  if('2'==handExam){
//                    //错题本复习
//                    window.open('/student/pc/reviewPage?resultId='+resultId, "_self");
//                }
            }
		} else {
			alert( "交卷失败，请重新交卷！");
		}
	},"json");
}
/**
 * 每10分钟自动提交用户答案
 */
function submit10 () {
	var _bp = jQuery("#hidBasePath").val();
	var _rid = jQuery("#hidResultId").val();
	var _ur = jQuery("#hidUrKey").val();
	var _su = _bp + "student/pc/submit_1_" + _rid;
	if(navigator.userAgent.indexOf("MSIE")>0){
		_su = "/student/pc/submit_1_" + _rid;
	}
	var _da = {"urs" : tostr(takeStructureLS(_ur))};
	jQuery.post(_su, _da, function (result) {
		// 不执行任何操作
	},"json");
}
/** 拖拽矩阵时钟执行函数 */
function tzjz(){// 绑定拖拽矩阵题答案操作
	try {
		jQuery(".dhl_tzjz").each(
				function(uindex, uele) {
					try {
						var _id = jQuery(uele).attr("id");
						var _qid = _id.split("_")[1];
						var _result = _id;
						jQuery(":input[name='" + _qid + "']").each(
								function(index, ele) {
									var _val = ele.value;
									if (undefined == _val) {
									} else {
										_result = _result + "@@" + _val;
									}
								});
						if (_result != _id) {
							var _ur = jQuery("#hidUrKey").val();
							var _temp = takeStructureLS(_ur);
							_temp[_id] = _result;
							LSSET(_ur, tostr(_temp));
						}
					} catch (e) {
						// TODO: handle exception
					}
				});
	} catch (e) {
		// TODO: handle exception
	}
}
/** 改错题时钟执行函数 */
function gcz(){
	try {
		jQuery(".dhl_gcz").each(function(uindex, uele){ // 绑定改错题答案操作
			var _ua = jQuery(uele).children(".dhl_gcz_ua").val();
			if("" == _ua){
			}else{
				var _id =  jQuery(uele).children(".dhl_gcz_ua").attr("id");
				var _uaTemp = _ua.substring(0, 4);
				if("107_" == _uaTemp){
					var _uaArr = _ua.split("_");
					_ua = "";
					for (var int = 2; int < _uaArr.length; int++) {
						_ua = _ua + _uaArr[int];
					}
				}
				
				var _ur = jQuery("#hidUrKey").val();
				var _temp = takeStructureLS(_ur);
				_temp[_id] = _id + "_" +  _ua;
				LSSET(_ur, tostr(_temp));
			}
		});
	} catch (e) {
		// TODO: handle exception
	}
}

function repeat(){ // 把用户答案显示到页面上
	var _ur = jQuery("#hidUrKey").val();
	var _temp = takeStructureLS(_ur);
	for (var t in _temp) {
		var _qt = t.split("_")[0];
		if ( 6 == _qt) { // 如果是普通单选题
			if(jQuery("#"+_temp[t]).hasClass("dhl_xztk")){ // 如果是选择填空
				jQuery("#"+_temp[t]).addClass("sd");
			}else{
				jQuery("#"+_temp[t]).attr("checked","checked");
			}
		}
		if (1 == _qt) { // 如果是多选题
			try{
				var _uas = _temp[t];
				if ("" != _uas&&_uas!=null) {
					var _uasa = (((_uas.split("_"))[2]).substring(2)).split("@@");
					for (var int = 0; int < _uasa.length; int++) {
						jQuery("#"+ t +"_"+_uasa[int]).attr("checked","checked");
					}
				}
			}catch(e){
				
			}
		}
		if(2 == _qt || 16 == _qt){ // 如果是填空题、计算填空题
			var ua = _temp[t];
			if ("" != ua&&ua!=null) {
				var _uaArr = ua.split("@@");
				for (var int2 = 0; int2 < _uaArr.length; int2++) {
					var uaArr = _uaArr[int2].split("_");
					jQuery("#"+uaArr[0]+"_"+uaArr[1]+"_"+uaArr[2]).val(uaArr[3]);
				}
			}
		}
		if(3 == _qt){ // 如果是简答题
			var ua = _temp[t];
			jQuery("#"+t).val(ua);
		}
		if(4 == _qt){ // 如果是写作题
			var ua = _temp[t];
			jQuery("#"+t).val(ua);
		}
		if(21 == _qt){ // 如果是方框题
			jQuery("#"+_temp[t].split("_")[0]+ "_" +_temp[t].split("_")[1]).val(_temp[t].split("_")[2]); // 给hidden赋值
			// 页面展示==》待完成
		}
		if(13 == _qt){ // 如果是排序题
			// 页面展示==》待完成
		}
		if(18 == _qt){ // 如果是听力题
			jQuery("#"+t.split("_")[1]).val(_temp[t]);
		}
		if(107 == _qt){ // 如果是改错题
			var ua = _temp[t];
			jQuery("#"+t).val(ua);
			// 页面展示==》待完成
		}
	}
}

/**
 * 请求本页面，使session保持
 */
function keepPage(){
	var loc = window.location;
	jQuery.post(loc);
}
/** 浏览器客户端锁定操作   */
function lock() {
    if (window.browser == null || jQuery("#hidExamType").val() != 1) {
        return;
    }
    window.browser.locking();
}
/** 浏览器客户端解锁操作   */
function unLock() {
    if (window.browser == null) {
        return;
    }
    window.browser.unLocking();
}
/** 浏览器客户端刷新操作是否可用   */
function refreshEnabled(enable) { 
	if (window.browser == null) { 
		return; 
	} 
	window.browser.refreshEnabled = enable; 
} 
/** 浏览器客户端后退操作是否可用   */
function backEnabled(enable) { 
	if (window.browser == null) { 
		return; 
	} 
	window.browser.backEnabled = enable; 
} 
/** 浏览器客户端前进操作是否可用   */
function forwardEnabled(enable) { 
	if (window.browser == null) { 
		return; 
	} 
	window.browser.forwardEnabled = enable; 
}
jQuery(document).ready(function () {
	// -----------------------
	try {
		lock();  // 客户端锁定
		
		refreshEnabled(false); // 客户端刷新禁用
		backEnabled(false); // 客户端后退禁用
		forwardEnabled(false); // 客户端前进禁用
	} catch (e) {
		// TODO: handle exception
	}
	// -----------------------
	//timer2(jQuery("#hidMM").val(), jQuery("#hidSS").val(), jQuery("#hidExamType").val()); // 计时器
	// -----------------------
	putStructureLS(jQuery("#hidUrKey").val(), jQuery("#hidUserResult").val()); // 向LocalStorage中放置用户答案结构
	// -----------------------

	// -----------------------
	jQuery(".dhl_dx").live("click", function () { // 绑定单选题型选择答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _idArr = _id.split("_");
		var _key = _idArr[0]+"_"+_idArr[1];
		var _temp = takeStructureLS(_ur);
		_temp[_key] = _id;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	jQuery(".dhl_ddx").live("click", function () { // 绑定多选题型选择答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _ck = this.checked;
		var _idArr = _id.split("_");
		var _key = _idArr[0]+"_"+_idArr[1];
		var _temp = takeStructureLS(_ur);
		if (_ck == true) {
			if (_temp[_key] == ""||_temp[_key] == "null"||_temp[_key] == null) {
				_temp[_key] = _key + "_@@" + _idArr[2];
			} else {
				_temp[_key] = _temp[_key] + "@@" + _idArr[2];
			}
		} else {
			var _old = "@@" + _idArr[2];
			_temp[_key] = _temp[_key].replace(_old, "").replace(/@@@@/g, "@@");
			if ("@@" == _temp[_key].substring(0, 2)) {
				_temp[_key] = _temp[_key].substring(2);
			}
			if ("@@" == _temp[_key].substring(_temp[_key].length - 2)) {
				_temp[_key] = _temp[_key].substring(0, _temp[_key].length - 2);
			}
		}
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	jQuery(".dhl_fill").live("blur", function(){ // 绑定填空题答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _idArr = _id.split("_");
		var _key = _idArr[0]+"_"+_idArr[1];
		var _temp = takeStructureLS(_ur);
		var ua = _temp[_key];
		if ("" != this.value.trim()) {
			//console.info(("null" == ua)+"   "+(null == ua));
			if ("" == ua||null == ua||"null" == ua) {
				_temp[_key] = _id + "_" + this.value.trim();
			} else {
				var uaArr = ua.split("@@");
				var flag = false;
				var seq = -1;
				for (var int = 0; int < uaArr.length; int++) {
					var t = uaArr[int].split("_")[2];
					if (_idArr[2] == t) {
						flag = true;
						seq = int;
						break;
					}
				}
				if (flag) {
					uaArr[seq] = _id + "_" + this.value.trim();
					var tt = "";
					for (var int2 = 0; int2 < uaArr.length; int2++) {
						if (tt == "") {
							tt = uaArr[int2];
						} else {
							tt = tt + "@@" + uaArr[int2];
						}
					}
					_temp[_key] = tt;
				} else {
					_temp[_key] = _temp[_key] + "@@" + _id + "_" + this.value.trim();
				}
				_temp[_key] = _temp[_key].replace(/@@@@/g, "@@");
				if ("@@" == _temp[_key].substring(0, 2)) {
					_temp[_key] = _temp[_key].substring(2);
				}
				if ("@@" == _temp[_key].substring(_temp[_key].length - 2)) {
					_temp[_key] = _temp[_key].substring(0,
							_temp[_key].length - 2);
				}
			}
			LSSET(_ur, tostr(_temp));
		}else{
			// 用户答案为空
		}
	});
	// -----------------------
	jQuery(".dhl_jd").live("blur", function(){ // 绑定简答题答案操作 (换成富文本，方法在ueditorJs.jsp)
		var _ur = jQuery("#hidUrKey").val();//获取取消localStroge key
		var _id = this.id;
		var _v = !this.value?"":this.value.trim();
		var _temp = takeStructureLS(_ur);
		_temp[_id] = _v;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	jQuery(".dhl_xz").live("blur", function(){ // 绑定写作题答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _v = this.value.trim();
		var _temp = takeStructureLS(_ur);
		_temp[_id] = _v;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	jQuery(".dhl_fk").live("mouseout", function(){ // 绑定方框题答案操作
		var _qid = jQuery(this).attr("questionid");
		var _fqid = "21_" + _qid;
		var _val = jQuery("#" + _fqid).val();
		if("" != _val){
			var _ur = jQuery("#hidUrKey").val();
			var _temp = takeStructureLS(_ur);
			_temp[_fqid] = _fqid + "_" + _val;
			LSSET(_ur, tostr(_temp));
		}
	});
	// -----------------------
	jQuery(".dhl_px").live("mouseout", function(){ // 绑定拖拽排序题答案操作
		var _ua = "";
		var _ths = jQuery(this); 
		var _ur = jQuery("#hidUrKey").val();
		var _id = _ths.attr("id");
		_ths.children("li").each(function(){
			_ua = _ua + "@@" + this.id;
		});
		var _temp = takeStructureLS(_ur);
		_temp[_id] = _id + "_" + _ua;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
//	function handlerSpoken(id, ua){ // 绑定口语题答案操作
//		alert("ID==>"+id+"【】UA==>"+ua);
//		var _ur = jQuery("#hidUrKey").val();
//		var _temp = takeStructureLS(_ur);
//		_temp[18+"_"+id] = ua;
//		LSSET(_ur, tostr(_temp));
//	}
	// -----------------------
	jQuery(".dhl_gcz").live("mouseout", function(){ // 绑定改错题答案操作
		var _ua = jQuery(this).children(".dhl_gcz_ua").val();
		if("" == _ua){
		}else{
			var _id =  jQuery(this).children(".dhl_gcz_ua").attr("id");
			var _uaTemp = _ua.substring(0, 4);
			if("107_" == _uaTemp){
				var _uaArr = _ua.split("_");
				_ua = "";
				for (var int = 2; int < _uaArr.length; int++) {
					_ua = _ua + _uaArr[int];
				}
			}
			
			var _ur = jQuery("#hidUrKey").val();
			var _temp = takeStructureLS(_ur);
			_temp[_id] = _id + "_" +  _ua;
			LSSET(_ur, tostr(_temp));
		}
	});
	jQuery(".dhl_gcz").live("mousemove", function(){ // 绑定改错题答案操作
		var _ua = jQuery(this).children(".dhl_gcz_ua").val();
		if("" == _ua){
		}else{
			var _id =  jQuery(this).children(".dhl_gcz_ua").attr("id");
			var _uaTemp = _ua.substring(0, 4);
			if("107_" == _uaTemp){
				var _uaArr = _ua.split("_");
				_ua = "";
				for (var int = 2; int < _uaArr.length; int++) {
					_ua = _ua + _uaArr[int];
				}
			}
			
			var _ur = jQuery("#hidUrKey").val();
			var _temp = takeStructureLS(_ur);
			_temp[_id] = _id + "_" +  _ua;
			LSSET(_ur, tostr(_temp));
		}
	});
	// -----------------------
	jQuery(".dhl_bgjz").live("click", function () { // 绑定表格矩阵题型选择答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _idArr = _id.split("_");
		var _key = _idArr[0]+"_"+_idArr[1];
		var _temp = takeStructureLS(_ur);
		_temp[_key] = _id;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	jQuery(".dhl_tzjz").live("mouseout", function(){ // 绑定拖拽矩阵题答案操作
		try {
			var _id = jQuery(this).attr("id");
			var _qid = _id.split("_")[1];
			var _result = _id;
			jQuery(":input[name='" + _qid + "']").each(function(index, ele) {
				var _val = ele.value;
				if (undefined == _val) {
				} else {
					_result = _result + "@@" + _val;
				}
			});
			if (_result != _id) {
				var _ur = jQuery("#hidUrKey").val();
				var _temp = takeStructureLS(_ur);
				_temp[_id] = _result;
				LSSET(_ur, tostr(_temp));
			}
		} catch (e) {
			// TODO: handle exception
		}
	});
//	jQuery(".dhl_tzjz").live("mouseleave", function(){
//		try {
//			var _id = jQuery(this).attr("id");
//			var _qid = _id.split("_")[1];
//			var _result = _id;
//			jQuery(":input[name='" + _qid + "']").each(function(index, ele) {
//				var _val = ele.value;
//				if (undefined == _val) {
//				} else {
//					_result = _result + "@@" + _val;
//				}
//			});
//			if (_result != _id) {
//				var _ur = jQuery("#hidUrKey").val();
//				var _temp = takeStructureLS(_ur);
//				_temp[_id] = _result;
//				LSSET(_ur, tostr(_temp));
//			}
//		} catch (e) {
//			// TODO: handle exception
//		}
//	});
//	jQuery(".dhl_tzjz").live("mousemove", function(){
//		try {
//			var _id = jQuery(this).attr("id");
//			var _qid = _id.split("_")[1];
//			var _result = _id;
//			jQuery(":input[name='" + _qid + "']").each(function(index, ele) {
//				var _val = ele.value;
//				if (undefined == _val) {
//				} else {
//					_result = _result + "@@" + _val;
//				}
//			});
//			if (_result != _id) {
//				var _ur = jQuery("#hidUrKey").val();
//				var _temp = takeStructureLS(_ur);
//				_temp[_id] = _result;
//				LSSET(_ur, tostr(_temp));
//			}
//		} catch (e) {
//			// TODO: handle exception
//		}
//	});
//	jQuery(".dhl_tzjz").live("mouseover", function(){
//		try {
//			var _id = jQuery(this).attr("id");
//			var _qid = _id.split("_")[1];
//			var _result = _id;
//			jQuery(":input[name='" + _qid + "']").each(function(index, ele) {
//				var _val = ele.value;
//				if (undefined == _val) {
//				} else {
//					_result = _result + "@@" + _val;
//				}
//			});
//			if (_result != _id) {
//				var _ur = jQuery("#hidUrKey").val();
//				var _temp = takeStructureLS(_ur);
//				_temp[_id] = _result;
//				LSSET(_ur, tostr(_temp));
//			}
//		} catch (e) {
//			// TODO: handle exception
//		}
//	});
//	jQuery(".dhl_tzjz").live("mouseenter", function(){
//		try {
//			var _id = jQuery(this).attr("id");
//			var _qid = _id.split("_")[1];
//			var _result = _id;
//			jQuery(":input[name='" + _qid + "']").each(function(index, ele) {
//				var _val = ele.value;
//				if (undefined == _val) {
//				} else {
//					_result = _result + "@@" + _val;
//				}
//			});
//			if (_result != _id) {
//				var _ur = jQuery("#hidUrKey").val();
//				var _temp = takeStructureLS(_ur);
//				_temp[_id] = _result;
//				LSSET(_ur, tostr(_temp));
//			}
//		} catch (e) {
//			// TODO: handle exception
//		}
//	});
//	

	// -----------------------
	jQuery(".dhl_xztk").live("click", function () { // 绑定选择填空题型选择答案操作
		var _ur = jQuery("#hidUrKey").val();
		var _id = this.id;
		var _idArr = _id.split("_");
		var _key = _idArr[0]+"_"+_idArr[1];
		var _temp = takeStructureLS(_ur);
		_temp[_key] = _id;
		LSSET(_ur, tostr(_temp));
	});
	// -----------------------
	repeat(); // 答案回写页面
	
	setInterval("submit10()", 600000); // 10分钟自动保存定时器
	
	setInterval("tzjz()", 50);
	setInterval("gcz()", 50);
});















