package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/7/5.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface SchedulerClassesUpDao {
    /**
     * 查询所有未毕业班级主键
     * @return
     */
    @SQL( "select id ,full_name , class_name,class_code ,level ,parent_id , graduate ,type ,year,range_name ,range_id ,teacher_id ,subject_name ,subject_id ,school_id ,status ，create_time " +
          " from classes where ( type = 1 or type = 0 ) and status=0 and graduate = 0 ")
    List<Classes> findAllClassesId();

    /**
     * 某个学校下的所有班级
     * @param schoolId
     * @return
     */
    @SQL( "select id ,full_name , class_name,class_code ,level ,parent_id , graduate ,type ,year,range_name ,range_id ,teacher_id ,subject_name ,subject_id ,school_id ,status ，create_time " +
          " from classes where ( type = 1 or type = 0 ) and status=0 and graduate = 0 and school_id=:schoolId ")
    List<Classes> findAllClassesIdBySchoolId( @SQLParam("schoolId")Integer schoolId);
    /**
     * 更新班级名称和level
     * @param classesId
     * @param fullName
     * @param levelUp
     * @return
     */
    @SQL( "update classes set full_name=:fullName ,level=:levelUp ,update_time=now() where id=:classesId" )
    int updateClassesUpInfo(Connection conn ,@SQLParam("classesId")Integer classesId, @SQLParam("fullName")String fullName, @SQLParam("levelUp")int levelUp);

    /**
     * 查询某班级下所有小组
     * @param classesId
     * @return
     */
    @SQL( " select id , class_name from classes where parent_id=:classesId and status=:status" )
    List<Classes> findAllTeamByClassesId( @SQLParam("classesId")Integer classesId ,@SQLParam("status")Integer status );

    /**
     * 更新小组名称
     * @param conn
     * @param teamId
     * @param fullName
     * @return
     */
    @SQL( " update classes set full_name=:fullName where id =:teamId " )
    int updateClassesTeamName(Connection conn ,@SQLParam("teamId") Integer teamId, @SQLParam("fullName")String fullName );

    /**
     * 修改班级毕业状态
     * @param conn
     * @param graduateStatus
     * @param classesId
     * @param levelUp
     * @return
     */
    @SQL( "update classes set graduate=:graduateStatus , level=:levelUp where id=:classesId and status =0 " )
    int updateClassesGraduateStatus(Connection conn, @SQLParam("graduateStatus")int graduateStatus, @SQLParam("classesId")Integer classesId, @SQLParam("levelUp")int levelUp );












}
