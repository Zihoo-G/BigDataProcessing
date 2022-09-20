package com.gazikel.controller;

import com.gazikel.service.MysqlService;
import com.gazikel.utils.Constants;
import com.gazikel.utils.CsvToXlsxUtil;
import com.gazikel.utils.FileUtil;
import com.gazikel.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DataImportController {

    @Autowired
    private MysqlService mysqlService;

    @GetMapping("/data/importing")
    public String toImportingPage(HttpSession session, Model model) {

        MultipartFile file = (MultipartFile) session.getAttribute("file");

        if (file == null) {
            return "data_import/importing";
        } else {
            List<String> tableName = mysqlService.listTableName();
            model.addAttribute("tableName", tableName);
            if(".xls".equals(FileUtil.getFileType(file)) || ".xlsx".equals(FileUtil.getFileType(file))) {
                return "data_import/excel_process";
            } else if(".csv".equals(FileUtil.getFileType(file))) {
                CsvToXlsxUtil.csvToXLSX(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());

                return "data_import/excel_process";
            } else {
                return "error/403";
            }
        }
    }

    @PostMapping("/data/get/file")
    public String getFile(@RequestPart("importFile")MultipartFile importFile, HttpSession session) {
        if (!importFile.isEmpty()) {
            session.setAttribute("file", importFile);
            String fileName = importFile.getOriginalFilename();
            String filePath = session.getServletContext().getRealPath("file/");

            try {
                FileUtil.uploadFile(importFile.getBytes(), filePath, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // System.out.println(FileUtil.getFileType(importFile));
        return "redirect:/data/importing";

    }

    @GetMapping("/data/clear")
    public String clearFile(HttpSession session) {
        if (session.getAttribute("file") != null) {
            session.removeAttribute("file");
        }
        return "redirect:/data/importing";
    }

    @PostMapping("/data/column")
    @ResponseBody
    public Map<String, Object> getColumnName(String tableName) {

        List<String> columnName = mysqlService.listColumnName(tableName);
        Map<String, Object> map = new HashMap<>();
        map.put("columnName", columnName);
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/file/column")
    @ResponseBody
    public Map<String, Object> getFileColumnName(HttpSession session) {
        MultipartFile file = (MultipartFile) session.getAttribute("file");
        List<String> columnNameXLS = POIUtil.getColumnNameXLS(file, session);
        Map<String, Object> map = new HashMap<>();
        map.put("columnName", columnNameXLS);
        map.put("status", 200);
        return map;
    }

    /**
     * 将数据导入mysql
     * @param dataLine
     * @param table
     * @param field
     * @param type
     * @return
     */
    @PostMapping("/data/input")
    @ResponseBody
    public Map<String, Object> dataInput(@RequestParam("dataLine") int dataLine
            , @RequestParam("table") String table
            , @RequestParam(value = "field[]") List<Integer> field
            , @RequestParam("type") int type
            , HttpSession session) {


        Map<String, Object> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        List<String> sql = new ArrayList<>();
        String sqlStr = "";
        MultipartFile file = (MultipartFile) session.getAttribute("file");

        // 查看共有多少行数据
        int rowNumber = POIUtil.getRowNumber(file, session);
        //System.out.println("rowNumber" + rowNumber);
        // System.out.println("dataLine=>" + dataLine);
        // 循环每一行数据
        for (int i = dataLine; i <= rowNumber; i++) {
            // 得到这一行的数据
            list = POIUtil.getColumnValueByLine(file, i, session);
            // 这一行数据的类型
            List<String> columnTypeByLine = POIUtil.getColumnTypeByLine(file, i, session);
            sqlStr = "INSERT INTO " + table + " values(";
            for(int j = 0; j < field.size(); j++) {

                if (Constants.VARCHAR.equals(columnTypeByLine.get(field.get(j))) || Constants.LONGTEXT.equals(columnTypeByLine.get(field.get(j)))) {
                    String value = "";
                    if (list.get(field.get(j)) != null && !"".equals(list.get(field.get(j)))) {
                        value = "" + list.get(field.get(j));
                    }
                    if (value.contains("'")) {
                        value = value.replaceAll("'", "-@");
                        // value =value.replaceAll("\"", "--@");
                        // System.out.println("这个value好烦人=>" + value);
                    }

                    sqlStr += "'" + value + "'";


                } else {
                    sqlStr += list.get(field.get(j));
                }

                if (j != field.size()-1) {

                    sqlStr += ", ";
                } else {
                    sqlStr += ")";
                }
            }
            sql.add(sqlStr);
        }

        mysqlService.insert(sql);

        map.put("status", 200);
        session.removeAttribute("file");
        return map;
    }
}
