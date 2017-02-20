package com.koolearn.cloud.exam.examcore.paper.dto;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperExt;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.login.entity.UserEntity;

public class TestPaperDto implements Serializable{
	private TestPaper paper;
	private List<TestPaperStructureDto> structures=new ArrayList<TestPaperStructureDto>();
	private TestPaperExtDto  testPaperExtDto;
	private int questionCount;
	private boolean isTree=true;
	
	/**
	 * 非结构元素,用于存储报错信息,正常使用直接忽略该字段
	 */
	private String msg;
	public TestPaper getPaper() {
		return paper;
	}
	public void setPaper(TestPaper paper) {
		this.paper = paper;
	}
	public List<TestPaperStructureDto> getStructures() {
		return structures;
	}
	public void setStructures(List<TestPaperStructureDto> structures) {
		this.structures = structures;
	}
	public TestPaperExtDto getTestPaperExtDto() {
		return testPaperExtDto;
	}
	public void setTestPaperExtDto(TestPaperExtDto testPaperExtDto) {
		this.testPaperExtDto = testPaperExtDto;
	}
	
	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	public void SetLoginUser(UserEntity loginUser){
		if(testPaperExtDto==null){
			return ;
		}
		TestPaperExt testPaperExt=testPaperExtDto.getTestPaperExt();
		testPaperExt.setSchoolId(loginUser.getSchoolId());
		testPaperExt.setTeacherId(loginUser.getId());
		paper.setUpdateTime(new Date());
		paper.setCreateTime(new Date());
	}
	public List<TestPaperStructureDto> getStructuresTree(){
		if(isTree){
			return this.structures;
		}
		List<TestPaperStructureDto> result=new ArrayList<TestPaperStructureDto>();
		
		for (TestPaperStructureDto temp : this.structures)
		{
			if (temp.getTestPaperStructure().getParent() == 0)
			{
				result.add(this.handlerTree(temp));
			}
		}
		return result;
	}
	
	private TestPaperStructureDto handlerTree(TestPaperStructureDto dto)
	{
		dto.setChildren(new ArrayList<TestPaperStructureDto>(0));
		for (TestPaperStructureDto temp : this.structures)
		{
			if (temp.getTestPaperStructure().getParent() == dto.getTestPaperStructure().getId())
			{
				dto.getChildren().add(this.handlerTree(temp));
			}
		}
		return dto;
	}
	
	/**
	 * 流水list结构
	 * @return
	 */
	public List<TestPaperStructureDto> getStructuresItems(){
		if(!isTree()){
			return structures;
		}
		List<TestPaperStructureDto> result=new ArrayList<TestPaperStructureDto>();
		for(int i=0,size=structures.size();i<size;i++){
			result.addAll(structures.get(i).getStructuresItems());
		}
		return result;
	}
	public void zeroIdPid(int paperId){
		paper.setId(paperId);
//		if(relaPaperTypeDto!=null){
//			relaPaperTypeDto.zeroId(paperId);
//		}
//		if(testPaperExtDto!=null){
//			testPaperExtDto.zeroId(paperId);
//		}
		for(TestPaperStructureDto dto:structures){
			dto.zeroIdPid(paperId);	
		}
		
	}
	public void summaryScore(){
		Double score=0.0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		for(TestPaperStructureDto dto:structures){
			score+=dto.summaryScore();
		}
		paper.setPoints(Double.valueOf(nf.format(score)));
	}
	/**
	 * 生成存在的 题目id和结构的map集合,如果没再idCodeMap中的code,不放入结果中
	 * @param idCodeMap
	 * @return
	 */
	public Map<String, TestPaperStructure> genExistsIdStrunctor(
			Map<Integer, String> idCodeMap) {
		TestPaperStructure structure=null;
		Map<String, TestPaperStructure> result=new HashMap<String, TestPaperStructure>();
		for(int i=0,size=structures.size();i<size;i++){
			structure=structures.get(i).getTestPaperStructure();
			if(structure.getStructureType()==TestPaperStructure.structure_type_question){
				if(idCodeMap.containsValue(structure.getName())){
					result.put(structure.getName(), structure);
				}
			}
		}
		return result;
	}
	/**
	 * @param qCode_stru 将存在的数据填充到对应的新结构中,目前是分数和时间限制
	 */
	public void fillScoreTime(Map<String, TestPaperStructure> qCode_stru) {
		TestPaperStructure structure=null;
		TestPaperStructure structure2=null;
		for(TestPaperStructureDto dto:structures){
			structure=dto.getTestPaperStructure();
			if(structure.getStructureType()==TestPaperStructure.structure_type_question){
				if(qCode_stru.containsKey(structure.getName())){
					structure.setPoints(qCode_stru.get(structure.getName()).getPoints());
					structure.setTimeout(qCode_stru.get(structure.getName()).getTimeout());
				}
			}
		}
		
	}
	public boolean isTree() {
		return isTree;
	}
	public void setTree(boolean isTree) {
		this.isTree = isTree;
	}
	
	/**
	 * 获取试卷中试题的数量
	 * @return
	 * @author DuHongLin
	 */
	public int takeQuestionCount()
	{
		this.questionCount = 0;
		this.handlerStructures(this.getStructuresItems());
		return this.questionCount;
	}
	
	/**
	 * 获取所有的题目Code
	 * @return
	 * @author DuHongLin
	 */
	public Set<String> takeQcodes()
	{
		if (this.qcodes == null || this.qcodes.size() <= 0)
		{
			this.handlerStructures(this.getStructuresItems());
		}
		return this.qcodes;
	}
	
	private Set<String> qcodes = null;
	
	/**
	 * 处理题目数量和抽取题目Code
	 * @param testPaperStructureDtos
	 * @author DuHongLin
	 */
	private void handlerStructures(List<TestPaperStructureDto> testPaperStructureDtos)
	{
		this.qcodes = new HashSet<String>(0);
		for (TestPaperStructureDto paperStructureDto : testPaperStructureDtos)
		{
			if (paperStructureDto.getTestPaperStructure().getStructureType() == 1)
			{
				this.questionCount++;
				this.qcodes.add(paperStructureDto.getTestPaperStructure().getName());
			}
		}
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
