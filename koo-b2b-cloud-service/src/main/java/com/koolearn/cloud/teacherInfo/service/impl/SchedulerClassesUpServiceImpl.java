package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.teacher.service.SchedulerClassesUpService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.SchedulerClassesUpDao;
import com.koolearn.cloud.teacherInfo.dao.StudentManageDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by fn on 2016/7/5.
 */
@Service("schedulerClassesUpService")
public class SchedulerClassesUpServiceImpl implements SchedulerClassesUpService {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private SchedulerClassesUpDao schedulerClassesUpDao;
    @Autowired
    private StudentManageDao studentManageDao;

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }

    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }

    public SchedulerClassesUpDao getSchedulerClassesUpDao() {
        return schedulerClassesUpDao;
    }

    public void setSchedulerClassesUpDao(SchedulerClassesUpDao schedulerClassesUpDao) {
        this.schedulerClassesUpDao = schedulerClassesUpDao;
    }

    public StudentManageDao getStudentManageDao() {
        return studentManageDao;
    }

    public void setStudentManageDao(StudentManageDao studentManageDao) {
        this.studentManageDao = studentManageDao;
    }
    //@Scheduled(cron="1 1 0 15 8 ?"  )//每年8月15日
    @Override
    public void classesUpLevel(   ) {
        //TODO 二期只对个别学校升级
        Integer schoolId = null;
        log.info( "未毕业班级升级开始," + DateFormatUtils.dateLFormat( new Date( )));
        List<Classes> classesList =null;
        if( null == schoolId ){
            //先查询全部班级
            classesList = schedulerClassesUpDao.findAllClassesId();
        }else{
            //某学校下所有班级
            classesList = schedulerClassesUpDao.findAllClassesIdBySchoolId( schoolId );
        }
        try{
            upLevelForClassesById( classesList );
        }catch ( Exception e ){
            e.printStackTrace();
            log.info( "班级升级异常");
        }
    }

    /**
     * 根据未毕业班级id升级班级（修改班级fullName，班级level ， 小组名称 ，毕业状态 ）
     * @param classesList
     */
    private void upLevelForClassesById(List<Classes> classesList){
        if( null != classesList && classesList.size() > 0 ){
            Connection conn = null;
            try {
                conn = ConnUtil.getTransactionConnection();
                for( int i = 0 ; i < classesList.size() ;i++ ){
                    Integer classesId = classesList.get( i ).getId();
                    Classes classes = classesList.get( i );
                    classes.setOk( false );//需要获取原名称
                    int level = classesList.get( i ).getLevel();
                    //还没到毕业的班级
                    if( level < 6 || level < 13&& level > 7  || level < 23 && level > 20){
                        String classesName = CommonEnum.getClassesNameByLevelEnum.getSource( level + 1 ).getValue();
                        String fullName = classesName + classes.getClassName();
                        int levelUp = level + 1;
                        //更新班级名称
                        log.info( "未毕业的班级id:" + classesId +"升级后level:"+levelUp );
                        int num = schedulerClassesUpDao.updateClassesUpInfo( conn , classesId , fullName , levelUp );
                        if( num == 0 ){
                            throw new BizException( "批量升级班级失败" );
                        }else{
                            //TODO 清除缓存
                            teacherAddClassService.deleteClassesFromCache( CommonInstence.classes_map , CommonInstence.REDIS_CLASSKY + classesId);
                            //升级班级下的小组（ 修改小组的fullName ）
                            //查询班级下所有小组
                            List< Classes > classTeamList = schedulerClassesUpDao.findAllTeamByClassesId( classesId , CommonInstence.STATUS_0 );
                            if( null != classTeamList && classTeamList.size() > 0 ){
                                for( int j = 0 ; j < classTeamList.size();j++ ){
                                    classTeamList.get( j ).setOk( false );
                                    String teamFullName = fullName + "_" + classTeamList.get( j ).getClassName();
                                    //更新小组名称
                                    int numTeam = schedulerClassesUpDao.updateClassesTeamName( conn ,classTeamList.get( j ).getId() ,teamFullName );
                                    log.info( "未毕业的班级小组id:" + classTeamList.get( j ).getId() +"小组名称:"+ teamFullName);
                                    if( 0 == numTeam ){
                                        throw new BizException( "批量升级班级的小组失败" );
                                    }
                                }
                            }
                        }
                    }//升级应该毕业的班级
                    else if( level == 6 || level == 13 || level == 23 ){
                        //TODO 修改参数类型  确认需求，是否只修改毕业状态
                        log.info( "应该毕业的班级id:" + classesId +"升级后level:"+level+1 );
                        int num = schedulerClassesUpDao.updateClassesGraduateStatus(conn, CommonInstence.STATUS_1 , classesId ,level+1 );
                        //修改
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                log.error( "批量升级班级异常 " + e.getMessage()  , e );
                ConnUtil.rollbackConnection(conn);
                throw new BizException( "批量升级班级异常" );
            } catch (Exception e) {
                log.error( "批量升级班级异常" + e.getMessage() ,e );
                throw new BizException( "批量升级班级异常" );
            } finally {
                ConnUtil.closeConnection(conn);
            }
        }
    }
}
