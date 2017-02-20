package com.koolearn.cloud.util;

import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;
import java.util.Map;

public class PDFUtil {
    private static Logger logger = Logger.getLogger(PDFUtil.class);

    void getPDFText(String[] args) throws IOException, BadSecurityHandlerException, CryptographyException {

        boolean toConsole = false;
        boolean toHTML = false;
        boolean force = false;
        boolean sort = false;
        boolean separateBeads = true;
        boolean useNonSeqParser = false;
        String password = "";
        String encoding = null;
        String pdfFile = "c:\\jianli.pdf";
        String outputFile = null;
        // Defaults to text files
        String ext = ".txt";
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;

        Writer output = null;
        PDDocument document = null;
        try {
            if (outputFile == null && pdfFile.length() > 4) {
                outputFile = new File(pdfFile.substring(0, pdfFile.length() - 4) + ext).getAbsolutePath();
            }
            if (useNonSeqParser) {
                document = PDDocument.loadNonSeq(new File(pdfFile), null, password);
            } else {
                document = PDDocument.load(pdfFile, force);
                if (document.isEncrypted()) {
                    StandardDecryptionMaterial sdm = new StandardDecryptionMaterial(password);
                    document.openProtection(sdm);
                }
            }

            AccessPermission ap = document.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text");
            }

            if ((encoding == null) && (toHTML)) {
                encoding = "UTF-8";
            }

            if (toConsole) {
                output = new OutputStreamWriter(System.out);
            } else {
                if (encoding != null) {
                    output = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
                } else {
                    // use default encoding
                    output = new OutputStreamWriter(new FileOutputStream(outputFile));
                }
            }

            PDFTextStripper stripper = null;
            if (toHTML) {
                stripper = new PDFText2HTML(encoding);
            } else {
                stripper = new PDFTextStripper(encoding);
            }
            stripper.setForceParsing(force);
            stripper.setSortByPosition(sort);
            stripper.setShouldSeparateByBeads(separateBeads);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);

            // Extract text for main document:
            stripper.writeText(document, output);

            System.out.println(stripper.getText(document));
            // ... also for any embedded PDFs:
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDDocumentNameDictionary names = catalog.getNames();
            if (names != null) {
                PDEmbeddedFilesNameTreeNode embeddedFiles = names.getEmbeddedFiles();
                if (embeddedFiles != null) {
                    Map<String, COSObjectable> embeddedFileNames = embeddedFiles.getNames();
                    if (embeddedFileNames != null) {
                        for (Map.Entry<String, COSObjectable> ent : embeddedFileNames.entrySet()) {

                            PDComplexFileSpecification spec = (PDComplexFileSpecification) ent.getValue();
                            PDEmbeddedFile file = spec.getEmbeddedFile();
                            if (file != null && file.getSubtype().equals("application/pdf")) {

                                InputStream fis = file.createInputStream();
                                PDDocument subDoc = null;
                                try {
                                    subDoc = PDDocument.load(fis);
                                } finally {
                                    fis.close();
                                }
                                try {
                                    stripper.writeText(subDoc, output);
                                } finally {
                                    subDoc.close();
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * @param pdfFilepath
     * @param force
     * @param wrighttextfile 是否保存测试文件
     * @param encoding       默认utf-8
     * @throws java.io.IOException
     * @throws org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException
     * @throws org.apache.pdfbox.exceptions.CryptographyException
     * @author liyong
     */
    public static String getPDFText(String pdfFilepath, boolean force, boolean wrighttextfile, String encoding) throws IOException,
            BadSecurityHandlerException, CryptographyException {

        boolean sort = false;
        boolean separateBeads = false;
        String outputFile = null;
        // Defaults to text files
        String ext = ".txt";
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;

        Writer output = null;
        PDDocument document = null;
        try {
            if (outputFile == null && pdfFilepath.length() > 4) {
                outputFile = new File(pdfFilepath.substring(0, pdfFilepath.length() - 4) + ext).getAbsolutePath();
            }
            document = PDDocument.load(pdfFilepath, force);

            AccessPermission ap = document.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text");
            }

            if (encoding != null) {
                encoding = "utf-8";
            }

            output = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);

            PDFTextStripper stripper = null;

            stripper = new PDFTextStripper(encoding);
            stripper.setForceParsing(force);
            stripper.setSortByPosition(sort);
            stripper.setShouldSeparateByBeads(separateBeads);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);

            String text = stripper.getText(document);

            if (wrighttextfile) {
                // Extract text for main document:
                stripper.writeText(document, output);
                // ... also for any embedded PDFs:
                PDDocumentCatalog catalog = document.getDocumentCatalog();
                PDDocumentNameDictionary names = catalog.getNames();
                if (names != null) {
                    PDEmbeddedFilesNameTreeNode embeddedFiles = names.getEmbeddedFiles();
                    if (embeddedFiles != null) {
                        Map<String, COSObjectable> embeddedFileNames = embeddedFiles.getNames();
                        if (embeddedFileNames != null) {
                            for (Map.Entry<String, COSObjectable> ent : embeddedFileNames.entrySet()) {

                                PDComplexFileSpecification spec = (PDComplexFileSpecification) ent.getValue();
                                PDEmbeddedFile file = spec.getEmbeddedFile();
                                if (file != null && file.getSubtype().equals("application/pdf")) {

                                    InputStream fis = file.createInputStream();
                                    PDDocument subDoc = null;
                                    try {
                                        subDoc = PDDocument.load(fis);
                                    } finally {
                                        fis.close();
                                    }
                                    try {
                                        stripper.writeText(subDoc, output);
                                    } finally {
                                        subDoc.close();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return text;
        } finally {
            if (output != null) {
                output.close();
            }
            if (document != null) {
                document.close();
            }
        }
    }

    public static String getPdfContext(String fileName) {
        String result = "";
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            PDFParser parser = new PDFParser(in);
            // 对PDF文件进行解析
            parser.parse();
            // 获取解析后得到的PDF文档对象
            PDDocument pdfdocument = parser.getPDDocument();
            // 新建一个PDF文本剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            // 从PDF文档对象中剥离文本
            result = stripper.getText(pdfdocument);
        } catch (Exception e) {
            logger.error("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

    /**
     * 多个换行转换成一个
     *
     * @param str
     * @return
     */
    public static String FuhaoMany2One(String str) {
        str = str.replaceAll("\n{1,}", "\n");
        return str;
    }
}
