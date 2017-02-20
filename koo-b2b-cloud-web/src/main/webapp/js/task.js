function joinExam(examId,userId,thisA){
	//添加loading
    if($('body').find('#jp-loading').length == 0){
        var mess = '';
        mess += '<div class="p-loading" id="jp-loading">';
        mess += '<div id="jp-bg" class="p-bg"></div>';
        mess += '<div class="i-loading"><span class="i-loading_ico"></span></div>';
        mess += '</div>';
        $('body').append(mess);
    }else{
        $("#jp-loading").show();
    }
    //loading自动获取屏幕分辨率的高
    var popboxBg = $('#jp-bg');
    var bodyHeight = $(document.body).height();
    var windowHeight = window.screen.height;
    if(windowHeight>bodyHeight){
        popboxBg.css('height',windowHeight);
    }else{
        popboxBg.css('height',bodyHeight);
    }

	var falg=false;
	$.ajax({
	          type: "POST",
	          contentType: "application/x-www-form-urlencoded; charset=utf-8", 
	          url:  '/student/pc/joinExam' ,
	          data : {  "examId" : examId},
	          async :false,
	          success: function(data)  { falg=data;  },
	          dataType:"json"
    });

     if(falg){
  	    window.location="/student/pc/taskPortal?examId="+examId ;
  	  }else{
  		  //进入考试按钮置灰
  		  //$(thisA).removeClass("ass bg").addClass("ass bg2").removeAttr("href").removeAttr("onclick");
  	  }
}