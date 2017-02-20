package com.koolearn.cloud.exam.examcore.util;

import java.util.ArrayList;
import java.util.List;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
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
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.cloud.exam.examcore.question.template.BoxTemplate;
import com.koolearn.cloud.exam.examcore.question.template.CaculationTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ChoiceBlankTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ChoiceFillBlankTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ChoiceTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ClozeFillTemplate;
import com.koolearn.cloud.exam.examcore.question.template.CorrectionTemplate;
import com.koolearn.cloud.exam.examcore.question.template.EssayTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ReadTemplate;
import com.koolearn.cloud.exam.examcore.question.template.ShortTemplate;
import com.koolearn.cloud.exam.examcore.question.template.SortTemplate;
import com.koolearn.cloud.exam.examcore.question.template.SpokenTemplate;
import com.koolearn.cloud.exam.examcore.question.template.SubCorrectionTemplate;
import com.koolearn.cloud.exam.examcore.question.template.TableTemplate;
import com.koolearn.cloud.exam.examcore.question.template.WriteTemplate;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.util.CacheTools;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * 生成序号
 */
public class QuestionUtil {
	public static String getSimpleCode(int type){
		String code= "qtq";
		switch (type) {
		case Question.QUESTION_TYPE_DUOXUAN:
			code="duox";
			break;
		case Question.QUESTION_TYPE_DANXUAN:
			code="danx";
			break;
		case Question.QUESTION_TYPE_FILL_BLANK:
			code="tk";
			break;
		case Question.QUESTION_TYPE_FILL_CALCULATION:
			code="jstk";
			break;
		case Question.QUESTION_TYPE_READ:
			code="ydlj";
			break;
		case Question.QUESTION_TYPE_LISTEN:
			code="tl";
			break;
		case Question.QUESTION_TYPE_TABLE:
			code="bgjz";
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			code="gc";
			break;
		case Question.QUESTION_TYPE_CHOICE_BLANK:
			code="xztk";
			break;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			code="xzwxtk";
			break;
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			code="tkwxtk";
			break;
		case Question.QUESTION_TYPE_WHRITE:
			code="zw";
			break;
		case Question.QUESTION_TYPE_SORT:
			code="paixu";
			break;
		case Question.QUESTION_TYPE_SHORT:
			code="jd";
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			code="ky";
			break;
		default:
			break;
		}
		return code;
	}
	/**
	 * 获取子题答案集合
	 */
	public static QuestionViewDto getSubQuestionViewDto(IExamQuestionDto dto,QuestionViewDto questionViewDto){
		if(dto==null || questionViewDto==null)
			questionViewDto = new QuestionViewDto();
		int type = dto.getQuestionType();
		List<IExamQuestionDto> subItems = dto.getSubQuestions();
		if(subItems==null||subItems.size()==0){
			return questionViewDto;
		}
		List<QuestionViewDto> subViews = new ArrayList<QuestionViewDto>();
       String qno= StringUtils.isBlank(questionViewDto.getQuestionNo())?"-1":questionViewDto.getQuestionNo();
		int indexNum=Integer.parseInt(qno);
		int subIndex=1;
		switch(type){
		case Question.QUESTION_TYPE_DANXUAN:
		case Question.QUESTION_TYPE_DUOXUAN:
		case Question.QUESTION_TYPE_NORMAL_FILL_BLANK:
		case Question.QUESTION_TYPE_FILL_CALCULATION:
		case Question.QUESTION_TYPE_FILL_BLANK:
		case Question.QUESTION_TYPE_SHORT:
		case Question.QUESTION_TYPE_SPOKEN:
		case Question.QUESTION_TYPE_WHRITE:
			return questionViewDto;
		case Question.QUESTION_TYPE_READ:
		case Question.QUESTION_TYPE_LISTEN:
			for(IExamQuestionDto sub:subItems){
				QuestionViewDto subView = new  QuestionViewDto();
				subView.setQuestionNo(getSubNo(indexNum,subIndex++));
				subView.setViewType(questionViewDto.getViewType());
				subView.setExaming(questionViewDto.isExaming());
				subViews.add(subView);
			}
			questionViewDto.setSubDtos(subViews);
			return questionViewDto;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			for(IExamQuestionDto sub:subItems){
				QuestionViewDto subView = new  QuestionViewDto();
				subView.setQuestionNo(getSubNo(indexNum,subIndex++));
				subView.setViewType(questionViewDto.getViewType());
				subView.setExaming(questionViewDto.isExaming());
				subViews.add(subView);
			}
			questionViewDto.setSubDtos(subViews);
			return questionViewDto;
		case Question.QUESTION_TYPE_CHOICE_BLANK:
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			for(IExamQuestionDto sub:subItems){
				QuestionViewDto subView = new  QuestionViewDto();
				subView.setQuestionNo(getSubNo(indexNum,subIndex++));
				subView.setViewType(questionViewDto.getViewType());
				subView.setExaming(questionViewDto.isExaming());
				subViews.add(subView);
			}
			questionViewDto.setSubDtos(subViews);
			break;
		case Question.QUESTION_TYPE_TABLE:
			break;
		default:
			return questionViewDto;
		}
		return questionViewDto;
	}
    private static String getSubNo(int indexNum,int subIndex){
        if(indexNum==-1){
            return ""+subIndex;
        }
        return indexNum+"."+subIndex;
    }
	/**
	 * 转换用户答案
	 * @param userAnswer
	 * @return
	 */
	public static QuestionViewDto convertUserAnswer(IExamQuestionDto dto,String userAnswer){
		QuestionViewDto questionViewDto = new QuestionViewDto();
		questionViewDto = dto.getQuestionDto().convertUserAnswer(dto, userAnswer);
		return questionViewDto;
	}
	public static Integer getSchoolId(HttpServletRequest request){
		UserEntity loginUser = CacheTools.getCache(request.getSession().getId(), UserEntity.class);
		return 22222;
//		return loginUser.getSchoolId();
	}
	public static void main(String a[]){
		System.out.println("");
	}
	/**
	 * 返回主观题型
	 * @return
	 */
	public static List<Questiontype> getCorrQuestionType() {
		List<Questiontype> qtList = new ArrayList<Questiontype>();
		Questiontype t1 = new Questiontype(Question.QUESTION_TYPE_FILL_BLANK,"普通填空题");
		Questiontype t2 = new Questiontype(Question.QUESTION_TYPE_WHRITE,"作文题");
		Questiontype t3 = new Questiontype(Question.QUESTION_TYPE_SPOKEN,"口语题");
		Questiontype t4 = new Questiontype(Question.QUESTION_TYPE_SHORT,"简答题");
		Questiontype t5 = new Questiontype(Question.QUESTION_TYPE_SUB_CORRECTION,"改错题");
		qtList.add(t1);
		qtList.add(t2);
		qtList.add(t3);
		qtList.add(t4);
		qtList.add(t5);
		return qtList;
	}
	/**返回静态题型名称*/
	public static String getQuestionTypeName(int questionType) {
		String qtName = "";
		switch (questionType) {
		case Question.QUESTION_TYPE_DUOXUAN:
			qtName="普通多选题";
			break;
		case Question.QUESTION_TYPE_DANXUAN:
			qtName="普通单选题";
			break;
		case Question.QUESTION_TYPE_FILL_BLANK:
			qtName="普通填空题";
			break;
		case Question.QUESTION_TYPE_FILL_CALCULATION:
			qtName="计算填空题";
			break;
		case Question.QUESTION_TYPE_READ:
			qtName="阅读理解题";
			break;
		case Question.QUESTION_TYPE_LISTEN:
			qtName="听力题";
			break;
		case Question.QUESTION_TYPE_TABLE:
			qtName="表格题";
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			qtName="改错题";
			break;
		case Question.QUESTION_TYPE_CHOICE_BLANK:
			qtName="选择填空题";
			break;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			qtName="选择型完形填空题";
			break;
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			qtName="填空型完形填空题";
			break;
		case Question.QUESTION_TYPE_WHRITE:
			qtName="作文题";
			break;
		case Question.QUESTION_TYPE_SORT:
			qtName="拖拽排序题";
			break;
		case Question.QUESTION_TYPE_SHORT:
			qtName="简答题";
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			qtName="口语题";
			break;
		case Question.QUESTION_TYPE_DANXUAN_BOX:
			qtName="单选方框题";
			break;
		default:
			break;
		}
		return qtName;
	}
	/**
	 * 获取正确答案or参考答案
	 */
	public static String getAnswer(IExamQuestionDto dto){
		int type = dto.getQuestionType();
		String answer = "";
		switch(type){
		case Question.QUESTION_TYPE_DANXUAN:// 1多选、6单选
		case Question.QUESTION_TYPE_DUOXUAN:
			ChoiceQuestionDto choiceQuestionDto = (ChoiceQuestionDto)dto;
			for (int i = 0; i < choiceQuestionDto.getChoiceAnswers().size(); i++) {
				ChoiceAnswer q = choiceQuestionDto.getChoiceAnswers().get(i);
				if(q.getIsright()==1){
					answer = answer + TestUtil.transToLetter(q.getSequenceId())+" ";
				}
			}
			break;
        case Question.QUESTION_TYPE_FILL_BLANK:// 填空题 2
        	EssayQuestionDto essayQuestionDto = (EssayQuestionDto)dto;
        	answer = essayQuestionRightAnswer(essayQuestionDto);
            break;
        case Question.QUESTION_TYPE_SHORT://简答题 3  参考答案
        	ShortQuestionDto shortQuestionDto = (ShortQuestionDto)dto;
        	answer = shortQuestionDto.getShortQuestion().getAnswerreference();
            break;
        case Question.QUESTION_TYPE_WHRITE://写作题 4
        	WhriteQuestionDto whriteQuestionDto = (WhriteQuestionDto)dto;
        	answer = whriteQuestionDto.getWhriteQuestion().getAnswerreference();
            break;
        case Question.QUESTION_TYPE_READ://阅读理解题 7
        case Question.QUESTION_TYPE_LISTEN: //听力题 19
        	//没有答案信息
//            answer = ReadTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_CHOICE_FILL_BLANK://选择型完形填空题 15
        	//没有答案信息
//            answer = ChoiceFillBlankTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
            break;
        case Question.QUESTION_TYPE_CORRECTION: //改错题 106
        	//没有答案信息
//            answer = CorrectionTemplate.instance().outTemplate((ComplexCorrectionQuestionDto)dto, questionViewDto);
            break;
		case Question.QUESTION_TYPE_FILL_CALCULATION:
			//暂时没有此题型
//			answer = CaculationTemplate.instance().outTemplate((EssayQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			//暂时没有此题型
//			answer = SpokenTemplate.instance().outTemplate((SpokenQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_CHOICE_BLANK:
			//暂时没有此题型
//			answer = ChoiceBlankTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_TABLE:
			//暂时没有此题型
//			answer = TableTemplate.instance().outTemplate((MatrixQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_SUB_CORRECTION:
			CorrectionQuestionDto correctionQuestionDto = (CorrectionQuestionDto)dto;
			answer = correctionQuestionDto.getCorrectionQuestion().getClauseAnswer();
			break;
		case Question.QUESTION_TYPE_SORT:
			//暂时没有此题型
//			answer = SortTemplate.instance().outTemplate((ChoiceQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			//没有答案信息
//			ComplexQuestionDto complexQuestionDto = (ComplexQuestionDto)dto;
//			answer = ClozeFillTemplate.instance().outTemplate((ComplexQuestionDto)dto, questionViewDto);
			break;
		case Question.QUESTION_TYPE_DANXUAN_BOX:
//			answer = BoxTemplate.instance().outTemplate((ChoiceQuestionDto)dto, questionViewDto);
			break;
			//...其他题型
		}
		return answer;
	}
	
	public static String essayQuestionRightAnswer(EssayQuestionDto dto) {
		String rightAnswer="";
		List<FillblankAnswer> answers= dto.getFillblankAnswers();
		for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
	  	{	
		  	if(rightAnswer.equals("")){
		  		rightAnswer=answers.get(ansIndex).getAnswer2();
		  	}else{
				rightAnswer=rightAnswer+", "+answers.get(ansIndex).getAnswer2();
			}
	  	}
		return  rightAnswer;
	}
}
