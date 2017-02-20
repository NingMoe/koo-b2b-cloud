package com.koolearn.cloud.exam.examcore.question.dto;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import org.apache.commons.lang.StringUtils;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;


/**
 * 题目展现样式dto
 * 配合IExamQuestionDto 页面展现
 * @author yangzhenye
 */
public class QuestionViewDto implements Serializable {
    /** viewType 题信息展示类型*/
     private int viewType=1;
	 public static final int view_type_question=0;//只显示题目本身(默认)
	 public static final int view_type_question_analysis=1; //显示题目和题目解析
	 public static final int view_type_all=2;  // 显示题目题目解析和答题结果
	 public static final int view_type_jiexi=3;  //只显示题目解析,不显示解析中的个人相关信息如(你的得分,您的答案)
	 public static final int view_type_jiangping=4;//作业讲评页面专用，默认显示出题目相关信息如试题解析，不显示个人信息
	 public static final int view_type_piyue=5;//作业批阅页面专用，显示批阅栏
	 public static final int view_type_review=6;//学生作业复习
	 
	 public static final int view_type_error=3;//默认显示正确答案、得分率、未作答、答错人 （适用页面  教师端：翻转课堂_作业讲评，作业_作业讲评）
	 

    /** buttonType 操作按钮类型 */
    private int buttonType=1;
    public static final int button_ype_null=0;//无操作按钮：老师：试卷预览  学生：作业详情
    public static final int button_ype_question_lib=1;// 题库列表操作按钮
    public static final int button_ype_question_lib_change=2;// 换题
    public static final int button_ype_create_paper=3;// 组卷展示时按钮操作
    public static final int button_ype_zuoye_jiangping=4;// 老师端作业讲评（课堂作业、作业）
    public static final int button_ype_zuoye_detaile=5;// 老师:作业详情、批阅（主、客观有区分）  学生：复习、结果详情
    public static final int button_ype_zuoye_error=6;// 学生: 错题本
    public static final int button_ype_zuoye_person_situation=7;// 老师:个人作业详情

    //结果明细te_exam_result_detail
    Map<String, TpExamResultDetail> detailsMap = new HashMap<String, TpExamResultDetail>();
	/**题目页签序号*/
	private String questionNo="1";
	private String userAnswer; // 用户答案
	private int isCorrect=-1; // 答案是否正确,默认未答
	private double score=0; // 题目分数
	private double userScore = 0; // 答题得分
	
	private int structureId=0;//结构id,试卷时候使用,考试有时候需要
	
	/**
	 * {@link OnlyExamConstant#EXAMTYPE_MYEXAM}
	 */
	private int examType= OnlyExamConstant.EXAMTYPE_MYEXAM;//考试类型 默认普通考试
	/**
	 * 子题答案集合
	 */
	private List<QuestionViewDto> subDtos;
	//考试时做ab卷显示
	private boolean examing = false;
    private String downloadType;//试卷下载类型 GlobalConstant.DOWNLOAD_PAPER_JIEX_LAST
	public Map<String, TpExamResultDetail> getDetailsMap() {
		return detailsMap;
	}
	public void setDetailsMap(Map<String, TpExamResultDetail> detailsMap) {
		this.detailsMap = detailsMap;
	}

	private String dua; // 结果明细里面所记录的用户的答案
	
