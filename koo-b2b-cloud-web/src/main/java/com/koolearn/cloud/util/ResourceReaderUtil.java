package com.koolearn.cloud.util;


import com.koolearn.cloud.resource.entity.ResourceInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;
import org.apache.log4j.Logger;
/**
 * 获取资源预览地址
 *
 */
public class ResourceReaderUtil {
	private final static Logger logger = Logger.getLogger(ResourceReaderUtil.class);
	public static final int READER_URL_TYPE_O_R=0;//运营资源库reader地址
	public static final int READER_URL_TYPE_T_R=1;//老师资源库reader地址
	public static final int READER_URL_TYPE_T_C=2;//老师课堂reader地址
	public static final int READER_URL_TYPE_S_C=3;//学生课堂reader地址

	public static String getPageUrl(int urlType,ResourceInfo resource,ModelMap modelMap) {
		String pageUrl="";
			try {
				if(urlType==READER_URL_TYPE_O_R){
						pageUrl=getPageUrlOR(resource, modelMap);
				}else if(urlType==READER_URL_TYPE_T_R){
					pageUrl=getPageUrlTR(resource, modelMap);
				}else if(urlType==READER_URL_TYPE_T_C){
					pageUrl=getPageUrlTC(resource, modelMap);
				}else if(urlType==READER_URL_TYPE_S_C){
					pageUrl=getPageUrlSC(resource, modelMap);
				}

			} catch (Exception e) {
				logger.debug("失败");
			}
		logger.debug("资源预览页面："+pageUrl);
		return pageUrl;
	}

