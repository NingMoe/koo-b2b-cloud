package com.koolearn.cloud.exam.examcore.paper.controller;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.*;
import com.koolearn.cloud.exam.examcore.paper.vo.SimpleStructureVo;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.util.CacheTools;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/teacher/exam/core/paper")
public class PaperController extends PaperAssistant{
	private static final Logger log= Logger.getLogger(PaperController.class);

    /***试卷展示 * @throws Exception */
	@RequestMapping("/preview")
	public String preview(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser) throws Exception{
        TestPaper testPaper=null;
        if(paperFilter.getPaperId()==null||paperFilter.getPaperId()==0){
            //新建试卷预览
            testPaper=testPaperService.createOrEditTestPaper(paperFilter,loginUser,getKeyOfPaperBar(loginUser));
            if(StringUtils.isBlank(testPaper.getPaperName())){
                testPaper.setPaperName(getTempPaperName( paperFilter));
            }
        }else{
            //试卷预览
            testPaper=testPaperService.createOrEditTestPaper(paperFilter,loginUser);
        }
        QuestionViewDto questionViewDto=new QuestionViewDto();
        questionViewDto.setViewType(QuestionViewDto.view_type_question_analysis);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_question_lib_change);//学生：QuestionViewDto.button_ype_null
        modelMap.addAttribute("questionViewDto", questionViewDto);
        modelMap.addAttribute("testPaper",testPaper);
        modelMap.put("paperFilter", paperFilter);
        return "/examcore/questionlib/paper/showPaper";
}

    /***加入或移除我的试卷库*/
    @RequestMapping("/jionMyself")
    @ResponseBody
    public boolean jionMyself(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser) throws Exception{
        testPaperService.jionMyself(paperFilter.getPaperId(),loginUser);
        return true;
    }
    /***加入或移除我的试卷库*/
    @RequestMapping("/paperDel")
    @ResponseBody
    public String paperDel(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser) throws Exception{
        testPaperService.deletPaperAndIndexByPaperId(paperFilter.getPaperId());
        return "{\"code\":\"200\",\"massage\":\"试卷删除成功！\"}";
    }
    /***试卷加题页面 * @throws Exception */
    @RequestMapping("/previewsjzt")
    public String previewsjzt(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser) throws Exception{
        TestPaper testPaper=testPaperService.createOrEditTestPaper(paperFilter,loginUser);
        QuestionViewDto questionViewDto=new QuestionViewDto();
        questionViewDto.setViewType(QuestionViewDto.view_type_question_analysis);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_question_lib);
        modelMap.addAttribute("questionViewDto", questionViewDto);
        modelMap.addAttribute("testPaper",testPaper);
        modelMap.put("paperFilter", paperFilter);
        return "/examcore/questionlib/paper/showPaperSjzt";
    }
	/**
	 * 1：手工组卷：页面展现组织好的试卷：直接获取缓存数据：questionBar
     * 2：智能组卷:/question/toCreatePaper转发过来
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/create")
	public String create(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser,HttpServletRequest request,HttpServletResponse response) throws Exception{
        PaperPager paperAuto= (PaperPager) request.getSession().getAttribute("paperAuto");
        if(paperAuto!=null) {
            paperFilter=paperAuto;
            request.getSession().removeAttribute("paperAuto");//智能组卷消费后删除
        }
        TestPaper testPaper=testPaperService.createOrEditTestPaper(paperFilter,loginUser,getKeyOfPaperBar(loginUser));
        if(StringUtils.isBlank(testPaper.getPaperName())){
           testPaper.setPaperName(getTempPaperName( paperFilter));
        }
        QuestionViewDto questionViewDto=new QuestionViewDto();
        questionViewDto.setViewType(QuestionViewDto.view_type_question_analysis);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_create_paper);
        modelMap.addAttribute("questionViewDto", questionViewDto);
        modelMap.addAttribute("questionBarHtml",new QuestionBarHtml(testPaper.getQuestionBar()));
        modelMap.addAttribute("testPaper",testPaper);
        modelMap.put("paperFilter", paperFilter);
		return "/examcore/questionlib/paper/createPaper";
	}
    public String getTempPaperName(PaperPager paperFilter){
        if(StringUtils.isNotBlank(paperFilter.getPaperName())){
            return paperFilter.getPaperName();
        }
        String subject=KlbTagsUtil.getInstance().getTag(paperFilter.getSubjectId()).getName();
        String range=KlbTagsUtil.getInstance().getTag(paperFilter.getRangeId()).getName();
        String obName="";
        if(paperFilter.getObligatoryId()!=null){
            obName  =KlbTagsUtil.getInstance().getTag(paperFilter.getObligatoryId()).getName();
        }
        String paperName=range+subject+obName+"-"+ ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDD_NO);
        return paperName;
    }
	/**
	 * 从模板对象过来的数据
	 * 保存试卷到数据库
	 * @param modelMap
	 * @param loginUser
     *             125766_0_985303_99,0_985303_985304_9.9,0_985303_985305_9.9
     *            结构id_题目父id_题id_分值
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save")
    @ResponseBody
	public String savePaper(ModelMap modelMap,PaperPager paperFilter,UserEntity loginUser,HttpServletRequest request) throws Exception{
        Map<Integer,String> idCode=new HashMap<Integer,String>();
		Map<Integer,List<SimpleStructureVo>> vos=SimpleStructureVo.parseString2Vo(paperFilter.getCommitPaper(),idCode);
		for(Integer id:idCode.keySet()){
            //key是大题id：封装题目编码
			idCode.put(id,questionBaseService.getQuestionById(id).getCode());
            questionService.saveUsetimes(id,loginUser);//修改题目使用次数
		}
		HttpSession session=request.getSession();
		TestPaperDto dto=new TestPaperDto();
		try {
            TestPaper paper=new TestPaper();dto.setPaper(paper);
			dto.getPaper().setPaperName(paperFilter.getTitle());
            Tags subject=KlbTagsUtil.getInstance().getTag(paperFilter.getSubjectId());
            dto.getPaper().setSubjectId(subject.getId());
            dto.getPaper().setSubject(subject.getName());
            Tags range=KlbTagsUtil.getInstance().getTag(paperFilter.getRangeId());
            dto.getPaper().setRangeId(range.getId());
            dto.getPaper().setRange(range.getName());
            dto.getPaper().setTeacherId(loginUser.getId());
//            dto.getPaper().setTeacherName(loginUser.getUserName());
            dto.getPaper().setTeacherName(loginUser.getRealName());
            dto.getPaper().setSchoolId(loginUser.getSchoolId());
            dto.getPaper().setCreateTime(new Date());
            dto.getPaper().setUpdateTime(new Date());
            dto.getPaper().setFromwh(GlobalConstant.PAPER_FORM_TEACHER_NEW);
            dto.getPaper().setType(Integer.parseInt(paperFilter.getNavigation()));
            dto.getPaper().setStatus(0);  dto.getPaper().setUseTimes(0);  dto.getPaper().setBrowseTimes(0);
		} catch (Exception e) {
                e.printStackTrace(); log.error(e.getMessage()); throw e;
		}
		SimpleStructureVo.fill2Paper(dto,vos,idCode);
		String strPaperId=request.getParameter("paperId");
		int paperId=0;
		if(strPaperId!=null&&strPaperId.trim().length()>0){
			paperId=Integer.parseInt(strPaperId);
		}
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
            dto.getPaper().setPaperCode("sgzj_code_"+dto.getPaper().getSubject()+"老师手工组卷"+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS));
		testPaperService.savePaper4Template(dto);
		}catch(Exception e){
			log.error("Exception  savePaper4Template==>"+e.getMessage());
			e.printStackTrace();
		}
        String json="{\"status\":0,\"url\":\"/teacher/exam/core/question/wdsj?createFrom="+paperFilter.getCreateFrom()+"\"}";
		return json;
	}

    /**第一次进入组卷页面调用*/
	@RequestMapping("/barInit")
    @ResponseBody
    public QuestionBar barInit(ModelMap modelMap,UserEntity loginUser, PaperPager paperFilter, HttpServletRequest request,HttpServletResponse response) throws Exception {
        QuestionBar qb=CacheTools.getCache(getKeyOfPaperBar(paperFilter ,loginUser ),QuestionBar.class);
        if(qb==null){
            qb=paperStructureService.findPaperBar(paperFilter);
            CacheTools.addCache(getKeyOfPaperBar(paperFilter ,loginUser),qb);
        }
        qb= testPaperService.parseKnowledgeCount( qb);
        return qb;
    }
    @RequestMapping("/barDel")
    @ResponseBody
    public boolean barDel(ModelMap modelMap,UserEntity loginUser, PaperPager paperFilter, HttpServletRequest request,HttpServletResponse response) throws Exception {
        CacheTools.delCache(getKeyOfPaperBar(paperFilter ,loginUser));
        return true;
    }

    /**
     * 根据题id计算知识点统计
     * @return
     * @throws Exception
     */
    @RequestMapping("/barUpdate")
    @ResponseBody
    public QuestionBar barupdate(ModelMap modelMap,UserEntity loginUser, PaperPager paperFilter, HttpServletRequest request,HttpServletResponse response) throws Exception {
        QuestionBar qb=new QuestionBar(paperFilter.getQuestionBar());// JSON.parse(filter.getQuestionBar(),QuestionBar.class);
        qb= testPaperService.parseKnowledgeCount( qb);
        CacheTools.addCache(getKeyOfPaperBar(paperFilter ,loginUser),qb);
        QuestionBar questionBar=CacheTools.getCache(getKeyOfPaperBar(paperFilter ,loginUser),QuestionBar.class);
        return qb;
    }
    public String getKeyOfPaperBar(UserEntity loginUser){
        return OnlyExamConstant.TEMPLATE_PREFIX+loginUser.getId()+"_";
    }
    public String getKeyOfPaperBar(PaperPager paperFilter,UserEntity loginUser){
        return OnlyExamConstant.TEMPLATE_PREFIX+loginUser.getId()+"_"+paperFilter.getSubjectId()+"_"+paperFilter.getRangeId();
    }
}
