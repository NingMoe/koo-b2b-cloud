package com.koolearn.cloud.exam.examcore.exam.dao;

import java.sql.Connection;
import java.util.List;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;


@DAO(source= OnlyExamConstant.MYSQL_DATASOURCE)
public interface ExamResultDao {
	
	/**
	 * 查询最新的考试结果
	 * @param examId
	 * @param studentId
	 * @return
	 * @author DuHongLin
	 */
	@SQL("SELECT * FROM te_exam_result WHERE exam_id =:examId AND student_id =:studentId ORDER BY begin_time DESC LIMIT 1")
	public TpExamResult selectLast(@SQLParam("examId") int examId, @SQLParam("studentId") int studentId);
	/**
	 * 更新考试结果信息
	 * @param conn
	 * @param examResult
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_UPDATE)
	public void updateResult(Connection conn, TpExamResult examResult);
	
	@SQL("select * from te_exam_result where id in(:ids)")
	List<TpExamResult> findResultsByIds(@SQLParam("ids") List<Integer> ids);
	
	
	
	
	
	
	
	//以上方法为更改过的方法
	
	
	
	
	
	
	
	
	
	

	/**
	 * 重复的数据获取最后考试的数据
	 * @param listPager 
	 * @param examId
	 * @return
	 */
	@SQL("SELECT r.* FROM te_exam_result r,(SELECT MAX(r2.id) id FROM te_exam_result r2 WHERE r2.exam_id=:examId  GROUP BY r2.student_id ) r3 WHERE r.id=r3.id")
	List<TpExamResult> findResults(@PageBy ListPager listPager, @SQLParam("examId") int examId);

	/**
	 * 数量
	 * @see com.koolearn.cloud.exam.examcore.exam.dao.ExamResultDao#findResults
	 * @param examId
	 * @return
	 */
	@SQL("SELECT count(r.id) FROM te_exam_result r,(SELECT MAX(r2.id) id FROM te_exam_result r2 WHERE r2.exam_id=:examId  GROUP BY r2.student_id ) r3 WHERE r.id=r3.id")
	int findResultsCount(int examId);


	
	/**
	 * 写入考试结果信息
	 * @param conn
	 * @param examResult
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insert(Connection conn, TpExamResult examResult);
	
	
	/**
	 * 查找得分前10学生信息
	 * @return
	 * @throws Exception
	 */
	@SQL(" select er.*,stu.name as studentName,stu.student_code as studentCode from te_exam_result er " +
			", student stu where er.student_id = stu.id and er.exam_id =:examId " +
			" order by  er.score desc  limit 10  ")
	public List<TpExamResult> getTopTen(@SQLParam("examId") Integer examId);
	/**
	 * 查找得分前1000学生信息
	 * @return
	 * @throws Exception
	 */
	@SQL(" select er.*,stu.name as studentName,stu.student_code as studentCode from te_exam_result er " +
			", student stu where er.student_id = stu.id and er.exam_id =:examId " +
			" order by  er.score desc  limit 1000  ")
	List<TpExamResult> getExamResultMax1000(@SQLParam("examId") Integer examId);

    /**
     * 查询本次考试未批改的题数
     * @param examId
     * @return
     */
    @SQL("SELECT count(1) from  te_exam_result_search where exam_id=:examId and pg_status=0")
    int findNotPgNum(@SQLParam("examId") Integer examId);

    /**批改时，如果批改完毕修改考试成绩发布状态*/
    @SQL("UPDATE tp_exam  set issue_result=1 where id=:examId")
    void updateExamIssueResult(@SQLParam("examId") int examId);
    
    @SQL("UPDATE tp_exam  set issue_result=1 where id=:examId")
    void queryExamIssueResult(@SQLParam("examId") int examId);
    /**
     * 根据考试结果Id 查询学生
     * @param rsid
     * @return
     */
    @SQL("select DISTINCT s.* from te_exam_result t ,user s where t.id=:rsid and t.student_id=s.id")
    UserEntity queryStudentByExamresultID(@SQLParam("rsid") int rsid);
    /**
     * 根据考试结果Id 查询考试
     * @param rsid
     * @return
     */
    @SQL("select DISTINCT s.* from te_exam_result t ,tp_exam s where t.id=:rsid and t.exam_id=s.id")
    TpExam queryExamByExamresultID(@SQLParam("rsid") int rsid);

    @SQL("select * from classes where id in (select classes_id from  tp_exam_class where exam_id=:examId)")
    List<Classes> findClassByExam(@SQLParam("examId") int examId);
    
    /** 作业批阅  保存老师评语 */
    @SQL(" update te_exam_result set reply=:reply where id=:resultId ")
	public int saveMarkReply(@SQLParam("resultId") int resultId,@SQLParam("reply") String reply);
}
