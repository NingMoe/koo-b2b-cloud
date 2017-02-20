package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import java.math.RoundingMode;

import com.koolearn.cloud.util.ParseDate;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "te_paper")
public class TestPaper implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
    /**考试系统试卷id*/
    @Column(name = "exam_paper_id")
    private Integer examPaperId;
	/** 名称 */
	@Column(name = "paper_name")
	private String paperName;
	/** 编码 */
	@Column(name = "paper_code")
	private String paperCode;
	/** 描述 */
	@Column(name = "descript")
	private String descript;
	/** 总分 */
	@Column(name = "points")
	private Double points;
    /**使用次数**/
    @Column(name = "use_times")
	private Integer useTimes;
    /**浏览次数*/
    @Column(name = "browse_times")
	private Integer browseTimes;
    /**创建老师,同步试卷为0*/
    @Column(name = "teacher_id")
	private Integer teacherId;
    /**学校id，同步试卷0*/
    @Column(name = "school_id")
	private Integer schoolId;
    /** 创建人 */
    @Column(name = "teacher_name")
    private String teacherName;
    /** 创建时间 */
    @Column(name = "create_time")
    private Date createTime;
    @Transient
    private String createTimeStr;
    /**更新时间 */
    @Column(name = "update_time")
    private Date updateTime;
    /**0：新东方同步试卷 1:老师创建试卷2.学生自测试卷*/
    @Column(name = "fromwh")
	private Integer fromwh;
    /**记录加入我的试卷库的老师id _ 分隔*/
    @Column(name = "joinself_user")
	private String joinselfUser;
    /**老师组卷类型： 1 进度点组卷 2知识点组卷  3新东方试卷组卷  4智能组卷*/
    @Column(name = "`type`")
	private Integer type;
    /**默认0：可用  1 删除*/
    @Column(name = "`status`")
	private Integer status;
    @Column(name = "tag_full_path")
    private String tagFullPath;
    @Column(name = "`subject_id`")
    private Integer subjectId;//知识点学科
    @Column(name = "`subject`")
    private String subject;
    @Column(name = "`range`")
    private String range;//知识点学段
    @Column(name = "range_id")
    private Integer rangeId;
    @Column(name = "question_min_count")
    private Integer questionMinCount;//以小题计算题数
    @Column(name = "question_count")
    private Integer questionCount;//以大题计算题数

   @Transient
    private List<PaperQuestionType> paperQuestionTypeList=new ArrayList<PaperQuestionType>();
   @Transient
   private QuestionBar questionBar=new QuestionBar();
    @Transient
    private String searchContent;// 关键字搜索字段
    @Transient
    private boolean join=false;//是否加入我的试卷库
    @Transient
    private boolean canDel=false;//是否有删除权限
    @Transient
    private String downloadPaperName;//试卷下载名称
    @Transient
    private String downloadType;//试卷下载类型 GlobalConstant.DOWNLOAD_PAPER_JIEX_LAST
    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public boolean isCanDel() {
        return canDel;
    }

    public void setCanDel(boolean canDel) {
        this.canDel = canDel;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(Integer examPaperId) {
        this.examPaperId = examPaperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Integer getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(Integer browseTimes) {
        this.browseTimes = browseTimes;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public String getTagFullPath() {
        return tagFullPath;
    }

    public void setTagFullPath(String tagFullPath) {
        this.tagFullPath = tagFullPath;
    }

    public Double getPoints() {
        if (null != this.points)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            return Double.valueOf(nf.format(this.points));
        }
        else
        {
            return this.points;
        }
    }

    public void setPoints(Double points) {
        if (null != points)
        {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            nf.setRoundingMode(RoundingMode.HALF_UP);
            this.points = Double.valueOf(nf.format(points));
        }
        else
        {
            this.points = points;
        }
    }

    public String getCreateTimeStr() {
        if(this.createTime==null )return ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);
        return ParseDate.formatByDate(this.createTime,ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = ParseDate.formatByDate(this.createTime,ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);;
    }

    public List<PaperQuestionType> getPaperQuestionTypeList() {
        return paperQuestionTypeList;
    }

    public void setPaperQuestionTypeList(List<PaperQuestionType> paperQuestionTypeList) {
        this.paperQuestionTypeList = paperQuestionTypeList;
    }

    public Integer getFromwh() {
        return fromwh;
    }

    public void setFromwh(Integer fromwh) {
        this.fromwh = fromwh;
    }

    public QuestionBar getQuestionBar() {
        return questionBar;
    }

    public void setQuestionBar(QuestionBar questionBar) {
        this.questionBar = questionBar;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public String getJoinselfUser() {
        return joinselfUser;
    }

    public void setJoinselfUser(String joinselfUser) {
        this.joinselfUser = joinselfUser;
    }

    public String getDownloadPaperName() {
        return downloadPaperName;
    }

    public void setDownloadPaperName(String downloadPaperName) {
        this.downloadPaperName = downloadPaperName;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getQuestionMinCount() {
        return questionMinCount;
    }

    public void setQuestionMinCount(Integer questionMinCount) {
        this.questionMinCount = questionMinCount;
    }
}
