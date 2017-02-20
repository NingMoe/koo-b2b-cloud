package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;
import freemarker.template.TemplateException;

public class ShortTemplate extends TemplateFtl {

	public static ShortTemplate instance() {
		ShortTemplate result = new ShortTemplate();
		return result;
	}

	public String outTemplate(ShortQuestionDto dto, QuestionViewDto questionViewDto)
			throws IOException, TemplateException {
		String fileName = "questions/shortQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", dto);
		data.put("paperItemDto", questionViewDto);
		String result = outTemplate(data);
		return result;
	}

}
