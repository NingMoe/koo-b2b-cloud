package com.koolearn.cloud.school.authority.controller;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.school.authorityschool.AuthoritySchoolService;
import com.koolearn.cloud.school.authorityschool.vo.SchoolManagerDTO;
import com.koolearn.cloud.school.entity.Manager;
import com.koolearn.cloud.school.entity.SchoolInfo;
import com.koolearn.cloud.school.entity.SchoolPage;
import com.koolearn.cloud.school.util.CheckUtil;
import com.koolearn.cloud.teacher.listenCourse.player.MD5;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 学校端权限控制
 * Created by fn on 2016/11/15.
 */
@RequestMapping("/school/authority")
@Controller
public class AuthoritySchoolController extends BaseController {

    Logger log = Logger.getLogger( this.getClass() );
    @Autowired
    private AuthoritySchoolService authoritySchoolService;

    /**
     * 用户管理页面跳转
     * @param request
     * @param manager
     * @return
     * /school/authority/goAuthorityManage
     */
    @RequestMapping(value = "/goAuthorityManage"  , method = RequestMethod.GET )
    public String goAuthorityManage(HttpServletRequest request ,  Manager manager){
        //获取当前学校的所有学段和年级及学科
        if( null != manager && manager.getSchoolId() != null ){
            SchoolInfo schoolInfo = authoritySchoolService.findShoolInfo( manager.getSchoolId() );
            if( null != schoolInfo ){
                request.setAttribute( "levelMap" ,schoolInfo.getLevelMap() );
                request.setAttribute( "subjectMap" ,schoolInfo.getSubjectMap() );
            }
        }
        return "/school/authoritymanage/authorityList";
    }
    /**
     * 学校用户管理模块分页查询
     * @param manager
     * @return
     * /school/authority/findSchoolManagePage
     */
    @ResponseBody
    @RequestMapping(value = "/findSchoolManagePage"  , method = RequestMethod.POST )
    public String findSchoolMangePage( Manager manager ,@ModelAttribute SchoolPage schoolPage ){
        if( null != schoolPage ){
            if( null == schoolPage.getCurrentPage() ){
                schoolPage.setCurrentPage( 0 );
            }
            if( null == schoolPage.getPageSize() ){
                schoolPage.setPageSize( CommonInstence.PAGE_SIZE_20 );
            }
        }
        Map< String , Object > map = authoritySchoolService.findSchoolMangePage( schoolPage );
        Map< String , Object > resultMap = new HashMap<String, Object>();
        resultMap.put( "datas" ,map );
        resultMap.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        JSONObject param = (JSONObject) JSONObject.toJSON( resultMap );
        log.info("学校用户管理分页列表结果:"+ param );
        return param.toString();

    }

    /**
     * 冻结解冻学校管理用户
     * @param manager
     * @return
     * /school/authority/updateSchoolManageStatus
     */
    @ResponseBody
    @RequestMapping(value = "/updateSchoolManageStatus"  , method = RequestMethod.POST )
    public String updateSchoolManageStatus( Manager manager ,@RequestParam Integer managerId ,@RequestParam Integer type ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != managerId && null != type ){
            try{
                authoritySchoolService.updateSchoolManageStatus( managerId , type );
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_0 );
            }catch( Exception e ){
                log.error( "冻结解冻学校管理用户异常" + e.getMessage() ,e );
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("冻结解冻学校管理用户结果:"+ param );
        return param.toString();
    }

    /**
     * 重置管理员密码
     * @return
     *  /school/authority/resetManagePassword
     */
    @ResponseBody
    @RequestMapping(value = "/resetManagePassword"  , method = RequestMethod.POST )
    public String resetManagePassword( @RequestParam Integer managerId ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != managerId ){
            String md5Word = MD5.calcMD5( CommonInstence.RESET_PASSWORD );
            authoritySchoolService.resetManagePassword( managerId ,md5Word );
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("重置管理员密码结果:"+ param );
        return param.toString();
    }

