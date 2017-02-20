package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;
import com.koolearn.exam.testProcess.dto.TestProcessDto;
import freemarker.template.TemplateException;

public class PointTemplate extends TemplateFtl {

	public static PointTemplate instance() {
		PointTemplate result = new PointTemplate();
		return result;
	}

	public String outTemplate(TestProcessDto process, int index)
			throws IOException, TemplateException {
		String fileName = "questions/pointQuestion.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("dto", process.getPaperObjectList().get(index));
		data.put("i", index);
		data.put("process", process);
		data.put("paperItemDtoList", process.getPaperItemDtoList());
		String result = outTemplate(data);
		return result;
	}

}
