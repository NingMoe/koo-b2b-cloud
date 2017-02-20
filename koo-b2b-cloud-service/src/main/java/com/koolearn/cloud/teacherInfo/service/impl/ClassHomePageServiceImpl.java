package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.teacher.service.ClassHomePageService;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacherInfo.dao.*;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fn on 2016/4/5.
 */
@Service("classHomePageService")
public class ClassHomePageServiceImpl implements ClassHomePageService {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ClassHomePageDao firstPageDao;
    @Autowired
    private TeacherDataMaintainDao teacherDataMaintainDao;
    @Autowired
    private ClassNewStatusDao classNewStatusDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private ClassesResourceDao classesResourceDao;
    @Autowired
    private CommonService commonService;

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public ClassesResourceDao getClassesResourceDao() {
        return classesResourceDao;
    }

    public void setClassesResourceDao(ClassesResourceDao classesResourceDao) {
        this.classesResourceDao = classesResourceDao;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public ClassNewStatusDao getClassNewStatusDao() {
        return classNewStatusDao;
    }

    public void setClassNewStatusDao(ClassNewStatusDao classNewStatusDao) {
        this.classNewStatusDao = classNewStatusDao;
    }

    public TeacherDataMaintainDao getTeacherDataMaintainDao() {
        return teacherDataMaintainDao;
    }

    public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }

    public ClassHomePageDao getFirstPageDao() {
        return firstPageDao;
    }

    public void setFirstPageDao(ClassHomePageDao firstPageDao) {
        this.firstPageDao = firstPageDao;
    }

    /**
     * 查询老师名下所有班级
     * @param teacherId
     * @return
     */
    @Override
    public List<Classes> findTeacherClassById(int teacherId,int classesType ,int status) {
        List< Classes > classList = firstPageDao.findTeacherAllClassByTeacherId( teacherId ,CommonInstence.STATUS_0 ,classesType );
        //Collections.sort( classList , ascComparator );
        //查询每个班级人数，
        if( null!= classList && classList.size() > 0 ){
            for( int i = 0 ; i < classList.size();i++ ){
                //TODO 拼装班级名称根据入学年份 （ 同步升级班级 ）
                int classNum = firstPageDao.findClassNumByClassId(classList.get(i).getId(),CommonInstence.STATUS_0 );
                classList.get( i ).setStudentNum( classNum );// 班级人数
                //查询当前登录的老师在该班级下是否有未读的动态
                List<ClassesDynamic > dynamicList = classNewStatusDao.findNoReadDynamic( classList.get( i ).getId()  ,CommonInstence.STATUS_0);
                //查询每个班级动态对应的老师列表
                int readed = 0 ;
                if( null != dynamicList && dynamicList.size() > 0 ){
                    for( int j = 0 ; j < dynamicList.size();j++ ) {
                        List<ClassesDynamicTeacher> teacherList = classNewStatusDao.findClassesDynamicTeacherById(dynamicList.get(j).getId(), CommonInstence.STATUS_0);
                        if (null != teacherList) {
                            for (ClassesDynamicTeacher dynamic : teacherList) {
                                if (dynamic.getTeacherId().intValue() == teacherId  && dynamic.getStatus() == 0) {
                                    readed = 1;//有新的未读动态
                                    break;
                                }
                            }
                        }
                    }
                }
                if( readed == 1 ){
                    classList.get( i ).setHasDynamic( 1 );
                }
            }
        }
        return classList;
    }

    /**
     * 老师下所有加入的班级及  班级最新动态标识判断
     * @param teacherId
     * @param classesType
     * @param status
     * @return
     */
    @Override
    public List<Classes> findTeacherClassForDynamicById(int teacherId,int classesType ,int status) {
        List< Classes > classList = firstPageDao.findTeacherAllClassByTeacherId( teacherId ,CommonInstence.STATUS_0 ,classesType );
        //Collections.sort( classList , ascComparator );
        //查询每个班级人数，
        if( null!= classList && classList.size() > 0 ){
            for( int i = 0 ; i < classList.size();i++ ){
                //TODO 拼装班级名称根据入学年份 （ 同步升级班级 ）
                int classNum = firstPageDao.findClassNumByClassId(classList.get(i).getId(),CommonInstence.STATUS_0 );
                classList.get( i ).setStudentNum( classNum );// 班级人数
            }
        }
        return classList;
    }
    /**
     * 查询班级下课程和作业
     * @return
     */
    @Override
    public List<HomePageClassesExam> findExamInfoByClassId( List<Classes > classList ,Integer teacherId){
        List< HomePageClassesExam> list = new LinkedList<HomePageClassesExam >(  );
        String nowDay = DateFormatUtils.getDay(new Date());
        Date dateBegin = DateFormatUtils.dateStrFormat( nowDay + CommonInstence.begin );
        Date dateEnd = DateFormatUtils.dateStrFormat( nowDay + CommonInstence.end );
        if( null != classList && classList.size() > 0 ){
            for( int i = 0 ;i < classList.size();i++ ){
                HomePageClassesExam classes = new HomePageClassesExam();
                //查询班级下的作业和课堂
                List<ClassesExam> examClassesList = getClassesExamBy( classList.get( i ).getId() ,teacherId , dateBegin ,nowDay + CommonInstence.end );
                //查询当前登录的老师在该班级下是否有未读的动态
                List<ClassesDynamic > dynamicList = classNewStatusDao.findNoReadDynamic( classList.get( i ).getId() ,CommonInstence.STATUS_0);
                //查询每个班级动态对应的老师列表
                int readed = 0 ;
                if( null != dynamicList && dynamicList.size() > 0 ){
                    for( int j = 0 ; j < dynamicList.size();j++ ) {
                        List<ClassesDynamicTeacher> teacherList = classNewStatusDao.findClassesDynamicTeacherById(dynamicList.get(j).getId(), CommonInstence.STATUS_0);
                        if (null != teacherList) {
                            for (ClassesDynamicTeacher dynamic : teacherList) {
                                if (dynamic.getTeacherId().intValue() == teacherId.intValue() && dynamic.getStatus() == 0) {
                                    readed = 1;//有新的未读动态
                                    break;
                                }
                            }
                        }
                    }
                }
                classes = makeClasses( classes , classList.get( i ) ,examClassesList ,readed );
                list.add( classes );
            }
        }
        return list;
    }

