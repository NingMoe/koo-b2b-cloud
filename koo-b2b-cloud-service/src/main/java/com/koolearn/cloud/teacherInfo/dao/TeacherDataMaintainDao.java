package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.teacher.entity.AreaCity;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;
import com.koolearn.framework.common.page.ListPager;

import java.sql.Connection;
import java.util.List;

/**
 * Created by dongfangnan on 2016/3/29.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface TeacherDataMaintainDao {

    @SQL("select id ,type ,name  from location where type = 1 ")
    public List<Location> findProvinceList();

    /**
     * 查询省份下所有城市
     * @param provinceId
     * @return
     */
    @SQL("select id ,type ,name from location where parent_id=:provinceId")
    public List< Location > findCityByProvinceIdList(@SQLParam("provinceId") int provinceId);

    /**
     * 查询城市下的区县
     * @param cityId
     * @return
     */
    @SQL("select id  ,name from location where parent_id=:cityId")
    public List<AreaCity> fineAreaByCityId(@SQLParam("cityId") int cityId);

    /**
     * 查询城市下面所有学校
     * @param cityId
     * @return
     */

    @SQL("select id ,name,grade_id from school where location_id=:cityId ORDER BY convert(name using gbk) COLLATE gbk_chinese_ci asc limit :pageNo , :pageSize ")
    public List<School> findSchoolByCityIdList(@SQLParam("cityId")int cityId ,@SQLParam("pageNo")int pageNoBegin , @SQLParam("pageSize")int pageSize );

    /**
     * 查询某区所有学校
     * @param cityId
     * @param listPager
     * @param schoolName
     * @return
     */
    @SQL(type=SQLType.READ)
    List<School> findSchoolByCityIdListLike(int cityId, ListPager listPager, String schoolName);
    /**
     * 查询学校行数
     * @param cityId
     * @return
     */
    @SQL("select count( 1 ) from school where location_id =:cityId")
    int findSchoolPageLine(@SQLParam("cityId") int cityId);

    @SQL(type=SQLType.READ)
    int findSchoolPageLine(String schoolName, int cityId );


    /**
     * 更新user表老师信息
     * @param conn
     * @param user
     */
    @SQL(type = SQLType.WRITE_UPDATE)
    public int updataUserInfo(Connection conn,User user);

    /**
     * 更新老师的真实姓名
     * @param conn
     * @param teacherid
     * @param realName
     * @return
     */
    @SQL( " update user set real_name=:realName where id=:teacherid" )
    public int updataUserInfoRealName( Connection conn, @SQLParam("teacherid")Integer teacherid , @SQLParam("realName")String realName );

    /**
     * 更新用户表信息填写进度
     * @param conn
     * @param teacherId
     * @param process
     * @return
     */
    @SQL( " update user set process=:process  where id=:teacherId " )
    public int updataUserInfoProcess(Connection conn, @SQLParam("teacherId")Integer teacherId ,@SQLParam("process")int process );
    /**
     *插入teacher_book_version表
     * @param conn
     * @param teacher
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    public int insertTeacherBookVersion(Connection conn,TeacherBookVersion teacher);

    /**
     * 更新teacher_book_version表教材信息
     * @param conn
     * @param teacher
     */
    @SQL(type = SQLType.WRITE_UPDATE)
    public void updateTeacherBookVersion(Connection conn,TeacherBookVersion teacher);
    /**
     * 查询所有学科
     * @return
      */
    @SQL("select id ,value ,name  from dictionary where status =1 and  type = 1")
    List<Dictionary> findAllSubject();

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @SQL("select id,real_name,type,user_id,user_name,student_code,mobile,email,password,school_id,school_name,province_id,province_name,city_id,city_name,county_id,county_name,process,status  from user where id =:userId")
    User findUserInfo(@SQLParam("userId")int userId );

    /**
     * 根据老师的学科学段id查询老师是否有重复记录
     * @param subjectId
     * @param rangeId
     * @return
     */
    @SQL("select id from teacher_book_version where status=:status and teacher_id=:teacherId and range_id=:rangeId and subject_id=:subjectId ")
    Integer findTeacherBySubAndRangeId(@SQLParam("subjectId")int subjectId , @SQLParam("rangeId")int rangeId , @SQLParam("teacherId")int teacherId , @SQLParam("status")int status);

    @SQL(type=SQLType.READ)
    Object showResult( String sql);
}

