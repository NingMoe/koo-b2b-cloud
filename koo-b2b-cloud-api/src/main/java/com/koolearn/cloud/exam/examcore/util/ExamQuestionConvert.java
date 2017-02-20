package com.koolearn.cloud.exam.examcore.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.koolearn.exam.base.dto.IExamQuestionDto;
import com.koolearn.exam.choice.dto.JudgeQuestionDTO;
import com.koolearn.exam.essayquestion.dto.EssayQuestionDTO;
import com.koolearn.exam.essayquestion.entity.FillblankAnswer;
import com.koolearn.exam.label.entity.TagObject;
import com.koolearn.exam.question.dto.ChoiceQuestionDto;
import com.koolearn.exam.question.dto.ComplexCorrectionQuestionDto;
import com.koolearn.exam.question.dto.ComplexQuestionDto;
import com.koolearn.exam.question.dto.CorrectionQuestionDto;
import com.koolearn.exam.question.dto.MatrixQuestionDto;
import com.koolearn.exam.question.dto.QuestionDto;
import com.koolearn.exam.question.entity.ChoiceAnswer;
import com.koolearn.exam.question.entity.Question;
import com.koolearn.exam.question.entity.QuestionAttach;
import com.koolearn.exam.shortquestion.dto.ShortQuestionDto;
import com.koolearn.exam.spokenquestion.dto.SpokenQuestionDto;
import com.koolearn.exam.whritequestion.dto.WhriteQuestionDto;

/**
 * 考试对象数据转换
 * @author yangzhenye
 */
