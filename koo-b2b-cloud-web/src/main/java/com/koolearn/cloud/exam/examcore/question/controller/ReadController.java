package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.cloud.exam.examcore.question.service.ReadQuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.ResultDto;
import com.koolearn.cloud.util.CacheTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/question/base/read")
public class ReadController extends QuestionController {
	@Autowired
	@Qualifier("readQuestionService")
	private ReadQuestionService readQuestionService;
	private final String ATTR_READ_DTO="readQuestionDto";
	private  final String SESSION_ERRMSG_KEY="errMsg";
	private  final String SESSION_OBJ_KEY="myObject";
	private final String LIST_PAGE="redirect:/question/base/questionList";
	@RequestMapping("/index")
	public String index() throws Exception{
		String path="/examcore/question/q-question-all";
		Questiontype qt1 = new Questiontype(6,"普通单选");
		Questiontype qt2 = new Questiontype(1,"普通多选");
		Questiontype qt8 = new Questiontype(2,"普通填空");
		Questiontype qt3 = new Questiontype(21,"方框题");
		Questiontype qt4 = new Questiontype(12,"拖拽矩阵");
		Questiontype qt5 = new Questiontype(12,"表格矩阵");
		Questiontype qt6 = new Questiontype(3,"简答题");
		Questiontype qt7 = new Questiontype(16,"计算填空");
		List<Questiontype> qlist = new ArrayList<Questiontype>();
		qlist.add(qt1);
		qlist.add(qt2);
		qlist.add(qt3);
		qlist.add(qt4);
		qlist.add(qt5);
		qlist.add(qt6);
		qlist.add(qt7);
		qlist.add(qt8);
		setAttribute("qtList",qlist);
		//修改 另存
		if(getAttribute(ATTR_READ_DTO)!=null){
			toModifyIndex((IExamQuestionDto)getAttribute(ATTR_READ_DTO));
			return path;
		}
		String questionId= getParamter("questionId");
		if(StringUtils.isNotEmpty(questionId)&&!questionId.equals("0")){//前期保存
			ComplexQuestionDto readQuestionDto=readQuestionService.getReadByQuestionId(Integer.parseInt(questionId));
			setAttribute("readQuestionDto", readQuestionDto);
			setAttribute("questionTypeTab", OnlyExamConstant.QUESTION_TAG_SHOW);
			return toModifyIndex(readQuestionDto);
			//return path;
		}
		
		//进入添加页面 保存出错页面
		String e=(String)getSessionObject(SESSION_ERRMSG_KEY);
//		ResultDto result=new ResultDto();
		if(e!=null){
			removeSessionObject(SESSION_ERRMSG_KEY);
			setAttribute("errMsg", e);
			if(getSessionObject(SESSION_OBJ_KEY)!=null){
				setAttribute("readQuestionDto", getSessionObject(SESSION_OBJ_KEY));
				removeSessionObject(SESSION_OBJ_KEY);
			}
		}
		return path;
	}
	@RequestMapping("/delSub/{questionId}/{typeId}")
	public String delSub(@PathVariable("questionId")int questionId,@PathVariable("typeId")int typeId,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		ResultDto resultDto=new ResultDto();
		try {
			readQuestionService.deleteSubItem(questionId,typeId);
		} catch (Exception e) {
			e.printStackTrace();
			resultDto.fail();
			resultDto.setMessage(e.getMessage());
			printWriterAjax(response, resultDto);
			return null;
		}
		resultDto.success();
		resultDto.setMessage("OK");
		printWriterAjax(response, resultDto);
		return null;
		
	}
	@RequestMapping("/save")
	public String save(ComplexQuestionDto readQuestionDto,HttpServletRequest request,ModelMap modelMap,HttpServletResponse response){
		preSave(readQuestionDto,request);
		int questionId=readQuestionDto.getQuestionDto().getQuestion().getId();
		int new_id=questionId;
		int[] ids=new int[2];
		try {
			createQuestionCode(modelMap, readQuestionDto, response, request);
			ids=readQuestionService.saveOrUpdateRead(readQuestionDto);
		} catch (Exception e) {
			e.printStackTrace();
			if(questionId==0){
				ResultDto resultDto=new ResultDto();
				resultDto.fail();
				resultDto.setMessage(e.getMessage());
				printWriterAjax(response, resultDto);
				return null;
			}else{
				setSessionObject(SESSION_ERRMSG_KEY,e.getMessage());
				setSessionObject(SESSION_OBJ_KEY, readQuestionDto);
			}
		}
		if(questionId==0){
			ResultDto resultDto=new ResultDto();
			resultDto.success();
			resultDto.setMessage(ids[0]+","+ids[1]+","+readQuestionDto.getQuestionDto().getQuestion().getCode());
			printWriterAjax(response, resultDto);
			return null;
		}
		if(getSessionObject(SESSION_ERRMSG_KEY)==null){
			setSessionObject(SESSION_ERRMSG_KEY,"保存成功");
			//响应跳转到其他页面
			removeSessionObject(SESSION_ERRMSG_KEY);
			CacheTools.delCache("bakQue_" + questionId);
			String questionTypeTab = request.getParameter("questionTypeTab");
			if(StringUtils.isNotBlank(questionTypeTab)
					&&Integer.parseInt(questionTypeTab)==OnlyExamConstant.QUESTION_TAG_SHOW){
				//试题增加
				return toAddIndexUrl(readQuestionDto.getQuestionDto());
			}
			return LIST_PAGE;
		}
		
		String redirctUrl="forward:/question/base/read/index";
		return redirctUrl;
	}
	/**
	 * 保存之前做的处理,在预览时候也使用
	 * @param
	 * @param request
	 */
	private void preSave(ComplexQuestionDto readQuestionDto, HttpServletRequest request) {
		List<String> contents=new ArrayList<String>();
		String[] aa=readQuestionDto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		Question question=readQuestionDto.getQuestionDto().getQuestion();
		for(int i=0;i<contents.size();i++){
			if(i==contents.size()-1){
				readQuestionDto.getComplexQuestion().setTopic(request.getParameter(contents.get(i)));
				continue;
			}
			QuestionAttach attach=new QuestionAttach();
			attach.setContent(request.getParameter(contents.get(i)));
			attach.setSequenceId(i-1);
			QuestionDto questionDto= readQuestionDto.getQuestionDto();
			if(questionDto.getQuestionAttachs()==null){
				questionDto.setQuestionAttachs(new ArrayList<QuestionAttach>());
			}
			questionDto.getQuestionAttachs().add(attach);
		}
		//id
		String strId=getParamter("questionDto_question_id");
		if(org.apache.commons.lang.StringUtils.isNotEmpty(strId)){
			int id=Integer.parseInt(strId);
			readQuestionDto.getQuestionDto().getQuestion().setId(id);
		}
		//添加当前登录用户信息
		question.setLastUpdateBy(getLoginUserName());
		question.setLastUpdateDate(new Date(System.currentTimeMillis()));
		if(question.getId()==0){
			question.setCreateBy(getLoginUserName());
			question.setCreateDate(new Date(System.currentTimeMillis()));
		}
		readQuestionDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_READ);
		
