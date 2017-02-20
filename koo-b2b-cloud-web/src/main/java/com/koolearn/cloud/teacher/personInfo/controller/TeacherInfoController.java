package com.koolearn.cloud.teacher.personInfo.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.koolearn.cloud.login.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.koolearn.cloud.teacher.service.TeacherAddClassService;
import com.koolearn.cloud.teacher.service.TeacherInfoService;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.SsoUtil;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.sso.util.ErrCode;
import com.koolearn.sso.util.SSOException;

@Controller
@RequestMapping("/teacher/info/")
public class TeacherInfoController extends BaseController{
    private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	TeacherInfoService teacherInfoService;
	@Autowired
	IOpenService iOpenService;
    @Autowired
    private TeacherAddClassService teacherAddClassService;
    @Autowired
    LoginService loginService;
	
	/**
	 * 个人中心--首页（老师基本信息查看页）
	 * @param ue
	 * @return
	 */
	@RequestMapping("/insertBookVersion")
	@ResponseBody
	public Map index(TeacherBookVersion bookVersion,UserEntity ue){
		Map map = new HashMap();
		map.put("success", false);
		bookVersion.setTeacherId(ue.getId());
		bookVersion.setCreateTime(new Date());
		bookVersion.setStatus(TeacherBookVersion.STATUS_VALID);
		List<TeacherBookVersion> list = teacherInfoService.findTeacherBookVersion(ue);
		for (int i = 0; i < list.size(); i++) {
			TeacherBookVersion tbv = list.get(i);
			if(tbv.getSubjectId().intValue()==bookVersion.getSubjectId().intValue()&&tbv.getRangeId().intValue()==bookVersion.getRangeId().intValue()&&tbv.getBookVersionId().intValue()==bookVersion.getBookVersionId().intValue()){
				map.put("errMsg", "您选的学科版本已存在，请重新选择!");
				return map;
			}
		}
		if(list!=null&&list.size()<3){
			int id = teacherInfoService.insertTeacherBookVersion(bookVersion);
            list = teacherInfoService.findTeacherBookVersion(ue);
            ue.setTeacherBookVersionList(list);
            changeUser(ue );
			if(id>0){
				map.put("success", true);
			}
		}else{
			map.put("errMsg", "每个教师最多可选三个教材版本!");
		}
		return map;
	}
	/**
	 * 个人中心--首页（老师基本信息查看页）
	 * @param ue
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(UserEntity ue){
		UserEntity person = teacherInfoService.findTeacherBaseInfo(ue);
		System.out.println(person);
		//获取老师的学科学段教材版本
		List<TeacherBookVersion> plist = teacherInfoService.findTeacherBookVersion(ue);
		ModelAndView view = new ModelAndView();
		view.addObject("person", person);
		view.addObject("plist", plist);
		view.setViewName("/teacher/pcenter/baseInfo");
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
		//获取老师的学科学段教材版本
		List<TeacherBookVersion> plist = teacherInfoService.findTeacherBookVersion(ue);
		ModelAndView view = new ModelAndView();
		view.addObject("person", person);
		view.addObject("plist", plist);
        view.addObject("uuid",  UUID.randomUUID().toString().replace("-", ""));
		view.setViewName("/teacher/pcenter/baseInfoModify");
		return view;
	}
	
	/**
	 * 个人中心--我的班级
	 * @param ue
	 * @return
	 */
	@RequestMapping("/myClasses")
	public ModelAndView myClasses(UserEntity ue){
		ModelAndView view = new ModelAndView();
		List<Classes> clist = teacherInfoService.findMyClassesById(ue);
		for (int i = 0; i < clist.size(); i++) {
			Classes classes = clist.get(i);
			List<TeacherBookVersion > teacherBookVersionsList = null;
	        if( classes != null ){
	            int type = classes.getType();//type=0 行政班   type=1 学科班
	            if( type == 0 ) {// 如果是行政班，则根据学段名称和老师id查询所有学科
	                //teacherBookVersionsList = teacherAddClassService.findTeacherBookVersion(teacherId, CommonInstence.STATUS_1);
	                teacherBookVersionsList = teacherAddClassService.findTeacherBookVersionByRangeName(ue.getId(), classes.getRangeName() , CommonInstence.STATUS_1);
	            }else if( type == 1 ){
	                teacherBookVersionsList = teacherAddClassService.findTeacherBookVersionByRangeIdAndSubjectId( ue.getId(), classes.getRangeId(), classes.getSubjectId() ,CommonInstence.STATUS_1);
	            }
	        }
	        classes.setTeacherBookVersionList(teacherBookVersionsList);
	        System.out.println(classes.getTeacherBookVersionList());
		}
		view.setViewName("/teacher/pcenter/myClasses1");
		view.addObject("clist", clist);
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
            loginService.hexinAccountTeacher(ue);
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
                    loginService.hexinAccountTeacher(ue);
					System.out.println("count=i="+i);
					map.put("success", true);
				}
			} catch (Exception e) {
                log.error( "个人中心--修改邮箱异常，" + e.getMessage() ,e );
                map.put("fail", "修改邮箱异常");
                return map;
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
	public Map saveRealName(UserEntity ue){
		String realName = getParamter("realName");
		Map map = new HashMap();
		map.put("success", true);
		try {
			teacherInfoService.updateRealName(ue.getId(), realName);
            //更新缓存
            ue.setRealName( realName );
            ue.setRealName(realName);
            loginService.hexinAccountTeacher(ue);//合心接口--修改老师资料
            changeUser(ue);
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
