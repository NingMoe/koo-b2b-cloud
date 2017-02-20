package com.koolearn.cloud.student;

import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.ClassesDynamic;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.cloud.util.SelectDTO;

import java.util.List;

/**
 * Created by fn on 2016/5/19.
 */
public interface StudentClassHomeService {
    /**
     * 查询当前学生加入的所有班级
     * @param studentId
     * @return
     */
    List<Classes> findAllClassesByStudentId(int studentId);

    /**
     * 查询班级列表
     * @param classesId
     * @return
     */
    public List<User> findClassStudentByClassesId( int classesId );

    /**
     * 学生加入班级
     * @param classesId
     * @param studentId
     * @return
     */
    int addStudentToClasses(Integer classesId , Integer studentId ,String userName );

    /**
     * 验证学生是否加入班级
     * @param classesCode :班级编码
     * @param studentId
     * @return
     */
    int checkStudentClasses( String classesCode , Integer studentId  );

    /**
     * 查询班级下是否有该学生
     * @param classesId
     * @param studentId
     * @return
     */
    int findStudentByClassIdAndStuId(Integer classesId, int studentId);

    /**
     * 退出班级
     * @param classesId
     * @param studentId
     * @return
     */
    boolean getOutClasses( Integer classesId, int studentId ,String realName );

    /**
     * 查询所有所在班级id
     * @param id
     * @return
     */
    List<Integer> findAllClassesIdByStudentId(Integer id);
    /**
     * 查询学生班级动态
     * @param classesId
     * @return
     */
    Pager findClassesDynamic( Integer classesId ,Integer studentId ,int pageNo );
    List<TreeBean>  findSubjectOrRangeOfStudent(Integer studentId,Integer subjectId );


}

