package com.example.boot.spring_boot_demo.service;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {
    private List<String> positions = new ArrayList<>();
    private String parseFile() throws Exception{
        InputStream ins = new BufferedInputStream(new FileInputStream(new File("D:\\poi\\poitest.docx")));
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(ins);
            // 按行读取
            List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
            for (int i = 0 ; i < paragraphs.size() ; i++) {
                String text = paragraphs.get(i).getText();
                System.out.println(text);
                if(text != null && text.length() != 0){
                    String substring = text;
                    int beginIndex = 0;
                    int endIndex = 0;
                    String citeNum = "";
                    //String test = text;
                    // 将一个段落所有角标内的数字解析出来
                    while(true){
                        int prefixIndex = substring.indexOf("[") + 1;
                        int suffixIndex = substring.indexOf("]");
                        if(prefixIndex <= 0){
                            break;
                        }
                        if(positions.size() == 0){
                            beginIndex = prefixIndex -1;
                        }else {
                            beginIndex = beginIndex + citeNum.length() + 1 + prefixIndex;
                        }
                        citeNum = substring.substring(prefixIndex, suffixIndex);
                        endIndex = beginIndex + citeNum.length() + 2;
                        substring = substring.substring(suffixIndex + 1);
                        System.out.println("循环数字" + citeNum);
                        //index = index + prefixIndex-1 + suffixIndex - prefixIndex;
                        // 段落/开始位置/结束位置/角标数据
                        positions.add(i + "/" + beginIndex + "/" + endIndex + "/" + citeNum);
                    }
                }
            }
        }finally {
            ins.close();
        }
        return null;
    }

    // 1. 主入口
        // -根据论文名判断
            // 已录入过的论文
                // 校验
            // 未录入过的论文
                // 重新录入
    // 2. 录入
        // 解析正文
        // 解析参考文献
        // DB

    // 3. 校验
        // -根据论文名操作DB
        // 校验后生成新的文件
        // 重新录入新文件(事务)
        // 假删原文件


    public static void main(String[] args) throws Exception{
        WordService wordService = new WordService();
        wordService.parseFile();
    }

    /*public static void main(String[] args) {
        String str = "0123456";
        System.out.println(str.substring(0,3));
    }*/

    // 可能的功能
    /**
     * 1. 会增加新的引用
     *
     * 2. 重新排序
     *
     *   -- 两张表
     *   1.content表   1. id   2. 正文开始位置(段落/下标)  结束位置(段落/下标) 3.is_delete 4.update_time 5.SEQ 6.file_id
     *
     *   2.cite表   1. id  2. 引用位置(行/列)  3.content_SEQ  4. is_delete 5.file_id
     *
     *   3.file表  1.id  2.file_name 3.is_new_file  4.file_path
     */


    // 1. 移动语句 改变角标

    // 2. 删除语句 删除引用

}
