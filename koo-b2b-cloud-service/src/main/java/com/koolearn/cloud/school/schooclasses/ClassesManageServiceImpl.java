package com.koolearn.cloud.school.schooclasses;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.dto.ClassPageDto;
import com.koolearn.cloud.school.dto.ClassTeacherPageDto;
import com.koolearn.cloud.school.entity.dto.TeacherDto;
import com.koolearn.cloud.school.schooclasses.dao.ClassesManageDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.util.ConnUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/10/31.
 */
@Service("classesManageService")
public class ClassesManageServiceImpl implements ClassesManageService {

    Logger log = Logger.getLogger( this.getClass() );
    private static final int BUF_SIZE = 8192;
    @Autowired
    private ClassesManageDao classesManageDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public ClassesManageDao getClassesManageDao() {
        return classesManageDao;
    }

    public void setClassesManageDao(ClassesManageDao classesManageDao) {
        this.classesManageDao = classesManageDao;
    }

    /**
     * 班级信息分页查询
     * @param classPageDto
     * @return
     */
    @Override
    public Map<String, Object> findClassPage(ClassPageDto classPageDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Classes> classesList =null;
        try{
            Integer pageLine = classesManageDao.findClassesPageLine( classPageDto );
            classPageDto.setTotalLine( pageLine );
            if( pageLine > 0 ){
                classesList = classesManageDao.findClassesPageInfo( classPageDto );
                if( null != classesList && classesList.size()> 0 ){
                    for( int i = 0 ; i < classesList.size();i++ ){
                        Integer classesId = classesList.get( i ).getId();
                        classesList.get( i ).setLevelName( CommonEnum.getClassesNameByLevelEnum.getSource(  classesList.get( i ).getLevel()).getValue() );
                        //查询班级下老师数量
                        Integer teacherNum = classesManageDao.findTeacherNumByClassesId( classesId );
                        classesList.get( i ).setTeacherNum( teacherNum );
                        //查询所有老师名字
                        StringBuffer sb = new StringBuffer();
                        List< String > teacherNameList = classesManageDao.findTeacherNameByClassesId( classesId );
                        if( null != teacherNameList && teacherNameList.size() > 0 ){
                            for( int j = 0 ; j < teacherNameList.size();j++ ){
                                sb.append( teacherNameList.get( j ));
                                if( j < ( teacherNameList.size() - 1) ){
                                    sb.append( "," );
                                }
                            }
                        }
                        classesList.get( i ).setTeacherName( sb.toString() );
                        //查询班级下学生数量
                        Integer studentNum = classesManageDao.findStudentNumByClassesId( classesId );
                        classesList.get( i ).setStudentNum( studentNum );
                    }
                }
            }
            map.put( CommonInstence.totalPage , classPageDto.getTotalPage() );
            map.put( CommonInstence.currentPage , classPageDto.getCurrentPage() );
            //map.put( CommonInstence.currentPage , classPageDto.getCurrentPage() );
            map.put( "dataList", classesList!= null?classesList:"{}" );
        }catch( Exception e ){
            e.printStackTrace();
            log.error( "班级分页查询异常" + e.getMessage() ,e );
        }
        return map;
    }
    /**
     * 修改班级状态（激活，关闭 ）
     * @param operType
     * @param classesId
     */
    @Override
    public void closeOrOpenClasses(String operType, Integer classesId) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            if( "0".equals( operType )){
                classesManageDao.closeOrOpenClasses( conn ,classesId ,0 );//激活班级
            }else if( "10".equals( operType )){
                classesManageDao.closeOrOpenClasses( conn ,classesId , 10);//关闭班级
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( " 修改班级状态异常"   + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
            throw new SQLException();
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }



    /**
     * 批量插入班级信息
     * @param classesList
     * @return
     */
    public int insertClassesLargeInfo(List<Classes> classesList) throws SQLException {
        int num =0;
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            for( Classes classes : classesList ){
                //先判断是否已经存在
                int count = classesManageDao.IsExistClassesNum( classes.getClassName() , classes.getYear() ,classes.getType(),classes.getSubjectId(),classes.getSubjectName(),
                        classes.getRangeId() , classes.getRangeName() );
                //不存在则入库
                if( 0 == count ){
                    teacherAddClassDao.insertClassInfo( conn , classes );
                    num++;
                }
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( " 批量插入班级信息异常"   + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            throw new SQLException();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return num;
    }
    /**
     * 判断是否为excel文件
     * @param extendName
     * @return
     */
    boolean isExcelFile( String extendName ){
        if( "xlsx".equals( extendName) || "xls".equals( extendName)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 查询班级下所有教师
     * @param classPageDto
     * @return
     */
    public Map<String,Object> findClassTeacherPage( ClassPageDto classPageDto ){
        Map< String , Object > map = new HashMap<String, Object>();
        int pageLine = classesManageDao.findClassTeacherTotalPage( classPageDto.getClassesId() , CommonInstence.STATUS_0 );
        classPageDto.setTotalLine( pageLine );
        if( 0 < pageLine ){
            map.put( "totalPage" ,classPageDto.getTotalPage() );
            //获取班级下的所有教师
            List<ClassTeacherPageDto> classTeacherPageDtoList = classesManageDao.findClassesTeacherPageList( classPageDto.getClassesId(),
                                                                                                              classPageDto.getCurrentPage() * classPageDto.getPageSize(),
                                                                                                              classPageDto.getPageSize() );
            if( null != classTeacherPageDtoList && classTeacherPageDtoList.size() > 0 ){
                for( int i =0 ; i < classTeacherPageDtoList.size();i++ ){
                    //获取教师的所有学科
                    List< String > teacherBookVersionList = classesManageDao.findTeacherBookVersionByTeacherId( classTeacherPageDtoList.get( i ).getUserId() );
                    if( null != teacherBookVersionList && teacherBookVersionList.size() > 0 ){
                        String str = "";
                        for( int j = 0 ; j < teacherBookVersionList.size();j++ ){
                            str+=teacherBookVersionList.get( j );
                            if( j < teacherBookVersionList.size() - 1 ){
                                str+=",";
                            }
                        }
                        classTeacherPageDtoList.get( i ).setSubjectStr( str );
                    }else{
                        classTeacherPageDtoList.get( i ).setSubjectStr( "" );
                    }
                }
            }
            map.put( "dataList" ,classTeacherPageDtoList );
        }
        return map;
    }
    /**
     * 查询班级下所有教师
     * @param classPageDto
     * @return
     */
    public Map<String,Object> findClassStudentPage( ClassPageDto classPageDto ){
        Map< String , Object > map = new HashMap<String, Object>();
        int pageLine = classesManageDao.findClassStudentTotalPage( classPageDto.getClassesId() , CommonInstence.STATUS_0 );
        classPageDto.setTotalLine( pageLine );
        if( 0 < pageLine ){
            map.put( "totalPage" ,classPageDto.getTotalPage() );
            //获取班级下的所有教师
            List<ClassTeacherPageDto> classTeacherPageDtoList = classesManageDao.findClassesStudentPageList( classPageDto.getClassesId(),
                    CommonInstence.STATUS_0,
                    classPageDto.getCurrentPage() * classPageDto.getPageSize(),
                    classPageDto.getPageSize() );
            map.put( CommonInstence.dataList ,classTeacherPageDtoList );
        }
        return map;
    }
    /**
     * 解除教师与班级关系
     * @param userId
     * @param classesId
     */
    public void updateClassesTeacherOrStudentStatus(Integer userId , Integer classesId , String type) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            int num =0;
            if( "1".equals( type)){
                num = classesManageDao.updateClassesTeacherStatus( conn ,userId , classesId , CommonInstence.STATUS_0 );
            }else if( "2".equals( type)){
                num = classesManageDao.updateClassesStudentStatus( conn ,userId , classesId , CommonInstence.STATUS_0 );
            }
            if( 0 == num ){
                log.info( "解除教师学生与班级关系失败，用户id:" + userId );
                throw new SQLException();
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( " 解除教师与班级关系异常"   + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            throw new SQLException();
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }

    /**
     * 查询某学科下除了当前学科下已加入该班级以外的所有老师
     */
    public List<TeacherDto> findAllTeacherBySubjectAndClass(Integer schoolId , Integer subjectId , Integer classesId ){
        //先查询该班级下的所有老师
        List< Integer > teacherDtoList =classesManageDao.findClassesTeacher( classesId ,subjectId , CommonInstence.STATUS_0 );
        List<TeacherDto> teacherList = null;
        if( null != teacherDtoList && teacherDtoList.size() > 0 ){
            teacherList = classesManageDao.findAllTeacherBySubjectAndClass( teacherDtoList , schoolId , subjectId );
        }else{
            teacherList = classesManageDao.findAllTeacherBySubjectAndClassNoTeacher(schoolId, subjectId);
        }
        return teacherList;
    }
    /**
     * 批量添加班级下教师
     * @param classes
     * @param teacherIdStr
     */
    public  void addClassesTeacher(Classes classes , String teacherIdStr , String subjectStr){
        String[] teacherIdArray = teacherIdStr.split( "," );
        String[] subjectIdArray = subjectStr.split( "," );
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            if( null != teacherIdArray && null != subjectIdArray && teacherIdArray.length == subjectIdArray.length ){
                int num = 0;
                for( int i = 0 ;i < teacherIdArray.length;i++ ){
                    if( classes.getType() == 1 ){
                        num = teacherAddClassDao.findTeacherByTeacherIdAndClassesId( new Integer( teacherIdArray[ i ] ) , classes.getId() , new Integer( subjectIdArray[ i ]) );
                    }else if( classes.getType() == 0 ){
                        num = teacherAddClassDao.findTeacherByTeacherIdForXingzheng( new Integer( teacherIdArray[ i ] ) , classes.getId() );
                    }
                    if( 0 == num ){
                        ClassesTeacher classesTeacher = new ClassesTeacher();
                        classesTeacher.setCreateTime( new Date() );
                        classesTeacher.setTeacherId( new Integer( teacherIdArray[ i ] ));
                        classesTeacher.setSubjectId( new Integer( subjectIdArray[ i ] ));
                        classesTeacher.setClassesId( classes.getId() );
                        classesTeacher.setStatus( CommonInstence.STATUS_0 );
                        teacherAddClassDao.insertClassesTeacher( conn , classesTeacher );
                    }
                }
            }
            conn.commit();
        }catch (SQLException e) {
            log.error( "添加班级下教师异常 ，班级id:" + classes.getId()  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
    }



}
