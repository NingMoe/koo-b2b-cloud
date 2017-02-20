package com.koolearn.cloud.util;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by fn on 2016/11/9.
 */
public  class SchoolUploadFileUtil {
    static Logger log = Logger.getLogger(String.valueOf(new SchoolUploadFileUtil()));
    private static final int BUF_SIZE = 8192;


    public static String uploadClassesFile(MultipartFile file , String uploadPath) {
        String fileName = file.getOriginalFilename();
        String extendName = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        System.out.println( "fileName:" + fileName );
        if( isExcelFile( extendName )&& file.getSize() < 1024*1024*3 ){
            String filePath = uploadPath  ;
            //指定上传路径
            File lastFilePath = new File(filePath ,fileName);
            BufferedInputStream bufferedInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                if(!lastFilePath.exists()){
                    lastFilePath.createNewFile();
                }
                OutputStream outputStream = new FileOutputStream(lastFilePath);
                bufferedInputStream = new BufferedInputStream( file.getInputStream());
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                final byte temp[] = new byte[BUF_SIZE];
                int readBytes = 0;
                while ((readBytes = bufferedInputStream.read(temp)) != -1) {
                    bufferedOutputStream.write(temp, 0, readBytes);
                }
                bufferedOutputStream.flush();
            } catch (FileNotFoundException e) {
                log.error( "批量上传班级异常" + e.getMessage() ,e );
                e.printStackTrace();
            } catch (IOException e) {
                log.error( "批量上传班级异常" + e.getMessage() ,e );
                e.printStackTrace();
            }finally {
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                        if (bufferedInputStream != null) {
                            bufferedInputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return fileName;
    }

    /**
     * 得到Excel表中的值
     *
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    @SuppressWarnings("static-access")
    public static String getValue(Cell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_STRING) {
            // 返回String的值
            return String.valueOf(hssfCell.getStringCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getBooleanCellValue());
        }
    }

    /**
     * 判断是否为excel文件
     * @param extendName
     * @return
     */
    static boolean isExcelFile( String extendName ){
        if( "xlsx".equals( extendName) || "xls".equals( extendName)){
            return true;
        }else{
            return false;
        }
    }
}
