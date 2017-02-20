<#assign questionDto=dto.questionDto>
<#assign question=questionDto.question>
<#if questionDto.questionAttachs??>
    <#assign attachs=questionDto.questionAttachs>
</#if>
<dl class="st tm_zt_${question.teId?c}" ty="1" teId='${question.teId?c}' qq="shortQuestion.ftl" questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  >
<dt class="nobold js_score"> 
	<#if paperItemDto.questionNo!="">
		<a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
    	<span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span>
    </#if>
	<#if attachs?? && (attachs?size>0)>
	    <#list attachs as attach>${attach.content}</#list>
	</#if>
	<#if dto.shortQuestion.topic??>${dto.shortQuestion.topic}【本题：<span class="jp-score-view jp-subscore-view">${paperItemDto.score!'0'}</span>分】</#if>
	<#if dto.shortQuestion.answer?? && (dto.shortQuestion.answer?length>0)>
	    <p class="ft3">${dto.shortQuestion.answer}</p>
	</#if>
</dt>
<#if paperItemDto.viewType==1>
    <#assign rightStr="">
  <#if  (question.teId>0)>
      <br>
  <dd class="jp-answer-min">
      <div class="p-btns p-little-style">
              <#if dto.shortQuestion.scorestandad??>
                  <div class=" ">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad}</span></div>
              </#if>
              <#if dto.shortQuestion.answerreference??>
                  <div>参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference}</span></div>
              </#if>
              <#if question.explan?? && (question.explan?length>0)><div class="" >试题解析：${question.explan!}</div></#if>
          </div>
   </dd>
  <#else>
      <br>
      <dd class="oppClass jp-answer p-btns">
          <#if dto.shortQuestion.scorestandad??>
              <div class=" ">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad}</span></div>
          </#if>
          <#if dto.shortQuestion.answerreference??>
              <div>参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference}</span></div>
          </#if>
          <#if question.explan?? && (question.explan?length>0)><div class="" >试题解析：${question.explan!}</div></#if>
      </dd>
  </#if>

<#elseif paperItemDto.viewType==2>
    <#assign rightStr="">
    <br>
    <#if dto.shortQuestion.keyWordList??>
        <#assign keyWordList=dto.shortQuestion.keyWordList>
        <#if keyWordList??>
            <#list keyWordList as str>
                <#if (rightStr?length>0)>
                    <#assign rightStr=rightStr+str>
                <#else>
                    <#assign rightStr=rightStr+","+str>
                </#if>
            </#list>
        </#if>
    </#if>
    <#if (question.teId >0)>
    	<#--作为小题-->
	    <dd class="jp-answer-min">
	    	<div class="p-btns p-little-style">
	    		<div class=" hideDiv">您的答案：<br>
		        	<span class="ft11"><#if paperItemDto.userAnswer??>${paperItemDto.userAnswer!}</#if></span>
			       	<#if paperItemDto.isCorrect==1>
		                <span class="icright"></span>
		            <#elseif paperItemDto.isCorrect!=-1>
		                <span class="icerror"></span>
		            </#if>
	            </div>
		        <#if dto.subjectived >
		            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
		        <#else>
		            <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
		        </#if>
	            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
	         	<#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
                	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
                </#if>
		        <div class="js_lastdl" >试题解析：${question.explan!}</div>
	    	</div>
	    </dd>
	<#else>
	    <#--作为大题-->
        <dd class="oppClass p-btns jp-answer">
	    	<div class=" hideDiv">您的答案：<br>
	        	<span class="ft11"><#if paperItemDto.userAnswer??>${paperItemDto.userAnswer!}</#if></span>
			    <#if paperItemDto.isCorrect==1>
	                <span class="icright"></span>
	            <#elseif paperItemDto.isCorrect!=-1>
	                <span class="icerror"></span>
	            </#if>
            </div>
	        <#if dto.subjectived >
	            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
	        <#else>
	            <#--<#if dto.shortQuestion.scorestandad??>-->
	                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
	            <#--</#if>-->
	        </#if>
	        <#--<#if dto.shortQuestion.answerreference??>-->
	            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
	        <#--</#if>-->
	         <#-- 未判题不显示用户得分 -->
	        <#if paperItemDto.isCorrect!=-1>
            	<#import "../userScore.ftl" as userScore_ftl>
            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
            </#if>
	        <#--<#if question.explan?? && (question.explan?length>0)>-->
	        <div class="js_lastdl" >试题解析：${question.explan!}</div>
	        <#--</#if>-->
	    </dd>
    </#if>
    
    
    
    
    
