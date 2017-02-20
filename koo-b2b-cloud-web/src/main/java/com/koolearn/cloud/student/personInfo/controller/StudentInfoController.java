package com.koolearn.cloud.student.personInfo.controller;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.koolearn.cloud.login.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.RedisKey;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.entity.PersonInfo;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherInfoService;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.SsoUtil;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.sso.util.CookieUtil;
import com.koolearn.sso.util.ErrCode;
import com.koolearn.sso.util.SSOException;

@Controller
@RequestMapping("/student/info/")
public class StudentInfoController extends BaseController{
	@Autowired
	TeacherInfoService teacherInfoService;
	@Autowired
	IOpenService iOpenService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    LoginService loginService;
	
	/**
	 * 个人中心--首页
	 * @param ue
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(UserEntity ue){
		UserEntity person = teacherInfoService.findTeacherBaseInfo(ue);
		List<School> schoolList = teacherInfoService.findStudentSchool(ue);
        StringBuffer schoolName = new StringBuffer();
        Set< String > nameSet = new HashSet< String >();
		if(!schoolList.isEmpty()){
			for (int i = 0; i < schoolList.size(); i++) {
				School s = schoolList.get(i);
                nameSet.add(s.getName());
			}
            Iterator< String > iterator = nameSet.iterator();
            while( iterator.hasNext() ){
                schoolName.append( iterator.next() );
                schoolName.append( "," );
            }
		}
		person.setSchoolName(schoolName.toString().substring( 0 , schoolName.toString().length() - 1 ) );
		ModelAndView view = new ModelAndView();
		view.addObject("person", person);
		view.setViewName("/student/pcenter/baseInfo");
		return view;
	}
	
	/**
	 * 个人中心--老师基本信息修改展示页
	 * @param ue
	 * @return
	 */
	@RequestMapping("/modifyPage")
	public ModelAndView modifyPage(UserEntity ue){
		UserEntity person = teacherInfoService.findTeacherBaseInfo(ue);
        List<School> schoolList = teacherInfoService.findStudentSchool(ue);
        StringBuffer schoolName = new StringBuffer();
        Set< String > nameSet = new HashSet< String >();
        if(!schoolList.isEmpty()){
            for (int i = 0; i < schoolList.size(); i++) {
                School s = schoolList.get(i);
                nameSet.add(s.getName());
            }
            Iterator< String > iterator = nameSet.iterator();
            while( iterator.hasNext() ){
                schoolName.append( iterator.next() );
                schoolName.append( "," );
            }
        }

		ModelAndView view = new ModelAndView();
		view.addObject("person", person);
		view.addObject("schoolName", schoolName.toString().substring( 0 , schoolName.toString().length() - 1 ) );
        view.addObject("uuid",  UUID.randomUUID().toString().replace("-", ""));
		view.setViewName("/student/pcenter/baseInfoModify");
		return view;
	}
	
	
	/**
	 * 个人中心--修改手机号
	 * @param ue
	 * @return
	 */
	@RequestMapping("/saveMobile")
	@ResponseBody
	public Map saveMobile(UserEntity ue){
		String mobile = getParamter("mobile");
		String random = getParamter("random");
		Map map = new HashMap();
		map.put("success", false);
		String errMsg = "";
		try {
			//校验手机验证码
			SsoUtil.checkMobileCode(random, mobile);
		} catch (SSOException e) {
			if(ErrCode.ERROR_REGCODE_OUTTIME.equals(e.getCode())){
		    	errMsg = "手机验证码超时";
		    }else if(ErrCode.ERROR_REGCODE_NOTEXISTS.equals(e.getCode())){
		    	errMsg = "手机验证码输入错误";
		    }else if(ErrCode.ERROR_REGCODE_NULL.equals(e.getCode())){
		    	errMsg = "手机验证码为空";
		    }else if("regCode.error".equals(e.getCode())){
				errMsg = "验证码错误";
			}
			e.printStackTrace();
			map.put("errMsg", errMsg);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errMsg", "注册失败，请检查网络是否通畅");
			return map;
		}
		//验证是否存在该手机号
		UsersDTO u = iOpenService.getUserByMobile(mobile);
		if(u==null){//不存在该手机号,可修改
			UserEntity user = new UserEntity();
			user.setId(ue.getId());
			user.setUserId(ue.getUserId());
			user.setMobile(mobile);
			int i = teacherInfoService.updateUserMobile(user);
            ue.setMobile(mobile);
            loginService.hexinAccountStudent(ue);
			System.out.println("count=i="+i);
			map.put("success", true);
		}else{
			map.put("errMsg", "手机号已被注册,请更换手机号!");
		}
		return map;
	}
	
