package com.koolearn.cloud.school.student;

import com.koolearn.cloud.school.student.dao.SchoolStudentManageDao;
import com.koolearn.cloud.school.student.vo.StudentPageDto;
import com.koolearn.cloud.school.student.vo.StudentPageVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2016/11/10.
 */
@Service("schoolStudentManageService")
public class SchoolStudentManageServiceImpl implements SchoolStudentManageService {
    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private SchoolStudentManageDao schoolStudentManageDao;

    /**
     * 学生分页查询
     * @param studentPageVo
     * @return
     */
    @Override
    public Map<String, Object> findStudentPage(StudentPageVo studentPageVo) {
        Integer totalPage = schoolStudentManageDao.findStudentTotalPage( studentPageVo );
        Map< String , Object > map = new HashMap<String, Object>();
        studentPageVo.setTotalLine( totalPage );
        map.put( "totalPage" ,studentPageVo.getTotalPage() );
        map.put( "currentPage" ,studentPageVo.getCurrentPage() );
        if( totalPage > 0 ){
            List<StudentPageDto > studentPageDtoList = schoolStudentManageDao.findStudentPageList( studentPageVo );
            map.put( "dataList" ,studentPageDtoList );
        }
        return map;
    }
}
