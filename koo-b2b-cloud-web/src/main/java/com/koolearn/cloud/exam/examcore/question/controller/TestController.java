package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.examcore.question.service.QuestionConvertService;
import com.koolearn.exam.syncQuestion.service.QuestionSyncService;
import com.koolearn.framework.common.utils.spring.SpringContextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 试题
 * @author yangzhenye
 */
@Controller
@RequestMapping("/question/view")
public class TestController extends QuestionBaseController {
	private static final Logger logger = Logger.getLogger(TestController.class);
//	@Autowired
//	protected QuestionSearchService questionSearchService;
	/**
	 * 题目结构html
	 */
	@ResponseBody
	@RequestMapping("/a")
	public Object body(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
//		try {
//			String html = "";
//			QuestionFilter questionFilter = new QuestionFilter();
//			questionFilter.setRandomCount(3);
//			questionFilter.setTag3(102571);
//			
//			return questionSearchService.queryRandomQuestion(questionFilter);
//			//return html;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//		}
		try {
			QuestionSyncService questionSyncService = (QuestionSyncService) SpringContextUtils.getBean("questionSyncServiceClient");
			QuestionConvertService questionConvertService = (QuestionConvertService)SpringContextUtils.getBean("questionConvertService");
			com.koolearn.exam.base.dto.IExamQuestionDto dto = 
					(com.koolearn.exam.base.dto.IExamQuestionDto) questionSyncService.
					getExamQuestionNoCache(19, 268224);  //266981
//			questionConvertService.saveQuestion(dto,30);
//			QuestionSearchService questionSearchService = (QuestionSearchService)SpringContextUtils.getBean("questionSearchService");
//			QuestionFilter filter = new QuestionFilter();
//			questionSearchService.queryRandomQuestion(filter);
			//questionService.aa();
//			String questionId = getParamter("questionId");
//			Question question = questionBaseService.getQuestionById(new Integer(questionId));
//			IExamQuestionDto dto = null;
//			dto = questionBaseService.getExamQuestionNoCache( 19,
//					new Integer(961077));
			String html = "";
//			html = TemplateFtl.outHtml(dto, null);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return "";
	}
}
