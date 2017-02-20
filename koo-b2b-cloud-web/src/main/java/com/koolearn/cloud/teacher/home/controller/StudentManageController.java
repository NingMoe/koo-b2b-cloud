package com.koolearn.cloud.teacher.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.service.StudentManageService;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherAddStudentService;
import com.koolearn.cloud.util.BizException;
import com.koolearn.cloud.util.DateFormatUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by fn on 2016/4/8.
 */
@RequestMapping("/teacher/studentManage")
@Controller
public class StudentManageController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private StudentManageService studentManageService;
    @Autowired
    private TeacherAddStudentService teacherAddStudentService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;

    /**
     * 从小组里删除学生
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteTeamStudents", method = RequestMethod.POST)
    public String deleteStudents(HttpServletRequest request) {
        //支持批量删除
        String studentsId = request.getParameter("studentsId");
        if (StringUtils.isNotBlank(studentsId)) {
            int num = studentManageService.deleteStudentsById(new Integer(studentsId));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", CommonInstence.CODE_200);
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("从小组里删除学生:"+param);
        return param.toString();
    }

    /**
     * 重置密码
     *
     * @param request
     * @return 127.0.0.1/studentManage/resetStudentPassword?ssoUserId=12121
     */
    @ResponseBody
    @RequestMapping(value = "/resetStudentPassword", method = RequestMethod.POST)
    public String resetStudentPassword(HttpServletRequest request) {
        String studentId = request.getParameter("ssoUserId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(studentId)) {
            try {
                studentManageService.resetStudentPassword(studentId);
                map.put("code", CommonInstence.CODE_200);
            } catch (Exception e) {
                map.put("code", CommonInstence.CODE_400);
                map.put("message", CommonInstence.MESSAGE_SSO);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 设置毕业状态
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateGraduateStatus", method = RequestMethod.POST)
    public String updateGraduateStatus(HttpServletRequest request) {
        String status = request.getParameter("status");//0未毕业   1 毕业'
        String classesId = request.getParameter("classesId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(classesId)) {
            try {//0未毕业   1 毕业
                if (!"0".equals(status)) {
                    status = "1";
                }
                int num = studentManageService.updateGraduateStatus(status, classesId);
                map.put("code", CommonInstence.CODE_200);
            } catch (Exception e) {
                map.put("code", CommonInstence.CODE_400);
                map.put("message", CommonInstence.MESSAGE_SSO);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        return param.toString();
    }

    /**
     * 查询所有未被分组的学生
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findNoClassesTeamStudents", method = RequestMethod.POST)
    public String findNoClassesTeamStudents(HttpServletRequest request ,UserEntity userEntity) {
        String classesId = request.getParameter("classesId");
        String subjectId = request.getParameter("subjectId");
        String rangeId = request.getParameter("rangeId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank( classesId) && StringUtils.isNotBlank( subjectId ) && StringUtils.isNotBlank( rangeId ) ) {
            List<ClassesStudent> list = teacherAddStudentService.findNoClassesTeamStudents(new Integer(classesId) , new Integer( subjectId ) ,userEntity.getId() );
            int teamId = 0;//组长id
            if (null != list) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getHeadman() != null && list.get(i).getHeadman() == 1) {
                        teamId = list.get(i).getId();
                    }
                }
            }
            map.put("list", list);
            map.put("teamId", teamId);
            map.put("code", CommonInstence.CODE_200);
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 将学生分组
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addTeamStudent", method = RequestMethod.POST)
    public String addTeamStudent(HttpServletRequest request) {
        String studentIds = request.getParameter("studentIds");
        String classesId = request.getParameter("teamId");//小组id
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNumeric(classesId) && StringUtils.isNotBlank(studentIds)) {
            List<ClassesStudent> list = teacherAddStudentService.insertClassesStudents(new Integer(classesId), studentIds);
            map.put("code", CommonInstence.CODE_200);
            map.put("list", list); //主键
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 设置组长
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateStudentJob", method = RequestMethod.POST)
    public String updateStudentJob(HttpServletRequest request) {
        String classStudentPrimaryNew = request.getParameter("newClassStudentPrimary");//新组长
        String classStudentPrimaryOld = request.getParameter("oldClassStudentPrimary");//原组长
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNumeric(classStudentPrimaryNew) && StringUtils.isNumeric(classStudentPrimaryOld)) {
            try{
                int num = teacherAddStudentService.updateStudentJob(new Integer(classStudentPrimaryNew), new Integer(classStudentPrimaryOld));
                if (num == 1) {
                    map.put("code", CommonInstence.CODE_200);
                } else {
                    map.put("code", CommonInstence.CODE_400);
                }
            }catch( Exception e ){
                map.put("code", CommonInstence.CODE_400);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        //log.info(param);
        return param.toString();
    }

    /**
     * 创建小组
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createTeamForClasses", method = RequestMethod.POST)
    public String createTeamForClasses(HttpServletRequest request ,UserEntity userEntity) {
        String teamName = request.getParameter("teamName");
        String classesId = request.getParameter("classesId");
        String subjectId = request.getParameter("subjectId");//学科id( 在不同的学科下创建小组)

        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(classesId) && StringUtils.isNotBlank(teamName) && StringUtils.isNotBlank( subjectId )) {
            Classes classes = new Classes( );
            classes.setClassName(teamName);
            classes.setParentId(new Integer(classesId));
            classes.setSubjectId( new Integer( subjectId ) );
            classes.setType( CommonInstence.CLASSES_TYPE_3 );
            classes.setCreateTime(new Date());
            classes.setStatus(0);
            classes.setGraduate(0);
            classes.setTeacherId(userEntity.getId() );
            //为不调用拼接班级姓名的方法，需要将属性isOk 置为false
            classes.setOk( false );
            Classes classesDb = teacherAddClassService.findClassesById( new Integer( classesId ));
            if( null != classesDb ){
                classes.setFullName( classesDb.getClassName() + "_" +teamName );
                classes.setRangeName( classesDb.getRangeName() );
                classes.setRangeId( classesDb.getRangeId() );
                classes.setSchoolId( classesDb.getSchoolId() );
            }
            int pramaryId = studentManageService.createTeamForClasses(classes ,userEntity.getRealName() );//主键
            map.put("code", CommonInstence.CODE_200);
            map.put("pramaryId", pramaryId);
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info("创建小组:"+param);
        return param.toString();
    }

    /**
     * 根据不同的学科查询不同的小组列表
     *
     * @param request
     * @return http://127.0.0.1/studentManage/findTeamBySubject?subjectId=2464&classesId=243547
     */
    @ResponseBody
    @RequestMapping(value = "/findTeamBySubject", method = RequestMethod.POST)
    public String findTeamBySubject(HttpServletRequest request,UserEntity userEntity) {
        String subjectId = request.getParameter("subjectId");
        String classesId = request.getParameter("classesId");
        String rangeId = request.getParameter("rangeId");
        int teacherId = userEntity.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNumeric(subjectId) && StringUtils.isNumeric(classesId) && StringUtils.isNotBlank( rangeId )) {
            List<Team> teamList = teacherAddClassService.findAllTeamStudentsBySubject(new Integer(subjectId) ,
                    new Integer(rangeId) , new Integer(classesId),userEntity.getSchoolId() ,userEntity.getId());
            if (null == teamList) {
                teamList = new ArrayList<Team>();
            }
            Classes classes = teacherAddClassService.findClassesById( new Integer(classesId) );
            //查询任课老师信息
            //List<ClassesTeacher> classesTeacherList =teacherAddClassService.findClassesTeacherByClassesId( new Integer( classesId ) ,classes.getRangeName() );
            //所有学科
            List<TeacherBookVersion > teacherBookVersionsList = null;
            if( classes != null ){
                int type = classes.getType();//type=0 行政班   type=1 学科班
                if( type == 0 ) {
                    teacherBookVersionsList = teacherAddClassService.findTeacherBookVersion(teacherId, CommonInstence.STATUS_1);
                }else if( type == 1 && StringUtils.isNotBlank( subjectId )){
                    teacherBookVersionsList = teacherAddClassService.findTeacherBookVersionByRangeIdAndSubjectId( teacherId, new Integer( rangeId ), new Integer( subjectId ) ,CommonInstence.STATUS_1);
                }
            }
            map.put("teamList", teamList);
            map.put("code", CommonInstence.CODE_200);
            //所有学科
            map.put( "teacherBookVersionsList",teacherBookVersionsList );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 删除小组
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteTeam", method = RequestMethod.POST)
    public String deleteTeamByTeamId(HttpServletRequest request) {
        String teamId = request.getParameter("teamId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNumeric(teamId)) {
            //更新班级下小组状态和小组下学生状态
            int num = studentManageService.deleteTeamByTeamId(new Integer(teamId));
            map.put("code", CommonInstence.CODE_200);
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 修改小组名字
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateTeamName", method = RequestMethod.POST)
    public String updateTeamName(HttpServletRequest request) {
        String teamName = request.getParameter("teamName");
        String classesId = request.getParameter("classesId");
        String teamId = request.getParameter("teamId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(teamName) && StringUtils.isNumeric(classesId) && StringUtils.isNumeric(teamId)) {
            int num = studentManageService.updateTeamName(new Integer(teamId), new Integer(classesId), teamName);
            if (num == 1) {
                map.put("code", CommonInstence.CODE_200);
            } else {
                map.put("code", CommonInstence.CODE_400);
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        log.info(param);
        return param.toString();
    }

    /**
     * 下载学生列表
     *
     * @param request 127.0.0.1/studentManage/exportExcelFile?classesId=243594
     */
    @RequestMapping(value = "/exportExcelFile", method = RequestMethod.GET)
    public void exportExcelFile(HttpServletRequest request,HttpServletResponse response ,UserEntity userEntity) {
        String classesId = request.getParameter("classesId");
        if (StringUtils.isNumeric(classesId)) {
            List<User> list = teacherAddStudentService.findClassesStudentByClassesId( userEntity.getId() , new Integer(classesId ) );
            Classes classesDb = teacherAddClassService.findClassesById( new Integer( classesId ));
            makeExcel(list ,request ,response ,classesDb );

        }
    }

    public void makeExcel(List<User> list ,HttpServletRequest request ,HttpServletResponse response ,Classes classes ) {
        {
            StringBuffer fileName = new StringBuffer();
            fileName.append( DateFormatUtils.getDay( new Date()) );
            String lastfilename = fileName.toString() + classes.getClassName() +".xls";
            String codedfilename = null;
            try {
                //解决IE浏览器中文乱码
                String agent = request.getHeader("USER-AGENT");
                if (null != agent && -1 != agent.indexOf("MSIE") || null != agent  && -1 != agent.indexOf("Trident")) {// ie
                    String name = java.net.URLEncoder.encode(lastfilename, "UTF8");
                    codedfilename = name;
                }else{
                    codedfilename = lastfilename;
                }
                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/ms-excel");
                // response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-disposition", "attachment;filename="
                        + new String( codedfilename.getBytes("utf-8"), "iso8859-1") );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("学生列表");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue("序号");
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue("帐号");
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);
            cell.setCellValue("初始密码");
            cell.setCellStyle(style);
            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow((int) i + 1);
                User stu = (User) list.get(i);
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue( ( i+ 1) );
                row.createCell((short) 1).setCellValue(stu.getUserName());
                row.createCell((short) 2).setCellValue(stu.getRealName());
                row.createCell((short) 3).setCellValue( CommonInstence.RESET_PASSWORD );
                // cell = row.createCell((short) 3);
                // cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu.getRealName()));
            }
            // 第六步，将文件存到指定位置
            try {
                OutputStream out = response.getOutputStream();
                wb.write(out);
                //String realPath = request.getRealPath("");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFile( String path ,HSSFWorkbook wb  ){

    }

    public void deleteFile( String path ){
        File file = new File( path );
        if( null != file && file.isDirectory() ){
            file.delete();
        }
    }

}


