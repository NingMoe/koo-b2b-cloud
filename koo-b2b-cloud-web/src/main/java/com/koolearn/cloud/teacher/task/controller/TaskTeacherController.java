package com.koolearn.cloud.teacher.task.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.service.ExamResultService;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.task.dto.*;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.DataSelectUtil;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.ParseDate;
import com.koolearn.cloud.util.SelectDTO;
import com.koolearn.cloud.util.pdf.ItextpdfUtils;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import java.io.*;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.*;

@RequestMapping("/teacher/task")
@Controller
public class TaskTeacherController extends BaseController {
    @Resource
    TaskService taskService;
    @Resource
    TestPaperService testPaperService;
    @Resource
    QuestionService questionService;
    @Resource
    ClassRoomService classRoomService;
    /**
     * 题目信息
     */
    @Autowired(required = true)
    protected QuestionBaseService questionBaseService;

    /**
     * 考试结果
     */
    @Autowired(required = true)
    protected ExamResultService resultService;

    /**
     * 作业列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, TaskPager taskPager, UserEntity ue) {
        List<SelectDTO> list = DataSelectUtil.getInstance().getSubject(ue.getId());//获取老师拥有的学科
        request.setAttribute("subjectList", list);
        return "/teacher/task/taskList";
    }

    /**
     * 作业列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/dataList")
    public String dataList(HttpServletRequest request, TaskPager taskPager, UserEntity ue) {
        String keyWord = request.getParameter("keyWord");//搜索关键字
        int subjectId = getParamterForInt("subjectId", 0);//科目（作业中试卷的科目）
        int pageNo = getParamterForInt("pageNo", 0);
        int pageSize = 12;
        taskPager.setUserId(ue.getId());
        taskPager.setPageSize(pageSize);
        taskPager.setPageNo(pageNo);
        taskPager.setKeyWord(keyWord);
        taskPager.setSubjectId(subjectId);
        //查询作业关联学生总数

        //查询作业列表
        TaskPager pager = taskService.searchTask(taskPager);
        //处理作业已完成和所有人数
        Map map = handleTaskStudentNum(pager);

        setAttribute("resultList", pager.getResultList());
        setAttribute("listPager", pager);
        setAttribute("pageNo", pageNo);
        return "/teacher/task/taskListData";
    }

    /**
     * 处理作业人数
     *
     * @param pager
     * @return
     */
    private Map handleTaskStudentNum(TaskPager pager) {
        Map map = new HashMap();
        String ids = "";
        for (int i = 0; i < pager.getResultList().size(); i++) {
            TaskDto task = (TaskDto) pager.getResultList().get(i);
            ids = ids + "," + task.getId();
        }
        if (ids.length() > 1) {
            ids = ids.substring(1);
        }
        if (pager != null && pager.getResultList() != null && pager.getResultList().size() > 0) {
            //查询作业对应的所有学生
            List<TaskStudentNum> list = taskService.findStudentNumByTaskIds(pager.getUserId(), ids);
            System.out.println(list);
            for (int i = 0; i < list.size(); i++) {//循环将list转map，存储成map（作业id，学生数）
                TaskStudentNum ts = list.get(i);
                map.put("all" + ts.getExamId(), ts.getStudentNum());
            }
            //查询作业对应的已完成作业的学生
            List<TaskStudentNum> clist = taskService.findCompStudentNumByTaskIds(pager.getUserId(), ids);
            System.out.println(clist);
            for (int i = 0; i < clist.size(); i++) {//循环将clist转map，存储成map（作业id，学生数）
                TaskStudentNum ts = clist.get(i);
                map.put("comp" + ts.getExamId(), ts.getStudentNum());
            }
            //查询作业对应班级列表
            List<TaskClassesName> tlist = taskService.searchClassesByTaskId(pager.getUserId(), ids);
            for (int i = 0; i < tlist.size(); i++) {//循环将clist转map，存储成map（作业id，学生数）
                TaskClassesName ts = tlist.get(i);
//				map.put("class"+ts.getExamId(), ts.getClassesName());
                map.put("class" + ts.getExamId(), ts.getClassesfullName());
            }

            //将学生数填弃到分页结果集中
            for (int i = 0; i < pager.getResultList().size(); i++) {
                TaskDto task = (TaskDto) pager.getResultList().get(i);
                Integer comp = (Integer) map.get("comp" + task.getId());
                Integer all = (Integer) map.get("all" + task.getId());
                String classesName = (String) map.get("class" + task.getId());
                task.setCompStudentNum(comp);
                task.setAllStudentNum(all);
                if (StringUtils.isNotBlank(classesName)) {
                    task.setClassesName(classesName.replaceAll(",", "/"));
                }
            }

        }
        return map;
    }

    /**
     * 撤回作业
     *
     * @param
     * @return
     */
    @RequestMapping("/revoke")
    @ResponseBody
    public Map revokeTask(int id) {
        System.out.println("taskId=" + id);
        Map map = new HashMap();
        map.put("success", false);
        TaskDto task = taskService.findTaskById(id);
        if (task.getEndTime().getTime() < new Date().getTime()) {
            map.put("errMsg", "作业已结束不可撤回!");
        } else {
            int count = taskService.revokeTask(id);
            if (count > 0) {
                map.put("success", true);
            } else {
                map.put("errMsg", "撤回失败!");
            }
        }
        return map;
    }

    /**
     * 删除作业
     * 只有已撤回的作业可删除
     *
     * @param
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map deleteTask(int id) {
        System.out.println("taskId=" + id);
        Map map = new HashMap();
        map.put("success", false);
        TaskDto task = taskService.findTaskById(id);
        if (task.getEndTime().getTime() < new Date().getTime()) {
            map.put("errMsg", "作业已结束不可删除!");
        } else if (task.getStatus() == TpExam.EXAM_STATUS_PUT) {
            map.put("errMsg", "作业已发布不可删除!");
        } else {
            int count = taskService.deleteTask(id);
            if (count > 0) {
                map.put("success", true);
            } else {
                map.put("errMsg", "删除失败，请重试!");
            }
        }
        return map;
    }

    /**
     * 布置作业 选择试卷
     *
     * @param request
     * @return
     */
    @RequestMapping("/assign")
    public String assignTask(HttpServletRequest request, PaperPager pager, ModelMap modelMap) {
        int classesId = getParamterForInt("classesId", 0);//班级管理中布置作业使用,直接传班级id,默认布置作业选中该班级
        int paperId = getParamterForInt("paperId", 0);
        if (paperId > 0) {//此处为从题库选择试卷布置作业时使用
            TestPaper paper = testPaperService.findItemById(paperId);
            request.setAttribute("paper", paper);
        }
        request.setAttribute("classesId", classesId);
        return "/teacher/task/assignTask";
    }

