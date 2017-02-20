package com.koolearn.cloud.teacher.classRoom.controller;

import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.exam.entity.TpExamAttachmentComment;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.util.*;
import com.koolearn.common.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 翻转课堂
 */
@Controller
@RequestMapping("/teacher/classRoom/")
public class TeacherClassRoomController {

    @Resource
    private ClassRoomService classRoomService;

    @Resource
    private ResourceInfoService resourceInfoService;


    private final static int RETURN_STATUS_SUCCESS = 200;
    private final static int RETURN_STATUS_LOSE = 500;
    private final static String CLASS_ROOM_EXAM = "作业";
    private final static String CLASS_ROOM_FILE = "附件";

    /**
     * 翻转课堂首页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "teacher/classRoom/classRoomIndex";
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("search")
    public String search(UserEntity user, Integer subjectId, String searchValue, Integer pageNo, ModelMap map) {
        pageNo = pageNo == null ? 0 : pageNo;
        subjectId = subjectId == null ? -1 : subjectId;
        searchValue = searchValue == null ? "" : searchValue.trim();

        ListPage listPager = classRoomService.findTeacherClassRoomPage(subjectId, searchValue, pageNo, user.getId());
        List<TpExam> resultList = (List<TpExam>) listPager.getResultList();
        List<String> classes;
        List<User> studentNameList;
        boolean finishStatus;
        for (TpExam te : resultList) {
            classes = classRoomService.findClassesFullNameByClassRoomId(te.getId());
            te.setClassesStr(StringUtil.join(classes, " / "));
            studentNameList = classRoomService.findAllStudentNameByClassRoomId(te.getId());
            //总人数
            te.setStudentNum(studentNameList.size());
            //计算总完成率
            TpExamStudent tpExamStudent;
            TaskDto taskDto;
            List<TpExamAttachment> taList = classRoomService.findAttachmentByClassRoomId(te.getId());
            List<User> finishAllStudentNameList = new ArrayList<User>();
            for (User allUser : studentNameList) {
                finishStatus = true;
                for (TpExamAttachment tpExamAttachment : taList) {
                    if (tpExamAttachment.getAttachmentType().equals(2)) {//作业
                        //查看学生是否有此作业
                        tpExamStudent = classRoomService.getTpExamStudent(allUser.getId(), tpExamAttachment.getAttachmentId());
                        if (tpExamStudent != null) {
                            taskDto = classRoomService.getStudentTpExamResult(tpExamAttachment.getAttachmentId(), allUser.getId());
                            if (taskDto == null || 2 != taskDto.getStatus()) {
                                finishStatus = false;
                                break;
                            }
                        } else {
                            finishStatus = false;
                            break;
                        }
                    } else {
                        if (!classRoomService.getFinishAttachment(allUser.getId(), tpExamAttachment.getId())) {
                            finishStatus = false;
                            break;
                        }
                    }
                }
                if (finishStatus) {
                    finishAllStudentNameList.add(allUser);
                }
            }
            te.setHadDoneNum(finishAllStudentNameList.size());
        }


        map.addAttribute("resultList", listPager.getResultList());
        map.addAttribute("listPager", listPager.getListPager());
        map.addAttribute("pageNo", pageNo);
        return "teacher/classRoom/classRoomList";
    }

    /**
     * 创建课堂
     *
     * @return
     */
    @RequestMapping("create")
    public String create(ModelMap map, HttpServletRequest request) {
        map.addAttribute("subjectId", request.getParameter("subjectId"));
        map.addAttribute("rangeId", request.getParameter("rangeId"));
        map.addAttribute("classesId", request.getParameter("classesId"));
        map.addAttribute("uuid", UUID.randomUUID());
        return "teacher/classRoom/addClassRoom";
    }

    /**
     * 保存课堂
     *
     * @return
     */
    @RequestMapping("save")
    public String save(TpExam bean, UserEntity user, Integer submitType) {
        classRoomService.save(bean, user.getId(), submitType);
        return "redirect:/teacher/classRoom/index";
    }

