package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.koolearn.cloud.exam.entity.DataSync;
import com.koolearn.cloud.exam.examcore.question.dao.*;
import com.koolearn.cloud.exam.examcore.question.dto.*;
import com.koolearn.cloud.exam.examcore.question.entity.*;
import com.koolearn.cloud.exam.examcore.question.service.*;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.resource.dao.ResourceDao;
import com.koolearn.cloud.util.*;
import com.koolearn.cloud.util.pdf.HtmlCssUtil;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class QuestionBaseServiceImpl implements QuestionBaseService {
	private Logger logger = Logger.getLogger(QuestionBaseServiceImpl.class);
	@Autowired
	private QuestionAttachDao questionAttachDao;
	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private ChoiceQuestionDao choiceQuestionDao;
	@Autowired
	private ChoiceAnswerDao choiceAnswerDao;
	@Autowired
	private ShortQuestionService shortQuestionService;
	@Autowired
	private SpokenQuestionService spokenQuestionService;
	@Autowired
	private ShortQuestionDao shortQuestionDao;
	@Autowired
	private SpokenQuestionDao spokenQuestionDao;
	@Autowired
	private EssayQuestionDAO essayQuestionDAO;
	@Autowired
	private ComplexQuestionDao complexQuestionDao;
	@Autowired
	private CorrectionQuestionDao correctionQuestionDao;
	@Autowired
	private MatrixQuestionDao matrixQuestionDao;
	@Autowired
	private WhriteQuestionDao whriteQuestionDao;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ChoiceQuestionService choiceQuestionService;
	@Autowired
	private ReadQuestionService readQuestionService;
	@Autowired
	private EssayQuestionService essayQuestionService;
	@Autowired
	private TagObjectService tagObjectService;
	@Autowired
	private CorrectionQuestionService correctionQuestionService;
	@Autowired
	private ComplexQuestionService complexQuestionService;
	@Autowired
	private WhriteQuestionService whriteQuestionService;
	@Autowired
	private MatrixQuestionService matrixQuestionService;
    @Autowired
    private ResourceDao resourceDao;
    public QuestionAttachDao getQuestionAttachDao() {
        return questionAttachDao;
    }

    public ResourceDao getResourceDao() {
        return resourceDao;
    }

    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    public void setQuestionAttachDao(QuestionAttachDao questionAttachDao) {
        this.questionAttachDao = questionAttachDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public ChoiceQuestionDao getChoiceQuestionDao() {
        return choiceQuestionDao;
    }

    public void setChoiceQuestionDao(ChoiceQuestionDao choiceQuestionDao) {
        this.choiceQuestionDao = choiceQuestionDao;
    }

    public ChoiceAnswerDao getChoiceAnswerDao() {
        return choiceAnswerDao;
    }

    public void setChoiceAnswerDao(ChoiceAnswerDao choiceAnswerDao) {
        this.choiceAnswerDao = choiceAnswerDao;
    }

    public ShortQuestionDao getShortQuestionDao() {
        return shortQuestionDao;
    }

    public void setShortQuestionDao(ShortQuestionDao shortQuestionDao) {
        this.shortQuestionDao = shortQuestionDao;
    }

    public SpokenQuestionDao getSpokenQuestionDao() {
        return spokenQuestionDao;
    }

    public void setSpokenQuestionDao(SpokenQuestionDao spokenQuestionDao) {
        this.spokenQuestionDao = spokenQuestionDao;
    }

    public EssayQuestionDAO getEssayQuestionDAO() {
        return essayQuestionDAO;
    }

    public void setEssayQuestionDAO(EssayQuestionDAO essayQuestionDAO) {
        this.essayQuestionDAO = essayQuestionDAO;
    }

    public ComplexQuestionDao getComplexQuestionDao() {
        return complexQuestionDao;
    }

    public void setComplexQuestionDao(ComplexQuestionDao complexQuestionDao) {
        this.complexQuestionDao = complexQuestionDao;
    }

    public CorrectionQuestionDao getCorrectionQuestionDao() {
        return correctionQuestionDao;
    }

    public void setCorrectionQuestionDao(CorrectionQuestionDao correctionQuestionDao) {
        this.correctionQuestionDao = correctionQuestionDao;
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

    public WhriteQuestionService getWhriteQuestionService() {
        return whriteQuestionService;
    }

    public void setWhriteQuestionService(WhriteQuestionService whriteQuestionService) {
        this.whriteQuestionService = whriteQuestionService;
    }

    public MatrixQuestionService getMatrixQuestionService() {
        return matrixQuestionService;
    }

    public void setMatrixQuestionService(MatrixQuestionService matrixQuestionService) {
        this.matrixQuestionService = matrixQuestionService;
    }

    public ShortQuestionService getShortQuestionService() {
		return shortQuestionService;
	}

	public void setShortQuestionService(ShortQuestionService shortQuestionService) {
		this.shortQuestionService = shortQuestionService;
	}

	public SpokenQuestionService getSpokenQuestionService() {
		return spokenQuestionService;
	}

	public void setSpokenQuestionService(SpokenQuestionService spokenQuestionService) {
		this.spokenQuestionService = spokenQuestionService;
	}
	
    /**
     * 考试试题批量查找，根据试题code
     * @param questionCodes
     * @param schoolId
     * @return
     * @throws Exception
     */
	public List<IExamQuestionDto> findQuestionByCodes(List<String> questionCodes,Integer schoolId)
			throws Exception {
		try {
			if(questionCodes==null)return null;
			List<IExamQuestionDto> dtos = new ArrayList<IExamQuestionDto>();
			for(String questionCode:questionCodes){
				Question q = questionDao.getQuestionByCode( questionCode);
				System.out.println("QuestionBaseServiceImpl=218hang==questionCode=="+questionCode+"  "+q);
				if(q!=null){
					IExamQuestionDto dto = this.getExamQuestionNoCache(q.getQuestionTypeId(), q.getId());
					if(dto!=null){
						dtos.add(dto);
					}
				}
			}
			return dtos;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
    public List<IExamQuestionDto> findQuestionByIds(List<Integer> questionIds)throws Exception{
        return findQuestionByIds(questionIds,-1);
    }
    /**
     * 考试试题批量查找，根据试题id
     * @param questionIds
     * @param schoolId
     * @return
     * @throws Exception
     */
	public List<IExamQuestionDto> findQuestionByIds(List<Integer> questionIds,Integer schoolId)
			throws Exception {
		try {
			if(questionIds==null)return null;
			List<IExamQuestionDto> dtos = new ArrayList<IExamQuestionDto>();
			for(Integer questionId:questionIds){
				IExamQuestionDto dto = this.getExamQuestionCache(questionId);
				if (null == dto)
				{
					Question q = questionDao.getQuestionById(questionId);
					dto = this.getExamQuestionNoCache(q.getQuestionTypeId(), questionId);
				}
				if(dto!=null){
					dtos.add(dto);
				}
			}
			return dtos;
		} catch (Exception e) {
            e.printStackTrace();
			logger.error(e);
			throw e;
		}
	}
	
	//查询题目缓存
	private IExamQuestionDto getExamQuestionCache(int id) throws Exception {
		IExamQuestionDto dto = CacheTools.getCache(ConstantTe.REPOSITORY_QUSTION_ID+id, IExamQuestionDto.class);
		return dto;
	}
	
	@Override
	public IExamQuestionDto findQuestionById(int questionId) throws Exception {
		return findQuestionByIdData(questionId);
	}
	
    /**
     * 考试试题批量查找，根据试题id
     * @return
     * @throws Exception
     */
	public IExamQuestionDto findQuestionByIdData(int questionId)
			throws Exception {
		try {
				IExamQuestionDto dto = this.getExamQuestionCache(questionId);
				if (null == dto)
				{
					Question q = questionDao.getQuestionById(questionId);
					dto = this.getExamQuestionNoCache(q.getQuestionTypeId(), questionId);
				}
			return dto;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	/**
     * 封装单题信息、知识点、进度点等标签）
     * @param questionTypeId
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public IExamQuestionDto getExamQuestionNoCache(int questionTypeId, int id) throws Exception {
		IExamQuestionDto dto=null;
		//查询缓存题对象
		//dto = getExamQuestionCache(id);
		if(dto!=null){
			return dto;
		}
		switch (questionTypeId) {
		case Question.QUESTION_TYPE_DANXUAN:
		case Question.QUESTION_TYPE_DUOXUAN:
		//case Question.QUESTION_TYPE_JUDGE:
		case Question.QUESTION_TYPE_SORT:
			dto=choiceQuestionService.getChoiceQuestion(id);
			break;
		case Question.QUESTION_TYPE_READ:
		case Question.QUESTION_TYPE_LISTEN:
			dto=readQuestionService.getReadByQuestionId(id);
			break;
		case Question.QUESTION_TYPE_SHORT:
			dto=shortQuestionService.getShortQuestionDto(id);
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			dto=spokenQuestionService.getSpokenQuestionDto(id);
			break;
		case Question.QUESTION_TYPE_FILL_BLANK://填空题
		case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
			dto = essayQuestionService.getEssayQuestionDTO(id);
			break;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
		case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
			dto=complexQuestionService.getByQuestionId(id);	
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			dto = correctionQuestionService.getByQuestionId(id);
			break;
		case Question.QUESTION_TYPE_SUB_CORRECTION:
			dto = correctionQuestionService.getSubByQuestionId(id);
			break;
		case Question.QUESTION_TYPE_WHRITE:
			dto = whriteQuestionService.getWhriteQuestionDto(id);
			break;
		case Question.QUESTION_TYPE_TABLE:
			dto = matrixQuestionService.getByQuestionId(id);
			break;
		default:
			break;
		}
        if(dto==null||dto.getQuestionDto()==null ||dto.getQuestionDto().getQuestion()==null){
            logger.debug("题目"+id+"在数据库中不存在，同步失败的题");
            return null;
        }
        //1.处理试题 知识点、进度点、题型等标签
        parseQuestionTagInfo(dto.getQuestionDto().getQuestion());
        //2.封装题目搜索文本信息
         parseQuestionSearchContent(dto);
        //3.封装使用次数，收藏人等
        parseQuestionCollectionUser(dto);
        List<IExamQuestionDto> subDtoList=dto.getSubQuestions();//处理子题知识点
        if(subDtoList!=null&&subDtoList.size()>0){
            for(IExamQuestionDto subDto:subDtoList){
                parseQuestionTagInfo(subDto.getQuestionDto().getQuestion());
                if(subDto.getQuestionDto().getQuestion().getIssubjectived()!=ConstantTe.QUESTION_OBJCECTIVE_FLAG){
                     //子题存在主观题，标识子题不全是客观题
                    dto.getQuestionDto().getQuestion().setSubAllObjective(false);
                }
            }
        }
        CacheTools.addCache(ConstantTe.REPOSITORY_QUSTION_ID+id, dto);
		
		return dto;
	}

    /**
     * 1.处理试题 知识点、进度点、题型等标签
     * @param question
     */
    private void parseQuestionTagInfo(Question question) {
        try {
            if(StringUtils.isBlank(question.getTagPath()) ) return ;
            String[] tagFullPatArr=question.getTagPath().split(",");
            Set<String> tagFullPathSet=new HashSet<String>();
            CollectionUtils.addAll(tagFullPathSet,tagFullPatArr);//set集合去重
            List<String> knowList=new ArrayList<String>();
            List<String> knowListFullPath=new ArrayList<String>();
            List<String> teacList=new ArrayList<String>();
            List<String> teacListFullPath=new ArrayList<String>();
            List<String> kaochaNlList=new ArrayList<String>();
            Integer questionTypeS=0;
            String  questionTypeN="";
            String questionTypeName=PropertiesConfigUtils.getProperty(GlobalConstant.CLOUD_TAG_NAME_QUSTION_TYPE);
            Integer klbsxQuestionType=DataDictionaryUtil.getInstance().getDataDictionaryListByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_QUESTION_TYPE).get(0).getValue();
            questionTypeName=StringUtils.isBlank(questionTypeName)?"_"+klbsxQuestionType+"_20":questionTypeName;
            Integer kcnlTagId=DataDictionaryUtil.getInstance().getFirstDictionaryByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_KCNL);//考察能力标签id
            Integer jingpin=DataDictionaryUtil.getInstance().getFirstDictionaryByType(GlobalConstant.DICTIONARY_TYPE_KLBSX_JPT);//精品题标签id
            for(String tagFP:tagFullPathSet){
                //83699_83697_83604_83601_83600_2471_60
                if(tagFP.endsWith("_60")){
                    //学科学科树
                    String[] tags=tagFP.split("_");
                    if(tags.length>4){
                        //知识点、进度点层
                        String kOrTId=tags[tags.length-4];
                        String tagName=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(kOrTId)).getName();
                        if(GlobalConstant.KLB_TAB_KNOWLEDGE_NAME.equals(tagName)){
                            //该标签是知识点
                            Tags tag=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(tags[0]));
                            question.getKnowledgeTagList().add(tag);
                            question.getKnowledgeTagIdList().add(tag.getId());
                            knowList.add(tag.getName());
                            knowListFullPath.add(KlbTagsUtil.getInstance().getCacheTagFullPathName(tagFP,GlobalConstant.KLB_KNOWLEDGE_NAME_OFFSET));
                        }else if(GlobalConstant.KLB_TAB_TEACHING_NAME.equals(tagName)){
                            //该标签是教材目录
                            teacList.add(KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(tags[0])).getName());
                            teacListFullPath.add(KlbTagsUtil.getInstance().getCacheTagFullPathName(tagFP,GlobalConstant.KLB_TEACHING_NAME_OFFSET));
                        }
                    }
                }else if(tagFP.endsWith(questionTypeName)){
                    //_93400_20 属性标签->学科题型
                    String[] tags=tagFP.split("_");
                    if(tags.length==GlobalConstant.DICTIONARY_TYPE_KLBSX_length){
                        //符合标签深度的才是题型标签
                        questionTypeS=Integer.parseInt(tags[0]);
                        questionTypeN=KlbTagsUtil.getInstance().getCacheTag(questionTypeS).getName();
                    }else{
                        questionTypeS=0;
                        questionTypeN="无题型标签";
                    }

                }else if(tagFP.indexOf("_"+kcnlTagId+"_")>0){
                    //处理考察能力
                    String[] tags=tagFP.split("_");
                    Tags tag=KlbTagsUtil.getInstance().getCacheTag(Integer.parseInt(tags[0]));
                    kaochaNlList.add(tag.getName());
                }else if(tagFP.startsWith(jingpin+"_")){
                    logger.error(jingpin+"精品标签："+tagFP);
                    //处理精品
                    question.setGood(true);
                }
            }
            String cloudTag=PropertiesConfigUtils.getProperty(GlobalConstant.CLOUD_TAG_NAME);
            String convertCallback=PropertiesConfigUtils.getProperty("convert_callback");
            if(!"159469".equals(cloudTag)&&StringUtils.isNotBlank(convertCallback)&&convertCallback.indexOf("trunk")>0){
                //不是neibu环境，组织trunk测试数据
//                int[] qx={93478,93479,93480,93481,93482,93483,125768,125763,125764,125765,125766,125767,93484,93485};
                  int[] qx={93563,93560,93561,155983,93562,93563,93564,93565,93566,93567,93568,93569,93570,93571};
                Random r=new Random();
                questionTypeS=qx[r.nextInt(qx.length-1)];
                questionTypeN=KlbTagsUtil.getInstance().getCacheTag(questionTypeS).getName();
            }
            question.setKaoChaNl(StringUtils.join(kaochaNlList, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
            question.setQuestionTypeSX(questionTypeS);question.setQuestionTypeSXN(questionTypeN);
            question.setKnowledgeTags(StringUtils.join(knowList, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
            question.setKnowledgeTagsFullPath(StringUtils.join(knowListFullPath, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
            question.setTeacheringTags(StringUtils.join(teacList, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
            question.setTeacheringTagsFullPath(StringUtils.join(teacListFullPath, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
    /**2.封装题目文本创建索引
     * 1.第一次获取缓存数据时调用更新到Question对象中
     * 2.新入库的题调用
     * */
    public void  parseQuestionSearchContent(IExamQuestionDto questionDto){
        int questionType=questionDto.getQuestionType();
        String searchContent="";
        questionDto.getQuestionDto().getQuestion().setExplan(HtmlCssUtil.parseImageUrl(questionDto.getQuestionDto().getQuestion().getExplan()));
        // 判断
        switch (questionType) {
            case Question.QUESTION_TYPE_DANXUAN:
            case Question.QUESTION_TYPE_DUOXUAN:
            case Question.QUESTION_TYPE_SORT:
                ChoiceQuestionDto cq= (ChoiceQuestionDto) questionDto;
                searchContent= cq.getChoiceQuestion().getTopic();
                cq.getChoiceQuestion().setTopic(HtmlCssUtil.parseImageUrl(cq.getChoiceQuestion().getTopic()));
                if(cq.getChoiceAnswers()!=null&&cq.getChoiceAnswers().size()>0){
                    for(int i=0;i<cq.getChoiceAnswers().size();i++){
                        cq.getChoiceAnswers().get(i).setDescription(HtmlCssUtil.parseImageUrl( cq.getChoiceAnswers().get(i).getDescription()));
                    }
                }
//                searchContent=cq.getShortTopic()+cq.getQuestionDto().getQuestion().getExplan()+ cq.getChoiceQuestion().getTopic();
                break;
            case Question.QUESTION_TYPE_FILL_BLANK:
            case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
                EssayQuestionDto sq= (EssayQuestionDto) questionDto;
                searchContent=sq.getEssayQuestion().getTopic() ;
                sq.getEssayQuestion().setTopic(HtmlCssUtil.parseImageUrl(sq.getEssayQuestion().getTopic()));
//                searchContent=sq.getShortTopic()+sq.getQuestionDto().getQuestion().getExplan()+sq.getEssayQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_READ:
            case Question.QUESTION_TYPE_LISTEN:
                ComplexQuestionDto cop= (ComplexQuestionDto) questionDto;
                searchContent=cop.getComplexQuestion().getTopic() ;
                cop.getComplexQuestion().setTopic(HtmlCssUtil.parseImageUrl(cop.getComplexQuestion().getTopic()));
//                searchContent=cop.getShortTopic()+cop.getQuestionDto().getQuestion().getExplan()+cop.getComplexQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
            case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
            case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
                ComplexQuestionDto cxp= (ComplexQuestionDto) questionDto;
                searchContent= cxp.getComplexQuestion().getTopic() ;
                cxp.getComplexQuestion().setTopic(HtmlCssUtil.parseImageUrl(cxp.getComplexQuestion().getTopic()));
//                searchContent=cxp.getShortTopic()+cxp.getQuestionDto().getQuestion().getExplan()+cxp.getComplexQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_SHORT:
                ShortQuestionDto shp= (ShortQuestionDto) questionDto;
                searchContent= shp.getShortQuestion().getTopic() ;
                shp.getShortQuestion().setTopic(HtmlCssUtil.parseImageUrl(shp.getShortQuestion().getTopic()));
                shp.getShortQuestion().setAnswerreference(HtmlCssUtil.parseImageUrl(shp.getShortQuestion().getAnswerreference()));
//                searchContent=shp.getShortTopic()+shp.getQuestionDto().getQuestion().getExplan()+shp.getShortQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_SPOKEN:
                SpokenQuestionDto skp= (SpokenQuestionDto) questionDto;
                searchContent= skp.getSpokenQuestion().getTopic();
                skp.getSpokenQuestion().setTopic(HtmlCssUtil.parseImageUrl(skp.getSpokenQuestion().getTopic()));
                skp.getSpokenQuestion().setAnswerreference(HtmlCssUtil.parseImageUrl(skp.getSpokenQuestion().getAnswerreference()));
//                searchContent=skp.getShortTopic()+skp.getQuestionDto().getQuestion().getExplan() +skp.getSpokenQuestion().getTopic();
                break;
            case Question.QUESTION_TYPE_CORRECTION:
                ComplexCorrectionQuestionDto ccp= (ComplexCorrectionQuestionDto) questionDto;
                searchContent= ccp.getComplexQuestion().getTopic() ;
                ccp.getComplexQuestion().setTopic(HtmlCssUtil.parseImageUrl(ccp.getComplexQuestion().getTopic()));
//                searchContent=ccp.getShortTopic()+ccp.getQuestionDto().getQuestion().getExplan()+ccp.getComplexQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_WHRITE:
                WhriteQuestionDto wp= (WhriteQuestionDto) questionDto;
                searchContent= wp.getWhriteQuestion().getTopic() ;
                wp.getWhriteQuestion().setTopic(HtmlCssUtil.parseImageUrl(wp.getWhriteQuestion().getTopic()));
                wp.getWhriteQuestion().setAnswerreference(HtmlCssUtil.parseImageUrl( wp.getWhriteQuestion().getAnswerreference()));
//                searchContent=wp.getShortTopic()+wp.getQuestionDto().getQuestion().getExplan()+wp.getWhriteQuestion().getTopic() ;
                break;
            case Question.QUESTION_TYPE_TABLE://表格（showForm 1：拖拽型，2：表格型）:
                MatrixQuestionDto mxp= (MatrixQuestionDto) questionDto;
                searchContent= mxp.getMatrixQuestion().getTopic() ;
                mxp.getMatrixQuestion().setTopic(HtmlCssUtil.parseImageUrl(mxp.getMatrixQuestion().getTopic()));
//                searchContent=mxp.getShortTopic()+mxp.getQuestionDto().getQuestion().getExplan()+mxp.getMatrixQuestion().getTopic() ;
                break;
            default:

        }
        //处理图片路径
        questionDto.getQuestionDto().getQuestion().setSearchContent(searchContent);
    }
    /***
     * 3.封装使用次数，收藏人等
     * @param dto
     */
    private void parseQuestionCollectionUser(IExamQuestionDto dto) {
        Question question=dto.getQuestionDto().getQuestion();
        List<Integer> collectionUserIds = resourceDao.findCollectionByResourceId(question.getId(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
        String userIds = StringUtils.join(collectionUserIds, "_");
        dto.getQuestionDto().getQuestion().setUserCollectionIds(userIds);//设置收藏用户id
        Set<Integer> recordUserIds = resourceDao.findUseRecordUser(question.getId(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
        String ruserIds = StringUtils.join(recordUserIds, "_");
        dto.getQuestionDto().getQuestion().setUserUseIds(ruserIds);//设置使用用户id
        int useTimes=resourceDao.findUseTimes(question.getId(),GlobalConstant.KLB_OBJECT_TYPE_QUESTION);
        dto.getQuestionDto().getQuestion().setUseTimes(useTimes);
    }
//    public static String parseImageUrl(String html){
//        try {
//            if(StringUtils.isBlank(html)) return "";
//            html=html.replace("\"","'");
//            String IMGURL_REG ="src=\\'([^\\']+)\\'";
//            Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html.toLowerCase()); //将正则编译到模式中，并匹配内容
//            while (matcher.find()) {          //尝试查找与该模式匹配的输入序列的下一个子序列
//                String url=matcher.group();//src='/upload/office-word/img/exama934f8e4b434467184a1356bf68f51ed.png'
//                int begin=url.indexOf("'");
//                int end=url.lastIndexOf("'");
//                String newUrl=url.substring(begin+1,end);
//                if(!newUrl.startsWith("http")){
//                    newUrl="http://"+ IndexNameUtils.getValueByKey("exam.host")+newUrl;
//                }
//                String newSrc="src='"+newUrl+"'";
//                html=html.toLowerCase().replace(url,newSrc);
//            }
//            return html;
//        }catch (Exception e){
//            e.printStackTrace();
//            return html;
//        }
//    }

    public static void main(String[] args) {
        String s="<div><img src=\"/upload/img/e.png\"><span>我的租个</span></div><img src=\"/rrrrrr/img/e.png\">";
        System.out.println(HtmlCssUtil.parseImageUrl(s));
    }

    @Override
	public Question getQuestionByCode(String code) {
		return questionDao.getQuestionByCode(code);
	}
	
	@Override
	public Question getQuestionById(int questionId) {
		Question question=questionDao.getQuestionById(questionId);
		return question;
	}
	
	@Override
	public List<Question> getSubQuestions(int teId) throws Exception
	{
		List<Question> result = null;
		try
		{
			//result = this.questionRepository.getSubQuestionsFecth(teId);
			result = questionDao.getQuestionByTeIds(teId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<Integer> getQuestionIdsByTeIdRepository(int teId) throws Exception {
		return questionDao.getQuestionIdByTeIds(teId);
	}
	
	  /**
	  *  根据试题code
	  * @return
	  * @throws Exception
	  */
	 public  IExamQuestionDto findQuestionByCode(String questionCode)
	         throws Exception {
	     try {
	         Question q = questionDao.getQuestionByCode( questionCode);
             if(q==null) return null;
	         IExamQuestionDto dto = this.getExamQuestionNoCache(q.getQuestionTypeId(), q.getId());
	        return dto;
	     } catch (Exception e) {
	         logger.error(e);
	         throw e;
	     }
	 }
	
	/**
   * 考试试题数据保存
   * @param conn
   * @param
   * @throws Exception
   */
	public void saveExamQuestion(Connection conn,
			IExamQuestionDto questionDto) throws Exception {
		Question question = null;
      try {
			int questionType = -1;
			question = questionDto.getQuestionDto().getQuestion();
			if (question == null){
			    question = new Question();
			    return;
			}
			// 创建人
			question.setCreateBy("新东方");
			question.setId(0);
			questionDto.getQuestionDto().setSaveType(ConstantTe.QUESTION_SAVETYPE_SAVE);
			Question oriQ = questionDao.getQuestionByCode(question.getCode());
          boolean newQuestion=true;
			if(oriQ!=null){
				//更新原来试题状态 0失效  1新版本为 1
				questionDao.updateNewVersion(conn, question.getCode(), 0);
				//设置新版本和状态
				int maxVersion=questionDao.getMaxVersion(conn,question.getCode());
				question.setVersion(maxVersion+1);
				question.setLastUpdateDate(new Date());
              newQuestion=false;
			}
			// 2 更新表签
			questionType = questionDto.getQuestionType();
			// 判断
			switch (questionType) {
			    case Question.QUESTION_TYPE_DANXUAN:
			    case Question.QUESTION_TYPE_DUOXUAN:
			    case Question.QUESTION_TYPE_SORT:
			        choiceQuestionService.saveOrUpdate(conn, (ChoiceQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_FILL_BLANK:
				case Question.QUESTION_TYPE_FILL_CALCULATION://计算填空题
			        essayQuestionService.saveOrUpdate(conn, (EssayQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_READ:
				case Question.QUESTION_TYPE_LISTEN:
			        readQuestionService.saveSyncRead(conn, (ComplexQuestionDto) questionDto);
			        break;
				case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
				case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
				case Question.QUESTION_TYPE_CHOICE_BLANK://选择填空题
					complexQuestionService.saveOrUpdate(conn, (ComplexQuestionDto) questionDto);
					break;
			    case Question.QUESTION_TYPE_SHORT:
			        shortQuestionService.saveOrUpdate(conn, (ShortQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_SPOKEN:
			    	spokenQuestionService.saveOrUpdate(conn, (SpokenQuestionDto) questionDto);
			    	break;
			    case Question.QUESTION_TYPE_CORRECTION:
			        correctionQuestionService.saveOrUpdate(conn, (ComplexCorrectionQuestionDto) questionDto);
			        break;
			    case Question.QUESTION_TYPE_WHRITE:
			    	whriteQuestionService.saveOrUpdate(conn, (WhriteQuestionDto) questionDto);
			    	break;
			    case Question.QUESTION_TYPE_TABLE://表格（showForm 1：拖拽型，2：表格型）:
			    	matrixQuestionService.saveOrUpdate(conn, (MatrixQuestionDto) questionDto,questionDto.getQuestionDto().getSaveType());
			    	break;
			    default:
			        // 无法使用的试题
			        throw new Exception("无法使用的试题,试题编码code:"+questionDto.getQuestionDto().getQuestion().getCode());
			}
          /**封装题目搜索文本信息*/
           parseQuestionSearchContent(questionDto);
          if(newQuestion){
              //新建索引
              questionService.createQuestionIndex(question);
          }else{
              //更新索引
              questionService.deleteQuestionIndex(oriQ);
              questionService.createQuestionIndex(question);
          }
		} catch (Exception e) {
          DataSync.updateDataSuncFromCache(question.getCode() + "<_>保存失败！",e);
          e.printStackTrace();
			logger.error("qid:"+question.getId()+",qcode:"+question.getCode()+",qtype:"+question.getQuestionTypeId(),e);
		}
      
	}
	
	@Override
	public void delCommQuestion(Connection conn ,int questionId){
		List<Integer> list=new ArrayList<Integer>();
		list.add(questionId);
		questionAttachDao.deleteAttatchsByQuestionIds(conn, list);
		questionDao.deleteByIds(conn, list);
	}
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	
	
	
	
	
	//以上方法为更新后方法
	
	@Override
	public List<QuestionAttach> getAttchesByQuestionId(int questionId) {
		List<QuestionAttach> list=questionAttachDao.getByQuestionid(questionId);
		return list;
	}

	@Override
	public List<ChoiceAnswer> getChoiceAnswernByChoiceId(int choiceId) {
		List<ChoiceAnswer>  list=choiceAnswerDao.getByChoiceId(choiceId);
		return list;
	}

	@Override
	public ChoiceQuestion getChoiceQuestionByQuestionId(int questionId) {
		ChoiceQuestion question=choiceQuestionDao.getByQuestionid(questionId);
		return question;
	}


	@Override
	public IExamQuestionDto getExamQuestion(int typeId, int questionId) throws Exception {
		//TODO .... 需要增加缓存 已经存在的缓存对象不在从数据库查询
		IExamQuestionDto examQuestionDto=null;
		List<Integer> ids=new ArrayList<Integer>();
		ids.add(questionId);
		switch (typeId) {
			case Question.QUESTION_TYPE_DANXUAN:
			case Question.QUESTION_TYPE_DANXUAN_BOX:
			case Question.QUESTION_TYPE_DANXUAN_GRAPH:
			case Question.QUESTION_TYPE_DANXUAN_POINT:
			case Question.QUESTION_TYPE_DANXUAN_SHADE:
			case Question.QUESTION_TYPE_DUOXUAN:
			case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
			case Question.QUESTION_TYPE_DUOXUAN_SHADE:
			case Question.QUESTION_TYPE_JUDGE:
			case Question.QUESTION_TYPE_SORT:	
			case Question.QUESTION_TYPE_READ_MULTICHOICE:
//				Question question=questionDao.getQuestionById(questionId);
//				ChoiceQuestion choiceQuestion=choiceQuestionDao.getByQuestionid(questionId);
//				QuestionDto questionDto=questionService.getQuestionDtoByQuestionId(questionId);
//				ChoiceQuestionDto dto=new ChoiceQuestionDto(choiceQuestion);
//				dto.setQuestionDto(questionDto);
//				ChoiceQuestionDto dto=choiceQuestionService.getChoiceQuestion(questionId);
				
				ChoiceQuestionDto dto=choiceQuestionService.batchFindRepository(ids).get(0);
				examQuestionDto= dto;
				break;
			case Question.QUESTION_TYPE_FILL_BLANK:
			case Question.QUESTION_TYPE_FILL_CALCULATION:
				EssayQuestionDto essayDTO=essayQuestionService.batchFindRepository(ids).get(0);
				examQuestionDto= essayDTO;
				 break;
			case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			case Question.QUESTION_TYPE_COMPOSITE_DICTATION:
			case Question.QUESTION_TYPE_CHOICE_BLANK:
			case Question.QUESTION_TYPE_CHOICE_WORD:
			case Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD:
			case Question.QUESTION_TYPE_ORAL_TRAINING:
				ComplexQuestionDto complexDto = complexQuestionService.batchFindRepository(ids).get(0);
				 examQuestionDto = complexDto;
				 break;
			case Question.QUESTION_TYPE_SHORT:
				ShortQuestionDto shortDto = shortQuestionService.getShortQuestionListRepository(ids).get(0);
				examQuestionDto = shortDto;
				break;
			case Question.QUESTION_TYPE_SPOKEN:
				SpokenQuestionDto  spokenDto = spokenQuestionService.getSpokenQuestionListRepository(ids).get(0);
				examQuestionDto = spokenDto;
				break;
			case Question.QUESTION_TYPE_READ:
			case Question.QUESTION_TYPE_LISTEN:
				ComplexQuestionDto readDto= readQuestionService.batchFindRepository(ids).get(0);
				examQuestionDto = readDto;
				break;
			case Question.QUESTION_TYPE_CORRECTION:
				ComplexCorrectionQuestionDto complexCorrectionQuestionDto = correctionQuestionService.batchFindRepository(ids).get(0);
				examQuestionDto = complexCorrectionQuestionDto;
				break;
			case Question.QUESTION_TYPE_SUB_CORRECTION:
				CorrectionQuestionDto correctionQuestionDto = correctionQuestionService.batchFindSubRepository(ids).get(0);
				examQuestionDto = correctionQuestionDto;
				break;
			default:
				break;
		}
		return examQuestionDto;
	}

	@Override
	public Map<Integer, IExamQuestionDto> initExamQuestionDto(int type, List<Integer> ids) throws Exception {
		//TODO ....  需要增加缓存 已经存在的缓存对象不在从数据库查询
		Map<Integer,IExamQuestionDto> map=new HashMap<Integer,IExamQuestionDto>();
		switch (type) {
			case Question.QUESTION_TYPE_DANXUAN:
			case Question.QUESTION_TYPE_DANXUAN_BOX:
			case Question.QUESTION_TYPE_DANXUAN_GRAPH:
			case Question.QUESTION_TYPE_DANXUAN_POINT:
			case Question.QUESTION_TYPE_DANXUAN_SHADE:
			case Question.QUESTION_TYPE_DUOXUAN:
			case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
			case Question.QUESTION_TYPE_DUOXUAN_SHADE:
			case Question.QUESTION_TYPE_SORT:
			case Question.QUESTION_TYPE_JUDGE:
			case Question.QUESTION_TYPE_READ_MULTICHOICE:
				List<ChoiceQuestionDto> choiceList= choiceQuestionService.batchFindRepository(ids);
				for (ChoiceQuestionDto choiceQuestionDto : choiceList) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=choiceQuestionDto;
					map.put(choiceQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break; 
			case Question.QUESTION_TYPE_SHORT:
				List<ShortQuestionDto> shorts = shortQuestionService.getShortQuestionListRepository(ids);
				for (ShortQuestionDto shortQuestionDto : shorts) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=shortQuestionDto;
					map.put(shortQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			case Question.QUESTION_TYPE_SPOKEN:
				List<SpokenQuestionDto> spokens = spokenQuestionService.getSpokenQuestionListRepository(ids);
				for (SpokenQuestionDto spokenQuestionDto : spokens) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=spokenQuestionDto;
					map.put(spokenQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			case Question.QUESTION_TYPE_READ:
			case Question.QUESTION_TYPE_LISTEN:
				List<ComplexQuestionDto> list=readQuestionService.batchFindRepository(ids);
				for (ComplexQuestionDto complexQuestionDto : list) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=complexQuestionDto;
					map.put(complexQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			case Question.QUESTION_TYPE_COMPOSITE_DICTATION:
			case Question.QUESTION_TYPE_CHOICE_BLANK:
			//added @ 0326
			case Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK:
			//end	
			case Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD:
			case Question.QUESTION_TYPE_CHOICE_WORD:
			case Question.QUESTION_TYPE_ORAL_TRAINING:
				List<ComplexQuestionDto> complexs = complexQuestionService.batchFindRepository(ids);
				for (ComplexQuestionDto complexQuestionDto : complexs) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=complexQuestionDto;
					map.put(complexQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				 break;
			case Question.QUESTION_TYPE_FILL_BLANK:	
			case Question.QUESTION_TYPE_NORMAL_FILL_BLANK:
			case Question.QUESTION_TYPE_FILL_CALCULATION:
				List<EssayQuestionDto> essayList=essayQuestionService.batchFindRepository(ids);
				for (EssayQuestionDto essayQuestionDTO : essayList) {
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto=essayQuestionDTO;
					map.put(essayQuestionDTO.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			case Question.QUESTION_TYPE_CORRECTION:
				List<ComplexCorrectionQuestionDto> complexCorrectionQuestionDtos = correctionQuestionService.batchFindRepository(ids);
				for(ComplexCorrectionQuestionDto complexCorrectionQuestionDto : complexCorrectionQuestionDtos){
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto = complexCorrectionQuestionDto;
					map.put(complexCorrectionQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			case Question.QUESTION_TYPE_SUB_CORRECTION:
				List<CorrectionQuestionDto> correctionQuestionDtos = correctionQuestionService.batchFindSubRepository(ids);
				for(CorrectionQuestionDto correctionQuestionDto : correctionQuestionDtos){
					IExamQuestionDto examQuestionDto=null;
					examQuestionDto = correctionQuestionDto;
					map.put(correctionQuestionDto.getQuestionDto().getQuestion().getId(), examQuestionDto);
				}
				break;
			default:
				break;
		}
		return map;
		
	}

	/**
	 * 获取选择题ID
	 * @param choices
	 * @return
	 */
	private List<Integer> findchoiceIds(List<ChoiceQuestion> choices) {
		List<Integer> list=new ArrayList<Integer>();
		for(ChoiceQuestion choiceQuestion:choices){
			list.add(choiceQuestion.getId());
		}
		return list;
	}

	

	@Override
	public List<Question> searchQuestionByFilter(QuestionFilter filter) {
		return questionDao.searchQuestionByFilter(filter);
	}

	@Override
	public void authedQuestion(String[] ids,String status) {
		List<Integer> list=new ArrayList<Integer>();
		for(String id:ids){
			list.add(Integer.parseInt(id));
		}
		if(status.equals("2")){
			//废除  题目
			questionDao.batchFindWithNouse(list, list);
		}else{
			//审核
			questionDao.batchFindWithUnaudit(list,list);
		}
		questionDao.updateLastUpdateDate(list,new Date(System.currentTimeMillis()));
		for(String id:ids){
			CacheTools.delCache(ConstantTe.REPOSITORY_QUSTION_ID + id);
		}
	}
	private void delTag(Connection conn,int questionId){
		tagObjectService.deleteByObject(conn,questionId, ConstantTe.TAG_TYPE_QUESTION_ID);
	}
	@Override
	public void delQuestion(int id,QuestionDto dto) {
		Question question=questionDao.getQuestionById(id);
		int type=question.getQuestionTypeId();
		List<Integer> ids=new ArrayList<Integer>();
		ids.add(id);
		Connection conn=null;
		try{
		conn = ConnUtil.getTransactionConnection();
		switch (type) {
		case Question.QUESTION_TYPE_DANXUAN:
		case Question.QUESTION_TYPE_DANXUAN_BOX:
		case Question.QUESTION_TYPE_DANXUAN_GRAPH:
		case Question.QUESTION_TYPE_DANXUAN_POINT:
		case Question.QUESTION_TYPE_DANXUAN_SHADE:
		case Question.QUESTION_TYPE_DUOXUAN:
		case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
		case Question.QUESTION_TYPE_DUOXUAN_SHADE:
			choiceQuestionService.deleteChoiceQuestion(conn, id);
			break;
		case Question.QUESTION_TYPE_SHORT:
			shortQuestionService.deleteByIds(conn, ids);
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			spokenQuestionService.deleteByIds(conn, ids);
			break;
		case Question.QUESTION_TYPE_READ:
		case Question.QUESTION_TYPE_LISTEN:
			readQuestionService.deleteReadByQuestionId(conn, id);
			break;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
		case Question.QUESTION_TYPE_COMPOSITE_DICTATION:
		case Question.QUESTION_TYPE_CHOICE_BLANK:
		case Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD:
		case Question.QUESTION_TYPE_CHOICE_WORD:
		case Question.QUESTION_TYPE_ORAL_TRAINING:
			complexQuestionService.deleteComplexQuestion(conn, id);
			 break;
		case Question.QUESTION_TYPE_SORT:
		case Question.QUESTION_TYPE_JUDGE:
			choiceQuestionService.deleteChoiceQuestion(conn, id);
			break;
		case Question.QUESTION_TYPE_TABLE:
			matrixQuestionService.deleteMatrixQuestion(conn, id);
			break;
		case Question.QUESTION_TYPE_FILL_BLANK:	
		case Question.QUESTION_TYPE_FILL_CALCULATION:
			essayQuestionService.deleteEssayQuestion(conn, id);
			break;
		case Question.QUESTION_TYPE_CORRECTION:
			correctionQuestionService.deleteByQuestionId(conn, id);
			break;
		case Question.QUESTION_TYPE_WHRITE:
			whriteQuestionService.deleteByIds(conn, ids);
			break;
		default:
			break;
		}
		//questionDao.deleteById(conn, id);
		//delTag(conn, id);
		dto.setQuestion(question);
		//dto.save2ElasticSearch(dto, ConstantTe.KLB_OPERATIONTYPE_DELETE, 0);
		conn.commit();
		}catch(Exception e){
			e.fillInStackTrace();
			if(conn!=null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int searchTotalNumByFilter(QuestionFilter filter) {
		return questionDao.searchQuestionCountByFilter(filter);
	}

	public void setComplexQuestionService(ComplexQuestionService complexQuestionService) {
		this.complexQuestionService = complexQuestionService;
	}

	public ComplexQuestionService getComplexQuestionService() {
		return complexQuestionService;
	}

	@Override
	public QuestionAttach getQuestionAttach(int QuestionAttachId) {
		return questionAttachDao.getQuestionAttachById(QuestionAttachId);
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	public ChoiceQuestionService getChoiceQuestionService() {
		return choiceQuestionService;
	}

	public void setChoiceQuestionService(ChoiceQuestionService choiceQuestionService) {
		this.choiceQuestionService = choiceQuestionService;
	}

	public ReadQuestionService getReadQuestionService() {
		return readQuestionService;
	}

	public void setReadQuestionService(ReadQuestionService readQuestionService) {
		this.readQuestionService = readQuestionService;
	}

	public EssayQuestionService getEssayQuestionService() {
		return essayQuestionService;
	}

	public void setEssayQuestionService(EssayQuestionService essayQuestionService) {
		this.essayQuestionService = essayQuestionService;
	}


    


    @Override
	public List<Questiontype> getAllTypes() {
		return questionDao.getAllTypes();
	}

	@Override
	public void updateTagFullPath(int id, String fullPath) throws Exception {
		questionDao.updateTagFullPath(id, fullPath);
		
	}

	public TagObjectService getTagObjectService() {
		return tagObjectService;
	}

	public void setTagObjectService(TagObjectService tagObjectService) {
		this.tagObjectService = tagObjectService;
	}
	@Override
	public Question getQuestionRepository(int id) throws Exception {
		return null;
	}

	public CorrectionQuestionService getCorrectionQuestionService() {
		return correctionQuestionService;
	}

	public void setCorrectionQuestionService(CorrectionQuestionService correctionQuestionService) {
		this.correctionQuestionService = correctionQuestionService;
	}

	@Override
	public String getTopic(int typeId,int questionId, int maxLength) throws Exception {
		String topic = "";
		switch (typeId) {
		case Question.QUESTION_TYPE_DANXUAN:
		case Question.QUESTION_TYPE_DANXUAN_BOX:
		case Question.QUESTION_TYPE_DANXUAN_GRAPH:
		case Question.QUESTION_TYPE_DANXUAN_POINT:
		case Question.QUESTION_TYPE_DANXUAN_SHADE:
		case Question.QUESTION_TYPE_DUOXUAN:
		case Question.QUESTION_TYPE_DUOXUAN_GRAPH:
		case Question.QUESTION_TYPE_DUOXUAN_SHADE:
		case Question.QUESTION_TYPE_READ_MULTICHOICE:
		case Question.QUESTION_TYPE_JUDGE:
		case Question.QUESTION_TYPE_SORT:
			ChoiceQuestion choiceQuestion = choiceQuestionDao.getByQuestionid(questionId);
			if(choiceQuestion != null){
				topic = choiceQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_SHORT:
			ShortQuestion shortQuestion = shortQuestionDao.getByQuestionid(questionId);
			if(shortQuestion != null){
				topic = shortQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_SPOKEN:
			SpokenQuestion spokenQuestion = spokenQuestionDao.getByQuestionid(questionId);
			if(spokenQuestion != null){
				topic = spokenQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_FILL_BLANK:
		case Question.QUESTION_TYPE_FILL_CALCULATION:
			EssayQuestion essayQuestion = essayQuestionDAO.getByQuestionid(questionId);
			if(essayQuestion != null){
				topic = essayQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
		case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
		case Question.QUESTION_TYPE_COMPOSITE_DICTATION:
		case Question.QUESTION_TYPE_CHOICE_BLANK:
		case Question.QUESTION_TYPE_STANDALONE_CHOICE_WORD:
		case Question.QUESTION_TYPE_CHOICE_WORD:
		case Question.QUESTION_TYPE_ORAL_TRAINING:
		case Question.QUESTION_TYPE_LISTEN_CHOICE_BLANK:
		case Question.QUESTION_TYPE_READ:
		case Question.QUESTION_TYPE_LISTEN:
		case Question.QUESTION_TYPE_CORRECTION:
			ComplexQuestion complexQuestion = complexQuestionDao.getByQuestionId(questionId);
			if(complexQuestion != null){
				topic = complexQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_TABLE:
			MatrixQuestion matrixQuestion = matrixQuestionDao.getByQuestionId(questionId);
			if(matrixQuestion != null){
				topic = matrixQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_WHRITE:
			WhriteQuestion whriteQuestion = whriteQuestionDao.getByQuestionid(questionId);
			if(whriteQuestion != null){
				topic = whriteQuestion.getTopic();
			}
			break;
		case Question.QUESTION_TYPE_SUB_CORRECTION:
			CorrectionQuestion correctionQuestion = correctionQuestionDao.getByQuestionId(questionId);
			if(correctionQuestion != null){
				topic = correctionQuestion.getTopic();
			}
			break;
		default:
			break;
		}
		if(StringUtils.isNotBlank(topic)){
			topic= HtmlUtil.Html2Text(topic);
			topic=HtmlUtil.delEscapeString(topic);
			if(topic.length()>maxLength){
				topic=topic.substring(0, maxLength);
			}
		}
		return topic;
	}

	@Override
	public List<Question> getLastOperateRecord(QuestionFilter filter) {
		return questionDao.getLastOperateRecord(filter);
	}

	//根据系统编号获得最近同步试题
	@Override
	public List<Question> getSyncQuestionBySysNo(QuestionFilter filter)
			throws Exception {
		return questionDao.getSyncQuestionBySysNo(filter);
	}

	//根据系统编号获得最近同步试题数量
	@Override
	public int getSyncQuestionBySysNoCount(QuestionFilter filter)
			throws Exception {
		return questionDao.getSyncQuestionBySysNoCount(filter);
	}






	//批量查找题目topic
	public Map<String, String> findQuestionTopicByCodes(List<String> questionCodes,
			Integer schoolId) {
		Map<String, String> topicMap = new HashMap<String,String>();
		try {
			if(questionCodes==null)return null;
			for(String questionCode:questionCodes){
				Question q = questionDao.getQuestionByCode( questionCode);
				if(q!=null){
					String topic = this.getTopic(q.getQuestionTypeId(), q.getId(),30);
					if(StringUtils.isBlank(topic)||StringUtils.isBlank(topic.trim())){
						topic =  QuestionUtil.getQuestionTypeName(q.getQuestionTypeId());
					}
					topicMap.put(questionCode, topic);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return topicMap;
	}
	//批量查找题目topic
	public Map<String, String> findQuestionTopicByIds(List<Integer> questionIds,Integer schoolId) {
		Map<String, String> topicMap = new HashMap<String,String>();
		try {
			if(questionIds==null || questionIds.size()==0)return null;
			long a1 = System.currentTimeMillis();
			List<Question> qList = questionDao.batchFind(questionIds);
			long a2 = System.currentTimeMillis();
			logger.info("批量查question时间："+(a2-a1));
			if( qList==null ||qList.size()<1)return null;
			for(Question q:qList){
				//Question q = questionDao.getQuestionById(questionId);
				if(q!=null){
					int questionId = q.getId();
					String topic = "";
					if(q.getTeId()==0 && q.getStatus()==Question.QUESTION_STATUS_AUDIT){
						//题目审核后取topic缓存
						topic = CacheTools.getCache(ConstantTe.REPOSITORY_QUESTION_TOPIC+questionId, String.class);
					}
					if(StringUtils.isBlank(topic)){
						topic = this.getTopic(q.getQuestionTypeId(), q.getId(),30);
						CacheTools.addCache(ConstantTe.REPOSITORY_QUESTION_TOPIC+questionId, topic);
					}
					if(StringUtils.isBlank(topic)||StringUtils.isBlank(topic.trim())){
						topic = QuestionUtil.getQuestionTypeName(q.getQuestionTypeId());
					}
					topicMap.put(questionId+"", topic);
				}
			}
			long a3 = System.currentTimeMillis();
			logger.info("查topic时间："+(a3-a2));
		} catch (Exception e) {
			logger.error(e);
		}
		return topicMap;
	}
}


