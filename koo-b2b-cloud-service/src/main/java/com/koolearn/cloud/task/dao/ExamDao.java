package com.koolearn.cloud.task.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.koolearn.cloud.common.entity.UseRecord;
import com.koolearn.cloud.exam.entity.ExamQueryDto;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.exam.entity.TpErrorNote;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.testpaper.entity.HandTestPagerDto;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.exam.question.entity.ErrorNote;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;
import com.koolearn.klb.tags.entity.Tags;

@DAO(dbtype=DbType.MYSQL,source= GlobalConstant.MYSQL_DATASOURCE)
public interface ExamDao {
	
    /** 更新考试结果表状态为 进行中 状态 */
    @SQL("UPDATE te_exam_result SET status = "+TpExamResult.STATUS_PROCESSING+",begin_time =now()  WHERE student_id=:studentId and exam_id=:examId and status="+TpExamResult.STATUS_PRE)
    void updatePreExamResult(@SQLParam("examId") int examId,@SQLParam("studentId") int studentId);
	
	@SQL(type=SQLType.READ_BY_ID)
	public TpExam queryExamById(int examId);
	
	// 批量写入错题本
	@SQL(type = SQLType.WRITE_INSERT)
	public void insertErrorNote(Connection conn, List<TpErrorNote> enList);
	
	
	
	
	//以上为有效使用方法

	@SQL(type=SQLType.WRITE_INSERT)
	public int insertExam(Connection conn, TpExam exam);

	@SQL(type=SQLType.READ)
	public int examListTotalRows(@SQLParam("examQueryDto") ExamQueryDto examQueryDto);
	
	@SQL(type=SQLType.READ)
	public List<TpExam> examList(@SQLParam("examQueryDto") ExamQueryDto examQueryDto,@SQLParam("rownum") int rownum,@SQLParam("pageSize") int pageSize);

	@SQL("update tp_exam set status=:status,create_time=now() where id=:examId")
	public int changeStatus(@SQLParam("examId") String examId,@SQLParam("status") String status);

	@SQL(type=SQLType.READ)
	public int studentExamListTotalRows(@SQLParam("examQueryDto") ExamQueryDto examQueryDto);

	@SQL(type=SQLType.READ)
	public List<TpExam> studentExamList(@SQLParam("examQueryDto") ExamQueryDto examQueryDto,@SQLParam("rownum") int rownum,@SQLParam("pageSize") int pageSize);

	@SQL(type=SQLType.READ)
	public int studentExamPracticeTotalRows(@SQLParam("examQueryDto") ExamQueryDto examQueryDto);

	@SQL(type=SQLType.READ)
	public List<TpExam> studentExamPractice(@SQLParam("examQueryDto") ExamQueryDto examQueryDto,@SQLParam("rownum") int rownum,@SQLParam("pageSize") int pageSize);

	@SQL(" select * From te_rela_paper_type trpt,tags t where trpt.tag_id=t.id and t.status!=0 and trpt.paper_id=:paperId")
	public Tags getTopTagByPaperId(@SQLParam("paperId") Integer paperId);

	@SQL(type=SQLType.WRITE_INSERT)
	@GeneratedKey
	public int insertExamPractice(Connection conn, TpExam exam);

	@SQL(" select * From tp_exam te where te.paper_id=:paperId and te.type=:examType ")
	public TpExam examOne(@SQLParam("examType") Integer examType,@SQLParam("paperId") Integer paperId);

	@SQL(type=SQLType.WRITE_INSERT)
	public void insertExamStuPractice(Connection conn, TpExamStudent es);

	@SQL(type=SQLType.READ)
	public List<TpExamStudent> studentExamPracticeCondition(@SQLParam("examQueryDto") ExamQueryDto examQueryDto,@SQLParam("rownum") int rownum,@SQLParam("pageSize") int pageSize);

//	@SQL(" delete from tp_exam_class where exam_id=:examId and teacher_id=:teacherId and sch_id in (:oldClassId) ")
	@SQL(type=SQLType.WRITE)
	public void deleteExamClass(Connection conn,@SQLParam("oldClassId") String oldClassId,@SQLParam("teacherId") Integer teacherId,@SQLParam("examId") Integer examId);

	@SQL(type=SQLType.WRITE_UPDATE)
	public int modifyExam(Connection conn,@SQLParam("exam") TpExam exam);

