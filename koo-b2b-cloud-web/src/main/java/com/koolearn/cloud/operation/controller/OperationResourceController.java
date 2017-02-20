package com.koolearn.cloud.operation.controller;


import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.ChunkUploadEntity;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.entity.ResponseAjaxData;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.operation.entity.OperationUser;
import com.koolearn.cloud.resource.dto.AddResourceBean;
import com.koolearn.cloud.resource.dto.SearchResourceBean;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.resource.dto.UploadFileInfo;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.util.*;
import com.koolearn.common.util.StringUtil;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.klb.tags.entity.Tags;
import com.koolearn.security.client.entity.SecurityUser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 资源库
 */
@Controller
@RequestMapping("/operation/core/resource/")
public class OperationResourceController extends BaseController{

    private static final Logger logger = Logger.getLogger(OperationResourceController.class);

    private static final int TYPE_ONE = 1;
    private static final int TYPE_SIX = 6;
    public static final String STATUS = "status";
    public static final String DATA = "data";
    public static final String MESSAGE = "message";


    @Resource
    private ResourceInfoService resourceInfoService;

    /**
     * 资源库首页
     *
     * @return
     */
    @RequestMapping("index")
    public String index(ModelMap map,HttpServletRequest request,OperationUser user) {
        map.addAttribute("user", user);
        return "/operation/resource/resourceIndex";
    }
    /**资源类型*/
    @RequestMapping("typeList")
    @ResponseBody
    public Map typeList(ModelMap modelMap,HttpServletRequest request) {
        List<Dictionary> typeList = DataDictionaryUtil.getInstance().getDataDictionaryListByType(TYPE_SIX);
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put(DATA , typeList );
        map.put(MESSAGE , "成功");
        map.put(STATUS, CommonInstence.CODE_0);
        return map;
    }

    /**
     * 获取所有学科  用户权限限制
     *
     * @return
     */
    @RequestMapping("getAllSubject")
    @ResponseBody
    public Map getAllSubject() {
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put(DATA , DataDictionaryUtil.getInstance().getDataDictionaryListByTypeOrder(TYPE_ONE) );
        map.put(MESSAGE , "成功");
        map.put(STATUS, CommonInstence.CODE_0);
        return map;
    }

    /**
     * 获取KLB树：获取学段  知识点 和进度点(教材版本／年级等)
     * @param parentId  节点父id  当子节点名称包含知识点或教材目录 则跳过获取下一级节点
     * @param treeType   1 知识点    0 进度点     学段可选
     * @return
     */
    @RequestMapping("getTreeData")
    @ResponseBody
    public List getKlbTree(Integer id,Integer parentId, Integer treeType) {
        if(id==null){
            id=parentId;
        }
        List<TreeBean> returnList=KlbTagsUtil.getInstance().getTagTreeById(id);
        for (TreeBean t : returnList) {
            boolean flag=false;
            if (treeType!=null&&treeType.intValue()==GlobalConstant.KLB_TAB_TYPE_KNOWLEDGE&& t.getName().equals(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME)) {
                returnList= KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                flag=true;
            }else if (treeType!=null&&treeType.intValue()==GlobalConstant.KLB_TAB_TYPE_TEACHING&& t.getName().equals(GlobalConstant.KLB_TAB_TEACHING_NAME)) {
                returnList= KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                flag=true;
            }
            if(flag){
                break;
            }
        }
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put(DATA , returnList );
        map.put(MESSAGE , "成功");
        map.put(STATUS, CommonInstence.CODE_0);
        return returnList;
    }
    /**
     * 上传人列表--获取权限用户列表
     *上传人，默认显示为全部，运营人员、教学总编可以查询所有上传人的资源，学科编辑只能查询本学科编辑、兼职编辑上传的资源，兼职编辑只能查询自己上传的资源
     * @return
     */
    @RequestMapping("uploadUser")
    @ResponseBody
    public Map uploadUser(OperationUser user) {
        List<SecurityUser> securityUsers = getSecurityUserList();
        List<Dictionary> dlist=new ArrayList<Dictionary>();
        if(securityUsers!=null&&!securityUsers.isEmpty()){
            for (SecurityUser su:securityUsers){
                Dictionary d=new Dictionary();
                d.setName(su.getName());
                d.setValue(su.getId());
                dlist.add(d);
            }
        }
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put(DATA, dlist);
        map.put(MESSAGE, "成功");
        map.put(STATUS, CommonInstence.CODE_0);
        return map;
    }



