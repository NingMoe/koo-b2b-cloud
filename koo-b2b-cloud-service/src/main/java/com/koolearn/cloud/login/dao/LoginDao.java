package com.koolearn.cloud.login.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.HexinLog;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

@DAO(dbtype=DbType.MYSQL,source=GlobalConstant.MYSQL_DATASOURCE)
public interface LoginDao {

	@GeneratedKey
	@SQL(type=SQLType.WRITE_INSERT)
	int insertUser(UserEntity ue);

	@SQL(" select group_concat(c.school_id) schoolIdStr,u.* from user u " +
			" left join classes_student cs on u.id=cs.student_id " +
			" left join classes c on c.id=cs.classes_id " +
			" where (u.email=:mobileEmail or u.mobile=:mobileEmail or u.user_name=:mobileEmail) " +
			" group by u.id ")
	List<UserEntity> findUser(@SQLParam("mobileEmail") String mobileEmail);

	@SQL(type=SQLType.WRITE)
	int updateUserEntity(Connection conn, @SQLParam("ue") UserEntity ue);

	@SQL(type=SQLType.WRITE_INSERT)
	int insertClassesStudent(Connection conn, ClassesStudent cs);

	@SQL(type=SQLType.READ_BY_ID)
	UserEntity getStudentById(int studentId);

	//查询学生所属班级
	@SQL(" select u.*,cs.classes_id classesId From classes_student cs " +
			" INNER JOIN user u on cs.student_id=u.id where cs.student_id=:id and cs.status="+ClassesStudent.STATUS_NOMAL)
	List<UserEntity> findClassesStudent(@SQLParam("id") int id);

	/**	查询用户信息	*/
	@SQL(" select * from user u where user_name=:userName and u.status="+UserMobi.USER_STATUS_VALID)
	UserMobi findMobileUser(@SQLParam("userName") String userName);

	/**	查询学校和班级	*/
	@SQL(" select c.class_name classesName,c.id classesId,s.name schoolName,s.id schoolId " +
			" From classes_student cs " +
			" INNER JOIN classes c on cs.classes_id=c.id " +
			" INNER JOIN school s on c.school_id=s.id " +
			" where cs.student_id=:id and c.`status`=" +Classes.STATUS_NOMAL +
			" order by c.`level` desc,c.id desc " +
			" LIMIT 0,1 ")
	UserEntity findSchoolClassInfo(@SQLParam("id") Integer id);

    /**
     * 查询用户 根据用户ID
     * @param userId
     * @return
     */
    @SQL("SELECT * FROM USER WHERE user_id =:userId LIMIT 1")
    User findUserByUserId(@SQLParam("userId")Integer userId);

    /**
     * SSO用户入本地库
     * @param conn
     * @param user
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int addSSOUser(Connection conn, User user);

    /**
     * 修改koolearn用户的角色类型
     * @param conn
     * @param userId
     * @param roleType
     * @return
     */
    @SQL( "update user set type=:roleType where id=:userId and status=0 " )
    int addUserRole(Connection conn, @SQLParam("userId")Integer userId ,@SQLParam("roleType") Integer roleType );

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @SQL( " select id ,type ,user_id ,user_name ,mobile , email , process ,status  from user where id =:userId and status=:status" )
    UserEntity findUserById(@SQLParam("userId")Integer userId , @SQLParam("status")Integer status);
    /**
     * 查询用户所在学校的地区id
     * @return
     */
    @SQL( "SELECT location_id from school where  id in (:schoolIds)" )
    List<Integer> locationIds(  @SQLParam("schoolIds")List<Integer> schoolIds);
    /**
     * 查询用户所在扶绥学校(只有扶绥学校的用户可以跳转到合心考试系统)
     * @return
     */
    @SQL( "SELECT * from school where  id in (:schoolIds) and location_id="+GlobalConstant.FUSUI_LOCATION_ID )
    List<School> getFusuiSchoolsOfUser(  @SQLParam("schoolIds")List<Integer> schoolIds);
    @SQL( "SELECT * from school where  id in (:schoolIds) " )
    List<School> getAllSchoolsOfUser(  @SQLParam("schoolIds")List<Integer> schoolIds);
    @SQL(type=SQLType.WRITE_INSERT)
    int insertHexinLog(HexinLog hexinLog);//合心接口日志

    /**
     * 同步koolearn用户信息到user表
     * @param userId
     * @param mobile
     * @param email
     */
    @SQL(type=SQLType.WRITE)
    void updateKoolearnUserToLocal(Connection conn, @SQLParam("userId")Integer userId, @SQLParam("mobile")String mobile, @SQLParam("email")String email);

    // 以下为学校用户登录操作功能
    /**
     * 通过账号查询用户信息
     * @param userName
     * @param password
     * @return
     */
    @SQL( "select id ,school_id ,manager_name ,manager_mobile ,manager_email ,creator ,version " +
            " from tb_school_manager where manager_name=:userName and pass_word=:password " )
    public Manager findUserByUserName(@SQLParam("userName")String userName, @SQLParam("password")String password);
    /**
     * 通过邮箱查询用户信息
     * @param userName
     * @param password
     * @return
     */
    @SQL( " select id ,school_id ,manager_name ,manager_mobile ,manager_email ,creator ,version " +
          " from tb_school_manager where manager_email=:userName and pass_word=:password " )
    public Manager findUserByEmail(@SQLParam("userName")String userName, @SQLParam("password")String password);
    /**
     * 通过手机查询用户信息
     * @param userName
     * @param password
     * @return
     */
    @SQL( " select id ,school_id ,manager_name ,manager_mobile ,manager_email ,creator ,version " +
          " from tb_school_manager where manager_mobile=:userName and pass_word=:password " )
    public Manager findUserByMobile(@SQLParam("userName")String userName, @SQLParam("password")String password);
    /**
     * 查询学校是否可用
     *
     */
    @SQL( " select begin_time ,end_time ,entity_status ,code,name ,status  from school where id=:schoolId " )
    public School findSchoolStatus(@SQLParam("schoolId")Integer schoolId);

}
