package com.koolearn.cloud.login.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.util.*;
import com.koolearn.library.maintain.remote.service.InterfaceCloudService;
import com.koolearn.sso.dto.UsersDTO;
import com.koolearn.util.SystemGlobals;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.RedisKey;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.teacher.service.TeacherInfoService;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.mc.sms.dto.SendResult;
import com.koolearn.mc.sms.enums.SMSApplyType;
import com.koolearn.mc.sms.service.ISmsClientService;
import com.koolearn.sso.dto.RegistDTO;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.sso.util.CommonUtil;
import com.koolearn.sso.util.CookieUtil;
import com.koolearn.sso.util.ErrCode;
import com.koolearn.sso.util.SSOException;

@Controller
public class LoginController extends BaseController{
	private static Log logger = LogFactory.getLog(LoginController.class);
	@Autowired
	IOpenService iopenService;
	@Autowired
	LoginService loginService;
	@Autowired
	ISmsClientService iSmsClientService;
	@Autowired
	TeacherInfoService teacherInfoService;
    @Autowired
    InterfaceCloudService interfaceCloudService;

	@RequestMapping("/first")
	public String first(HttpServletRequest request,HttpServletResponse response,UserEntity ue){
		int ut = ue.getType();
		int p = ue.getProcess();
		if(UserEntity.USER_TYPE_TEACHER==ut){//老师角色
			if(UserEntity.USER_PROCESS_ONE==p){//跳转到老师完善资料
				return "redirect:/teacher/data/findProvinceList";
			}else if(UserEntity.USER_PROCESS_TWO==p){//跳转到老师选择教材版本
				List<TeacherBookVersion> list = teacherInfoService.findTeacherBookVersion(ue);
				if(list!=null&&!list.isEmpty()){//选择教材版本需要传teacherBookVersionId参数
					TeacherBookVersion bv = list.get(0);
					return "redirect:/teacher/choiceSubject/goShowBook?teacherBookVersionId="+bv.getId();
				}
			}else{//老师首页
                if(ue.isFusui()){
                    //扶绥跳转
                    return "redirect:/teacher/choiceSubject/goFusuiHomePage";
                }
				return "redirect:/teacher/choiceSubject/goClasssHomePage";
			}
		}else{
			UserEntity user = loginService.findClassesStudent(ue);
			ue.setClassesId(user!=null?user.getClassesId():0);
			changeUser(ue);//更新用户缓存信息
			if(UserEntity.USER_PROCESS_ONE==p){//跳转到学生加入班级页面
				return "redirect:/student/classes/perfectInfo";
			}else{//学生首页
                request.setAttribute("ue", ue);
                return "redirect:/student/allSubject/findAllSubjectExam";
			}
		}
		request.setAttribute("ue", ue);
		return "/sso";
	}

	@RequestMapping("/loginPage")
	public String loginPage(HttpServletRequest request){
		request.setAttribute("ssoUrl", GlobalConstant.HOSTS_SSO);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		request.setAttribute("uuid", uuid);
		return "/login/login";
	}

