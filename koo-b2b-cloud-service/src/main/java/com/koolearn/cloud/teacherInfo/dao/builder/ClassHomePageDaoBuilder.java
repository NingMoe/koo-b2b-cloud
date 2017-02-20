package com.koolearn.cloud.teacherInfo.dao.builder;

import com.koolearn.cloud.common.entity.ClassesExam;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by fn on 2016/6/27.
 */
public class ClassHomePageDaoBuilder implements AriesDynamicSqlBuilder {

    private Logger log = Logger.getLogger(this.getClass());
    /**
     * 截止当天作业和课堂
     * @param classesIds ( 班级和小组的id集合 )
     * @return
     */

    public String findExamClassedByClassIdDay(String classesIds ,Integer teacherId , Date nowdayBegin  ,String nowdayEnd ){
        StringBuffer sb = new StringBuffer();
        sb.append( "select distinct b.id ,a.classes_id as classesId, a.exam_id as examId , b.exam_name examName ,b.type examType " );
        sb.append( " from tp_exam_student as a inner join tp_exam b on a.exam_id = b.id " );
        sb.append( " where b.status = 4 and a.classes_id in ( "+ classesIds + ") and a.teacher_id="+teacherId+" and a.status = 1 and " );
        sb.append( " b.end_time <=  date_format(:nowdayEnd, '%Y-%m-%d %H:%i:%s')");
        sb.append( " order by b.end_time desc ,b.type asc limit 0 , 5 " );
        return sb.toString();
    }


    /**
     * 查询当前班级编号已生成学生的数量，其他后加入班级的学生不计算数量
     * @param classesId :班级主键
     * @param classesNo :班级编码
     * @return
     */
    public String findClassesNumByClassCode(Integer classesId, String classesNo , Integer status ){
        StringBuffer sb = new StringBuffer();
        sb.append( "select count( * ) from user a inner join classes_student b on a.id = b.student_id " );
        sb.append( "where a.user_name like '" + classesNo +"%' and b.classes_id =" + classesId  );
        sb.append( " and b.status =" + status + "  and a.status = " + status);
        return sb.toString();
    }

}
