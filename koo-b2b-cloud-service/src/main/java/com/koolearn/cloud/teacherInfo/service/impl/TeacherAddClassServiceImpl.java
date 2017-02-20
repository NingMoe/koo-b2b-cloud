package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.common.serializer.RangeTypeEnum;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.ClassHomePageDao;
import com.koolearn.cloud.teacherInfo.dao.ClassNewStatusDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.util.*;
import com.koolearn.framework.redis.client.KooJedisClient;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by fn on 2016/4/5.
 */
@Service("teacherAddClassService")
public class TeacherAddClassServiceImpl implements TeacherAddClassService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private TeacherDataMaintainDao teacherDataMaintainDao;
    @Autowired
    private ClassHomePageDao classHomePageDao;
    @Autowired
    private ClassNewStatusDao classNewStatusDao;
    @Autowired
    private KooJedisClient kooJedisClient;

    public KooJedisClient getKooJedisClient() {
        return kooJedisClient;
    }

    public void setKooJedisClient(KooJedisClient kooJedisClient) {
        this.kooJedisClient = kooJedisClient;
    }

    public ClassNewStatusDao getClassNewStatusDao() {
        return classNewStatusDao;
    }

    public void setClassNewStatusDao(ClassNewStatusDao classNewStatusDao) {
        this.classNewStatusDao = classNewStatusDao;
    }


    public ClassHomePageDao getClassHomePageDao() {
        return classHomePageDao;
    }

    public void setClassHomePageDao(ClassHomePageDao classHomePageDao) {
        this.classHomePageDao = classHomePageDao;
    }

    public TeacherDataMaintainDao getTeacherDataMaintainDao() {
        return teacherDataMaintainDao;
    }

    public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }
    /**
     * 根据学段id和学科id查询对应的所有班级
     * @param rangeId
     * @param subjectId
     * @return
     */
    @Override
    public List<Classes> findAllClassByRangeSub(int schoolId , Integer rangeId, Integer subjectId ,int teacherId) {
        //List< Classes> list = teacherAddClassDao.findAllClassByRangeSub(rangeId ,subjectId ,CommonInstence.CLASSES_TYPE_3 );
        //根据学段id获取学段名称

        String rangeName = KlbTagsUtil.getInstance().getTagName( rangeId );
       // List< Classes> list = teacherAddClassDao.findAllClassByRangeSubType( schoolId , rangeName ,subjectId ,CommonInstence.CLASSES_TYPE_1 );
       // List< Classes> list2 = teacherAddClassDao.findAllClassByRangeSubType2( schoolId , rangeName ,CommonInstence.CLASSES_TYPE_0 );

        List< Classes> list =teacherAddClassDao.findAllClassByRangeSubTypeForSort( schoolId , rangeName ,subjectId );

        //判断班级是否可以删除
        if( null == list ){
            list = new LinkedList<Classes>();
        }
        for( int i = 0 ; i < list.size();i++ ){
            //班级人数
            int studentNum = classHomePageDao.findClassNumByClassId( list.get( i ).getId(),CommonInstence.STATUS_0  );
            int teacherId2 = list.get( i ).getTeacherId();
            User user =  null;
            if( teacherId2 > 0 ){
                user = teacherDataMaintainDao.findUserInfo( list.get( i ).getTeacherId() );
            }
            if( null != user ){
                list.get( i ).setTeacherName( user.getRealName() );
            }
            if( 0 == studentNum && teacherId  ==  ( list.get( i ).getTeacherId() != null?list.get( i ).getTeacherId().intValue():0 ) ){
                list.get( i ).setIsDeleteOk( CommonInstence.STUDENT_CAN_DELETE_1 );
            }else{
                list.get( i ).setIsDeleteOk( CommonInstence.STUDENT_CAN_DELETE_2 );
            }
            Classes classes = new Classes();
            Collections.sort(list , classes );
        }
        return list;
    }
    /**
     * 按入学年份和学科学段查询所有班级
     * @param year
     * @param rangeId
     * @param subjectId
     * @return
     */
    @Override
    public List<Classes> findAllClassesByYearAndSubRange(int schoolId , Integer year, Integer rangeId, Integer subjectId,int teacherId) {
        String rangeName = KlbTagsUtil.getInstance().getTagName( rangeId );
        List< Classes> list = null;
        List< Classes> list2 = null;
        if( StringUtils.isNotBlank( rangeName )){
            list = teacherAddClassDao.findAllClassesByYearAndRangeType(schoolId ,rangeName, subjectId, year);//查询学科班
            list2 = teacherAddClassDao.findAllClassesByYearAndRangeType2( schoolId ,rangeName , year);//查询行政班
        }
        //判断班级是否可以删除
        if( null != list ){
            list.addAll( list2 );
            for( int i = 0 ; i < list.size();i++ ){
                //班级人数
                int studentNum = classHomePageDao.findClassNumByClassId( list.get( i ).getId() ,CommonInstence.STATUS_0 );
                if( 0 == studentNum && teacherId == ( list.get( i ).getTeacherId() != null ?list.get( i ).getTeacherId().intValue():0 ) ){
                    list.get( i ).setIsDeleteOk( CommonInstence.STUDENT_CAN_DELETE_1 );
                }else{
                    list.get( i ).setIsDeleteOk( CommonInstence.STUDENT_CAN_DELETE_2 );
                }
                int teacherIdDb =list.get( i ).getTeacherId();
                if( 0 < teacherIdDb ){
                    User user =  teacherDataMaintainDao.findUserInfo( teacherIdDb  );
                    if( null != user ){
                        list.get( i ).setTeacherName( user.getRealName() );
                    }
                }
            }
        }
        return list;
    }
    /**
     * 查询所有学科
     * @return
     */
    @Override
    public List<Dictionary> findAllSubject(){
        return teacherDataMaintainDao.findAllSubject();
    }

    /**
     * 查询老师当前学段下的所有学科
     * @param teacherId
     * @param rangeName
     * @return
     */
    public List< TeacherBookVersion> findTeacherSubjects(int teacherId , String rangeName){
        List< TeacherBookVersion> list = teacherAddClassDao.findTeacherSubjects( teacherId , rangeName );
        return list;
    }

    /**
     * 每年班级统一升级
     * @return
     */
    @Override
    public int upgradeClasses( int schoolId , int status , int graduate ) {
        List< Classes > classesList = teacherAddClassDao.findAllClasses( schoolId , status ,graduate   );

        return 0;
    }

    private int analyClassFroGraduate( List< Classes > list ){
        int num = 0 ;
        for( Classes classes : list ){
            if( null != classes ){
                int year = classes.getYear();
                String rangeName = classes.getRangeName();

            }
        }

        return num;
    }



    /**
     * 每年根据具体学校为班级统一升级
     * @return
     */
    @Override
    public int upgradeClassesBySchoolId( int status , int graduate ,int schoolId ,int outTime ) {
        //TODO 方法作废
        List< Classes > classesList = teacherAddClassDao.findAllClassesBySchoolId( status ,graduate ,schoolId ,outTime);
        return 0;
    }
    /**
     * 老师加入班级产生的动态
     * @param classesDynamic
     */
    @Override
    public int insertClassesDynamic(ClassesDynamic classesDynamic) {
        Connection conn = null;
        int key = 0;
        try {
            conn = ConnUtil.getTransactionConnection();
            //插入班级动态表
            key = classNewStatusDao.insertClassesDynamic( conn , classesDynamic );
            //先查询当前班级下所有老师( 之前刚刚加入该班级的老师不会被下面的sql查询出来*** )
            List< ClassesTeacher> list = teacherAddClassDao.findClassesTeacherByClassesId( classesDynamic.getClassesId()+"");
            // 插入老师动态关联表
            if( null != list && list.size() > 0 ){
                for( int i = 0 ; i< list.size();i++ ){
                    ClassesDynamicTeacher classesDynamicTeacher = makeClassesDynamicTeacher( key , list.get( i ).getTeacherId() , classesDynamic.getTeacherId() );
                    int id = classNewStatusDao.insertClassesDynamicTeacher( conn , classesDynamicTeacher );
                }
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( "班级产生的动态异常 :"  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return key;
    }

    /**
     * 组装班级动态和老师的关联表对象
     * @param classesDynamicId
     * @param teacherId
     * @return
     */
    public ClassesDynamicTeacher makeClassesDynamicTeacher( int classesDynamicId , Integer teacherId ,Integer nowTeacherId){
        ClassesDynamicTeacher dynamicTeacher = new ClassesDynamicTeacher();
        dynamicTeacher.setTeacherId( teacherId );
        dynamicTeacher.setClassesDynamicId( classesDynamicId );
        if( teacherId.intValue() == nowTeacherId.intValue() ){
            dynamicTeacher.setStatus( 1 );
        }else{
            dynamicTeacher.setStatus( 0 );
        }
        return dynamicTeacher;
    }
    /**
     * 创建班级
     * @param classes
     * @return
     */
    @Transactional
    @Override
    public int checkAndInsertClassInfo(Classes classes ,String realName ) {
        int id = 0;
        if( checkClassIsOk( classes )){
            Connection conn = null;
            try {
                conn = ConnUtil.getTransactionConnection();
                id = teacherAddClassDao.insertClassInfo(conn,classes);
                classes.setId( id );
                //ClassesTeacher classesTeacher = makeClassesTeacher( classes );
                //int classesTeacherKey =  insertClassesTeacher( classesTeacher ,classes.getTeacherId() , classes, realName + "创建了新班级" );
                conn.commit();
            }catch (SQLException e) {
                log.error( " 新增班级信息异常 ，班级名称:" +classes.getClassName()  + e.getMessage() , e );
                ConnUtil.rollbackConnection(conn);
                e.printStackTrace();
            }finally {
                ConnUtil.closeConnection(conn);
            }
        }
        return id;
    }

    @Override
    public ClassesTeacher makeClassesTeacher( Classes classes ){
        ClassesTeacher teacher = new ClassesTeacher();
        teacher.setStatus( CommonInstence.STATUS_0 );
        teacher.setClassesId( classes.getId() );
        teacher.setTeacherId( classes.getTeacherId() );
        teacher.setSubjectId( classes.getSubjectId() );
        return teacher;
    }

    @Override
    public User findTeacherNameById(int teacherId) {
        return teacherAddClassDao.findTeacherNameById( teacherId );
    }

    /**
     * 四位随机字母编码
     * @return
     */
    @Override
    public String makeClassCode(List< String > classedCodeList) {
        String random = makeRandomWord();
        if( null != classedCodeList && classedCodeList.size() > 0 ){
            int num = 0;
            for( int i = 0 ; i < classedCodeList.size();i++ ){
               // System.out.println( "班级编码:" + classedCodeList.get( i ) + " ,"+ i  );
                if( null != classedCodeList.get(i) && ( classedCodeList.get( i ).toLowerCase().equals( random ) ||classedCodeList.get( i ).toUpperCase().equals( random )) ){
                    num++;
                    break;
                }
            }
            if( num > 0 ){
                makeClassCode(classedCodeList);
            }
        }
        return random;
    }

    /**
     * 查询系统内所有班级编码
     * @return
     */
    public List< String > findClassesCode(){
        return  teacherAddClassDao.findAllClassesCode();
    }

    /**
     * 查询某学校内所有编辑编码
     * @return
     */

    public List< String > findClassesCode( int schoolId ){
        return  teacherAddClassDao.findAllClassesCodeBySchoolId( schoolId );
    }

    /**
     * 验证老师信息
     * @param className
     * @param year
     * @param type
     * @param rangeId
     * @param rangeName
     * @param subjectId
     * @param subjectName
     * @return
     */
    @Override
    public Classes checkInfo(String className ,String year ,String type ,String  rangeId,String rangeName,
                      String subjectId,String subjectName  ){
        Classes classes = new Classes(  );
        if( StringUtils.isBlank( className ) ){
            throw new BizException( "班级姓名为空" );
        }else if( StringUtils.isBlank( year ) ){
            throw new BizException( "班级入学年份为空" );
        }else if( StringUtils.isBlank( rangeId ) || !StringUtils.isNumeric( rangeId) ){
            throw new BizException( "班级学段id为空" );
        }else if( StringUtils.isBlank( subjectId ) || !StringUtils.isNumeric( subjectId ) ){
            throw new BizException( "班级学科id为空" );
        }else if( StringUtils.isBlank( subjectName )  ){
            throw new BizException( "学科名称为空" );
        }else if( StringUtils.isBlank( rangeName )  ){
            throw new BizException( "学段名称为空" );
        }
        classes.setClassName(className);
        //拼接全名
        String fullName = TeacherCommonUtils.getClassesName( className , new Integer(year) , rangeName ,0 );
        classes.setFullName( fullName );
        classes.setYear(new Integer(year));
        classes.setRangeId(new Integer(rangeId));
        classes.setRangeName(rangeName);
        //如果是学科班则记录具体的学科
        if( "1".equals( type )){
            classes.setSubjectId(new Integer(subjectId));
            classes.setSubjectName(subjectName);
        }
        classes.setType(new Integer(type));
        classes.setCreateTime( new Date());
        classes.setGraduate( CommonInstence.GRADUATE_0 );
        classes.setStatus( CommonInstence.STATUS_0 );
        //获取班级级别
        int level = analyForLevel( classes );
        classes.setLevel( level );
        return classes;
    }

    private int analyForLevel(Classes classes){
        int year = classes.getYear();
        //当前年
        int nowYear = DateFormatUtils.getYear(new Date());
        int lastYear = nowYear - year;
        String rangeName = classes.getRangeName();
        int nowMouthDay = DateFormatUtils.getMouthDay();
        // TODO 取school_up 表升级时间替换815 ，动态获取
        if( CommonInstence.UPTIME_815 < nowMouthDay ){
            lastYear+=1;
        }
        String name = TeacherCommonUtils.getRangeName(rangeName, lastYear );
        int level = CommonEnum.getLevelEnum.getSource( name ).getValue();
        return level;
    }

    /**
     * 查询班级人数
     * @param classesIdInt
     * @return
     */
    @Override
    public int findClassesNum(int classesIdInt) {
        return classHomePageDao.findClassNumByClassId( classesIdInt ,CommonInstence.STATUS_0 );
    }
    /**
     * 根据班级id查询班级信息
     * @param classesId
     * @return
     */
    @Override
    public Classes findClassesById(int classesId) {
        //先从缓存中取
        Classes classes = getClassesFromCache( CommonInstence.classes_map , CommonInstence.REDIS_CLASSKY + classesId);
        if( classes == null ){
            //为空则从数据库取，之后再存入缓存
            classes = teacherAddClassDao.findClassesById( new Integer( classesId ));
            if( classes != null ){
                classes.setTypeName(CommonEnum.classesTypeEnum.getSource( classes.getType()).getValue());
                addClassesToCache(CommonInstence.REDIS_CLASSKY + classes.getId(), classes);
            }
        }
        return classes;
    }
    /**
     * 根据班级id查询该班级下的任课教师
     * @param classesId
     * @return
     */
    @Override
    public List<ClassesTeacher> findClassesTeacherByClassesId(Integer classesId ,String rangeName ) {
        List<ClassesTeacher> list = teacherAddClassDao.findClassesTeacherByClassesId( classesId +"" );
       // Classes classes = teacherAddClassDao.findClassesById( new Integer( classesId ));
        if( null != list && list.size() > 0 ){
            for( int i = 0 ;i < list.size();i++ ){
                //查询老师下所有学科
                List<String > subjectList = teacherAddClassDao.findAllSubjectNameByTeacher( list.get( i ).getTeacherId() ,rangeName );
                String name = teacherAddClassDao.findUserNameByUserId( list.get( i ).getTeacherId() );
                list.get( i ).setTeacherName( name );
                list.get( i ).setList( subjectList );
            }
        }
        return list;
    }
    private static String makeRandomWord(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        for( int i = 0 ;i < 4 ;i++ ) {
            sb.append( chars.charAt((int) (Math.random() * 26)) );
        }
        return sb.toString();
    }
    public static void main( String[] args ){
        System.out.println( makeRandomWord() );
    }

    private boolean checkClassIsOk(Classes classes){
        if(StringUtils.isBlank(classes.getClassName())){
            throw new BizException( "班级名称为空","301");
        }else if( null == classes.getYear() ){
            throw new BizException( "入学年份为空","300");
        }else if( null == classes.getRangeId() ){
            throw new BizException( "学段id为空","302");
        }else if( StringUtils.isBlank(classes.getRangeName()) ){
            throw new BizException( "学段名称为空","303");
        }
        if( classes.getType() == 1 ){
            if( null == classes.getSubjectId() ){
                throw new BizException( "学科id为空","304");
            }else if( null == classes.getSubjectName()){
                throw new BizException( "学科名称为空","305");
            }
        }
        return true;
    }

    /**
     * 查询老师所有的学科信息
     * @param teacherId
     * @return
     */
    @Override
    public List<TeacherBookVersion> findTeacherBookVersion(int teacherId , int status){
        return teacherAddClassDao.findTeacherBookVersion( teacherId ,status );
    }

    /**
     * 从缓存获取班级
     * @param classesId
     * @return
     */
    public Classes getClassesFromCache(int classesId ){
        Classes classes = getClassesFromCache( CommonInstence.classes_map ,CommonInstence.REDIS_CLASSKY + classesId);
        if( classes == null ){
            //为空则从数据库取，之后再存入缓存
            classes = teacherAddClassDao.findClassesById( classesId );
            if( classes != null ){
                classes.setTypeName(CommonEnum.classesTypeEnum.getSource( classes.getType()).getValue());
                addClassesToCache(CommonInstence.REDIS_CLASSKY + classes.getId(), classes);
            }
        }
        return classes;
    }

    /**
     * 查询学科下的所有小组和学生
     * @param subjectId
     * @return
     */
    @Override
    public List<Team> findAllTeamStudentsBySubject(int subjectId ,int rangeId ,int classesId ,int schoolId ,int teacherId) {
        List< Team > teamList = null;
       // Classes classes = teacherAddClassDao.findClassesById( classesId );
        //先从缓存中取
        Classes classes = getClassesFromCache( CommonInstence.classes_map ,CommonInstence.REDIS_CLASSKY + classesId);
        if( classes == null ){
            //为空则从数据库取，之后再存入缓存
            classes = teacherAddClassDao.findClassesById( classesId );
            if( classes != null ){
                classes.setTypeName(CommonEnum.classesTypeEnum.getSource( classes.getType()).getValue());
                addClassesToCache(CommonInstence.REDIS_CLASSKY + classes.getId(), classes);
            }
        }
        List< Classes > classesList = teacherAddClassDao.findClassesBySubjectId( subjectId ,classes.getRangeId() ,classesId ,schoolId ,teacherId);
        if( null != classesList && classesList.size()> 0 ){
            teamList = new LinkedList<Team>( );
            for( int i = 0 ; i < classesList.size();i++ ){
                classesList.get( i ).setOk( false );
                Team team = new Team();
                team.setId( classesList.get( i ).getId() );
                team.setTeamName( classesList.get( i ).getClassName() );
                int teamManId=0;//组长id
                //查询班级下所有学生
                List< ClassesStudent > classesStudentList = teacherAddClassDao.findClassesStudentByClassesId( classesList.get( i ).getId() );
                if( null != classesStudentList ){
                    for( int j = 0 ; j < classesStudentList.size();j++ ){
                        //查询学生姓名
                        String studentName = teacherAddClassDao.findUserNameByUserId( classesStudentList.get( j ).getStudentId() );
                        classesStudentList.get( j ).setStudentName( studentName );
                        if(classesStudentList.get( j ).getHeadman() != null && classesStudentList.get( j ).getHeadman() == 1 ){
                            teamManId = classesStudentList.get( j ).getId();
                        }
                    }
                    team.setTeamManId( teamManId );
                }
                team.setClassesStudentsList( classesStudentList );
                teamList.add( team );
            }
        }
        return teamList;
    }
    /**
     * 查询老师名下所有学科
     * @param teacherId
     * @return
     */
    @Override
    public List<TeacherBookVersion> findAllSubjectByTeacher(int teacherId) {
        return teacherAddClassDao.findAllSubjectByTeacher( teacherId );
    }
    /**
     * 根据学科查询学段id
     * @param subjectId
     * @param rangeType
     * @return
     */
    @Override
    public int findRangeIdBySubjectId(Integer subjectId, String rangeType) {
        int rangeTypeI = new Integer( rangeType );
        int rangeId = 0 ;
        //先查询学科下的所有学段
        List<Tags> tagesList = KlbTagsUtil.getInstance().getTagById( subjectId );
        if( null != tagesList ){
            for( Tags tags : tagesList ){
                log.info( tags.getName() + "，学段名称：" +  RangeTypeEnum.getSource( rangeTypeI ).getValue() );
                if( tags.getName().equals(RangeTypeEnum.getSource( rangeTypeI ).getValue())){
                    rangeId = tags.getId() ;
                    break;
                }
            }
        }
        return rangeId;
    }
    /**
     * 删除班级
     * @param classesId
     * @param teacherId
     * @return
     */
    @Override
    public int deleteClasses(Integer classesId, int teacherId) {
        int num = 0;
        int studentNum = classHomePageDao.findClassNumByClassId( classesId ,CommonInstence.STATUS_0 );
        if( studentNum > 0 ){
            log.info( "班级id：" + classesId +"人数不为0，班级删除失败");
            return 0;
        }else{
            Connection conn = null;
            try {
                conn = ConnUtil.getTransactionConnection();
                num = teacherAddClassDao.deleteClasses(classesId, teacherId);
                deleteClassesFromCache(CommonInstence.classes_map , CommonInstence.REDIS_CLASSKY + classesId);
                conn.commit();
            }catch (SQLException e) {
                log.error( " 删除班级异常 ，班级id:" + classesId  + e.getMessage() , e );
                ConnUtil.rollbackConnection(conn);
                deleteClassesFromCache( CommonInstence.classes_map , CommonInstence.REDIS_CLASSKY + classesId );
                e.printStackTrace();
            }finally {
                ConnUtil.closeConnection(conn);
            }
            return num;
        }
    }

    /**
     * 插入老师班级表
     * @param classesTeacher
     * @return
     */
    @Override
    public int insertClassesTeacher(ClassesTeacher classesTeacher ,Integer teacherId , Classes classes , String dynamicInfo ,boolean isDynamic ) {
        int key = 0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            int type = classes.getType();
            //先查询老师是否已经加入当前的班级
            int num = 0 ;
            if( type == 1 ){
                 num = teacherAddClassDao.findTeacherByTeacherIdAndClassesId( classesTeacher.getTeacherId() , classesTeacher.getClassesId(),classesTeacher.getSubjectId() );
            }else if( type == 0 ){
                 num = teacherAddClassDao.findTeacherByTeacherIdForXingzheng(  classesTeacher.getTeacherId() , classesTeacher.getClassesId() );
            }
            if( 0 == num ){
                key = teacherAddClassDao.insertClassesTeacher( conn , classesTeacher );
                if( isDynamic ){
                    ClassesDynamic classesDynamic = makeClassesDynamic( teacherId , classes.getId() , dynamicInfo);
                    try {
                        insertClassesDynamic( classesDynamic );
                    }catch ( Exception e ){
                        log.info( "老师加入班级产生的动态异常" + e.getMessage() , e );
                        e.printStackTrace();
                    }
                }
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( " 插入老师班级表异常"   + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return key;
    }

    /**
     * 组装用户动态信息
     * @param userId
     * @param classesId
     * @param dynamicInfo
     * @return
     */
    @Override
    public ClassesDynamic makeClassesDynamic( int userId ,Integer classesId ,String dynamicInfo ){
        ClassesDynamic classesDynamic = new ClassesDynamic();
        classesDynamic.setCreateTime( new Date() );
        classesDynamic.setTeacherId( userId );
        classesDynamic.setClassesId( classesId );
        classesDynamic.setDynamicInfo(  dynamicInfo);
        classesDynamic.setStatus( CommonInstence.STATUS_0 );
        classesDynamic.setUserId( userId );
        return classesDynamic;
    }
    /**
     * 查询老师下具体学科
     * @param teacherId
     * @param rangeId
     * @param status
     * @return
     */
    @Override
    public List<TeacherBookVersion> findTeacherBookVersionByRangeIdAndSubjectId(int teacherId, int rangeId,int subjectId , int status) {
        return teacherAddClassDao.findTeacherBookVersionByRangeIdAndSubjectId( teacherId ,rangeId ,subjectId ,status );
    }
    /**
     * 查询老师下某学段的所有学科
     * @param teacherId
     * @param status
     * @return
     */
    @Override
    public List<TeacherBookVersion> findTeacherBookVersionByRangeId(int teacherId  , int status) {
        return teacherAddClassDao.findTeacherBookVersionByRangeId( teacherId  ,status );
    }
    @Override
    public List<TeacherBookVersion> findTeacherBookVersionByRangeName(int teacherId ,String rangeName , int status) {
        return teacherAddClassDao.findTeacherBookVersionByRangeName( teacherId ,rangeName ,status );
    }
    /**
     * 添加班级到缓存中
     * @param key
     * @param classes
     * @return
     */
    public boolean addClassesToCache( String key , Classes classes ){
        //失效缓存
        kooJedisClient.hset( CommonInstence.classes_map , key , classes);
        return true;
    }

    /**
     * 从缓存中获取班级对象
     * @param fieldKey
     * @return
     */
    public Classes getClassesFromCache( String mapKey , String fieldKey ){
        return kooJedisClient.hgetE( Classes.class ,mapKey , fieldKey );
    }

    /**
     * 从缓存删除
     * @param mapKey
     * @param fildkey
     */
    public void deleteClassesFromCache( String mapKey , String fildkey ){
        CacheTools.delMapCache( mapKey,fildkey );
    }

}
