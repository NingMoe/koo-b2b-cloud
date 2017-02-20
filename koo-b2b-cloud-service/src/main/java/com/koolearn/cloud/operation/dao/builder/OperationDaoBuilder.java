package com.koolearn.cloud.operation.dao.builder;

import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.operation.entity.SchoolFilter;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.commons.lang3.StringUtils;

public class OperationDaoBuilder implements AriesDynamicSqlBuilder{
	/**
	 * 根据过滤条件查询试题列表。分页查询
	 * @param
	 * @return
	 */
	public String searchSchoolData(SchoolFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        sql.append(" order by s.create_time DESC ");
        sql.append("limit ").append(filter.getPageNo()*filter.getPageSize())
                .append(",").append(filter.getPageSize());
		return sql.toString();
	}
	/**
	 * 构造sql语句片段
     * userCount 学校用户数量
	 * @param filter
	 * @param
	 * @param
	 */
	private StringBuffer structureSql(SchoolFilter filter) {
        StringBuffer sql = new StringBuffer(" select if(isnull( u.userCount),0,u.userCount) as 'userCount',`id`,`name` ,`shortname` " +
                ",`update_user`,`code`,`location_id`,`grade_id`,`logo`,`read_system_status`,`status`,`entity_status`" +
                ",`begin_time`,`end_time`,`update_time`,`create_time`,`access_key`,`access_secret` ");
        sql.append(" from school s left join (select count(1) as userCount,school_id from user group by school_id) u on s.`id`=u.school_id ");
        sql.append(" where 1=1 and entity_status <> 2");
        if(StringUtils.isNotBlank(filter.getName())){
            //学校名称
            sql.append(" and s.name like '%").append(filter.getName()).append("%' ");
        }
        if(filter.getLocationIdList()!=null&&filter.getLocationIdList().size()>0){
            //地区
            sql.append(" and s.location_id in (").append(StringUtils.join(filter.getLocationIdList(), ",")).append(") ");
        }
        if(filter.getGrade()!=null){
            //学校学段grade_id eg: 1,2,3
            sql.append(" and s.grade_id like '%").append(filter.getGrade()).append("%' ");
        }else{
            //限制只显示小学初中高中的学校
            sql.append(" and (  ");
            sql.append("  s.grade_id like '%").append(CommonInstence.GRADE_TYPE_xx).append("%' ");
            sql.append(" or s.grade_id like '%").append(CommonInstence.GRADE_TYPE_gz).append("%' ");
            sql.append(" or s.grade_id like '%").append(CommonInstence.GRADE_TYPE_cz).append("%' ");
            sql.append(" )");
        }

        if(filter.getSchoolStatus()!=null&&filter.getSchoolStatus().intValue()== CommonInstence.DIC_SCHOOL_SEARCH_STATUS_SHIELD){
            //查询屏蔽学校
            sql.append(" and s.entity_status <>1");
        }else if(filter.getSchoolStatus()!=null&&filter.getSchoolStatus().intValue()== CommonInstence.DIC_SCHOOL_SEARCH_STATUS_HASUSER){
            //查询有用户的学校 排除屏蔽
            sql.append(" and u.userCount is not null and s.entity_status <>3");
        }else if(filter.getSchoolStatus()!=null&&filter.getSchoolStatus().intValue()== CommonInstence.DIC_SCHOOL_SEARCH_STATUS_NOUSER){
            //查询无用户的学校
            sql.append(" and u.userCount is  null ");
        }
		return sql;

	}

    /**
     * 根据过滤条件查询试题数量
     * @param
     * @return
     */
    public String searchSchoolDataCount(SchoolFilter filter) {
        StringBuffer sql ;
        sql = structureSql(filter);
        String sqlResult = " select count(*) from ("+ sql.toString() +") lin ";
        return sqlResult;

    }

}
