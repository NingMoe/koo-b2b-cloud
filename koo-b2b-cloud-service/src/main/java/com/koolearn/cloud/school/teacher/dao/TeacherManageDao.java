package com.koolearn.cloud.school.teacher.dao;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.dto.TeacherPageDto;
import com.koolearn.cloud.school.teacher.vo.TeacherPageResultDto;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/11/3.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface TeacherManageDao {

    /**
     * 某学校下教师总数
     * @param teacherPageDto
     * @return
     java.util.*/
    @SQL(type= SQLType.READ)
    Integer findTeacherPageLine(TeacherPageDto teacherPageDto);

    /**
     * 教师分页查询
     * @param teacherPageDto
     * @return
     */
    @SQL(type= SQLType.READ)
    List<TeacherPageResultDto> findTeacherPageList( TeacherPageDto teacherPageDto );

    /**
     * 查询老师所有学段和学科
     * @param userId
     * @return
     */
    @SQL( "select range_name as rangeNameStr , subject_name as subjectNameStr from teacher_book_version where teacher_id=:userId " )
    List<TeacherPageResultDto> findTeacherSubjectAndRange( @SQLParam("userId")Integer userId);

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    @SQL( " select count( 1 ) from user where email=:email and status != 1" )
    int findUserEmail( @SQLParam("email")String email);

    /**
     * 判断电话是否存在
     * @param mobile
     * @return
     */
    @SQL( "select count( 1 ) from user where mobile=:mobile and status != 1 " )
    int findUserMobile(@SQLParam("mobile")String mobile);

    /**
     * 修改教师信息
     * @param conn
     * @param userId
     * @param email
     * @param mobile
     * @param realName
     * @param updater
     */
    @SQL( " update user set email=:email , mobile=:mobile ,real_name=:realName ,updater=:updater ,update_time=now() where id=:userId " )
    void updateTeacherBaseInfo( Connection conn , @SQLParam("userId")Integer userId, @SQLParam("email")String email, @SQLParam("mobile")String mobile ,
                                @SQLParam("realName")String realName, @SQLParam("updater")String updater );

    /**
     * 修改教师状态
     * @param conn
     * @param userId
     * @param status
     * @param updater
     */
    @SQL( " update user set status=:status ,updater=:updater ,update_time=now() where id=:userId " )
    void updateTeacherStatus(Connection conn ,@SQLParam("userId")Integer userId, @SQLParam("status")Integer status, @SQLParam("updater")String updater );

    /**
     * 查询教师信息
     * @param userId
     * @return
     */
    @SQL( " select real_name as realName ,mobile , email from user where id=:userId " )
    TeacherPageResultDto findTeacherInfoForUpdate(@SQLParam("userId")Integer userId);

    /**
     * 查询学校编号
     * @param schoolId
     * @return
     */
    @SQL( " select code from school where id=:schoolId and status=0 and entity_status=1 " )
    String findSchoolCode( @SQLParam("schoolId")Integer schoolId );

    /**
     * 先判断当前信息是否已存在
     * @param realName
     * @param email
     * @param mobile
     * @return
     */
    @SQL( " select count( 1 ) from user where real_name=:realName and email=:email and mobile=:mobile and status=0 " )
    int findUserByNameEmailMobile(@SQLParam("realName")String realName, @SQLParam("email")String email, @SQLParam("mobile")String mobile);


}
