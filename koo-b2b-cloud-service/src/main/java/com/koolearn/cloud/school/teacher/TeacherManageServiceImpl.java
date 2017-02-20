package com.koolearn.cloud.school.teacher;

import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.Common.dao.SchoolCommonDao;
import com.koolearn.cloud.school.CommonConstant;
import com.koolearn.cloud.school.dto.TeacherPageDto;
import com.koolearn.cloud.school.homePage.dao.HomePageDao;
import com.koolearn.cloud.school.schoolmanage.dao.SchoolManageDao;
import com.koolearn.cloud.school.teacher.dao.TeacherManageDao;
import com.koolearn.cloud.school.teacher.vo.TeacherPageResultDto;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddStudentDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.sso.dto.RegistDTO;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.sso.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by fn on 2016/11/3.
 */
@Service("teacherManageService")
public class TeacherManageServiceImpl implements TeacherManageService {
    Logger log = Logger.getLogger( this.getClass() );

    @Autowired
    private SchoolManageDao schoolManageDao;
    @Autowired
    private TeacherDataMaintainDao teacherDataMaintainDao;
    @Autowired
    private IOpenService iopenService;
    @Autowired
    private TeacherManageDao teacherManageDao;
    @Autowired
    private SchoolCommonDao schoolCommonDao;
    @Autowired
    private TeacherAddStudentDao teacherAddStudentDao;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private HomePageDao homePageDao;

    public SchoolManageDao getSchoolManageDao() {
        return schoolManageDao;
    }

    public void setSchoolManageDao(SchoolManageDao schoolManageDao) {
        this.schoolManageDao = schoolManageDao;
    }

    public HomePageDao getHomePageDao() {
        return homePageDao;
    }
    public void setHomePageDao(HomePageDao homePageDao) {
        this.homePageDao = homePageDao;
    }

    public IUserService getiUserService() {
        return iUserService;
    }

