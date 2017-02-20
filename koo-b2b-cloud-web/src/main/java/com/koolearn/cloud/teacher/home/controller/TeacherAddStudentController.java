package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.service.CommonService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherAddStudentService;
import com.koolearn.cloud.util.BizException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fn on 2016/4/6.
 */
@RequestMapping("/teacher/addStudent")
@Controller
public class TeacherAddStudentController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private TeacherAddStudentService teacherAddStudentService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    private CommonService commonService;
    @Autowired
    LoginService loginService;

    /**
     * 批量添加学生获取SSO学生账号
     * @param request
     * @param modelMap
     * @return
     * 127.0.0.1/addStudent/writeAll?studentStr=张三;we323,李四;d2343,王五;939294&
     */
    @ResponseBody
    @RequestMapping(value ="/writeAll"  , method = RequestMethod.POST)
    public String writeAllUserInfo(HttpServletRequest request ,ModelMap modelMap, final UserEntity userEntity){
        String studentStr = request.getParameter( "studentStr" );//所有批量学生字符
        String classesId = request.getParameter( "classesId" );//班级id
        final String classesIdFinal=classesId;
        String classNo = request.getParameter( "classNo" );//班级编码
        List<User> userList = null;
        Map< String , Object > map = new HashMap< String , Object>();
        try {
            if (StringUtils.isNotBlank(studentStr) && StringUtils.isNotBlank(classesId)) {
                int classesIdInt = new Integer(classesId);
                //用队列的方法（暂停使用）
                //teacherAddStudentService.setStudentInfoQueue( studentStr ,  classNo , new Integer( classesId ) , userEntity);
                userList = teacherAddStudentService.checkStudentNameAndInsert(studentStr, classNo);
                //入库
                userList = teacherAddStudentService.insertUserAndStudent(userList, classesIdInt, userEntity);
                ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
                final List<User> finalUserList=userList;
                fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //合心接口--批量注册学生
                        Classes classes = teacherAddClassService.findClassesById( new Integer( classesIdFinal ));
                        loginService.hexinAccountStudentList(classes , userEntity, finalUserList);
                    }
                });
               // System.out.println( "导入结束时间2:" + (System.currentTimeMillis()- begin) );
            }
        }catch ( BizException e ){
            map.put("code" ,CommonInstence.CODE_400 );
            map.put("data" ,e.getMessage() );
            JSONObject param = (JSONObject) JSONObject.toJSON( map );
            log.info( "批量添加学生返回数据："+ param );
            return param.toString();
        } catch (Exception e) {
            map.put("code" ,CommonInstence.CODE_400 );
            map.put("data" ,e.getMessage() );
            JSONObject param = (JSONObject) JSONObject.toJSON( map );
            log.info(  "批量添加学生返回数据："+param );
            return param.toString();
        }
        map.put("list",userList );
        map.put("classesId" ,classesId );
        map.put("code" ,CommonInstence.CODE_200 );
        map.put("classNo",classNo );
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        //log.info( param );
        return param.toString();
    }
    /**http://cloud.trunk.koolearn.com/addStudent/showStudentInfo
     * 入库完成后跳转到学生信息页
     * @param request
     * @param modelMap
     * @return   , method = RequestMethod.POST
     */
    @RequestMapping(value ="/showStudentInfo")
    public String showStudentInfo(HttpServletRequest request ,ModelMap modelMap,UserEntity userEntity){
        List<User> userList = null;//
        String classesId = request.getParameter("classesId");
        String rangeIdStr = request.getParameter("rangeId");
        String subjectIdStr = request.getParameter("subjectId");
        int classesIdInt = 0 ;
        if( StringUtils.isNumeric( classesId )&&StringUtils.isNotBlank( classesId ) ){
            classesIdInt = new Integer( classesId );
            if( StringUtils.isBlank( subjectIdStr )){
                subjectIdStr = "0";
            }if( StringUtils.isBlank( rangeIdStr )){
                rangeIdStr = "0";
            }
            Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,userEntity.getId(), subjectIdStr ,rangeIdStr );
            //查询当前班级所有学生
            userList = teacherAddStudentService.findClassesStudentByClassesId( userEntity.getId(), classesIdInt );
            //所有学生列表
            modelMap.put( "userList" ,userList);
            //班级信息
            modelMap.put( "classes" ,map.get("classes") );
            modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
            //任课老师信息
            modelMap.put( "classesTeacherList" ,map.get("classesTeacherList"));
            //学科id
            modelMap.put( "subjectId",map.get( "subjectId" ) );
            modelMap.put( "rangeId",map.get( "rangeId" ) );
            modelMap.put( "teacherBookVersionsList",map.get("teacherBookVersionsList") );
        }
        return "/teacherdata/StudentsList";
    }
    /**
     * 跳转到学生小组列表页
     * @param request
     * @param userEntity
     * @return
     */
    @RequestMapping(value = "/findTeamListBySubject", method = RequestMethod.GET)
    public String findTeamListBySubject(HttpServletRequest request ,ModelMap modelMap , UserEntity userEntity) {
        String subjectId = request.getParameter("subjectId");
        String classesId = request.getParameter("classesId");
        String rangeId = request.getParameter("rangeId");
        if (StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classesId)) {
            List<Team> teamList = teacherAddClassService.findAllTeamStudentsBySubject(new Integer(subjectId) ,
                                                                                      new Integer( rangeId ) ,
                                                                                      new Integer(classesId) ,
                                                                                      userEntity.getSchoolId(),
                                                                                      userEntity.getId());
            if (null == teamList) {
                teamList = new ArrayList<Team>();
            }
            //左侧 查询班级信息
            Map< String ,Object > map = commonService.findLeftInfo( new Integer( classesId ) ,userEntity.getId(), subjectId ,rangeId );
            modelMap.put("teamList", teamList);
            modelMap.put( "teacherBookVersionsList",map.get("teacherBookVersionsList") );
            modelMap.put( "classesTeacherList",map.get("classesTeacherList") );
            modelMap.put( "classesStudensNum",map.get("classesStudensNum") );
            modelMap.put( "classes",map.get("classes") );
            modelMap.put( "rangeId",rangeId );
        }
        return "/teacherdata/StudentsTeamList";
    }

    /**
     * 删除学生
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/deleteUserAndClassStudent")
    public String deleteUserAndClassStudent(HttpServletRequest request,ModelMap model ){
        String classId = request.getParameter( "classesId" );//
        String userId = request.getParameter( "userId" );//用户id
        Map< String , Object > map = new HashMap< String , Object>();
        if( StringUtils.isNumeric( classId )&&StringUtils.isNumeric( userId )){
            int num = teacherAddStudentService.deleteUserAndClassStudent( new Integer( classId ) ,new Integer( userId) );
            map.put( "code" ,CommonInstence.CODE_200 );
        }else{
            map.put( "code" ,CommonInstence.CODE_400 );
            map.put( "message" ,"参数传递异常" );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param );
        return param.toString();
        //return "redirect:/addStudent/showStudentInfo?classesId=" + classId ;
    }
    /**
     * 查询学科下面的所有小组和学生
     */
    @ResponseBody
    @RequestMapping(value ="/findAllTeamStudentsBySubject")
    public String findAllTeamStudentsBySubject(HttpServletRequest request ,UserEntity userEntity){
        String subjectId = request.getParameter("subjectId");//学科id
        String rangeId = request.getParameter("rangeId");//学段id
        String classesId = request.getParameter("classesId");//班级id
        Map< String , Object > map = new HashMap< String , Object>();
        List<Team> teamList = null;
        if( StringUtils.isNumeric( subjectId ) ) {
            teamList = teacherAddClassService.findAllTeamStudentsBySubject( new Integer( subjectId ) ,new Integer( rangeId )  , new Integer( classesId),userEntity.getSchoolId(),userEntity.getId());
            map.put("list",teamList );
            map.put("code", CommonInstence.CODE_200 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param );
        return param.toString();
    }

    private List< User > makeUserData(String[] userNameArray , String[] realNameArray , String[] studentCodeArray){
        List< User > userList = new LinkedList<User>(   );
        for( int i = 0 ; i < userNameArray.length;i++ ){
            User user = new User();
            user.setUserName( userNameArray[ i ] );
            user.setRealName( realNameArray[ i ]);
            user.setStudentCode( studentCodeArray[ i ]);
            userList.add(user );
        }
        return userList;
    }

    /**
     * 修改学生名称和学号
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/updateRealNameAndStudentCode")
    public String updateRealNameAndStudentCode(HttpServletRequest request){
        String realName = request.getParameter( "realName" );
        String studentCode = request.getParameter( "studentCode" );
        String userId = request.getParameter( "userId" );
        Map< String , Object > map = new HashMap< String , Object>();
        if( StringUtils.isNumeric( userId )){
            //TODO 如果学号还是重复怎么办
            int num = 0;
            try {
                num = teacherAddStudentService.updateRealNameAndStudentCode( new Integer( userId ) , realName , studentCode );
                map.put("code", CommonInstence.CODE_200 );
            } catch (SQLException e) {
                map.put("code", CommonInstence.CODE_400 );
            } catch (Exception e) {
                map.put("code", CommonInstence.CODE_400 );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info( param );
        return param.toString();
    }

}
