package com.koolearn.cloud.mobi.service;

import com.koolearn.cloud.composition.dto.CompositionOrderDto;
import com.koolearn.cloud.mobi.dto.*;
import java.util.List;

/**
 * 作文批改 的学生端手机hessian接口
 * 统一返回json的字符串
 * Created by haozipu on 2016/7/18.
 */
public interface StudentCompositionHessianService {

    /**
     * 创建一个作文记录
     * @param compostionTitle 作文标题
     * @param type 作文类型 议论文 记叙文
     * @param schoolLev 作文对应的学段 高中 初中
     * @param imgListInfo 图片url-顺序,图片url-顺序 逗号分隔
     * @return
     */
    public CompositionResult makeComposition(String compostionTitle,Integer userId,Integer type,Integer schoolLev,String imgListInfo);

    /**
     * 下一个订单
     * @param teacherId 提交给教师
     * @param userId 提交用户
     * @param compositionId 作文ID
     * @param payType 支付类型 支付宝 微信
     * @return
     */
    public CompositionOrderResult makeCompositionOrder(Integer teacherId,Integer userId,Integer compositionId,String payType,Integer teacherType);

    /**
     * 返回用户的基本信息
     * @param userId 用户的ID
     * @return
     */
    public UserInfo queryUserInfo(Integer userId);

    /**
     * 查询用户的消费记录
     * @param userId 用户的ID
     * @param size 分页大小
     * @param pageNo 页码
     * @return
     */
    public List<PurchaseHistory> queryUserPayList(Integer userId,Integer size,Integer pageNo);

    /**
     * 查询用户最新的五条作文记录
     * @param userId 用户ID
     * @return
     */
    public List<CompositionOrderDto> queryNewFiveCompositionRecord(Integer userId);

    /**
     * 查询作文详情
     * @param compositionId 作文ID
     * @return
     */
    public CompositionDetail queryCompostionDetail(Integer compositionId,Integer userId);

    /**
     * 查询用户的历史作文记录
     * @param userId 用户ID
     * @param type 作文状态 已批阅 未批阅
     * @param size 分页大小
     * @param pageNo 页码
     * @return
     */
    public List<CompositionOrderDto> queryCompositionHistory(Integer userId,int type,Integer size,Integer pageNo);

    /**
     * 查询学生的可见老师列表
     * @param studentId 学生的ID
     * @return
     */
    public  List<TeacherInfo> queryTeacherList(Integer studentId);

    /**
     * 查询订单支付状态
     * @param orderNo
     * @return
     */
    public OrderStatusResult queryOrderStatus(Integer orderNo);



}
