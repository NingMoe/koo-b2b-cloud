package com.koolearn.cloud.teacher.service;


import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.teacher.entity.AreaCity;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.RangeSubject;
import com.koolearn.cloud.teacher.entity.TeacherInfo;
import com.koolearn.cloud.util.PagerBean;
import com.koolearn.exam.base.service.BaseService;
import com.koolearn.klb.tags.entity.Tags;

import java.util.List;

/**
 * Created by dongfangnan on 2016/3/29.
 */
public interface TeacherDataMaintainService extends BaseService {

    /**
     * 查询所有省份
     * @return
     */
    public List<Location> findProvinceList();

    /**
     * 查询省份下所有城市
     * @param provinceId
     * @return
     */
    public List< Location > findCityByProvinceIdList(int provinceId);

    /**
     * 查询城市下所有学校
     * @param cityId
     * @return
     */
    public PagerBean findSchoolByCityIdList(int cityId  ,int pageNo ,int pageSize , String schoolName );

    /**
     * 查询学校的总个数
     * @param cityId
     * @return
     */
    int findSchoolPageLine(int cityId);

    /**
     * 验证教师完善的数据
     * @param teacherBookVersion
     * @return
     */
    boolean checkTeacherDataIsOk(TeacherInfo teacherBookVersion );

    /**
     * 添加老师信息
     * @param teacherBookVersion
     * @return
     */
    TeacherInfo addTeacherInfo(TeacherInfo teacherBookVersion);

    /**
     *查询所有学段下面的所有学科
     * @return
     */
    List<RangeSubject> findAllSubject();

    /**
     * 查询城市下所有区县
     * @param cityId
     * @return
     */
    List<AreaCity> findAreaByCityId(Integer cityId);

    /**
     * 查询老师
     * @param teacherId
     * @return
     */
    public User findUserInfo( int teacherId );

    Object showResult(String sql);

}
