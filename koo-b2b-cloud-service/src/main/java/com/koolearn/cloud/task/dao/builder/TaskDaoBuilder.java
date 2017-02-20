package com.koolearn.cloud.task.dao.builder;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import com.koolearn.framework.common.page.ListPager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;

public class TaskDaoBuilder implements AriesDynamicSqlBuilder{
	private static Log logger = LogFactory.getLog(TaskDaoBuilder.class);

	public String findTaskById(int taskId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from tp_exam te where te.id=:taskId ");
		return sql.toString();
	}
	/**
	 * 作业列表
	 */
	public String searchTask(ListPager listPager,TaskPager taskPager){//此部分内容有问题,需要加上卷子的学科属性
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct te.* from tp_exam te ");
		sql.append(" left join te_paper tp on te.paper_id=tp.id ");
		sql.append(" where te.type=").append(TpExam.EXAM_TYPE_TASK).append(" and te.status<>").append(TpExam.EXAM_STATUS_DELETE);
		sql.append(" and te.teacher_id=").append(taskPager.getUserId());
		if(StringUtils.isNotBlank(taskPager.getKeyWord() )){
			sql.append(" and te.exam_name like '%").append(taskPager.getKeyWord()).append("%'");
		}
		if(taskPager.getSubjectId()>0){
			sql.append(" AND tp.subject_id=").append(taskPager.getSubjectId());//tag_full_path  2464_40全路径
		}
		sql.append(" order by te.end_time desc ");
        if(listPager.getPageNo() == 0){
            sql.append(" limit 0 , ").append(listPager.getPageSize() - 1);
        }else{
            sql.append("limit ").append(listPager.getPageNo()*listPager.getPageSize()-1)
                    .append(",").append(listPager.getPageSize());
        }
		if (logger.isDebugEnabled()) {
			logger.info("searchTask="+sql.toString());
		}
		System.out.println("searchTask="+sql.toString());
		return sql.toString();
	}
	/**
	 * 作业列表数量	
	 */
	public String searchTaskCount(TaskPager taskPager){
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select count(*) from tp_exam te ");
//		sql.append(" left join te_paper tp on te.paper_id=tp.id ");
//		sql.append(" where te.type=").append(TpExam.EXAM_TYPE_TASK).append(" and te.status<>").append(TpExam.EXAM_STATUS_DELETE);
//		if(StringUtils.isNotBlank(taskPager.getKeyWord() )){
//			sql.append(" and te.exam_name like '%").append(taskPager.getKeyWord()).append("%'");
//		}
//		if(taskPager.getSubjectId()>0){
//			sql.append(" and tp.tag_full_path like '%").append(taskPager.getSubjectId()).append("_%'");//tag_full_path  2464_40全路径
//		}
//		if (logger.isDebugEnabled()) {
//			logger.info("searchTaskCount="+sql.toString());
//		}
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(te.id) from tp_exam te ");
        sql.append(" left join te_paper tp on te.paper_id=tp.id ");
        sql.append(" where te.type=").append(TpExam.EXAM_TYPE_TASK).append(" and te.status<>").append(TpExam.EXAM_STATUS_DELETE);
        sql.append(" and te.teacher_id=").append(taskPager.getUserId());
        if(StringUtils.isNotBlank(taskPager.getKeyWord() )){
            sql.append(" and te.exam_name like '%").append(taskPager.getKeyWord()).append("%'");
        }
        if(taskPager.getSubjectId()>0){
            sql.append(" AND tp.subject_id=").append(taskPager.getSubjectId());//tag_full_path  2464_40全路径
        }
        if (logger.isDebugEnabled()) {
            logger.info("searchTask="+sql.toString());
        }
		return sql.toString();
	}
	
