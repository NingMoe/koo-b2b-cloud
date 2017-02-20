package com.koolearn.cloud.exam.entity;

import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "tp_exam")
public class TpExam implements Serializable , Comparator<Object> {
	private static final long serialVersionUID = 1L;
	/*te_tag_object 对象类型（1：试卷，2：试题、3：考试/练习）*/
	public static final int TAG_OBJECT_PAPER = 1;
	/* 1作业  2课堂   20课堂作业 3学生自测  4学生错题复习*/
	public static final int EXAM_TYPE_TASK = 1;
	public static final int EXAM_TYPE_CLASS_TASK = 20;
	public static final int EXAM_TYPE_TASK_stuzc = 3;
	public static final int EXAM_TYPE_TASK_stufuxi = 4;
	/* 默认1：1.新建(修改) 2.撤回 3.删除 4.发布(翻转课堂)  */
	public static final int EXAM_STATUS_REVOKE = 2;
	/* 默认1：1.新建(修改) 2.撤回 3.删除 4.发布(翻转课堂)  */
	public static final int EXAM_STATUS_DELETE = 3;
	/* 默认1：1.新建(修改) 2.撤回 3.删除 4.发布(翻转课堂)  */
	public static final int EXAM_STATUS_PUT = 4;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 考试/作业 名称 (默认取试卷名称)  |  type=2为课堂名称 */
	@Column(name = "exam_name")
	private String examName;
	/** 默认0：试卷id */
	@Column(name = "paper_id")
	private Integer paperId;
	/** 开始时间 */
	@Column(name = "start_time")
	private Date startTime;
	/** 结束时间 */
	@Column(name = "end_time")
	private Date endTime;
	/** 默认0： 0作业、1考试   2课堂   20课堂作业 */
	@Column(name = "type")
	private Integer type;
	/** 默认1：1.新建(有效) 2.撤回 3.删除 */
	@Column(name = "status")
	private Integer status;
	/** 创建老师id */
	@Column(name = "teacher_id")
	private Integer teacherId;
	@Column(name = "tag_id")
	private Integer tagId;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** 课堂教学进度id */
	@Column(name = "teaching_point_id")
	private Integer teachingPointId;
	/** 课堂教学进度点名 */
	@Column(name = "teaching_point_name")
	private String teachingPointName;
	/** 进度点显示全路径 */
	@Column(name = "tag_full_path")
	private String tagFullPath;
	/** 进度点显示中文名称 */
	@Column(name = "tag_str")
	private String tagStr;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 学段id */
	@Column(name = "range_id")
	private Integer rangeId;
	/** 版本id */
	@Column(name = "book_version")
	private Integer bookVersion;
	/** 教材id */
	@Column(name = "obligatory_id")
	private Integer obligatoryId;
	/**班级人数*/
    @Transient
    private int studentNum;
	/**作业完成人数*/
    @Transient
    private int hadDoneNum;
	@Transient
    private String startTimeStr;
	@Transient
    private String endTimeStr;
	/**课堂显示班级**/
	@Transient
	private String classesStr;
	/**课堂结束状态 0结束1正常 **/
	@Transient
	private Integer finishStatus;
	/**学生完成时间 **/
	@Transient
	private Date finishTime;
	/** 课堂附件 **/
	@Transient
	private List<TpExamAttachment> attachments;

	/** 课堂学生 **/
	@Transient
	private List<TpExamStudent> students;
	/** 班级id **/
	@Transient
	private String classesIds;
	/** 完成率 **/
	@Transient
	private double rate;
    /**作业课堂完成进度描述 **/
    @Transient
    private String progressDis;

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getProgressDis() {
        return progressDis;
    }

    public void setProgressDis(String progressDis) {
        this.progressDis = progressDis;
    }

    public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Integer getBookVersion() {
		return bookVersion;
	}

	public void setBookVersion(Integer bookVersion) {
		this.bookVersion = bookVersion;
	}

	public Integer getObligatoryId() {
		return obligatoryId;
	}

	public void setObligatoryId(Integer obligatoryId) {
		this.obligatoryId = obligatoryId;
	}

	public String getClassesIds() {
		return classesIds;
	}

	public void setClassesIds(String classesIds) {
		this.classesIds = classesIds;
	}

	public List<TpExamStudent> getStudents() {
		return students;
	}

	public void setStudents(List<TpExamStudent> students) {
		this.students = students;
	}

	public List<TpExamAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TpExamAttachment> attachments) {
		this.attachments = attachments;
	}

	public Integer getRangeId() {
		return rangeId;
	}

	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public Integer getPaperId() {
		return paperId;
	}

	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
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

	public Integer getTeachingPointId() {
		return teachingPointId;
	}

	public void setTeachingPointId(Integer teachingPointId) {
		this.teachingPointId = teachingPointId;
	}

	public String getTeachingPointName() {
		return teachingPointName;
	}

	public void setTeachingPointName(String teachingPointName) {
		this.teachingPointName = teachingPointName;
	}

	public String getTagFullPath() {
		return tagFullPath;
	}

	public void setTagFullPath(String tagFullPath) {
		this.tagFullPath = tagFullPath;
	}

	public String getTagStr() {
		return tagStr;
	}

	public void setTagStr(String tagStr) {
		this.tagStr = tagStr;
	}

	public int getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}

	public int getHadDoneNum() {
		return hadDoneNum;
	}

	public void setHadDoneNum(int hadDoneNum) {
		this.hadDoneNum = hadDoneNum;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getClassesStr() {
		return classesStr;
	}

	public void setClassesStr(String classesStr) {
		this.classesStr = classesStr;
	}

    @Override
    public int compare(Object o1, Object o2) {
        if(   o1 !=null && o2 != null  ){
            TpExam tpExam1 = (TpExam )o1;
            TpExam tpExam2 = (TpExam )o2;
            long date1 = tpExam1.getEndTime().getTime();
            long date2 = tpExam2.getEndTime().getTime();

            if( 0 != date1 && 0 != date2 ){
                if( date1 < date2 ) {
                    return -1;
                }else if( date1 == date2 ) {
                    return 0;
                }else{
                    return 1;
                }
            } else{
                return 1;
            }
        }else{
            return 1;
        }
    }
}
