package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;

public class TestPaperStructureDto implements Serializable{
	private PaperDetailDto detailDto;
	private TestPaperStructure testPaperStructure;
	private List<PaperSubScore> subScores=new ArrayList<PaperSubScore>();
	private List<TestPaperStructureDto> children=new ArrayList<TestPaperStructureDto>();
	public PaperDetailDto getDetailDto() {
		return detailDto;
	}

	public void setDetailDto(PaperDetailDto detailDto) {
		this.detailDto = detailDto;
	}

	public TestPaperStructure getTestPaperStructure() {
		return testPaperStructure;
	}

	public void setTestPaperStructure(TestPaperStructure testPaperStructure) {
		this.testPaperStructure = testPaperStructure;
	}

	public List<TestPaperStructureDto> getChildren() {
		return children;
	}

	public void setChildren(List<TestPaperStructureDto> children) {
		this.children = children;
	}

	public List<TestPaperStructureDto> getStructuresItems() {
		List<TestPaperStructureDto> result=new ArrayList<TestPaperStructureDto>();
		result.add(this);
		for(int i=0;i<this.children.size();i++){
			result.addAll(children.get(i).getStructuresItems());
		}
		return result;
	}

	public void zeroIdPid(int paperId) {
		detailDto.zeroId();
		testPaperStructure.setId(0);
		testPaperStructure.setPaperId(paperId);
		testPaperStructure.setParent(0);
		for(TestPaperStructureDto dto:children){
			dto.zeroIdPid(paperId);
		}
	}

	public Double summaryScore() {
		Double score=0.0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		if(testPaperStructure.getStructureType()==TestPaperStructure.structure_type_structure){
			for(TestPaperStructureDto dto:children){
				score+=dto.summaryScore();
			}
			testPaperStructure.setPoints(Double.valueOf(nf.format(Double.valueOf(score.toString()))));
		}else{
			if(testPaperStructure.getPoints()==null){
				testPaperStructure.setPoints(0d);
			}
			score=Double.valueOf(nf.format(Double.valueOf(testPaperStructure.getPoints().toString())));
		}
		return Double.valueOf(nf.format(score));
	}

	public List<PaperSubScore> getSubScores() {
		return subScores;
	}

	public void setSubScores(List<PaperSubScore> subScores) {
		this.subScores = subScores;
	}
	
	
	public int takeQuestionCount(TestPaperStructureDto dto, int count)
	{
		if (dto.getTestPaperStructure().getStructureType() == TestPaperStructure.structure_type_question)
		{
			count ++;
		}
		else if (dto.getTestPaperStructure().getStructureType() == TestPaperStructure.structure_type_structure)
		{
			for (TestPaperStructureDto temp : dto.getChildren())
			{
				count = temp.takeQuestionCount(temp, count);
			}
		}
		return count;
	}
}
