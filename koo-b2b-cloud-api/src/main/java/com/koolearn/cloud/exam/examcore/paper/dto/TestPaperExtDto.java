package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;

import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperExt;

public class TestPaperExtDto  implements Serializable {
	private TestPaperExt testPaperExt;

	public TestPaperExt getTestPaperExt() {
		return testPaperExt;
	}

	public void setTestPaperExt(TestPaperExt testPaperExt) {
		this.testPaperExt = testPaperExt;
	}

	public void zeroId(int paperId) {
		testPaperExt.setId(0);
		testPaperExt.setPaperId(paperId);
	}
	
}
