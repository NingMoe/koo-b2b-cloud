package com.koolearn.cloud.util;

import com.koolearn.cloud.composition.CommonHessianServiceImpl;
import com.koolearn.tfs.client.TfsException;
import com.koolearn.tfs.client.Tfstool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.*;

/**
 * Created by haozipu on 2016/8/9.
 */
public class ZipFileUtil {

    private static Log logger = LogFactory.getLog(ZipFileUtil.class);

    public static String filePath = "/compositionImg";

    private ZipFileUtil() {

    }

    public static void main(String[] args) throws Exception {

        File file = null;
        try {
            file = new File("i111bbbb1.zip");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            byte[] fileDataBytes = FileUtil.getBytes("e:\\bbb.zip");
            bos.write(fileDataBytes);
            bos.flush();
            bos.close();
            List<String> filePathList = ZipFileUtil.unzipToTfs("i111bbbb1.zip");
            System.out.println(filePathList.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.deleteOnExit();
            }
        }
    }

    /**
     * 解压缩zip包 保存zip中的文件到tfs
     *
     * @param zipFilePath zip文件路径
     * @throws IOException
     */
    public static List<String> unzipToTfs(String zipFilePath)
            throws IOException {
        InputStream is = null;
        ZipFile zipFile = null;
        List<File> imageFileList =new ArrayList<File>();
        List<String> filePathList = new ArrayList<String>();
        List<String> originFileNameList = new ArrayList<String>();

        Map<String, String> fileNameMap = new HashMap<String, String>();
        try {
            zipFile = new ZipFile(zipFilePath);
            Enumeration entryEnum = zipFile.getEntries();
            if (null != entryEnum) {

                //取得tfs客户端
                Tfstool tfstool = Tfstool.getInstance();

                if (!tfstool.isDirExists(filePath)) {
                    tfstool.createDir(filePath);
                }

                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {
                        continue;
                    }
                    if (zipEntry.getSize() > 0) {

                        //保存到tfs中
                        String fileType = FileUtil.getFileType(zipEntry.getName());

                        String newFileName = FileUtil.generatorNewFileName(fileType);

                        String tfsFilePath = filePath + "/" + newFileName;

                        is = zipFile.getInputStream(zipEntry);

                        File imageFile = new File(CommonHessianServiceImpl.CLOUD_BASE_PATH+newFileName);
                        imageFileList.add(imageFile);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(imageFile));

                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }

                        os.flush();
                        os.close();

                        tfstool.pushFile(CommonHessianServiceImpl.CLOUD_BASE_PATH+newFileName,tfsFilePath);

                        //tfstool.writeFile(tfsFilePath, buffer);

                        fileNameMap.put(zipEntry.getName(), tfsFilePath);
                        originFileNameList.add(zipEntry.getName());
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } catch (TfsException e) {
            e.printStackTrace();
        } finally {
            if (null != zipFile) {
                zipFile.close();
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if(imageFileList!=null){
                for (File file:imageFileList){
                    if(file!=null){
                        file.delete();
                    }
                }
            }
        }

        //处理图片的顺序问题
        Collections.sort(originFileNameList);
        for (String file : originFileNameList) {
            filePathList.add(ConfigUtil.tfsDomain+fileNameMap.get(file));
        }

        return filePathList;
    }

    /**
     * 压缩
     *
     * @param zipFileName  压缩产生的zip包文件名--带路径,如果为null或空则默认按文件名生产压缩文件名
     * @param relativePath 相对路径，默认为空
     * @param directory    文件或目录的绝对路径
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void zip(String zipFileName, String relativePath,
                           String directory) throws IOException {
        String fileName = zipFileName;
        if (fileName == null || fileName.trim().equals("")) {
            File temp = new File(directory);
            if (temp.isDirectory()) {
                fileName = directory + ".zip";
            } else {
                if (directory.indexOf(".") > 0) {
                    fileName = directory.substring(0, directory
                            .lastIndexOf("."))
                            + "zip";
                } else {
                    fileName = directory + ".zip";
                }
            }
        }
        ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(fileName));
        try {
            zip(zos, relativePath, directory);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != zos) {
                zos.close();
            }
        }
    }

    /**
     * 压缩
     *
     * @param zos          压缩输出流
     * @param relativePath 相对路径
     * @param absolutPath  文件或文件夹绝对路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zos, String relativePath,
                            String absolutPath) throws IOException {
        File file = new File(absolutPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File tempFile = files[i];
                if (tempFile.isDirectory()) {
                    String newRelativePath = relativePath + tempFile.getName()
                            + File.separator;
                    createZipNode(zos, newRelativePath);
                    zip(zos, newRelativePath, tempFile.getPath());
                } else {
                    zipFile(zos, tempFile, relativePath);
                }
            }
        } else {
            zipFile(zos, file, relativePath);
        }
    }

    /**
     * 压缩文件
     *
     * @param zos          压缩输出流
     * @param file         文件对象
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void zipFile(ZipOutputStream zos, File file,
                                String relativePath) throws IOException {
        ZipEntry entry = new ZipEntry(relativePath + file.getName());
        zos.putNextEntry(entry);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int BUFFERSIZE = 2 << 10;
            int length = 0;
            byte[] buffer = new byte[BUFFERSIZE];
            while ((length = is.read(buffer, 0, BUFFERSIZE)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.flush();
            zos.closeEntry();
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != is) {
                is.close();
            }
        }
    }

    /**
     * 创建目录
     *
     * @param zos          zip输出流
     * @param relativePath 相对路径
     * @throws IOException
     */
    private static void createZipNode(ZipOutputStream zos, String relativePath)
            throws IOException {
        ZipEntry zipEntry = new ZipEntry(relativePath);
        zos.putNextEntry(zipEntry);
        zos.closeEntry();
    }


    /**
     * 生产文件 如果文件所在路径不存在则生成路径
     *
     * @param fileName    文件名 带路径
     * @param isDirectory 是否为路径
     * @return
     */

    public static File buildFile(String fileName, boolean isDirectory) {

        File target = new File(fileName);

        if (isDirectory) {

            target.mkdirs();

        } else {

            if (!target.getParentFile().exists()) {

                target.getParentFile().mkdirs();

                target = new File(target.getAbsolutePath());

            }

        }

        return target;

    }

}
