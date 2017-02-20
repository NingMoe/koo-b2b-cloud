package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto;
import freemarker.template.TemplateException;

public class SpokenTemplate extends TemplateFtl {
	private String bastPath;

	public static SpokenTemplate instance() {
		SpokenTemplate t = new SpokenTemplate();
		return t;
	}

	public String outTemplate(SpokenQuestionDto dto, QuestionViewDto questionViewDto)
			throws IOException, TemplateException {
		String fileName = "questions/spokenQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}
}
