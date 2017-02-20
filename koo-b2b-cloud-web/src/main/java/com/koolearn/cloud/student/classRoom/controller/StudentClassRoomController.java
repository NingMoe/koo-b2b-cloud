package com.koolearn.cloud.student.classRoom.controller;

import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.entity.TpExamAttachment;
import com.koolearn.cloud.exam.entity.TpExamAttachmentComment;
import com.koolearn.cloud.exam.entity.TpExamStudentNote;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.resource.service.ResourceInfoService;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.task.service.TaskService;
import com.koolearn.cloud.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 学生翻转课堂
 */
@Controller
@RequestMapping("/student/classRoom/")
public class StudentClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ResourceInfoService resourceInfoService;

    /**
     * 学生课堂首页
     *
     * @param user
     * @param map
     * @return
     */
    @RequestMapping("index")
    public String index(UserEntity user, ModelMap map, Integer pageNo, String searchValue, Integer subjectId, String endTimeStr) {
        pageNo = pageNo == null ? 0 : pageNo;
        searchValue = searchValue == null ? "" : searchValue.trim();
        endTimeStr = endTimeStr == null ? "" : endTimeStr;
        subjectId = subjectId == null ? 0 : subjectId;
        List<TeacherBookVersion> subjectList = taskService.findClassesTeacherSubject(user, GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM);
        if (subjectList != null && subjectList.size() > 0) {
            if (subjectId == 0) {
                subjectId = subjectList.get(0).getSubjectId();
            } else {
                //取消更新提示
                classRoomService.deleteSubjectView(user.getId(), subjectId);
            }

            //处理实时学科进度点提醒
            for (TeacherBookVersion tb : subjectList) {
                boolean view = classRoomService.findSubjectView(user.getId(), tb.getSubjectId());
                if (view) {
                    tb.setTagStatus(1);
                }
            }

            //查询所有课堂
            ListPage listPager = classRoomService.findStudentClassRoom(user.getId(), subjectId, pageNo, searchValue, endTimeStr);
            List<TpExam> tpExamList = listPager.getResultList();
            //查询课堂完成率
            List<TpExamAttachment> teaList;
            TpExamStudent tes;
            for (TpExam tpExam : tpExamList) {
                double finishCount = 0;
                teaList = classRoomService.findAttachmentByClassRoomId(tpExam.getId());
                finishCount = setFinishCount(user, teaList, finishCount);
                if (teaList == null || teaList.size() == 0) {
                    tpExam.setRate(0);
                } else {
                    tpExam.setRate(finishCount / teaList.size());
                }
                tes = classRoomService.getTpExamStudent(user.getId(), tpExam.getId());
                tpExam.setFinishTime(tes.getFinishTime());
            }

            map.addAttribute("tpExamList", tpExamList);
            map.addAttribute("listPager", listPager.getListPager());
        }

        map.addAttribute("pageNo", pageNo);
        map.addAttribute("subjectId", subjectId);
        map.addAttribute("searchValue", searchValue);
        map.addAttribute("endTimeStr", endTimeStr);
        map.addAttribute("subjectList", subjectList);
        return "student/classRoom/classRoomIndex";
    }


    /**
     * 课堂详情
     *
     * @param user
     * @param map
     * @param tpExamId
     * @return
     */
    @RequestMapping("detail/{tpExamId}/{subjectId}")
    public String detail(UserEntity user, ModelMap map,
                         @PathVariable("tpExamId") Integer tpExamId, @PathVariable("subjectId") Integer subjectId, HttpServletRequest request) {
        //权限
        if (!classRoomService.getClassRoom(tpExamId, user.getId())) {
            return "redirect:/student/classRoom/index";
        }

        List<TpExamAttachment> tpExamAttachmentList = classRoomService.findAttachmentByClassRoomId(tpExamId);
        ExamFilter(user.getId(), tpExamAttachmentList);
        map.addAttribute("tpExamAttachmentList", tpExamAttachmentList);
        if (tpExamAttachmentList != null && tpExamAttachmentList.size() > 0) {
            map.addAttribute("tpExamAttachment", tpExamAttachmentList.get(0));
            map.addAttribute("tpExamAttachmentId", tpExamAttachmentList.get(0).getId());
        }
        map.addAttribute("tpExamId", tpExamId);
        map.addAttribute("pageNo", request.getParameter("pageNo") == null ? 0 : request.getParameter("pageNo"));
        map.addAttribute("subjectId", subjectId);
        map.addAttribute("searchValue", request.getParameter("searchValue"));
        map.addAttribute("endTimeStr", request.getParameter("endTimeStr"));
        return "student/classRoom/classRoomDetail";
    }

    /**
     * 过滤班级作业
     *
     * @param studentId
     * @param taList
     */
    private void ExamFilter(Integer studentId, List<TpExamAttachment> taList) {
        TpExamAttachment ta;
        for (int i = 0; i < taList.size(); i++) {
            ta = taList.get(i);
            if (ta.getAttachmentType().equals(2)) {
                boolean isExist = classRoomService.isExistExamByStudent(ta.getAttachmentId(), studentId);
                if (!isExist) {
                    taList.remove(i);
                    i--;
                }
                ta.setTypeStr("作业");
            } else if (ta.getAttachmentType().equals(0)) {
                ta.setTypeStr("附件");
            } else {
                ResourceInfo resourceInfo = resourceInfoService.getResoueceById(ta.getAttachmentId());
                String str = DataDictionaryUtil.getInstance().getDictionaryName(6, resourceInfo.getType());
                ta.setTypeStr(str);
            }
        }
    }

    private Double setFinishCount(UserEntity user, List<TpExamAttachment> teaList, Double finishCount) {
        TpExamAttachment ta;
        for (int i = 0; i < teaList.size(); i++) {
            ta = teaList.get(i);
            if (ta.getAttachmentType().equals(2)) {
                boolean isExist = classRoomService.isExistExamByStudent(ta.getAttachmentId(), user.getId());
                if (!isExist) {
                    teaList.remove(i);
                    i--;
                    continue;
                }
                if (classRoomService.getExamComplete(user.getId(), ta.getAttachmentId())) {
                    finishCount++;
                }
            } else {
                if (classRoomService.getComplete(user.getId(), ta.getId())) {
                    finishCount++;
                }
            }
        }
        return finishCount;
    }

    /**
     * 课件学习
     *
     * @param tpExamAttachmentId
     * @param user
     * @param map
     * @return
     */
    @RequestMapping("reader")
    public String reader(Integer tpExamId, Integer tpExamAttachmentId, UserEntity user, ModelMap map, String submitType) throws Exception {
        String pageUrl;

        //权限
        if (!classRoomService.getClassRoom(tpExamId, user.getId())) {
            return "redirect:/student/classRoom/index";
        }
        TpExam tpExam = classRoomService.getClassRoomById(tpExamId);

        //上一节,下一节
        if (submitType != null && !"".equals(submitType)) {
            List<String> tpExamAttachmentList = classRoomService.findAttachmentIdsByClassRoomId(tpExamId);
            int index = tpExamAttachmentList.indexOf(String.valueOf(tpExamAttachmentId));
            if (submitType.equals("pre")) {//上一节
                if (index > 0) {
                    tpExamAttachmentId = Integer.valueOf(tpExamAttachmentList.get(index - 1));
                }
            } else if (submitType.equals("next")) {//下一节
                if (index < tpExamAttachmentList.size() - 1) {
                    tpExamAttachmentId = Integer.valueOf(tpExamAttachmentList.get(index + 1));
                }
            }
        }


        map.addAttribute("tpExamId", tpExamId);
        map.addAttribute("tpExamAttachmentId", tpExamAttachmentId);
        TpExamAttachment tpExamAttachment = classRoomService.getTpExamAttachmentById(tpExamAttachmentId);
        map.addAttribute("tpExamAttachment", tpExamAttachment);
        if (tpExamAttachment.getAttachmentType().equals(2)) {
            //作业
            TaskDto ta = classRoomService.getStudentTpExamResult(tpExamAttachment.getAttachmentId(), user.getId());
            long nowIntDate = new Date().getTime();
            if (ta == null) {
                //开始作业
                pageUrl = "student/classRoom/classRoomExam";
            } else if (TpExamResult.STATUS_PROCESSING == ta.getStatus() && ta.getEndTime().getTime() > nowIntDate) {
                //继续作业
                pageUrl = "student/classRoom/classRoomExam";
            } else if (TpExamResult.STATUS_COMPLETE == ta.getStatus()
                    || (ta.getStatus() == TpExamResult.STATUS_PRE && ta.getEndTime().getTime() < nowIntDate)) {
                //复习作业
                map.addAttribute("resultId", ta.getId());
                pageUrl = "student/classRoom/classRoomExamFinish";
            } else if (TpExamResult.STATUS_PROCESSING == ta.getStatus() && ta.getEndTime().getTime() < nowIntDate) {
                pageUrl = "student/classRoom/classRoomExamUnFinish";
            } else {
                //开始作业
                pageUrl = "student/classRoom/classRoomExam";
            }
            map.addAttribute("examId", tpExamAttachment.getAttachmentId());
            map.addAttribute("studentId", user.getId());
            return pageUrl;
        } else {
            ResourceInfo resource = resourceInfoService.getResoueceById(tpExamAttachment.getAttachmentId());
            pageUrl = ResourceReaderUtil.getPageUrl(ResourceReaderUtil.READER_URL_TYPE_S_C,resource, map);
            map.addAttribute("resource", resource);
        }

        //讨论
        List<TpExamAttachmentComment> commentList = classRoomService.findCommentByExamAttachmentId(tpExamAttachmentId);

        //学习人数
        addLearning(user, tpExamAttachment.getId());
        List<LineUser> userList = getUserCache(tpExamAttachment.getId());

        //笔记
        List<TpExamStudentNote> noteList = classRoomService.findNote(user.getId(), tpExamAttachment.getId());

        //增加学习状态
        if (tpExam.getFinishStatus() == 1) {
            //是否学习过
            boolean learning = classRoomService.getComplete(user.getId(), tpExamAttachmentId);
            if (!learning) {
                classRoomService.saveComplete(user.getId(), tpExamAttachmentId);
            }
        }

        TpExamStudent tes = classRoomService.getTpExamStudent(user.getId(), tpExamId);
        //保存完成时间
        if (tes != null && tes.getFinishTime() == null) {
            double finishCount = 0;
            List<TpExamAttachment> teaList = classRoomService.findAttachmentByClassRoomId(tpExam.getId());
            finishCount = setFinishCount(user, teaList, finishCount);
            if (finishCount == teaList.size()) {
                classRoomService.updateFinishTime(user.getId(), tpExamId);
            }
        }

        map.addAttribute("cList", commentList);
        map.addAttribute("noteList", noteList);
        map.addAttribute("userList", userList);
        return pageUrl;

    }

    /**
     * 查询学习人数
     *
     * @param tpExamAttachmentId
     * @return
     */
    @RequestMapping("findLearning")
    @ResponseBody
    public List<LineUser> findLearning(UserEntity user, Integer tpExamAttachmentId) {
        addLearning(user, tpExamAttachmentId);
        return getUserCache(tpExamAttachmentId);
    }

    /**
     * 添加学习缓存
     *
     * @param user
     * @param tpExamAttachmentId
     */
    private void addLearning(UserEntity user, Integer tpExamAttachmentId) {
        if (tpExamAttachmentId != null) {
            LineUser u = new LineUser();
            u.setImageUrl(user.getIco());
            u.setName(user.getRealName());
            CacheTools.addCache("learning_" + tpExamAttachmentId + "_" + user.getId(), 70, u);
        }
    }

    /**
     * 获取学生学习缓存
     *
     * @param tpExamAttachmentId
     * @return
     */
    private List<LineUser> getUserCache(Integer tpExamAttachmentId) {
        if (tpExamAttachmentId != null) {
            Set<String> stringSet = CacheTools.getKeys("learning_" + tpExamAttachmentId + "_*");
            List<LineUser> userEntityList = new ArrayList<LineUser>();
            LineUser user;
            String _str;
            for (String str : stringSet) {
                _str = str.substring(6, str.length());
                user = CacheTools.getCacheForever(_str, LineUser.class);
                if (null != user) {
                    userEntityList.add(user);
                }
            }
            return userEntityList;
        }
        return null;
    }


      /*private List<UserEntity> findLearningNum(UserEntity user, int tpExamAttachmentId) {
        //获取旧学习状态
        //查询用户下最近一次学习课程id
        CacheTools.getCache("cloud_cloud_*", List.class);
        Integer _tpExamAttachmentId = CacheTools.getCache("user_" + user.getId(), Integer.class);
        //重复学一次课程
        if (_tpExamAttachmentId != null && _tpExamAttachmentId.equals(tpExamAttachmentId)) {
            return CacheTools.getCache("learning_" + tpExamAttachmentId, List.class);
        }
        if (_tpExamAttachmentId != null) {
            //删除旧学习状态
            List<UserEntity> _userEntityList = CacheTools.getCache("learning_" + _tpExamAttachmentId, List.class);
            if (_userEntityList != null && _userEntityList.size() > 0) {
                for (int i = 0; i < _userEntityList.size(); i++) {
                    if (user.getId().equals(_userEntityList.get(i).getId())) {
                        _userEntityList.remove(i);
                        i--;
                    }
                }
                CacheTools.addCache("learning_" + tpExamAttachmentId, _userEntityList);
            }
        }

        //加入新学习状态
        List<UserEntity> userEntityList = CacheTools.getCache("learning_" + tpExamAttachmentId, List.class);
        if (userEntityList == null || userEntityList.size() == 0) {
            userEntityList = new ArrayList<UserEntity>();
        }
        userEntityList.add(user);
        //新的学习课程总人数
        CacheTools.addCache("learning_" + tpExamAttachmentId, userEntityList);
        //新的个人学习课程记录
        CacheTools.addCache("user_" + user.getId(), tpExamAttachmentId);

        return userEntityList;
    }*/

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

    /**
     * 保存笔记
     *
     * @param user
     * @param tpExamAttachmentId
     * @param note
     * @return
     */
    @RequestMapping("saveNote")
    @ResponseBody
    public TpExamStudentNote saveNote(UserEntity user, Integer tpExamAttachmentId, String note) {
        TpExamStudentNote t = null;
        try {
            if (tpExamAttachmentId != null) {
                t = new TpExamStudentNote();
                t.setComment(note);
                t.setCreateTime(new Date());
                t.setExamAttachmentId(tpExamAttachmentId);
                t.setStudentId(user.getId());
                classRoomService.saveNote(t);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                t.setCreateTimeStr(sdf.format(t.getCreateTime()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;


    }


}
