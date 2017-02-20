package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.Classes;

/**
 * Created by fn on 2016/4/8.
 */
public interface StudentManageService {
     ;

    /**
     * 删除学生
     * @param classesId
     * @return
     */
    int deleteStudentsById(int classesId);
    /**
     * 重置密码
     * @param
     * @return
     */
    void resetStudentPassword(String studentId) throws Exception;
    /**
     * 修改毕业状态
     * @param status
     * @return
     */
    int updateGraduateStatus(String status ,String classesId);
    /**
     * 创建小组
     * @param classes
     * @return
     */
    int createTeamForClasses(Classes classes , String realName );

    /**
     * 删除小组
     * @return
     */
    int deleteTeamByTeamId(Integer classesId);

    /**
     * 修改小组名字
     * @param teamId
     * @param teamName
     * @return
     */
    int updateTeamName( int teamId ,int classesId, String teamName );

}
