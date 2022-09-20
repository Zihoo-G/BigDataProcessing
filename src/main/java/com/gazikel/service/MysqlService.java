package com.gazikel.service;

import java.util.List;
import java.util.Map;

public interface MysqlService {

    public List<Map> listTable();

    public List<String> listTableName();

    public List<String> listColumnName(String tableName);

    /**
     * 创建一张表
     * @param sql
     * @return
     */
    public void createTable(String sql);

    /**
     * 插入数据
     *
     * @param sql
     */
    public void insert(List<String> sql);

    /**
     * 删除一张表
     *
     * @param tableName
     */
    public void dropTable(String tableName);


    /**
     * 删除表的字段
     * @param tableName 表名
     * @param fieldName 字段名
     */
    public void dropFiled(String tableName, String fieldName);

    /**
     * 列出表中所有字段信息
     * @param tableName
     */
    public List<Map> listColumns(String tableName);

    /**
     * 获取主键列名
     *
     * @param tableName
     * @return
     */
    public List<String> listPrimaryColumnName(String tableName);

    /**
     * 对表增加一个外键
     *
     * @param table
     * @param field
     * @param foreignTable
     * @param foreignField
     */
    public void alterTableForeign(String table, String field, String foreignTable, String foreignField);


    void alertTableName(String name, String newName);

    void alertFieldName(String tableName, String oldFieldName, String newFieldName, String type);

    String getDataTypeByColumnName(String tableName, String fieldName);

    /**
     * 修改表的字段类型
     * @param tableName
     * @param columnName
     * @param columnType
     */
    void alterTableColumnType(String tableName, String columnName, String columnType, int columnTypeLong);

    /**
     * 查询空值的行
     * @param tableName
     * @param columName
     * @return
     */
    List<Map> getNullRow(String tableName, String columName);

    /**
     * 查询不是空值的行数
     * @param tableName
     * @param columnName
     * @return
     */
    Integer getRowNumber(String tableName, String columnName);

    /**
     * 获取总共数据条数
     * @param tableName
     * @return
     */
    Integer getRowSumNumber(String tableName);

    /**
     * 更新空数据
     * @param tableName
     * @param columnName
     * @param value
     * @param id
     */
    void updateNullRow(String tableName, String columnName, String value, String id);

    /**
     * 删除行数据
     * @param tableName
     * @param field
     * @param condition
     */
    int deleteRow(String tableName, String field, String condition);

    /**
     * 获取符合条件的行信息
     * @param tableName
     * @param field
     * @param condition
     * @return
     */
    int getRowNumberByCondition(String tableName, String field, String condition);

    /**
     * 将json字符串转换
     * @param tableName
     * @param field
     * @param input
     * @param id
     */
    void updateJsonRow(String tableName, String field, String input, String id);

    /**
     * 获取所有数据
     * @param tableName
     * @return
     */
    List<Map> getAllData(String tableName);

    void dataOutput(String tableName, List<String> columnNames, List<Map> allDatas);

    /**
     * 重复值数量
     * @param tableName
     * @param field
     * @return
     */
    int getRepetitionList(String tableName, String field);

    /**
     * 删除重复值
     * @param tableName
     * @param field
     */
    void deleteRepetition(String tableName, String field);


    void joinHandle(String tableName, String field1, String field2);

    void addressHandle(String tableName, String field);
}
