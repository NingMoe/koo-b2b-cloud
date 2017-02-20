package com.koolearn.cloud.classRoom.service.impl;

import com.koolearn.cloud.classRoom.dao.ClassRoomDao;
import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.exam.entity.*;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dao.ResourceDao;
import com.koolearn.cloud.resource.entity.ResourceInfo;
import com.koolearn.cloud.task.dto.TaskDto;
import com.koolearn.cloud.task.entity.TpExamStudent;
import com.koolearn.cloud.teacherInfo.dao.TeacherAddClassDao;
import com.koolearn.cloud.util.*;
import com.koolearn.framework.common.page.ListPager;
import org.apache.commons.lang3.StringUtils;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 16/4/19.
 */
public class ClassRoomServiceImpl implements ClassRoomService {

    private ClassRoomDao classRoomDao;

    private TeacherAddClassDao teacherAddClassDao;

    private ResourceDao resourceDao;

    public void setTeacherAddClassDao(TeacherAddClassDao teacherAddClassDao) {
        this.teacherAddClassDao = teacherAddClassDao;
    }

    public void setClassRoomDao(ClassRoomDao classRoomDao) {
        this.classRoomDao = classRoomDao;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Override
    public ListPage findTeacherClassRoomPage(int subjectId, String searchValue, int pageNo, int userId) {
        ListPage listPage = new ListPage();
        ListPager listPager = new ListPager();
        listPager.setPageNo(pageNo);
        listPager.setPageSize(12);
        if (searchValue != null && !"".equals(searchValue.trim())) {
            searchValue = "%" + searchValue + "%";
        }
        int count = classRoomDao.findTeacherClassRoomPageCount(subjectId, searchValue, userId);
        listPager.setTotalRows(count + 1);
        //List<TpExam> lists = classRoomDao.findTeacherClassRoomPage(subjectIdStr, searchValue, listPager, userId);
        List<TpExam> lists = classRoomDao.findTeacherClassRoomPage(subjectId, searchValue, userId, pageNo);
        listPage.setResultList(lists);
        listPage.setListPager(listPager);
        return listPage;
    }

    @Override
    public void updateAttachmentName(int attachmentId, String attachmentName) {
        classRoomDao.updateAttachmentName(attachmentId, attachmentName);
    }

    @Override
    public int create(TpExam classRoom) {
        return classRoomDao.create(classRoom);
    }

    @Override
    public List<Classes> findClassesGroupByTeacherIdSubjectId(int subjectId,int teacherId, int classesId) {
        return classRoomDao.findClassesGroupByTeacherIdSubjectId(subjectId,teacherId, classesId);
    }

    @Override
    public void save(TpExam te, int teacherId, int type) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            te.setPaperId(0);
            if (type == 1) {//发布
                te.setStatus(GlobalConstant.TP_EXAM_STATUS_RELEASE);
            } else {
                te.setStatus(GlobalConstant.TP_EXAM_STATUS_NEW);
            }
            //保存课堂
            te.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (StringUtils.isNotEmpty(te.getStartTimeStr())) {
                te.setStartTime(format.parse(te.getStartTimeStr()));
            }
            if (StringUtils.isNotEmpty(te.getEndTimeStr())) {
                te.setEndTime(format.parse(te.getEndTimeStr()));
            }

            te.setTeacherId(teacherId);
            if (te.getTagId() != null) {
                te.setTagFullPath(KlbTagsUtil.getInstance().getTagFullId(te.getTagId()));
                te.setTagStr(KlbTagsUtil.getInstance().getCacheTagFullPathName(te.getTagFullPath(), 1));
            }
            te.setCreateTime(new Date());
            int classRoomId = classRoomDao.saveTpExam(conn, te);
            //课件
            setClassRoomProperty(te, teacherId, conn, classRoomId,type);
            conn.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }

    }

    @Override
    public List<String> findClassesFullNameByClassRoomId(int classRoomId) {
        return classRoomDao.findClassesFullNameByClassRoomId(classRoomId);
    }

    @Override
    public List<Classes> findClassesById(String[] classesIds) {
        return classRoomDao.findClassesById(classesIds);
    }

    @Override
    public int saveClassRoomExam(TestPaper paper, String[] classesIds, int teacherId, String examName) {
        Connection conn = ConnUtil.getTransactionConnection();
        int tcId = 0;
        try {
            //创建课堂作业
            TpExam te = new TpExam();
            te.setPaperId(paper.getId());
            te.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM_WORk);
            te.setCreateTime(new Date());
            te.setTeacherId(teacherId);
            te.setExamName(examName);
            te.setSubjectId(paper.getSubjectId());
            te.setRangeId(paper.getRangeId());
            te.setStatus(GlobalConstant.TP_EXAM_STATUS_NEW);
            tcId = classRoomDao.saveTpExam(conn, te);
            //创建课堂作业布置班级
            TpExamStudent ts;
            if (classesIds != null && classesIds.length > 0) {
                for (String cId : classesIds) {
                    //查询班级、小组下所有的学生
                    List<Integer> studentIdList = classRoomDao.findStudentByClassesId(Integer.valueOf(cId));
                    if (studentIdList != null && studentIdList.size() > 0) {
                        for (Integer studentId : studentIdList) {
                            ts = new TpExamStudent();
                            ts.setTeacherId(teacherId);
                            ts.setPaperId(paper.getId());
                            ts.setExamId(tcId);
                            ts.setCreateTime(new Date());
                            ts.setStatus(GlobalConstant.STATUS_ON);
                            ts.setClassesId(Integer.valueOf(cId));
                            ts.setStudentId(studentId);
                            ts.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM_WORk);
                            classRoomDao.saveTpExamStudent(conn, ts);
                        }
                    }
                }
            }
            conn.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }
        return tcId;
    }

    @Override
    public List<String> findClassesFullNameByIds(String[] classesIds) {
        return classRoomDao.findClassesFullNameByIds(classesIds);
    }

    @Override
    public TpExam getClassRoomById(int classRoomId, String type) {
        TpExam tpExam = classRoomDao.getClassRoomById(classRoomId);
        //附件
        List<TpExamAttachment> teaList = classRoomDao.findAttachmentByClassRoomId(classRoomId);

        if (type != null && "copy".equals(type)) {
            if (tpExam != null) {
                Connection conn = ConnUtil.getTransactionConnection();
                try {
                    //复制后新课堂
                    tpExam.setStatus(GlobalConstant.TP_EXAM_STATUS_NEW);
                    tpExam.setCreateTime(new Date());
                    tpExam.setStartTime(null);
                    tpExam.setEndTime(null);
                    int newExamId = classRoomDao.saveTpExam(conn, tpExam);
                    //新课堂学生
                    List<TpExamStudent> studentList = classRoomDao.findTpExamStudent(classRoomId);
                    if (studentList != null && studentList.size() > 0) {
                        for (TpExamStudent student : studentList) {
                            student.setExamId(newExamId);
                            student.setView(1);
                        }
                        classRoomDao.saveTpExamStudent(conn, studentList);
                    }
                    //新课堂附件
                    if (teaList != null && teaList.size() > 0) {
                        List<TpExamStudent> _studentList;
                        for (TpExamAttachment tea : teaList) {
                            //新课堂作业
                            if (tea.getAttachmentType() == 2) {
                                int oldAttachmentId = tea.getAttachmentId();
                                //生成新课堂作业
                                int teId = classRoomDao.saveTpExam(conn, classRoomDao.getClassRoomById(oldAttachmentId));
                                tea.setAttachmentId(teId);
                                //新课堂作业学生
                                _studentList = classRoomDao.findTpExamStudent(oldAttachmentId);
                                if (_studentList != null && _studentList.size() > 0) {
                                    for (TpExamStudent student : _studentList) {
                                        student.setExamId(teId);
                                    }
                                    classRoomDao.saveTpExamStudent(conn, _studentList);
                                }
                                tea.setClassesFullNames(StringUtils.join(classRoomDao.findClassesFullNameByClassRoomId(conn, oldAttachmentId), ","));
                            } else if (tea.getAttachmentType() == 1) {
                                ResourceInfo resourceInfo = resourceDao.getResourceById(tea.getAttachmentId());
                                tea.setTypeStr(resourceInfo != null ? DataDictionaryUtil.getInstance().getDictionaryName(6, resourceInfo.getType()) : "附件");
                            }
                            tea.setExamId(newExamId);
                            //生成新的附件
                            classRoomDao.saveClassRoomAttachment(conn, tea);
                        }
                    }
                    conn.commit();
                    tpExam.setAttachments(teaList);
                } catch (Throwable e) {
                    e.printStackTrace();
                    ConnUtil.rollbackConnection(conn);
                } finally {
                    ConnUtil.closeConnection(conn);
                }
            }
        }
        if (type != null && "recall".equals(type)) {
            if (tpExam != null) {
                Connection conn = ConnUtil.getTransactionConnection();
                try {
                    //复制后新课堂
                    tpExam.setStatus(GlobalConstant.TP_EXAM_STATUS_RECALL);
                    tpExam.setStartTime(null);
                    tpExam.setEndTime(null);
                    int newExamId = classRoomDao.saveTpExam(conn, tpExam);
                    //新课堂学生
                    List<TpExamStudent> studentList = classRoomDao.findTpExamStudent(classRoomId);
                    if (studentList != null && studentList.size() > 0) {
                        for (TpExamStudent student : studentList) {
                            student.setExamId(newExamId);
                            student.setView(1);
                        }
                        classRoomDao.saveTpExamStudent(conn, studentList);
                    }
                    //新课堂附件
                    if (teaList != null && teaList.size() > 0) {
                        List<TpExamStudent> _studentList;
                        for (TpExamAttachment tea : teaList) {
                            //新课堂作业
                            if (tea.getAttachmentType() == 2) {
                                int oldAttachmentId = tea.getAttachmentId();
                                //生成新课堂作业
                                int teId = classRoomDao.saveTpExam(conn, classRoomDao.getClassRoomById(oldAttachmentId));
                                tea.setAttachmentId(teId);
                                tea.setClassesFullNames(StringUtils.join(classRoomDao.findClassesFullNameByClassRoomId(conn, oldAttachmentId), ","));
                                //新课堂作业学生
                                _studentList = classRoomDao.findTpExamStudent(oldAttachmentId);
                                if (_studentList != null && _studentList.size() > 0) {
                                    for (TpExamStudent student : _studentList) {
                                        student.setExamId(teId);
                                    }
                                    classRoomDao.saveTpExamStudent(conn, _studentList);
                                }
                            } else if (tea.getAttachmentType() == 1) {
                                ResourceInfo resourceInfo = resourceDao.getResourceById(tea.getAttachmentId());
                                tea.setTypeStr(resourceInfo != null ? DataDictionaryUtil.getInstance().getDictionaryName(6, resourceInfo.getType()) : "附件");
                            }
                            tea.setExamId(newExamId);
                            //生成新的附件
                            classRoomDao.saveClassRoomAttachment(conn, tea);
                        }
                    }
                    //删除旧数据
                    classRoomDao.deleteClassRoom(conn, classRoomId);
                    classRoomDao.deleteClassRoomAttachment(conn, classRoomId);
                    classRoomDao.deleteClassRoomStudent(conn, classRoomId);
                    conn.commit();
                    tpExam.setAttachments(teaList);
                } catch (Throwable e) {
                    e.printStackTrace();
                    ConnUtil.rollbackConnection(conn);
                } finally {
                    ConnUtil.closeConnection(conn);
                }
            }
        } else {
            if (tpExam != null) {
                if (teaList != null && teaList.size() > 0) {
                    for (TpExamAttachment te : teaList) {
                        if (te.getAttachmentType() == 2) {
                            te.setClassesFullNames(StringUtils.join(classRoomDao.findClassesFullNameByClassRoomId(te.getAttachmentId()), ","));
                        } else if (te.getAttachmentType() == 1) {
                            ResourceInfo resourceInfo = resourceDao.getResourceById(te.getAttachmentId());
                            te.setTypeStr(resourceInfo != null ? DataDictionaryUtil.getInstance().getDictionaryName(6, resourceInfo.getType()) : "附件");
                        }
                    }
                }
                tpExam.setAttachments(teaList);
            }
        }
        //班级
        List<String> cList = classRoomDao.findClassesIdByClassRoomId(classRoomId);
        tpExam.setClassesIds(StringUtils.join(cList, ","));
        return tpExam;
    }

    @Override
    public void update(TpExam te, int teacherId, int type) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            te.setPaperId(0);
            if (type == 1) {//发布
                te.setStatus(GlobalConstant.TP_EXAM_STATUS_RELEASE);
            } else {
                te.setStatus(GlobalConstant.TP_EXAM_STATUS_NEW);
            }
            int classRoomId = te.getId();
            //删除旧数据
            classRoomDao.deleteClassRoomAttachment(conn, classRoomId);
            classRoomDao.deleteClassRoomStudent(conn, classRoomId);

            TpExam oldTe = classRoomDao.getClassRoomById(classRoomId);
            if (oldTe.getStatus() == GlobalConstant.TP_EXAM_STATUS_RECALL &&
                    te.getStatus() != GlobalConstant.TP_EXAM_STATUS_RELEASE) {
//                te.setStatus(GlobalConstant.TP_EXAM_STATUS_RECALL);//测试要求撤回后在编辑的是新课堂
                te.setStatus(GlobalConstant.TP_EXAM_STATUS_NEW);
            }

            //保存课堂
            te.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (StringUtils.isNotEmpty(te.getStartTimeStr())) {
                te.setStartTime(format.parse(te.getStartTimeStr()));
            }
            if (StringUtils.isNotEmpty(te.getEndTimeStr())) {
                te.setEndTime(format.parse(te.getEndTimeStr()));
            }

            te.setTeacherId(teacherId);
            if (te.getTagId() != null) {
                te.setTagFullPath(KlbTagsUtil.getInstance().getTagFullId(te.getTagId()));
                te.setTagStr(KlbTagsUtil.getInstance().getCacheTagFullPathName(te.getTagFullPath(), 1));
            }
            te.setUpdateTime(new Date());
            te.setCreateTime(oldTe.getCreateTime());
            classRoomDao.updateTpExam(conn, te);

            setClassRoomProperty(te, teacherId, conn, classRoomId,type);

            conn.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }

    }

    @Override
    public List<Classes> findClassesByClassRoomId(int classRoomId) {
        return classRoomDao.findClassesByClassRoomId(classRoomId);
    }

    @Override
    public List<TpExamAttachment> findAttachmentByClassRoomId(int classRoomId) {
        return classRoomDao.findAttachmentByClassRoomId(classRoomId);
    }

    @Override
    public TpExamAttachment getTpExamAttachmentById(int id) {
        return classRoomDao.getTpExamAttachmentById(id);
    }

    @Override
    public TpExamAttachmentComment saveComment(TpExamAttachmentComment comment, UserEntity user) {

        try {
            comment.setCreateTime(new Date());
            comment.setUserId(user.getUserId());
            comment.setUserName(user.getRealName());
            comment.setUpdateTime(new Date());
            comment.setUserType(user.getType());
            classRoomDao.saveTpExamAttachmentComment(comment);
            return comment;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TpExamAttachmentComment> findCommentByExamAttachmentId(int examAttachmentId) {
        List<TpExamAttachmentComment> lists = classRoomDao.findCommentByExamAttachmentId(examAttachmentId);
        List<TpExamAttachmentComment> cLists;
        if (lists != null && lists.size() > 0) {
            for (TpExamAttachmentComment c : lists) {
                cLists = classRoomDao.findCommentByParentId(c.getId());
                c.setLists(cLists);
            }
        }
        return lists;
    }

    @Override
    public Classes getClassesById(int classesId) {
        return classRoomDao.getClassesById(classesId);
    }


    @Override
    public List<User> findStudentNameByClassRoomId(int classRoomId, int classesId) {
        return classRoomDao.findStudentNameByClassRoomId(classRoomId, classesId);
    }

    @Override
    public List<User> findFinishAttachment(int tpExamAttachmentId, int classesId) {
        return classRoomDao.findFinishAttachment(tpExamAttachmentId, classesId);
    }

    @Override
    public List<User> findFinishExamResult(int attachmentId, List<Integer> classesId) {
        return classRoomDao.findFinishExamResult(attachmentId, classesId);
    }

    @Override
    public ListPage findStudentClassRoom(int studentId, int subjectId, int pageNo, String searchValue, String endTimeStr) {
        ListPage listPage = new ListPage();
        ListPager listPager = new ListPager();
        listPager.setPageNo(pageNo);
        listPager.setPageSize(10);
        if (searchValue != null && !"".equals(searchValue.trim())) {
            searchValue = "%" + searchValue + "%";
        }
        if (endTimeStr != null && !"".equals(endTimeStr.trim())) {
            endTimeStr += ":59";
        }
        int count = classRoomDao.findStudentClassRoomCount(studentId, subjectId, searchValue, endTimeStr);
        listPager.setTotalRows(count);
        List<TpExam> lists = classRoomDao.findStudentClassRoom(listPager, studentId, subjectId, searchValue, endTimeStr);
        listPage.setResultList(lists);
        listPage.setListPager(listPager);
        return listPage;
    }

    @Override
    public List<String> findAttachmentIdsByClassRoomId(int classRoomId) {
        return classRoomDao.findAttachmentIdsByClassRoomId(classRoomId);
    }

    @Override
    public boolean findSubjectView(int studentId, int subjectId) {
        int count = classRoomDao.findSubjectView(studentId, subjectId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteSubjectView(int studentId, int subjectId) {
        classRoomDao.deleteSubjectView(studentId, subjectId);
    }

    @Override
    public boolean isExistExamByStudent(int examId, int studentId) {
        int i = classRoomDao.isExistExamByStudent(examId, studentId);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int saveNote(TpExamStudentNote t) {
        return classRoomDao.saveNote(t);
    }

    @Override
    public List<TpExamStudentNote> findNote(int studentId, int tpExamAttachmentId) {
        return classRoomDao.findNote(studentId, tpExamAttachmentId);
    }

    @Override
    public TpExam getClassRoomById(int tpExamId) {
        return classRoomDao.getClassRoomById(tpExamId);
    }

    @Override
    public boolean getComplete(int studentId, int tpExamAttachmentId) {
        int count = classRoomDao.getComplete(studentId, tpExamAttachmentId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveComplete(int studentId, int tpExamAttachmentId) {
        TpExamAttachmentComplete t = new TpExamAttachmentComplete();
        t.setStudentId(studentId);
        t.setExamAttachmentId(tpExamAttachmentId);
        t.setCreateTime(new Date());
        classRoomDao.saveComplete(t);
    }

    @Override
    public boolean getExamComplete(int studentId, int tpExamId) {
        int count = classRoomDao.getExamComplete(studentId, tpExamId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean getClassRoom(int tpExamId, int studentId) {
        int count = classRoomDao.getClassRoom(tpExamId, studentId);
        if (count > 0) {
            return true;
        }
        return false;
    }


    @Override
    public List<Classes> findAllClassByRangeSub(int rangeId, int subjectId, int teaherId) {
        String rangeName = KlbTagsUtil.getInstance().getTagName(rangeId);
        return teacherAddClassDao.findAllClassByRangeSub(rangeName, subjectId, teaherId);
    }

    @Override
    public boolean isExistClassesStudentByClassesId(int classesId) {
        int count = classRoomDao.isExistClassesStudentByClassesId(classesId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updateFinishTime(int studentId, int tpExamId) {
        classRoomDao.updateFinishTime(studentId, tpExamId);
    }

    @Override
    public TpExamStudent getTpExamStudent(int studentId, int tpExamId) {
        return classRoomDao.getTpExamStudent(studentId, tpExamId);
    }

    @Override
    public TaskDto getStudentTpExamResult(int tpExamId, int studentId) {
        return classRoomDao.getStudentTpExamResult(tpExamId, studentId);
    }

    @Override
    public List<Classes> findClassesByParentId(int subjectId, int classesId, int teacherId) {
        return classRoomDao.findClassesByParentId(subjectId,classesId, teacherId);
    }

    @Override
    public List<User> findUnFinishExamResult(int examId, List<Integer> classesIds) {
        return classRoomDao.findUnFinishExamResult(examId, classesIds);
    }

    @Override
    public boolean getFinishAttachment(int studentId, int examAttachmentId) {
        int count = classRoomDao.getComplete(studentId, examAttachmentId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAllStudentNameByClassRoomId(int classRoomId) {
        return classRoomDao.findAllStudentNameByClassRoomId(classRoomId);
    }

    @Override
    public List<Integer> findClassesChild(int id) {
        return classRoomDao.findClassesChild(id);
    }

    @Override
    public boolean isExistExamByClassesId(int attachmentId, int classesId) {
        int i = classRoomDao.isExistExamByClassesId(attachmentId, classesId);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistExamByClassesParentId(int attachmentId, int classesId) {
        int i = classRoomDao.isExistExamByClassesParentId(attachmentId, classesId);
        if (i > 0) {
            return true;
        }
        return false;
    }


    /**
     * 保存课堂附件.班级.学生
     *
     * @param te
     * @param teacherId
     * @param conn
     * @param classRoomId
     */
    private void setClassRoomProperty(TpExam te, int teacherId, Connection conn, int classRoomId,int submitType) {


        //保存附件
        List<TpExamAttachment> lists = te.getAttachments();
        if (lists != null && lists.size() > 0) {
            for (TpExamAttachment ta : lists) {
                if (ta.getAttachmentId() != null) {
                    if (ta.getAttachmentType() == 2) {
                        //处理课堂作业
                        setTpExamStudent(te, teacherId, conn, ta);
                        if(submitType == 1){
                            //课堂作业发布状态
                            classRoomDao.updateTpExamStatus(conn,ta.getAttachmentId(),GlobalConstant.TP_EXAM_STATUS_RELEASE);
                        }
                    }
                    ta.setExamId(classRoomId);
                    ta.setStatus(GlobalConstant.STATUS_ON);
                    classRoomDao.saveClassRoomAttachment(conn, ta);
                }
            }
        }
        //保存班级
        String[] classesIds = null;
        if (te.getClassesIds() != null && !"".equals(te.getClassesIds())) {
            classesIds = te.getClassesIds().split(",");
        }
        TpExamStudent ts;
        if (classesIds != null) {
            for (String classesId : classesIds) {
                //保存学生
                List<Integer> studentIdList = classRoomDao.findStudentByClassesId(Integer.valueOf(classesId));
                if (studentIdList != null && studentIdList.size() > 0) {
                    for (Integer studentId : studentIdList) {
                        ts = new TpExamStudent();
                        ts.setTeacherId(teacherId);
                        ts.setExamId(classRoomId);
                        ts.setCreateTime(new Date());
                        ts.setStatus(GlobalConstant.STATUS_ON);
                        ts.setClassesId(Integer.valueOf(classesId));
                        ts.setStudentId(studentId);
                        ts.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM);
                        ts.setSubjectId(te.getSubjectId());
                        ts.setView(1);
                        classRoomDao.saveTpExamStudent(conn, ts);
                    }
                }
            }
        }
    }

    /**
     * 重新绑定课堂作业学生
     *
     * @param te
     * @param teacherId
     * @param conn
     * @param ta
     */
    private void setTpExamStudent(TpExam te, int teacherId, Connection conn, TpExamAttachment ta) {
        TpExam tp = classRoomDao.getClassRoomById(ta.getAttachmentId());
        //重新查询班级学生
        List<Classes> classesList = classRoomDao.findClassesByClassRoomId(ta.getAttachmentId());
        classRoomDao.deleteClassRoomStudent(conn, tp.getId());
        List<Integer> studentIdList;
        TpExamStudent ts;
        if (null != classesList && classesList.size() > 0) {
            for (Classes classes : classesList) {
                studentIdList = classRoomDao.findStudentByClassesId(classes.getId());
                if (studentIdList != null && studentIdList.size() > 0) {
                    for (Integer studentId : studentIdList) {
                        ts = new TpExamStudent();
                        ts.setTeacherId(teacherId);
                        ts.setPaperId(tp.getPaperId());
                        ts.setExamId(tp.getId());
                        ts.setCreateTime(new Date());
                        ts.setStatus(GlobalConstant.STATUS_ON);
                        ts.setClassesId(Integer.valueOf(classes.getId()));
                        ts.setStudentId(studentId);
                        ts.setType(GlobalConstant.TP_EXAM_TYPE_CLASS_ROOM_WORk);
                        classRoomDao.saveTpExamStudent(conn, ts);
                    }
                }
            }
        }
        tp.setStartTime(te.getStartTime());
        tp.setEndTime(te.getEndTime());
        classRoomDao.updateTpExam(conn, tp);
    }


    @Override
    public void deleteClassRoom(int classRoomId) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            classRoomDao.deleteClassRoom(conn, classRoomId);
            conn.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }
    }

    @Override
    public List<Integer> queryTeachersByClassIdsAndSubjectId(List<Integer> classIds, Integer subjectId) {

        List<Integer> teacherIds = classRoomDao.queryTeacherIdsByClassIdAndSubjectId(classIds, subjectId);

        return teacherIds;
    }

    @Override
    public List<Classes> queryClassesByStudentId(Integer studentId) {

        List<Classes> classesList = classRoomDao.queryStudentClassInfo(studentId);

        return classesList;
    }
}
