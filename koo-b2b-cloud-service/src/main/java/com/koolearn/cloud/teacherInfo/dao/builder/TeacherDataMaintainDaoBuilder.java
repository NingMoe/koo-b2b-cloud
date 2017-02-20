package com.koolearn.cloud.teacherInfo.dao.builder;

import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import com.koolearn.framework.common.page.ListPager;
 

/**
 * Created by fn on 2016/4/6.
 */
public class TeacherDataMaintainDaoBuilder implements AriesDynamicSqlBuilder {

    public String findSchoolByCityIdList(int cityId ,ListPager listPager){
        StringBuffer sql = new StringBuffer();
         sql.append("select id ,name,grade_id from school where location_id =" + cityId  + " ORDER BY convert(name using gbk) COLLATE gbk_chinese_ci asc ");
         sql.append( " limit " + listPager.getPageNo() * listPager.getPageSize() + ", "  );
         sql.append( listPager.getPageSize() );
        return sql.toString();
    }

    public String findSchoolByCityIdListLike(int cityId, ListPager listPager, String schoolName){
        StringBuffer sql = new StringBuffer();
        sql.append("select id ,name ,grade_id from school where name like '%"+ schoolName +"%' and location_id =" + cityId + " ORDER BY convert(name using gbk) COLLATE gbk_chinese_ci asc ");
        sql.append( " limit " + listPager.getPageNo() * listPager.getPageSize() + ", "  );
        sql.append( listPager.getPageSize() );
        return sql.toString();
    }

    public String findSchoolPageLine( String schoolName, int cityId ){
        StringBuffer sql = new StringBuffer();
        sql.append( "select count( 1 ) from school where name like '%" + schoolName +"%' " );
        sql.append( " and location_id="+cityId  );
        return sql.toString();
    }

    public String showResult( String sql){
        return sql;
    }

}
