package com.koolearn.cloud.util;

import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.dictionary.service.DictionaryService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



/**
 * 字典类型信息工具类.配置bean
 * @version 1.0
 */
public class DataDictionaryUtil implements ApplicationContextAware  {
	/** 学科类型 */
	public static final int DICTIONARY_DISCIPLINE_TYPE = 1;

	private static final String CACHE_DICTIONARY = "dictionary_cloud_";
    private static final String CACHE_DICTIONARY_SUBJECT_ = "dictionary_cloud_subject_";
	private DictionaryService dictionaryService;

	private static DataDictionaryUtil instance = null;

	public static DataDictionaryUtil getInstance() {
		return instance;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		dictionaryService = (DictionaryService) applicationContext
				.getBean("dictionaryService");
		DataDictionaryUtil.instance = this;
//		this.initDataDictionaryMap();
	}

	/**
	 * 初始化字典类型缓存MAP。
	 */
	public void initDataDictionaryMap() {
		// 字典按类型归类MAP
		HashMap<Integer, List<Dictionary>> dataDictionaryMap =
				new HashMap<Integer, List<Dictionary>>();
		// 获得所有可用的字典
		List<Dictionary> dataDictionaryList = dictionaryService.getDataDictionaryByStats(Dictionary.STATUS);

		// 判断是否有数据
		if (dataDictionaryList != null) {
			// 将字典归类
			for (Dictionary dd : dataDictionaryList) {
				dataDictionaryMap = setSubjectInfoToMap(dataDictionaryMap,dd);
			}

			// 获得归类类型迭代
			Iterator<Integer> iterator = dataDictionaryMap.keySet().iterator();
			// 迭代将分类下字典放入缓存
			while (iterator.hasNext()) {
				Integer type = iterator.next();
				// 存入缓存
				CacheTools.addCacheForever(CACHE_DICTIONARY + type, dataDictionaryMap.get(type));
			}
		}
	}

	/**
	 * 按字典数据type类型封装map对象
	 * @param dataDictionaryMap
	 * @param dd
	 * @return
	 */
	private HashMap<Integer, List<Dictionary>> setSubjectInfoToMap(
			HashMap<Integer, List<Dictionary>> dataDictionaryMap,
			Dictionary dd) {
		List<Dictionary> list = dataDictionaryMap.get(dd.getType());
		if (list == null) {
			list = new ArrayList<Dictionary>();
		}

		if (list.indexOf(dd) < 0) {
			list.add(dd);
		}

		dataDictionaryMap.put(dd.getType(), list);
		return dataDictionaryMap;
	}

	/**
	 * 根据类型获得字典信息分类集合.
	 *
	 * @param type
	 *            类型
	 * @return 字典信息分类集合
	 */
	public List<Dictionary> getDataDictionaryListByType(Integer type) {
        CacheTools.delCache(CACHE_DICTIONARY + type);
		@SuppressWarnings("unchecked")
		List<Dictionary> list = CacheTools.getCacheForever(CACHE_DICTIONARY + type, List.class);
		if (list == null || list.size() == 0) {
			list = dictionaryService.getDataDictionaryByType(type);
			CacheTools.addCacheForever(CACHE_DICTIONARY + type, list);
		}

		return list;
	}

    /**
     * 获取集合的第一个元素(考察能力、精品)的id
     * @return
     */
    public  Integer getFirstDictionaryByType(Integer type){
        List<Dictionary> dataList=getDataDictionaryListByType(  type);
        if(dataList==null || dataList.size()<1)return -1;
        return dataList.get(0).getValue();
    }
	/**
	 * 根据类型获得字典分类信息MAP.
	 * @param type  类型
	 * @return 字典分类信息MAP
	 */
	public HashMap<Integer, Dictionary> getDataDictionaryMapByType(Integer type) {
		HashMap<Integer, Dictionary> map = new HashMap<Integer, Dictionary>();
		List<Dictionary> list = this.getDataDictionaryListByType(type);
		if (list != null && list.size() > 0) {
			for (Dictionary dictionary : list) {
				map.put(dictionary.getValue(), dictionary);
			}
		}

		return map;
	}
	/**
	 * 根据类别和值获取字典数据
	 * @param type
	 * @param valueList
	 * @return
	 */
	public List<Dictionary> getDataDictionaryForList(int type,List<Integer> valueList){
	    List<Dictionary> list = this.getDataDictionaryListByType(type);
	    List<Dictionary> resultList = new ArrayList<Dictionary>();
	    for(Dictionary dic : list){
	        for(Integer value : valueList){
	            if(dic.getValue().intValue()==value.intValue()){
	                resultList.add(dic);
	                break;
	            }
	        }
	    }
	    return resultList;
	}
	/**
	 * 根据类型和名称获得字典分类信息.
	 *
	 * @param type
	 *            类型
	 * @param name
	 *            名称
	 * @return 字典分类信息
	 */
	public Dictionary getDataDictionaryListByTypeAndName(Integer type, String name) {
		List<Dictionary> list = this.getDataDictionaryListByType(type);
		if (list != null && list.size() > 0) {
			for (Dictionary dictionary : list) {
				if (dictionary.getName().equals(name)) {
					return dictionary;
				}
			}
		}

		return null;
	}

	/**
	 * 根据类型和值获得字典分类信息.
	 *
	 * @param type
	 *            类型
	 * @param value
	 *            值
	 * @return 字典分类信息
	 */
	public Dictionary getDictionaryListByTypeAndValue(Integer type,
			Integer value) {
		List<Dictionary> list = this.getDataDictionaryListByType(type);
		if (list != null && list.size() > 0) {
			for (Dictionary dictionary : list) {
				if (dictionary.getValue().equals(value)) {
					return dictionary;
				}
			}
		}

		return null;
	}
	/**
	 * 根据类型和值获得字典分类信息.
	 *
	 */
	public String getDictionaryName(Integer type,Integer value) {
		if(type==null ||value==null)return "";
		Dictionary d= getDictionaryListByTypeAndValue(type,value);
		if(d==null) return "";
		return d.getName();
	}


    /**
     * 根据类型获得字典信息分类集合.
     *
     * @param type
     *            类型
     * @return 字典信息分类集合
     */
    public List<Dictionary> getDataDictionaryListByTypeOrder(Integer type) {
        List<Dictionary> list = CacheTools.getCacheForever(CACHE_DICTIONARY_SUBJECT_ + type, List.class);
        if (list == null || list.size() == 0) {
            list = dictionaryService.getDataDictionaryByTypeOrder(type);
            CacheTools.addCacheForever(CACHE_DICTIONARY_SUBJECT_ + type, list);
        }

        return list;
    }
}
