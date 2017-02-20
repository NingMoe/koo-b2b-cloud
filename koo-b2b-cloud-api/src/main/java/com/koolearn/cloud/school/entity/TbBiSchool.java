package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
学校增长统计表、学校详细数据\r\nteacher_book_version、 classes
*/
@Entity
@Table(name = "tb_bi_school")
public class TbBiSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学校 */
	@Column(name = "school_name")
	private String schoolName;
	/** 学校学段:1 : 大学， 2：高中， 6:初中 ，8 ： 小学 , 9: 九年一贯制，7：职校 */
	@Column(name = "grade_id")
	private Integer gradeId;
	/** 新成的所有学校(小，初，高） */
	@Column(name = "new_all_school")
	private Integer newAllSchool;
	/** 新生成的小学 */
	@Column(name = "new_all_first")
	private Integer newAllFirst;
	/** 新生成的初中 */
	@Column(name = "new_all_second")
	private Integer newAllSecond;
	/** 新生成的高中 */
	@Column(name = "new_all_third")
	private Integer newAllThird;
	/** 累计的全部学校 */
	@Column(name = "whole_school")
	private Integer wholeSchool;
	/** 累计小学的数量 */
	@Column(name = "whole_first")
	private Integer wholeFirst;
	/** 累计初中学校 */
	@Column(name = "whole_second")
	private Integer wholeSecond;
	/** 累计生成的高中 */
	@Column(name = "whole_third")
	private Integer wholeThird;
	/** 每日该学校学段下所有老师的累计登录次数 */
	@Column(name = "teacher_login_num")
	private Integer teacherLoginNum;
	/** 每日该学校学段下所有学生的累计登录次数 */
	@Column(name = "student_login_num")
	private Integer studentLoginNum;
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
	
	public Integer getNewAllSchool() {
		return newAllSchool;
	}

	public void setNewAllSchool(Integer newAllSchool) {
		this.newAllSchool = newAllSchool;
	}
	
	public Integer getNewAllFirst() {
		return newAllFirst;
	}

	public void setNewAllFirst(Integer newAllFirst) {
		this.newAllFirst = newAllFirst;
	}
	
	public Integer getNewAllSecond() {
		return newAllSecond;
	}

	public void setNewAllSecond(Integer newAllSecond) {
		this.newAllSecond = newAllSecond;
	}
	
	public Integer getNewAllThird() {
		return newAllThird;
	}

	public void setNewAllThird(Integer newAllThird) {
		this.newAllThird = newAllThird;
	}
	
	public Integer getWholeSchool() {
		return wholeSchool;
	}

	public void setWholeSchool(Integer wholeSchool) {
		this.wholeSchool = wholeSchool;
	}
	
	public Integer getWholeFirst() {
		return wholeFirst;
	}

	public void setWholeFirst(Integer wholeFirst) {
		this.wholeFirst = wholeFirst;
	}
	
	public Integer getWholeSecond() {
		return wholeSecond;
	}

	public void setWholeSecond(Integer wholeSecond) {
		this.wholeSecond = wholeSecond;
	}
	
	public Integer getWholeThird() {
		return wholeThird;
	}

	public void setWholeThird(Integer wholeThird) {
		this.wholeThird = wholeThird;
	}
	
	public Integer getTeacherLoginNum() {
		return teacherLoginNum;
	}

	public void setTeacherLoginNum(Integer teacherLoginNum) {
		this.teacherLoginNum = teacherLoginNum;
	}
	
	public Integer getStudentLoginNum() {
		return studentLoginNum;
	}

	public void setStudentLoginNum(Integer studentLoginNum) {
		this.studentLoginNum = studentLoginNum;
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
