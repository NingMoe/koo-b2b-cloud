package com.koolearn.cloud.mobi.service;

import com.koolearn.cloud.mobi.dto.FileUploadResult;
import com.koolearn.cloud.mobi.dto.UserJson;

import java.util.List;

/**
 * 通用的手机端 hessian接口
 * Created by haozipu on 2016/7/18.
 */
public interface CommonHessianService {

    /**
     * 获取云用户信息操作
     * @param userName 用户名
     * @return json串
     */
    public UserJson loginCloud(String userName);

    /**
     * 登出操作
     * @return
     */
    public String loginOut();

    /**
     * 重置密码
     * @param userName 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    public String resetPassword(String userName,String oldPassword,String newPassword);


    /**
     * 上传一个文件 base64 字符串
     * @param fileName
     * @param base64Str
     * @return
     */
    public FileUploadResult uploadFile3(String fileName,String base64Str);

    /**
     * 上传一个zip文件
     * @param fileDataBytes 打包图片的zip包
     * @param fileName zip文件的文件名 比如 images.zip
     * @return
     */
    public List<String> uploadZipImages(byte[] fileDataBytes,String fileName);

    /**
     * 用户注册接口
     * @param mobile
     * @param userName
     * @param password
     * @param random
     * @return
     */
	UserJson regist(String mobile, String userName, String password,String random);
	/**
	 * 发送验证码接口
	 * @param mobile
	 * @return
	 */
	UserJson sendRegisterCode(String mobile);


}
