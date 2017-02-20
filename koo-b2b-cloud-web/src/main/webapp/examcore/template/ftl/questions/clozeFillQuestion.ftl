<#-- 填空完形填空---小题模版是：essayQuestion.ftl
dto
<#import "../mark.ftl" as mark_ftl>
-->

<#assign questionDto=dto.questionDto>
<#assign question=questionDto.question>
<#if questionDto.questionAttachs??>
<#assign attachs = questionDto.questionAttachs>
</#if>
<#assign essayQuestionDTOs = dto.essayQuestionDTOs>
<#assign rightAnswer = "">
<#assign iExamQuestionDto = dto>
	<dl class="st tm_zt_${question.teId?c}" qq="clozeFillQuestion" questionType="${dto.questionType}" ty="0" teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}" >
			<dt class="nobold">
            <#if paperItemDto.questionNo!="">
                <a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
	           <span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span>
        </#if>
	<div class="stp js_score">
	<#if (attachs?? && attachs?size>0)>
	<#list attachs as attach >
		${attach.content}
	</#list>
	</#if>	
		<#if dto.complexQuestion.topic??>${dto.complexQuestion.topic}</#if>【本题：<span class="jp-score-view">${paperItemDto.score!'0'}</span>分】
	<#if (question.questionTip?? && question.questionTip?length>0)>
		<p class="ft3">${question.questionTip}</p>
	 </#if>
	</div> 	
	
		<#if dto.subQuestions?? && dto.subQuestions?size gt 0>
		<#assign t=1>
		<#list dto.subQuestions as examQuestionDto>
			${TemplateFtl.outHtml(examQuestionDto, paperItemDto.subDtos[examQuestionDto_index])}
		</#list>
		</#if>
</dl>