    /**
     * 查询  用户权限过滤？
     *
     * @param bean
     * @return
     */
    @RequestMapping("search")
    @ResponseBody
    public ResponseAjaxData search(SearchResourceBean bean, ModelMap modelMap, OperationUser user) {
        bean.setOperationUser(user);
        Pager pager =new Pager();
        Map<String, Object> map = resourceInfoService.searchResourceList(bean);
        if (map != null) {
            ListPager listPager= (ListPager) map.get("listPager");
            List<ResourceInfo> sourceList = (List<ResourceInfo>) map.get("sourceList");
            for (ResourceInfo r : sourceList) {
                OperationUser upUser = getSecurityUserById(r.getUploadUserId());
                if(upUser!=null){
                    r.setUserStr(upUser.getSecurityUser().getName());
                }else {
                    r.setUserStr("新东方教育云");
                }
            }
            pager.setPageNo(listPager.getPageNo());
            pager.setResultList(sourceList);
            pager.setTotalRows(listPager.getTotalRows());
        }
        ResponseAjaxData responseData=ResponseAjaxData.getResponseAjaxData(pager);
        return responseData;
    }

    /**
     * 上传资源页面
     *
     * @return
     */
    @RequestMapping("uploadResourceIndex")
    public String addResourceIndex(ModelMap map,OperationUser user) {
        map.addAttribute("uuid", UUID.randomUUID());
        map.addAttribute("fuuid", UUID.randomUUID());
        map.addAttribute("user",user);
        return "/operation/resource/resourceUpload";
    }

    /**
     * 修改资源页面
     *
     * @return
     */
    @RequestMapping("editResourceIndex")
    public String editResourceIndex(ModelMap map,Integer resourceId,OperationUser user) {
        map.addAttribute("uuid", UUID.randomUUID());
        map.addAttribute("fuuid", UUID.randomUUID());
        ResourceInfo resource = resourceInfoService.searchResourceById(resourceId);
        resource.setBookVersionTagsEntityList(parseResourceTags(resource.getBookVersionIds()));
        resource.setKonwledgeTagsEntityList(parseResourceTags(resource.getKnowledgeTags()));
        map.addAttribute("resource", resource);//修改资源
        map.addAttribute("marrowYes", GlobalConstant.RESOURCE_MARROW_YES);//修改资源
        map.addAttribute("user",user);
        return "/operation/resource/resourceEdit";
    }

    private List<Tags>  parseResourceTags(Integer[] tagIds){
        List<Tags> tagsList=new ArrayList<Tags>();
        if(tagIds!=null&&tagIds.length>0){
            for (Integer tid:tagIds){
                Tags tag=KlbTagsUtil.getInstance().getCacheTag(tid);
                tagsList.add(tag);
            }
        }
        return  tagsList;
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile")
    public UploadFileInfo uploadFile(MultipartFile file, String uuid, Integer chunk, Integer chunks) {
        UploadFileInfo uf = new UploadFileInfo();
        try {
            // 根据随机整数，生成新文件路径,资源转换文件路径根据资源主键生成
            String fileName = file.getOriginalFilename();
            String extendName = fileName.substring(fileName.lastIndexOf(ParseDate.DELIMITER_ONE_PERIOD) + 1).toLowerCase();
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
                ChunkUploadEntity cue=new ChunkUploadEntity();
                cue.setUf(uf);
                cue.setUuid(uuid);
                cue.setChunk(chunk);
                cue.setChunks(chunks);
                cue.setFileName(fileName);
                cue.setExtendName(extendName);
                cue.setFilePath(filePath);
                chunkUploadFile(file,cue);
            } else {
                FileUploadUtils.saveFileFromInputStream(file.getInputStream(), filePath);
                uf.setStorageSize(file.getSize() / 1024);// 资源存储大小(单位字节: size/1024=K)
            }
            uf.setExtendName(extendName);
            uf.setFilePath(filePath);
            uf.setPageSize(null);// 资源页数
            uf.setFileOldName(fileName);// 原文件名
            uf.setFileNewName(filePath.substring(filePath.lastIndexOf(ParseDate.DELIMITER_ONE_SLASH) + 1));
            uf.setDocumentIcon(FileUploadUtils.getDocumentIcon(uf.getExtendName()));
            uf.setDocumentIconSmall(FileUploadUtils.getDocumentIconSmall(uf.getExtendName()));
        } catch (Exception e) {
            logger.error("",e);
        }
        return uf;
    }

