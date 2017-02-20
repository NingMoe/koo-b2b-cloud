<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.kootest.examcore.question.entity.*"%>
<%@page import="com.koolearn.kootest.examcore.question.dto.*"%>
<%@page import="com.koolearn.kootest.examcore.util.TestUtil"%>
<%@page import="java.util.*"%>
<%
			  ChoiceQuestionDto dto=(ChoiceQuestionDto)obj;
			  
			  Integer questionType=Integer.parseInt(String.valueOf(questionType_));
			  List<ChoiceAnswer> choiceAnswer=dto.getChoiceAnswers();
			  List<QuestionAttach> attaches=dto.getQuestionDto().getQuestionAttachs();
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
			String imgPath = basePath + "/images";
			  
%>

<dl class="st" ty="0" checkbox="true">
<div class="tf_box">
    <div class="dianju_dx"> <!--参考答案-->
         <div class="dx_l">
         
         	<%if(dto.getQuestionDto().getQuestion().getQuestionTip()!=null && !"".equals(dto.getQuestionDto().getQuestion().getQuestionTip())){ %>
	    		<div class="dx_t tf_mt"><%= dto.getQuestionDto().getQuestion().getQuestionTip()%></div>
	    	<%}
         		if(attaches != null && attaches.size()>0){
					for(QuestionAttach attach : attaches){%>
						<div class="dx_main tf_mt"><%=attach.getContent()%></div>
					<%}
				}
				%>
         			
              <div class="dx_main tf_mt">${dto.choiceQuestion.topic}</div>
              <div class="dx_answer">
              	  <strong>参考答案:</strong>
                  <%
                  	for(int i=0;i<choiceAnswer.size();i++) {%>
                  <%
                  		if(choiceAnswer.get(i).getIsright() == 1) {
                  %>			
                  			<p><%=choiceAnswer.get(i).getDescription()%></p>	
                  <%	}		
                  	}
                  %>
              </div>
         </div>
         <!--点句多选-->
         <div class="dx_r">
         	  <p class="dx_main_t">Opportunists and Competitors</p>
              <div class="dx_dt">
              	   <div class="dx_nr">
                   		<div class="dx_dluo">
                   		
                        	<%	String topic = dto.getQuestionDto().getQuestion().getTopicExt();
                        		if(topic!=null&&!topic.equals("")){
	                        		topic = topic.replaceAll("\\[","");
	                        		topic = topic.replaceAll("\\]","");
	                        		out.println(topic);
                        		}
                        	%>
                        	
                        </div>
                   </div>
                   <div class="dx_scroll"><p></p></div>
              </div>
         </div>
         <div class="clear"></div>
    </div>
</div>
		</dl>
		
		<script type="text/javascript">
    	//<![CDATA[
    
    	//]]>
    	function scroll_drag(obj,ele,opar){
			//滚动条的高度比例应当由内容的实际高度的比例来算。
			obj.css("height",100+"px").show();
			//拖拽滚动条，内容随之滚动。（若查询到该元素，那么执行函数，若查询不到该元素，不执行函数。）
			obj.size() && (obj.get(0).onmousedown = function(ev){
				var ev = ev || event,
					idisy = ev.clientY - $(this).position().top,
					iHeight = $(this).parent().height()-$(this).height();
				if(this.setCapture){
					this.setCapture();
				}
				this.onmousemove = function(ev){
				    var ev = ev || event,
				    	iT = ev.clientY - idisy;
					//若内容的高度小于或者等于它的父级的高度时候，那么滚动条是不应该滚动的。
					if(ele.height()<opar.height() || ele.height()==opar.height()){
						$(this).css("top","0px");
						return false;
					}else{
						$(this).css("top",iT+"px");
					}
					//滚动条的上下拖拽范围。
					if(parseInt($(this).css("top"))<0 || parseInt($(this).css("top"))==0){
						$(this).css("top","0px");	
					}else if(parseInt(obj.css("top"))==iHeight || parseInt(obj.css("top"))>iHeight){
						$(this).css("top",iHeight+"px");			
					}
					//滚动条一侧的内容相应的滚动。
					var per = parseInt(obj.css("top"))/iHeight, itop = (opar.height()-ele.height())*per;
					ele.css("top",itop+"px");
				};
				this.onmouseup = function(){
					if(this.releaseCapture){
						this.releaseCapture();	
					}
					this.onmousemove = null;
					this.onmouseup = null;
				};
				return false;
			});
		}
		</script>	 