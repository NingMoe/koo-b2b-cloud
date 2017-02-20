package com.koolearn.cloud.studentHome.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesDynamic;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * 查询学生班级首页资源
 * Created by fn on 2016/5/19.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface StudentClassHomeDao {
    /**
     * 查询学生加入的所有班级
     *
     * @param studentId
     * @return
     */
    @SQL("select a.id ,a.class_name ,a.full_name ,a.type ,a.class_code ,a.level ,a.year ,a.subject_name ,a.range_name , a.range_id , a.subject_id ,a.teacher_id " +
         " from classes a inner join classes_student b on a.id = b.classes_id where a.status=:status  and b.status =:status" +
         " and a.type !=:type and  b.student_id=:studentId order by b.create_time desc ")
    public List< Classes > findAllClassesByStudentId(@SQLParam("studentId") int studentId, @SQLParam("status") int status , @SQLParam("type") int type);

    /**
     * 验证班级学生是否存在
     *
     * @param classesId
     * @param status
     * @return
     */
    @SQL("select count( 1 ) from classes_student where status=:status and classes_id=:classesId and student_id=:studentId ")
    int checkStudentClasses(@SQLParam("classesId") Integer classesId, @SQLParam("studentId") Integer studentId, @SQLParam("status") int status);

    /**
     * 查询班级编码是否可用
     *
     * @param classesCode
     * @param status
     * @return
     */
    @SQL("select id from classes where status=:status and class_code=:classesCode")
    Integer findClassesByClassesCode(@SQLParam("classesCode") String classesCode, @SQLParam("status") int status);

    /**
     * 查询班级名称
     *
     * @param classesId
     * @return
     */
    @SQL("select full_name from classes where id =:classesId ")
    String findClassesNameById(@SQLParam("classesId") Integer classesId);

    /**
     * 先查出该学生下所有的小组
     * @param classesId
     * @param status
     * @return
     */
    @SQL( "select id  from classes where parent_id=:classesId and status=:status " )
    List< Integer > findAllTeamByClassesId(@SQLParam("classesId")Integer classesId, @SQLParam("status") int status);

    /**
     * 修改学生小组状态
     * @param classesId
     * @param studentId
     * @return
     */
    @SQL( " update classes_student set status=:status where student_id=:studentId and classes_id=:classesId " )
    int updateClassesStatus( Connection conn , @SQLParam("classesId")Integer classesId, @SQLParam("studentId")int studentId ,@SQLParam("status")int statusDelete);

    /**
     * 分页查询班级动态行数
     * @param classesId
     * @return
     */
    @SQL( " select count( 1 ) from classes_dynamic where classes_id=:classesId and create_time >:createTime " )
    int findClassesDynamicLineByClassId( @SQLParam("classesId")Integer classesId , @SQLParam("createTime") Date createTime );

    /**
     * 分页班级动态列表
     * @param classesId
     * @param fromPage
     * @param pageSize
     * @return
     */
    @SQL( " select id , classes_id , dynamic_info ,create_time from classes_dynamic " +
          " where classes_id=:classesId and create_time >:createTime order by create_time desc limit :fromPage , :pageSize " )
    List<ClassesDynamic> findClassesDynamicList(@SQLParam("classesId")Integer classesId , @SQLParam("fromPage")int fromPage,
                                                @SQLParam("pageSize")int pageSize , @SQLParam("createTime") Date createTime );

    /**
     * 获取学生所在班级id
     * @param studentId
     * @return
     */
    @SQL( "select classes_id from classes_student cs ,classes c where cs.classes_id =c.id   and c.type<>3 and  student_id=:studentId" )
    List<Integer> findClassOfStudent(@SQLParam("studentId")Integer studentId);
    /**
     * 获取学生所在班级的所有学科学段
     * @param classids
     * @return
     */
    @SQL( "SELECT DISTINCT  concat(subject_name,'_' ,range_name) from  teacher_book_version tbv " +
          " where tbv.teacher_id in (select teacher_id from classes_teacher where classes_id in (:classids))" )
    List<String> findCSubRangeOfClassIds(@SQLParam("classids")List<Integer> classids);

    /**
     * 查询学生可以查看动态的起始时间，（ 学生退出班级时会记录学生id  ）
     * @param classesId
    java.util. * @param studentId
     * @return
     */
    @SQL( " select max( create_time ) from classes_dynamic where classes_id=:classesId and user_id=:studentId " )
    Date findDateCanReadBegin(@SQLParam("classesId")Integer classesId , @SQLParam("studentId")Integer studentId );

    /**
     * 查询用户加入班级时间
     * @param userId
     * @return
     */
    @SQL( " select create_time  from classes_student where student_id=:userId and classes_id=:classesId and status =0  ")
    Date findCreateTimeByUserId(@SQLParam("userId")Integer userId , @SQLParam("classesId")Integer classesId);

    /**
     * 查询所有学生班级主键
     * @param studentId
     * @return
     */
    @SQL( " select b.id from classes_student a inner join classes b on a.classes_id = b.id where a.student_id=:studentId and b.type!=3 and a.status=0 and b.status=0 " )
    List<Integer> findAllClassesIdByStudentId(@SQLParam("studentId")Integer studentId);
}
