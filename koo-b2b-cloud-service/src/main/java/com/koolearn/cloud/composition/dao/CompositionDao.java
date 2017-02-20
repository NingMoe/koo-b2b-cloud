package com.koolearn.cloud.composition.dao;

import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.composition.entity.*;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.annotation.*;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.PageBy;
import com.koolearn.framework.common.page.ListPager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * Created by haozipu on 2016/7/18.
 */
@DAO(source = GlobalConstant.MYSQL_DATASOURCE)
public interface CompositionDao {

    /**
     * 根据ID查询作文
     * @param compositionId
     * @return
     */
    @SQL("SELECT * FROM composition WHERE id =:compositionId")
    Composition queryCompositionById(@SQLParam("compositionId")Integer compositionId);

    /**
     * 保存作文 返回作文ID
     * @param composition
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveComposition(Connection connection,Composition composition);

    /**
     * 保存一个作文图片
     * @param image
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveCompostionImage(Connection connection,CompositionImage image);

    /**
     * 保存批改记录
     * @param record
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveCompostionCorrectRecord(Connection connection,CompositionCorrectionRecord record);

    /**
     * 保存评分项得分
     * @param itemScore
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveCompositionItemScore(Connection connection,CompositionItemScore itemScore);

    /**
     * 保存一个订单
     * @param order
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveCompositionOrder(Connection connection,CompositionOrder order);

    /**
     * 保存教师默认评分规则
     * @param rule
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveDefaultRule(Connection connection,DefaultRule rule);

    /**
     * 保存批改报告
     * @param reprot
     * @return
     */
    @SQL(type = SQLType.WRITE_INSERT)
    @GeneratedKey
    int saveCompositionReport(Connection connection,CompositionReprot reprot);

    /**
     *根据用户和作文的信息 返回该用户对应的默认评分规则
     * @param userId
     * @param type
     * @param area
     * @param schoolLev
     * @return
     */
    @SQL("SELECT * FROM default_rule WHERE user_id =:userId AND school_lev =:schoolLev AND TYPE =:type limit 1")
    DefaultRule queryDefaultRuleByUserIdAndCompositionInfo(@SQLParam("userId")Integer userId,@SQLParam("type")Integer type,@SQLParam("area")String area,@SQLParam("schoolLev")Integer schoolLev);

    /**
     *根据用户和作文的信息 返回该用户对应的默认评分规则
     * @param userId
     * @param type
     * @param area
     * @param schoolLev
     * @return
     */
    @SQL("SELECT * FROM default_rule WHERE user_id =:userId AND school_lev =:schoolLev limit 1")
    DefaultRule queryDefaultRuleByUserIdAndCompositionInfo2(@SQLParam("userId")Integer userId,@SQLParam("type")Integer type,@SQLParam("area")String area,@SQLParam("schoolLev")Integer schoolLev);

    /**
     * 查询作文的图片列表
     * @param compositionId
     * @return
     */
    @SQL("SELECT * FROM composition_image WHERE composition_id =:compositionId AND TYPE =1 ORDER BY img_index asc")
    List<CompositionImage> queryCompositionImageByCompostionId(@SQLParam("compositionId")Integer compositionId);

    /**
     * 查询订单批改后的作文图片列表
     * @param orderId
     * @return
     */
    @SQL("SELECT * FROM composition_image WHERE order_id =:orderId AND TYPE =2 ORDER BY img_index asc")
    List<CompositionImage> queryCompositionImageByOrderId(@SQLParam("orderId")Integer orderId);

    /**
     * 查询某个评分规则的全部评分项
     * @param ruleId
     * @return
     */
    @SQL("SELECT * FROM rule_item WHERE rule_id =:ruleId ORDER BY item_name ASC,lev ASC")
    List<RuleItem> queryAllItemByRuleId(@SQLParam("ruleId")Integer ruleId);

    /**
     * 查询某个评分项对应的所有评语
     * @param itemId
     * @return
     */
    @SQL("SELECT * FROM rule_item_remarks WHERE item_id =:itemId")
    List<RuleItemRemarks> queryAllRemarkByItemId(@SQLParam("itemId")Integer itemId);

