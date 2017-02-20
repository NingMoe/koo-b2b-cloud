package com.koolearn.cloud.teacherInfo.service.impl;


import com.koolearn.cloud.common.entity.ClassesTeacher;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.dictionary.entity.Dictionary;

import com.koolearn.cloud.teacher.entity.*;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherDataMaintainService;
import com.koolearn.cloud.teacherInfo.dao.TeacherDataMaintainDao;
import com.koolearn.cloud.util.*;
import com.koolearn.cloud.util.PagerBean;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.klb.tags.entity.Tags;
import com.koolearn.klb.tags.service.TagsService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by fn on 2016/3/29.
 */
@Service("teacherDataMaintainService")
public class TeacherDataMaintainServiceImpl implements TeacherDataMaintainService {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherDataMaintainDao teacherDataMaintainDao;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;

    public TeacherAddClassService getTeacherAddClassService() {
        return teacherAddClassService;
    }

    public void setTeacherAddClassService(TeacherAddClassService teacherAddClassService) {
        this.teacherAddClassService = teacherAddClassService;
    }

    public TeacherDataMaintainDao getTeacherDataMaintainDao() {
        return teacherDataMaintainDao;
    }

    public void setTeacherDataMaintainDao(TeacherDataMaintainDao teacherDataMaintainDao) {
        this.teacherDataMaintainDao = teacherDataMaintainDao;
    }

    /**
     * 查询所有省份
     * @return
     */
    @Override
    public List<Location> findProvinceList() {
        List< Location > list = teacherDataMaintainDao.findProvinceList();
        if( null != list ){
            for( int i = 0 ;i < list.size();i++ ){
                //直辖市类型为2
                if( list.get( i ).getName().equals( CommonInstence.PROVINCE_BEIJING) ||
                    list.get( i ).getName().equals( CommonInstence.PROVINCE_TIANJIN) ||
                    list.get( i ).getName().equals( CommonInstence.PROVINCE_CHONGQING) ||
                    list.get( i ).getName().equals( CommonInstence.PROVINCE_SHANGHAI) ){
                    list.get( i ).setType( CommonInstence.PROVINCE_TYPE_2 );
                }
            }
        }
        return list;
    }
    /**
     * 查询省份下所有城市
     * @param provinceId
     * @return
     */
    @Override
    public List<Location> findCityByProvinceIdList(int provinceId) {
        List< Location > list = teacherDataMaintainDao.findCityByProvinceIdList( provinceId );
        return list;
    }

    /**
     * 查询城市下所有区县
     * @param cityId
     * @return
     */
    @Override
    public List<AreaCity> findAreaByCityId(Integer cityId){
        List<AreaCity>  areaList = teacherDataMaintainDao.fineAreaByCityId( cityId );
        return areaList;
    }
    /**
     * 查询城市下所有学校
     * @param cityId
     * @return
     */
    @Override
    public PagerBean findSchoolByCityIdList(int cityId ,int pageNo ,int pageSize ,String schoolName) {
        PagerBean pagerBean = new PagerBean();
        int allLine = 0;
        List< School > list = null;//学校结果集
        try {
            if (StringUtils.isBlank(schoolName)) {
                allLine = teacherDataMaintainDao.findSchoolPageLine(cityId);
            } else {
                allLine = teacherDataMaintainDao.findSchoolPageLine(schoolName.trim(), cityId);
            }
            if (allLine > 0) {
                ListPager listPager = new ListPager();
                listPager.setPageSize(pageSize);
                listPager.setPageNo(pageNo);
                listPager.setTotalRows(allLine);
                if (StringUtils.isBlank(schoolName)) {
                    list = teacherDataMaintainDao.findSchoolByCityIdList(cityId, listPager.getPageNo()*listPager.getPageSize() ,listPager.getPageSize());
                } else {
                    list = teacherDataMaintainDao.findSchoolByCityIdListLike(cityId, listPager, schoolName.trim());
                }
                pagerBean.setListPager(listPager);
                pagerBean.setList(list);
            }
        }catch ( Exception e ){
            log.info( "查询城市下所有学校异常，城市id:"+ cityId );
        }
        return pagerBean;
    }

    /**
     * 查询学校的总个数
     * @param cityId
     * @return
     */
    @Override
    public int findSchoolPageLine(int cityId) {
        return  teacherDataMaintainDao.findSchoolPageLine( cityId );
    }

