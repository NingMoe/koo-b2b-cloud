package com.koolearn.cloud.student;

import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.student.entity.StudentSubject;

import java.util.List;

/**
 * 学生登录显示所有作业和课堂所属的学科及相关信息
 * Created by fn on 2016/5/24.
 */
public interface StudentAllSubjectExamService {

    /**
     * 查询该学生所有留过的作业和课堂的学科
     * @param studentId
     * @param pageNo
     * @return
     */
    Pager findAllSubjectsByExam(Integer studentId ,Integer pageNo ) throws Exception;
}
