package com.koolearn.cloud.school.schoolInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.schoolmanage.SchoolManageService;
import com.koolearn.cloud.school.schoolmanage.vo.SchoolPowerDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 学校信息管理类
 * Created by fn on 2016/12/21.
 */
@RequestMapping("/school/manage")
@Controller
public class SchoolManageController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private SchoolManageService schoolManageService;

    /**
     * 学校管理页面跳转
     * @param request
     * @param manager
     * @return
     * /school/manage/goSchoolManage
     */
    @RequestMapping(value = "/goSchoolManage"  , method = RequestMethod.GET )
    public String goSchoolManage(HttpServletRequest request ,  Manager manager){
        if( manager.getSchoolId() != null ){
            SchoolPowerDto schoolPowerDto = schoolManageService.findSchoolBaseInfo( manager.getSchoolId() );
            request.setAttribute( "schoolPowerDto" , schoolPowerDto );
        }
        return "/school/schoolinfo/schoolManage";
    }

    /**
     * 查询学校扩展信息用于编辑
     * @return
     * /school/manage/findSchoolExtendInfo
     */
    @ResponseBody
    @RequestMapping(value = "/findSchoolExtendInfo"  , method = RequestMethod.POST )
    public String findSchoolExtendInfo( @RequestParam Integer schoolId ){
        SchoolPowerDto schoolPowerDto = new SchoolPowerDto();
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != schoolId ){
            schoolPowerDto = schoolManageService.findSchoolExtend( schoolPowerDto , schoolId );
            schoolPowerDto = schoolManageService.findSchoolManagerBuSchoolId( schoolPowerDto , schoolId );
        }
        map.put( CommonInstence.DATA , schoolPowerDto );
        map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("查询学校扩展信息用于编辑结果:" + param);
        return param.toString();
    }
    /**
     * 更新学校扩展信息
     * @param manager
     * @param schoolPowerDto
     * @return
     * /school/manage/updateSchoolExtendInfo
     */
    @ResponseBody
    @RequestMapping(value = "/updateSchoolExtendInfo"  , method = RequestMethod.POST )
    public String updateSchoolExtendInfo(  Manager manager ,@ModelAttribute SchoolPowerDto schoolPowerDto ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != schoolPowerDto ){
            schoolPowerDto.setSchoolId( manager.getSchoolId() );
            schoolPowerDto.setUpdater( manager.getManagerName() );
            schoolManageService.updateSchoolExtendInfo( schoolPowerDto );
            map.put( CommonInstence.STATUS ,CommonInstence.CODE_0 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("更新学校扩展信息结果:" + param);
        return param.toString();
    }

    /**
     * 学校升级毕业测试
     * @return
     * /school/manage/scheduledUpSchool
     */
    @RequestMapping( value="/scheduledUpSchool" , method=RequestMethod.POST)
    public String scheduledUpSchool( ){
        schoolManageService.schoolClassesUpScheduled();
        return "";
    }

    /**
     * 财务确认后生成学校升级的默认升级数据（对外接口）
     * @return
     *  /school/manage/addSchoolUpForFinanceConfirm?schoolId=334487
     */
    @ResponseBody
    @RequestMapping( value = "/addSchoolUpForFinanceConfirm" ,method =RequestMethod.POST )
    public String addSchoolUpForFinanceConfirm( @RequestParam Integer schoolId ){
        if( null != schoolId ){
            schoolManageService.addSchoolUpForFinanceConfirm( schoolId );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( "" );
        log.info( param );
        return param.toString();
    }


}
