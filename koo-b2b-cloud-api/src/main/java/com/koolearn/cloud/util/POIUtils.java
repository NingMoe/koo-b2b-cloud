package com.koolearn.cloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
/**
 * 
 * @author gehaisong
 *
 */
public class POIUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * * @Description: TODO()
	 * 
	 * @param wb
	 * @return
	 * @return CellStyle
	 * @author: 葛海松
	 * @time: 2015年1月22日 上午10:14:42
	 * @throws
	 */
	private static CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

	/**
	 * * @Description: TODO(创建样式)
	 * 
	 * @param wb
	 * @return
	 * @return Map<String,CellStyle>
	 * @author: 葛海松
	 * @time: 2015年1月22日 上午10:15:13
	 * @throws
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

		Font titleFont = wb.createFont();
		//设置表头字体大小
		titleFont.setFontHeightInPoints((short) 48);
		//设置表头字体颜色
		titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(titleFont);
		styles.put("title", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	/**
	 * * @Description: TODO(导出excel--生成一个sheet)
	 * 
	 * @param sheetTitle
	 *            工作表 表名
	 * @param headers
	 *            表格字段
	 * @param fields
	 *            对象属性
	 * @param list
	 *            对象数据集合
	 * @param name
	 *            文件名称
	 * @param path
	 *            判断path==null 网络下载， path!=null 本地保存路径
	 * @param response
	 *            当path==null时response不能为空
	 * @return void
	 * @author: 葛海松
	 * @time: 2015年1月22日 上午10:15:37
	 * @throws
	 */
	public static void exportExcel(
			String sheetTitle, String[] headers,
			String[] fields, List<?> list, 
			String name, String path,
			HttpServletResponse response) {
		// 创建工作簿
		Workbook wb = new HSSFWorkbook();
        createExcelSheet(  wb,sheetTitle, headers, fields,list);
        exportFile(  wb,  name,   path,  response);

	}
    /**
     * * @Description: TODO(导出excel--生成多个sheet)

     * @param headers
     *            表格字段
     * @param fields
     *            对象属性
     * @param sheetMap 对应多个sheet表的数据
     *            对象数据集合
     * @param name
     *            文件名称
     * @param path
     *            判断path==null 网络下载， path!=null 本地保存路径
     * @param response
     *            当path==null时response不能为空
     * @return void
     * @author: 葛海松
     * @time: 2015年1月22日 上午10:15:37
     * @throws
     */
    public static void exportExcel( String[] headers,
            String[] fields, Map<String,List<?>> sheetMap,
            String name, String path,
            HttpServletResponse response) {
        // 创建工作簿
        Workbook wb = new HSSFWorkbook();
        Iterator it=sheetMap.keySet().iterator();
        while (it.hasNext()){
            String keySheetTitle= (String) it.next();
            List<?> list=sheetMap.get(keySheetTitle);
            createExcelSheet(  wb,keySheetTitle, headers, fields,list);
        }
        exportFile(  wb,  name,   path,  response);

    }
    public static void exportFile(Workbook wb,String name, String path,HttpServletResponse response){
        // 写入
        if (path != null) {
            if (wb instanceof XSSFWorkbook)
                path += "x";
            try {
                FileOutputStream out = new FileOutputStream(path + File.separator + name + ".xlsx");
                wb.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ServletOutputStream sos = null;
            response.setContentType("text/html;charset=UTF-8");
            response.setContentType("application/vnd.ms-excel");// 改成输出excel文件
            try {
                sos = response.getOutputStream();
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename="+ new String(name.getBytes("utf-8"),
                                "ISO8859-1"));
                wb.write(sos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (sos != null) {
                    try {
                        sos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static void createExcelSheet( Workbook wb, String sheetTitle, String[] headers, String[] fields, List<?> list) {
        // 样式
        Map<String, CellStyle> styles = createStyles(wb);
        // 创建一个工作表,并设置工作表名称
        Sheet sheet = wb.createSheet(sheetTitle);
        // sheet.autoSizeColumn(1, true);//自适应大小
        sheet.setDefaultColumnWidth(19);
        // sheet.setColumnWidth(i, 256 * 15); //针对第个单元格 设置宽度
        // 关闭非数据区域的网格线
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        // HSSF
        sheet.setAutobreaks(true);
        printSetup.setFitHeight((short) 1);
        printSetup.setFitWidth((short) 1);

        //设置表头标题
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(80);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(sheetTitle);
        titleCell.setCellStyle(styles.get("title"));
        // sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$E$1"));
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0,
                headers.length - 1);
        sheet.addMergedRegion(cellRangeAddress);

        // 表头
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(styles.get("header"));
        }

        // 数据
        Row row;
        Cell cell;
        Object obj;
        Object value;
        Field field;
        int rownum = 2;
        int size = list.size();
        for (int i = 0; i < size; i++, rownum++) {
            // 创建一行（指定行号）
            row = sheet.createRow(rownum);
            obj = list.get(i);
            if (obj == null)
                continue;
            if (fields != null) {
                //
                for (int j = 0; j < fields.length; j++) {
                    try {
                        // 获取实体对象指定属性（反射）
                        field = obj.getClass().getDeclaredField(fields[j]);
                        // 类中的成员变量为private,故必须进行此操作
                        field.setAccessible(true);
                        // 获取当前对象中当前Field的value
                        value = field.get(obj);
                        // 创建单元格
                        cell = row.createCell(j);
                        if (value instanceof Date) {
                            cell.setCellValue(sdf.format((Date) value));
                        } else {
                            cell.setCellValue(String.valueOf(value));
                        }
                        cell.setCellStyle(styles.get("cell_normal"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
	/***************** 1. excel end *************************/
	/***************** 2. word begin *************************/
	/**
	 * * @Description: TODO(word导出试卷数据)
	 * 
	 * @param request
	 * @param response
	 * @return void
	 * @author: 葛海松
	 * @time: 2015年4月23日 下午2:51:08
	 * @throws
	 */
	@SuppressWarnings("unused")
	public static void exportWord(Map<String, Object> dataMap, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.setContentType("octets/stream");
			String wordName = dataMap.get("wordName") + ".docx";
			wordName = new String(wordName.getBytes("GB2312"), "iso8859-1");
			response.addHeader("Content-Disposition", "attachment;filename="+ wordName);
			OutputStream out = response.getOutputStream();
			XWPFDocument doc = new XWPFDocument();
			if (true) {
				// 创建带水印模版(WebRoot下)
				String path = request
						.getRealPath("/template/paperTemplate.docx");
				doc=new XWPFDocument(new FileInputStream(new File(path)));
			}
			decodeDataMap(doc, dataMap);
			dataMap = null;
			doc.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void decodeDataMap(XWPFDocument doc, Map<String, Object> dataMap)
			throws Exception {

		try {
			// 试卷名称:
			addKV(doc, "试卷名称", "高二语文期末考试（一）");
			// 试卷编码:
			addKV(doc, "试卷编码", "GKC1120");
			// 试卷总分:
			addKV(doc, "试卷总分", "150分");
			addKV(doc, "");
			addKV(doc,(String)dataMap.get("content"));
			//插入表格
			addTable(doc);
			addKV(doc, "题干",(String)dataMap.get("content"));
			// 数据解析
			decodeQuestion(doc, dataMap);
		} catch (Exception e) {
			throw new Exception("导出试卷，数据解析出现异常", e);
		}
	}

	private static void decodeQuestion(XWPFDocument doc, Map<String, Object> dataMap) {

	}

	/**
	 * * @Description: TODO(向doc文本写入（key：value数据）	) 
	   *  @param doc
	   *  @param key
	   *  @param value    
	   * @return void    
	   * @author: 葛海松
	   * @time:    2015年4月23日 下午4:54:00 
	   *       XWPFDocument代表一个docx文档，其可以用来读docx文档，也可以用来写docx文档.包含对象如下：
	   *         XWPFParagraph：代表一个段落。
                 XWPFRun：代表具有相同属性的一段文本。
                 XWPFTable：代表一个表格。
                 XWPFTableRow：表格的一行。
                 XWPFTableCell：表格对应的一个单元格。
	 */
	private static void addKV(XWPFDocument doc, String key, String value) {
		//代表一个段落
		XWPFParagraph p3 = doc.createParagraph();
		p3.setWordWrap(true);
		// XWPFRun 代表具有相同属性的一段文本 ,有不同样式的文本 需创建多个
		XWPFRun r4 = p3.createRun();
		r4.setTextPosition(5);
		String temp = "【" + key + "】" + (value == null ? "" : value);
		r4.setText(temp);
	}

	/** 向doc文本写入（key：value数据） */
	private static void addKV(XWPFDocument doc, String value) {
		XWPFParagraph p3 = doc.createParagraph();
		p3.setWordWrap(true);
		XWPFRun r4 = p3.createRun();
		r4.setTextPosition(5);
		r4.setText(value == null ? "" : value);
	}

	/** 向doc文本写入（key：value数据） */
	private static void addBoldKV(XWPFDocument doc, String value) {
		XWPFParagraph p3 = doc.createParagraph();
		p3.setWordWrap(true);
		XWPFRun r4 = p3.createRun();
		r4.setTextPosition(5);
		r4.setBold(true);
		r4.setText(value == null ? "" : value);
	}
	
	/**插入表格*/
	private static void addTable(XWPFDocument doc) {
		XWPFTable table=doc.createTable(2, 4);
		  table.addNewCol(); //给表格增加一列 
	      table.createRow(); //给表格新增一行 
		XWPFTableRow row=table.getRow(1);
		XWPFTableCell cell=row.getCell(1);
		XWPFParagraph p= doc.createParagraph();
       // XWPFRun 代表具有相同属性的一段文本
		XWPFRun r4 = p.createRun();
		r4.setTextPosition(5);
		r4.setText("表格段落文本1");
		cell.setParagraph(p);
		cell.setText("表格文本1");
		
		 List<XWPFTableRow> rows = table.getRows();
	      //表格属性
	      CTTblPr tablePr = table.getCTTbl().addNewTblPr();
	      //表格宽度
	      CTTblWidth width = tablePr.addNewTblW();
	      width.setW(BigInteger.valueOf(8000));
	}
	 /**
	  * * @Description: TODO(
                                                      通过XWPFDocument对内容进行访问。对于XWPF文档而言，用这种方式进行读操作更佳) 
	    *  @throws Exception    
	    * @return void    
	    * @author: 葛海松
	    * @time:    2015年4月23日 下午5:18:11 
	    * @throws
	  */
	   public void testReadByDoc() throws Exception {
	      InputStream is = new FileInputStream("D:\\table.docx");
	      XWPFDocument doc = new XWPFDocument(is);
	      List<XWPFParagraph> paras = doc.getParagraphs();
	      for (XWPFParagraph para : paras) {
	         //当前段落的属性
//	       CTPPr pr = para.getCTP().getPPr();
	         System.out.println(para.getText());
	      }
	      //获取文档中所有的表格
	      List<XWPFTable> tables = doc.getTables();
	      List<XWPFTableRow> rows;
	      List<XWPFTableCell> cells;
	      for (XWPFTable table : tables) {
	         //表格属性
//	       CTTblPr pr = table.getCTTbl().getTblPr();
	         //获取表格对应的行
	         rows = table.getRows();
	         for (XWPFTableRow row : rows) {
	            //获取行对应的单元格
	            cells = row.getTableCells();
	            for (XWPFTableCell cell : cells) {
	                System.out.println(cell.getText());;
	            }
	         }
	      }
	      this.close(is);
	   }
	  
	   
	   /**
	    * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中
	    * ${reportDate}
	    * @throws Exception
	    */
	   public void testTemplateWrite() throws Exception {
	      Map<String, Object> params = new HashMap<String, Object>();
	      params.put("reportDate", "2014-02-28");
	      params.put("appleAmt", "100.00");
	      params.put("bananaAmt", "200.00");
	      params.put("totalAmt", "300.00");
	      String filePath = "D:\\word\\template.docx";
	      InputStream is = new FileInputStream(filePath);
	      XWPFDocument doc = new XWPFDocument(is);
	      //替换段落里面的变量
	      this.replaceInPara(doc, params);
	      //替换表格里面的变量
	      this.replaceInTable(doc, params);
	      OutputStream os = new FileOutputStream("D:\\word\\write.docx");
	      doc.write(os);
	      this.close(os);
	      this.close(is);
	   }
	  
	   /**
	    * 替换段落里面的变量
	    * @param doc 要替换的文档
	    * @param params 参数
	    */
	   private void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
	      Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
	      XWPFParagraph para;
	      while (iterator.hasNext()) {
	         para = iterator.next();
	         this.replaceInPara(para, params);
	      }
	   }
	  
	   /**
	    * 替换段落里面的变量
	    * @param para 要替换的段落
	    * @param params 参数
	    */
	   private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
	      List<XWPFRun> runs;
	      Matcher matcher;
	      if (this.matcher(para.getParagraphText()).find()) {
	         runs = para.getRuns();
	         for (int i=0; i<runs.size(); i++) {
	            XWPFRun run = runs.get(i);
	            String runText = run.toString();
	            matcher = this.matcher(runText);
	            if (matcher.find()) {
	                while ((matcher = this.matcher(runText)).find()) {
	                   runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
	                }
	                //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
	                //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
	                para.removeRun(i);
	                para.insertNewRun(i).setText(runText);
	            }
	         }
	      }
	   }
	  
	   /**
	    * 替换表格里面的变量
	    * @param doc 要替换的文档
	    * @param params 参数
	    */
	   private void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
	      Iterator<XWPFTable> iterator = doc.getTablesIterator();
	      XWPFTable table;
	      List<XWPFTableRow> rows;
	      List<XWPFTableCell> cells;
	      List<XWPFParagraph> paras;
	      while (iterator.hasNext()) {
	         table = iterator.next();
	         rows = table.getRows();
	         for (XWPFTableRow row : rows) {
	            cells = row.getTableCells();
	            for (XWPFTableCell cell : cells) {
	                paras = cell.getParagraphs();
	                for (XWPFParagraph para : paras) {
	                   this.replaceInPara(para, params);
	                }
	            }
	         }
	      }
	   }
	  
	   /**
	    * 正则匹配字符串
	    * @param str
	    * @return
	    */
	   private Matcher matcher(String str) {
	      Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
	      Matcher matcher = pattern.matcher(str);
	      return matcher;
	   }
	  
	   /**
	    * 关闭输入流
	    * @param is
	    */
	   private void close(InputStream is) {
	      if (is != null) {
	         try {
	            is.close();
	         } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	   }
	  
	   /**
	    * 关闭输出流
	    * @param os
	    */
	   private void close(OutputStream os) {
	      if (os != null) {
	         try {
	            os.close();
	         } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	   }
	/***************** word end *************************/
}