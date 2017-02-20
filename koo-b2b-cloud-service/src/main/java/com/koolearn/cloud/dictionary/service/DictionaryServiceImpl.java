package com.koolearn.cloud.dictionary.service;

import com.koolearn.cloud.dictionary.dao.DictionaryDao;
import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.util.SelectDTO;

import java.util.List;

public class DictionaryServiceImpl implements DictionaryService{
	DictionaryDao dictionaryDao;
	public void setDictionaryDao(DictionaryDao dictionaryDao) {

		this.dictionaryDao = dictionaryDao;
	}

	/**
	 * 获得所有可用的字典
	 * @param status
	 * @return
	 */
	@Override
	public List<Dictionary> getDataDictionaryByStats(int status) {
		return dictionaryDao.getDataDictionaryByStats(status);
	}

	/**
	 * 根据类型获得字典信息分类集合
	 * @param type
	 * @return
	 */
	@Override
	public List<Dictionary> getDataDictionaryByType(Integer type) {
		return dictionaryDao.getDataDictionaryByType(type);
	}

    @Override
    public List<Dictionary> getDataDictionaryByTypeOrder(Integer type) {
        return dictionaryDao.getDataDictionaryByTypeOrder(type);
    }

	@Override
	public List<SelectDTO> findTeacherSubject(int teacherId) {
		return dictionaryDao.findTeacherSubject(teacherId);
	}

	@Override
	public List<SelectDTO> findTeacherRange(int teacherId, int subjectId) {
		return dictionaryDao.findTeacherRange(teacherId,subjectId);
	}

	@Override
	public List<SelectDTO> findTeacherBookVersion(int teacherId, int rangeId) {
		return dictionaryDao.findTeacherBookVersion(teacherId,rangeId);
	}

	@Override
	public List<SelectDTO> findTeacherSubjectName(int teacherId) {
		return dictionaryDao.findTeacherSubjectName(teacherId);
	}

    @Override
    public Dictionary queryDictionaryByTypeAndName(String name, Integer type) {
        Dictionary dictionary =dictionaryDao.queryDictionaryByNameAndType(name,type);
        return dictionary;
    }
}