    /**
     * 扶绥登录页  locationId=451421  扶绥
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public String fusuiLoginPage(HttpServletRequest request){
        request.setAttribute("ssoUrl", GlobalConstant.HOSTS_SSO);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        request.setAttribute("uuid", uuid);
        return "/login/loginfusui";
    }
	
	/**
	 * 登录验证码正确性验证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/randomLogin")
	@ResponseBody
	public Map randomLogin(HttpServletRequest request,HttpServletResponse response){
		String uuid = request.getParameter("uuid");
		Map map = new HashMap();
		map.put("success", true);
		String random = getParamter("random");
		String randomCache = CacheTools.getCacheNoTime(RedisKey.getLoginVerifyImage(uuid), String.class);
		System.out.println("前端验证码:"+random+"  缓存中的验证码："+randomCache);
		if(StringUtils.isBlank(random) ||StringUtils.isBlank(randomCache) || !random.equalsIgnoreCase(randomCache)){
			map.put("success", false);
		}
		return map;
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public Map login(HttpServletRequest request,HttpServletResponse response){
		Map map = new HashMap();
		String random = getParamter("random");
		String uuid = request.getParameter("uuid");
		String randomCache = CacheTools.getCache(RedisKey.getLoginVerifyImage(uuid), String.class);
		System.out.println("输入的验证码:"+random+"  缓存中的验证码："+randomCache);
        if(StringUtils.isBlank( randomCache )  ){
            map.put("success", false );
            map.put("errMsg", "验证码失效！");
        }else if(StringUtils.isBlank(random) || !random.equalsIgnoreCase(randomCache)){
			//存储业务系统用户信息
			map.put("success", false);
			map.put("errMsg", "验证码错误！");
		}else{
			String mobileEmail = getParamter("mobileEmail");
			String password = getParamter("password");
			UserEntity ue = loginService.findUser(mobileEmail);
			if(ue!=null){
                if( ue.getStatus() == CommonInstence.STATUS_10 ||ue.getStatus() != CommonInstence.STATUS_0 ){
                    map.put("success", false);
                    map.put("errMsg", "你的账户被冻结，请与学校管理员联系！");
                }else{
                    //存储业务系统用户信息
                    CookieUtil.setCookie(request, response, GlobalConstant.AUTH_KEY, ue.getId().toString());
                    CacheTools.addCacheForever(ue.getId().toString(), ue);
                    if( ue.getCityId()!= null && ue.getCityId() == GlobalConstant.FUSUI_LOCATION_ID ){
                        ue.setFusui( true );
                    }
                    if( StringUtils.isEmpty(  ue.getType()+"" ) || null == ue.getType() ){
                        map.put("userType", 0 );
                    }else{
                        map.put("userType", ue.getType() );
                    }
                    map.put("success", true);
                    map.put("system", "cloud");
                    map.put("userName", ue.getUserName());
                    map.put("userId", ue.getId() );
                    map.put("ssoUserId", ue.getUserId() );
                    map.put("password", password);
                    map.put("passwordStatus", ue.getUpdatePasswordStatus() );
                }
			}else{
                //如果云平台不存在此账户则再验证SSO系统验证是否为koolearn的用户
                //----->如果是则通过账户获取koolearn的用户信息查询到云平台库
                //-----> 插入成功后跳转到身份确认页-----> 确认完身份后在跳转到具体的完善资料页面
                UsersDTO usersDTO = getUserDTOByMailAndTe( mobileEmail );
                if( null == usersDTO ){
                    map.put("success", false);
                    map.put("errMsg", "用户名密码错误！");
                }else{
                    //通过账户再查询一次
                    UserEntity yunUser = loginService.findUser(usersDTO.getUserName());
                    int userId = 0;
                    if( yunUser == null ){
                        boolean flag=interfaceCloudService.saveLibUser(usersDTO.getId(), Integer.valueOf(SystemGlobals.getPreference("libraryId")));
                        if(!flag){
                            map.put("success", false);
                            map.put("errMsg", "数字图书馆用户注册失败！");
                            return map;
                        }
                        //官网注册用户：入本地user表
                        userId = addKoolearnUserIntoLocal( usersDTO );
                        map.put("system", "koolearn");
                        map.put("userType", 0 );
                    }else{
                        //官网更新用户：更新koolearn用户属性
                        updateKoolearnUserToLocal( yunUser , usersDTO );
                        map.put("system", "cloud");
                        map.put("userType", yunUser.getType() == null ? 0: yunUser.getType());
                        userId = yunUser.getId();
                    }
                    CookieUtil.setCookie(request, response, GlobalConstant.AUTH_KEY, userId +"");
                    CacheTools.addCacheForever(userId +"", changeUserDto( usersDTO ,yunUser) );
                    map.put("success", true);
                    map.put("userName", usersDTO.getUserName());
                    map.put("password", password);
                    map.put("userId", userId );
                }
			}
		}
		return map;
	}

    /**
     * 修改密码重置提示状态
     * @param
     */
    @RequestMapping("/updateUserPasswordStatus")
    @ResponseBody
    public void updateUserPasswordStatus(HttpServletRequest request,HttpServletResponse response){
        String ssoUserId = request.getParameter( "ssoUserId" );
        if( StringUtils.isNotEmpty( ssoUserId )){
            try{
                loginService.updateUserPasswordStatus( new Integer( ssoUserId ) );
            }catch( Exception e ){
                logger.error( "修改密码重置提示状态" + e.getMessage() , e );
            }
        }
    }

    public UserEntity changeUserDto(UsersDTO usersDTO ,UserEntity yunUser){
        if( null != usersDTO && null != yunUser ){
            yunUser.setMobile( usersDTO.getMobile() );
            yunUser.setEmail(usersDTO.getEmail() );
        }
        return yunUser;
    }