    public void setiUserService(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    public TeacherAddStudentDao getTeacherAddStudentDao() {
        return teacherAddStudentDao;
    }

    public void setTeacherAddStudentDao(TeacherAddStudentDao teacherAddStudentDao) {
        this.teacherAddStudentDao = teacherAddStudentDao;
    }

    public IOpenService getIopenService() {
        return iopenService;
    }

    public void setIopenService(IOpenService iopenService) {
        this.iopenService = iopenService;
    }

    public TeacherManageDao getTeacherManageDao() {
        return teacherManageDao;
    }

    public void setTeacherManageDao(TeacherManageDao teacherManageDao) {
        this.teacherManageDao = teacherManageDao;
    }

    public SchoolCommonDao getSchoolCommonDao() {
        return schoolCommonDao;
    }

    public void setSchoolCommonDao(SchoolCommonDao schoolCommonDao) {
        this.schoolCommonDao = schoolCommonDao;
    }

    /**
     * 教师分页查询
     * @param teacherPageDto
     * @return
     */
    public Map<String,Object> findClassPage(TeacherPageDto teacherPageDto){
        Map< String ,Object > map = new HashMap<String, Object>( );
        Integer totalLine = teacherManageDao.findTeacherPageLine( teacherPageDto );
        List<TeacherPageResultDto> teacherPageResultDtoList = null;
        teacherPageDto.setTotalLine( totalLine );
        if( totalLine > 0 ){
            teacherPageResultDtoList = teacherManageDao.findTeacherPageList( teacherPageDto );
            if( null != teacherPageResultDtoList && teacherPageResultDtoList.size() > 0 ){
                for( int i = 0 ; i < teacherPageResultDtoList.size();i++ ){
                    List< TeacherPageResultDto > subjectAndRangeNameList = teacherManageDao.findTeacherSubjectAndRange( teacherPageResultDtoList.get( i ).getUserId());
                    if( null != subjectAndRangeNameList && subjectAndRangeNameList.size() > 0 ){
                        StringBuffer subjectName = new StringBuffer();
                        StringBuffer rangeName = new StringBuffer();
                        Set< String > rangeNameSet = new HashSet<String >();
                        for( int j = 0 ;j < subjectAndRangeNameList.size();j++ ){
                            subjectName.append(StringUtils.isNotEmpty( subjectAndRangeNameList.get( j ).getSubjectNameStr() ) ?subjectAndRangeNameList.get( j ).getSubjectNameStr():""  );
                            rangeNameSet.add( subjectAndRangeNameList.get( j ).getRangeNameStr() );
                            if( j < subjectAndRangeNameList.size() - 1 && StringUtils.isNotEmpty( subjectAndRangeNameList.get( j ).getSubjectNameStr() )){
                                subjectName.append( "," );
                            }
                        }
                        Iterator<String > rangeNameItertor = rangeNameSet.iterator();
                        while ( rangeNameItertor.hasNext() ){
                            rangeName.append( rangeNameItertor.next() );
                            rangeName.append( "," );
                        }
                        teacherPageResultDtoList.get( i ).setSubjectNameStr( subjectName.toString() );
                        teacherPageResultDtoList.get( i ).setRangeNameStr( rangeName.toString().substring( 0 ,rangeName.length() -1 ) );
                    }
                }
            }
        }
        map.put( "dataList" , teacherPageResultDtoList!=null ?teacherPageResultDtoList:"{}" );
        map.put( "totalPage" , teacherPageDto.getTotalPage() );
        map.put( "currentPage" , teacherPageDto.getCurrentPage() );
        return map;
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    public boolean isExistMail(String email ){
        UsersDTO usersDTO = iUserService.getUserByEmail(email);
        if( null == usersDTO ){
            int num = teacherManageDao.findUserEmail( email );
            if( num > 0 ){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    /**
     * 判断电话是否存在
     * @param mobile
     * @return
     */
    public boolean isExistMobile(String mobile){
        UsersDTO usersDTO = iUserService.getUserByMobile( mobile );
        if( null == usersDTO ){
            int num = teacherManageDao.findUserMobile(mobile);
            if( num > 0 ){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }
    /**
     * 批量添加excel教师
     * @param userList
     * @return
     */
    public List<User> insertTeacherExcelToDb(List<User> userList ,Integer schoolId) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            School school = schoolManageDao.findSchoolById( schoolId ,CommonInstence.STATUS_0 );

            for (int i = 0; i < userList.size(); i++) {
                userList.get( i ).setSchoolId(schoolId);
                userList.get( i ).setProcess(CommonInstence.PROCESS_1);
                userList.get( i ).setCreateTime(new Date());
                userList.get( i ).setStatus(CommonInstence.STATUS_0);
                userList.get( i ).setType(CommonInstence.TEACHER_TYPE_1);
                userList.get( i ).setSource( CommonInstence.SOURCE_20 );//数据来源
                int id = teacherAddStudentDao.insertUser( conn , userList.get( i ) );
                userList.get( i ).setId( id );
                //入库book_version
                String gradeIds = school.getGradeId();
                if( null != gradeIds ){
                    String[] gradeIdArray = gradeIds.split( "," );
                    if( null != gradeIdArray ){
                        for( int j = 0 ; j < gradeIdArray.length;j++ ){
                            TeacherBookVersion teacherBookVersion = new TeacherBookVersion();
                            teacherBookVersion.setTeacherId( id );
                            teacherBookVersion.setRangeName(CommonEnum.getRangeNameEnum.getSource( new Integer( gradeIdArray[ j ] )).getValue() );
                            teacherBookVersion.setCreateTime( new Date());
                            teacherBookVersion.setStatus( CommonInstence.STATUS_1 );
                            teacherDataMaintainDao.insertTeacherBookVersion( conn ,teacherBookVersion );
                        }
                    }
                }
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( "批量添加教师excel信息异常"  + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "批量添加教师excel信息异常" + e.getMessage() , e );
        } catch (Exception e) {
            log.error( "批量添加教师excel信息异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new Exception( "批量添加教师excel信息异常" + e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return userList;
    }

    /**
     * 添加教师信息
     * @param user
     * @param schoolId
     * @return
     */
    public int addTeacherBaseInfo( User user , Integer schoolId) throws Exception {
        Connection conn = null;
        int num = 0 ;
        try {
            List< User > listUser = new ArrayList<User>();
            listUser.add( user );
            makeTeacherUserName( listUser , schoolId );
            conn = ConnUtil.getTransactionConnection();
            RegistDTO registDTO = new RegistDTO();
            if( listUser.get( 0 ) != null &&listUser.get( 0 ).getUserName() != null ){
                registDTO.setUserName(  listUser.get( 0 ).getUserName() );
            }else{
                return 0;
            }
            registDTO.setPassword( CommonInstence.RESET_PASSWORD);
            registDTO.setRole( 1 );
            registDTO.setChannel( CommonInstence.USER_CHANNEL );//云平台的渠道
            registDTO.setMobile( user.getMobile());
            //TODO sso 异常
            int ssoId = iopenService.registerUser(registDTO);

            user.setSchoolId(schoolId);
            user.setProcess(CommonInstence.PROCESS_1);
            user.setCreateTime(new Date());
            user.setStatus(CommonInstence.STATUS_0);
            user.setType(CommonInstence.TEACHER_TYPE_1);
            user.setUserId(ssoId);
            num = teacherAddStudentDao.insertUser( conn , user );
            conn.commit();
        }catch (SQLException e) {
            log.error( "添加教师信息异常"  + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "添加教师信息异常" + e.getMessage() , e );
        } catch (Exception e) {
            log.error( "添加教师信息异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new Exception( "添加教师信息异常" + e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 修改教师信息
     */
    public void updateTeacherBaseInfo(User user ) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            teacherManageDao.updateTeacherBaseInfo( conn , user.getId() , user.getEmail() ,user.getMobile() ,user.getRealName() ,user.getUpdater() );
            conn.commit();
        }catch (SQLException e) {
            log.error( "修改教师信息异常"  + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "修改教师信息异常" + e.getMessage() , e );
        } catch (Exception e) {
            log.error( "修改教师信息异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "修改教师信息异常" + e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }
    /**
     * 修改教师或学生状态
     * @param userId
     * @param status
     * @param managerName
     */
    public void updateTeacherOrStudentStatus(Integer userId, Integer status, String managerName) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            teacherManageDao.updateTeacherStatus(conn,userId ,status ,managerName );
            conn.commit();
        }catch (SQLException e) {
            log.error( "修改教师信息异常"  + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "修改教师信息异常" + userId +"," + e.getMessage() , e );
        } catch (Exception e) {
            log.error( "修改教师信息异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "修改教师信息异常" + userId +","+ e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
    * 查询教师学段学科及电话信息
    * @param userId
    * @return
    */
    public TeacherPageResultDto findTeacherInfoForUpdate(Integer userId){
        TeacherPageResultDto teacherPageResultDto = teacherManageDao.findTeacherInfoForUpdate( userId );
        StringBuffer sb = new StringBuffer();
        if( null != teacherPageResultDto ){
            List< TeacherPageResultDto > subjectAndRangeNameList = teacherManageDao.findTeacherSubjectAndRange( userId );
            if( null != subjectAndRangeNameList && subjectAndRangeNameList.size() > 0 ){
                Map< String , String > map = new HashMap<String, String>( );
                for( int j = 0 ;j < subjectAndRangeNameList.size();j++ ){
                    String subjectName = ( String )map.get( subjectAndRangeNameList.get( j ).getRangeNameStr() );
                    if( StringUtils.isEmpty( subjectName ) ){
                        map.put( subjectAndRangeNameList.get( j ).getRangeNameStr() , StringUtils.isNotEmpty( subjectAndRangeNameList.get( j ).getSubjectNameStr())?
                                subjectAndRangeNameList.get( j ).getSubjectNameStr():"");
                    }else{
                        map.put( subjectAndRangeNameList.get( j ).getRangeNameStr() ,subjectName + "," +  ( StringUtils.isNotEmpty( subjectAndRangeNameList.get( j ).getSubjectNameStr())?
                                subjectAndRangeNameList.get( j ).getSubjectNameStr():"" ));
                    }
                }
                for(Map.Entry<String, String> entry:map.entrySet()){
                    sb.append( entry.getKey() + ":" + entry.getValue() );
                    sb.append( "  " );
                }

            }
        }
        teacherPageResultDto.setSubjectNameStr( sb.toString() );
        return teacherPageResultDto;
    }

    /**
     * 生成教师账号
     */
    public List< User> makeTeacherUserName(List<User> userList, Integer schoolId ){
        String schoolCode = teacherManageDao.findSchoolCode( schoolId );
        if( null != schoolCode ){
            try {
                checkStudentNameAndInsert( userList , schoolCode , schoolId );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userList;
    }
    public List<User> checkStudentNameAndInsert(List< User > list, String schoolCode ,Integer schoolId ) throws Exception {
        //先拆分学生的名字和学号，然后调用SSO接口验证是否重复
        try{
            Integer teacherNum = homePageDao.findTeacherNumBySchoolId( schoolId , CommonConstant.TYPE_1 ,CommonConstant.STATUS_0);
            //剩余可新增的学生人数
            int lastTeacherNum = 5000 - teacherNum;
            if( 0 < lastTeacherNum && list.size() > lastTeacherNum ){
                list = list.subList( 0 , lastTeacherNum );
            }else if( lastTeacherNum == 0 ){
                return null;
            }
            int classUserNum = list.size();//当前导入教师的总人数
            Set< String > noSet = new HashSet<String>( classUserNum );//初始化大小
            //生成唯一账号并判断唯一性
            noSet =  makeUserNameAndIsOnly( classUserNum , noSet ,schoolCode );
            //验证SSO系统是否有重复账号，并且重新生成账号
            noSet = checkSSOUserName( noSet , classUserNum , schoolCode );
            //最后组装user对象
            if( noSet.size() == classUserNum ){
                Iterator<String> it = noSet.iterator();
                List< String > lastOnlyList = new LinkedList<String>();
                while (it.hasNext() ){
                    lastOnlyList.add( it.next() );
                }
                for( int i = 0 ;i < list.size() ;i++ ){
                    list.get( i ).setUserName( lastOnlyList.get( i ));
                }
            }
        }catch( Exception e ){
            e.printStackTrace();
            throw new Exception( "批量添加教师验证异常" );
        }
        return list;
    }
    /**
     * 验证SSO系统是否有重复账号
     * @param noSet
     * @return
     */
    public Set< String > checkSSOUserName( Set< String > noSet ,int classUserNum ,String classNo ){
        List< String > list = new LinkedList<String>();
        Iterator<String> it = noSet.iterator();
        while( it.hasNext() ) {
            String itNext = it.next();
            //TODO sso
            UsersDTO user = iopenService.getUserByName(itNext);
            if (null != user) {
                list.add(itNext);
            }
        }
        if( list.size() > 0 ){
            for( int i = 0 ;i < list.size() ;i++ ){
                noSet.remove( list.get( i ));
            }
            noSet = makeUserNameAndIsOnly(classUserNum , noSet ,classNo );
            //递归重新生成
            noSet = checkSSOUserName( noSet ,classUserNum ,  classNo);
        }
        return noSet;
    }


    private  Set< String > makeUserNameAndIsOnly(int classUserNum , Set< String > noSet ,String classNo ){
        //随机生成账号
        noSet = randomDigit( noSet , classNo , classUserNum );
        //判断唯一性
        List< String > userDoubleList = isOnlyOne( noSet );
        if( userDoubleList.size() > 0  ){
            for( int i = 0 ;i < userDoubleList.size() ;i++ ){
                noSet.remove( userDoubleList.get( i ));
            }
        }
        //如果当前班级里有重复账号则重新生成
        if( noSet.size() < classUserNum ){
            //递归重新生成
            makeUserNameAndIsOnly( classUserNum , noSet , classNo);
        }
        return noSet;
    }
    /**
     * 判断账号唯一性
     * @param noSet
     * @return
     */
    private List< String > isOnlyOne(Set< String > noSet ){
        List< String > userDoubleList = new LinkedList<String >();//记录重复的账号
        List<String > userNameList = findUserInfoCacheAndDB();
        Iterator<String> it = noSet.iterator();
        while( it.hasNext() ){
            String userName = it.next();
            if( userNameList.contains( userName )){
                userDoubleList.add( userName );
            }
        }
        return userDoubleList;
    }
    public List<String > findUserInfoCacheAndDB( ){
        return teacherAddStudentDao.findUserNameInUser();
    }
    /**
     * 随机生成当前班级与学生人数对应的唯一编码
     * @param classNo
     * @return
     */
    private static Set< String > randomDigit( Set< String > noSet , String classNo , int size ){
        String random=(int)(Math.random()*10000)+"";
        int length = random.length();
        if( length < 4 ){
            for( int i = 0 ;i < ( 4 - length );i++ ){
                random = "0"+random;
            }
        }
        noSet.add(classNo + random );
        if( noSet.size() < size ){
            //递归重新生成
            randomDigit( noSet , classNo ,size  );
        }
        return noSet;
    }







}
