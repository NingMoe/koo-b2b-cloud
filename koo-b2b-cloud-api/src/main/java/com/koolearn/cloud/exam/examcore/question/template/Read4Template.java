package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.Map;
import com.koolearn.exam.testProcess.dto.TestProcessDto;
import freemarker.template.TemplateException;

public class Read4Template extends TemplateFtl {

	public static Read4Template instance() {
		Read4Template result = new Read4Template();
		return result;
	}

	public String outTemplate(TestProcessDto process, int index)
			throws IOException, TemplateException {
		String fileName = "questions/readQuestion4.ftl";
		setTemplateFile(fileName);
		Map<String, Object> data = installMethods();
		data.put("readQuestionDto", process.getPaperObjectList().get(index));
		data.put("i", index);
		data.put("process", process);
		data.put("paperItemDtoList", process.getPaperItemDtoList());
		String result = outTemplate(data);
		return result;
	}

}
