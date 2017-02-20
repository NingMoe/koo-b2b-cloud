package com.koolearn.cloud.teacher.resource.controller;


import com.koolearn.cloud.common.entity.ChunkUploadEntity;
import com.koolearn.cloud.common.serializer.CommonEnum;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dto.AddResourceBean;
import com.koolearn.cloud.resource.dto.SearchResourceBean;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.resource.dto.UploadFileInfo;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.util.*;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/teacher/resource/")
public class TeacherResourceController {

    private static final Logger logger = Logger.getLogger(TeacherResourceController.class);

    private static final int TYPE_ONE = 1;
    private static final int TYPE_SIX = 6;
    private static final int TYPE_SEVEN = 7;
    private static final int TYPE_ENGIT = 8;
    private  String urlTypeMap="urlType";
    private  String typeListMap="typeList";
    private  String sourceListMap="sourceList";

    @Resource
    private ResourceInfoService resourceInfoService;

    /**
     * 资源库首页
     *
     * @return
     */
    @RequestMapping("index")
    public String index(ModelMap map, Integer urlType, Integer saveType,HttpServletRequest request) {
        Integer urlTypeA = urlType == null ? 0 : urlType;
        String resourceType=request.getParameter("type");
        List<Dictionary> formatList = getDataDictionaryListByType(TYPE_SEVEN);
        List<Dictionary> typeList = getDataDictionaryListByType(TYPE_SIX);
        List<Dictionary> questionFilterList = getDataDictionaryListByType(TYPE_ENGIT);
        map.addAttribute(urlTypeMap, urlTypeA);
        map.addAttribute("saveType", saveType == null ? -1 : saveType);
        map.addAttribute(typeListMap, typeList);
        map.addAttribute("formatList", formatList);
        map.addAttribute("questionFilterList", questionFilterList);
        map.addAttribute("subjectId", request.getParameter("subjectId"));
        map.addAttribute("rangeId", request.getParameter("rangeId"));
        map.addAttribute("bookVersion", request.getParameter("bookVersion"));
        map.addAttribute("resourceType",StringUtils.isBlank(resourceType)?-1:resourceType);

        return "/teacher/resource/resourceIndex";
    }

    /**
     * 资源库知识点首页
     *
     * @return
     */
    @RequestMapping("knowledgeIndex")
    public String knowledgeIndex(ModelMap map, Integer urlType) {
        int urlTypeA = urlType == null ? 0 : urlType;
        List<Dictionary> formatList = getDataDictionaryListByType(TYPE_SEVEN);
        List<Dictionary> typeList = getDataDictionaryListByType(TYPE_SIX);
        List<Dictionary> questionFilterList = getDataDictionaryListByType(TYPE_ENGIT);

        map.addAttribute(urlTypeMap, urlTypeA);
        map.addAttribute(typeListMap, typeList);
        map.addAttribute("formatList", formatList);
        map.addAttribute("questionFilterList", questionFilterList);

        return "/teacher/resource/resourceKnowledgeIndex";
    }

    private List<Dictionary> getDataDictionaryListByType(int type) {
        return DataDictionaryUtil.getInstance().getDataDictionaryListByType(type);
    }

    /**
     * 获取教材进度点学科
     *
     * @return
     */
    @RequestMapping("getSubject")
    @ResponseBody
    public List<SelectDTO> getSubject(UserEntity user) {
        return DataSelectUtil.getInstance().getSubject(user.getId());
    }

    /**
     * 获取所有学科
     *
     * @return
     */
    @RequestMapping("getAllSubject")
    @ResponseBody
    public List<Dictionary> getAllSubject() {
        return DataDictionaryUtil.getInstance().getDataDictionaryListByTypeOrder(TYPE_ONE);
    }

    /**
     * 获取教材进度点年级
     *
     * @return
     */
    @RequestMapping("getRange")
    @ResponseBody
    public List<SelectDTO> getRange(UserEntity user, Integer subjectId, Integer id) {
        int subjectIdA = subjectId == null ? id : subjectId;
        return DataSelectUtil.getInstance().getRange(user.getId(), subjectIdA);
    }

    /**
     * 获取所有年级
     *
     * @return
     */
    @RequestMapping("getAllRange")
    @ResponseBody
    public List getAllRange(Integer subjectId ,UserEntity user) {
        int schoolId = user.getSchoolId();
        //查询学校学段
        Integer grandId = resourceInfoService.findSchoolInfoById( schoolId );
        return getTagByShoolGrandId( subjectId , grandId );
    }

