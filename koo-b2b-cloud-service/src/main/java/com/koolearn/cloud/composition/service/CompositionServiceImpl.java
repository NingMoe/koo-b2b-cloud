package com.koolearn.cloud.composition.service;

import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.classRoom.dao.ClassRoomDao;
import com.koolearn.cloud.common.entity.Classes;
import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.composition.dao.CompositionDao;
import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.composition.dto.RuleDto;
import com.koolearn.cloud.composition.entity.*;
import com.koolearn.cloud.composition.enums.CompositionOrderStatus;
import com.koolearn.cloud.composition.enums.RuleItemType;
import com.koolearn.cloud.dictionary.dao.DictionaryDao;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.login.dao.LoginDao;
import com.koolearn.cloud.util.ConnUtil;
import com.koolearn.framework.common.page.ListPager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作文批改 dubbo service 实现类
 * Created by haozipu on 2016/7/15.
 */
public class CompositionServiceImpl implements CompositionService {

    private static Log logger = LogFactory.getLog(CompositionServiceImpl.class);

    CompositionDao compositionDao;

    ClassRoomDao classRoomDao;

    LoginDao loginDao;

    DictionaryDao dictionaryDao;

    @Override
    public Integer insertComposition(Composition composition, List<CompositionImage> imgs) {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer id=compositionDao.saveComposition(conn,composition);
            for (CompositionImage image:imgs){
                image.setCompositionId(id);
                compositionDao.saveCompostionImage(conn,image);
            }
            conn.commit();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Integer insertCompositionOrder(CompositionOrder order) {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer id=compositionDao.saveCompositionOrder(conn,order);
            conn.commit();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean updateCompositionOrderStatus(Integer orderId, Integer status) {

        if(compositionDao.updateCompositionOrderStatus(orderId,status)>0){
            return true;
        }

        return false;
    }

    @Override
    public Integer insertCompositionCorrectionRecord(CompositionCorrectionRecord record) {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer id=compositionDao.saveCompostionCorrectRecord(conn,record);
            conn.commit();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Integer insertCompositionImage(CompositionImage image) {

        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer id = compositionDao.saveCompostionImage(conn,image);
            conn.commit();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;


    }

    @Override
    public Integer insertCompositionReport(CompositionReprot compositionReport) {

        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer reportId =compositionDao.saveCompositionReport(conn,compositionReport);
            conn.commit();
            return reportId;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;

    }

    @Override
    public Integer insertCompositionItemScore(CompositionItemScore score) {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            Integer reportId =compositionDao.saveCompositionItemScore(conn,score);
            conn.commit();
            return reportId;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Composition queryCompositionById(Integer compositionId) {
        Composition composition =compositionDao.queryCompositionById(compositionId);
        return composition;
    }

    @Override
    public List<CompositionImage> queryCompositionImages(Integer compositionId) {
        List<CompositionImage> images =compositionDao.queryCompositionImageByCompostionId(compositionId);
        return images;
    }

    @Override
    public List<CompositionImage> queryCompositionCorrectImages(Integer orderId) {

        List<CompositionImage> images =compositionDao.queryCompositionImageByOrderId(orderId);

        return images;
    }

    @Override
    public List<CompositionCorrectionRecord> queryCompositionCorrectRecord(Integer compositionId,Integer orderId) {

        List<CompositionCorrectionRecord> records =compositionDao.queryCompositionCorrectRecords(orderId,compositionId);

        return records;
    }

    @Override
    public CompositionReprot queryCompositionReport(Integer compositionId,Integer orderId) {
        CompositionReprot compositionReprot =compositionDao.queryCompositionReportByOrderId(orderId,compositionId);
        return compositionReprot;
    }

    @Override
    public List<CompositionOrderDto> queryNewFiveCompositionOrders(Integer userId) {
        List<CompositionOrder> orders = compositionDao.queryFiveNewOrders(userId);
        List<CompositionOrderDto> orderDtos = new ArrayList<CompositionOrderDto>();
        if(orders!=null&&orders.size()>0){

            for (CompositionOrder order:orders){

                CompositionOrderDto dto = new CompositionOrderDto();

                BeanUtils.copyProperties(order,dto);

                dto.setStatus(order.getOrderStatus());
                dto.setType(order.getOrderType());
                dto.setArea(order.getOrderArea());
                Composition composition = compositionDao.queryCompositionById(order.getCompositionId());

                if(composition!=null){
                    dto.setCompositionTitle(composition.getTitle());
                }
                fillTeacherName(order, dto);
                CompositionReprot compositionReprot = compositionDao.queryCompositionReportByOrderId(order.getId(),order.getCompositionId());
                if(compositionReprot!=null){
                    dto.setCorrectTime(compositionReprot.getCreateTime());
                }

                orderDtos.add(dto);
            }

        }else if(orders==null||orders.size()==0){
            //查询为批改的作文记录
            List<CompositionOrder> compositionOrders = compositionDao.queryFiveUnCorrectionOrder(userId);

            for (CompositionOrder order:compositionOrders){

                CompositionOrderDto dto = new CompositionOrderDto();
                BeanUtils.copyProperties(order,dto);
                dto.setStatus(order.getOrderStatus());
                dto.setType(order.getOrderType());
                dto.setArea(order.getOrderArea());
                Composition composition = compositionDao.queryCompositionById(order.getCompositionId());

                if(composition!=null){
                    dto.setCompositionTitle(composition.getTitle());
                }
                fillTeacherName(order, dto);

                orderDtos.add(dto);

            }

        }

        logger.info("queryNewFiveCompositionOrders "+JSONObject.toJSONString(orderDtos));

        return orderDtos;
    }


    /**
     * 设置教师名称通用逻辑
     * @param order
     * @param dto
     */
    private void fillTeacherName(CompositionOrder order, CompositionOrderDto dto) {
        User teacher = loginDao.findUserByUserId(order.getTeacherId());
        dto.setTeacherName("");
        if(teacher!=null){
            dto.setTeacherName(teacher.getRealName());
        }
    }

    @Override
    public List<CompositionOrderDto> queryUserPayHistoryList(ListPager listPager, Integer userId,Integer type) {

        List<CompositionOrder> compositionOrders = compositionDao.queryPayedOrdersByUserIdWithPager(listPager,userId);



        List<CompositionOrderDto> dtos =  new ArrayList<CompositionOrderDto>();
        for (CompositionOrder order:compositionOrders){
            CompositionOrderDto dto = new CompositionOrderDto();
            BeanUtils.copyProperties(order,dto);
            Composition composition = compositionDao.queryCompositionById(order.getCompositionId());

            if(composition!=null){
                dto.setCompositionTitle(composition.getTitle());
                dto.setPic(composition.getPic());
            }
            dto.setStatus(order.getOrderStatus());
            dto.setType(order.getOrderType());
            dto.setArea(order.getOrderArea());
            fillTeacherName(order, dto);
            CompositionReprot compositionReprot = compositionDao.queryCompositionReportByOrderId(order.getId(),order.getCompositionId());
            if(compositionReprot!=null){
                dto.setCorrectTime(compositionReprot.getCreateTime());
            }

            dtos.add(dto);
        }

        logger.info("query user composition history: "+JSONObject.toJSONString(dtos));

        return dtos;
    }

    @Override
    public List<CompositionOrderDto> queryUserCompostionList(ListPager listPager, Integer userId, Integer type) {

        List<CompositionOrder> compositionOrders = null;

        if(type!=null&&CompositionOrderStatus.CORRECTED.getValue()==type){
            compositionOrders =compositionDao.queryCorrectCompositionOrderByUserId(listPager,userId);
        }else {
            compositionOrders = compositionDao.queryCompositionOrderByUserIdAndTypeWithPager(listPager, userId, type);
        }
        List<CompositionOrderDto> dtos =  new ArrayList<CompositionOrderDto>();
        if(compositionOrders!=null&&compositionOrders.size()>0){

            for (CompositionOrder order:compositionOrders){
                CompositionOrderDto dto = new CompositionOrderDto();
                BeanUtils.copyProperties(order,dto);
                Composition composition = compositionDao.queryCompositionById(order.getCompositionId());

                if(composition!=null){
                    dto.setCompositionTitle(composition.getTitle());
                    dto.setPic(composition.getPic());
                }
                dto.setStatus(order.getOrderStatus());
                dto.setType(order.getOrderType());
                dto.setArea(order.getOrderArea());

                fillTeacherName(order, dto);
                CompositionReprot compositionReprot = compositionDao.queryCompositionReportByOrderId(order.getId(),order.getCompositionId());
                if(compositionReprot!=null){
                    dto.setCorrectTime(compositionReprot.getCreateTime());
                }

                dtos.add(dto);
            }

            logger.info("queryUserCompostionList: "+JSONObject.toJSONString(dtos));

        }

        return dtos;

    }

    @Override
    public CompositionReprot generateCompositionReportByItems(List<Integer> itemIds,Map<Integer,Integer> keyIdValueScore,Integer ruleId,Integer score) {

        CompositionReprot compositionReprot = new CompositionReprot();
        JSONObject reportJson =  generateReportContent(itemIds,keyIdValueScore,ruleId,score);
        compositionReprot.setReport(reportJson.toJSONString());

        compositionReprot.setRuleId(ruleId);

        compositionReprot.setScore(reportJson.getString("得分"));

        return compositionReprot;
    }

    @Override
    public CompositionOrder queryOrderInfoByCompositionId(Integer compositionId) {

        CompositionOrder compositionOrder =compositionDao.queryCompositionOrderByCompositionId(compositionId);

        return compositionOrder;
    }

    /**
     * 根据指标项随机返回一个评语
     * @param remarksList
     * @return
     */
    private String randomRemark(List<RuleItemRemarks> remarksList){

        if(remarksList!=null&&remarksList.size()>0){
            int n = remarksList.size();
            int nn =n-1;
            if(nn<=0){
                nn=1;
            }
            Integer randIndex = RandomUtils.nextInt(nn);

            return remarksList.get(randIndex).getRemark();
        }
        return "暂无评语";

    }

    /**
     * 动态生成评分报告
     * @param itemIds
     * @param ruleId
     * @return
     */
    private JSONObject generateReportContent(List<Integer> itemIds,Map<Integer,Integer> keyIdValueScore,Integer ruleId,Integer scoreSum){

        Integer score =0;
        JSONObject reportContent = new JSONObject();
        List<JSONObject> remarkContent = new ArrayList<JSONObject>();

        Rule rule = compositionDao.queryRuleByRuleId(ruleId);

        if(rule==null){
            throw  new RuntimeException("ruleid 传入出错");
        }

        //生成得分和报告内容
        for(Integer itemId:itemIds){
            RuleItem item = compositionDao.queryRuleItemById(itemId);
            if(item!=null){
                if(item.getItemType()== RuleItemType.NORMAL_ITEM.getValue()||item.getItemType()==RuleItemType.ADD_ITEM.getValue()){
                    score =score+item.getScore();
                    JSONObject remark = new JSONObject();
                    List<RuleItemRemarks> ruleItemRemarkses =compositionDao.queryAllRemarkByItemId(itemId);
                    remark.put(item.getItemName(),randomRemark(ruleItemRemarkses));
                    remarkContent.add(remark);
                }else{
                    score =score-keyIdValueScore.get(itemId);
                }

            }

        }

        if(logger.isInfoEnabled()){
            logger.info("自动计算的分 "+score);
            logger.info("教师传入的分 "+scoreSum);
        }


        //判断规则集是否是显示得分的规则集
        if(rule.getNoScore()!=null&&rule.getNoScore()==1){
            //取报告级别和分数对应关系 循环判断
            Integer realRuleId =rule.getRuleId();
            List<RuleLev> ruleLevs =compositionDao.queryRuleLevByRuleId(realRuleId);

            if(ruleLevs!=null&&ruleLevs.size()>0){

                for (RuleLev ruleLev:ruleLevs){
                    if(ruleLev.getMaxScore()>=scoreSum&&scoreSum>=ruleLev.getMinScore()){
                        reportContent.put("得分",ruleLev.getDesc());
                        break;
                    }
                }

            }

        }else {
            reportContent.put("得分",scoreSum);
        }

        reportContent.put("remark",remarkContent);

        return reportContent;
    }

    @Override
    public List<RuleItem> queryAllItemByRuleId(Integer ruleId) {

        Rule rule = compositionDao.queryRuleByRuleId(ruleId);
        if(rule!=null&&rule.getNoScore()!=null&&rule.getNoScore()==1){
            ruleId = rule.getRuleId();
        }

        List<RuleItem> ruleItems = compositionDao.queryAllItemByRuleId(ruleId);
        return ruleItems;
    }

    @Override
    public RuleDto queryRuleInfoByRuleId(Integer ruleId) {

        RuleDto dto = new RuleDto();
        Rule rule = compositionDao.queryRuleByRuleId(ruleId);
        BeanUtils.copyProperties(rule,dto);
        if(rule!=null&&rule.getNoScore()!=null&&rule.getNoScore()==1){
            List<RuleLev> ruleLevs = compositionDao.queryRuleLevByRuleId(ruleId);
            dto.setRuleLevs(ruleLevs);
        }
        return dto;
    }

    @Override
    public DefaultRule queryDefaultRuleByCondition(Integer userId, Integer type, String area, Integer schoolLev) {

        if(schoolLev==2){
            DefaultRule defaultRule = compositionDao.queryDefaultRuleByUserIdAndCompositionInfo2(userId,type,area,schoolLev);
            return defaultRule;
        }
        DefaultRule defaultRule = compositionDao.queryDefaultRuleByUserIdAndCompositionInfo(userId,type,area,schoolLev);

        return defaultRule;
    }

    @Override
    public Integer insertDefaultRule(DefaultRule rule) {
        Connection conn = null;
        try{
            conn = ConnUtil.getTransactionConnection();
            if(rule.getSchoolLev()==2){
                DefaultRule defaultRule = compositionDao.queryDefaultRuleByUserIdAndCompositionInfo2(rule.getUserId(),rule.getType(),rule.getArea(),rule.getSchoolLev());
                if(defaultRule!=null){
                    compositionDao.updateDefaultRuleId(rule.getRuleId(),defaultRule.getId());
                    return defaultRule.getId();
                }
            }else{
                DefaultRule defaultRule = compositionDao.queryDefaultRuleByUserIdAndCompositionInfo(rule.getUserId(),rule.getType(),rule.getArea(),rule.getSchoolLev());
                if(defaultRule!=null){
                    compositionDao.updateDefaultRuleId(rule.getRuleId(),defaultRule.getId());
                    return defaultRule.getId();
                }
            }

            Integer id=compositionDao.saveDefaultRule(conn,rule);
            conn.commit();
            return id;
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            ConnUtil.rollbackConnection(conn);
        }finally {
            ConnUtil.closeConnection(conn);
        }
        return null;

    }

    @Override
    public List<Rule> queryRuleListByCondition(Integer type, String area, Integer schoolLev) {

        if(schoolLev==2){
            List<Rule> rules = compositionDao.queryRuleListByCondition2(area,type,schoolLev);
            return rules;
        }

        List<Rule> rules = compositionDao.queryRuleListByCondition(area,type,schoolLev);

        return rules;
    }

    @Override
    public List<CompositionTeacher> queryXDFTeacherByStudentId(Integer userId) {
        List<CompositionTeacher> teachers = compositionDao.queryXdfTeacherList();
        return teachers;
    }

    @Override
    public List<CompositionOrderDto> queryCompositionOrderListByConditions(ListPager listPager, String keyWord, Integer status, Integer teacherId, String startDate, String endTime) {
        List<CompositionOrderDto> compositionOrderDtos =compositionDao.queryOrderListByCondition(listPager,keyWord,status,teacherId,startDate,endTime);

        for (CompositionOrderDto dto :compositionOrderDtos){

            User userEntity = loginDao.findUserByUserId(dto.getUserId());

            if(userEntity!=null){
                dto.setStudentName(userEntity.getRealName());
            }

            if(dto.getStatus()== CompositionOrderStatus.CORRECTED.getValue()){
                //添加批阅时间
                CompositionReprot compositionReprot = compositionDao.queryCompositionReportByOrderId(dto.getId(),dto.getCompositionId());
                dto.setCorrectTime(compositionReprot.getCreateTime());
            }

            List<String> classNameList = this.queryClassNameListByTeacherIdAndStudentId(dto.getTeacherId(),userEntity.getId());

            dto.setClassName(StringUtils.join(classNameList,","));

        }

        return compositionOrderDtos;
    }

    @Override
    public BigDecimal queryPayCountByTeacherId(Integer teacherId) {

        BigDecimal count = compositionDao.queryPayCountByTeacherId(teacherId);

        return count;
    }

    @Override
    public Integer queryCompositionOrderListByConditionsCount(String keyWord, Integer status, Integer teacherId, String startDate, String endTime) {

        Integer count =compositionDao.queryOrderListByConditionCount(keyWord,status,teacherId,startDate,endTime);
        return count;
    }

    public CompositionDao getCompositionDao() {
        return compositionDao;
    }

    public void setCompositionDao(CompositionDao compositionDao) {
        this.compositionDao = compositionDao;
    }

    public LoginDao getLoginDao() {
        return loginDao;
    }

    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public ClassRoomDao getClassRoomDao() {
        return classRoomDao;
    }

    public void setClassRoomDao(ClassRoomDao classRoomDao) {
        this.classRoomDao = classRoomDao;
    }

    @Override
    public CompositionTeacher queryXdfCompositionTeacher(Integer teacherId) {
        CompositionTeacher teacher = compositionDao.queryXdfCompositionTeacherById(teacherId);



        return teacher;
    }

    @Override
    public CompositionOrder queryOrderInfoByOrderId(Integer orderNo) {

        CompositionOrder compositionOrder = compositionDao.queryCompositionOrderById(orderNo);

        return compositionOrder;
    }

    @Override
    public CompositionOrderDto queryNextCompositionOrder(Integer teacherId, Integer orderId) {

        CompositionOrderDto dto =compositionDao.queryNextCompositionOrder(teacherId,orderId);

        return dto;
    }

    @Override
    public CompositionOrderDto queryPreCompositionOrder(Integer teacherId, Integer orderId) {

        CompositionOrderDto dto =compositionDao.queryPreCompositionOrder(teacherId, orderId);

        return dto;
    }

    @Override
    public List<String> queryClassNameListByTeacherIdAndStudentId(Integer teacherId, Integer studentId) {
        List<CompositionTeacher> compositionTeachers = compositionDao.queryXdfTeacherList();
        List<Integer> teacherIds = new ArrayList<Integer>();
        for (CompositionTeacher teacher:compositionTeachers){
            teacherIds.add(teacher.getId());
        }

        if(!teacherIds.contains(teacherId)){
            //userId --> id

            //查询教师的班级
            List<Integer> teacherClassIds = classRoomDao.queryClassIdsByTeacherId(teacherId);

            logger.info("老师的班级ID "+JSONObject.toJSONString(teacherClassIds));

//            User user = loginDao.findUserByUserId(studentId);
//            Assert.notNull(user,"没有查询到指定的学生 user_id:"+studentId);
            //查询学生的班级

            List<Classes> classesList = classRoomDao.queryStudentClassInfo(studentId);
            logger.info("学生的班级ID" +JSONObject.toJSONString(classesList));

            Map<Integer,Classes> classMap = new HashMap<Integer, Classes>();
            List<String> classNameList= new ArrayList<String>();
            for (Classes classes:classesList){
                classMap.put(classes.getId(),classes);
            }
            for(Integer id:teacherClassIds){
                Classes classes =classMap.get(id);
                if(classes!=null){
                    classNameList.add(classes.getClassName());
                }
            }
            return classNameList;
        }

        return new ArrayList<String>();
    }

    @Override
    public int updateCompositionOrderViewFlag(Integer orderId) {
        int c =compositionDao.updateCompositionOrderViewFlag(orderId);
        return c;
    }

    @Override
    public int queryTeacherNewCompositionCount(Integer teacherId) {

        int c =compositionDao.queryTeacherNewCompositionCount(teacherId);
        return c;
    }

    @Override
    public Boolean queryIsCompositionTeacher(Integer teacherId) {
        //判断是不是新东方收费

        CompositionTeacher compositionTeacher =compositionDao.queryXdfCompositionTeacherById(teacherId);
        if(compositionTeacher!=null){
            return true;
        }
        //判断是不是有语文学科
        com.koolearn.cloud.dictionary.entity.Dictionary dictionary =dictionaryDao.queryDictionaryByNameAndType("语文", Dictionary.TYPE_SUBJECT);

        if(dictionary==null){
            throw new RuntimeException("语文学科字典数据不存在");
        }

        User user = loginDao.findUserByUserId(teacherId);
        if(user!=null){
            Integer count = compositionDao.queryTeacherCompositionClassCount(user.getId(),dictionary.getValue());
            if(count>0){
                return true;
            }
        }

        return false;
    }

    @Override
    public Boolean updateCompositionOrderCodeByEclass(String orderCode, Integer orderId) {
        compositionDao.updateCompositionOrderCodeByEclassInfo(orderCode,orderId);
        return true;
    }

    public DictionaryDao getDictionaryDao() {
        return dictionaryDao;
    }

    public void setDictionaryDao(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }
}
