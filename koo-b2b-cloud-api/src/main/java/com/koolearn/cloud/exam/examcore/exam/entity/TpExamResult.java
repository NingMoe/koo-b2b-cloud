package com.koolearn.cloud.exam.examcore.exam.entity;
import java.io.Serializable;
import java.math.RoundingMode;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;

import java.text.NumberFormat;
import java.util.Date;

@Entity
@Table(name = "te_exam_result")
public class TpExamResult implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_PRE = 0;//0,未开始 1.考试中    2.已交卷'
	public static final int STATUS_PROCESSING = 1;//0,未开始 1.考试中    2.已交卷'
	public static final int STATUS_COMPLETE = 2;//0,未开始 1.考试中    2.已交卷'
	public static final int TEACHER_VIEW_TWO = 2;//'老师浏览状态  1.老师未浏览  2.老师已浏览'
	public static final int STUDENT_VIEW_ZERO = 0;//学生浏览状态  1.学生未浏览  2.学生已浏览 
	public static final int STUDENT_VIEW_ONE = 1;//学生浏览状态  1.学生未浏览  2.学生已浏览 
	public static final int STUDENT_VIEW_TWO = 2;//学生浏览状态  1.学生未浏览  2.学生已浏览 
	public static final String IS_REPLY_YES = "1";//是否批阅 0.未批阅 1.已批阅
	

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 考试name */
	@Column(name = "exam_name")
	private String examName;
	/** 考试ID */
	@Column(name = "exam_id")
	private Integer examId;
	/** 学生id */
	@Column(name = "student_id")
	private Integer studentId;
	/** 总分 */
	@Column(name = "points")
	private Double points;
	/** 成绩 */
	@Column(name = "score")
	private Double score;
	/** 开始时间 */
	@Column(name = "begin_time")
	private Date beginTime;
	/** 所用时长，单位：秒 */
	@Column(name = "time_off")
	private Integer timeOff;
	/** 状态: 0,未开始 1.考试中    2.已交卷 */
	@Column(name = "status")
	private Integer status;
	/** 客观题得分 */
	@Column(name = "objectives_score")
	private Double objectivesScore;
	/** 是否批阅 */
	@Column(name = "isreply")
	private String isreply;
	/** 作业批阅内容 */
	@Column(name = "reply")
	private String reply;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** 学生浏览状态  1.学生未浏览  2.学生已浏览  */
	@Column(name = "student_view")
	private int studentView;
	/** 老师浏览状态  1.老师未浏览  2.老师已浏览*/
	@Column(name = "teacher_view")
	private int teacherView;
	/** 学生编码  */
	@Transient
	private String studentCode;
	/** 学生真实姓名 */
	@Transient
	private String realName;
	/** 学生排名 */
	@Transient
	private String rank;
	/** 学生得分率 % */
	@Transient
	private String rate;
    /** 学生得分率 不保留小数点 */
    @Transient
    private Double rateDouble;
	/** 完成时间 */
	@Transient
	private Date completeTime;
	/** 作业类型 默认1： 1作业  2课堂   20课堂作业 3.学生自测 4学生错题复习 */
	@Transient
	private int examType;
	
	public int getExamType() {
		return examType;
	}

	public void setExamType(int examType) {
		this.examType = examType;
	}

	public int getStudentView() {
		return studentView;
	}

	public void setStudentView(int studentView) {
		this.studentView = studentView;
	}

	public int getTeacherView() {
		return teacherView;
	}

	public void setTeacherView(int teacherView) {
		this.teacherView = teacherView;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}
	
	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	public Integer getTimeOff() {
		return timeOff;
	}

	public void setTimeOff(Integer timeOff) {
		this.timeOff = timeOff;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Double getObjectivesScore() {
		return objectivesScore;
	}

	public void setObjectivesScore(Double objectivesScore)
	{
		if (null != objectivesScore)
		{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			this.objectivesScore = Double.valueOf(nf.format(objectivesScore));
		}
		else
		{
			this.objectivesScore = objectivesScore;
		}
	}
	
	public String getIsreply() {
		return isreply;
	}

	public void setIsreply(String isreply) {
		this.isreply = isreply;
	}
	
	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public Double getRateDouble() {
        return rateDouble;
    }

    public void setRateDouble(Double rateDouble) {
        this.rateDouble = rateDouble;
    }
}
