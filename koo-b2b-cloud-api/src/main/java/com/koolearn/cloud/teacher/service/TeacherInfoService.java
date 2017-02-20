package com.koolearn.cloud.teacher.service;

import java.util.List;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.entity.School;

public interface TeacherInfoService {

	/**
	 * 个人中心--我的班级，查询老师所属班级
	 * @param classesIds
	 * @return
	 */
	List<Classes> findMyClassesById(UserEntity ue);

	/**
	 * 个人中心-- 老师基本信息
	 * @param ue
	 * @return
	 */
	UserEntity findTeacherBaseInfo(UserEntity ue);

	/**
	 * 个人中心-- 老师担任学科
	 * @param ue
	 * @return
	 */
	List<TeacherBookVersion> findTeacherBookVersion(UserEntity ue);

	/**
	 * 个人中心--修改手机号
	 * @param user
	 * @return
	 */
	int updateUserMobile(UserEntity user);

	/**
	 * 个人中心--修改邮箱
	 * @param user
	 * @return
	 */
	int updateUserEmail(UserEntity user);

	/**
	 * 个人中心--修改密码
	 * @param user
	 * @return
	 */
	void updatePassword(Integer userId, String oldPassword, String password);
	/**
	 * 个人中心--更新姓名
	 * @param userId
	 * @param realName
	 */
	void updateRealName(Integer userId, String realName);

	/**
	 * 插入学科学段教材版本
	 * @param bookVersion
	 * @return
	 */
	int insertTeacherBookVersion(TeacherBookVersion bookVersion);

	/**
	 * 个人中心--更新头像地址
	 * @param id
	 * @param ico
	 * @return 
	 */
	String updateIco(Integer id, String ico);

	/**
	 * 查询学生所属班级的学校信息
	 * @param ue
	 * @return
	 */
	List<School> findStudentSchool(UserEntity ue);

	/**
	 * 查询老师加入的班级
	 * @param ue
	 * @return
	 */
	List<Integer> findTeacherJoinClasses(UserEntity ue);
	
}
