package com.koolearn.cloud.composition;


import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.classRoom.service.ClassRoomService;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.composition.entity.*;
import com.koolearn.cloud.composition.enums.*;
import com.koolearn.cloud.composition.service.CompositionService;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.dictionary.service.DictionaryService;
import com.koolearn.cloud.login.dto.UserMobi;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.login.service.LoginService;
import com.koolearn.cloud.mobi.dto.*;
import com.koolearn.cloud.mobi.service.StudentCompositionHessianService;
import com.koolearn.cloud.student.StudentClassHomeService;
import com.koolearn.cloud.util.PayConstants;
import com.koolearn.cloud.util.RemoteHttpRequestUtil;
import com.koolearn.cloud.util.WeiXinUtil;
import com.koolearn.common.util.StringUtil;
import com.koolearn.dubbo.share.service.order.OrderDubboService;
import com.koolearn.framework.common.page.ListPager;
import com.koolearn.pay.api.common.util.KooPayConstant;
import com.koolearn.pay.api.redirect.KooPayAlipayRedirect;
import com.koolearn.pay.api.redirect.KooPayWeixinRedirect;
import com.koolearn.pay.api.redirect.dto.KooPayAppResponseNewDto;
import com.koolearn.pay.api.redirect.dto.KooPayParamDto;
import com.koolearn.pay.api.redirect.util.KooPayResponseUtil;
import com.koolearn.util.SystemGlobals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by haozipu on 2016/7/18.
 */
public class CompositionHessianServiceImpl implements StudentCompositionHessianService {

    private static Log logger = LogFactory.getLog(CompositionHessianServiceImpl.class);

    @Autowired
    CompositionService compositionService;

    @Autowired
    StudentClassHomeService studentClassHomeService;

    @Autowired
    ClassRoomService classRoomService;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    LoginService loginService;

    //@Autowired
    OrderDubboService orderDubboService;

    @Override
    public CompositionResult makeComposition(String compostionTitle,Integer userId, Integer type, Integer schoolLev, String imgListInfo) {

        Composition composition = new Composition();
        composition.setCreateTime(new Date());
        composition.setSchoolLev(schoolLev);
        composition.setType(type);
        composition.setUserId(userId);
        composition.setTitle(compostionTitle);

        List<CompositionImage> compositionImages = new ArrayList<CompositionImage>();
        if(StringUtil.isNotEmpty(imgListInfo)){
            String[] imgOrderArr =imgListInfo.split(",");
            int i=1;
            for (String imgOrder:imgOrderArr){
                CompositionImage img = new CompositionImage();
                img.setType(CompositionImageType.ORIGIN_IMG.getValue());
                img.setImgUrl(imgOrder);
                //img.setOrderId(i++);
                img.setImgIndex(i++);
                img.setStatus(1);
                img.setBatchId(1);
                compositionImages.add(img);

            }
        }
        composition.setPic(compositionImages.get(0).getImgUrl());
        Integer compositionId = compositionService.insertComposition(composition,compositionImages);
        composition.setId(compositionId);
        //返回作文编号
        logger.info(JSONObject.toJSONString(composition));
        //返回指定对象结果
        CompositionResult compositionResult = new CompositionResult();
        compositionResult.setCreateTime(composition.getCreateTime());
        compositionResult.setCompositionId(compositionId);
        compositionResult.setCompositionTitle(compostionTitle);
        compositionResult.setImgUrl(compositionImages.get(0).getImgUrl());

        return compositionResult;
    }

