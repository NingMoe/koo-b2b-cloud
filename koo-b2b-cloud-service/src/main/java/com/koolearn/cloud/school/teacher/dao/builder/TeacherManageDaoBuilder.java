package com.koolearn.cloud.school.teacher.dao.builder;

import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.dto.TeacherPageDto;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by fn on 2016/11/8.
 */
public class TeacherManageDaoBuilder  implements AriesDynamicSqlBuilder {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 教师分页查询总行数
     * @param teacherPageDto
     * @return
     */
    public String findTeacherPageLine(TeacherPageDto teacherPageDto){
        StringBuffer sb = new StringBuffer();
        sb.append( " select count( c.num )  from ( " );
        sb.append( " select count( 1 ) as num  from user a left join teacher_book_version b on a.id = b.teacher_id where a.type = 1 " );
        if( teacherPageDto.getSchoolId() != null ){
            sb.append( " and a.school_id =" + teacherPageDto.getSchoolId() );
        }
        if( StringUtils.isNotEmpty( teacherPageDto.getSubjectName() ) && !CommonInstence.ALL_SUBJECT.equals(teacherPageDto.getSubjectName())){
            sb.append( " and b.subject_name ='" + teacherPageDto.getSubjectName() +"'");
        }
        if( StringUtils.isNotEmpty( teacherPageDto.getRangeName() )){
            sb.append( " and b.range_name='" + teacherPageDto.getRangeName() +"'" );
        }
        if( teacherPageDto.getStatus() != null &&teacherPageDto.getStatus() != 100 ){
            sb.append( " and a.status=" + teacherPageDto.getStatus() );
        }
        if(StringUtils.isNotEmpty( teacherPageDto.getTeacherName() ) ){
            sb.append( " and a.real_name like '%" + teacherPageDto.getTeacherName() +"%'" );
        }
        sb.append( " group by a.id ) c" );

        return sb.toString();
    }

    /**
     * 教师分页查询
     * @param teacherPageDto
     * @return
     */
    public String findTeacherPageList( TeacherPageDto teacherPageDto ){
        StringBuffer sb = new StringBuffer();
        sb.append( " select distinct( a.id ) as userId ,a.user_id as SsoUserId , a.real_name as realName ,a.user_name as userName ,a.mobile ,a.email ,a.status  " );
        sb.append( " from user a left join teacher_book_version b on a.id = b.teacher_id where a.type = 1 " );
        if( teacherPageDto.getSchoolId() != null ){
            sb.append( " and a.school_id =" + teacherPageDto.getSchoolId() );
        }
        if( StringUtils.isNotEmpty( teacherPageDto.getSubjectName() ) &&! CommonInstence.ALL_SUBJECT.equals(teacherPageDto.getSubjectName())){
            sb.append( " and b.subject_name ='" + teacherPageDto.getSubjectName() +"'");
        }
        if( StringUtils.isNotEmpty( teacherPageDto.getRangeName() )){
            sb.append( " and b.range_name='" + teacherPageDto.getRangeName() +"'" );
        }
        if( teacherPageDto.getStatus()  != null &&teacherPageDto.getStatus() != 100 ){
            sb.append( " and a.status=" + teacherPageDto.getStatus() );
        }
        if(StringUtils.isNotEmpty( teacherPageDto.getTeacherName() ) ){
            sb.append( " and a.real_name like '%" + teacherPageDto.getTeacherName() +"%'" );
        }
        sb.append( " group by a.id " );
        sb.append( " order by convert( a.real_name using gbk) COLLATE gbk_chinese_ci asc " );
        sb.append( " limit " + teacherPageDto.getCurrentPage()* teacherPageDto.getPageSize() + " , " + teacherPageDto.getPageSize() );
        return sb.toString();
    }











}
