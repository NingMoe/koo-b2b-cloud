package com.koolearn.cloud.composition.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.common.entity.*;
import com.koolearn.cloud.composition.CommonHessianServiceImpl;
import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.composition.dto.RuleDto;
import com.koolearn.cloud.composition.entity.*;
import com.koolearn.cloud.composition.enums.CompositionImageType;
import com.koolearn.cloud.composition.enums.CompositionOrderStatus;
import com.koolearn.cloud.composition.enums.RuleItemType;
import com.koolearn.cloud.composition.service.CompositionService;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.util.ConfigUtil;
import com.koolearn.cloud.util.FileUtil;
import com.koolearn.cloud.util.PTItemComparator;
import com.koolearn.cloud.util.PayConstants;
import com.koolearn.common.util.StringUtil;
import com.koolearn.dubbo.share.service.order.OrderDubboService;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.framework.redis.client.KooJedisClient;
import com.koolearn.pay.api.redirect.dto.KooPayResponseDto;
import com.koolearn.pay.api.redirect.util.KooPayResponseUtil;
import com.koolearn.tfs.client.Tfstool;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作文批改教师端controller
 * Created by haozipu on 2016/7/20.
 */

@Controller
@RequestMapping("composition")
public class CompositionController extends BaseController {

    private static final Logger logger = Logger.getLogger(CompositionController.class);

    public static final String FILE_PATH = "/compositionCorrectImg";

    private PTItemComparator comparator = new PTItemComparator();

    @Autowired
    CompositionService compositionService;

    @Autowired
    LoginService loginService;

    @Autowired
    KooJedisClient redisClient;

//    @Autowired
    OrderDubboService orderDubboService;

    @RequestMapping("index")
    public String index(UserEntity userEntity,Model model){


        Integer count = compositionService.queryTeacherNewCompositionCount(userEntity.getUserId());
        model.addAttribute("count",count);
        return "composition/index";
    }

    /**
     * 返回动态生成的批改报告 json格式 由前端来处理显示
     * @param jsonData 前端传递的json格式字符串
     *
     * @return
     */
    @RequestMapping("makeCompositionReport")
    @ResponseBody
    public Object queryCompositionReport(@RequestParam("jsonData") String jsonData) {

        JSONObject jsonObject = JSONObject.parseObject(jsonData);

        Integer ruleId =jsonObject.getInteger("ruleId");
        Integer score = jsonObject.getInteger("score");
        JSONArray ptitems = jsonObject.getJSONArray("items");
        JSONArray jitems = jsonObject.getJSONArray("jitems");
        JSONArray aitems = jsonObject.getJSONArray("aitems");

        List<Integer> ids = new ArrayList<Integer>();

        for (int i = 0; i < ptitems.size(); i++) {
            JSONObject temp = ptitems.getJSONObject(i);
            ids.add(temp.getInteger("itemId"));
        }

        for (int i = 0; i < aitems.size(); i++) {
            JSONObject temp = aitems.getJSONObject(i);
            ids.add(temp.getInteger("itemId"));
        }

        Map<Integer, Integer> keyIdvalueScoreMap = new HashMap<Integer, Integer>();

        for (int i = 0; i < jitems.size(); i++) {
            JSONObject temp = jitems.getJSONObject(i);
            keyIdvalueScoreMap.put(temp.getInteger("itemId"), temp.getInteger("score"));
            ids.add(temp.getInteger("itemId"));
        }

        CompositionReprot compositionReprot = compositionService.generateCompositionReportByItems(ids, keyIdvalueScoreMap, ruleId,score);

        JSONObject jsonObject1 =new JSONObject();

        JSONObject reprotContentObj = JSONObject.parseObject(compositionReprot.getReport());
        //解析json格式报告 生成正常形式的文本
        StringBuffer sb = generatePrettyReprot(reprotContentObj);

        jsonObject1.put("reportContent",sb.toString());
        logger.info(sb.toString());

        return jsonObject1;


    }