    @Override
    public CompositionOrderResult makeCompositionOrder(Integer teacherId, Integer userId, Integer compositionId,String payType,Integer teacherType) {

        Composition composition = compositionService.queryCompositionById(compositionId);

        CompositionOrder compositionOrder = new CompositionOrder();
        compositionOrder.setCreateTime(new Date());
        compositionOrder.setCompositionId(compositionId);
        compositionOrder.setOrderArea(composition.getArea());

        //查询老师金额的逻辑
        BigDecimal price =getTeacherCorrectPrice(teacherId,teacherType);

        DecimalFormat myformat = new DecimalFormat("#########0.00");
        String strAmount = String.valueOf(myformat.format(price));// 格式化，去掉多余小数
        if(logger.isInfoEnabled()){
            logger.info("教师ID "+teacherId+" 金额 "+strAmount);
        }

        //组装本系统的订单对象
        compositionOrder.setPrice(price);
        compositionOrder.setTeacherId(teacherId);
        compositionOrder.setSchoolLev(composition.getSchoolLev());
        compositionOrder.setOrderType(teacherType);
        compositionOrder.setUserId(userId);
        compositionOrder.setOrderStatus(CompositionOrderStatus.NO_PAY.getValue());
        compositionOrder.setPayType(payType);
        compositionOrder.setViewFlag(0);
        //支付方式
        if(teacherType==CompositionTeacherType.FREE.getValue()){
            compositionOrder.setOrderStatus(CompositionOrderStatus.PAYED.getValue());
        }

        //本系统的订单ID
        Integer orderId = compositionService.insertCompositionOrder(compositionOrder);

        if(logger.isInfoEnabled()){
            logger.info("插入作文订单 "+orderId);
        }

        //支付中心下单逻辑
        CompositionOrderResult compositionOrderResult = new CompositionOrderResult();
        compositionOrderResult.setOrderId(orderId);

        if(teacherType!=CompositionTeacherType.FREE.getValue()){

            //如果是新东方 则需要与支付中心进行交互 下单等
            KooPayParamDto dto = new KooPayParamDto();
            dto.setAmount(strAmount);
            dto.setCharset("utf8");
            dto.setCuryId("100");
            dto.setNotifyUrl(SystemGlobals.getPreference("domain") + "/composition/payCenter/notify");// 后台通知接口
            dto.setOutOrderNo(orderId.toString());// 订单id
            dto.setPartnerId(PayConstants.PARTNER_ID);
            dto.setPayDesc("新东方在线云平台语文作文批改");
            dto.setPaySubject("新东方在线云平台语文作文批改");
            dto.setPayWayId(payType);// 支付方式
            dto.setReturnUrl(SystemGlobals.getPreference("domain") + "/composition/payCenter/notify");// 页面跳转地址
            dto.setService("pay_app");
            dto.setSiteType(KooPayConstant.SITE_TYPE_M);
            dto.setTradeType("APP");
            dto.setSignKey(PayConstants.PAY_SIGN_KEY);
            dto.setPayUrl(SystemGlobals.getPreference("hosts.pay"));

            String url ="";

            if(PayType.WEIXIN.getValue().equals(payType)){
                url = new KooPayWeixinRedirect().kooPay(null, null, dto);
            }else if (PayType.ALIPAY.getValue().equals(payType)){
                url = new KooPayAlipayRedirect().kooPay(null, null, dto);
            }

            logger.info("KooPayWeixinRedirect().kooPay 返回的url "+url);

            logger.info("后台接收到的支付方式 "+payType);

            String s = RemoteHttpRequestUtil.getHttpresponseData(url, new ArrayList<String>(),new ArrayList<String>(), RemoteHttpRequestUtil.REQUEST_TYPE_GET);

            if(logger.isInfoEnabled()){
                logger.info("支付中心返回的result "+s);
            }

            KooPayAppResponseNewDto dtos = KooPayResponseUtil.createNewAppResponseDto(s);

            logger.info("调用创建KooPayDto方法 拿到的Dto "+JSONObject.toJSONString(dtos));

            dtos.setSignKey(PayConstants.PAY_SIGN_KEY);// 支付中心为业务系统分配的key
            boolean isSign = KooPayResponseUtil.isAppSignMatchingNew(dtos);// 验证签名
            boolean isSucess = KooPayResponseUtil.isAppSuccessNew(dtos);// 验证交互是否成功

            if (isSign && isSucess) {
                //sendOrderInfoToClass();
                compositionOrderResult.setPrice(strAmount);
                //如果是收费的 判断支付方式 返回不同的数据给手机端
                if(PayType.WEIXIN.getValue().equals(payType)){
                    //如果是微信
                    compositionOrderResult.setOrderNo(dtos.getOrderNo());
                    compositionOrderResult.setPayCenterOrderNo(dtos.getOrderNo());
                    compositionOrderResult.setPayType(PayType.WEIXIN.getValue());
                    compositionOrderResult.setWeixinPrepayId(dtos.getWeixinPrepayId());
                    compositionOrderResult.setPackageStr("Sign=WXPay");
                    compositionOrderResult.setNoncestr(WeiXinUtil.randomString());
                    compositionOrderResult.setTimestamp(new Date().getTime()/1000+"");

                    compositionOrderResult.setSign(getWeixinSign(dtos,compositionOrderResult));


                    compositionOrderResult.setPartnerId(PayConstants.WEIXIN_PARTNER_ID);
                    compositionOrderResult.setNotifyUrl(SystemGlobals.getPreference("hosts.pay")+"/appNotify/alipayNotify");
                    compositionOrderResult.setError(0);
                    //增加调用课堂接口的逻辑
                    sendOrderInfoToClass(userId+"", ClassPayType.WEIXIN.getValue(),orderId);
                    logger.info("支付方式是为微信 " + JSONObject.toJSONString(compositionOrderResult));

                }else if (PayType.ALIPAY.getValue().equals(payType)){
                    //如果是支付宝

                    compositionOrderResult.setAli_partner(dtos.getAli_partner());
                    compositionOrderResult.setAli_seller_id(dtos.getAli_seller_id());
                    compositionOrderResult.setAli_out_trade_no(dtos.getAli_out_trade_no());
                    compositionOrderResult.setAli_subject(dtos.getAli_subject());
                    compositionOrderResult.setAli_service(dtos.getAli_service());
                    compositionOrderResult.setAli_input_charset(dtos.getAli_input_charset());
                    compositionOrderResult.setAli_sign_type(dtos.getAli_sign_type());
                    compositionOrderResult.setAli_sign(dtos.getAli_sign());
                    compositionOrderResult.setAli_notify_url(dtos.getAli_notify_url());
                    compositionOrderResult.setAli_payment_type(dtos.getAli_payment_type());
                    compositionOrderResult.setAli_total_fee(dtos.getAli_total_fee());
                    compositionOrderResult.setAli_body(dtos.getAli_body());

//                    compositionOrderResult.setOrderNo(dtos.getOrderNo());
//                    compositionOrderResult.setPayCenterOrderNo(dtos.getOrderNo());
//                    compositionOrderResult.setPayType(PayType.ALIPAY.getValue());
//                    compositionOrderResult.setTimestamp(new Date().getTime() + "");
//                    compositionOrderResult.setProductName(dto.getPayDesc());
//                    compositionOrderResult.setNotifyUrl(SystemGlobals.getPreference("hosts.pay")+"/appNotify/alipayNotify");

                    compositionOrderResult.setError(0);
                    //增加调用课堂接口的逻辑
                    sendOrderInfoToClass(userId+"", ClassPayType.ALIPAY.getValue(),orderId);
                    logger.info("支付方式是为支付宝 "+JSONObject.toJSONString(compositionOrderResult));
                }else {
                    compositionOrderResult.setError(1);
                    compositionOrderResult.setMsg("支付方式不支持");
                }
            }else {
                compositionOrderResult.setError(1);
                compositionOrderResult.setMsg("与支付中心通信或者签名校验失败");
            }

        }

        return compositionOrderResult;
    }

