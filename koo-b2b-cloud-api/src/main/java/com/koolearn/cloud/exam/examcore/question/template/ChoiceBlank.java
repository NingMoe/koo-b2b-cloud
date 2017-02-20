package com.koolearn.cloud.exam.examcore.question.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koolearn.exam.testProcess.dto.TestProcessDto;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * @author Du HongLin 2014-06-12 18:04
 * 
 */
public class ChoiceBlank extends TemplateFtl {

	public static ChoiceBlank instance() {
		ChoiceBlank result = new ChoiceBlank();
		return result;
	}


	public String outTemplate(TestProcessDto process, int index)
			throws IOException, TemplateException {
		String fileName = "questions/choiceBlank.ftl";
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
