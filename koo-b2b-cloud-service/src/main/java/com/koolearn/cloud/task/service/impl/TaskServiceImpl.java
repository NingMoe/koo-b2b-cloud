package com.koolearn.cloud.task.service.impl;

import com.koolearn.cloud.classRoom.dao.ClassRoomDao;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.vo.StudentResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dao.TaskDao;
import com.koolearn.cloud.task.dto.*;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DataDictionaryUtil;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

public class TaskServiceImpl implements TaskService {
    public TaskDao taskDao;
    private ClassRoomDao classRoomDao;

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public ClassRoomDao getClassRoomDao() {
        return classRoomDao;
    }

    public void setClassRoomDao(ClassRoomDao classRoomDao) {
        this.classRoomDao = classRoomDao;
    }
    /* 学生端使用的方法 start*/

    /**
     * 根据考试结果id,查询结果信息
     */
    @Override
    public TpExamResult findExamResultById(int resultId) {
        TpExamResult examResult = taskDao.findExamResultById(resultId);
        parseExamResultRate(examResult);
        return examResult;
    }

    public void parseExamResultRate(TpExamResult examResult) {
        if (examResult != null) {
            DecimalFormat df = new DecimalFormat("0%");
            if (examResult.getPoints() != null && examResult.getPoints() > 0) {
                double score = examResult.getScore() == null ? 0d : examResult.getScore();
                BigDecimal b1 = new BigDecimal(score);
                BigDecimal b2 = new BigDecimal(examResult.getPoints());
                BigDecimal b3 = b1.divide(b2, 3, BigDecimal.ROUND_HALF_EVEN);
                double rate = b3.doubleValue();
                examResult.setRate(df.format(rate));//格式化比率
                examResult.setRateDouble(rate * 100);
            } else {
                examResult.setRate("0%");
                examResult.setRateDouble(0d);
            }
        }
    }

    /**
     * 根据学生id和作业id查询结果
     */
    @Override
    public TpExamResult findExamResult(TaskPager pager) {
        TpExamResult tp = null;
        List<TpExamResult> list = taskDao.findExamResult(pager);
        if (list != null && !list.isEmpty()) {
            tp = list.get(0);
        }
        return tp;
    }

    /**
     * 根据学科查询作业列表
     *
     * @param
     * @return
     */
    @Override
    public TaskPager searchTaskBySubject(TaskPager task) {
        List<TaskDto> resultList = taskDao.searchTaskBySubject(task, task);
        //处理页面显示逻辑,即学生显示作业当前应该进行的操作,开始作业,继续作业,复习
        String resultIds = "";
        if (resultList != null) {
            long nowIntDate = new Date().getTime();
            for (int i = 0; i < resultList.size(); i++) {
                TaskDto ta = (TaskDto) resultList.get(i);
                handelTaskListButton(nowIntDate,ta,task);
                if (ta.getResultId() != null) {
                    resultIds = resultIds + "," + ta.getResultId();
                }
            }
            if (resultIds.length() > 0) {
            	handleTaskCompleteRate(resultIds,resultList);//将作业完成率补充到作业列表
            }
        }
        int totalRows = taskDao.searchTaskBySubjectCount(task);
        task.setTotalRows(totalRows);
        task.setResultList(resultList);
        return task;
    }
    /**
     * 处理作业列表按钮显示
     * @param nowIntDate
     * @param ta
     * @param task
     */
    private void handelTaskListButton(long nowIntDate, TaskDto ta, TaskPager task) {
    	String resultStatus = ta.getResultStatus();
        long endTime = ta.getEndTime().getTime();//作业截止时间
        if(endTime<nowIntDate){//截止时间已过,显示复习
        	if(resultStatus==null){
        		ta.setButtonWords("复习");
                ta.setButtonCss("p-list-enter white-btn-gry");
                ta.setButtonUrl("/student/pc/showPaper");
            }else{
            	ta.setButtonWords("复习");
                ta.setButtonCss("p-list-enter white-btn-gry");
                ta.setButtonUrl("/student/pc/reviewPage?resultId=" + ta.getResultId());
            }
        }else{
        	if(String.valueOf(TpExamResult.STATUS_PROCESSING).equals(resultStatus)){
            	ta.setButtonWords("继续作业");
                ta.setButtonCss("p-list-enter white-btn pdlr4");
                ta.setButtonUrl("javascript:joinExam(" + ta.getId() + "," + task.getUserId() + ",this);");
            }else if(String.valueOf(TpExamResult.STATUS_COMPLETE).equals(resultStatus)){
            	ta.setButtonWords("复习");
                ta.setButtonCss("p-list-enter white-btn-gry");
                ta.setButtonUrl("/student/pc/reviewPage?resultId=" + ta.getResultId());
            }else{
            	ta.setButtonWords("开始作业");
                ta.setButtonCss("p-list-enter green-btn green-btn-style");
                ta.setButtonUrl("javascript:joinExam(" + ta.getId() + "," + task.getUserId() + ",this);");
            }
        }
	}

