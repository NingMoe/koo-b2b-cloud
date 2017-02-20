package com.koolearn.cloud.school.schoolmanage.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.TbSchoolExtend;
import com.koolearn.cloud.school.entity.TbSchoolUp;
import com.koolearn.cloud.school.schoolmanage.vo.SchoolPowerDto;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/11/21.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface SchoolManageDao {

    /**
     * 查询学校基础信息
     * @param schoolId
     * @return
     */
    @SQL( "select id , name , grade_id ,begin_time ,end_time  from school where id=:schoolId and status=:status_0 " )
    School findSchoolById( @SQLParam( "schoolId" )Integer schoolId ,@SQLParam( "status_0")Integer status_0 );

    /**
     * 查询学校扩展信息
     * @param schoolId
     * @return
     */
    @SQL( " select id , contacter ,contacter_mobile ,contacter_mail,begin_time,end_time,max_online   from tb_school_extend where school_id=:schoolId " )
    TbSchoolExtend findSchoolExtendBySchoolId( @SQLParam( "schoolId" )Integer schoolId);

    /**
     * 查询学校升级信息
     * @param schoolId
     * @return
     */
    @SQL( " select id ,up_year ,graduate_year , auto_up_time ,auto_graduate_time ,class_graduate_level ,version  from tb_school_up where school_id=:schoolId " )
    TbSchoolUp findSchoolUpBySchoolId( @SQLParam( "schoolId" )Integer schoolId);

    /**
     * 查询学校管理员列表
     * @param schoolId
     * @return
     */
    @SQL( " select manager_name , manager_mobile ,manager_email from tb_school_manager where school_id=:schoolId and status=:status_1" )
    List<Manager> findManagerBySchoolId( @SQLParam( "schoolId" )Integer schoolId , @SQLParam( "status_1" )Integer status_1);

    /**
     * 修改学校扩展信息
     * @param schoolPowerDto
     */
    @SQL(type= SQLType.READ)
    int updateSchoolExtendInfo(SchoolPowerDto schoolPowerDto);

    /**
     * 更新学校的升级时间信息
     * @param autoUpTime
     * @param updater
     */
    @SQL( " update tb_school_up set auto_up_time=:autoUpTime , updater=:updater , update_time= now(),version=:versionUp " +
          " where school_id=:schoolId and  version=:version" )
    void updateSchoolUp(@SQLParam( "schoolId" )Integer schoolId ,@SQLParam( "autoUpTime" )String autoUpTime,@SQLParam( "updater" ) String updater ,@SQLParam( "version" ) Integer version,@SQLParam( "versionUp" ) Integer versionUp );

    /**
     * 更新学校升级表毕业时间
     * @param autoGraduateTime
     * @param updater
     * @param version
     * @param versionUp
     */
    @SQL( " update tb_school_up set auto_graduate_time=:autoGraduateTime ,updater=:updater , update_time= now(),version=:versionUp  " +
         " where school_id=:schoolId and version=:version " )
    void updateSchoolUpGraduate(@SQLParam( "schoolId" )Integer schoolId ,@SQLParam( "autoGraduateTime" )String autoGraduateTime,@SQLParam("updater")String updater,@SQLParam( "version" )Integer version,@SQLParam( "versionUp" )int versionUp);

    /**
     * 创建学校升级记录
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    Integer insertSchoolUp( Connection conn , TbSchoolUp tbSchoolUp);

    /**
     * 查询学校升级信息
     * @return
     */
    @SQL( " select a.id ,a.school_id ,a.auto_up_time , a.up_year , a.auto_graduate_time ,a.class_graduate_level ,a.graduate_year  " +
          " from tb_school_up a inner join school b on a.school_id = b.id where b.status=:status " )
    List< TbSchoolUp > findAllSchoolUp( @SQLParam( "status") Integer status );

    /**
     * 查询排除毕业年级后剩下的年级（都升级）
     */
    @SQL( " select id ,full_name , class_name ,level from classes where status=:status_0 and level not in(:classesLevelList) " +
          " and type != 3 and graduate=:status_0 and school_id=:schoolId" )
    List<Classes> findExceptGradeClassesByLevel(@SQLParam( "schoolId")Integer schoolId, @SQLParam( "classesLevelList")List<Integer> classesLevelList ,@SQLParam( "status_0" )Integer status_0 );

    /**
     * 查询所有需要毕业的年级
     * @param schoolId
     * @param classesLevelList
     * @param status_0
     * @return
     */
    @SQL( " select id ,full_name , class_name ,level from classes where status=:status_0 and level in(:classesLevelList) " +
          " and type != 3 and graduate=:status_0 and school_id=:schoolId" )
    List<Classes> findGradeClassesByLevel(@SQLParam( "schoolId")Integer schoolId, @SQLParam( "classesLevelList")List<Integer> classesLevelList ,@SQLParam( "status_0" )Integer status_0 );

    /**
     * 更新年级升级后的升级年份和毕业年份
     * @param schoolId
     * @param nowYear
     * @param schoolUpId
     */
    @SQL( " update tb_school_up set up_year=:nowYear ,graduate_year=:nowYear where school_id=:schoolId and id=:schoolUpId " )
    void updateSchoolUpGradeYearBySchoolId( @SQLParam( "schoolId")Integer schoolId ,@SQLParam( "nowYear")Integer nowYear ,@SQLParam( "schoolUpId") int schoolUpId );

    /**
     * 创建学校升级信息
     * @param conn
     * @param tbSchoolUp
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int addSchoolUp( Connection conn , TbSchoolUp tbSchoolUp );
}