    /**
     * 布置作业  选择班级
     *
     * @param request
     * @return
     */
    @RequestMapping("/class")
    @ResponseBody
    public String assignTaskClass(HttpServletRequest request, UserEntity ue) {
        int classesId = getParamterForInt("classesId", 0);
        List<Classes> clist = findClassesByUserId(ue);
        //设置班级是否选中状态
        for (int i = 0; i < clist.size(); i++) {
            Classes c = clist.get(i);
            if (classesId == c.getId().intValue()) {
                c.setChecked(true);
            }
        }
        return JSON.toJSONString(clist);
    }

    /**
     * 作业修改  给出老师拥有班级,及某作业已选班级的关联数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/mclass")
    @ResponseBody
    public String assignTaskSelectedClass(HttpServletRequest request, UserEntity ue) {
        int taskId = getParamterForInt("taskId", 0);//搜索关键字
        List<Classes> clist = findClassesByUserId(ue);
        //查询此作业已经选择的班级
        Map<Integer, Boolean> mapClasses = new HashMap<Integer, Boolean>();
        List<Classes> selectedClasses = taskService.findClassesByTaskId(taskId, ue.getId());//选出布置作业的组和班
        for (int i = 0; i < selectedClasses.size(); i++) {
            Classes c = selectedClasses.get(i);
            mapClasses.put(c.getId(), true);
        }
        //设置班级是否选中状态
        for (int i = 0; i < clist.size(); i++) {
            Classes c = clist.get(i);
            Boolean checked = mapClasses.get(c.getId());
            if (checked == null) {
                c.setChecked(false);
                List<Classes> groupclass = c.getGroupList();
                if (groupclass != null && groupclass.size() > 0) {
                    for (Classes gc : groupclass) {
                        Boolean groupChecked = mapClasses.get(gc.getId());
                        groupChecked = groupChecked == null ? false : groupChecked;
                        gc.setChecked(groupChecked);
                    }
                }
            } else {
                c.setChecked(checked);
            }
        }
        return JSON.toJSONString(clist);
    }

    public List<Classes> findClassesByUserId(UserEntity ue) {
        //查询老师所属班级service
        List<Classes> lists = taskService.findClassesByUserId(ue);
        boolean result;
        Classes c;
        if (null != lists && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                c = lists.get(i);
                result = classRoomService.isExistClassesStudentByClassesId(c.getId());
                if (!result) {
                    //移除没有学生的班级
                    lists.remove(i);
                    i--;
                }
            }
        }
        return lists;
    }

    /**
     * 布置作业 选择试卷
     * 只查询老师自己的试卷(试卷里 我的试卷)
     *
     * @param request
     * @return
     */
    @RequestMapping("/paper")
    public String assignTaskPaper(HttpServletRequest request, PaperPager pager, ModelMap modelMap, UserEntity ue) {
        int pageNo = getParamterForInt("pageNo", 0);
        pager.setSearchText(request.getParameter("searchText"));
        pager.setPageNo(pageNo);
        pager.setPageSize(5);
        pager.setLoginUser(ue);//查询老师自已的试卷
        pager.setSearchFrom(GlobalConstant.PAPER_SEARCH_TYPE_WDSJ);
        PaperPager paper = questionService.searchPaper(pager);
        modelMap.addAttribute("listPager", paper);
        modelMap.addAttribute("resultList", paper.getResultList());
        return "/teacher/task/assignTaskPaperData";
    }

    /**
     * 完成布置（保存作业）
     * 复制作业保存
     *
     * @param request
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map saveTask(HttpServletRequest request, Classes classes, TaskDto dto, UserEntity ue) {
        //由于前端传到后台的时间数据缺少秒,dto中时间只能取到日期，取不到具体时分秒，此处处理
        dto.setTeacherId(ue.getId());//存老师id
        Map map = new HashMap();
        map.put("success", false);
        map.put("errMsg", "请求失败,请重试!");
        if (StringUtils.isBlank(dto.getClassesId())) {
            map.put("errMsg", "请选择班级！");
        } else if (dto.getPaperId() <= 0) {
            map.put("errMsg", "请选择试卷！");
        } else {
            //查询学生列表,用于插入学生与作业中间表的学生数据
            List<UserEntity> studentList = taskService.findStudentByClassesIds(dto.getClassesId());
            //查询卷子，只为获取卷子的学科属性
            TestPaper paper = taskService.findPaperById(dto.getPaperId());
            //保存作业数据
            dto.setType(TpExam.EXAM_TYPE_TASK);
            dto.setSubjectId(paper.getSubjectId());
            dto.setRangeId(paper.getRangeId());
            int tf = taskService.saveTask(dto, studentList);
            //拼接“班级”数据显示提示布置成功弹窗中 初三一班，初三二班
            List<Classes> clist = taskService.findClassesByIds(dto.getClassesId());
            String classesName = "";
            for (int i = 0; i < clist.size() && clist != null; i++) {
                Classes c = clist.get(i);
                classesName += "," + c.getFullName();
            }
            if (StringUtils.isNotBlank(classesName)) {
                classesName = classesName.substring(1);
            }
            map.put("success", true);
            map.put("errMsg", classesName);
        }
        return map;
    }

    /**
     * 编辑作业页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/edit")
    public String editTask(HttpServletRequest request, Classes classes, UserEntity ue) {
        int taskId = getParamterForInt("id", 0);
        TaskDto task = taskService.findTaskById(taskId);
        if (task == null || task.getEndTime().getTime() < new Date().getTime() || task.getStatus() == TpExam.EXAM_STATUS_DELETE) {//已结束，已删除的不允许编辑
            System.out.println("非法作业id");
            return "redirect:/teacher/task/list";
        }
        request.setAttribute("task", task);
        return "/teacher/task/modifyTask";
    }

    /**
     * 编辑作业页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/copy")
    public String copyTask(HttpServletRequest request, Classes classes, UserEntity ue) {
        int taskId = getParamterForInt("id", 0);
        TaskDto task = taskService.findTaskById(taskId);
        if (task == null) {
            System.out.println("非法的作业id");
            return "redirect:/teacher/task/list";
        }
        request.setAttribute("task", task);
        return "/teacher/task/copyTask";
    }

    /**
     * 编辑作业保存
     * status=4表示编辑作业发布
     *
     * @param request
     * @return
     */
    @RequestMapping("/modifySave")
    @ResponseBody
    public Map modifySave(HttpServletRequest request, Classes classes, TaskDto dto, UserEntity ue) {
        //由于前端传到后台的时间数据缺少秒,dto中时间只能取到日期，取不到具体时分秒，此处处理
        dto.setTeacherId(ue.getId());//存老师id

        Map map = new HashMap();
        map.put("success", false);
        TaskDto task = taskService.findTaskById(dto.getId());
        if (task == null && task.getEndTime().getTime() < new Date().getTime()) {//判断作业id是否存在，且该作业是否结束   结束的作业不能够修改
            map.put("errMsg", "非法的作业id");
            return map;
        }
        if (StringUtils.isBlank(dto.getClassesId())) {
            map.put("errMsg", "请选择班级！");
        } else {
            //查询学生列表,用于插入学生与作业中间表的学生数据
            List<UserEntity> studentList = taskService.findStudentByClassesIds(dto.getClassesId());
            //查询卷子，只为获取卷子的学科属性
            TestPaper paper = taskService.findPaperById(dto.getPaperId());
            //保存作业数据
            dto.setType(TpExam.EXAM_TYPE_TASK);
            dto.setSubjectId(paper.getSubjectId());
            dto.setRangeId(paper.getRangeId());
            //修改作业数据
            int tf = taskService.modifySaveTask(dto, studentList);
            //拼接“班级”数据显示提示布置成功弹窗中 初三一班，初三二班
            List<Classes> clist = taskService.findClassesByIds(dto.getClassesId());
            String classesName = "";
            for (int i = 0; i < clist.size() && clist != null; i++) {
                Classes c = clist.get(i);
                classesName += "," + c.getFullName();
            }
            if (StringUtils.isNotBlank(classesName)) {
                classesName = classesName.substring(1);
            }
            map.put("success", true);
            map.put("errMsg", classesName);
        }
        return map;
    }

