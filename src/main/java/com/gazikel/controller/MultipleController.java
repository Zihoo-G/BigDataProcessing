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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MultipleController {

    @Autowired
    private MysqlService mysqlService;

    @GetMapping("/multiple/data/importing")
    public String toImportingPage(HttpSession session, Model model) {

        MultipartFile[] file = (MultipartFile[]) session.getAttribute("file_multiple");

        if (file == null) {
            return "multiple/importing";
        } else {

            List<String> tableName = mysqlService.listTableName();
            model.addAttribute("tableName", tableName);
            model.addAttribute("file_multiple", file);
            for (MultipartFile multipartFile : file) {
                if (".csv".equals(FileUtil.getFileType(multipartFile))) {
                    CsvToXlsxUtil.csvToXLSX(session.getServletContext().getRealPath("file/") + multipartFile.getOriginalFilename());
                }
            }
            return "multiple/excel_process";
        }
    }


    @PostMapping("/multiple/data/get/file")
    public String getFile(@RequestPart("importFile") MultipartFile[] importFile, HttpSession session) {
        if (importFile.length >= 1) {
            session.setAttribute("file_multiple", importFile);
            String filePath = null;
            String fileName = null;
            for (MultipartFile multipartFile : importFile) {
                fileName = multipartFile.getOriginalFilename();
                filePath = session.getServletContext().getRealPath("file/");
                try {
                    FileUtil.uploadFile(multipartFile.getBytes(), filePath, fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // System.out.println(FileUtil.getFileType(importFile));
        return "redirect:/multiple/data/importing";
    }

    @PostMapping("/multiple/data/file/column")
    @ResponseBody
    public Map<String, Object> getFileColumnName(HttpSession session) {
        MultipartFile[] file = (MultipartFile[]) session.getAttribute("file_multiple");
        List<String> columnNameXLS = POIUtil.getColumnNameXLS(file[0], session);
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
    @PostMapping("/multiple/data/input")
    @ResponseBody
    public Map<String, Object> dataInput(@RequestParam("dataLine") int dataLine
            , @RequestParam("table") String table
            , @RequestParam(value = "field[]") List<Integer> field
            , @RequestParam("type") int type
            , HttpSession session) {


        Map<String, Object> map = new HashMap<>();
        MultipartFile[] file = (MultipartFile[]) session.getAttribute("file_multiple");

        for (MultipartFile multipartFile : file) {
            importDataSingle(multipartFile, dataLine, table, field, session);
        }

        map.put("status", 200);
        session.removeAttribute("file_multiple");
        return map;
    }

    public void importDataSingle(MultipartFile file, int dataLine, String table, List<Integer> field, HttpSession session) {

        List<Object> list = new ArrayList<>();
        String sqlStr = "";
        List<String> sql = new ArrayList<>();
        // 查看共有多少行数据
        int rowNumber = POIUtil.getRowNumber(file, session);
        // 循环每一行数据
        for (int i = dataLine; i <= rowNumber; i++) {
            // 得到这一行的数据
            list = POIUtil.getColumnValueByLine(file, i, session);
            // 这一行数据的类型
            List<String> columnTypeByLine = POIUtil.getColumnTypeByLine(file, i, session);
            System.out.println("list size => " + list.size());
            sqlStr = "INSERT INTO " + table + " values(";
            for(int j = 0; j < field.size(); j++) {

                if (Constants.VARCHAR.equals(columnTypeByLine.get(field.get(j)))) {
                    sqlStr += "'" + list.get(field.get(j)) + "'";
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
    }
}
