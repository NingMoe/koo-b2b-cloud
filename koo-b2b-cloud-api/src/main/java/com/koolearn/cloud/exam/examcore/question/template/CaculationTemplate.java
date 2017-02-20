package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;
/**
 * 计算题（填空）
 * @author yangzhenye
 *
 */
public class CaculationTemplate extends TemplateFtl {

	public static CaculationTemplate instance() {
		CaculationTemplate result = new CaculationTemplate();
		return result;
	}

	public String outTemplate(EssayQuestionDto dto,
			QuestionViewDto questionViewDto)throws  IOException, TemplateException {
		String fileName = "questions/caculationQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}

}
