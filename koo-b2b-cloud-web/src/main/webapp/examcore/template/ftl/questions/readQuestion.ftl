<#assign myattaches=readQuestionDto.questionDto.questionAttachs > 
<#assign question=readQuestionDto.questionDto.question >
<#--阅读理解a  子题模版：choiceQuestion.ftl-->
<div class="tit1" questionType="${question.questionTypeId}" teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  >
	<dl class="st"><dt  class="nobold "> <#if paperItemDto.questionNo!=""><a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
	<span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span></#if>
    <#if myattaches?? && myattaches?size gt 0>
        <#list myattaches as myattache >
            <div class="ft5">
            ${myattache.content}
            </div>
        </#list>
    </#if>
    ${readQuestionDto.complexQuestion.topic!''}【本题：<span class="jp-score-view">${paperItemDto.score!'0'}</span>分】
	</dt>
	<#--<dd class="ft5 js_score">
	${readQuestionDto.complexQuestion.topic!''}【本题：<span class="jp-score-view">${paperItemDto.score!'0'}</span>分】
	</dd>-->
    <dd class="oppClass">
	<div class="ft3 js_lastdl">
	<#if (paperItemDto.viewType==1||paperItemDto.viewType==2) && question.questionTypeId==7>
		<#if readQuestionDto.complexQuestion.translate?? &&((readQuestionDto.complexQuestion.translate?trim)?length gt 0)>
		原文翻译：${readQuestionDto.complexQuestion.translate}
		</#if>
	</#if>
	
	<#if (paperItemDto.viewType==1||paperItemDto.viewType==2) && question.questionTypeId==19>
		<#if readQuestionDto.questionDto.question.topicExt??>
			<#assign translate=readQuestionDto.questionDto.question.topicExt>
			<#if translate?? &&((translate?trim)?length gt 0)>
				${"听力原文："+translate+""}
		 	</#if>
		</#if>
	</#if>
	</div>
    </dd>
    </dl>
	<#if readQuestionDto.subItems?? && readQuestionDto.subItems?size gt 0>
		<#assign t=1>
		<#list readQuestionDto.subItems as examQuestionDto>
			${TemplateFtl.outHtml(examQuestionDto, paperItemDto.subDtos[examQuestionDto_index])}
		</#list>
	</#if>
</div>