package com.koolearn.cloud.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 导出excel文件工具类
 * @author fangjianwei
 *
 */
public class ExcelUtil
{
	/**
	 * 产生excel文件
	 * @param sheetModel ExcelSheetModel对象
	 * @return HSSFWorkbook对象
	 */
	public static HSSFWorkbook genExcelWorkbook( ExcelSheetModel sheetModel ){
		String[] titleArr = sheetModel.getColName();
		List contentList = sheetModel.getContent();
		String title = sheetModel.getTitle();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(title);

		// 插入标题行
		HSSFRow titleRow = sheet.createRow((short) 0);
		for (int i = 0; i < titleArr.length; i++) {
			HSSFCell celltitle = titleRow.createCell(i);
			celltitle.setCellType(HSSFCell.CELL_TYPE_STRING);
			celltitle.setCellValue(titleArr[i]);
		}

		//循环插入数据
		int iRow = 1;
		//System.out.println(contentList.size());
		for (int j = 0; j < contentList.size(); j++) {
			HSSFRow row = sheet.createRow(iRow);
			Map contentMap = (Map)contentList.get(j);
			Iterator ite = contentMap.entrySet().iterator();
			for(int m = 0 ; m <  contentMap.size(); m++){
				HSSFCell celldata = row.createCell(m);
				celldata.setCellType(HSSFCell.CELL_TYPE_STRING);
				Entry entry = (Entry) ite.next();				
				if(entry.getValue() != null && m == 2){
					String desc = (String)entry.getValue();
					//System.out.println("desc=====" + desc);
					int start = desc.lastIndexOf("<![endif]-->");
					
					if(start != -1){
						//System.out.println("substring.start=" + start);
						desc = desc.substring(start+12, desc.length());
					}
					//System.out.println("desc=====" + desc);	
//					desc = desc.replace("<", "&lt;");
//					desc = desc.replace(">", "&gt;");	
//					desc = desc.replace("&", "&amp;");
//					desc = desc.replace("\'", "&apos;");
//					desc = desc.replace("\"", "&quot;");
					celldata.setCellValue(desc);
				} else {
					celldata.setCellValue((String)entry.getValue());
				}
			}
			iRow++;
		}
		
		return wb;
	}
	
	
	/**
	 * 创建HSSFWorkbook对象
	 * @return HSSFWorkbook对象
	 */
	public static HSSFWorkbook createExcelWorkbook()
	{
		HSSFWorkbook wb = new HSSFWorkbook();
		return wb;
	}
	
	
	/**
	 * 在excel表中增加一个sheet
	 * @param wb HSSFWorkbook对象
	 * @param sheetModel ExcelSheetModel对象
	 * @return HSSFWorkbook对象
	 */
	public static HSSFWorkbook appendExcelSheet( HSSFWorkbook wb, ExcelSheetModel sheetModel ){

		String[] titleArr = sheetModel.getColName();
		List contentList = sheetModel.getContent();
		String title = sheetModel.getTitle();

		HSSFSheet sheet = wb.createSheet(title);
		HSSFCellStyle cs = wb.createCellStyle();
		HSSFRow row = null;
		HSSFCell cell = null;
		
		// 插入标题行
		row = sheet.createRow((short) 0);
		for (int i = 0; i < titleArr.length; i++) {
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(titleArr[i]);
		}

		//循环插入数据
		short iRow = 1;
		for (int j = 0; j < contentList.size(); j++) {
			row = sheet.createRow((short) iRow);
			Map contentMap = (Map)contentList.get(j);
			Iterator ite = contentMap.entrySet().iterator();
			for(int m = 0 ; m <  contentMap.size(); m++){
				cell.setCellStyle(cs);
				cell = row.createCell(m);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				Entry entry = (Entry) ite.next();		
				cell.setCellValue((String)entry.getValue());
			}
			iRow++;
		}
		
		return wb;
	}
	
	
	
