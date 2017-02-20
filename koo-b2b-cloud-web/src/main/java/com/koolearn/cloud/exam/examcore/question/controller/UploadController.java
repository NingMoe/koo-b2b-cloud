package com.koolearn.cloud.exam.examcore.question.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 上传文件控制器
 * @author wangpeng
 * @date Oct 30, 2012
 * 技术教辅社区组@koolearn.com
 */
@Controller
@RequestMapping("/maintain/upload2")
public class UploadController extends BaseController {
	private final int IMAGE_SIZE=1024*1024*2;
	private static String BASE_DIR=PropertiesConfigUtils.getProperty("upload_base_dir");
	/**
	 * 上传图片
	 * @return
	 */
	@RequestMapping("/image")
	public String uploadImage(ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile filemark = multipartRequest.getFile("NewFile");
		String fileName=null;
		try {
			byte[] bb=filemark.getBytes();
			if(bb.length>IMAGE_SIZE){
				//TODO .... 处理图片过大
			}
			System.out.println("图片大小:"+bb.length);
			String name=filemark.getOriginalFilename();
			name=name.substring(name.lastIndexOf(".")+1);
			fileName=System.currentTimeMillis()+"."+name;
			 //判断目标文件所在的目录是否存在  
			File fileD = new File(BASE_DIR+"/image/");
	        if(!fileD.exists()) {  
	        	fileD.mkdirs(); 
	        }
	        FileCopyUtils.copy(bb, new File(BASE_DIR+"/image/"+fileName));
			System.out.println("图片上传："+BASE_DIR+"/image/"+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path="/upload/image/";
		String name=fileName;
		String msg="OKLA";
		String message=findMessage(0,path, name, msg);
		printWriterAjax(response, message);
		return null;
	}
	private String findMessage(int status,String path,String name,String msg){
		StringBuilder sb=new StringBuilder();
		sb.append("<script type=\"text/javascript\">");
//		sb.append("window.parent.OnUploadCompleted(0,'http://y0.ifengimg.com/mappa/2012/10/30/5e9cbf5b257a56b47f7b7298622c4e8a.jpg','5e9cbf5b257a56b47f7b7298622c4e8a.jpg');");
		sb.append("window.parent.OnUploadCompleted("+status+",'"+path+name+"','"+name+"','"+msg+"');");
		sb.append("</script>");
		sb.append("");
		return sb.toString();
	}
	/**
	 * 上传媒体
	 * @return
	 */
	@RequestMapping("/media")
	public String uploadMedia(ModelMap modelMap, HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile filemark = multipartRequest.getFile("NewFile");
		String fileName=null;
		try {
			byte[] bb=filemark.getBytes();
			if(bb.length>IMAGE_SIZE){
				//TODO .... 处理图片过大
			}
			String name=filemark.getOriginalFilename();
			name=name.substring(name.lastIndexOf(".")+1);
			fileName=System.currentTimeMillis()+"."+name;
			File fileD = new File(BASE_DIR+"/video/");
	        if(!fileD.exists()) {  
	        	fileD.mkdirs(); 
	        }
			FileCopyUtils.copy(bb, new File(BASE_DIR+"/video/"+fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
//		String path="http://localhost:8080/exam2012";
		String path="/upload/video/";
		String name=fileName;
		String msg="OKLA";
		String message=findMessage(0,path, name, msg);
		printWriterAjax(response, message);
		return null;
	}
	/**
	 * 选项上传图片
	 */
	@RequestMapping("/optImg")
	public String upload2OptionImg(ModelMap modelMap, HttpServletRequest request,HttpServletResponse response){
		String num = request.getParameter("randomNum");
		MultipartFile filemark=findUpFile("imagefile1", request);
		String url=findLoadedName(filemark,"image");
		printWriterAjax(response, callBackScript(url,"image",num));
		return null;
	}
	/**
	 * 选项上传mp3
	 */
	@RequestMapping("/optMp3")
	public String upload2OptionMp3(ModelMap modelMap, HttpServletRequest request,HttpServletResponse response){
		String num = request.getParameter("randomNum");
		MultipartFile filemark=findUpFile("mp3file2", request);
		String url=findLoadedName(filemark,"video");
		printWriterAjax(response, callBackScript(url,"video",num));
		return null;
	}
	
	private String callBackScript(String url,String partPath,String num){
		StringBuilder sb=new StringBuilder(100);
		sb.append("<script>");
		sb.append("parent.uploadCallBack('"+url+"','"+partPath+"','"+num+"')");
		sb.append("</script>");
		return sb.toString();
	}
	
	
	private MultipartFile findUpFile(String UpFileName,HttpServletRequest request){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile filemark = multipartRequest.getFile(UpFileName);
		return filemark;
	}
	private String findLoadedName(MultipartFile filemark,String partPath) {
		String name=filemark.getOriginalFilename();
		name=name.substring(name.lastIndexOf(".")+1);
		String fileName=System.currentTimeMillis()+"."+name.toLowerCase();
		try {
			byte[] bb=filemark.getBytes();
			File fileD = new File(BASE_DIR+"/"+partPath+"/");
	        if(!fileD.exists()) {  
	        	fileD.mkdirs(); 
	        }
			FileCopyUtils.copy(bb, new File(BASE_DIR+"/"+partPath+"/"+fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path="/upload/"+partPath+"/";
		String url=path+fileName;
		return url;
	}
}