public class ExamQuestionConvert {
	private static Log logger = LogFactory.getLog(ExamQuestionConvert.class);
	/**
	 * 将原考试对象 转换成新考试对象
	 * @param
	 * @return 
	 */
	public static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convert(IExamQuestionDto questionDto)throws Exception{
		com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto destDto = null;
		
		try {
			// 创建人
			Question question = questionDto.getQuestionDto().getQuestion();
			question.setCreateBy("新东方");
			question.setStatus(Question.QUESTION_STATUS_AUDIT);//导入的题强制为审核状态
			question.setNewVersion(1);
			// 判断
			int questionType = questionDto.getQuestionType();
			switch (questionType) {
			    case Question.QUESTION_TYPE_DANXUAN:
			    case Question.QUESTION_TYPE_DUOXUAN:
			    case Question.QUESTION_TYPE_DANXUAN_BOX://单选方框-仅子题
			    	destDto = convertChoice((ChoiceQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_SORT://排序题
			    	ChoiceQuestionDto toChoice = new ChoiceQuestionDto();
			    	JudgeQuestionDTO oriJudge = (JudgeQuestionDTO) questionDto;
			    	toChoice.setQuestionDto(oriJudge.getQuestionDto());
			    	toChoice.setChoiceQuestion(oriJudge.getChoiceQuestion());
			    	toChoice.setChoiceAnswers(oriJudge.getChoiceAnswers());
			    	destDto = convertChoice((ChoiceQuestionDto) toChoice);
			    	break;
			    case Question.QUESTION_TYPE_FILL_BLANK://填空题
			    case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
			    	destDto = convertEssay((EssayQuestionDTO) questionDto);
			        break;
			    // 完型填空
			    case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			    case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			    case Question.QUESTION_TYPE_READ:
			    case Question.QUESTION_TYPE_LISTEN:
			    case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
			        destDto = convertComplex((ComplexQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_SPOKEN:
			    	destDto = convertSpoken((SpokenQuestionDto) questionDto);
					break;
			    case Question.QUESTION_TYPE_SHORT:
			        destDto = convertShort((ShortQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_WHRITE:
			    	destDto = convertWhrite((WhriteQuestionDto) questionDto);
			    	break;
			    case Question.QUESTION_TYPE_CORRECTION:
			    	destDto = convertCorrection((ComplexCorrectionQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_TABLE://表格（showForm 1：拖拽型，2：表格型）
			    	destDto = convertMatrix((MatrixQuestionDto) questionDto);
			    	break;
			    default:
			        // 无法使用的试题
			        throw new Exception("无法使用的试题类型,试题编码code:"+questionDto.getQuestionDto().getQuestion().getCode());
			}
		} catch (Exception e) {
			logger.error("试题转换失败:qid="+questionDto.getQuestionDto().getQuestion().getId()
					+",qcode="+questionDto.getQuestionDto().getQuestion().getCode()
					+",qtype="+questionDto.getQuestionType(),e);
			System.out.println("试题转换失败:qid="+questionDto.getQuestionDto().getQuestion().getId()
					+",qcode="+questionDto.getQuestionDto().getQuestion().getCode()
					+",qtype="+questionDto.getQuestionType());
			throw e;
		}
        
	
		return destDto;
	}
	
	/**
	 * 表格矩阵（showForm 1：拖拽型，2：表格型）
	 * @param questionDto
	 * @return
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertMatrix(
			MatrixQuestionDto questionDto) throws Exception{
		com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.MatrixQuestionDto();
		//1 QuestionDto 转换
		com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
		toQdto = convertQDto(questionDto.getQuestionDto());
		to.setQuestionDto(toQdto);
		
		//MatrixQuestion
		if(questionDto.getMatrixQuestion()!=null){
			com.koolearn.cloud.exam.examcore.question.entity.MatrixQuestion toMatrix = new com.koolearn.cloud.exam.examcore.question.entity.MatrixQuestion();
			BeanUtils.copyProperties(toMatrix, questionDto.getMatrixQuestion());
			to.setMatrixQuestion(toMatrix);
		}
		
		
		//List<ChoiceQuestionDto> choiceQuestionDtos;
		if(questionDto.getChoiceQuestionDtos()!=null){
			Integer showForm = questionDto.getMatrixQuestion().getShowForm();
			List<com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto> toChoiceDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto>();
			for(ChoiceQuestionDto fromChoiceDto:questionDto.getChoiceQuestionDtos()){
				com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto toChoiceDto = new com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto();
				//单选题，去除多余的正确答案，对表格矩阵和拖拽矩阵
				if(showForm==com.koolearn.cloud.exam.examcore.question.entity.Question.QUESTION_SHOWFORM_SORT){
					//拖拽题设置为多选
					fromChoiceDto.getQuestionDto().getQuestion().setQuestionTypeId(Question.QUESTION_TYPE_DUOXUAN);
				}
				dealWithSingleChoiceAn(fromChoiceDto);
				toChoiceDto = (com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto) convertChoice(fromChoiceDto);
				toChoiceDto.getChoiceQuestion().setQuestionId(0);
				toChoiceDtoList.add(toChoiceDto);
			}
			to.setChoiceQuestionDtos(toChoiceDtoList);
		}
		
		
		//choiceAnswers
		if(questionDto.getChoiceAnswers()!=null){
			List<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer> toChoiceAnswers = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer>();
			toChoiceAnswers = convertChoiceAnswer(questionDto.getChoiceAnswers());
			to.setChoiceAnswers(toChoiceAnswers);
		}
		return to;
	}
	/**
	 * 口语题转换
	 * @param questionDto
	 * @return
	 * @throws Exception 
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertSpoken(
			SpokenQuestionDto questionDto) throws Exception {
		try {
			com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			com.koolearn.cloud.exam.examcore.question.entity.SpokenQuestion toSpokenQuestion = new com.koolearn.cloud.exam.examcore.question.entity.SpokenQuestion();
			BeanUtils.copyProperties(toSpokenQuestion, questionDto.toEntity());
			to.setSpokenQuestion(toSpokenQuestion);
			return to;
		} catch (Exception e) {
			logger.error("简答题转换失败");
			throw e;
		}
	}
	/**
	 * 改错题转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertCorrection(
			ComplexCorrectionQuestionDto questionDto) throws Exception {
		com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.ComplexCorrectionQuestionDto();
		//1 QuestionDto 转换
		com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
		toQdto = convertQDto(questionDto.getQuestionDto());
		to.setQuestionDto(toQdto);
		
		if(questionDto.getComplexQuestion()!=null){
			//ComplexQuestion
			com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion toComplexQuestion = new com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion();
			BeanUtils.copyProperties(toComplexQuestion, questionDto.getComplexQuestion());
			to.setComplexQuestion(toComplexQuestion);
		}
		//correctionQuestionDtos
		if(questionDto.getCorrectionQuestionDtos()!=null){
			List<com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto> toList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto>();
        	to.setCorrectionQuestionDtos(toList);
			for(CorrectionQuestionDto orig : questionDto.getCorrectionQuestionDtos()){
        		if(orig==null)continue;
        		com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto toCorrectionDto = new com.koolearn.cloud.exam.examcore.question.dto.CorrectionQuestionDto();
        		//子题Question
        		if(orig.getQuestionDto()!=null){
        			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toSubQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
        			toSubQdto = convertQDto(orig.getQuestionDto());
        			toCorrectionDto.setQuestionDto(toSubQdto);
        		}
        		//子题CorrectionQuestion
        		if(orig.getCorrectionQuestion()!=null){
        			com.koolearn.cloud.exam.examcore.question.entity.CorrectionQuestion toC = new com.koolearn.cloud.exam.examcore.question.entity.CorrectionQuestion();
        			BeanUtils.copyProperties(toC, orig.getCorrectionQuestion());
        			toCorrectionDto.setCorrectionQuestion(toC);
        		}
        		
        		toList.add(toCorrectionDto);
        	}
		}
		return to;
	}
	/**
	 * 简答题（问答）转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertShort(
			ShortQuestionDto questionDto) throws Exception {
		try {
			com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			com.koolearn.cloud.exam.examcore.question.entity.ShortQuestion toShort = new com.koolearn.cloud.exam.examcore.question.entity.ShortQuestion();
			BeanUtils.copyProperties(toShort, questionDto.toEntity());
			//简答题全设置为主观题
			toShort.setMarktype(1);
			toQdto.getQuestion().setIssubjectived(0);
			
			to.setShortQuestion(toShort);
			return to;
		} catch (Exception e) {
			logger.error("简答题转换失败");
			throw e;
		}
	}
	/**
	 * 作文转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertWhrite(
			WhriteQuestionDto questionDto) throws Exception {
		try {
			com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.WhriteQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			com.koolearn.cloud.exam.examcore.question.entity.WhriteQuestion toWhrite = new com.koolearn.cloud.exam.examcore.question.entity.WhriteQuestion();
			BeanUtils.copyProperties(toWhrite, questionDto.toEntity());
			to.setWhriteQuestion(toWhrite);
			return to;
		} catch (Exception e) {
			logger.error("简答题转换失败");
			throw e;
		}
	}
	/**
	 * 组合题转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertComplex(
			ComplexQuestionDto questionDto) throws Exception {
		try {
			com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.ComplexQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			//ComplexQuestion
			if(questionDto.getComplexQuestion()!=null){
				com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion toComplex = new com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion();
				BeanUtils.copyProperties(toComplex, questionDto.getComplexQuestion());
				to.setComplexQuestion(toComplex);
			}
			
			
			//List<ChoiceQuestionDto> choiceQuestionDtos;
			if(questionDto.getChoiceQuestionDtos()!=null){
				List<com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto> toChoiceDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto>();
				for(ChoiceQuestionDto fromChoiceDto:questionDto.getChoiceQuestionDtos()){
					com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto toChoiceDto = new com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto();
					toChoiceDto = (com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto) convertChoice(fromChoiceDto);
					toChoiceDtoList.add(toChoiceDto);
				}
				to.setChoiceQuestionDtos(toChoiceDtoList);
			}
			
			//List<EssayQuestionDTO> essayQuestionDTOs;
			if(questionDto.getEssayQuestionDTOs()!=null){
				List<com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto> toEssayDtoList = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto>();
				for(EssayQuestionDTO fromEssayDto:questionDto.getEssayQuestionDTOs()){
					com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto toEssayDto = new com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto();
					toEssayDto = (com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto) convertEssay(fromEssayDto);
					toEssayDtoList.add(toEssayDto);
				}
				to.setEssayQuestionDTOs(toEssayDtoList);
			}
			
			//choiceAnswers
			if(questionDto.getChoiceAnswers()!=null){
				List<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer> toChoiceAnswers = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer>();
				toChoiceAnswers = convertChoiceAnswer(questionDto.getChoiceAnswers());
				to.setChoiceAnswers(toChoiceAnswers);
			}
			
			//List<IExamQuestionDto> SubItems;
			if(questionDto.getSubItems()!=null){
				List<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto> toSubItems = new ArrayList<com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto>();
				for(IExamQuestionDto fromExamDto:questionDto.getSubItems()){
					if(fromExamDto==null)continue;
					com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto subItem = null;
					subItem = convert(fromExamDto);
					toSubItems.add(subItem);
				}
				to.setSubItems(toSubItems);
			}
			
			return to;
		} catch (Exception e) {
			logger.error("组合题转换失败");
			throw e;
		}
	}
	/**
	 * 试题QuestionDto转换
	 * 转换 question,tagObjectList,questionAttachs
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.QuestionDto convertQDto(
			QuestionDto questionDto) throws Exception{
		try {
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toDto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			Question question = questionDto.getQuestion();
			com.koolearn.cloud.exam.examcore.question.entity.Question toQuestion = new com.koolearn.cloud.exam.examcore.question.entity.Question();
			//1 Question转换
            try{
                if(question.getTeId()>0){
                    question.setCreateDate(question.getLastUpdateDate());
                }
			   BeanUtils.copyProperties(toQuestion, question);
            }catch (Exception e){
                e.printStackTrace();
            }
			toQuestion.setId(0);//question新保存
			toDto.setQuestion(toQuestion);
			toDto.setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVE);
			//2 tagObjects转换
			List<TagObject> tagObjects = questionDto.getTagObjectList();
			if(tagObjects!=null){
				List<com.koolearn.cloud.exam.examcore.question.entity.TagObject> toList = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.TagObject>();
				toDto.setTagObjectList(toList);
				for(TagObject orig:tagObjects){
					if(orig==null)continue;
					com.koolearn.cloud.exam.examcore.question.entity.TagObject dest = new com.koolearn.cloud.exam.examcore.question.entity.TagObject();
					BeanUtils.copyProperties(dest, orig);
					toList.add(dest);
				}
			}
			
			//3 attaches 转换
			List<QuestionAttach> attaches=questionDto.getQuestionAttachs();
			if(attaches!=null){
				List<com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach> toList = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach>();
				toDto.setQuestionAttachs(toList);
				for(QuestionAttach orig:attaches){
					if(orig==null)continue;
					com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach dest = new com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach();
					BeanUtils.copyProperties(dest, orig);
					toList.add(dest);
				}
				
			}
			return toDto;
		} catch (Exception e) {
			logger.error("question题转换失败");
			throw e;
		}
	}
	/**
	 * 填空题转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertEssay(
			EssayQuestionDTO questionDto) throws Exception{
		try {
			com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			if(questionDto.getEssayQuestion()!=null){
				com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion toEssayQ = new com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion();
				BeanUtils.copyProperties(toEssayQ, questionDto.getEssayQuestion());
				to.setEssayQuestion(toEssayQ);
			}
			if(questionDto.getFillblankAnswers()!=null){
				List<com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer> toFillBlankAns = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer>();
				for(FillblankAnswer origFillAns: questionDto.getFillblankAnswers()){
					if(origFillAns==null)continue;
					com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer toFillAns = new com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer();
					BeanUtils.copyProperties(toFillAns, origFillAns);
					toFillBlankAns.add(toFillAns);
				}
				to.setFillblankAnswers(toFillBlankAns);
			}
			
			return to;
		} catch (Exception e) {
			logger.error("填空题转换失败");
			throw e;
		}
	}
	/**
	 * 选择题对象转换
	 * @param questionDto
	 * @return
	 * @throws Exception
	 */
	private static com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto convertChoice(ChoiceQuestionDto questionDto) throws Exception{
		try {
			
			com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto to = new com.koolearn.cloud.exam.examcore.question.dto.ChoiceQuestionDto();
			//1 QuestionDto 转换
			com.koolearn.cloud.exam.examcore.question.dto.QuestionDto toQdto = new com.koolearn.cloud.exam.examcore.question.dto.QuestionDto();
			toQdto = convertQDto(questionDto.getQuestionDto());
			to.setQuestionDto(toQdto);
			
			if(questionDto.getChoiceQuestion()!=null){
				com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion toChoiceQuestion = new com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion();
				BeanUtils.copyProperties(toChoiceQuestion, questionDto.getChoiceQuestion());
				to.setChoiceQuestion(toChoiceQuestion);
			}
			if(questionDto.getChoiceAnswers()!=null){
				List<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer> toChoiceAnswers = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer>();
				toChoiceAnswers = convertChoiceAnswer(questionDto.getChoiceAnswers());
				to.setChoiceAnswers(toChoiceAnswers);
			}
			return to;
		} catch (Exception e) {
			logger.error("选择题转换失败");
			throw e;
		}
	}
	/**
	 * 单选题，去除多余的正确答案，对表格矩阵和拖拽矩阵
	 * @param questionDto
	 */
	private static void dealWithSingleChoiceAn(ChoiceQuestionDto questionDto){
		if(questionDto!=null && questionDto.getQuestionType()==Question.QUESTION_TYPE_DANXUAN){
			boolean rightF=false;
			for(ChoiceAnswer choiceAnswer:questionDto.getChoiceAnswers()){
				if(rightF){
					choiceAnswer.setIsright(0);
					continue;
				}
				if(choiceAnswer.getIsright()!=null && choiceAnswer.getIsright()==1){
					rightF = true;
				}
			}
			String answer = questionDto.getChoiceQuestion().getAnswer();
			if(StringUtils.isNotBlank(answer) && answer.length()>1){
				String ans[] = answer.split(",");
				questionDto.getChoiceQuestion().setAnswer(ans[0]);
			}
		}
		
	}
	/**
	 * ChoiceAnswer 转换
	 * @param choiceAnswers
	 * @return
	 * @throws Exception
	 */
	private static List<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer> convertChoiceAnswer(
			List<ChoiceAnswer> choiceAnswers) throws Exception{
		try {
			if(choiceAnswers==null)return null;
			List<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer> toChoiceAnswers = new ArrayList<com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer>();
			for(ChoiceAnswer ansFrom : choiceAnswers){
				if(ansFrom==null)continue;
				com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer toAns = new com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer();
				BeanUtils.copyProperties(toAns, ansFrom);
				toChoiceAnswers.add(toAns);
			}
			return toChoiceAnswers;
		} catch (Exception e) {
			logger.error("选择题答案转换失败");
			throw e;
		}
	}
	public static void main(String [] asrgs){
		//com.koolearn.cloud.exam.examcore.question.entity.
		//com.koolearn.cloud.exam.examcore.question.dto.
		Question from = new Question();
		com.koolearn.cloud.exam.examcore.question.entity.Question to = new com.koolearn.cloud.exam.examcore.question.entity.Question();
		from.setName("asdfa");
		try {
			BeanUtils.copyProperties(to, from);
			p(to.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public static void p(Object t){
		System.out.println(t);
	}
}