    /**
     * 最后保存老师的批阅记录
     * @return
     */
    @RequestMapping(value="saveReport",method = RequestMethod.POST)
    public Object saveReport(HttpServletRequest request) {

        String jsonData =request.getParameter("jsonData");

        if(logger.isInfoEnabled()){
            //logger.info("保存报告 接收到的json "+jsonData);
        }


        JSONObject jsonObject = JSONObject.parseObject(jsonData);

        Integer score = jsonObject.getInteger("score");
        //解析订单和作文的ID
        Integer orderId = jsonObject.getInteger("orderId");
        Integer compositionId = jsonObject.getInteger("compositionId");
        Integer ruleId = jsonObject.getInteger("ruleId");

        if(logger.isInfoEnabled()){
            logger.info("规则ID "+ruleId);
            logger.info("作文ID "+compositionId);
            logger.info("订单ID "+orderId);
        }

        List<RuleItem> items = compositionService.queryAllItemByRuleId(ruleId);
        Map<Integer, RuleItem> itemMap = new HashMap<Integer, RuleItem>();

        for (RuleItem ite : items) {
            itemMap.put(ite.getId(), ite);
        }

        //解析普通得分项
        parsePTItemsJsonObject(jsonObject, orderId, compositionId, itemMap,ruleId);

        //解析加分项
        parseJItemsJsonObject(jsonObject, orderId, compositionId, itemMap,ruleId);

        //解析加分项
        parseAitemsJsonObject(jsonObject, orderId, compositionId, itemMap,ruleId);


        //获取教师修改后的报告
        String reportContent = jsonObject.getString("reportContent");
        paserReportJsonObect(score, orderId, compositionId,reportContent,ruleId);

        //解析处理批改后的图片和批注等
        JSONArray imageArr = jsonObject.getJSONArray("images");
        parseImageJsonObject(orderId, compositionId, imageArr);

        compositionService.updateCompositionOrderStatus(orderId,CompositionOrderStatus.CORRECTED.getValue());

//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("error", "0");
        return "redirect:/composition/compositionDetail?cid="+compositionId+"&oid="+orderId;
    }

    /**
     * 分页返回老师的批改记录
     * 教师批改收益分页列表
     * @return
     */
    @RequestMapping("correctList")
    public String queryCorrectRecordList(UserEntity userEntity, Model model, HttpServletRequest request) {

        //添加查询
        String pageNo = request.getParameter("pageNo");
        ListPager listPager = new ListPager();
        listPager.setPageNo(pageNo == null ? 0 : Integer.parseInt(pageNo));
        listPager.setPageSize(20);

        BigDecimal count = compositionService.queryPayCountByTeacherId(userEntity.getUserId());
        DecimalFormat myformat = new DecimalFormat("#########0.00");
        if(count==null){
            count = new BigDecimal(0);
        }
        String strAmount = String.valueOf(myformat.format(count));

        List<CompositionOrderDto> dtos = compositionService.queryCompositionOrderListByConditions(listPager, null, CompositionOrderStatus.CORRECTED.getValue(), userEntity.getUserId(), null, null);
        Integer countSize = compositionService.queryCompositionOrderListByConditionsCount(null, CompositionOrderStatus.CORRECTED.getValue(), userEntity.getUserId(), null, null);
        listPager.setTotalRows(countSize);
        model.addAttribute("correctedOrders", dtos);
        model.addAttribute("payCount", strAmount);
        model.addAttribute("listPager", listPager);

        return "composition/my_correct_list";
    }

    /**
     * 返回教师对应的作文批改规则，如果没有设置则返回可选规则的集合
     * @param compositionId 适配规则的作文ID
     * @param action 动作标识符
     * @return
     */
    @ResponseBody
    @RequestMapping("queryCorrectRuleList")
    public Object queryCorrectRuleList(@RequestParam("compositionId") Integer compositionId,@RequestParam(value = "a",required = false)String action,UserEntity userEntity) {

        Composition composition = compositionService.queryCompositionById(compositionId);

        DefaultRule defaultRule = compositionService.queryDefaultRuleByCondition(userEntity.getUserId(), composition.getType(), composition.getArea(), composition.getSchoolLev());

        if(logger.isInfoEnabled()){
            logger.info("查询出来的defaultrule "+JSONObject.toJSONString(defaultRule));
        }

        JSONObject result = new JSONObject();

        if(action==null||"".equals(action)){
            if (defaultRule != null) {
                defaultRule.setId(defaultRule.getRuleId());
                result.put("defaultRule", defaultRule);
                result.put("hasSet", true);
            } else {
                returnSelectRuleList(composition, result);

            }
        }else {
            returnSelectRuleList(composition, result);

        }

        return result;
    }


