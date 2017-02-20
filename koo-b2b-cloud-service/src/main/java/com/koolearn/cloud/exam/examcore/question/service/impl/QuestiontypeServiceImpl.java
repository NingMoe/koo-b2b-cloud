package com.koolearn.cloud.exam.examcore.question.service.impl;

import java.util.List;

import com.koolearn.cloud.exam.examcore.question.dao.QuestiontypeDao;
import com.koolearn.cloud.exam.examcore.question.entity.Questiontype;
import com.koolearn.cloud.exam.examcore.question.service.QuestiontypeService;
import com.koolearn.cloud.util.CacheTools;
import org.springframework.beans.factory.annotation.Autowired;


public class QuestiontypeServiceImpl implements QuestiontypeService {
	@Autowired
	private QuestiontypeDao questiontypeDao;

    public QuestiontypeDao getQuestiontypeDao() {
        return questiontypeDao;
    }

    public void setQuestiontypeDao(QuestiontypeDao questiontypeDao) {
        this.questiontypeDao = questiontypeDao;
    }

    @Override
	public String findItemName(int id) {
		Questiontype type= cacheItem(id);
		if(type==null){
			return null;
		}
		return type.getName();
	}
	private Questiontype cacheItem(int id){
		Questiontype questiontype= CacheTools.getCache(id + "", Questiontype.class);
		if(questiontype==null){
			questiontype= questiontypeDao.findItemById(id);
			CacheTools.addCache(id+"", questiontype);
			return questiontype;
		}
		return questiontype;
	}
	@Override
	public List<Questiontype> searchByParent(int parent) throws Exception
	{
		return this.questiontypeDao.selectByParent(parent);
	}
	@Override
	public List<Questiontype> searchAll() throws Exception
	{
		return this.questiontypeDao.selectAll();
	}

}
