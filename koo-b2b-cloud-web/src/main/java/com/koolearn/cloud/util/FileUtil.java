package com.koolearn.cloud.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.UUID;

/**
 * Created by xiaohao on 2014/8/12.
 * 进行一些文件操作
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 根据文件名取得扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName) {
        if (fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        return null;
    }

    /**
     * 根据扩展名生成一个UUID命名的文件
     *
     * @param fileType
     * @return
     */
    public static String generatorNewFileName(String fileType) {
        StringBuffer fileName = new StringBuffer(UUID.randomUUID().toString());
        fileName.append(".");
        fileName.append(fileType);
        return fileName.toString();
    }

    /**
     * @param filePath
     * @return
     */
    public static String takeOutFileName(String filePath) {
        String fileName = filePath;
        if (null != filePath && !"".equals(filePath)) {
            int port = filePath.lastIndexOf("\\");
            if (port != -1) {
                fileName = filePath.substring(port + 1);
            }
        }
        return fileName;
    }

    /**
     * 根据文件原名 重新 生成一个文件名
     *
     * @param originalFileName
     * @return
     */
    public static String getNewFileName(String originalFileName) {
        String fileType = getFileType(originalFileName);
        String newFileName = generatorNewFileName(fileType);
        return newFileName;
    }

    //获得指定文件的byte数组
    public static byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    //根据byte数组，生成文件
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 解码base64的图片格式 转成byte数组
     *
     * @param content
     * @return
     */
    public static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }


}