    /**
     * 报存教师的默认评分规则
     * @param compositionId 作文ID
     * @param ruleId 规则ID
     * @return
     */
    @ResponseBody
    @RequestMapping("saveCorrectRule")
    public Object saveDefaultCorrectRule(@RequestParam("compositionId") Integer compositionId, @RequestParam("ruleId") Integer ruleId, UserEntity userEntity) {

        DefaultRule rule = new DefaultRule();
        Composition composition = compositionService.queryCompositionById(compositionId);
        rule.setType(composition.getType());
        rule.setArea(composition.getArea());
        rule.setRuleId(ruleId);
        rule.setSchoolLev(composition.getSchoolLev());
        rule.setUserId(userEntity.getUserId());
        compositionService.insertDefaultRule(rule);
        rule.setId(ruleId);
        return rule;
    }

    /**
     * 根据教师指定的规则 返回规则下的评分项 用户渲染评分项表格
     * @param compositionId 作文ID
     * @param ruleId 规则ID
     * @return
     */
    @RequestMapping("queryScoreItems")
    @ResponseBody
    public Object queryRuleItemList(@RequestParam(value = "compositionId", required = false) Integer compositionId, @RequestParam("ruleId") Integer ruleId) {

        //返回规则集下全部的指标的得分项 不用包含评语

        RuleDto ruleDto = compositionService.queryRuleInfoByRuleId(ruleId);

        if(ruleDto.getNoScore()!=null&&ruleDto.getNoScore()==1){
            ruleId =ruleDto.getId();
        }

        List<RuleItem> items = compositionService.queryAllItemByRuleId(ruleId);

        JSONObject result = new JSONObject();

        JSONArray resultItems = new JSONArray();

        JSONArray jResultItems = new JSONArray();

        JSONArray aResultItems = new JSONArray();

        Map<String, List> itemsMap = new HashMap<String, List>();

        for (RuleItem item : items) {
            if (item.getItemType() == RuleItemType.NORMAL_ITEM.getValue()) {
                List<RuleItem> tempItems = itemsMap.get(item.getItemName());
                if (tempItems != null) {
                    tempItems.add(item);
                } else {
                    tempItems = new ArrayList<RuleItem>();
                    tempItems.add(item);
                    itemsMap.put(item.getItemName(), tempItems);
                }
            } else if (item.getItemType() == RuleItemType.SUBTRACT_ITEM.getValue()) {
                //减分项目
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId", item.getId());
                jsonObject.put("itemName", item.getDescrib());
                jsonObject.put("score", item.getScore());

                jResultItems.add(jsonObject);

            } else {
                //加分项目
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId", item.getId());
                jsonObject.put("itemName", item.getDescrib());
                jsonObject.put("score", item.getScore());
                aResultItems.add(jsonObject);
            }

        }

        //处理普通的项目的map
        Iterator<Map.Entry<String, List>> iterator = itemsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List> entry = iterator.next();
            String desc = entry.getKey();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item", desc);
            List<RuleItem> subItems = entry.getValue();
            logger.info("普通评分项是 "+JSONObject.toJSONString(subItems));
            //给普通的指标按照等级来排序
            Collections.sort(subItems,comparator);

            JSONArray jsonArray = new JSONArray();
            for (RuleItem subItem : subItems) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("itemId", subItem.getId());
                jsonObject1.put("itemName", subItem.getDescrib());
                jsonObject1.put("levDesc", subItem.getLevDesc());
                jsonObject1.put("score", subItem.getScore());
                jsonArray.add(jsonObject1);

            }
            jsonObject.put("subItems", jsonArray);