	@SQL(type=SQLType.READ)
	public int studentExamPracticeConditionTotalRows(@SQLParam("examQueryDto") ExamQueryDto examQueryDto);
	/**
	 * 写入错题本
	 * @param conn
	 * @param ext
	 * @return
	 * @author DuHongLin
	 */
	@SQL(type = SQLType.WRITE_INSERT)
	public int insertErrorNote(Connection conn, TpErrorNote en);
	
	@SQL(type=SQLType.READ)
	public Long queryErrorNoteCount(@SQLParam("listPager") HandTestPagerDto listPager);
	
	@SQL(type=SQLType.READ)
	public List<ErrorNote> queryErrorNote(@SQLParam("listPager") HandTestPagerDto listPager);
	
	@SQL("delete from tp_error_note where id=:errorId")
	public int deleteError(@SQLParam("errorId") Integer errorId);
	
	@SQL("SELECT   `id`, `structure_id` , `question_id` , `points` , `score` ,`right_answer` , `user_answer` , `result_answer` , `subjective` ,"
			+ " `question_type_id` , `result_id`,`te_id` ,`reply` , `tag1` , `tag2` , `tag3`, `te_type` , `question_code` "
			+ " FROM te_exam_result_detail WHERE te_id = 0 and result_id = :resultId")
	List<TpExamResultDetail> selectByResultId(@SQLParam("resultId") int resultId);

	@SQL(" SELECT distinct ter.* FROM te_exam_result_detail terd,te_exam_result ter WHERE ter.id=terd.result_id and te_id=0 and ter.exam_id=:bean.examId and student_id=:bean.userId ")
	public List<TpExamResultDetail> selectByResult(@SQLParam("bean") HandTestPagerDto bean);

	@SQL(" select te.name,te.start_time startTime,te.end_time endTime,ter.points,ter.score,ter.time_off timeOff ,ter.objectives_score objectivesScore "
			+ " from tp_exam te,te_exam_result ter "
			+ " where te.id=ter.exam_id and ter.exam_id=:bean.examId and ter.student_id=:bean.userId "
			+ " ORDER BY ter.begin_time desc LIMIT 0,1")
	public TpExam queryExamResultById(@SQLParam("bean") HandTestPagerDto bean);
	
	/**
	 * 根据考试编码查找考试
	 * 只查询 1.在线统考 2.随堂测评 
	 * 不查询 3.在线练习
     * 考试状态 0.新建（未审核） 1.有效（审核通过） 2.删除 3.作废
	 */
	@SQL("select * from tp_exam where code=:code and type in (1,2) and status=1 limit 1")
	public TpExam getExamByCode(@SQLParam("code") String code);
	
	@SQL("select *  from tp_exam   where  id=:examId ")
	public TpExam findExamById(@SQLParam("examId")Integer examId);


	/**
	 * 修改考试（先删后加） - 删除考试ip段限制
	 * @param conn
	 * @param id
	 */
	@SQL("delete from tp_exam_ip where exam_id=:examId")
	public void deleteExamIp(Connection conn,@SQLParam("examId") Integer examId);

    /**
     * 获取班级学生
     * @param classIdList
     * @return
     */
    @SQL("select s.* from student s INNER JOIN   class_student cs on cs.student_id=s.id where cs.class_id in (:classIdList)")
    List<UserEntity> getStudentByclassids(@SQLParam("classIdList") List<Integer> classIdList);
    
    @SQL("update tp_exam set issue_answer=1 where id=:examId")
    public int issueAnswer(@SQLParam("examId") int examId);
    @SQL("update tp_exam set issue_result=1 where id=:examId")
    public int issueResult(@SQLParam("examId") int examId);

    /**
     * 查询 试卷中  主观题的个数    issubjectived  1 为客观 0 为主观'
     * @param paperId
     * @return
     */
    @SQL("select  count(1) from (SELECT `name` from te_test_paper_structure where paper_id=:paperId and structure_type=1) pq " +
            " LEFT JOIN te_question q on q.`code`=pq.`name` " +
            " and q.new_version=1 and q.issubjectived=0 " +
            " where q.id is not null")
    int subjectiveCountOfPaper(@SQLParam("paperId") Integer paperId);

    /** 查询是否已经存在该错题   */
    @SQL(" select * from tp_error_note ten where ten.student_id=:en.studentId and ten.question_code=:en.questionCode  ")
	TpErrorNote findErrorNote(@SQLParam("en") TpErrorNote en);

    /** 更新错题本次数   特例,如果之前移除错误本的记录要保留之前的错次数,故此处处理这种数据,次数+1,状态更新为有效   */
    @SQL(type = SQLType.WRITE_UPDATE)
	void updateErrorNote(Connection conn, TpErrorNote note);
}
