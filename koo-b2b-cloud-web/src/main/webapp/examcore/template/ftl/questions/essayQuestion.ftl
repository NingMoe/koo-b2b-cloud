<#-- 
传递的参数有 作为clozeFillQuestion.ftl的小题模版
dto EssayQuestionDTO
//paperItemDtoList
i
process
a-->
<#--import "../mark.ftl" as mark_ftl>
-->
<#assign questionDto=dto.questionDto >
<#assign question=questionDto.question >
<#if dto.questionDto.questionAttachs??>
    <#assign attachs=dto.questionDto.questionAttachs >
</#if>
<#assign answers=dto.fillblankAnswers >

<#-- 
<#assign paperItemDtoList=process.paperItemDtoList >
-->
<#-- //正确答案串 -->
<#assign rightAnswer="" >
<dl class="st tm_zt_${question.teId?c}" qq="essayQuestion.ftl" ty="2" teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"    >
    <dt class="nobold js_score">
    <#if paperItemDto.questionNo!="">
        <a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a><span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span>
    </#if>
    <#if attachs?? && (attachs?size>0)>
        <#list attachs as attach >
        ${attach.content}
        </#list>
    </#if>

    <#if dto.essayQuestion.topic?? >
    ${dto.essayQuestion.topic}【本题：<span class="jp-score-view">${paperItemDto.score}</span>分】
    </#if>
    </dt>
    <dd class="fc">
    <#if answers?? && (answers?size > 0)>
        <span class="ft4 fl">填写答案：</span>
    <span class="fl w2">
        <#assign rightAnswer=QuestionHelper.essayQuestionRightAnswer(dto)>
                  	${QuestionHelper.essayQuestionSeg(paperItemDto.userAnswer,dto)}
				</#if>
    </span>
    </dd>

<#--
  <%@ include file="../mark.inc"%>
-->
<#--
<#if type??>
<@mark_ftl.mark type=type testProcess=process paperItemDto=paperItemDto />
</#if>
-->
<#if paperItemDto.viewType==1&& (question.teId ==0)>
<#--作为大题-->
    <#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <dd class="oppClass jp-answer">
        <div>正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
        <#if  question.explan?? && (question.explan?length >0) >
            <div class=" ">试题解析：
            ${question.explan!}
            </div>
        </#if>
    </dd>
</#if>
<#if paperItemDto.viewType==1&& (question.teId >0)>
   <#--作为小题-->
    <#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <dd class="jp-answer-min">
        <div class="p-btns p-little-style">
            <div>正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
            <#if  question.explan?? && (question.explan?length >0) >
                <div class=" ">试题解析：
                ${question.explan!}
                </div>
            </#if>
        </div>
    </dd>
</#if>
<#if paperItemDto.viewType==2>
    <#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <#if (question.teId >0)>
    	<#--作为小题-->
	    <dd class="jp-answer-min">
	    	<div class="p-btns p-little-style">
	    		<div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
		            <#--<#if iExamQuestionDto.subjectived??>-->
		                <#if paperItemDto.isCorrect==1>
		                    <span class="icright"></span>
		                <#elseif paperItemDto.isCorrect!=-1>
		                    <span class="icerror"></span>
		                </#if>
		            <#--</#if>-->
		        </div>
		        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
		        <#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
                	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
                </#if>
		        <#--
		        <#if  question.explan?? && (question.explan?length >0) >
		        -->
		            <div class=" js_lastdl">试题解析：
		            ${question.explan!}
		            </div>
		        <#--</#if>-->
	    	</div>
	    </dd>
	<#else>
	    <#--作为大题-->
	    <dd class="oppClass jp-answer">
	        <div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
	            <#--<#if iExamQuestionDto.subjectived??>-->
	                <#if paperItemDto.isCorrect==1>
	                    <span class="icright"></span>
	                <#elseif paperItemDto.isCorrect!=-1>
	                    <span class="icerror"></span>
	                </#if>
	            <#--</#if>-->
	        </div>
	        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
             	<#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
	            	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	            </#if>
	        <#if  question.explan?? && (question.explan?length >0) >
	            <div class=" js_lastdl">试题解析：
	            ${question.explan!}
	            </div>
	        </#if>
	    </dd>    
    </#if>
</#if>

<#if paperItemDto.viewType==3>
	<#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <dd class="oppClass jp-answer">
        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
        <#if  question.explan?? && (question.explan?length >0) >
            <div class=" js_lastdl">试题解析：
            ${question.explan!}
            </div>
        </#if>
    </dd>
</#if>

