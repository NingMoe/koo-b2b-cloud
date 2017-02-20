<#--改错题correctionQuestion】106   子题模版：
dto
i
-->
<#assign questionType = dto.questionType>
<#assign questionDto = dto.questionDto>
<#if questionDto.questionAttachs??>
<#assign attachs = questionDto.questionAttachs>
</#if>
	<dl class="st" qq="correctionQuestion.ftl${paperItemDto.viewType}"><dt  class="nobold "> <#if paperItemDto.questionNo!=""><a id="card${paperItemDto.questionNo}" name="card${paperItemDto.questionNo}"></a>
	<span class="num" id='${paperItemDto.questionNo}'>${paperItemDto.questionNo}</span></#if>
	</dt></dl>
	<div class="tit1 pd1 " questionType="${dto.questionType}" teId='${questionDto.question.teId?c}' questionId='${questionDto.question.id?c}' structureId='${paperItemDto.structureId?c}' score="${paperItemDto.score!'0'}" >
	<div class="ZW st " id="topic">
		<div class='js_score'>
		<#if attachs?? && attachs?size gt 0 >
		<#list attachs as attach>
		${attach.content}
		</#list>
		</#if>
		<#if dto.complexQuestion.topic??>
			${dto.complexQuestion.topic?replace("<span class=\"hot\">[\\[\\]]</span>","")?replace("class=\"tb4\"","")}【本题：<span class="jp-score-view">${paperItemDto.score!'0'}</span>分】
		</#if>
	</div>
	</div>
		<input type="hidden" id="ZW_Content" title="保存修改后的批改内容" />
		<#if dto.questionDto.question.questionTip?? && dto.questionDto.question.questionTip!="">
    	<p class="ft3 ft4">${dto.questionDto.question.questionTip}</p>
		<#else>
		<p class="ft3 ft4">请选择要修改的单词或词组，修改或删除</p>
		</#if>
	</div>
	
	<#-- 子题 -->
	<#list dto.subQuestions as dto2>
	${TemplateFtl.outHtml(dto2,paperItemDto.subDtos[dto2_index])}
	</#list>
	
	<#if paperItemDto.viewType==1||paperItemDto.viewType==2||paperItemDto.viewType==5>
		<#--<#if questionDto.question.explan?? && questionDto.question.explan!="">-->
		<dd class="oppClass  p-btns jp-answer jp-hid">
			<div class="pd1m js_lastdl oppClass">试题解析：${questionDto.question.explan}</div>
		</dd>
		<#--</#if>-->
	<#elseif paperItemDto.viewType==4>
		<div class="pd1m js_lastdl">试题解析：${questionDto.question.explan}</div>
	<#else>
	<!--添加修改标注a-->
	<div class="bztit bztit2" id="setBZ" style="display:none;z-index:150;">
	    <div class="bzticon" style="display:none;">
	    </div>
	    <div class="bztitbx">
	    <table class="tab3__zw mb10">
	        <tr>
	            <td>
	                <input type="radio" name="radioBZ" id="radio1_1">
	                <label for="radio1_1" data-tit="修改">
	                    	修改为
	                </label>
	                <input type="text" class="ipt editipt" style="width:200px" />
	            </td>
	        </tr>
	        <tr>
	            <td>
	                <input type="radio" name="radioBZ" id="radio1_2">
	                <label for="radio1_2" data-tit="删除">
	                  	 删除
	                </label>
	            </td>
	        </tr>
	    </table>
	        <div class="ta_r">
	            <span class="btn2 btn1_l"><a href="javascript:void(0)">确 定</a></span>
	            <span class="btn2 btn1_b"><a href="javascript:void(0)">取 消</a></span>
	        </div>
	    </div>
	</div>
	<!--end添加修改标注-->
	</#if>