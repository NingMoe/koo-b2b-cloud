package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/4/5.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface TeacherAddClassDao {
    /**
     * 查询学科学段下所有班级
     * @param rangeId
     * @param subjectId
     * @return
     */
    @SQL("select id ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id from classes where status=0 and  graduate =0 and type !=:status  and  range_id=:rangeId and subject_id=:subjectId order by create_time,class_name desc" )
    List<Classes> findAllClassByRangeSub(@SQLParam("rangeId") Integer rangeId, @SQLParam("subjectId")Integer subjectId ,@SQLParam("status")int status);

    @SQL("select id ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id " +
            "from classes where school_id=:schoolId and  status=0 and  graduate =0 and type =:type  and" +
            "  range_name=:rangeName and subject_id=:subjectId order by year desc ," +
            "convert(class_name USING gbk) COLLATE gbk_chinese_ci asc ,create_time desc" )
    List<Classes> findAllClassByRangeSubType( @SQLParam("schoolId")int schoolId , @SQLParam("rangeName") String rangeName, @SQLParam("subjectId")Integer subjectId ,@SQLParam("type")int type);

    @SQL("select id ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id from classes where  school_id=:schoolId and status=0 and  graduate =0 and type =:type  and  range_name=:rangeName order by year desc ,convert(class_name USING gbk) COLLATE gbk_chinese_ci asc  ,create_time desc" )
    List<Classes> findAllClassByRangeSubType2( @SQLParam("schoolId")int schoolId , @SQLParam("rangeName") String rangeName  ,@SQLParam("type")int type);

    /**
     * 按照班级大小升序排，同年级按班级名称排序（包含学科班和行政班 ）
     * @param schoolId
     * @param rangeName
     * @param subjectId

     * @return
     */
    @SQL(" select id ,full_name ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id " +
            "from (  select id ,full_name ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id " +
            "from classes where school_id=:schoolId and  status=0 and  graduate =0 and type =1  and" +
            "  range_name=:rangeName and subject_id=:subjectId  " +
            " union " +
            " select id ,full_name ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id " +
            "from classes where  school_id=:schoolId and status=0 and  graduate =0 and type =0  and " +
            " range_name=:rangeName" +
            " ) f  " +
            " order by f.year desc ,convert( f.full_name USING gbk) COLLATE gbk_chinese_ci asc   ")
    List<Classes> findAllClassByRangeSubTypeForSort( @SQLParam("schoolId")int schoolId , @SQLParam("rangeName") String rangeName, @SQLParam("subjectId")Integer subjectId  );

    /**
     * 查询某入学年份的班级
     * @param rangeId
     * @param subjectId
     * @param year
     * @return
     */
    @SQL("select id ,class_name,class_code,type ,year,range_name,range_id ,subject_name ,subject_id ,teacher_id from classes where graduate =0 and year =:year and  range_id=:rangeId and subject_id=:subjectId order by year desc ,convert(class_name USING gbk) COLLATE gbk_chinese_ci asc" )
    List<Classes> findAllClassesByYearAndSubRange(@SQLParam("rangeId") Integer rangeId, @SQLParam("subjectId")Integer subjectId , @SQLParam("year")Integer year);

    @SQL("select id ,class_name,class_code,type ,year,range_name,range_id ,subject_name ,subject_id ,teacher_id from classes where status=0 and graduate =0 and year =:year and school_id=:schoolId and  range_name=:rangeName and subject_id=:subjectId and type=1 order by year desc ,convert(class_name USING gbk) COLLATE gbk_chinese_ci asc" )
    List<Classes> findAllClassesByYearAndRangeType(@SQLParam("schoolId")int schoolId , @SQLParam("rangeName") String rangeName, @SQLParam("subjectId")Integer subjectId , @SQLParam("year")Integer year);


    @SQL("select id ,class_name,class_code,type ,year,range_name,range_id ,subject_name ,subject_id ,teacher_id from classes where status=0 and graduate =0 and year =:year and school_id=:schoolId and   range_name=:rangeName and type=0  order by create_time ,class_name" )
    List<Classes> findAllClassesByYearAndRangeType2(@SQLParam("schoolId")int schoolId ,@SQLParam("rangeName") String rangeName , @SQLParam("year")Integer year);


    /**
     * 新增班级
     * @param classes
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    public int insertClassInfo( Connection conn, Classes classes);

    /**
     * 通过教师id查询教师属性信息
     * @param teacherId
     * @return
     */
    @SQL("select real_name ,type,user_name ,school_id ,school_name,province_id,province_name,city_id,city_name from user where id=:teacherId")
    User findTeacherNameById(@SQLParam("teacherId")int teacherId);

    /**
     * 查询所有的班号
     * @return
     */
    @SQL("select distinct(class_code) from classes where status = 0")
    List<String> findAllClassesCode();

    /**
     * 查询某学校内所有班级编码
     * @param schoolId
     * @return
     */
    @SQL( "select distinct(class_code) from classes where status = 0 and school_id=:schoolId" )
    List< String > findAllClassesCodeBySchoolId(@SQLParam("schoolId")int schoolId );
    /**
     * 根据班级id查询班级信息
     * @param classesId
     * @return
     */
    @SQL("select * from classes where status=0 and id=:classesId ")
    Classes findClassesById(@SQLParam("classesId")int classesId);
    /**
     * 根据班级id查询该班级下的任课教师
     * @param classesId
     * @return
     */
    @SQL("select distinct a.teacher_id ,a.classes_id , b.name as subjectName from classes_teacher a left JOIN dictionary b on a.subject_id = b.value where a.classes_id=:classesId")
    List<ClassesTeacher> findClassesTeacherByClassesId(@SQLParam("classesId")String classesId);

    /**
     * 查询老师下所有学科信息
     * @param teacherId
     * @return
     */
    @SQL("select id ,range_id,range_name,subject_id,subject_name from teacher_book_version " +
         " where status=:status and  teacher_id=:teacherId order by create_time asc")
    List<TeacherBookVersion> findTeacherBookVersion(@SQLParam("teacherId")int teacherId , @SQLParam("status") int status);

    /**
     * 查询老师下具体学科
     * @param teacherId
     * @param rangeId
     * @param status
     * @return
     */
    @SQL("select id ,range_id,range_name,subject_id,subject_name from teacher_book_version " +
         " where status=:status and  teacher_id=:teacherId and range_id=:rangeId  and subject_id=:subjectId order by create_time asc")
    List<TeacherBookVersion> findTeacherBookVersionByRangeIdAndSubjectId(@SQLParam("teacherId")int teacherId ,@SQLParam("rangeId")int rangeId,@SQLParam("subjectId")int subjectId , @SQLParam("status") int status);
    /**
     * 查询老师下某学段的所有学科
     * @param teacherId
     * @param status
     * @return
     */
    @SQL("select id ,range_id,range_name,subject_id,subject_name   from teacher_book_version where status=:status and  teacher_id=:teacherId   ")
    List<TeacherBookVersion> findTeacherBookVersionByRangeId(@SQLParam("teacherId")int teacherId  , @SQLParam("status") int status);

    /**
     * 通过学段名称查询老师下所有学科
     * @param teacherId
     * @param rangeName
     * @param status
     * @return
     */
    @SQL("select id ,range_id,range_name,subject_id,subject_name   from teacher_book_version where range_name=:rangeName and status=:status and  teacher_id=:teacherId order by create_time asc  ")
    List<TeacherBookVersion> findTeacherBookVersionByRangeName(@SQLParam("teacherId")int teacherId ,@SQLParam("rangeName")String rangeName , @SQLParam("status") int status);

    /**
     * 查询学科下的所有小组和学生
     * @param subjectId
     * @return
     */
    @SQL("select id,class_name ,year , range_name  from classes where status = 0 and  parent_id=:classesId and subject_id=:subjectId and range_id=:rangeId and school_id=:schoolId and teacher_id =:teacherId")
    List<Classes> findClassesBySubjectId(@SQLParam("subjectId")int subjectId ,@SQLParam("rangeId")int rangeId ,@SQLParam("classesId") int classesId ,@SQLParam("schoolId")int schoolId ,@SQLParam("teacherId")int teacherId);

    /**
     * 查询小组下所有同学
     * @param classesId
     * @return
     */
    @SQL( "select a.id , a.student_id ,a.classes_id ,a.headman  from classes_student a  where a.classes_id=:classesId and a.status=0 "   )
    List<ClassesStudent> findClassesStudentByClassesId(@SQLParam("classesId")Integer classesId   );

    @SQL( "select a.id , a.student_id ,a.classes_id ,a.headman  from classes_student a inner join classes b on a.classes_id = b.id  where a.classes_id=:classesId and a.status=0 and b.teacher_id=:teacherId "   )
    List<ClassesStudent> findClassesStudentByClassesIdAndTeacherId(@SQLParam("classesId")Integer classesId ,@SQLParam("teacherId")Integer teacherId );

    /**
     * 查询学生姓名
     * @param studentId
     * @return
     */
    @SQL("select real_name from user where id=:studentId")
    String findUserNameByUserId(@SQLParam("studentId")Integer studentId);

    /**
     * 查询该班级已分配小组的小组主键
     * @param classesId
     * @return
     */
    @SQL("select id  from classes where parent_id=:classesId and status = 0 and subject_id =:subjectId and range_id =:rangeId and teacher_id=:teacherId")
    List<Integer> findHaveTeamClassesIdByClassesId(@SQLParam("classesId")Integer classesId ,@SQLParam("subjectId")Integer subjectId ,@SQLParam("rangeId") Integer rangeId ,@SQLParam("teacherId") Integer teacherId);

    /**
     * 查询老师名下所有学科
     * @param teacherId
     * @return
     */
    @SQL("select id ,range_id ,subject_id ,subject_name  from teacher_book_version where teacher_id=:teacherId  and status= 1 order by  create_time asc ")
    List<TeacherBookVersion> findAllSubjectByTeacher(@SQLParam("teacherId")int teacherId);

    /**
     * 查询老师下所有学科的名称
     * @param teacherId
     * @param rangeName
     * @return
     */
    @SQL(" select subject_name  from teacher_book_version where teacher_id=:teacherId and range_name=:rangeName and status= 1 order by  create_time asc ")
    List<String> findAllSubjectNameByTeacher(@SQLParam("teacherId")int teacherId ,@SQLParam("rangeName") String rangeName );

    /**
     * 删除班级
     * @param classesId
     * @param teacherId
     * @return
     */
    @SQL("update  classes set status=1  where id=:classesId and teacher_id=:teacherId")
    int deleteClasses(@SQLParam("classesId")Integer classesId, @SQLParam("teacherId")int teacherId);

    /**
     * 通过班级编码查询班级信息
     * @param classesCode
     * @return
     */
    @SQL(" select c.*,s.name schoolName from classes c left join school s on c.school_id=s.id where c.class_code=:classesCode ")
	Classes findClassesByCode(@SQLParam("classesCode") String classesCode);

    /**
     * 插入老师班级表
     * @param conn
     * @param classesTeacher
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int insertClassesTeacher(Connection conn , ClassesTeacher classesTeacher);
    /**
     * 查询老师当前学段下的所有学科
     * @param teacherId
     * @param rangeName
     * @return
     */
    @SQL("select subject_id , subject_name ,id ,book_version_id  from teacher_book_version where teacher_id=:teacherId and range_name=:rangeName  and status= 1")
    List<TeacherBookVersion> findTeacherSubjects(@SQLParam("teacherId")int teacherId, @SQLParam("rangeName")String rangeName);

    /**
     * 查询所有未毕业班级
     * @return
     */
    @SQL( "select :outTime ,full_name ,class_name ,year ,type ,parent_id , range_id , range_name ,subject_id ,subject_name ,teacher_id ,school_id ,create_time" +
           " from classes where status=:status and graduate=:graduate and  school_id=:schooolId " )
    List<Classes> findAllClassesBySchoolId( @SQLParam("status")int status, @SQLParam("graduate")int graduate , @SQLParam("schooolId")int schooolId , @SQLParam("outTime")int outTime);

    /**
     * 查询某个学校没有毕业的所有班级
     * @param status
     * @param graduate
     * @param schoolId
     * @return
     */
    @SQL( "select full_name ,class_name ,year ,type ,parent_id , range_id , range_name ,subject_id ,subject_name ,teacher_id ,school_id ,create_time  " +
          " from classes where status=:status and graduate=:graduate and school_id=:schoolId " )
    List<Classes> findAllClasses(  @SQLParam("schoolId")int schoolId , @SQLParam("status")int status, @SQLParam("graduate")int graduate  );

    /**
     * 查询老师是否已经加入过改班级
     * @param teacherId
     * @param classesId
     * @return
     */
    @SQL( "select count( * ) from classes_teacher where teacher_id=:teacherId and classes_id=:classesId and subject_id=:subjectId and status=0 " )
    int findTeacherByTeacherIdAndClassesId( @SQLParam("teacherId")Integer teacherId , @SQLParam("classesId")Integer classesId ,@SQLParam("subjectId")Integer subjectId ) ;

    /**
     * 查询老师是否有行政班
     * @param teacherId
     * @param classesId
     * @return
     */
    @SQL( "select count( * ) from classes_teacher where teacher_id=:teacherId and classes_id=:classesId  and status=0 " )
    int findTeacherByTeacherIdForXingzheng( @SQLParam("teacherId")Integer teacherId , @SQLParam("classesId")Integer classesId   ) ;

    @SQL(" select id ,class_name,class_code,type ,year,range_name,teacher_id ,range_id ,subject_name ,subject_id " +
            "   from (  select c.id ,c.class_name,c.class_code,c.type ,c.year,c.range_name,c.teacher_id ,c.range_id ,c.subject_name ,c.subject_id " +
            "    from classes c, classes_teacher ct where c.status=0 and  c.graduate =0 and c.type =1 and c.range_name=:rangeName and c.subject_id=:subjectId and c.id = ct.classes_id and ct.teacher_id = :teacherId " +
            "     union " +
            "  select c.id ,c.class_name,c.class_code,c.type ,c.year,c.range_name,c.teacher_id ,c.range_id ,c.subject_name ,c.subject_id " +
            "    from classes c, classes_teacher ct where c.status=0 and  c.graduate =0 and c.type =0 and c.range_name=:rangeName and c.id = ct.classes_id and ct.teacher_id = :teacherId " +
            "      ) f  " +
            "  order by f.year desc ,convert( f.class_name USING gbk) COLLATE gbk_chinese_ci asc ")
    List<Classes> findAllClassByRangeSub(@SQLParam("rangeName") String rangeName, @SQLParam("subjectId")Integer subjectId,@SQLParam("teacherId")int teacherId  );
}
