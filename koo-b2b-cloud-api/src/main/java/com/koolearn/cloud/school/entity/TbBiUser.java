package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
用户增长数据统计表
*/
@Entity
@Table(name = "tb_bi_user")
public class TbBiUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学校名称 */
	@Column(name = "school_name")
	private String schoolName;
	/** 学校学段:1 : 大学， 2：高中， 6:初中 ，8 ： 小学 , 9: 九年一贯制，7：职校 */
	@Column(name = "grade_id")
	private Integer gradeId;
	/** 新增的全部用户 */
	@Column(name = "new_all_user")
	private Integer newAllUser;
	/** 新增教师用户 */
	@Column(name = "new_teacher")
	private Integer newTeacher;
	/** 新增学生用户 */
	@Column(name = "new_student")
	private Integer newStudent;
	/** 新增的家长 */
	@Column(name = "new_parent")
	private Integer newParent;
	/** 累计的全部用户 */
	@Column(name = "whole_user")
	private Integer wholeUser;
	/** 累计教师用户 */
	@Column(name = "whole_teacher")
	private Integer wholeTeacher;
	/** 累计学生用户 */
	@Column(name = "whole_student")
	private Integer wholeStudent;
	/** 累计家长 */
	@Column(name = "whole_parent")
	private Integer wholeParent;
	/** 当前月份 */
	@Column(name = "month")
	private Integer month;
	/** 备注 */
	@Column(name = "remark")
	private String remark;
	/** 创建日期,每日执行定时任务 */
	@Column(name = "create_day")
	private Date createDay;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	
	public Integer getNewAllUser() {
		return newAllUser;
	}

	public void setNewAllUser(Integer newAllUser) {
		this.newAllUser = newAllUser;
	}
	
	public Integer getNewTeacher() {
		return newTeacher;
	}

	public void setNewTeacher(Integer newTeacher) {
		this.newTeacher = newTeacher;
	}
	
	public Integer getNewStudent() {
		return newStudent;
	}

	public void setNewStudent(Integer newStudent) {
		this.newStudent = newStudent;
	}
	
	public Integer getNewParent() {
		return newParent;
	}

	public void setNewParent(Integer newParent) {
		this.newParent = newParent;
	}
	
	public Integer getWholeUser() {
		return wholeUser;
	}

	public void setWholeUser(Integer wholeUser) {
		this.wholeUser = wholeUser;
	}
	
	public Integer getWholeTeacher() {
		return wholeTeacher;
	}

	public void setWholeTeacher(Integer wholeTeacher) {
		this.wholeTeacher = wholeTeacher;
	}
	
	public Integer getWholeStudent() {
		return wholeStudent;
	}

	public void setWholeStudent(Integer wholeStudent) {
		this.wholeStudent = wholeStudent;
	}
	
	public Integer getWholeParent() {
		return wholeParent;
	}

	public void setWholeParent(Integer wholeParent) {
		this.wholeParent = wholeParent;
	}
	
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}
}
