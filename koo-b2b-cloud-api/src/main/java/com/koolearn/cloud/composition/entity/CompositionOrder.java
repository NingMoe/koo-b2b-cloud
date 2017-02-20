package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "composition_order")
public class CompositionOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 订单创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 订单归属教师ID */
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** 作文ID */
	@Column(name = "composition_id")
	private Integer compositionId;
	/** 作文ID */
	@Column(name = "user_id")
	private Integer userId;
	/** 批改订单金额 */
	@Column(name = "price")
	private BigDecimal price;
	/** 订单状态 */
	@Column(name = "order_status")
	private Integer orderStatus;
	/** 作文类型 */
	@Column(name = "order_type")
	private Integer orderType;
	/** 学段 */
	@Column(name = "school_lev")
	private Integer schoolLev;
	/** 适用地区 */
	@Column(name = "order_area")
	private String orderArea;
	/** 订单编号 业务规则 */
	@Column(name = "order_code")
	private String orderCode;
	/** 支付中心订单号 */
	@Column(name = "pay_center_order_Id")
	private String payCenterOrderId;
	/** 支付方式 */
	@Column(name = "pay_type")
	private String payType;
	/** 是否查看 */
	@Column(name = "view_flag")
	private Integer viewFlag;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getCompositionId() {
		return compositionId;
	}

	public void setCompositionId(Integer compositionId) {
		this.compositionId = compositionId;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public Integer getSchoolLev() {
		return schoolLev;
	}

	public void setSchoolLev(Integer schoolLev) {
		this.schoolLev = schoolLev;
	}
	
	public String getOrderArea() {
		return orderArea;
	}

	public void setOrderArea(String orderArea) {
		this.orderArea = orderArea;
	}
	
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	public String getPayCenterOrderId() {
		return payCenterOrderId;
	}

	public void setPayCenterOrderId(String payCenterOrderId) {
		this.payCenterOrderId = payCenterOrderId;
	}
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public Integer getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(Integer viewFlag) {
		this.viewFlag = viewFlag;
	}
}