    public List< Tags > getTagByShoolGrandId(Integer subjectId ,Integer grandId){
        List< Tags > list = KlbTagsUtil.getInstance().getTagById(subjectId);
        List< Tags > lastList = new ArrayList<Tags>( );//存放最后匹配的学段
        if( null != list ){
            for( int i = 0 ; i < list.size();i++ ){
                String name = list.get( i ).getName();
                if(CommonEnum.getRangeNameEnum.getSource( grandId ).getValue().indexOf( name ) != -1 ){
                    lastList.add( list.get( i ));
                }
            }
        }
        return lastList;
    }

    /**
     * 选择教材版本和知识库
     *
     * @param rangeId
     * @param klbType
     * @return
     */
    @RequestMapping("getBookVersion")
    @ResponseBody
    public List getBookVersion(UserEntity user, Integer rangeId, Integer klbType, Integer id) {
        int rangeIdA = rangeId == null ? id : rangeId;
        if (klbType == null || klbType == 0) {
            return DataSelectUtil.getInstance().getBookVersion(user.getId(), rangeIdA);
        } else {
            List<Tags> lists = KlbTagsUtil.getInstance().getTagById(rangeIdA);
            if (lists != null && !lists.isEmpty()) {
                parse( lists);
            }
            return lists;
        }
    }

    private List parse(List<Tags> lists){
        for (Tags t : lists) {
            if (t.getName().equals(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME)) {
                return KlbTagsUtil.getInstance().getTagById(t.getId());
            }
        }
        return new ArrayList();
    }

    /**
     * 选择所有教材版本和知识库
     *
     * @param rangeId
     * @param klbType
     * @return
     */
    @RequestMapping("getAllBookVersion")
    @ResponseBody
    public List getAllBookVersion(Integer rangeId, Integer klbType) {
        List<Tags> lists = KlbTagsUtil.getInstance().getTagById(rangeId);
            if (klbType != null&&lists != null && !lists.isEmpty()) {
                return parseTaglist(  klbType,  lists);
            }
        return lists;
    }
private List parseTaglist(Integer klbType,List<Tags> lists){
    if (klbType == 0) {
        for (Tags t : lists) {
            if ("教材目录".equals(t.getName())) {
                return KlbTagsUtil.getInstance().getTagById(t.getId());
            }
        }
    } else {
        for (Tags t : lists) {
            if ("知识点".equals(t.getName())) {
                return KlbTagsUtil.getInstance().getTagById(t.getId());
            }
        }
    }
    return new ArrayList();
}
    /**
     * 获取必修选择
     *
     * @param bookVersion
     * @return
     */
    @RequestMapping("getObligatory")
    @ResponseBody
    public List getObligatory(Integer bookVersion) {
        return KlbTagsUtil.getInstance().getTagById(bookVersion);
    }


    /**
     * 获取树
     *
     * @return
     */
    @RequestMapping("getTree")
    @ResponseBody
    public List getTree(Integer id, Integer obligatory, Integer klbType) {
        Integer parentId = id != null ? id : obligatory;
        List<Tags> lists = KlbTagsUtil.getInstance().getTagById(parentId);
        if (lists != null && !lists.isEmpty()) {
            for (Tags t : lists) {
                if (klbType != null && klbType == GlobalConstant.KLB_TAB_TYPE_KNOWLEDGE && t.getName().equals(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME)) {
                    return KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                }else if (klbType != null && klbType == GlobalConstant.KLB_TAB_TYPE_TEACHING&&t.getName().equals(GlobalConstant.KLB_TAB_TEACHING_NAME)) {
                    return KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                }
            }
        }

        return KlbTagsUtil.getInstance().getTagTreeById(parentId);
    }

    /**
     * 根据父节点id获取树并级联下一级子节点
     *
     * @return
     */
    @RequestMapping("getTreeRefChild")
    @ResponseBody
    public List getTreeRefChild(Integer id) {
        List<TreeBean> tagsList = KlbTagsUtil.getInstance().getTagTreeById(id);
        if (tagsList != null && !tagsList.isEmpty()) {
            for (int i = 0; i < tagsList.size(); i++) {
                TreeBean tb = tagsList.get(i);
                tb.setChild(KlbTagsUtil.getInstance().getTagTreeById(tb.getId()));
            }
        }
        return tagsList;
    }

    public static final String empty="[]";
    @RequestMapping("getStr")
    @ResponseBody
    public String getStr() {
        return empty;
    }

