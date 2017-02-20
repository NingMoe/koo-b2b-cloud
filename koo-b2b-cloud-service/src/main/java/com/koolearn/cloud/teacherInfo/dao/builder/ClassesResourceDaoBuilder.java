package com.koolearn.cloud.teacherInfo.dao.builder;

import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import com.koolearn.framework.common.page.ListPager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by fn on 2016/4/25.
 */
public class ClassesResourceDaoBuilder implements AriesDynamicSqlBuilder {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 查询课堂全部资源
     * @param classesIds
     * @param teacherId
     * @param
     * @return
     */
    public String findExamByClassesIdAndTeacherId (String classesIds ,  int teacherId ,  int begin ,int end ){
        StringBuffer sb = new StringBuffer();
        sb.append( "select count( 1 )  as studentNum ,a.classes_id ,b.exam_name,b.type , a.exam_id ,a.type ,a.classes_id ,b.start_time ,b.end_time ,b.create_time ");
        sb.append( " from tp_exam_student a inner join tp_exam b on a.exam_id = b.id ");
        sb.append( " where a.classes_id in ("  + classesIds + " ) and a.teacher_id =" + teacherId +  " and b.status = 4 and a.status = 1 and ( b.type = 1 or b.type = 2 )" );
        sb.append( "  group by a.exam_id order by b.create_time desc limit  " + begin + "," + end  );
        log.info( "班级资源分页：" + sb.toString() );
        return sb.toString();
    }

    /**
     * 按照资源类型查询课堂资源
     * @param classesId
     * @param teacherId
     * @param listPager
     * @param typeId
     * @return
     */
    public String findExamByClassesIdAndTeacherIdAndType (int classesId ,  int teacherId ,  ListPager listPager ,String typeId ){
        StringBuffer sb = new StringBuffer();
        sb.append( "select count( 1 )  as studentNum  , a.exam_id ,b.exam_name,b.type ,b.create_time ,b.start_time ,b.end_time " +
                "  from tp_exam_student a inner join tp_exam b on a.exam_id = b.id ");
        sb.append( " where b.type=" + typeId + " and a.classes_id =" + classesId + " and a.teacher_id =" + teacherId +  "  and b.status = 4 and a.status = 1 group by a.exam_id  order by b.create_time desc limit " );
        sb.append(  listPager.getPageNo() * listPager.getPageSize() + "," + listPager.getPageSize()  );
        log.info( "班级资源分页：" + sb.toString() );
        return sb.toString();
    }


    public String findStudentDoneNumByExamId( String attachmentIds, Integer studentId ){
        StringBuffer sb = new StringBuffer();
        sb.append( " select  count( 1 )  from te_exam_result where exam_id in (");
        sb.append( attachmentIds );
        sb.append( ") and student_id =" + studentId +" and status = 2" );
        return sb.toString();
    }

}
