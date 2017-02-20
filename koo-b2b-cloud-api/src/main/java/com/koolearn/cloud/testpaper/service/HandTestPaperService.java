package com.koolearn.cloud.testpaper.service;

import java.util.List;

import com.koolearn.cloud.exam.examcore.paper.entity.PaperTemplateStructure;
import com.koolearn.exam.structure.entity.TeTestPaperInfo;
import com.koolearn.klb.tags.entity.Tags;

public interface HandTestPaperService {

	public Tags getTagsById(Integer id);
	/**
	 * * @Description: TODO(parentId==null获取综合模考一级分类  parentId！=null  获取直接子级集合) 
	   *  @return    
	   * @return List<Tags>    
	   * @author: 葛海松
	   * @time:    2015年3月4日 上午11:25:48 
	   * @throws
	 */
	public List<Tags> getCateTags(Integer parentId);
	/**
	 * * @Description: TODO(parentId==null获取综合模考一级分类  parentId！=null  获取直接子级集合
	 *                     只显示学校购买的一级分类) 
	   *  @param parentId
	   *  @param schoolId
	   *  @return    
	   * @return List<Tags>    
	   * @author: 葛海松
	   * @time:    2015年4月17日 下午7:47:57 
	   * @throws
	 */
	public List<Tags> getCateTags(Integer parentId,Integer schoolId);
	/**
	 * * @Description: TODO(缓存获取标签  过期试卷6小时) 
	   *  @param object
	   *  @param schoolId
	   *  @return    
	   * @return List<Tags>    
	   * @author: 葛海松
	   * @time:    2015年6月8日 上午10:37:23 
	   * @throws
	 */
	public List<Tags> getCateTagsCache(Integer parentId,Integer schoolId);
	public List<Tags> getCateTagsCache(Integer paperType);
	/**
	 * * @Description: TODO(根据试卷分类获得模版数据) 
	   *  @param paperTagId
	   *  @return    
	   * @return List<TemplateDto>    
	   * @author: 葛海松
	   * @time:    2015年3月5日 上午11:01:05 
	   * @throws
	 */
	public List<PaperTemplateStructure> getTemplateByPaperTagId(Integer paperTagId);
	/**
	 * * @Description: TODO(根据分类名称获取分类id 
	 *                       topTagId ==null 表示查询topTag
	 *                       topTagId !=null  表示查询topTag下的标签  ) 
	   *  @param tagName
	   *  @return    
	   * @return Integer    
	   * @author: 葛海松
	   * @time:    2015年3月6日 下午1:11:37 
	   * @throws
	 */
	public Tags getTagByTagName(Integer topTagId, String tagName);
	/**
	 * * @Description: TODO(标签管理-属性标签-内容分类-综合模考   到 知识库-综合模考) 
	   *  @return    
	   * @return Integer    
	   * @author: 葛海松
	   * @time:    2015年3月11日 上午9:42:17 
	   * @throws
	 */
	public Integer toJSTagId(Integer tagId);
	/**
	 * * @Description: TODO( 知识库-综合模考   到  标签管理-属性标签-内容分类-综合模考  ) 
	   *  @param tagId
	   *  @return    
	   * @return Integer    
	   * @author: 葛海松
	   * @time:    2015年3月11日 上午9:44:07 
	   * @throws
	 */
	public Integer fromJSTagId(Integer tagId);

	/**
	 * * @Description: TODO(判断标签是否是江苏平台使用的标签 （知识库-综合模考） ) 
	   *  @param tagId
	   *  @return    
	   * @return boolean    
	   * @author: 葛海松
	   * @time:    2015年3月11日 上午10:28:49 
	   * @throws
	 */
	public boolean isJSTags(Integer tagId);
	/**
	 * * @Description: TODO(获取江苏考试平台所有标签（同时封装子标签集合）) 
	   *  @return    
	   * @return List<Tags>    
	   * @author: 葛海松
	   * @time:    2015年3月17日 下午12:12:48 
	   * @throws
	 */
	public List<Tags> findTagsWithChildTags();
	/**
	 * 通过标签id集合获取标签列表
	 * @param tagIds
	 * @return
	 */
	public List<Tags> getTagsByIds(String tagIds);

    /**
     * 同步时发现，已分配的试卷没有同步过来，在保存一次试卷
     * @param paperInfo
     * @param schoolId
     * @return
     */
    boolean checkPageExistOfSchool(TeTestPaperInfo paperInfo, int schoolId);
}
