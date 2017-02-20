package com.koolearn.cloud.student.studentClass.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesStudent;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.student.StudentClassHomeService;
import com.koolearn.cloud.teacher.service.TeacherAddStudentService;

@Controller
@RequestMapping("/student/classes")
public class StudentClassesController extends BaseController{
	private static Log logger = LogFactory.getLog(StudentClassesController.class);
    @Autowired
    private TeacherAddStudentService teacherAddStudentService;
    @Autowired
	LoginService loginService;
    @Autowired
    private StudentClassHomeService studentClassHomeService;
	
	@RequestMapping("/perfectInfo")
	public String perfectInfo(HttpServletRequest request){
		return "/student/perfectInfo";
	}
	
	@RequestMapping("/exist")
	@ResponseBody
	public Map exist(HttpServletRequest request){
		Map<String ,Object > map = new HashMap<String, Object>();
		String classesCode = request.getParameter("classesCode");//班级编码
		/*验证班级编码 班级service，按编码查询班级*/
		boolean existClass = true;
		Classes c = teacherAddStudentService.findClassesByCode(classesCode);
		if(c==null){//不存在该班级
			existClass = false;
		}
		if(existClass){//存在该班级
			map.put("success", true);
			map.put("status",c.getStatus() );//0 正常,10：关闭状态
			if( c.getStatus() == 10 ){
				map.put("message", "班级已被关闭，不能加入");
			}
		}else{
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/join")
	@ResponseBody
	public Map joinClasses(HttpServletRequest request,UserEntity ue){
		Map map = new HashMap<String, String>();
		map.put("success", false);
		String classesCode = request.getParameter("classesCode");//班级编码
		String realName = request.getParameter("realName");//学生姓名
		Classes c = teacherAddStudentService.findClassesByCode(classesCode);
		int result = studentClassHomeService.checkStudentClasses( classesCode , ue.getId());
		if(2 == result){
			map.put("errMsg", "已经加入该班级!");
		}else{
			ClassesStudent cs = new ClassesStudent();
			cs.setClassesId(c.getId());
			cs.setStudentId(ue.getId());
			cs.setStatus(ClassesStudent.STATUS_NOMAL);
			cs.setCreateTime(new Date());
			UserEntity update = new UserEntity();
			update.setId(ue.getId());
			update.setRealName(realName);
			update.setSchoolId(c.getSchoolId());
			update.setSchoolName(c.getSchoolName());
			update.setProcess(UserEntity.USER_PROCESS_TWO);
			int i = loginService.insertClassesStudent(cs,update);
			UserEntity user = loginService.findUser(ue.getUserName());
            //合心接口-- 学生注册加入班级
            loginService.hexinAccountStudentAndRelation(user, c);//合心接口--班级关系请求
			changeUser(user);//学生完善资料后,更新用户信息缓存
			if(i>0){
				map.put("success", true);
			}else{
				map.put("errMsg", "加入失败，请重试!");
			}
			logger.debug("insertClassesStudent="+i);
		}
		return map;
	}
}
