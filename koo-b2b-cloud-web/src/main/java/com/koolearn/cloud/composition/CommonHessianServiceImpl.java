package com.koolearn.cloud.composition;

import com.koolearn.cloud.common.entity.RedisKey;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.mobi.dto.FileUploadResult;
import com.koolearn.cloud.mobi.dto.UserJson;
import com.koolearn.cloud.mobi.service.CommonHessianService;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.FileUtil;
import com.koolearn.cloud.util.SsoUtil;
import com.koolearn.cloud.util.ZipFileUtil;
import com.koolearn.mc.sms.dto.SendResult;
import com.koolearn.mc.sms.enums.SMSApplyType;
import com.koolearn.mc.sms.service.ISmsClientService;
import com.koolearn.sso.dto.RegistDTO;
import com.koolearn.sso.service.IOpenService;
import com.koolearn.sso.util.CommonUtil;
import com.koolearn.sso.util.ErrCode;
import com.koolearn.sso.util.SSOException;
import com.koolearn.tfs.client.TfsException;
import com.koolearn.tfs.client.Tfstool;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Decoder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by haozipu on 2016/7/18.
 */
public class CommonHessianServiceImpl implements CommonHessianService, InitializingBean {

    public static final String CLOUD_BASE_PATH = "/tol/data/cloud/";
    private static Log logger = LogFactory.getLog(CommonHessianServiceImpl.class);

    private String filePath = "/compositionImg";
    private static int userRole = 2;
    @Autowired
    LoginService loginService;
	@Autowired
	IOpenService iopenService;
	@Autowired
	ISmsClientService iSmsClientService;
	
	/**
	 * 获取云平台用户 
	 */
    @Override
    public UserJson loginCloud(String userName) {
    	UserMobi ue = loginService.findMobileUser(userName);
        UserJson uj = new UserJson();
        if (ue == null) { //-1获取用户信息失败 0正常获取用户信息
            uj.setCode("-1");
        } else {
            uj.setCode("0");
        }
        uj.setUe(ue);//用户信息
        return uj;
    }
    /**
     * 用户注册
     */
    @Override
    public UserJson regist(String mobile,String userName,String password,String random){
    	UserJson uj = new UserJson();
		RegistDTO reg = ssoRegister(mobile,userName,password,random);
		if(UserJson.CODE_INVALID.equals(reg.getComStr())){//sso注册异常
			uj.setCode(UserJson.CODE_INVALID);
			uj.setMessge(reg.getCommenderName());
			return uj;
		}
		//本地用户表信息录入
		UserEntity ue = new UserEntity();
		ue.setUserId(reg.getCommender());//代用RegistDTO中的Commender存用户id
		ue.setUserName(reg.getUserName());
		ue.setType(reg.getRole());
		ue.setMobile(reg.getMobile());
		ue.setEmail(reg.getEmail());
		ue.setCreateTime(new Date());
		ue.setProcess(UserEntity.USER_PROCESS_ONE);
		ue.setStatus(UserEntity.USER_STATUS_VALID);
		int ueId = loginService.insertUser(ue);
		ue.setId(ueId);
		if(ueId==0){
			uj.setCode(UserJson.CODE_INVALID);
			uj.setMessge("用户注册失败");
			return uj;
		}else{
			uj.setCode(UserJson.CODE_VALID);
		}
		return uj;
	}
    
