 
function showAnswer8(th){
	var dd=$(th).parent();
	
	dd.next().show();
	
	var an='';
	dd.prev().prev().find('input[type="radio"]:checked,input[type="checkbox"]:checked').each(function(){
		an+=String.fromCharCode(parseInt($(this).val())+65)+" ";
	});
	if($.browser.msie&&($.browser.version == "7.0")){
		$('dd').show();
		$('dd').find('.ft11:eq(0)').html(an)
	}
	dd.next().find('.ft11:eq(0)').html(an);
	var txt=dd.next().find('.mb20:eq(1)').find('span').text();
	//console.log($.trim(txt));
	//console.log($.trim(an));
	//console.log(dd.next().find('.ft11:eq(0)').html());
	var tmp=$(th).parent().next('dd').find('.mb20 span:eq(1)');
	if($.trim(txt)==$.trim(an)){
		//console.log("utopia test");
		//console.log(tmp.html());
		tmp.removeClass('icerror').addClass('icright');
	}else{
		tmp.removeClass('icright').addClass('icerror');
	}
}
function showAnswer2(th,sp){
	var dd=$(th).parent();
	dd.next().show();
	
	var an='';
	dd.prev().prev().find('input[type="text"]').each(function(){
		if(sp){
			an+=$.trim($(this).val())+sp;
		}else{
			an+=$.trim($(this).val())+" ";
		}
	});
	if(sp&&an.length>0){
		an=an.substring(0,an.length-sp.length);
	}
	if($.browser.msie&&($.browser.version == "7.0")){
		$('dd').show();
		$('dd').find('.ft11:eq(0)').html(an)
	}
	dd.next().find('.ft11:eq(0)').html(an);
	var txt=dd.next().find('.mb20:eq(1)').find('span').text();
	//console.log($.trim(txt));
	//console.log($.trim(an));
	//console.log(dd.next().find('.ft11:eq(0)').html());
	var tmp=$(th).parent().next('dd').find('.mb20 span:eq(1)');
	if($.trim(txt)==$.trim(an)){
		//console.log("utopia test");
		//console.log(tmp.html());
		tmp.removeClass('icerror').addClass('icright');
	}else{
		tmp.removeClass('icright').addClass('icerror');
	}
}
function showAnswer3(th,isSub){
	var dd=$(th).parent();
	dd.next().show();
	var an='';
	if(isSub){
		var ok_times=0;
		var error=0;
		var txt=dd.next().find('.mb20:eq(1)').find('span').text();
		var keys=txt.split(',');
		
		dd.prev().prev().find('textarea').each(function(){
			var txt=$(this).val();
			for(var i=0;i<keys.length;i++){
				if(txt.indexOf(keys[i])!=-1){
					ok_times=1;
					break;
				}
			}
			//console.log("gogo....");
			if(ok_times==0){
				error=1;
			}
			an+=$(this).val()+" ";
		});
		dd.next().find('.ft11:eq(0)').html(an);
		if($.browser.msie&&($.browser.version == "7.0")){ 
			$('dd').show();
			$('dd').find('.ft11:eq(0)').html(an)
		}
		var tmp=$(th).parent().next('dd').find('.mb20 span:eq(1)');
		if(error==0){
			tmp.removeClass('icerror').addClass('icright');
		}else{
			tmp.removeClass('icright').addClass('icerror');
		}
	}else{
		var my_ans=$(th).parent().next('dd').find('.mb20 span:eq(0)');
		dd.prev().prev().find('textarea').each(function(){
			an+=$(this).val()+"<br/>";	
		});
		my_ans.html(an);
	}
}
function showAnswer36(th){
		var dd=$(th).parent();
	dd.next().show();
	var an='';
	dd.prev().prev().find('input[type="hidden"]').each(function(){
		an+=String.fromCharCode(parseInt($(this).val())+65)+" ";
	});
	dd.next().find('.ft11:eq(0)').html(an);
	if($.browser.msie&&($.browser.version == "7.0")){
		$('dd').show();
		$('dd').find('.ft11:eq(0)').html(an)
	}
	var txt=dd.next().find('.mb20:eq(1)').find('span').text();
	//console.log($.trim(txt));
	//console.log($.trim(an));
	//console.log(dd.next().find('.ft11:eq(0)').html());
	var tmp=$(th).parent().next('dd').find('.mb20 span:eq(1)');
	if($.trim(txt)==$.trim(an)){
		//console.log("utopia test");
		//console.log(tmp.html());
		tmp.removeClass('icerror').addClass('icright');
	}else{
		tmp.removeClass('icright').addClass('icerror');
	}
}
/*
table 类型
*/
function tableOne(th,showForm){
	//显示答案和解析
	$(th).parent().next().removeClass("hide");
	if(showForm==1){
		var myAnswer='';
		var myAnswer2='';
		var rs=[];
		$(th).closest('.st').find('.dragto').each(function(){
			var indexNum=0;
			var txt=$(this).prev().html();
			myAnswer2+=" "+txt+" : "
			$(this).find('input').each(function(){
				myAnswer+=" "+String.fromCharCode(parseInt($(this).val())+65);
				myAnswer2+=String.fromCharCode(parseInt($(this).val())+65)+" ";
			});
		myAnswer+=";"
		});
		
		var oppClass=$(th).parent().next();//has oppClass
		var mb20=oppClass.find('.mb20');
		mb20.first().find('.ft11:eq(0)').text(myAnswer);
		
		var v=mb20.eq(1).find('.ft11:eq(0)').html();
			var span=mb20.first().find('span:eq(1)');
		if($.trim(v)==$.trim(myAnswer2)){
			span.removeClass('icerror').addClass('icright');
		}else{
			span.removeClass('icright').addClass('icerror');
		}
	}else if(showForm==2){
		var myAnswer='';
		var myAnswer2='';
		var rs=[];
		$(th).closest('.st').find('table:eq(0)').find('tr').each(function(index,el){
			if(index>0){
				$(this).find('td:eq(1)').each(function(index, el) {
					var checked=$(this).find('input[type="radio"]:checked');
					if(checked.size()>0){
						myAnswer+=" "+String.fromCharCode(parseInt(checked.val())+65);
					}else{
						myAnswer+=" ";
					}
				});
			}
		});
		
		var oppClass=$(th).parent().next();//has oppClass
		var mb20=oppClass.find('.mb20');
		myAnswer=$.trim(myAnswer);
		mb20.first().find('.ft11:eq(0)').text(myAnswer);

		var v=mb20.eq(1).find('.ft11:eq(0)').html();
			var span=mb20.first().find('span:eq(1)');
		if($.trim(v)==$.trim(myAnswer)){
			span.removeClass('icerror').addClass('icright');
		}else{
			span.removeClass('icright').addClass('icerror');
		}
	}
}
/**
 * 改错题
 */
