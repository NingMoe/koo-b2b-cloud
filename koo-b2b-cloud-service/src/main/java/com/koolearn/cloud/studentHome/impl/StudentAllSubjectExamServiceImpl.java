package com.koolearn.cloud.studentHome.impl;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.student.StudentAllSubjectExamService;
import com.koolearn.cloud.student.entity.StudentSubject;
import com.koolearn.cloud.student.entity.TeacherSubject;
import com.koolearn.cloud.studentHome.dao.StudentAllSubjectExamDao;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.ClassesResourceDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.DateFormatUtils;
import com.meidusa.amoeba.sqljep.function.Hash;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Int;

import java.util.*;

/**
 * 学生所有作业所属学科进度展示
 * Created by fn on 2016/5/24.
 */
@Service("studentAllSubjectExamService")
public class StudentAllSubjectExamServiceImpl implements StudentAllSubjectExamService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentAllSubjectExamDao studentAllSubjectExamDao;
    @Autowired
    private ClassesResourceDao classesResourceDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private TeacherAddClassService teacherAddClassesService;

    public TeacherAddClassService getTeacherAddClassesService() {
        return teacherAddClassesService;
    }

    public void setTeacherAddClassesService(TeacherAddClassService teacherAddClassesService) {
        this.teacherAddClassesService = teacherAddClassesService;
    }

    public TeacherAddClassDao getTeacherAddClassDao() {
        return teacherAddClassDao;
    }

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public ClassesResourceDao getClassesResourceDao() {
        return classesResourceDao;
    }

    public void setClassesResourceDao(ClassesResourceDao classesResourceDao) {
        this.classesResourceDao = classesResourceDao;
    }

    public StudentAllSubjectExamDao getStudentAllSubjectExamDao() {
        return studentAllSubjectExamDao;
    }

    public void setStudentAllSubjectExamDao(StudentAllSubjectExamDao studentAllSubjectExamDao) {
        this.studentAllSubjectExamDao = studentAllSubjectExamDao;
    }

    /**
     * 查询该学生所有留过的作业和课堂的学科
     * @param studentId
     * @param pageNo
     * @return
     */
    public Pager findAllSubjectsByExam(Integer studentId ,Integer pageNo ) throws Exception {
        Pager pagerBean = new Pager();
        try{
            int pageSize = studentAllSubjectExamDao.findExamHistorySubjectLine( studentId ,CommonInstence.STATUS_4 ,  CommonInstence.STATUS_1);
            List<StudentSubject> pageStudentSubjectList = null;
            pagerBean.setPageSize( CommonInstence.PAGE_SIZE_10 );
            pagerBean.setPageNo(pageNo);
            pagerBean.setTotalRows( pageSize );
            List<StudentSubject> studentSubjectList = null;
            if( 0 < pageSize ){
                studentSubjectList = studentAllSubjectExamDao.findExamHistorySubjectAndRange( studentId ,
                        pageNo * CommonInstence.PAGE_SIZE_10 ,
                        CommonInstence.PAGE_SIZE_10 ,
                        CommonInstence.STATUS_4 ,
                        CommonInstence.STATUS_1 );
                if( null != studentSubjectList && studentSubjectList.size() > 0 ){
                    // 查询学科下的老师和班级
                    pageStudentSubjectList = findTeacherAndClassesListBySubject(studentSubjectList, studentId);
                }
            }
            //查询每个学科下所有的作业和课堂
            List<StudentSubject> list = findSubjectExamList( pageStudentSubjectList , studentId);
            pagerBean.setResultList( list );
        }catch( Exception e ){
            log.error( "学生端首页查询异常" + e.getMessage() , e  );
            throw new Exception();
        }
        return pagerBean;
    }

    /**
     * 查询学科下的老师和班级( 升级 )
     * @param studentSubjectList
     * @param studentId
     * @return
     */
    private List<StudentSubject> findTeacherAndClassesListBySubject( List<StudentSubject> studentSubjectList ,int studentId ) {
        Set< String > rangeNameSet = new HashSet<String>( );//存储班级所有学段
        //先查询学生下在当前学科作业的某学段的所有班级
        List<Classes> classesList = studentAllSubjectExamDao.findAllClassesByStudentId(studentId, CommonInstence.STATUS_0);
        for( Classes classes : classesList ){
            rangeNameSet.add( classes.getRangeName() );
        }
        try{
            if (null != classesList && classesList.size() > 0){
                for (int j = 0; j < studentSubjectList.size(); j++) {
                    //查询学科名称
                    String subjectName = studentAllSubjectExamDao.findSubjectNameById(studentSubjectList.get(j).getSubjectId());
                    studentSubjectList.get( j ).setSubjectName(subjectName);
                    studentSubjectList.get( j ).setSubjectId(studentSubjectList.get(j).getSubjectId());
                    /*
                    Set<String> teacherSet = new HashSet<String>();//记录某学科所有老师
                    Set< Integer > teacherIdSet = new HashSet<Integer>();//所有老师id

                    for (int i = 0; i < classesList.size(); i++) {
                        if( classesList.get( i ).getType().intValue() != 3 ){
                            //某学科班下所有的老师
                            List<TeacherSubject> teacherList = studentAllSubjectExamDao.findAllTeacherByclassesId(classesList.get(i).getId(), CommonInstence.STATUS_0);
                            //判断老师的集合中是否有担任当前该学段和学科的老师
                            if (null != teacherList) {
                                for (int n = 0; n < teacherList.size(); n++) {
                                    List<Integer> teacherBookVersionSubjectIdList = studentAllSubjectExamDao.findTeacherBookVersionByTeacherId(
                                            teacherList.get(n).getTeacherId(),
                                            CommonInstence.STATUS_1 ,
                                            rangeNameSet
                                    );
                                    if (teacherBookVersionSubjectIdList != null && teacherBookVersionSubjectIdList.contains(studentSubjectList.get(j).getSubjectId())) {
                                        teacherSet.add(teacherList.get(n).getTeacherName());
                                        teacherIdSet.add( teacherList.get( n ).getTeacherId() );
                                    }
                                }
                            }
                        }
                    }
                    studentSubjectList.get( j ).setTeacherSet( teacherSet );
                    //查询老师下所有的班级
                    Set< String > classesNameSet = studentAllSubjectExamDao.findAllClassesByTeacherIds( teacherIdSet ,studentSubjectList.get( j ).getSubjectId());
                    studentSubjectList.get( j ).setClassesSet( classesNameSet );
                    */
                }
            }
        }catch ( Exception e ){
            log.error( " 学生端查询学科作业异常"+e.getMessage(), e );
            e.printStackTrace();
        }

        return studentSubjectList;
    }

    /**
     * 查询学科下的老师和班级
     * @param subjectIdList
     * @param studentId
     * @return
     */
    private List<StudentSubject> findAllSubjectInfo( List<Integer> subjectIdList ,int studentId ){
        List<StudentSubject> pageStudentSubjectList = new ArrayList<StudentSubject>( subjectIdList.size() );
        for( int i = 0 ;i < subjectIdList.size();i++ ){
            StudentSubject studentSubject = new StudentSubject();
            //查询学科名称
            String subjectName = studentAllSubjectExamDao.findSubjectNameById( subjectIdList.get( i ));
            studentSubject.setSubjectName( subjectName );
            studentSubject.setSubjectId( subjectIdList.get( i ));
            //查询学科下老师id
            Set< String > teacherIdSet = studentAllSubjectExamDao.findExamSubjectAllTeacher( CommonInstence.STATUS_1 , subjectIdList.get( i ) , studentId );
            if( null != teacherIdSet ){
                Set< String > teacherNameSet = new HashSet<String>( teacherIdSet.size() );
                Iterator< String > teacherIds = teacherIdSet.iterator();
                while ( teacherIds.hasNext() ){
                    String id = teacherIds.next();
                    UserEntity user = CacheTools.getCache( id , UserEntity.class );
                    if( user == null ){
                        String realName = teacherAddClassDao.findUserNameByUserId( new Integer( id ));
                        teacherNameSet.add( realName );
                    }else{
                        teacherNameSet.add( user.getRealName() );
                    }
                }
                //添加学科下所有老师
                studentSubject.setTeacherSet( teacherNameSet );
            }
            //查询某学科在作业课堂表的所有班级
            List< Integer > classesIdList = studentAllSubjectExamDao.findAllClassesBySubjectId( CommonInstence.STATUS_1, subjectIdList.get( i ) ,studentId );
            if( null != classesIdList && classesIdList.size() > 0 ){
                Set<String > classesNameSet = new HashSet<String>( classesIdList.size() );
                for( int j = 0 ; j < classesIdList.size();j++ ){
                    Classes classes = teacherAddClassesService.getClassesFromCache(classesIdList.get(j));
                    classesNameSet.add( classes.getFullName());
                }
                studentSubject.setClassesSet( classesNameSet );
            }
            pageStudentSubjectList.add( studentSubject );
        }
        return pageStudentSubjectList;
    }

    /**
     * 查询每个学科下所有的作业和课堂
     * @return
     */
    private  List< StudentSubject > findSubjectExamList(List<StudentSubject> subjectList , Integer studentId ){
        List< StudentSubject > lastList = new ArrayList<StudentSubject>();
        if( null != subjectList ){
            for( int i = 0 ;i < subjectList.size();i++ ){
                //查询作业和课堂
                List<TpExam > examList = studentAllSubjectExamDao.findSubjectExamList( subjectList.get( i ).getSubjectId() ,
                                                                                        studentId,
                                                                                        CommonInstence.STATUS_4 ,
                                                                                        4 );
                long dateTime1 = 0L;
                //用于学科的排序（作业的结束时间）
                if(  null != examList && examList.size() > 0 ){
                    dateTime1 = null != examList.get( 0 ).getEndTime()?examList.get( 0 ).getEndTime().getTime():new Date().getTime();
                    int showButton = 0;
                    //遍历作业集合判断是否有作业，用于前端页判断是否要显示“组题自测按钮”
                    for( int j = 0 ;j< examList.size();j++ ){
                        Integer type = examList.get( j ).getType();
                        if( type == CommonInstence.EXAM_TYPE_1 || type == CommonInstence.EXAM_TYPE_3 || type == CommonInstence.EXAM_TYPE_4  ){
                            showButton = 1;
                            break;
                        }
                    }
                    if( showButton == 1 ){//需要显示按钮
                        subjectList.get( i ).setShowButton( 1 );
                    }else{
                        subjectList.get( i ).setShowButton( 0 );
                    }
                }
                subjectList.get( i ).setCreateDate( dateTime1 );
                if( null == examList ){
                    examList = new LinkedList<TpExam>( );
                }
                if( examList.size() > 0 ){
                    //查询作业的进度
                    examList = findExamProgress( examList , studentId );
                    subjectList.get( i ).setTpExamList( examList );
                    lastList.add( subjectList.get( i ) );
                }
                StudentSubject studentSubject = new StudentSubject();
                Collections.sort(lastList, studentSubject);
            }
        }
        return lastList;
    }
    /**
     * 查询作业进度
     * @param examList
     * @param studentId
     * @return
     */
    public List<TpExam > findExamProgress( List<TpExam > examList ,Integer studentId ){
        if( null != examList && examList.size() > 0 ){
            for( int  i = 0 ; i < examList.size();i++ ){
                Integer examId = examList.get( i ).getId();
                int type = examList.get( i ).getType();
                long endTimeLong = examList.get( i ).getEndTime().getTime();
                Integer status = 0;
                if( type == CommonInstence.EXAM_TYPE_1 || type == CommonInstence.EXAM_TYPE_3 || type == CommonInstence.EXAM_TYPE_4|| type == CommonInstence.EXAM_TYPE_20   ){//作业
                    //0,未开始 1.考试中 2.已交卷
                    List< Integer > statusList = studentAllSubjectExamDao.findExamProgress( examId , studentId );
                    if( null != statusList && statusList.size() > 0 ){
                        status = statusList.get( 0 );
                    }
                }else if( type == CommonInstence.EXAM_TYPE_2 ){//课堂
                    //分析课堂完成的进度
                    status = analyKeTangFinshNum( examId , studentId );
                }
                if( new Date().getTime() > endTimeLong ){
                    status = 2;
                }
                String dis = null;
                //获取进度的中文描述
                if( type == 1 || type == 3 || type == 4 || type == 20) {
                    dis = CommonEnum.getExamProgressEnum.getSource(status != null ? status : 0).getValue();
                }else if( type == 2 ){
                    dis = CommonEnum.getExamKeTangProgressEnum.getSource(status != null ? status : 0).getValue();
                }
                examList.get( i ).setProgressDis( dis );
                Date endTime = examList.get( i ).getEndTime();
                if( null != endTime ){
                    examList.get( i ).setEndTimeStr(DateFormatUtils.dateLFormat( endTime ));
                }
            }
        }
        return examList;
    }
    /**
     * 查询学生在在该课堂是否完成
     * @param examId
     * @param studentId
     * @return
     */
    public int analyKeTangFinshNum( int examId ,int studentId ){
        //查询每个课堂的附件个数
        List<TpExamAttachment> attachmentList = classesResourceDao.findAttachmentStudentList(examId, CommonInstence.STATUS_1 ,studentId);
        //先查询当前课堂下的所有作业 ---> 再查询跟这个学生有关的课堂作业
        List<TpExamAttachment > attachmentZuoyeListFirst = classesResourceDao.findExamAttachmentZuoye(examId, CommonInstence.STATUS_1);
        //再查询跟当天学生有关的课堂作业
        List<TpExamAttachment > attachmentZuoyeList = new ArrayList<TpExamAttachment>();//记录跟学生有关的作业
        if( null != attachmentZuoyeListFirst && attachmentZuoyeListFirst.size() > 0 ){
            for( int n = 0 ; n < attachmentZuoyeListFirst.size();n++ ){
                int examZuoYeId = attachmentZuoyeListFirst.get( n ).getAttachmentId();
                List<TpExamAttachment > attachmentStudentList = classesResourceDao.findStudentAttachementZuoye( examZuoYeId , CommonInstence.STATUS_1 , studentId );
                if( null != attachmentStudentList && attachmentStudentList.size() > 0 ){
                    attachmentZuoyeList.addAll( attachmentStudentList );
                }
            }
        }
        int readNum = 0 ;
        for( int i = 0 ; i < attachmentList.size();i++ ){
            int readLine = classesResourceDao.findStudentReadAttachmentLine( studentId , attachmentList.get( i ).getId() );
            if( 1 == readLine ){
                ++readNum;
            }
        }
        //是否完成课堂的所有作业
        int readZuoYeNum = 0;
        if( null != attachmentZuoyeList && attachmentZuoyeList.size() > 0 ){
            for( int m = 0 ; m < attachmentZuoyeList.size();m++ ){
                int readZuoyeLine = classesResourceDao.findExamZuoyeLine(studentId ,  attachmentZuoyeList.get( m ).getAttachmentId() ,
                                                                         CommonInstence.STATUS_2 );
                if( readZuoyeLine == 1 ){
                    ++readZuoYeNum;
                }
            }
        }
        //当只有课堂附件
        if( ( null != attachmentList && attachmentList.size() > 0 ) && (null == attachmentZuoyeList || attachmentZuoyeList.size() == 0) ) {
            //当前这个学生完成了所有的附件
            if (readNum == attachmentList.size() ) {
                return 2;
            }else if( readNum > 0 && readNum != attachmentList.size() ){
                return 1;
            }else{
                return 0;
            }
        }else if( ( null == attachmentList || attachmentList.size() == 0 ) && (null != attachmentZuoyeList && attachmentZuoyeList.size() > 0) ) {
            if( readZuoYeNum == attachmentZuoyeList.size() ){
                return 2;
            }else if( readZuoYeNum > 0 && readZuoYeNum != attachmentZuoyeList.size() ){
                return 1;
            }else{
                return 0;
            }
        }else if( ( null != attachmentList && attachmentList.size() > 0 ) && (null != attachmentZuoyeList && attachmentZuoyeList.size() > 0) ){
            if( readNum == attachmentList.size() && readZuoYeNum == attachmentZuoyeList.size()  ){
                return 2;
            }else if( readNum == 0 && readZuoYeNum == 0 ){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 0;
        }
    }















}
