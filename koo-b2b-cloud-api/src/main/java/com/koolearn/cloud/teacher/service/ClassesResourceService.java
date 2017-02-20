package com.koolearn.cloud.teacher.service;

import com.koolearn.cloud.common.entity.Pager;

/**
 * Created by fn on 2016/4/14.
 */
public interface ClassesResourceService {

    /**
     * 查询班级资源
     * @param classesIdInt
     * @param teacherId
     * @param typeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Pager findExamByClassesIdAndTeacherId(int classesIdInt,int teacherId ,String typeId ,int pageNo ,int pageSize );
}
