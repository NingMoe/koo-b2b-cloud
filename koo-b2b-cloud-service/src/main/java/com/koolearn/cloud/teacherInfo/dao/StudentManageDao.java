package com.koolearn.cloud.teacherInfo.dao;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;

import java.sql.Connection;
import java.util.List;

/**
 * Created by fn on 2016/4/8.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface StudentManageDao {
    /**
     * 删除学生
     * @param studentsId
     * @return
     */
    @SQL("update  classes_student set status = 1 where id =:studentsId")
    int deleteStudentsById(@SQLParam("studentsId") int studentsId );

    /**
     * 修改毕业状态
     * @param status
     * @return
     */
    @SQL("update classes set graduate=:status where id=:classesId")
    int updateGraduateStatus(Connection conn , @SQLParam("status")String status,@SQLParam("classesId")String classesId);

    /**
     * 创建小组
     * @param classes
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int createTeamForClasses(Connection conn , Classes classes );

    /**
     * 删除小组学生
     * @param conn
     * @param classesId
     * @return
     */
    @SQL("update classes_student set status=:status where  classes_id=:classesId")
    int deleteStudentByClassesId(Connection conn  ,@SQLParam("classesId")int classesId ,@SQLParam("status")int status );
    /**
     * 删除classes表的小组
     * @param conn
     * @param classesId
     * @return
     */
    @SQL("update classes set status=:status where id=:classesId")
    int deleteClassesByClassesId(Connection conn  ,@SQLParam("classesId")int classesId ,@SQLParam("status")int status);

    /**
     * 查询同一个班级下是否有重名的小组
     * @param conn
     * @param classesId
     * @param name
     * @return
     */
    @SQL("select class_name from classes where parent_id=:classesId and class_name=:name " )
    List<String> findTeamIdById(Connection conn , @SQLParam("classesId") int classesId ,@SQLParam("name")String name );

    /**
     * 修改小组名字
     * @param conn
     * @param teamId
     * @param teamName
     * @return
     */
    @SQL("update classes set class_name=:teamName , full_name =:fullName ,update_time =now()  where id=:teamId")
    int updateTeamName(Connection conn , @SQLParam("teamId") int teamId ,@SQLParam("teamName") String teamName,@SQLParam("fullName") String fullName);

    /**
     * 修改用户密码重置过的状态
     * @param ssoUserId
     */
    @SQL( "update user set update_password_status =:status  where user_id=:ssoUserId" )
    void updateUserPasswordStatusBySSOId(Connection conn , @SQLParam("ssoUserId")Integer ssoUserId , @SQLParam("status") int status );

}
