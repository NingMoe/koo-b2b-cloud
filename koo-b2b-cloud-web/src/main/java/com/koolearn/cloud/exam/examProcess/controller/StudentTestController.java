package com.koolearn.cloud.exam.examProcess.controller;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.vo.StudentResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.paper.vo.SimpleStructureVo;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.student.StudentClassHomeService;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.dto.TaskPager;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/student/test")
public class StudentTestController  extends BaseController {
    @Autowired
    private StudentClassHomeService studentClassHomeService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    protected TestPaperService testPaperService;
    @Resource
    TaskService taskService;
    @Autowired
    protected QuestionBaseService questionBaseService;

    public TestPaperService getTestPaperService() {
        return testPaperService;
    }

    public void setTestPaperService(TestPaperService testPaperService) {
        this.testPaperService = testPaperService;
    }

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    public QuestionService getQuestionService() {
        return questionService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public StudentClassHomeService getStudentClassHomeService() {
        return studentClassHomeService;
    }

    public void setStudentClassHomeService(StudentClassHomeService studentClassHomeService) {
        this.studentClassHomeService = studentClassHomeService;
    }

    /**
     *组题自测
     * @param request
     * @return
     */
    @RequestMapping("/self")
    public String questionZnzt(ModelMap modelMap,Integer subjectId, QuestionFilter questionFilter, HttpServletRequest request) {
       modelMap.addAttribute("subjectId",subjectId);
        return "/student/task/selfTest";
    }

    @RequestMapping("/findSubject")
    @ResponseBody
    public  List<TreeBean>  getStudentSubject(ModelMap modelMap,UserEntity student) {
        List<TreeBean> treeBeans=new ArrayList<TreeBean>();
        List<TeacherBookVersion> subjectList = taskService.findClassesTeacherSubject(student, GlobalConstant.TP_EXAM_TYPE_EXAM);//获取作业的学科
//        List<TreeBean> subjectList= studentClassHomeService.findSubjectOrRangeOfStudent(student.getId(), 0);//获取所在班级学科
        if(subjectList!=null && subjectList.size()>0){
            for(TeacherBookVersion tvb:subjectList){
                TreeBean tb=new TreeBean();
                tb.setName(tvb.getSubjectName());
                tb.setId(tvb.getSubjectId());
                treeBeans.add(tb);
            }
        }
       return treeBeans;
    }
    @RequestMapping("/findRange")
    @ResponseBody
    public  List<TreeBean>  getStudentRange(ModelMap modelMap,UserEntity student,Integer subjectId) {
        return  studentClassHomeService.findSubjectOrRangeOfStudent(student.getId(), subjectId);
    }
    /**
     * 选择教材版本和知识库
     * @param rangeId
     * @return
     */
    @RequestMapping("/getBookVersion")
    @ResponseBody
    public List getBookVersion(Integer rangeId,  Integer id) {
        rangeId = rangeId == null ? id : rangeId;
            List<Tags> lists = KlbTagsUtil.getInstance().getTagById(rangeId);
            if (lists != null && lists.size() > 0) {
                for (Tags t : lists) {
                    if (t.getName().equals(GlobalConstant.KLB_TAB_TEACHING_NAME)) {
                            return KlbTagsUtil.getInstance().getTagById(t.getId());
                    }
                }
            }
            return lists;

    }
    /**
     * 获取必修选择
     *
     * @param bookVersion
     * @return
     */
    @RequestMapping("/getObligatory")
    @ResponseBody
    public List getObligatory(Integer bookVersion) {
        return KlbTagsUtil.getInstance().getTagById(bookVersion);
    }
    @RequestMapping("/getStr")
    @ResponseBody
    public String getStr() {
        return "[]";
    }
    /**
     * 获取树
     *
     * @return
     */
    @RequestMapping("/getTree")
    @ResponseBody
    public List getTree(Integer id, Integer obligatoryId, Integer klbType,Integer obligatory) {
        Integer parentId = id != null ? id : obligatoryId;
        parentId=parentId==null?obligatory:parentId;
        List<Tags> lists = KlbTagsUtil.getInstance().getTagById(parentId);
        if (klbType != null && klbType == 1) {
            if (lists != null && lists.size() > 0) {
                for (Tags t : lists) {
                    if (t.getName().equals(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME)) {
                        return KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                    }
                }
            }
        } else if (klbType != null && klbType == 0) {
            if (lists != null && lists.size() > 0) {
                for (Tags t : lists) {
                    if (t.getName().equals(GlobalConstant.KLB_TAB_TEACHING_NAME)) {
                        return KlbTagsUtil.getInstance().getTagTreeById(t.getId());
                    }
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
    public List getTreeRefChild(Integer id, Integer obligatoryId,Integer obligatory ) {
        Integer parentId = id != null ? id : obligatoryId;
        parentId=parentId==null?obligatory:parentId;
        List<TreeBean> tagsList = KlbTagsUtil.getInstance().getTagTreeRefChildById(parentId);
        return tagsList;
    }
    /**
     * 错题本复习 -生成复习试卷
     * @param modelMap
     * @param questionFilter
     * @param request
     * @return
     */
    @RequestMapping("/question/errorfx")
    @ResponseBody
    public String questionErrorFx(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request,UserEntity ue){
        questionFilter.setLoginUser(ue);
        String  commitPaper= questionService.createStudentErrorFx(questionFilter );
        int paperId= 0;
        try {
            paperId = savePaper(commitPaper,ue,questionFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"paperId\":\""+paperId+"\",\"testType\":\"errorfx\",\"url\":\"/student/test/selftest/start\",\"time\":\""+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_POINT)+"\"}";
    }
    /**
     * 组题自测-生成自测试卷
     * @param modelMap
     * @param questionFilter
     * @param request
     * @return
     */
    @RequestMapping("/question/ztzc")
    @ResponseBody
    public String questionZtzc(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request,UserEntity ue){
        questionFilter.setLoginUser(ue);
        questionFilter.setExcludeSubjective(true);//需要排除小题有主观题的题
       Map<String,String> resultMap= questionService.createStudentSelfTest(questionFilter );
        String commitPaper=resultMap.get("paperCommit");
        int paperId= 0;
        try {
            paperId = savePaper(commitPaper,ue,questionFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"zcCount\":\""+resultMap.get("zcCount")+"\",\"paperId\":\""+paperId+"\",\"url\":\"/student/test/selftest/start\",\"time\":\""+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_POINT)+"\"}";
    }
    /**
     * 组题自测-开始自测
     * 错题本复习-开始自测
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping("/selftest/start")
    public String questionZtzc(ModelMap modelMap,Integer paperId,String name,String  testType,HttpServletRequest request,UserEntity ue) throws UnsupportedEncodingException {
        int type=TpExam.EXAM_TYPE_TASK_stuzc;
        int handExam=1;//表示组题自测
        if(StringUtils.isNotBlank(testType)&&testType.equals("errorfx")){
            //错题复习练习
             type=TpExam.EXAM_TYPE_TASK_stufuxi;
             handExam=2;
             name="错题本复习";
        }
        String title = request.getParameter("title");
            if (StringUtils.isNotBlank(title)) {
                //错题本
                System.out.println(title);
                title = URLDecoder.decode(title, "UTF-8");
                name=title.trim();
            }
        List<UserEntity> studentList=new ArrayList<UserEntity>();
        studentList.add(ue);
        //查询卷子
        TestPaper paper = taskService.findPaperById(paperId);
        Date begin =new Date();
        Date end=ParseDate.addDate(begin,1);//
        TaskDto dto=new TaskDto();dto.setPaperId(paperId);dto.setExamName(paper.getPaperName());
        dto.setStartTime(begin); dto.setEndTime(end);dto.setStatus(TpExam.EXAM_STATUS_PUT);dto.setTeacherId(-1);
        dto.setType(type);
        dto .setSubjectId(paper.getSubjectId());
        dto.setRangeId(paper.getRangeId());
        dto.setExamName(name);
        //保存作业数据
        int tf = taskService.saveTask(dto,studentList);
        return "redirect:/student/pc/start_"+tf+"_"+ue.getId()+"?handExam="+handExam;
    }
    public Integer savePaper(String commitPaper,UserEntity loginUser,QuestionFilter questionFilter) throws Exception{
        Map<Integer,String> idCode=new HashMap<Integer,String>();
        Map<Integer,List<SimpleStructureVo>> vos=SimpleStructureVo.parseString2Vo(commitPaper,idCode);
        for(Integer id:idCode.keySet()){
            //key是大题id：封装题目编码
            idCode.put(id,questionBaseService.getQuestionById(id).getCode());
            questionService.saveUsetimes(id,loginUser);//修改题目使用次数
        }
        TestPaperDto dto=new TestPaperDto();
        try {
            TestPaper paper=new TestPaper();dto.setPaper(paper);
            Tags subject=KlbTagsUtil.getInstance().getTag(questionFilter.getSubjectId());
            dto.getPaper().setSubjectId(subject.getId());
            dto.getPaper().setSubject(subject.getName());
            Tags range=KlbTagsUtil.getInstance().getTag(questionFilter.getRangeId());
            dto.getPaper().setPaperName(ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_POINT)+subject.getName()+"习题自测试卷");
            dto.getPaper().setRangeId(range.getId());
            dto.getPaper().setRange(range.getName());
            dto.getPaper().setTeacherId(loginUser.getId());
            dto.getPaper().setTeacherName(loginUser.getUserName());
            dto.getPaper().setSchoolId(loginUser.getSchoolId());
            dto.getPaper().setCreateTime(new Date());
            dto.getPaper().setUpdateTime(new Date());
            dto.getPaper().setFromwh(GlobalConstant.PAPER_FORM_STUDENT_NEW);
            dto.getPaper().setType(0);
            dto.getPaper().setStatus(0);  dto.getPaper().setUseTimes(0);  dto.getPaper().setBrowseTimes(0);
        } catch (Exception e) {
            e.printStackTrace(); throw e;
        }
        SimpleStructureVo.fill2Paper(dto,vos,idCode);
        int paperId=0;
        dto.zeroIdPid(paperId);
        dto.summaryScore();
        dto.SetLoginUser(loginUser);

        //set sub code
        for(TestPaperStructureDto sdto:dto.getStructuresItems()){
            TestPaperStructure structure=sdto.getTestPaperStructure();
            if(structure.getStructureType()==TestPaperStructure.structure_type_question){
                int size=sdto.getSubScores().size();
                if(size>0){
                    Question question=questionBaseService.getQuestionByCode(structure.getName());
                    List<Question> questions=questionBaseService.getSubQuestions(question.getId());
                    for(int i=0;i<size;i++){
                        sdto.getSubScores().get(i).setCode(questions.get(i).getCode());
                        sdto.getSubScores().get(i).setParentCode(question.getCode());
                    }
                }
            }
        }
        try{
            dto.getPaper().setPaperCode("xxzc_code_"+dto.getPaper().getSubject()+"习题自测试卷"+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS));
           return testPaperService.savePaper4Template(dto);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 错题本
     * @param modelMap
     * @param questionFilter
     * @param request
     * @return
     */
    @RequestMapping("/errornote")
    public String errornote(ModelMap modelMap,QuestionFilter questionFilter,HttpServletRequest request,UserEntity ue){
        return "/student/error/errorNote";
    }
    /**
     * 学生作业列表
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping("/testList")
    @ResponseBody
    public String testList(ModelMap modelMap, TaskPager taskfilter, HttpServletRequest request, UserEntity ue){
        if(StringUtils.isNotBlank(taskfilter.getKeyWord())) {
            try {
                if (StringUtils.isNotBlank(taskfilter.getKeyWord())) {
                    String keyword = URLDecoder.decode(taskfilter.getKeyWord(), "UTF-8");
                    keyword = keyword.trim();
                    taskfilter.setKeyWord(keyword);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // pageNo  当前页数   keyWord 关键字 subjectId学科 rangeId
        int pageSize=10;
        boolean more=false;
        taskfilter.setPageSize(pageSize);
        taskfilter.setStudentId(ue.getId());
        taskfilter=testPaperService.findSelfTest(taskfilter);
        int listSize=0;
        if(taskfilter.getResultList()!=null &&taskfilter.getResultList().size()>0) listSize=taskfilter.getResultList().size();
        int findedSize=taskfilter.getPageNo()*pageSize+listSize;
       if(findedSize<taskfilter.getTotalRows()){
           more=true;
       }
        return "{\"more\":"+more+",\"list\":"+ JSON.toJSONString(taskfilter.getResultList())+"}";
    }
    /**RequestMapping("/result_{rid}")
    public String result(@PathVariable("rid")
     * 错题本题目列表
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping("/errornote/list")
    public String errornoteList(ModelMap modelMap,@RequestParam(value = "examId", required = false) Integer examId,HttpServletRequest request,UserEntity ue,QuestionViewDto questionViewDto){
        List<IExamQuestionDto> questionDtoList=testPaperService.findErrorQuestionByExamId(ue.getId(),examId);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_error);
        questionDtoList=testPaperService.parseSubQuestionPoints(questionDtoList,examId,null);
        modelMap.addAttribute("questionViewDto", questionViewDto);
        modelMap.addAttribute("questionList", questionDtoList);
        return "/student/error/errorNoteList";
    }
    /**
     * 错题本 进度点搜索题列表
     * @param modelMap
     * @param questionFilter
     * @param request
     * @return
     */
    @RequestMapping("/errornote/jddList")
    public String errornoteJddList(ModelMap modelMap,QuestionFilter questionFilter,QuestionViewDto questionViewDto ,HttpServletRequest request,UserEntity ue){
        questionFilter.setLoginUser(ue);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_error);
        questionFilter= testPaperService.findErrorQuestionByJdd(questionFilter);

        modelMap.addAttribute("questionList", testPaperService.parseSubQuestionPoints( questionFilter.getResultList(),-1,null));
        modelMap.addAttribute("totalRows", questionFilter.getTotalRows());
        modelMap.addAttribute("listPager", questionFilter);
        modelMap.addAttribute("questionViewDto", questionViewDto);
        return "/student/error/errorNoteList";
    }
    @RequestMapping("/findDetailByResult")
    @ResponseBody
    public StudentResult findDetailByResult(ModelMap modelMap,Integer resultId ,HttpServletRequest request,UserEntity ue){
        TpExamResult examResult = taskService.findExamResultById(resultId);//findDetailByResult
        StudentResult studentResult = taskService.findDetailByResult(resultId);
        if(examResult!=null){
            studentResult.setCorrectRateNum(examResult.getRateDouble());
            studentResult.setCorrectRate(examResult.getRate());
        }
        return studentResult;
    }
    @RequestMapping("/errorDel")
    @ResponseBody
    public String errorDel(Integer id , UserEntity ue){
        try{
            taskService.deleteError(id,ue.getId());
            return "{\"status\": 0}";
        }catch (Exception e){
            e.printStackTrace();
            return "{\"status\": -1}";
        }
    }
}

