<%@page import="com.koolearn.exam.question.template.ChoiceBlank"%>
<%@page import="com.koolearn.exam.question.template.ClozeFillTemplate"%>
<%@page import="com.koolearn.exam.question.template.ChoiceTemplate"%>
<%@page import="com.koolearn.exam.question.template.EssayTemplate"%>
<%@page import="com.koolearn.exam.question.template.ChoiceBlankTemplate"%>
<%@page import="com.koolearn.exam.testProcess.dto.TestProcessDto"%>
<%@page import="com.koolearn.exam.question.template.FillBlankTemplate"%>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.question.entity.*"%>
<%@page import="com.koolearn.exam.question.dto.*"%>
<%@page import="com.koolearn.exam.essayquestion.dto.EssayQuestionDTO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.koolearn.exam.util.TestUtil"%>
<style>
.bq_word{width:100%;height:auto;}
.bq_word li,.bq_list ul li{width:105px;height:25px;line-height:25px;white-space:nowrap;padding:0px 10px;background:#F1F1F1;float:left;color:#333333;text-align:center;margin:0px 10px 15px 0px;}
.tk_article{width:100%;height:auto;position:relative;padding-top:20px;float:left;}
.bq_list{width:390px;height:auto;border:#55BD38 solid 1px;background:#fff;position:absolute;left:0px;top:0px;padding:5px 0px 0px 5px;display:none;z-Index:10000;}
.bq_list ul{width:100%;height:auto;}
.bq_list ul li{margin:0px 5px 5px 0px;cursor:pointer;}
.bq_list ul li.act{background:#cccccc;color:#fff;}
.bq_list ul li.hover{background:green;color:#fff;}
.bq_as{color:#55A93A;float:left; font-size:12px; width:100%;}
.bq_as .as_list{padding:0px 30px 20px 0px;}
.bq_as .as_list dt,.bq_as .as_list dd{padding-left:0px;}
.bq_as .bq_t{font-weight:bolder; padding-bottom:0px;}
</style>
<%
	ComplexQuestionDto dto = (ComplexQuestionDto)obj;
	int questionType = dto.getQuestionType();
	QuestionDto questionDto = dto.getQuestionDto();
	List<QuestionAttach> attachs = questionDto.getQuestionAttachs();
	
	//选择型完型
	if(questionType == Question.QUESTION_TYPE_CHOICE_FILL_BLANK){
		TestProcessDto process =(TestProcessDto)request.getAttribute("process"); 
	int index=(Integer)request.getAttribute("i");
	 out.println(FillBlankTemplate.instance(request, response).outTemplate(process, index));
	ComplexQuestionDto dto2=(ComplexQuestionDto)process.getPaperObjectList().get(index);
	for(int i=0,_size=dto2.getChoiceQuestionDtos().size();i<_size;i++){ 
	 out.println(ChoiceTemplate.instance(request, response).outTemplate(process, index+i+1));
	 }
	%>
		
		<%
	}
	//选择填空
	else if(questionType == Question.QUESTION_TYPE_CHOICE_BLANK){
	TestProcessDto process =(TestProcessDto)request.getAttribute("process"); 
	int index=(Integer)request.getAttribute("i");
	out.println(ChoiceBlankTemplate.instance(request, response).outTemplate(process, index));
	%>
	<%--
	<%
		List<ChoiceQuestionDto> choiceDtos = dto.getChoiceQuestionDtos();
	 %>
		<dl class="st">
		<dt class="nobold"><span class="num">
		<% if(request.getAttribute("question_index")!=null){
			 	out.print(request.getAttribute("question_index"));
			 	request.setAttribute("question_index",((Integer)request.getAttribute("question_index"))+1);
			 }else{
			 	out.print(1);
			 } %>
		</span>
		<%if(attachs != null && attachs.size()>0){
			for(QuestionAttach attach : attachs){
		%>
			<p class="mb10"><%=attach.getContent()%></p>
		<%
			}
		}%>	
		<p class="mb10"><%=dto.getComplexQuestion().getTopic()%></p>
		<p class="ft3">提示：<%=dto.getQuestionDto().getQuestion().getQuestionTip()%></p>
		</dt>
		<dd>
        <div class="fc">
	        <%for(int i=0;i<choiceDtos.size();i++){
				ChoiceQuestionDto choiceDto = choiceDtos.get(i);
				ChoiceQuestion choiceQuestion = choiceDto.getChoiceQuestion();
				List<ChoiceAnswer> choiceAnswers = choiceDto.getChoiceAnswers();
			%>
        	<dl class="bx3 fl">
        		<dt>(<%=i+1 %>)</dt>
        		 <%for(int j=0;j<choiceAnswers.size();j++){ 
        		 	ChoiceAnswer answer = choiceAnswers.get(j);
        		 %>
				<dd><%=answer.getDescription() %></dd>
				<%} %>
        	</dl>
        	<%} %>
        </div>
		</dd>
	</dl>
	 --%>
	<%}
	//选词填空
	else if(questionType == Question.QUESTION_TYPE_CHOICE_WORD){
		List<ChoiceQuestionDto> choiceDtos = dto.getChoiceQuestionDtos();
		List<ChoiceAnswer> choiceAnswers  = choiceDtos.get(0).getChoiceAnswers();
	%>
	<div class="tf_box">
	<div class="tf_main">
    	<p class="music_title"><%=dto.getQuestionDto().getQuestion().getQuestionTip()%></p>
    	<%if(attachs != null && attachs.size()>0){
			for(QuestionAttach attach : attachs){
	%>
	 <div class="xc_box">
		<div class="up"><%=attach.getContent()%></div> 	
	</div>
		<%
			}
		}%>
        <div class="xc_box">
        	  <div class="up">
              	  <%=dto.getComplexQuestion().getTopic()%></p>
              </div>
              <ul class="down">
               <%for(int j=0;j<choiceAnswers.size();j++){ 
             	  ChoiceAnswer answer = choiceAnswers.get(j);
        	   %>
              	  <li class="btn"><span><%=answer.getDescription() %></span></li>
                  <%} %>
              </ul>
        </div>
    </div>
    </div>
    <%}
	//用于交互练习题的不作为子题的选词填空
	else if(questionType == Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD){
		/* Du HongLin 2014-06-13 11:51 */
		TestProcessDto process =(TestProcessDto)request.getAttribute("process");
		out.println(ChoiceBlank.instance(request, response).outTemplate(process, 0));
		/* Du HongLin 2014-06-13 11:51 */
	}else if(questionType == Question.QUESTION_TYPE_CLOZE_FILL_BLANK){//填空型完型
	TestProcessDto process =(TestProcessDto)request.getAttribute("process"); 
	int _index=(Integer)request.getAttribute("i");
	 out.println(FillBlankTemplate.instance(request, response).outTemplate(process, _index));
	ComplexQuestionDto _dto2=(ComplexQuestionDto)process.getPaperObjectList().get(_index);	
	for(int i=0,_size=_dto2.getEssayQuestionDTOs().size();i<_size;i++){ 
	 out.println(EssayTemplate.instance(request, response).outTemplate(process, _index+i+1));
	 }		
	%>
	<%-- 
		<div class="pd1m">
			<%if(attachs != null && attachs.size()>0){
					for(QuestionAttach attach : attachs){
			%>
			<div class="pd1m">
				<div class="mb20 stp"><%=attach.getContent()%></div> 	
			</div>
			<%
				}
			}%>	
			<div class="mb20 stp"><%=dto.getComplexQuestion().getTopic()%></div> 	
		</div>
		<div class="mb20">
			<%List<EssayQuestionDTO> essayQuestionDTOs = dto.getEssayQuestionDTOs();
			if(essayQuestionDTOs != null && essayQuestionDTOs.size() >0){
			for(int i=0;i<essayQuestionDTOs.size();i++){
				EssayQuestionDTO essayDTO = essayQuestionDTOs.get(i);
			%>
			<dl class="st" ty="0">
			<dt><span class="num" ><%=i+1 %></span> </dt>
			<dd class="fc">
			 <span class="ft4 fl">填写答案：</span>
                  <span class="fl w2"><span class="tktb"><input type="text"  class="tkt" /></span></span>
			</dd>
			</dl>
			<%} 
			}%>
		</div>	  
	--%>
	<%}else{//复合听写
	TestProcessDto process =(TestProcessDto)request.getAttribute("process"); 
	int index=(Integer)request.getAttribute("i");
	 out.println(ClozeFillTemplate.instance(request, response).outTemplate(process, index));
	%>
	<%--
	<div class="mb20">
		<dl class="st" ty="0">
			<dt><span class="num">
			<% if(request.getAttribute("question_index")!=null){
			 	out.print(request.getAttribute("question_index"));
			 	request.setAttribute("question_index",((Integer)request.getAttribute("question_index"))+1);
			 }else{
			 	out.print(1);
			 } %>
			</span></dt>
		</dl>
	</div>
	<div class="pd1m">
		<%if(attachs != null && attachs.size()>0){
				for(QuestionAttach attach : attachs){
		%>
		<div class="pd1m">
			<div class="mb20 stp"><%=attach.getContent()%></div> 	
		</div>
		<%
			}
		}%>	
		<div class="mb20 stp"><%=dto.getComplexQuestion().getTopic()%></div> 	
	</div>
	<div class="mb20">
		<%List<EssayQuestionDTO> essayQuestionDTOs = dto.getEssayQuestionDTOs();
		%>
		<div class="fc">
			<span class="ft4 fl">填写答案：</span>
			<span  class="fl w2">
		<%
		if(essayQuestionDTOs != null && essayQuestionDTOs.size() >0){
		for(int i=0;i<essayQuestionDTOs.size();i++){
			EssayQuestionDTO essayDTO = essayQuestionDTOs.get(i);
		%>
		<span class="tktb">(<%=i+1%>)<input type="text" class="tkt" /></span>
		<%} 
		}%>
		</span>
		</div>
	</div>	  
	 --%>
	<%}%>
	<script>
$(function(){
	 //补全对话，填空。
	 var tk_time = null;
	 for(var i=0;i<$(".tk_article input.tkt").length;i++){
	 	$(".tk_article input.tkt").get(i).index = i;	
	 }
	 $(".tk_article input.tkt").hover(function(){
	 	  clearTimeout(tk_time);
	 	  var otk_box = $(this).parents(".tk_article").find(".bq_list"),
		  	  offset = $(this).position();
		  otk_box.show().css({
		  	"left":offset.left+"px",
			"top":offset.top+25+"px"
		  });
		  $(".bq_list").attr("index",this.index);
	 },function(){
	 	 tk_time = setTimeout(hide,500);
	 });
	 $(".tk_article .bq_list").hover(function(){
	 	clearTimeout(tk_time);
		$(this).show();
	 },function(){
	 	tk_time = setTimeout(hide,500);
	 });
	 //鼠标滑过弹框的每一个标签，有变化。
	 $(".tk_article .bq_list ul li").live("mouseover",function(){
		 if(this.btn==true){
		 	$(this).addClass("hover");
		 }
	 });
	 $(".tk_article .bq_list ul li").live("mouseout",function(){
		if(this.btn==true){
			$(this).removeClass("hover");
		}
	 });
	 //点击某一个标签，把标签的值赋给填空。
	 $(".tk_article .bq_list ul li").live("click",function(){
         var aLi = $(this).parent().find("li"),par_index = $(this).parents(".bq_list").attr("index");
	 	 if(this.btn==true && trim($(this).text())!="取消选择"){
		 	aLi.each(function(){
				if($(this).attr("index")==par_index){
					$(this).removeClass();
					$(this).attr("index","");
					this.btn = true;
				}
			});
			$(this).removeClass().addClass("act");
			$(".tk_article input.tkt").eq(par_index).prev().val($(this).index());
			$(".tk_article input.tkt").eq(par_index).val($(this).html());
			$(this).attr("index",par_index);
			this.btn=false;
		 }
		 if(trim($(this).text())=="取消选择"){
			 aLi.each(function(){
			 	if($(this).attr("index")==par_index){
					$(this).removeClass();
					$(".tk_article input.tkt").eq(par_index).prev().val("");
					$(".tk_article input.tkt").eq(par_index).val("");
					$(this).attr("index","");
					this.btn = true;
				}
			 });
		 }
	 });
	//给弹框的各个标签给定义开关。
	 $(".tk_article .bq_list ul li").each(function(){
			this.btn = true;
	 });
	 //连线题
	//左边的问题，点击只有一个选中状态。
	$(".lx_left li").live("click",function(){
		$(this).parent().find("li").removeClass();
		$(this).addClass("act");
	});
	//右边的答案，点击只有一个选中状态。
	$(".lx_right li").live("click",function(){
		$(this).parent().find("li").removeClass();
		$(this).addClass("act");
	});
	$(".lx_list li").each(function(){
		this.str = "";
	});
	$(".lx_list li").live("click",function(){
		if($(this).parent().index()==0){   //若是左边的,将右边的答案的选中状态的索引值赋给左边的属性里。
			var aLi = $(this).parent().next().find("li"),This = this,oposi = null;
		    aLi.each(function(){
				if($(this).attr("class")=="act"){
					This.str+=$(this).index();
					oposi = this; 
				}
			});
			if(this.str.length!=0 && this.str.search($(oposi).index())!=-1){   //若能查询到值，那么就开始划线。
			    var offset_L = $(this).offset(),offset_r = $(oposi).offset(),
					x1 = offset_L.left+$(this).width(),y1 = offset_L.top - $(this).height()/2,
				    x2 = offset_r.left,y2 = offset_r.top - $(oposi).height()/2;
			    //创建一个父级的div,来包含里面所有的小div。同时规定div的各种属性。
				var oPar_div = $("<div></div>");
				oPar_div.attr("class","line_"+$(this).index()+""+$(oposi).index());
				oPar_div.css({
					"position":"absolute",
					"left":x1+"px",
					"width":Math.abs(x2-x1)+"px",
					"height":Math.abs(y1-y2)+"px"
				});
				
				$(this).parents(".lx_list").append(oPar_div);
				if(y1==y2){
					oPar_div.css("height","1px");
				}
			    //确定创建父级盒子的定位。同时创建子级的位置以及定位。
				if(y1<y2){
					oPar_div.css("top",y2+"px");
					var disY = Math.abs((y2 - y1))/(x2-x1);
					for(var i=0;i<x2-x1;i++){
						var oDiv = $("<div></div>");
						oDiv.css({
							"position":"absolute",
							"left":i+"px",
							"top":i*disY+"px",
							"width":"1px",
							"height":"1px",
							"background":"red"
						});
						oPar_div.append(oDiv);
					}
				}else if(y1==y2 || y1>y2){
					oPar_div.css("top",y1+"px");
					var disY = Math.abs((y2 - y1))/(x2-x1);
					for(var i=0;i<x2-x1;i++){
						var oDiv = $("<div></div>");
						oDiv.css({
							"position":"absolute",
							"left":i+"px",
							"top":i*disY+"px",
							"width":"1px",
							"height":"1px",
							"background":"red"
						});
						oPar_div.append(oDiv);
					}
				}
			};
		}else{  //若是右边的
			
		}
	});
});
function hide(){
	$(".bq_list").hide();
}
function LTrim(str)
{
    var i;
    for(i=0;i<str.length;i++)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(i,str.length);
    return str;
}
function RTrim(str)
{
    var i;
    for(i=str.length-1;i>=0;i--)
    {
        if(str.charAt(i)!=" "&&str.charAt(i)!=" ")break;
    }
    str=str.substring(0,i+1);
    return str;
}
function trim(str)
{
    return LTrim(RTrim(str));
}
</script>