package com.koolearn.cloud.school.student.dao;

import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.student.vo.StudentPageDto;
import com.koolearn.cloud.school.student.vo.StudentPageVo;
import com.koolearn.framework.aries.annotation.DAO;
import com.koolearn.framework.aries.annotation.DbType;
import com.koolearn.framework.aries.annotation.SQL;
import com.koolearn.framework.aries.annotation.SQLType;

import java.util.List;

/**
 * Created by fn on 2016/11/10.
 */
@DAO(dbtype= DbType.MYSQL,source= CommonConstant.MYSQL_DATASOURCE)
public interface SchoolStudentManageDao {

    /**
     * 学生分页查询总行数
     * @param studentPageVo
     * @return
     */
    @SQL(type= SQLType.READ)
    Integer findStudentTotalPage(StudentPageVo studentPageVo);
    /**
     * 学生分页查询结果列
     * @param studentPageVo
     * @return
     */
    @SQL(type= SQLType.READ)
    List<StudentPageDto> findStudentPageList(StudentPageVo studentPageVo);
}