    /**
     * 查询某个订单的批改报告
     * @param orderId
     * @return
     */
    @SQL("SELECT * FROM composition_reprot WHERE order_id =:orderId AND composition_id =:compositionId ")
    CompositionReprot queryCompositionReportByOrderId(@SQLParam("orderId")Integer orderId,@SQLParam("compositionId")Integer compositionId);

    /**
     * 查询某个用户的最新的批阅记录
     * @param userId
     * @return
     */
    @SQL("SELECT co.* FROM  (SELECT * FROM composition_order WHERE order_status >=3 AND user_id =:userId) co,composition_reprot cr WHERE co.id =cr.`order_id` ORDER BY cr.create_time DESC limit 5")
    List<CompositionOrder> queryFiveNewOrders(@SQLParam("userId")Integer userId);

    /**
     * 查询作文的订单
     * @param compositionId
     * @return
     */
    @SQL("SELECT * FROM composition_order WHERE composition_id =:compositionId LIMIT 1")
    CompositionOrder queryCompositionOrderByCompositionId(@SQLParam("compositionId")Integer compositionId);

    /**
     * 主键查询作文订单
     * @param orderId
     * @return
     */
    @SQL(type=SQLType.READ_BY_ID)
    CompositionOrder queryCompositionOrderById(@SQLParam("")Integer orderId);

    /**
     * 查询某个作文订单的全部批改记录
     * @param orderId
     * @param compositionId
     * @return
     */
    @SQL("SELECT * FROM composition_correction_record WHERE composition_id =:compositionId AND order_id =:orderId ORDER BY pic_id ASC,pic_order ASC")
    List<CompositionCorrectionRecord> queryCompositionCorrectRecords(@SQLParam("orderId")Integer orderId,@SQLParam("compositionId")Integer compositionId);

    /**
     * 主键查询指标
     * @param itemId
     * @return
     */
    @SQL(type = SQLType.READ_BY_ID)
    RuleItem queryRuleItemById(Integer itemId);

    /**
     * 主见查询指标集对象
     * @param ruleId
     * @return
     */
    @SQL(type = SQLType.READ_BY_ID)
    Rule queryRuleByRuleId(Integer ruleId);

    /**
     * 根据规则集ID查询级别得分区间信息
     * @param ruleId
     * @return
     */
    @SQL("SELECT * FROM `rule_lev` WHERE rule_id =:ruleId")
    List<RuleLev> queryRuleLevByRuleId(@SQLParam("ruleId")Integer ruleId);

    /**
     * 查询五个没有批阅的作文
     * @param userId
     * @return
     */
    @SQL("SELECT * FROM composition_order WHERE user_id =:userId AND order_status = 2 ORDER BY create_time DESC LIMIT 1")
    List<CompositionOrder> queryFiveUnCorrectionOrder(@SQLParam("userId")Integer userId);

    /**
     * 查询指定状态的订单分页列表
     * @param listPager
     * @param userId
     * @param type
     * @return
     */
    @SQL("SELECT * FROM composition_order WHERE order_status =:type AND user_id =:userId ORDER BY create_time DESC ")
    List<CompositionOrder> queryCompositionOrderByUserIdAndTypeWithPager(@PageBy ListPager listPager,@SQLParam("userId")Integer userId,@SQLParam("type")Integer type);

    @SQL("SELECT co.* FROM  (SELECT * FROM composition_order WHERE order_status =3 AND user_id =:userId) co,composition_reprot cr WHERE co.id =cr.`order_id` ORDER BY cr.create_time DESC")
    List<CompositionOrder> queryCorrectCompositionOrderByUserId(@PageBy ListPager listPager,@SQLParam("userId")Integer userId);

    /**
     * 查询支付成功和批改成功的订单记录
     * @param listPager
     * @param userId
     * @return
     */
    @SQL("SELECT * FROM composition_order WHERE order_status >=2 AND user_id =:userId and order_type =2 ORDER BY create_time DESC ")
    List<CompositionOrder> queryPayedOrdersByUserIdWithPager(@PageBy ListPager listPager,@SQLParam("userId")Integer userId);

