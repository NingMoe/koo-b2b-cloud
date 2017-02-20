package com.koolearn.cloud.school.common;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.entity.TbSchoolUp;
import com.koolearn.cloud.school.entity.dto.SubjectDto;
import com.koolearn.cloud.school.entity.dto.TeacherDto;
import com.koolearn.cloud.teacher.entity.School;

import java.util.List;

/**
 * Created by fn on 2016/10/31.
 */
public interface SchoolCommonService {
    /**
     * 根据学校id和用户id查询当前用户所属权限下的所属学校的所有学段和班级
     * @param schoolId
     * @return
     */;
    public SchoolInfo findAllSchoolBySchoolId( Integer schoolId ,Integer userId );

    /**
     * 查询学校升级信息
     * @param schoolId
     * @return
     */
    public TbSchoolUp findSchoolUpInfo(Integer schoolId);

    /**
     * 查询班级的毕业日期
     * @param schoolId
     * @ classes
     * @return
     */
    String findClassFinishDay(Integer schoolId, Classes classes);

    /**
     * 查询所有学科
     */
    List<SubjectDto> findAllSubject();

    /**
     * 查询学校下所有学段
     * @param schoolId
     * @param userId
     * @return
     */
    SchoolInfo findAllSchoolRangeBySchoolId(Integer schoolId, Integer userId);
    /**
     * 接口异常信息记录
     * @param interfaceName
     * @param logInfo
     */
    public void insertErrorLog( String interfaceName , String logInfo ,String remark );
}
