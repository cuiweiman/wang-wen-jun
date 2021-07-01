package com.wang.tsdb.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description: OpenCSV 操作 csv
 * @Author: cuiweiman
 * @Since: 2021/6/29 下午4:13
 */
@Slf4j
public class OpenCsvUtil {

    private static final char CSV_DELIMITER = ',';

    /**
     * 解析 csv 文件
     *
     * @param file csv 文件
     */
    public static <T> List<T> readFromCsv(MultipartFile file, Class clazz) {
        try (InputStreamReader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(clazz);
            CsvToBean<T> build = new CsvToBeanBuilder<T>(in).withMappingStrategy(mappingStrategy).withSeparator(CSV_DELIMITER).build();
            return build.parse();
        } catch (IOException e) {
            log.error("IOException {}", e.getMessage());
            throw new RuntimeException();
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
    public static boolean exportToCsv(HttpServletResponse response,
                                      String fileName,
                                      List<String> headerList,
                                      List<String[]> lineDataList) {
        try (final ServletOutputStream outputStream = response.getOutputStream();
             final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "GBK");
             final CSVWriter csvWriter = new CSVWriter(outputStreamWriter)) {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("utf-8");
            csvWriter.writeNext(headerList.toArray(new String[0]));
            csvWriter.writeAll(lineDataList);
            return true;
        } catch (IOException e) {
            log.error("IOException: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 解析无固定格式的 csv data
     *
     * @param inputStream inputStream
     * @return data
     */
    public static boolean readFromCsv(InputStream inputStream) {
        try (InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            CSVReader reader = new CSVReader(in);
            List<String[]> allRecords = reader.readAll();
            for (String[] records : allRecords) {
                for (String filed : records) {
                    log.info("filed= {}", filed);
                }
            }
            reader.close();
            return true;
        } catch (IOException | CsvException e) {
            log.error("IOException {}", e.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * export data to csv
     *
     * @param headerList   field headers
     * @param lineDataList line data array
     * @return byte[]
     */
    public static byte[] writeToBytes(List<String> headerList,
                                      List<String[]> lineDataList) {
        byte[] bytes = null;
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             final OutputStreamWriter outputStreamWriter =
                     new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             final CSVWriter csvWriter = new CSVWriter(outputStreamWriter)) {
            csvWriter.writeNext(headerList.toArray(new String[0]));
            csvWriter.writeAll(lineDataList);
            csvWriter.close();
            outputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            log.error("IOException: {}", e.toString());
        }
        return bytes;
    }

}












