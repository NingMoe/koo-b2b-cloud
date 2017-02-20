package com.koolearn.cloud.exam.core.controller;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.exam.entity.DataRebuild;
import com.koolearn.cloud.exam.entity.DataSync;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.teacher.service.FirstLoginChoiceService;
import com.koolearn.cloud.util.*;
import com.koolearn.cloud.util.pdf.ItextpdfUtils;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @classname: QuestionController
 * @description: 试题同步
 * Created by gehaisong on 2016/3/22.
 */
@Controller
@RequestMapping("/exam/core")
public class ExamSynchroController extends BaseController {

    private final Log logger = LogFactory.getLog(ExamSynchroController.class);
    @Autowired
    private TestPaperService testPaperService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FirstLoginChoiceService firstLoginChoiceService;
    @Autowired
    private ResourceInfoService resourceInfoService;
    public FirstLoginChoiceService getFirstLoginChoiceService() {
        return firstLoginChoiceService;
    }

    public void setFirstLoginChoiceService(FirstLoginChoiceService firstLoginChoiceService) {
        this.firstLoginChoiceService = firstLoginChoiceService;
    }

    public ResourceInfoService getResourceInfoService() {
        return resourceInfoService;
    }

    public void setResourceInfoService(ResourceInfoService resourceInfoService) {
        this.resourceInfoService = resourceInfoService;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public TestPaperService getTestPaperService() {
        return testPaperService;
    }

    public void setTestPaperService(TestPaperService testPaperService) {
        this.testPaperService = testPaperService;
    }

    @RequestMapping("/reloadQuestionCache")
    @ResponseBody
    public String reloadQuestionCache(Integer id) throws Exception {
        CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID+id);
        return "根据题目id清除缓存："+ConstantTe.REPOSITORY_QUSTION_ID+id;
    }
    @RequestMapping("/deleteCacheBykey")
    @ResponseBody
    public String deleteCacheBykey(String key) throws Exception {
        CacheTools.delCache(key);
        return "根据题目id清除缓存："+key;
    }

