package com.koolearn.cloud.teacher.resource.controller;


import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.util.FileUploadUtils;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ResourceReaderUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;


@Controller
@RequestMapping("/teacher/resource/preview/")
public class TeacherResourcePreviewController {

    private final static Logger logger = Logger.getLogger(TeacherResourcePreviewController.class);
    private static final int CONSUMER_TYPES = 1003003;
    private static final String PC_KEY = "AB22CFF2-E895-4A31-9EE2-CD576EA652A1";
    private static final String MOVE_KEY = "9659972E-C956-4AE3-AE6E-E47F9690AAE1";

    @Autowired
    private ResourceInfoService resourceInfoService;

    @RequestMapping("reader/{resourceId}")
    public String reader(ModelMap modelMap, @PathVariable Integer resourceId, UserEntity user) throws Exception {
        if (resourceId == null) {
            throw new RuntimeException("资源ID不存在");
        }
        ResourceInfo resource = resourceInfoService.searchResourceById(resourceId);
        if (resource == null) {
            throw new RuntimeException("资源不存在");
        }
        if(GlobalConstant.RESOURCE_SOURCE_SYSTEM==resource.getSource().intValue()) {
            resource.setUserStr("新东方教育云");
        }else{
            UserEntity upUser =  resourceInfoService.getUserById(resource.getUploadUserId());
            resource.setUserStr(upUser.getProvinceName()+upUser.getCityName()+"/"+upUser.getSchoolName()+"/"+upUser.getRealName());
        }
        resource.setCollection(resourceInfoService.isCollection(user.getId(), resource.getId(), 1));
        String pageUrl = ResourceReaderUtil.getPageUrl(ResourceReaderUtil.READER_URL_TYPE_T_R,resource, modelMap);
        modelMap.addAttribute("resource", resource);
        return pageUrl;
    }

    /**
     * 附件下载
     *
     * @param request
     * @param response
     */
    @RequestMapping("download/{resourceId}")
    public void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable Integer resourceId) {
        try {
            this.download(request, response, resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private void download(HttpServletRequest request,
                          HttpServletResponse response, Integer resourceId)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis;
        BufferedOutputStream bos;
        ResourceInfo resource = resourceInfoService.getResoueceById(resourceId);
        String realName = resource.getFileOldName();

        String path = FileUploadUtils.getAbsolutePath(resource.getFilePath());
        File file = new File(path);

        long fileLength = file.length();

        response.setContentType(this.getType(realName));
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(realName.getBytes("UTF-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(fileLength));

        bis = new BufferedInputStream(new FileInputStream(file));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }

    /**
     * 根据文件名获取类型（直线运动的图象.pptx）
     *
     * @param filename
     * @return
     * @throws Exception
     */
    private String getType(String filename) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(filename);
    }

}