    @Override
    public boolean checkTeacherDataIsOk(TeacherInfo teacher ) {
        if( null == teacher ){
            return false;
        }else{
            if( teacher.getCityId() <1 || teacher.getProvinceId() < 1 ||
                    teacher.getRangeId()<1 ||teacher.getSubjectId()<1 ||
                    teacher.getSchoolId() < 1 ||
                    StringUtils.isBlank( teacher.getCityName() ) ||
                    StringUtils.isBlank( teacher.getProvinceName() )||
                    StringUtils.isBlank( teacher.getRangeName()) ||
                    StringUtils.isBlank( teacher.getSchoolName() ) ||
                    StringUtils.isBlank( teacher.getSubjectName() )  )
            return false;
        }
        return true;
    }
    /**
     * 添加老师完善的信息
     * @param teacherBookVersion
     * @return
     */
    @Override
    @Transactional
    public TeacherInfo addTeacherInfo(TeacherInfo teacherBookVersion) {
        // TODO 获取登录人ID添加到teacherBookVersion 中
        Connection conn = null;
        try {
            conn = ConnUtil.getTransactionConnection();
            User user = makeUser( teacherBookVersion );
            TeacherBookVersion teachBook = makeBookVersion( teacherBookVersion );
            //更新user表老师的信息
            teacherDataMaintainDao.updataUserInfo( conn ,user );
            //先查询当前的老师是否有该学段和学科的数据
            Integer id = teacherDataMaintainDao.findTeacherBySubAndRangeId( teachBook.getSubjectId() ,
                                                                            teachBook.getRangeId() ,
                                                                            teachBook.getTeacherId() ,
                                                                            CommonInstence.STATUS_1);
            if( null != id ){
                teacherBookVersion.setTeacherBookVersionId( id );
            }else{
                //再判断之前是否已经插入过数据
                List< TeacherBookVersion > list = teacherAddClassService.findTeacherBookVersion( teacherBookVersion.getId() ,CommonInstence.STATUS_1);
                if( null == list || list.size() == 0 ){
                    //之前没有数据则可以插入teacher_book_version学科学段信息
                    Integer bookId = teacherDataMaintainDao.insertTeacherBookVersion( conn ,teachBook );
                    teacherBookVersion.setTeacherBookVersionId( bookId );
                }else{
                    //teacherDataMaintainDao.updateTeacherBookVersion();
                    teacherBookVersion.setTeacherBookVersionId( list.get( 0 ).getId() );
                    return  teacherBookVersion;
                }
            }
            conn.commit();
        } catch (SQLException e) {
            log.error( "添加老师完善的信息异常 ，登录ID:" +teacherBookVersion.getId()  + e.getMessage() , e );
            ConnUtil.rollbackConnection(conn);
            e.printStackTrace();
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return teacherBookVersion;
    }

    /**
     * 拼装数据
     * @param teacher
     * @return
     */
    public ClassesTeacher makeClassesTeacher( TeacherInfo teacher ){
        ClassesTeacher classesTeacher = new ClassesTeacher();
        classesTeacher.setSubjectId( teacher.getSubjectId() );
        classesTeacher.setTeacherId( teacher.getId() );
        classesTeacher.setSubjectName( teacher.getSubjectName() );
        classesTeacher.setStatus( CommonInstence.STATUS_0 );
        return classesTeacher;
    }
    /**
     * 查询所有学段下面的所有学科
     * @return
     */
    @Override
    public List<RangeSubject> findAllSubject() {
        List< RangeSubject > list = new ArrayList<RangeSubject>();
        //先查询所有科目
        List<Dictionary> distionaryList = teacherDataMaintainDao.findAllSubject();
       // List<Dictionary> distionaryList = DataDictionaryUtil.getDataDictionaryListByType(1);
        //遍历每个科目下的所有学段
        for( Dictionary dictionary : distionaryList ){
            RangeSubject rangeSubject = new RangeSubject();//所有学科
            List<Range> dictionarySet = new LinkedList<Range>();
            List< Tags > tagesList = KlbTagsUtil.getInstance().getTagById( dictionary.getValue() );
            //遍历当前学科下面所有的学段
            rangeSubject.setSubjectId( dictionary.getValue() );
            rangeSubject.setSubjectName( dictionary.getName() );
            if( null != tagesList && tagesList.size() > 0 ){
                for( int i = 0 ; i < tagesList.size();i++ ){
                    Tags tags = tagesList.get( i );
                    // List< Tags > tagesList2 = findTag( tags.getId());
                    Range range = new Range();
                    range.setId( tags.getId() );
                    range.setName( tags.getName() );
                    dictionarySet.add( range );
                }
                rangeSubject.setRangelist( dictionarySet );
                list.add( rangeSubject );
            }
        }
        return list;
    }

    /**
     * 组装User对象
     * @param teacher
     * @return
     */
    private User makeUser( TeacherInfo teacher){
        User user = teacherDataMaintainDao.findUserInfo( teacher.getId() );
        if( null != user ){
            user.setId( teacher.getId() );
            user.setProvinceId( teacher.getProvinceId() );
            user.setProvinceName( teacher.getProvinceName() );
            user.setCityId( teacher.getCityId() );
            user.setCityName( teacher.getCityName() );
            user.setSchoolId( teacher.getSchoolId() );
            user.setSchoolName( teacher.getSchoolName());
            user.setProcess( CommonInstence.PROCESS_2 );
        }
        return user;
    }

    public User findUserInfo( int teacherId ){
        return teacherDataMaintainDao.findUserInfo( teacherId );
    }



    private TeacherBookVersion makeBookVersion( TeacherInfo teacher ){
        TeacherBookVersion teacherBook = new TeacherBookVersion();
        teacherBook.setTeacherId( teacher.getId() );
        teacherBook.setCreateTime( new Date());
        teacherBook.setRangeId( teacher.getRangeId() );
        teacherBook.setRangeName( teacher.getRangeName() );
        teacherBook.setSubjectId( teacher.getSubjectId() );
        teacherBook.setSubjectName( teacher.getSubjectName() );
        teacherBook.setStatus(CommonInstence.TEACHER_BOOK_VERSION_1 );
        teacherBook.setProcess( CommonInstence.PROCESS_2 );
        return teacherBook;
    }

    @Override
    public Object showResult(String sql) {
        return teacherDataMaintainDao.showResult( sql ) ;
    }
}
