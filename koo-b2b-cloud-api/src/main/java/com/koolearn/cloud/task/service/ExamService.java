package com.koolearn.cloud.task.service;

import java.util.Date;
import java.util.List;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.exam.entity.ExamQueryDto;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.testpaper.entity.HandTestPagerDto;
import com.koolearn.klb.tags.entity.Tags;



public interface ExamService {
	
	/**
	 * 更新考试结果状态 从未开始更新为进行中
	 * @param examId
	 * @param studentId
	 */
    public void parsePreExamResult(int examId, int studentId);
	
	/**
	 * * @Description: TODO(考试过程——考试信息缓存) 
	   *  @param examId
	   *  @return    
	   * @return ExamEntity    
	   * @author: 葛海松
	   * @time:    2015年6月5日 下午6:40:06 
	   * @throws
	 */
	public TpExam queryExamByIdCache(int examId);
	
	
	
	
	
	
	
	
	
	
	
	
	
	//以上方法为正在使用方法
	
	
	
	
	
	
	
	/**
	 * 通过id查询考试信息
	 * @param examId
	 * @return
	 */
	public TpExam queryExamById(int examId);
	public Integer deleteError(Integer errorId);
	




    /**
    发布成绩  答案
     a :发布答案 r ：发布成绩
     *
     */
    boolean issue(Integer examId,String issuetype);

    /**
     * 判断试卷中是否全是 客观题
     * @param paperId
     * @return  true   是
     */
    boolean isAllObjectiveOfPaper(Integer paperId);
}