	/**
	 * 个人中心--修改邮箱
	 * @param ue
	 * @return
	 */
	@RequestMapping("/saveEmail")
	@ResponseBody
	public Map saveEmail(HttpServletRequest request, UserEntity ue){
        String uuid = request.getParameter("uuid");
		String email = getParamter("email");
		String random = getParamter("random");
		Map map = new HashMap();
		map.put("success", false);
		map.put("errMsg", "邮箱已被注册,请更换邮箱!");
		//手机验证码
		String regCode = (String)CacheTools.getCache(RedisKey.getPcenterVerifyCodeEmail(uuid), String.class);
		if(StringUtils.isBlank(random)||!random.equalsIgnoreCase(regCode)){//验证码错误
			map.put("errMsg", "验证码输入错误！");
			return map;
		}else{
			//验证是否存在该手机号
			try {
				UsersDTO u = iOpenService.getUserByEmail(email);
				if(u==null){//不存在该邮箱,可修改
					UserEntity user = new UserEntity();
					user.setId(ue.getId());
					user.setUserId(ue.getUserId());
					user.setEmail(email);
					int i = teacherInfoService.updateUserEmail(user);
                    ue.setEmail(email);
                    loginService.hexinAccountStudent(ue);
					System.out.println("count=i="+i);
					map.put("success", true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
		}
	}
	
	/**
	 * 个人中心--修改手机号
	 * @param ue
	 * @return
	 */
	@RequestMapping("/savePassword")
	@ResponseBody
	public Map savePassword(UserEntity ue){
		String password = getParamter("password");
		String oldPassword = getParamter("oldPassword");
		Map map = new HashMap();
		map.put("success", true);
		try {
			teacherInfoService.updatePassword(ue.getUserId(), oldPassword, password);
		} catch (Exception e) {
			map.put("success", false);
			map.put("errMsg", "原密码错误!");
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 个人中心--更新姓名
	 * @param ue
	 * @return
	 */
	@RequestMapping("/saveRealName")
	@ResponseBody
	public Map saveRealName(HttpServletRequest request,UserEntity ue){
		String realName = getParamter("realName");
		Map map = new HashMap();
		map.put("success", true);
		try {
			teacherInfoService.updateRealName(ue.getId(), realName);
			Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
	    	String authId = authCookie.getValue();
	    	UserEntity user = CacheTools.getCacheForever(authId, UserEntity.class);
	    	ue.setRealName(realName);
            loginService.hexinAccountStudent(ue);//合心接口--修改学生资料
	    	CacheTools.addCache(ue.getId().toString(), user);
		} catch (Exception e) {
			map.put("success", false);
			map.put("errMsg", "操作失败,请重试!");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 个人中心--更新头像地址
	 * @param ue
	 * @return
	 */
	@RequestMapping("/saveIco")
	@ResponseBody
	public Map saveIco(HttpServletRequest request,UserEntity ue){
		String ico = getParamter("url");
		Map map = new HashMap();
		map.put("status", 0);
		try {
			String icoUrl = teacherInfoService.updateIco(ue.getId(), ico);
			ue.setIco(icoUrl);//更新用户头像url
			changeUser(ue);
		} catch (Exception e) {
			map.put("status", 1);
			map.put("errMsg", "操作失败,请重试!");
			e.printStackTrace();
		}
		return map;
	}
}
