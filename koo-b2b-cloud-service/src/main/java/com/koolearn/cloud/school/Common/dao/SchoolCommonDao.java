package com.koolearn.cloud.school.Common.dao;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.common.entity.TbErrorLog;
import com.koolearn.cloud.school.entity.TbSchoolUp;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/10/31.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface SchoolCommonDao {
    /**
     * 查询当前学校下所有学段
     * @param schoolId
     * @return
     */
    @SQL( " select grade_id from school where id=:schoolId and status=0 and entity_status=1 " )
    String findAllRangeBySchoolId(@SQLParam("schoolId")Integer schoolId);

    /**
     * 查询学校升级信息
     * @param schoolId
     * @return
     */
    @SQL( "select id,school_id  ,auto_up_time ,up_year ,auto_graduate_time ,class_graduate_level ,graduate_year ,create_time from tb_school_up where school_id=:schoolId " )
    TbSchoolUp findSchoolUpInfo( @SQLParam("schoolId")Integer schoolId );

    /**
     * 查询全部学科
     * @return
     */
    @SQL( "select name ,value as id  from dictionary where type=1 " )
    List<SubjectDto> findAllSubject();

    /**
     * 记录异常日志
     * @param conn
     * @param tbErrorLog
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    void insertErrorLog(Connection conn , TbErrorLog tbErrorLog );
}