		String str=getParamter("subItemOrder");
		readQuestionDto.setSubItemOrderStr(str);
	}
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/modifyIndex")
	public String modifyIndex(HttpServletRequest request){
		int questionId=(Integer)getAttribute("questionId");
		int questionType=(Integer)getAttribute("questionType");
		int saveType=(Integer)getAttribute("saveType");
		int responseType=(Integer)getAttribute("responseType");
		int teId=(Integer)getAttribute("te_id");
		
		ComplexQuestionDto readQuestionDto=null;
		try {
			readQuestionDto = readQuestionService.getReadByQuestionId(questionId);
//			readQuestionDto = (ComplexQuestionDto)questionBaseService.setQuestionExt(readQuestionDto, QuestionUtil.getSchoolId(request));
			Integer bakQue = CacheTools.getCache("bakQue_"+questionId, Integer.class);
			if(saveType != ConstantTe.QUESTION_SAVETYPE_SAVE && bakQue==null 
					&& readQuestionDto.getQuestionDto().getQuestion().getStatus()==Question.QUESTION_STATUS_AUDIT){
				CacheTools.addCache("bakQue_"+questionId, 1);
				saveType = ConstantTe.QUESTION_SAVETYPE_SAVEAS;
				//保存阅读理解副本
				int id[] = readQuestionService.backReadQuestion(readQuestionDto);
				readQuestionDto = readQuestionService.getReadByQuestionId(id[0]);
			}
			
			readQuestionDto.getQuestionDto().setSaveType(saveType);
			readQuestionDto.getQuestionDto().setResponseType(responseType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setAttribute("readQuestionDto", readQuestionDto);
		toModifyIndex(readQuestionDto);
		return "forward:/question/base/read/index";
	}
	/**
	 * 大题预览
	 * @param step
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/preview/{step}")
	public String preview( ComplexQuestionDto readQuestionDto,@PathVariable("step") int step,HttpServletRequest request, ModelMap modelMap) throws Exception{
		setAttribute("step", step);
		if(step==1){
			preSave(readQuestionDto, request);
			int id=readQuestionDto.getQuestionDto().getQuestion().getId();
			if(id!=0){
				 List<IExamQuestionDto> list=readQuestionService.getReadByQuestionId(id).getSubItems();
				 readQuestionDto.setSubItems(list);
			}
			setAttribute("dto", readQuestionDto);
			setAttribute("questionType", readQuestionDto.getQuestionDto().getQuestion().getQuestionTypeId());
		}
		
		return "/examcore/question/read/preview";
	}
}