<#elseif paperItemDto.viewType==3>
    <#assign rightStr="">
    <br>
    <#if dto.shortQuestion.keyWordList??>
        <#assign keyWordList=dto.shortQuestion.keyWordList>
        <#if keyWordList??>
            <#list keyWordList as str>
                <#if (rightStr?length>0)>
                    <#assign rightStr=rightStr+str>
                <#else>
                    <#assign rightStr=rightStr+","+str>
                </#if>
            </#list>
        </#if>
    </#if>
    <dd class="oppClass p-btns jp-answer">
        <#if dto.subjectived >
            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
        <#else>
            <#if dto.shortQuestion.scorestandad??>
                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
            </#if>
        </#if>
        <#if dto.shortQuestion.answerreference??>
            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
        </#if>
        <#if question.explan?? && (question.explan?length>0)><div class="js_lastdl" >试题解析：${question.explan!}</div></#if>
    </dd>
<#elseif paperItemDto.viewType==4>
	<div class="p-temp-wrap">
		<div class="p-temp-ps fc">
	        <#if dto.questionErrUser??>
	        	<#if dto.questionErrUser.avgRate??>
	        		<p class="p-pro">错误率：<span><em style="width:${dto.questionErrUser.avgRate}%"></em></span>${dto.questionErrUser.avgRate?number?string('#')}%</p>
	        	<#else>
	        		<p class="p-pro">错误率：<span><em style="width:0%"></em></span>0%</p>
	        	</#if>
	        	<#if dto.questionErrUser.noAnswerUserName??>
	        		<p class="p-bottom p-names-bottom" title="${dto.questionErrUser.noAnswerUserName!}">未作答：${dto.questionErrUser.noAnswerUserName!}等${dto.questionErrUser.noAnswerUserNum!}人</p>
	        	<#else>
	        		<p class="p-bottom p-names-bottom" title="">未作答：无</p>
	        	</#if>
	            <#if dto.questionErrUser.errUserName??>
	        		<p class="p-bottom p-names-bottom" title="${dto.questionErrUser.errUserName!}">答错：${dto.questionErrUser.errUserName!}等${dto.questionErrUser.errUserNum!}人</p>
	        	<#else>
	        		<p class="p-bottom p-names-bottom" title="">答错：无</p>
	        	</#if>
	        <#else>
	        	<p class="p-pro">错误率：<span><em style="width:0%"></em></span>0%</p>
	        	<p class="p-bottom p-names-bottom" title="">未作答：无</p>
	        	<p class="p-bottom p-names-bottom" title="">答错：无</p>
	        </#if>
	    </div>
	</div>
    <#assign rightStr="">
    <br>
    <#if dto.shortQuestion.keyWordList??>
        <#assign keyWordList=dto.shortQuestion.keyWordList>
        <#if keyWordList??>
            <#list keyWordList as str>
                <#if (rightStr?length>0)>
                    <#assign rightStr=rightStr+str>
                <#else>
                    <#assign rightStr=rightStr+","+str>
                </#if>
            </#list>
        </#if>
    </#if>
    <dd class="p-btns">
        <#if dto.subjectived >
            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
        <#else>
            <#--<#if dto.shortQuestion.scorestandad??> -->
                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
            <#--</#if>-->
        </#if>
        <#--<#if dto.shortQuestion.answerreference??>-->
            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
        <#--</#if>-->
        <#--<#if question.explan?? && (question.explan?length>0)>-->
        <div class="js_lastdl" >试题解析：${question.explan!}</div>
        <#--</#if>-->
    </dd>
