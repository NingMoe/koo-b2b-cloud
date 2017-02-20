package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.awt.Label;
import java.sql.Connection;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.koolearn.cloud.common.entity.Collection;
import com.koolearn.cloud.common.entity.UseRecord;
import com.koolearn.cloud.common.index.PaperIndexUtils;
import com.koolearn.cloud.common.index.QuestionIndexUtils;
import com.koolearn.cloud.dictionary.entity.*;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.exam.entity.OnlyExamConstant;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarType;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaper;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperService;
import com.koolearn.cloud.exam.examcore.question.dao.*;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionFilter;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionTag;
import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.exam.examcore.question.service.QuestionBaseService;
import com.koolearn.cloud.exam.examcore.question.service.QuestionService;
import com.koolearn.cloud.exam.examcore.question.service.TagObjectService;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.queue.ProducerQuestionServiceImpl;
import com.koolearn.cloud.resource.dao.ResourceDao;
import com.koolearn.cloud.util.*;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.framework.search.declare.SearchResult;
import com.koolearn.framework.search.declare.SearchService;
import com.koolearn.framework.search.declare.sort.Order;
import com.koolearn.framework.search.declare.sort.Sort;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;


public class QuestionServiceImpl implements QuestionService {
    private static final Logger logger = Logger.getLogger(QuestionServiceImpl.class);
    @Autowired
    private SearchService searchService;
    @Autowired
    private TestPaperService testPaperService;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private QuestionAttachDao questionAttachDao;
	//不同题型DAO
	//选择题 //判读/排序
	@Autowired
	private ChoiceAnswerDao choiceAnswerDao;
	@Autowired
	private ChoiceQuestionDao choiceQuestionDao;
	//填空\计算
	@Autowired
	private EssayQuestionDAO essayQuestionDAO;
	@Autowired
	private FillblankAnswerDAO fillblankAnswerDAO;

	@Autowired
	private ComplexQuestionDao complexQuestionDao;
	//口语写作
	@Autowired
	private SpokenQuestionDao spokenQuestionDao;
//	@Autowired
//	private QuestionTagDao questionTagDao;
//	@Autowired
//	private QuestionBankExtDao questionBankExtDao;
	//简答
	@Autowired
	private ShortQuestionDao shortQuestionDao;
	@Autowired
	private MatrixQuestionDao matrixQuestionDao;
	@Autowired
	private WhriteQuestionDao whriteQuestionDao;
	@Autowired
	private TagObjectService tagObjectService;
    @Autowired
    protected QuestionBaseService questionBaseService;
    @Autowired
    private ResourceDao resourceDao;

    private ProducerQuestionServiceImpl  producerQuestionService;
    @Autowired
    public ProducerQuestionServiceImpl getProducerQuestionService() {
        return producerQuestionService;
    }

    public void setProducerQuestionService(ProducerQuestionServiceImpl producerQuestionService) {
        this.producerQuestionService = producerQuestionService;
    }

    public TestPaperService getTestPaperService() {
        return testPaperService;
    }

    public void setTestPaperService(TestPaperService testPaperService) {
        this.testPaperService = testPaperService;
    }

    public QuestionBaseService getQuestionBaseService() {
        return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
        this.questionBaseService = questionBaseService;
    }

    public ResourceDao getResourceDao() {
        return resourceDao;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    public ChoiceAnswerDao getChoiceAnswerDao() {
        return choiceAnswerDao;
    }

    public void setChoiceAnswerDao(ChoiceAnswerDao choiceAnswerDao) {
        this.choiceAnswerDao = choiceAnswerDao;
    }

    public ChoiceQuestionDao getChoiceQuestionDao() {
        return choiceQuestionDao;
    }

    public void setChoiceQuestionDao(ChoiceQuestionDao choiceQuestionDao) {
        this.choiceQuestionDao = choiceQuestionDao;
    }

    public EssayQuestionDAO getEssayQuestionDAO() {
        return essayQuestionDAO;
    }

    public void setEssayQuestionDAO(EssayQuestionDAO essayQuestionDAO) {
        this.essayQuestionDAO = essayQuestionDAO;
    }

    public FillblankAnswerDAO getFillblankAnswerDAO() {
        return fillblankAnswerDAO;
    }

    public void setFillblankAnswerDAO(FillblankAnswerDAO fillblankAnswerDAO) {
        this.fillblankAnswerDAO = fillblankAnswerDAO;
    }

    public ComplexQuestionDao getComplexQuestionDao() {
        return complexQuestionDao;
    }

    public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
        this.complexQuestionDao = complexQuestionDao;
    }

    public SpokenQuestionDao getSpokenQuestionDao() {
        return spokenQuestionDao;
    }

    public void setSpokenQuestionDao(SpokenQuestionDao spokenQuestionDao) {
        this.spokenQuestionDao = spokenQuestionDao;
    }

    public ShortQuestionDao getShortQuestionDao() {
        return shortQuestionDao;
    }

    public void setShortQuestionDao(ShortQuestionDao shortQuestionDao) {
        this.shortQuestionDao = shortQuestionDao;
    }

    public MatrixQuestionDao getMatrixQuestionDao() {
        return matrixQuestionDao;
    }

    public void setMatrixQuestionDao(MatrixQuestionDao matrixQuestionDao) {
        this.matrixQuestionDao = matrixQuestionDao;
    }

    public WhriteQuestionDao getWhriteQuestionDao() {
        return whriteQuestionDao;
    }

    public void setWhriteQuestionDao(WhriteQuestionDao whriteQuestionDao) {
        this.whriteQuestionDao = whriteQuestionDao;
    }

    public TagObjectService getTagObjectService() {
        return tagObjectService;
    }

