package com.koolearn.cloud.exam.core.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.service.FirstLoginChoiceService;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @classname: QuestionController
 * @description: 试题同步
 * Created by gehaisong on 2016/3/22.
 */
@Controller
@RequestMapping("/teacher/exam/core")
public class QuestionChroController extends BaseController {

    private final Log logger = LogFactory.getLog(QuestionChroController.class);
    @Autowired
    private TestPaperService testPaperService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FirstLoginChoiceService firstLoginChoiceService;

    public FirstLoginChoiceService getFirstLoginChoiceService() {
        return firstLoginChoiceService;
    }

    public void setFirstLoginChoiceService(FirstLoginChoiceService firstLoginChoiceService) {
        this.firstLoginChoiceService = firstLoginChoiceService;
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

    /**
     * 获取学科题型
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/questionType")
    @ResponseBody
    public List questionShowType(HttpServletRequest request,QuestionFilter filter) throws Exception {
        return KlbTagsUtil.getInstance().findQuestionType(filter.getSubjectId(),filter.getRangeId());
    }
    /**
     * 获取获取使用年级
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/shiyongGrade")
    @ResponseBody
    public List shiyongGrade(HttpServletRequest request,QuestionFilter filter) throws Exception {
        return KlbTagsUtil.getInstance().findQuestionType(filter.getSubjectId(),filter.getRangeId());
    }
    /**
     * 获取题目难度
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/questionHard")
    @ResponseBody
    public List questionHard(HttpServletRequest request,QuestionFilter filter) throws Exception {
        return KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_QUESTION_HARD);
    }


    /**
     * 加入试卷库
     * @param modelMap
     * @param pager
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/addPaperLib")
    public String addPaperLib(ModelMap modelMap,PaperPager pager,HttpServletRequest request) throws Exception {
        pager=testPaperService.findPaperList(pager);
        modelMap.addAttribute("paperList", pager.getResultList());
        modelMap.addAttribute("totalRows", pager.getTotalRows());
        modelMap.addAttribute("listPager", pager);
        return "/examcore/questionlib/paper/paperList";
    }
    /**
     * 教学进度点组题
     * @param request
     * @return
     */
    @RequestMapping("/question/jdd")
    public String questionJdd(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request){
        List<Tags> qhard=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_QUESTION_HARD);
        modelMap.addAttribute("questionHard",qhard);
        modelMap.addAttribute("questionFilter",questionFilter);
        modelMap.addAttribute("buttonType", StringUtils.isBlank(request.getParameter("buttonType"))?"1":request.getParameter("buttonType"));
        return "/examcore/questionlib/question1_jdd";
    }
    /**
     * 知识点组题
     * @param request
     * @return
     */
    @RequestMapping("/question/zsd")
    public String questionZsd(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request){
        List<Tags> qhard=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_QUESTION_HARD);
        modelMap.addAttribute("questionHard",qhard);
        modelMap.addAttribute("questionFilter",questionFilter);
        return "/examcore/questionlib/question2_zsd";
    }
    /**
     * 试卷组题
     * @param request
     * @return
     */
    @RequestMapping("/question/sjzt")
    public String questionSjzt(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request,UserEntity user){
        List<Tags> area=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_KLBSX_AREA);
        List<Tags> year=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_KLBSX_YEAR);
        List<Tags> paperType=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_KLBSX_PAPERTYPE);
        Map<String,List<Tags>> subjectRange=questionService.findSubjectRangeOfPaper(user);
        modelMap.addAttribute("area",area);
        modelMap.addAttribute("year",year);
        modelMap.addAttribute("paperType",paperType);
        modelMap.addAttribute("grade",subjectRange.get("grade"));
        modelMap.addAttribute("subject",subjectRange.get("subject"));
        modelMap.addAttribute("questionFilter",questionFilter);
