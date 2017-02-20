
<#--改错子题：改错题correctionQuestion】106
dto
i
paperItemDtoList
process
<#import "../mark.ftl" as mark_ftl>
-->
<#assign questionType = dto.questionType>
<#assign question=dto.questionDto.question >
<#assign userAnswers="">
<#if paperItemDto.userAnswer??>
<#assign userAnswers=paperItemDto.userAnswer>
</#if>
<#assign encodeAnswer = "">
	<div class="mb20" qq="subCorrectionQuestion.ftl${paperItemDto.viewType}" teId='${dto.questionDto.question.teId?c}' questionId='${dto.questionDto.question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}">
		<dl class="st">
			<dt class="nobold dhl_gcz 	js_score"><a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
				<span class="num" id="${paperItemDto.questionNo}">${paperItemDto.questionNo}</span>
				<div class="ZW ZwEdit">
					${dto.correctionQuestion.clause!}【本题：<span class="jp-subscore-view">${paperItemDto.score!'0'}</span>分】
				</div>
				<input type="hidden" class="sc_ans_html" name="${dto.questionDto.question.id?c}" value="${encodeAnswer}"/>
				<#if paperItemDto.clickIndex??>
				<input type="hidden" id='${dto.questionDto.question.questionTypeId?c}_${dto.questionDto.question.id?c}' class="sc_ans dhl_gcz_ua" name="${dto.questionDto.question.id?c}_ans" value="${paperItemDto.clickIndex}"/>
				<#else>
				<input type="hidden" id='${dto.questionDto.question.questionTypeId?c}_${dto.questionDto.question.id?c}' class="sc_ans dhl_gcz_ua" name="${dto.questionDto.question.id?c}_ans" value=""/>
				</#if>
			</dt>

			<#-- 
				<%@ include file="../mark.inc"%>
			<#if type??>
			<@mark_ftl.mark type=type testProcess=process paperItemDto=paperItemDto />
			</#if>
			-->
			<#if paperItemDto.viewType==1&& (question.teId ==0)>
            <#--作为大题-->
			<dd class="oppClass jp-answer">
				<div class="ft5 mb20 cc2"><strong>正确答案：</strong> <br />
					${dto.correctionQuestion.clauseAnswer!}
				</div>
			</dd>
            </#if>
            <#if paperItemDto.viewType==1&& (question.teId >0)>
            <#--作为小题-->
                <dd class="jp-answer-min">
                    <div class="ft5 cc2 p-btns p-little-style"><strong>正确答案：</strong> <br />
                    ${dto.correctionQuestion.clauseAnswer!}
                    </div>
                </dd>
			</#if>
			<#if paperItemDto.viewType==2>
				<#if (question.teId >0)>
			    	<#--作为小题-->
					<dd class="jp-answer-min">
						<div class="p-btns p-little-style">
							<div class="ft5 mb20 hideDiv"><strong>您的答案：</strong> <br />
								<div class="ZW">${userAnswers}
								<#if paperItemDto.isCorrect==1 >
									<span class="icright" style="display:none;"></span>
								<#elseif paperItemDto.isCorrect!=-1>
									<span class="icerror" style="display:none;"></span>
								</#if>
								</div>
							</div>
							<div class="ft5 mb20 cc2 js_lastdl"><strong>正确答案：</strong> <br />
								${dto.correctionQuestion.clauseAnswer!}
							</div>
				         	<#-- 未判题不显示用户得分 -->
					        <#if paperItemDto.isCorrect!=-1>
			                	<#import "../userScore.ftl" as userScore_ftl>
				            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
			                </#if>
						</div>
					</dd>
				<#else>
				    <#--作为大题-->
					<dd class="oppClass jp-answer">
						<div class="ft5 mb20 hideDiv"><strong>您的答案：</strong> <br />
							<div class="ZW">${userAnswers}
							<#if paperItemDto.isCorrect==1 >
								<span class="icright" style="display:none;"></span>
							<#elseif paperItemDto.isCorrect!=-1>
								<span class="icerror" style="display:none;"></span>
							</#if>
							</div>
						</div>
						<div class="ft5 mb20 cc2 js_lastdl"><strong>正确答案：</strong> <br />
							${dto.correctionQuestion.clauseAnswer!}
						</div>
						<#-- 未判题不显示用户得分 -->
				        <#if paperItemDto.isCorrect!=-1>
		                	<#import "../userScore.ftl" as userScore_ftl>
			            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
		                </#if>
					</dd>
			    </#if>
			</#if>
			<#if paperItemDto.viewType==3>
				<dd class="oppClass jp-answer">
					<div class="ft5 mb20 cc2 js_lastdl"><strong>正确答案：</strong> <br />
						${dto.correctionQuestion.clauseAnswer!}
					</div>
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
			</#if>
			<#if paperItemDto.viewType==5>
				    <#--作为大题-->
					<dd class="oppClass jp-answer">
						<div class="p-btns p-little-style">
							<div class="ft5 hideDiv"><strong>您的答案：</strong> <br />
								<div class="ZW">${userAnswers}
								<#if paperItemDto.isCorrect==1 >
									<span class="icright" style="display:none;"></span>
								<#elseif paperItemDto.isCorrect!=-1>
									<span class="icerror" style="display:none;"></span>
								</#if>
								</div>
							</div>
							<div class="ft5 cc2 js_lastdl"><strong>正确答案：</strong> <br />
								${dto.correctionQuestion.clauseAnswer!}
							</div>
							<#-- 未判题不显示用户得分 -->
					        <#if paperItemDto.isCorrect!=-1>
			                	<#import "../userScore.ftl" as userScore_ftl>
				            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
			                </#if>
			            </div>
					</dd>
			    <#if (question.teId >0)&&question.issubjectived==1>
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
				                    <!--a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi-min">
				                        <i class="ico"></i>查看解析
				                    </a-->
				                </dt>
				            </dl>
				        </div>
				    </div>
			    </#if>
			</#if>
			
			<#if paperItemDto.viewType==6>
				<#if (question.teId >0)>
			    	<#--作为小题-->
					<dd class="jp-answer-min">
						<div class="p-btns p-little-style">
							<div class="ft5 mb20 hideDiv"><strong>您的答案：</strong> <br />
								<div class="ZW">${userAnswers}
								<#if paperItemDto.isCorrect==1 >
									<span class="icright" style="display:none;"></span>
								<#elseif paperItemDto.isCorrect!=-1>
									<span class="icerror" style="display:none;"></span>
								</#if>
								</div>
							</div>
							<div class="ft5 mb20 cc2 js_lastdl"><strong>正确答案：</strong> <br />
								${dto.correctionQuestion.clauseAnswer!}
							</div>
				         	<#-- 未判题不显示用户得分 -->
					        <#if paperItemDto.isCorrect!=-1>
			                	<#import "../userScore.ftl" as userScore_ftl>
				            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
			                </#if>
						</div>
					</dd>
				<#else>
				    <#--作为大题-->
					<dd class="oppClass jp-answer">
						<div class="ft5 mb20 hideDiv"><strong>您的答案：</strong> <br />
							<div class="ZW">${userAnswers}
							<#if paperItemDto.isCorrect==1 >
								<span class="icright" style="display:none;"></span>
							<#elseif paperItemDto.isCorrect!=-1>
								<span class="icerror" style="display:none;"></span>
							</#if>
							</div>
						</div>
						<div class="ft5 mb20 cc2 js_lastdl"><strong>正确答案：</strong> <br />
							${dto.correctionQuestion.clauseAnswer!}
						</div>
						<#-- 未判题不显示用户得分 -->
				        <#if paperItemDto.isCorrect!=-1>
		                	<#import "../userScore.ftl" as userScore_ftl>
			            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
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
                            <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="12-改错子题"><i class="ico"></i>查看解析</a>
                        </div>
                    </div>
                </dd>
            </#if>
		</dl>
	</div>
	
