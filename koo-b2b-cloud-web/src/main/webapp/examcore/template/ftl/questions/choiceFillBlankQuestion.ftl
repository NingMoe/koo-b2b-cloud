<#-- 
<%@page import="com.koolearn.exam.util.HtmlUtil"%>
-->
<#-- 
传递过来的数据有
processList.get(i);
paperItemDtoList
i

-->
<#assign questionDto=dto.questionDto >
<#assign question=questionDto.question >
<#if questionDto.questionAttachs??>
<#assign attachs=questionDto.questionAttachs > 
</#if>
<#assign tagTh=0>
	<dl class="st"><dt  class="nobold "><a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
	<span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span>
	</dt></dl>
<#if  (attachs??) && (attachs?size>0)>
<#-- 选择型完型 -->
	<#list attachs as attach>
		<div class="pd1m " <#if tagTh==0><#assign tagTh=1> teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  taga='${dto.questionDto.questionTagByLevel2.tagId?c}' tagb='${dto.questionDto.questionTagByLevel3.tagId?c}'</#if>>
			<div class=" stp">${attach.content}</div>
		</div>			
	</#list>
</#if>
<div class="pd1m " qq="choiceFillBlankQuestion.ftl" questionType="${dto.questionType}"<#if tagTh==0><#assign tagTh=1> teId='${question.teId?c}' questionId='${question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}"  </#if>>
	<div class=" stp js_score">
	<#--if (paperItemDtoList??) && (paperItemDtoList?size == (i+1)) >
			${dto.complexQuestion.topic}
	<#else>
		${HtmlUtil.replaceQuestionNo4Content(dto.complexQuestion.topic,paperItemDtoList[i+1].questionNo)}
	</#if-->
	${HtmlUtil.replaceQuestionNo4Content(dto.complexQuestion.topic,1)}
</div>
	<#if questionDto.question.questionTip?? && (questionDto.question.questionTip?length>0) >
	  <p class="ft3">${questionDto.question.questionTip}</p>
	</#if>
	<#if dto.subQuestions?? && dto.subQuestions?size gt 0>
		<#assign t=1>
		<#list dto.subQuestions as examQuestionDto>
			${TemplateFtl.outHtml(examQuestionDto, paperItemDto.subDtos[examQuestionDto_index])}
		</#list>
	</#if>
</div>