    /**
     * 作业情况
     *
     * @param request
     * @return
     */
    @RequestMapping("/situation")
    public String situation(HttpServletRequest request, UserEntity ue, TaskPager t) {
        int classId = getParamterForInt("classId", 0);
        int examId = getParamterForInt("examId", 0);
        //查询作业基本信息
        TaskDto task = taskService.findTaskById(examId);
        String source = request.getParameter("source");
        List<Classes> clist;
        if (source != null && !"".equals(source)) {
            int classRoomClassId = getParamterForInt("classRoomClassId", 0);
            request.setAttribute("classRoomClassId", classRoomClassId);
            request.setAttribute("source", source);//课堂标示
            Classes classes = classRoomService.getClassesById(classRoomClassId);
            if (classes.getType().equals(3)) {
                clist = new ArrayList<Classes>();
                clist.add(classes);
            } else {
                //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
                clist = classRoomSetClasses(ue, classRoomClassId, examId);
                if (classId == 0 && clist.size() > 0) {
                    classId = clist.get(0).getId();
                }
            }
        } else {
            //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
            clist = taskService.findClassesByTaskId(examId, ue.getId());
            if (classId == 0 && clist.size() > 0) {
                classId = clist.get(0).getId();
            }
        }
        t.setExamId(examId);
        t.setClassId(classId);
        t.setUserId(ue.getId());
        //更新考试结果表老师浏览状态：由未查看改成已查看
        taskService.updateTeExamResultTeacherView(t);
        //查询提交作业的学生列表
        List<TpExamResult> userResultList = taskService.findStudentByExamResult(t);
        String studentIds = handlerStudentIds(userResultList);//有考试结果的学生id集合
        t.setStudentIds(studentIds);
        List<TpExamResult> userNoResultList = taskService.findStudentNoResultByClassesId(t);
        request.setAttribute("examId", examId);
        request.setAttribute("classId", classId);
        request.setAttribute("task", task);
        request.setAttribute("clist", clist);
        request.setAttribute("userResultList", userResultList);
        request.setAttribute("userNoResultList", userNoResultList);


        return "/teacher/task/taskSituation";
    }

    /**
     * 翻转课堂班级列表
     *
     * @param ue
     * @param classId
     * @param examId
     * @return
     */
    private List<Classes> classRoomSetClasses(UserEntity ue, int classId, int examId) {
        List<Classes> clist;
        clist = taskService.findClassesByTaskId(examId, ue.getId());
        List<Integer> childList = classRoomService.findClassesChild(classId);
        childList.add(classId);
        Classes classes1;
        for (int i = 0; i < clist.size(); i++) {
            boolean num = false;
            classes1 = clist.get(i);
            for (int j = 0; j < childList.size(); j++) {
                if (classes1.getId().equals(childList.get(j))) {
                    num = true;
                    break;
                }
            }
            if (!num) {
                clist.remove(i);
                i--;
            }
        }
        return clist;
    }

    //有考试结果的学生id集合
    public String handlerStudentIds(List<TpExamResult> list) {
        String studentIds = "";
        for (int i = 0; i < list.size(); i++) {
            TpExamResult ter = list.get(i);
            studentIds += "," + ter.getStudentId();
        }
        if (studentIds.length() > 0) {
            studentIds = studentIds.substring(1);
        }
        return studentIds;
    }

