package com.koolearn.cloud.school.authorityschool;

import com.koolearn.cloud.school.authorityschool.vo.*;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.entity.SchoolPage;

import java.util.Map;

/**
 * Created by fn on 2016/11/15.
 */
public interface AuthoritySchoolService {
    /**
     * 管理用户分页查询
     * @param schoolPage
     * @return
     */
    Map<String,Object> findSchoolMangePage(SchoolPage schoolPage);
    /**
     * 冻结解冻学校管理用户
     * @param type ( 1激活  2 冻结)
     */
    void updateSchoolManageStatus(Integer managerId, int type);

    /**
     * 重置管理员密码
     * @param managerId
     */
    void resetManagePassword(Integer managerId, String password);

    /**
     * 获取当前学校的所有学段和年级及学科
     */
    SchoolInfo findShoolInfo(Integer schoolId);

    /**
     * 创建学校管理用户
     * @param managerNew
     * @return
     */
    int insertSchoolManager(Manager managerNew);

    /**
     * 验证管理员邮箱是否可用
     * param email
     * @return
     */
    boolean checkManagerEmail(String email);

    /**
     * 验证管理者手机
     * @param mobile
     * @return
     */
    boolean checkManagerMobile(String mobile);

    /**
     * 查询管理者信息用于更新
     * @param managerId
     * @return
     */
    SchoolManagerDTO findManagerForUpdate(Integer managerId);

    /**
     * 更新管理者信息
     * @param schoolManagerDto
     * @return
     */
    int updateManagerInfo(SchoolManagerDTO schoolManagerDto);
}
