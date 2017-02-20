package com.koolearn.cloud.school.homePage.dao;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

/**
 * Created by fn on 2016/10/12.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface HomePageDao {
    /**
     * 通过学校主键id查询学校信息
     * @param schoolId
     * @param status
     * @return
     */
    @SQL( " select a.id ,a.name ,a.grade_id ,a.code ,b.begin_time as beginTime ,b.end_time as endTime " +
          " from school a left join tb_school_normal_history b on a.id = b.school_id " +
          " where a.id=:schoolId and a.status=:status " )
    public School findSchoolInfoById(@SQLParam("schoolId") Integer schoolId, @SQLParam("status") Integer status);

    /**
     * 教师或学生的数量
     * @param schoolId
     * @return
     */
    @SQL( " select count( 1 ) from user where  school_id=:schoolId and type=:type and status=:status" )
    Integer findTeacherNumBySchoolId(@SQLParam("schoolId") Integer schoolId, @SQLParam("type") Integer type, @SQLParam("status") Integer status);

    /**
     * 查询班级数量
     * @param schoolId
     * @return
     */
    @SQL( " select count( id ) from classes where school_id=:schoolId and status=0 " )
    Integer findClassesNumBySchoolId(@SQLParam("schoolId") Integer schoolId);

}
