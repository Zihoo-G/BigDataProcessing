package com.gazikel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gazikel.mapper.MysqlMapper;
import com.gazikel.utils.POIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MysqlServiceImpl implements MysqlService {

    @Autowired
    private MysqlMapper mysqlMapper;


    @Override
    public List<Map> listTable() {
        List<Map> maps = mysqlMapper.listTable();
        return maps;
    }

    @Override
    public List<String> listTableName() {
        List<String> list = new ArrayList<>();
        List<Map> maps = mysqlMapper.listTable();

        for (Map map : maps) {
            list.add(String.valueOf(map.get("TABLE_NAME")));
        }

        return list;
    }

    @Override
    public List<String> listColumnName(String tableName) {
        return mysqlMapper.listColumnName(tableName);
    }

    @Override
    public void createTable(String sql) {
        mysqlMapper.createTable(sql);
    }

    @Override
    public void insert(List<String> sql) {
        for (String s : sql) {
            mysqlMapper.insert(s);
        }
    }

    @Override
    public void dropTable(String tableName) {
        mysqlMapper.dropTable(tableName);
    }

    @Override
    public List<Map> listColumns(String tableName) {
        return mysqlMapper.listColumns(tableName);
    }

    @Override
    public List<String> listPrimaryColumnName(String tableName) {
        return mysqlMapper.listPrimaryColumnName(tableName);
    }

    @Override
    public void alterTableForeign(String table, String field, String foreignTable, String foreignField) {
        mysqlMapper.alertTableForeign(table, field, foreignTable, foreignField);
    }

    @Override
    public void alertTableName(String name, String newName) {
        mysqlMapper.alertTableName(name, newName);
    }

    @Override
    public void alertFieldName(String tableName, String oldFieldName, String newFieldName, String type) {
        mysqlMapper.alertFieldName(tableName, oldFieldName, newFieldName, type);
    }

    @Override
    public String getDataTypeByColumnName(String tableName, String fieldName) {
        return mysqlMapper.getDataTypeByColumnName(tableName, fieldName);
    }

    @Override
    public void alterTableColumnType(String tableName, String columnName, String columnType, int columnTypeLong) {
        if ("varchar".equals(columnType) || "char".equals(columnType)) {
            columnType = columnType + "(" + columnTypeLong + ")";
        }

        mysqlMapper.alterTableColumnType(tableName, columnName, columnType);
    }

    @Override
    public List<Map> getNullRow(String tableName, String columName) {
        return mysqlMapper.getNullRow(tableName, columName);
    }

    @Override
    public Integer getRowNumber(String tableName, String columnName) {
        return mysqlMapper.getRowNum(tableName, columnName);
    }

    @Override
    public void dropFiled(String tableName, String fieldName) {
        mysqlMapper.dropField(tableName, fieldName);
    }

    public Integer getRowSumNumber(String tableName) {
        return mysqlMapper.getRowSumNum(tableName);
    }

    @Override
    public void updateNullRow(String tableName, String columnName, String value, String id) {
        if(!value.startsWith("'")) {
            columnName = "'"+value;
        }
        if (!value.endsWith("'")) {
            value = value+"'";
        }
        mysqlMapper.updateRowNull(tableName, columnName, value, id);
    }

    @Override
    public int deleteRow(String tableName, String field, String condition) {
        condition = field + condition;
        return mysqlMapper.deleteRow(tableName, condition);
    }

    @Override
    public int getRowNumberByCondition(String tableName, String field, String condition) {
        return mysqlMapper.getCountByCondition(tableName, field + condition);
    }

    @Override
    public void updateJsonRow(String tableName, String field, String input, String id) {
        List<Map> rows = mysqlMapper.getRow(tableName, id, field);

        for (Map row : rows) {
            String keywordsStr = row.get("keywords").toString();

            String result = "";

            try {
                JSONArray jsonArray = JSONArray.parseArray(keywordsStr);
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (i != 0) {
                        result += ",";
                    }
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Object o = jsonObject.get(input);
                    result += o.toString();
                }
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mysqlMapper.updateJsonRow(tableName, id, row.get("id").toString(), field, result);
        }

    }

    @Override
    public List<Map> getAllData(String tableName) {
        return mysqlMapper.getAllData(tableName);
    }

    @Override
    public void dataOutput(String tableName, List<String> columnNames, List<Map> allDatas) {
        System.out.println("====正在进行数据导出====");
        POIUtil.dataOutput(tableName, columnNames, allDatas);
    }

    @Override
    public int getRepetitionList(String tableName, String field) {
        return mysqlMapper.getRepetitionList(tableName, field).size();
    }

    @Override
    public void deleteRepetition(String tableName, String field) {
        mysqlMapper.listRepetition(tableName, field);
        mysqlMapper.deleteRepetition(tableName, field);
        mysqlMapper.truncateDeleteList();
    }

    @Override
    public void joinHandle(String tableName, String field1, String field2) {
        List<String> columnList = mysqlMapper.getColumnList(tableName, field2);

        List<String> result = new ArrayList<>();

        List<String> columnList1 = mysqlMapper.getColumnList(tableName, field1);

        for(int i= 0; i<columnList1.size(); i++){
            result.add("" + columnList1.get(i) + columnList.get(i));
        }

        for (int i = 0; i < result.size(); i++) {
            mysqlMapper.updateColumn(tableName, field1, result.get(i), i);
        }
    }

    @Override
    public void addressHandle(String tableName, String field) {
        List<Map> maps = mysqlMapper.listAddress();

        for (Map map : maps) {
            System.out.println(map);
        }
        List<String> columnList = mysqlMapper.getColumnList(tableName, "sbelongwhere");
        List<String> result = new ArrayList<>();

        for(int i = 0; i < columnList.toArray().length; i++) {
            String substring = columnList.get(i).substring(0, 6);
            System.out.println(substring);
            for (Map map : maps) {

                if (substring.equals(map.get("id").toString())) {
                    result.add(map.get("name") + "科技局");
                    break;
                }
            }
        }

        for(int i = 0; i <10;i++) {
            mysqlMapper.updateColumn(tableName, field, result.get(i), i);
        }

    }
}