function correctOne(th){
	//显示答案和解析
	$(th).parent().next().removeClass("hide");
	var myAnswer='';
	myAnswer = $(th).closest('.st').find('.dhl_gcz_ua').val();
	
	var oppClass=$(th).parent().next();//has oppClass
	var mb20=oppClass.find('.mb20');
	mb20.first().find('.ft11:eq(0)').text(myAnswer);
	
	var v=mb20.eq(1).find('.ft11:eq(0)').html();
		var span=mb20.first().find('span:eq(1)');
	if($.trim(v)==$.trim(myAnswer)){
		span.removeClass('icerror').addClass('icright');
	}else{
		span.removeClass('icright').addClass('icerror');
	}
}
/*
box
*/
function showAnswer21(th){
	$(th).parent().next().show();

	var myAnswer='';
	$(th).closest('.st').find('.tb1').each(function(index, el) {
		if($(this).css('display')=='none'){
			myAnswer=$(this).text();
		}
	});

 var next=$(th).parent().next();
 	next.find('.ft11:eq(0)').text(myAnswer);
 	var right=next.find('.ft11:eq(1)').text();
 	var span=next.find('.ft11:eq(0)').next();
 	if($.trim(right)==$.trim(myAnswer)){
 				span.removeClass('icerror').addClass('icright');
 			}else{
 				span.removeClass('icright').addClass('icerror');
 			}
}