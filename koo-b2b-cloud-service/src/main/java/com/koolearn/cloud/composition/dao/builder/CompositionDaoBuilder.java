package com.koolearn.cloud.composition.dao.builder;

import com.koolearn.framework.aries.provider.sqlbuilder.AriesDynamicSqlBuilder;
import com.koolearn.framework.common.page.ListPager;

/**
 * Created by haozipu on 2016/7/18.
 */
public class CompositionDaoBuilder  implements AriesDynamicSqlBuilder {


    public String queryOrderListByCondition(ListPager listPager,String keyWord,Integer status,Integer teacherId,String startDate,String endDate){

        StringBuilder stringBuilder = new StringBuilder("SELECT " +
                "   o.id," +
                "  o.create_time createTime," +
                "  o.teacher_id teacherId," +
                "  composition_id compositionId," +
                "  o.user_id userId," +
                "  o.price," +
                "  o.order_status status," +
                "  o.order_type type," +
                "  o.school_lev schoolLev," +
                "  o.order_area area," +
                "  o.order_code," +
                "  o.pay_center_order_Id payCenterOrderId," +
                "  o.pay_type payType," +
                "  o.view_flag viewFlag," +
                "  t.`title` as compositionTitle," +
                "  t.`pic`" +
                "FROM" +
                "  composition_order o," +
                "  composition t " +
                "WHERE o.`composition_id` = t.`id`");

        if(keyWord!=null&&!"".equals(keyWord)){
            stringBuilder.append(" and  t.title LIKE '%").append(keyWord).append("%'");
        }
        if(status!=null&&status!=-1){
            stringBuilder.append(" AND o.`order_status` =").append(status);
        }else {
            stringBuilder.append(" AND o.`order_status` in (2,3)");
        }

        if(teacherId!=null){
            stringBuilder.append(" and o.`teacher_id` =").append(teacherId);
        }

        if(startDate!=null&&!"".equals(startDate)){
            stringBuilder.append(" AND DATE_FORMAT(o.create_time, '%Y-%m-%d') >='").append(startDate).append("'");
        }
        if(endDate!=null&&!"".equals(endDate)){
            stringBuilder.append(" and DATE_FORMAT(o.create_time, '%Y-%m-%d') <='").append(endDate).append("'");
        }

        stringBuilder.append(" order by o.create_time desc");

        return stringBuilder.toString();

    }


    public String queryOrderListByConditionCount(String keyWord,Integer status,Integer teacherId,String startDate,String endDate){
        StringBuilder stringBuilder = new StringBuilder("SELECT " +
                "count(1) " +
                "FROM" +
                "  composition_order o," +
                "  composition t " +
                "WHERE o.`composition_id` = t.`id`");

        if(keyWord!=null&&!"".equals(keyWord)){
            stringBuilder.append(" and  t.title LIKE '%").append(keyWord).append("%'");
        }
        if(status!=null&&status!=-1){
            stringBuilder.append(" AND o.`order_status` =").append(status);
        }else {
            stringBuilder.append(" AND o.`order_status` in (2,3)");
        }

        if(teacherId!=null){
            stringBuilder.append(" and o.`teacher_id` =").append(teacherId);
        }

        if(startDate!=null&&!"".equals(startDate)){
            stringBuilder.append(" AND DATE_FORMAT(o.create_time, '%Y-%m-%d') >='").append(startDate).append("'");
        }
        if(endDate!=null&&!"".equals(endDate)){
            stringBuilder.append(" and DATE_FORMAT(o.create_time, '%Y-%m-%d') <='").append(endDate).append("'");
        }

        stringBuilder.append(" order by o.create_time desc");

        return stringBuilder.toString();
    }


}
