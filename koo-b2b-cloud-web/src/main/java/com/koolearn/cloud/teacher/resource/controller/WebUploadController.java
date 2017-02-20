package com.koolearn.cloud.teacher.resource.controller;
import com.koolearn.cloud.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * 资源库
 */
@Controller
@RequestMapping("/web/upload/")
public class WebUploadController {

    private static final Logger logger = Logger.getLogger(WebUploadController.class);


    /**上传*/
    @RequestMapping("add")
    public String addResourceIndex(ModelMap map) {
        map.addAttribute("uuid", UUID.randomUUID());
        return "/js/webuploader/upload";
    }


    /** 上传文件
     *
     * @param file
     * @param uuid
     * @param chunk   当前分片
     * @param chunks  分片总数
     * @param userName 扩展其他参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile")
    public UploadInfo uploadFile(MultipartFile file, String uuid, Integer chunk, Integer chunks,String userName) {
        UploadInfo uf = new UploadInfo();
        try {
            // 根据随机整数，生成新文件路径,资源转换文件路径根据资源主键生成
            String fileName = file.getOriginalFilename();
            String extendName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            int format = FileUploadUtils.getModuleValue(extendName);
            String filePath = FileUploadUtils.getPathByRandomIdAndType(extendName, format);
            uf.setFormat(format);
            if (!FileUploadUtils.rightSizeOfDocPptTxt(format, file.getSize())) {
                //文件过大
                uf.setError(true);
                File parentFileDir = new File(FileUploadUtils.getAbsolutePath(FileUploadUtils.PATH_PREFIX) + "tmp/" + uuid);
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);
                return uf;
            }
            if (chunks != null) {
                //chunked: true 且文件不小于分片
                chunkUploadFile(uf, file, uuid, chunk, chunks, fileName, extendName, filePath);
            } else {
                //chunked: false 或 文件小于分片
                FileUploadUtils.saveFileFromInputStream(file.getInputStream(), filePath);
                uf.setStorageSize(file.getSize() / 1024);// 资源存储大小(单位字节: size/1024=K)
            }
            uf.setExtendName(extendName);
            uf.setFilePath(filePath);
            uf.setPageSize(null);// 资源页数
            uf.setFileOldName(fileName);// 原文件名
            uf.setFileNewName(filePath.substring(filePath.lastIndexOf("/") + 1));
            uf.setDocumentIcon(FileUploadUtils.getDocumentIcon(uf.getExtendName()));
            uf.setDocumentIconSmall(FileUploadUtils.getDocumentIconSmall(uf.getExtendName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uf;
    }

    /**
     * 分片上传
     *
     * @param file
     * @param uuid
     * @param chunk   当前分片
     * @param chunks  分配个数
     * @param fileName
     * @param extendName
     * @param filePath
     * @throws java.io.IOException
     */
    private void chunkUploadFile(UploadInfo uf, MultipartFile file, String uuid, Integer chunk, Integer chunks, String fileName, String extendName, String filePath) throws IOException {
        // 临时目录用来存放所有分片文件
        filePath = FileUploadUtils.getAbsolutePath(filePath);
        File parentFileDir = new File(FileUploadUtils.getAbsolutePath(FileUploadUtils.PATH_PREFIX) + "tmp/" + uuid);
        if (!parentFileDir.exists()) {
            parentFileDir.mkdirs();
        }
        String _fileName = fileName.substring(0, fileName.lastIndexOf("."));
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
        File tempPartFile = new File(parentFileDir, _fileName + "_" + chunk
                + "." + extendName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),
                tempPartFile);
        // 是否全部上传完成
        // 所有分片都存在才说明整个文件上传完成
        boolean uploadDone = true;
        File partFile;
        for (int i = 0; i < chunks; i++) {
            //判断所有分片是否都存在
            partFile = new File(parentFileDir, _fileName + "_" + i
                    + "." + extendName);
            if (!partFile.exists()) {
                uploadDone = false;
                continue;
            }
        }
        // 所有分片文件都上传完成 ，将所有分片文件合并到一个文件中
        if (uploadDone) {
            File destTempFilePath = new File(filePath);
            if (!destTempFilePath.getParentFile().exists()) {
                destTempFilePath.getParentFile().mkdirs();
            }
            long fileSize = 0;
            FileOutputStream destTempfos;
            for (int i = 0; i < chunks; i++) {
                partFile = new File(parentFileDir, _fileName + "_"
                        + i + "." + extendName);
                fileSize += partFile.length();
                destTempfos = new FileOutputStream(filePath, true);
                FileUtils.copyFile(partFile, destTempfos);
                destTempfos.close();
            }
            // 删除临时目录中的分片文件
            FileUtils.deleteDirectory(parentFileDir);
            uf.setStorageSize(fileSize / 1024);// 资源存储大小(单位字节: size/1024=K)
        }
    }
}
/**WebUploadcontroller  End**/
/**上传文件信息实体 Begin**/
class UploadInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filePath;// 文件相对路径路径(包含第三方资源路径)
    private boolean error;//true 附件过大
    private String fileConverPath;// 资源文件转换目录
    private Integer format;//资源格式
    private Long storageSize;//资源存储大小
    private Integer pageSize;//资源页数
    private Integer timeLength;//资源时长
    private String fileOldName;//原文件名
    private String fileNewName;//新文件名
    private String extendName;//扩展名
    private String documentIcon;//文档icon图片名称
    private String documentIconSmall;//文档icon小图片名称（不含扩展名）
    private String tooBigSizeInfo;//附件过大提示信息
    private String frontcoverUrl;// 封面图片路径(资源图片缩略图)
    private Integer status = 0;
    public Integer getStatus() {return status;}
    public void setStatus(Integer status) {this.status = status;}
    public String getFilePath() {return filePath; }
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public String getFileConverPath() {return fileConverPath;}
    public void setFileConverPath(String fileConverPath) {this.fileConverPath = fileConverPath;}
    public Integer getFormat() {return format;}
    public void setFormat(Integer format) {this.format = format;}
    public Long getStorageSize() {return storageSize;}
    public void setStorageSize(Long storageSize) {this.storageSize = storageSize;}
    public Integer getPageSize() { return pageSize;}
    public void setPageSize(Integer pageSize) {this.pageSize = pageSize;}
    public Integer getTimeLength() {return timeLength;}
    public void setTimeLength(Integer timeLength) {this.timeLength = timeLength;}
    public String getFileOldName() { return fileOldName;}
    public void setFileOldName(String fileOldName) {this.fileOldName = fileOldName; }
    public String getFileNewName() {return fileNewName;}
    public void setFileNewName(String fileNewName) {this.fileNewName = fileNewName;}
    public String getExtendName() { return extendName;}
    public void setExtendName(String extendName) {this.extendName = extendName;}
    public String getDocumentIcon() { return documentIcon;}
    public void setDocumentIcon(String documentIcon) {this.documentIcon = documentIcon; }
    public String getDocumentIconSmall() { return documentIconSmall;}
    public void setDocumentIconSmall(String documentIconSmall) {this.documentIconSmall = documentIconSmall;}
    public String getTooBigSizeInfo() { return tooBigSizeInfo;}
    public void setTooBigSizeInfo(String tooBigSizeInfo) {this.tooBigSizeInfo = tooBigSizeInfo;}
    public boolean isError() {return error;}
    public void setError(boolean error) {this.error = error; }
    public String getFrontcoverUrl() {return frontcoverUrl;}
    public void setFrontcoverUrl(String frontcoverUrl) {this.frontcoverUrl = frontcoverUrl;}
}
/**上传文件信息实体 end**/