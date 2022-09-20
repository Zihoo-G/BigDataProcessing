package com.gazikel.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MysqlMapper {

    /**
     * 获取一个库的所有字段
     * @return
     */
    @Select("select * from information_schema.TABLES where TABLE_SCHEMA=(select database())")
    public List<Map> listTable();


    /**
     * 获取一个表中的所有字段
     * @param tableName 表名
     * @return
     */
    @Select("select COLUMN_NAME from information_schema.COLUMNS where table_name = #{tableName}  ORDER BY ORDINAL_POSITION")
    public List<String> listColumnName(String tableName);

    @Select("select DATA_TYPE from information_schema.COLUMNS where table_name = #{tableName} AND COLUMN_NAME = #{fieldName}")
    public String getDataTypeByColumnName(String tableName, String fieldName);

    @Select("select COLUMN_NAME from information_schema.COLUMNS where table_name = #{tableName} AND COLUMN_KEY = 'PRI' ORDER BY ORDINAL_POSITION ")
    public List<String> listPrimaryColumnName(String tableName);

    /**
     * 删除表的一个字段
     * Delete a field of the table
     * @param tableName 表名
     * @param fieldName 字段名
     */
    @Update("ALTER TABLE ${tableName} DROP ${fieldName}")
    void dropField(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    /**
     * 获取一个表中的所有字段信息
     * Get all the field information in a table
     * @param tableName 表名
     * @return
     */
    @Select("select * from information_schema.COLUMNS where table_name = #{tableName}  ORDER BY ORDINAL_POSITION")
    public List<Map> listColumns(String tableName);

    /**
     * 查询所有数据值
     * @param tableName
     * @return
     */
    @Select("select * from ${tableName}")
    List<Map> getAllData(@Param("tableName") String tableName);

    /**
     * 创建一个表
     * Create a table
     * @param sql
     * @return
     */
    @Select("${sql}")
    public void createTable(String sql);

    /**
     * 插入数据
     * Insert data
     * @param sql
     */
    @Insert("${sql}")
    public void insert(String sql);

    @Update("DROP TABLE ${tableName}")
    public void dropTable(String tableName);

    /**
     * 对表增加外键
     * Add external key to the table
     * @param table
     * @param field
     * @param foreignTable
     * @param foreignField
     */
    @Update("ALTER TABLE ${table} add constraint FK_ID foreign key(${field}) REFERENCES ${foreignTable}(${foreignField})")
    public void alertTableForeign(@Param("table") String table, @Param("field") String field, @Param("foreignTable") String foreignTable, @Param("foreignField") String foreignField);

    /**
     * 更改表名
     * Change the table name
     * @param name
     * @param newName
     */
    @Update("ALTER TABLE ${name} RENAME TO ${newName}")
    void alertTableName(@Param("name") String name, @Param("newName") String newName);

    /**
     * 更改表的字段名
     * Change the field name of the table
     * @param tableName
     * @param oldFieldName
     * @param newFieldName
     * @param type
     */
    @Update("ALTER TABLE ${tableName} CHANGE ${oldFieldName} ${newFieldName} ${type};")
    void alertFieldName(@Param("tableName") String tableName
            , @Param("oldFieldName") String oldFieldName
            , @Param("newFieldName") String newFieldName
            , @Param("type") String type);

    @Update("ALTER TABLE ${tableName} MODIFY COLUMN ${columnName} ${columnType}")
    void alterTableColumnType(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("columnType") String columnType);

    @Select("SELECT * FROM ${tableName} WHERE ${columnName} IS NULL or ${columnName} = ''")
    List<Map> getNullRow(@Param("tableName") String tableName, @Param("columnName") String columnName);

    @Select("SELECT COUNT(*) FROM ${tableName} WHERE ${columnName} IS NOT NULL AND ${columnName} != ''")
    Integer getRowNum(@Param("tableName") String tableName, @Param("columnName") String columnName);

    @Select("SELECT COUNT(*) FROM ${tableName}")
    Integer getRowSumNum(@Param("tableName") String tableName);

    @Update("UPDATE ${tableName} SET ${columnName} = ${value} where id = ${id}")
    void updateRowNull(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("value") String value, @Param("id") String id);

    @Delete("delete from ${tableName} where ${condition}")
    int deleteRow(String tableName, String condition);

    @Select("SELECT count(*) from ${tableName} where ${condition}")
    int getCountByCondition(@Param("tableName") String tableName, @Param("condition") String condition);

    @Select("select ${id} as `id`, ${field} as keywords from ${tableName}")
    List<Map> getRow(String tableName, String id, String field);

    @Update("UPDATE ${tableName} set ${field} = #{fieldValue} WHERE ${id} = #{idValue}")
    void updateJsonRow(@Param("tableName") String tableName, @Param("id") String id, @Param("idValue") String idValue, String field, @Param("fieldValue") String fieldValue);

    /**
     * 查询重复数据
     * @param tableName
     * @param field
     * @return
     */
    @Select("select ${field} from ${tableName} group by ${field} having count(${field})>1;")
    List<Map> getRepetitionList(@Param("tableName") String tableName, @Param("field") String field);

    /**
     * 删除重复数据
     * @param tableName
     * @param field
     */
    @Delete("delete from ${tableName} where ${field} in (select `id` from delete_list)")
    void deleteRepetition(String tableName, String field);

    @Insert("insert into delete_list(`id`) (select ${columnName} as `id` from ${tableName} group by ${columnName} having count(${columnName}) > 1)")
    void listRepetition(String tableName, String columnName);

    @Delete("truncate table delete_list")
    void truncateDeleteList();

    @Select("select ${field} from ${tableName}")
    List<String> getColumnList(String tableName, String field);

    @Update("update ${tableName} set ${field} = #{value} where sid = ${id}")
    void updateColumn(String tableName, String field, String value, int id);


    @Select("select * from address")
    List<Map> listAddress();
}
