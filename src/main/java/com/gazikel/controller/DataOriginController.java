package com.gazikel.controller;

import com.gazikel.service.FileService;
import com.gazikel.service.MysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DataOriginController {

    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private FileService fileService;

    @GetMapping("/data/origin/table")
    public String toDataOriginController(Model model) {

        List<Map> maps = mysqlService.listTable();
        for (Map map : maps) {
            System.out.println(map.get("TABLE_NAME"));
        }
        model.addAttribute("maps", maps);

        return "data_origin/data_origin_table";
    }

    @GetMapping("/table/browse")
    public String toDataBrowsePage(@RequestParam("tableName") String tableName, Model model) {

        System.out.println("============");
        List<String> columnNames = mysqlService.listColumnName(tableName);

        List<Map> allData = mysqlService.getAllData(tableName);

        model.addAttribute("columnNames", columnNames);
        model.addAttribute("datas", allData);
        model.addAttribute("tableName", tableName);

        return "data_origin/data_browser";
    }

    /**
     * 跳转视图
     * @return  data_origin_field
     */
    @GetMapping("/data/origin/field")
    public String toDataOriginFieldPage() {
        return "data_origin/data_origin_field";
    }

    @PostMapping("/data/origin/field/info")
    @ResponseBody
    public Map<String, Object> getDataOriginFieldInfo() {
        List<String> tableList = new ArrayList<>();
        List<Map> columnsList = new ArrayList<>();

        // 获取所有表名
        List<Map> maps = mysqlService.listTable();
        for (Map map : maps) {
            tableList.add((String) map.get("TABLE_NAME"));
        }

        int i = 0;
        for (String s : tableList) {
            List<String> columnList = mysqlService.listColumnName(s);
            System.out.println(columnList);
            for (String column : columnList) {
                // 循环每一个字段
                Map<String, Object> map = new HashMap<>();
                map.put("name", column);
                map.put("pid", i);
                // j++;
                columnsList.add(map);
            }
            i++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("table", tableList);
        result.put("column", columnsList);
        return result;
    }


    @PostMapping("/data/origin/field/alert/table/name")
    @ResponseBody
    public Map<String, Object> alertTableName(@RequestParam("name") String name, @RequestParam("newName") String newName) {
        mysqlService.alertTableName(name, newName);
        // 修改文件表中的信息
        int i = fileService.updateTableNameByTableName(name, newName);
        Map<String, Object> map = new HashMap<>();
        if (i != 1) {
            map.put("status", 201);
        } else {
            map.put("status", 200);
        }



        return map;
    }

    @PostMapping("/data/origin/field/alert/field/name")
    @ResponseBody
    public Map<String, Object> alertFieldName(@RequestParam("tableName") String tableName
            , @RequestParam("oldFieldName") String oldFieldName
            , @RequestParam("newFieldName") String newFieldName) {

        String type = mysqlService.getDataTypeByColumnName(tableName, oldFieldName);

        mysqlService.alertFieldName(tableName, oldFieldName, newFieldName, type);
        Map<String, Object> map = new HashMap<>();
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/origin/field/drop")
    @ResponseBody
    public Map<String, Object> dropField(@RequestParam("tableName") String tableName, @RequestParam("fieldName") String fieldName) {
        mysqlService.dropFiled(tableName, fieldName);
        Map<String, Object> map = new HashMap<>();
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/origin/delete")
    @ResponseBody
    public Map<String, Integer> dropTable(@RequestParam("tableName") String tableName) {
        mysqlService.dropTable(tableName);

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/origin/info")
    @ResponseBody
    public Map<String, Object> getDataInfo(@RequestParam("tableName") String tableName) {
        List<Map> maps = mysqlService.listColumns(tableName);
        Map<String, Object> map = new HashMap<>();
        map.put("infos", maps);
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/origin/table/name")
    @ResponseBody
    public Map<String, Object> getTableName() {
        List<String> tableName = mysqlService.listTableName();
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/origin/table/column/primary")
    @ResponseBody
    public Map<String, Object> getPrimaryColumnName(@RequestParam("tableName") String tableName) {

        System.out.println(tableName);

        Map<String, Object> map = new HashMap<>();

        List<String> strings = mysqlService.listPrimaryColumnName(tableName);

        map.put("columnName", strings);
        map.put("status", 200);

        return map;
    }

    @PostMapping("/data/origin/alter")
    @ResponseBody
    public Map<String, Object> alterTableForeign(@RequestParam("table") String table, @RequestParam("field") String field, @RequestParam("foreignTable") String foreignTable, @RequestParam("foreignField") String foreignField) {
        Map<String, Object> map = new HashMap<>();

        mysqlService.alterTableForeign(table, field, foreignTable, foreignField);

        map.put("status", 200);
        return map;
    }

    @PostMapping("/data/output")
    @ResponseBody
    public Map<String, Object> dataOutput(@RequestParam("tableName") String tableName) {
        Map<String, Object> map = new HashMap<>();

        List<String> columnName = mysqlService.listColumnName(tableName);
        List<Map> allData = mysqlService.getAllData(tableName);

        mysqlService.dataOutput(tableName, columnName, allData);


        map.put("status", 200);
        return map;
    }
}