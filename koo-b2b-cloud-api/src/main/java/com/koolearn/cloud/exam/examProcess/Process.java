package com.koolearn.cloud.exam.examProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.vo.ResultDetailVO;
import com.koolearn.cloud.exam.examProcess.vo.ResultStructureVO;
import com.koolearn.cloud.exam.examProcess.vo.ResultVO;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;
import com.koolearn.cloud.exam.examcore.paper.dto.TestPaperDto;
import com.koolearn.cloud.exam.examcore.paper.entity.PaperSubScore;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.login.entity.UserEntity;

/**
 * @author DuHongLin 考试过程传递对象
 */
public class Process implements Serializable
{

	private static final long serialVersionUID = -7890804461595751810L;

	private TpExam exam; // 考试信息对象
	private TpExamResult examResult; // 考试结果对象
	private List<TpExamResultDetail> examResultDetails = new ArrayList<TpExamResultDetail>(0); // 考试结果明细集合
	private List<TpExamResultStructure> examResultStructures = new ArrayList<TpExamResultStructure>(0); // 考试结果结构集合
	private Map<String, IExamQuestionDto> mqcs = new HashMap<String, IExamQuestionDto>(0); // mqcs ==> Map Question Codes
	private Map<String, IExamQuestionDto> mqis = new HashMap<String, IExamQuestionDto>(0); // mqcs ==> Map Question IDs
	private Map<String, Object> resultMap = new HashMap<String, Object>(0); // 考试分类型处理结果数据结果

	private int studentId; // 学生ID

	private TestPaperDto testPaperDto; // 试卷信息DTO
	private UserEntity student;// 学生信息
	private Map<String, PaperSubScore> subScores = new HashMap<String, PaperSubScore>(0); // 试卷中所有子题分数
	private List<TpExamResultStructure> group; // 排序后的树形结构集合
	private List<Integer> qids; // 试卷中所有大题ID

	private Map<String, TpExamResultDetail> qs; // 所有题目明细

	private Map<String, String> dseq; // 明细的序列
	private int seq = 1; // 明细序列初始值

	private List<String> qcodes; // 试卷中所有大题Code

	private Map<String, TpExamResultDetail> qcs; // 所有明细key==》question code

	private UserEntity loginUser; // 当前登录的用户
	
	private int[] num;// 储存答题卡序列数，错题数和未答数

	/**
	 * 获取所有【大题】题目整体排序对应的序列，key==》题目ID，value==》序列号
	 * @return
	 * @author DuHongLin
	 */
	public Map<String, String> getDseq()
	{
		if (this.dseq == null || this.dseq.size() <= 0)
		{
			this.handlerShowQuestions();
		}
		return this.dseq;
	}

	/**
	 * 获取考试信息
	 * @return
	 * @author DuHongLin
	 */
	public TpExam getExam()
	{
		return this.exam;
	}

	/**
	 * 获取考试结果明细
	 * @return
	 * @author DuHongLin
	 */
	public TpExamResult getExamResult()
	{
		return this.examResult;
	}
	
	/**
	 * 获取考试结果明细按ID升序排列后的集合
	 * @return
	 * @author DuHongLin
	 */
	public List<TpExamResultDetail> getExamResultDetails()
	{
		this.sortAscResultDetail();
		return this.examResultDetails;
	}
	
	/**
	 * 获取考试结果结构按ID升序排列后的集合
	 * @return
	 * @author DuHongLin
	 */
	public List<TpExamResultStructure> getExamResultStructures()
	{
		this.sortAscResultStructure();
		return this.examResultStructures;
	}

	/**
	 * 获取树形结构
	 * @return
	 * @author DuHongLin
	 */
	public List<TpExamResultStructure> getGroup()
	{
		if (null == this.group || this.group.size() <= 0)
		{
			this.handlerShowQuestions();
		}
		return this.group;
	}

	/** 获取已登录的用户 */
	public UserEntity getLoginUser()
	{
		return loginUser;
	}
	
	public Map<String, IExamQuestionDto> getMqcs()
	{
		return this.mqcs;
	}

	public Map<String, IExamQuestionDto> getMqis()
	{
		return mqis;
	}

	public int[] getNum()
	{
		return num;
	}

	/**
	 * 获取考试明细中所有大题code的集合
	 * @return
	 * @author DuHongLin
	 */
	public List<String> getQcodes()
	{
		if (this.qcodes == null || this.qcodes.size() <= 0)
		{
			this.handlerShowQuestions();
		}
		return this.qcodes;
	}

