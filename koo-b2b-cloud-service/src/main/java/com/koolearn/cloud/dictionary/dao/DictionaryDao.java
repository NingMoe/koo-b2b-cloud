package com.koolearn.cloud.dictionary.dao;

import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.SelectDTO;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLParam;

import java.util.List;

@DAO(dbtype = DbType.MYSQL, source = GlobalConstant.MYSQL_DATASOURCE)
public interface DictionaryDao {

    /**
     * 获得所有可用的字典
     *
     * @param status
     * @return
     */
    @SQL(" select * From dictionary where status=:status order by sort asc")
    List<Dictionary> getDataDictionaryByStats(@SQLParam("status") int status);

    /**
     * 根据类型获得字典信息分类集合
     *
     * @param type
     * @return
     */
    @SQL(" select * From dictionary where type=:type and status = 1 order by sort asc")
    List<Dictionary> getDataDictionaryByType(@SQLParam("type") Integer type);

    @SQL(" select name, value , type From dictionary where type=:type and status = 1 order by sort asc")
    List<Dictionary> getDataDictionaryByTypeOrder(@SQLParam("type") Integer type);

    @SQL("select distinct subject_id id ,subject_name name from teacher_book_version where teacher_id = :teacherId and status = 1")
    List<SelectDTO> findTeacherSubject(@SQLParam("teacherId") int teacherId);

    @SQL("select distinct range_id id ,range_name name from teacher_book_version where teacher_id = :teacherId and subject_id =:subjectId and status = 1")
    List<SelectDTO> findTeacherRange(@SQLParam("teacherId") int teacherId, @SQLParam("subjectId") int subjectId);

    @SQL("select distinct book_version_id id ,book_version_name name from teacher_book_version where teacher_id = :teacherId and range_id = :rangeId and status = 1")
    List<SelectDTO> findTeacherBookVersion(@SQLParam("teacherId") int teacherId, @SQLParam("rangeId") int rangeId);

    @SQL("select distinct subject_name  name, subject_id id from teacher_book_version where teacher_id = :teacherId and status = 1")
    List<SelectDTO> findTeacherSubjectName(@SQLParam("teacherId") int teacherId);

    /**
     * 查询指定名字 指定类型的 第一个符合条件的字典实体
     * @param name
     * @param type
     * @return
     */
    @SQL("SELECT * FROM dictionary WHERE TYPE =:dicType AND NAME =:name limit 1")
    Dictionary queryDictionaryByNameAndType(@SQLParam("name")String name,@SQLParam("dicType")Integer type);

}