    private RegistDTO ssoRegister(String mobile,String userName,String password,String random) {
		String errMsg = "";
		String randomCode = random;
		int userId = 0;
		RegistDTO reg = new RegistDTO();
		try {
			SsoUtil.checkMobileCode(randomCode,mobile);//检查手机验证码正确性
			reg.setMobile(mobile);
			reg.setChannel(CommonInstence.USER_CHANNEL);
			reg.setUserName(userName);
			reg.setPassword(password);
			reg.setRole(userRole);//老师:1 学生:2
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
		    }
			reg.setCommenderName(errMsg);//借用此字段存储错误信息
			reg.setComStr(UserJson.CODE_INVALID);//借用此字段存储错误状态
			e.printStackTrace();
			return reg;
		} catch (Exception e) {
			reg.setComStr(UserJson.CODE_INVALID);
			e.printStackTrace();
			return reg;
		}
		return reg;
	}
    
    /**
     * 发送验证码
     * @param mobile
     * @return
     */
    @Override
    public UserJson sendRegisterCode(String mobile) {
    	UserJson uj = new UserJson();
    	uj.setCode(UserJson.CODE_VALID);
        if (!CommonUtil.isMobile(mobile)) {
        	uj.setCode(UserJson.CODE_INVALID);
        	uj.setMessge(SsoUtil.PARAM_MOBILE_INVALID_MESSGE);
            return uj;
        }
        String regCode = (String)CacheTools.getCache(RedisKey.mobileVerifyCode(mobile), String.class);
        if (StringUtils.isNotBlank(regCode)) {
        	int minInterval = 30*1000;//最小30秒间隔
            String[] regCodeArr = regCode.split(",");
            long createTime = Long.valueOf(regCodeArr[1]);
            long now = System.currentTimeMillis();
            if (createTime >= now - minInterval) {//发送时间间隔未到点
            	uj.setCode(UserJson.CODE_INVALID);
            	uj.setMessge(SsoUtil.BIZ_MSG_SEND_INTERVAL_MESSGE);
                return uj;
            }
        }
        // 生成 验证码
        int codeCacheTime = ConstantTe.CACHE_TIME_FIVE_MINUTES;
        String randomCode = null;
        randomCode = SsoUtil.randomMobiCode();
        CacheTools.addCache(RedisKey.mobileVerifyCode(mobile), codeCacheTime, (randomCode +","+ System.currentTimeMillis()));
        String errCode = sendMessage(mobile, randomCode);
        if(StringUtils.isNotBlank(errCode)){
        	logger.info("send message failed, errCode:"+errCode);
        	uj.setCode(UserJson.CODE_INVALID);
        	uj.setMessge(SsoUtil.BIZ_MSG_SEND_FAILED_MESSGE);
        	return uj;
        }else{
        	uj.setMessge(randomCode);
        	return uj;
        }
    }
    
	private String sendMessage(String mobile, String randomCode) {
    	String message = "短信验证码："+randomCode+"，5分钟内输入有效。";
		if(logger.isDebugEnabled()){
        	logger.debug("mobile:"+mobile+", sendMessage:"+message);
        }
		try {
			SendResult result = iSmsClientService.sendMessage(mobile, message, 16, 16, SMSApplyType.NON_MARKETING);
        }
        catch (Exception e) {
	        e.printStackTrace();
            return ErrCode.BIZ_MSG_SEND_FAILED;
        }
        return null;
    }
	
    @Override
    public String loginOut() {
        return null;
    }

    @Override
    public String resetPassword(String userName, String oldPassword, String newPassword) {
        return null;
    }

    private Tfstool tfstool = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(tfstool==null){
              try {
                  tfstool = Tfstool.getInstance();
              }catch (Exception e){
                  e.printStackTrace();
                  logger.error("初始化tfs客户端出错",e);
              }

        }

    }


    /**
     * 填充上传成功的jsonObject
     *
     * @param jsonObject
     * @param newFileName
     */
    private void fillUploadResult(FileUploadResult jsonObject, String newFileName) {
        jsonObject.setFilePath(filePath + "/" + newFileName);
        jsonObject.setMsg("上传成功");
        jsonObject.setError("0");
    }

    /**
     * 填充上传失败的jsonObject
     *
     * @param jsonObject
     */
    private void fillUploadResultError(FileUploadResult jsonObject,String errorMsg) {
        jsonObject.setMsg(errorMsg);
        jsonObject.setError("1");
    }

    @Override
    public List<String> uploadZipImages(byte[] fileDataBytes, String fileName){
        File file =null;
        try{
            logger.info(fileDataBytes);
            logger.info("上传文件的fileName "+fileName);

            String fileType = FileUtil.getFileType(fileName);
            //校验文件类型
            String newFileName = FileUtil.generatorNewFileName(fileType);

            logger.info("服务器端生成新的文件名称 "+newFileName);

            file = new File(CLOUD_BASE_PATH+newFileName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(fileDataBytes);
            bos.flush();
            bos.close();

            logger.info("写zip文件到 "+file.getAbsolutePath());
            List<String> filePathList = ZipFileUtil.unzipToTfs(CLOUD_BASE_PATH +newFileName);
            return filePathList;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
        }finally {
            if(file!=null){
                file.delete();
            }
        }

        return new ArrayList<String>();
    }

    @Override
    public FileUploadResult uploadFile3(String fileName, String base64Str) {

        FileUploadResult fileUploadResult = new FileUploadResult();

        if (base64Str == null) {
            fillUploadResultError(fileUploadResult,"图片base64字符串传递为空");
            return fileUploadResult;
        }

        if (tfstool.isDirExists(filePath)) {
            String fileType = FileUtil.getFileType(fileName);
            //校验文件类型
            String newFileName = FileUtil.generatorNewFileName(fileType);

            try {
                BASE64Decoder decoder = new BASE64Decoder();
                // Base64解码
                byte[] bytes = new byte[0];
                bytes = decoder.decodeBuffer(base64Str);

                //判断大小逻辑

                if(bytes.length>2*1024*1024){
                    fillUploadResultError(fileUploadResult,"图片最大2M");
                    return fileUploadResult;
                }

                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {// 调整异常数据
                        bytes[i] += 256;
                    }

                }

                tfstool.writeFile(filePath + "/" + newFileName, bytes);

                fillUploadResult(fileUploadResult,filePath+"/"+newFileName);

            }catch (Exception e){
                e.printStackTrace();
                fillUploadResultError(fileUploadResult,"图片上传失败");
            }

        }else{
            try {
                tfstool.createDir(filePath);
                uploadFile3(fileName, base64Str);
            } catch (TfsException e) {
                e.printStackTrace();
                logger.error(e);
                fillUploadResultError(fileUploadResult,"tfs创建图片存储路径失败");
            }
        }
        return fileUploadResult;
    }
}
