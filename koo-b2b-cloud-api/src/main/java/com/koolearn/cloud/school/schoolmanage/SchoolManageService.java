package com.koolearn.cloud.school.schoolmanage;

import com.koolearn.cloud.school.schoolmanage.vo.SchoolPowerDto;

/**
 * Created by fn on 2016/11/21.
 */
public interface SchoolManageService {
    /**
     * 查询学校基本信息
     * @param schoolId
     * @return
     */
    SchoolPowerDto findSchoolBaseInfo(Integer schoolId);

    /**
     * 查询学校扩展信息
     * @param schoolPowerDto
     * @param schoolId
     * @return
     */
    SchoolPowerDto findSchoolExtend( SchoolPowerDto schoolPowerDto ,Integer schoolId );

    /**
     * 查询学校管理员信息
     * @param schoolPowerDto
     * @param schoolId
     * @return
     */
    SchoolPowerDto findSchoolManagerBuSchoolId( SchoolPowerDto schoolPowerDto ,Integer schoolId );

    /**
     * 修改学校扩展信息
     * @param schoolPowerDto
     */
    void updateSchoolExtendInfo(SchoolPowerDto schoolPowerDto );

    /**
     * 创建学校升级信息
     * @param schoolPowerDto
     * @return
     */
    Integer insertSchoolUp( SchoolPowerDto schoolPowerDto );

    /**
     * 学校年级定时任务升级
     */
    void schoolClassesUpScheduled();
    /**
     * 财务确认后生成学校升级的默认升级数据（对外接口）
     * @return
     */
    int addSchoolUpForFinanceConfirm(Integer schoolId);

}