	/**
	 * 产生excel文件   EAS模版，详情参考文档格式
	 * @param sheetModel ExcelSheetModel对象
	 * @return HSSFWorkbook对象
	 */
	public static HSSFWorkbook genExcelWorkbookEAS( ExcelSheetModel sheetModel ){
		
		String[] versionArr = sheetModel.getVersionName();
		String[] billArr = sheetModel.getBillName();
		String[] titleArrE = sheetModel.getColEName();
		String[] titleArrC = sheetModel.getColName();
		List contentList = sheetModel.getContent();
		String title = sheetModel.getTitle();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(title);
		
		// 插入版本行
		HSSFRow easRow = sheet.createRow((short) 0);
		for (int i = 0; i < versionArr.length; i++) {
			HSSFCell celltitle = easRow.createCell(i);
			celltitle.setCellType(HSSFCell.CELL_TYPE_STRING);
			celltitle.setCellValue(versionArr[i]);
		}
		
		// 插入应收单行
		HSSFRow billRow = sheet.createRow((short) 1);
		for (int i = 0; i < billArr.length; i++) {
			HSSFCell celltitle = billRow.createCell(i);
			celltitle.setCellType(HSSFCell.CELL_TYPE_STRING);
			celltitle.setCellValue(billArr[i]);
		}

		// 插入标题行
		HSSFRow titleRowE = sheet.createRow((short) 2);
		for (int i = 0; i < titleArrE.length; i++) {
			HSSFCell celltitle = titleRowE.createCell(i);
			celltitle.setCellType(HSSFCell.CELL_TYPE_STRING);
			celltitle.setCellValue(titleArrE[i]);
		}
		
		// 插入标题行
		HSSFRow titleRowC = sheet.createRow((short) 3);
		for (int i = 0; i < titleArrC.length; i++) {
			HSSFCell celltitle = titleRowC.createCell(i);
			celltitle.setCellType(HSSFCell.CELL_TYPE_STRING);
			celltitle.setCellValue(titleArrC[i]);
		}

		//循环插入数据
		short iRow = 4;
		for (int j = 0; j < contentList.size(); j++) {
			HSSFRow row = sheet.createRow((short) iRow);
			Map contentMap = (Map)contentList.get(j);
			Iterator ite = contentMap.entrySet().iterator();
			for(int m = 0 ; m <  contentMap.size(); m++){
				HSSFCell celldata = row.createCell(m);
				celldata.setCellType(HSSFCell.CELL_TYPE_STRING);
				Entry entry = (Entry) ite.next();				
				if(entry.getValue() != null && m == 2){
					String desc = (String)entry.getValue();
					//System.out.println("desc=====" + desc);
					int start = desc.lastIndexOf("<![endif]-->");
					
					if(start != -1){
						//System.out.println("substring.start=" + start);
						desc = desc.substring(start+12, desc.length());
					}
					//System.out.println("desc=====" + desc);	
//					desc = desc.replace("<", "&lt;");
//					desc = desc.replace(">", "&gt;");	
//					desc = desc.replace("&", "&amp;");
//					desc = desc.replace("\'", "&apos;");
//					desc = desc.replace("\"", "&quot;");
					celldata.setCellValue(desc);
				} else {
					celldata.setCellValue((String)entry.getValue());
				}
			}
			iRow++;
		}
		
		return wb;
	}
	
    /**
     * 导入Excel
     *
     * @param list 保存返回数据中Map的key值
     * @param in   Excel文件流
     * @return List
     * @throws Exception exception
     */
    public static List<Map> importExcelToData(List<String> list, InputStream in) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(in);
        return importExcelToData(list, fs);
    }
	
	/**
     * 导入Excel
     *
     * @param list     保存返回数据中Map的key值
     * @param FileName Excel路径名称
     * @return List
     * @throws Exception exception
     */
    public static List<Map> importExcelToData(List<String> list, String FileName) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(FileName));
        return importExcelToData(list, fs);
    }

    /**
     * 导入Excel
     *
     * @param list 保存返回数据中Map的key值
     * @param fs   POIFSFileSystem
     * @return List
     * @throws Exception exception
     */
    public static List<Map> importExcelToData(List<String> list, POIFSFileSystem fs) throws Exception {
        HSSFWorkbook wb;
        try {
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            throw new Exception(" 导入数据出错！ ");
        }
        List<Map> result = new ArrayList<Map>();
        HSSFSheet sheet;
        HSSFRow row;
        HSSFCell cell;
        Map<String, Object> t;
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            ArrayList<Map> temp = new ArrayList<Map>(rows);

            row = sheet.getRow(0);
            int cells = (row != null) ? row.getPhysicalNumberOfCells() : 0;
            for (int r = 1; r < rows; r++) {
            	boolean rowValueIsNotBlank = false;
                row = sheet.getRow(r);
                t = new Hashtable<String, Object>();
                String key;
                for (int c = 0; c < cells; c++) {
                	if(row==null) continue;
                    cell = row.getCell(c);
                    key = list.get(c);
                    if (cell == null) continue;
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_FORMULA:
                            t.put(key, cell.getCellFormula());
                            rowValueIsNotBlank = true;
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                t.put(key, (HSSFDateUtil.getJavaDate(cell.getNumericCellValue())));
                            } else {
                                t.put(key, String.valueOf(cell.getNumericCellValue()));
                            }
                            rowValueIsNotBlank = true;
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            t.put(key, cell.getStringCellValue());
                            rowValueIsNotBlank = true;
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            t.put(key, cell.getStringCellValue());
                        	break;
                        default:
                            t.put(key, cell.getStringCellValue());
                        	rowValueIsNotBlank = true;
                            break;
                    }
                }
                if(rowValueIsNotBlank )temp.add(t);
            }
            result.addAll(temp);
        }
        return result;

    }
}
