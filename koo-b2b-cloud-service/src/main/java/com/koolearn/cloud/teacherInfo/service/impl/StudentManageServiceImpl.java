package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.teacher.service.StudentManageService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.StudentManageDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.sso.service.IOpenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by fn on 2016/4/8.
 */
@Service("studentManageService")
public class StudentManageServiceImpl implements StudentManageService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentManageDao studentManageDao;
    @Autowired
    private IOpenService iopenService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }

    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }

    public StudentManageDao getStudentManageDao() {
        return studentManageDao;
    }

    public void setStudentManageDao(StudentManageDao studentManageDao) {
        this.studentManageDao = studentManageDao;
    }

    public IOpenService getIopenService() {
        return iopenService;
    }

    public void setIopenService(IOpenService iopenService) {
        this.iopenService = iopenService;
    }

    /**
     * 删除学生
     * @param studentsId
     * @return
     */
    @Transactional
    @Override
    public int deleteStudentsById(int studentsId ) {
        int num = 0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            studentManageDao.deleteStudentsById( studentsId );
            conn.commit();
        } catch (SQLException e) {
            log.error( " 删除小组学生信息异常 ，学生id：" + studentsId + " ," +e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (Exception e) {
            log.error( " 删除小组学生信息异常 ，学生id：" + studentsId + " ," +e.getMessage()  , e );
            e.printStackTrace();
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 重置密码
     * @param  studentId
     * @return
     */
    @Override
    public void resetStudentPassword(String studentId) throws Exception {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            iopenService.resetPassword( new Integer( studentId ), CommonInstence.RESET_PASSWORD );
            //更新用户表修改过密码状态
            studentManageDao.updateUserPasswordStatusBySSOId(conn, new Integer( studentId ) ,CommonInstence.STATUS_1 );
            conn.commit();
        }catch ( SQLException e ){
            log.error( "重置密码SSO接口异常，学生或教师id:" + studentId );
            ConnUtil.rollbackConnection(conn);
            throw new  Exception("重置密码SSO接口异常，学生id:" + studentId + e.getMessage() ,e   );
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 修改毕业状态
     * @param status
     * @return
     */
    @Override
    public int updateGraduateStatus(String status ,String classesId) {
        Connection conn = null;
        int num=0;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = studentManageDao.updateGraduateStatus(conn, status ,classesId );
            //从缓存中删除
            teacherAddClassService.deleteClassesFromCache(CommonInstence.classes_map , CommonInstence.REDIS_CLASSKY + classesId );
            conn.commit();
        } catch (SQLException e) {
            log.error( "修改毕业状态异常 ，班级ID:"+classesId  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }

    /**
     * 创建小组
     * @param classes
     * @return
     */
    @Override
    public int createTeamForClasses(Classes classes , String realName ) {
        Connection conn = null;
        int pramary =0;
        try {
            conn = ConnUtil.getTransactionConnection();
            //创建小组
            pramary = studentManageDao.createTeamForClasses( conn , classes );
            //创建小组老师表数据
            classes.setId( pramary );
            ClassesTeacher teacher = teacherAddClassService.makeClassesTeacher(classes);
            //插入班级老师表
            teacherAddClassService.insertClassesTeacher( teacher ,classes.getTeacherId() , classes , realName + "创建了新小组" ,false );
            conn.commit();
        } catch (SQLException e) {
            log.error( "创建小组异常 ，小组名称:" +classes.getClassName() + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return pramary;
    }

    /**
     * 删除小组
     * @param classesId
     * @return
     */
    @Override
    public int deleteTeamByTeamId(Integer classesId) {
        Connection conn = null;
        int num =0;
        try {
            conn = ConnUtil.getTransactionConnection();
            //删除小组学生
            num = studentManageDao.deleteStudentByClassesId( conn , classesId ,CommonInstence.STUDENT_STATUS_DELETE);
            num = studentManageDao.deleteClassesByClassesId(conn , classesId ,CommonInstence.STUDENT_STATUS_DELETE);
            conn.commit();
        } catch (SQLException e) {
            log.error( "删除（修改状态）小组异常 ，小组id:" + classesId+ e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 修改小组名字
     * @param classesId
     * @param teamName
     * @return
     */
    public int updateTeamName( int teamId ,int classesId, String teamName ){
        Connection conn = null;
        int num =0;
        try {
            conn = ConnUtil.getTransactionConnection();
            /*
            验证是否有重名
             List<String> nameList = studentManageDao.findTeamIdById( conn , classesId ,teamName );
            if( nameList == null || nameList.size() == 0 ){
            }
            */
            Classes classes = teacherAddClassService.findClassesById(  classesId   );
            if( classes != null ){
                String fullName = classes.getClassName()+teamName;
                num = studentManageDao.updateTeamName(conn , teamId ,teamName ,fullName );
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "删除（修改状态）小组异常 ，小组id:" + classesId + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
}