<#if paperItemDto.viewType==4>
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
	<#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <dd>
        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
        <#--
        <#if  question.explan?? && (question.explan?length >0) >
        -->
            <div class=" js_lastdl">试题解析：
            ${question.explan!}
            </div>
        <#--</#if>-->
    </dd>
</#if>

<#if paperItemDto.viewType==5>
	<#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <#if (question.teId >0)>
    	<#--作为小题-->
    	<dd class="jp-answer-min">
    		<div class="p-btns p-little-style">
		    	<div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
		    		<#if paperItemDto.isCorrect==1>
	                    <span class="icright"></span>
	                <#elseif paperItemDto.isCorrect!=-1>
	                    <span class="icerror"></span>
	                </#if>
		    	</div>
		        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
			         <#-- 未判题不显示用户得分 -->
			        <#if paperItemDto.isCorrect!=-1>
	                	<#import "../userScore.ftl" as userScore_ftl>
		            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	                </#if>
		        <#--
		        <#if  question.explan?? && (question.explan?length >0) >
		        -->
		            <div class=" js_lastdl">试题解析：
		            ${question.explan!}
		            </div>
		        <#--</#if>-->
	       </div>
	    </dd>
	<#else>
	    <#--作为大题-->
	    <dd class="oppClass jp-answer">
	    	<div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
	    		<#if paperItemDto.isCorrect==1>
                    <span class="icright"></span>
                <#elseif paperItemDto.isCorrect!=-1>
                    <span class="icerror"></span>
                </#if>
	    	</div>
	        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
	        <#--
	        <#if  question.explan?? && (question.explan?length >0) >
	        -->
	            <div class=" js_lastdl">试题解析：
	            ${question.explan!}
	            </div>
	       <#-- </#if>-->
	    </dd>    
    </#if>
    <#if (question.teId >0)&&question.issubjectived==1>
    	<dd class="jp-little-quest p-little-quest">
	        <div class="fc p-little-div">
	            <div class="fr p-w20">
	                <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="10-批阅-客观填空小题"><i class="ico"></i>查看解析</a>
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
	                    <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi-min" jiexi="10-批阅-主观填空小题">
	                        <i class="ico"></i>查看解析
	                    </a>
	                </dt>
	            </dl>
	        </div>
	    </div>
    </#if>
</#if>

<#if paperItemDto.viewType==6>
    <#assign strr="">
    <#if paperItemDto.userAnswer??>
        <#assign strr=paperItemDto.userAnswer?replace("&","   ,   ")>
    </#if>
    <#assign iExamQuestionDto=dto>
    <br>
    <#if (question.teId >0)>
    	<#--作为小题-->
	    <dd class="jp-answer-min">
	    	<div class="p-btns p-little-style">
	    		<div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
		            <#--<#if iExamQuestionDto.subjectived??>-->
		                <#if paperItemDto.isCorrect==1>
		                    <span class="icright"></span>
		                <#elseif paperItemDto.isCorrect!=-1>
		                    <span class="icerror"></span>
		                </#if>
		            <#--</#if>-->
		        </div>
		        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
		        <#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
                	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
                </#if>
		        <#--
		        <#if  question.explan?? && (question.explan?length >0) >
		        -->
		            <div class=" js_lastdl">试题解析：
		            ${question.explan!}
		            </div>
		        <#--</#if>-->
	    	</div>
	    </dd>
	<#else>
	    <#--作为大题-->
	    <dd class="oppClass jp-answer">
	        <div class="hideDiv">您的答案：<span class="ft11">${strr}</span>
	            <#--<#if iExamQuestionDto.subjectived??>-->
	                <#if paperItemDto.isCorrect==1>
	                    <span class="icright"></span>
	                <#elseif paperItemDto.isCorrect!=-1>
	                    <span class="icerror"></span>
	                </#if>
	            <#--</#if>-->
	        </div>
	        <div class="js_lastdl">正确答案：<span class="ft11 ftc1">${rightAnswer}</span></div>
             	<#-- 未判题不显示用户得分 -->
		        <#if paperItemDto.isCorrect!=-1>
	            	<#import "../userScore.ftl" as userScore_ftl>
	            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	            </#if>
	        <#if  question.explan?? && (question.explan?length >0) >
	            <div class=" js_lastdl">试题解析：
	            ${question.explan!}
	            </div>
	        </#if>
	    </dd>    
    </#if>
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
                <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="10-非批阅-填空小题"><i class="ico"></i>查看解析</a>
            </div>
        </div>
    </dd>
</#if>
</dl>
