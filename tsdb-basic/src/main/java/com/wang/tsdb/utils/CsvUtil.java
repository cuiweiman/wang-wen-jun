package com.wang.tsdb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Description: csv 文件进行数据的 导入和导出
 * @Author: cuiweiman
 * @Since: 2021/6/28 下午8:45
 */
@Slf4j
public class CsvUtil {

    /**
     * 导出到 CSV
     *
     * @param fileName     csv 文件名
     * @param headerList   field 文件头
     * @param lineDataList 行数据集合
     * @return 结果
     */
    public static boolean exportToCsv(HttpServletResponse response,
                                      String fileName,
                                      List<String> headerList,
                                      List<Object[]> lineDataList) {
        try (OutputStream os = new BufferedOutputStream(response.getOutputStream());
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os, "GBK")) {
            // 初始化 CSVFormat
            CSVFormat format = CSVFormat.DEFAULT.withHeader(headerList.toArray(new String[0]));
            //创建CSVPrinter对象
            CSVPrinter printer = new CSVPrinter(outputStreamWriter, format);
            for (Object[] lineData : lineDataList) {
                printer.printRecord(lineData);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/download;charset=gb2312");
            response.setContentType("application/csv;charset=gb2312");
            response.setCharacterEncoding("utf-8");
            printer.close(true);
            return true;
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            return false;
        }
    }


    /**
     * 导出到 CSV
     *
     * @param fileName     csv 文件名
     * @param headerList   field 文件头
     * @param lineDataList 行数据集合
     * @return 结果
     */
    public static boolean exportToCsv(String fileName,
                                      List<String> headerList,
                                      List<String[]> lineDataList) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("/Users/cuiweiman/Documents/".concat(fileName));
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "GBK")) {
            // 初始化 CSVFormat
            CSVFormat format = CSVFormat.DEFAULT.withHeader(headerList.toArray(new String[0]));
            //创建CSVPrinter对象
            CSVPrinter printer = new CSVPrinter(outputStreamWriter, format);
            for (String[] lineData : lineDataList) {
                printer.printRecord(lineData);
            }
            printer.close(true);
            return true;
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            return false;
        }
    }


}