    private List<ClassesExam> getClassesExamBy(  Integer classesId ,Integer teacherId ,Date dateBegin ,String dateEnd ){
        //查询班级下的小组
        List< Integer > teamIdList = classesResourceDao.findAllTeamByClassesId(classesId);
        if( null == teamIdList ){
            teamIdList = new ArrayList<Integer>();
        }
        teamIdList.add( classesId );
        /*
        StringBuffer classesIds = new StringBuffer();
            classesIds.append( classesId );
            if( teamIdList != null && teamIdList.size() > 0 ){
                classesIds.append( "," );
                for( int i = 0 ; i < teamIdList.size();i++ ){
                    classesIds.append( teamIdList.get( i ) );
                    if( i < teamIdList.size() - 1 ){
                        classesIds.append( "," );
                    }
                }
            }
            */
        List<ClassesExam> examClassesList = firstPageDao.findExamClassedByClassIdDay( teamIdList ,teacherId , dateBegin ,dateEnd );

        if( null == examClassesList ){
            examClassesList = new ArrayList<ClassesExam>();
        }
        // 查询老师是否浏览过
        //TODO　第一期数据库不满足 课堂是否被浏览过的记录，需要加表，
        /*
        for( int j = 0;j < examClassesList.size();j++ ){
            int type = examClassesList.get( j ).getExamType();
            //作业
            if( 1 == type ){
                List<Integer> teacherView = firstPageDao.findTeacherViewByExamId(examClassesList.get(j).getExamId(),
                        CommonInstence.teacherView_1,
                        CommonInstence.examResultStatus_2);
                if (null != teacherView) {
                    for (Integer isRead : teacherView){
                        //1.老师未浏览  2.老师已浏览
                        if (isRead != null && isRead.intValue() == 1){
                            examClassesList.get(j).setTeacherView(isRead.intValue());
                        }
                    }
                }
            }else if (2 == type){//课堂
                    if ( isAllDone( examClassesList.get(j).getExamId(), classesId, teacherId ) ) {
                        //有学生完成课堂了
                        examClassesList.get(j).setTeacherView( 1 );
                    }
             }
        }
        */
        return examClassesList;
    }
    /**
     * 判断是否有学生完成课堂
     * @param examId
     * @param classesIdInt
     * @param teacherId
     * @return
     */
    public boolean isAllDone( int examId , int classesIdInt ,int teacherId ){
        List<TpExamStudent> studentList = classesResourceDao.findKeTangStudents(examId, classesIdInt, teacherId);
        int ketangDoneNum = commonService.analyKeTangDoneNum( examId ,classesIdInt ,teacherId);
        if ( 0 < ketangDoneNum ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 组装首页班级数据
     * @param classesNew
     * @param classes ：DB班级信息
     * @param examClassesList ： 班级作业课堂信息
     * @param status ： 是否被浏览过
     * @return
     */
    private HomePageClassesExam makeClasses( HomePageClassesExam classesNew , Classes classes , List<ClassesExam> examClassesList ,int status ){
        classesNew.setClassId( classes.getId());
        classesNew.setClassName(classes.getFullName());
        classesNew.setClassType( classes.getType() );
        classesNew.setClassTypeName( CommonEnum.classesTypeEnum.getSource( classes.getType() ).getValue() );
        classesNew.setPepoleNum(classes.getStudentNum());//班级人数
        classesNew.setSubjectName(classes.getSubjectName());
        classesNew.setRangeName(classes.getRangeName());
        classesNew.setExamClassesList(examClassesList);
        if( null != classes.getSubjectId() ){
            classesNew.setSubjectId(classes.getSubjectId());
        }
        if( null != classes.getRangeId() ){
            classesNew.setRangeId( classes.getRangeId() );
        }
        //是否有动态
        classesNew.setDynamicStatus( status );
        return classesNew;
    }
    /**
     * 根据User主键查询user
     * @param teacherId
     * @return
     */
    @Override
    public User findUserInfoById(int teacherId){
        User user = teacherDataMaintainDao.findUserInfo( teacherId );
        return user;
    }
}
