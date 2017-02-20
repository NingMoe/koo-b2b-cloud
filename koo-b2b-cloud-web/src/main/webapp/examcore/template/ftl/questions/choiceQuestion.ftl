		<#-- 1 阅读题readQuestion模版的子题模版
		     2.选择型完形填空题子题模版choiceFillBlankQuestion
		<#import "../mark.ftl" as mark_ftl>
		-->
		<#assign rightStr="" > 
		<#assign question=dto.questionDto.question > 
		<#if dto.questionDto.questionAttachs??>
		<#assign attachs=dto.questionDto.questionAttachs > 
		</#if>
		<#-- 改调用原生选项的方法 -->
		<#-- 以下涉及显示序号的用循环序列的索引展示，与业务及数据解耦 -->
		<#if paperItemDto.viewType==0 && paperItemDto.examing==true>
			<#assign choiceAnswer=dto.cas >
		<#else>
			<#assign choiceAnswer=dto.choiceAnswers >
		</#if>
		<dl class="st tm_zt_${question.teId?c}" qq="choiceQuestion.ftl${paperItemDto.viewType}" questionType="${dto.questionType}" ty="0" teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  >
			<dt class="nobold js_score">
                <#if paperItemDto.questionNo!="">
                  <a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a><span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span>
                </#if>
			<#if  (attachs??) && (attachs?size>0)>
				<#list attachs as attach>
					${attach.content}			
				</#list>
			</#if>
			<#if dto.choiceQuestion.topic?? && (dto.choiceQuestion.topic?length gt 0) >
				${dto.choiceQuestion.topic}【本题：<span class="jp-score-view jp-subscore-view">${paperItemDto.score!'0'}</span>分】
				<#if question.questionTip?? &&(question.questionTip?length gt 0) >
				<p class="ft3">${question.questionTip}</p>
				</#if>
				</dt>
				<dd>
					<ul class="stul">
						<#list choiceAnswer as ca >
						<li style="width:${100/dto.choiceQuestion.composeType}%"><span class="xx">[${TestUtil.transToLetter(ca_index) }]</span> ${ca.description}</li>
						</#list>
					</ul>
				</dd>
			<#else>
				<#if question.questionTip?? && (question.questionTip?length>0) >
					<p class="ft3">${question.questionTip}</p>
				</#if>
				<ul class="stul mt1">
					<#list choiceAnswer as ca >
						<li style="width:${100/dto.choiceQuestion.composeType}%"><span class="xx">[${TestUtil.transToLetter(ca_index) }]</span> ${ca.description}</li>
					</#list>
				</ul>
				</dt>
			</#if>
				<dd>
				<div class="dAn fc">
					<span class="ft4 fl">选择答案：</span>
					<span class="fl w2 bx7"> 
					<#list choiceAnswer as ca >
						<#if ca.isright?? && ca.isright==1>
							<#assign rightStr=rightStr+TestUtil.transToLetter(ca.sequenceId)+" ">
						</#if>
						<#if paperItemDto.userAnswer??>
						<#assign userAnswer=paperItemDto.userAnswer >
						</#if>
						<#if userAnswer??>
						<#else>
							<#assign userAnswer="&" >
						</#if>
						<#assign questionType=dto.questionType >
						<#if questionType==Question.QUESTION_TYPE_DANXUAN || questionType==Question.QUESTION_TYPE_DANXUAN_SHADE ||questionType==Question.QUESTION_TYPE_DANXUAN_GRAPH>
							<#if userAnswer ==(ca.sequenceId+"")>
								<label class="sd"><input type="radio" name='${dto.choiceQuestion.questionId?c}' value='${ca.sequenceId}' id='${questionType}_${dto.choiceQuestion.questionId?c}_${ca.sequenceId?c}' checked="checked"/> ${TestUtil.transToLetter(ca_index)}</label>
							<#else>
								<label><input type="radio" name='${dto.choiceQuestion.questionId?c}' value='${ca.sequenceId}' id='${questionType}_${dto.choiceQuestion.questionId?c}_${ca.sequenceId?c}' class="dhl_dx" /> ${TestUtil.transToLetter(ca_index)}</label>
							</#if>
						</#if>
						<#if questionType==Question.QUESTION_TYPE_DUOXUAN || questionType==Question.QUESTION_TYPE_DUOXUAN_SHADE ||questionType==Question.QUESTION_TYPE_DUOXUAN_GRAPH>
							<#if userAnswer?index_of(ca.sequenceId+"")!=-1>
								<label class="sd"><input type="checkbox" name='${dto.choiceQuestion.questionId?c}' value='${ca.sequenceId}' id='${questionType}_${dto.choiceQuestion.questionId?c}_${ca.sequenceId?c}' checked/> ${TestUtil.transToLetter(ca_index) }</label>
							<#else>
								<label><input type="checkbox" name='${dto.choiceQuestion.questionId?c}' value='${ca.sequenceId}' id='${questionType}_${dto.choiceQuestion.questionId?c}_${ca.sequenceId?c}' class="dhl_ddx"/> ${TestUtil.transToLetter(ca_index)}</label>
							</#if>
						</#if>
					</#list>
				</span>
				</div>
			</dd>
			<#--
			<%@ include file="../mark.inc"%>
			
			<#if type??>
			<@mark_ftl.mark type=type testProcess=process paperItemDto=paperItemDto />
			</#if>
			-->

			<#if paperItemDto.viewType==1&& (question.teId ==0)>
                  <#--作为大题-->

				    <dd class="oppClass p-btns jp-answer">
					<div class="mb10 ">答案：<span class="ft11 ftc1">${rightStr}</span></div>
					<#if question.explan?? && question.explan?length  gt 0 >
					<div>解析：
					${question.explan}
					</div>					
					</#if>
					</dd>
			</#if>
        <#if paperItemDto.viewType==1&& (question.teId >0)>
            <#--作为小题-->
            <dd class="jp-answer-min">
                <div class="p-btns p-little-style">
                    <div class="mb10 ">答案：<span class="ft11 ftc1">${rightStr}</span></div>
                    <#if question.explan?? && question.explan?length  gt 0 >
                        <div>解析：
                        ${question.explan}
                        </div>
                    </#if>
                </div>
            </dd>
        </#if>
		<#if paperItemDto.viewType==2>
			<#if (question.teId >0)>
		    	<#--作为小题-->
		    	<dd class="jp-answer-min">
		    		<div class="p-btns p-little-style">
		    			<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
							<#if paperItemDto.isCorrect==1>
							<span class="icright"></span>
							<#elseif paperItemDto.isCorrect!=-1>
							<span class="icerror"></span>
							</#if>
						</div>
						<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
						<#import "../userScore.ftl" as userScore_ftl>
						<@userScore_ftl.userScore paperItemDto=paperItemDto/>
						<#--
						<#if question.explan?? && question.explan?length  gt 0 >
						-->
						<div class="js_lastdl">试题解析：
						${question.explan!}
						</div>					
						<#--</#if>-->
		    		</div>
				</dd>
			<#else>
			    <#--作为大题-->
			    <dd class="oppClass  p-btns jp-answer">
					<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
						<#if paperItemDto.isCorrect==1>
						<span class="icright"></span>
						<#elseif paperItemDto.isCorrect!=-1>
						<span class="icerror"></span>
						</#if>
					</div>
					<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
					<#import "../userScore.ftl" as userScore_ftl>
					<@userScore_ftl.userScore paperItemDto=paperItemDto/>
					<#--
					<#if question.explan?? && question.explan?length  gt 0 >
					-->
					<div class="js_lastdl">试题解析：
					${question.explan!}
					</div>					
					<#--</#if>-->
				</dd>
		    </#if>		
		</#if>
		<#if paperItemDto.viewType==3>
			<dd class="oppClass  p-btns jp-answer">
				<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
				<#--
				<#if question.explan?? && question.explan?length  gt 0 >
				-->
				<div class="js_lastdl">试题解析：
				${question.explan!}
				</div>					
				<#--</#if>-->
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
			<dd class="p-btns">
				<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
				<#--
				<#if question.explan?? && question.explan?length  gt 0 >
				-->
				<div class="js_lastdl">试题解析：
				${question.explan!}
				</div>					
				<#--</#if>-->
			</dd>
		</#if>
		<#if paperItemDto.viewType==5>
			 <#if (question.teId >0)>
		    	<#--作为小题-->
			    <dd class="jp-answer-min">
			    	<div class="p-btns p-little-style">
						<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
							<#if paperItemDto.isCorrect==1>
							<span class="icright"></span>
							<#elseif paperItemDto.isCorrect!=-1>
							<span class="icerror"></span>
							</#if>
						</div>
						<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
						<#import "../userScore.ftl" as userScore_ftl>
						<@userScore_ftl.userScore paperItemDto=paperItemDto/>
						<#--
						<#if question.explan?? && question.explan?length  gt 0 >
						-->
						<div class="js_lastdl">试题解析：
						${question.explan!}
						</div>					
						<#--</#if>-->
					</div>
				</dd>
			<#else>
			    <#--作为大题-->
			    <dd class="oppClass  p-btns jp-answer">
					<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
						<#if paperItemDto.isCorrect==1>
						<span class="icright"></span>
						<#elseif paperItemDto.isCorrect!=-1>
						<span class="icerror"></span>
						</#if>
					</div>
					<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
					<#import "../userScore.ftl" as userScore_ftl>
					<@userScore_ftl.userScore paperItemDto=paperItemDto/>
					<#--
					<#if question.explan?? && question.explan?length  gt 0 >
					-->
					<div class="js_lastdl">试题解析：
					${question.explan!}
					</div>					
					<#--</#if>-->
				</dd>
		    </#if>
			<#if (question.teId >0)>
				<#--批阅不显示知识点-->
                <dd class="jp-little-quest p-little-quest">
                    <div class="fc p-little-div">
                        <div class="fr p-w20">
                            <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="8-批阅-选择题(小题)"><i class="ico"></i>查看解析</a>
                        </div>
                    </div>
                </dd>
            </#if>
		</#if>
		<#-- 学生端作业复习 -->
		<#if paperItemDto.viewType==6>
			<#if (question.teId >0)>
		    	<#--作为小题-->
		    	<dd class="jp-answer-min">
		    		<div class="p-btns p-little-style">
		    			<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
							<#if paperItemDto.isCorrect==1>
							<span class="icright"></span>
							<#elseif paperItemDto.isCorrect!=-1>
							<span class="icerror"></span>
							</#if>
						</div>
						<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
						<#import "../userScore.ftl" as userScore_ftl>
						<@userScore_ftl.userScore paperItemDto=paperItemDto/>
						<#--
						<#if question.explan?? && question.explan?length  gt 0 >
						-->
						<div class="js_lastdl">试题解析：
						${question.explan!}
						</div>					
						<#--</#if>-->
		    		</div>
				</dd>
			<#else>
			    <#--作为大题-->
			    <dd class="oppClass  p-btns jp-answer">
					<div class="hideDiv">您的答案：<span class="ft11 userAnswerSource">${QuestionHelper.findUserAnswer(paperItemDto)}</span>
						<#if paperItemDto.isCorrect==1>
						<span class="icright"></span>
						<#elseif paperItemDto.isCorrect!=-1>
						<span class="icerror"></span>
						</#if>
					</div>
					<div class="js_lastdl">正确答案：<span class="ft11 ftc1 rightAnswerSource">${rightStr!}</span></div>
					<#import "../userScore.ftl" as userScore_ftl>
					<@userScore_ftl.userScore paperItemDto=paperItemDto/>
					<#--
					<#if question.explan?? && question.explan?length  gt 0 >
					-->
					<div class="js_lastdl">试题解析：
					${question.explan!}
					</div>					
					<#--</#if>-->
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
                            <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="9-讲评-选择小题-不显示"><i class="ico"></i>查看解析</a>
                        </div>
                    </div>
                </dd>
            </#if>
</dl>

		  