package com.gazikel.controller;

import com.alibaba.fastjson.JSON;
import com.gazikel.mapper.MysqlMapper;
import com.gazikel.service.MysqlService;
import com.gazikel.vo.Missing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CleanController {

    @Autowired
    private MysqlService mysqlService;

    @GetMapping("/clean/type")
    public String toTypeHandlePage(Model model) {

        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);

        return "clean/typeHandle";
    }

    @GetMapping("/clean/missing")
    public String toMissingHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);

        return "clean/missingHandle";
    }

    @GetMapping("/clean/repetition")
    public String toRepetitionHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);

        return "clean/repetitionHandle";
    }

    @GetMapping("/clean/screen")
    public String toScreenHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);

        return "clean/screenHandle";
    }

    @GetMapping("/clean/json")
    public String toJsonHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);

        return "clean/jsonHandle";
    }

    @GetMapping("/clean/join")
    public String toJoinHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);
        return "clean/joinHandle";
    }

    @GetMapping("/clean/address")
    public String toAddressHandlePage(Model model) {
        List<String> tableName = mysqlService.listTableName();
        model.addAttribute("tableName", tableName);
        return "clean/addressHandle";
    }

    @PostMapping("/clean/type/column/list")
    @ResponseBody
    public Map<String, Object> getColumnList(String tableName) {
        Map<String, Object> map = new HashMap<>();
        List<Map> maps = mysqlService.listColumns(tableName);
        map.put("columns", maps);
        map.put("status", 200);
        return map;
    }

    @RequestMapping("/clean/type/handle")
    @ResponseBody
    public Map<String, Object> alterColumnType(@RequestParam("tableName") String tableName, @RequestParam("type") String type, @RequestParam("typeLong") String typeLong) {

        Map<String, Object> map = new HashMap<>();

        System.out.println(tableName);

        type = type.replaceAll("\"", "");
        type = type.replaceAll("\\[", "");
        type = type.replaceAll("]", "");
        System.out.println(type);
        String[] types = type.split(",");
        for (String s : types) {
            System.out.println(s);
        }

        typeLong = typeLong.replaceAll("\"", "");
        typeLong = typeLong.replaceAll("\\[", "");
        typeLong = typeLong.replaceAll("]", "");
        String[] typeLongs = typeLong.split(",");

        if (types.length != typeLongs.length) {
            map.put("status", 201);
            return map;
        }

        int length = types.length;

        List<String> columns = mysqlService.listColumnName(tableName);

        try {
            for (int i = 0; i < length; i++) {
                mysqlService.alterTableColumnType(tableName, columns.get(i), types[i], Integer.parseInt(typeLongs[i]));
            }
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
        }

        // map.put("status", 200);
        return map;
    }


    @PostMapping("/clean/missing/row")
    @ResponseBody
    public Map<String, Object> getNullRowList(@RequestParam("tableName") String tableName) {

        List<String> columnNames = mysqlService.listColumnName(tableName);

        Map<String, Object> map = new HashMap<>();

        List<Missing> list = new ArrayList<>();

        // 总共条数
        Integer rowSumNumber = mysqlService.getRowSumNumber(tableName);

        for (String columnName : columnNames) {
            Integer rowNumber = mysqlService.getRowNumber(tableName, columnName);
            Missing missing = new Missing(columnName, rowNumber, rowSumNumber - rowNumber);
            list.add(missing);
        }
        map.put("list", list);
        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/missing/row/list")
    @ResponseBody
    public Map<String, Object> getNullRow(@RequestParam("tableName") String tableName, @RequestParam("search") String search) {
        Map<String, Object> map = new HashMap<>();


        List<Map> nullRows = mysqlService.getNullRow(tableName, search);
        List<String> columnNames = mysqlService.listColumnName(tableName);

        map.put("list", nullRows);
        map.put("columnNames", columnNames);

        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/missing/row/handle")
    @ResponseBody
    public Map<String, Object> updateNullRow(@RequestParam("id") String id, @RequestParam("value") String value, @RequestParam("field") String field, @RequestParam("tableName") String tableName) {
        Map<String, Object> map = new HashMap<>();

        id = id.replaceAll("\"", "");
        id = id.replaceAll("\\[", "");
        id = id.replaceAll("]", "");
        String[] ids = id.split(",");

        value = value.replaceAll("\"", "");
        value = value.replaceAll("\\[", "");
        value = value.replaceAll("]", "");
        String[] values = value.split(",");

        System.out.println(ids);
        System.out.println(values);


        if (ids.length != values.length) {
            map.put("status", 201);
            return map;
        }

        int number = ids.length;
        for (int i = 0; i < number; i++) {
            mysqlService.updateNullRow(tableName, field, values[i], ids[i]);
        }
        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/screen/column")
    @ResponseBody
    public Map<String, Object> getColumnName(@RequestParam("tableName") String tableName) {
        Map<String, Object> map  = new HashMap<>();

        List<String> columnNames = mysqlService.listColumnName(tableName);

        map.put("columnNames", columnNames);

        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/screen/delete")
    @ResponseBody
    public Map<String, Object> deleteRow(@RequestParam("tableName") String tableName, @RequestParam("field") String field, @RequestParam("condition") String condition) {
        field = field.replaceAll("\"", "");
        field = field.replaceAll("\\[", "");
        field = field.replaceAll("]", "");
        String[] fields = field.split(",");

        condition = condition.replaceAll("\"", "");
        condition = condition.replaceAll("\\[", "");
        condition = condition.replaceAll("]", "");

        String[] conditions = condition.split(",");

        Map<String, Object> map = new HashMap<>();

        try {
            int number = 0;
            for (int i = 0; i < fields.length; i++) {
                int deleteRows = mysqlService.deleteRow(tableName, fields[i], conditions[i]);
                number += deleteRows;
                System.out.println("==>" + number);
            }
            map.put("number", number);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
        }

        return map;
    }
    
    @PostMapping("/clean/json/handle")
    @ResponseBody
    public Map<String, Object> updateJson(@RequestParam("id") String id
            , @RequestParam("tableName") String tableName
            , @RequestParam("field") String field
            , @RequestParam("input") String input) {
        field = field.replaceAll("\"", "");
        field = field.replaceAll("\\[", "");
        field = field.replaceAll("]", "");
        String[] fields = field.split(",");

        input = input.replaceAll("\"", "");
        input = input.replaceAll("\\[", "");
        input = input.replaceAll("]", "");
        String[] inputs = input.split(",");
        
        Map<String, Object> map = new HashMap<>();
        
        try {

            for (int i = 0; i < fields.length; i++) {
                try {
                    mysqlService.updateJsonRow(tableName, fields[i], inputs[i], id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", 201);
        }

        return map;
    }

    @PostMapping("/clean/repetition/number")
    @ResponseBody
    public Map<String, Object> getRepetitionNumber(String tableName, String field) {
        Map<String, Object> map = new HashMap<>();
        int repetitionList = mysqlService.getRepetitionList(tableName, field);
        map.put("number", repetitionList);

        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/repetition/delete")
    @ResponseBody
    public Map<String, Object> deleteRepetition(String tableName, String field) {
        Map<String, Object> map = new HashMap<>();

        try {
            mysqlService.deleteRepetition(tableName, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 200);
        return map;
    }

    @PostMapping("/clean/join/handle")
    @ResponseBody
    public Map<String, Object> joinHandle(@RequestParam("tableName") String tableName, @RequestParam("field1") String field1, @RequestParam("field2") String field2) {
        Map<String, Object> map = new HashMap<>();


        mysqlService.joinHandle(tableName, field1, field2);
        map.put("status", 200);

        return map;
    }

    @PostMapping("/clean/address/handle")
    @ResponseBody
    public Map<String, Object> addressHandle(@RequestParam("tableName") String tableName, @RequestParam("field") String field) {
        Map<String, Object> map = new HashMap<>();

        System.out.println(tableName + field);
        mysqlService.addressHandle(tableName, field);


        map.put("status", 200);
        return map;
    }
}