	/**
	 * 运营端资源库预览地址
	 * @param resource
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	private static String getPageUrlOR(ResourceInfo resource,ModelMap modelMap) throws Exception {
		int format=resource.getFormat();
		String pageUrl;
		if (format == GlobalConstant.RESOURCE_FORMAT_WORD
				|| format == GlobalConstant.RESOURCE_FORMAT_PPT
				|| format == GlobalConstant.RESOURCE_FORMAT_PDF
				|| format == GlobalConstant.RESOURCE_FORMAT_TEXT) {
			pageUrl = "/operation/resource/preview/resourceReader";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO
				&& GlobalConstant.RESOURCE_SOURCE_SYSTEM!=resource.getSource().intValue()) {
			pageUrl = "/operation/resource/preview/resourceReaderMP4";
		} else if(format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM==resource.getSource().intValue()){
			if(StringUtils.isNotBlank(resource.getFilePath())&&resource.getFilePath().startsWith("/cloud")){
				//新运营平台上传资源在本服务器
				pageUrl = "/operation/resource/preview/sysnewResourceReaderMP4";
			}else{
				//老运营平台上传资源在多媒体服务器
				SysMp4Player.getSysMp4Url(resource, modelMap);
				pageUrl = "/operation/resource/preview/sysResourceReaderMP4";
			}
		}else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
			pageUrl = "/operation/resource/preview/resourceReaderMP3";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
			pageUrl = "/operation/resource/preview/resourceReaderImage";
		} else {
			pageUrl = "/operation/resource/preview/resourceReader";
		}
		return pageUrl;
	}


	/**
	 * 获取老师资源库预览地址
	 * @param resource
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	private static String getPageUrlTR(ResourceInfo resource,ModelMap modelMap) throws Exception {
		int format=resource.getFormat();
		String pageUrl;
		if (format == GlobalConstant.RESOURCE_FORMAT_WORD
				|| format == GlobalConstant.RESOURCE_FORMAT_PPT
				|| format == GlobalConstant.RESOURCE_FORMAT_PDF
				|| format == GlobalConstant.RESOURCE_FORMAT_TEXT) {
			pageUrl = "/teacher/preview/resourceReader";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO
				&& GlobalConstant.RESOURCE_SOURCE_SYSTEM!=resource.getSource().intValue()) {
			pageUrl = "/teacher/preview/resourceReaderMP4";
		} else if(format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM==resource.getSource().intValue()){
			if(StringUtils.isNotBlank(resource.getFilePath())&&resource.getFilePath().startsWith("/cloud")){
				//新运营平台上传资源在本服务器
				pageUrl = "/teacher/preview/sysnewResourceReaderMP4";
			}else{
				//老运营平台上传资源在多媒体服务器
				SysMp4Player.getSysMp4Url(resource, modelMap);
				pageUrl = "/teacher/preview/sysResourceReaderMP4";
			}
		}else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
			pageUrl = "/teacher/preview/resourceReaderMP3";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
			pageUrl = "/teacher/preview/resourceReaderImage";
		} else {
			pageUrl = "/teacher/preview/resourceReader";
		}
		return pageUrl;
	}

	/**
	 * 老师课堂资源预览地址
	 * @param resource
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	private static String getPageUrlTC( ResourceInfo resource, ModelMap modelMap) throws Exception {
		int format=resource.getFormat();
		String pageUrl;
		if (format == GlobalConstant.RESOURCE_FORMAT_WORD
				|| format == GlobalConstant.RESOURCE_FORMAT_PPT
				|| format == GlobalConstant.RESOURCE_FORMAT_PDF
				|| format == GlobalConstant.RESOURCE_FORMAT_TEXT) {
			pageUrl = "teacher/classRoom/preview/classRoomReader";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM!=resource.getSource().intValue()) {
			pageUrl = "teacher/classRoom/preview/classRoomReaderMP4";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM==resource.getSource().intValue()) {
			if(StringUtils.isNotBlank(resource.getFilePath())&&resource.getFilePath().startsWith("/cloud")){
				//新运营平台上传资源在本服务器
				pageUrl = "teacher/classRoom/preview/sysnewclassRoomReaderMP4";
			}else{
				//老运营平台上传资源在多媒体服务器
				SysMp4Player.getSysMp4Url(resource, modelMap);
				pageUrl = "teacher/classRoom/preview/sysClassRoomReaderMP4";
			}
		} else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
			pageUrl = "teacher/classRoom/preview/classRoomReaderMP3";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
			pageUrl = "teacher/classRoom/preview/classRoomReaderImage";
		} else {
			pageUrl = "teacher/classRoom/preview/classRoomReader";
		}
		return pageUrl;
	}
	/**
	 * 学生课堂资源预览地址
	 * @param resource
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	private static String getPageUrlSC(ResourceInfo resource,ModelMap modelMap) throws Exception {
		int format=resource.getFormat();
		String pageUrl;
		if (format == GlobalConstant.RESOURCE_FORMAT_WORD
				|| format == GlobalConstant.RESOURCE_FORMAT_PPT
				|| format == GlobalConstant.RESOURCE_FORMAT_PDF
				|| format == GlobalConstant.RESOURCE_FORMAT_TEXT) {
			pageUrl = "student/classRoom/classRoomReader";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM!=resource.getSource().intValue()) {
			pageUrl = "student/classRoom/classRoomReaderMP4";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_VIDEO && GlobalConstant.RESOURCE_SOURCE_SYSTEM==resource.getSource().intValue()) {
			if(StringUtils.isNotBlank(resource.getFilePath())&&resource.getFilePath().startsWith("/cloud")){
				//新运营平台上传资源在本服务器
				pageUrl = "student/classRoom/sysnewclassRoomReaderMP4";
			}else{
				//老运营平台上传资源在多媒体服务器
				SysMp4Player.getSysMp4Url(resource, modelMap);
				pageUrl = "student/classRoom/sysClassRoomReaderMP4";
			}

			SysMp4Player.getSysMp4Url(resource, modelMap);
		} else if (format == GlobalConstant.RESOURCE_FORMAT_AUDIO) {
			pageUrl = "student/classRoom/classRoomReaderMP3";
		} else if (format == GlobalConstant.RESOURCE_FORMAT_IMAGE) {
			pageUrl = "student/classRoom/classRoomReaderImage";
		} else {
			pageUrl = "student/classRoom/classRoomReader";
		}
		return pageUrl;
	}
}
