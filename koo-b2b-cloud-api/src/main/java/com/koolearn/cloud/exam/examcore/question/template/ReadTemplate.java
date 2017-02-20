package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;

import freemarker.template.TemplateException;

public class ReadTemplate extends TemplateFtl {

	public static ReadTemplate instance() {
		ReadTemplate result = new ReadTemplate();
		return result;
	}
	
	public String outTemplate(ComplexQuestionDto dto, QuestionViewDto questionViewDto)
			throws IOException, TemplateException {
		String fileName = "questions/readQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("readQuestionDto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}
}
