package com.koolearn.cloud.composition.service;

import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.composition.dto.RuleDto;
import com.koolearn.cloud.composition.entity.*;
import com.koolearn.framework.common.page.ListPager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 作文批改的service
 * Created by haozipu on 2016/7/15.
 */
public interface CompositionService {

    /**
     * 插入一篇作文
     * @param composition 作文对象
     * @param imgs 作文对象的图片列表信息
     * @return
     */
    public Integer insertComposition(Composition composition,List<CompositionImage> imgs);

    /**
     * 插入一个作文订单
     * @param order
     * @return
     */
    public Integer insertCompositionOrder(CompositionOrder order);

    /**
     * 修改订单的状态
     * @param orderId
     * @param status
     * @return
     */
    public boolean updateCompositionOrderStatus(Integer orderId,Integer status);

    /**
     * 插入一条批改记录
     * @param record
     * @return
     */
    public Integer insertCompositionCorrectionRecord(CompositionCorrectionRecord record);

    /**
     * 插入张作文图片
     * @param image
     * @return
     */
    public Integer insertCompositionImage(CompositionImage image);

    /**
     * 插入一篇批改报告
     * @param compositionReport
     * @return
     */
    public Integer insertCompositionReport(CompositionReprot compositionReport);

    /**
     * 插入一个指标评分项的得分记录
     * @param score
     * @return
     */
    public Integer insertCompositionItemScore(CompositionItemScore score);

    /**
     * 根据主键查询作文
     * @param compositionId
     * @return
     */
    public Composition queryCompositionById(Integer compositionId);

    /**
     * 查询作文的图片列表
     * @param compositionId
     * @return
     */
    public List<CompositionImage> queryCompositionImages(Integer compositionId);

    /**
     * 查询作文批改的图片列表
     * @param orderId
     * @return
     */
    public List<CompositionImage> queryCompositionCorrectImages(Integer orderId);

    /**
     * 查询作文批改记录
     * @param compositionId
     * @return
     */
    public List<CompositionCorrectionRecord> queryCompositionCorrectRecord(Integer compositionId,Integer orderId);

    /**
     * 查询作文批改报告
     * @param compositionId
     * @return
     */
    public CompositionReprot queryCompositionReport(Integer compositionId,Integer orderId);
    /**
     * 查询用户最新的五条批改作文信息 如果没有 返回作文信息
     * @param userId
     * @return
     */
    public List<CompositionOrderDto> queryNewFiveCompositionOrders(Integer userId);

    /**
     * 分页查询用户的消费记录
     * @param listPager
     * @param userId
     * @return
     */
    public List<CompositionOrderDto> queryUserPayHistoryList(ListPager listPager,Integer userId,Integer type);

    /**
     * 分页查询用户的作文记录 提供给手机端tab调用
     *
     * @param listPager
     * @param userId
     * @param type
     * @return
     */
    public List<CompositionOrderDto> queryUserCompostionList(ListPager listPager,Integer userId,Integer type);

    /**
     * 根据老师勾选的指标和规则集ID 生成报告
     * @param itemIds
     * @Param ruleId
     * @return
     */
    public CompositionReprot generateCompositionReportByItems(List<Integer> itemIds,Map<Integer,Integer> keyIdValueScore,Integer ruleId,Integer score);

    /**
     * 查询作文订单
     * @param compositionId
     * @return
     */
    public CompositionOrder queryOrderInfoByCompositionId(Integer compositionId);

    /**
     * 查询订单 按照订单ID
     * @param orderNo
     * @return
     */
    public CompositionOrder queryOrderInfoByOrderId(Integer orderNo);

    /**
     * 根据规则ID查询所有的规则评分项目
     * @param ruleId
     * @return
     */
    public List<RuleItem> queryAllItemByRuleId(Integer ruleId);

    /**
     * 查询规则集的信息和对应的评分等级对应的名称
     * @param ruleId
     * @return
     */
    public RuleDto queryRuleInfoByRuleId(Integer ruleId);

    /**
     * 根据给定的条件返回教师设置默认规则集
     * @param userId
     * @param type
     * @param area
     * @param schoolLev
     * @return
     */
    public DefaultRule queryDefaultRuleByCondition(Integer userId,Integer type,String area,Integer schoolLev);

    /**
     * 按照指定的条件返回可供教师选择的评分规则集
     * @param type
     * @param area
     * @param schoolLev
     * @return
     */
    public List<Rule> queryRuleListByCondition(Integer type,String area,Integer schoolLev);


    /**
     * 插入默认规则集
     * @param rule
     * @return
     */
    public Integer insertDefaultRule(DefaultRule rule);

    /**
     * 根据学生ID 查询符合条件的批改老师
     * @param userId
     * @return
     */
    public List<CompositionTeacher> queryXDFTeacherByStudentId(Integer userId);

    /**
     * 按照条件模糊查询教师的批阅作文列表 分页
     * @param listPager 分页信息
     * @param keyWord 关键词
     * @param status 状态
     * @param teacherId 教师ID
     * @param startDate 开始日期
     * @param endTime 结束日期
     * @return
     */
    public  List<CompositionOrderDto> queryCompositionOrderListByConditions(ListPager listPager,String keyWord,
                                                                            Integer status,Integer teacherId,
                                                                            String startDate,String endTime);

    /**
     * 按照教师ID查询 已经批阅过得作文的总金额
     * @param teacherId
     * @return
     */
    public BigDecimal queryPayCountByTeacherId(Integer teacherId);

    /**
     * 查询符合条件的数据总数
     * @param keyWord
     * @param status
     * @param teacherId
     * @param startDate
     * @param endTime
     * @return
     */
    public Integer queryCompositionOrderListByConditionsCount(String keyWord,
                                                              Integer status,Integer teacherId,
                                                              String startDate,String endTime);

    /**
     * 查询新东方收费的老师
     * @param teacherId
     * @return
     */
    public CompositionTeacher queryXdfCompositionTeacher(Integer teacherId);

    /**
     * 查询下一篇作文订单
     * @param teacherId 教师ID
     * @param orderId 当前订单ID
     * @return
     */
    public CompositionOrderDto queryNextCompositionOrder(Integer teacherId,Integer orderId);

    /**
     * 查询上一篇作文订单
     * @param teacherId 教师ID
     * @param orderId 当前订单ID
     * @return
     */
    public CompositionOrderDto queryPreCompositionOrder(Integer teacherId,Integer orderId);

    /**
     * 查询老师和学生的班级的交集 每个元素是一个班级的名字
     * @param teacherId
     * @param studentId
     * @return
     */
    public List<String> queryClassNameListByTeacherIdAndStudentId(Integer teacherId,Integer studentId);

    /**
     * 修改订单的查阅状态为已经查阅
     * @param orderId
     * @return
     */
    public int updateCompositionOrderViewFlag(Integer orderId);

    /**
     * 查询教师总共有多少为查阅的作文
     * @param teacherId
     * @return
     */
    public int queryTeacherNewCompositionCount(Integer teacherId);

    /**
     * 判断当前插入的教师 是不是语文老师
     * @param teacherId
     * @return
     */
    public Boolean queryIsCompositionTeacher(Integer teacherId);

    /**
     * 更新订单的orderCode 为课堂下单接口的订单号
     * @param orderCode
     * @param orderId
     * @return
     */
    public Boolean updateCompositionOrderCodeByEclass(String orderCode,Integer orderId);

}