//        String p="2456_2455_2454_2443_40,2326_2325_2301_20,2239_10,104827_2341_20,63581_2415_20,2455_2454_2443_40,101915_2379_20";
//        Map<String ,Tags> s=questionService.getSubjectByPaperFullPath( p);//根据试卷fullpath获取知识点树学科学段
        return "/examcore/questionlib/question3_sjzt";
    }
    /**
     * 智能组题
     * @param request
     * @return
     */
    @RequestMapping("/question/znzt")
    public String questionZnzt(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request){
        request.getSession().removeAttribute("paperAuto");//智能组卷消费后删除
        List<Tags> qhard=KlbTagsUtil.getInstance().findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_QUESTION_HARD);
        modelMap.addAttribute("questionHard",qhard);
        modelMap.addAttribute("questionFilter",questionFilter);
        return "/examcore/questionlib/question4_znzt";
    }
    /**
     * 智能组题题型
     * @param request
     * @return
     */
    @RequestMapping("/question/znztType")
    public String questionZnztType(ModelMap modelMap,HttpServletRequest request,QuestionFilter filter){
        List<Tags> questionType=KlbTagsUtil.getInstance().findQuestionType(filter.getSubjectId(),filter.getRangeId());
        modelMap.addAttribute("questionType",questionType);
        return "/examcore/questionlib/question4_znzt_type";
    }
    /**
     * 智能组题 跳转到试卷创建页面
     * @param request
     * @return
     */
    @RequestMapping("/question/toCreatePaper")
    public String questionZnztType(ModelMap modelMap,HttpServletRequest request,PaperPager paper,UserEntity loginUser){
       // subjectId  rangeId bookVersion questionHard pagerTag数组  questionCount：题型_数量
        QuestionFilter qf=pTOQFilter(paper);
        qf.setLoginUser(loginUser);
        qf= questionService.searchQuestionAuto(qf,getKeyOfPaperBar(paper,loginUser));
        paper.setTitle("智能组卷"+ ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS));
        modelMap.addAttribute("questionList", qf.getResultList());
        request.getSession().setAttribute("paperAuto",paper);
        RedirectView view=new RedirectView();
        return "redirect:/teacher/exam/core/paper/create";
    }
    /**
     * 智能组题 验证当前条件是否有题
     * @param request
     * @return
     */
    @RequestMapping("/question/znyzts")
    @ResponseBody
    public String questionZnztTypeYan(ModelMap modelMap,HttpServletRequest request,PaperPager paper,UserEntity loginUser){
        // subjectId  rangeId bookVersion questionHard pagerTag数组  questionCount：题型_数量
        QuestionFilter qf=pTOQFilter(paper);
        qf.setLoginUser(loginUser);
        qf= questionService.searchQuestionAuto(qf,getKeyOfPaperBar(paper,loginUser));
        return "{\"hasQuestion\":\""+qf.isHasQuestion()+"\"}";
//        return "{\"hasQuestion\":\"true\"}";
    }
    private QuestionFilter pTOQFilter(PaperPager paper) {
        QuestionFilter qf=new QuestionFilter();
        qf.setSubjectId(paper.getSubjectId());
        qf.setRangeId(paper.getRangeId());
        qf.setBookVersion(paper.getBookVersion());
        qf.setObligatoryId(paper.getObligatoryId());
        qf.setQuestionHard(paper.getQuestionHard());
        qf.setPagerTag(paper.getPagerTag());
        qf.setQuestionCount(paper.getQuestionCount());//题型题目数量
        return qf;
    }

    /**
     * 本校试卷
     * @param request
     * @return
     */
    @RequestMapping("/question/bxsj")
    public String questionBxsj(ModelMap modelMap,HttpServletRequest request){
        List<TeacherBookVersion>  teacherBookVersions = firstLoginChoiceService.findTeacherSubject(2);
        modelMap.addAttribute("teacherBookVersions",teacherBookVersions);
        return "/examcore/questionlib/question5_bxsj";
    }
    /**
     * 我的试卷
     * @param request
     * @return
     */
    @RequestMapping("/question/wdsj")
    public String questionWdsj(ModelMap modelMap,HttpServletRequest request){
        List<TeacherBookVersion>  teacherBookVersions = firstLoginChoiceService.findTeacherSubject(2);
        modelMap.addAttribute("teacherBookVersions",teacherBookVersions);
        return "/examcore/questionlib/question6_wdsj";
    }
    /**
     * 试卷查询列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/findPaperList")
    public String findPaperList(ModelMap modelMap,PaperPager pager,HttpServletRequest request,UserEntity loginUser) throws Exception {
        pager.setPageSize(OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE);
        pager.setLoginUser(loginUser);
        if(pager.getSearchFrom().equals(GlobalConstant.PAPER_SEARCH_TYPE_XDF)){
            //试卷组卷 过滤学科学段
            Map<String,List<Tags>> subjectRange=questionService.findSubjectRangeOfPaper(loginUser);
            parseSubjectGrade(pager,subjectRange);
        }
        pager=questionService.searchPaper(pager);
//        pager=testPaperService.findPaperList(pager);
        modelMap.addAttribute("paperList", pager.getResultList());
        modelMap.addAttribute("totalRows", pager.getTotalRows());
        modelMap.addAttribute("listPager", pager);
        if(GlobalConstant.PAPER_SEARCH_TYPE_WDSJ.equals(pager.getSearchFrom())){
            return "/examcore/questionlib/paper/paperWdsjList";
        }
        if(GlobalConstant.PAPER_SEARCH_TYPE_BXSJ.equals(pager.getSearchFrom())){
            return "/examcore/questionlib/paper/paperBxsjList";
        }
        return "/examcore/questionlib/paper/paperList";
    }

    private void parseSubjectGrade(PaperPager pager, Map<String, List<Tags>> subjectRange) {
        List<Tags> subjectList=subjectRange.get("subject");
        List<Tags> gradeList=subjectRange.get("grade");
        if(pager.getPaperTagIds()!=null && pager.getPaperTagIds().length>0){
            List<Integer> listo= Arrays.asList(pager.getPaperTagIds());
            ArrayList<Integer> list = new ArrayList<Integer>(listo);
           if(list.contains(GlobalConstant.PAPER_SEARCH_TYPE_XDF_Subject_all)){
               //学科全部条件
               if(subjectList!=null&&subjectList.size()>0){
                   for(Tags t:subjectList){
                       pager.getSjztGJAll().add(t.getId());//加载全部学科查询条件
                   }
               }
               list.remove(GlobalConstant.PAPER_SEARCH_TYPE_XDF_Subject_all);
               Integer[] tags={};
               pager.setPaperTagIds(list.toArray(tags));//清除学科全部标识
            }else{
               //单选学科
               if(subjectList!=null&&subjectList.size()>0){
                   for(Integer tj:list){
                       boolean has=false;
                       for(Tags t:subjectList){
                           if(tj!=null&&tj.intValue()==t.getId()){
                               has=true;
                               break;
                           }
                       }
                       if(has){
                           pager.getSjztGJAll().add(tj);
                           list.remove(tj);
                           Integer[] tags={};
                           pager.setPaperTagIds(list.toArray(tags));//清除已选的学科或学段
                           break;
                       }
                   }
               }
           }
            listo= Arrays.asList(pager.getPaperTagIds());
            list = new ArrayList<Integer>(listo);
            if(list.contains(GlobalConstant.PAPER_SEARCH_TYPE_XDF_Grade_all)){
                if(gradeList!=null&&gradeList.size()>0){
                    for(Tags t:gradeList){
                        pager.getSjztGJGradeAll().add(t.getId());//学段搜索条件
                    }
                }
                list.remove(GlobalConstant.PAPER_SEARCH_TYPE_XDF_Grade_all);
                Integer[] tags={};
                pager.setPaperTagIds(list.toArray(tags));//删除学段全部标识，保留地区年份等搜索条件
            }else{
                //单选学科
                if(gradeList!=null&&gradeList.size()>0){
                    for(Integer tj:list){
                        boolean has=false;
                        for(Tags t:gradeList){
                            if(tj!=null&&tj.intValue()==t.getId()){
                                has=true;
                                break;
                            }
                        }
                        if(has){
                            pager.getSjztGJGradeAll().add(tj);
                            list.remove(tj);
                            Integer[] tags={};
                            pager.setPaperTagIds(list.toArray(tags));//清除已选的学科或学段
                            break;
                        }
                    }
                }
            }
        }
    }

    /** *题库收藏  */
    @RequestMapping("question/collection")
    @ResponseBody
    public  boolean questionCollection(ModelMap modelMap,QuestionFilter pager,UserEntity user,HttpServletRequest request){
        try {
                questionService.collection(pager,user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /** *题库使用次数 */
    @RequestMapping("/question/use")
    @ResponseBody
    public boolean questionUsetimes(ModelMap modelMap,QuestionFilter pager,UserEntity user,HttpServletRequest request){
        try {
            questionService.saveUsetimes(pager.getQuestionId(), user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getKeyOfPaperBar(PaperPager paperFilter,UserEntity loginUser){
        return OnlyExamConstant.TEMPLATE_PREFIX+loginUser.getId()+"_"+paperFilter.getSubjectId()+"_"+paperFilter.getRangeId();
    }
}
