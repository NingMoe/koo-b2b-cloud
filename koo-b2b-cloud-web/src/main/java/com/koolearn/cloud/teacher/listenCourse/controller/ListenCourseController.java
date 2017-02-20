package com.koolearn.cloud.teacher.listenCourse.controller;

import com.koolearn.cloud.base.controller.BaseController;
import com.koolearn.cloud.login.entity.UserEntity;
import com.koolearn.cloud.teacher.listenCourse.player.Mp4Player;
import com.koolearn.library.maintain.course.entity.CourseUnit;
import com.koolearn.library.maintain.frontCategory.entity.ProductFrontCategory;
import com.koolearn.library.maintain.frontCategory.service.FrontCategoryService;
import com.koolearn.library.maintain.knowledge.entity.Knowledgeitem;
import com.koolearn.library.maintain.listen.service.ListenService;
import com.koolearn.library.maintain.product.entity.Product;
import com.koolearn.library.maintain.product.service.ProductService;
import com.koolearn.library.maintain.remote.service.InterfaceCloudService;
import com.koolearn.library.maintain.util.ListPage;
import com.koolearn.util.SystemGlobals;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 16/5/10.
 */
@Controller
@RequestMapping("/teacher/listenCourse/")
public class
        ListenCourseController extends BaseController {

    @Autowired(required = false)
    private InterfaceCloudService interfaceCloudService;
    @Autowired(required = false)
    private ProductService productService;
    @Autowired(required = false)
    private FrontCategoryService frontCategoryService;
    @Autowired(required = false)
    private ListenService listenService;

//    public static final int LIBRARY_ID = 14461;
    public static final int LIBRARY_ID = Integer.valueOf(SystemGlobals.getPreference("libraryId"));


    /**
     * 首页
     *
     * @param searchValue
     * @param map
     * @return
     */
    @RequestMapping("index")
    public String index(String searchValue, ModelMap map) {

        int pageNo = getParamterForInt("pageNo", 0);
        int oneCategoryId = getParamterForInt("oneCategoryId", 0);
        int subCategoryId = getParamterForInt("subCategoryId", 0);
        int threeCategoryId = getParamterForInt("threeCategoryId", 0);
        List<ProductFrontCategory> oneCategoryList = interfaceCloudService.findOneCategory(LIBRARY_ID);
        if(oneCategoryList != null && oneCategoryList.size() > 0 && oneCategoryId == 0){
            oneCategoryId = oneCategoryList.get(0).getId();
        }
        List<ProductFrontCategory> subCategoryList = interfaceCloudService.findSubCategory(LIBRARY_ID, oneCategoryId);
        if(subCategoryList != null && subCategoryList.size() > 0 && subCategoryId == 0){
            subCategoryId = subCategoryList.get(0).getId();
        }
        List<ProductFrontCategory> threeCategoryList = interfaceCloudService.findThreeCategory(LIBRARY_ID, subCategoryId);
        if(threeCategoryList != null && threeCategoryList.size() > 0 && threeCategoryId == 0){
            threeCategoryId = threeCategoryList.get(0).getId();
        }
        ListPage listPage = interfaceCloudService.findProductListPage(LIBRARY_ID, oneCategoryId, subCategoryId, threeCategoryId, pageNo, searchValue);

        map.addAttribute("searchValue", searchValue);
        map.addAttribute("oneCategoryList", oneCategoryList);
        map.addAttribute("subCategoryList", subCategoryList);
        map.addAttribute("threeCategoryList", threeCategoryList);
        map.addAttribute("listPager", listPage.getListPager());
        map.addAttribute("productList", listPage.getResultList());
        map.addAttribute("oneCategoryId", oneCategoryId);
        map.addAttribute("subCategoryId", subCategoryId);
        map.addAttribute("threeCategoryId", threeCategoryId);
        map.addAttribute("pageNo", pageNo);
        return "/teacher/listenCourse/listenCourseIndex";
    }

    /**
     * 产品详情
     *
     * @param productId
     * @param oneCategoryId
     * @param subCategoryId
     * @param threeCategoryId
     * @param map
     * @return
     */
    @RequestMapping("/detail/{productId}/{oneCategoryId}/{subCategoryId}/{threeCategoryId}/{pageNo}")
    public String detail(@PathVariable(value = "productId") Integer productId,
                         @PathVariable(value = "oneCategoryId") Integer oneCategoryId,
                         @PathVariable(value = "subCategoryId") Integer subCategoryId,
                         @PathVariable(value = "threeCategoryId") Integer threeCategoryId,
                         @PathVariable(value = "pageNo") Integer pageNo,
                         ModelMap map) {
        Product product = productService.getProductDetailById(productId);
        String teacherStr = productService.findTeacherByProductId(productId);
        //定位面包屑
        String oneName = frontCategoryService.getFrontCategoryNameById(oneCategoryId);
        String subName = frontCategoryService.getFrontCategoryNameById(subCategoryId);
        String threeName = frontCategoryService.getFrontCategoryNameById(threeCategoryId);
        //热门课程
        List<Product> productList = interfaceCloudService.findHotProductList(LIBRARY_ID, threeCategoryId);

        map.addAttribute("product", product);
        map.addAttribute("productList", productList);
        map.addAttribute("oneCategoryId", oneCategoryId);
        map.addAttribute("subCategoryId", subCategoryId);
        map.addAttribute("threeCategoryId", threeCategoryId);
        map.addAttribute("oneName", oneName);
        map.addAttribute("subName", subName);
        map.addAttribute("threeName", threeName);
        map.addAttribute("teacherStr", teacherStr);
        map.addAttribute("pageNo", pageNo);
        return "/teacher/listenCourse/listenCourseDetail";
    }

    /**
     * 课程表
     *
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping("/tree/{productId}")
    public List<Map> tree(@PathVariable(value = "productId") Integer productId) {
        return productService.getCourseUnit(productId);
    }

    /**
     * 播放
     *
     * @return
     */
    @RequestMapping("player/{productId}/{courseUnitId}/{oneCategoryId}/{subCategoryId}/{threeCategoryId}/{pageNo}")
    public String player(@PathVariable(value = "productId") Integer productId,
                         @PathVariable(value = "oneCategoryId") Integer oneCategoryId,
                         @PathVariable(value = "subCategoryId") Integer subCategoryId,
                         @PathVariable(value = "threeCategoryId") Integer threeCategoryId,
                         @PathVariable(value = "courseUnitId") Integer courseUnitId,
                         @PathVariable(value = "pageNo") Integer pageNo,
                         UserEntity user, ModelMap map, HttpServletRequest request) {

        //防止网路爬虫
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            String[] spiders = this.getSpiders();
            for (String spider : spiders) {
                if (spider != null && userAgent.indexOf(spider) != -1) {
                    return null;
                }
            }
        }

        //判断产品是否购买
        String[] productIds = interfaceCloudService.getProductIds(LIBRARY_ID);
        if (!isExistProductId(productIds, String.valueOf(productId))) {
            map.addAttribute("errorMessage", "尚未购买产品，无法播放！");
            return "/teacher/listenCourse/listenError";
        }
        Product product = productService.getProductDetailById(productId);
        CourseUnit courseUnit;

        if (user != null) {
            //添加课程相关记录
            courseUnit = setRecord(productId, courseUnitId, user);
        } else {
            //默认第一个节点开始播放
            courseUnit = initCourseUnit(productId, courseUnitId);
        }

        //不存在课程目录
        if (courseUnit == null) {
            map.addAttribute("errorMessage", "不存在课程目录");
            return "/teacher/listenCourse/listenError";
        }

        courseUnit.setProductId(productId);

        if (courseUnit.getKnowledgeitemId() == null) {
            map.addAttribute("errorMessage", "课程中不存在知识点,courseUnitId=" + courseUnit.getId());
            map.addAttribute("courseUnit", courseUnit);
            return "/teacher/listenCourse/listenError";
        }



        Knowledgeitem item = listenService.getKnowledgeitemById(courseUnit.getKnowledgeitemId());
        //不存在知识点
        if (item == null) {
            map.addAttribute("errorMessage", "知识点不存在,knowledgeId=" + courseUnit.getKnowledgeitemId());
            map.addAttribute("courseUnit", courseUnit);
            return "/teacher/listenCourse/listenError";
        }
        //Course c = interfaceCloudService.getCourse(courseUnit.getCourseId());

        courseUnit.setKnowledgeitem(item);
        item.setResourceList(listenService.getResourceList(item.getId()));
        item.setMp4List(listenService.getMp4List(item.getId()));
        map.addAttribute("courseUnit", courseUnit);
        map.addAttribute("percent", interfaceCloudService.getUserPercent(productId,user.getId(),LIBRARY_ID));
        map.addAttribute("oneCategoryId", oneCategoryId);
        map.addAttribute("subCategoryId", subCategoryId);
        map.addAttribute("threeCategoryId", threeCategoryId);
        map.addAttribute("pageNo", pageNo);
        map.addAttribute("amout",product.getLessonAmount());
        map.addAttribute("teachers", StringUtils.join(interfaceCloudService.getCourseTeacher(courseUnit.getCourseId()),","));
        map.addAttribute("threeName", frontCategoryService.getFrontCategoryNameById(threeCategoryId));
        return new Mp4Player(courseUnit, map).getUrl();
    }


    /**
     * 添加学习的记录
     *
     * @param productId
     * @param courseUnitId
     * @param user
     */
    private CourseUnit setRecord(Integer productId, Integer courseUnitId, UserEntity user) {
        CourseUnit courseUnit;
        if (courseUnitId == 0) {//进入听课页面
            //查询以往学习记录
            courseUnitId = listenService.getUserLastListenUnitId(productId, user.getId(),LIBRARY_ID);
        }
        courseUnit = listenService.getCourseUnitById(courseUnitId);
        if (courseUnit == null) {
            courseUnit = initCourseUnit(productId, courseUnitId);
        }
        if (courseUnit != null) {
            interfaceCloudService.insertRecord(productId, courseUnit.getCourseId(), user.getId(), courseUnit.getId(),LIBRARY_ID);
        }
        return courseUnit;
    }

    /**
     * 初始化课程
     *
     * @param productId
     * @return
     */
    private CourseUnit initCourseUnit(Integer productId, Integer courseUnitId) {
        CourseUnit courseUnit = null;
        if (courseUnitId != null && courseUnitId != 0) {
            courseUnit = listenService.getCourseUnitById(courseUnitId);
        } else {
            //查询产品下课程的根目录
            List<CourseUnit> cuList = listenService.getUnitRootNodes(productId);
            if (cuList != null && cuList.size() > 0) {
                //递归查询第一个子节点
                courseUnit = listenService.getUnitFristChildNode(cuList.get(0).getId());
            }
        }
        return courseUnit;
    }

    /**
     * 网络爬虫的特征
     *
     * @return
     */
    private String[] getSpiders() {
        String[] spiders = new String[4];
        //百度爬虫  Baiduspider+(+http://www.baidu.com/search/spider.htm)
        spiders[0] = "spider";
        //雅虎爬虫，分别是雅虎中国和美国总部的爬虫
        //Mozilla/5.0 (compatible; Yahoo! Slurp China; http://misc.yahoo.com.cn/help.html)
        //Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)
        spiders[1] = "yahoo.com";
        //新浪爱问爬虫
        //iaskspider/2.0(+http://iask.com/help/help_index.html)
        //Mozilla/5.0 (compatible; iaskspider/1.0; MSIE 6.0)
        //搜狗爬虫
        //Sogou web spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07″)
        //Sogou Push Spider/3.0(+http://www.sogou.com/docs/help/webmasters.htm#07″)
        //【早期用法：“sogou spider”】
        //
        //Google爬虫
        //Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)
        spiders[2] = "bot";

        //Google AdSense广告内容匹配爬虫
        //Mediapartners-Google/2.1
        //
        //网易爬虫
        //Mozilla/5.0 (compatible; YodaoBot/1.0; http://www.yodao.com/help/webmaster/spider/;)
        //【早期采用“ OutfoxBot/0.5 (for internet experiments; http://”; outfoxbot@gmail.com)”】
        //
        //Alexa排名爬虫
        //ia_archiver
        //
        //MSN爬虫
        //msnbot/1.0 (+http://search.msn.com/msnbot.htm)
        //特点未知
        //msnbot-media/1.0 (+http://search.msn.com/msnbot.htm)
        //
        //据称为北大天网的搜索引擎爬虫程序
        //P.Arthur 1.1
        spiders[3] = "p.arthur 1.1";

        //看来是Qihoo的
        //Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; QihooBot 1.0)
        //
        //Gigabot搜索引擎爬虫
        //Gigabot/2.0 (http://www.gigablast.com/spider.html)
        return spiders;
    }

    private boolean isExistProductId(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }
}