	/**
     * 处理填充作业完成率
     * @param resultIds
     * @param resultList
     */
    private void handleTaskCompleteRate(String resultIds,List<TaskDto> resultList) {
    	resultIds = resultIds.substring(1);//截取掉第一个逗号
        List<TaskCompletRate> doneList = taskDao.searchTaskResultDone(resultIds);//用户已做答的题目数量
        List<TaskCompletRate> allList = taskDao.searchTaskResultAll(resultIds);//用户的结果明细表中总题目数量
        Map<Integer, Integer> doneMap = ListToMap(doneList);
        Map<Integer, Integer> allMap = ListToMap(allList);
        for (int i = 0; i < resultList.size(); i++) {//循环作业列表数据,将完成率数据组装进去
            TaskDto ta = (TaskDto) resultList.get(i);
            if(ta.getResultId()!=null){
            	 Integer done = doneMap.get(ta.getResultId());
            	 if(done==null){
            		 done = 0;
            	 }
                 Integer all = allMap.get(ta.getResultId());
                 double compRate = done*100.0/all;
                 ta.setCompleteRate(Math.round(compRate));
            }else{
            	ta.setCompleteRate(0);
            }
        }
	}

	public Map<Integer,Integer> ListToMap(List<TaskCompletRate> doneList){
    	Map<Integer,Integer> map = new HashMap<Integer, Integer>();
    	for (int i = 0; i < doneList.size(); i++) {
    		TaskCompletRate comp = doneList.get(i);
    		map.put(comp.getResultId(), comp.getQuestionCount());
		}
		return map;
    }

