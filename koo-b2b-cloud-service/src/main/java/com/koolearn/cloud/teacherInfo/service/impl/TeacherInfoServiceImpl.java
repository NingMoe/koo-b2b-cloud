package com.koolearn.cloud.teacherInfo.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacher.service.TeacherInfoService;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherInfoDao;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.sso.service.IOpenService;

/**
 * Created by wujun
 */
public class TeacherInfoServiceImpl implements TeacherInfoService{
	IOpenService iopenService;
	public void setIopenService(IOpenService iopenService) {
		this.iopenService = iopenService;
	}

	TeacherInfoDao teacherInfoDao;
	public void setTeacherInfoDao(TeacherInfoDao teacherInfoDao) {
		this.teacherInfoDao = teacherInfoDao;
	}
	private TeacherDataMaintainDao teacherDataMaintainDao;
	public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }

	/**
	 * 个人中心--我的班级，查询老师所属班级
	 */
	@Override
	public List<Classes> findMyClassesById(UserEntity ue) {
		return teacherInfoDao.findMyClassesById(ue);
	}

	/**
	 * 个人中心-- 老师基本信息
	 */
	@Override
	public UserEntity findTeacherBaseInfo(UserEntity ue) {
		UserEntity p = teacherInfoDao.findTeacherBaseInfo(ue);
		return p;
	}

	/**
	 * 个人中心-- 老师担任学科
	 */
	@Override
	public List<TeacherBookVersion> findTeacherBookVersion(UserEntity ue) {
		return teacherInfoDao.findTeacherBookVersion(ue);
	}

	/**
	 * 个人中心--修改手机号
	 */
	@Override
	public int updateUserMobile(UserEntity user) {
		int i = 0;
		Connection conn = ConnUtil.getTransactionConnection();
		try {
			i = teacherInfoDao.updateUserMobile(conn,user);
			iopenService.updateMobile(user.getUserId(), user.getMobile());
			conn.commit();
		} catch (SQLException e) {
			ConnUtil.rollbackConnection(conn);
			e.printStackTrace();
		}finally{
			ConnUtil.closeConnection(conn);
		}
		return i;
	}

	/**
	 * 个人中心--修改邮箱
	 */
	@Override
	public int updateUserEmail(UserEntity user) {
		int i = 0;
		Connection conn = ConnUtil.getTransactionConnection();
		try {
			i = teacherInfoDao.updateUserEmail(conn,user);
			iopenService.updateEmail(user.getUserId(), user.getEmail(), false);
			conn.commit();
		} catch (SQLException e) {
			ConnUtil.rollbackConnection(conn);
			e.printStackTrace();
		}finally{
			ConnUtil.closeConnection(conn);
		}
		return i;
	}

	/**
	 * 个人中心--修改密码
	 */
	@Override
	public void updatePassword(Integer userId, String oldPassword,
			String password) {
		iopenService.updatePassword(userId, oldPassword, password);
	}

	/**
	 * 个人中心--更新姓名
	 */
	@Override
	public void updateRealName(Integer userId, String realName) {
		teacherInfoDao.updateRealName(userId,realName);
	}

	/**
	 * 插入学科学段教材版本
	 */
	@Override
	public int insertTeacherBookVersion(TeacherBookVersion bookVersion) {
		int id = 0;
		Connection conn = null;
		try {
			conn = ConnUtil.getTransactionConnection();
			id = teacherDataMaintainDao.insertTeacherBookVersion( conn ,bookVersion );
			conn.commit();
		} catch (SQLException e) {
			ConnUtil.rollbackConnection(conn);
			e.printStackTrace();
		}finally{
			ConnUtil.closeConnection(conn);
		}
		return id;
	}

	/**
	 * 个人中心--更新头像地址
	 */
	@Override
	public String updateIco(Integer id, String ico) {
		int count = teacherInfoDao.updateIco(id,ico);
		if(count>0){
			return ico;
		}
		return "";
	}

	/**
	 * 查询学生所属班级的学校信息
	 */
	@Override
	public List<School> findStudentSchool(UserEntity ue) {
		return teacherInfoDao.findStudentSchool(ue.getId());
	}

	/**
	 * 查询老师加入的班级
	 */
	@Override
	public List<Integer> findTeacherJoinClasses(UserEntity ue) {
		return teacherInfoDao.findTeacherJoinClasses(ue);
	}
	
}
