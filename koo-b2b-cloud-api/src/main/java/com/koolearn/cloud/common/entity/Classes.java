package com.koolearn.cloud.common.entity ;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.koolearn.cloud.util.TeacherCommonUtils;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;


@Entity
@Table(name = "classes")
public class Classes implements Serializable , Comparator<Object> {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_NOMAL = 0;//实体状态：默认0 正常 
	public static final int GRADUATE = 0;//实体状态：默认0 正常 
	public static final int TYPE_GROUP = 3;//默认0：type=0 行政班   type=1 学科班、type=3班级小组

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 班级名称 |  当type=3 时为小组名称 */
	@Column(name = "class_name")
	private String className;
	/** 全名（班级名+组名） */
	@Column(name = "full_name")
	private String fullName;
	/** 班级编码 */
	@Column(name = "class_code")
	private String classCode;
	/** 入学年份 */
	@Column(name = "year")
	private Integer year;
	/** 默认0：type=0 行政班   type=1 学科班、type=3班级小组 */
	@Column(name = "type")
	private Integer type;
	/** 默认0： 当type=3 时，为班级id */
	@Column(name = "parent_id")
	private Integer parentId;
	/** 毕业标识：默认0：0未毕业   1 毕业 */
	@Column(name = "graduate")
	private Integer graduate;
	/** 学段id */
	@Column(name = "range_id")
	private Integer rangeId;
	/** 学段name */
	@Column(name = "range_name")
	private String rangeName;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 学科name */
	@Column(name = "subject_name")
	private String subjectName;
	/** 班级或小组的创建者 */
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学校id */
	@Transient
	private String schoolName;
	/** 实体状态：默认0 正常 */
	@Column(name = "status")
	private Integer status;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
    /**标识年级*/
    @Column(name = "level")
    private int level;
	 /**班级人数*/
    @Transient
    private int studentNum;
	@Transient
	private List<Classes> groupList;
    /**是否可以删除的标识*/
    @Transient
    private int isDeleteOk;//1：可以删除 ，2：不可以删除
    /**老师姓名*/
    @Transient
    private String teacherName;
    /**修改班级时,是否选中班级*/
    @Transient
    private Boolean checked;
    /**班级类型名称*/
    @Transient
    private String typeName;
    @Transient
    private boolean isOk = true;
    /** 班级的学科,这里同班级左侧中的学科相同(即学科班显示班级班级对应的学科,行政班显示当前老师当前班级学段的所有学科) */
    @Transient
    private List<TeacherBookVersion > teacherBookVersionList;
    /**标识班级下是否有最新动态 ，1：有 */
    @Transient
    private int hasDynamic;
    /**学校毕业时间*/
    @Transient
    private int outTime;
    @Transient
    private JSONArray joinUserIdArray;//合心：加入改班级的用户id
    /**
     * 老师人数
     */
    @Transient
    private int teacherNum;
    /**
     * 年级
     */
    @Transient
    private String levelName;

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public List<TeacherBookVersion> getTeacherBookVersionList() {
		return teacherBookVersionList;
	}

	public void setTeacherBookVersionList(
			List<TeacherBookVersion> teacherBookVersionList) {
		this.teacherBookVersionList = teacherBookVersionList;
	}

    public int getHasDynamic() {
        return hasDynamic;
    }

    public void setHasDynamic(int hasDynamic) {
        this.hasDynamic = hasDynamic;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClassName() {
        if( isOk ){
            return TeacherCommonUtils.getClassesName( className , getYear() , getRangeName() ,getOutTime() );
        }
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getGraduate() {
		return graduate;
	}
	public void setGraduate(Integer graduate) {
		this.graduate = graduate;
	}
	public Integer getRangeId() {
		return rangeId;
	}
	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public int getStudentNum() {
		return studentNum;
	}
	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}
	public List<Classes> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Classes> groupList) {
		this.groupList = groupList;
	}
	public int getIsDeleteOk() {
		return isDeleteOk;
	}
	public void setIsDeleteOk(int isDeleteOk) {
		this.isDeleteOk = isDeleteOk;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


    @Override
    public int compare(Object o1, Object o2) {
        if( null != o1 && null != o2 && o1 instanceof Classes && o2 instanceof Classes ){
            Classes classes1 = (Classes )o1;
            Classes classes2 = (Classes )o2;
            Integer level1 = classes1.getLevel();
            Integer level2 = classes2.getLevel();

            if( null != level1 && null != level2 ){
                if( level1.intValue() > level2.intValue() ) {
                    return 1;
                }else if( level1.intValue() == level2.intValue() ) {
                    return 0;
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    public JSONArray getJoinUserIdArray() {
        return joinUserIdArray;
    }

    public void setJoinUserIdArray(JSONArray joinUserIdArray) {
        this.joinUserIdArray = joinUserIdArray;
    }
}
