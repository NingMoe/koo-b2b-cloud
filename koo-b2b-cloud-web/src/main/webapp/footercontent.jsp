<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<fe:html title="" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/i-footer-partners/page.css"
         defaultHead="<html>">

    <body>
    <!-- 公共的头部 B-->
    <div class="p-header">
        <div class="i-box">
            <a class="p-lg fl" href="javascript:;">
                <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
            </a>
            <b class="p-b fl">中小学教育云平台</b>
            <div class="p-personl fr">
                <span id="userName" title=""> </span>

            </div>

        </div>
    </div>
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
    <div class="i-fbanner">  </div>
    <div class="i-fcont">
        <div class="i-main fc i-fcont-conts">
            <div class="if-side fl">
                <dl class="if-side-nav" id="jp-nav">
                    <dt  <%if("lxwm".equals(request.getParameter("tag"))){%>class="hid"<%}%>   >联系我们${tag}</dt>
                    <dd <%if("lxwm".equals(request.getParameter("tag"))){%>class="cur"<%}%>  >
                        <h6>联系我们</h6>
                        <p>Contact Us</p>
                    </dd>
                    <dt   <%if("bqsm".equals(request.getParameter("tag"))){%>class="hid"<%}%>  >版权声明</dt>
                    <dd  <%if("bqsm".equals(request.getParameter("tag"))){%>class="cur"<%}%>   >
                        <h6>版权声明</h6>
                        <p>Copyright Notice</p>
                    </dd>
                    <dt  <%if("hzhb".equals(request.getParameter("tag"))){%>class="hid"<%}%> >合作伙伴</dt>
                    <dd  <%if("hzhb".equals(request.getParameter("tag"))){%>class="cur"<%}%>  >
                        <h6>合作伙伴</h6>
                        <p>Partner</p>
                    </dd>
                    <dt  <%if("sybz".equals(request.getParameter("tag"))){%>class="hid"<%}%> >使用帮助</dt>
                    <dd  <%if("sybz".equals(request.getParameter("tag"))){%>class="cur"<%}%>  >
                        <h6>使用帮助</h6>
                        <p>Use help</p>
                    </dd>
                    <dt class="<%if("gywm".equals(request.getParameter("tag"))){%>hid<%}%>">关于我们</dt>
                    <dd  class="<%if("gywm".equals(request.getParameter("tag"))){%>cur<%}%>">
                        <h6>关于我们</h6>
                        <p>About us</p>
                    </dd>
                </dl>

            </div>
            <div class="if-cont fl" id="jp-cont">
                <div class="if-w i-f-contactus cur">
                    <h2>客户服务电话</h2>
                    <div class="p-conta">
                        <p>
                            用户服务，产品咨询，购买，技术支持 <br>
                            服务时间：周一至周五9:30-12:30 &nbsp;&nbsp;&nbsp;&nbsp; 13:30-18:30<br>
                        </p>
                        <p>
                            公司名称：北京新东方迅程网络股份科技有限公司<br>
                            地址：北京市海淀区海淀东三街2号新东方南楼18层<br>
                            邮政编码：100080<br>
                            电话：010-82183727<br>
                            E-mail：k12yun@koolearn-inc.com<br>
                        </p>
                        <p class="p-img-map">
                            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/if-map.jpg">
                        </p>
                    </div>
                </div>
                <div class="if-w i-f-copytice">
                    <p>
                        1、“新东方在线”上的所有产品及其附属内容，包括但不限于产品课件、声音、图象、标识、标志等均为“新东方在线”或北京新东方迅程网络科技有限公司所有，其版权受法律保护，任何未经允许的复制、下载、传播、链接等均为违法行为，均应承担相应的法律责任。
                    </p>
                    <p>
                        2、“新东方在线”上的其它内容，包括文章、资料、资讯等， 本网注明“稿件来源：新东方”的，其版权均 为“新东方在线”或北京新东方迅程网络科技有限公司所有，任何公司、媒体、网站或个人未经授权不得转载、链接、转贴或以其他方式使用。已经得到 “新东方在线”许可 的媒体、网站，在使用时必须注明“稿件来源：新东方”，违者本网站将依法追究责任。
                    </p>
                    <p>
                        3、“新东方在线” 未注明“稿件来源：新东方”的 文章、资料、资讯等 均为转载稿，本网站转载出于传递更多信息之目的，并不意味着赞同其观点或证实其内容的真实性。如其他媒体、网站或个人从本网站下载使用，必须保留本网站注明的“稿件来源”，并自负版权等法律责任。如擅自篡改为 " 稿件来源：新东方 " ，本网站将依法追究其法律责任。
                    </p>
                    <p>
                        4、“新东方在线”中“新东方论坛”中的用户应严格遵守中华人民共和国著作权法和其它相关法律，任何转载或转贴都应注明真实作者和真实出处。“新东方在线”有权在本网站范围内引用、发布、转载用户在“新东方论坛”发布的内容。“新东方在线”对于用户发布的内容所引发的版权、署名权的异议、纠纷不承担任何责任。传统媒体转载须事先与原作者和“新东方在线”联系。任何用户的发言均属个人行为，与本网站立场无关。
                    </p>
                    <p>5、对不遵守本声明或其他违法、恶意使用本网内容者，本网保留追究其法律责任的权利。</p>
                </div>
                <div class="if-w i-f-partner">
                    <dl>
                        <dt>推广支持：</dt>
                        <dd>
                            在对外推广产品的同时，我们为各代理商提供市场推广支持：包含产品宣传片，产品使用讲解视频，及产品平面设计宣传资料,同时还将在全国各地不定期举办大型名师讲座活动。
                        </dd>
                        <dt>技术支持：</dt>
                        <dd>
                            专业的技术开发人员，网站后期的维护人员，为您的运营，排除了一切后顾之忧！
                        </dd>
                        <dt>服务支持：</dt>
                        <dd>
                            为每个地区配置了专门的渠道经理，给每位代理商提供周到细致的服务和全方位的深入培训，包括：课程培训、销售培训、售后服务培训等。并且在新东方在线网站首页设有“免费客服咨询”，提供各地购课咨询、网络学习疑问解答、常见问题查询功能。
                        </dd>
                        <dt>盈利培训：</dt>
                        <dd>
                            新东方在线将在现有赢利模式基础上，及时汇总各地代理商从实践中总结出来的成功经验，并以电话或邮递的形式分享给不同区域代理商，促进大家的共同提高。
                        </dd>
                        <dd class="p-partner-map">
                            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/p-partner-map.jpg">
                        </dd>
                        <dd>
                            <p>
                                联系人：申老师<br>
                                电话：010-82183728<br>
                                E-mail：k12yun@ koolearn-inc.com<br>
                                工作时间：周一至周五 9:30 - 12:30  13:30-18:30<br>
                                邮件回复时间： 24小时之内(工作日)，48小时之内（节假日）<br>
                            </p>
                        </dd>
                    </dl>
                </div>
                <div class="if-w i-f-usehelp">
                    <dl id="jp-usehelp">
                        <dt>一、老师如何加入班级？</dt>
                        <dd class="p-one">
                            <h6>1、通过查找班级的方式加入。</h6>
                            <p>登陆后，在首页点击“添加班级”的图标，进入添加班级的界面。</p>
                            <p>
                                <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/if-help-img1.png">
                            </p>
                            <p>
                                选择所担任的学科及所在的学段，通过班级入学年份进行筛选查询，在列表中查到自己所在班级的名称，勾选后点击“确定”加入。
                            </p>
                            <p>
                                <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/if-help-img2.png">
                            </p>
                            <h6>2、通过创建的方式加入：</h6>
                            <p>
                                列表中找不到所在班级的，可以创建新班级后加入。点击创建班级的按钮，选择班级的“入学年份”，填写班级名称后点击“保存”完成创建班级。如果班级只是针对本学科的走班制、选课的班级，需要勾选下面的学科班选项，并选择班级的学科。
                            </p>
                            <p>
                                <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/if-help-img3.png">
                            </p>
                            <p>
                                <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/if-help-img4.png">
                            </p>
                        </dd>
                        <dt>二、“学科班”和“行政班”有什么区别？</dt>
                        <dd>
                            <p>
                                行政班即为普通的班级，所有学生在固定的班级上所有的科目。学科班即为走班制的班级，该班级的学生只在这个班级学习唯一的一个科目，其他科目和其他不同的同学在不同的班级学习。班级的学生一般是通过选课的方式或走班制的形式组成的一个组织。
                            </p>
                        </dd>
                        <dt>三、课堂与作业有什么区别？</dt>
                        <dd>
                            <p>
                                课堂一般是由资源、作业组成，对资源、作业的数量没有限制，老师可以通过云平台在线的资源、作业或者自己本地上传的资源组成课堂内容。课堂的作业，可以针对已经分组的学生选择不同的分组、班级布置不同的作业。作业每次只能布置一份作业，不能在一份作业任务中选择多份作业进行布置，作业与课堂一样可以选择这份作业针对的班级或分组。
                            </p>
                        </dd>
                        <dt>四、课堂、作业的开放时间、截止时间是什么意思？</dt>
                        <dd>
                            <p>
                                开放时间指的是学生可以看到课堂、作业任务并进行预习、在线完成作业的时间，截止时间为作业截止提交的时间以及计算课堂预习的时间，截止时间到后学生不能在提交作业，浏览的课堂不再计入预习的统计中。
                            </p>
                        </dd>
                        <dt>五、什么是复制课堂、作业？</dt>
                        <dd>
                            <p>
                                复制课堂、作业后，原课堂、作业不受影响，原课堂的内容会拷贝一份，可在此基础上进行修改和编辑后以一份新的课堂、作业进行发布，方便使用之前组织好的课堂内容、减少重复性工作。历史课堂、作业可以通过搜索的方式进行查找。
                            </p>
                        </dd>
                        <dt>六、什么是撤回课堂、作业？怎么才可以撤回课堂、作业？</dt>
                        <dd>
                            <p>
                                课堂、作业截止前，都可以撤回，撤回后，学生端将看不到该课堂、作业，已经提交的作业数据也不会进行保存。
                            </p>
                        </dd>
                        <dt>七、已经发布的课堂、作业怎么才能删除？</dt>
                        <dd>
                            <p>
                                已经发布的课堂、作业，必须撤回后才能删除。
                            </p>
                        </dd>
                        <dt>八、为什么发布课堂、作业时，选择我的试卷时没有可选试卷？</dt>
                        <dd>
                            <p>
                                课堂、作业的试卷，只能从我的试卷列表中选择，在布置课堂、作业前，需要先在“题库”里面先组卷或者从“试卷组题”中，将选中的试卷加入到我的试卷后才能看到。
                            </p>
                        </dd>
                        <dt>九、什么是校本试卷库、资源库？</dt>
                        <dd>
                            <p>
                                校本试卷库、资源库，指的是本学校相关学科老师上传、组卷的资源。其中资源库包括本校老师备课、上传资源时选择公开到本校范围的资源。
                            </p>
                        </dd>
                        <dt>十、怎么让学生加入班级？</dt>
                        <dd>
                            <p>
                                老师可以通过批量创建的方式，让学生加入到本班级中，如果其他老师已经为学生创建账号或者学生已经有账号的，可以将所在班级的班级码告知学生，让学生登陆后通过加入班级的方式输入班级码后加入。
                            </p>
                        </dd>
                        <dt>十一、备课上传的资源能重复使用吗？</dt>
                        <dd>
                            <p>
                                备课上传的资源，默认都存在“我的资源库”，下次备课时，直接在我的资源库搜索选择即可，可以重复使用，不需要重复上传。
                            </p>
                        </dd>
                        <dt>十二、怎么对班级的学生进行分组。</dt>
                        <dd>
                            <p>
                                登陆后点击右上角的个人姓名，在下拉菜单中进入“我的班级”，选择要进行分组的班级，点击“小组名单”进入分组管理列表，进行分组管理。同一个学生不能在不同的小组。老师担任多个学科的，每个学科都需要进行分组设置。
                            </p>
                        </dd>
                        <dt>十三、我在学校担任多个学科怎么办？</dt>
                        <dd>
                            <p>
                                登陆后点击右上角的个人姓名，点击姓名进入“个人资料”，点击 “修改”在担任学科后点击“添加”进行学科管理，每个老师最多可以选择三个学科，添加学科后，在题库、资源库中可以进行学科切换进行资源的搜索。
                            </p>
                        </dd>
                        <dt>十四、老师怎么进行在线作业批阅？</dt>
                        <dd>
                            <p>
                                已经布置的作业、课堂，老师点击课堂、作业的标题进入作业列表，点击学生的姓名进入该学生提交的作业界面进行批阅，没有提交作业的学生不能进行批阅。客观题，系统会自动批阅，但需要老师点击提交批阅后，学生才能看到批阅结果。主观题，老师需要进行手动批阅选择“正确”“错误”或“部分正确”，并输入相应的分值，提交批阅后学生可以看到批阅结果。
                            </p>
                        </dd>
                        <dt>十五、什么是设置毕业？</dt>
                        <dd>
                            <p>
                                设置毕业后，改班级将不在首页的列表中显示，如果要恢复该班级在首页的显示，可以在班级管理中将班级恢复正常。
                            </p>
                        </dd>
                        <dt>十六、我是学生，该如何加入班级？</dt>
                        <dd>
                            <p>
                                登陆系统后，点击班级进入班级列表，点击加入班级按钮，输入老师给的班级码即可加入班级。没有账号的，可以让老师在班级里进行创建，创建后自动加入创建时选择的班级，不需要再次加入。
                            </p>
                        </dd>
                        <dt>十七、我是学生，为什么我看不到老师布置的课堂、作业？</dt>
                        <dd>
                            <p>
                                老师布置课堂、作业后，需要等到开放时间后，才能看到课堂和作业。
                            </p>
                        </dd>
                        <dt>十八、我是学生，老师创建的账号太复杂，记不住怎么办？</dt>
                        <dd>
                            <p>
                                用老师创建的账号登陆系统后，点击右上角姓名选择“个人资料”后，点击 “修改”，填写自己的邮箱、手机，下次可以用手机或邮箱进行登陆。
                            </p>
                        </dd>
                    </dl>
                </div>
                <div class="if-w i-f-partner">
                    <dl>
                        <dt>产品背景：</dt>
                        <dd>
                            新东方中小学教育云平台是以云系统为技术架构，web服务、优化数据结构和大数据分析为软件工具，优质教学资源和教学工具为载体的面向教育教学环节的教育服务体系。定位于现有传统教学的信息化辅助和补充，也是教育均衡化和自主性学习的优化解决方案。
                        </dd>
                        <dt>产品描述：</dt>
                        <dd>
                            中小学教育云平台包含中小学相关丰富的题库，资源库，同时增加了备课、作业、在线作业批阅、错题本、题目组题、试卷组题、校本个人资源库管理、班级管理、班级层分组教学及作业数据分析等功能，以适应当前K12公立学校翻转课堂、分层分组教学的需求。
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
    <!--#include virtual="/project/b-ms-cloud/1.x/include/_footer.html"-->

    </body>
<jsp:include page="footer.jsp"></jsp:include>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/i-footer-partners/page': 'project/b-ms-cloud/1.x/js/i-footer-partners/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/i-footer-partners/page',function(init){
            init();
        });
    </script>
</fe:html>