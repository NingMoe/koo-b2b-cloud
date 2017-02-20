package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.task.entity.TpExamStudent;

import java.util.List;
import java.util.Map;

/**
 * 为公共组件提供通用的功能操作
 * Created by fn on 2016/5/18.
 */
public interface CommonService {
    /**
     * 查询左侧页面信息
     * @param classesId
     * @param teacherId
     * @param subjectId
     * @return
     */
    public Map< String , Object >  findLeftInfo( int classesId , int teacherId ,String subjectId ,String rangeId  );

    /**
     * 分析课堂完成的情况
     * @param examId
     * @param classesId
     * @return
     */
    public int analyKeTangDoneNum( Integer examId ,Integer classesId , Integer teacherId );

    /**
     * 查询班级人数
     * @param classesId
     * @return
     */
    public int findClassesNum( int classesId );
    /**
     * 分析每个课堂作业完成的人数
     * @return
     */
    public int analyExamDoneNum( Integer examId ,int type ,String attachmentIds , List<TpExamAttachment> examAttachmentList );
}
