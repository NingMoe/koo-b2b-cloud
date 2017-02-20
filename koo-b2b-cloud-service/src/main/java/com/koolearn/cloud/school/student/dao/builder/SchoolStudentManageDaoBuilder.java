package com.koolearn.cloud.school.student.dao.builder;

import com.koolearn.cloud.school.student.vo.StudentPageVo;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by fn on 2016/11/10.
 */
public class SchoolStudentManageDaoBuilder implements AriesDynamicSqlBuilder {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 学生分页查询总行数
     * @param studentPageVo
     * @return
     */
    public String findStudentTotalPage(StudentPageVo studentPageVo){
        StringBuffer sb = new StringBuffer();
        sb.append( " select count( d.num ) from ( " );
        sb.append( " select count( 1 ) as num  from classes_student a inner join classes b on a.classes_id = b.id" +
                   " inner join user  c on a.student_id = c.id  where c.status != 1 and a.status = 0 and b.status = 0 and b.graduate =0 " );
        sb.append( " and c.school_id = " + studentPageVo.getSchoolId() );
        if( StringUtils.isNotEmpty( studentPageVo.getRangeName() ) && !"100".equals( studentPageVo.getRangeName() )){
            sb.append( " and b.range_name= '"+ studentPageVo.getRangeName() +"'");
        }
        if( null != studentPageVo.getLevel()&& 100 != studentPageVo.getLevel() ){
            sb.append( " and b.level="+studentPageVo.getLevel() );
        }
        if(StringUtils.isNotEmpty(studentPageVo.getStudentName()) ){
            sb.append( " and c.real_name like '%" + studentPageVo.getStudentName()+"%'" );
        }
        sb.append( " group by c.id  ) d " );


        return sb.toString();
    }

    /**
     * 全校学生分页查询列表
     * @param studentPageVo
     * @return
     */
    public String findStudentPageList(StudentPageVo studentPageVo){
        StringBuffer sb = new StringBuffer();
        sb.append( " select distinct ( c.id )  as userId , c.user_id as ssoUserId , c.real_name as realName ,c.user_name as userName , c.student_code as studentCode ,b.full_name as fullName ,c.email , c.mobile ,c.status  " );
        sb.append( " from classes_student a inner join classes b on a.classes_id = b.id " );
        sb.append( " inner join user  c on a.student_id = c.id  where c.status != 1 and a.status = 0 and b.status = 0 and b.graduate =0 " );
        sb.append( " and c.school_id = " + studentPageVo.getSchoolId() );
        if( StringUtils.isNotEmpty( studentPageVo.getRangeName()) && !"100".equals( studentPageVo.getRangeName() )){
            sb.append( " and b.range_name= '"+ studentPageVo.getRangeName() +"'");
        }
        if( null != studentPageVo.getLevel()&& 100 != studentPageVo.getLevel() ){
            sb.append( " and b.level="+studentPageVo.getLevel() );
        }
        if(StringUtils.isNotEmpty(studentPageVo.getStudentName()) ){
            sb.append( " and c.real_name like '%" + studentPageVo.getStudentName()+"%'" );
        }
        sb.append( " group by c.id order by c.create_time desc " );
        sb.append( " limit " + studentPageVo.getCurrentPage() * studentPageVo.getPageSize() + "," + studentPageVo.getPageSize() );

        return sb.toString();
    }

}
