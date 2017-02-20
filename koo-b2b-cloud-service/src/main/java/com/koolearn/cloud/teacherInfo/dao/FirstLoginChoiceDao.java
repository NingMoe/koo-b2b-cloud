package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/3/31.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface FirstLoginChoiceDao {

    @SQL("select count( 1 ) from teacher_book_version where teacher_id =:teacherId")
    int findTeacherSubjectNum(@SQLParam("teacherId") int teacherId);

    /**
     * 查询教师下的教材
     * @param teacherId
     * @return
     */
    @SQL("select  id, teacher_id ,range_id , range_name, subject_id , subject_name , book_version_id , book_version_name ,create_time ,status ,reorder from teacher_book_version where teacher_id =:teacherId order by reorder ")
    List<TeacherBookVersion> findTeacherSubject(@SQLParam("teacherId") int teacherId);

    /**
     * 查询教师教材信息
     * @param teacherBookVersionId
     * @return
     */
    @SQL("select id, teacher_id ,range_id , range_name, subject_id , subject_name , book_version_id , book_version_name ,create_time ,status ,reorder from teacher_book_version where id=:teacherBookVersionId")
    TeacherBookVersion findTeacherBookVersionById(@SQLParam("teacherBookVersionId")int teacherBookVersionId);
}
