<#macro userScore  paperItemDto>
<div class="h30 js_lastdl">
您的得分： ${QuestionHelper.formatNumber("#.#",paperItemDto.userScore)} 分
</div>
</#macro>