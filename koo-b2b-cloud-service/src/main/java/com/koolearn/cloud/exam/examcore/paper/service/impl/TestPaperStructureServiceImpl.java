package com.koolearn.cloud.exam.examcore.paper.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.exam.examcore.paper.dao.TestPaperStructureDao;
import com.koolearn.cloud.exam.examcore.paper.dto.PaperPager;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar;
import com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarType;
import com.koolearn.cloud.exam.examcore.paper.entity.TestPaperStructure;
import com.koolearn.cloud.exam.examcore.paper.service.TestPaperStructureService;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.KlbTagsUtil;
import com.koolearn.klb.tags.entity.Tags;
import org.apache.commons.lang.StringUtils;

public class TestPaperStructureServiceImpl implements TestPaperStructureService {
	private TestPaperStructureDao testPaperStructureDao;
	@Override
	public List<TestPaperStructure> findQuestionIdsByPaperId(int paperId) {
		List<TestPaperStructure> list=testPaperStructureDao.findItems4Type(paperId,TestPaperStructure.structure_type_question);
		return list;
	}
	public TestPaperStructureDao getTestPaperStructureDao() {
		return testPaperStructureDao;
	}
	public void setTestPaperStructureDao(TestPaperStructureDao testPaperStructureDao) {
		this.testPaperStructureDao = testPaperStructureDao;
	}
	@Override
	public List<TestPaperStructure> findItemsByPaperId(int paperId) {
		List<TestPaperStructure> list=testPaperStructureDao.findItemsByPaperId(paperId);
		return list;
	}

    @Override
    public QuestionBar findPaperBar(PaperPager filter) {
        QuestionBar qb=new QuestionBar();
        if(filter.getSubjectId()!=null){
            //设置回显字段
            qb.setSubjectId(filter.getSubjectId());
        }
        if(filter.getRangeId()!=null){
            qb.setRangeId(filter.getRangeId());
        }
        if(filter.getBookVersion()!=null){
            qb.setBookVersion(filter.getBookVersion());
        }
        if(filter.getObligatoryId()!=null){
            qb.setObligatoryId(filter.getObligatoryId());
        }
        List<QuestionBarType> questionBarTypeList=new ArrayList<QuestionBarType>();
        List<Tags> tagses= KlbTagsUtil.getInstance().findQuestionType(filter.getSubjectId(),filter.getRangeId());
        if(tagses==null || tagses.size()<1) return qb;
        for (Tags tag:tagses) {
            QuestionBarType qbt=new QuestionBarType();
            qbt.setName(tag.getName());
            qbt.setType(tag.getId());
            String desc=KlbTagsUtil.getInstance().getTagsDesc(tag.getId());
            qbt.setDefaultScore(StringUtils.isBlank(desc)? GlobalConstant.PAPER_QUESTION_DEFAULT_SCORE:Double.parseDouble(desc));
            questionBarTypeList.add(qbt);
        }
        qb.setQuestionTypeArr(questionBarTypeList);
        return qb;
    }


}