    /**
     * 调用课堂的下订单接口
     * @param userId
     * @param payWayId
     */
    private void sendOrderInfoToClass(String userId,String payWayId,Integer myOrderId) {
        try{
            Map<String,String> requestMap = new HashMap<String, String>();

            requestMap.put("userId",userId);
            requestMap.put("products",PayConstants.composition_products);//产品id_产品版本号
            requestMap.put("payWayId",payWayId);
            requestMap.put("appId",PayConstants.appId);
            requestMap.put("appVersionId",PayConstants.appVersion);

            Map<String,String> reponseMap = orderDubboService.createOrder(requestMap);

            String code  = reponseMap.get("code");

            //调用课堂订单成功
            if(code!=null&&"0".equals(code)){
                //调用课堂订单成功
                logger.info("课堂订单创建接口调用成功 "+JSONObject.toJSONString(reponseMap));
                //记录订单ID
                String orderId = reponseMap.get("orderId");
                if(orderId!=null){
                    //开始更新本系统订单表
                    compositionService.updateCompositionOrderCodeByEclass(orderId,myOrderId);
                }


            }else {
                //调用失败
                logger.info("课堂订单创建接口调用失败 "+JSONObject.toJSONString(reponseMap));
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
        }


    }


    /**
     * 生成返回给手机端的微信 sign
     * @param dtos
     * @return
     */
    private String getWeixinSign(KooPayAppResponseNewDto dtos,CompositionOrderResult compositionResult){
        Map<String,String> signMap = new HashMap<String, String>();
        signMap.put("appid", PayConstants.WEIXIN_APPID);
        signMap.put("partnerid", PayConstants.WEIXIN_PARTNER_ID);
        signMap.put("prepayid", dtos.getWeixinPrepayId());
        signMap.put("package", "Sign=WXPay");
        signMap.put("noncestr", compositionResult.getNoncestr());
        signMap.put("timestamp", compositionResult.getTimestamp());

        logger.info("服务器端签名 map "+JSONObject.toJSONString(signMap));

        logger.info("服务器端签名 "+WeiXinUtil.weixinSign(signMap, PayConstants.WEIXIN_SIGN_KEY));

        return WeiXinUtil.weixinSign(signMap, PayConstants.WEIXIN_SIGN_KEY);

    }

    /**
     * 教师订单金额的逻辑
     * @param teacherId
     * @param teacherType
     * @return
     */
    private BigDecimal getTeacherCorrectPrice(Integer teacherId, Integer teacherType) {

        if(teacherType==CompositionTeacherType.UNFREE.getValue()){

            CompositionTeacher teacher = compositionService.queryXdfCompositionTeacher(teacherId);

            logger.info("查询到的新东方老师："+JSONObject.toJSONString(teacher));

            if(teacher!=null){
                return new BigDecimal(0.01);
                //return teacher.getPrice();
            }else {
                logger.error("给定新东方老师不存在 id "+teacherId);
                throw new RuntimeException("\"给定新东方老师不存在 id \"+teacherId");

            }

        }
        return new BigDecimal(0);
    }

    @Override
    public UserInfo queryUserInfo(Integer userId) {

        UserInfo userInfo = new UserInfo();
        UserEntity ue = new UserEntity();
        ue.setType(UserEntity.USER_TYPE_STUDENT);
        ue.setUserId(userId);
        UserEntity student = loginService.findClassesStudent(ue);

        if(student!=null){
            userInfo.setUserId(userId);
            userInfo.setHeadUrl(student.getIco());
            userInfo.setSchoolName(student.getSchoolName());
            userInfo.setUserName(student.getRealName());
            List<Classes> classesList=classRoomService.queryClassesByStudentId(userId);
            //多个班级只显示一个
            if(classesList!=null&&classesList.size()>0){
                userInfo.setClassName(classesList.get(0).getClassName());
            }
        }

        return userInfo;
    }

    @Override
    public  List<PurchaseHistory> queryUserPayList(Integer userId,Integer size, Integer pageNo) {
        ListPager listPager = new ListPager();
        listPager.setPageSize(size);
        listPager.setPageNo(pageNo);

        List<CompositionOrderDto> dtos =compositionService.queryUserPayHistoryList(listPager,userId,CompositionOrderStatus.PAYED.getValue());

        List<PurchaseHistory> purchaseHistories = new ArrayList<PurchaseHistory>();

        for (CompositionOrderDto dto:dtos){
            PurchaseHistory history = new PurchaseHistory();
            history.setCreateTime(dto.getCreateTime());
            history.setOrderId(dto.getId());
            history.setTeacherName(dto.getTeacherName());
            //history.setUserName();
            history.setPrice(dto.getPrice());
            history.setPayType(dto.getPayType());
            history.setCompositionTitle(dto.getCompositionTitle());
            history.setOrderNo(dto.getOrderCode());
            purchaseHistories.add(history);
        }


        return purchaseHistories;
    }

    @Override
    public  List<CompositionOrderDto> queryNewFiveCompositionRecord(Integer userId) {
        List<CompositionOrderDto> dtos = compositionService.queryNewFiveCompositionOrders(userId);
        return dtos;
    }

    @Override
    public CompositionDetail queryCompostionDetail(Integer compositionId,Integer userId) {

        Composition composition = compositionService.queryCompositionById(compositionId);
        CompositionOrder compositionOrder = compositionService.queryOrderInfoByCompositionId(compositionId);

        CompositionDetail compositionDetail = new CompositionDetail();
        compositionDetail.setCreateTime(compositionOrder.getCreateTime());
        compositionDetail.setCompositionTitle(composition.getTitle());

        //教师名称
        User user = loginService.findUserByUserId(compositionOrder.getTeacherId());
        compositionDetail.setTeacherName("暂无");
        if(user!=null){
            compositionDetail.setTeacherName(user.getRealName());
        }

        compositionDetail.setCompositionId(compositionId);
        compositionDetail.setStatus(compositionOrder.getOrderStatus());
        //已经批改的查询批改的记录 和 批改的报告
        if(compositionOrder.getOrderStatus()==CompositionOrderStatus.CORRECTED.getValue()){
            //批改记录
            List<CompositionCorrectionRecord> records = compositionService.queryCompositionCorrectRecord(compositionId,compositionOrder.getId());
            List<CompositionCorrectionRecordResult> recordResults = new ArrayList<CompositionCorrectionRecordResult>();
            for (CompositionCorrectionRecord record:records){
                CompositionCorrectionRecordResult recordResult = new CompositionCorrectionRecordResult();
                BeanUtils.copyProperties(record,recordResult);
                recordResults.add(recordResult);
            }
            compositionDetail.setCompositionCorrectionRecords(recordResults);

            //批改后图片
            List<CompositionImage> images1 = compositionService.queryCompositionCorrectImages(compositionOrder.getId());
            List<CompositionImageResult> imageResults1 = new ArrayList<CompositionImageResult>();

            for (CompositionImage compositionImage:images1){
                CompositionImageResult result = new CompositionImageResult();
                BeanUtils.copyProperties(compositionImage,result);
                imageResults1.add(result);
            }
            compositionDetail.setImages(imageResults1);

            //批阅报告
            CompositionReprot compositionReprot = compositionService.queryCompositionReport(compositionId,compositionOrder.getId());
            CompositionReprotResult compositionReprotResult = new CompositionReprotResult();
            if(logger.isInfoEnabled()){
                logger.info("查询详情 报告对象 "+compositionReprot);
            }
            if(compositionReprot!=null){
                BeanUtils.copyProperties(compositionReprot,compositionReprotResult);
            }
            compositionDetail.setCompositionReport(compositionReprotResult);
            //批阅时间
            compositionDetail.setCorrectTime(compositionReprot.getCreateTime());

        }else {
            //作文图片
            List<CompositionImage> images = compositionService.queryCompositionImages(compositionId);
            List<CompositionImageResult> imageResults = new ArrayList<CompositionImageResult>();
            for (CompositionImage compositionImage:images){
                CompositionImageResult result = new CompositionImageResult();
                BeanUtils.copyProperties(compositionImage,result);
                imageResults.add(result);
            }
            compositionDetail.setImages(imageResults);

        }

        return compositionDetail;
    }

    @Override
    public  List<CompositionOrderDto> queryCompositionHistory(Integer userId, int type, Integer size, Integer pageNo) {

        ListPager listPager = new ListPager();
        listPager.setPageNo(pageNo);
        listPager.setPageSize(size);

        List<CompositionOrderDto> dtos = compositionService.queryUserCompostionList(listPager, userId, type);
        if(logger.isInfoEnabled()){
            logger.info("历史查询接口 "+JSONObject.toJSONString(dtos));
        }

        return dtos;
    }

    @Override
    public  List<TeacherInfo> queryTeacherList(Integer studentId) {
        List<TeacherInfo> teacherInfos = new ArrayList<TeacherInfo>();
        //查询学生所在班级(多个) 返回每个班级的语文老师
        List<Integer> classesIdList = studentClassHomeService.findAllClassesIdByStudentId(studentId);

        Dictionary dictionary = dictionaryService.queryDictionaryByTypeAndName("语文", Dictionary.TYPE_SUBJECT);

        if(dictionary!=null){
            List<Integer> teacherIds =classRoomService.queryTeachersByClassIdsAndSubjectId(classesIdList,dictionary.getValue());

            for (Integer teacherId:teacherIds){
                TeacherInfo teacherInfo = new TeacherInfo();

                User user =loginService.findUserByUserId(teacherId);
                logger.info("查询教师接口查询一个免费老师  "+JSONObject.toJSONString(user));
                teacherInfo.setTeacherId(teacherId);
                teacherInfo.setTeacherName("测试老师");
                teacherInfo.setHeadUrl("a.jpg");
                if(user!=null){
                    UserMobi mobi =loginService.findMobileUser(user.getUserName());
                    logger.info("查询教师接口查询一个免费老师MOBI  "+JSONObject.toJSONString(mobi));
                    teacherInfo.setTeacherName(user.getRealName());
                    teacherInfo.setHeadUrl(mobi.getIco());
                }
                teacherInfo.setTeacherType(CompositionTeacherType.FREE.getValue());
                teacherInfo.setPrice(new BigDecimal(0));
                teacherInfos.add(teacherInfo);
            }

        }else {
            throw new RuntimeException("查询基础语文学科字典数据出错");
        }
        List<CompositionTeacher> teachers = compositionService.queryXDFTeacherByStudentId(studentId);

        if(teachers!=null&&teachers.size()>0){

            for (CompositionTeacher teacher:teachers){
                TeacherInfo teacherInfo = new TeacherInfo();
                teacherInfo.setTeacherId(teacher.getId());
                teacherInfo.setTeacherName(teacher.getTeacherName());
                teacherInfo.setHeadUrl(teacher.getHeadUrl());
                teacherInfo.setTeacherType(CompositionTeacherType.UNFREE.getValue());
                teacherInfo.setPrice(teacher.getPrice());
                teacherInfos.add(teacherInfo);

            }

        }
        return teacherInfos;
    }

    @Override
    public OrderStatusResult queryOrderStatus(Integer orderNo) {

        CompositionOrder compositionOrder = compositionService.queryOrderInfoByOrderId(orderNo);
        OrderStatusResult result = new OrderStatusResult();
        if(compositionOrder!=null){
            result.setOrderNo(orderNo);
            result.setOrderStatus(compositionOrder.getOrderStatus());
            result.setError(0);
            return result;
        }
        result.setError(1);
        result.setMsg("没有查到订单信息");
        return result;

    }
}
