package com.koolearn.cloud.operation.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.ResponseAjaxData;
import com.koolearn.cloud.common.serializer.CommonInstence;
import com.koolearn.cloud.operation.entity.OperationUser;
import com.koolearn.cloud.operation.entity.SchoolFilter;
import com.koolearn.cloud.operation.service.OperationService;
import com.koolearn.cloud.teacher.entity.Location;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/operation/core")
public class OperationController extends BaseController {

    private final Log logger = LogFactory.getLog(OperationController.class);
    @Autowired
    private OperationService  operationService;

    public OperationService getOperationService() {
        return operationService;
    }

    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    /**
     *运营平台首页
     * @param modelMap
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/index")
    public String index(ModelMap modelMap, HttpServletRequest request,OperationUser user){
        modelMap.addAttribute("user",user);
        return "/operation/ophome";
    }
    @RequestMapping("/schoolDictionary")
    public String schoolDictionary(ModelMap modelMap, HttpServletRequest request,OperationUser user){
        modelMap.addAttribute("user",user);
        return "/operation/opdict";
    }
    /**
     * 省市县借口  parentId=0时查询省
     * @param id
     * @return
     */
    @RequestMapping("/findLocationList")
    @ResponseBody
    public Map findLocationList(ModelMap modelMap,Integer id, HttpServletRequest request){
        List<Location> list= operationService.findLocationList(id);
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put("data" , list );
        map.put("message" , "成功");
        map.put("status", CommonInstence.CODE_0);
        return map;
    }

    /** 数据字典学校搜索 */
    @RequestMapping("/searchSchoolList")
    @ResponseBody
    public ResponseAjaxData searchSchoolList(ModelMap modelMap,SchoolFilter pager ,HttpServletRequest request){
            pager= operationService.searchSchoolList(pager);
        ResponseAjaxData  responseData=ResponseAjaxData.getResponseAjaxData(pager);
        return responseData;
    }

    /** 学校增加修改 */
    @RequestMapping("/addUpdateSchool")
    @ResponseBody
    public String addUpdateSchool(ModelMap modelMap,SchoolFilter pager ,HttpServletRequest request){
        boolean falg=operationService.addUpdateSchool(pager);
        return "{\"status\":\"0\"}";
    }
    /** 学校删除 */
    @RequestMapping("/deleteSchool")
    @ResponseBody
    public Map deleteSchool(ModelMap modelMap,SchoolFilter pager ,HttpServletRequest request){
        boolean falg=operationService.deleteSchool(pager);
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put("status", CommonInstence.CODE_0);
        return map;
    }
    /** 取消或屏蔽学校 */
    @RequestMapping("/blockSchool")
    @ResponseBody
    public Map blockSchool(ModelMap modelMap,SchoolFilter pager ,HttpServletRequest request){
        boolean falg=operationService.blockSchool(pager);
        Map<String,Object>  map=new HashMap<String, Object>();
        map.put("status", CommonInstence.CODE_0);
        return map;
    }
    /** 学校是否存在 */
    @RequestMapping("/isExist")
    @ResponseBody
    public String isExist(ModelMap modelMap,SchoolFilter pager ,HttpServletRequest request){
        return operationService.schoolIsExist(pager.getName());

    }


}