    public void setTagObjectService(TagObjectService tagObjectService) {
        this.tagObjectService = tagObjectService;
    }
    public  void saveAsTag(Connection conn,Question question,int new_id,int oldId){

		List<TagObject> tagObjects=tagObjectService.searchByObject(oldId, ConstantTe.TAG_TYPE_QUESTION_ID);
		if(tagObjects!=null&&tagObjects.size()>0){
			for(TagObject t:tagObjects){
				t.setId(0);
				t.setObjectId(new_id);
			}
			tagObjectService.batchInsert(conn,tagObjects);
		}

	}
	@Override
	public int saveOrUpate(Connection conn,QuestionDto questionDto) throws Exception {
		Question question=questionDto.getQuestion();
		int returnId=0;
		int id=question.getId();
		Date date=new Date(System.currentTimeMillis());
		//处理保存还是更新
		if(id==0){
			//保存试题
			questionDao.updateNewVersion(conn, question.getCode(), 0);
			question.setCreateDate(question.getCreateDate()==null?date:question.getCreateDate());
			question.setLastUpdateDate(date);
			question.setLastUpdateBy(question.getCreateBy());
			int new_id=questionDao.insert(conn,question);
			if(question.getTeId()!=0&&question.getSequenceId()==0){
				questionDao.updateSequence4TeId(conn,question.getTeId(),new_id);
			}
			returnId=new_id;
			question.setId(new_id);

			if(question.getTeId()==0){
				//保存标签
				this.saveOrUpdateQuestionTag(conn,questionDto);
				questionDao.update(conn, question);

				//标签存在增加
				//扩展信息(子题不添加)
//				QuestionBankExt questionBankExt = questionDto.getQuestionBankExt();
//				questionBankExt.setQuestionCode(question.getCode());
//				questionBankExt.setQuestionId(new_id);
//				questionBankExt.setNewVersion(1);
//				questionBankExtDao.updateCodeVersion(conn, question.getCode(),questionBankExt.getSchoolId());
//				questionBankExtDao.insert(conn, questionBankExt);
			}

			//保存材料
			List<QuestionAttach> questionAttachs=questionDto.getQuestionAttachs();
			if(questionAttachs!=null&&questionAttachs.size()>0){
				for(int i=0,size=questionAttachs.size();i<size;i++){
					QuestionAttach attach=questionAttachs.get(i);
					attach.setQuestionId(new_id);
				}
				questionAttachDao.batchInsert(conn,questionAttachs);
			}
			//questionDto.save2ElasticSearch(questionDto, ConstantTe.KLB_OPERATIONTYPE_SAVE,0);

		}else if(questionDto.getSaveType()==ConstantTe.QUESTION_SAVETYPE_SAVEAS){//另存
			//copy试题
			builderQuestion(conn,question);
			int oldId=question.getId();
			//更新原来试题状态
			questionDao.updateNewVersion(conn, question.getCode(), 0);

			//设置新版本和状态
			int maxVersion=questionDao.getMaxVersion(conn,question.getCode());
			question.setId(0);
			question.setVersion(maxVersion+1);
			question.setStatus(Question.QUESTION_STATUS_AUDIT);
			question.setLastUpdateDate(date);
			//保存试题
			int new_id=questionDao.insert(conn,question);
			question.setId(new_id);
			returnId=new_id;
			if(question.getTeId()==0){
				//扩展信息
//				questionDto.getQuestionBankExt().setQuestionCode(question.getCode());
//				QuestionBankExt oriQuestionBankExt = builderQuestionBankExt(questionDto.getQuestionBankExt(),oldId);
//				oriQuestionBankExt.setQuestionCode(question.getCode());
//				oriQuestionBankExt.setQuestionId(new_id);
//				oriQuestionBankExt.setNewVersion(1);
//				oriQuestionBankExt.setId(0);
//				questionBankExtDao.updateCodeVersion(conn, question.getCode(),oriQuestionBankExt.getSchoolId());
//				questionBankExtDao.insert(conn, oriQuestionBankExt);
				//copy保存标签
//				questionDto.setQuestionBankExt(oriQuestionBankExt);
				this.saveOrUpdateQuestionTag(conn,questionDto);
			}

			//copy保存材料
			List<QuestionAttach> questionAttachs=questionDto.getQuestionAttachs();
			if(questionAttachs!=null&&questionAttachs.size()>0){
				for(int i=0,size=questionAttachs.size();i<size;i++){
					QuestionAttach attach=questionAttachs.get(i);
					attach.setQuestionId(new_id);
				}
				questionAttachDao.batchInsert(conn,questionAttachs);
			}

			//Elastic

			questionDto.setQuestion(question);
			//questionDto.save2ElasticSearch(questionDto,ConstantTe.KLB_OPERATIONTYPE_SAVE,oldId);

		}else{//更新
			returnId=question.getId();
			//更新试题
			builderQuestion(conn,question);

			//处理标签的保存或者更新
			/*标签扩展不能变更
			if(question.getTeId()==0){
				//保存标签
				this.saveOrUpdateQuestionTag(conn,questionDto);
				//扩展信息
				QuestionBankExt oriQuestionBankExt = builderQuestionBankExt(questionDto.getQuestionBankExt(),returnId);
				questionBankExtDao.update(conn, oriQuestionBankExt);
			}
			*/
			questionDao.update(conn, question);

			//处理材料
			List<QuestionAttach> attachs=questionDto.getQuestionAttachs();
			if(attachs!=null&&attachs.size()>0){
				for(QuestionAttach attach:attachs){
					if(attach.getQuestionId()==0){
						attach.setQuestionId(question.getId());
					}
				}
			}
			saveOrUpdateAttachs(conn,attachs,returnId);
		}
		return returnId;

	}
	/**
	 * 复合题目编辑前备份
	 */
	public int backQuestion(Connection conn,QuestionDto questionDto) throws Exception {
		Question question=questionDto.getQuestion();
		int returnId=0;
		Date date=new Date(System.currentTimeMillis());
		//处理保存还是更新
		//保存试题
		question.setNewVersion(0);
		question.setVersion(0);
		//question.setCreateDate(date);
		question.setLastUpdateDate(date);
		question.setLastUpdateBy(question.getCreateBy());
		int new_id=questionDao.insert(conn,question);
		if(question.getTeId()!=0&&question.getSequenceId()==0){
			questionDao.updateSequence4TeId(conn,question.getTeId(),new_id);
		}
		returnId=new_id;
		question.setId(new_id);

		if(question.getTeId()==0){
			//保存标签
			this.saveOrUpdateQuestionTag(conn,questionDto);
			questionDao.update(conn, question);

			//标签存在增加
			//扩展信息(子题不添加)
//			QuestionBankExt questionBankExt = questionDto.getQuestionBankExt();
//			questionBankExt.setQuestionCode(question.getCode());
//			questionBankExt.setQuestionId(new_id);
//			questionBankExt.setNewVersion(0);
//			questionBankExtDao.insert(conn, questionBankExt);
		}

		//保存材料
		List<QuestionAttach> questionAttachs=questionDto.getQuestionAttachs();
		if(questionAttachs!=null&&questionAttachs.size()>0){
			for(int i=0,size=questionAttachs.size();i<size;i++){
				QuestionAttach attach=questionAttachs.get(i);
				attach.setQuestionId(new_id);
			}
			questionAttachDao.batchInsert(conn,questionAttachs);
		}

		return returnId;
	}