	public String getDua()
	{
		return dua;
	}
	public void setDua(String dua)
	{
		this.dua = dua;
	}
	public int getViewType() {
		return viewType;
	}
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}
	public String getQuestionNo() {
		return questionNo;
	}
	public void setQuestionNo(String questionNo) {
		this.questionNo = questionNo;
	}
	public String getUserAnswer() {
		if(StringUtils.isBlank(userAnswer) || StringUtils.isBlank(userAnswer.trim()) ){
			return null;
		}
		return userAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public int getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(int isCorrect) {
		this.isCorrect = isCorrect;
	}
	public double getScore() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		return Double.valueOf(nf.format(this.score)).doubleValue();
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getUserScore() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		return Double.valueOf(nf.format(this.userScore)).doubleValue();
	}
	public void setUserScore(double userScore) {
		this.userScore = userScore;
	}
	public int getStructureId() {
		return structureId;
	}
	public void setStructureId(int structureId) {
		this.structureId = structureId;
	}
	public int getExamType() {
		return examType;
	}
	public void setExamType(int examType) {
		this.examType = examType;
	}
	public List<QuestionViewDto> getSubDtos() {
		return subDtos;
	}
	public void setSubDtos(List<QuestionViewDto> subDtos) {
		this.subDtos = subDtos;
	}
	public void initSubViewByPaprentView(){
        if(this.subDtos!=null&&this.subDtos.size()>0){
            for(QuestionViewDto sub:subDtos){
                sub.setButtonType(this.buttonType);
                sub.setViewType(this.viewType);
            }
        }
    }
	public void handlerUserResult(TpExamResultDetail detail, IExamQuestionDto dto)
	{
		String ua = detail.getUserAnswer();
		this.score = detail.getPoints();
		this.userScore = detail.getScore();
//		if(detail.getSubjective().intValue()==TpExamResultDetail.SUBJECTIVE){
//			this.userAnswer = ua;
//		}else{
			if (null != ua && !ua.trim().isEmpty())
			{
				int qt = detail.getQuestionTypeId();
				switch (qt) {
				case Question.QUESTION_TYPE_DANXUAN: // 单选判题
				case Question.QUESTION_TYPE_DANXUAN_BOX: // 方框题
					this.dx(ua);
					break;
				case Question.QUESTION_TYPE_DUOXUAN: // 多选题
					this.ddx(ua);
					break;
				case Question.QUESTION_TYPE_SORT: // 拖拽排序题
					this.px(ua);
					break;
				case Question.QUESTION_TYPE_FILL_BLANK: // 填空题
				case Question.QUESTION_TYPE_FILL_CALCULATION: // 计算填空
					this.fill(ua, dto);
					break;
				case Question.QUESTION_TYPE_SUB_CORRECTION: // 改错子题
					this.gcz(ua);
					break;
				case Question.QUESTION_TYPE_CHOICE_BLANK: // 选择填空题
					this.chb(ua, dto);
					break;
				case Question.QUESTION_TYPE_TABLE: // 矩阵题
					this.tt(ua, dto);
					break;
				case Question.QUESTION_TYPE_SHORT: // 简答题
				case Question.QUESTION_TYPE_SPOKEN: // 口语题
				case Question.QUESTION_TYPE_WHRITE: // 写作题
					this.userAnswer = ua;
					break;
				case Question.QUESTION_TYPE_CLOZE_FILL_BLANK: // 填空型完型填空题
					this.clfb(ua);
					break;
				case Question.QUESTION_TYPE_CORRECTION: // 改错题
				case Question.QUESTION_TYPE_CHOICE_FILL_BLANK: // 选择型完型填空题
				case Question.QUESTION_TYPE_READ: // 阅读理解题
				case Question.QUESTION_TYPE_LISTEN: // 听力题
					this.userAnswer = ua;
					break;
				default:
					break;
				}
			}
			else
			{
				this.userAnswer = "";
			}
//		}
	}
	/** 排序 */
	private void px(String ua)
	{
		this.userAnswer = "";
		String[] uaTemp = ua.split("@@");
		for (int i = 1; i < uaTemp.length; i++)
		{
			if (null != this.userAnswer && !this.userAnswer.trim().isEmpty())
			{
				this.userAnswer = this.userAnswer + "&" + uaTemp[i];
			}
			else
			{
				this.userAnswer = uaTemp[i];
			}
		}
	}
	/** 改错子题 */
	private void gcz(String ua)
	{
		if (null == ua || "".equals(ua.trim()))
		{
			this.userAnswer = "";
		}
		else
		{
			String uaArr[] = ua.split("_");
			this.userAnswer = "";
			for (int i = 2; i < uaArr.length; i++)
			{
				this.userAnswer = this.userAnswer + uaArr[i];
			}
		}
	}
	
	/** 填空型完形 **/
	private void clfb(String ua)
	{
		String[] uaArr = ua.split("@#@");
		this.userAnswer = null;
		for (int i = 1; i < uaArr.length; i++)
		{
			try
			{
				if (null != this.userAnswer)
				{
					this.userAnswer = this.userAnswer + "&" + uaArr[i].split("_")[3].trim();
				}
				else
				{
					this.userAnswer = uaArr[i].split("_")[3].trim();
				}
			}
			catch (Exception e)
			{
				if (null != this.userAnswer)
				{
					this.userAnswer = this.userAnswer + "&    ";
				}
				else
				{
					this.userAnswer = "    ";
				}
			}
		}
	}
	/** 单选 */
	private void dx(String ua)
	{
		this.userAnswer = ua.substring(ua.lastIndexOf("_") + 1);
	}
	/** 多选 */
	private void ddx(String ua)
	{
		this.userAnswer = "";
		String[] uaTemp = ua.split("@@");
		for (int i = 1; i < uaTemp.length; i++)
		{
			if (null != this.userAnswer && !this.userAnswer.trim().isEmpty())
			{
				this.userAnswer = this.userAnswer + "&" + uaTemp[i];
			}
			else
			{
				this.userAnswer = uaTemp[i];
			}
		}
		String[] ts = this.userAnswer.split("&");
		List<Integer> tsInt = new ArrayList<Integer>(0);
		for (String string : ts)
		{
			if (null != string && !"".equals(string.trim()))
			{
				tsInt.add(Integer.valueOf(string));
			}
		}
		for (int i = 0; i < tsInt.size(); i++)
		{
			for (int j = i; j < tsInt.size(); j++)
			{
				if (tsInt.get(i) > tsInt.get(j))
				{
					Integer temp = tsInt.get(i);
					tsInt.set(i, tsInt.get(j));
					tsInt.set(j, temp);
				}
			}
		}
		this.userAnswer = "";
		for (Integer integer : tsInt)
		{
			this.userAnswer = this.userAnswer + "&" + integer.toString();
		}
		if (this.userAnswer.length() > 2)
		{
			this.userAnswer = this.userAnswer.substring(1);
		}
	}
	/** 填空 */
	private void fill(String ua, IExamQuestionDto dto)
	{
		String[] uaArr = ua.split("@@");
		Map<String, String> uaMap = new HashMap<String, String>(0);
		for (int i = 0; i < uaArr.length; i++)
		{
			try
			{
				String[] tt = uaArr[i].split("_");
				uaMap.put(tt[2].trim(), tt[3].trim());
			}
			catch (Exception e)
			{
			}
		}
		EssayQuestionDto tdto = (EssayQuestionDto)dto;
		List<FillblankAnswer> list = tdto.getFillblankAnswers();
		for (int i = 0; i < list.size(); i++)
		{
			String uatt = uaMap.get(String.valueOf(list.get(i).getId()));
			if (null == uatt)
			{
				uatt = "";
			}
			if (null != this.userAnswer)
			{
				this.userAnswer = this.userAnswer + "&" + uatt;
			}
			else
			{
				this.userAnswer = uatt;
			}
		}
	}
	/** 选择填空 */
	private void chb(String ua, IExamQuestionDto dto)
	{
		String[] uaTemp2 = ua.split("@#@");
		Map<String, String> uam = new HashMap<String, String>(0);
		for (int i = 0; i < uaTemp2.length; i++)
		{
			if (uaTemp2[i] == null || "".equals(uaTemp2[i].trim()))
			{
				continue;
			}
			String[] uatt = uaTemp2[i].split("_");
			uam.put(uatt[1], uatt[2]);
		}
		ComplexQuestionDto cdto = (ComplexQuestionDto)dto;
		for (ChoiceQuestionDto dt : cdto.getChoiceQuestionDtos())
		{
			String uatt = uam.get(String.valueOf(dt.getQuestionDto().getQuestion().getId()));
			if (null != this.userAnswer && !this.userAnswer.trim().isEmpty())
			{
				this.userAnswer = this.userAnswer + "&" + uatt;
			}
			else
			{
				this.userAnswer = uatt;
			}
		}
	}
	/** 矩阵题 */
	private void tt(String ua, IExamQuestionDto dto)
	{
		this.dua = ua;
		String[] uaTemp2 = ua.split("@#@");
		List<String> ualist = new ArrayList<String>(0);
		MatrixQuestionDto mqd = (MatrixQuestionDto) dto;
		for (int i = 1; i < uaTemp2.length; i++)
		{
			String[] uatt = uaTemp2[i].split("_");
			if (mqd.getMatrixQuestion().getShowForm() == 1)
			{
				String[] uattt = uatt[1].split("@@");
				for (int j = 1; j < uattt.length; j++)
				{
					ualist.add(uattt[0] + "-" + uattt[j]);
				}
			}
			else if (mqd.getMatrixQuestion().getShowForm() == 2)
			{
				ualist.add(uatt[1] + "-" + uatt[2]);
			}
		}
		for (String str : ualist)
		{
			String uatt = str;
			if (null != this.userAnswer && !this.userAnswer.trim().isEmpty())
			{
				this.userAnswer = this.userAnswer + "&" + uatt;
			}
			else
			{
				this.userAnswer = uatt;
			}
		}
	}
	public boolean isExaming() {
//		return examing;
        return false;//取消ab卷考试
	}
	public void setExaming(boolean examing) {
		this.examing = examing;
	}

    public int getButtonType() {
        return buttonType;
    }

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }
}
