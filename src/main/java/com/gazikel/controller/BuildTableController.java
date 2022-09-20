package com.gazikel.controller;

import com.gazikel.pojo.User;
import com.gazikel.service.FileService;
import com.gazikel.service.MysqlService;
import com.gazikel.utils.Constants;
import com.gazikel.utils.CsvToXlsxUtil;
import com.gazikel.utils.FileUtil;
import com.gazikel.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BuildTableController {

    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private FileService fileService;

    @GetMapping("/table/importing")
    public String toBuildTablePage(HttpSession session, Model model) {

        MultipartFile file = (MultipartFile) session.getAttribute("file_table");

        if (file != null) {
            String fileType = FileUtil.getFileType(file);

            if (".xls".equals(fileType) || ".xlsx".equals(fileType)) {
                return "build_table/excel_process";
            } else if (".csv".equals(fileType)) {
                // 将csv文件转换为xlsx文件
                CsvToXlsxUtil.csvToXLSX(session.getServletContext().getRealPath("file/") + file.getOriginalFilename());
                return "build_table/excel_process";
            } else {
                model.addAttribute("error", "该文件类型不符合规范");
                return "build_table/importing";
            }
        } else{
            return "build_table/importing";
        }

    }

    @GetMapping("/table")
    public String toBuildPage() {
        return "build_table/build";
    }

    @PostMapping("/table/file-upload")
    @ResponseBody
    public Map<String, Object> fileUpload(@RequestPart("importFile") MultipartFile file, HttpSession session) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String filename = file.getOriginalFilename();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        User user = (User) session.getAttribute("user");
        // 文件后缀名
        String extension = FileUtil.getFileType(file);
        // 表名
        String tableName = simpleDateFormat.format(new Date()) + "_" + UUID.randomUUID().toString().replace("-", "");
        // 上传文件路径
        String filePath = ResourceUtils.getURL("classpath:").getPath() + Constants.BASE_PATH + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 将文件信息保存在数据库
        fileService.addFile(new com.gazikel.pojo.File(0, filename, tableName, user.getId(), extension, filePath + "/" + tableName + extension));

        String sql = fileService.uploadFile(file, session);

        mysqlService.createTable(sql);

        map.put("status", 200);
        return map;
    }

    @GetMapping("/table/clear")
    public String tableClear(HttpSession session) {
        session.removeAttribute("file_table");
        return "redirect:/table/importing";
    }


    @PostMapping("/table/get/file")
    public String getFile(@RequestPart("importFile") MultipartFile file, HttpSession session) {
        if (!file.isEmpty()) {
            session.setAttribute("file_table", file);
            String fileName = file.getOriginalFilename();
            System.out.println(fileName);
            String filePath = session.getServletContext().getRealPath("file/");

            try {
                FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/table/importing";
    }

    @PostMapping("/table/input")
    @ResponseBody
    public Map<String, Integer> buildTable(@RequestParam("name") String name
            , @RequestParam("field") int field
            , @RequestParam("comment") int comment
            , @RequestParam("units") int units
            , @RequestParam("test") int test
            , HttpSession session) {

        Map<String, Integer> map = new HashMap<>();
        MultipartFile file_table = (MultipartFile) session.getAttribute("file_table");

        System.out.println("------->" + file_table.getOriginalFilename());
        // 解析三行的内容
        // 1. 字段行
        System.out.println(field);
        List<String> fieldColumn = POIUtil.getColumnNameByLine(file_table, field, session);

        for (String s : fieldColumn) {
            System.out.println(s);
        }
        List<String> commentColumn = new ArrayList<>();
        if(comment != 0) {
            // 2. 注释行
            commentColumn = POIUtil.getColumnNameByLine(file_table, comment, session);
        } else {
            for (int i = 0; i < fieldColumn.size(); i++) {
                commentColumn.add("暂无注释信息");
            }
        }


        if(units != 0) {
            // 3. 单位行
            List<String> unitsColumn = POIUtil.getColumnNameByLine(file_table, units, session);
        }


        // 通过测试行获取字段类型
        List<String> typeColumn = POIUtil.getColumnTypeByLine(file_table, test, session);


        // 根据内容生成sql语句
        String sql  = "CREATE TABLE " + name + "(";

        for (int i=0; i < fieldColumn.size(); i++) {
            if (i == 0) {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " NOT NULL COMMENT '" + commentColumn.get(i) + "',";
            } else if(i == fieldColumn.size()-1) {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " COMMENT '" + commentColumn.get(i) + "')";
            } else {
                sql += "`" + fieldColumn.get(i) + "` " + typeColumn.get(i) + " COMMENT '" + commentColumn.get(i) + "',";
            }
        }

        System.out.println(sql);
        mysqlService.createTable(sql);

        // 成功保存
        map.put("status", 200);
        // 删除session中的数据
        session.removeAttribute("file_table");
        return map;
    }

}
