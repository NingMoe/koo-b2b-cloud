package com.koolearn.cloud.operation.service;

import com.koolearn.cloud.operation.entity.SchoolFilter;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.School;

import java.util.List;

public interface OperationService {

    public List<Location> findLocationList(Integer parentId);

    /***
     * 学校数据字典列表
     * @param pager
     * @return
     */
    SchoolFilter searchSchoolList(SchoolFilter pager);
    List<School> searchSchoolData(SchoolFilter pager) ;

     Long searchSchoolDataCount(SchoolFilter pager) ;
    public List<Location> getAllLocation();

    boolean addUpdateSchool(SchoolFilter pager);

    boolean deleteSchool(SchoolFilter pager);

    boolean blockSchool(SchoolFilter pager);

    String schoolIsExist(String name);
}