    /**
     * 更新订单的状态
     * @param orderId
     * @param status
     * @return
     */
    @SQL("UPDATE composition_order SET order_status =:status WHERE id =:orderId")
    int updateCompositionOrderStatus(@SQLParam("orderId")Integer orderId,@SQLParam("status")Integer status);

    /**
     * 查询某个老师的全部成交订单的总金额
     * @param teacherId
     * @return
     */
    @SQL("SELECT COUNT(price) FROM composition_order WHERE order_status =1 AND teacher_id =:teacherId")
    Double queryTeachersOrderCount(@SQLParam("teacherId")Integer teacherId);

    /**
     * 按照条件查询符合规则的评分规则集
     * @param area 规则适用地区
     * @param type 规则适用作文类型 记叙 议论
     * @param schoolLev 学段
     * @return
     */
    @SQL("SELECT * FROM rule WHERE school_lev =:schoolLev AND rule_type =:type")
    List<Rule> queryRuleListByCondition(@SQLParam("area")String area,@SQLParam("type")Integer type,@SQLParam("schoolLev")Integer schoolLev);

    /**
     * 按照条件查询符合规则的评分规则集
     * @param area 规则适用地区
     * @param type 规则适用作文类型 记叙 议论
     * @param schoolLev 学段
     * @return
     */
    @SQL("SELECT * FROM rule WHERE school_lev =:schoolLev")
    List<Rule> queryRuleListByCondition2(@SQLParam("area")String area,@SQLParam("type")Integer type,@SQLParam("schoolLev")Integer schoolLev);

    /**
     * 按照条件查询 符合教师的作文订单数据 分页
     * @param listPager 分页信息
     * @param keyWord 关键词
     * @param status 订单的状态
     * @param startDate 开始日期
     * @param endTime 结束日期
     * @return
     */
    @SQL(type = SQLType.READ)
    List<CompositionOrderDto> queryOrderListByCondition(@PageBy ListPager listPager,@SQLParam("keyWord")String keyWord,
                                                        @SQLParam("status")Integer status,@SQLParam("teacherId")Integer teacherId,
                                                        @SQLParam("startDate")String startDate,@SQLParam("endDate")String endTime);
    /**
     * 按照条件查询 符合教师的作文订单数据总数
     * @param keyWord 关键词
     * @param status 订单的状态
     * @param startDate 开始日期
     * @param endTime 结束日期
     * @return
     */
    @SQL(type = SQLType.READ)
    Integer queryOrderListByConditionCount(@SQLParam("keyWord")String keyWord,
                                                        @SQLParam("status")Integer status,@SQLParam("teacherId")Integer teacherId,
                                                        @SQLParam("startDate")String startDate,@SQLParam("endDate")String endTime);

    /**
     * 查询某个学生的学段
     * @param userId
     * @return
     */
    @SQL("SELECT DISTINCT range_id FROM classes WHERE id IN(SELECT classes_id FROM classes_student WHERE student_id =:userId AND STATUS =0 )")
    List<Integer> querySutdentXueDuanList(@SQLParam("userId")Integer userId);


    /**
     * 查询所有的新东方老师
     * @return
     */
    @SQL("SELECT * FROM composition_teacher")
    List<CompositionTeacher> queryXdfTeacherList();

    /**
     * 查询收费老师收费总额
     * SELECT COUNT(price) FROM composition_order WHERE teacher_id =1 AND order_status =3
     * @param teacherId 教师ID
     * @return
     */
    @SQL("SELECT sum(price) FROM composition_order WHERE teacher_id =:teacherId AND order_status =3")
    BigDecimal queryPayCountByTeacherId(@SQLParam("teacherId")Integer teacherId);

    /**
     * 查询xdf收费老师
     * @param teacherId 教师ID
     * @return
     */
    @SQL("SELECT * FROM composition_teacher WHERE id =:teacherId")
    CompositionTeacher queryXdfCompositionTeacherById(@SQLParam("teacherId")Integer teacherId);

