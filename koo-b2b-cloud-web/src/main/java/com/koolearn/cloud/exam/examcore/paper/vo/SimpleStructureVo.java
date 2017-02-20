package com.koolearn.cloud.exam.examcore.paper.vo;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperDetailDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperStructureDto;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperDetail;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.util.KlbTagsUtil;

import java.util.*;


public class SimpleStructureVo {
	public SimpleStructureVo(){}
	public SimpleStructureVo(int structureId,int teId,int questionId,Double score){
		this.structureId=structureId;
		this.questionId=questionId;
		this.teId=teId;
		this.score=score;
	}
	public SimpleStructureVo(int structureId,int teId,int questionId){
		this.structureId=structureId;
		this.questionId=questionId;
		this.teId=teId;
	}
	/**
	 * 结构
	 */
	private int structureId;
	/**
	 * 试题
	 */
	private int questionId;
	private int teId;
	/**
	 * 分数
	 */
	private Double score;
	/**
	 * 子题
	 */
	private List<SimpleStructureVo> children=new ArrayList<SimpleStructureVo>();
	public int getStructureId() {
		return structureId;
	}
	public void setStructureId(int structureId) {
		this.structureId = structureId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	
	public int getTeId() {
		return teId;
	}
	public void setTeId(int teId) {
		this.teId = teId;
	}
	
	public List<SimpleStructureVo> getChildren() {
		return children;
	}
	public void setChildren(List<SimpleStructureVo> children) {
		this.children = children;
	}
	/**
	 * score可能是空的
	 * @param str structureId_questionId_score,structureId_questionId_score...
	 * @return
	 */
	public static Map<Integer,List<SimpleStructureVo>> parseString2Vo(String str,Map<Integer,String> idCode){
		Map<Integer,List<SimpleStructureVo>> result=new HashMap<Integer,List<SimpleStructureVo>>();
		int structureId=0;
		int questionId=0;
		int teId=0;
		SimpleStructureVo vo=null;
		List<SimpleStructureVo> last=null;
		
		String[] ss=str.split(",");
		String[] aa=null;
		for(int i=0;i<ss.length;i++){
			aa=ss[i].split("_");
			if(aa.length==3){
				structureId=Integer.parseInt(aa[0]);
				teId=Integer.parseInt(aa[1]);
				questionId=Integer.parseInt(aa[2]);
				vo=new SimpleStructureVo(structureId,teId,questionId);
			}else if(aa.length==4){
				structureId=Integer.parseInt(aa[0]);
				teId=Integer.parseInt(aa[1]);
				questionId=Integer.parseInt(aa[2]);
				double score=Double.parseDouble(aa[3]);
				vo=new SimpleStructureVo(structureId,teId,questionId,score);
			}
			if(teId!=0){//子题添加
                //有序：封装每个大题的子题
				last.get(last.size()-1).getChildren().add(vo);
				continue;
			}
			if(!result.containsKey(structureId)){
				result.put(structureId,new ArrayList<SimpleStructureVo>());
			}
			idCode.put(questionId, null);//只封装大题id
			result.get(structureId).add(vo);//封装每个结构下的只有大题
			last=result.get(structureId);//记录本次要封装的大题结构
		}
		
		return result;
	}
	/**
	 * 填充为可用的试卷结构
	 * @param dto
	 * @param vos
	 * @param idCode 
	 */
	public static void fill2Paper(TestPaperDto dto,Map<Integer, List<SimpleStructureVo>> vos, Map<Integer, String> idCode) {
       Set<Integer> keySet=vos.keySet();
        Iterator<Integer>   ki=keySet.iterator();
        while (ki.hasNext()){
            Integer questionTypeId=ki.next();
            TestPaperStructureDto tpsDto=new TestPaperStructureDto();
            TestPaperStructure tps=new TestPaperStructure();
            tps.setId(questionTypeId);
            if(KlbTagsUtil.getInstance().getTag(questionTypeId)==null){
                tps.setName("题型id不存在!:"+questionTypeId);
            }else {
                tps.setName(KlbTagsUtil.getInstance().getTag(questionTypeId).getName());
            }
            tps.setStructureType(TestPaperStructure.structure_type_structure);
            tpsDto.setTestPaperStructure(tps);
            PaperDetailDto detailDto= new PaperDetailDto();PaperDetail paperDetail=new PaperDetail();
            paperDetail.setQuestionCode(tps.getName());
            detailDto.setPaperDetail(paperDetail);
            tpsDto.setDetailDto(detailDto);
            dto.getStructures().add(tpsDto);
        }
		List<TestPaperStructureDto> list=dto.getStructures();
		TestPaperStructureDto structureDto=null;
		TestPaperStructure testPaperStructure=null;
		list=removeQuestion(list);
		for(int i=0,size=list.size();i<size;i++){
			structureDto=list.get(i);
			testPaperStructure=structureDto.getTestPaperStructure();
			if(vos.containsKey(testPaperStructure.getId())){
				structureDto.setChildren(fillstructureDtos(vos.get(testPaperStructure.getId()),idCode));
			}
		}
		conver2Tree(list);
		dto.setStructures(list);
		dto.setTree(true);
		
	}
	/**
	 * 转换为tree结构,用于保存
	 * @param list
	 */
	private static void conver2Tree(List<TestPaperStructureDto> list) {
		Map<Integer,List<TestPaperStructureDto>> pidList=new HashMap<Integer,List<TestPaperStructureDto>>();
		Map<Integer,TestPaperStructureDto> idObj=new HashMap<Integer,TestPaperStructureDto>();
		TestPaperStructureDto dto=null;
		int pid=0,id;
		for(int i=0,size=list.size();i<size;i++){
			dto=list.get(i);
			pid=dto.getTestPaperStructure().getParent();
//			id=dto.getTestPaperStructure().getId();
			if(!pidList.containsKey(pid)){
				pidList.put(pid, new ArrayList<TestPaperStructureDto>());
			}
			pidList.get(pid).add(dto);
		}
		for(TestPaperStructureDto dto2:list){
			if(pidList.containsKey(dto2.getTestPaperStructure().getId())){
				dto2.setChildren(pidList.get(dto2.getTestPaperStructure().getId()));
			}
		}
		Iterator<TestPaperStructureDto> iterator=list.iterator();
		while(iterator.hasNext()){
			dto=iterator.next();
			if(dto.getTestPaperStructure().getParent()!=0){
				iterator.remove();
			}
		}
	}
	private static List<TestPaperStructureDto> fillstructureDtos(List<SimpleStructureVo> list, Map<Integer, String> idCode) {
		List<TestPaperStructureDto> result=new ArrayList<TestPaperStructureDto>();
		TestPaperStructureDto dto=null;
		TestPaperStructure testPaperStructure=null;
		PaperDetail paperDetail=null;
		SimpleStructureVo vo=null;
		String code=null;
		for(int i=0,size=list.size();i<size;i++){
			vo=list.get(i);
			paperDetail=new PaperDetail();
		 testPaperStructure=new TestPaperStructure();
		 code=idCode.get(vo.getQuestionId());
		 testPaperStructure.setName(code);
		 testPaperStructure.setOdr(i);
		 testPaperStructure.setStructureType(TestPaperStructure.structure_type_question);
		 if(vo.getScore()!=null){
			 testPaperStructure.setPoints(vo.getScore());
			 paperDetail.setPoints(vo.getScore());
		 }
		 paperDetail.setQuestionCode(code);
		 
		 dto=new TestPaperStructureDto();
		 dto.setDetailDto(new PaperDetailDto());
		 dto.getDetailDto().setPaperDetail(paperDetail);
		 dto.setTestPaperStructure(testPaperStructure);
		 
		 addSubScore(dto,vo);
		 result.add(dto);
		}
		return result;
	}
	private static void addSubScore(TestPaperStructureDto dto,SimpleStructureVo vo) {
		if(vo.getChildren().size()==0){
			return;
		}
		SimpleStructureVo vo2=null;
		PaperSubScore subScore=null;
		double total=0;
		boolean allZero=true;
		for(int i=0,size=vo.getChildren().size();i<size;i++){
			vo2=vo.getChildren().get(i);
			subScore=new PaperSubScore();
			subScore.setPoints(vo2.getScore());
			if(subScore.getPoints()>=1){
				allZero=false;
			}
			total+=subScore.getPoints();
			dto.getSubScores().add(subScore);
		}
		//设置分数
		if(allZero){
			double avg=dto.getTestPaperStructure().getPoints()/vo.getChildren().size();
			for(PaperSubScore subScore2:dto.getSubScores()){
				subScore2.setPoints(avg);
			}
		}else{
			dto.getTestPaperStructure().setPoints(total);
		}
		
	}
	private static List<TestPaperStructureDto> removeQuestion(List<TestPaperStructureDto> list) {
		Iterator<TestPaperStructureDto> iterator=list.iterator();
		TestPaperStructureDto dto=null;
		while(iterator.hasNext()){
			dto=iterator.next();
			if(dto.getTestPaperStructure().getStructureType()==TestPaperStructure.structure_type_question){
				iterator.remove();
			}else{
				removeQuestion(dto.getChildren());
			}
		}
		return list;
	}
}
