package com.koolearn.cloud.teacherInfo.dao;

import java.sql.Connection;
import java.util.List;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.entity.PersonInfo;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.GeneratedKey;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

@DAO(dbtype=DbType.MYSQL,source = GlobalConstant.MYSQL_DATASOURCE)
public interface TeacherInfoDao {

	@SQL(type=SQLType.READ)
	List<Classes> findMyClassesById(@SQLParam("ue") UserEntity ue);

	@SQL(type=SQLType.READ)
	UserEntity findTeacherBaseInfo(@SQLParam("ue") UserEntity ue);

	@SQL(type=SQLType.READ)
	List<TeacherBookVersion> findTeacherBookVersion(@SQLParam("ue") UserEntity ue);

	@SQL(" update user u set u.mobile=:user.mobile where u.id=:user.id ")
	int updateUserMobile(Connection conn,@SQLParam("user") UserEntity user);
	
	@SQL(" update user u set u.email=:user.email where u.id=:user.id ")
	int updateUserEmail(Connection conn,@SQLParam("user") UserEntity user);

	@SQL(" update user u set u.real_name=:realName where u.id=:userId ")
	void updateRealName(@SQLParam("userId") Integer userId,@SQLParam("realName") String realName);

	@SQL(" update user set ico=:ico where id=:id ")
	int updateIco(@SQLParam("id") Integer id,@SQLParam("ico") String ico);

	/**
     *
	 * 查询学生所属班级的学校信息
	 */
	@SQL(" select s.*,c.school_id,cs.* From classes_student cs left join classes c on c.id=cs.classes_id " +
			" left join school s on c.school_id=s.id " +
			" where cs.student_id=:id and cs.status="+ClassesStudent.STATUS_NOMAL)
	List<School> findStudentSchool(@SQLParam("id") Integer id);

	/**
	 * 查询老师加入的班级
	 */
	@SQL(" select ct.classes_id from classes_teacher ct where ct.teacher_id=:ue.id and ct.status="+ClassesTeacher.STATUS_VALID)
	List<Integer> findTeacherJoinClasses(@SQLParam("ue") UserEntity ue);
	
}
