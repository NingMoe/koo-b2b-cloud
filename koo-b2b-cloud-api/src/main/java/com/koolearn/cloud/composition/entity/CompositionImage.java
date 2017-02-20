package com.koolearn.cloud.composition.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "composition_image")
public class CompositionImage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 作文ID */
	@Column(name = "composition_id")
	private Integer compositionId;
	/** 图片地址 */
	@Column(name = "img_url")
	private String imgUrl;
	/** 图片的状态 */
	@Column(name = "status")
	private Integer status;
	/** 图片顺序 */
	@Column(name = "img_index")
	private Integer imgIndex;
	/** 是批改结果图片 2 还是作文图片 1 */
	@Column(name = "type")
	private Integer type;
	/** 如果是批改图片 则需要记录是那个订单批改的结果图片 */
	@Column(name = "order_id")
	private Integer orderId;
	/** 批次ID */
	@Column(name = "batch_id")
	private Integer batchId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCompositionId() {
		return compositionId;
	}

	public void setCompositionId(Integer compositionId) {
		this.compositionId = compositionId;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getImgIndex() {
		return imgIndex;
	}

	public void setImgIndex(Integer imgIndex) {
		this.imgIndex = imgIndex;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
}
