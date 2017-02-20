package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.ClassesDynamic;
import com.koolearn.cloud.common.entity.ClassesDynamicTeacher;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * 班级最新动态
 * Created by fn on 2016/4/13.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface ClassNewStatusDao {


    /**
     * 记录班级动态
     * @param classesDynamic
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int insertClassesDynamic(Connection conn , ClassesDynamic classesDynamic);

    /**
     * 记录班级动态老师关联表
     * @param conn
     * @param classesDynamicTeacher
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int insertClassesDynamicTeacher(Connection conn , ClassesDynamicTeacher classesDynamicTeacher);

    /**
     * 查询班级下所有未读的动态
     * @param classesId
     * @return
     */
    @SQL( "select id , classes_id  ,status  from classes_dynamic where status =:status and classes_id=:classesId order by create_time desc " )
    List< ClassesDynamic> findNoReadDynamic(@SQLParam("classesId")Integer classesId ,@SQLParam("status")Integer status);

    /**
     * 查询班级动态表对应的班级老师动态是否读过的列表
     * @param classesDynamicId
     * @return
     */
    @SQL( "select id , teacher_id ,status from classes_dynamic_teacher where status=:status  and classes_dynamic_id=:classesDynamicId" )
    List<ClassesDynamicTeacher> findClassesDynamicTeacherById(@SQLParam("classesDynamicId")Integer classesDynamicId ,@SQLParam("status")Integer status );

    /**
     * 更新班级动态表
     * @param conn
     * @param classesDynamicId
     * @return
     */
    @SQL("update classes_dynamic set status=:status where id=:classesDynamicId")
    int updateClassesDynamicStatus(Connection conn,@SQLParam("classesDynamicId")Integer classesDynamicId ,@SQLParam("status")Integer status);

    @SQL("update classes_dynamic_teacher set status=:status where classes_dynamic_id=:classesDynamicId and teacher_id=:teacherId ")
    int updateClassesDynamicTeacherStatus(Connection conn , @SQLParam("teacherId")int teacherId ,@SQLParam("classesDynamicId")int classesDynamicId ,@SQLParam("status") int status);

    /**
     * 查询班级资源
     * @param classesId

     * @return
     */
    @SQL( "select count(1)  from classes_dynamic where classes_id=:classesId  " )
    int findClassesDynamicLine(@SQLParam("classesId")Integer classesId );

    /**
     * 查询班级动态
     * @param classesId
     * @return
     */
    @SQL("select id ,classes_id , dynamic_info ,status ,create_time from classes_dynamic where classes_id=:classesId order by create_time desc limit :begin , :end")
    List<ClassesDynamic> findClassesDynamicByClassId(@SQLParam("classesId")Integer classesId , @SQLParam("begin")int begin, @SQLParam("end")int end );
}