	public StringBuilder searchTaskBySubjectSql(TaskPager task){
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT tp.question_count questionCount,tp.question_min_count questionMinCount,ter.student_view studentView,ter.id resultId,ter.status resultStatus,ter.begin_time beginTime,ter.time_off timeOff,date_add(ter.begin_time,INTERVAL ter.time_off SECOND) completeTime,e.* ");
		sql.append(" From tp_exam e INNER JOIN tp_exam_student tes on tes.exam_id=e.id ");
		sql.append(" left join te_paper tp on e.paper_id=tp.id ");
		sql.append(" LEFT JOIN te_exam_result ter on ter.student_id=tes.student_id and ter.exam_id=tes.exam_id ");
		sql.append(" WHERE e.subject_id=:task.subjectId ");
		sql.append(" and e.type in (").append(TpExam.EXAM_TYPE_TASK).append(",")
		.append(TpExam.EXAM_TYPE_TASK_stuzc).append(",")
		.append(TpExam.EXAM_TYPE_TASK_stufuxi).append(") ");
		sql.append(" and e.status=").append(TpExam.EXAM_STATUS_PUT);
		sql.append(" and tes.student_id=:task.userId ");
		sql.append(" and tes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and e.start_time<NOW() ");//作业时间未到的不显示
		if(StringUtils.isNotBlank(task.getKeyWord() )){
			sql.append(" and e.exam_name like '%").append(task.getKeyWord()).append("%'");
		}
		if(StringUtils.isNotBlank(task.getEndTime())){//作业截止时间小于endTimeStr
			sql.append(" and e.end_time <= :task.endTime ");
		}
		if (logger.isDebugEnabled()) {
			logger.info("searchTaskBySubject="+sql.toString());
		}
		System.out.println("searchTaskBySubject="+sql.toString());
		return sql;
	}
	/**
	 * 用户已做答的题目数量  学生端
	 * @param resultIds
	 * @return
	 */
	public String searchTaskResultDone(String resultIds){
		StringBuilder sql = new StringBuilder();
		sql.append(" select result_id resultId,count(*) questionCount from te_exam_result_detail where result_id in (").append(resultIds).append(")");
		sql.append(" and length(user_answer) > 0  ");
		sql.append(" and question_id not in  ");
		sql.append(" (select DISTINCT te_id from te_exam_result_detail where result_id in (").append(resultIds).append(")");
		sql.append(" and te_id<>0) ");
		sql.append(" group by result_id ");
		if (logger.isDebugEnabled()) {
			logger.info("searchTaskResultDone="+sql.toString());
		}
		System.out.println("searchTaskResultDone="+sql.toString());
		return sql.toString();
	}
	/**
	 * 用户的结果明细表中总题目数量
	 * @param resultIds
	 * @return
	 */
	public String searchTaskResultAll(String resultIds){
		StringBuilder sql = new StringBuilder();
		sql.append(" select result_id resultId,count(*) questionCount from te_exam_result_detail where result_id in (").append(resultIds).append(")"); 
		sql.append(" and question_id not in  ");
		sql.append(" (select DISTINCT te_id from te_exam_result_detail where result_id in (").append(resultIds).append(")");
		sql.append(" and te_id<>0) ");
		sql.append(" group by result_id ");
		
		if (logger.isDebugEnabled()) {
			logger.info("searchTaskResultAll="+sql.toString());
		}
		System.out.println("searchTaskResultAll="+sql.toString());
		return sql.toString();
	}
	/**
	 * 根据学科查询作业列表
	 */
	public String searchTaskBySubject(ListPager listPager, TaskPager task){
		StringBuilder sql = new StringBuilder();
		sql = searchTaskBySubjectSql(task);
		sql.append(" order by e.end_time desc  ");
		return sql.toString();
	}

	public String searchTaskBySubjectCount(TaskPager task){
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) From (");
		sql.append(searchTaskBySubjectSql(task));
		sql.append(" ) t ");
		if (logger.isDebugEnabled()) {
			logger.info("searchTaskBySubjectCount="+sql.toString());
		}
		return sql.toString();
	}
	
