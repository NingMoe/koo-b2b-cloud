package com.koolearn.cloud.teacherInfo.service.impl;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.teacher.entity.ExamNum;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacherInfo.dao.ClassesResourceDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fn on 2016/5/18.
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private ClassesResourceDao classesResourceDao;
    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }
    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }
    public ClassesResourceDao getClassesResourceDao() {
        return classesResourceDao;
    }
    public void setClassesResourceDao(ClassesResourceDao classesResourceDao) {
        this.classesResourceDao = classesResourceDao;
    }

    /**
     * 查询用户第一次加入班级的时间
     * @return
     */
    public Date findUserOutInClassDate( Integer userId ){
        Date date = classesResourceDao.findUserOutInClassDate( userId );
        return date;
    }

    /**
     * 查询班级人数
     * @param classesId
     * @return
     */
    public int findClassesNum( int classesId ){
        return teacherAddClassService.findClassesNum( classesId );
    }

    @Override
    public Map<String, Object> findLeftInfo(int classesId , int teacherId ,String subjectIdStr ,String rangeIdStr  ) {
        Map<String, Object> map =new HashMap<String, Object>();
        int rangeId = 0;
        int subjectId = 0;
        //左侧 查询班级信息
         Classes classes = teacherAddClassService.findClassesById( classesId );
        //查询班级人数
        int classesStudensNum = teacherAddClassService.findClassesNum( classesId );
        //查询任课老师信息
        List<ClassesTeacher > classesTeacherList = null;
        List<ClassesTeacher > classesTeacherLastList = new ArrayList<ClassesTeacher>();
        if( null != classes ){
            classesTeacherList =teacherAddClassService.findClassesTeacherByClassesId( classesId , classes.getRangeName());
            //如果是学科班，去除非本班学科以外的学科
            if( classes.getType().intValue() == 1 ){
                String subjectName = classes.getSubjectName();
                if( null != classesTeacherList && classesTeacherList.size() > 0 ){
                    for( int i = 0 ;i < classesTeacherList.size();i++ ){
                        List< String > subjectList = new ArrayList<String>();
                        subjectList.add( subjectName );
                        classesTeacherList.get( i ).setList( subjectList );
                        /*
                        List<String > subjectList = classesTeacherList.get( i ).getList();
                        if( null != subjectList ){
                            for( int j = 0 ; j < subjectList.size();j++ ){
                                if( subjectName.equals( subjectList.get( j ))){
                                    //subjectList.remove(subjectList.get( j ) );

                                }
                            }
                            if( 0 < subjectList.size() ){
                                classesTeacherLastList.add( classesTeacherList.get( i ) );
                            }

                        }*/
                    }
                    classesTeacherLastList = classesTeacherList;
                }
            }else{
                classesTeacherLastList = classesTeacherList;
            }
        }
        //查询老师下所有学科
        List<TeacherBookVersion> teacherBookVersionsList = null;
        if( classes != null && teacherId > 0 ){
            if( classes.getType().intValue() == 0 ) {// 如果是行政班，则根据学段名称和老师id查询所有学科
                //teacherBookVersionsList = teacherAddClassService.findTeacherBookVersion(teacherId, CommonInstence.STATUS_1);
                teacherBookVersionsList = teacherAddClassService.findTeacherBookVersionByRangeName(teacherId, classes.getRangeName() , CommonInstence.STATUS_1);
                if( null != teacherBookVersionsList && teacherBookVersionsList.size() > 0 ){
                    rangeId = teacherBookVersionsList.get( 0 ).getRangeId();
                    subjectId = teacherBookVersionsList.get( 0 ).getSubjectId();
                }
            }else{
                teacherBookVersionsList = teacherAddClassService.findTeacherBookVersionByRangeIdAndSubjectId( teacherId, classes.getRangeId(), classes.getSubjectId() ,CommonInstence.STATUS_1);
                rangeId = classes != null ? classes.getRangeId() : new Integer( rangeIdStr );
                subjectId = classes != null ? classes.getSubjectId():new Integer( subjectIdStr );
            }
        }
        map.put( "classes" , classes);
        map.put( "subjectId" , subjectId );
        map.put( "rangeId" , rangeId );
        map.put( "classesStudensNum" , classesStudensNum);
        map.put( "classesTeacherList" ,classesTeacherLastList );
        map.put( "teacherBookVersionsList" ,teacherBookVersionsList );
        return map;
    }

    /**
     * 分析课堂完成的情况
     */
    public int analyKeTangDoneNum( Integer examId ,Integer classesId , Integer teacherId  ){
        List<TpExamStudent> studentList = classesResourceDao.findKeTangStudents(examId, classesId, teacherId);
        int ketangDoneNum = 0;//课堂完成的人数
        //查询每个课堂的附件个数
        List<TpExamAttachment> attachmentList = classesResourceDao.findAttachmentList(examId, CommonInstence.STATUS_1);
        //再查询当前课堂下的所有作业
        List<TpExamAttachment > attachmentZuoyeList = classesResourceDao.findExamAttachmentZuoye(examId, CommonInstence.STATUS_1);
        log.info("课堂id:" + examId + "附件个数:" + attachmentList.size());
        //判断每个学生是否完成全部的附件
        for (int j = 0; j < studentList.size(); j++) {
            int readNum = 0;
            for (int n = 0; n < attachmentList.size(); n++) {
                int readLine = classesResourceDao.findStudentReadAttachmentLine(studentList.get(j).getStudentId(), attachmentList.get(n).getId());
                if (1 == readLine) {
                    ++readNum;
                }
            }
            //是否完成课堂的所有作业
            int readZuoYeNum = 0;
            if( null != attachmentZuoyeList && attachmentZuoyeList.size() > 0 ){

                for( int m = 0 ; m < attachmentZuoyeList.size();m++ ){
                    int readZuoyeLine = classesResourceDao.findExamZuoyeLine(studentList.get(j).getStudentId() ,
                            attachmentZuoyeList.get( m ).getAttachmentId() ,CommonInstence.STATUS_2 );
                    if( readZuoyeLine == 1 ){
                        ++readZuoYeNum;
                    }
                }
            }
            //当只有课堂附件
            if( ( null != attachmentList && attachmentList.size() > 0 ) && (null == attachmentZuoyeList || attachmentZuoyeList.size() == 0) ) {
                //当前这个学生完成了所有的附件
                if (readNum == attachmentList.size() ) {
                    ketangDoneNum++;
                }
            }else if( ( null == attachmentList || attachmentList.size() == 0 ) && (null != attachmentZuoyeList && attachmentZuoyeList.size() > 0) ) {
                if( readZuoYeNum == attachmentZuoyeList.size() ){
                    ketangDoneNum++;
                }
            }else if( ( null != attachmentList && attachmentList.size() > 0 ) && (null != attachmentZuoyeList && attachmentZuoyeList.size() > 0) ){
                if( readNum == attachmentList.size() && readZuoYeNum == attachmentZuoyeList.size()  ){
                    ketangDoneNum++;
                }
            }
        }
        return ketangDoneNum;
    }

    /**
     * 分析每个课堂作业完成的人数
     * @param examId
     * @param type
     * @param attachmentIds  : 所有examId 集合( 附件主键 和课堂作业主键)
     * @param examAttachmentList :主课堂下 的所有附件和课堂作业
     * @return
     */
    public int analyExamDoneNum( Integer examId ,int type ,String attachmentIds , List< TpExamAttachment > examAttachmentList ){
        int ketangDoneNum = 0;
        if( 1 == type ){//全部为附件
            //查询所有附件下所有学生id
            List<Integer> studentIdList = classesResourceDao.findExamStudents( examId ,CommonInstence.STATUS_1 );
            //查询每个课堂的附件个数
            List<TpExamAttachment> attachmentList = classesResourceDao.findAttachmentList(examId, CommonInstence.STATUS_1);
            for (int j = 0; j < studentIdList.size(); j++) {
                int fuJianNum = 0;
                for (int n = 0; n < attachmentList.size(); n++) {
                    int readLine = classesResourceDao.findStudentReadAttachmentLine(studentIdList.get(j) , attachmentList.get(n).getId());
                    if (1 == readLine) {
                        fuJianNum++;
                    }
                }
                if( fuJianNum == attachmentList.size() && attachmentList.size() > 0 ){
                    ketangDoneNum++;
                }
            }
        }else if(2 == type ) {//全部为课堂作业
            // < studentId , num >
            List< ExamNum > allStudentList = new ArrayList<ExamNum>();
            String[] array = attachmentIds.split( "," );
            if( null != array && array.length > 0 ){
                for( int m = 0 ; m < array.length;m++ ){
                    if(StringUtils.isNotBlank( array[ m ] )){
                        List<ExamNum> studentList = classesResourceDao.findExamKeTangStudents( array[ m ] );
                        if( null != studentList && studentList.size() > 0 ){
                            allStudentList.addAll( studentList );
                        }
                    }
                }
            }
            for (ExamNum examNum : allStudentList ) {
                Integer num = classesResourceDao.findStudentDoneNumByExamId( attachmentIds , examNum.getStudentId() );
                Integer numDone = examNum.getNum();
                if( null != num && num.intValue() == numDone.intValue() ){
                    ketangDoneNum++;
                }
            }
        }else{//有课堂作业和附件
            //全部学生
            List<Integer> studentIdList = classesResourceDao.findExamStudents( examId ,CommonInstence.STATUS_1 );
            //先查询课堂作业完成的数量
            StringBuffer keTangAttachmentIds = new StringBuffer();
            for( int i = 0 ;i < examAttachmentList.size();i++ ){
                if( examAttachmentList.get( i ).getAttachmentType().intValue() == 2 ){//课堂作业
                    keTangAttachmentIds.append( examAttachmentList.get( i ).getAttachmentId() );
                    keTangAttachmentIds.append(",");
                }
            }
            String attachmenttIds =  keTangAttachmentIds.toString().substring( 0 , keTangAttachmentIds.toString().length()-1 );
            List<ExamNum> studentList = classesResourceDao.findExamKeTangStudents( attachmenttIds );
            //查询每个课堂的附件个数
            List<TpExamAttachment> attachmentList = classesResourceDao.findAttachmentList(examId, CommonInstence.STATUS_1);
            for (ExamNum examNum : studentList ) {
                //删除做过作业的学生用于判断附件的完成情况
                for( int m = 0 ;m <  studentIdList.size();m++ ){
                    if( examNum.getStudentId() == studentIdList.get( m ).intValue() ){
                        studentIdList.remove( m );
                    }
                }
                Integer num = classesResourceDao.findStudentDoneNumByExamId( attachmentIds , examNum.getStudentId() );
                Integer numDone = examNum.getNum();
                if( null != num && num.intValue() == numDone.intValue() ){
                    //再判断是否完成所有附件
                    for (int n = 0; n < attachmentList.size(); n++) {
                        int readLine = classesResourceDao.findStudentReadAttachmentLine( examNum.getStudentId() , attachmentList.get(n).getId());
                        if (1 == readLine) {
                            ketangDoneNum++;
                        }
                    }
                }
            }
            //再判断剩下的人是否有完成附件的
            if( null != studentIdList && studentIdList.size() > 0 ){
                for( int j = 0 ; j < studentIdList.size();j++ ){
                    for (int n = 0; n < attachmentList.size(); n++) {
                        int readLine = classesResourceDao.findStudentReadAttachmentLine(studentIdList.get(j) , attachmentList.get(n).getId());
                        if (1 == readLine) {
                            ketangDoneNum++;
                        }
                    }
                }
            }
        }
        return ketangDoneNum;
    }

}