    /**
     *  查询给定的作文订单的下一个作文订单
     *  上一个下一个 按照 已经支付 或者批阅的 作文当 时间倒序排列
     * @param teacherId 教师ID
     * @param orderId 订单ID
     * @return
     */
    @SQL("SELECT \n" +
            "  o.order_status STATUS,\n" +
            "  o.id,\n" +
            "  o.composition_id compositionId,\n" +
            "  t.`title` AS compositionTitle,\n" +
            "  t.`pic` \n" +
            "FROM composition_order o,composition t \n" +
            "WHERE o.`composition_id` = t.`id` AND o.`order_status` IN (2, 3) AND o.`teacher_id` =:teacherId AND UNIX_TIMESTAMP(o.create_time) <(SELECT UNIX_TIMESTAMP(create_time) FROM composition_order WHERE id =:orderId) ORDER BY o.create_time DESC LIMIT 1")
    CompositionOrderDto queryNextCompositionOrder(@SQLParam("teacherId")Integer teacherId,@SQLParam("orderId") Integer orderId);

    /**
     *  查询给定的作文订单的上一个作文订单
     *  上一个下一个 按照 已经支付 或者批阅的 作文当 时间倒序排列
     * @param teacherId 教师ID
     * @param orderId 订单ID
     * @return
     */
    @SQL("SELECT \n" +
            "  o.order_status STATUS,\n" +
            "  o.id,\n" +
            "  o.composition_id compositionId,\n" +
            "  t.`title` AS compositionTitle,\n" +
            "  t.`pic` \n" +
            "FROM composition_order o,composition t \n" +
            "WHERE o.`composition_id` = t.`id` AND o.`order_status` IN (2, 3) AND o.`teacher_id` =:teacherId AND UNIX_TIMESTAMP(o.create_time) >(SELECT UNIX_TIMESTAMP(create_time) FROM composition_order WHERE id =:orderId) ORDER BY o.create_time ASC LIMIT 1")
    CompositionOrderDto queryPreCompositionOrder(@SQLParam("teacherId")Integer teacherId,@SQLParam("orderId") Integer orderId);


    /**
     * 更新作文订单的是否查看过状态位
     * @param orderId
     * @return
     */
    @SQL("UPDATE `composition_order` SET view_flag =1 WHERE id=:orderId")
    int updateCompositionOrderViewFlag(@SQLParam("orderId")Integer orderId);

    /**
     * 查询指定老师未查看的带批改的作文总数
     * @param teacherId
     * @return
     */
    @SQL("SELECT \n" +
            "  COUNT(1) \n" +
            "FROM\n" +
            "  composition_order o \n" +
            "WHERE o.`order_status` IN (2, 3) \n" +
            "  AND o.`teacher_id` = :teacherId  AND o.`view_flag` = 0 ")
    int queryTeacherNewCompositionCount(@SQLParam("teacherId")Integer teacherId);

    /**
     * 查询指定老师 在某个学科教的班级总数
     * @param teacherId
     * @param subject
     * @return
     */
    @SQL("SELECT COUNT(1) FROM `classes_teacher` WHERE teacher_id =:teacherId AND subject_id =:subjectId AND STATUS =0")
    int queryTeacherCompositionClassCount(@SQLParam("teacherId")Integer teacherId,@SQLParam("subjectId")Integer subject);

    /**
     * 更新作文订单表的orderCode字段
     * @param orderCode
     * @param orderId
     */
    @SQL("UPDATE composition_order SET order_code =:orderCode WHERE id = :orderId")
    void updateCompositionOrderCodeByEclassInfo(@SQLParam("orderCode")String orderCode,@SQLParam("orderId")Integer orderId);

    /**
     * 更新默认作文评分规则
     * @param ruleId
     * @param id
     * @return
     */
    @SQL("UPDATE default_rule SET rule_id =:ruleId WHERE id =:id")
    int updateDefaultRuleId(@SQLParam("ruleId")Integer ruleId,@SQLParam("id")Integer id);

}