    public UsersDTO getUserDTOByMailAndTe( String userName ){
        UsersDTO usersDTO = null;
        //正则验证userName是邮箱还是电话或者用户名
        if(CheckUtil.isMobileNO( userName )){
            usersDTO = iopenService.getUserByMobile( userName );
        }else if(CheckUtil.isMail(userName)){
            usersDTO = iopenService.getUserByEmail(userName);
        }else{
            usersDTO = iopenService.getUserByName(userName);
        }

        return usersDTO;
    }

    public void updateKoolearnUserToLocal(UserEntity yunUser , UsersDTO usersDTO ){
        if( StringUtils.isNotBlank( usersDTO.getEmail() )){
            yunUser.setEmail( usersDTO.getEmail() );
        }
        if( StringUtils.isNotBlank( usersDTO.getMobile() )){
            yunUser.setMobile( usersDTO.getMobile() );
        }
        //同步koolearn用户信息
        loginService.updateKoolearnUserToLocal( yunUser );
    }
    /**
     * 将koolearn用户信息添加到本地库
     * @param usersDTO
     * @return
     */
    public int addKoolearnUserIntoLocal(UsersDTO usersDTO){
        User user = new User();
        int userId = 0;
        if( null != usersDTO ){
            user.setUserName( usersDTO.getUserName() );
            user.setEmail( usersDTO.getEmail() );
            user.setMobile( usersDTO.getMobile() );
            user.setCreateTime( new Date());
            user.setStatus(CommonInstence.STATUS_0);
            user.setProcess(CommonInstence.USER_PROCESS_1);
            user.setUserId( usersDTO.getId() );
            try{
                 userId = loginService.addSSOUser( user );
            }catch ( Exception e ){}
        }
        return userId;
    }

    /**
     * koolearn注册的用户首次登录需要确认用户身份
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/makeSureUserRole" ,method = RequestMethod.GET)
    public String makeSureUserRole(HttpServletRequest request, HttpServletResponse response){
        String userId = request.getParameter( "userId" );
        request.setAttribute( "userId" , userId );
        return "/login/MakesureUserRole";
    }

    /**
     *记录koolearn用户的角色并跳转到对应的首页
     * @return
     */
    @RequestMapping(value="/addUserRoleAndGoHome" , method = RequestMethod.POST)
    public String addUserRoleAndGoHome(HttpServletRequest request, HttpServletResponse response){
        String userId = request.getParameter( "userId" );
        String roleId = request.getParameter( "roleId" );
        if( StringUtils.isNotBlank( userId ) &&StringUtils.isNotBlank( roleId ) ){
            try {
                int result = loginService.addUserRole( new Integer( userId ) , new Integer( roleId ));
                UserEntity userEntity = loginService.findUserById( new Integer( userId ));
                if( userEntity != null ){
                    changeUser( userEntity );
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error( "记录koolearn用户的角色异常" + e.getMessage() ,e );
                return "redirect:/makeSureUserRole";
            }
            return "redirect:/first";
        }
        return "redirect:/makeSureUserRole";
    }
	/* sso方法 start */
	/**
	 * @title: image
	 * @description: 获取图片验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws
	 */
	@RequestMapping("/random")
	public String genVerifyImage(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String uuid = request.getParameter("uuid");
		String keyCode = "";
		if("2".equals(type)){//注册时验证码类型
			keyCode = RedisKey.getRegistVerifyImage(uuid);
		}else{
			keyCode = RedisKey.getLoginVerifyImage(uuid);
		}
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			int width = 104, height = 40;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			OutputStream os = response.getOutputStream();
			Graphics g = image.getGraphics();
			Random random = new Random();
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);

			g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < 155; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}
			String sRand = "";
			for (int i = 0; i < 4; i++) {
				int randomInt = Math.abs(random.nextInt()) % 36;
				char code = '0';
				if (randomInt > 9) {
					code = (char) ('A' + randomInt - 10);
					// 大写字母按照计划转小写字母
					if (Math.abs(random.nextInt()) % 2 == 1) {
						code = (char) (code + 32);
					}
				} else {
					code = (char) ('0' + randomInt);
				}
				String rand = String.valueOf(code);
				sRand += rand;
				g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(rand, 18 * i + 18, 28);
			}
            //五分钟失效缓存
			CacheTools.addCache(keyCode, ConstantTe.CACHE_TIME_FIVE_MINUTES , sRand);
			g.dispose();

			ImageIO.write(image, "JPEG", os);

