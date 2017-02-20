package com.koolearn.cloud.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 文件上传工具类.
 */
public class FileUploadUtils implements Serializable {

    public static final String PATH_PREFIX = "/cloud/upload/";

    public static final String PATH_KEY = "disk_path_root";

    private static final String ACCESS_PATH_KEY = "access_path_root";

    private static final String SHARE_IP_KEY = "share_ip_key";

    private static final String CONVERT_URL_NAME = "convert_url";
    private static final String CONVERT_CALLBACK = "convert_callback";
    private static final int MOD_VALUE = 30000;

    /**
     * 转换服务请求类型 Conversion 转换请求 Query 转换状态查询
     */
    public static final String CONVER_SERVER_ASK_CONVERSION = "Conversion";
    public static final String CONVER_SERVER_ASK_QUERY = "Query";
    public static final String CONVER_SERVER_KEY = "cloud-12345";
    /**
     * 资源图片所支持的格式
     */
    public static final String IMAGE_EXTEND = "bmp gif jpeg jpg png";
    // 磁盘挂载根路径.
    private static String rootPath;
    // 网络访问路径
    private static String accessRootPath;
    // 共享路径
    private static String shareRootPath;
    // 转换服务路径
    private static String convertUrl;
    // 转换回调
    private static String converCallBack;

    /**
     * 富文本保存 相对路径 前缀
     */
    public static final String RICHTEXT_RELATIVE_PATH = "/richText/";
    /**
     * 文件上传 相对路径 前缀
     */
    public static final String FILE_UPLOAD_RELATIVE_PATH = "/uploadFile/";

    private static final String DOWNLOAD_PATH_PREFIX = "/download/";