	/**
	 * 获取考试明细Map，，key==》题目code
	 * @return
	 * @author DuHongLin
	 */
	public Map<String, TpExamResultDetail> getQcs()
	{
		if (null == this.qcs)
		{
			this.handlerShowQuestions();
		}
		return this.qcs;
	}

	/**
	 * 获取所有大题ID
	 * @return
	 * @author DuHongLin
	 */
	public List<Integer> getQids()
	{
		if (null == this.qids || this.qids.size() <= 0)
		{
			this.handlerShowQuestions();
		}
		return this.qids;
	}

	/**
	 * 获取所有明细，KEY==》题目ID，value==》明细对象
	 * @return
	 * @author DuHongLin
	 */
	public Map<String, TpExamResultDetail> getQs()
	{
		if (this.qs == null || this.qs.size() <= 0)
		{
			this.handlerShowQuestions();
		}
		return this.qs;
	}

	public Map<String, Object> getResultMap()
	{
		return this.resultMap;
	}

	public UserEntity getStudent()
	{
		return this.student;
	}

	public int getStudentId()
	{
		return this.studentId;
	}

	public Map<String, PaperSubScore> getSubScores()
	{
		return subScores;
	}

	public TestPaperDto getTestPaperDto()
	{
		return this.testPaperDto;
	}

	/** 对排序结果进行树形结构组织 */
	private void groupResultStructure() // 对排序结果进行树形结构组织
	{
		this.group = new ArrayList<TpExamResultStructure>(0);
		this.qids = new ArrayList<Integer>(0);
		this.qs = new HashMap<String, TpExamResultDetail>(0);
		this.dseq = new HashMap<String, String>(0);
		this.seq = 1;
		this.qcodes = new ArrayList<String>(0);
		this.qcs = new HashMap<String, TpExamResultDetail>(0);
		this.sortAscResultDetail();
		for (TpExamResultStructure parent : this.examResultStructures)
		{
			if (parent.getParent() == 0)
			{
				parent.setQuestions(this.takeQuestions(parent));
				parent.setChildren(this.takeResultStructureChildren(parent, this.examResultStructures));
				this.group.add(parent);
			}
		}
	}

	/** 处理明细VO */
	private List<ResultDetailVO> handlerDVO(List<TpExamResultDetail> details)
	{
		List<ResultDetailVO> result = null;
		if (null != details && details.size() > 0)
		{
			result = new ArrayList<ResultDetailVO>(0);
			ResultDetailVO detailVO = null;
			for (TpExamResultDetail detail : details)
			{
				detailVO = new ResultDetailVO();
				detailVO.setDetail(detail);
				detailVO.setSubs(this.handlerDVO(detail.getSubDetails()));
				result.add(detailVO);
			}
		}
		return result;
	}

	/** 处理页面题目的展现 */
	private void handlerShowQuestions() // 处理页面题目的展现
	{
		this.sortAscResultStructure();
		this.groupResultStructure();
	}

	/** 处理结构VO */
	private List<ResultStructureVO> handlerSVO(List<TpExamResultStructure> structureTree)
	{
		List<ResultStructureVO> result = null;
		if (null != structureTree && structureTree.size() > 0)
		{
			result = new ArrayList<ResultStructureVO>(0);
			ResultStructureVO structureVO = null;
			for (TpExamResultStructure structure : structureTree)
			{
				structureVO = new ResultStructureVO();
				structureVO.setResultStructure(structure);
				structureVO.setSubs(this.handlerSVO(structure.getChildren()));
				structureVO.setDetails(this.handlerDVO(structure.getQuestions()));
				result.add(structureVO);
			}
		}
		return result;
	}

	public void setDseq(Map<String, String> dseq)
	{
		this.dseq = dseq;
	}

	public void setExam(TpExam exam)
	{
		this.exam = exam;
	}
	public void setExamResult(TpExamResult examResult)
	{
		this.examResult = examResult;
	}
	public void setExamResultDetails(List<TpExamResultDetail> examResultDetails)
	{
		this.examResultDetails = examResultDetails;
	}
	public void setExamResultStructures(List<TpExamResultStructure> examResultStructures)
	{
		this.examResultStructures = examResultStructures;
	}
	public void setGroup(List<TpExamResultStructure> group)
	{
		this.group = group;
	}
	public void setLoginUser(UserEntity loginUser)
	{
		this.loginUser = loginUser;
	}
	
	public void setMqcs(Map<String, IExamQuestionDto> mqcs)
	{
		this.mqcs = mqcs;
	}

	public void setMqis(Map<String, IExamQuestionDto> mqis)
	{
		this.mqis = mqis;
	}

	public void setNum(int[] num)
	{
		this.num = num;
	}