			os.flush();
			os.close();
		}
		catch (IllegalStateException e) {
			logger.error("image validate code error");
			e.printStackTrace();
		}
		catch (IOException e) {
			logger.error("image validate code error");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 注册页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/registPage")
	public String registerJsp(HttpServletRequest request){
		int regType = getParamterForInt("role", 2);//1.l老师  2. 学生
		request.setAttribute("regType", regType);
		request.setAttribute("ssoUrl", GlobalConstant.HOSTS_SSO);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		request.setAttribute("uuid", uuid);
		return "/login/register";
	}
    /**
     * 扶绥注册页面
     * @param request
     * @return
     */
    @RequestMapping("/registIndex")
    public String registerFusuiJsp(HttpServletRequest request){
        int regType = getParamterForInt("role", 2);//1.l老师  2. 学生
        request.setAttribute("regType", regType);
        request.setAttribute("ssoUrl", GlobalConstant.HOSTS_SSO);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        request.setAttribute("uuid", uuid);
        return "/login/registerfusui";
    }
	
	/**
	 * 注册方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/regist")
	@ResponseBody
	public Map regist(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		RegistDTO reg = ssoRegister(request,map);
		if(reg==null){//sso注册异常
			return map;
		}
		//本地用户表信息录入
		UserEntity ue = new UserEntity();
		ue.setUserId(reg.getCommender());
		ue.setUserName(reg.getUserName());
		ue.setType(reg.getRole());
		ue.setMobile(reg.getMobile());
		ue.setEmail(reg.getEmail());
		ue.setCreateTime(new Date());
		ue.setProcess(UserEntity.USER_PROCESS_ONE);
		ue.setStatus(UserEntity.USER_STATUS_VALID);
        boolean flag=interfaceCloudService.saveLibUser(ue.getUserId(), Integer.valueOf(SystemGlobals.getPreference("libraryId")));
        if(!flag){
            map.put("success", false);
            map.put("errMsg", "数字图书馆用户注册失败！");
            return map;
        }
		int ueId = loginService.insertUser(ue);
		ue.setId(ueId);
		//存储业务系统用户信息
		CookieUtil.setCookie(request, response, GlobalConstant.AUTH_KEY, ue.getId().toString());
		CacheTools.addCacheForever(ueId+"", ue);

		if(ueId==0){
			map.put("errMsg", "注册失败，重新注册！");
			map.put("success", false);
			return map;
		}else{
			map.put("success", true);
			map.put("ssoUrl", PropertiesConfigUtils.getProperty("hosts.sso"));
		}
		return map;
	}
	
	/**
	 * 注册验证码正确性验证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/randomRegister")
	@ResponseBody
	public Map randomRegister(HttpServletRequest request,HttpServletResponse response){
		Map map = new HashMap();
		map.put("success", true);
		String mobile = getParamter("mobile");
		String randomCode = getParamter("random");
		String uuid = getParamter("uuid");
		int role = getParamterForInt("role", 0);//1.l老师  2. 学生
		String errMsg = "";
		
		try {
			if(role==UserEntity.USER_TYPE_TEACHER){
				SsoUtil.checkMobileCode(randomCode,mobile);//检查手机验证码正确性
			}else{
				SsoUtil.checkEmailCode(randomCode,uuid);
			}
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
			map.put("success", false);       
			return map;
		}		
		return map;
	}
	
	/**
	 * sso注册
	 * @param request
	 * @param map
	 * @return
	 */
	private RegistDTO ssoRegister(HttpServletRequest request, Map map) {
		String errMsg = "";
		String userName = getParamter("userName");
		String password = getParamter("password");
		String mobile = getParamter("mobile");
		String randomCode = getParamter("random");
		String uuid = getParamter("uuid");
		int role = getParamterForInt("role", 0);//1.l老师  2. 学生
		int userId = 0;
		RegistDTO reg = new RegistDTO();
		try {
			if(StringUtils.isNotBlank(mobile)&&mobile.contains("@")){
				reg.setEmail(mobile);
			}else{
				reg.setMobile(mobile);
			}
			if(role==UserEntity.USER_TYPE_TEACHER){
				SsoUtil.checkMobileCode(randomCode,mobile);//检查手机验证码正确性
			}else{
				SsoUtil.checkEmailCode(randomCode,uuid);
			}
			reg.setChannel(CommonInstence.USER_CHANNEL);
			reg.setUserName(userName);
			reg.setPassword(password);
			reg.setRole(role);//老师:1 学生:2
			userId = iopenService.registerUser(reg);
			reg.setCommender(userId);//借用此字段存用户id
		} catch (SSOException e) {
			if(ErrCode.ERROR_MOBILE_EXISTS.equals(e.getCode()) ){//手机号已存在
		    	errMsg = "手机号已存在";
		    }else if(ErrCode.ERROR_PASSWORD_LENGTH.equals(e.getCode())){//密码长度不正确
		    	errMsg = "密码长度不正确";
		    }else if(ErrCode.ERROR_PASSWORD_INVALID_CHAR.equals(e.getCode())){//密码含有非法字符
		    	errMsg = "密码含有非法字符";
		    }else if(ErrCode.ERROR_USER_EXISTS.equals(e.getCode())){//用户名存在
		    	errMsg = "用户名存在";
		    }else if(ErrCode.ERROR_USERNAME_LENGTH.equals(e.getCode())){//用户名长度不对
		    	errMsg = "用户名长度不对";
		    }else if(ErrCode.ERROR_USERNAME_INVALID_CHAR.equals(e.getCode())){//用户名含有非法字符
		    	errMsg = "用户名含有非法字符";
		    }else if(ErrCode.ERROR_REGCODE_OUTTIME.equals(e.getCode())){
		    	errMsg = "手机验证码超时";
		    }else if(ErrCode.ERROR_REGCODE_NOTEXISTS.equals(e.getCode())){
		    	errMsg = "手机验证码输入错误";
		    }else if(ErrCode.ERROR_REGCODE_NULL.equals(e.getCode())){
		    	errMsg = "手机验证码为空";
		    }else if("regCode.error".equals(e.getCode())){
				errMsg = "验证码错误";
			}else{
				errMsg = "网络错误";
			}
			e.printStackTrace();
			map.put("errMsg", errMsg);
			map.put("success", false);       
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errMsg", "注册失败，重新注册！");
			map.put("success", false);       
			return null;
		}
		return reg;
	}

	
	/**
	 * 发送手机验证码生成
	 * @param mobile
	 * @param appendId
	 * @return
	 */
	@RequestMapping("/sendRegisterCode")
	@ResponseBody
	public String sendRegisterCode(String mobile, String appendId) {
        if (!CommonUtil.isMobile(mobile)) {
            return ErrCode.PARAM_MOBILE_INVALID;
        }
        
        String regCode = (String)CacheTools.getCache(RedisKey.mobileVerifyCode(mobile), String.class);
        if (StringUtils.isNotBlank(regCode)) {
        	int minInterval = 30*1000;//最小30秒间隔
            String[] regCodeArr = regCode.split(",");
            long createTime = Long.valueOf(regCodeArr[1]);
            long now = System.currentTimeMillis();
            if (createTime >= now - minInterval) {//发送时间间隔未到点
                return ErrCode.BIZ_MSG_SEND_INTERVAL;
            }
        }

        // 校验一天内最多发送3次(从发的时候开始算起)--修改为每天半夜12点清零
//        Integer times = (Integer) CacheTools.getCache(RedisKey.mobileVerifyCodeTimes(mobile), Integer.class);
//        if (times != null && times >= ConstantCommon.SENDMSG_MAX_TIMES) {
//            return ErrCode.BIZ_SENDMSG_MAXTIMES;
//        }
//        // 累加发送次数
//        if (times != null) {
//            times++;
//        } else {
//            times = 1;
//        }
//        //计算到凌晨12点的间隔
//        Calendar c = new GregorianCalendar();
//    	int hours = c.getActualMaximum(Calendar.HOUR_OF_DAY)-c.get(Calendar.HOUR_OF_DAY);
//    	int minutes = c.getActualMaximum(Calendar.MINUTE)-c.get(Calendar.MINUTE);
//    	int cacheTime = hours*60*60+minutes*60;
//    	if(cacheTime>0){
//    		CacheTools.addCache(RedisKey.mobileVerifyCodeTimes(mobile), cacheTime, times);
//    	}
        // 生成 验证码
        int codeCacheTime = ConstantTe.CACHE_TIME_FIVE_MINUTES;
        String randomCode = null;
        randomCode = SsoUtil.randomMobiCode();
        logger.info("手机验证码:"+randomCode);
        System.out.println("手机验证码:" + randomCode);
        CacheTools.addCache(RedisKey.mobileVerifyCode(mobile), codeCacheTime, (randomCode +","+ System.currentTimeMillis()));
        String errCode = sendMessage(mobile, randomCode, "1");
        if(StringUtils.isNotBlank(errCode)){
        	logger.info("send message failed, errCode:"+errCode);
        	return errCode;
        }
	    return "0";
    }
	
	private String sendMessage(String mobile, String randomCode, String appendId) {
    	String message = null;
		message = "短信验证码："+randomCode+"，5分钟内输入有效。";
		if(logger.isDebugEnabled()){
        	logger.debug("mobile:"+mobile+", sendMessage:"+message);
        }
		try {
            String subSystemtId=PropertiesConfigUtils.getProperty(GlobalConstant.SEND_MESSAGE_SUB_SYSTEMT_ID);
            String departmemtId=PropertiesConfigUtils.getProperty(GlobalConstant.SEND_MESSAGE_DEPARTMENTID);
            int sysid=StringUtils.isBlank(subSystemtId)?16:Integer.parseInt(subSystemtId);
            int depid=StringUtils.isBlank(departmemtId)?16:Integer.parseInt(departmemtId);
            System.out.println("注册发送验证码，短信子系统id"+sysid+",部门id"+depid);
            SendResult result = iSmsClientService.sendMessage(mobile, message, sysid, depid, SMSApplyType.NON_MARKETING);
            if(result!=null){
                System.out.println("注册返回结果 " + result  );
                System.out.println("注册返回结果 " + JSON.toJSONString(result));
                System.out.println("注册返回结果 " + result.getCode()!=null ? +result.getCode().getValue():"返回结果空" );
            }else {
                System.out.println("注册失败！！！！" );
            }

        }
        catch (Exception e) {
	        e.printStackTrace();
            return ErrCode.BIZ_MSG_SEND_FAILED;
        }
        
        return null;
    }


	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255 || fc < 0) {
			fc = 255;
		}
		if (bc > 255 || bc < 0) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	/* sso方法 end */
	/**
	 * 个人中心修改邮箱
	 * 发送邮箱验证码
	 *  fromAddr String 发送邮件地址
     *  fromName String 发件人姓名
     *  toAddr String 接收邮件地址
     *  title String 邮件标题
     *  attachPath String 邮件附件路径
     *  attachName String 邮件附件名
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/sendMail")
	@ResponseBody
	public Map sendMail(HttpServletRequest request,UserEntity ue){
		String uuid = request.getParameter("uuid");
		Map map = new HashMap();
		map.put("success", false);
		if(ue==null){
			return map;
		}
		String title = "修改邮箱";
        String fromAddr = "Eservice@koolearn.com";
		String fromName = "新东方在线";
		String toAddr = request.getParameter("email");
		String random = SsoUtil.randomEmailCode();
		String sendtext = "验证码:"+random;
		String host = "pubmail.koolearn.com";
		try {
			Properties props = new Properties();
	        // 设置smtp服务器地址
	        props.put("mail.smtp.host", host);
	        System.setProperty("mail.mime.charset", "UTF-8");
	        props.put("mail.smtp.auth", "false");
	        // 5秒抛出
	        props.put("mail.smtp.timeout", "5000");
	        props.put("mail.smtp.port", "25");
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.ssl.enable", false);
	        // 创建新邮件
	        Session session = Session.getDefaultInstance(props, null);
	        // 创建过程对象
	        MimeMessage message = new MimeMessage(session);
	        if (fromName != null && !"".equals(fromName)) {
	            message.setFrom(new InternetAddress(fromAddr, fromName));
	        } else {
	            message.setFrom(new InternetAddress(fromAddr));
	        }
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
	        // 设置主题
	        sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			message.setSubject(title);
	        // 如果非附件发送，则只要简单设置内容
	        message.setContent(sendtext, "text/html;charset=UTF-8");
	        message.setSentDate(new Date());
	        message.saveChanges();
	        Transport.send(message, message.getAllRecipients());
        } catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		CacheTools.addCache(RedisKey.getPcenterVerifyCodeEmail(uuid), random);//存储个人中心邮箱验证码
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response,UserEntity loginUser){
		Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    	String authId = authCookie.getValue();
		CacheTools.delCache(authId);
		CookieUtil.deleteCookie(request, response, "sso.ssoId");
		CookieUtil.deleteCookie(request, response, "ssoSessionID");
        if(loginUser.isFusui()){
		    return "redirect:/index";
        }
		return "redirect:/loginPage";
	}
}
