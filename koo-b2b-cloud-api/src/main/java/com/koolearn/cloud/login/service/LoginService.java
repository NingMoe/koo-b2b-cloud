package com.koolearn.cloud.login.service;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.teacher.entity.School;

import java.util.List;
import java.util.Map;

public interface LoginService {

	/**
	 * 插入用户信息
	 * @param ue
	 * @return
	 */
	int insertUser(UserEntity ue);

	/**
	 * 通过手机、邮箱、用户名查询用户数据，取出sso对应的用户信息
	 */
	UserEntity findUser(String mobileEmail);

	/**
	 * 更新user表数据
	 */
	public int updateUserEntity(UserEntity ue);

	/**
	 * 插入学生班级中间表数据，关联学生与班级
	 * @param update 
	 */
	int insertClassesStudent(ClassesStudent cs, UserEntity update);

	/**
	 * 查询学生的班级
	 * @param ue
	 * @return
	 */
	UserEntity findClassesStudent(UserEntity ue);
	/**
	 * 查询用户信息
	 * @param userName 
	 * @return
	 */
	UserMobi findMobileUser(String userName);

    /**
     * 根据用户ID查询 用户对象
     * @param userId
     * @return
     */
    User findUserByUserId(Integer userId);
    /**
     * 将SSO用户信息添加到本地库
     * @param user
     * @return
     */
    int addSSOUser(User user ) throws Exception;

    /**
     * 更新用户角色
     * @param integer
     * @param integer1
     * @return
     */
    int addUserRole(Integer integer, Integer integer1) throws Exception;

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    UserEntity findUserById(Integer userId);
    /**合心用户登录注册接口**/
    public Map<String,String> hexinLogin(UserEntity user);
    public String hexinAccountTeacher(UserEntity user);
    public String hexinAccountStudent(UserEntity user);
    public String hexinAccountStudentList(Classes classes ,UserEntity loginUser,List<User> userList );
    public String hexinAccountGroup(UserEntity user,Classes classes);
    public String hexinAccountRelation(UserEntity user,Classes classes);
    public String hexinAccountStudentAndRelation(  UserEntity user,   Classes classes);

    /**
     * 同步koolearn用户信息
     * @param yunUser
     */
    void updateKoolearnUserToLocal(UserEntity yunUser);
    /*java.lang.String *
        * 通过账户密码查询用户信息
        * @param userName
        * @param password
        * @return
        */
    Manager findUserByAccout( String userName , String password );

    /**
     *查询学校是否可用
     */
    School findSchoolStatus(Integer schoolId);

    /**
     * 修改密码重置提示状态
     * @param ssoUserId
     */
    void updateUserPasswordStatus( Integer ssoUserId ) throws Exception;

}
