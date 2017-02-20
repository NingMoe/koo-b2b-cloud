package com.koolearn.cloud.task.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.exam.entity.ExamQueryDto;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dao.ExamDao;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.task.service.ExamService;
import com.koolearn.cloud.testpaper.entity.HandTestPagerDto;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.klb.tags.entity.Tags;


public class ExamServiceImpl implements ExamService{
	private static Logger logger=Logger.getLogger(ExamServiceImpl.class);
	private ExamDao examDao;
	@Autowired
	private QuestionBaseService questionBaseService;
	@Autowired
	private TestPaperService testPaperService;
	
	public TestPaperService getTestPaperService() {
		return testPaperService;
	}
	public void setTestPaperService(TestPaperService testPaperService) {
		this.testPaperService = testPaperService;
	}
	public QuestionBaseService getQuestionBaseService() {
		return questionBaseService;
	}
	public void setQuestionBaseService(QuestionBaseService questionBaseService) {
		this.questionBaseService = questionBaseService;
	}
	public ExamDao getExamDao() {
		return examDao;
	}
	public void setExamDao(ExamDao examDao) {
		this.examDao = examDao;
	}
	
	/**
	 * * @Description: TODO(考试过程——考试信息缓存) 
	   *  @param examId
	   *  @return    
	   * @return ExamEntity    
	   * @author: 葛海松
	   * @time:    2015年6月5日 下午6:40:06 
	   * @throws
	 */
	@Override
	public TpExam queryExamByIdCache(int examId) {
		 TpExam exam =CacheTools.getCache(ConstantTe.PROCESS_EXAMPAPER_EXAMID+examId, TpExam.class);
			if(exam==null){
				exam=queryExamById(examId);
				 CacheTools.addCache(ConstantTe.PROCESS_EXAMPAPER_EXAMID+examId, ConstantTe.CACHE_TIME, exam);
			}
		return exam;
	}
	
	/**
	 * 更新考试结果状态 从未开始更新为进行中
	 * @param examId
	 * @param studentId
	 */
    @Override
    public void parsePreExamResult(int examId, int studentId) {
        examDao.updatePreExamResult( examId,studentId);
    }
	
	
    
    
	
	
	
	
	
	
	
	
	
	//以上方法为正在使用中方法
	
	
	
	
	

	@Override
	public TpExam queryExamById(int examId) {
		return examDao.queryExamById(examId);
	}
	@Override
	public Integer deleteError(Integer errorId) {
		return examDao.deleteError(errorId);
	}





    @Override
    public boolean issue(Integer examId, String issuetype) {
//        a:发布答案 r：发布成绩    对应状态置1
        if("a".equals(issuetype)){
            examDao.issueAnswer(examId);
        }else if("r".equals(issuetype)){
            examDao.issueResult(examId);
        }
        return true;
    }
    /**
     * 判断试卷中是否全是 客观题
     * @param paperId
     * @return  true   是
     */
    @Override
    public boolean isAllObjectiveOfPaper(Integer paperId) {
        boolean isAllObjective=true;
        //查询试卷中  主观题的个数
        int subjectiveCount=examDao.subjectiveCountOfPaper(paperId);
        if(subjectiveCount>0){
            isAllObjective=false;
        }
        return isAllObjective;
    }

}
