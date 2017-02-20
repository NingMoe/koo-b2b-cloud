package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesExam;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;
import com.koolearn.framework.aries.annotation.SQLType;

import java.util.Date;
import java.util.List;

/**
 * 班级主页资源
 * Created by fn on 2016/4/5.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface ClassHomePageDao {
    /**
     * 查询老师名下所有班级
     * @param teacherId
     * @return
     */
    @SQL("select id ,class_name, range_id ,range_name , subject_name , subject_id  ,type ,year from classes" +
         " where teacher_id =:teacherId and type !=:classesType and graduate = 0 and status =:status  order by create_time desc ")
    List<Classes> findTeacherClassById( @SQLParam("teacherId")int teacherId ,@SQLParam("classesType")int classesType ,@SQLParam("status")int status);
    /**
     * 查询班级人数
     * @param classesId
     * @return
     */
    @SQL("select count(1) from classes_student where classes_id=:classesId and status =:status ")
    int findClassNumByClassId(@SQLParam("classesId")Integer classesId ,@SQLParam("status")int status );

    /**
     * 查询截止日期小于当天的作业课程ID
     * @param classesId
     * @return
     */
    @SQL("select distinct(b.id) ,a.classes_id as classesId, a.exam_id as examId , b.exam_name examName ,b.type examType from tp_exam_student as a inner join tp_exam b on a.exam_id = b.id " +
         " where b.status = 4 and a.classes_id =:classesId and a.teacher_id=:teacherId and a.status = 1 and b.end_time <:nowday  order by b.end_time desc, b.type asc limit 0 ,:size" )
    List<ClassesExam> findExamClassedByClassId(@SQLParam("classesId")Integer classesId ,@SQLParam("teacherId")Integer teacherId , @SQLParam("nowday")Date nowday , @SQLParam("size")int size );

    /**
     * 截止当天作业和课堂
     * @param classesIdList ( 班级huo小组的id集合 )
     * @return
     */
    //@SQL(type= SQLType.READ)
    @SQL( "select distinct b.id ,a.classes_id as classesId, a.exam_id as examId , b.exam_name examName ,b.type examType" +
          " from tp_exam_student as a inner join tp_exam b on a.exam_id = b.id " +
          " where b.status = 4 and a.classes_id in ( :classesIdList) and a.teacher_id=:teacherId and a.status = 1 and " +
          " b.end_time >= now() and b.start_time <= now() GROUP BY a.exam_id order by b.end_time desc ,b.type asc limit 0 , 5 " )
    List<ClassesExam> findExamClassedByClassIdDay(@SQLParam("classesIdList")List<Integer> classesIdList ,@SQLParam("teacherId")Integer teacherId , @SQLParam("nowdayBegin")Date nowdayBegin  ,@SQLParam("nowdayEnd")String nowdayEnd );

    /**
     * 根据examId查询老师是否浏览过作业
     * @param examId
     * @return
     */
    @SQL( "select teacher_view from te_exam_result where teacher_view=:teacherView and status=:examResultStatus and exam_id=:examId " )
    List< Integer > findTeacherViewByExamId(@SQLParam("examId")int examId ,@SQLParam("teacherView")int teacherView ,@SQLParam("examResultStatus")int examResultStatus);

    /**
     * 查询老师班级表中属于某老师的所有班级
     * @param teacherId
     * @return
     */
    @SQL( "select  distinct( classes_id ) from classes_teacher where teacher_id=:teacherId and status=:status " )
    List< Integer> findAllClasssesByTeacherId(@SQLParam("teacherId")int teacherId ,@SQLParam("status")int status);

    /**
     * 查询老师所有加入的班级
     * @param teacherId
     * @param status
     * @return
     */
    @SQL( " select b.id ,b.full_name ,b.class_name ,b.class_code,b.year,b.type ,b.graduate,b.range_id,b.range_name,b.subject_name ,b.subject_id " +
            " from classes_teacher a inner join classes b on a.classes_id= b.id where a.teacher_id =:teacherId and b.type!=:type " +
            " and a.status=:status and b.status=:status and b.graduate=:status order by a.create_time desc " )
    List<Classes> findTeacherAllClassByTeacherId(@SQLParam("teacherId")int teacherId,@SQLParam("status") int status ,@SQLParam("type") int type);

    /**
     * 查询当前班级编号已生成学生的数量，其他后加入班级的学生不计算数量
     * @param classesId :班级主键
     * @param classesNo :班级编码
     * @return
     */
    @SQL(type=SQLType.READ)
    int findClassesNumByClassCode(Integer classesId, String classesNo ,Integer status );
}
