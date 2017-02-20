package com.koolearn.cloud.exam.examcore.question.controller;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.question.dto.*;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.cloud.exam.examcore.question.entity.TagsRelaQts;
import com.koolearn.cloud.exam.examcore.question.service.*;
import com.koolearn.cloud.exam.examcore.util.AjaxMessageDto;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.util.CacheTools;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 
/**
 * 试题基础controller
 * @author yangzhenye
 */
public class QuestionBaseController extends BaseController {
	private static final Logger logger = Logger.getLogger(QuestionBaseController.class);
	 
	@Autowired
	protected ReadQuestionService readQuestionService;
	@Autowired
	protected QuestionService questionService;
	@Autowired
	protected QuestionBaseService questionBaseService;
	@Autowired
	protected ChoiceQuestionService choiceQuestionService;

	@Autowired
	protected TagObjectService tagObjectService;
	@Autowired
	protected QuestiontypeService questiontypeService;

	/**
	 * 添加大题目后跳转到本题型的增加页
	 */
	protected String toAddIndexUrl(QuestionDto dto) {
		String url = "redirect:/question/base/toAddIndex";
		if(dto!=null && dto.getQuestion()!=null
				){
			dto.getQuestion().getQuestionTypeId();
			url+="?questionTypeId="+dto.getQuestion().getQuestionTypeId();
		}
		return url;
	}
	/**
	 * 生成题编码
	 */
	protected String createQuestionCode(ModelMap modelMap,IExamQuestionDto questionDto,
			HttpServletResponse response, HttpServletRequest request) throws Exception{
		
		try {
			//设置修改类型，修改或另存
			setSaveType(request, questionDto);
			if(questionDto.getQuestionDto().getQuestion().getId()!=0){
				return null;
			}
			if(questionDto.getQuestionDto().getQuestion().getTeId()==0){
				//createQuestionCode(modelMap, choiceQuestionDto, response, request);
			}else{
				//子题
				String pCode = "";
				Question q = questionBaseService.getQuestionById(questionDto.getQuestionDto().getQuestion().getTeId());
				List<Integer> qIds = questionBaseService.getQuestionIdsByTeIdRepository(q.getId());
				//编号唯一
				boolean unic = false;
				int i=1;
				while(!unic){
					pCode = q.getCode()+"_"+(qIds.size()+i);
					unic = choiceQuestionService.checkUniqueCode(null, pCode);
					i++;
				}
				questionDto.getQuestionDto().getQuestion().setCode(pCode);
				return pCode;
			}
			return "";
		} catch ( Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw e;
		}
	}
	/**
	 * 从页面获取试题的多个提干数据，通过attachs返回
	 * @return 主提干 topic
	 */
	protected String getQuestionTopic(ModelMap modelMap,IExamQuestionDto readQuestionDto,
			HttpServletResponse response, HttpServletRequest request){
		String topic = "";
		List<String> contents=new ArrayList<String>();
		String[] aa=readQuestionDto.getQuestionDto().getTextarea_param().split(",");
		for(int i=0;i<aa.length;i++){
			contents.add(aa[i]);
		}
		for(int i=0;i<contents.size();i++){
			if(i==contents.size()-1){
				topic = request.getParameter(contents.get(i));
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
		return topic;
	}
	 
	private String getQuestionTypeHtml(List<Questiontype> qlist,int qTypeId){
		StringBuffer sb = new StringBuffer();
		for(Questiontype qtype:qlist){
			sb.append("<span onclick='goNew(");
			sb.append(qtype.getId());
			sb.append(")' id='t_");
			sb.append(qtype.getId());
			sb.append("' class='span_cur ");
			if(qtype.getId()==qTypeId){
				sb.append("current");
			}
			sb.append("' style=\"display:none;\"> ");
			sb.append(qtype.getName());
			sb.append(" </span>");
		}
		return sb.toString();
	}
	protected String getQuestionTypesHtml(Integer tagId,int questionTypeId){
		StringBuffer sb = new StringBuffer();
		sb.append("");
		try {
			TagsRelaQts tagsRelaQts = tagObjectService.searchByTag(Integer.valueOf(tagId));
			if(tagsRelaQts!=null && StringUtils.isNotBlank(tagsRelaQts.getQts())){
				String[] types = tagsRelaQts.getQts().split(",");
				for(int i=0;i<types.length;i++){
					String type = types[i];
					if(questionTypeId==0){
						questionTypeId = Integer.parseInt(type);
						setAttribute("questionTypeId", questionTypeId);
					}
					String qName = questiontypeService.findItemName(new Integer(type));
					
					sb.append("<span onclick='goNew(");
					sb.append(type);
					sb.append(")' id='t_");
					sb.append(type);
					sb.append("' class='span_cur ");
					if(questionTypeId==Integer.parseInt(type)){
						sb.append("current");
						setAttribute("questionTypeId", questionTypeId);
					}
					sb.append("' > ");
					sb.append(qName);
					sb.append(" </span>");
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	private String siniType(int type){
		String typeStr = ""+type;
		switch(type){
		case 6:typeStr="danx";break;
		}
		
		return typeStr;
	}
	/**
	 * 将结果写到页面内容上，作为Ajax请求的结果
	 * TODO
	 * @param response
	 * @param message
	 */
	protected void printWriterAjax(HttpServletResponse response, String message){
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter pwriter = response.getWriter();
			pwriter.print(message);
			pwriter.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * print  对象为json
	 * @param response
	 * @param
	 */
	protected void printWriterAjax(HttpServletResponse response, Object obj){
		printWriterAjax(response,JSON.toJSONString(obj));
	}
	/**
	 * print  成功对象为json
	 * @param response
	 * @param
	 */
	protected void ajaxSuccessAjax(HttpServletResponse response, String msg){
		AjaxMessageDto dto = new AjaxMessageDto();
		dto.setSuccess(true);
		dto.setMessageInfo(msg);
		printWriterAjax(response,dto);
	}
	protected void ajaxErrorAjax(HttpServletResponse response, String msg){
		AjaxMessageDto dto = new AjaxMessageDto();
		dto.setSuccess(false);
		dto.setMessageInfo(msg);
		printWriterAjax(response,dto);
	}
	protected String getLoginUserName(){
		return "";
	}
	protected User getLoginUser(){
        User loginUser = CacheTools.getCache(OnlyExamConstant.LOGIN_REDIS_PREFIX + getHttpRequest().getSession().getId(), User.class);
		return loginUser;
	}
	@ModelAttribute("loginUser")
	public User loginUser(){
		return getLoginUser();
	}
	public static String nextSeq(){
		Integer seq = CacheTools.getCache("question_seq_", Integer.class);
		if(seq==null||seq==9999){
			seq=0;
		}
		seq++;
		CacheTools.addCache("question_seq_", seq);
		String seqS = seq+"";
		int difBit = 4-seqS.length();
		String preFix = "";
        for (int i=0;i<difBit;i++){
            preFix = preFix+"0";
        }
		return preFix+seq;
	}
	
	protected void dealErrorOne(QuestionViewDto viewDto, IExamQuestionDto dto,
			HttpServletRequest request) {
		String param="errorOne";
		String value=getParamter(param);
		if(StringUtils.isEmpty(value)||!value.equalsIgnoreCase("1")){
			return;
		}
		switch (dto.getQuestionType()) {
			case Question.QUESTION_TYPE_READ:
			case Question.QUESTION_TYPE_LISTEN:
				ComplexQuestionDto cDto=(ComplexQuestionDto)dto;
				List<IExamQuestionDto> dtos=cDto.getSubQuestions();
				Iterator<IExamQuestionDto> iterator=dtos.iterator();
				viewDto=QuestionUtil.getSubQuestionViewDto(dto, viewDto);
				Iterator<QuestionViewDto> sViewIterator=viewDto.getSubDtos().iterator();
				QuestionViewDto sViewDto;
				IExamQuestionDto sDto;
				while(iterator.hasNext()){
					sDto=iterator.next();
					sViewDto=sViewIterator.next();
					if(!sDto.isSubjectived()){
						iterator.remove();
						sViewIterator.remove();
					}
				}
				break;
	
			default:
				break;
		}
				
	}
	/**
	 * 清除批改题中其他子题
	 * @param viewDto
	 * @param dto
	 * @param questionId
	 */
	protected void dealPigai(QuestionViewDto viewDto, IExamQuestionDto dto,
			int questionId) {
		int i=1;
		dto.getQuestionDto().getQuestion().setSequenceId(i);
		switch (dto.getQuestionType()) {
		case Question.QUESTION_TYPE_READ:
		case Question.QUESTION_TYPE_LISTEN:
			ComplexQuestionDto cDto=(ComplexQuestionDto)dto;
			List<IExamQuestionDto> dtos=cDto.getSubQuestions();
			Iterator<IExamQuestionDto> iterator=dtos.iterator();
			IExamQuestionDto sDto;
			while(iterator.hasNext()){
				sDto=iterator.next();
				if(sDto.getQuestionDto().getQuestion().getId()!=questionId){
					iterator.remove();
				}else{
					sDto.getQuestionDto().getQuestion().setSequenceId(i);
				}
				i++;
			}
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			ComplexCorrectionQuestionDto cDto2=(ComplexCorrectionQuestionDto)dto;
			dtos=cDto2.getSubQuestions();
			iterator=dtos.iterator();
			List<CorrectionQuestionDto> tL = new ArrayList<CorrectionQuestionDto>();
			while(iterator.hasNext()){
				sDto=iterator.next();
				if(sDto.getQuestionDto().getQuestion().getId()==questionId){
					sDto.getQuestionDto().getQuestion().setSequenceId(i);
					tL.add((CorrectionQuestionDto)sDto);
				}
				i++;
			}
			cDto2.setCorrectionQuestionDtos(tL);
			break;
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			ComplexQuestionDto cDto3=(ComplexQuestionDto)dto;
			dtos=cDto3.getSubQuestions();
			iterator=dtos.iterator();
			List<EssayQuestionDto> eL = new ArrayList<EssayQuestionDto>();
			while(iterator.hasNext()){
				sDto=iterator.next();
				if(sDto.getQuestionDto().getQuestion().getId()==questionId){
					EssayQuestionDto ed = (EssayQuestionDto)sDto;
					sDto.getQuestionDto().getQuestion().setSequenceId(i);
					eL.add(ed);
				}
				i++;
			}
			cDto3.setEssayQuestionDTOs(eL);
			break;
			
		default:
			break;
		}
		
	}
	/**
	 * 题目修改入页
	 */
	public void setSaveType(HttpServletRequest request,IExamQuestionDto dto){
		String saveType = getParamter("saveTypeE");
		//dto.getQuestionDto().setSaveType(new Integer(saveType));
	}
	/**
	 * 子试题关闭窗口
	 * @return
	 */
	protected String printClose() {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("try{");
		sb.append("window.opener.readReload()");
		sb.append("}catch(e){window.close();}");
		sb.append("window.close();");
		sb.append("</script>");
		sb.append("");
		return sb.toString();
	}
}