    /**
     * 作业分析
     *
     * @param request
     * @return
     *
     * /teacher/task/analysis
     */
    @RequestMapping("/analysis")
    public String taskAnalysis(HttpServletRequest request, UserEntity ue, TaskPager t) {
        int classId = 243810;// getParamterForInt("classId", 0);
        int examId = 1863;//getParamterForInt("examId", 0);
        //查询作业基本信息
        TaskDto task = taskService.findTaskById(examId);
        String source = request.getParameter("source");
        List<Classes> clist;
        if (source != null && !"".equals(source)) {
            int classRoomClassId = getParamterForInt("classRoomClassId", 0);
            request.setAttribute("classRoomClassId", classRoomClassId);
            request.setAttribute("source", source);//课堂标示
            Classes classes = classRoomService.getClassesById(classRoomClassId);
            if (classes.getType().equals(3)) {
                clist = new ArrayList<Classes>();
                clist.add(classes);
            } else {
                //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
                clist = classRoomSetClasses(ue, classRoomClassId, examId);
                if (classId == 0 && clist.size() > 0) {
                    classId = clist.get(0).getId();
                }
            }
        } else {
            //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
            clist = taskService.findClassesByTaskId(examId, ue.getId());
            if (classId == 0 && clist.size() > 0) {
                classId = clist.get(0).getId();
            }
        }
        t.setExamId(examId);
        t.setClassId(classId);
        t.setUserId(ue.getId());
        //查询某班下的所有学生
        String studentIds = taskService.findStudentIdsByClassesId(classId);
        t.setStudentIds(studentIds);

        Map map = new HashMap<String, String>();
        /** 题目得分率 start */
        try {
            questionScoreRate(t, map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /** 题目得分率 end */

        /** 知识点得分率 start */
        try {
            knowlegeScoreRate(t, map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /** 知识点得分数据 end */

        /** 成绩分布 start */
        try {
            studentScoreSpread(t, map, task);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /** 成绩分布 end */
        request.setAttribute("map", map);
        request.setAttribute("task", task);
        request.setAttribute("clist", clist);
        request.setAttribute("taskPager", t);
        return "/teacher/task/taskAnalysis";
    }

    /**
     * 题目得分率
     *
     * @param t
     * @param map
     */
    private void questionScoreRate(TaskPager t, Map map) {
        //查询学生题目得分情况
        List<QuestionErrUser> listScoreRate = taskService.findStudentScoreRate(t);
        List<String> questionOrderList = new ArrayList<String>();//题目数据
        List<String> scoreRate = new ArrayList<String>();//得分率数据
        int bigQuestionNo = 1;
        int subQuestionNo = 1;
        Integer preQuestionId = -1;
        LinkedHashMap<String, String> scoreRateMapX = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> scoreRateMapY = new LinkedHashMap<String, String>();
        DecimalFormat df = new DecimalFormat("0%");
        for (int i = 0; i < listScoreRate.size(); i++) {
            QuestionErrUser score = listScoreRate.get(i);
            if (score.getTeId() != 0 && score.getTeId() == preQuestionId.intValue()) {
//				questionOrderList.remove((bigQuestionNo-1)+"题");//如果有子题，不显示大题的题目序号,显示子题的题目序号
//				questionOrderList.add(bigQuestionNo+"."+subQuestionNo+"题");
                bigQuestionNo--;
                scoreRateMapY.remove(preQuestionId + "");
                scoreRateMapX.remove(preQuestionId + "");
                scoreRateMapX.put(score.getQuestionId() + "", bigQuestionNo + "." + subQuestionNo + "题");
                subQuestionNo++;//子题序号增加
            } else {
//				questionOrderList.add((bigQuestionNo)+"题");
                scoreRateMapX.put(score.getQuestionId() + "", bigQuestionNo + "题");
                subQuestionNo = 1;
            }
            if (StringUtils.isNotBlank(score.getScoreRate())) {
                DecimalFormat dfo = new DecimalFormat("#");
                Double sr = Double.parseDouble(score.getScoreRate());
                score.setScoreRate(dfo.format(sr));
            }
            scoreRateMapY.put(score.getQuestionId() + "", score.getScoreRate());//y轴得分率
            if (score.getTeId() == 0) {//保存大题的题目id
                preQuestionId = score.getQuestionId();
            }
            bigQuestionNo++;//大题题序增加
        }
        map.put("question_socre_rate_xData", JSON.toJSONString(scoreRateMapX.values()));
        map.put("question_socre_rate_sData", JSON.toJSONString(scoreRateMapY.values()));
    }

    /**
     * 知识点得分率
     *
     * @param t
     * @throws Exception
     */
    private void knowlegeScoreRate(TaskPager t, Map rmap) throws Exception {
        //通过题目信息获取题目对应知识点数据
        List<TpExamResultDetail> listDetailScore = taskService.findStudentQuestionScore(t);
        List<Integer> questionIds = new ArrayList<Integer>();//题目id集合
        for (int i = 0; i < listDetailScore.size(); i++) {
            TpExamResultDetail de = listDetailScore.get(i);
            questionIds.add(de.getQuestionId());
        }
        //通过题目id查询题目数据
        List<IExamQuestionDto> questionList = questionBaseService.findQuestionByIds(questionIds);
        //通过题目数据查询题目对应的知识点
        Map<Integer, List<Tags>> knowledgeList = questionService.getQuestionKnowledges(questionList);
        //由题目得分情况转换成知识点得分情况
        Map<Integer, TpExamResultDetail> map = new HashMap<Integer, TpExamResultDetail>();//此map处理相同知识点题目得分和题目总分累加
        for (int i = 0; i < listDetailScore.size(); i++) {//循环题目得分情况数据
            TpExamResultDetail detail = listDetailScore.get(i);
            List<Tags> tags = knowledgeList.get(detail.getQuestionId());//获取题目id对应的知识点
            for (int j = 0; j < tags.size(); j++) {//循环知识点，将题目得分和总分累加
                Tags tag = tags.get(j);
                //以下部分处理相同知识点的题目得分和总分合并处理
                System.out.println(tag.getId() + "    " + tag.getName() + "  detail.getQuestionId()="
                        + detail.getQuestionId() + "  detail.getScore()=" + detail.getScore() + "  detail.getPoints()=" + detail.getPoints());
                if (map.get(tag.getId()) == null) {
                    TpExamResultDetail nDetail = new TpExamResultDetail();
                    nDetail.setUserAnswer(tag.getName());//借用此字段存储知识点名称
                    nDetail.setScore(detail.getScore());
                    nDetail.setPoints(detail.getPoints());
                    map.put(tag.getId(), nDetail);
                } else {
                    TpExamResultDetail nDetail = map.get(tag.getId());
                    nDetail.setScore(nDetail.getScore() + detail.getScore());
                    nDetail.setPoints(nDetail.getPoints() + detail.getPoints());
                    map.put(tag.getId(), nDetail);
                }
            }
        }
        List<String> knowlegeOrderList = new ArrayList<String>();//知识点数据
        List<String> scoreRate = new ArrayList<String>();//得分率数据
        Set<Integer> key = map.keySet();
        for (Iterator iterator = key.iterator(); iterator.hasNext(); ) {
            Integer integer = (Integer) iterator.next();
            System.out.println("integer=" + integer);
            TpExamResultDetail de = map.get(integer);
//			System.out.println("知识点x轴数据:"+de.getUserAnswer() +"  detail="+de.getId()+"  "+de.getQuestionId());
            knowlegeOrderList.add(de.getUserAnswer());//借用此字段存储知识点名称
            DecimalFormat df = new DecimalFormat("#");
            scoreRate.add(df.format(100 * de.getScore() / de.getPoints()));
        }
        rmap.put("knowlege_socre_rate_xData", JSON.toJSONString(knowlegeOrderList));
        rmap.put("knowlege_socre_rate_sData", JSON.toJSONString(scoreRate));
    }

    /**
     * 学生成绩分布
     *
     * @param t
     * @param task
     */
    private void studentScoreSpread(TaskPager t, Map map, TaskDto task) {
        TestPaper paper = testPaperService.findItemById(task.getPaperId());
        List<TpExamResult> listExamResult = taskService.findStudentScore(t);//某次作业的考试结果
        Double points = paper.getPoints();
        //查询学生成绩分布
        List slist = new ArrayList<Integer>();
        if (points < 10) {//此处主要是处理卷子总分为0的情况,如果总分小于10,那只有一个区间段,人数即为考试结果的长度
            List list = new ArrayList<Integer>();
            list.add(0);
            list.add(10);
            slist.add(listExamResult.size());
            map.put("score_spread_xData", JSON.toJSONString(list));
            map.put("score_spread_sData", JSON.toJSONString(slist));//处理y轴数据
        } else {
            int ceng = (int) Math.ceil(points / 10);//求x轴有多少个区间段
            studentScoreSpreadXData(ceng, map);//处理x轴数据

            //查询学生成绩分布
            for (int i = 1; i <= ceng; i++) {
                int cengPersonSum = studentScoreSpreadYData(listExamResult, i);
                slist.add(cengPersonSum);
            }
            map.put("score_spread_sData", JSON.toJSONString(slist));//处理y轴数据
        }
    }

    /**
     * 学生成绩分布y轴数据
     *
     * @param listExamResult
     * @param i
     * @return
     */
    private int studentScoreSpreadYData(List<TpExamResult> listExamResult, int i) {
        int cengPersonSum = 0;//每个得分区间段的总人数
        for (int j = 0; j < listExamResult.size(); j++) {
            TpExamResult er = listExamResult.get(j);
            if (i > 1) {//区间都是不包含区间内较小的数,如 10-20 应为10<x<=20
                if (er.getScore() <= i * 10 && er.getScore() > (i - 1) * 10) {
                    cengPersonSum++;
                }
            } else {//0分计入0-10区间段内
                if (er.getScore() <= i * 10) {
                    cengPersonSum++;
                }
            }
        }
        return cengPersonSum;
    }

    /**
     * 学生成绩分布x轴数据
     *
     * @param
     * @param map
     */
    private void studentScoreSpreadXData(int ceng, Map map) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= ceng; i++) {
            list.add(i * 10);
        }
        map.put("score_spread_xData", JSON.toJSONString(list));
    }

    /**
     * 作业讲评
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/comment")
    public String taskComment(HttpServletRequest request, UserEntity ue, TaskPager taskPager) throws Exception {
        //接收页面搜索条件是 0.全部错题 1.错误率手动输入
        String radioType = getParamter("radioType");
        System.out.println("radioType==" + radioType);
        if (StringUtils.isBlank(radioType)) {
            radioType = "1";//默认页面显示错误率大于50%的错题
        }
        String source = request.getParameter("source");
        List<Classes> clist;
        if (source != null && !"".equals(source)) {
            int classRoomClassId = getParamterForInt("classRoomClassId", 0);
            request.setAttribute("classRoomClassId", classRoomClassId);
            request.setAttribute("source", source);//课堂标示
            Classes classes = classRoomService.getClassesById(classRoomClassId);
            if (classes.getType().equals(3)) {
                clist = new ArrayList<Classes>();
                clist.add(classes);
            } else {
                //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
                clist = classRoomSetClasses(ue, classRoomClassId, taskPager.getExamId());
                if (taskPager.getClassId() == 0 && clist.size() > 0) {
                    taskPager.setClassId(clist.get(0).getId());
                }
            }
        } else {
            //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
            clist = taskService.findClassesByTaskId(taskPager.getExamId(), ue.getId());
            if (taskPager.getClassId() == 0 && clist.size() > 0) {
                taskPager.setClassId(clist.get(0).getId());
            }
        }


        //查询作业基本信息
        TaskDto task = taskService.findTaskById(taskPager.getExamId());
        //查询题目得分率和平均得分 每道题的
        List<QuestionErrUser> avg = taskService.findQuestionAvgScore(taskPager);
        Map<Integer, QuestionErrUser> mapAvg = Maps.uniqueIndex(avg, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapAvg=" + mapAvg);
        //题目id和题目对应的答错人及人数集合
        List<QuestionErrUser> errUser = taskService.findQuestionErrAnswerUser(taskPager);
        Map<Integer, QuestionErrUser> mapErrUser = Maps.uniqueIndex(errUser, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapErrUser=" + mapErrUser);
        //查询未作答人 	未作答人，指的是提交作业但该题没有选择或上传答案的人
        List<QuestionErrUser> noAnswerUser = taskService.findQuestionNoAnswerUser(taskPager);
        Map<Integer, QuestionErrUser> mapNoAnswerUser = Maps.uniqueIndex(noAnswerUser, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapNoAnswerUser=" + mapNoAnswerUser);
        //将三个map合并成一个map
        Map<Integer, QuestionErrUser> mapAll = handleData(mapAvg, mapErrUser, mapNoAnswerUser);

        for (Map.Entry<Integer, QuestionErrUser> a : mapAll.entrySet()) {
            System.out.println(a.getValue().getAvgRate());
            System.out.println(a.getValue().getErrUserName());
            System.out.println(a.getValue().getErrUserNum());
            System.out.println(a.getValue().getNoAnswerUserName());
            System.out.println(a.getValue().getNoAnswerUserNum());
        }
        //获取题目列表
        List<IExamQuestionDto> questionDtoList = commentQuestionDtoList(taskPager, mapAll);

        request.setAttribute("radioType", radioType);
        request.setAttribute("questionDtoList", questionDtoList);
        request.setAttribute("clist", clist);
        request.setAttribute("taskPager", taskPager);
        request.setAttribute("task", task);
        request.setAttribute("mapAvg", mapAvg);
        request.setAttribute("mapErrUser", mapErrUser);
        request.setAttribute("mapNoAnswerUser", mapNoAnswerUser);
        request.setAttribute("source", request.getParameter("source"));//课堂标示
        return "/teacher/task/taskComment";
    }

    private List<IExamQuestionDto> commentQuestionDtoList(TaskPager taskPager, Map<Integer, QuestionErrUser> mapAll) throws Exception {
        List<IExamQuestionDto> questionDtoList = null;
        //查询错误率符合条件的题目id
        String resultIds = taskService.findAllResultId(taskPager);
        if (StringUtils.isNotBlank(resultIds)) {
            taskPager.setResultIds(resultIds);
            String[] r = resultIds.split(",");
            String resultId = r[0];
            //查询结果中所有大题
            List<TpExamResultDetail> teIdDetail = taskService.findResultDetailTeId(resultId);

            //查询需要排除的错误率小于等于页面输入错误率的题目数据
            List<QuestionErrUser> resultDetail = taskService.findAllResultDetail(taskPager);
            int viewType = QuestionViewDto.view_type_jiangping;
            questionDtoList = testPaperService.createPartTestPaper(teIdDetail, resultDetail, taskPager.getExamId(), mapAll, viewType);
        }
        return questionDtoList;
    }

    private Map<Integer, QuestionErrUser> handleData(Map<Integer, QuestionErrUser> mapAvg,
                                                     Map<Integer, QuestionErrUser> mapErrUser,
                                                     Map<Integer, QuestionErrUser> mapNoAnswerUser) {
        Map result = new HashMap<Integer, QuestionErrUser>();
        Set all = new HashSet<Integer>();
        all.addAll(mapAvg.keySet());
        all.addAll(mapErrUser.keySet());
        all.addAll(mapNoAnswerUser.keySet());
        Iterator<Integer> it = all.iterator();
        while (it.hasNext()) {
            Integer questionId = it.next();
            System.out.println(questionId);
            QuestionErrUser qe = new QuestionErrUser();
            QuestionErrUser avg = mapAvg.get(questionId);
            QuestionErrUser err = mapErrUser.get(questionId);
            QuestionErrUser answer = mapNoAnswerUser.get(questionId);
            if (avg != null) {
                qe.setAvgRate(avg.getAvgRate() == null ? "0" : avg.getAvgRate());//错误率
            }
            if (err != null) {
                qe.setErrUserName(err.getErrUserName());
                qe.setErrUserNum(err.getErrUserNum());
            }
            if (answer != null) {
                qe.setNoAnswerUserName(answer.getNoAnswerUserName());
                qe.setNoAnswerUserNum(answer.getNoAnswerUserNum());
            }
            result.put(questionId, qe);
        }
        return result;
    }

    /**
     * 作业讲评下载
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/commentPDF")
    public void taskCommentDownPDF(HttpServletRequest request, HttpServletResponse response, UserEntity ue, TaskPager taskPager) throws Exception {
        //查询作业基本信息
        TaskDto task = taskService.findTaskById(taskPager.getExamId());
//		//查询题目得分率和平均得分 每道题的
//		List<QuestionErrUser> avg = taskService.findQuestionAvgScore(taskPager);
//		//题目id和题目对应的答错人及人数集合
//		List<QuestionErrUser> errUser = taskService.findQuestionErrAnswerUser(taskPager);
//		//查询未作答人 	未作答人，指的是提交作业但该题没有选择或上传答案的人
//		List<QuestionErrUser> noAnswerUser = taskService.findQuestionNoAnswerUser(taskPager);

        //查询题目得分率和平均得分 每道题的
        List<QuestionErrUser> avg = taskService.findQuestionAvgScore(taskPager);
        Map<Integer, QuestionErrUser> mapAvg = Maps.uniqueIndex(avg, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapAvg=" + mapAvg);
        //题目id和题目对应的答错人及人数集合
        List<QuestionErrUser> errUser = taskService.findQuestionErrAnswerUser(taskPager);
        Map<Integer, QuestionErrUser> mapErrUser = Maps.uniqueIndex(errUser, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapErrUser=" + mapErrUser);
        //查询未作答人 	未作答人，指的是提交作业但该题没有选择或上传答案的人
        List<QuestionErrUser> noAnswerUser = taskService.findQuestionNoAnswerUser(taskPager);
        Map<Integer, QuestionErrUser> mapNoAnswerUser = Maps.uniqueIndex(noAnswerUser, new Function<QuestionErrUser, Integer>() {
            public Integer apply(QuestionErrUser from) {
                return from.getQuestionId();
            }
        });
        System.out.println("mapNoAnswerUser=" + mapNoAnswerUser);
        //将三个map合并成一个map
        Map<Integer, QuestionErrUser> mapAll = handleData(mapAvg, mapErrUser, mapNoAnswerUser);

        //获取题目列表
        List<IExamQuestionDto> questionDtoList = commentQuestionDtoList(taskPager, null);
        if(questionDtoList!=null && !questionDtoList.isEmpty()){
        	 //下载作业讲评
            String downloadName = task.getId() + "_" + ParseDate.formatByDate(new Date(), ParseDate.DATE_FORMAT_YYYYMMDDHHMMSS);
            //生成pdf过程
            testPaperService.downloadTaskComment(questionDtoList, avg, errUser, noAnswerUser, task, mapAll, downloadName);
            //下载生成的pdf
            this.download(request, response, downloadName, task.getExamName() + ".pdf");
        }else{
        	response.setContentType("text/html;charset=utf-8");
        	PrintWriter wr = response.getWriter();
        	wr.write("搜索无结果");
        }
    }

    /**
     * 下载
     *
     * @param request
     * @param response
     * @param
     * @param fileName 直线运动的图象.pptx
     * @throws Exception
     */
    public static void download(HttpServletRequest request,
                                HttpServletResponse response, String downloadPaperName, String fileName)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String realName = fileName;
        realName = toUtf8String(request, fileName);
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
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 根据文件名获取类型（直线运动的图象.pptx）
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getType(String filename) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filename);
        return mimeType;
    }

    /**
     * 作业批阅 -- 搜索学生列表
     *
     * @param request
     * @param ue
     * @param
     * @return
     */
    @RequestMapping(value = "/searchStudent")
    @ResponseBody
    public String searchStudent(HttpServletRequest request, UserEntity ue, TaskPager taskPager) {
        int classId = getParamterForInt("classId", 0);
        taskPager.setClassId(classId);
        //查询某班提交考试结果的学生
        List<UserEntity> userResultList = taskService.findStudentResultByClassesId(taskPager);
        return JSON.toJSONString(userResultList);
    }

    /**
     * 个人作业详情 同 作业批阅
     *
     * @param request
     * @param ue
     * @param
     * @return
     */
    @RequestMapping("/personStation")
    public String personTaskStation(HttpServletRequest request, UserEntity ue, TaskPager taskPager) {
        //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
        String ptype = request.getParameter("ptype");//ptype 个人作业详情 只看错题ptype=e	 查看全部 ptype=p
        System.out.println("ptype============" + ptype);
        List<Classes> clist = taskService.findClassesByTaskId(taskPager.getExamId(), ue.getId());
        if (taskPager.getClassId() == 0) {
            taskPager.setClassId(clist.get(0).getId());
        }
        //查询作业基本信息
        TaskDto task = taskService.findTaskById(taskPager.getExamId());
        //查询某班提交考试结果的学生
        List<UserEntity> userList = taskService.findStudentResultByClassesId(taskPager);
        if (userList.size() > 0 && (taskPager.getUserId() == null || taskPager.getUserId().intValue() == 0)) {
            taskPager.setUserId(userList.get(0).getId());
        }
        request.setAttribute("userList", userList);
        request.setAttribute("task", task);
        request.setAttribute("clist", clist);
        request.setAttribute("taskPager", taskPager);
        request.setAttribute("ptype", ptype);
        return "/teacher/task/taskPersonSituation";
    }

    /**
     * 作业批阅
     *
     * @param request
     * @param ue
     * @param
     * @return
     */
    @RequestMapping(value = "/mark")
    public String taskMark(HttpServletRequest request, UserEntity ue, TaskPager taskPager) {
        //班级列表，显示真实所选班级或分组，即该作业如果只选择了a班的甲组，那么也显示a班甲组。如果选择了b班，那么显示b班
        List<Classes> clist = taskService.findClassesByTaskId(taskPager.getExamId(), ue.getId());
        if (taskPager.getClassId() == 0 && clist.size() > 0) {
            taskPager.setClassId(clist.get(0).getId());
        }
        //查询作业基本信息
        TaskDto task = taskService.findTaskById(taskPager.getExamId());
        //查询某班提交考试结果的学生
        List<UserEntity> userList = taskService.findStudentResultByClassesId(taskPager);
        if (userList.size() > 0 && (taskPager.getUserId() == null || taskPager.getUserId().intValue() == 0)) {
            taskPager.setUserId(userList.get(0).getId());
        }
        request.setAttribute("userList", userList);
        request.setAttribute("task", task);
        request.setAttribute("clist", clist);
        request.setAttribute("taskPager", taskPager);
        return "/teacher/task/taskMark";
    }

    /**
     * 作业批阅
     *
     * @param request
     * @param ue
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/markSubmit_{rid}")
    @ResponseBody
    public Map markSubmit(HttpServletRequest request, UserEntity ue, @PathVariable("rid") int rid) {
        Map rmap = new HashMap();
        rmap.put("success", false);
        rmap.put("errMsg", "保存失败,请重试!");
        TpExamResult examResult;
        try {
            examResult = this.resultService.searchById(rid);
//			Process process = new Process();
//			process.setExamResult(examResult);
//			process = this.resultService.searchFullByProcess(process);
            // 所有主观题考试结果明细集合
            List<TpExamResultDetail> examResultDetails = this.resultService.searchResultDetail(examResult.getId());

            if (examResultDetails != null && !examResultDetails.isEmpty()) {
                Map<String, String> map = new HashMap<String, String>();
                double resultScore = examResult.getScore();//考试结果表学生卷子所得分数
                for (int i = 0; i < examResultDetails.size(); i++) {
                    TpExamResultDetail detail = examResultDetails.get(i);
                    if (detail.getSubjective().intValue() == TpExamResultDetail.SUBJECTIVE) {//主观题老师打分
                        System.out.println("resultanswer=" + request.getParameter("result_answer_" + detail.getQuestionId()) + "  " + "score=" + request.getParameter("score_" + detail.getQuestionId()));
                        map.put("result_answer_" + detail.getId(), request.getParameter("result_answer_" + detail.getQuestionId()));
                        map.put("score_" + detail.getId(), request.getParameter("score_" + detail.getQuestionId()));
                        //主观题老师判分所得分数
                        if (StringUtils.isNotBlank(request.getParameter("score_" + detail.getQuestionId()))) {
                            resultScore = resultScore + Float.parseFloat(request.getParameter("score_" + detail.getQuestionId()));
                        }
                    }
                }
                if (StringUtils.isNotBlank(request.getParameter("reply"))) {
                    examResult.setStudentView(TpExamResult.STUDENT_VIEW_ONE);//学生是否查看过评语
                }
                examResult.setScore(resultScore);//设置学生得分
                examResult.setIsreply(TpExamResult.IS_REPLY_YES);
//				examResult.setReply(request.getParameter("reply"));//老师批语
                //老师批阅给学生主观题判分，且更新te_exam_result表的学生得分(score),且更新老师批阅状态为已批阅
                int count = this.resultService.updateDetailScoreResultAnswer(examResultDetails, map, examResult);
                rmap.put("success", true);
//				UserEntity student = taskService.findUserById(examResult.getStudentId());
//				rmap.put("errMsg", student.getRealName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rmap;
    }

    /**
     * 作业批阅 保存老师评语
     *
     * @param request
     * @param ue
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/markReply")
    @ResponseBody
    public Map markReplySave(HttpServletRequest request, UserEntity ue) {
        int resultId = getParamterForInt("resultid", 0);
        String reply = getParamter("reply");
        Map rmap = new HashMap();
        rmap.put("success", false);
        rmap.put("errMsg", "保存失败,请重试!");
        if (resultId > 0 && StringUtils.isNotEmpty(reply)) {
            int count = resultService.saveMarkReply(resultId, reply);
            if (count > 0) {
                rmap.put("success", true);
            }
        }
        return rmap;
    }

    /**
     * 个人作业详情 只看错题 按序显示题目信息
     *
     * @throws Exception
     */
    @RequestMapping("/seeWrong_{rid}")
    public String seeWrong(HttpServletRequest request, @PathVariable("rid") int rid, UserEntity ue) throws Exception {
        TpExam task = taskService.findTpExamByResultId(rid);
        //为题填入分数,如果detailsMap不为空则填入用户答案
        List<TpExamResultDetail> examResultDetail = resultService.searchFullExamResultDetail(rid);
        Map<String, TpExamResultDetail> detailsMap = new HashMap<String, TpExamResultDetail>();
        for (TpExamResultDetail detail : examResultDetail) {
            detailsMap.put(String.valueOf(detail.getQuestionId()), detail);
        }
        //查询结果中所有大题
        List<TpExamResultDetail> teIdDetail = taskService.findResultDetailTeId(rid + "");
        //查询需要排除的错误率小于等于页面输入错误率的题目数据
        List<QuestionErrUser> resultDetail = taskService.findTpExamResultDetailQuestionIds(rid);
        int viewType = QuestionViewDto.view_type_all;
        List<IExamQuestionDto> questionDtoList = testPaperService.createPartTestPaper(teIdDetail, resultDetail, task.getId(), viewType, detailsMap);
        request.setAttribute("questionDtoList", questionDtoList);
        return "/teacher/task/situationShowPaperResult";
    }


    /**
     * 作业批阅显示试卷
     */
    @RequestMapping("/result_{rid}")
    public String result(@PathVariable("rid") int rid, UserEntity loginUser,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        String result = null;
        int viewType = QuestionViewDto.view_type_piyue;//作业批阅页面专用，显示批阅栏
        try {
            resultAll(request, rid, loginUser, viewType);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "/teacher/task/markShowPaperResult";
    }

    /**
     * 个人作业详情 查看全部
     */
    @RequestMapping("/personResult_{rid}")
    public String resultPerson(@PathVariable("rid") int rid, UserEntity loginUser,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        String result = null;
        int viewType = QuestionViewDto.view_type_all;
        try {
            resultAll(request, rid, loginUser, viewType);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return "/teacher/task/markShowPaperResult";
    }

    public void resultAll(HttpServletRequest request, int rid, UserEntity loginUser, int viewType) throws Exception {
        /* 获取考试结果 */
        TpExamResult examResult = this.resultService.searchById(rid);
        Process process = new Process();
        process.setExamResult(examResult);
        process = this.resultService.searchFullByProcess(process);
        /* 获取考试信息 */
        TpExam exam = this.taskService.queryExamByIdCache(examResult.getExamId());
        process.setExam(exam);
        //将考试结果明细数据组装成map方便通过题目id获取数据
        Map<String, TpExamResultDetail> detailsMap = new HashMap<String, TpExamResultDetail>(0);
        for (TpExamResultDetail detail : process.getExamResultDetails()) {
            detailsMap.put(String.valueOf(detail.getQuestionId()), detail);
        }

        PaperPager paperFilter = new PaperPager();
        paperFilter.setPaperId(exam.getPaperId());
        paperFilter.setDetailsMap(detailsMap);//用户答题结果map
        QuestionBarHtml questionBarHtml = testPaperService.getQuestionBarHtml(exam.getPaperId());
        //试卷预览
        TestPaper testPaper = testPaperService.createOrEditTestPaperResultDetail(paperFilter, loginUser,rid ,viewType);
        QuestionViewDto questionViewDto = new QuestionViewDto();
        questionViewDto.setViewType(QuestionViewDto.view_type_question);
        questionViewDto.setButtonType(QuestionViewDto.button_ype_null);//学生：QuestionViewDto.button_ype_null

        request.setAttribute("questionViewDto", questionViewDto);
        request.setAttribute("testPaper", testPaper);
        request.setAttribute("paperFilter", paperFilter);
        request.setAttribute("exam", exam); // 考试名称
        request.setAttribute("questionBarHtml", questionBarHtml);
        request.setAttribute("detailsMap", detailsMap);
    }

    /**
     * 翻转课堂 选择试卷
     *
     * @return
     */
    @RequestMapping("/classRoom/assign")
    public String classRoomAssignTask(ModelMap modelMap, String classesIds, UserEntity user, Integer subjectId) {
        String[] ids = classesIds.split(",");
        List<Classes> list = classRoomService.findClassesById(ids);
        List<Classes> groupList;
        boolean result;
        Classes c;
        for (Classes classes : list) {
            if (!classes.getType().equals(3)) {
                groupList = classRoomService.findClassesByParentId(subjectId,classes.getId(), user.getId());
                if (null != groupList && groupList.size() > 0) {
                    for (int i = 0; i < groupList.size(); i++) {
                        c = groupList.get(i);
                        result = classRoomService.isExistClassesStudentByClassesId(c.getId());
                        if (!result) {
                            groupList.remove(i);
                            i--;
                        }
                    }
                }
                classes.setGroupList(groupList);
            }
        }
        //JSONArray jsonarray = JSONArray.fromObject(pList);
        modelMap.addAttribute("classesList", list);
        modelMap.addAttribute("urlType", 1);
        return "/teacher/task/assignTask";
    }

    /**
     * 翻转课堂保存课堂作业
     *
     * @param paperId
     * @param classesId
     * @param examName
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/classRoom/save")
    public Map classRoomSave(Integer paperId, String classesId, String examName, UserEntity user) {
        Map map = new HashMap();
        map.put("success", false);
        try {
            if (classesId != null && !"".equals(classesId)) {
                //查询卷子，只为获取卷子tag标签全路径
                TestPaper paper = taskService.findPaperById(paperId);
                String classesIds[] = classesId.split(",");
                //保存作业数据
                int tpExamId = classRoomService.saveClassRoomExam(paper, classesIds, user.getId(), examName);
                map.put("examId", tpExamId);
                map.put("success", true);
                map.put("examName", examName);
                List<String> fullName = classRoomService.findClassesFullNameByIds(classesIds);
                map.put("fullName", StringUtils.join(fullName, ","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     *
     * @param s 原文件名
     * @return 重新编码后的文件名
     */
    public static String toUtf8String(HttpServletRequest request, String s) {
        String agent = request.getHeader("User-Agent");
        try {
            boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
            if (isFireFox) {
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
}