	public void setQcodes(List<String> qcodes)
	{
		this.qcodes = qcodes;
	}

	public void setQcs(Map<String, TpExamResultDetail> qcs)
	{
		this.qcs = qcs;
	}

	public void setQids(List<Integer> qids)
	{
		this.qids = qids;
	}

	public void setQs(Map<String, TpExamResultDetail> qs)
	{
		this.qs = qs;
	}

	public void setResultMap(Map<String, Object> resultMap)
	{
		this.resultMap = resultMap;
	}

	public void setStudent(UserEntity student)
	{
		this.student = student;
	}
	
	public void setStudentId(int studentId)
	{
		this.studentId = studentId;
	}
	public void setSubScores(Map<String, PaperSubScore> subScores)
	{
		this.subScores = subScores;
	}
	public void setTestPaperDto(TestPaperDto testPaperDto)
	{
		this.testPaperDto = testPaperDto;
	}
	/** 对明细按照ID升序排列 */
	public void sortAscResultDetail() // 对明细按照ID升序排列
	{
		for (int i = 0; i < this.examResultDetails.size(); i++)
		{
			for (int j = i; j < this.examResultDetails.size(); j++)
			{
				if (this.examResultDetails.get(i).getId() > this.examResultDetails.get(j).getId())
				{
                    TpExamResultDetail temp = this.examResultDetails.get(i);
					this.examResultDetails.set(i, this.examResultDetails.get(j));
					this.examResultDetails.set(j, temp);
				}
			}
		}
	}
	/** 对结果结构按照ID升序排列 */
	private void sortAscResultStructure() // 对结果结构进行升序排列
	{
		for (int i = 0; i < this.examResultStructures.size(); i++)
		{
			for (int j = i; j < this.examResultStructures.size(); j++)
			{
				if (this.examResultStructures.get(i).getId() > this.examResultStructures.get(j).getId())
				{
                    TpExamResultStructure temp = this.examResultStructures.get(i);
					this.examResultStructures.set(i, this.examResultStructures.get(j));
					this.examResultStructures.set(j, temp);
				}
			}
		}
	}
	/** 获取错题数和未答题数 */
	public int[] takeCount()
	{
		int err = 0;
		int nop = 0;
		for (TpExamResultDetail detail : this.examResultDetails)
		{
			if (detail.getTeId() == 0)
			{
				if (detail.getResultAnswer() == -1)
				{
					nop ++;
				}
				else if (detail.getResultAnswer() == 0)
				{
					err ++;
				}
			}
		}
		return new int[]{err, nop};
	}
	
	/** 获取结构下的大题题目 */
	private List<TpExamResultDetail> takeQuestions(TpExamResultStructure structure) // 获取结构下的题目
	{
		List<TpExamResultDetail> result = new ArrayList<TpExamResultDetail>(0);
		for (TpExamResultDetail detail : this.examResultDetails)
		{
//			System.out.println(" structure.getId()="+ structure.getId() + "  detail.getExamResultStructure()="+detail.getExamResultStructure() + "  "+(structure.getId() == detail.getExamResultStructure()));
			if (structure.getId().intValue() == detail.getExamResultStructure().intValue())
			{
				this.qs.put(String.valueOf(detail.getQuestionId()), detail);
				this.qcs.put(detail.getQuestionCode(), detail);
				if (detail.getTeId() == 0)
				{
					result.add(detail);
					this.qids.add(Integer.valueOf(detail.getQuestionId()));
					this.qcodes.add(detail.getQuestionCode());
					this.dseq.put(String.valueOf(detail.getQuestionId()), String.valueOf(this.seq++));
				}
			}
		}
		return result;
	}
	/** 获取子结构 */
	private List<TpExamResultStructure> takeResultStructureChildren(TpExamResultStructure parent, List<TpExamResultStructure> opt) // 获取子结构
	{
		List<TpExamResultStructure> children = new ArrayList<TpExamResultStructure>(0);
		for (TpExamResultStructure child : opt)
		{
//			System.out.println("parent.getId()="+parent.getId()+"   child.getParent()="+child.getParent()+"  "+(parent.getId() == child.getParent()));
			if (parent.getId().intValue() == child.getParent().intValue())
			{
				child.setQuestions(this.takeQuestions(child));
				child.setChildren(this.takeResultStructureChildren(child, opt));
				children.add(child);
			}
		}
		return children;
	}
	/** 获取树形考试结果 */
	public ResultVO takeResultTree()
	{
		List<TpExamResultStructure> structureTree = this.getGroup();
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(this.examResult);
		resultVO.setStructures(this.handlerSVO(structureTree));
		return resultVO;
	}
	
}
