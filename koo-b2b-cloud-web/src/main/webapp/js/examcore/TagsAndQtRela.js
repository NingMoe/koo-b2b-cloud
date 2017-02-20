jQuery(document).ready(function(){
	var bp = jQuery("#basePath").val();
	/** 获取选中的题目类型 */
	function onopt(){
		var ua = "";
		jQuery(".qt").each(function(index,ele){
			var ck = this.checked;
			var val = jQuery(ele).attr("value");
			if(true == ck){
				ua = ua + "," + val;
			}
		});
		return ua;
	}
	/** 题型回写处理 */
	function opt(qts){
		if(null != qts && "" != qts && "null" != qts){
			var qtArr = qts.split(",");
			jQuery(".qt").each(function(index,ele){
				jQuery(ele).attr("checked",false);
				for (var int = 0; int < qtArr.length; int++) {
					if(qtArr[int] == jQuery(ele).attr("value")){
						jQuery(ele).attr("checked","checked");
					}
				}
			});
		}else{
			jQuery(".qt").each(function(index,ele){
				jQuery(ele).attr("checked",false);
			});
		}
	}
	/** 题型回写 */
	function repeat(tag){
		var url = bp + "mc/tr";
		var data = {"tag":tag};
		jQuery.post(url,data,function(result){
			opt(result);
		});
	}
	/** 显示与隐藏 */
	jQuery(".sh").live("click",function(){
		jQuery("#qts").hide();
		opt(null);
		jQuery(".rela").each(function(index,ele){
			jQuery(ele).text("关联考试题型");
			jQuery(ele).removeClass("show");
		});
		jQuery(".can").each(function(index,ele){
			jQuery(ele).hide();
		});
		var val =jQuery(this).val();
		var id =jQuery(this).attr("id");
		var _id = id.substring(2);
		if(0 == val){
			jQuery("#sp" + _id).show();
			jQuery(this).text("隐藏");
			jQuery(this).val("1");
		}
		if(1 == val){
			jQuery("#sp" + _id).hide();
			jQuery(this).text("显示");
			jQuery(this).val("0");
		}
	});
	/** 展开关联信息 */
	jQuery(".rela").live("click",function(){
		var flag = jQuery(this).hasClass("show");
		var val = jQuery(this).attr("value");
		if(!flag){
			repeat(val);
			jQuery("#qts").show();
			jQuery("#can"+val).show();
			jQuery(this).text("确认关联");
			jQuery(this).addClass("show"); 
		}else{
			var qts = onopt();
			var url = bp + "mc/er";
			var data = {"tag":val, "qts":qts};
			jQuery.post(url,data,function(result){
				if ( true == result || "true" == result){
					alert("保存成功！");
					jQuery("#qts").hide();
					jQuery("#can"+val).hide();
					jQuery("#rela"+val).text("关联考试题型");
					jQuery("#rela"+val).removeClass("show");
				}else{
					alert("保存失败！");
				}
			});
		}
	});
	/** 取消操作 */
	jQuery(".can").live("click",function(){
		var val = jQuery(this).attr("value");
		jQuery("#qts").hide();
		jQuery("#can"+val).hide();
		jQuery("#rela"+val).text("关联考试题型");
		jQuery("#rela"+val).removeClass("show");
		opt(null);
	});
});