package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;

import com.koolearn.cloud.exam.examcore.paper.entity.PaperDetail;

public class PaperDetailDto implements Serializable {
	private PaperDetail paperDetail;

	public PaperDetail getPaperDetail() {
		return paperDetail;
	}

	public void setPaperDetail(PaperDetail paperDetail) {
		this.paperDetail = paperDetail;
	}

	public void zeroId() {
		paperDetail.setId(0);
		paperDetail.setPaperStructureId(0);
	}
	
}
