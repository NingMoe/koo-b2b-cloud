package com.koolearn.cloud.school.schoolmanage;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.Common.dao.SchoolCommonDao;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.TbSchoolExtend;
import com.koolearn.cloud.school.entity.TbSchoolUp;
import com.koolearn.cloud.school.entity.dto.ClassesDto;
import com.koolearn.cloud.school.schoolmanage.dao.SchoolManageDao;
import com.koolearn.cloud.school.schoolmanage.vo.SchoolLevelDto;
import com.koolearn.cloud.school.schoolmanage.vo.SchoolPowerDto;
import com.koolearn.cloud.teacher.entity.RangeSubject;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.SchedulerClassesUpDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.log4j.Logger;
import org.jasypt.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by fn on 2016/11/21.
 */
@Service("schoolManageService")
public class SchoolManageServiceImpl implements SchoolManageService {
    Logger log = Logger.getLogger( this.getClass() );

    @Autowired
    private SchoolManageDao schoolManageDao;
    @Autowired
    private SchoolCommonDao schoolCommonDao;
    @Autowired
    private SchedulerClassesUpDao schedulerClassesUpDao;
    @Autowired
    private TeacherAddClassService teacherAddClassService;

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }
    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }
    /**
     * 查询学校基本信息
     * @param schoolId
     * @return
     */
    public SchoolPowerDto findSchoolBaseInfo(Integer schoolId){
        School school = schoolManageDao.findSchoolById( schoolId , CommonInstence.STATUS_0);
        SchoolPowerDto schoolPowerDto = null;
        if( null != school ){
            schoolPowerDto = new SchoolPowerDto();
            schoolPowerDto.setSchoolId( schoolId );
            schoolPowerDto.setSchoolName( school.getName() );
            schoolPowerDto.setBeginTime(DateFormatUtils.getDay( school.getBeginTime()) );
            schoolPowerDto.setEndTime( DateFormatUtils.getDay(school.getEndTime()) );
            String grandId = school.getGradeId();
            if( null != grandId ){
                String[] grandIdArray = grandId.split( "," );
                if( null != grandIdArray ){
                    StringBuffer sb = new StringBuffer( );
                    //获取学段名称
                    for( int i = 0 ;i < grandIdArray.length;i++ ){
                        String rangeName = CommonEnum.getRangeNameEnum.getSource( new Integer( grandIdArray[ i ])).getValue();
                        sb.append( rangeName );
                        if( i < grandIdArray.length - 1 ){
                            sb.append( "," );
                        }
                    }
                    schoolPowerDto.setRangeInfo( sb.toString() );
                }
            }
            //查询学校扩展信息
            findSchoolExtend( schoolPowerDto , schoolId );
            //查询学校管理员
            findSchoolManagerBuSchoolId( schoolPowerDto , schoolId );
            //查询学校下所有学段的所有年级
            findAllRangeAndLevel( schoolPowerDto , schoolId );


        }
        return schoolPowerDto;
    }

    /**
     * 查询学校下所有学段的所有年级
     * @param schoolPowerDto
     * @param schoolId
     * @return
     */
    private SchoolPowerDto findAllRangeAndLevel( SchoolPowerDto schoolPowerDto , Integer schoolId ){
        Map< String , Map<String,SchoolLevelDto >> rangedLevelMap = new LinkedHashMap<String, Map<String, SchoolLevelDto>>();
        //查询当前学校下所有学段
        String rangeType = schoolCommonDao.findAllRangeBySchoolId( schoolId );
        if( StringUtils.isNotEmpty(rangeType)){
            String[] rangeArray = rangeType.split( "," );
            if( null != rangeArray ){
                //获取学段id对应的学段名称
                for( String range : rangeArray ){
                    Integer rangeId = new Integer( range );
                    String rangeName = CommonEnum.getRangeNameEnum.getSource( rangeId ).getValue();
                    //获取所有班级 :1:一年级,2:二年级,3:三年级,4:四年级,5:五年级,6:六年级
                    String classes = CommonEnum.getClassesLevelsEnum.getSource( rangeId ).getValue();
                    if( StringUtils.isNotEmpty(classes)){
                        String[] classLevelsArray = classes.split( "," );
                        if( null != classLevelsArray ){
                            //存放每个学段的年级
                            Map<String,SchoolLevelDto > levelMap = new LinkedHashMap<String, SchoolLevelDto>();
                            for( String classIdAndName : classLevelsArray ){
                                if( null != classIdAndName ){
                                    String[] idNameArray = classIdAndName.split(":");
                                    SchoolLevelDto schoolLevelDto = new SchoolLevelDto();
                                    for( int i = 0 ; i < idNameArray.length;i++ ){
                                        //存储班级id和名称
                                        schoolLevelDto.setLevelId( new Integer(idNameArray[0]) );
                                        schoolLevelDto.setLevelName( idNameArray[1] );
                                        //用户前端页面CheckBox判断毕业的年级是否需要选中
                                        if( schoolPowerDto.getClassGraduateLevel().indexOf( idNameArray[1] ) != -1 ){
                                            schoolLevelDto.setIsCheck( 1 );
                                        }
                                        levelMap.put(idNameArray[1], schoolLevelDto);
                                    }
                                }
                            }
                            rangedLevelMap.put( rangeName , levelMap );
                        }
                    }
                }
                schoolPowerDto.setRangedLevelMap( rangedLevelMap );
            }
        }
        return schoolPowerDto;
    }

    /**
     *  查询学校扩展信息
     * @param schoolPowerDto
     * @param schoolId
     * @return
     */
    public SchoolPowerDto findSchoolExtend( SchoolPowerDto schoolPowerDto ,Integer schoolId ){
        if( null != schoolPowerDto ){
            TbSchoolExtend tbSchoolExtend = schoolManageDao.findSchoolExtendBySchoolId(schoolId );
            // 自动毕业年级名称
            List< Integer > aotoGradeLevelNameList = new ArrayList<Integer>();
            if( null != tbSchoolExtend ){
                schoolPowerDto.setContacter( tbSchoolExtend.getContacter() );
                schoolPowerDto.setContacterMail( tbSchoolExtend.getContacterMail() );
                schoolPowerDto.setContacterMobile( tbSchoolExtend.getContacterMobile() );
                schoolPowerDto.setMaxOnLine( tbSchoolExtend.getMaxOnline() );

                //查询学校升级信息
                TbSchoolUp tbSchoolUp = schoolManageDao.findSchoolUpBySchoolId(schoolId);
                if( null != tbSchoolUp ){
                    //用于判断前端页面是否可以设置升级时间，
                    Integer upYear = tbSchoolUp.getUpYear();
                    Integer gradeYear = tbSchoolUp.getGraduateYear();
                    int nowYear = DateFormatUtils.getYear( new Date( ));
                    if( nowYear == upYear ){
                        schoolPowerDto.setHadUpClasses( CommonInstence.STATUS_1 );
                    }if( nowYear == gradeYear ){
                        schoolPowerDto.setHadGrade( CommonInstence.STATUS_1 );
                    }
                    schoolPowerDto.setAutoUpTime(  DateFormatUtils.getDay(tbSchoolUp.getAutoUpTime() ));
                    schoolPowerDto.setAutoGraduateTime( DateFormatUtils.getDay(tbSchoolUp.getAutoGraduateTime() ) );
                    String classGraduateLevel = tbSchoolUp.getClassGraduateLevel();
                    if( null != classGraduateLevel ) {
                        String[] classLevelArray = classGraduateLevel.split( "," );
                        if( null != classLevelArray ){
                            StringBuffer sbLevel = new StringBuffer();
                            for( int i = 0 ; i < classLevelArray.length;i++ ){
                                sbLevel.append( CommonEnum.getClassesNameByLevelEnum.getSource( new Integer( classLevelArray[ i ])).getValue());
                                aotoGradeLevelNameList.add(new Integer(classLevelArray[i]));
                                if( i < classLevelArray.length ){
                                    sbLevel.append( "," );
                                }
                            }
                            schoolPowerDto.setAotoGradeLevelValueList(aotoGradeLevelNameList);
                            schoolPowerDto.setClassGraduateLevel( sbLevel.toString() );
                        }
                    }
                }
            }
        }
        return schoolPowerDto;
    }

    /**
     * 查询学校管理员信息
     * @param schoolPowerDto
     * @param schoolId
     * @return
     */
    public SchoolPowerDto findSchoolManagerBuSchoolId( SchoolPowerDto schoolPowerDto ,Integer schoolId ){
        if( null != schoolPowerDto ){
            List< Manager > managerList = schoolManageDao.findManagerBySchoolId( schoolId ,CommonInstence.STATUS_1 );
            if( null != managerList ){
                List< String > managerNameList = new LinkedList<String>();
                for( Manager manager : managerList ){
                    managerNameList.add( manager.getManagerName() );
                }
                schoolPowerDto.setManagerNameList( managerNameList );
            }
        }
        return schoolPowerDto;
    }
    /**
     * 修改学校扩展信息
     * @param schoolPowerDto
     */
    public void updateSchoolExtendInfo(SchoolPowerDto schoolPowerDto ){
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            //修改联系人
            int num = schoolManageDao.updateSchoolExtendInfo( schoolPowerDto );
            TbSchoolUp tbSchoolUp = schoolManageDao.findSchoolUpBySchoolId(schoolPowerDto.getSchoolId());
            int nowYear = DateFormatUtils.getYear( new Date());
            if( null != tbSchoolUp ){
                //判断是否升级过
                Integer nowYearUpDb = tbSchoolUp.getUpYear();//升级年份
                if( nowYear > nowYearUpDb ){
                    //更新升级信息
                    schoolManageDao.updateSchoolUp( schoolPowerDto.getSchoolId() , schoolPowerDto.getAutoUpTime(),schoolPowerDto.getUpdater() ,tbSchoolUp.getVersion(),tbSchoolUp.getVersion()+1);
                }
                TbSchoolUp tbSchoolUpGraduate = schoolManageDao.findSchoolUpBySchoolId(schoolPowerDto.getSchoolId());
                if( null != tbSchoolUpGraduate ){
                    Integer nowYearGraduateDb = tbSchoolUpGraduate.getGraduateYear();//毕业年份
                    //如果未毕业
                    if( nowYear > nowYearGraduateDb ){
                        schoolManageDao.updateSchoolUpGraduate( schoolPowerDto.getSchoolId() , schoolPowerDto.getAutoGraduateTime() ,schoolPowerDto.getUpdater(),
                                tbSchoolUpGraduate.getVersion() , tbSchoolUpGraduate.getVersion() +1 );
                    }
                }
            }else{
                insertSchoolUp( schoolPowerDto );
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( "修改学校扩展信息异常"   + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }


    }

    /**
     * 学校升级信息入库
     * @param schoolPowerDto
     * @return
     */
    public Integer insertSchoolUp( SchoolPowerDto schoolPowerDto ){
        Integer id = 0 ;
        if( null != schoolPowerDto ){
            TbSchoolUp tbSchoolUp = new TbSchoolUp();
            tbSchoolUp.setClassGraduateLevel( schoolPowerDto.getClassGraduateLevel() );
            tbSchoolUp.setSchoolId( schoolPowerDto.getSchoolId() );
            tbSchoolUp.setAutoGraduateTime( DateFormatUtils.dateStrFormat( schoolPowerDto.getAutoGraduateTime() ));
            tbSchoolUp.setAutoUpTime( DateFormatUtils.dateStrFormat( schoolPowerDto.getAutoUpTime() ) );
            tbSchoolUp.setCreator( schoolPowerDto.getUpdater());
            tbSchoolUp.setCreateTime( new Date());
            Connection conn = null;
            try {
                conn = ConnUtil.getTransactionConnection();
                id = schoolManageDao.insertSchoolUp( conn, tbSchoolUp );
                conn.commit();
            }catch (SQLException e) {
                log.error("创建学校升级信息异常" + e.getMessage(), e);
                ConnUtil.rollbackConnection(conn);
            }finally {
                ConnUtil.closeConnection(conn);
            }
        }
        return id;
    }

    //@Scheduled(cron="1 1 0 15 8 ?"  )//每年8月15日

    /**
     * 学校升级和毕业的定时任务
     *
     * 1.学校升级的年级即除了毕业年级以外的所有年级，
     * 2.只有学校在开通后才可升级，默认开通后的升级日期是8月15号
     * 3.如果学校的开通后在8月15号之后，则不做升级和毕业处理，本年度升过级后则当前年不再做升级处理
     */
    //TODO  确认学校升级 和毕业的逻辑（ ）
    //0 0 12 * * ? 每天12点触发
    //@Scheduled(cron="10 * * * * ?"  )//每年8月15日
    public void schoolClassesUpScheduled(){
        List< TbSchoolUp > tbSchoolUpList = schoolManageDao.findAllSchoolUp( CommonInstence.STATUS_0 );
        if( null != tbSchoolUpList ){
            for( int i = 0 ; i < tbSchoolUpList.size();i++ ){
                //处理升级和毕业的班级
                upAndGradeClasses( tbSchoolUpList.get( i )  );
            }
        }
    }

    /**
     *
     * @param   ：需要毕业的年级level，（ 排除毕业年级后剩下的年级都升级 ）
     */
    public void upAndGradeClasses( TbSchoolUp tbSchoolUp  ){
        Integer schoolId = tbSchoolUp.getSchoolId();
        //当天日期
        String nowDay = DateFormatUtils.getDay(new Date());
        //当前年
        int nowYear = DateFormatUtils.getYear( new Date( ));
        Integer upYear = tbSchoolUp.getUpYear();
        Integer gradeYear = tbSchoolUp.getGraduateYear();
        //当前学校升级日期时间点
        String upDay = DateFormatUtils.getDay( tbSchoolUp.getAutoUpTime() );
        //当前学校毕业日期时间点
        String gradeDay = DateFormatUtils.getDay( tbSchoolUp.getAutoGraduateTime() );
        String gradeClassesLevelStr = tbSchoolUp.getClassGraduateLevel();
        //查询除毕业年级以外的所有需要升级的班级列表
        if( null != gradeClassesLevelStr ){
            String[] gradeClassesArray = gradeClassesLevelStr.split( "," );
            if( null != gradeClassesArray ){
                List< Integer > classesLevelList = new ArrayList<Integer>();
                //获取所有毕业的年级
                for( int i = 0 ; i < gradeClassesArray.length; i++ ){
                    if(StringUtils.isNumeric( gradeClassesArray[ i ])){
                        classesLevelList.add( new Integer( gradeClassesArray[ i ] ));
                    }
                }
                if( classesLevelList.size() > 0 ){
                    Connection conn = null;
                    try {
                        conn = ConnUtil.getTransactionConnection();
                        //为避免数据库事务异常更新过程中只有一半更新则年级的升级和毕业的时间都分开判断
                        //如果没有升过级，或者升级的年份小于当前年份并且正好是设置的升级日期
                        if( ( null == gradeYear || ( gradeYear != null && gradeYear < nowYear ) ) && nowDay.equals( gradeDay ) ){
                            //查询所有需要毕业的班级
                            List< Classes > gradeClassesList = schoolManageDao.findGradeClassesByLevel( schoolId ,classesLevelList ,CommonInstence.STATUS_0 );
                            if( null != gradeClassesList && gradeClassesList.size() > 0 ){
                                //批量毕业并且更新学校升级表毕业年份
                                updateClassesLevelForGrade( gradeClassesList , schoolId ,nowYear ,tbSchoolUp.getId() );
                            }
                        }
                        if( ( null == upYear || ( upYear != null && upYear < nowYear ) ) && nowDay.equals( upDay ) ){
                            //查询所有需要升级的班级
                            List<Classes> upClassesList = schoolManageDao.findExceptGradeClassesByLevel( schoolId, classesLevelList ,CommonInstence.STATUS_0 );
                            if( null != upClassesList && upClassesList.size() > 0 ){
                                //批量升级并且更新班级升级年份
                                updateClassesLevelForUp( upClassesList ,schoolId );
                            }
                        }

                        conn.commit();
                    } catch (SQLException e) {
                        log.error( "批量升级班级异常 ，"  + e.getMessage()  , e );
                        ConnUtil.rollbackConnection(conn);
                        throw new BizException( "批量升级班级异常" );
                    } catch (Exception e) {
                        log.error( "批量升级班级异常，"  + e.getMessage() ,e );
                        throw new BizException( "批量升级班级异常" );
                    } finally {
                        ConnUtil.closeConnection(conn);
                    }
                }
            }
        }
    }
    /**
     * 处理所有班级升级
     * @param classesList
     */
    public void updateClassesLevelForUp(List<Classes> classesList ,Integer schoolId  ){
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
                    String classesName = CommonEnum.getClassesNameByLevelEnum.getSource( level + 1 ).getValue();
                    String fullName = classesName + classes.getClassName();
                    int levelUp = level + 1;
                    //更新班级名称
                    log.info( "未毕业的班级id:" + classesId +"升级后level:"+levelUp );
                    int num = schedulerClassesUpDao.updateClassesUpInfo( conn , classesId , fullName , levelUp );
                    if( num == 0 ){
                       // throw new BizException( "批量升级班级失败" );
                        log.info("批量升级班级失败，schoolId:" + schoolId);
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
                                    log.info("批量升级班级的小组失败，schoolId:" + schoolId );
                                   // throw new BizException( "批量升级班级的小组失败" );
                                }
                            }
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                log.error( "批量升级班级异常 ，schoolId:" + schoolId + e.getMessage()  , e );
                ConnUtil.rollbackConnection(conn);
                throw new BizException( "批量升级班级异常" );
            } catch (Exception e) {
                log.error( "批量升级班级异常，schoolId:" + schoolId  + e.getMessage() ,e );
                throw new BizException( "批量升级班级异常" );
            } finally {
                ConnUtil.closeConnection(conn);
            }
        }
    }

    /**
     * 将某学校的年级列表全部毕业
     * @param classesList
     * @param schoolId
     */
    public void updateClassesLevelForGrade( List<Classes> classesList , Integer schoolId ,int nowYear ,int schoolUpId){
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            for( int  i = 0 ; i < classesList.size();i++ ) {
                Integer classesId = classesList.get( i ).getId();
                Integer level = classesList.get( i ).getLevel();
                //处于关闭状态的班级不能毕业（ classes的status=0 ）
                if( null != level ){
                    schedulerClassesUpDao.updateClassesGraduateStatus(conn, CommonInstence.STATUS_1, classesId, level + 1);
                }
            }
            //更新班级升级和毕业年份
            schoolManageDao.updateSchoolUpGradeYearBySchoolId( schoolId ,nowYear ,schoolUpId );
            conn.commit();
        } catch (SQLException e) {
            log.error( "批量毕业班级异常 ，schoolId:" + schoolId + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new BizException( "批量毕业班级异常" );
        } catch (Exception e) {
            log.error( "批量毕业班级异常，schoolId:" + schoolId  + e.getMessage() ,e );
            throw new BizException( "批量毕业班级异常" );
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }
    /**
     * 财务确认后生成学校升级的默认升级数据（对外接口）
     * @return
     */
    public int addSchoolUpForFinanceConfirm(Integer schoolId){
        TbSchoolUp tbSchoolUp = schoolManageDao.findSchoolUpBySchoolId( schoolId );
        if( null == tbSchoolUp ){
            tbSchoolUp = new TbSchoolUp();
            int nowYear = DateFormatUtils.getYear( new Date( ));
            //默认8月15号升级
            tbSchoolUp.setAutoUpTime( DateFormatUtils.dateStrDayFormat(nowYear+"-08-15"));
            tbSchoolUp.setCreateTime( new Date());
            tbSchoolUp.setAutoGraduateTime( DateFormatUtils.dateStrDayFormat(nowYear+"-08-15"));
            tbSchoolUp.setSchoolId( schoolId );
            tbSchoolUp.setVersion( 0 );
            //查询学校的所有最高学段的最高年级
            School school = schoolManageDao.findSchoolById( schoolId ,CommonInstence.STATUS_0 );
            if( null != school ){
                //所有学段
                String gradeId = school.getGradeId();
                if( null != gradeId ){
                    StringBuffer maxLevelSb = new StringBuffer();
                    String[] gradeIdArray = gradeId.split( "," );
                    if( null != gradeIdArray ){
                        for( int i = 0 ; i < gradeIdArray.length;i++ ){
                            String allLevelStr = CommonEnum.getClassesLevelsEnum.getSource( new Integer( gradeIdArray[ i ]) ).getValue();
                            String[] levelArray = allLevelStr.split( "," );
                            if( null != levelArray ){
                                //取最高年级level
                                String maxLevel = levelArray[ levelArray.length - 1 ];
                                String[] lastLevel = maxLevel.split( ":" );
                                maxLevelSb.append( lastLevel[ 0 ] );
                                if( i < gradeIdArray.length - 1 ){
                                    maxLevelSb.append( "," );
                                }
                            }
                        }
                    }
                    tbSchoolUp.setClassGraduateLevel( maxLevelSb.toString() );
                }
            }
            return addSchoolUp( tbSchoolUp );
        }
        return CommonInstence.CODE_0;
    }

    /**
     * 创建学校升级信息
     * @param tbSchoolUp
     * @return
     */
    public int addSchoolUp( TbSchoolUp tbSchoolUp ){
        Connection conn = null;
        int id = 0;
        try {
            conn = ConnUtil.getTransactionConnection();
            id = schoolManageDao.addSchoolUp( conn , tbSchoolUp );
            conn.commit();
        } catch (SQLException e) {
            log.error( "创建学校升级信息异常 ，schoolId:" + tbSchoolUp.getSchoolId() + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new BizException( "创建学校升级信息异常" );
        } catch (Exception e) {
            log.error( "创建学校升级信息异常，schoolId:" + tbSchoolUp.getSchoolId()  + e.getMessage() ,e );
            throw new BizException( "创建学校升级信息异常" );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return id;
    }




}
