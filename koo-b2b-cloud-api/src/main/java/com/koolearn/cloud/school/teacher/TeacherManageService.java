package com.koolearn.cloud.school.teacher;

import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.school.dto.TeacherPageDto;
import com.koolearn.cloud.school.teacher.vo.TeacherPageResultDto;

import java.util.List;
import java.util.Map;

/**
 * 学校端教师管理服务
 * Created by fn on 2016/11/3.
 */
public interface TeacherManageService {

    /**
     * 教师分页查询
     * @param teacherPageDto
     * @return
     */
    Map<String,Object> findClassPage(TeacherPageDto teacherPageDto);

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    boolean isExistMail(String email );

    /**
     * 批量添加excel教师
     * @param userList
     * @return
     */
    List<User> insertTeacherExcelToDb(List<User> userList,Integer schoolId) throws Exception;

    /**
     * 添加教师信息
     * @param user
     * @param schoolId
     * @return
     */
    int addTeacherBaseInfo( User user , Integer schoolId) throws Exception;
    /**
     * 判断电话是否存在
     * @param mobile
     * @return
     */
    boolean isExistMobile(String mobile);

    /**
     * 修改教师信息
     */
    void updateTeacherBaseInfo(User user ) throws Exception;

    /**com.koolearn.cloud.school.teacher.vo.
     * 修改教师状态
     * @param userId
     * @param status
     * @param managerName
     */
    void updateTeacherOrStudentStatus(Integer userId, Integer status, String managerName) throws Exception;

    /**
     * 查询教师学段学科及电话信息
     * @param userId
     * @return
     */
    TeacherPageResultDto findTeacherInfoForUpdate(Integer userId);

    /**
     * 生成教师账号
     */
    List< User> makeTeacherUserName(List<User> userList, Integer schoolId );



}
