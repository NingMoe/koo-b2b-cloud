package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "composition_correction_record")
public class CompositionCorrectionRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 订单ID */
	@Column(name = "order_id")
	private Integer orderId;
	/** 作文ID */
	@Column(name = "composition_id")
	private Integer compositionId;
	/** 批改类型 错字 病句等 */
	@Column(name = "type")
	private Integer type;
	/** 批注 */
	@Column(name = "remark")
	private String remark;
	/** 对应图片坐标 {x:10,y:10} */
	@Column(name = "pic_index")
	private String picIndex;
	/** 批改记录所在图片ID */
	@Column(name = "pic_id")
	private Integer picId;
	/** 顺序 */
	@Column(name = "pic_order")
	private Integer picOrder;
	@Column(name = "create_time")
	private Date createTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getCompositionId() {
		return compositionId;
	}

	public void setCompositionId(Integer compositionId) {
		this.compositionId = compositionId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getPicIndex() {
		return picIndex;
	}

	public void setPicIndex(String picIndex) {
		this.picIndex = picIndex;
	}
	
	public Integer getPicId() {
		return picId;
	}

	public void setPicId(Integer picId) {
		this.picId = picId;
	}
	
	public Integer getPicOrder() {
		return picOrder;
	}

	public void setPicOrder(Integer picOrder) {
		this.picOrder = picOrder;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