    /**
     * 删除试题 试卷数据
         同步参数： ?rebuild=true&utmterm=0f3d30c8dba7459bb52f2eb5eba8ac7d_0_894f7b6f885e4f7ba46cfe34674b4aa9
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteSyncQuestion")
    @ResponseBody
    public String deleteSyncQuestion(HttpServletRequest request,String key) throws Exception {
        String url=request.getServerName();
        String message="";
        if(isIegitimate(request)&&StringUtils.isNotBlank(url)&&(url.indexOf("release")>-1 || url.indexOf("trunk")>-1)){
            testPaperService.deleteSyncQuestion();
            message="删除成功!";
        }else {
            message="删除失败,只在trunk/release环境可以删除的试卷和题目";
        }

        return "{\"url\":\""+url+"\",\"message\":\""+message+"\"}";
    }

    /**
     * 查看指定题目的标签信息
     * @param modelMap
     * @param qid
     * @return
     * @throws Exception
     */
    @RequestMapping("/questionTag")
    public String questionTag(ModelMap modelMap,Integer qid) throws Exception {
        IExamQuestionDto questionDto=questionService.getQuestionTagsByQid(qid);
        modelMap.addAttribute("questionDto",questionDto);
        return "/examcore/question/whriteQuestion/questiontag";
    }
    private static final String utmterm="0f3d30c8dba7459bb52f2eb5eba8ac7d_0_894f7b6f885e4f7ba46cfe34674b4aa9";//题库，资源同步验证串
    /**
     * 同步exam题目数据
     * 同步参数： ?rebuild=true&utmterm=0f3d30c8dba7459bb52f2eb5eba8ac7d_0_894f7b6f885e4f7ba46cfe34674b4aa9
     * @param request
     * @return
       考试同步接口：SELECT  FROM te_test_paper_info t
                    WHERE UNIX_TIMESTAMP(t.last_update_date) > UNIX_TIMESTAMP('2014-05-11 17:43:33') AND t.tag_full_path LIKE '%63581%'
     * 试卷同步日志：logs/exam.log
     */
    @RequestMapping("/syncQuestion")
    public Object syncQuestion(ModelMap modelMap,HttpServletRequest request) throws Exception {

        DataSync dataSync = null;
        String url ="/examcore/question/syncQuestion";//页面
        //只查看同步信息
        String currentSyncCacheKey= DataSync.getCurrrentCacheKey();
        if(isIegitimate(request)){
            CacheTools.delCache(GlobalConstant.SYNCHRO_CURRENT_KEY_oldsync_data);//每次开始清除上次同步信息
            CacheTools.delCache(currentSyncCacheKey);
            CacheTools.delCache(GlobalConstant.DEFAULT_SYNC_CURRENT_KEY);
            dataSync=testPaperService.syncExamPaper();
            url ="redirect:/exam/core/syncQuestion";
        }else{
            dataSync=CacheTools.getCache(currentSyncCacheKey,DataSync.class);//根据当前缓存key获取同步状态信息
        }
        if(dataSync==null){
            //获取上次同步信息
            dataSync= CacheTools.getCache(GlobalConstant.SYNCHRO_CURRENT_KEY_oldsync_data,DataSync.class);
        }
        modelMap.addAttribute("dataSync",dataSync);
        return url;
    }
    /**
     * 资源索引重建
     *  同步参数： ?rebuild=true&utmterm=0f3d30c8dba7459bb52f2eb5eba8ac7d_0_894f7b6f885e4f7ba46cfe34674b4aa9
     *  */
    @RequestMapping("/rebuildResource")
    public String rebuildResource(ModelMap modelMap,HttpServletRequest request) throws Exception {
        String url ="/examcore/question/syncRebuildresource";
        if(isIegitimate(request)) {
//            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
            ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    resourceInfoService.rebuildSearch();
                }
            });
            url ="redirect:/exam/core/rebuildResource";
        }
        DataRebuild dataSync=  CacheTools.getCache(GlobalConstant.REBUILD_RESOURCE_INFO_KEY,DataRebuild.class);
        modelMap.addAttribute("dataSync",dataSync);
        return url;
    }
    private boolean isIegitimate(HttpServletRequest request){
        String rebuild=request.getParameter("rebuild");//true 走重建流程，否则只拿重建信息
        String um=request.getParameter("utmterm");//验证字符串
        return utmterm.equals(um)&&"true".equals(rebuild);
    }
    @RequestMapping("/searchQuestion")
    public String searchQuestion(ModelMap modelMap,Integer id,QuestionViewDto questionViewDto ,UserEntity user,HttpServletRequest request){
        QuestionFilter pager=new QuestionFilter();
        pager.setQuestionId(id);
        pager.setLoginUser(user);
        IExamQuestionDto questionDto=questionService.searchQuestionByQuestionId(pager);
        modelMap.addAttribute("questionDto",testPaperService.parseSubQuestionPoints(questionDto));
        questionViewDto.setButtonType(QuestionViewDto.button_ype_create_paper);
        modelMap.addAttribute("questionViewDto",questionViewDto);
        return "/examcore/question/questionSingle";
    }
    /**
     *题库列表
     * @param request
     * @return
     */
    @RequestMapping("/questionlib")
    public String questionlib(ModelMap modelMap,QuestionFilter pager,QuestionViewDto questionViewDto ,UserEntity user,HttpServletRequest request){
        try {
            pager.setLoginUser(user);
            if(pager.getQuestionType()==null ||pager.getQuestionType().intValue()<=0){
                List<Tags> tagses=KlbTagsUtil.getInstance().findQuestionType(pager.getSubjectId(),pager.getRangeId());
                pager.setPagerTag(getTagIds(tagses));//搜索全部题型是，加上该学科性所有的题型标签
            }
            pager= questionService.searchQuestion(pager);
            modelMap.addAttribute("questionList", testPaperService.parseSubQuestionPoints( pager.getResultList(),-1,null));
            modelMap.addAttribute("totalRows", pager.getTotalRows());
            modelMap.addAttribute("listPager", pager);
            modelMap.addAttribute("questionViewDto", questionViewDto);
            modelMap.addAttribute("showGoodIco", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/examcore/question/questionList";
    }

    private List<Integer> getTagIds(List<Tags> tagses) {
        if(tagses!=null||tagses.size()>0){
            List<Integer> ids=new ArrayList<Integer>();
            for(Tags t:tagses){
               ids.add(t.getId());
            }
            return ids;
        }
        return null;
    }

    /** 题库列表 换题搜索 */
    @RequestMapping("/questionChange")
    public String questionChange(ModelMap modelMap,QuestionFilter pager,QuestionViewDto questionViewDto ,UserEntity user,HttpServletRequest request){
        try {
            pager.setLoginUser(user);
            pager.setMustNotPaperId(pager.getId());
            questionViewDto.setButtonType(QuestionViewDto.button_ype_question_lib_change);
            pager.setPageSize(10);
            pager= questionService.searchQuestionChange(pager);
            modelMap.addAttribute("questionList", pager.getResultList());
            modelMap.addAttribute("questionViewDto", questionViewDto);
            int questionCount=pager.getResultList()==null?0:pager.getResultList().size();
            modelMap.addAttribute("questionCount", questionCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/examcore/question/questionListChange";
    }
    @RequestMapping("/downloadPaperPDF")
    public void downloadPaperPDF(Integer paperId,String paperName,String downloadType,HttpServletRequest request, HttpServletResponse response,UserEntity ue) throws Exception {
        TestPaper testPaper=testPaperService.downloadPaperPDF(paperId);
        if(testPaper!=null){
            String createPaperName=ue.getId()+"_"+testPaper.getId()+"_"+ ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS);
            testPaper.setDownloadPaperName(createPaperName);
            testPaper.setDownloadType(downloadType);
            testPaperService.generalPaperFrame(testPaper);
            this.download(request,response,testPaper.getDownloadPaperName(),paperName);
        }else {
            this.download(request,response,"nullPaper",paperName);
        }

    }
    /**
     * 下载
     * @param request
     * @param response
     * @param
     * @param fileName  直线运动的图象.pptx
     * @throws Exception
     */
    public static  void download(HttpServletRequest request,
                                 HttpServletResponse response, String downloadPaperName,String fileName)
            throws Exception {
        try {
            if (StringUtils.isNotBlank(fileName)) {
                System.out.println(fileName);
                fileName = URLDecoder.decode(fileName, "UTF-8");
                fileName=fileName.trim();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        fileName=toUtf8String(  request, fileName);
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String realName =fileName+".pdf";
        String path = ItextpdfUtils.downloadPaperPath(downloadPaperName);
        File file = new File(path);
        long fileLength = file.length();
        response.setContentType(getType(realName));
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
        if(file.exists()){
           file.delete();
        }
    }
    /**
     * 根据文件名获取类型（直线运动的图象.pptx）
     * @param filename
     * @return
     * @throws Exception
     */
    public static  String getType(String filename) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filename);
        return mimeType;
    }
    /**
     * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     *
     * @param s
     *            原文件名
     * @return 重新编码后的文件名
     */
    public static String toUtf8String(HttpServletRequest request, String s) {
        String agent = request.getHeader("User-Agent");
        try {
            boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
            if (isFireFox) {
                s = new String(s.getBytes("UTF-8"), "ISO8859-1");
                s = URLDecoder.decode(s, "UTF-8");
            } else {
                s = toUtf8String(s);
                if ((agent != null && agent.indexOf("MSIE") != -1)) {
                    // see http://support.microsoft.com/default.aspx?kbid=816868
                    if (s.length() > 150) {
                        // 根据request的locale 得出可能的编码
                        s = new String(s.getBytes("UTF-8"), "ISO8859-1");
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
    public static String toUtf8String(String s) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    @RequestMapping("/xiaoyangXX")
    public String rebuildUpdateSql(ModelMap modelMap,HttpServletRequest request,String xiaoyang) throws Exception {
        if(isIegitimate(request)) {
            modelMap.addAttribute("xiaoyang",xiaoyang);
            testPaperService.rebuildUpdate(xiaoyang);
            return "/error/xiaoyang";
        }
       return "error";
    }
    /**
     *
     *  参数： ?rebuild=true&utmterm=0f3d30c8dba7459bb52f2eb5eba8ac7d_0_894f7b6f885e4f7ba46cfe34674b4aa9
     *  */
    @RequestMapping("/xiaoyangpage")
    public String rebuildUpdatepage(ModelMap modelMap,HttpServletRequest request,String sql) throws Exception {
        if(isIegitimate(request)) {
            return "/error/xiaoyang";
        }
        return "error";
    }
    public static void main(String[] args) {
        //重建索引
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContex*.xml");
        ResourceInfoService resourceInfoService = context.getBean("resourceInfoService",ResourceInfoService.class);
        resourceInfoService.rebuildSearch();
    }

    /***
     * 云平台知识库导出接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/excelDowload")
    public String excelDowload(ModelMap modelMap,HttpServletRequest request,
                               HttpServletResponse response) {
        String subjectName = request.getParameter("name");
            boolean falg = StringUtils.isBlank(request.getParameter("f")) ? true : false;
        String fileName = falg?"资源数量统计(知识点)": "资源数量统计(进度点)";
        final Map<String, Boolean> paramMap = new HashMap<String, Boolean>();
        paramMap.put(GlobalConstant.loadKonwledge, falg);//true下载知识点,false 进度点
        CacheTools.addCache(GlobalConstant.loadKonwledge,falg);
        //表格字段
        String[] headers = {  "学科", "学段", "类别", "进度点1", "进度点2", "进度点3", "进度点4", "进度点5", "进度点6", "进度点7"};
        if(falg){
            headers = new String[]{  "学科", "学段", "类别", "知识点1", "知识点2", "知识点3", "知识点4", "知识点5", "知识点6", "知识点7"};
        }
        //对象属性
        String[] fields = {  "tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8", "tag9", "tag10"};
        Boolean obj=CacheTools.getCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM_finish+falg, Boolean.class);
        boolean finished=obj==null?false:obj;
        if(isIegitimate(request)) {
            //数据集合
            testPaperService.tongjiResourceNumByKnowledge(paramMap);
        }else{
            Map<String,List<?>>  sheetMap=CacheTools.getCache(GlobalConstant.DOWNLOAD_KNOWLEDGE_RESOURCE_NUMM+falg, Map.class);
            if(finished&&sheetMap!=null){
                if(StringUtils.isBlank(subjectName)){
                    //导出数据
                    POIUtils.exportExcel( headers, fields, sheetMap,  fileName + ".xls", null, response);
                }else {
                    List list=sheetMap.get(subjectName);
                    POIUtils.exportExcel(subjectName,headers, fields,list ,  subjectName+fileName + ".xls", null, response);
                }
            }
        }
        return "/error/tjResourceNum";
    }
}