    /**
     * 查询
     *
     * @param bean
     * @return
     */
    @RequestMapping("search")
    public String search(SearchResourceBean bean, ModelMap modelMap, UserEntity user) {
        bean.setUserEntity(user);
        Map<String, Object> map = resourceInfoService.searchResourceList(bean);
        if (map != null) {
            modelMap.addAttribute("listPager", map.get("listPager"));
            List<ResourceInfo> sourceList = (List<ResourceInfo>) map.get(sourceListMap);
            UserEntity upUser ;
            for (ResourceInfo r : sourceList) {
                if(GlobalConstant.RESOURCE_SOURCE_SYSTEM==r.getSource()) {
                    r.setUserStr("新东方教育云");
                }else{
                    upUser = resourceInfoService.getUserById(r.getUploadUserId());
                    r.setUserStr(upUser.getProvinceName() + upUser.getCityName() + "/" + upUser.getSchoolName() + "/" + upUser.getRealName());
                }
                r.setCollection(resourceInfoService.isCollection(user.getId(), r.getId(), 1));
            }
            modelMap.addAttribute(sourceListMap, map.get(sourceListMap));
            modelMap.addAttribute("searchCount", map.get("searchCount"));
        }
        modelMap.addAttribute(urlTypeMap, bean.getUrlType());
        return "/teacher/resource/resourceList";
    }