            resultItems.add(jsonObject);
        }

        //最后添加 普通项目

        result.put("items", resultItems);

        result.put("aitems", aResultItems);

        result.put("jitems", jResultItems);

        result.put("ruleName",ruleDto.getRuleName());
        result.put("ruleId",ruleId);
        result.put("maxScore",ruleDto.getScoreSum());

        return result;
    }

    /**
     * 查询教师批改 和 为批改的作文分页列表
     * @param keyWord
     * @return
     */
    @RequestMapping("compositionList")
    public String queryCompositionList(@RequestParam(value = "keyWord", required = false) String keyWord,

                                       UserEntity userEntity, Model model, HttpServletRequest request) throws Exception {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String statusStr = request.getParameter("status");

        Integer status = null;

        if (StringUtil.isNotEmpty(statusStr)) {
            status = Integer.parseInt(statusStr);
        }
        String pageNo = request.getParameter("pageNo");
        ListPager listPager = new ListPager();
        listPager.setPageNo(pageNo == null ? 0 : Integer.parseInt(pageNo));
        listPager.setPageSize(20);
        if(keyWord!=null){
            keyWord=keyWord.trim();
        }
        List<CompositionOrderDto> compositionOrderDtos = compositionService.queryCompositionOrderListByConditions(listPager, keyWord, status, userEntity.getUserId(), startDateStr, endDateStr);
        BigDecimal count = compositionService.queryPayCountByTeacherId(userEntity.getUserId());

        Integer countSize = compositionService.queryCompositionOrderListByConditionsCount(keyWord, status, userEntity.getUserId(), startDateStr, endDateStr);
        listPager.setTotalRows(countSize);

        DecimalFormat myformat = new DecimalFormat("#########0.00");
        if(count==null){
            count = new BigDecimal(0);
        }

        String strAmount = String.valueOf(myformat.format(count));

        List<CompositionTeacher> compositionTeachers = compositionService.queryXDFTeacherByStudentId(null);

        List<Integer> teacherIds =new ArrayList<Integer>();

        for (CompositionTeacher teacher:compositionTeachers){
            teacherIds.add(teacher.getId());
        }

        model.addAttribute("hideFlag",true);

        if(teacherIds.contains(userEntity.getUserId())){
            model.addAttribute("hideFlag",false);
        }

        model.addAttribute("payCount", strAmount);
        model.addAttribute("compositionOrders", compositionOrderDtos);
        model.addAttribute("listPager", listPager);
        StringBuilder sb = new StringBuilder("");
        if (StringUtil.isNotEmpty(keyWord)) {
            sb.append("&");
            sb.append("keyWord=").append(keyWord);
            model.addAttribute("keyWord",keyWord);
        }

        if (startDateStr != null&&!"".equals(startDateStr)) {
            sb.append("&");
            sb.append("startDate=").append(startDateStr);
            model.addAttribute("startDateStr",startDateStr);
        }
        if (endDateStr != null&&!"".equals(endDateStr)) {
            sb.append("&");
            sb.append("endDate=").append(endDateStr);
            model.addAttribute("endDateStr",endDateStr);
        }
        if (status != null) {
            sb.append("&");
            sb.append("status=").append(status);
            model.addAttribute("status",status);
        }

        model.addAttribute("condition", sb.toString());

        return "composition/composition_list";
    }

    /**
     * 批改作文 初始化批改作文的页面
     * @param compositionId 作文ID
     * @param orderId 订单ID
     * @return
     */
    @RequestMapping("correctComposition")
    public String correctComposition(@RequestParam("cid") Integer compositionId, @RequestParam("oid") Integer orderId, UserEntity userEntity, Model model) {

        CompositionOrder compositionOrder = compositionService.queryOrderInfoByCompositionId(compositionId);

        if(!compositionOrder.getTeacherId().equals(userEntity.getUserId())){
            throw new RuntimeException("只能批改属于自己的作文");
        }
        if(compositionOrder.getOrderStatus()==CompositionOrderStatus.CORRECTED.getValue()){
            throw new RuntimeException("改作文已经批改过，不能再次批改");
        }
        //查询作文的图片记录
        List<CompositionImage> compositionImages = compositionService.queryCompositionImages(compositionId);

        Composition composition = compositionService.queryCompositionById(compositionId);
        User user = loginService.findUserByUserId(composition.getUserId());
        UserMobi userMobi = loginService.findMobileUser(user.getUserName());

        List<String> classNameList = compositionService.queryClassNameListByTeacherIdAndStudentId(userEntity.getUserId(),userMobi.getId());
        userMobi.setClassesName(StringUtils.join(classNameList,","));

        CompositionOrderDto nextDto = compositionService.queryNextCompositionOrder(userEntity.getUserId(),orderId);
        CompositionOrderDto preDto = compositionService.queryPreCompositionOrder(userEntity.getUserId(),orderId);

        //增加变更作文查阅状态的操作
        compositionService.updateCompositionOrderViewFlag(orderId);

        model.addAttribute("nextComposition",nextDto);
        model.addAttribute("preComposition",preDto);
        model.addAttribute("userInfo", userMobi);
        model.addAttribute("order", compositionOrder);
        model.addAttribute("composition", composition);
        model.addAttribute("images", compositionImages);

        return "composition/composition_correct";

    }

    /**
     * 查看已经批阅过得作文 作文
     * @param orderId 订单ID
     * @param compositionId 作文ID
     * @return
     */
    @RequestMapping("compositionDetail")
    public String viewCompositionDetail(@RequestParam("cid") Integer compositionId, @RequestParam("oid") Integer orderId, UserEntity userEntity, Model model) {

        //查询作文的图片记录

        List<CompositionImage> compositionImages = compositionService.queryCompositionCorrectImages(orderId);

        Composition composition = compositionService.queryCompositionById(compositionId);

        CompositionReprot reprot = compositionService.queryCompositionReport(compositionId, orderId);

//        String content = reprot.getReport().replace("\n","<br>");
//        reprot.setReport(content);

        String[] reprotArr=new String[0];
        String reportContent =reprot.getReport();
        if(reportContent!=null||"".equals(reportContent)){
            reprotArr = reportContent.split("\n");
        }

        List<CompositionCorrectionRecord> compositionCorrectionRecords = compositionService.queryCompositionCorrectRecord(compositionId, orderId);

        User user = loginService.findUserByUserId(composition.getUserId());
        UserMobi userMobi = loginService.findMobileUser(user.getUserName());
        List<String> classNameList = compositionService.queryClassNameListByTeacherIdAndStudentId(userEntity.getUserId(),userMobi.getId());
        userMobi.setClassesName(StringUtils.join(classNameList,","));

        CompositionOrderDto nextDto = compositionService.queryNextCompositionOrder(userEntity.getUserId(),orderId);
        CompositionOrderDto preDto = compositionService.queryPreCompositionOrder(userEntity.getUserId(),orderId);

        model.addAttribute("nextComposition",nextDto);
        model.addAttribute("preComposition",preDto);
        model.addAttribute("userInfo", userMobi);
        model.addAttribute("correctRecords", compositionCorrectionRecords);
        model.addAttribute("report", reprot);
        model.addAttribute("reportItems",reprotArr);
        model.addAttribute("scoreFlag",isNumeric(reprot.getScore()));
        model.addAttribute("composition", composition);
        model.addAttribute("images", compositionImages);
        model.addAttribute("teacherName",userEntity.getRealName());
        return "composition/composition_detail";

    }

    /**
     * 支付中心回调url 用于订单状态的更新
     *
     * @return
     */
    @RequestMapping("payCenter/notify")
    @ResponseBody
    public String orderCenterCallBack(HttpServletRequest request) {

        //支付中心回调的参数情况
        KooPayResponseDto kooPayResponseDto = KooPayResponseUtil.createResponseDto(request);
        kooPayResponseDto.setSignKey(PayConstants.PAY_SIGN_KEY);
        logger.info("支付中心回调传递参数 "+JSONObject.toJSONString(kooPayResponseDto));
        // 判断交易是否成功
        boolean isSuccces = KooPayResponseUtil.isSuccess(kooPayResponseDto);

        boolean isSignMatch = KooPayResponseUtil.isSignMatching(kooPayResponseDto); // 判断签名是否正确

        //如果都成功
        if (isSuccces && isSignMatch) {
            try {
                logger.info("支付中心调用和签名验证成功同步订单状态 ");
                compositionService.updateCompositionOrderStatus(Integer.parseInt(kooPayResponseDto.getOutOrderNo()), CompositionOrderStatus.PAYED.getValue());
                logger.info("支付中心调用和签名验证成功更新订单状态 订单ID："+kooPayResponseDto.getOutOrderNo());
                // 调用课堂开订单的接口
                try{
                    CompositionOrder compositionOrder = compositionService.queryOrderInfoByOrderId(Integer.valueOf(kooPayResponseDto.getOutOrderNo()));

                    if(compositionOrder!=null&&compositionOrder.getOrderCode()!=null){
                        //orderCode不为空调用课堂下单接口成功
                        openOrderInfoToClass(compositionOrder.getUserId()+"",compositionOrder.getOrderCode(),kooPayResponseDto.getAmount(),kooPayResponseDto.getOrderNo());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("调用课堂开订单的接口"+e.getMessage());
                }
                return "success";
            }catch (Exception e){
                e.printStackTrace();

                logger.error("回调更新订单状态失败"+e);

            }

        }

        return "fail";
    }


    /**
     * 调用课堂的开订单接口
     * @param userId
     * @param orderId
     * @param amount
     * @param serial_no
     */
    private void openOrderInfoToClass(String userId,String orderId,String amount,String serial_no){
        Map<String,String> requestMap = new HashMap<String, String>();
        requestMap.put("isActivate","true");
        requestMap.put("userId",userId);
        requestMap.put("orderId",orderId);
        requestMap.put("amount",amount);
        requestMap.put("serial_no",serial_no);

        logger.info("调用开单接口传递的map "+JSONObject.toJSONString(requestMap));

        Map<String,String> reponseMap = orderDubboService.appOpenOrder(requestMap);

        String code  = reponseMap.get("code");

        //调用课堂订单成功
        if(code!=null&&"0".equals(code)){
            //调用课堂订单成功
            logger.info("课堂开订单接口调用成功 "+JSONObject.toJSONString(reponseMap));

        }else {
            //调用失败
            logger.info("课堂开订单接口调用失败 "+JSONObject.toJSONString(reponseMap));
        }
    }

    /**
     * 保存文件到tfs系统
     * @param base64Str
     * @return
     */
    private String saveFileToTfs(String base64Str) {

        Tfstool tfstool = Tfstool.getInstance();
        if (!tfstool.isDirExists(CompositionController.FILE_PATH)) {
            try {
                tfstool.createDir(FILE_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            }

        }

        //校验文件类型
        String newFileName = FileUtil.generatorNewFileName("jpg");

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            // Base64解码
            byte[] bytes = new byte[0];
            String[] bArr = base64Str.split(",");
            bytes = decoder.decodeBuffer(bArr[1]);

            File imageFile = new File(CommonHessianServiceImpl.CLOUD_BASE_PATH+newFileName);

            OutputStream os = new BufferedOutputStream(new FileOutputStream(imageFile));

            os.write(bytes);
            os.flush();
            os.close();

            tfstool.pushFile(CommonHessianServiceImpl.CLOUD_BASE_PATH+newFileName,FILE_PATH + "/" + newFileName);
            return FILE_PATH + "/" + newFileName;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

        return null;

    }

    /**
     * 字符串数组 转换成
     *
     * @param itemsArr
     * @return
     */
    private List<Integer> convertStrArrToIntegerArr(String[] itemsArr) {
        List<Integer> integers = new ArrayList<Integer>();
        for (String str : itemsArr) {
            integers.add(Integer.valueOf(str));
        }
        return integers;
    }

    /**
     * 将程序生成的json串格式的报告内容生成一个一行一行的文本报告内容
     * @param reprotContentObj
     * @return
     */
    private StringBuffer generatePrettyReprot(JSONObject reprotContentObj) {
        JSONArray remarks =reprotContentObj.getJSONArray("remark");
        String defen = reprotContentObj.getString("得分");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < remarks.size(); i++) {
            JSONObject remark = remarks.getJSONObject(i);

            Set<String> keySet = remark.keySet();

            Iterator<String> iterator = keySet.iterator();

            while (iterator.hasNext()){
                String k =iterator.next();
                sb.append(k);
                sb.append(":");
                sb.append(remark.get(k).toString());
                sb.append("\n");
                logger.info(k + "    " + remark.get(k).toString());
            }

        }
        //sb.append("得分:");
        //sb.append(defen);
        return sb;
    }

    /**
     * 查询某个作文匹配的规则集合 返回给客户端
     * @param composition
     * @param result
     */
    private void returnSelectRuleList(Composition composition, JSONObject result) {
        //查询符合指定条件全部的规则集列表 供客户端选择
        List<Rule> rules = compositionService.queryRuleListByCondition(composition.getType(), composition.getArea(), composition.getSchoolLev());

        JSONArray jsonArray  = new JSONArray();

        for (Rule rule:rules){

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",rule.getId());
            jsonObject.put("noScore",rule.getNoScore());
            jsonObject.put("area",rule.getRuleArea());
            jsonObject.put("ruleId",rule.getRuleId());
            jsonObject.put("name",rule.getRuleName());
            jsonObject.put("type",rule.getRuleType());
            jsonObject.put("schoolLev",rule.getSchoolLev());
            jsonObject.put("scoreSum",rule.getScoreSum());
            jsonArray.add(jsonObject);
        }

        result.put("rules", jsonArray);
        result.put("hasSet", false);
    }

    /**
     * 解析前端发送过来的图片批阅记录和图片信息
     *
     * @param orderId
     * @param compositionId
     * @param imageArr
     */
    private void parseImageJsonObject(Integer orderId, Integer compositionId, JSONArray imageArr) {
        for (int i = 0; i < imageArr.size(); i++) {
            JSONObject imageJson = imageArr.getJSONObject(i);

            String base64Str = imageJson.getString("imageCode");

            //处理图片
            String filePath = saveFileToTfs(base64Str);

            CompositionImage correctImg = new CompositionImage();
            correctImg.setType(CompositionImageType.CORRECT_IMG.getValue());
            correctImg.setImgIndex(i + 1);
            correctImg.setOrderId(orderId);
            correctImg.setImgUrl(ConfigUtil.tfsDomain+filePath);
            correctImg.setCompositionId(compositionId);
            correctImg.setStatus(0);

            Integer imageId = compositionService.insertCompositionImage(correctImg);

            JSONArray remarkArr = imageJson.getJSONArray("correctText");

            logger.info("第 "+(i+1)+"张图片的 批注数组 "+JSONObject.toJSONString(remarkArr));

            for (int j = 0; j < remarkArr.size(); j++) {
                String remark = remarkArr.getString(j);
                CompositionCorrectionRecord record = new CompositionCorrectionRecord();
                record.setOrderId(orderId);
                record.setRemark(remark);
                record.setCreateTime(new Date());
                record.setCompositionId(compositionId);
                record.setPicIndex("");
                record.setPicOrder(j + 1);
                record.setPicId(imageId);
                compositionService.insertCompositionCorrectionRecord(record);
            }


        }
    }

    /**
     * 解析加分项Json
     *
     * @param jsonObject
     * @param orderId
     * @param compositionId
     * @param itemMap
     */
    private void parseAitemsJsonObject(JSONObject jsonObject, Integer orderId, Integer compositionId, Map<Integer, RuleItem> itemMap,Integer ruleId) {
        JSONArray aitems = jsonObject.getJSONArray("aitems");

        for (int i = 0; i < aitems.size(); i++) {
            JSONObject item = aitems.getJSONObject(i);

            Integer itemId = item.getInteger("itemId");

            RuleItem ruleItem = itemMap.get(itemId);
            CompositionItemScore itemScore = new CompositionItemScore();
            itemScore.setCompositionId(compositionId);
            itemScore.setRuleId(ruleId);
            itemScore.setOrderId(orderId);

            if (ruleItem != null) {
                itemScore.setItemDesc(ruleItem.getItemName());
                itemScore.setItemScore(ruleItem.getScore());
                itemScore.setType(ruleItem.getItemType());
                itemScore.setItemId(ruleItem.getId());
            }
            //入库操作
            compositionService.insertCompositionItemScore(itemScore);
        }
    }

    /**
     * 解析减分项的json
     *
     * @param jsonObject
     * @param orderId
     * @param compositionId
     * @param itemMap
     */
    private void parseJItemsJsonObject(JSONObject jsonObject, Integer orderId, Integer compositionId, Map<Integer, RuleItem> itemMap,Integer ruleId) {
        JSONArray jitems = jsonObject.getJSONArray("jitems");

        for (int i = 0; i < jitems.size(); i++) {
            JSONObject item = jitems.getJSONObject(i);

            Integer itemId = item.getInteger("itemId");
            Integer jScore = item.getInteger("score");
            RuleItem ruleItem = itemMap.get(itemId);
            CompositionItemScore itemScore = new CompositionItemScore();
            itemScore.setCompositionId(compositionId);
            itemScore.setRuleId(ruleId);
            itemScore.setOrderId(orderId);
            itemScore.setItemScore(jScore);
            if (ruleItem != null) {
                itemScore.setItemId(ruleItem.getId());
                itemScore.setItemDesc(ruleItem.getItemName());
                itemScore.setType(ruleItem.getItemType());
            }
            //入库操作
            compositionService.insertCompositionItemScore(itemScore);
        }
    }

    /**
     * 解析普通得分项的json
     *
     * @param jsonObject
     * @param orderId
     * @param compositionId
     * @param itemMap
     */
    private void parsePTItemsJsonObject(JSONObject jsonObject, Integer orderId, Integer compositionId, Map<Integer, RuleItem> itemMap,Integer ruleId) {
        JSONArray ptitems = jsonObject.getJSONArray("items");

        for (int i = 0; i < ptitems.size(); i++) {
            JSONObject item = ptitems.getJSONObject(i);

            Integer itemId = item.getInteger("itemId");
            RuleItem ruleItem = itemMap.get(itemId);
            CompositionItemScore itemScore = new CompositionItemScore();
            itemScore.setCompositionId(compositionId);
            itemScore.setRuleId(ruleId);
            itemScore.setOrderId(orderId);
            if (ruleItem != null) {
                itemScore.setItemId(ruleItem.getId());
                itemScore.setItemDesc(ruleItem.getItemName());
                itemScore.setItemScore(ruleItem.getScore());
                itemScore.setType(ruleItem.getItemType());
            }

            //入库操作
            compositionService.insertCompositionItemScore(itemScore);
        }
    }

    /**
     * 解析前端发送过来的批阅报告信息
     *
     * @param score
     * @param orderId
     * @param compositionId
     */
    private void paserReportJsonObect(Integer score, Integer orderId, Integer compositionId,String reportContent,Integer ruleId) {
        CompositionReprot report = new CompositionReprot();
        report.setRuleId(ruleId);

        RuleDto ruleDto = compositionService.queryRuleInfoByRuleId(ruleId);
        report.setScore(score.toString());
        if(ruleDto!=null){
            if(ruleDto.getNoScore()!=null&&ruleDto.getNoScore()==1){
                List<RuleLev> ruleLevs = ruleDto.getRuleLevs();
                if(ruleLevs!=null&&ruleLevs.size()>0){

                    for (RuleLev ruleLev:ruleLevs){
                        if(ruleLev.getMaxScore()>=score&&score>=ruleLev.getMinScore()){
                           report.setScore(ruleLev.getDesc());
                            break;
                        }
                    }

                }
            }
        }

        report.setCreateTime(new Date());
        report.setOrderId(orderId);
        report.setCompositionId(compositionId);
        report.setReport(reportContent);

        compositionService.insertCompositionReport(report);
    }

    /**
     * 判断字符是不是数字
     * @param str
     * @return
     */
    private boolean isNumeric(String str){
        if(str!=null){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }
        return false;

    }
}
