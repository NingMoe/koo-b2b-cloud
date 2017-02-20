package com.koolearn.cloud.operation.dao;

/**
 * Created by gehaisong on 2016/11/16.
 */

import com.koolearn.cloud.operation.entity.SchoolFilter;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

@DAO(dbtype = DbType.MYSQL, source = GlobalConstant.MYSQL_DATASOURCE)
public interface OperationDao {

    @SQL("select id ,type ,name,parent_id  from location where parent_id = :parentId ")
    List<Location> findLocationList(@SQLParam("parentId") Integer parentId);

    @SQL(type = SQLType.READ)
    List<School> searchSchoolData(SchoolFilter pager);
    @SQL(type = SQLType.READ)
    Long searchSchoolDataCount(SchoolFilter pager);

    /**
     * 查询区域
     * @return
     */
    @SQL("select id ,type ,name ,parent_id from location ")
    List<Location> findAllLocationList();

    @SQL(type = SQLType.READ_BY_ID)
    School findSchoolById(Integer id);

    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveSchool( School school);

    @SQL(type = SQLType.WRITE_UPDATE)
    void updateSchool(  School school);

    @SQL("select count(1) from  school where  `name` = :name or  shortname =:name ")
    int schoolNumByName(@SQLParam("name")String name);
}