    /**
     * 上传资源页面
     *
     * @return
     */
    @RequestMapping("addResourceIndex")
    public String addResourceIndex(ModelMap map) {
        List<Dictionary> typeList = getDataDictionaryListByType(TYPE_SIX);

        map.addAttribute(typeListMap, typeList);
        map.addAttribute("uuid", UUID.randomUUID());
        return "/teacher/resource/addResource";
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
            String extendName = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
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
                chunkUploadFile(file, cue);
            } else {
                FileUploadUtils.saveFileFromInputStream(file.getInputStream(), filePath);
                uf.setStorageSize(file.getSize() / 1024);// 资源存储大小(单位字节: size/1024=K)
            }
            uf.setExtendName(extendName);
            uf.setFilePath(filePath);
            uf.setPageSize(null);// 资源页数
            uf.setFileOldName(fileName);// 原文件名
            uf.setFileNewName(filePath.substring(filePath.lastIndexOf('/') + 1));
            uf.setDocumentIcon(FileUploadUtils.getDocumentIcon(uf.getExtendName()));
            uf.setDocumentIconSmall(FileUploadUtils.getDocumentIconSmall(uf.getExtendName()));
        } catch (Exception e) {
            logger.error("",e);
        }
        return uf;
    }

    /**
     * 分片上传
     *
     * @param file
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
        String _fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
        File tempPartFile = new File(parentFileDir, _fileName + '_' + chunk
                + '.' + extendName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),
                tempPartFile);
        // 是否全部上传完成
        // 所有分片都存在才说明整个文件上传完成
        boolean uploadDone = true;
        File partFile;
        for (int i = 0; i < chunks; i++) {
            partFile = new File(parentFileDir, _fileName + '_' + i
                    + '.' + extendName);
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
                partFile = new File(parentFileDir, _fileName + '_'
                        + i + '.' + extendName);
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
     * 文档转换回调处理
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/converCallBack")
    public String converCallBack(HttpServletRequest request) {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        //转换服务，1为成功，0为失败
        int statusInt = StringUtils.isBlank(status) ? 1 : Integer.parseInt(status) + 1;
        resourceInfoService.updateResourcesStatus(Integer.parseInt(id), statusInt);
        logger.info("resource callback,id=" + id + ",status=" + status);
        return null;
    }


    /**
     * 保存资源
     *
     * @param bean
     * @return
     * @throws Exception classVideo
     */
    @RequestMapping(value = "/saveResource")
    public String saveResource(
            UserEntity user,
            @ModelAttribute AddResourceBean bean) {
        try {
            ResourceInfo resource = generateResource(bean, user);
            resource = resourceInfoService.createResourceInfo(resource);
            if(resource.isCallConver()){
                conver(resource);
            }

        } catch (Exception e) {
            logger.debug("上传资源",e);
        }
        return "redirect:/teacher/resource/index?subjectId="+bean.getSubjectId()+"&rangeId="+bean.getRangeId()+"&bookVersion="+bean.getBookVersion();
    }

    private void conver(ResourceInfo resource) throws UnsupportedEncodingException {
        // 创建转换目录（/tol/data需要挂载到web）
        FileUploadUtils.createDir(resource.getFileConverPath());
        // 转换文档请求路径、提供网络共享路径 \\\\192.168.102.232\\share\\k12
        String convertUrl = FileUploadUtils.getConvertUrl(FileUploadUtils.CONVER_SERVER_ASK_CONVERSION,
                resource.getId(), FileUploadUtils.getShareAbsolutePath(resource.getFilePath()),
                FileUploadUtils.getShareAbsolutePath(resource.getFileConverPath()));
        this.runConverFileTask(convertUrl);
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

    private ResourceInfo generateResource(AddResourceBean bean, UserEntity user) {
        ResourceInfo resource = new ResourceInfo();
        Date now=new Date();
        resource.setDocumentIcon(bean.getDocumentIcon());
        resource.setType(bean.getType());
        resource.setUploadTime(now);
        resource.setUploadUserId(user.getId());
        resource.setOptTime(now);
        resource.setOptUserId(user.getId());
        resource.setUpdateTime(now);
        resource.setUpdateUserId(user.getId());
        resource.setSource(bean.getShareType());
        resource.setFilePath(bean.getFilePath());
        resource.setFileConverPath(bean.getFileConverPath());
        resource.setFormat(bean.getFormat());
        resource.setStorageSize(bean.getStorageSize());
        resource.setPageSize(bean.getPageSize());
        resource.setTimeLength(bean.getTimeLength());
        resource.setFileOldName(bean.getFileOldName());
        resource.setFileNewName(bean.getFileNewName());
        resource.setName(bean.getFileOldName().substring(0, bean.getFileOldName().lastIndexOf('.')));
        resource.setExtendName(bean.getExtendName());

        resource.setStageTagId(bean.getRangeId());
        resource.setSubjectTagId(bean.getSubjectId());
        resource.setStageTagName(KlbTagsUtil.getInstance().getTagName(bean.getRangeId()));
        resource.setSubjectTagName(KlbTagsUtil.getInstance().getTagName(bean.getSubjectId()));

        resource.setSchoolId(user.getSchoolId());
        //搜索字段
        resource.setTypeName(DataDictionaryUtil.getInstance().getDictionaryName(6, bean.getType()));
        int tagId = bean.getBookVersionIds()[0];
        resource.setTagFullPath(KlbTagsUtil.getInstance().getTagFullId(tagId));
        resource.setBookVersionIds(bean.getBookVersionIds());
        resource.setBookVersionNames(KlbTagsUtil.getInstance().getCacheTagFullPathName(
                KlbTagsUtil.getInstance().getTagFullId(tagId),
                GlobalConstant.KLB_TEACHING_NAME_OFFSET));
        resource.setUseTimes(0);
        resource.setKlbType(GlobalConstant.KLB_TYPE_BOOKVERSION);//教材进度点
        return resource;
    }

    /**
     * 收藏
     *
     * @param resourceId
     * @param user
     * @return
     */
    @RequestMapping("collection/{resourceId}")
    @ResponseBody
    public String collection(@PathVariable Integer resourceId, UserEntity user) {
        String success = "false";
        if (user != null) {
            resourceInfoService.collection(resourceId, user);
            success = "true";
        }
        return success;

    }

    /**
     * 选择
     *
     * @param resourceId
     * @param user
     * @return
     */
    @RequestMapping("use/{resourceId}")
    @ResponseBody
    public String use(@PathVariable Integer resourceId, UserEntity user) {
        String success = "false";
        if (user != null && resourceId != null) {
            resourceInfoService.use(resourceId, user);
            success = "true";
        }
        return success;
    }

//***************************************************************************************************

    /**
     * 翻转课堂上传文件
     *
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "classRoom/uploadFile")
    public ResourceInfo classRoomUploadFile(MultipartFile file, String uuid,
                                            Integer chunk, Integer chunks,
                                            Integer subjectId, Integer rangeId,
                                            Integer[] bookVersionIds, UserEntity user) {
        AddResourceBean uf = new AddResourceBean();
        uf.setSubjectId(subjectId);
        uf.setRangeId(rangeId);
        uf.setBookVersionIds(bookVersionIds);
        ResourceInfo resource = null;
        try {
            // 根据随机整数，生成新文件路径,资源转换文件路径根据资源主键生成
            String fileName = file.getOriginalFilename();
            String extendName = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            int format = FileUploadUtils.getModuleValue(extendName);
            uf.setFormat(format);
            if (!FileUploadUtils.rightSizeOfDocPptTxt(format, file.getSize())) {
                File parentFileDir = new File(FileUploadUtils.getAbsolutePath(FileUploadUtils.PATH_PREFIX) + "tmp/" + uuid);
                // 删除临时目录中的分片文件
                FileUtils.deleteDirectory(parentFileDir);
                //文件过大
                resource = new ResourceInfo();
                resource.setError(true);
                return resource;
            }

            String filePath = FileUploadUtils.getPathByRandomIdAndType(extendName, format);
            if (chunks != null) {
                ChunkUploadEntity cue=new ChunkUploadEntity();
                cue.setUuid(uuid);
                cue.setChunk(chunk);
                cue.setChunks(chunks);
                cue.setFileName(fileName);
                cue.setExtendName(extendName);
                cue.setFilePath(filePath);
                resource = classRoomChunkUploadFile( resource,  uf,file, user, cue);
            } else {
                FileUploadUtils.saveFileFromInputStream(file.getInputStream(), filePath);
                uf.setStorageSize(file.getSize() / 1024);// 资源存储大小(单位字节: size/1024=K)
                resource = setClassRoomFileValue(user, uf, resource, fileName, extendName, filePath);
            }


        } catch (Exception e) {
            logger.error("",e);
        }
        return resource;
    }

    private ResourceInfo classRoomChunkUploadFile(ResourceInfo resource, AddResourceBean uf,
                                                  MultipartFile file,UserEntity user,ChunkUploadEntity cue) throws IOException {
        String uuid=cue.getUuid();
        Integer chunk=cue.getChunk();
        Integer chunks=cue.getChunks();
        String fileName=cue.getFileName();
        String extendName=cue.getExtendName();
        String filePath=cue.getFilePath();
        // 临时目录用来存放所有分片文件
        String _filePath = FileUploadUtils.getAbsolutePath(filePath);
        File parentFileDir = new File(FileUploadUtils.getAbsolutePath(FileUploadUtils.PATH_PREFIX) + "tmp/" + uuid);
        if (!parentFileDir.exists()) {
            parentFileDir.mkdirs();
        }
        String _fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
        File tempPartFile = new File(parentFileDir, _fileName + '_' + chunk
                + '.' + extendName);
        FileUtils.copyInputStreamToFile(file.getInputStream(),
                tempPartFile);
        // 是否全部上传完成
        // 所有分片都存在才说明整个文件上传完成
        boolean uploadDone = true;
        File partFile;
        for (int i = 0; i < chunks; i++) {
            partFile = new File(parentFileDir, _fileName + '_' + i
                    + '.' + extendName);
            if (!partFile.exists()) {
                uploadDone = false;
            }
        }
        // 所有分片文件都上传完成
        // 将所有分片文件合并到一个文件中
        if (uploadDone) {
            File destTempFilePath = new File(_filePath);
            if (!destTempFilePath.getParentFile().exists()) {
                destTempFilePath.getParentFile().mkdirs();
            }
            long fileSize = 0;
            FileOutputStream destTempfos;
            for (int i = 0; i < chunks; i++) {
                partFile = new File(parentFileDir, _fileName + '_'
                        + i + '.' + extendName);
                fileSize += partFile.length();
                destTempfos = new FileOutputStream(_filePath, true);
                FileUtils.copyFile(partFile, destTempfos);
                destTempfos.close();
            }
            // 删除临时目录中的分片文件
            FileUtils.deleteDirectory(parentFileDir);
            uf.setStorageSize(fileSize / 1024);// 资源存储大小(单位字节: size/1024=K)
            resource = setClassRoomFileValue(user, uf, resource, fileName, extendName, filePath);
        }
        return resource;
    }


    private ResourceInfo setClassRoomFileValue(UserEntity user, AddResourceBean uf, ResourceInfo resource, String fileName, String extendName, String filePath) {
        uf.setExtendName(extendName);
        uf.setFilePath(filePath);
        uf.setPageSize(null);// 资源页数
        uf.setFileOldName(fileName);// 原文件名
        uf.setFileNewName(filePath.substring(filePath.lastIndexOf('/') + 1));
        uf.setDocumentIcon(FileUploadUtils.getDocumentIcon(uf.getExtendName()));
        uf.setDocumentIconSmall(FileUploadUtils.getDocumentIconSmall(uf.getExtendName()));
        uf.setType(6);//其他
        uf.setShareType(2);
        resource = resourceInfoService.createResourceInfo(generateResource(uf, user));
        // 创建转换目录（/tol/data需要挂载到web）
        try {
            conver(resource);
        } catch (UnsupportedEncodingException e) {
            logger.debug("",e);
        }
        return resource;
    }


}