    static {
        try {
            rootPath = (String) IndexNameUtils.getValueByKey(PATH_KEY);
            accessRootPath = (String) IndexNameUtils
                    .getValueByKey(ACCESS_PATH_KEY);
            shareRootPath = "\\\\" + IndexNameUtils.getValueByKey(SHARE_IP_KEY) + "/share";
            convertUrl = (String) IndexNameUtils
                    .getValueByKey(CONVERT_URL_NAME);
            converCallBack = (String) IndexNameUtils
                    .getValueByKey(CONVERT_CALLBACK);

            rootPath = removeSlashAndBackslash(rootPath);
            accessRootPath = removeSlashAndBackslash(accessRootPath);
            //shareRootPath = removeSlashAndBackslash(shareRootPath);
            convertUrl = removeSlashAndBackslash(convertUrl);
            converCallBack = removeSlashAndBackslash(converCallBack);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID和类型获得新文件名
     *
     * @param id   主键
     *             文件类型
     * @param type 文件类型
     * @return 新文件名
     */
    public static String getTextNameByIdAndType(Long id, String type) {
        return id + "_" + type + "." + type;
    }

    /**
     * 根据ID和类型获得新文件名
     *
     * @param id         主键
     * @param extendNmae 文件扩展名
     * @return 新文件名
     */
    public static String getNameByIdAndType(Integer id, String extendNmae) {
        return id + "." + extendNmae;
    }

    /**
     * 根据ID和类型获得新富文本文件路径.
     *
     * @param id   主键
     * @param type 文件类型
     * @param func 所属模块
     * @return 新文件路径
     */
    public static String getTextPathByIdAndType(Integer id, String type,
                                                Integer func) {
        return PATH_PREFIX + func + "/" + (id / MOD_VALUE) + "/" + id + "_"
                + type + "." + type;
    }

    /**
     * 根据资源主键ID和类型获得新文件路径
     *
     * @param id             主键
     * @param extendNmae     资源文件扩展名
     * @param resourceFormat 资源格式
     * @return 新文件路径
     */
    public static String getPathByIdAndType(Integer id, String extendNmae,
                                            Integer resourceFormat) {

        return getDirPathByIdAndType(id, resourceFormat) + "." + extendNmae;
    }

    /**
     * 根据随机ID和类型获得新文件路径
     *
     * @param extendNmae     资源文件扩展名
     * @param resourceFormat 资源格式
     * @return 新文件路径
     */
    public static String getPathByRandomIdAndType(String extendNmae,
                                                  Integer resourceFormat) {
        long id = generate();
        return PATH_PREFIX + resourceFormat + "/" + (id / MOD_VALUE) + "/" + id
                + "." + extendNmae;
    }

    public static String getPathByRandomIdAndType(Integer resourceFormat) {
        long id = generate();
        return PATH_PREFIX + resourceFormat + "/" + (id / MOD_VALUE) + "/" + id;
    }

    public synchronized static Long generate() {
        return System.currentTimeMillis();
    }

    /**
     * 获得文件夹路径
     *
     * @param id             主键
     * @param resourceFormat 资源格式
     * @return 文件夹路径
     */
    public static String getDirPathByIdAndType(Integer id,
                                               Integer resourceFormat) {
        return PATH_PREFIX + resourceFormat + "/" + (id / MOD_VALUE) + "/" + id;
    }

    /**
     * 根据相对路径获得磁盘绝对路径.
     *
     * @param relativePath 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String relativePath) {
        return rootPath + relativePath;
    }

    /**
     * 根据相对路径获得访问绝对路径.
     *
     * @param relativePath 相对路径
     * @return 绝对路径
     */
    public static String getAccessAbsolutePath(String relativePath) {
        return accessRootPath + relativePath;
    }

    /**
     * 根据相对路径获得访问绝对路径.
     *
     * @param relativePath 相对路径
     * @return 绝对路径
     */
    public static String getShareAbsolutePath(String relativePath) {
        String path = shareRootPath + relativePath;
        return path.replace("//", "\\\\");
    }

    /**
     * 文件写入.
     * FileUploadUtils.saveFileFromInputStream(upFile.getInputStream(),filepath);
     *
     * @param stream 文件流
     * @param path   相对路径
     * @throws java.io.IOException
     */
    @SuppressWarnings("unused")
    public static void saveFileFromInputStream(InputStream stream, String path)
            throws IOException {
        File file = new File(getAbsolutePath(path));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fs = new FileOutputStream(getAbsolutePath(path));
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }

        fs.flush();
        fs.close();
        stream.close();
    }

    /**
     * 文件写入.
     * <p/>
     * 文件流
     *
     * @param path 相对路径
     * @throws java.io.IOException
     */
    @SuppressWarnings("unused")
    public static void saveSourceFile(File sourceFile, String path)
            throws IOException {
        File file = new File(getAbsolutePath(path));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fs = new FileOutputStream(getAbsolutePath(path));
        byte[] buffer = new byte[1024 * 1024];
        int bytesum = 0;
        int byteread = 0;

        InputStream stream = new FileInputStream(sourceFile);
        while ((byteread = stream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }

        fs.flush();
        fs.close();
        stream.close();
    }

    /**
     * 将富文本内容写入文件.
     *
     * @param content 富文本内容
     * @param path    相对路径
     * @throws java.io.IOException
     */
    public static void saveFileFromText(String content, String path)
            throws IOException {
        File file = new File(getAbsolutePath(path));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        file.createNewFile();

        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
                file), "UTF-8");
        out.write(content);
        out.flush();
        out.close();
    }

    private static String removeSlashAndBackslash(String path) {
        if (path != null && !path.equals("")
                && (path.endsWith("\\") || path.equals("/"))) {
            path = path.substring(1);
        }

        return path;
    }

    /**
     * 根据相对地址获得富文本内容
     *
     * @param path
     * @return
     */
    public static String getRichTextContentByPath(String path) {
        try {
            File file = new File(getAbsolutePath(path));
            if (!file.exists() || file.isDirectory()) {
                return "";
            }
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    file), "utf-8");
            BufferedReader read = new BufferedReader(isr);
            String s = null;
            StringBuffer sb = new StringBuffer();
            while ((s = read.readLine()) != null) {
                // System.out.println(s);
                if (s.trim().length() > 1) {
                    sb.append(s.trim());
                }
            }

            read.close();
            isr.close();

