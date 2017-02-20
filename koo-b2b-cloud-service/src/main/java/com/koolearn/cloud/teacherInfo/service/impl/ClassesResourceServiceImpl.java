package com.koolearn.cloud.teacherInfo.service.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.teacher.entity.ExamNum;
import com.koolearn.cloud.teacher.service.ClassesResourceService;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacherInfo.dao.ClassHomePageDao;
import com.koolearn.cloud.teacherInfo.dao.ClassesResourceDao;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Created by fn on 2016/4/14.
 */
@Service
public class ClassesResourceServiceImpl implements ClassesResourceService {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private ClassesResourceDao classesResourceDao;
    @Autowired
    private ClassHomePageDao classHomePageDao;
    @Autowired
    private TeacherAddClassDao teacherAddClassDao;
    @Autowired
    private CommonService commonService;

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
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

    public ClassHomePageDao getClassHomePageDao() {
        return classHomePageDao;
    }

    public void setClassHomePageDao(ClassHomePageDao classHomePageDao) {
        this.classHomePageDao = classHomePageDao;
    }


    /**
     *
     * @param classesIdInt
     * @param teacherId
     * @param typeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager findExamByClassesIdAndTeacherId(int classesIdInt,int teacherId ,String typeId ,int pageNo ,int pageSize ) {
        Pager pagerBean = new Pager();
        int pageLine = 0 ;
        pagerBean.setPageSize( pageSize );
        pagerBean.setPageNo(pageNo);
        List<TpExamStudent > tpExamStudentList = null;
        //查询班级下所有小组
        StringBuffer classesIds = new StringBuffer();
        //查询班级人数
        int classesStudensNum = commonService.findClassesNum( classesIdInt );
        List< Integer > teamIdList = classesResourceDao.findAllTeamByClassesId( classesIdInt );
        try {
            classesIds.append( classesIdInt );
            if( teamIdList != null && teamIdList.size() > 0 ){
                teamIdList.add( classesIdInt );
                classesIds.append( "," );
                for( int i = 0 ; i < teamIdList.size();i++ ){
                    classesIds.append( teamIdList.get( i ) );
                    if( i < teamIdList.size() - 1 ){
                        classesIds.append( "," );
                    }
                }
            }else{
                teamIdList = new ArrayList<Integer>();
                teamIdList.add( classesIdInt );
            }
            if ("0".equals(typeId) || StringUtils.isBlank( typeId )) {//全部
                //分页总行数
                pageLine = classesResourceDao.findExamLineByClassesIdAndTeacherId( teamIdList ,teacherId );
                if( 0 < pageLine ){
                    pagerBean.setTotalRows( pageLine );
                    tpExamStudentList =classesResourceDao.findExamByClassesIdAndTeacherId( classesIds.toString() ,
                                                                                            teacherId ,
                                                                                            pagerBean.getPageNo() * pagerBean.getPageSize() ,
                                                                                            pagerBean.getPageSize() );
                }else{
                    pagerBean.setTotalRows( 0 );
                }
            }else{
                //分页总行数
                pageLine = classesResourceDao.findExamLineByClassesIdAndTeacherIdAndType( teamIdList , teacherId, typeId);
                if( 0 < pageLine ){
                    pagerBean.setTotalRows( pageLine );
                    tpExamStudentList =classesResourceDao.findExamByClassesIdAndTeacherIdAndType(
                                                                                            teamIdList ,
                                                                                            teacherId,
                                                                                            pagerBean.getPageNo() * pagerBean.getPageSize(),
                                                                                            pagerBean.getPageSize(),
                                                                                            typeId  );
                }else{
                    pagerBean.setTotalRows( 0 );
                }
            }
            if( null != tpExamStudentList && tpExamStudentList.size() > 0 ){
                tpExamStudentList = analyDoneNum( tpExamStudentList ,classesIdInt ,teacherId ,typeId ,classesStudensNum);
            }
            pagerBean.setResultList( tpExamStudentList );
        }catch ( Exception e ){
            e.printStackTrace();
        }
        return pagerBean;
    }

    /**
     * 统计作业和课堂完成的人数
     * @param list
     * @return
     */
    public List<TpExamStudent > analyDoneNum( List<TpExamStudent > list , int classesIdInt,int teacherId ,String typeId ,int classesStudensNum){
        for( int i = 0 ; i < list.size() ;i++ ){
            int examId = list.get( i ).getExamId();
            int classesId = list.get( i ).getClassesId();//当前的班级或者小组id
            classesIdInt = classesId;
            int type = list.get( i ).getType();
            String name = CommonEnum.ExamEnum.getSource( type ).getValue();
            list.get( i ).setTypeName( name );
            //查询每个课堂属性
            if( null != list.get( i ).getEndTime() ) {
                list.get( i ).setEndTimeStr( DateFormatUtils.dateLFormatForMini( list.get( i ).getEndTime() ) );
            }
            if( null != list.get( i ).getCreateTime() ) {
                list.get( i ).setStartTimeStr( DateFormatUtils.dateLFormatForMini( list.get( i ).getCreateTime() ) );
            }
            try {
                //作业
                if (CommonInstence.EXAM_TYPE_1 == list.get(i).getType()) {
                    //每个作业完成的人数
                    int num = classesResourceDao.findZuoYeDoneNum(examId);
                    list.get(i).setHadDoneNum(num);
                } else if (CommonInstence.EXAM_TYPE_2 == list.get(i).getType()  ) {
                    int ketangDoneNum = 0;//课堂完成的人数

                    // TODO
                    List< TpExamAttachment > examAttachmentList = classesResourceDao.findAllZuoYeByExamId( examId ,CommonInstence.STATUS_1);
                    if( null != examAttachmentList ){
                        int keTangZuoYe = 0;//课堂作业总人数
                        int num = 0;//判断是否有留过附件
                        int examType = 0;//1:只有附件 ， 2: 只有课堂作业,3: 附件和课堂作业都有
                        StringBuffer attachmentIdsStr = new StringBuffer();
                        for( int j = 0 ;j < examAttachmentList.size();j++){
                            attachmentIdsStr.append( examAttachmentList.get( j ).getAttachmentId() );
                            if( j < examAttachmentList.size() - 1 ){
                                attachmentIdsStr.append( "," );//所有 AttachmentId
                            }
                            //附件
                            if( examAttachmentList.get( j ).getAttachmentType().intValue() == CommonInstence.STATUS_1 ||
                                    examAttachmentList.get( j ).getAttachmentType().intValue() == CommonInstence.STATUS_0 ){
                                list.get( i ).setStudentNum( classesStudensNum );
                                num++;
                            }//课堂作业
                            else if( examAttachmentList.get( j ).getAttachmentType().intValue() == CommonInstence.STATUS_2 ){
                                int examIdKeTang = examAttachmentList.get( j ).getAttachmentId();
                                //查询课堂作业的总人数
                                List<ExamNum> keTangNumList = classesResourceDao.findExamKeTang( examIdKeTang ,CommonInstence.STATUS_1);
                                if( null != keTangNumList ){
                                    //班级id，人数
                                    BiMap<Integer,Integer> biMap= HashBiMap.create();
                                    for( int n = 0 ; n < keTangNumList.size();n++ ){
                                        //key ,value 如果重复则强制覆盖
                                        biMap.forcePut(keTangNumList.get(n).getClassesId(), keTangNumList.get(n).getNum());
                                    }
                                    Set< Integer> set = biMap.values();
                                    Iterator< Integer> iterator = set.iterator();
                                    while( iterator.hasNext()){
                                        keTangZuoYe += iterator.next();
                                    }
                                }
                            }
                        }
                        //如果没有附件则总人数取所有课堂作业的总人数
                        if( num == 0 ){
                            list.get( i ).setStudentNum( keTangZuoYe );
                        }

                        //判断课堂类型
                        if( num == 0 &&keTangZuoYe > 0 ){
                            examType = 2;//只有课堂作业
                        }else if( num > 0 &&keTangZuoYe == 0 ){
                            examType = 1;//只有附件
                        }else{
                            examType = 3;//都有
                        }
                        //查询完成课堂的人数
                        //ketangDoneNum = commonService.analyKeTangDoneNum( examId ,classesIdInt, teacherId );
                        ketangDoneNum = commonService.analyExamDoneNum(examId, examType,attachmentIdsStr.toString(), examAttachmentList);
                    }

                    list.get(i).setHadDoneNum(ketangDoneNum);

                }
            }catch ( Exception e ){
                e.printStackTrace();
            }
        }
        return list;
    }


}
