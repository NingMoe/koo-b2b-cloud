package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.entity.TeacherBookVer;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.util.BizException;

import com.koolearn.cloud.util.KlbTagsUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 老师创建班级
 * Created by fn on 2016/4/5.
 */
@RequestMapping("/teacher/addClass")
@Controller
public class TeacherAddClassController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LoginService loginService;
    /**
     * 根据学段id和学科id查询对应的所有班级
     * @param request
     * @return
     * http://cloud.trunk.koolearn.com/addClass/findTeacherSubjects?rangeId=243669&rangeName=初中
     */
    //@ResponseBody
    @RequestMapping(value ="/findAllClassByRangeSub"  , method = RequestMethod.POST )
    public String findAllClassByRangeSub( HttpServletRequest request ,ModelMap model ,UserEntity userEntity) {
        String rangeId = request.getParameter("rangeId");
        String subjectId = request.getParameter("subjectId");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Classes> classesList = null;//所有班级列表
        List<TeacherBookVersion> bookList = null;//老师的所有学科和学段
        int schoolId = userEntity.getSchoolId();
        if( StringUtils.isBlank( rangeId) || StringUtils.isBlank( subjectId )){
            List<TeacherBookVersion> list = teacherAddClassService.findAllSubjectByTeacher(userEntity.getId());
            if( null != list && list.size() > 0 ){
                rangeId = list.get( 0 ).getRangeId()+"";
                subjectId = list.get( 0 ).getSubjectId()+"";
            }
        }
        try {
            if (StringUtils.isNotBlank(rangeId) && StringUtils.isNotBlank(subjectId)) {
                classesList = teacherAddClassService.findAllClassByRangeSub(schoolId ,new Integer(rangeId), new Integer(subjectId),userEntity.getId());
                bookList = teacherAddClassService.findAllSubjectByTeacher(userEntity.getId());
            }else{
                return "/teacher/addClass/findAllClassByRangeSub";
            }
            model.put( "rangeId",rangeId);
            model.put( "subjectId",subjectId);
            model.put( "bookList",bookList);
            model.put( "classesList",classesList);
            model.put( "teacherName",userEntity.getRealName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/teacherdata/TeacherAddClasses";
    }
    /**
     * 根据入学年份，学段id和学科id查询对应的所有班级
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/findAllClassesByYearAndSubRange"  , method = RequestMethod.POST )
    public String findAllClassesByYearAndSubRange(HttpServletRequest request,UserEntity userEntity){
        String year = request.getParameter( "year" );
        String rangeId = request.getParameter("rangeId");
        String subjectId = request.getParameter("subjectId");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Classes> classesList = null;
        if( StringUtils.isNotBlank( year )&& StringUtils.isNotBlank( rangeId )&&
                StringUtils.isNotBlank( subjectId ) ) {
            if( "1".equals( year )){//全部
                classesList = teacherAddClassService.findAllClassByRangeSub( userEntity.getSchoolId() ,
                                                                             new Integer(rangeId),
                                                                             new Integer(subjectId),
                                                                             userEntity.getId());
            }else {
                classesList = teacherAddClassService.findAllClassesByYearAndSubRange(
                        userEntity.getSchoolId() ,
                        new Integer(year),
                        new Integer(rangeId),
                        new Integer(subjectId),
                        userEntity.getId()
                );
            }
            map.put("classesList", classesList);
        }
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("查询所有学科:"+param);
        return param.toString();
    }
    /**
     * 查询老师名下所有学科
     * @return
     * http://127.0.0.1/addClass/findAllSubjectByTeacher
     */
    @ResponseBody
    @RequestMapping(value ="/findAllSubjectByTeacher"  , method = RequestMethod.POST )
    public String findAllSubjectByTeacher( UserEntity userEntity ){
        List<TeacherBookVersion> list = teacherAddClassService.findAllSubjectByTeacher(userEntity.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("查询所有学科:"+param);
        return param.toString();
    }

    /**
     * 根据学科和学段类型查询学段id
     * @param request
     * @return
     * http://cloud.trunk.koolearn.com/addClass//findRangeIdBySubjectId?subjectId=2469&rangeType=3
     */
    @ResponseBody
    @RequestMapping(value ="/findRangeIdBySubjectId"  , method = RequestMethod.POST )
    public String findRangeIdBySubjectId(HttpServletRequest request){
        String subjectId = request.getParameter( "subjectId" );//学科id
        String rangeType = request.getParameter( "rangeType" );//学段类型
        int rangeId = 0;
        if( StringUtils.isNumeric( subjectId ) && StringUtils.isNumeric( rangeType )) {
            rangeId = teacherAddClassService.findRangeIdBySubjectId( new Integer( subjectId ) , rangeType);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rangeId", rangeId);
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("查询所有学科:"+param);
        return param.toString();
    }

    /**
     * 新增班级
     * @param request
     * @return
     * {"className":"三班","classCode":"3333","year":"1","type":"1","rangeId":"121212","rangeName":"初中","subjectId":"232"}
     * http://cloud.trunk.koolearn.com/addClass/addClassesInfo?className=班级&year=2010&type=1&rangeId=121&
     * rangeName=中学&subjectId=2464&subjectName=语文&schoolId=232
     */
    @ResponseBody
    @RequestMapping(value ="/addClassesInfo" ,method=RequestMethod.POST   )
    public String addClassesInfo(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){
        String className = request.getParameter("className");
        String year = request.getParameter("year");
        String type = request.getParameter("type");
        String rangeId = request.getParameter("rangeId");
        String rangeName  = request.getParameter("rangeName");
        String subjectId  = request.getParameter("subjectId");
        String subjectName  = request.getParameter("subjectName");
        Classes classes = teacherAddClassService.checkInfo(className ,year ,type , rangeId,rangeName,subjectId, subjectName  );
        Map<String, Object> map = new HashMap<String, Object>();
        int classPirmaryKey = 0;//班级主键
        if( null != classes ){
            try{
                //TODO 先生成班级编码 ,
                List< String > listCode = teacherAddClassService.findClassesCode();
                //List< String > listCode = teacherAddClassService.findClassesCode( userEntity.getSchoolId() );
                String classCode = teacherAddClassService.makeClassCode( listCode );
                int teacherId = userEntity.getId();
                //新增班级
                classes.setClassCode( classCode );
                classes.setTeacherId( teacherId );
                classes.setSchoolId( userEntity.getSchoolId() );
                //为不调用拼接班级姓名的方法，需要将属性isOk 置为false
                classes.setOk( false );
                classPirmaryKey = teacherAddClassService.checkAndInsertClassInfo( classes ,userEntity.getRealName() );
                classes.setId( classPirmaryKey );
                classes.setTypeName(CommonEnum.classesTypeEnum.getSource( classes.getType()).getValue());
                //放入缓存
                teacherAddClassService.addClassesToCache( CommonInstence.REDIS_CLASSKY, classes );
                loginService.hexinAccountGroup(userEntity,  classes);//合心接口 --创建班级
            }catch ( BizException e ){
                log.error( "新增班级异常，班级名称：" + classes.getClassName() + e.getMessage(), e );
                map.put("code", CommonInstence.CODE_400);
                JSONObject param = (JSONObject) JSONObject.toJSON(map);
                return param.toString();
            }catch ( Exception e1){
                log.error( "新增班级异常，班级名称：" + classes.getClassName() + e1.getMessage(), e1 );
                map.put("code", CommonInstence.CODE_400);
                JSONObject param = (JSONObject) JSONObject.toJSON(map);
                return param.toString();
            }
        }
        map.put("classesId", classPirmaryKey);
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("添加班级:"+param);
        return param.toString();
    }

    /**
     * 查询老师当前学段下的所有学科
     * @param request
     * @param userEntity
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/findTeacherSubjects" ,method=RequestMethod.POST   )
    public String findTeacherSubjects(HttpServletRequest request ,UserEntity userEntity){
        String rangeName = request.getParameter( "rangeName" );
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( rangeName )){
            List< TeacherBookVersion > list = teacherAddClassService.findTeacherSubjects( userEntity.getId() , rangeName );
            List<TeacherBookVer> teacherList = new ArrayList<TeacherBookVer>();
            if( null != list && list.size() > 0 ){
                for( TeacherBookVersion book : list ){
                    TeacherBookVer bookVer = new TeacherBookVer();
                    bookVer.setId( book.getSubjectId() );
                    bookVer.setName( book.getSubjectName() );
                    teacherList.add( bookVer );
                }
            }
            map.put( "data" , teacherList );
            map.put( "code" , CommonInstence.CODE_200 );
        }

        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info( param.toString() );
        return param.toString();
    }
    /**
     * 删除班级
     * @param request
     * @param userEntity
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/deleteClasses" ,method=RequestMethod.POST   )
    public String deleteClasses(HttpServletRequest request ,UserEntity userEntity){
        String classesId = request.getParameter( "classesId" );
        Map<String, Object> map = new HashMap<String, Object>();
        if( StringUtils.isNotBlank( classesId ) && userEntity.getId() != null ) {
            int num = teacherAddClassService.deleteClasses( new Integer( classesId ) ,userEntity.getId() );
            if(num == 0 ){
                map.put("code", CommonInstence.CODE_400);
            }else{
                map.put("code", CommonInstence.CODE_200);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }


    /**
     * 添加完班级后跳转到添加学生列表
     * @param request
     */
    @RequestMapping(value ="/goAddStudent" )
    public String goAddStudent(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){
        String classesId = request.getParameter( "classesId" );
        String rangeId = request.getParameter( "rangeId" );
        String subjectId = request.getParameter( "subjectId" );
        ClassesTeacher classesTeacher = new ClassesTeacher();
        Classes classes = null;
        Map< String ,Object > map = null;//左侧信息集合
        //查询任课老师信息
        if( StringUtils.isNotBlank( classesId )){
            //老师加入所选的班级
            String subjectName =null;
            if( StringUtils.isNotBlank( subjectId )&&StringUtils.isNumeric( subjectId )){
                 subjectName = KlbTagsUtil.getInstance().getTagName( new Integer( subjectId ));
                classesTeacher.setSubjectId(new Integer( subjectId ) );
            }
            classesTeacher.setStatus( CommonInstence.STATUS_0 );
            classesTeacher.setSubjectName(subjectName);
            classesTeacher.setTeacherId(userEntity.getId());
            classesTeacher.setClassesId( new Integer( classesId ));
            classesTeacher.setCreateTime( new Date());
            classes = teacherAddClassService.findClassesById( new Integer( classesId ));
            //插入老师班级
            teacherAddClassService.insertClassesTeacher( classesTeacher ,userEntity.getId() , classes ,userEntity.getRealName()+"加入了新班级" ,false );
            loginService.hexinAccountRelation(userEntity,classes);//合心接口--老师关联班级
            //查询左侧信息
            map = commonService.findLeftInfo( new Integer( classesId ) ,userEntity.getId(), subjectId ,rangeId );
        }
        model.put("classesStudensNum", map.get("classesStudensNum") );// 学生数量
        model.put("classes", classes);//用于添加学生的界面显示班级信息（班级编码）
        model.put("teacherBookVersionsList", map.get("teacherBookVersionsList"));//
        model.put("teacherList",map.get("classesTeacherList") );
        model.put("rangeId",rangeId != null ? rangeId :0 );
        model.put("subjectId",subjectId != null ? subjectId : 0 );
        if( 0 ==  ( null!= map.get("classesStudensNum") ? (Integer)map.get("classesStudensNum"): 0 )  ){
            return "/teacherdata/TeacherAddStudent";
        }else{
            if( null != subjectId ){
                return  "redirect:/teacher/addStudent/showStudentInfo?classesId="+classesId + "&subjectId="+subjectId +"&rangeId="+rangeId ;
            }else{
                return  "redirect:/teacher/addStudent/showStudentInfo?classesId="+classesId ;
            }
         }
    }

    private ClassesDynamic makeClassesDynamic( int teacherId ,Classes classes ){
        ClassesDynamic classesDynamic = new ClassesDynamic();
        classesDynamic.setCreateTime( new Date() );
        classesDynamic.setTeacherId( teacherId );
        classesDynamic.setClassesId( classes.getId() );
        classesDynamic.setDynamicInfo( "老师加入" +classes.getFullName() );
        classesDynamic.setStatus( CommonInstence.STATUS_0 );
        return classesDynamic;
    }
    /**
     * 添加跳转
     * @param request
     * @param model
     * @param userEntity
     * @return
     */
    @RequestMapping(value ="/goAddStudentLink" )
    public String goAddStudentLink(HttpServletRequest request ,ModelMap model ,UserEntity userEntity){
        String classesId = request.getParameter( "classesId" );
        String subjectId = request.getParameter( "subjectId" );
        String rangeId = request.getParameter( "rangeId" );
        //查询任课老师信息
        if( StringUtils.isNotBlank( classesId )){
            Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,userEntity.getId(), subjectId ,rangeId );
            model.put( "classesStudensNum",map.get("classesStudensNum")  );
            model.put( "teacherBookVersionsList",map.get("teacherBookVersionsList") );
            model.put("classes", map.get("classes") );//用于添加学生的界面显示班级信息（班级编码）
            model.put("teacherList",map.get("classesTeacherList") );
            model.put("subjectId",subjectId );
            model.put("rangeId",rangeId );
        }
        return "/teacherdata/TeacherAddStudent";
    }

    /**
     * 8月15号班级统一升级
     * @param request
     * @param userEntity
     * @return
     */
    @RequestMapping(value ="/upgradeClasses" )
    public String upgradeClasses(HttpServletRequest request  ,UserEntity userEntity){
        String schoolId = request.getParameter( "schoolId" );
        String outTime = request.getParameter( "outTime" );//过期时间08-01 格式
        Map<String, Object> map = new HashMap<String, Object>();
        int outTimeInt = 0 ;
        if( StringUtils.isNotBlank( schoolId ) ){
            if( StringUtils.isNumeric( outTime )){
                outTimeInt = new Integer( outTime );
            }else{
                outTimeInt = 815;
            }
            int num = teacherAddClassService.upgradeClassesBySchoolId( new Integer( schoolId ) , CommonInstence.STATUS_0 , CommonInstence.GRADUATE_0 ,outTimeInt );

        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }



    public static void main( String[] args ){
        List< String > list = new ArrayList< String >();
        list.add( "abC" );
        list.add( "aBc" );
        list.add( "Abc" );
        list.add( "AbcD" );
        list.add( "ABCd" );

        System.out.println( isNumeric( "43345"));

    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