    /**
     * 分片上传
     * @throws IOException
     */
    private void chunkUploadFile(MultipartFile file,ChunkUploadEntity cue) throws IOException {
        // 临时目录用来存放所有分片文件
          UploadFileInfo uf=cue.getUf();
          String uuid=cue.getUuid();
          Integer chunk=cue.getChunk();
          Integer chunks=cue.getChunks();
          String fileName=cue.getFileName();
          String extendName=cue.getExtendName();
          String filePath=cue.getFilePath();
        filePath = FileUploadUtils.getAbsolutePath(filePath);
        File parentFileDir = new File(FileUploadUtils.getAbsolutePath(FileUploadUtils.PATH_PREFIX) + "tmp/" + uuid);
        if (!parentFileDir.exists()) {
            parentFileDir.mkdirs();
        }
        String _fileName = fileName.substring(0, fileName.lastIndexOf(ParseDate.DELIMITER_ONE_PERIOD));
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
        File tempPartFile = new File(parentFileDir, _fileName + ParseDate.DELIMITER_ONE_UNDERLINE + chunk
                + ParseDate.DELIMITER_ONE_PERIOD + extendName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),
                tempPartFile);
        // 是否全部上传完成
        // 所有分片都存在才说明整个文件上传完成
        boolean uploadDone = true;
        File partFile;
        for (int i = 0; i < chunks; i++) {
            partFile = new File(parentFileDir, _fileName + ParseDate.DELIMITER_ONE_UNDERLINE + i
                    + ParseDate.DELIMITER_ONE_PERIOD + extendName);
            if (!partFile.exists()) {
                uploadDone = false;
            }
        }
        // 所有分片文件都上传完成
        // 将所有分片文件合并到一个文件中
        if (uploadDone) {
            File destTempFilePath = new File(filePath);
            if (!destTempFilePath.getParentFile().exists()) {
                destTempFilePath.getParentFile().mkdirs();
            }
            long fileSize = 0;
            FileOutputStream destTempfos;
            for (int i = 0; i < chunks; i++) {
                partFile = new File(parentFileDir, _fileName + ParseDate.DELIMITER_ONE_UNDERLINE
                        + i + ParseDate.DELIMITER_ONE_PERIOD + extendName);
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


    /**
     * 保存资源
     *
     * @param bean
     * @return
     * @throws Exception classVideo
     */
    @RequestMapping(value = "/saveResource")
    @ResponseBody
    public Map saveResource(OperationUser user,@ModelAttribute AddResourceBean bean) {
            Map<String,Object>  map=new HashMap<String, Object>();
        try {
            ResourceInfo resource = generateResource(bean, user);
            resource = resourceInfoService.createResourceInfo(resource);
            if(resource.isCallConver()){
                //转换中或失败的再次调用转换服务
                //文档转换回调白名单接口在老师端/teacher/resource/converCallBack
                conver(resource);
            }
            map.put(STATUS, CommonInstence.CODE_0);
            map.put(MESSAGE , "上传成功");
        } catch (RuntimeException e) {
            map.put(STATUS, CommonInstence.CODE_1);
            map.put(MESSAGE , "上传失败");
            logger.debug("",e);
        }

        return map;
    }

    /**
     * 修改资源
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "/editResource")
    @ResponseBody
    public Map editResource( OperationUser user,@ModelAttribute AddResourceBean bean) {
            Map<String,Object>  map=new HashMap<String, Object>();
        try {
            ResourceInfo resource = resourceInfoService.getResoueceById(bean.getResourceId());
              resource = generateEditResource(resource, bean, user);
            resource = resourceInfoService.updateResourcesInfo(resource);
            if(resource.isCallConver()){
                //转换中或失败的再次调用转换服务
                //文档转换回调白名单接口在老师端/teacher/resource/converCallBack
                conver(resource);
            }
            map.put(STATUS, CommonInstence.CODE_0);
            map.put(MESSAGE , "上传成功");
        } catch (Exception e) {
            logger.debug("修改资源失败",e);
            map.put(STATUS, CommonInstence.CODE_1);
            map.put(MESSAGE , "修改失败");
        }
            return map;
    }
    /**
     * 删除资源
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/delResource")
    public Map delResource(Integer resourceId,OperationUser operationUser) {
        Map<String,Object>  map=new HashMap<String, Object>();
        resourceInfoService.updateResourcesStatus(resourceId, GlobalConstant.RESOURCE_STATUS_UNAVAILABLE);
        map.put(STATUS, CommonInstence.CODE_0);
        map.put(MESSAGE , "上传成功");
        return map;
    }


    private ResourceInfo generateEditResource(ResourceInfo resource, AddResourceBean bean, OperationUser operationUser) {
        int userId=-1;
        if(operationUser.isExistUser()){
            userId=operationUser.getSecurityUser().getId();
        }
        if(StringUtils.isNotBlank(bean.getFilePath())){
            resource.setNewUpfile(true);//新上传的附件
            //不为空是新上传的附件
            resource.setFormat(bean.getFormat());
            resource.setFilePath(bean.getFilePath());
            resource.setExtendName(bean.getExtendName());
            resource.setFileOldName(bean.getFileOldName());
            resource.setFileNewName(bean.getFileNewName());
            resource.setDocumentIcon(bean.getDocumentIcon());
            resource.setDocumentIconSmall(bean.getDocumentIconSmall());
            resource.setStorageSize(bean.getStorageSize());
            resource.setName(bean.getFileOldName().substring(0, bean.getFileOldName().lastIndexOf(ParseDate.DELIMITER_ONE_PERIOD)));
            resource.setStatus(resource.getNewStatus());//根据格式format设置新上传附件状态
        }
        //编辑封面：null取转换的封面
        resource.setFrontcoverUrl(bean.getFrontcoverUrl());
        resource.setType(bean.getType());
        resource.setSource(GlobalConstant.RESOURCE_SOURCE_SYSTEM);
        //标题
        if(StringUtil.isNotBlank(bean.getName())){
            resource.setName(bean.getName());
        }
        //精华
        if(bean.getMarrow()!=null&&bean.getMarrow().intValue()==GlobalConstant.RESOURCE_MARROW_YES){
            resource.setMarrow(GlobalConstant.RESOURCE_MARROW_YES);
        }else {
            resource.setMarrow(GlobalConstant.RESOURCE_MARROW_NO);
        }
        //简介
        resource.setDescription(bean.getDescription());
        resource.setStageTagId(bean.getRangeId());
        resource.setSubjectTagId(bean.getSubjectId());
        resource.setStageTagName(KlbTagsUtil.getInstance().getTagName(bean.getRangeId()));
        resource.setSubjectTagName(KlbTagsUtil.getInstance().getTagName(bean.getSubjectId()));

        //搜索字段
        resource.setTypeName(DataDictionaryUtil.getInstance().getDictionaryName(6, bean.getType()));
        //处理进度点,进度点
        resource.setTagFullPath(KlbTagsUtil.getInstance().getAllTagListFullPath(bean.getKnowlgedIds()) +ParseDate.DELIMITER_ONE_COMMA+
                KlbTagsUtil.getInstance().getAllTagListFullPath(bean.getBookVersionIds()));
        resource = resourceInfoService.parseResourceTagInfo(resource);
        resource.setBookVersionIds(bean.getBookVersionIds());
        resource.setKnowledgeTags(bean.getKnowlgedIds());
        resource.setOptTime(new Date());
        resource.setOptUserId(userId);
        resource.setUpdateTime(resource.getOptTime());
        resource.setUpdateUserId(userId);
        return resource;
    }

    /**
     *创建转换目录并请求转换服务
     * @param resource
     */

    private void conver(ResourceInfo resource)  {
        try {
            // 创建转换目录（/tol/data需要挂载到web）
            FileUploadUtils.createDir(resource.getFileConverPath());
            // 转换文档请求路径、提供网络共享路径 \\\\192.168.102.232\\share\\k12
            String convertUrl = FileUploadUtils.getConvertUrl(FileUploadUtils.CONVER_SERVER_ASK_CONVERSION,
                    resource.getId(), FileUploadUtils.getShareAbsolutePath(resource.getFilePath()),
                    FileUploadUtils.getShareAbsolutePath(resource.getFileConverPath()));
            this.runConverFileTask(convertUrl);
        } catch (UnsupportedEncodingException e) {
            logger.error("",e);
        }
    }

    private void runConverFileTask(final String converUrl) {
        new Thread() {
            @Override
            public void run() {
                try {
                    logger.info("file conver url 文档转换：" + converUrl);
                    DocumentConverUtil.transFile(converUrl);
                    logger.info("resource upload success，start file conver Thead！url\n" + converUrl);
                } catch (Exception e) {
                    logger.info("启动文档转换线程异常！",e);
                }
            }
        }.start();
    }

    private ResourceInfo generateResource(AddResourceBean bean, OperationUser operationUser) {
        int userId=-1;
        if(operationUser.isExistUser()){
              userId=operationUser.getSecurityUser().getId();
        }
        Date now=new Date();
        ResourceInfo resource = new ResourceInfo();
        resource.setDocumentIcon(bean.getDocumentIcon());
        resource.setType(bean.getType());
        resource.setUploadTime(now);
        resource.setUploadUserId(userId);
        resource.setOptTime(now);
        resource.setOptUserId(userId);
        resource.setUpdateTime(now);
        resource.setUpdateUserId(userId);
        resource.setSource(GlobalConstant.RESOURCE_SOURCE_SYSTEM);
        resource.setFilePath(bean.getFilePath());
        resource.setFileConverPath(bean.getFileConverPath());
        resource.setFormat(bean.getFormat());
        resource.setStorageSize(bean.getStorageSize());
        resource.setPageSize(bean.getPageSize());
        resource.setTimeLength(bean.getTimeLength());
        resource.setFileOldName(bean.getFileOldName());
        resource.setFileNewName(bean.getFileNewName());
        resource.setName(bean.getFileOldName().substring(0, bean.getFileOldName().lastIndexOf(ParseDate.DELIMITER_ONE_PERIOD)));
        resource.setFrontcoverUrl(bean.getFrontcoverUrl());
        //标题
        if(StringUtil.isNotBlank(bean.getName())){
            resource.setName(bean.getName());
        }
        //精华
        if(bean.getMarrow()!=null&&bean.getMarrow().intValue()==GlobalConstant.RESOURCE_MARROW_YES){
            resource.setMarrow(GlobalConstant.RESOURCE_MARROW_YES);
        }else {
            resource.setMarrow(GlobalConstant.RESOURCE_MARROW_NO);
        }
        //简介
        resource.setDescription(bean.getDescription());
        resource.setExtendName(bean.getExtendName());

        resource.setStageTagId(bean.getRangeId());
        resource.setSubjectTagId(bean.getSubjectId());
        resource.setStageTagName(KlbTagsUtil.getInstance().getTagName(bean.getRangeId()));
        resource.setSubjectTagName(KlbTagsUtil.getInstance().getTagName(bean.getSubjectId()));
        //搜索字段
        resource.setTypeName(DataDictionaryUtil.getInstance().getDictionaryName(6, bean.getType()));
        //处理进度点,进度点
        resource.setTagFullPath(KlbTagsUtil.getInstance().getAllTagListFullPath(bean.getKnowlgedIds()) + ParseDate.DELIMITER_ONE_COMMA+
                KlbTagsUtil.getInstance().getAllTagListFullPath(bean.getBookVersionIds()));
        resource=resourceInfoService.parseResourceTagInfo(resource);
        resource.setBookVersionIds(bean.getBookVersionIds());
        resource.setKnowledgeTags(bean.getKnowlgedIds());

        resource.setUseTimes(0);
        return resource;
    }



}
