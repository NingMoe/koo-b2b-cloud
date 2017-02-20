<#assign questionDto=dto.questionDto>
<#assign question=questionDto.question>
<#if questionDto.questionAttachs??>
	<#assign attachs=questionDto.questionAttachs>
</#if>
<dl class="st tm_zt_${dto.questionDto.question.teId?c}" qq="writeQuestion.ftl${paperItemDto.viewType}" teId='${dto.questionDto.question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  >
	<dt class="nobold js_score"> <#if paperItemDto.questionNo!=""><a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
		<span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span></#if>
		<#if attachs?? && (attachs?size gt 0)>
			<#list attachs as attach>
				${attach.content}
			</#list>
		</#if>
		<#if dto.whriteQuestion.topic??>${dto.whriteQuestion.topic}</#if>
		<#if dto.whriteQuestion.answer?? && (dto.whriteQuestion.answer?length gt 0)>
			<p class="ft3">${dto.whriteQuestion.answer}</p>
		</#if>
		【本题：<span class="jp-score-view">${paperItemDto.score}</span>分】
	</dt>

     <#--<#if paperItemDto.buttonType==0>-->
        <#--parseShowInStudent()show_in_stu 方法控制隐藏填写答案模块-->
		<dd class="js_nohide show_in_stu">
            <div class="ft4">填写答案</div>
            <#if paperItemDto.userAnswer??>
                <textarea class="jdt dhl_xz" name='${question.id?c}' id='${question.questionTypeId?c}_${question.id?c}'>${paperItemDto.userAnswer}</textarea>
            <#else>
                <textarea class="jdt dhl_xz" name='${question.id?c}' id='${question.questionTypeId?c}_${question.id?c}'></textarea>
            </#if>
		<#--<div class="ta_r"><a href="javascript:;" class="zhk">展开</a></div> -->
	   </dd>
     <#--</#if>-->
		<#if paperItemDto.viewType==1>
			<dd class="oppClass jp-answer">
				<#if dto.whriteQuestion.answerreference??>
					<div class="">参考答案：</div>
	                <div>
	                    <span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span>
	                </div>
				</#if>
				<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>
					<div class=" ">试题解析：${question.explan!}</div>
				</#if>
			</dd>
		</#if>
		<#if paperItemDto.viewType==2>
			<#if (question.teId >0)>
		    	<#--作为小题-->
				<dd class="jp-answer-min">
					<div class="p-btns p-little-style">
						<#--<#if dto.whriteQuestion.scorestandad??>-->
							<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
						<#--</#if> -->
						<#--<#if dto.whriteQuestion.answerreference??>-->
							<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
						<#--</#if>-->
						<#-- 未判题不显示用户得分 -->
				        <#if paperItemDto.isCorrect!=-1>
		                	<#import "../userScore.ftl" as userScore_ftl>
			            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
		                </#if>
						<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
							<div class="js_lastdl">试题解析：${question.explan!}</div>
						<#--</#if>-->
					</div>
				</dd>
			<#else>
			    <#--作为大题-->
		        <dd class="oppClass jp-answer">
					<#--<#if dto.whriteQuestion.scorestandad??>-->
						<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
					<#--</#if> -->
					<#--<#if dto.whriteQuestion.answerreference??>-->
						<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
					<#--</#if>-->
					<#-- 未判题不显示用户得分 -->
			        <#if paperItemDto.isCorrect!=-1>
	                	<#import "../userScore.ftl" as userScore_ftl>
		            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	                </#if>
					<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
						<div class="js_lastdl">试题解析：${question.explan!}</div>
					<#--</#if>-->
				</dd>
		    </#if>
		</#if>
		<#if paperItemDto.viewType==4>
			<div class="p-temp-wrap">
				<div class="p-temp-ps fc">
			        <#if dto.questionErrUser??>
			        	<#if dto.questionErrUser.avgRate??>
			        		<p class="p-pro">错误率：<span><em style="width:${dto.questionErrUser.avgRate!}%"></em></span>${dto.questionErrUser.avgRate?number?string('#')}%</p>
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
			<dd>
				<#--<#if dto.whriteQuestion.scorestandad??>-->
					<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
				<#--</#if> -->
				<#--<#if dto.whriteQuestion.answerreference??>-->
					<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
				<#--</#if>-->
				<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
					<div class="js_lastdl">试题解析：${question.explan!}</div>
				<#--</#if>-->
			</dd>
		</#if>
		<#if paperItemDto.viewType==5>
			<#if (question.teId >0)>
		    	<#--作为小题-->
    			<dd class="jp-answer-min">
    				<div class="p-btns p-little-style">
    					<#--<#if dto.whriteQuestion.scorestandad??>-->
							<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
						<#--</#if> -->
						<#--<#if dto.whriteQuestion.answerreference??>-->
							<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
						<#--</#if>-->
						<#-- 未判题不显示用户得分 -->
				        <#if paperItemDto.isCorrect!=-1>
		                	<#import "../userScore.ftl" as userScore_ftl>
			            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
		                </#if>
						<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
							<div class="js_lastdl">试题解析：${question.explan!}</div>
						<#--</#if>-->
    				</div>
				</dd>
			<#else>
			    <#--作为大题-->
		        <dd class="oppClass jp-answer">
					<#--<#if dto.whriteQuestion.scorestandad??>-->
						<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
					<#--</#if> -->
					<#--<#if dto.whriteQuestion.answerreference??>-->
						<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
					<#--</#if>-->
					<#-- 未判题不显示用户得分 -->
			        <#if paperItemDto.isCorrect!=-1>
	                	<#import "../userScore.ftl" as userScore_ftl>
		            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	                </#if>
					<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
						<div class="js_lastdl">试题解析：${question.explan!}</div>
					<#--</#if>-->
				</dd>
		    </#if>
			<#if (question.teId >0)&&question.issubjectived==1>
		    	<dd class="jp-little-quest p-little-quest">
			        <div class="fc p-little-div">
			            <div class="fr p-w20">
			                <a class="white-btn white-btn-jx jp-parsi-min" href="javascript:;" jiexi="13-批阅-客观写作小题"><i class="ico"></i>查看解析</a>
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
			                    <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi-min" jiexi="13-批阅-主观写作小题">
			                        <i class="ico"></i>查看解析
			                    </a>
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
						<#--<#if dto.whriteQuestion.scorestandad??>-->
							<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
						<#--</#if> -->
						<#--<#if dto.whriteQuestion.answerreference??>-->
							<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
						<#--</#if>-->
						<#-- 未判题不显示用户得分 -->
				        <#if paperItemDto.isCorrect!=-1>
		                	<#import "../userScore.ftl" as userScore_ftl>
			            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
		                </#if>
						<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
							<div class="js_lastdl">试题解析：${question.explan!}</div>
						<#--</#if>-->
					</div>
				</dd>
			<#else>
			    <#--作为大题-->
		        <dd class="oppClass jp-answer">
					<#--<#if dto.whriteQuestion.scorestandad??>-->
						<div class=" js_lastdl">评分标准：<span class="ft11 ftc1">${dto.whriteQuestion.scorestandad!}</span></div>
					<#--</#if> -->
					<#--<#if dto.whriteQuestion.answerreference??>-->
						<div class="js_lastdl">参考答案：<span class="ft11 ftc1">${dto.whriteQuestion.answerreference!}</span></div>
					<#--</#if>-->
					<#-- 未判题不显示用户得分 -->
			        <#if paperItemDto.isCorrect!=-1>
	                	<#import "../userScore.ftl" as userScore_ftl>
		            	<@userScore_ftl.userScore  paperItemDto=paperItemDto/>
	                </#if>
					<#--<#if dto.questionDto.question.explan?? && (dto.questionDto.question.explan?length gt 0)>-->
						<div class="js_lastdl">试题解析：${question.explan!}</div>
					<#--</#if>-->
				</dd>
		    </#if>
		</#if>
</dl>