    //	private QuestionBankExt builderQuestionBankExt(
//			QuestionBankExt questionBankExt,int qId) {
//		QuestionBankExt oriQuestionBankExt = questionBankExtDao.getQuestionExtByCode(questionBankExt.getQuestionCode(),questionBankExt.getSchoolId());
//		if(oriQuestionBankExt==null){
//			return questionBankExt;
//		}
////		oriQuestionBankExt.setTag1(questionBankExt.getTag1());
////		oriQuestionBankExt.setTag2(questionBankExt.getTag2());
////		oriQuestionBankExt.setTag3(questionBankExt.getTag3());
////		oriQuestionBankExt.setStatus(questionBankExt.getStatus());
//		return oriQuestionBankExt;
//	}
	private void saveOrUpdateQuestionTag(Connection conn, QuestionDto questionDto) throws Exception{
		List<QuestionTag> questionTags = new ArrayList<QuestionTag>();
//		int t3 = questionDto.getQuestionBankExt().getTag3();
//		Tags tag3 = handTestPaperService.getTagsById(t3);
//		Tags tag2 = handTestPaperService.getTagsById(tag3.getParentId());
//		Tags tag1 = handTestPaperService.getTagsById(tag2.getParentId());
//		int qid = questionDto.getQuestion().getId();
//		QuestionTag tagq1 = new QuestionTag(qid,tag1.getId(),1,tag1.getName(),""+tag1.getId());
//		QuestionTag tagq2 = new QuestionTag(qid,tag2.getId(),2,tag2.getName(),tag2.getId()+"_"+tag1.getId());
//		QuestionTag tagq3 = new QuestionTag(qid,tag3.getId(),3,tag3.getName(),tag3.getId()+"_"+tag2.getId()+"_"+tag1.getId());
//		questionTags.add(tagq1);
//		questionTags.add(tagq2);
//		questionTags.add(tagq3);
//		//questionTagDao.deleteQuestionTagByQuestionId(conn,qid);
//		questionTagDao.batchInsert(conn, questionTags);
//		questionDto.getQuestion().setTagPath(tag3.getId()+"_"+tag2.getId()+"_"+tag1.getId());
	}
	/**
	 * 从数据库取出数据,将页面没提交的变量重新填充,防止数据丢失
	 * 用户更新数据的时候
	 * @param conn
	 * @param question
	 */
	private void builderQuestion(Connection conn, Question question) {
		int id=question.getId();
		Question question2=questionDao.getQuestionById(conn,id);
		if(question2==null)return;
		question.setCreateDate(question2.getCreateDate()==null?new Date():question2.getCreateDate());
		question.setCreateBy(question2.getCreateBy());
		question.setVersion(question2.getVersion());
//		question.setIssubjectived(question2.getIssubjectived());
		question.setNewVersion(question2.getNewVersion());
		question.setStatus(question2.getStatus());
		question.setSequenceId(question2.getSequenceId());
		question.setTagPath(question2.getTagPath());
		question.setErrorPer(question2.getErrorPer());
		question.setInputType(question2.getInputType());
		question.setVin(question2.getVin());
		question.setLastUpdateDate(new Date());


	}
	/**
	 *  保存或者更新材料信息
	 * @param conn
	 * @param attachs
	 */
	private void saveOrUpdateAttachs(Connection conn, List<QuestionAttach> attachs,int questionID) {
		List<QuestionAttach> updateObj=new ArrayList<QuestionAttach>();
		List<Integer> updateIds=new ArrayList<Integer>();
		List<QuestionAttach> saveObj=new ArrayList<QuestionAttach>();
		if(attachs!=null){
			for(QuestionAttach attach:attachs){
				if(attach.getId()==0){
					saveObj.add(attach);
				}else{
					updateObj.add(attach);
				}
//				updateIds.add(attach.getId());
			}
		}
//		int questionID=attachs.get(0).getQuestionId();

		//删除不存在的材料
		questionAttachDao.deleteAttatch(conn, questionID, updateIds);
		//更新材料
		if(attachs!=null&&attachs.size()>0){
			questionAttachDao.batchUpdate(conn, attachs);
		}
		//保存材料
		if(saveObj.size()>0){
			questionAttachDao.batchInsert(conn, saveObj);
		}
	}
	@Override
	public QuestionDto getQuestionDtoByQuestionId(Connection conn, int questionId) throws Exception {
		Question question=questionDao.getQuestionById(conn,questionId);
		List<QuestionAttach> attaches=questionAttachDao.getByQuestionid(conn,questionId);
//		List<QuestionTag> questionTagList = questionTagDao.queryQuestionTagByQuestionId(conn,questionId);
//		QuestionBankExt questionBankExt = questionBankExtDao.queryByQuestionId(conn,questionId,schoolId);
		QuestionDto questionDto=new QuestionDto();
		questionDto.setQuestion(question);
		questionDto.setQuestionAttachs(attaches);
//		questionDto.setQuestionBankExt(questionBankExt);
//		questionDto.setQuestionTagList(questionTagList);
		return questionDto;
	}
	@Override
	public List<QuestionDto> getQuestionDtoByTeId(Connection conn, int teId) throws Exception {
		List<QuestionDto> questionDtos=new ArrayList<QuestionDto>();
		List<Question>  questions=questionDao.getQuestionByTeId(conn,teId);
		Map<Integer,List<Label>> map=new HashMap<Integer,List<Label>>();
		Map<Integer,List<QuestionAttach>> attachMap=new HashMap<Integer,List<QuestionAttach>>();
		List<Integer> ids=new ArrayList<Integer>();
		for(Question question:questions){
			ids.add(question.getId());
			QuestionDto questionDto=new QuestionDto();
			questionDto.setQuestion(question);
			questionDtos.add(questionDto);
		}
		List<QuestionAttach> attaches=questionAttachDao.batchFindAttaches(conn,ids);
		for(QuestionAttach attach:attaches){
			int id=attach.getQuestionId();
			if(attachMap.containsKey(id)){
				attachMap.get(id).add(attach);
			}else{
				List<QuestionAttach> list=new ArrayList<QuestionAttach>();
				list.add(attach);
				attachMap.put(id, list);
			}
		}
		for(QuestionDto dto:questionDtos){
			dto.setQuestionAttachs(attachMap.get(dto.getQuestion().getId()));
		}
		return questionDtos;
	}
	@Override
	public List<Question> getQuestionByTeId(Connection conn, int questionId) {
		List<Question>  questions=questionDao.getQuestionByTeId(conn,questionId);
		return questions;
	}
	@Override
	public List<Question> getQuestionByTeId(int questionId) {
		List<Question>  questions=questionDao.getQuestionByTeId(questionId);
		return questions;
	}
	@Override
	public void updateQuestionOrder(Connection conn, String subItemOrderStr) {
		String[] ids=subItemOrderStr.split(",");
			for(int i=0;i<ids.length;i++){
				questionDao.updateOrder(conn,i+1,Integer.parseInt(ids[i].trim()));
			}
	}
	@Override
	public void deleteQuestion(Connection conn, List<Integer> ids, int type) throws Exception {
		switch (type) {
			case Question.QUESTION_TYPE_DANXUAN:
			case Question.QUESTION_TYPE_DANXUAN_BOX:
			case Question.QUESTION_TYPE_DANXUAN_GRAPH:
			case Question.QUESTION_TYPE_DANXUAN_POINT:
			case Question.QUESTION_TYPE_DANXUAN_SHADE:
			case Question.QUESTION_TYPE_DUOXUAN:
			case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
			case Question.QUESTION_TYPE_DUOXUAN_SHADE:
			case Question.QUESTION_TYPE_READ_MULTICHOICE:
				//删除答案
				choiceAnswerDao.deleteAnswerByQuestionIds(conn,ids);
				//删除题
				choiceQuestionDao.deleteChoiceByQuestionIds(conn,ids);
				break;
			case Question.QUESTION_TYPE_JUDGE:
			case Question.QUESTION_TYPE_SORT:
				//删除答案
				choiceAnswerDao.deleteAnswerByQuestionIds(conn,ids);
				//删除题
				choiceQuestionDao.deleteChoiceByQuestionIds(conn,ids);
				break;
			case Question.QUESTION_TYPE_FILL_BLANK:
			case Question.QUESTION_TYPE_FILL_CALCULATION:
				//删除题(计算\填空)
				essayQuestionDAO.deleteEssayByQuestionIds(conn,ids);
				//删除答案(计算/填空)
				fillblankAnswerDAO.deleteAnswerByQuestionIds(conn,ids);
				break;
			case Question.QUESTION_TYPE_SHORT:
				shortQuestionDao.deleteByIds(conn, ids);
				break;
			case Question.QUESTION_TYPE_SPOKEN:
				spokenQuestionDao.deleteByIds(conn, ids);
				break;
			case Question.QUESTION_TYPE_TABLE:
				List<Question> questions= questionDao.getQuestionByTeIds(conn, ids);
				//删除子题目
				Map<Integer,List<Integer>> type_ids=new HashMap<Integer, List<Integer>>();
				for(Question question:questions){
					int type2=question.getQuestionTypeId();
					int id=question.getId();
					if(type_ids.containsKey(type2)){
						type_ids.get(type2).add(id);
					}else{
						List<Integer> list=new ArrayList<Integer>();
						list.add(id);
						type_ids.put(type2, list);
					}
				}
				for(int type2:type_ids.keySet()){
					deleteQuestion(conn, type_ids.get(type2), type2);
				}
				//删除题
				matrixQuestionDao.deleteByQuestionIds(conn, ids);
				break;
			case Question.QUESTION_TYPE_READ:
			case Question.QUESTION_TYPE_LISTEN:
			case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			case Question.QUESTION_TYPE_COMPOSITE_DICTATION:
			case Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD:
			case Question.QUESTION_TYPE_CHOICE_WORD:
			case Question.QUESTION_TYPE_ORAL_TRAINING:
			case Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK:
				List<Question> questions1= questionDao.getQuestionByTeIds(conn, ids);
				//删除子题目
				Map<Integer,List<Integer>> type_ids1=new HashMap<Integer, List<Integer>>();
				for(Question question:questions1){
					int type2=question.getQuestionTypeId();
					int id=question.getId();
					if(type_ids1.containsKey(type2)){
						type_ids1.get(type2).add(id);
					}else{
						List<Integer> list=new ArrayList<Integer>();
						list.add(id);
						type_ids1.put(type2, list);
					}
				}
				for(int type2:type_ids1.keySet()){
					deleteQuestion(conn, type_ids1.get(type2), type2);
				}
				//删除题
				complexQuestionDao.deleteByQuestionIds(conn,ids);
				break;
			case Question.QUESTION_TYPE_WHRITE:
				whriteQuestionDao.deleteByIds(conn, ids);
				break;
			default:
				break;
		}
		deleteQuestionDto(conn, ids);
	}
	@Override
	public void deleteQuestionDto(Connection conn, List<Integer> qustionIds) throws Exception {
		//删除 标签
		//删除 材料
		questionAttachDao.deleteAttatchsByQuestionIds(conn, qustionIds);
		//删除 基础试题
		questionDao.deleteQuestions(conn,qustionIds);

	}
	@Override
	public QuestionDto getQuestionDtoByQuestionId(int qustionId){
		Question question=questionDao.getQuestionById(qustionId);
		List<QuestionAttach> attaches=questionAttachDao.getByQuestionid(qustionId);
		List<QuestionTag> questionTagList = null;//questionTagDao.queryQuestionTagByQuestionId(qustionId);
		//QuestionBankExt questionBankExt = questionBankExtDao.queryByQuestionId(qustionId);
		QuestionDto questionDto=new QuestionDto();
		questionDto.setQuestion(question);
		questionDto.setQuestionAttachs(attaches);
//		questionDto.setQuestionTagList(questionTagList);
		//questionDto.setQuestionBankExt(questionBankExt);
		return questionDto;
	}

