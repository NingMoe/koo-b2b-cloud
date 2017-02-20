package com.koolearn.cloud.studentHome.impl;

import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.student.StudentClassHomeService;
import com.koolearn.cloud.studentHome.dao.StudentClassHomeDao;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.ClassHomePageDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddStudentDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 查询学生班级首页信息
 * Created by fn on 2016/5/19.
 */
@Service("studentClassHomeService")
public class StudentClassHomeServiceImpl implements StudentClassHomeService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentClassHomeDao studentClassHomeDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private ClassHomePageDao classHomePageDao;
    @Autowired
    private TeacherDataMaintainDao teacherDataMaintainDao;
    @Autowired
    private TeacherAddStudentDao teacherAddStudentDao;
    @Autowired
    private TeacherAddClassService teacherAddClassesService;


    public ClassHomePageDao getClassHomePageDao() {
        return classHomePageDao;
    }

    public void setClassHomePageDao(ClassHomePageDao classHomePageDao) {
        this.classHomePageDao = classHomePageDao;
    }

    public StudentClassHomeDao getStudentClassHomeDao() {
        return studentClassHomeDao;
    }

    public void setStudentClassHomeDao(StudentClassHomeDao studentClassHomeDao) {
        this.studentClassHomeDao = studentClassHomeDao;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public TeacherDataMaintainDao getTeacherDataMaintainDao() {
        return teacherDataMaintainDao;
    }

    public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }

    public TeacherAddStudentDao getTeacherAddStudentDao() {
        return teacherAddStudentDao;
    }

    public void setTeacherAddStudentDao(TeacherAddStudentDao teacherAddStudentDao) {
        this.teacherAddStudentDao = teacherAddStudentDao;
    }

    public TeacherAddClassService getTeacherAddClassesService() {
        return teacherAddClassesService;
    }

    public void setTeacherAddClassesService(TeacherAddClassService teacherAddClassesService) {
        this.teacherAddClassesService = teacherAddClassesService;
    }

    /**
     * 查询学生首页班级列表
     * @param studentId
     * @return
     */
    @Override
    public List<Classes> findAllClassesByStudentId(int studentId) {
        List< Classes > list = null;
        List<Classes> classesList = studentClassHomeDao.findAllClassesByStudentId( studentId , CommonInstence.STATUS_0 ,CommonInstence.CLASSES_TYPE_3);
        if( null != classesList ){
            list = new ArrayList<Classes>( classesList.size() );
            for( Classes classes : classesList ){
                    //查询班级人数
                int num = classHomePageDao.findClassNumByClassId( classes.getId(),CommonInstence.STATUS_0 );
                classes.setStudentNum( num );
                classes.setTypeName( CommonEnum.classesTypeEnum.getSource( classes.getType() ).getValue() );
                list.add( classes );
            }
        }
        return list;
    }

    /**
     * 查询班级列表
     * @param classesId
     * @return
     */
    public List<User> findClassStudentByClassesId( int classesId ){
        List<ClassesStudent> classesStudentList =  teacherAddClassDao.findClassesStudentByClassesId( classesId );
        List< User > list = null;

        if( null != classesStudentList ){
            list = new ArrayList<User>( classesStudentList.size() );
            //查询该班级下的小组
            List< Classes > classesList = teacherAddStudentDao.findClassesByParentId( classesId ,CommonInstence.STATUS_0 );
            for( int i = 0 ; i < classesStudentList.size();i++ ){
                User user = null;
                try{
                    user = teacherDataMaintainDao.findUserInfo(classesStudentList.get( i ).getStudentId());
                    if( null != classesList && classesList.size() > 0 ) {
                        for (int j = 0; j < classesList.size(); j++) {
                            ClassesStudent student = teacherAddStudentDao.findStudentTeamExist(classesStudentList.get(i).getStudentId(), classesList.get(j).getId(), CommonInstence.STATUS_0);
                            if (null != student) {
                                if (student.getHeadman() != null && student.getHeadman().intValue() == 1) {
                                    user.setHeadman(1);
                                    break;
                                }
                            }
                        }
                    }
                }catch( Exception e ){
                    log.info( " 学生id是：" + classesStudentList.get( i ).getStudentId());
                }
                list.add( user );
            }
        }
        return list;
    }

    /**
     * 学生加入班级
     * @param classesId
     * @param studentId
     * @return
     */
    @Override
    public int addStudentToClasses(Integer classesId, Integer studentId ,String studentName ) {
        // 加入班级
        int pramaryKey = 0 ;
        ClassesStudent classesStudent = makeClassesStudent( classesId , studentId );
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            //验证是否加入过
            int num = studentClassHomeDao.checkStudentClasses( classesId , studentId ,CommonInstence.STATUS_0 );
            log.info( "学生加入班级结果" + num );
            if( num > 0 ){
                log.info( "学生加入班级结果：已经加入过班级，学生名：" + studentName  );
                return 2;//已经加入过班级
            }

            pramaryKey = teacherAddStudentDao.insertClassesStudent( conn ,classesStudent );
            //记录学生动态
            String classesName = studentClassHomeDao.findClassesNameById(classesId);
            ClassesDynamic classesDynamic = teacherAddClassesService.makeClassesDynamic( studentId , classesId, studentName + "新加入" + classesName);
            if( null != classesDynamic ){
                 teacherAddClassesService.insertClassesDynamic( classesDynamic );
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "将学生加入班级和插入动态表异常 "  , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        } catch (BizException e) {
            log.error( "将学生加入班级和插入动态表异常" + e.getMessage() ,e );
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return pramaryKey;
    }



    private ClassesStudent makeClassesStudent( Integer classesId, Integer studentId ){
        ClassesStudent cs = new ClassesStudent();
        cs.setClassesId( classesId );
        cs.setHeadman( CommonInstence.STATUS_0 );
        cs.setStudentId( studentId );
        cs.setStatus( CommonInstence.STATUS_0 );
        cs.setCreateTime( new Date());
        return cs;
    }
    /**
     * 验证学生是否加入班级
     * @param classesCode
     * @param studentId
     * @return
     */
    @Override
    public int checkStudentClasses(String classesCode, Integer studentId) {
        Integer classesId = studentClassHomeDao.findClassesByClassesCode( classesCode , CommonInstence.STATUS_0 );
        if( null == classesId || 0 >= classesId.intValue() ){
            return 1;//编码不存在
        }else{//大于0可以加入
            //验证是否加入过
            int num = studentClassHomeDao.checkStudentClasses( classesId , studentId ,CommonInstence.STATUS_0 );
            log.info( "学生加入班级验证结果 " + num  + ",学生id:"+ studentId );
            if( num > 0 ){
                return 2;//已经加入过班级
            }else{
                return classesId;//可以加入
            }
        }
    }
    /**
     * 查询班级下是否有该学生
     * @param classesId
     * @param studentId
     * @return
     */
    @Override
    public int findStudentByClassIdAndStuId(Integer classesId, int studentId) {
        return studentClassHomeDao.checkStudentClasses( classesId , studentId ,CommonInstence.STATUS_0 );
    }

    /**
     * 学生退出班级
     * @param classesId
     * @param studentId
     * @return
     */
    @Override
    public boolean getOutClasses(Integer classesId, int studentId ,String studentName ) {
        //先查出该学生下所有的小组
        List< Integer > teamList = studentClassHomeDao.findAllTeamByClassesId( classesId , CommonInstence.STATUS_0 );
            Connection conn = null;
            try {
                conn = ConnUtil.getTransactionConnection();
                studentClassHomeDao.updateClassesStatus( conn , classesId , studentId ,CommonInstence.STATUS_1 );
                //从小组移除
                if( null != teamList && teamList.size() > 0 ) {
                    for (int i = 0; i < teamList.size(); i++) {
                        //修改classes_student状态( 删除 )
                        int num = studentClassHomeDao.updateClassesStatus(conn, teamList.get(i), studentId, CommonInstence.STATUS_1);
                    }
                }
                //记录学生动态
                String classesName = studentClassHomeDao.findClassesNameById(classesId);
                ClassesDynamic classesDynamic = teacherAddClassesService.makeClassesDynamic( studentId , classesId, studentName + "退出" + classesName);
                if( null != classesDynamic ){
                    teacherAddClassesService.insertClassesDynamic( classesDynamic );
                }
                conn.commit();
            } catch (SQLException e) {
                log.error( "将学生移除班级和小组异常 "  , e );
                ConnUtil.rollbackConnection(conn);
                e.printStackTrace();
                return false;
            } catch (BizException e) {
                log.error( "将学生移除班级和小组异常" + e.getMessage() ,e );
            } finally {
                ConnUtil.closeConnection(conn);
            }

        return true;
    }

    /**
     * 查询班级动态
     * @param classesId
     * @return
     */
    @Override
    public Pager findClassesDynamic(Integer classesId ,Integer studentId , int pageNo ) {
        Pager pagerBean = new Pager();
        int pageLine = 0 ;
        pagerBean.setPageSize( CommonInstence.PAGE_SIZE_20 );
        pagerBean.setPageNo(pageNo);
        //先根据学生id和班级id查询最后可以查看班级动态的时间点
        Date date = studentClassHomeDao.findDateCanReadBegin( classesId , studentId );
        if( date == null ){
            Date createTime = studentClassHomeDao.findCreateTimeByUserId( studentId ,classesId );
            date = createTime;
        }
        pageLine = studentClassHomeDao.findClassesDynamicLineByClassId( classesId , date );
        pagerBean.setTotalRows( pageLine );
        if( pageLine > 0 ){
            List< ClassesDynamic > list =  studentClassHomeDao.findClassesDynamicList( classesId ,pageNo * CommonInstence.PAGE_SIZE_20 ,CommonInstence.PAGE_SIZE_20 ,date);
            if( null != list ){
                for( int i = 0 ;i < list.size();i++ ){
                    Date createTime = list.get( i ).getCreateTime();
                    if( null != createTime ){
                        list.get( i ).setCreateTimeStr(DateFormatUtils.dateLFormat( createTime ));
                    }
                }
            }
            pagerBean.setResultList( list );
        }
        return pagerBean;
    }
    /**
     * author ：gehaisong
     * 获取学生学科学段
     * @param
     * @return
     */
    @Override
    public List<TreeBean>  findSubjectOrRangeOfStudent(Integer studentId,Integer subjectId ) {
         List<Integer> classIdList=studentClassHomeDao.findClassOfStudent( studentId  );
         List<String> subjectRangeName=studentClassHomeDao.findCSubRangeOfClassIds(classIdList);
        if(subjectRangeName==null&&subjectRangeName.size()<1)return null;
        Map<String,List<TreeBean>> rangeBySubjectMap=new HashMap<String, List<TreeBean>>();
        List<TreeBean> subjectList=new ArrayList<TreeBean>();
        for(String sr:subjectRangeName){
            String[] srarr=sr.split("_");
            String subjectName=srarr[0],rangeName=srarr[1];
            List<TreeBean> rangeList=rangeBySubjectMap.get(subjectName);
            if(rangeList==null) rangeList=new ArrayList<TreeBean>();
            Dictionary dictionary=DataDictionaryUtil.getInstance().getDataDictionaryListByTypeAndName(GlobalConstant.DICTIONARY_TYPE_KLB_SUBJECT,subjectName);
            TreeBean subTree=new TreeBean();
            subTree.setId(dictionary.getValue());
            subTree.setName(dictionary.getName());
            TreeBean rangeTree=KlbTagsUtil.getInstance().getTagByTagnameAndPrentId(rangeName,subTree.getId());
            addQuchongList(subjectList,subTree);
            addQuchongList(rangeList,rangeTree);
            orderRange(rangeList);
            rangeBySubjectMap.put(subjectName,rangeList);
        }
        if(subjectId>0){
            //获取学生学段
           return rangeBySubjectMap.get(KlbTagsUtil.getInstance().getTag(subjectId).getName());
        }
        //获取学生学科
        return subjectList;
    }

    /**
     * 按学科排序 高中 初中 小学
     * @param rangeList
     */
    private static  void orderRange(List<TreeBean> rangeList) {
        TreeBean temp; // 记录临时中间值
        int size = rangeList.size(); // 数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                int iragenameValue=getValueOfRange(rangeList.get(i).getName());
                int jragenameValue=getValueOfRange(rangeList.get(j).getName());
                if (iragenameValue < jragenameValue) { // 交换两数的位置
                    temp =rangeList.get(i);
                    rangeList.set(i,rangeList.get(j));
                    rangeList.set(j,temp);
                }
            }
        }
    }
    private static int getValueOfRange(String name){
        //获取学段值
        if("高中".equals(name)) return 3;
        if("初中".equals(name)) return 2;
        if("小学".equals(name)) return 1;
        return -1;
    }

    private void addQuchongList(List<TreeBean> subjectList, TreeBean subTree) {
        boolean f=true;
        for(TreeBean tb:subjectList){
            if(tb.getId()==subTree.getId()){
                f=false;
                break;
            }
        }
        if(f){
            subjectList.add(subTree);
        }
    }
    /**
     * 查询所有所在班级id
     * @param studentId
     * @return
     */
    public List<Integer> findAllClassesIdByStudentId(Integer studentId ){
        return studentClassHomeDao.findAllClassesIdByStudentId( studentId );
    }
}