<#elseif paperItemDto.viewType==5>
    <#assign rightStr="">
    <br>
    <#if dto.shortQuestion.keyWordList??>
        <#assign keyWordList=dto.shortQuestion.keyWordList>
        <#if keyWordList??>
            <#list keyWordList as str>
                <#if (rightStr?length>0)>
                    <#assign rightStr=rightStr+str>
                <#else>
                    <#assign rightStr=rightStr+","+str>
                </#if>
            </#list>
        </#if>
    </#if>
    <dd class="p-i-answeradd">
        <div class="p-answer-title">答案</div>
        <div class="p-answer-conts">${paperItemDto.userAnswer!}</div>
    </dd>
    <#if (question.teId >0)>
    	<#--作为小题-->
        <dd class="jp-answer-min">
        	<div class="p-btns p-little-style">
		        <#if dto.subjectived >
		            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
		        <#else>
		            <#--<#if dto.shortQuestion.scorestandad??>-->
		                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
		            <#--</#if>-->
		        </#if>
		        <#--<#if dto.shortQuestion.answerreference??>-->
		            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
		        <#--</#if>-->
		         <#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
                	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
                </#if>
		        <#--<#if question.explan?? && (question.explan?length>0)>-->
		        	<div class="js_lastdl" >试题解析：${question.explan!}</div>
		        <#--</#if>-->
        	</div>
	    </dd>
	<#else>
	    <#--作为大题-->
        <dd class="oppClass p-btns jp-answer">
	        <#if dto.subjectived >
	            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
	        <#else>
	            <#--<#if dto.shortQuestion.scorestandad??>-->
	                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
	            <#--</#if>-->
	        </#if>
	        <#--<#if dto.shortQuestion.answerreference??>-->
	            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
	        <#--</#if>-->
        	<#-- 未判题不显示用户得分 -->
	        <#if paperItemDto.isCorrect!=-1>
            	<#import "../userScore.ftl" as userScore_ftl>
            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
            </#if>
	        <#--<#if question.explan?? && (question.explan?length>0)>-->
	        	<div class="js_lastdl" >试题解析：${question.explan!}</div>
	        <#--</#if>-->
	    </dd> 
    </#if>
    <#if (question.teId >0)&&question.issubjectived==1>
    	<dd class="jp-little-quest p-little-quest">
	        <div class="fc p-little-div">
	            <div class="fr p-w20">
	                <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="11-批阅-客观简答小题"><i class="ico"></i>查看解析</a>
	            </div>
	        </div>
	    </dd>
    <#elseif (question.teId >0)>
    	<!--1 为客观 0 为非客观-->
	    <div class="p-zy-zg jp-little-quest">
	        <div class="p-parsi">
	            <dl class="p-parklg">
	                <dt class="p-parsi-top jp-coradio">
	                    <label>批阅</label>
	                    <input type="radio" <#if paperItemDto.isCorrect==1>checked="checked"</#if> data-status ="a" name="result_answer_${question.id?c}" id="a" value="1"><span>正确</span>
	                    <input type="radio" <#if paperItemDto.isCorrect==0>checked="checked"</#if> data-status ="b" name="result_answer_${question.id?c}" id="b" value="0"><span>错误</span>
	                    <input type="radio" <#if paperItemDto.isCorrect==2>checked="checked"</#if> data-status ="c"name="result_answer_${question.id?c}" id="c" value="2"><span>部分正确</span>
	                    <i class="shu"></i>
	                    <label>得分</label>
	                    <input class="nums jp-inpt-numb" name="score_${question.id?c}" type="text" placeholder="0" readonly="readonly" value="${paperItemDto.userScore}">
	                    <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi-min" jiexi="11-批阅-客观简答小题">
	                        <i class="ico"></i>查看解析
	                    </a>
	                </dt>
	            </dl>
	        </div>
	    </div>
    </#if>
    