    /**
     * 试卷组题 加载试卷的学科学段
     * @param user
     * @return
     */
    @Override
    public Map<String,  List<Tags>> findSubjectRangeOfPaper(UserEntity user) {
        Map<String,  List<Tags>> subRanMap=new HashMap<String, List<Tags>>();
        List<Tags> paperSubjectList=new ArrayList<Tags>();//试卷学科
        List<Tags> paperGradeList=new ArrayList<Tags>();//适用年级
        Set<String> rangeNameSet=new HashSet<String>();
        //获取试卷上的学科
        List<Dictionary> paperSubject=DataDictionaryUtil.getInstance().getDataDictionaryListByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_PAPER_SUBJECT);
        Map<String,List<Tags>> gradeNap= KlbTagsUtil.getInstance().findPagerAllGrade();// 获取小学 初中 高中对应年级map
        logger.info("获取所有试卷的年级信息："+JSON.toJSON(gradeNap));
        List<SelectDTO> subject= DataSelectUtil.getInstance().getSubject(user.getId());//获取老师设定的学科
        for(SelectDTO sub:subject){
            List<SelectDTO> range=DataSelectUtil.getInstance().getRange(user.getId(), sub.getId());//获取老师设定的学段
            for(SelectDTO ran:range){
                rangeNameSet.add(ran.getName());
            }
        }
        //拿到返回的使用年级
        for(String rangeName:rangeNameSet){
            paperGradeList.addAll(gradeNap.get(rangeName));
        }
        logger.info("最后返回的年级："+JSON.toJSON(paperGradeList));
        for(SelectDTO s:subject){
            for(Dictionary t:paperSubject){
                if(s.getName().equals(t.getName())){
                    Tags tags=new Tags();tags.setId(t.getValue());tags.setName(t.getName());
                    paperSubjectList.add(tags);
                }
            }
        }
        subRanMap.put("subject",paperSubjectList);
        subRanMap.put("grade",paperGradeList);
        return subRanMap;
    }

    /**根据试卷fullpath 获取知识点树上对应的学科学段
     * @param
     * @return
     */
    @Override
    public Map<String,  Tags>   getSubjectByPaperFullPath(TestPaper testPaper) {
        String paperTagFullPath=testPaper.getTagFullPath();
        Map<String,  Tags> subRanMap=new HashMap<String, Tags>();
        if(StringUtils.isBlank(paperTagFullPath)){
            return subRanMap;
        }
        if(GlobalConstant.PAPER_FORM_XDF_NEW==testPaper.getFromwh() ||GlobalConstant.PAPER_FORM_XDFtoTEACHER_NEW==testPaper.getFromwh() ){
            //1.获取新东方同步的试卷学科转换成知识点树的学科学段
            List<Dictionary> paperSubject=DataDictionaryUtil.getInstance().getDataDictionaryListByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_PAPER_SUBJECT);
            List<Dictionary> knowledgeSubject=DataDictionaryUtil.getInstance().getDataDictionaryListByType(GlobalConstant.DICTIONARY_TYPE_KLB_SUBJECT);
            Map<String,List<Tags>> gradeNap= KlbTagsUtil.getInstance().findPagerAllGrade();// 获取小学 初中 高中对应年级map
            String[] fullPath=paperTagFullPath.split(",");
            Set<String> tagFullPathSet=new HashSet<String>();
            CollectionUtils.addAll(tagFullPathSet, fullPath);//set集合去重
            String rangeName="";
            for(String tagFP:tagFullPathSet){
                String[] tags=tagFP.split("_");
                if(tags.length==2&&tagFP.endsWith("_10")){
                    // 10结尾，fullpath 长度为2是试卷学科
                    String paperSubjectName=  DataDictionaryUtil.getInstance().getDictionaryName(GlobalConstant.DICTIONARY_TYPE_KLBSX_PAPER_SUBJECT,Integer.parseInt(tags[0]));
                    for(Dictionary sub:knowledgeSubject){
                        if(sub.getName().endsWith(paperSubjectName)){
                            Tags t=new Tags();
                            t.setName(sub.getName());t.setId(sub.getValue());
                            subRanMap.put(GlobalConstant.SUBJECT_KEY_subject,t);
                            break;
                        }
                    }
                }else if(tagFP.endsWith("2454_2443_40")){
                    //学习阶段--》中学--》高中
                    rangeName=GlobalConstant.SUBJECT_NAME_gaozhong;
                }else if(tagFP.endsWith("2444_2443_40")){
                    //学习阶段--》中学--》初中
                    rangeName=GlobalConstant.SUBJECT_NAME_chuzhong;
                }else if(tagFP.endsWith("2424_40")){
                    //学习阶段--》小学
                    rangeName=GlobalConstant.SUBJECT_NAME_xiaoxue;
                }
            }
            Tags sub=subRanMap.get(GlobalConstant.SUBJECT_KEY_subject);
            if(sub!=null){
                List<Tags> rangeList=KlbTagsUtil.getInstance().getTagById(sub.getId());
                for(Tags range:rangeList){
                    if(range.getName().equals(rangeName)){
                        subRanMap.put(GlobalConstant.SUBJECT_KEY_range,range);
                        break;
                    }
                }
            }

        }else{
            //老师创建的试卷直接去学科学段字段
        }

        return subRanMap;
    }
    @Override
   public Map<Integer,List<Tags>> getQuestionKnowledges(List<IExamQuestionDto> questionList) throws Exception {
       Map<Integer,List<Tags>> knowMap=new HashMap<Integer, List<Tags>>();
       for(IExamQuestionDto questionDto:questionList){
           putQuestionKnowledge(  questionDto, knowMap);
       }
       return knowMap;
   }
   private static void putQuestionKnowledge(IExamQuestionDto questionDto,Map<Integer,List<Tags>> knowMap){
       knowMap.put(questionDto.getQuestionDto().getQuestion().getId(),questionDto.getQuestionDto().getQuestion().getKnowledgeTagList());
       List<IExamQuestionDto> subQuestionList=questionDto.getSubQuestions();
       if(subQuestionList==null||subQuestionList.size()<1) return ;
       for(IExamQuestionDto sub:subQuestionList){
           //递归处理小题
           putQuestionKnowledge(  sub, knowMap);
       }
   }

    /***************************题目搜索引擎 begin********************************/
    /**
     *根据题目集合 转化成 DTO对象集合
     * @param questionList
     * @return
     * @throws Exception
     */
    public  List<IExamQuestionDto> convertQuestionToDto(List<Question> questionList,UserEntity loginUser) throws Exception {
        //封装试题对象集合
        List<IExamQuestionDto> questionDtoList=new ArrayList<IExamQuestionDto>();
        List<Integer> questionIdList=new ArrayList<Integer>();
        if(questionList!=null  && questionList.size()>0){
            for(Question question:questionList){
                IExamQuestionDto questionDto=questionBaseService.getExamQuestionNoCache(question.getQuestionTypeId(),question.getId());
//                questionDto.getQuestionDto().getQuestion().setUseTimes(question.getUseTimes());
                if(questionDto!=null){
                    questionDtoList.add(questionDto);
                    questionIdList.add(question.getId());
                    //处理小题分值
                    testPaperService.setsubPoint(questionDto, null,null);
                }
            }
            List<Integer> collectionIds=getCollectionQuestionByUser(loginUser.getId(), questionIdList,GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
            for(IExamQuestionDto questionDto:questionDtoList){
               if(collectionIds.contains(questionDto.getQuestionDto().getQuestion().getId())){
                   questionDto.getQuestionDto().getQuestion().setLoginUserCollectioned(true);//该题当前用户已收藏
               }
           }
        }
        return questionDtoList;
    }
    /***
     * 获取单题
     * @param
     * @return
     */
    @Override
    public IExamQuestionDto findQuestionById(Integer questionId) throws Exception {
        QuestionFilter pager=new QuestionFilter();
        pager.setQuestionId(questionId);
        List<Question> questionList= questionDao.searchQuestionByFilter(pager);
        if(questionList==null ||questionList.size()<1) return null;
        IExamQuestionDto questionDto=questionBaseService.
                getExamQuestionNoCache(questionList.get(0).getQuestionTypeId(),questionList.get(0).getId());
        return questionDto;
    }

    /***
     * 加载题库列表
     * @param pager
     * @return
     */
    @Override
    public QuestionFilter findQuestionList(QuestionFilter pager) throws Exception {
        int count=questionDao.searchQuestionCountByFilter(pager);
        pager.setTotalRows(count);
        pager.setPageSize(OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE);
        List<Question> questionList= questionDao.searchQuestionByFilter(pager);
        //封装试题对象集合
        List<IExamQuestionDto> questionDtoList= convertQuestionToDto(questionList,pager.getLoginUser());
        pager.setResultList(questionDtoList);
        return pager;
    }
    /**
     * 题目搜索 EH
     * @param pager
     * @return
     */
    @Override
    public IExamQuestionDto searchQuestionByQuestionId(QuestionFilter pager) {
        try {
            pager.setTotalRows(1);
            pager.setPageSize(1);
            List<Question> listBysearch = this.searchQuestionList(pager);
            //封装试题对象集合
            List<IExamQuestionDto> questionDtoList= convertQuestionToDto(listBysearch,pager.getLoginUser());
            if(questionDtoList!=null &&questionDtoList.size()>0) return questionDtoList.get(0);
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 题目搜索 EH
     * @param pager
     * @return
     */
    @Override
    public QuestionFilter searchQuestion(QuestionFilter pager) {
        try {
            long start = System.currentTimeMillis();
            Long count = this.searchQuestionCount(pager);
            pager.setTotalRows(count);
            pager.setPageSize(OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE);
            List<Question> listBysearch = this.searchQuestionList(pager);
            long searchEnd = System.currentTimeMillis();
            logger.info("搜索引擎用时："+(searchEnd-start));
            //封装试题对象集合
            List<IExamQuestionDto> questionDtoList= convertQuestionToDto(listBysearch,pager.getLoginUser());
            long converEnd=System.currentTimeMillis();
            logger.info("搜索转换用时："+(converEnd-searchEnd));
            logger.info("搜索总用时："+(converEnd-start));
            pager.setResultList(questionDtoList);
            return pager;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 题目搜索 EH 换题、智能组卷、自测搜索列表 需要传登录人
     * @param pager
     * @return
     */
    @Override
    public QuestionFilter searchQuestionChange(QuestionFilter pager) {
        int huanSize=pager.getPageSize();
        int relSize=OnlyExamConstant.KOOTEST_DEFAULT_PAGESIZE*10*huanSize;
        try {
            if(pager.getMustNotPaperId()!=null){
                //换题
                List<Integer> questionIdList=new ArrayList<Integer>();
                questionIdList.add(pager.getMustNotPaperId());
                List<IExamQuestionDto> curq=questionBaseService.findQuestionByIds(questionIdList);
                if(curq!=null &&curq.size()==1){
                    List<Integer> knowledgeList=curq.get(0).getQuestionDto().getQuestion().getKnowledgeTagIdList();
                    List<Integer> knowledgeListSub=null;
                    List<IExamQuestionDto> subDtoList=curq.get(0).getSubQuestions();//处理子题知识点
                    if(subDtoList!=null&&subDtoList.size()>0){
                        for(IExamQuestionDto subDto:subDtoList){
                            knowledgeListSub=subDto.getQuestionDto().getQuestion().getKnowledgeTagIdList();
                        }
                    }
                    pager.setPagerTag(knowledgeList);
                    if(knowledgeList!=null&&knowledgeListSub!=null){
//                        pager.getPagerTag().addAll(knowledgeListSub);//添加小题知识点搜索
                    }
                }
            }
            Long count = this.searchQuestionCount(pager);
            int rPageNo=randonPangeNo(count.intValue(),relSize);
            pager.setTotalRows(count);
            pager.setPageNo(rPageNo);//随机页数
            pager.setPageSize(relSize);//每页
            List<Question> listBysearch = this.searchQuestionList(pager);
            List<IExamQuestionDto> questionDtoList=new ArrayList<IExamQuestionDto>();
            if(listBysearch!=null &&listBysearch.size()>huanSize){
                //封装试题对象集合
                questionDtoList= convertQuestionToDto(listBysearch,pager.getLoginUser());
                Collections.shuffle(questionDtoList);//对list洗牌
                List<IExamQuestionDto> listBysearchMin=new ArrayList<IExamQuestionDto>();
                int last=0;//最大循环次数不能超过listBysearch.size()
                for (int i = 0; i <huanSize ; i++) {
                    if(last>=questionDtoList.size()) break;
                    IExamQuestionDto question=questionDtoList.get(last); last++;
                    if(pager.isExcludeSubjective()&&!question.getQuestionDto().getQuestion().isSubAllObjective()){
                        //需要排除主观题小题并且该题的小题含有主观题
                        i--;continue;
                    }
                    listBysearchMin.add(question);
                }
                questionDtoList=listBysearchMin;
            }
            pager.setResultList(questionDtoList);
            return pager;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static int randonPangeNo(int count, int relSize) {
        if(relSize==0) return 0;
        //根据 总数和pageSize随机获取pageNo
        Random rmin=new Random();
        int p=count/relSize;
        int m=count%relSize;
        if(p==0) return p;
        if(m>0){
            p++;
        }
        return rmin.nextInt(p);
    }

    /**
     * 题目搜索 EH 智能组题 */
    @Override
    public QuestionFilter searchQuestionAuto(QuestionFilter pager,String questionBarkey ) {
        try {
            boolean hasQuestion=false;//标识本次组卷是否有题
            QuestionBar questionBar=new QuestionBar();
            Set<Integer> mustNotQidSet=new HashSet<Integer>();
            pager.setMustNotQuestionSet(mustNotQidSet);
            String[] qcs=pager.getQuestionCount().split(",");
            for(String qc:qcs){//智能组卷:题型_数量
                QuestionBarType qbt=new QuestionBarType();
                String[] qcarr=qc.split("_");
                pager.setPageSize(Integer.parseInt(qcarr[1]));//设置每种题型的页面大小
                if(pager.getPageSize()<1) continue;
                qbt.setType(Integer.parseInt(qcarr[0]));
                qbt.setName(KlbTagsUtil.getInstance().getTag(qbt.getType()).getName());
                //获取题型默认值
                String defaultScore=KlbTagsUtil.getInstance().getTagsDesc(qbt.getType());
                Double defaultScored=StringUtils.isBlank(defaultScore)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(defaultScore);
                qbt.setDefaultScore(defaultScored);
                pager.setQuestionType(qbt.getType());
                pager=  searchQuestionChange(  pager);//获取每种题型的题目
                List<IExamQuestionDto> questionDtoList=pager.getResultList();
                if(questionDtoList!=null&&questionDtoList.size()>0){
                    hasQuestion=true;//有题
                    for(IExamQuestionDto q:questionDtoList){
                        pager.getMustNotQuestionSet().add(q.getQuestionDto().getQuestion().getId());//排除已加入试卷的题目
                        qbt.getExamIdArr().add(q.getQuestionDto().getQuestion().getId());
                    }
                }
                questionBar.getQuestionTypeArr().add(qbt);
            }
            CacheTools.addCache(questionBarkey, questionBar);
            pager.setHasQuestion(hasQuestion);
            return pager;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 题目搜索 EH 学生错题复习 */
    @Override
    public String createStudentErrorFx(QuestionFilter pagers  ) {
        try {
            StringBuilder sb=new StringBuilder();//125766_0_985303_99,0_985303_985304_9.9,0_985303_985305_9.9
            String questionTypeArr=pagers.getQuestionTypeArr();
            JSONArray objArray= JSON.parseArray(questionTypeArr);
            for(Object jsonObject :objArray){
                JSONObject jb= (JSONObject) jsonObject;
                String questionType= (String) jb.get("type");
                JSONArray examIdArr= (JSONArray) jb.get("examIdArr");
                //获取题型默认值小题
                String ds=KlbTagsUtil.getInstance().getTagsDesc(Integer.parseInt(questionType));
                Double defaultScored=StringUtils.isBlank(ds)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(ds);
                for (Object qid:examIdArr){
                    List<Integer> questionIdList=new ArrayList<Integer>();
                    questionIdList.add(Integer.parseInt((String)qid));
                    List<IExamQuestionDto> curq= questionBaseService.findQuestionByIds(questionIdList);
                    IExamQuestionDto q=null;
                    if(curq!=null &&curq.size()==1) {
                        q= curq.get(0)  ;
                    }
                    if(q==null) continue;
                    Integer questionId=q.getQuestionDto().getQuestion().getId();
                    List<IExamQuestionDto> subQuestion=q.getSubQuestions();//子题集合
                    Double allScore=subQuestion!=null&&subQuestion.size()>0?defaultScored*subQuestion.size():defaultScored;
                    sb.append(questionType).append("_0_").append(questionId).append("_").append(allScore).append(",");
                    if(subQuestion!=null&&subQuestion.size()>0){
                        for(IExamQuestionDto sub:subQuestion){
                            Integer subId=sub.getQuestionDto().getQuestion().getId();
                            sb.append(0).append("_"+questionId+"_").append(subId).append("_").append(defaultScored).append(",");
                        }
                    }
                }
            }
            String paperCommit=sb.toString();//创建试卷数据
            return paperCommit;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IExamQuestionDto getQuestionTagsByQid(Integer qid) {
        try {
            List<Integer> qlist=new ArrayList<Integer>();
            qlist.add(qid);
            List<IExamQuestionDto> dtoList= questionBaseService.findQuestionByIds(qlist);
            return dtoList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 题目搜索 EH 学生自测 题数试卷总数算 */
    @Override
    public  Map<String,String> createStudentSelfTest(QuestionFilter pager  ) {
        try {
            Map<String,String> resultMap=new HashMap<String, String>();
            StringBuilder sb=new StringBuilder();//125766_0_985303_99,0_985303_985304_9.9,0_985303_985305_9.9
            double defaultScored=1;
                //获取题型默认值小题
                pager.setPageSize(pager.getNumber());//设置测试总题数
                pager.setIssubjectived(1);
                Integer klbsxQuestionType=DataDictionaryUtil.getInstance().getDataDictionaryListByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_QUESTION_TYPE).get(0).getValue();
                if( pager.getPagerTag()!=null){
                    pager.getPagerTag().add(klbsxQuestionType);
                }else {
                    pager.setPagerTag(new ArrayList<Integer>());
                    pager.getPagerTag().add(klbsxQuestionType);
                }
                pager=  searchQuestionChange(  pager);
                List<IExamQuestionDto> questionDtoList=pager.getResultList();
                pager.setZcCount(0);
                if(questionDtoList!=null&&questionDtoList.size()>0){
                     pager.setZcCount(questionDtoList.size());
                    for(IExamQuestionDto q:questionDtoList){
                        Integer questionId=q.getQuestionDto().getQuestion().getId();
                        List<IExamQuestionDto> subQuestion=q.getSubQuestions();//子题集合
                        Double allScore=subQuestion!=null&&subQuestion.size()>0?defaultScored*subQuestion.size():defaultScored;
                        Integer questionTypeId=q.getQuestionDto().getQuestion().getQuestionTypeSX();
                        sb.append(questionTypeId).append("_0_").append(questionId).append("_").append(allScore).append(",");
                        if(subQuestion!=null&&subQuestion.size()>0){
                            for(IExamQuestionDto sub:subQuestion){
                                Integer subId=sub.getQuestionDto().getQuestion().getId();
                                sb.append(0).append("_"+questionId+"_").append(subId).append("_").append(defaultScored).append(",");
                            }
                        }
                    }
                }
            String paperCommit=sb.toString();//创建试卷数据
            resultMap.put("paperCommit",paperCommit);
            resultMap.put("zcCount",pager.getZcCount()+"");
            return resultMap;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 题目搜索 EH 学生自测 题数按题型算 */
    @Override
    public String createStudentSelfTestQy(QuestionFilter pager  ) {
        try {
            StringBuilder sb=new StringBuilder();//125766_0_985303_99,0_985303_985304_9.9,0_985303_985305_9.9
            List<Tags> questionType=KlbTagsUtil.getInstance().findQuestionType(pager.getSubjectId(),pager.getRangeId());
            Set<Integer> mustNotQidSet=new HashSet<Integer>();
            pager.setMustNotQuestionSet(mustNotQidSet);
            for(Tags tag:questionType){
                //获取题型默认值小题
                String ds=KlbTagsUtil.getInstance().getTagsDesc(tag.getId());
                Double defaultScored=StringUtils.isBlank(ds)?GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(ds);
                pager.setPageSize(pager.getNumber());//设置每种题型的页面大小
                pager.setQuestionType(tag.getId());
                pager=  searchQuestionChange(  pager);//获取每种题型的题目
                List<IExamQuestionDto> questionDtoList=pager.getResultList();
                if(questionDtoList!=null&&questionDtoList.size()>0){
                    for(IExamQuestionDto q:questionDtoList){
                        pager.getMustNotQuestionSet().add(q.getQuestionDto().getQuestion().getId());//排除已加入试卷的试题
                        Integer questionId=q.getQuestionDto().getQuestion().getId();
                        List<IExamQuestionDto> subQuestion=q.getSubQuestions();//子题集合
                        Double allScore=subQuestion!=null&&subQuestion.size()>0?defaultScored*subQuestion.size():defaultScored;
                        sb.append(tag.getId()).append("_0_").append(questionId).append("_").append(allScore).append(",");
                        if(subQuestion!=null&&subQuestion.size()>0){
                            for(IExamQuestionDto sub:subQuestion){
                                Integer subId=sub.getQuestionDto().getQuestion().getId();
                                sb.append(0).append("_"+questionId+"_").append(subId).append("_").append(defaultScored).append(",");
                            }
                        }
                    }
                }
            }
            String paperCommit=sb.toString();//创建试卷数据
            return paperCommit;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**  题目列表搜索  */
//    @InterceptorType(type = "TEACHER")
    public List<Question> searchQuestionList(QuestionFilter pager) throws Exception {
        QueryBuilder query = this.builderElasticsearchQuery(pager);
        String orderFunc = "desc";
        Integer from = pager.getPageNo() * pager.getPageSize();
        List<Map<String, Object>> list = null;
        //设置排序字段
        Sort sort = new Sort();
        List<Order> orderList=new ArrayList<Order>();
        sort.setOrderList(orderList);
//      sort.getOrderList().add(new Order("_score", orderFunc));//elasticsearch自动生成的字段
        sort.getOrderList().add(new Order(QuestionIndexUtils.INDEX_CREATE_DATE, orderFunc));
        sort.getOrderList().add(new Order(QuestionIndexUtils.INDEX_ID, orderFunc));
        // 最后一个参数是String... 类型，传入需要查询的字段名称
        SearchResult result = searchService.simpleSearch(new String[] { PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME) }, query
                        .buildAsBytes().toBytes(), from, pager.getPageSize(),sort,
                QuestionIndexUtils.INDEX_ID,QuestionIndexUtils.INDEX_QUESTION_TYPE_ID,
                QuestionIndexUtils.INDEX_USE_TIMES,QuestionIndexUtils.INDEX_USER_USE_ID, QuestionIndexUtils.INDEX_USER_COLLECTION_ID);
        if (result != null) {
            list = result.getResult();
        }
        List<Question> questions = new ArrayList<Question>();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                Question question = QuestionIndexUtils.getQuestionByMap(map);
                if (question != null) {
                    questions.add(question);
                }
            }
        }
        return questions;
    }

    /** 获取总数 */
    @Override
    public Long searchQuestionCount(QuestionFilter pager) {
        QueryBuilder query = this.builderElasticsearchQuery(pager);
        String questionIndex=PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME);
        long count= searchService.getCount(
                new String[] {   PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME) }, query.buildAsBytes().toBytes());
        logger.info("题目总数："+count+",查询query：["+questionIndex+"]【"+query.toString()+"】");
        return count;
    }
    /**构建索引搜索Query.
     * @return QueryBuilder 对象
     */
    private QueryBuilder builderElasticsearchQuery(QuestionFilter pager) {

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if(pager.getMustNotPaperId()!=null){
            //换题查询排除当前题目
            query.mustNot(QueryBuilders.termQuery(QuestionIndexUtils.INDEX_ID,pager.getMustNotPaperId()));
        }
        query.must(QueryBuilders.termQuery(QuestionIndexUtils.INDEX_NEW_VERSION,1));
        if(pager.getQuestionId()!=null){
            query.must(QueryBuilders.termQuery(QuestionIndexUtils.INDEX_ID,pager.getQuestionId()));
            return query;//根据题目id查询
        }
        query.must(QueryBuilders.termQuery(QuestionIndexUtils.INDEX_STATUS,1));
        if(pager.getSubjectId()!=null){
          query.must(QueryBuilders.queryString(pager.getSubjectId()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
        }
        if(pager.getRangeId()!=null){
            query.must(QueryBuilders.queryString(pager.getRangeId()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
        }
        // 文本检索条件
        if (StringUtils.isNotBlank(pager.getKeyTxt())) {
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            searchQuery.should(QueryBuilders.queryString(pager.getKeyTxt()).field(QuestionIndexUtils.INDEX_SEARCH_CONTENT).minimumShouldMatch("75%"));
            query.must(searchQuery);
        }
        if(pager.getQuestionFilter()!=null){
            String teacherId=pager.getLoginUser().getId()+"";
            switch (pager.getQuestionFilter()){
                case GlobalConstant.QUESTION_FILTER_USERED_NOT:
                    query.mustNot(QueryBuilders.queryString(teacherId).field(QuestionIndexUtils.INDEX_USER_USE_ID));
                    break;
                case GlobalConstant.QUESTION_FILTER_COLLECTION:
                    query.must(QueryBuilders.queryString(teacherId).field(QuestionIndexUtils.INDEX_USER_COLLECTION_ID));
                    break;
                case GlobalConstant.QUESTION_FILTER_USERED:
                    query.must(QueryBuilders.queryString(teacherId).field(QuestionIndexUtils.INDEX_USER_USE_ID));
                    break;
            }
        }
//        if(pager.getObligatoryId()!=null){
//            //必修不为空
//            query.must(QueryBuilders.queryString(pager.getObligatoryId()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
//        }
//        if(pager.getBookVersion()!=null){
//            // 教材版本不为空
//            query.must(QueryBuilders.queryString(pager.getBookVersion()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
//        }
        if(pager.getQuestionHard()!=null && pager.getQuestionHard()>0){
            //难度
            query.must(QueryBuilders.queryString(pager.getQuestionHard()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
        }
        if(pager.getQuestionType()!=null &&pager.getQuestionType()>0){
            //换题题型
            query.must(QueryBuilders.queryString(pager.getQuestionType()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
        }
        if(pager.getTagId()!=null){
            //知识点不为空
            query.must(QueryBuilders.queryString(pager.getTagId()+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
        }
        if(pager.getPagerTag()!=null &&pager.getPagerTag().size()>0){
            //对多个标签id进行过滤
            BoolQueryBuilder tagsQuery = QueryBuilders.boolQuery();
            for(Integer tag:pager.getPagerTag()){
                tagsQuery.should(QueryBuilders.queryString(tag+"").field(QuestionIndexUtils.INDEX_TAG_FULL_PATH));
            }
            query.must(tagsQuery);
        }
        //题目自测是排除已加入试卷的题目id
        if(pager.getMustNotQuestionSet()!=null &&pager.getMustNotQuestionSet().size()>0){
            //对多个标签id进行过滤
            BoolQueryBuilder mustnotqidQuery = QueryBuilders.boolQuery();
            Iterator i=pager.getMustNotQuestionSet().iterator();
            while (i.hasNext()){
                Integer qid= (Integer) i.next();
                //换题查询排除当前题目
                query.mustNot(QueryBuilders.termQuery(QuestionIndexUtils.INDEX_ID, qid));
            }
            query.must(mustnotqidQuery);
        }
        if(pager.getIssubjectived()!=null&&pager.getIssubjectived()==1){
            // 学生自测只查客观题
            query.must(QueryBuilders.queryString(pager.getIssubjectived()+"").field(QuestionIndexUtils.INDEX_ISSUBJECTIVED));
        }
        return query;
    }
    /**
     * 创建索引，同步题库时创建、默认没有收藏和使用
     * @throws Exception 索引创建失败异常处理
     */
    @Override
    public void createQuestionIndex(Question question) throws Exception {
        HashMap<String, Object[]> map =  QuestionIndexUtils.initQuestionMap(question);
        if (map != null) {
//            logger.error( IndexNameUtils.QUESTION_INDEX_NAME+"：：创建题目索引"+question.getId()+":"+question.getCode());
            boolean flag=searchService.bulkInsertData(
                    PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME), map);
            // 判断索引创建是否创建成功，如果失败抛出异常，触发事务回滚.
            if (!flag) {
                String message = "索引创建失败：Id= ; Name=";
                throw new Exception(message);
            }
        }
    }
    /**更新索引：设置收藏，使用次数等 */
    @Override
    public void updateQuestionIndex(Question question){
        try{
            HashMap<String, Object[]> map = QuestionIndexUtils.initQuestionMap(question);
            if (map != null) {
                // 根据原有ID 创建 索引查询条件
                HashMap<String, Object[]> searchMap = new HashMap<String, Object[]>();
                searchMap.put(QuestionIndexUtils.INDEX_ID, new Integer[] { question.getId() });
                this.searchService.bulkUpdateData(PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME), searchMap, map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**更新索引：设置收藏，使用次数等 */
    @Override
    public void deleteQuestionIndex(Question question){
        try{
                // 根据原有ID 创建 索引查询条件
                HashMap<String, Object[]> searchMap = new HashMap<String, Object[]>();
                searchMap.put(QuestionIndexUtils.INDEX_ID, new Integer[] { question.getId() });
                this.searchService.bulkDeleteData(PropertiesConfigUtils.getProperty(IndexNameUtils.QUESTION_INDEX_NAME), searchMap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void collection(QuestionFilter pager, UserEntity user) {
             Integer questionId=  pager.getQuestionId();
            Connection conn = ConnUtil.getTransactionConnection();
            try {
                Collection c = resourceDao.getCollectionByUserIdAndResourceId(user.getId(), questionId, GlobalConstant.STATUS_OFF,GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                if (c == null) {
                    c = resourceDao.getCollectionByUserIdAndResourceId(user.getId(), questionId, GlobalConstant.STATUS_ON,GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                    if (c == null) {
                        c = new Collection();
                        c.setObjectType(GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                        c.setObjectId(questionId);
                        c.setCreateTime(new Date());
                        c.setSchoolId(user.getSchoolId());
                        c.setUserId(user.getId());
                        c.setIsfrom(GlobalConstant.CLIENT_TYPE_TEACHER);
                        c.setStatus(GlobalConstant.STATUS_ON);
                        c.setObjectType(GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                        resourceDao.saveCollection(conn, c);
                    } else {
                        c.setStatus(GlobalConstant.STATUS_OFF);
                        c.setCreateTime(new Date());
                        resourceDao.updateCollection(conn, c);
                    }
                } else {
                    c.setStatus(GlobalConstant.STATUS_ON);
                    c.setCreateTime(new Date());
                    resourceDao.updateCollection(conn, c);
                }
                conn.commit();
                Question q=questionBaseService.getQuestionById(questionId);
                IExamQuestionDto questionDto=questionBaseService.getExamQuestionNoCache(q.getQuestionTypeId(),q.getId());
                List<Integer> ids = resourceDao.findCollectionByResourceId(questionId,GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                String userIds = StringUtils.join(ids, "_");
                questionDto.getQuestionDto().getQuestion().setUserCollectionIds(userIds);//更新收藏用户id到索引
                questionDto.getQuestionDto().getQuestion().setLastUpdateDate(new Date());
//                updateQuestionIndex(  questionDto.getQuestionDto().getQuestion());
                producerQuestionService.send(questionDto.getQuestionDto().getQuestion());//队列更新索引
                setAllCollectionQuestionByUser(user.getId(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);//更新用户题目收藏缓存
            } catch (Throwable e) {
                e.printStackTrace();
                ConnUtil.rollbackConnection(conn);
            } finally {
                ConnUtil.closeConnection(conn);
            }
    }
    @Override
    public void saveUsetimes(Integer questionId, UserEntity user) {
        Connection conn = ConnUtil.getTransactionConnection();
        try {
            UseRecord ur=resourceDao.findUseRecordByuser(user.getId(), questionId, GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
            if (ur == null) {
                ur=new UseRecord();
                ur.setObjectType(GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
                ur.setObjectId(questionId);
                ur.setStatus(GlobalConstant.STATUS_ON);
                ur.setUseTime(new Date());
                ur.setUserId(user.getId());
                ur.setUseTimes(1);
                resourceDao.saveUseRecord(conn,ur);
            } else {
                ur.setUseTimes(ur.getUseTimes()+1);
                resourceDao.updateUseRecord(conn,ur);
            }
            conn.commit();
            Question question=questionBaseService.getQuestionById(questionId);
            IExamQuestionDto dto=questionBaseService.getExamQuestionNoCache(question.getQuestionTypeId(),question.getId());
            Set<Integer> recordUserIds = resourceDao.findUseRecordUser(question.getId(), GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
            String ruserIds = StringUtils.join(recordUserIds, "_");
            dto.getQuestionDto().getQuestion().setUserUseIds(ruserIds);//设置使用用户id
            int useTimes=resourceDao.findUseTimes(question.getId(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
            dto.getQuestionDto().getQuestion().setUseTimes(useTimes);
            dto.getQuestionDto().getQuestion().setLastUpdateDate(new Date());
//            updateQuestionIndex(  dto.getQuestionDto().getQuestion());
            producerQuestionService.send(dto.getQuestionDto().getQuestion());//队列更新索引
        } catch (Throwable e) {
            e.printStackTrace();
            ConnUtil.rollbackConnection(conn);
        } finally {
            ConnUtil.closeConnection(conn);
        }

    }

    public boolean isCollection(int userId, int resourceId,int type) {
         Collection c = resourceDao.getCollectionByUserIdAndResourceId(userId, resourceId, GlobalConstant.STATUS_ON,type);
        if (c == null) {
            return false;
        }
        return true;
    }
    @Override
    public List<Integer> getCollectionQuestionByUser(int userId,List<Integer> questionIds,int type) {
        //获取用户全部的题目收藏，收藏时更新缓存
        List<Integer> collectionIds=CacheTools.getCache(GlobalConstant.USER_COLLECTION_KEY+userId,List.class);
        if(collectionIds==null||collectionIds.size()<1){
            String isSearchedKey=GlobalConstant.USER_COLLECTION_KEY+userId+"_flag_"+ParseDate.formatByDate(new Date(),ParseDate.DATE_FORMAT_YYYYMMDDHH);
            String ifSearched=CacheTools.getCache(isSearchedKey ,String.class);
            if(!"false&&false".equals(ifSearched)){
                setAllCollectionQuestionByUser(userId,type);
                CacheTools.addCache(isSearchedKey,"false&&false");
            }
            //collectionIds = resourceDao.getCollectionQuestionByUser(userId, questionIds, GlobalConstant.STATUS_ON,type);
        }
            collectionIds=CacheTools.getCache(GlobalConstant.USER_COLLECTION_KEY+userId,List.class);
        return collectionIds;
    }

    /**
     * 设置用户所有收藏缓存
     * @param userId
     * @param type
     * @return
     */
    public void setAllCollectionQuestionByUser(int userId,int type) {
        List<Integer> collectionIds = resourceDao.getAllCollectionQuestionByUser(userId, GlobalConstant.STATUS_ON,type);
        CacheTools.addCache(GlobalConstant.USER_COLLECTION_KEY+userId,collectionIds);
    }
    /***************************题目搜索引擎 end********************************/
    /***************************试卷搜索引擎 begin********************************/
    @Override
    public void createPaperIndex(TestPaper paper) throws Exception {
        HashMap<String, Object[]> map = PaperIndexUtils.initPaperMap(paper);
        if (map != null) {
            boolean flag=searchService.bulkInsertData(
                    PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME), map);
            // 判断索引创建是否创建成功，如果失败抛出异常，触发事务回滚.
            if (!flag) {
                String message = "索引创建失败：Id= ; Name=";
                throw new Exception(message);
            }
        }
    }

    @Override
    public void updatePaperIndex(TestPaper paper) {
        try{
            HashMap<String, Object[]> map =PaperIndexUtils.initPaperMap(paper);
            if (map != null) {
                // 根据原有ID 创建 索引查询条件
                HashMap<String, Object[]> searchMap = new HashMap<String, Object[]>();
                searchMap.put(PaperIndexUtils.INDEX_ID, new Integer[] { paper.getId() });
                this.searchService.bulkUpdateData(PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME), searchMap, map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deletePaperIndex(TestPaper paper) {
        try{
            // 根据原有ID 创建 索引查询条件
            HashMap<String, Object[]> searchMap = new HashMap<String, Object[]>();
            searchMap.put(PaperIndexUtils.INDEX_ID, new Integer[] { paper.getId() });
            this.searchService.bulkDeleteData(PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME), searchMap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 试卷搜索
     * @param pager
     * @return
     */
    @Override
    public PaperPager searchPaper(PaperPager pager) {
        try {
            Long count = this.searchPaperCount(pager);
            pager.setTotalRows(count);
            List<TestPaper> listBysearch = this.searchPaperList(pager);
            if(listBysearch!=null && listBysearch.size()>0){
                for(TestPaper tp:listBysearch){
                    String joinUser=tp.getJoinselfUser();
                    if(StringUtils.isNotBlank(joinUser)&& joinUser.indexOf("_"+pager.getLoginUser().getId()+"_")>-1){
                        tp.setJoin(true);
                    }
                    if(tp.getTeacherId()!=null&&tp.getTeacherId().equals(pager.getLoginUser().getId())){
                        //自己创建的题可以删除
                        tp.setCanDel(true);
                    }
                }
            }
            pager.setResultList(listBysearch);
            return pager;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<TestPaper> searchPaperList(PaperPager pager) throws Exception {
        QueryBuilder query = this.builderElasticsearchQueryPaper(pager);
        String orderFunc = "desc";
        Integer from = pager.getPageNo() * pager.getPageSize();
        List<Map<String, Object>> list = null;
        //设置排序字段
        Sort sort = new Sort();
        List<Order> orderList=new ArrayList<Order>();
        sort.setOrderList(orderList);
//      sort.getOrderList().add(new Order("_score", orderFunc));//elasticsearch自动生成的字段
        sort.getOrderList().add(new Order(PaperIndexUtils.INDEX_CREATE_DATE, orderFunc));
        sort.getOrderList().add(new Order(PaperIndexUtils.INDEX_ID, orderFunc));
        // 最后一个参数是String... 类型，传入需要查询的字段名称
        SearchResult result = searchService.simpleSearch(new String[] { PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME) }, query
                        .buildAsBytes().toBytes(), from, pager.getPageSize(),sort,
                PaperIndexUtils.INDEX_ID,PaperIndexUtils.INDEX_PAPER_NAME,
                PaperIndexUtils.INDEX_PAPER_CODE,PaperIndexUtils.INDEX_POINTS,
                PaperIndexUtils.INDEX_BROWSE_TIMES,PaperIndexUtils.INDEX_NEW_TEACHER_ID,
                PaperIndexUtils.INDEX_TEACHER_NAME,PaperIndexUtils.INDEX_SCHOOL_ID,
                PaperIndexUtils.INDEX_CREATE_DATE,PaperIndexUtils.INDEX_UPDATE_TIME,
                PaperIndexUtils.INDEX_FROMWH,PaperIndexUtils.INDEX_JOINSELF_USER,
                PaperIndexUtils.INDEX_SUBJECT_ID,PaperIndexUtils.INDEX_SUBJECT,
                PaperIndexUtils.INDEX_RANGE_ID,PaperIndexUtils.INDEX_RANGE,
                PaperIndexUtils.INDEX_STATUS,PaperIndexUtils.INDEX_TAG_FULL_PATH);
        if (result != null) {
            list = result.getResult();
        }
        List<TestPaper> testPaperList = new ArrayList<TestPaper>();
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                TestPaper testPaper = PaperIndexUtils.getPaperByMap(map);
                if (testPaper != null) {
                    testPaperList.add(testPaper);
                }
            }
        }
        return testPaperList;
    }
    /** 获取总数 */
    public Long searchPaperCount(PaperPager pager) {
        QueryBuilder query = this.builderElasticsearchQueryPaper(pager);
        String paperIndex=PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME);
        long count= searchService.getCount(
                new String[] {   PropertiesConfigUtils.getProperty(IndexNameUtils.PAPER_INDEX_NAME) }, query.buildAsBytes().toBytes());
        logger.info("试卷总数："+count+",查询query：["+paperIndex+"]【"+query.toString()+"】");
        return count;
    }
    /**构建索引搜索Query.
     * @return QueryBuilder 对象
     */
    private QueryBuilder builderElasticsearchQueryPaper(PaperPager pager) {

        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_STATUS,0));
        if(pager.getPaperId()!=null&&pager.getPaperId()!=0){
            query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_ID,pager.getPaperId()));
            return query;//根据题目id查询
        }
        // 文本检索条件
        if (StringUtils.isNotBlank(pager.getSearchText())) {
            BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
            searchQuery.should(QueryBuilders.queryString(pager.getSearchText()).field(PaperIndexUtils.INDEX_SEARCH_CONTENT).minimumShouldMatch("75%"));
            query.must(searchQuery);
        }
       if(StringUtils.isNotBlank(pager.getUserName())){
           //创建人
           BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
           searchQuery.should(QueryBuilders.queryString(pager.getUserName()).field(PaperIndexUtils.INDEX_TEACHER_NAME));
           query.must(searchQuery);
       }
        if(pager.getSubjectId()!=null){
            //学科（本校和我的试卷库）
            query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_SUBJECT_ID,pager.getSubjectId()));
        }
        if(pager.getRangeId()!=null){
            //x学段（本校和我的试卷库）
            query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_RANGE_ID,pager.getRangeId()));
        }
        if(pager.getPaperTagIds()!=null &&pager.getPaperTagIds().length>0){
            //新东方试卷标签：地区、年份、地区类型
//            BoolQueryBuilder tagsQuery = QueryBuilders.boolQuery();
            if(pager.getPaperTagIds()!=null && pager.getPaperTagIds().length>0){
                for(Integer tag:pager.getPaperTagIds()){
                    if(tag!=null){
//                       tagsQuery.should(QueryBuilders.termQuery(PaperIndexUtils.INDEX_TAG_FULL_PATH, tag));
                        query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_TAG_FULL_PATH,tag));
                    }
                }
//                query.must(tagsQuery);
            }
        }
        if(StringUtils.isNotBlank(pager.getSearchFrom())){
            if(pager.getSearchFrom().equals(GlobalConstant.PAPER_SEARCH_TYPE_BXSJ)){
                //查询本校(本校老师创建)
                BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
                searchQuery.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_FROMWH,GlobalConstant.PAPER_FORM_TEACHER_NEW));//老师建的试卷
                searchQuery.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_SCHOOL_ID,pager.getLoginUser().getSchoolId()));//且是本校的
                query.must(searchQuery);
            }else  if(pager.getSearchFrom().equals(GlobalConstant.PAPER_SEARCH_TYPE_WDSJ)){
                //查询我的试卷库（创建和加入的新东方试卷）
                //创建人
                BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
                searchQuery.should(QueryBuilders.termQuery(PaperIndexUtils.INDEX_NEW_TEACHER_ID,pager.getLoginUser().getId()));
                searchQuery.should(QueryBuilders.queryString(pager.getLoginUser().getId()+"").field(PaperIndexUtils.INDEX_JOINSELF_USER));
                query.must(searchQuery);
            }else{
                //新东方试卷
                query.must(QueryBuilders.termQuery(PaperIndexUtils.INDEX_FROMWH,GlobalConstant.PAPER_FORM_XDF_NEW));
            }
        }
        if(pager.getSjztGJAll()!=null &&pager.getSjztGJAll().size()>0){
            //试题组卷 试卷学科过滤标签
            BoolQueryBuilder tagsQuery = QueryBuilders.boolQuery();
                for(Integer tag:pager.getSjztGJAll()){
                    if(tag!=null){
                       tagsQuery.should(QueryBuilders.termQuery(PaperIndexUtils.INDEX_TAG_FULL_PATH, tag));
                    }
                }
                query.must(tagsQuery);
        }
        if(pager.getSjztGJGradeAll()!=null &&pager.getSjztGJGradeAll().size()>0){
            //试题组卷 试卷年级为全部时的过滤标签
            BoolQueryBuilder tagsQuery = QueryBuilders.boolQuery();
            for(Integer tag:pager.getSjztGJGradeAll()){
                if(tag!=null){
                    tagsQuery.should(QueryBuilders.termQuery(PaperIndexUtils.INDEX_TAG_FULL_PATH, tag));
                }
            }
            query.must(tagsQuery);
        }
        return query;
    }
    /***************************试卷搜索引擎 end********************************/
}
