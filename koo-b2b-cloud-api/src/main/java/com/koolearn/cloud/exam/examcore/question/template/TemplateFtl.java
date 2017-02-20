package com.koolearn.cloud.exam.examcore.question.template;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.StringEscapeUtils;

import com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public  class TemplateFtl {
	private static Configuration cfg=null;
	private Template template =null;
	public TemplateFtl(){
		if(cfg!=null){
			return ;
		}
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		cfg=new Configuration();
		String path=request.getSession().getServletContext().getRealPath("/")+ OnlyExamConstant.QUESTION_TEMPLATE_PATH;
        String p=  System.getProperty("cloud.root");
		path=path.replaceAll("%20", " ");
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			//设置字符集
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
		} catch (IOException e) {
			cfg=null;
			e.printStackTrace();
		}
	}
	
	public void setTemplateFile(String fileName) throws IOException{
		 template = cfg.getTemplate(fileName,"UTF-8");
	}
	protected String outTemplate(Map<String, Object> data) throws IOException, TemplateException{
		Writer out=null;
		out=new StringBuilderWriter();
		if(data.get("QuestionHelper")==null){
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModels = wrapper.getStaticModels();
			data.put("QuestionHelper", (TemplateHashModel) staticModels.get("com.koolearn.cloud.exam.examcore.util.QuestionHelper"));
		}
		template.process(data, out);
		return out.toString();
	}
	protected Map<String, Object> installMethods() throws TemplateModelException {
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		Map<String, Object> data=new HashMap<String, Object>();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel fileStatics =
		    (TemplateHashModel) staticModels.get("com.koolearn.cloud.exam.examcore.util.TestUtil");
		TemplateHashModel fileStatics2 =
				(TemplateHashModel) staticModels.get("com.koolearn.cloud.exam.examcore.question.entity.Question");
		TemplateHashModel fileStatics3 =
				(TemplateHashModel) staticModels.get("com.koolearn.cloud.exam.examcore.util.QuestionHelper");
		TemplateHashModel fileStatics4 =
				(TemplateHashModel) staticModels.get("java.net.URLEncoder");
		TemplateHashModel fileStatics5 =
			    (TemplateHashModel) staticModels.get("com.koolearn.cloud.exam.examcore.util.HtmlUtil");
		TemplateHashModel fileStatics6 = (TemplateHashModel) staticModels
				.get("com.koolearn.exam.util.MD5");
		TemplateHashModel fileStatics7 = (TemplateHashModel) staticModels
				.get("com.koolearn.cloud.exam.entity.OnlyExamConstant");
		TemplateHashModel fileStatics8 = (TemplateHashModel) staticModels
				.get("com.koolearn.cloud.exam.examcore.question.template.TemplateFtl");
		
		data.put("TestUtil", fileStatics);
		data.put("Question", fileStatics2);
		data.put("QuestionHelper", fileStatics3);
		data.put("URLEncoder", fileStatics4);
		data.put("HtmlUtil", fileStatics5);
		data.put("MD5", fileStatics6);
		data.put("Constant", fileStatics7);
		data.put("TemplateFtl", fileStatics8);
		return data;
	}
	/**
	 * 根据题型选择模板输出
	 * @param dto
	 * @param questionViewDto
	 * @return
	 * @throws java.io.IOException
	 * @throws freemarker.template.TemplateException
	 */
	public static String outHtml(IExamQuestionDto dto,QuestionViewDto questionViewDto) throws IOException, TemplateException{
		int type = dto.getQuestionType();
		String html = "";
		if(questionViewDto==null)
			questionViewDto = new QuestionViewDto();
		switch(type){
		case Question.QUESTION_TYPE_DANXUAN:// 1多选、6单选
		case Question.QUESTION_TYPE_DUOXUAN:
			html = ChoiceTemplate.instance().outTemplate((ChoiceQuestionDto)dto, questionViewDto);
			break;
        case Question.QUESTION_TYPE_FILL_BLANK:// 填空题 2
            html = EssayTemplate.instance().outTemplate((EssayQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_SHORT://简答题 3
            html = ShortTemplate.instance().outTemplate((ShortQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_WHRITE://写作题 4
            html = WriteTemplate.instance().outTemplate((WhriteQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_READ://阅读理解题 7
        case Question.QUESTION_TYPE_LISTEN: //听力题 19
            html = ReadTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_CHOICE_FILL_BLANK://选择型完形填空题 15
            html = ChoiceFillBlankTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_CORRECTION: //改错题 106
            html = CorrectionTemplate.instance().outTemplate((ComplexCorrectionQuestionDto)dto, questionViewDto);
            break;
		case Question.QUESTION_TYPE_FILL_CALCULATION:
//			html = CaculationTemplate.instance().outTemplate((EssayQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_SPOKEN:
//			html = SpokenTemplate.instance().outTemplate((SpokenQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_CHOICE_BLANK:
//			html = ChoiceBlankTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_TABLE:
			html = TableTemplate.instance().outTemplate((MatrixQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_SUB_CORRECTION:
			html = SubCorrectionTemplate.instance().outTemplate((CorrectionQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_SORT:
			html = SortTemplate.instance().outTemplate((ChoiceQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			html = ClozeFillTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_DANXUAN_BOX:
			html = BoxTemplate.instance().outTemplate((ChoiceQuestionDto)dto, questionViewDto);
			break;
			//...其他题型
		default:
			System.out.println(1);
			;
		}
		html=StringEscapeUtils.unescapeHtml(html);
		return html;
	}
	
}

