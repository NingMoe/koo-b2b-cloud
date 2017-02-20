package com.koolearn.cloud.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.koolearn.cloud.util.FileUploadUtils;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.cloud.util.IndexNameUtils;
import com.koolearn.framework.common.utils.PropertiesConfigUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * itextpdf工具类
 * @author jishengbao
 *  Chunk : 块，PDF文档中描述的最小原子元素
    Phrase : 短语，Chunk的集合
    Paragraph : 段落，一个有序的Phrase集合
 *
 */
public class ItextpdfUtils {
    private static final Logger log=Logger.getLogger(ItextpdfUtils.class);
    public static final int contentType_default_200=200;
    public static final int contentType_sjm=0;//试卷
    public static final int contentType_tmlx=1;
    public static final int contentType_tmlx_80=80;
    public static final int contentType_tg=2;//tigan提干
    public static final int contentType_tg_100=100;
    public static final int contentType_txx=3;//选项
    public static final int contentType_txx_110=110;
    public static final int contentType_no=4;//T恤
    public static final  List<Font> fonts=new ArrayList<Font>();
    static {
        initFont();
    }
    /**
     *1  获得一个pdfDocument
     * @param fileName
     * @param title
     * @return
     */
	public static Document addTitle(String fileName, String title){
       //创建文档
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = null;
			try {
                String watermarktext="kootest.koolearn.com新东方综合考试平台";
                ByteArrayOutputStream ba=new ByteArrayOutputStream();
				BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				Font fontChinese = new Font(bfChinese, 14, Font.NORMAL);
				//把文档写入指定文件
				writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
				writer.setStrictImageSequence(true); 
				document.open();//打开文本
				// 设置PDF标题
				Paragraph titleGraph = new Paragraph();
				titleGraph.setSpacingBefore(8);
				titleGraph.setSpacingAfter(2);
				titleGraph.setAlignment(1);
				titleGraph.add(new Chunk(title ,new Font(bfChinese, 18,Font.BOLD)));
				document.add(titleGraph);//设置文档标题
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return document;
	}

    /**
     * 添加一个 段落Paragraph 到 文档Document
     * @param document
     * @param
     * @param contentType
     * @throws DocumentException
     * @throws IOException
     */
    public static void addParagraphToDocument(Document document, String content,int contentType,int suojinLeft) throws DocumentException, IOException {
        content= StringUtils.isBlank(content)?"":content;
        List<String> imageUrlList=HtmlCssUtil.getImageUrl(content);// 提取图片
        ItextpdfUtils.addImageListToDocument(document,imageUrlList);
        content= HtmlCssUtil.delHTMLTag(content);//删除html标签
        //添加 一个段落（换行）
        Paragraph paragraph=new Paragraph(content,fonts.get(contentType));
        //设置段落对齐方式
        paragraph.setAlignment(Element.ALIGN_LEFT);
        //设置缩进
        paragraph.setIndentationLeft(suojinLeft);
        document.add(paragraph);
    }
    public static void addParagraphListToDocument(Document document, List<String> paragraphList,int contentType,int suojinLeft) throws DocumentException, IOException{
        if(paragraphList!=null && paragraphList.size()>0){
            for(String content:paragraphList){
                if(StringUtils.isNotBlank(content)){
                   addParagraphToDocument(  document,   content,  contentType,suojinLeft);
                }
            }
        }
    }
    /**
     * 添加一个 文本Chunk  到 段落Paragraph
     * @param paragraph
     * @param content
     * @param contentType
     */
	public static  void addChunkToParagraph( Paragraph paragraph,String content,int contentType){
        content= StringUtils.isBlank(content)?"":content;
        Chunk chunk = new Chunk(content,fonts.get(contentType));
        paragraph.add(chunk);
    }

    /**
     *  设置图片：
     *     1.  右对齐Image.RIGHT   居中Image.MIDDLE   左对齐Image.LEFT
     *     2.  文字绕图形显示Image.TEXTWRAP      Image.UNDERLYING 图形作为文字的背景显示
     * @param document
     * @param imgUrl
     * @throws BadElementException
     * @throws IOException
     */
   public static void addImageToDocument( Document document,String imgUrl) throws DocumentException, IOException {
       Image image = Image.getInstance (imgUrl);
       image.setAlignment(Image.RIGHT|Image.TEXTWRAP);
       // image.scaleAbsolute(200,100);//自定义大小
       image.scalePercent(50);//依照比例缩放
       document.add(image);
   }
    public static void addImageListToDocument( Document document,List<String> imgUrlList) throws DocumentException, IOException {
        if(imgUrlList!=null&&imgUrlList.size()>0){
            for(String imgUrl:imgUrlList){
                addImageToDocument(   document,  imgUrl);
            }
            for(int i=0;i<imgUrlList.size();i++){
                //生成题间距
                ItextPDFUtil.addBlankLine(document);
            }
        }
    }
    /**
     * 初始化字体设置
     */
    public static void initFont() {
        BaseFont bfCN = null;
        try {
            bfCN = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font sjmFont = new Font(bfCN, 14, Font.NORMAL, BaseColor.BLUE);
            Font tmlxFont = new Font(bfCN, 16, Font.NORMAL,BaseColor.BLACK);
            Font tgFont = new Font(bfCN, 12, Font.NORMAL, BaseColor.BLACK);
            Font txxFont = new Font(bfCN, 12, Font.NORMAL, BaseColor.BLACK);
            Font noFont = new Font(bfCN, 14, Font.NORMAL, BaseColor.BLACK);
            fonts.add(sjmFont);
            fonts.add(tmlxFont);
            fonts.add(tgFont);
            fonts.add(txxFont);
            fonts.add(noFont);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出echarts报表图片
     * @return
     *  String[] base64Img = new String[2];
    base64Img[0] = com.getImg2();//接收图片base64图片：var images = myChart.getDataURL('png');//获取base64编码 图片字符串
    base64Img[1] = com.getImg3();
    doc = ItextpdfUtils.addImgsTable(doc, base64Img, null);
     */
	public static Document addImgsTable(Document document, String[] bese64Img, String[] title) {

		BaseFont bfChinese = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		Font fontChinese = new Font(bfChinese, 14, Font.NORMAL);
		
		PdfPTable tableImg = new PdfPTable(2);
		tableImg.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		try {
			tableImg.setTotalWidth(new float[]{400.0f,  400.0f});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		if(bese64Img.length == 1) {
			tableImg = new PdfPTable(1);
			tableImg.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
			try {
				tableImg.setTotalWidth(new float[]{800.0f});
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		
		
		tableImg.setLockedWidth(true);
		// 创建一个表格的表头单元格
		PdfPCell imgCell = new PdfPCell();
		imgCell.setBorderWidth(0);
		imgCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		imgCell.setMinimumHeight(20);

		PdfPCell imgTableHeaderCell = new PdfPCell();
		// 设置表格的表头单元格颜色
		imgTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		imgTableHeaderCell.setBorderWidth(0);

		imgTableHeaderCell.setPhrase(new Paragraph((title!=null && title.length>=1) ? title[0] : "", fontChinese));
		tableImg.addCell(imgTableHeaderCell);

		if(bese64Img.length == 2) {
			imgTableHeaderCell.setPhrase(new Paragraph((title!=null && title.length>=2) ? title[1] : "", fontChinese));
			tableImg.addCell(imgTableHeaderCell);
		}
		
		String imgStr = bese64Img[0];
        Base64 base64 = new Base64();
        String[] arr = imgStr.split("base64,"); 
        
        //Base64解码
    	byte[] bytes;
		try {
			bytes = base64.decode(imgStr.split("base64,")[1].getBytes());
			for (int i = 0; i < bytes.length; i++) {
				if(bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
	        Image jpg = Image.getInstance(bytes); 
	        imgCell.addElement(jpg);
	        if(bese64Img.length == 1) {
					tableImg.setTotalWidth(new float[]{800.0f});
	        }
			tableImg.addCell(imgCell);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		

		if(bese64Img.length >= 2) {
			imgStr = bese64Img[1];
	        arr = imgStr.split("base64,");  
	        //Base64解码
	    	try {
	    		bytes = base64.decode(imgStr.split("base64,")[1].getBytes());
				for (int i = 0; i < bytes.length; i++) {
					if(bytes[i] < 0) {
						bytes[i] += 256;
					}
				}
				Image jpg = Image.getInstance(bytes); 
		        imgCell = new PdfPCell();
				imgCell.setBorder(0);
				imgCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		        imgCell.addElement(jpg);
				tableImg.addCell(imgCell);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(bese64Img.length >= 3) {
			imgStr = bese64Img[2];
	        arr = imgStr.split("base64,");  
	        //Base64解码
	    	try {
	    		bytes = base64.decode(imgStr.split("base64,")[1].getBytes());
				for (int i = 0; i < bytes.length; i++) {
					if(bytes[i] < 0) {
						bytes[i] += 256;
					}
				}
				Image jpg = Image.getInstance(bytes); 
		        imgCell = new PdfPCell();
				imgCell.setBorder(0);
				imgCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		        imgCell.addElement(jpg);
				tableImg.addCell(imgCell);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
    	
		try {
			document.add(tableImg);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public static void addDataTableCom(Document document, String[] titles, String[] keys, List<Map> list) {
		BaseFont bfChinese = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		Font fontChinese = new Font(bfChinese, 14, Font.NORMAL);
		Font fontChinese8 = new Font(bfChinese, 8, Font.NORMAL);
		
		
		PdfPTable table = new PdfPTable(titles.length);
		// 设置表格占PDF文档100%宽度
		table.setWidthPercentage(100);
		// 水平方向表格控件左对齐
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		// 创建一个表格的表头单元格
		PdfPCell pdfTableHeaderCell = new PdfPCell();
		// 设置表格的表头单元格颜色
		pdfTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		/*pdfTableHeaderCell.setPhrase(new Paragraph("省", fontChinese));
		table.addCell(pdfTableHeaderCell);*/
		//添加标题
		for (int i = 0; i < titles.length; i++) {
			pdfTableHeaderCell.setPhrase(new Paragraph(titles[i], fontChinese));
			table.addCell(pdfTableHeaderCell);
		}
		
		
		// 创建一个表格的正文内容单元格
		PdfPCell pdfTableContentCell = new PdfPCell();
		pdfTableContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTableContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		// 表格内容行数的填充
		/*for (int i = 0; i < 20; i++) {
			pdfTableContentCell.setPhrase(new Paragraph(i + "", fontChinese8));
			table.addCell(pdfTableContentCell);
		}*/
		
		if(list!=null && list.size()>0) {
			Map tempMap = null;
			for (int i = 0; i < list.size(); i++) {
				tempMap = list.get(i);
				if(keys!=null && keys.length>0) {
					for (int j = 0; j < keys.length; j++) {
						pdfTableContentCell.setPhrase(new Paragraph(tempMap.get(keys[j]+"") + "", fontChinese8));
						table.addCell(pdfTableContentCell);	
					}
				}
			}
		}
    	
        
        try {
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	
	}
	

	
	public static void addDataTable(Document document, String[] titles, String[] keys, List<Map> list) {
		BaseFont bfChinese = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		Font fontChinese = new Font(bfChinese, 14, Font.NORMAL);
		Font fontChinese8 = new Font(bfChinese, 8, Font.NORMAL);
		
		
		PdfPTable table = new PdfPTable(titles.length);
		// 设置表格占PDF文档100%宽度
		table.setWidthPercentage(100);
		// 水平方向表格控件左对齐
		table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		// 创建一个表格的表头单元格
		PdfPCell pdfTableHeaderCell = new PdfPCell();
		// 设置表格的表头单元格颜色
		pdfTableHeaderCell.setBackgroundColor(new BaseColor(136, 136, 136));
		pdfTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		/*pdfTableHeaderCell.setPhrase(new Paragraph("省", fontChinese));
		table.addCell(pdfTableHeaderCell);*/
		//添加标题
		for (int i = 0; i < titles.length; i++) {
			pdfTableHeaderCell.setPhrase(new Paragraph(titles[i], fontChinese));
			table.addCell(pdfTableHeaderCell);
		}
		
		
		// 创建一个表格的正文内容单元格
		PdfPCell pdfTableContentCell = new PdfPCell();
		pdfTableContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		pdfTableContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		// 表格内容行数的填充
		/*for (int i = 0; i < 20; i++) {
			pdfTableContentCell.setPhrase(new Paragraph(i + "", fontChinese8));
			table.addCell(pdfTableContentCell);
		}*/
		
		if(list!=null && list.size()>0) {
			Map tempMap = null;
			for (int i = 0; i < list.size(); i++) {
				tempMap = list.get(i);
				if(keys!=null && keys.length>0) {
					for (int j = 0; j < keys.length; j++) {
						if(tempMap.get(keys[j]+"")==null) {
							pdfTableContentCell.setPhrase(new Paragraph(((tempMap.get(keys[j]+"")==null  ? "-" : ((tempMap.get(keys[j]+"") + "")))), fontChinese8));
						} else if("null%".equals((tempMap.get(keys[j]+"")))) {
							pdfTableContentCell.setPhrase(new Paragraph((("null%".equals((tempMap.get(keys[j]+""))) ? "0%" : ((tempMap.get(keys[j]+"") + "")))), fontChinese8));
						} else {
							pdfTableContentCell.setPhrase(new Paragraph(tempMap.get(keys[j]+"") + "", fontChinese8));
						}
						table.addCell(pdfTableContentCell);	
					}
				}
			}
		}
    	
		//加入隔行换色事件
        PdfPTableEvent event = new AlternatingBackground();
        table.setTableEvent(event);
        
        try {
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	
		document.close();
	}
 public static String downloadPaperPath(String downloadPaperName){
     String path=FileUploadUtils.getAbsolutePath("/cloud/downloadTemp/"+downloadPaperName+".pdf");
         try {
             File file = new File( path);
             if (!file.getParentFile().exists()) {
                 file.getParentFile().mkdirs();
             }
             if (!file.exists()) {
                file.createNewFile();
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     return path;
 }

    public static void main(String[] args) throws IOException, DocumentException {
//        Document doc = ItextpdfUtils.addTitle("D:\\tol\\data\\student.pdf", "学生名单");
//        String[] base64Img = new String[2];
//        base64Img[0] = "asfasf";//传入base64图片串
//        base64Img[1] = "asfasdf";
//
//
//        //表格字段
//        String[] headers = {"记录ID","学员用户名","学员姓名"};
////		doc = ItextpdfUtils.addImgsTable(doc, base64Img, title);
//
//        String content="　You will have 30 minutes to organize your thoughts and compose a response that represents your point of view on the topic presented. Do not respond to any topic other than the one given; a response to any other topic will receive a score of 0.<br /> You will be required to discuss your perspective on the issue, using examples and reasons drawn from your own experiences and observations.<br /> Use scratch paper to organize your response before you begin writing. Write your response on the pages provided, or type your response using a word processor with the spell-and grammar-check functions turned off.<br /> <strong>Issue Topic</strong><br /> \"Art does not exist unless it is shared; it requires both artist and audience.\" Discuss the extent to which you agree or disagree with the claim made above. Use relevant reasons and examples to support your point of view.";
//
////        BaseFont bf=BaseFont.createFont("/com/koolearn/cloud/util/fonts/SIMYOU.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
////        Font font=new Font(bf, 14.0F, 0);
////        Paragraph paragraph1=new Paragraph(content, font);
////        doc.add(paragraph1);
//        ItextpdfUtils.addDataTable(doc, headers, null, null);
//
//
//        // step 1
//        Document document = new Document();
//        // step 2
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\tol\\data\\fpdf.pdf"));
//        // step 3
//        document.open();
//        // step 4
//        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//                new FileInputStream("D:\\tol\\data\\index.html"));
//        //step 5
//        document.close();
//
//        System.out.println( "PDF Created!" );
    }
}
