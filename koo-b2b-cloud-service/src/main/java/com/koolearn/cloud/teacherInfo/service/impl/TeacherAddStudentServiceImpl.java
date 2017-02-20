package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.queue.MessageContent;
import com.koolearn.cloud.queue.ProducerUserServiceImpl;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherAddStudentService;
import com.koolearn.cloud.teacherInfo.dao.ClassHomePageDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddStudentDao;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.library.maintain.remote.service.InterfaceCloudService;
import com.koolearn.sso.dto.RegistDTO;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.sso.service.IOpenService;

import com.koolearn.util.SystemGlobals;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fn on 2016/4/6.
 */
@Service("teacherAddStudentService")
public class TeacherAddStudentServiceImpl implements TeacherAddStudentService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddStudentDao teacherAddStudentDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private ClassHomePageDao classHomePageDao;
    @Autowired
    private IOpenService iopenService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private InterfaceCloudService interfaceCloudService;

    private ProducerUserServiceImpl producerUserService;

    public ProducerUserServiceImpl getProducerUserService() {
        return producerUserService;
    }

    public void setProducerUserService(ProducerUserServiceImpl producerUserService) {
        this.producerUserService = producerUserService;
    }

    public InterfaceCloudService getInterfaceCloudService() {
        return interfaceCloudService;
    }

    public void setInterfaceCloudService(InterfaceCloudService interfaceCloudService) {
        this.interfaceCloudService = interfaceCloudService;
    }

    public ClassHomePageDao getClassHomePageDao() {
        return classHomePageDao;
    }

    public void setClassHomePageDao(ClassHomePageDao classHomePageDao) {
        this.classHomePageDao = classHomePageDao;
    }

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }

    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public IOpenService getIopenService() {
        return iopenService;
    }

    public void setIopenService(IOpenService iopenService) {
        this.iopenService = iopenService;
    }

    public TeacherAddStudentDao getTeacherAddStudentDao() {
        return teacherAddStudentDao;
    }

    public void setTeacherAddStudentDao(TeacherAddStudentDao teacherAddStudentDao) {
        this.teacherAddStudentDao = teacherAddStudentDao;
    }

    //存入队列
    public void setStudentInfoQueue( String studentStr , String classNo ,int classesId ,UserEntity userEntity){
        MessageContent messageContent = new MessageContent();
        messageContent.setClassesId( classesId );
        messageContent.setStudentStr(studentStr);
        messageContent.setClassNo( classNo );
        messageContent.setUserEntity( userEntity );
        //消息队列
        try {
            producerUserService.send( messageContent );
        } catch (Exception e) {
            log.equals( "批量添加学生存消息队列异常 , 班级编码：" + classNo );
            e.printStackTrace();
        }
    }
    /**
     * 验证学号，( 班级编码+ 三位随机数 向SSO接口确认是否重复可用 )
     * @param studentStr
     * @return
     */
    @Override
    public List<User> checkStudentNameAndInsert(String studentStr , String classNo) throws Exception {
        //先拆分学生的名字和学号，然后调用SSO接口验证是否重复
        List< User > list =null;
        try{
            list = makeUser( studentStr );
            //查询当前班级人数
            Integer classesId = teacherAddStudentDao.findClassesIdByClassCode( classNo );
            int classesStudensNum = 0 ;
            if( classesId != null ){
                //查询当前编号已使用多少，其他后加入班级的学生不计算数量
                classesStudensNum = classHomePageDao.findClassesNumByClassCode( classesId ,classNo ,CommonInstence.STATUS_0 );
            }
            //剩余可新增的学生人数
            int lastStudentNum = 1000 - classesStudensNum;
            if( 0 < lastStudentNum && list.size() > lastStudentNum ){
                list = list.subList( 0 , lastStudentNum );
            }else if( lastStudentNum == 0 ){
                return null;
            }
            int classUserNum = list.size();//当前导入学生的总人数
            Set< String > noSet = new HashSet<String>( classUserNum );//初始化大小
            //生成唯一账号并判断唯一性
            noSet =  makeUserNameAndIsOnly(classUserNum , noSet ,classNo );
            //验证SSO系统是否有重复账号，并且重新生成账号
            noSet = checkSSOUserName( noSet , classUserNum , classNo );
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
            throw new Exception( "批量添加学生验证异常" );
        }
        return list;
    }

    /**
     * 在SSO和本地库插入学生信息
     * @param userList
     * @param classesId
     * @param userEntity
     * @return
     */
    @Override
    public List<User> insertUserAndStudent(List<User> userList ,int classesId ,UserEntity userEntity) throws Exception {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            for( int i =0;i<userList.size();i++ ){
                //学生信息同时加入到SSO系统
                RegistDTO registDTO = new RegistDTO();
                if( userList.get( i ).getUserName().length() > 50 ){
                    throw new BizException( "学生姓名长度大于50" );
                }
                registDTO.setUserName(userList.get( i ).getUserName() );
                registDTO.setPassword( CommonInstence.RESET_PASSWORD);
                registDTO.setRole( 2 );
                registDTO.setChannel( CommonInstence.USER_CHANNEL );//云平台的渠道
                //TODO sso
                int ssoId = iopenService.registerUser(registDTO);
                log.info( "批量添加学生接口返回值:" + ssoId );
                //入库user表
                ClassesStudent classesStudent = new ClassesStudent();
                User user = userList.get( i );
                user.setType( CommonInstence.STUDENT_TYPE );//学生
                user.setUserId(ssoId);
                user.setRealName(userList.get(i).getRealName());
                user.setStudentCode(userList.get(i).getStudentCode());
                user.setStatus(CommonInstence.STATUS_0);
                user.setSchoolId(userEntity.getSchoolId());
                user.setSchoolName(userEntity.getSchoolName());
                user.setProvinceId(userEntity.getProvinceId());
                user.setCityId(userEntity.getCityId());
                user.setCityName(userEntity.getCityName());
                user.setProcess(CommonInstence.STUDENT_PROCESS_2);
                user.setCreateTime( new Date());
                Integer id = 0;
                try{
                    id = teacherAddStudentDao.insertUser( conn , user );
                }catch ( Exception e ){log.error( "批量添加学生入库异常," + ssoId);}
                try{
                    //log.info( "图书馆用户:" + PropertiesConfigUtils.getProperty("libraryId") );
                    Integer libraryId = Integer.valueOf( SystemGlobals.getPreference("libraryId") );
                    boolean result = interfaceCloudService.saveLibUser( id , libraryId );
                    log.info( "批量插入数字图书馆用户结果:" + result + ","+libraryId );
                }catch ( Exception e ){log.error( "批量添加学生入库插入数字图书馆系统异常，用户主键:" + id );}
                user.setId( id );
                //TODO set 方法使用
                userList.set( i ,user );
                //拼装班级学生对象
                classesStudent.setClassesId( classesId );
                classesStudent.setStudentId(id);
                classesStudent.setStatus(CommonInstence.STATUS_0);
                classesStudent.setCreateTime( new Date());
                Integer classesStudentId = teacherAddStudentDao.insertClassesStudent( conn,classesStudent );
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "批量添加学生信息异常" + userEntity+ "," + e.getMessage()  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "批量添加学生信息异常"+ userEntity+ "," + e.getMessage() , e );
        } catch (Exception e) {
            log.error( "批量添加学生信息异常"+ userEntity+ "," + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "批量添加学生信息异常"+ userEntity+ "," + e.getMessage() , e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return userList;
    }



    /**
     * 查询未分组的学生
     * @param classesId
     * @return
     */
    @Override
    public List<ClassesStudent> findNoClassesTeamStudents(Integer classesId ,Integer subjectId  ,Integer teacherId) {
        //先查询班级下的所有学生
        List< ClassesStudent > classesStudentList = teacherAddClassDao.findClassesStudentByClassesId( classesId   );
        //用班级的学段id查询，
        Classes classes = teacherAddClassService.findClassesById( new Integer( classesId ));
        List< ClassesStudent > lastList = new ArrayList<ClassesStudent>();
        if( null != classesStudentList ){
            for( int j = 0 ; j < classesStudentList.size();j++ ){
                //查询学生姓名
                String studentName = teacherAddClassDao.findUserNameByUserId( classesStudentList.get( j ).getStudentId() );
                classesStudentList.get( j ).setStudentName( studentName );
            }
            //再查询已分配的所有学生的小组主键集合
            List< Integer > teamIdList = teacherAddClassDao.findHaveTeamClassesIdByClassesId( classesId ,subjectId ,classes.getRangeId() ,teacherId );
            //查询已分配的所有学生
            List< ClassesStudent > addTeamStudentList = new ArrayList<ClassesStudent>();
            if( null != teamIdList && teamIdList.size() > 0 ){
                for( int i = 0 ;i < teamIdList.size();i++ ){
                    List< ClassesStudent > haveTeamClassesList = teacherAddClassDao.findClassesStudentByClassesId( teamIdList.get( i )  );
                    addTeamStudentList.addAll( haveTeamClassesList );
                }
            }

            for( int i = 0 ;i < classesStudentList.size();i++ ){
                int num = 0 ;
                for( int j =0 ;j < addTeamStudentList.size();j++){
                    if( classesStudentList.get( i ).getStudentId().intValue() == addTeamStudentList.get( j ).getStudentId().intValue() ){
                        //classesStudentList.remove( classesStudentList.get( i ) );
                        num++;
                    }
                }
                if(num == 0 ){
                    lastList.add( classesStudentList.get( i ) );
                }
            }
        }
        return lastList;
    }

    /**
     * 将学生分组入库
     * @param classesId
     * @param studentIds
     * @return
     */
    @Override
    public List< ClassesStudent > insertClassesStudents(Integer classesId, String studentIds) {
        Connection conn = null;
        Integer classesStudentId = 0;
        List< ClassesStudent > list = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            String[] studentArray = studentIds.split( ",");
            if( null != studentArray && studentArray.length > 0 ){
                list = new ArrayList<ClassesStudent>( studentArray.length );
                for( int i = 0 ;i < studentArray.length;i++ ){
                    ClassesStudent classesStudent = new ClassesStudent();
                    int studentId = new Integer( studentArray[ i ] );
                    classesStudent.setStudentId( studentId );
                    classesStudent.setClassesId( classesId );
                    classesStudent.setStatus( 0 );
                    classesStudent.setCreateTime( new Date( ));
                    //先判断当前的学生是否已被分配小组
                    List<ClassesStudent> hadClassesStudentList = teacherAddStudentDao.findClassesStudentByClassesIdAndStudentId( classesId ,studentId );
                    //如果未分配则入库
                    if( null == hadClassesStudentList || hadClassesStudentList.size() == 0 ){
                        classesStudentId = teacherAddStudentDao.insertClassesStudent( conn,classesStudent );
                        classesStudent.setId( classesStudentId );
                        String studentName = teacherAddClassDao.findUserNameByUserId( studentId );
                        classesStudent.setStudentName( studentName );
                        list.add( classesStudent );
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "将学生分组入库异常 "  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (BizException e) {
            log.error( "将学生分组入库异常" + e.getMessage() ,e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return list;
    }

    /**
     * 设置组长
     * @param classesStudentPrimary
     * @return
     */
    @Override
    public int updateStudentJob(Integer classesStudentPrimary ,Integer oldClassesStudentPrimary) throws Exception {
        Connection conn = null;
        Integer num = 0;
        try {
            conn = ConnUtil.getTransactionConnection();
            num = teacherAddStudentDao.updateStudentJob(conn, classesStudentPrimary, 1);
            teacherAddStudentDao.updateStudentJob(conn, oldClassesStudentPrimary, 0);
            conn.commit();
        } catch (SQLException e) {
            log.error( "设置组长异常 "  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (Exception e) {
            log.error( "设置组长异常" + e.getMessage() ,e);
            e.printStackTrace();
            throw new Exception( "设置组长异常" );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }



    /**
     * 删除学生
     * @param classesId
     * @param userId
     * @return
     */
    @Override
    public int deleteUserAndClassStudent(Integer classesId, Integer userId) {
        Connection conn = null;
        Integer num = 0;
        try {
            conn = ConnUtil.getTransactionConnection();
            //删除用户
           // num = teacherAddStudentDao.deleteUser( conn , userId ,1 );
            //删除班级下学生
            num = teacherAddStudentDao.deleteClassesStudent( conn , userId ,classesId ,1 );
            //删除小组小学生
            List<Classes>  classesList = teacherAddStudentDao.findClassesByParentId(  classesId ,CommonInstence.STATUS_0);
            if( null != classesList && classesList.size() > 0 ){
                for( int i = 0 ; i < classesList.size();i++ ){
                    teacherAddStudentDao.deleteClassesStudent(conn , userId ,classesList.get(i).getId() ,1);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "删除学生异常 "  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (BizException e) {
            log.error( "删除学生异常" + e.getMessage() ,e);
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }

    /**
     * 修改学生学号和姓名
     * @param userId
     * @param realName
     * @param studentCode
     * @return
     */
    @Override
    public int updateRealNameAndStudentCode(Integer userId, String realName, String studentCode) throws Exception {
        Connection conn = null;
        Integer num = 0;
        try {
            conn = ConnUtil.getTransactionConnection();
           // List<User> userList = teacherAddStudentDao.findUserByStudentCode( studentCode );
            num = teacherAddStudentDao.updateRealNameAndStudentCode( userId , realName , studentCode );
            conn.commit();
        } catch (SQLException e) {
            log.error( "删除学生异常"  , e );
            ConnUtil.rollbackConnection(conn);
            throw new Exception( "删除学生异常" + e.getMessage() ,e );
        } catch (Exception e) {
            log.error( "删除学生异常" + e.getMessage() ,e);
            throw new Exception( "删除学生异常" + e.getMessage() ,e );
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 查询当前班级所有学生
     * @param classesId
     * @return
     */
    @Override
    public List<User> findClassesStudentByClassesId(Integer teacherId , int classesId) {
        List< User > list =teacherAddStudentDao.findClassesStudentByClassesId( classesId );
        //查询该班级下的小组
        if( null != list ){
            //查询老师自己创建的小组
            List< Classes > classesList = teacherAddStudentDao.findTeacherTeamClassesByParentId( teacherId ,classesId ,CommonInstence.STATUS_0 );
            if( null != classesList && classesList.size() > 0 ){
                for( int i = 0 ; i < list.size();i++ ){
                    StringBuffer sb = new StringBuffer();
                    for( int j = 0 ; j < classesList.size();j++ ){
                        ClassesStudent student = teacherAddStudentDao.findStudentTeamExist( list.get( i ).getId() , classesList.get(j ).getId() ,CommonInstence.STATUS_0);
                        if( null != student ){
                            if( student.getHeadman() != null && student.getHeadman().intValue() == 1 ){
                                list.get( i ).setHeadman( 1 );
                            }
                        }
                    }
                }
            }
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

    public static void main( String[] args ){
        //isChineseWord( "地方1" );
        //isNotHasWord( "s23");
        TeacherAddStudentServiceImpl t = new TeacherAddStudentServiceImpl();
        Set< String > noSet = new HashSet<String>( 20 );
        noSet = t.makeUserNameAndIsOnly(20, noSet, "test");
        noSet = t.checkSSOUserName( noSet , 20 ,"test");
        Iterator<String> it = noSet.iterator();
        while( it.hasNext() ){
            System.out.println( it.next() );
        }
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
        String random=(int)(Math.random()*1000)+"";
        int length = random.length();
        if( length < 3 ){
            for( int i = 0 ;i < ( 3 - length );i++ ){
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
    /**
     *
     * @param studentStr : 前端输入的所有学生的名称和学号字符
     * @param      ： 班级编码
     * @return
     */
    private  List< User > makeUser( String studentStr  ){
        List< User > list = new ArrayList< User >();
        if( null != studentStr ){
            String[] studentArray = studentStr.split( "," );
            if( null != studentArray && studentArray.length > 0 ){
                for( int i = 0 ; i < studentArray.length;i++ ){
                    String[] userArray = studentArray[ i ].split( ";" );
                    if( null != userArray && userArray.length > 0 ){
                        User user = new User();
                        for( int j = 0 ; j < userArray.length;j++ ){
                            if( j == 0 && userArray[ j ] != null   ){
                                if( userArray[ j ].length() > 30 ){
                                    userArray[ j ]  = userArray[ j ].substring( 0 , CommonInstence.add_student_length );
                                }
                                user.setRealName( userArray[ j ] );//姓名
                            }else if( j == 1 && StringUtils.isNotBlank( userArray[ j ] ) ){
                                if( userArray[ j ].length() > 12 ){
                                    user.setStudentCode( userArray[ j ].substring( 0 , 11 ));
                                    //throw new BizException( "学号长度过长" );
                                }
                                 user.setStudentCode( userArray[ j ]);//学生学号
                            }
                        }
                        list.add( user );
                    }
                }
            }
        }
        return list;
    }


    /**
     * 判断字符串是否全部为汉字（不能包含字母和数字）
     * @param word
     * @return
     */
    private static boolean isChineseWord( String word ) {
        Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p_str.matcher(word);
        if (m.find() && m.group(0).equals(word)) {
            return true;
        }
        return false;
    }
    private static boolean isNotHasWord( String str ){
        for(int i=0 ; i<str.length() ; i++) {
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                return false;
            }
        }
        return true;
    }

    /**
     * 通过班级编码查询班级
     */
	@Override
	public Classes findClassesByCode(String classesCode) {
		return this.teacherAddClassDao.findClassesByCode(classesCode);
	}




}