    /**
     * 创建学校管理者
     * @param manager
     * @param schoolManagerDTO
     * @return
     * /school/authority/addSchoolManager?managerMobile=13122232321&managerEmail=jkdsj@126.com&managerName=管理员1&roleTypeId=30&classesLevelStr=11,12,13&subjectIdStr=2614,2615
     */
    @ResponseBody
    @RequestMapping(value = "/addSchoolManager"  , method = RequestMethod.POST )
    public String addSchoolManager( Manager manager ,@ModelAttribute SchoolManagerDTO schoolManagerDTO){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != schoolManagerDTO ){

            if( StringUtils.isNotEmpty( schoolManagerDTO.getManagerMobile() ) && !CheckUtil.isMobileNO( schoolManagerDTO.getManagerMobile())){
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
                map.put( CommonInstence.MESSAGE_FAIL , "手机格式有误" );
            }else if( !CheckUtil.isMail(schoolManagerDTO.getManagerEmail())){
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
                map.put( CommonInstence.MESSAGE_FAIL , "邮箱格式有误" );
            }else if( schoolManagerDTO.getRoleTypeId() == 40 && StringUtils.isEmpty(schoolManagerDTO.getSubjectIdStr())){
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
                map.put( CommonInstence.MESSAGE_FAIL , "角色对应的学科设置为空" );
            }else if( schoolManagerDTO.getRoleTypeId() == 50 && StringUtils.isEmpty(schoolManagerDTO.getClassesLevelStr())){
                map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
                map.put( CommonInstence.MESSAGE_FAIL , "角色对应的年级设置为空" );
            }else {
                schoolManagerDTO.setSchoolId( manager.getSchoolId() );
                schoolManagerDTO.setCreator( manager.getManagerName() );
                Manager managerNew = makeManager( schoolManagerDTO );
                int num = authoritySchoolService.insertSchoolManager( managerNew );
                if( num == 0 ){
                    map.put( CommonInstence.STATUS , CommonInstence.STATUS_1 );
                    map.put( CommonInstence.MESSAGE_FAIL , "创建失败" );
                }else{
                    map.put( CommonInstence.STATUS , CommonInstence.STATUS_0 );
                }
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("创建学校管理者结果:"+ param );
        return param.toString();
    }

    /**
     * 拼装管理员对象
     * @param schoolManagerDTO
     * @return
     */
    private Manager makeManager( SchoolManagerDTO schoolManagerDTO ){
        Manager manager = new Manager();
        manager.setManagerName( schoolManagerDTO.getManagerName() );
        manager.setManagerEmail( schoolManagerDTO.getManagerEmail() );
        manager.setManagerMobile( schoolManagerDTO.getManagerMobile() );
        manager.setCreateTime( new Date( ));
        manager.setStatus( CommonInstence.STATUS_1 );
        manager.setPassWord( MD5.calcMD5( CommonInstence.RESET_PASSWORD ) );
        manager.setSchoolId( schoolManagerDTO.getSchoolId() );
        manager.setRoleTypeId(schoolManagerDTO.getRoleTypeId());
        manager.setCreator( schoolManagerDTO.getCreator() );
        if(StringUtils.isNoneEmpty( schoolManagerDTO.getClassesLevelStr() )){
            manager.setClassesLevels( schoolManagerDTO.getClassesLevelStr() );
        }
        if( StringUtils.isNotEmpty( schoolManagerDTO.getSubjectIdStr() )){
            manager.setSubjectIds( schoolManagerDTO.getSubjectIdStr() );
        }
        return manager;
    }

    /**
     * 验证管理者邮箱
     * @param manager
     * @return
     * /school/authority/isExistManagerEmail
     */
    @ResponseBody
    @RequestMapping(value = "/isExistManagerEmail"  , method = RequestMethod.POST )
    public String isExistManagerEmail( Manager manager ,@RequestParam String email ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( StringUtils.isNotEmpty( email ) ){
            boolean result = authoritySchoolService.checkManagerEmail( email );
            if( result ){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );//不可用
            }else{
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );//可用
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("验证管理员邮箱是否存在结果:"+ param );
        return param.toString();
    }

    /**
     * 验证管理者手机是否可用
     * @param manager
     * @return
     * /school/authority/isExistManagerMobile
     */
    @ResponseBody
    @RequestMapping(value = "/isExistManagerMobile"  , method = RequestMethod.POST )
    public String isExistManagerMobile( Manager manager ,@RequestParam String mobile ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( StringUtils.isNotEmpty( mobile ) ){
            boolean result = authoritySchoolService.checkManagerMobile(mobile);
            if( result ){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );//不可用
            }else{
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );//可用
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("验证管理者手机是否存在结果:"+ param );
        return param.toString();
    }

    /**
     * 查询管理者信息用于更新
     * @param manager
     * @param managerId
     * @return
     * /school/authority/findManagerForUpdate
     */
    @ResponseBody
    @RequestMapping(value = "/findManagerForUpdate"  , method = RequestMethod.POST )
    public String findManagerForUpdate( Manager manager ,@RequestParam Integer managerId ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != managerId ){
            SchoolManagerDTO schoolManagerDTO = authoritySchoolService.findManagerForUpdate( managerId );
            map.put( CommonInstence.DATA , schoolManagerDTO );
            map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("查询管理者信息用于更新结果:"+ param );
        return param.toString();
    }

    /**
     * 更新学校管理者信息
     * @param manager
     * @return
     * /school/authority/updateManagerInfo?managerId=10&managerMobile=13111111111&managerEmail=jkdsj@126.com&managerName=管理员1&roleTypeId=30&classesLevelStr=11,12,13&subjectIdStr=2614,2615
     */
    @ResponseBody
    @RequestMapping(value = "/updateManagerInfo"  , method = RequestMethod.POST )
    public String updateManagerInfo( Manager manager ,@ModelAttribute SchoolManagerDTO schoolManagerDTO ){
        Map< String , Object > map = new HashMap<String, Object>();
        if( null != schoolManagerDTO ){
            schoolManagerDTO.setUpdater( manager.getManagerName() );
            int num = authoritySchoolService.updateManagerInfo( schoolManagerDTO );
            if( num == 1 ){
                map.put( CommonInstence.STATUS , CommonInstence.CODE_0 );
            }else{
                map.put( CommonInstence.STATUS , CommonInstence.CODE_1 );
            }
        }
        JSONObject param = (JSONObject) JSONObject.toJSON( map );
        log.info("查询管理者信息用于更新结果:"+ param );
        return param.toString();
    }













}