    /**
     * 查询学生学科是否已阅
     */
    @Override
    public Map<String, StudentHasTaskLog> findTaskLogByStudentId(int studentId) {
        Map<String, StudentHasTaskLog> map = new HashMap<String, StudentHasTaskLog>();
        List<StudentHasTaskLog> list = taskDao.findTaskLogByStudentId(studentId);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                StudentHasTaskLog sl = list.get(i);
                map.put(studentId + "_" + sl.getSubjectId(), sl);
            }
        }
        return map;
    }

    /**
     * 插入学生浏览作业日志表数据
     */
    @Override
    public void insertStudentHasTaskLog(int studentId) {
        //查询学生未查看作业
        List<StudentTaskLogDto> list = taskDao.findStudentTaskLog(studentId);
        List<StudentHasTaskLog> inList = new ArrayList<StudentHasTaskLog>();
        //将学生未查看作业插入到学生作业日志查看表
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                StudentTaskLogDto st = list.get(i);
                StudentHasTaskLog sh = new StudentHasTaskLog();
                sh.setStatus(StudentHasTaskLog.STATUS_SEE_NO);
                sh.setStudentId(studentId);
                sh.setSubjectId(st.getSubjectId());
                sh.setExamId(st.getExamId());
                sh.setCreateTime(new Date());
                inList.add(sh);
            }
            taskDao.insertStudentHasTaskLog(inList);
        }
    }

    /**
     * 更新学生作业日志表，将已查看过的科目更新为已阅
     */
    @Override
    public void updateTaskLog(int subject, int studentId) {
        taskDao.updateTaskLog(subject, studentId, StudentHasTaskLog.STATUS_SEE_NO, StudentHasTaskLog.STATUS_SEE_YES);
    }
    /* 学生端使用的方法 end*/

    @Override
    public TaskDto findTaskById(int taskId) {
        TaskDto t = taskDao.findTaskById(taskId);
        if (t.getEndTime() != null && t.getEndTime().getTime() < new Date().getTime()) {
            t.setFinishStatus(TaskDto.FINISH_STATUS_END);
        } else {
            t.setFinishStatus(TaskDto.FINISH_STATUS_NOMAL);
        }
        return t;
    }

    /**
     * 作业列表
     */
    @Override
    public TaskPager searchTask(TaskPager taskPager) {
        List<TaskDto> resultList = taskDao.searchTask(taskPager, taskPager);
        int totalRows = taskDao.searchTaskCount(taskPager);
        taskPager.setTotalRows(totalRows + 1);
        taskPager.setResultList(resultList);
        return taskPager;
    }

    /**
     * 保存作业
     */
    @Override
    public int saveTask(TaskDto dto, List<UserEntity> studentList) {
        Connection conn = ConnUtil.getTransactionConnection();
        int taskId = 0;
        try {
            taskId=saveTask(conn,dto,studentList);
            conn.commit();
        } catch (SQLException e) {
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return taskId;
    }
    
    public int saveTask(Connection conn, TaskDto dto, List<UserEntity> studentList){
    	TpExam task = new TpExam();
        task.setPaperId(dto.getPaperId());
        task.setExamName(dto.getExamName());
        task.setStartTime(dto.getStartTime());
        task.setEndTime(dto.getEndTime());
        task.setType(dto.getType());
        task.setStatus(dto.getStatus());
        task.setTeacherId(dto.getTeacherId());
        task.setCreateTime(new Date());
//		task.setUpdateTime(new Date());
        //取学科学段标签id存到作业表中
        task.setSubjectId(dto.getSubjectId());
        task.setRangeId(dto.getRangeId());
        //保存作业
        int taskId = taskDao.saveTask(conn, task);
        dto.setId(taskId);//存已经生成的作业id
        //保存作业与学生,作业与班级中间表数据
        saveTaskClassesAndStudent(conn, dto, studentList);
        return taskId;
    }

    //保存作业与学生,作业与班级中间表数据
    public void saveTaskClassesAndStudent(Connection conn, TaskDto dto, List<UserEntity> studentList) {
        //保存作业学生中间表tp_exam_student
        List<TpExamStudent> sList = new ArrayList<TpExamStudent>();
        for (int i = 0; i < studentList.size(); i++) {
            UserEntity u = studentList.get(i);
            TpExamStudent stu = new TpExamStudent();
            stu.setClassesId(u.getClassesId());
            stu.setExamId(dto.getId());
            stu.setCreateTime(new Date());
            stu.setPaperId(dto.getPaperId());
            stu.setStatus(TpExamStudent.STATUS_VALID);
            stu.setStudentId(u.getId());
            stu.setTeacherId(dto.getTeacherId());
            stu.setType(TpExamStudent.TYPE_TASK);
            stu.setSubjectId(dto.getSubjectId());
            sList.add(stu);
        }
        if (sList != null && sList.size() > 0) {
            taskDao.saveTaskStudent(conn, sList);
        }
    }

    /**
     * 修改作业
     */
    @Override
    public int modifySaveTask(TaskDto dto, List<UserEntity> studentList) {
        Connection conn = ConnUtil.getTransactionConnection();
        int taskId = 0;
        try {
        	taskDao.deleteTask(conn,dto.getId(), TpExam.EXAM_STATUS_DELETE);//删除原作业
        	taskDao.deleteTpExamStudent(conn, dto);//删除原作业与学生关联
            //保存作业与学生,作业与班级中间表数据
        	taskId = saveTask(conn,dto,studentList);
            conn.commit();
        } catch (SQLException e) {
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return taskId;
    }

    /**
     * 查询某老师某作业对应的班级或分组列表
     */
    @Override
    public List<Classes> findClassesByTaskId(int taskId, int teacherId) {
        return taskDao.findClassesByTaskId(taskId, teacherId);
    }

    /**
     * 查询已完成作业的学生列表
     */
    @Override
    public List<TpExamResult> findStudentByExamResult(TaskPager t) {
        List<TpExamResult> examResultList = taskDao.findStudentByExamResult(t);
        if (examResultList != null && examResultList.size() > 0) {
            for (TpExamResult er : examResultList) {
                parseExamResultRate(er);
            }
        }
        return examResultList;
    }

    /**
     * 查询作业对应的学生总人数
     */
    @Override
    public List<TaskStudentNum> findStudentNumByTaskIds(int userId, String ids) {
        return taskDao.findStudentNumByTaskIds(userId, ids);
    }

    /**
     * 查询已经完成作业的学生人数
     */
    @Override
    public List<TaskStudentNum> findCompStudentNumByTaskIds(int userId, String ids) {
        return taskDao.findCompStudentNumByTaskIds(userId, ids);
    }

    /**
     * 查询作业对应班级列表
     */
    @Override
    public List<TaskClassesName> searchClassesByTaskId(int userId, String ids) {
        return taskDao.searchClassesByTaskId(userId, ids);
    }

    /**
     * 查询题目id和题目对应的答错人及人数集合
     */
    @Override
    public List<QuestionErrUser> findQuestionErrAnswerUser(TaskPager t) {
        return taskDao.findQuestionErrAnswerUser(t);
    }

    /**
     * 查询题目对应的未作答人及人数集合
     */
    @Override
    public List<QuestionErrUser> findQuestionNoAnswerUser(TaskPager t) {
        return taskDao.findQuestionNoAnswerUser(t);
    }

    /**
     * 查询题目平均得分率和平均得分
     */
    @Override
    public List<QuestionErrUser> findQuestionAvgScore(TaskPager t) {
        return taskDao.findQuestionAvgScore(t);
    }

    /**
     * 查询提交作业的学生按班级id
     */
    @Override
    public List<UserEntity> findStudentResultByClassesId(TaskPager t) {
        return taskDao.findStudentResultByClassesId(t);
    }

    /**
     * 查询未提交作业的学生
     */
    @Override
    public List<TpExamResult> findStudentNoResultByClassesId(TaskPager t) {
        return taskDao.findStudentNoResultByClassesId(t);
    }

    /**
     * 查询老师拥有的班级列表
     */
    @Override
    public List<Classes> findClassesByUserId(UserEntity ue) {
        List<Classes> clist = taskDao.findClassesByUserId(ue);
        if (clist != null) {
            for (int i = 0; i < clist.size(); i++) {
                Classes c = clist.get(i);
//				List<Classes> glist = taskDao.findGroupByClassesId(c.getId());
                List<Classes> glist = classRoomDao.findClassesGroupByTeacherId(ue.getId(), c.getId());//没有学生的小组去掉
                c.setGroupList(glist);
            }
        }
        return clist;
    }

    /**
     * 通过班级id集合查询班级列表
     */
    @Override
    public List<Classes> findClassesByIds(String classesIds) {
        List<Classes> list = null;
        if (StringUtils.isNotBlank(classesIds)) {
            list = taskDao.findClassesByIds(classesIds);
        }
        return list;
    }

    /**
     * 撤回作业
     */
    @Override
    public int revokeTask(int taskId) {
        return taskDao.revokeTask(taskId, TpExam.EXAM_STATUS_REVOKE);
    }

    /**
     * 删除作业
     */
    @Override
    public int deleteTask(int taskId) {
        return taskDao.deleteTask(taskId, TpExam.EXAM_STATUS_DELETE);
    }

    /**
     * 查找班级下的学生(有效的)
     */
    @Override
    public List<UserEntity> findStudentByClassesIds(String classesIds) {
        List<UserEntity> list = null;
        if (StringUtils.isNotBlank(classesIds)) {
            list = taskDao.findStudentByClassesIds(classesIds);
        }
        return list;
    }

    /**
     * 查询te_paper表数据
     */
    @Override
    public TestPaper findPaperById(int paperId) {
        return taskDao.findPaperById(paperId);
    }

    /**
     * 查询学生所属班级
     */
    @Override
    public List<Classes> findClassesByStudentId(UserEntity ue) {
        return taskDao.findClassesByStudentId(ue);
    }

    /**
     * 查询学生对应的班级及老师拥有的学科
     */
    @Override
    public List<TeacherBookVersion> findClassesTeacherSubject(UserEntity ue, int type) {
        List<TpExamStudent> tpExamList = classRoomDao.findSubjectByTpExam(ue.getId(), type);
        List<TeacherBookVersion> teacherBookVersionList = new ArrayList<TeacherBookVersion>();
        TeacherBookVersion tb;
        List<Dictionary> dictionarieList = DataDictionaryUtil.getInstance().getDataDictionaryListByType(1);
        for (Dictionary dictionary : dictionarieList) {
            for (TpExamStudent tpExamStudent : tpExamList) {
                if (dictionary.getValue().equals(tpExamStudent.getSubjectId())) {
                    tb = new TeacherBookVersion();
                    tb.setSubjectId(dictionary.getValue());
                    tb.setSubjectName(dictionary.getName());
                    teacherBookVersionList.add(tb);
                    break;
                }
            }
        }
        return teacherBookVersionList;
    }

    /**
     * 查询题目 根据错误率 作业讲评
     */
    @Override
    public List<QuestionErrUser> findAllResultDetail(TaskPager taskPager) {
        return taskDao.findAllResultDetail(taskPager);
    }

    /**
     * 查询考试by考试id
     */
    @Override
    public TpExam queryExamByIdCache(Integer examId) {
        TpExam exam = CacheTools.getCache(ConstantTe.PROCESS_EXAMPAPER_EXAMID + examId, TpExam.class);
        if (exam == null) {
            exam = queryExamById(examId);
            CacheTools.addCache(ConstantTe.PROCESS_EXAMPAPER_EXAMID + examId, ConstantTe.CACHE_TIME, exam);
        }
        return exam;
    }

    /**
     * 查询考试by考试id
     */
    private TpExam queryExamById(Integer examId) {
        return taskDao.queryExamById(examId);
    }

    /**
     * 更新考试结果表老师浏览状态：由未查看改成已查看
     *
     * @param t
     */
    @Override
    public void updateTeExamResultTeacherView(TaskPager t) {
        taskDao.updateTeExamResultTeacherView(t);
    }

    /**
     * 查询学生id集合，通过班级id
     */
    @Override
    public String findStudentIdsByClassesId(int classId) {
        List<Integer> list = taskDao.findStudentIdsByClassesId(classId);
        String studentIds = "";
        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i);
            studentIds = studentIds + "," + id;
        }
        if (StringUtils.isNotBlank(studentIds)) {
            studentIds = studentIds.substring(1);
        }
        return studentIds;
    }

    /**
     * 题目得分率
     */
    @Override
    public List<QuestionErrUser> findStudentScoreRate(TaskPager t) {
        return taskDao.findStudentScoreRate(t);
    }

    /**
     * 成绩分布数据
     */
    @Override
    public List<TpExamResult> findStudentScore(TaskPager t) {
        return taskDao.findStudentScore(t);
    }

    /**
     * 知识点得分率
     * 题目得分情况数据
     */
    @Override
    public List<TpExamResultDetail> findStudentQuestionScore(TaskPager t) {
        return taskDao.findStudentQuestionScore(t);
    }

    /**
     * 查询学生用户
     */
    @Override
    public UserEntity findUserById(Integer studentId) {
        return taskDao.findUserById(studentId);
    }

    /**
     * 根据结果id
     * 查看结果明细中的错题id集合
     */
    @Override
    public List<QuestionErrUser> findTpExamResultDetailQuestionIds(int rid) {
        return taskDao.findTpExamResultDetailQuestionIds(rid);
    }

    /**
     * 学生浏览状态更新  更新为学生已查看评语
     *
     * @param resultId
     */
    @Override
    public void updateTaskStudentView(int resultId) {
        taskDao.updateTaskStudentView(resultId);
    }

    @Override
    public StudentResult findDetailByResult(Integer resultId) {
        StudentResult sr = new StudentResult();
        List<TpExamResultDetail> detailList = new ArrayList<TpExamResultDetail>();
        List<TpExamResultDetail> alldetailList = taskDao.findDetailByResult(resultId);
        if (alldetailList != null && alldetailList.size() > 0) {
            for (TpExamResultDetail erds : alldetailList) {
                if (erds.getTeId().intValue() == 0) {
                    for (int i = 0; i < alldetailList.size(); i++) {
                        TpExamResultDetail childErd = alldetailList.get(i);
                        //封装小题结果
                        if (erds.getQuestionId().equals(childErd.getTeId())) {
                            erds.getSubDetails().add(childErd);
                        }
                    }
                    //拿到封装好的大题结果明细
                    detailList.add(erds);
                }
            }
        }
        int allNum = detailList.size();
        int rightNum = 0;
        if (allNum > 0) {
            for (TpExamResultDetail erd : detailList) {
                if (erd.getSubDetails().size() > 0) {
                    int subRightNum = 0;//子题做对题数
                    for (TpExamResultDetail sub : erd.getSubDetails()) {
                        if (sub.getResultAnswer() == TpExamResultDetail.RESULT_ANSWER_CORRECT) {
                            subRightNum++;
                        }
                    }
                    if (erd.getSubDetails().size() == subRightNum) {
                        //子题全对，大题才对
                        rightNum++;
                    }
                } else {
                    //没有子题
                    if (erd.getResultAnswer() == TpExamResultDetail.RESULT_ANSWER_CORRECT) {
                        rightNum++;
                    }
                }
            }
        }
        sr.setCorrectNum(rightNum);
        sr.setErrorNum(allNum - rightNum);
        BigDecimal b1 = new BigDecimal(sr.getCorrectNum());
        BigDecimal b2 = new BigDecimal(allNum);
        BigDecimal b3 = b1.divide(b2, 3, BigDecimal.ROUND_HALF_EVEN);
        double rate = b3.doubleValue();
        DecimalFormat df = new DecimalFormat("0.##%");
        String r = df.format(rate);
        sr.setCorrectRate(r);
        sr.setCorrectRateNum(rate);
//        BigDecimal rateBig = new BigDecimal(rate);
//        BigDecimal rateBig100 = new BigDecimal(100);
//        sr.setCorrectRateNum(rateBig.multiply(rateBig100).doubleValue());
        return sr;
    }

    @Override
    public void deleteError(Integer questionId, Integer userId) {
        taskDao.deleteError(questionId, userId);
    }

    /**
     * 通过结果id查询作业
     */
    @Override
    public TpExam findTpExamByResultId(int rid) {
        return taskDao.findTpExamByResultId(rid);
    }

    /**
     * 按班级和错误题查询，该班的所有学生答题结果id
     */
	@Override
	public String findAllResultId(TaskPager taskPager) {
		String resultIds = taskDao.findAllResultId(taskPager);
		return resultIds;
	}

	@Override
	public List<TpExamResultDetail> findResultDetailTeId(String resultId) {
		return taskDao.findResultDetailTeId(resultId);
	}


}
