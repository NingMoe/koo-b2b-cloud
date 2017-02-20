package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.HomePageClassesExam;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.composition.service.CompositionService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.service.ClassHomePageService;
import com.koolearn.cloud.teacher.service.FirstLoginChoiceService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * 教师首次登录操作
 * Created by fn on 2016/3/31.
 */
@RequestMapping("/teacher/choiceSubject")
@Controller
public class FirstLoginChoiceController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private FirstLoginChoiceService firstLoginChoiceService;
    @Autowired
    private ClassHomePageService classHomePageService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private CompositionService compositionService;
    @Autowired
    private LoginService loginService;
    /**
     * 跳转到首次登陆选择版本页面
     * @param request
     * @return
     * eg: rangeId:53062
     *     subjectId: 1
     *http://127.0.0.1/teacher/choiceSubject/goShowBook?subjectId=1&rangeId=53062
     */
    @RequestMapping(value ="/goShowBook")
    public String find(HttpServletRequest request ,  ModelMap model  ,UserEntity userEntity){
        String teacherBookVersionId = request.getParameter( "teacherBookVersionId" );
        if( StringUtils.isNumeric( teacherBookVersionId )){
            TeacherBookVersion teacherBookVersion = firstLoginChoiceService.findTeacherBookVersionById( new Integer( teacherBookVersionId ));
            if( null != teacherBookVersion ){
                //先获取所有该学科下面的学段的所有科目
                List<Tags> kemuNameList = firstLoginChoiceService.findKeMuList( teacherBookVersion.getSubjectId() , teacherBookVersion.getRangeId());
                model.put( "list" , kemuNameList );
                model.put( "subjectId" , teacherBookVersion.getSubjectId() );
                model.put( "rangeId" , teacherBookVersion.getRangeId() );
                model.put( "rangeName" , teacherBookVersion.getRangeName() );
                model.put( "subjectName" , teacherBookVersion.getSubjectName() );
                model.put( "teacherBookVersionId" ,teacherBookVersionId );
            }
        }
        //拼装科目图片URL
        return "/teacherdata/TeacherAddSubject";
    }
    /**
     * 添加老师选择的教材信息
     * @param request
     * @param
     * @return
     * 127.0.0.1/teacher/choiceSubject/addTeacherRangeAndJiaoCai?teacherBookVersionId=4&bookVersionId=122757&bookVersionName=任教版
     */
    @ResponseBody
    @RequestMapping(value = "/addTeacherRangeAndJiaoCai"  , method = RequestMethod.POST )
    public String addTeacherRangeAndJiaoCai( HttpServletRequest request  ,UserEntity userEntity){
        // TODO 获取当前登录人ID， 替换
        int teacherId =  userEntity.getId();
        String teacherBookVersionId = request.getParameter( "teacherBookVersionId" );//老师教材主键
        String bookVersionId = request.getParameter( "bookVersionId" );//教材版本id
        String bookVersionName = request.getParameter( "bookVersionName" );//教材版本名称
        try{
            if( StringUtils.isNotBlank( teacherBookVersionId) && StringUtils.isNotBlank( bookVersionId)){
                TeacherBookVersion book = firstLoginChoiceService.findTeacherBookVersionById( new Integer( teacherBookVersionId ));
                if( null != book ){
                    book.setId( new Integer( teacherBookVersionId ));
                    book.setBookVersionName( bookVersionName );
                    book.setBookVersionId( new Integer( bookVersionId ) );
                    book.setUpdateTime( new Date() );
                    firstLoginChoiceService.addTeacherRangeAndJiaoCai( book ,teacherId );
                    //更新缓存
                    UserEntity ue = loginService.findUser(userEntity.getUserName());
                    changeUser(ue);
                }
            }
        }catch( Exception e ){
            log.error( "教师选择教材版本异常 "+e.getMessage() , e );
        }
        //int num = firstLoginChoiceService.findTeacherSubjectNum( teacherId );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", CommonInstence.CODE_200);
        map.put("isFusui","false");
        if(userEntity.isFusui()){
            //扶绥用户完善资料
            map.put("isFusui","true");
            //合心接口-注册老师
            loginService.hexinAccountTeacher(userEntity);
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }
    /**
     * 跳转到班级首页
     * @param request
     * @return
     * cloud.trunk.koolearn.com/teacher/choiceSubject/goClassHomePage
     */
    @RequestMapping(value = "/goClasssHomePage"  , method = RequestMethod.GET )
    public String goClasssHomePage(HttpServletRequest request ,UserEntity userEntity,ModelMap modelMap ) throws Exception {
        int teacherId = userEntity.getId();
        try{
            //查询老师下所有班级
            List<Classes> classList = classHomePageService.findTeacherClassById( teacherId ,CommonInstence.CLASSES_TYPE_3 ,CommonInstence.STATUS_0);
            //每个班级的作业和课堂
            List<HomePageClassesExam> tpExamsList = classHomePageService.findExamInfoByClassId( classList ,userEntity.getId());
            //查询老师信息
            List< TeacherBookVersion > list = teacherAddClassService.findTeacherBookVersion( teacherId ,CommonInstence.STATUS_1);
            if( null != list && list.size() > 0 ){
                modelMap.put( "rangeId" , list.get(0 ).getRangeId() );
                modelMap.put( "rangeName" , list.get(0 ).getRangeName() );
                modelMap.put( "subjectId" , list.get(0 ).getSubjectId() );
            }
            User user = classHomePageService.findUserInfoById( teacherId );
            Boolean f = compositionService.queryIsCompositionTeacher(userEntity.getUserId());

            modelMap.addAttribute("compositionNotifyFlag",false);
            if(f){
                //如果是
                Integer count = compositionService.queryTeacherNewCompositionCount(userEntity.getUserId());
                if(count>0){
                    modelMap.addAttribute("count",count);
                    modelMap.addAttribute("compositionNotifyFlag",true);
                }
            }
            // TODO 修改老师姓名后同步缓存，可以从缓存里取姓名，效率高
            modelMap.put( "nameT" , null!=user ? user.getRealName(): "" );
            modelMap.put("classList", tpExamsList);
        }catch (Exception e ){
            log.error(" 教师班级首页展示异常 " + e.getMessage(), e);
            throw new Exception( e );
        }
        return "/teacherdata/ClassHomePage";
    }
    /**
     * 跳转到扶绥首页
     * @param request
     * @return
     */
    @RequestMapping(value = "/goFusuiHomePage"  , method = RequestMethod.GET )
    public String goFusuiHomePage(HttpServletRequest request ,UserEntity userEntity,ModelMap modelMap ) throws Exception {
        int teacherId = userEntity.getId();
        try{
            //查询老师信息
            List< TeacherBookVersion > list = teacherAddClassService.findTeacherBookVersion( teacherId ,CommonInstence.STATUS_1);
            if( null != list && list.size() > 0 ){
                modelMap.put( "rangeId" , list.get(0 ).getRangeId() );
                modelMap.put( "rangeName" , list.get(0 ).getRangeName() );
                modelMap.put( "subjectId" , list.get(0 ).getSubjectId() );
            }
            User user = classHomePageService.findUserInfoById( teacherId );
            // TODO 修改老师姓名后同步缓存，可以从缓存里取姓名，效率高
            modelMap.put( "nameT" , null!=user ? user.getRealName(): "" );
            //合心考悦
            Map<String,String> paramMap=loginService.hexinLogin(userEntity);
            modelMap.put( "access_key" , paramMap.get("access_key"));
            modelMap.put( "timestamp" , paramMap.get("timestamp"));
            modelMap.put( "nonce" ,  paramMap.get("nonce"));
            modelMap.put( "signature" ,  paramMap.get("signature"));
            modelMap.put( "userEntity" ,userEntity);
        }catch (Exception e ){
            log.error(" 教师班级首页展示异常 " + e.getMessage(), e);
            throw new Exception( e );
        }
        return "/teacherdata/fusuiHomePage";
    }
    /**
     * 查询老师当前一共创建了多少个科目（不能大于三个）
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findTeacherSubjectNum"  , method = RequestMethod.POST)
    public String findTeacherSubjectNum( HttpServletRequest request ,UserEntity userEntity){
        // TODO 获取当前登录人ID， 替换
        int teacherId = userEntity.getId();
        int num = firstLoginChoiceService.findTeacherSubjectNum( teacherId );
        Map< String , Object > map = new HashMap< String , Object >();
        map.put( "code" , CommonInstence.CODE_200 );
        map.put( "data" , num );
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info(param.toString());
        return param.toString();
    }



}
