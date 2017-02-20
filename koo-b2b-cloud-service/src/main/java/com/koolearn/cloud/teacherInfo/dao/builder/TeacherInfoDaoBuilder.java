package com.koolearn.cloud.teacherInfo.dao.builder;

import java.util.List;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;

public class TeacherInfoDaoBuilder implements AriesDynamicSqlBuilder{
	/**
	 * 个人中心--我的班级，查询老师所属班级
	 */
	public String findMyClassesById(UserEntity ue){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT	c.id,c.class_code,c.class_name,c.create_time,c.full_name,c.graduate,c.parent_id,c.range_id,c.range_name,c.school_id, ");
		sql.append(" c.`status`,c.subject_id,c.subject_name,c.teacher_id,c.type,c.`year`,count(cs.student_id) studentNum ");
		sql.append(" FROM classes c   ");
		sql.append(" INNER JOIN classes_teacher ct on c.id=ct.classes_id  ");
		sql.append(" and ct.teacher_id=:ue.id and ct.status=").append(ClassesTeacher.STATUS_VALID);
		sql.append(" left join classes_student cs on ct.classes_id=cs.classes_id and cs.status=").append(ClassesStudent.STATUS_NOMAL);
		sql.append(" WHERE c.STATUS =").append(Classes.STATUS_NOMAL).append(" AND c.type <>").append(Classes.TYPE_GROUP);
		sql.append(" GROUP BY c.id,c.class_code,c.class_name,c.create_time,c.full_name,c.graduate,c.parent_id,c.range_id,c.range_name,c.school_id, ");
		sql.append(" c.`status`,c.subject_id,c.subject_name,c.teacher_id,c.type,c.`year` ");
		sql.append(" ORDER BY c.graduate ASC,ct.id DESC ");
		System.out.println("findMyClassesById=  "+sql.toString());
		return sql.toString();
	}
	/**
	 * 个人中心-- 老师基本信息
	 */
	public String findTeacherBaseInfo(UserEntity ue){
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct u.* From user u  where  u.status=").append(UserEntity.USER_STATUS_VALID);
		sql.append(" and u.id=:ue.id");
		System.out.println("findTeacherBaseInfo=  "+sql.toString());
		return sql.toString();
	}
	/**
	 * 个人中心-- 老师担任学科
	 */
	public String findTeacherBookVersion(UserEntity ue){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from teacher_book_version tbv ");
		sql.append(" where status=").append(TeacherBookVersion.STATUS_VALID);
		sql.append(" and tbv.teacher_id=").append(ue.getId());
		sql.append(" order by tbv.id desc ");
		System.out.println("findTeacherBookVersion=  "+sql.toString());
		return sql.toString();
	}
}