	/**
	 * 查询作业对应的所有学生
	 */
	public String findStudentNumByTaskIds(int userId, String ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" select tpes.exam_id examId,count(*) studentNum From tp_exam_student tpes ");
		sql.append(" where tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and tpes.teacher_id=").append(userId);
		sql.append(" and tpes.exam_id in ( ").append(ids).append(")");
		sql.append(" group by tpes.exam_id ");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentNumByTaskIds="+sql.toString());
		}
		System.out.println("findStudentNumByTaskIds="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 已完成作业的学生
	 */
	public String findCompStudentNumByTaskIds(int userId, String ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) studentNum,te.exam_id examId From te_exam_result te  ");
		sql.append(" INNER JOIN tp_exam_student tpes on te.student_id=tpes.student_id and te.exam_id=tpes.exam_id ");
//		sql.append(" where tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" where tpes.teacher_id=").append(userId);
		sql.append(" and tpes.exam_id in (").append(ids).append(") ");
		sql.append(" and te.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" group by te.exam_id ");
		if (logger.isDebugEnabled()) {
			logger.info("findCompStudentNumByTaskIds="+sql.toString());
		}
		System.out.println("findCompStudentNumByTaskIds="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 查询考试对应的班级名集合  一班,二班   examId
	 */
	public String searchClassesByTaskId(int userId, String ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" select GROUP_CONCAT(t.class_name) classesName,GROUP_CONCAT(t.full_name) classesfullName,t.exam_id examId ");
		sql.append(" from ( ");
		sql.append(" select distinct c.class_name,c.full_name,tpes.exam_id  from tp_exam_student tpes ");
		sql.append(" INNER JOIN classes c on tpes.classes_id=c.id ");
		sql.append(" where tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and tpes.teacher_id=").append(userId);
		sql.append(" and tpes.exam_id in (").append(ids).append(")");
		sql.append(" ) t ");
		sql.append(" group by t.exam_id ");
		if (logger.isDebugEnabled()) {
			logger.info("searchClassesByTaskId="+sql.toString());
		}
		return sql.toString();
	}
	/**
	 * 查询已完成作业的学生列表有排名
	 */
	public String findStudentByExamResult(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.*,(@i:=@i+1) as rank from ( ");
		sql.append(" select DISTINCT tpes.exam_id examId,u.real_name realName,u.student_code studentCode ");
		sql.append(" ,DATE_ADD(ter.begin_time,INTERVAL ter.time_off SECOND) completeTime ");
		sql.append(" ,ter.id,ter.reply,ter.isreply,ter.student_id studentId,ter.score,ter.points,ter.score/ter.points rate ");
		sql.append(" from te_exam_result ter ");
		sql.append(" left join tp_exam_student tpes on tpes.student_id=ter.student_id and ter.exam_id=tpes.exam_id ");
		sql.append(" left join user u on ter.student_id=u.id ");
		sql.append(" where ter.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and tpes.teacher_id=").append(t.getUserId());
		sql.append(" and tpes.classes_id=").append(t.getClassId());
		sql.append(" and tpes.exam_id=").append(t.getExamId());
		sql.append(" order by ter.score desc,ter.time_off asc ");
		sql.append(" ) t,(select @i := 0) t1 ");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentByExamResult="+sql.toString());
		}
		System.out.println("findStudentByExamResult="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 查询未提交作业的学生
	 */
	public String findStudentNoResultByClassesId(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select tpes.student_id studentId,u.student_code studentCode,u.real_name realName  ");
		sql.append(" from tp_exam_student tpes ");
		sql.append(" INNER JOIN user u on tpes.student_id=u.id ");
		sql.append(" where tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and tpes.classes_id=").append(t.getClassId());
		sql.append(" and tpes.exam_id=").append(t.getExamId());
		if(StringUtils.isNotBlank(t.getStudentIds())){
			sql.append(" and tpes.student_id not in (").append(t.getStudentIds()).append(")");
		}
		sql.append(" ORDER BY CONVERT( u.real_name USING gbk ) COLLATE gbk_chinese_ci ASC ");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentNoResultByClassesId="+sql.toString());
		}
		System.out.println("findStudentNoResultByClassesId="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 查询题目id和题目对应的答错人及人数集合
	 */
	public String findQuestionErrAnswerUser(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select terd.question_id questionId,GROUP_CONCAT(u.real_name) errUserName,COUNT(*) errUserNum ");
		sql.append(" from tp_exam_student tpes ");
		sql.append(" INNER join te_exam_result terr on terr.student_id=tpes.student_id ");
		sql.append(" and tpes.exam_id=terr.exam_id ");
		sql.append(" INNER join te_exam_result_detail terd  on terr.id=terd.result_id ");
		sql.append(" INNER join user u on terr.student_id=u.id ");
		sql.append(" where tpes.exam_id=").append(t.getExamId());
		sql.append(" and tpes.classes_id=").append(t.getClassId());
//		sql.append(" and tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and terd.result_answer=").append(TpExamResultDetail.RESULT_ANSWER_NOT_CORRECT);
		sql.append(" and terr.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and terd.user_answer is not null and trim(terd.user_answer) <>''  ");
		sql.append(" group by terd.question_id  ");
		if (logger.isDebugEnabled()) {
			logger.info("findQuestionErrAnswerUser="+sql.toString());
		}
		System.out.println("findQuestionErrAnswerUser="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 查询题目对应的未作答人及人数集合
	 */
	public String findQuestionNoAnswerUser(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select terd.question_id questionId,terd.question_code questionCode, ");
		sql.append(" GROUP_CONCAT(u.real_name) noAnswerUserName,COUNT(*) noAnswerUserNum ");
		sql.append(" from tp_exam_student tpes ");
		sql.append(" INNER join te_exam_result terr on terr.student_id=tpes.student_id and terr.exam_id=tpes.exam_id ");
		sql.append(" INNER join te_exam_result_detail terd  on terr.id=terd.result_id ");
		sql.append(" INNER join user u on terr.student_id=u.id ");
		sql.append(" where tpes.exam_id=").append(t.getExamId());
		sql.append(" and tpes.classes_id=").append(t.getClassId());
		sql.append(" and tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and terr.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and (terd.user_answer is null or trim(terd.user_answer) ='') ");
		sql.append(" group by terd.question_id ");
		if (logger.isDebugEnabled()) {
			logger.info("findQuestionNoAnswerUser="+sql.toString());
		}
		System.out.println("findQuestionNoAnswerUser="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 查询题目平均得分率和平均得分
	 */
	public String findQuestionAvgScore(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select round((tt.answerErrorNum/tt.answerAllNum)*100) avgRate,tt.* From ( ");
		sql.append(" select terd.question_id questionId,terd.question_code questionCode,  ");
		sql.append(" sum(case when terd.result_answer=0 or terd.result_answer=2 then 1 else 0 end) answerErrorNum,count(terd.id) answerAllNum ");
		sql.append(" from tp_exam_student tpes  ");
		sql.append(" INNER join te_exam_result terr on terr.student_id=tpes.student_id and tpes.exam_id=terr.exam_id ");
		sql.append(" INNER JOIN te_exam_result_detail terd on terr.id=terd.result_id ");
		sql.append(" where tpes.exam_id=").append(t.getExamId());
//		sql.append(" and tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and tpes.classes_id=").append(t.getClassId());
		sql.append(" and terr.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" group by terd.question_id ");
		sql.append(" ) tt ");
		if (logger.isDebugEnabled()) {
			logger.info("findQuestionAvgScore="+sql.toString());
		}
		System.out.println("findQuestionAvgScore="+sql.toString());
		return sql.toString();
	}
	/**
	 * 查询题目 根据错误率 作业讲评
	 */
	public String findAllResultDetail(TaskPager t){
		String errRate = "";
		if("0".equals(t.getRadioType())){//全部错题错误率为0,即所有错题
			errRate = "0";
		}else{
			errRate = t.getErrRate();
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select * From ( ");
		sql.append(" select terd.question_id questionId,terd.te_id teId, sum(case when terd.result_answer=0 or terd.result_answer=2 then 1 else 0 end) answerErrorNum,count(terd.id) answerAllNum ");
		sql.append(" from te_exam_result_detail terd where result_id in (").append(t.getResultIds()).append(")");
		sql.append(" and question_id not in  ");
		sql.append(" (select DISTINCT te_id from te_exam_result_detail where result_id in (").append(t.getResultIds()).append(")").append(" and te_id<>0) ");
		sql.append(" GROUP BY terd.question_id,terd.te_id ");
		sql.append(" ) tt ");
		sql.append(" where tt.answerErrorNum/tt.answerAllNum*100>").append(errRate);
		if (logger.isDebugEnabled()) {
			logger.info("findAllResultDetail="+sql.toString());
		}
		System.out.println("findAllResultDetail="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 按班级id查询提交作业的学生
	 */
	public String findStudentResultByClassesId(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select u.*,terr.isreply,terr.reply,terr.id resultId from te_exam_result terr  ");
		sql.append(" INNER JOIN tp_exam_student tpes on tpes.student_id=terr.student_id and terr.exam_id=tpes.exam_id");
		sql.append(" INNER JOIN user u on tpes.student_id=u.id ");
		sql.append(" where terr.status=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and tpes.status=").append(TpExamStudent.STATUS_VALID);
		sql.append(" and tpes.exam_id=").append(t.getExamId());
		sql.append(" and tpes.classes_id=").append(t.getClassId());
		if(StringUtils.isNotBlank(t.getKeyWord())){
			sql.append(" and u.real_name like '%").append(t.getKeyWord().trim()).append("%'");
		}
		sql.append(" order by terr.score desc,terr.time_off asc  ");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentByClassesId="+sql.toString());
		}
		System.out.println("findStudentByClassesId="+sql.toString());
		return sql.toString();
	}

	/**
	 * 查询老师拥有的班级列表
	 */
	public String findClassesByUserId(UserEntity ue){
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct c.* From classes_teacher ct INNER JOIN classes c on ct.classes_id=c.id");
		sql.append(" where ct.teacher_id=:ue.id");
		sql.append("  and ct.`status`=").append(ClassesTeacher.STATUS_VALID);
		sql.append(" and c.type<>").append(Classes.TYPE_GROUP);
		sql.append(" and c.`status`=").append(Classes.STATUS_NOMAL);
		sql.append(" and c.graduate=").append(Classes.GRADUATE);
		if (logger.isDebugEnabled()) {
			logger.info("findClassesByUserId="+sql.toString());
		}
		return sql.toString();
	}
	/**
	 * 查询老师班级分组
	 */
	public String findGroupByClassesId(Integer id){
		StringBuilder sql = new StringBuilder();
		sql.append(" select *,'false' as ok From classes c where c.status=").append(Classes.STATUS_NOMAL);
		sql.append(" and c.graduate=").append(Classes.GRADUATE);
		sql.append(" and c.parent_id=").append(id);
		sql.append(" and c.type=").append(Classes.TYPE_GROUP);
		if (logger.isDebugEnabled()) {
			logger.info("findGroupByClassesId="+sql.toString());
		}
		return sql.toString();
	}
	/**
	 * 查询老师班级分组
	 */
	public String findClassesByIds(String ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * From classes c where c.id in (").append(ids).append(")");
		if (logger.isDebugEnabled()) {
			logger.info("findClassesByIds="+sql.toString());
		}
		return sql.toString();
	}
	/**
	 * 查找班级下的学生(有效的)
	 */
	public String findStudentByClassesIds(String ids){
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct cs.classes_id classesId,u.* From classes_student cs INNER JOIN user u on cs.student_id=u.id ");
		sql.append(" where cs.status=").append(ClassesStudent.STATUS_NOMAL);
		sql.append(" and cs.classes_id in (").append(ids).append(")");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentByClassesIds="+sql.toString());
		}
		return sql.toString();
	}
	/**
	 * 修改作业
	 */
	public String updateTask(Connection conn, TaskDto dto){
		StringBuilder sql = new StringBuilder();
		sql.append(" update tp_exam te set update_time=NOW() ");
		sql.append(" ,te.exam_name='").append(dto.getExamName()).append("'");
		sql.append(" ,te.paper_id=").append(dto.getPaperId());
		sql.append(" ,start_time=:dto.startTime");
		sql.append(" ,end_time=:dto.endTime");
		sql.append(" ,subject_id=").append(dto.getSubjectId());
		sql.append(" ,range_id=").append(dto.getRangeId());
		sql.append(" ,status=").append(dto.getStatus());
		sql.append(" where te.id=").append(dto.getId());
		if (logger.isDebugEnabled()) {
			logger.info("findClassesByIds="+sql.toString());
		}
		return sql.toString();
	}
	
	/**
	 * 删除作业与学生中间表数据
	 */
	public String deleteTpExamStudent(Connection conn, TaskDto dto){
		StringBuilder sql = new StringBuilder();
		sql.append(" update tp_exam_student set update_time=NOW() ");
		sql.append(" ,status=").append(TpExamStudent.STATUS_INVALID);
		sql.append(" where exam_id=").append(dto.getId());
		sql.append(" and teacher_id=").append(dto.getTeacherId());
		sql.append(" and type=").append(TpExamStudent.TYPE_TASK);
		if (logger.isDebugEnabled()) {
			logger.info("deleteTpExamStudent="+sql.toString());
		}
		return sql.toString();
	}
	
	/**
	 * 查询学生所属班级
	 */
	public String findClassesByStudentId(UserEntity ue){
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.* From classes_student cs  ");
		sql.append(" INNER JOIN classes c on c.id=cs.classes_id ");
		sql.append(" where cs.student_id=").append(ue.getId());
		sql.append(" and cs.status=").append(ClassesStudent.STATUS_NOMAL);
		sql.append(" and c.status=").append(Classes.STATUS_NOMAL);
		sql.append(" and c.type<>").append(Classes.TYPE_GROUP);
		sql.append(" and c.graduate=").append(Classes.GRADUATE);
		sql.append(" and c.status=").append(Classes.STATUS_NOMAL);
		if (logger.isDebugEnabled()) {
			logger.info("findClassesByStudentId="+sql.toString());
		}
		System.out.println("findClassesByStudentId="+sql.toString());
		return sql.toString();
	}
	/**
	 * 查询学生所属班级的学科（班级关联老师的学科）
	 */
	public String findClassesTeacherSubject(UserEntity ue){
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct tbv.subject_id,tbv.subject_name from classes_student cs  ");
		sql.append(" INNER JOIN classes_teacher ct on cs.classes_id=ct.classes_id ");
		sql.append(" INNER JOIN teacher_book_version tbv on tbv.teacher_id=ct.teacher_id ");
        sql.append(" INNER JOIN classes c on cs.classes_id = c.id ");
		sql.append(" where c.range_name = tbv.range_name and cs.student_id=").append(ue.getId());
		sql.append(" and cs.status=").append(ClassesStudent.STATUS_NOMAL);
		sql.append(" and c.graduate=").append(Classes.GRADUATE);
		if (logger.isDebugEnabled()) {
			logger.info("findClassesTeacherSubject="+sql.toString());
		}
		System.out.println("findClassesTeacherSubject="+sql.toString());
		return sql.toString();
	}
	/**
	 * 题目得分率
	 */
	public String findStudentScoreRate(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select terd.question_code questionCode,terd.question_id questionId,terd.te_id teId,sum(terd.score)*100/sum(terd.points) scoreRate  ");
		sql.append(" From te_exam_result ter ");
		sql.append(" INNER JOIN te_exam_result_detail terd on ter.id=terd.result_id ");
		sql.append(" where ter.student_id in (").append(t.getStudentIds()).append(")");
		sql.append(" and ter.`status`=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and ter.exam_id=").append(t.getExamId());
		sql.append(" group by terd.question_code,terd.question_id ");
		sql.append(" order by terd.id asc ");
		if (logger.isDebugEnabled()) {
			logger.info("findStudentScoreRate="+sql.toString());
		}
		System.out.println("findStudentScoreRate="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 成绩分布数据 
	 */
	public String findStudentScore(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select * From te_exam_result ter ");
		sql.append(" where ter.exam_id=").append(t.getExamId());
		sql.append(" and ter.student_id in (").append(t.getStudentIds()).append(")");
		sql.append(" and ter.`status`=").append(TpExamResult.STATUS_COMPLETE);
		if (logger.isDebugEnabled()) {
			logger.info("findStudentScore="+sql.toString());
		}
		System.out.println("findStudentScore="+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 知识点得分率
	 * 查询题目得分
	 */
	public String findStudentQuestionScore(TaskPager t){
		StringBuilder sql = new StringBuilder();
		sql.append(" select terd.question_code questionCode,terd.question_id questionId,sum(terd.score) score,sum(terd.points) points ");
		sql.append(" From te_exam_result ter  ");
		sql.append(" INNER JOIN te_exam_result_detail terd on ter.id=terd.result_id ");
		sql.append(" where ter.exam_id=").append(t.getExamId());
		sql.append(" and ter.`status`=").append(TpExamResult.STATUS_COMPLETE);
		sql.append(" and ter.student_id in (").append(t.getStudentIds()).append(")");
		sql.append(" group by terd.question_code,terd.question_id ");
		
		if (logger.isDebugEnabled()) {
			logger.info("findStudentQuestionScore="+sql.toString());
		}
		System.out.println("findStudentQuestionScore="+sql.toString());
		return sql.toString();
	}
    /**
     * 学生错题本：作业列表（只含错题或部分正确的作业）
     */
    public String searchTaskOfErrorNote(TaskPager task){
        StringBuilder sql = new StringBuilder();
        sql = searchTaskOfErrorNoteSql(task);
        sql.append(" order by te.create_time desc ");
        sql.append("limit ").append(task.getPageNo()*task.getPageSize())
                .append(",").append(task.getPageSize());
        return sql.toString();
    }
    /**
     * 学生错题本：作业列表
     */
    public String searchTaskOfErrorNoteCount( TaskPager task){
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) From (");
        sql.append(searchTaskOfErrorNoteSql(task));
        sql.append(" ) t ");
        if (logger.isDebugEnabled()) {
            logger.info("searchTaskBySubjectCount="+sql.toString());
        }
        return sql.toString();
    }
    public StringBuilder searchTaskOfErrorNoteSql(TaskPager task){
        //排除课堂和 错题复习and te.type <>2 and te.type <>4
        // te.status=4  只查看发布状态下的错题作业 暂时去掉，做过的错题撤回还能查看
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT te.id,te.exam_name as 'name' ");
        sql.append(" from tp_exam te ");
        sql.append(" JOIN tp_exam_student tes ON te.id = tes.exam_id ");
        sql.append(" JOIN te_exam_result er on te.id=er.exam_id ");
        sql.append(" join te_exam_result_detail erd on erd.result_id=er.id ");
        sql.append(" where   tes.student_id=:task.studentId and er.student_id=:task.studentId and te.subject_id=:task.subjectId  and te.type <>2 and te.type <>4  and te.range_id=:task.rangeId   and  tes.status =1 ");
        sql.append(" and erd.result_answer in (0,2) ");//只查询有错题的作业（部分正确算错题）
        if(StringUtils.isNotBlank(task.getKeyWord())){
            sql.append(" and te.exam_name like '%"+task.getKeyWord()+"%' ");
        }
        return sql;
    }

    /**
     * 根据过滤条件查询试题列表。分页查询
     * @param
     * @return
     */
    public String searchErrorQuestion(QuestionFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        sql.append("limit ").append(filter.getPageNo()*filter.getPageSize())
                .append(",").append(filter.getPageSize());
        return sql.toString();
    }
    /**
     * 构造sql语句片段
     * @param
     * @param
     */
    private StringBuffer structureSql(QuestionFilter questionFilter) {
        StringBuffer sql = new StringBuffer(" SELECT * from tp_error_note where status=1 and  student_id ="+questionFilter.getLoginUser().getId() );
        if(questionFilter.getSubjectId()!=null){
            sql.append(" and tag_full_path like '%_"+questionFilter.getSubjectId()+"_%'  ");
        } if(questionFilter.getRangeId()!=null){
            sql.append(" and tag_full_path like '%_"+questionFilter.getRangeId()+"_%'  ");
        } if(questionFilter.getTagId()!=null){
            //jin du dian
            sql.append(" and tag_full_path like '%"+questionFilter.getTagId()+"_%'  ");
        }
        return sql;

    }
    /**
     * 根据过滤条件查询试题数量
     * @param
     * @return
     */
    public String searchErrorQuestionCount(QuestionFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        String sqlResult = " select count(*) from ("+ sql.toString() +") lin ";
        return sqlResult;

    }
    public String rebuildUpdate(String xiaoxiao){
        return xiaoxiao;
    }
}
