package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.ClassesDynamic;
import com.koolearn.cloud.common.entity.Pager;

import java.util.List;

/**
 * Created by fn on 2016/4/13.
 */
public interface ClassNewStatusService {
    /**
     * 查询班级动态
     * @param classesId
     * @return
     */
    Pager findClassesDynamicByClassId(Integer classesId ,int pageSize , int pageNo );

    /**
     * 更新老师下关于当前班级的所有动态状态
     */
    void updateTeacherDynamicStatus(Integer classesId, Integer teacherId ) throws Exception;
}
