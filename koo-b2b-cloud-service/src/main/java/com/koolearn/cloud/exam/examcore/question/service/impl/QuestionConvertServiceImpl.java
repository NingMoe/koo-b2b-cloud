package com.koolearn.cloud.exam.examcore.question.service.impl;

import com.koolearn.cloud.exam.entity.DataSync;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionConvertService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.ExamQuestionConvert;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.exam.base.dto.IExamQuestionDto;
import com.koolearn.exam.question.entity.Question;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;

/**
 * 考试试题转换服务
 * @author yangzhenye
 */
public class QuestionConvertServiceImpl implements QuestionConvertService {
	private Log logger = LogFactory.getLog(QuestionConvertServiceImpl.class);
	
	@Autowired
	private QuestionBaseService questionBaseService;

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    /**
	 * 试题数据转换和保存
	 * @param examQuestionDto
	 * @throws Exception
	 */
	public void saveQuestion(IExamQuestionDto examQuestionDto)
			throws Exception {
		Connection conn=null;
        com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto destDto=null;
		try {
			conn= ConnUtil.getTransactionConnection();
			//将原考试对象 转换成新考试对象，并设置为另存方式保存题目ConstantTe.QUESTION_SAVETYPE_SAVEAS
			destDto = ExamQuestionConvert.convert(examQuestionDto);
			destDto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVEAS);
			
			//1 转换保存标签数据
			//题目来源记录 te_question_bank_ext
//			destDto.getQuestionDto().setQuestionBankExt(questionBankExt);
			
			//2 保存考试试题原始数据
			questionBaseService.saveExamQuestion(conn,destDto);
			conn.commit();
            DataSync.updateErrorNumCache("question",true);//记录题目成功个数
		} catch (Exception e) {
            DataSync.updateErrorNumCache("question",false);//记录题目同步失败个数
			logger.error("题库试卷同步>>>>>>题目保存失败>>>考试试题同步转换异常，qcode="+examQuestionDto.getQuestionDto().getQuestion().getCode()
					+",qid="+examQuestionDto.getQuestionDto().getQuestion().getId()
					+",qtypeid="+examQuestionDto.getQuestionType(),e);
			ConnUtil.rollbackConnection(conn);
		}finally{
			ConnUtil.closeConnection(conn);
		}
		
	}
	/**
	 * 题目非法性验证（判断支持题目类型及其他验证）
	 * @param examQuestionDto
	 * @throws Exception
	 */
	public boolean saveCheckQuestion(IExamQuestionDto examQuestionDto) {
        boolean falg=false;
		try {
			// 判断支持题目类型
			switch (examQuestionDto.getQuestionType()) {
			    case Question.QUESTION_TYPE_DANXUAN:
			    case Question.QUESTION_TYPE_DUOXUAN:
			    case Question.QUESTION_TYPE_SORT:
			    case Question.QUESTION_TYPE_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
			    case Question.QUESTION_TYPE_READ:
				case Question.QUESTION_TYPE_LISTEN:
				case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
				case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
				case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
			    case Question.QUESTION_TYPE_SHORT:
			    case Question.QUESTION_TYPE_SPOKEN:
			    case Question.QUESTION_TYPE_CORRECTION:
			    case Question.QUESTION_TYPE_WHRITE:
			    case Question.QUESTION_TYPE_TABLE://表格（showForm 1：拖拽型，2：表格型）:
                falg=true;
			    break;
			    default:
			        // 无法使用的试题
//			        throw new Exception("无法使用的试题类型,试题编码code:"+examQuestionDto.getQuestionDto().getQuestion().getCode());
			}
            return falg;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
	}

}