            return sb.toString();
        } catch (Exception e) {

        }
        return "";
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        FileUploadUtils.rootPath = rootPath;
    }

    public static void createDir(String path) {
        File file = new File(getAbsolutePath(path));
        file.mkdirs();
        // writable:true允许有写权限  ownerOnly:ture只有所有者有写权限，false所有用户都有权限
        file.setWritable(true,false);
    }

    /**
     * 获得文件转换URL.
     * <p/>
     * 转换id
     *
     * @param action    1 Conversion 转换请求 2 Query 转换状态查询
     * @param filePath  需要转换的文件
     * @param desFolder 转换后的文件存放的位置
     *                  回调（回调返回参数： id：转换ID status：转换状态，1为成功，0为失败 ret 其他返回） 转换接口
     *                  http://app2013:8081/StartConvert.ashx?Action=Conversion
     *                  &ID=0010 &Key=CONVER_SERVER_KEY
     *                  &FilePath=\\server\docs\folder\file.docx
     *                  &DesFolder=\\server\pdfdocs\folder\myfile &CallBack=xxx 查询状态：
     *                  http://app2013:8081/StartConvert.ashx?Action=Query &ID=0010
     *                  &Key=CONVER_SERVER_KEY
     * @return
     */
    public static String getConvertUrl(String action, Integer id,
                                       String filePath, String desFolder) throws UnsupportedEncodingException {
        String url = convertUrl;
        if (CONVER_SERVER_ASK_CONVERSION.equals(action)) {
            url += "?Action=" + action + "&Key=" + CONVER_SERVER_KEY + "&ID=" + id + ""
                    + "&FilePath=" + URLEncoder.encode(filePath, "utf-8") + "&DesFolder=" + URLEncoder.encode(desFolder, "utf-8")
                    + "&CallBack=" + converCallBack;
        } else if (CONVER_SERVER_ASK_QUERY.equals(action)) {
            url += "?Action=" + action + "&Key=" + CONVER_SERVER_KEY + "&ID=" + id;
        }
        return url;
    }

    /**
     * 调用文件转换服务，返回HTTP状态.
     *
     * @param filePath
     *            源文件相对路径
     * @param desFolder
     *            目的文件夹相对路径
     * @return HTTP状态
     */

    /**
     * @param richText
     * @param parentDirectory Constants.QUESTION_MATERIAL
     * @return 返回文件相对路径
     */
    public static String generateRichTextFile(String richText,
                                              String parentDirectory) {
        if (StringUtils.isBlank(richText))
            return "";
        // String realPath = request.getRealPath("resources/upload/"
        // + parentDirectory);
        // 服务器 磁盘 根路径.
        String realPath = FileUploadUtils.getRootPath();
        Random r = new Random();
        // 相对路径
        String relativePath = FileUploadUtils.RICHTEXT_RELATIVE_PATH
                + parentDirectory + "/" + r.nextInt(999999999) % MOD_VALUE;
        realPath += relativePath;
        try {
            File fp = new File(realPath);
            // 创建目录
            if (!fp.exists()) {
                fp.mkdirs();// 目录不存在的情况下，创建目录。
            }
            String fileName = UUID.randomUUID().toString() + ".html";
            // 文件绝对路径
            realPath = FileUploadUtils.getAbsolutePath(relativePath + "/"
                    + fileName);
            byte[] bb = richText.getBytes("UTF-8");
            FileCopyUtils.copy(bb, new File(realPath));

            return relativePath + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String pathFix = "";

    /**
     * 根据富文本路径，获得文件内容 FileUploadUtils
     *
     * @param content1Url    相对路径或绝对路径
     * @param isAbsolutePath true 表示绝对路径
     * @return
     */
    @SuppressWarnings("resource")
    public static String getRichUrlText(String content1Url,
                                        boolean isAbsolutePath) {
        if (StringUtils.isBlank(content1Url))
            return "";
        StringBuilder sb = new StringBuilder();
        String absolutePath = isAbsolutePath ? content1Url : FileUploadUtils
                .getAbsolutePath(content1Url);
        File file = new File(absolutePath);
        if (!file.exists())
            return "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            // BufferedReader br = new BufferedReader(new
            // InputStreamReader(inputStream, "UTF-8"));
            BufferedReader br = new BufferedReader(new UnicodeReader(
                    inputStream, Charset.defaultCharset().name()));
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            String text = sb.toString();
            if (StringUtils.isNotBlank(text)) {
                String regex = "</?span[^>]*>";
                text = text.replaceAll(regex, " ");
                return text.trim();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据ID和类型获得新富文本文件路径.
     *
     * @param id   主键
     * @param type 文件类型
     * @return 新文件路径
     */
    public static String getDowloadZIPFileByIdAndType(Long id, String type) {
        return DOWNLOAD_PATH_PREFIX + type + File.separator + id + ".zip";
    }

    public static List<String> getAllFilePathByDir(String rootPath) {
        List<String> pathList = new ArrayList<String>();

        File root = new File(getAbsolutePath(rootPath));
        if (root.exists() && root.isDirectory()) {
            getChildrenFilePathByDirectory(pathList, root);
        }

        return pathList;
    }

    private static void getChildrenFilePathByDirectory(List<String> pathList,
                                                       File root) {
        File[] children = root.listFiles();
        for (File childFile : children) {
            if (childFile.isDirectory()) {
                getChildrenFilePathByDirectory(pathList, childFile);
            } else {
                String path = childFile.getAbsolutePath();
                path = path.substring(rootPath.length());

                pathList.add(path);
            }
        }

    }

    /**
     * 资源文档icon图
     * word文档：.doc;.docm;.docx;.dot;.dotm;.dotx;.odt
     * ppt文档：.odp;.pot;.potm;.potx;.pps;.ppsm;.ppsx;.ppt;.pptm;.pptx
     * .pdf
     * 图片：GIF JPEG JPG PNG BMP
     */
    public static final Map<String, String> DOCUMENT_ICON_MAP = new HashMap<String, String>() {

        {
            put("doc", "WORD.png");
            put("docx", "WORD.png");
            put("xls", "EXCEL.png");
            put("xlsx", "EXCEL.png");
            put("pptx", "PPT.png");
            put("ppt", "PPT.png");
            put("pot", "PPT.png");
            put("pps", "PPT.png");
            put("rtf", "PPT.png");
            put("pdf", "PDF.png");
            put("mp3", "MP3.png");
            put("mp4", "MP4.png");
            put("gif", "IMAGE.png");
            put("jpeg", "IMAGE.png");
            put("jpg", "IMAGE.png");
            put("png", "IMAGE.png");
            put("bmp", "IMAGE.png");
            put("other", "MP3.png");
        }
    };

    /**
     * 获取文档转换目录分类 根据扩展名获得格式类型，作为目录分类
     *
     * @return
     */
    public static Integer getModuleValue(String extendName) {
        if (StringUtils.isBlank(extendName)) {
            return GlobalConstant.RESOURCE_FORMAT_OTHER;
        }
        extendName = extendName.toLowerCase();
        if (extendName.contains("txt")) {
            return GlobalConstant.RESOURCE_FORMAT_TEXT;
        } else if (extendName.contains("doc")) {
            return GlobalConstant.RESOURCE_FORMAT_WORD;
        } else if (extendName.contains("ppt")) {
            return GlobalConstant.RESOURCE_FORMAT_PPT;
        } else if (extendName.contains("pdf")) {
            return GlobalConstant.RESOURCE_FORMAT_PDF;
        } else if (extendName.contains("xls")) {
            return GlobalConstant.RESOURCE_FORMAT_EXCEL;
        } else if (extendName.contains("mp3")) {
            return GlobalConstant.RESOURCE_FORMAT_AUDIO;
        } else if (extendName.contains("mp4")) {
            return GlobalConstant.RESOURCE_FORMAT_VIDEO;
        } else if (IMAGE_EXTEND.contains(extendName)) {
            return GlobalConstant.RESOURCE_FORMAT_IMAGE;
        } else {
            return GlobalConstant.RESOURCE_FORMAT_OTHER;
        }
    }

    /**
     * 资源文件大小限制
     *
     * @param format
     * @param size
     * @return
     */
    public static boolean rightSizeOfDocPptTxt(int format, long size) {
        //word ppt txt 限制大小10m
        //音频pdf 限制大小100m
        boolean flag = false;
        long sizeM = size / (1024 * 1024);
        flag = sizeM < 100;
        if (format == GlobalConstant.RESOURCE_FORMAT_WORD) {
            //资源 word文档大小限制
            sizeM = size / (1024 * 1024);
            flag = sizeM < 10;
            return flag;
        } else if (format == GlobalConstant.RESOURCE_FORMAT_PPT) {
            //资源 PPt文档大小限制
            sizeM = size / (1024 * 1024);
            flag = sizeM < 10;
            return flag;
        }
        return flag;
    }

    /**
     * 根据文件扩展名，获取icon标识图片
     *
     * @return
     */
    public static String getDocumentIcon(String extendName) {
        String icon = DOCUMENT_ICON_MAP.get(extendName);
        if (StringUtils.isNotBlank(icon)) {
            return icon;
        } else {
            return DOCUMENT_ICON_MAP.get("other");
        }

    }

    /**
     * 根据文件扩展名，获取icon标识 小图片
     *
     * @return
     */
    public static String getDocumentIconSmall(String extendName) {
        String bigIcon = getDocumentIcon(extendName);

        return "SMALL_" + bigIcon.substring(0, bigIcon.lastIndexOf("."));
    }

    /**
     * @param size 单位字节
     * @return
     */
    public static String unitConversion(Integer size) {
        if (size == null) return "";
        long _1Kb = 1024l;
        long _1Mb = 1024l * 1024l;
        long _1Gb = 1024l * 1024l * 1024l;
//		long _1Tb=1024l*1024l*1024l*1024l;
        BigDecimal bdSize = new BigDecimal(size);
        if (size <= _1Mb) {
            BigDecimal bd1Kb = new BigDecimal(_1Kb);
            double s = bdSize.divide(bd1Kb, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return s + "K";
        } else if (size <= _1Gb) {
            BigDecimal bd1Mb = new BigDecimal(_1Mb);
            double s = bdSize.divide(bd1Mb, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return s + "M";
        }
//		else if(size<=_1Tb){
//			BigDecimal bd1Gb=new  BigDecimal(_1Gb);
//			double s=bdSize.divide(bd1Gb, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
//			return s+"G";
//		}
        return size + "B";
    }


    /**
     * 根据文件名获取类型
     *
     * @param filename 直线运动的图象.pptx
     * @return
     * @throws Exception
     */
    private static String getType(String filename) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filename);
        return mimeType;
    }

    public static void main(String[] args) {
        Integer[] ida = {32369, 32379, 32373, 32347, 32339, 32341};
        for (Integer id : ida) {
            System.out.println("/tol/data" + getDirPathByIdAndType(id, GlobalConstant.RESOURCE_FORMAT_WORD));
        }
        System.out.println(getPathByRandomIdAndType("doc", 1));
//		try {
//			System.out.println(getType("D:/tol/data/mooc/image/q.txt"));;
//			return ;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		getModuleValue("JPeG");
//		File file=new File("D:\\ttt\\q\\员工转正自我考核表.doc");  
//		if(file.exists())
//		{
//		file.renameTo(new File("d:\\ttt\\员工转正自我考核表.doc"));
//		}
    }
    
	/**
	 * 图片限制jpg,jpeg,png,gif   图片大小限制 2M
	 * @param format
	 * @param size
	 * @param extendName 
	 * @return
	 */
	public static boolean verifySizeType(long size,String extendName, long limitSize) {
		boolean flag=false;
		if("jpg,jpeg,png,gif,bmg".indexOf(extendName)==-1){
			return flag;
		}
		flag=size<=limitSize;
		return flag;
	}
	
	/**
	 * 文件写入.
	 * 
	 * @param stream
	 *            文件流
	 * @param path
	 *            文件相对路径
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void saveQuestionPic(InputStream stream, String path)
			throws IOException {
		File file = new File(getAbsolutePath(path));
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fs = new FileOutputStream(getAbsolutePath(path));
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}

		fs.flush();
		fs.close();
		stream.close();
	}
	
	public static String getPathByRandomId(String extendName,
			String path) {
		long id = generate();
		return path + "/" + (id / MOD_VALUE) + "/" + id
				+ "." + extendName;
	}

}
