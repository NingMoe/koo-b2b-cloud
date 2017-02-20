package com.koolearn.cloud.util;

import com.koolearn.cloud.dictionary.entity.Dictionary;
import com.koolearn.cloud.resource.dto.TreeBean;
import com.koolearn.klb.tags.entity.Tags;
import com.koolearn.klb.tags.entity.TagsDesc;
import com.koolearn.klb.tags.service.TagsDescService;
import com.koolearn.klb.tags.service.TagsService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KlbTagsUtil implements ApplicationContextAware{
    private static final Logger logger = Logger.getLogger(KlbTagsUtil.class);
	private static String CACHE_KLBTAG = "klb_tags_childlist";
	private static String CACHE_KLBTAG_SINGLE = "cloud_klb_tags_single_";
    private static String CACHE_KLBTAG_TREE = "klb_tags_tree_";
	private TagsService tagsService;
	private TagsDescService tagsDescService;
	private static KlbTagsUtil instance = null;
    private static Map<String,Integer>  questionTypeMap=new HashMap<String, Integer>();
	public static KlbTagsUtil getInstance(){
		return instance;
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		tagsService = (TagsService) arg0.getBean("tagsService");
        tagsDescService = (TagsDescService) arg0.getBean("tagsDescService");
		KlbTagsUtil.instance = this;
        //加载学科学段相关题型属性标签
         loadQuestionTypeMap();
	}

    /**
     * 预加载学科题型标签
     * questionTypeMap   Key:subjectName_rangeName   valeu : 学科题型的id
     */
    private void loadQuestionTypeMap() {
        List<Tags> sxSubject = findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_KLBSX_QUESTION_TYPE);
        if (sxSubject != null && sxSubject.size() > 0) {
            for (int i = 0; i < sxSubject.size(); i++) {
                Tags subject = sxSubject.get(i);
                List<Tags> rangeList = getTagById(subject.getId());
                if (rangeList != null && rangeList.size() > 0) {
                    for (int j = 0; j < rangeList.size(); j++) {
                        Tags range = rangeList.get(j);
                        List<Tags> qtypeParent = getTagById(range.getId());
                        if (qtypeParent != null && qtypeParent.size() > 0) {
                            questionTypeMap.put(subject.getName().trim() + range.getName().trim(), qtypeParent.get(0).getId());
                        }
                    }
                }
            }
        }
    }

    /**
     * 查询tag
     *
     * @param tagId
     * @return
     */
    public Tags tagById(int tagId) {
        return tagsService.getTag(tagId);
    }


    /**
     * 查询某tagid下的tag列表
     *
     * @param tagId
     * @return
     */
    public List<Tags> getTagById(Integer tagId) {
        List<Tags> list = CacheTools.getCache(CACHE_KLBTAG + tagId, List.class);
        if (list == null || list.size() == 0) {
            list = tagsService.getTagsListByTagId(tagId);
            CacheTools.addCache(CACHE_KLBTAG + tagId, list);
        }
        return list;
    }

    public Tags getTag(int tagId) {
        return tagsService.getTag(tagId);
    }
    /**
     * 1查询某tagid下的tree列表
     *
     * @param tagId
     * @return
     */
    public List<TreeBean> getTagTreeById(int tagId) {
        CacheTools.delCache(CACHE_KLBTAG_TREE + tagId);
        List<TreeBean> TreeBeanList = CacheTools.getCache(CACHE_KLBTAG_TREE + tagId, List.class);
        TreeBean tb;
        if (TreeBeanList == null || TreeBeanList.size() == 0) {
            TreeBeanList = new ArrayList<TreeBean>();
            List<Tags> list = tagsService.getTagsListByTagId(tagId);
            for (Tags t : list) {
                tb = new TreeBean();
                tb.setpId(t.getParent_id());
                tb.setName(t.getName());
                tb.setId(t.getId());
                TreeBeanList.add(tb);
            }
            CacheTools.addCacheForever(CACHE_KLBTAG_TREE + tagId, TreeBeanList);
        }
        return TreeBeanList;
    }

    /**
     * 2查询某tagid下的tree列表(关联子节点)
     * @param id
     * @return
     */
    public List getTagTreeRefChildById(Integer id) {
        List<TreeBean> tagsList = KlbTagsUtil.getInstance().getTagTreeById(id);
        if (tagsList != null || tagsList.size() > 0) {
            for (int i = 0; i < tagsList.size(); i++) {
                TreeBean tb = tagsList.get(i);
                tb.setChild(KlbTagsUtil.getInstance().getTagTreeById(tb.getId()));
            }
        }
        return tagsList;
    }
    public  TreeBean  getTagByTagnameAndPrentId(String tagName,int prentTagId){
        List<TreeBean> childList=getTagTreeById(prentTagId);
        if(childList==null&&childList.size()<1)return null;
        for(TreeBean tb:childList){
            if(tb.getName().equals(tagName)){
                return tb;
            }
        }
        return null;
    }
    /**
     * 得到tags的full_id
     * @param tagId
     * @return
     */
    public String getTagFullId(Integer tagId){
        Tags tag= getCacheTag(tagId);
        if(tag==null){
            logger.info("标签不存在："+tagId);
            return "";
        }
        return tag.getFull_Path();
    }

    /**
     * 根据标签id集合生成fullpath   id串保存
     * @param tagIdList
     * @return
     */
    public String getAllTagListFullPath(Integer[] tagIdList){
        if(tagIdList==null || tagIdList.length<1) return "";
        List<String> tagListFullPath=new ArrayList<String>();
        for(Integer tagId:tagIdList){
            tagListFullPath.add(getTagFullId(tagId));
        }

        return StringUtils.join(tagListFullPath, GlobalConstant.KLB_KNOWLEDGE_LIST_SEPARATOR);
    }

    /**
     * 根据TagId 得到TAGName
     *
     * @param tagId
     * @return
     */
    public String getTagName(Integer tagId) {
        Tags tag=getCacheTag(tagId);
        return tag.getName();
    }
    /***获取缓存单个标签*/
    public Tags getCacheTag(Integer tagId){
        Tags tag=CacheTools.getCache(CACHE_KLBTAG_SINGLE+tagId,Tags.class);
        if(tag==null){
            tag = tagsService.getTag(tagId);
            CacheTools.addCache(CACHE_KLBTAG_SINGLE+tagId,tag);
        }
        return tag;
    }

     /**获取缓存单个标签的全路径节点名称(知识点和进度点)
     * :
     * @param tagFullPath   83699_83697_83604_83601_83600_2471_60
     * @param offset 知识点：偏移量0,  进度点偏移量1:   从必修这一级显示
     * @return
     */
    public String getCacheTagFullPathName(String tagFullPath,int offset){
            if(StringUtils.isBlank(tagFullPath)) return " ";
            int index=GlobalConstant.KLB_KNOWLEDGE_LEVEL_VALUE+offset;
            String[] tagFullPatArr = tagFullPath.split("_");
            String[] tagFullPathNameArr = new String[tagFullPatArr.length];
            String[] tagFullPathArrNameDest=new String[tagFullPathNameArr.length-index];
            ArrayUtils.reverse(tagFullPatArr);
            if(tagFullPath.endsWith("_60")&&tagFullPatArr.length>GlobalConstant.KLB_KNOWLEDGE_LEVEL_VALUE) {
             for (int i=0;i<tagFullPatArr.length;i++) {
                 Tags tag=getCacheTag(Integer.parseInt(tagFullPatArr[i]));
                 if(tag!=null){
                     tagFullPathNameArr[i]=getCacheTag(Integer.parseInt(tagFullPatArr[i])).getName();
                 }else{
                     logger.info("【资源】知识点、进度点标签【"+tagFullPatArr[i]+"】不存在>>>>>>>>>>>>>>>>>>>");
                 }
             }
                 System.arraycopy(tagFullPathNameArr,index,tagFullPathArrNameDest,0,tagFullPathArrNameDest.length);
         }
         return StringUtils.join(tagFullPathArrNameDest,GlobalConstant.KLB_KNOWLEDGE_FULLPATH_SEPARATOR);
     }

     /***通过数据字典父节点（唯一）查询klb标签列表
     * 1.获取klb地区标签  GlobalConstant.DICTIONARY_TYPE_KLBSX_AREA
     * 2.获取klb年份标签  GlobalConstant.DICTIONARY_TYPE_KLBSX_YEAR
     * *3.获取klb数据类型标签  GlobalConstant.DICTIONARY_TYPE_KLBSX_PAPERTYPE
     * 4.获取klb适用年级标签  GlobalConstant.DICTIONARY_TYPE_KLBSX_GRADE***/
    public List<Tags> findChildByDictionary(Integer type){
        List<Dictionary>  dictionary= DataDictionaryUtil.getInstance().getDataDictionaryListByType(type);
        if(dictionary==null ||dictionary.size()!=1) return null;
        List<Tags> tagsList=getTagById(dictionary.get(0).getValue());
        if(GlobalConstant.DICTIONARY_TYPE_QUESTION_HARD==type&&tagsList.size()>3){
            //处理题目难度标签
            List<Tags> tagss=new ArrayList<Tags>();
            tagss.add(tagsList.get(2));
            tagss.add(tagsList.get(1));
            tagss.add(tagsList.get(0));
            tagsList=tagss;
        }
        return tagsList;
    }
    /**
     * 获取试卷的全部使用年级
     * @return
     */
    public  Map<String,List<Tags>> findPagerAllGrade(){
        Map<String,List<Tags>> map=new HashMap<String, List<Tags>>();
        List<Tags>  xiaoZhong=findChildByDictionary(GlobalConstant.DICTIONARY_TYPE_KLBSX_GRADE);
        for(Tags t:xiaoZhong){
            if(GlobalConstant.SUBJECT_NAME_xiaoxue.equals(t.getName())){
                //拿到小学下面的 各年级标签
                map.put(GlobalConstant.SUBJECT_NAME_xiaoxue,getTagById(t.getId()) );
            }else{
                List<Tags>  zhong=getTagById(t.getId());//拿到中学标签下的：初中、高中、初小标签， 中学和小学平级
                for(Tags tt:zhong){
                    if(GlobalConstant.SUBJECT_NAME_chuzhong.equals(tt.getName())){
                        map.put(GlobalConstant.SUBJECT_NAME_chuzhong,getTagById(tt.getId()));
                    }else if(GlobalConstant.SUBJECT_NAME_gaozhong.equals(tt.getName())){
                        map.put(GlobalConstant.SUBJECT_NAME_gaozhong,getTagById(tt.getId()));
                    } else if(GlobalConstant.SUBJECT_NAME_chuxiao.equals(tt.getName())){
                        map.put(GlobalConstant.SUBJECT_NAME_chuxiao,getTagById(tt.getId()));
                    }
                }
            }
        }
        return map ;
    }

    /*** 根据 学科学段 获取试题类型 */
    public List<Tags> findQuestionType(Integer subject ,Integer range){
//        loadQuestionTypeMap();
        Tags sub=getCacheTag(subject);
        Tags ran=getCacheTag(range);
        //加载学科学段题型
        Integer questionTypeParentId=questionTypeMap.get(sub.getName().trim()+ran.getName().trim());
        if(questionTypeParentId==null){
            loadQuestionTypeMap();
            questionTypeParentId=questionTypeMap.get(sub.getName().trim()+ran.getName().trim());
        }
        if(questionTypeParentId==null) return  new ArrayList<Tags>();
        List list= getTagById(questionTypeParentId);
        return list;
    }
    /*** 根据 学科学段 获取试题类型 */
    public Map<String,Tags> findQuestionTypeMap(Integer subject ,Integer range){
        Map<String ,Tags> tagsMap=new HashMap<String, Tags>();
        List<Tags> tagses =findQuestionType(  subject ,  range);
        if(tagses!=null&&tagses.size()>0){
            for(Tags t:tagses){
                tagsMap.put(t.getName(),t);
            }
        }
        return tagsMap;
    }
    public String getTagsDesc(Integer tagId){
        TagsDesc td=tagsDescService.getTagDescByTagId(tagId);
        if(td==null) return "";
        return td.getDescription();
    }

    public static void main(String[] args) {
        System.out.println(Double.valueOf(( ParseDate.parse("2016-6-2 00:10:00").getTime()-ParseDate.parse("2016-6-1 23:55:00").getTime()) / 1000));
    }
}
