package com.koolearn.cloud.operation.service.impl;

import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.operation.dao.OperationDao;
import com.koolearn.cloud.operation.entity.SchoolFilter;
import com.koolearn.cloud.operation.service.OperationService;
import com.koolearn.cloud.teacher.entity.Location;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationServiceImpl implements OperationService {

    private static final Logger logger = Logger.getLogger(OperationServiceImpl.class);
    @Autowired
    private OperationDao operationDao;

    /**
     * 省市县借口  parentId=0时查询省
     * @param parentId
     * @return
     */
    @Override
    public List<Location> findLocationList(Integer parentId) {
        return operationDao.findLocationList(parentId);
    }

    @Override
    public SchoolFilter searchSchoolList(SchoolFilter pager) {
        parseSchoolFilterLocationId(pager);
        Long count = this.searchSchoolDataCount(pager);
        pager.setTotalRows(count);
        pager.setPageNo(pager.getPageNo());//随机页数
        pager.setPageSize(GlobalConstant.PAGER_SIZE_DEFAULT_20);//每页
        List<School> listBysearch = this.searchSchoolData(pager);
        listBysearch=parseSchoolData(listBysearch);
        pager.setResultList(listBysearch);
        return pager;
    }

    private void parseSchoolFilterLocationId(SchoolFilter pager) {
        List<Location> allLocation=getAllLocation();
        List<Integer> searchLocation=new ArrayList<Integer>();

        if(pager.getProvince()==null){
            searchLocation=null;  //如果省为空，则查询所有地区
        }else{
            if(pager.getCity()==null){
                //拿到所有市区下的所有县级地区
                List<Location> cityList=findChildLocalhost(pager.getProvince(),allLocation);
                for(Location city:cityList){
                    List<Location> countyList=findChildLocalhost(city.getId(),allLocation);
                    for(Location county:countyList){
                        searchLocation.add(county.getId());
                    }
                }

            }else {
                if(pager.getCounty()==null){
                    List<Location> countyList=findChildLocalhost(pager.getCity(),allLocation);
                    for(Location county:countyList){
                        searchLocation.add(county.getId());
                    }
                }else {
                    searchLocation.add(pager.getCounty());
                }
            }

        }
        pager.setLocationIdList(searchLocation);
    }

    private List<School> parseSchoolData(List<School> listBysearch) {
        if(listBysearch!=null&&listBysearch.size()>0){
            List<Location> allLocation=getAllLocation();
            for (School school:listBysearch){
                //设置列表地区显示['18','181','183']
                getAreaFull(school,allLocation );
                //设置学段
                if(StringUtils.isNotBlank(school.getGradeId())){
                    String[] gradeIds=school.getGradeId().split(",");
                    List<String> rangeList=new ArrayList<String>();
                    for(String gid:gradeIds){
                        rangeList.add(gid + "_" + CommonInstence.getGradeName(Integer.parseInt(gid)));
                    }
                    school.setRange(rangeList);
                }

            }
        }
        return listBysearch;
    }

    /**
     * 获取地区表数据
     * @return
     */
    @Override
    public List<Location> getAllLocation(){
        List<Location> allLocation =CacheTools.getCache(CommonInstence.LOCATION_ALL_DATA_KEY, List.class);
        if(allLocation==null){
            allLocation =  operationDao.findAllLocationList();
            CacheTools.addCache(CommonInstence.LOCATION_ALL_DATA_KEY, ConstantTe.CACHE_TIME, allLocation);
        }
        return allLocation;
    }

    @Override
    public boolean addUpdateSchool(SchoolFilter pager) {
        try {
            if(pager.getId()==null){
                //新增学校
                School school=new School();
                school.setName(pager.getName());
                school.setShortname(pager.getShortname());
                school.setGradeId(StringUtils.join(pager.getGrades(), ","));
                school.setLocationId(pager.getCounty());
                school.setEntityStatus(CommonInstence.SCHOOL_ENTITY_STATUS_OK);
                school.setUpdateTime(new Date());
                school.setCreateTime(new Date());
                school.setAdder("新东方");
                operationDao.saveSchool(school);

            }else {
                //修改
                School school=operationDao.findSchoolById(pager.getId());
                school.setName(pager.getName());
                school.setShortname(pager.getShortname());
                school.setGradeId(StringUtils.join(pager.getGrades(), ","));
                school.setLocationId(pager.getCounty());
                school.setUpdateTime(new Date());
                school.setAdder("新东方");
                operationDao.updateSchool(school);
            }
            return true;
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public boolean deleteSchool(SchoolFilter pager) {
        School school=operationDao.findSchoolById(pager.getId());
        school.setEntityStatus(CommonInstence.SCHOOL_ENTITY_STATUS_DEL);
        school.setUpdateTime(new Date());
        operationDao.updateSchool(school);
        return true;
    }

    @Override
    public boolean blockSchool(SchoolFilter pager) {
        School school=operationDao.findSchoolById(pager.getId());
        if(StringUtils.isBlank(pager.getCancel())){
            school.setEntityStatus(CommonInstence.SCHOOL_ENTITY_STATUS_PINGBI);
        }else {
            school.setEntityStatus(CommonInstence.SCHOOL_ENTITY_STATUS_OK);
        }
        school.setUpdateTime(new Date());
        operationDao.updateSchool(school);
        return true;
    }

    @Override
    public String schoolIsExist(String name) {
        int  num=operationDao.schoolNumByName(name);
        if(num>0){
            return "{\"isExist\":true}";
        }
        return "{\"isExist\":false}";
    }

    /**
     * 处理学校地区信息
     * @param school
     * @param allLocation
     */
    private void getAreaFull(School school,List<Location> allLocation) {
        List<String> areaIdList=new ArrayList<String>();
        Location areaId3=getArea(  school.getLocationId(),  allLocation);
        Location areaId2=getArea(  areaId3.getParentId(),  allLocation);
        Location areaId1=getArea(  areaId2.getParentId(),  allLocation);
        String proviceName="",cityName="",countyName="";
        if(areaId1!=null){
            areaIdList.add(areaId1.getId().toString());
            proviceName=areaId1.getName();
        }
        if(areaId2!=null){
            areaIdList.add(areaId2.getId().toString());
            cityName=areaId2.getName();
        }
        if(areaId3!=null){
            areaIdList.add(areaId3.getId().toString());
            countyName=areaId3.getName();
        }
        school.setArea(areaIdList); //地区学段
        school.setAreaName(StringUtils.isBlank(cityName)?countyName:proviceName+cityName);
    }

    private Location getArea(Integer locationId, List<Location> allLocation) {
        for (Location l:allLocation){
             if(l.getId().equals(locationId)){
                 return l;
             }
        }
        return null;
    }
    private  List<Location> findChildLocalhost(Integer parentId, List<Location> allLocation){
        List<Location> child=new ArrayList<Location>();
        for (Location c:allLocation){
            if(c.getParentId()!=null&&c.getParentId().intValue()==parentId.intValue()){
                child.add(c);
            }
        }
        return child;
    }

    @Override
    public List<School> searchSchoolData(SchoolFilter pager) {
         return this.operationDao.searchSchoolData(pager);
    }
    @Override
    public Long searchSchoolDataCount(SchoolFilter pager) {
        return this.operationDao.searchSchoolDataCount(pager);
    }
}