    /**
     * 撤回
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("recall")
    public int recall(Integer id) {
        int code = RETURN_STATUS_LOSE;
        try {
            if (id != null) {
                classRoomService.getClassRoomById(id, "recall");
                code = RETURN_STATUS_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 删除
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public int delete(Integer id) {
        int code = RETURN_STATUS_LOSE;
        try {
            if (id != null) {
                classRoomService.deleteClassRoom(id);
                code = RETURN_STATUS_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 拷贝页面
     *
     * @return
     */
    @RequestMapping("copyIndex")
    public String copyIndex(Integer id, ModelMap map, UserEntity user) {
        TpExam tpExam = new TpExam();
        try {
            if (id != null) {
                tpExam = classRoomService.getClassRoomById(id, "copy");
                if (!tpExam.getTeacherId().equals(user.getId())) {
                    return "redirect:/teacher/classRoom/index";
                }
                map.addAttribute("tpExam", tpExam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/teacher/classRoom/updateIndex?id=" + tpExam.getId();
    }

    /**
     * 修改页面
     *
     * @return
     */
    @RequestMapping("updateIndex")
    public String updateIndex(Integer id, ModelMap map, UserEntity user) {
        try {
            if (id != null) {
                TpExam tpExam = classRoomService.getClassRoomById(id, null);
                if (!tpExam.getTeacherId().equals(user.getId())) {
                    return "redirect:/teacher/classRoom/index";
                }
                if (tpExam.getStatus() == GlobalConstant.TP_EXAM_STATUS_RELEASE ||
                        tpExam.getStatus() == GlobalConstant.TP_EXAM_STATUS_DELETE) {
                    return "redirect:/teacher/classRoom/index";
                }
                if (tpExam.getEndTime() != null) {
                    if (tpExam.getEndTime().getTime() < new Date().getTime()) {
                        return "redirect:/teacher/classRoom/index";
                    }
                }
                map.addAttribute("tpExam", tpExam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "teacher/classRoom/updateClassRoom";
    }


    /**
     * 修改or拷贝
     *
     * @param bean
     * @param user
     * @param submitType
     * @return
     */
    @RequestMapping("update")
    public String update(TpExam bean, UserEntity user, Integer submitType) {
        //权限
        TpExam tpExam = classRoomService.getClassRoomById(bean.getId());
        if (tpExam != null && tpExam.getTeacherId().equals(user.getId())) {
            classRoomService.update(bean, user.getId(), submitType);
        }
        return "redirect:/teacher/classRoom/index";
    }

    /**
     * 获取学科学段
     *
     * @param user
     * @return
     */
    @RequestMapping("getSubject")
    @ResponseBody
    public List<SelectDTO> getSubject(UserEntity user) {
        return DataSelectUtil.getInstance().getSubjectName(user.getId());
    }

    /**
     * 修改附件名称
     *
     * @param attachmentId
     * @param attachmentName
     * @return
     */
    @RequestMapping("updateAttachmentName")
    @ResponseBody
    public int updateAttachmentName(Integer attachmentId, String attachmentName) {
        int code = RETURN_STATUS_LOSE;
        try {
            if (attachmentId != null) {
                classRoomService.updateAttachmentName(attachmentId, attachmentName);
                code = RETURN_STATUS_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取老师班级
     *
     * @param user
     * @return
     */
    @RequestMapping("getClasses")
    @ResponseBody
    public List<Classes> getClasses(UserEntity user, Integer subjectId, Integer rangeId) {
        //List<Classes> lists = teacherAddClassService.findAllClassByRangeSub(user.getSchoolId() ,rangeId, subjectId,user.getId());
        List<Classes> lists = classRoomService.findAllClassByRangeSub(rangeId, subjectId, user.getId());
        boolean result;
        Classes c;
        Classes c1;
        List<Classes> groupList;
        if (null != lists && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                c = lists.get(i);
                result = classRoomService.isExistClassesStudentByClassesId(c.getId());
                if (result) {
                    groupList = classRoomService.findClassesGroupByTeacherIdSubjectId(subjectId, user.getId(), c.getId());
                    if (null != groupList && groupList.size() > 0) {
                        for (int j = 0; j < groupList.size(); j++) {
                            c1 = groupList.get(j);
                            result = classRoomService.isExistClassesStudentByClassesId(c1.getId());
                            if (!result) {
                                groupList.remove(j);
                                j--;
                            }
                        }
                    }
                    c.setGroupList(groupList);
                } else {
                    lists.remove(i);
                    i--;
                }
            }
        }
        return lists;
    }

    /**
     * 播放课堂
     *
     * @param classRoomId
     * @param user
     * @param map
     * @param classesId
     * @return
     */
    @RequestMapping("previewPlayer")
    public String previewPlayer(Integer classRoomId, UserEntity user, ModelMap map, Integer classesId) {
        TpExam tpExam = classRoomService.getClassRoomById(classRoomId, null);
        if (!tpExam.getTeacherId().equals(user.getId())) {
            return "redirect:/teacher/classRoom/index";
        }
        Classes classes = classRoomService.getClassesById(classesId);
        if (classes != null) {
            map.addAttribute("classesName", classes.getFullName());
        }
        List<TpExamAttachment> taList = classRoomService.findAttachmentByClassRoomId(classRoomId);
        // 过滤作业
        ExamFilter(classesId, taList);
        map.addAttribute("classesId", classesId);
        map.addAttribute("classRoomId", classRoomId);
        map.addAttribute("taList", taList);
        map.addAttribute("tpExam", tpExam);

        return "teacher/classRoom/preview/previewPlayer";
    }

    /**
     * 预览详情
     *
     * @param classRoomId
     * @param user
     * @return
     */
    @RequestMapping("previewDetail")
    public String previewDetail(Integer classRoomId, UserEntity user, ModelMap map, Integer classesId) {
        TpExam tpExam = classRoomService.getClassRoomById(classRoomId, null);
        if (!tpExam.getTeacherId().equals(user.getId())) {
            return "redirect:/teacher/classRoom/index";
        }
        List<Classes> cList = classRoomService.findClassesByClassRoomId(classRoomId);
        if (classesId == null && cList != null && cList.size() > 0) {
            classesId = cList.get(0).getId();
        }
        List<TpExamAttachment> taList = classRoomService.findAttachmentByClassRoomId(classRoomId);
        //过滤作业
        ExamFilter(classesId, taList);
        map.addAttribute("cList", cList);
        map.addAttribute("classesId", classesId);
        map.addAttribute("classRoomId", classRoomId);
        map.addAttribute("taList", taList);
        map.addAttribute("tpExam", tpExam);

        return "teacher/classRoom/preview/previewDetail";
    }

    /**
     * 过滤课堂作业
     *
     * @param classesId
     * @param taList
     */
    private void ExamFilter(Integer classesId, List<TpExamAttachment> taList) {
        TpExamAttachment ta;
        for (int i = 0; i < taList.size(); i++) {
            ta = taList.get(i);
            if (ta.getAttachmentType().equals(2)) {
                boolean isExist = classRoomService.isExistExamByClassesId(ta.getAttachmentId(), classesId);
                if (!isExist) {
                    boolean isExistChild = classRoomService.isExistExamByClassesParentId(ta.getAttachmentId(), classesId);
                    if (!isExistChild) {
                        taList.remove(i);
                        i--;
                        continue;
                    }
                }
                ta.setTypeStr(CLASS_ROOM_EXAM);
            } else if (ta.getAttachmentType().equals(0)) {
                ta.setTypeStr(CLASS_ROOM_FILE);
            } else {
                ResourceInfo resourceInfo = resourceInfoService.getResoueceById(ta.getAttachmentId());
                String str = DataDictionaryUtil.getInstance().getDictionaryName(6, resourceInfo.getType());
                ta.setTypeStr(str);
            }
        }
    }

    /**
     * 资源预览
     *
     * @param modelMap
     * @param tpExamAttachmentId
     * @param sType              预览or播放
     * @return
     */
    @RequestMapping("reader")
    public String reader(ModelMap modelMap, Integer tpExamAttachmentId, UserEntity user, String sType, Integer classesId, Integer classRoomId) throws Exception {
        String pageUrl;
        modelMap.addAttribute("classesId", classesId);
        classesId = classesId == null ? 0 : classesId;
        //查询课堂班级下所有学生名称
        List<User> studentNameList = classRoomService.findStudentNameByClassRoomId(classRoomId, classesId);
        //所有附件
        List<TpExamAttachment> taList = classRoomService.findAttachmentByClassRoomId(classRoomId);
        //单个附件完成学生名单
        List<User> finishStudentNameList;
        //单个附件未完成学生名单
        List<User> unFinishStudentNameList;
        //单个课堂作业未完成学生名单
        List<User> studentList;
        //所有附件完成学生名单
        List<User> finishAllStudentNameList = new ArrayList<User>();
        Classes classes;
        if (tpExamAttachmentId == null) {//总览
            double allRate = 0;
            //计算单个附件的完成率
            if (taList != null && taList.size() > 0 && studentNameList != null && studentNameList.size() > 0) {
                double rate = 0;
                TpExamAttachment ta;
                for (int i = 0; i < taList.size(); i++) {
                    ta = taList.get(i);
                    unFinishStudentNameList = new ArrayList<User>(studentNameList);
                    if (ta.getAttachmentType().equals(2)) {
                        boolean isExist = classRoomService.isExistExamByClassesId(ta.getAttachmentId(), classesId);
                        if (!isExist) {
                            boolean isExistChild = classRoomService.isExistExamByClassesParentId(ta.getAttachmentId(), classesId);
                            if (!isExistChild) {
                                taList.remove(i);
                                i--;
                                continue;
                            }
                        }
                        List<Integer> classesIdList = new ArrayList<Integer>();
                        classes = classRoomService.getClassesById(classesId);
                        classesIdList.add(classes.getId());
                        if (!classes.getType().equals(3)) {
                            List<Integer> childClassesIds = classRoomService.findClassesChild(classes.getId());
                            classesIdList.addAll(childClassesIds);
                        }
                        //课堂作业对应班级下未完成作业学生
                        studentList = classRoomService.findUnFinishExamResult(ta.getAttachmentId(), classesIdList);
                        //课堂作业对应班级下完成作业学生
                        finishStudentNameList = classRoomService.findFinishExamResult(ta.getAttachmentId(), classesIdList);
                        //作业完成率
                        if (studentList != null && studentList.size() > 0) {
                            rate = (Double.valueOf(finishStudentNameList.size()) / Double.valueOf(studentList.size()));
                        }
                        //作业未完成学生
                        studentList.removeAll(finishStudentNameList);
                        ta.setUnFinishStudentName(listToString(studentList));
                    } else {
                        //附件完成学生
                        finishStudentNameList = classRoomService.findFinishAttachment(ta.getId(), classesId);
                        //附件完成率
                        if (unFinishStudentNameList.size() > 0) {
                            rate = (Double.valueOf(finishStudentNameList.size()) / Double.valueOf(unFinishStudentNameList.size()));
                        }
                        //附件未完成学生
                        unFinishStudentNameList.removeAll(finishStudentNameList);
                        ta.setUnFinishStudentName(listToString(unFinishStudentNameList));
                    }
                    ta.setRate(rate);
                }
                //计算总完成率
                TpExamStudent tpExamStudent;
                TaskDto taskDto;
                for (User allUser : studentNameList) {
                    boolean finishStatus = true;
                    for (TpExamAttachment tpExamAttachment : taList) {
                        if (tpExamAttachment.getAttachmentType().equals(2)) {//作业
                            //查看学生是否有此作业
                            tpExamStudent = classRoomService.getTpExamStudent(allUser.getId(), tpExamAttachment.getAttachmentId());
                            if (tpExamStudent != null) {
                                taskDto = classRoomService.getStudentTpExamResult(tpExamAttachment.getAttachmentId(), allUser.getId());
                                if (taskDto == null || 2 != taskDto.getStatus()) {
                                    finishStatus = false;
                                    break;
                                }
                            } else {
                                finishStatus = false;
                                break;
                            }
                        } else {
                            if (!classRoomService.getFinishAttachment(allUser.getId(), tpExamAttachment.getId())) {
                                finishStatus = false;
                                break;
                            }
                        }
                    }
                    if (finishStatus) {
                        finishAllStudentNameList.add(allUser);
                    }
                }
                //未完成学生
                int studentSize = studentNameList.size();
                studentNameList.removeAll(finishAllStudentNameList);
                int unStudentSize = finishAllStudentNameList.size();
                //未完成名单
                String allStudentName = listToString(studentNameList);
                //查询总完成率
                allRate = Double.valueOf(unStudentSize) / Double.valueOf(studentSize);
                modelMap.addAttribute("allStudentName", allStudentName);
            }
            modelMap.addAttribute("allRate", allRate);
            modelMap.addAttribute("taList", taList);
            return "teacher/classRoom/preview/classRoomList";
        }
        TpExamAttachment ta = classRoomService.getTpExamAttachmentById(tpExamAttachmentId);
        if (ta.getAttachmentType().equals(2)) {//作业
            modelMap.addAttribute("examId", ta.getAttachmentId());
            modelMap.addAttribute("examName", ta.getAttachmentName());
            return "teacher/classRoom/preview/classRoomExamList";
        } else {//资源
            //附件完成学生
            finishStudentNameList = classRoomService.findFinishAttachment(ta.getId(), classesId);

            Integer resourceId = ta.getAttachmentId();
            if (resourceId == null) {
                throw new RuntimeException("资源ID不存在");
            }
            ResourceInfo resource = resourceInfoService.searchResourceById(resourceId);
            if (resource == null) {
                throw new RuntimeException("资源不存在");
            }
            resource.setCollection(resourceInfoService.isCollection(user.getId(), resource.getId(), 1));
            pageUrl = ResourceReaderUtil.getPageUrl(ResourceReaderUtil.READER_URL_TYPE_T_C, resource, modelMap);
            modelMap.addAttribute("resource", resource);
        }
        //完成率
        if (studentNameList != null && studentNameList.size() > 0) {
            //附件完成率
            double rate = (Double.valueOf(finishStudentNameList.size()) / Double.valueOf(studentNameList.size()));
            //附件未完成学生
            studentNameList.removeAll(finishStudentNameList);
            ta.setUnFinishStudentName(listToString(studentNameList));
            ta.setRate(rate);
        } else {
            ta.setRate(0.0);
        }

        modelMap.addAttribute("cList", classRoomService.findCommentByExamAttachmentId(tpExamAttachmentId));
        modelMap.addAttribute("ta", ta);
        modelMap.addAttribute("sType", sType);
        modelMap.addAttribute("tpExamAttachmentId", tpExamAttachmentId);
        return pageUrl;
    }

    private String listToString(List<User> list) {
        if (list != null && list.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (User user : list) {
                str.append(user.getRealName() + "、");
            }
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    /**
     * 查询回复
     *
     * @param tpExamAttachmentId
     * @return
     */
    @RequestMapping("findComment")
    public String findComment(ModelMap map, int tpExamAttachmentId) {
        List<TpExamAttachmentComment> commentLists = classRoomService.findCommentByExamAttachmentId(tpExamAttachmentId);
        map.addAttribute("cList", commentLists);
        return "teacher/classRoom/preview/playerComment";
    }

    /**
     * 保存回复
     *
     * @param comment
     * @param user
     * @return
     */
    @RequestMapping("saveComment")
    @ResponseBody
    public TpExamAttachmentComment saveComment(TpExamAttachmentComment comment, UserEntity user) {
        try {
            if (comment.getExamAttachmentId() != null && user != null) {
                comment = classRoomService.saveComment(comment, user);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                comment.setCreateTimeStr(sdf.format(comment.getCreateTime()));
                return comment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