<#elseif paperItemDto.viewType==6>
    <#assign rightStr="">
    <br>
    <#if dto.shortQuestion.keyWordList??>
        <#assign keyWordList=dto.shortQuestion.keyWordList>
        <#if keyWordList??>
            <#list keyWordList as str>
                <#if (rightStr?length>0)>
                    <#assign rightStr=rightStr+str>
                <#else>
                    <#assign rightStr=rightStr+","+str>
                </#if>
            </#list>
        </#if>
    </#if>
    <#if (question.teId >0)>
    	<#--作为小题-->
	    <dd class="jp-answer-min">
	    	<div class="p-btns p-little-style">
	    		<div class=" hideDiv">您的答案：<br>
		        	<span class="ft11"><#if paperItemDto.userAnswer??>${paperItemDto.userAnswer!}</#if></span>
			       	<#if paperItemDto.isCorrect==1>
		                <span class="icright"></span>
		            <#elseif paperItemDto.isCorrect!=-1>
		                <span class="icerror"></span>
		            </#if>
	            </div>
		        <#if dto.subjectived >
		            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
		        <#else>
		            <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
		        </#if>
	            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
	         	<#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
                	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
                </#if>
		        <div class="js_lastdl" >试题解析：${question.explan!}</div>
	    	</div>
	    </dd>
	<#else>
	    <#--作为大题-->
        <dd class="oppClass p-btns jp-answer">
	    	<div class=" hideDiv">您的答案：<br>
	        	<span class="ft11"><#if paperItemDto.userAnswer??>${paperItemDto.userAnswer!}</#if></span>
			    <#if paperItemDto.isCorrect==1>
	                <span class="icright"></span>
	            <#elseif paperItemDto.isCorrect!=-1>
	                <span class="icerror"></span>
	            </#if>
            </div>
	        <#if dto.subjectived >
	            <div class=" js_lastdl">正确关键字：<span class="ft11 ftc1">${rightStr}</span></div>
	        <#else>
	            <#--<#if dto.shortQuestion.scorestandad??>-->
	                <div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.shortQuestion.scorestandad!}</span></div>
	            <#--</#if>-->
	        </#if>
	        <#--<#if dto.shortQuestion.answerreference??>-->
	            <div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.shortQuestion.answerreference!}</span></div>
	        <#--</#if>-->
	         <#-- 未判题不显示用户得分 -->
	        <#if paperItemDto.isCorrect!=-1>
            	<#import "../userScore.ftl" as userScore_ftl>
            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
            </#if>
	        <#--<#if question.explan?? && (question.explan?length>0)>-->
	        <div class="js_lastdl" >试题解析：${question.explan!}</div>
	        <#--</#if>-->
	    </dd>
    </#if>
<#else>
    <dd class="oppClass p-btns">
        <div class="ft4">填写答案</div>
        <#if paperItemDto.userAnswer??>
            <textarea class="ueditor ueditorShort" rows='${dto.shortQuestion.boxheight}' name='${question.id?c}' style="display:none" id='${question.id?c}_${question.questionTypeId?c}'>${paperItemDto.userAnswer}</textarea>
            <div class="jdtdiv">${paperItemDto.userAnswer!}</div>
        <#else>
            <textarea class="dhl_jd ueditor ueditorShort" rows='${dto.shortQuestion.boxheight}' name='${question.id?c}'  id='${question.questionTypeId?c}_${question.id?c}'></textarea>
            <div class="jdtdiv" style="display:none"></div>
        </#if>

    </dd>
</#if>

<#if (question.teId >0) &&((paperItemDto.viewType==1)||(paperItemDto.viewType==2)||(paperItemDto.viewType==6))>
<#--作为小题模版处理-->
<dd class="jp-little-quest p-little-quest">
    <div class="fc p-little-div">
    	<#-- 作业复习不显示知识点 -->
    	<#if (paperItemDto.viewType!=6)	>
            <div class="fl p-w80">
                <p><span>知识点：${(question.knowledgeTags)!""}</span></p>
                <p><span>考查能力：${(question.kaoChaNl)!""}</span></p>
            </div>
        </#if>
        <div class="fr p-w20">
            <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="9-讲评-选择小题-不显示"><i class="ico"></i>查看解析</a>
        </div>
    </div>
</dd>
</#if>
</dl>